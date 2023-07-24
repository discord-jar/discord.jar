package com.seailz.discordjar.oauth2;

import com.seailz.databaseapi.Column;
import com.seailz.databaseapi.ColumnType;
import com.seailz.databaseapi.Database;
import com.seailz.databaseapi.annotation.builder.TableBuilder;
import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.oauth2.response.Response;
import com.seailz.discordjar.oauth2.response.error.CodeNotPresentResponse;
import com.seailz.discordjar.oauth2.response.error.InvalidEndpointResponse;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.URLS;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This is an internal class used by discord.jar & spring to handle OAuth2 requests.
 *
 * @author Seailz
 * @since  1.0
 * @see    LinkedRoles
 */
@RestController
public class LinkedRolesRestController {

    private static String clientId;
    private static String clientSecret;
    private static String redirectEndpoint;
    private static String redirectUrl;
    private static DiscordJar discordJar;
    private static boolean redirectBackToDiscord;
    private static Database database;
    /**
     * Logic to run when the user is redirected to the redirect endpoint & discord.jar has completed its processing.
     * <br>discord.jar will automatically refresh tokens for you if they are expired, so no need to worry about that.
     * <p>{@link TriConsumer} parameters:
     * <ol>
     *     <li><b>HttpServletRequest</b> - Allows you to respond to the request.</li>
     *     <li><b>String</b> - ID of the user who completed authentication</li>
     *     <li><b>String</b> - Authorization code that was retrieved..</li>
     * </ol>
     */
    private static TriConsumer<HttpServletResponse, String, String> onCodeReceived;

    @RequestMapping(value = "/*", method = {org.springframework.web.bind.annotation.RequestMethod.GET, org.springframework.web.bind.annotation.RequestMethod.POST})
    protected Response request(@RequestParam Map<String, String> reqParam, @NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws IOException, InterruptedException, URISyntaxException, SQLException {
        String endpoint = request.getRequestURI();
        if (endpoint.equals(redirectEndpoint) && !endpoint.equals("")) {
            String code = reqParam.get("code");
            if (code == null) {
                Logger.getLogger("discord.jar").warning("Code was null, were you redirected by Discord?");
                return new CodeNotPresentResponse();
            }

            HashMap<String, String> params = new HashMap<>();
            params.put("client_id", clientId);
            params.put("client_secret", clientSecret);
            params.put("grant_type", "authorization_code");
            params.put("code", code);
            params.put("redirect_uri", LinkedRolesRestController.redirectUrl);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI("https://discord.com/api/oauth2/token"))
                    .POST(getParamsUrlEncoded(params))
                    .headers("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            // store refresh with user id in database
            String userId = getUser(
                    new JSONObject(res.body()).getString("access_token")
            ).id();
            String refreshToken = new JSONObject(res.body()).getString("refresh_token");

            HashMap<String, String> data = new HashMap<>();
            data.put("refresh_token", refreshToken);
            data.put("user_id", userId);
            data.put("acc_token", new JSONObject(res.body()).getString("access_token"));

            if (database.getConnection() == null)
                database.connect();

            if (database.tableExists("discordjar_linked_roles")) {
                database.insert("discordjar_linked_roles", data);
            } else {
                ArrayList<Column> columns = new ArrayList<>();
                columns.add(new Column(ColumnType.VARCHAR, "refresh_token"));
                columns.add(new Column(ColumnType.VARCHAR, "user_id"));
                columns.add(new Column(ColumnType.VARCHAR, "acc_token"));
                database.createTable(new TableBuilder("discordjar_linked_roles", columns));
                database.insert("discordjar_linked_roles", data);
            }


            String accessToken = new JSONObject(res.body()).getString("access_token");
            if (onCodeReceived != null) onCodeReceived.accept(response, userId, accessToken);


            if (redirectBackToDiscord) {
                response.setHeader("Location", "https://discord.com/oauth2/authorized");
                response.setStatus(302);
            }

        }

        return new InvalidEndpointResponse();
    }

    protected static void set(String clientId, String clientSecret, String redirectUrl, String redirectEndpoint, boolean redirectBackToDiscord, DiscordJar discordJar, Database database) {
        LinkedRolesRestController.clientId = clientId;
        LinkedRolesRestController.clientSecret = clientSecret;
        LinkedRolesRestController.redirectEndpoint = redirectEndpoint;
        LinkedRolesRestController.discordJar = discordJar;
        LinkedRolesRestController.redirectUrl = redirectUrl;
        LinkedRolesRestController.redirectBackToDiscord = redirectBackToDiscord;
        LinkedRolesRestController.database = database;
    }

    private HttpRequest.BodyPublisher getParamsUrlEncoded(Map<String, String> parameters) {
        String urlEncoded = parameters.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        return HttpRequest.BodyPublishers.ofString(urlEncoded);
    }

    private User getUser(String accessToken) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(new URI(URLS.BASE_URL + "/users/@me"))
                .GET()
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .header("User-Agent", "discord.jar (https://github.com/discord.jar/, 1.0.0)")
                .build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return User.decompile(
                new JSONObject(res.body()), discordJar
        );
    }
}
