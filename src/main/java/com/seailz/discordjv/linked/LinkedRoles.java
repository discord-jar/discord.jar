package com.seailz.discordjv.linked;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.utils.URLS;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import org.json.JSONObject;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Class for managing linked roles.
 * <p>
 * While discord.jv does not normally support OAuth2,
 * <br>in this case we determined that it would be a good idea to support it,
 * <br>as this is a feature for bots rather than OAuth2 applications.
 * <p>
 * For <b>Linked Roles</b>, discord.jv will use Spring's web API.
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
    private DiscordJv discordJv;

    private static boolean isInstance = false;
    private String appId;

    /**
     * Creates a new LinkedRoles instance.
     *
     * @param clientId              The client ID of the bot.
     * @param clientSecret          The client secret of the bot.
     * @param redirectEndpoint      The redirect endpoint that you set in the developer portal. This endpoint is important as it tells discord.jv where to listen.
     *                              For example, if my bot's IP is 1111, in the developer portal you'd set the redirect URL to something like <b>http://1111:8080/redirect</b>.
     *                              In this case, the redirect endpoint would be <b>/request</b>. This endpoint can be anything you want,
     *                              but it must be the same as the one you set in the developer portal.
     * @param applicationId         The ID of the application.
     * @param redirectUrl           The redirect URL that you set in the developer portal.
     * @param redirectBackToDiscord Whether to redirect back to <a href="https://discord.com/oauth2/authorized">Discord's Authorization Finished page</a> after the user has been redirected to the redirect endpoint. This is recommended, however if you have an external login page you may not want to redirect back to Discord.
     * @param discordJv             The discord.jv instance.
     */
    public LinkedRoles(String clientId, String clientSecret, String redirectUrl, String redirectEndpoint, String applicationId, boolean redirectBackToDiscord, DiscordJv discordJv) {
        if (isInstance)
            throw new IllegalStateException("LinkedRoles is already instantiated.");
        LinkedRolesRestController.set(clientId, clientSecret, redirectUrl, redirectEndpoint, redirectBackToDiscord, discordJv);

        SpringApplication app = new SpringApplication(LinkedRoles.class);
        Properties props = new Properties();
        // Stops spring from logging start up messages
        props.setProperty("spring.main.log-startup-info", "false");
        app.setDefaultProperties(props);
        app.run();
        SLF4JBridgeHandler.uninstall();

        Logger.getLogger("discord.jv").info("Linked Roles is now running.");
        isInstance = true;
        this.discordJv = discordJv;
        this.appId = applicationId;
    }

    protected LinkedRoles() {}

    /**
     * Updates user application role connections
     * @param userId The user ID
     * @param platformName The platform name
     * @param platformUsername The platform username
     * @param values object mapping application role connection metadata keys to their string-ified value (max 100 characters) for the user on the platform a bot has connected
     * @param authToken The authorization token
     */
    private void updateRoles(String userId, String platformName, String platformUsername, HashMap<String, Object> values, String authToken) {
        HashMap<String, String> headers = new HashMap<>();
        if (authToken == null) headers.put("Authorization", "Bearer " + "" /* get auth token from db */);
        if (authToken != null) headers.put("Authorization", "Bearer " + authToken);

        JSONObject metadata = new JSONObject();
        for (String key : values.keySet()) {
            metadata.put(key, values.get(key));
        }

        System.out.println(headers.get("Authorization"));
        DiscordRequest req = new DiscordRequest(
                new JSONObject()
                        .put("platform_name", platformName)
                        .put("platform_username", platformUsername)
                        .put("metadata", metadata),
                headers,
                URLS.OAUTH2.PUT.USERS.APPLICATIONS.ROLE_CONNECTIONS.UPDATE_USER_APPLICATION_ROLE_CONNECTION
                        .replace("{application.id}", appId),
                discordJv,
                URLS.OAUTH2.PUT.USERS.APPLICATIONS.ROLE_CONNECTIONS.UPDATE_USER_APPLICATION_ROLE_CONNECTION,
                RequestMethod.PUT
        );
        req.invokeNoAuth(new JSONObject());
    }

    public void updateRoles(String userId, String platformName, String platformUsername, HashMap<String, Object> values) {
        updateRoles(userId, platformName, platformUsername, values, null);
    }

    public void updateRoles(String platformName, String platformUsername, HashMap<String, Object> values, String authToken) {
        updateRoles(null, platformName, platformUsername, values, authToken);
    }

}
