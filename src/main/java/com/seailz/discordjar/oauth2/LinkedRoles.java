package com.seailz.discordjar.oauth2;

import com.seailz.databaseapi.Database;
import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import org.json.JSONObject;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Class for managing linked roles.
 * <p>
 * While discord.jar does not normally support OAuth2,
 * <br>in this case we determined that it would be a good idea to support it,
 * <br>as this is a feature for bots rather than OAuth2 applications.
 * <p>
 * For <b>Linked Roles</b>, discord.jar will use Spring's web API.
 *
 * @author  Seailz
 * @see     package-info.java
 * @since   1.0
 */
@SpringBootApplication
public class LinkedRoles {

    private String clientId;
    private String clientSecret;
    private String redirectEndpoint;
    private DiscordJar discordJar;
    private Database database;

    private static boolean isInstance = false;
    private String appId;

    /**
     * Creates a new LinkedRoles instance.
     *
     * @param clientId              The client ID of the bot.
     * @param clientSecret          The client secret of the bot.
     * @param redirectEndpoint      The redirect endpoint that you set in the developer portal. This endpoint is important as it tells discord.jar where to listen.
     *                              For example, if my bot's IP is 1111, in the developer portal you'd set the redirect URL to something like <b>http://1111:8080/redirect</b>.
     *                              In this case, the redirect endpoint would be <b>/request</b>. This endpoint can be anything you want,
     *                              but it must be the same as the one you set in the developer portal.
     * @param applicationId         The ID of the application.
     * @param redirectUrl           The redirect URL that you set in the developer portal.
     * @param redirectBackToDiscord Whether to redirect back to <a href="https://discord.com/oauth2/authorized">Discord's Authorization Finished page</a> after the user has been redirected to the redirect endpoint. This is recommended, however if you have an external login page you may not want to redirect back to Discord.
     * @param discordJar             The discord.jar instance.
     * @param db                    The database you'd like to use.
     */
    public LinkedRoles(String clientId, String clientSecret, String redirectUrl, String redirectEndpoint, String applicationId, boolean redirectBackToDiscord, DiscordJar discordJar, Database db) {
        if (isInstance)
            throw new IllegalStateException("LinkedRoles is already instantiated. Please use your existing instance of the class.");
        LinkedRolesRestController.set(clientId, clientSecret, redirectUrl, redirectEndpoint, redirectBackToDiscord, discordJar, db);

        SpringApplication app = new SpringApplication(LinkedRoles.class);
        Properties props = new Properties();
        // Stops spring from logging start up messages
        props.setProperty("spring.main.log-startup-info", "false");
        app.setDefaultProperties(props);
        app.run();
        SLF4JBridgeHandler.uninstall();

        Logger.getLogger("discord.jar").info("Linked Roles is now running.");
        isInstance = true;
        this.discordJar = discordJar;
        this.appId = applicationId;
        this.database = db;
    }

    protected LinkedRoles() {}

    /**
     * Updates user application role connections
     *
     * @param userId           The user ID
     * @param platformName     The platform name
     * @param platformUsername The platform username
     * @param values           object mapping application role connection metadata keys to their string-ified value (max 100 characters) for the user on the platform a bot has connected
     * @param authToken        The authorization token
     */
    private void updateRoles(String userId, String platformName, String platformUsername, HashMap<String, Object> values, String authToken, boolean attemptedRefreshToken) throws IOException, InterruptedException, URISyntaxException, DiscordRequest.UnhandledDiscordAPIErrorException {
        HashMap<String, String> headers = new HashMap<>();
        if (authToken == null) {
            String accToken;
            try {
                accToken = (String) database.get("discordjar_linked_roles", "user_id", userId, "acc_token");
            } catch (SQLException | ClassCastException e) {
                throw new IllegalStateException("Could not find access token for user.");
            }
            headers.put("Authorization", "Bearer " + accToken);
        }
        if (authToken != null) headers.put("Authorization", "Bearer " + authToken);

        JSONObject metadata = new JSONObject();
        for (String key : values.keySet()) {
            metadata.put(key, values.get(key));
        }

        DiscordRequest req = new DiscordRequest(
                new JSONObject()
                        .put("platform_name", platformName)
                        .put("platform_username", platformUsername)
                        .put("metadata", metadata),
                headers,
                URLS.OAUTH2.PUT.USERS.APPLICATIONS.ROLE_CONNECTIONS.UPDATE_USER_APPLICATION_ROLE_CONNECTION
                        .replace("{application.id}", appId),
                discordJar,
                URLS.OAUTH2.PUT.USERS.APPLICATIONS.ROLE_CONNECTIONS.UPDATE_USER_APPLICATION_ROLE_CONNECTION,
                RequestMethod.PUT
        );
        DiscordResponse res = req.invokeNoAuth(new JSONObject());
        if (res.code() == 401) {
            if (attemptedRefreshToken) {
                throw new IllegalStateException("Could not updated roles for user - it's likely the user has de-authed the app.");
            }
            System.out.println("Attempting refresh token...");
            String refresh;
            try {
                refresh = (String) database.get("discordjar_linked_roles", "user_id", userId, "refresh_token");
            } catch (SQLException | ClassCastException e) {
                throw new IllegalStateException("Could not find refresh token for user.");
            }

            HashMap<String, String> params = new HashMap<>();
            params.put("client_id", clientId);
            params.put("client_secret", clientSecret);
            params.put("grant_type", "refresh_token");
            params.put("refresh_token", refresh);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest refreshReq = HttpRequest.newBuilder()
                    .uri(new URI("https://discord.com/api/v10/oauth2/token"))
                    .POST(getParamsUrlEncoded(params))
                    .headers("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            HttpResponse<String> refreshRes = client.send(refreshReq, HttpResponse.BodyHandlers.ofString());
            if (refreshRes.statusCode() == 401) {
                throw new IllegalStateException("Could not updated roles for user - it's likely the user has de-authed the app.");
            }
            JSONObject refreshResponseJson = new JSONObject(refreshRes);
            updateRoles(null, platformName, platformUsername, values, refreshResponseJson.getString("access_token"), true);
        }
    }

    public void updateRoles(String userId, String platformName, String platformUsername, HashMap<String, Object> values) throws IllegalStateException, IOException, URISyntaxException, InterruptedException, DiscordRequest.UnhandledDiscordAPIErrorException {
        updateRoles(userId, platformName, platformUsername, values, null, false);
    }

    public void updateRoles(String platformName, String platformUsername, HashMap<String, Object> values, String authToken) throws IllegalStateException, IOException, URISyntaxException, InterruptedException, DiscordRequest.UnhandledDiscordAPIErrorException {
        updateRoles(null, platformName, platformUsername, values, authToken, false);
    }

    private HttpRequest.BodyPublisher getParamsUrlEncoded(Map<String, String> parameters) {
        String urlEncoded = parameters.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        return HttpRequest.BodyPublishers.ofString(urlEncoded);
    }

}
