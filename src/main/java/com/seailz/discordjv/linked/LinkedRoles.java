package com.seailz.discordjv.linked;

import com.seailz.discordjv.DiscordJv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

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

    private static boolean isInstance = false;

    /**
     * Creates a new LinkedRoles instance.
     * @param clientId The client ID of the bot.
     * @param clientSecret The client secret of the bot.
     *
     * @param redirectEndpoint
     *          The redirect endpoint that you set in the developer portal. This endpoint is important as it tells discord.jv where to listen.
     *          For example, if my bot's IP is 1111, in the developer portal you'd set the redirect URL to something like <b>http://1111:8080/redirect</b>.
     *          In this case, the redirect endpoint would be <b>/request</b>. This endpoint can be anything you want,
     *          but it must be the same as the one you set in the developer portal.
     * @param discordJv The discord.jv instance.
     */
    public LinkedRoles(String clientId, String clientSecret, String redirectUrl, String redirectEndpoint, DiscordJv discordJv) {
        if (isInstance)
            throw new IllegalStateException("LinkedRoles is already instantiated.");
        LinkedRolesRestController.set(clientId, clientSecret, redirectUrl, redirectEndpoint, discordJv);

        SpringApplication app = new SpringApplication(LinkedRoles.class);
        Properties props = new Properties();
        // Stops spring from logging start up messages
        props.setProperty("spring.main.log-startup-info", "false");
        // Disables the whitelabel error page
        //props.setProperty("server.error.whitelabel.enabled", "false");
        // stop spring from stealing log messages
        //props.setProperty("logging.level.root", "OFF");
        app.setDefaultProperties(props);
        app.run();
        Logger.getLogger("discord.jv").info("Linked Roles is now running.");
        isInstance = true;
    }

    protected LinkedRoles() {}

}
