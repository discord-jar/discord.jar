package com.seailz.javadiscordwrapper;

import com.seailz.javadiscordwrapper.gateway.GatewayFactory;
import com.seailz.javadiscordwrapper.model.Application;
import com.seailz.javadiscordwrapper.model.guild.Guild;
import com.seailz.javadiscordwrapper.model.Intent;
import com.seailz.javadiscordwrapper.model.User;
import com.seailz.javadiscordwrapper.utils.discordapi.Requester;
import com.seailz.javadiscordwrapper.utils.URLS;
import com.seailz.javadiscordwrapper.utils.cache.Cache;
import com.seailz.javadiscordwrapper.utils.presence.StatusType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;

import java.io.*;
import java.util.EnumSet;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * The main class of the Discord.jv wrapper
 * @author Seailz
 * @since 1.0
 */
public class DiscordJv {

    /**
     * The token of the bot
     */
    private String token;
    /**
     * Used to manage the gateway connection
     */
    private GatewayFactory gatewayFactory;
    /**
     * Stores the logger
     */
    private Logger logger;
    /**
     * Intents the bot will use when connecting to the gateway
     */
    private EnumSet<Intent> intents;
    /**
     * Used for caching guilds in memory
     */
    private Cache<Guild> guildCache;
    /**
     * Used for caching users in memory
     */
    private Cache<User> userCache;

    public DiscordJv(String token, EnumSet<Intent> intents) throws ExecutionException, InterruptedException {
        this.token = token;
        this.intents = intents;
        logger = Logger.getLogger("DiscordJv");
        this.gatewayFactory = new GatewayFactory(this);
        this.guildCache = new Cache<>();
        this.userCache = new Cache<>();

        initiateNoShutdown();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        String token = "";
        File file = new File("token.txt");
        // get first line of that file
        try (FileReader reader = new FileReader(file)) {
            token = new BufferedReader(reader).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DiscordJv discordJv = new DiscordJv(token, EnumSet.of(Intent.GUILDS, Intent.GUILD_MESSAGES));
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                discordJv.setStatus(StatusType.DO_NOT_DISTURB);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void initiateNoShutdown() {
        // stop program from shutting down
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setStatus(StatusType status) throws IOException {
        JSONObject payload = new JSONObject();
        payload.put("op", 3);
        JSONObject data = new JSONObject();
        data.put("since", 0);
        data.put("activities", new JSONArray());
        data.put("status", status.toString().toLowerCase());
        data.put("afk", false);
        payload.put("d", data);
        gatewayFactory.getClientSession().sendMessage(new TextMessage(payload.toString()));
    }

    /**
     * Returns the bot's token inputted in the constructor
     */
    public String getToken() {
        return token;
    }

    /**
     * Returns the bot's intents inputted in the constructor
     */
    public EnumSet<Intent> getIntents() {
        return intents;
    }

    /**
     * Returns the cache for storing guilds
     */
    public Cache<Guild> getGuildCache() {
        return guildCache;
    }

    /**
     * Returns a {@link Application} object containing information about the bot
     */
    public Application getSelfInfo() {
        return Application.decompile(Requester.get(URLS.GET.APPLICATION.APPLICATION_INFORMATION, this).body());
    }

    /**
     * Returns the cache for storing users
     */
    public Cache<User> getUserCache() {
        return userCache;
    }

}
