package com.seailz.discordjv.linked;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.linked.response.Response;
import com.seailz.discordjv.linked.response.error.CodeNotPresentResponse;
import com.seailz.discordjv.linked.response.error.InvalidEndpointResponse;
import com.seailz.discordjv.model.user.User;
import com.seailz.discordjv.utils.URLS;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This is an internal class used by discord.jv & spring to handle OAuth2 requests.
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
    private static DiscordJv discordJv;
    private static boolean redirectBackToDiscord;
    /**
     * Logic to run when the user is redirected to the redirect endpoint & discord.jv has completed its processing.
     * <br>discord.jv will automatically refresh tokens for you if they are expired, so no need to worry about that.
     * <p>{@link TriConsumer} parameters:
     * <ol>
     *     <li><b>HttpServletRequest</b> - Allows you to respond to the request.</li>
     *     <li><b>String</b> - ID of the user who completed authentication</li>
     *     <li><b>String</b> - Authorization code that was retrieved..</li>
     * </ol>
     */
    private static TriConsumer<HttpServletResponse, String, String> onCodeReceived;

    @RequestMapping(value = "/*", method = {org.springframework.web.bind.annotation.RequestMethod.GET, org.springframework.web.bind.annotation.RequestMethod.POST})
    protected Response request(@RequestParam Map<String, String> reqParam, @NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws IOException, InterruptedException, URISyntaxException {
        String endpoint = request.getRequestURI();
        if (endpoint.equals(redirectEndpoint) && !endpoint.equals("")) {
            String code = reqParam.get("code");
            if (code == null) {
                Logger.getLogger("discord.jv").warning("Code was null, were you redirected by Discord?");
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
            System.out.println(res.body());

            // store refresh with user id in database
            String userId = getUser(
                    new JSONObject(res.body()).getString("access_token")
            ).id();
            String refreshToken = new JSONObject(res.body()).getString("refresh_token");

            String accessToken = new JSONObject(res.body()).getString("access_token");
            onCodeReceived.accept(response, userId, accessToken);


            if (redirectBackToDiscord) {
                response.setHeader("Location", "https://discord.com/oauth2/authorized");
                response.setStatus(302);
            }

        }

        return new InvalidEndpointResponse();
    }

    protected static void set(String clientId, String clientSecret, String redirectUrl, String redirectEndpoint, boolean redirectBackToDiscord, DiscordJv discordJv) {
        LinkedRolesRestController.clientId = clientId;
        LinkedRolesRestController.clientSecret = clientSecret;
        LinkedRolesRestController.redirectEndpoint = redirectEndpoint;
        LinkedRolesRestController.discordJv = discordJv;
        LinkedRolesRestController.redirectUrl = redirectUrl;
        LinkedRolesRestController.redirectBackToDiscord = redirectBackToDiscord;
    }

    private HttpRequest.BodyPublisher getParamsUrlEncoded(Map<String, String> parameters) {
        String urlEncoded = parameters.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        return HttpRequest.BodyPublishers.ofString(urlEncoded);
    }

    private User getUser(String accessToken) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        return User.decompile(
                new DiscordRequest(
                        new JSONObject(),
                        headers,
                        URLS.OAUTH2.GET.GET_CURRENT_AUTH_INFO,
                        discordJv,
                        URLS.OAUTH2.GET.GET_CURRENT_AUTH_INFO,
                        RequestMethod.GET
                ).invoke().body().getJSONObject("user"), discordJv
        );
    }
}
