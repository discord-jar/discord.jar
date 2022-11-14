package com.seailz.javadiscordwrapper;

import com.seailz.javadiscordwrapper.events.DiscordListener;
import com.seailz.javadiscordwrapper.events.EventDispatcher;
import com.seailz.javadiscordwrapper.events.annotation.EventMethod;
import com.seailz.javadiscordwrapper.events.model.command.CommandPermissionUpdateEvent;
import com.seailz.javadiscordwrapper.events.model.message.MessageCreateEvent;
import com.seailz.javadiscordwrapper.gateway.GatewayFactory;
import com.seailz.javadiscordwrapper.model.application.Application;
import com.seailz.javadiscordwrapper.model.channel.Channel;
import com.seailz.javadiscordwrapper.model.guild.Guild;
import com.seailz.javadiscordwrapper.model.application.Intent;
import com.seailz.javadiscordwrapper.model.user.User;
import com.seailz.javadiscordwrapper.model.status.Status;
import com.seailz.javadiscordwrapper.model.status.activity.Activity;
import com.seailz.javadiscordwrapper.model.status.activity.ActivityType;
import com.seailz.javadiscordwrapper.utils.discordapi.*;
import com.seailz.javadiscordwrapper.utils.URLS;
import com.seailz.javadiscordwrapper.utils.cache.Cache;
import com.seailz.javadiscordwrapper.model.status.StatusType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.springframework.boot.web.servlet.support.ServletContextApplicationContextInitializer;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
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
     * Used for caching {@link Guild}s in memory
     */
    private Cache<Guild> guildCache;
    /**
     * Used for caching {@link User}s in memory
     */
    private Cache<User> userCache;
    /**
     * Used for caching {@link Channel}s in memory
     */
    private Cache<Channel> channelCache;
    /**
     * Used for caching {@link Application}s in memory
     */
    private Cache<Application> applicationCache;
    /**
     * Manages dispatching events to listeners
     */
    private EventDispatcher eventDispatcher;
    /**
     * Queued messages to be sent to the Discord API incase the rate-limits are hit
     */
    private List<DiscordRequest> queuedRequests;
    /**
     * The rate-limits the bot is facing
     * Key: The endpoint
     * Value: The amount of requests left
     */
    private HashMap<String, RateLimit> rateLimits;

    /**
     * Creates a new instance of the DiscordJv class
     * @param token   The token of the bot
     * @param intents The intents the bot will use
     *
     * @throws ExecutionException
     *         If an error occurs while connecting to the gateway
     * @throws InterruptedException
     *         If an error occurs while connecting to the gateway
     */
    public DiscordJv(String token, EnumSet<Intent> intents) throws ExecutionException, InterruptedException {
        this.token = token;
        this.intents = intents;
        logger = Logger.getLogger("DiscordJv");
        this.guildCache = new Cache<>();
        this.userCache = new Cache<>();
        this.channelCache = new Cache<>();
        this.applicationCache = new Cache<>();
        this.eventDispatcher = new EventDispatcher(this);
        this.queuedRequests = new ArrayList<>();
        this.rateLimits = new HashMap<>();
        new RequestQueueHandler(this);
        this.gatewayFactory = new GatewayFactory(this);

        initiateNoShutdown();
    }

    // This method is used for testing purposes only
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
        discordJv.registerListeners(
                new DiscordListener() {
                    @Override
                    @EventMethod
                    public void onMessageReceived(@NotNull MessageCreateEvent event) {
                        System.out.println("Message received: " + event.getGuild().name());
                    }
                }
        );

        ArrayList<Activity> activities = new ArrayList<>();
        activities.add(
                new Activity("Hello World2", ActivityType.WATCHING)
        );
        Status status = new Status(0, activities.toArray(new Activity[0]), StatusType.DO_NOT_DISTURB, false);
        discordJv. setStatus(status);

        discordJv.getCacheByObjectType(ServletContextApplicationContextInitializer.class);

        //System.out.println(discordJv.getUserById("947691195658797167").username());
    }

    /**
     * Stops the bot from shutting down when processes are finished
     */
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

    /**
     * Sets the bot's status
     *
     * @param status
     *        The status to set
     *
     * @throws IOException
     *         If an error occurs while setting the status
     */
    public void setStatus(Status status) throws IOException {
        JSONObject json = new JSONObject();
        json.put("d", status.compile());
        json.put("op", 3);
        gatewayFactory.queueMessage(json);
    }

    /**
     * Returns info about a user
     *
     * @param id
     *        The id of the user
     *
     * @return The user
     */
    @Nullable
    public User getUserById(String id) {
        return userCache.getById(id);
    }

    /**
     * Returns info about a user
     *
     * @param id
     *        The id of the user
     *
     * @return The user
     */
    @Nullable
    public User getUserById(long id) {
        return getUserById(String.valueOf(id));
    }

    /**
     * Returns info about a channel
     *
     * @param id
     *        The id of the channel
     *
     * @return A {@link Channel} object containing the information.
     */
    @Nullable
    public Channel getChannelById(String id) {
        return channelCache.getById(id);
    }

    @Nullable
    private Application getApplicationById(String id) {
        return applicationCache.getById(id);
    }

    /**
     * Registers a listener, or multiple, with the event dispatcher
     * @param listeners The listeners to register
     */
    public void registerListeners(DiscordListener... listeners) {
        eventDispatcher.addListener(listeners);
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
    public Guild getGuildById(String id) {
        return guildCache.getById(id);
    }

    /**
     * Returns a {@link Application} object containing information about the bot
     */
    public Application getSelfInfo() {
        DiscordResponse response = new DiscordRequest(
                new JSONObject(), new HashMap<>(),
                URLS.GET.APPLICATION.APPLICATION_INFORMATION,
                this, URLS.GET.APPLICATION.APPLICATION_INFORMATION
        ).invoke();
        return Application.decompile(response.body());
    }

    /**
     * Returns the event dispatcher
     */
    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    /**
     * Returns the rate-limit info
     */
    public HashMap<String, RateLimit> getRateLimits() {
        return rateLimits;
    }

    /**
     * Gets queued requests
     */
    public List<DiscordRequest> getQueuedRequests() {
        return queuedRequests;
    }

    private Cache<?> getCacheByObjectType(Class<?> clazz) {
        for (Field field : this.getClass().getFields()) {
            if (!field.getType().equals(Cache.class)) continue;
            //if (clazz)
            System.out.println(field.getName());
            System.out.println(field.getType().getName());
        }
        return null;
    }

    private Cache<?> getCacheByObjectType(Object obj) {
        return getCacheByObjectType(obj.getClass());
    }

    public void addToCache(Object obj) {

    }
}
