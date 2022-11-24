package com.seailz.javadiscordwrapper;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.seailz.javadiscordwrapper.events.DiscordListener;
import com.seailz.javadiscordwrapper.events.EventDispatcher;
import com.seailz.javadiscordwrapper.gateway.GatewayFactory;
import com.seailz.javadiscordwrapper.model.application.Application;
import com.seailz.javadiscordwrapper.model.channel.Channel;
import com.seailz.javadiscordwrapper.model.commands.*;
import com.seailz.javadiscordwrapper.model.guild.Guild;
import com.seailz.javadiscordwrapper.model.application.Intent;
import com.seailz.javadiscordwrapper.model.user.User;
import com.seailz.javadiscordwrapper.model.status.Status;
import com.seailz.javadiscordwrapper.model.status.activity.Activity;
import com.seailz.javadiscordwrapper.model.status.activity.ActivityType;
import com.seailz.javadiscordwrapper.utils.Checker;
import com.seailz.javadiscordwrapper.utils.discordapi.*;
import com.seailz.javadiscordwrapper.utils.URLS;
import com.seailz.javadiscordwrapper.utils.cache.Cache;
import com.seailz.javadiscordwrapper.model.status.StatusType;
import com.seailz.javadiscordwrapper.utils.version.APIVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.*;
import java.util.*;
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
    /**
     * Used for caching channels in memory
     */
    private Cache<Channel> channelCache;
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
     * This will start the connection to the Discord gateway, set caches, set the event dispatcher, set the logger, set up eliminate handling, and initiates no shutdown
     *
     * @param token
     *        The token of the bot
     * @param intents
     *        The intents the bot will use
     * @param version
     *        The version of the Discord API the bot will use
     *
     * @throws ExecutionException
     *         If an error occurs while connecting to the gateway
     * @throws InterruptedException
     *         If an error occurs while connecting to the gateway
     */
    public DiscordJv(String token, EnumSet<Intent> intents, APIVersion version) throws ExecutionException, InterruptedException {
        token = token.replaceAll("\\r\\n", "");
        this.token = token;
        this.intents = intents;
        new URLS(version);
        logger = Logger.getLogger("DiscordJv");
        this.rateLimits = new HashMap<>();
        this.queuedRequests = new ArrayList<>();
        this.gatewayFactory = new GatewayFactory(this);
        this.guildCache = new Cache<>(this, Guild.class,
                new DiscordRequest(
                        new JSONObject(),
                        new HashMap<>(),
                        URLS.GET.GUILDS.GET_GUILD.replace("{guild.id}", "%s"),
                        this,
                        URLS.GET.GUILDS.GET_GUILD,
                        RequestMethod.GET
                ));

        this.userCache = new Cache<>(this, User.class, new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.USER.GET_USER.replace("{user.id}", "%s"),
                this,
                URLS.GET.USER.GET_USER,
                RequestMethod.GET
        ));

        this.channelCache = new Cache<>(this, Channel.class, new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.CHANNELS.GET_CHANNEL.replace("{channel.id}", "%s"),
                this,
                URLS.GET.CHANNELS.GET_CHANNEL,
                RequestMethod.GET
        ));

        this.eventDispatcher = new EventDispatcher(this);
        new RequestQueueHandler(this);

        initiateNoShutdown();
        initiateShutdownHooks();
    }

    public DiscordJv(String token) throws ExecutionException, InterruptedException {
        this(token, EnumSet.of(Intent.GUILDS, Intent.GUILD_MESSAGES, Intent.GUILD_MESSAGE_REACTIONS), APIVersion.getLatest());
    }

    public DiscordJv(String token, EnumSet<Intent> intents) throws ExecutionException, InterruptedException {
        this(token, intents, APIVersion.getLatest());
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
        ArrayList<CommandOption> options = new ArrayList<>();
        options.add(
                new CommandOption(
                        "test1",
                        "test1",
                        CommandOptionType.STRING,
                        false,
                        Collections.emptyList()
                )
        );
        options.add(
                new CommandOption(
                        "test2",
                        "test2",
                        CommandOptionType.CHANNEL,
                        false,
                        Collections.emptyList()
                )
        );
        options.add(
                new CommandOption(
                        "test3",
                        "test3",
                        CommandOptionType.BOOLEAN,
                        false,
                        Collections.emptyList()
                )
        );
        options.add(
                new CommandOption(
                        "test4",
                        "test4",
                        CommandOptionType.ATTACHMENT,
                        false,
                        Collections.emptyList()
                )
        );
        discordJv.registerCommand(new Command("foo", CommandType.SLASH_COMMAND, "multi options", options.stream().toList()));

        discordJv.clearCommands();

        ArrayList<Activity> activities = new ArrayList<>();
        activities.add(
                new Activity("Hello World2", ActivityType.WATCHING)
        );
        Status status = new Status(0, activities.toArray(new Activity[0]), StatusType.DO_NOT_DISTURB, false);
        discordJv.setStatus(status);
        discordJv.getChannelById("1042459398213210164").sendMessage("hello, world.").run();
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

    public void initiateShutdownHooks() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            gatewayFactory.close();
        }));
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
    public void setStatus(@NotNull Status status) throws IOException {
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
        Checker.isSnowflake(id, "Given id is not a snowflake");
        return userCache.getById(id);
    }


    /**
     * Returns info about the bot user
     * This shouldn't return null, but it might if the Discord API didn't respond correctly.
     *
     * @return a {@link User} object
     */
    @Nullable
    public User getSelfUser() {
        DiscordResponse response = new DiscordRequest(
                new JSONObject(), new HashMap<>(),
                URLS.GET.USER.GET_USER.replace("{user.id}", "@me"),
                this, URLS.GET.USER.GET_USER, RequestMethod.GET
        ).invoke();
        return User.decompile(response.body(), this);
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
        Checker.isSnowflake(String.valueOf(id), "Given id is not a snowflake");
        return getUserById(String.valueOf(id));
    }

    /**
     * Returns info about a {@link Channel}
     * @param id
     *      The id of the channel
     *
     * @return A {@link Channel} object
     */
    @Nullable
    public Channel getChannelById(String id) {
        Checker.isSnowflake(id, "Given id is not a snowflake");
       return getChannelCache().getById(id);
    }

    /**
     * Returns info about a {@link Guild}
     *
     * @param id
     *       The id of the guild
     *
     * @return A {@link Guild} object
     */
    @Nullable
    public Guild getGuildById(long id) {
        Checker.isSnowflake(String.valueOf(id), "Given id is not a snowflake");
        return getGuildById(String.valueOf(id));
    }

    /**
     * Returns info about a {@link Guild}
     *
     * @param id
     *       The id of the guild
     *
     * @return A {@link Guild} object
     */
    @Nullable
    public Guild getGuildById(String id) {
        Checker.isSnowflake(id, "Given id is not a snowflake");
        return getGuildCache().getById(id);
    }

    /**
     * Registers a listener, or multiple, with the event dispatcher
     * @param listeners The listeners to register
     */
    public void registerListeners(@NotNull DiscordListener... listeners) {
        eventDispatcher.addListener(listeners);
    }

    /**
     * Returns the bot's token inputted in the constructor
     */
    @NotNull
    public String getToken() {
        return token;
    }

    /**
     * Returns the bot's intents inputted in the constructor
     */
    @NotNull
    public EnumSet<Intent> getIntents() {
        return intents;
    }

    /**
     * Returns the cache for storing guilds
     */
    @NotNull
    public Cache<Guild> getGuildCache() {
        return guildCache;
    }

    /**
     * Returns a {@link Application} object containing information about the bot
     */
    @Nullable
    public Application getSelfInfo() {
        DiscordResponse response = new DiscordRequest(
                new JSONObject(), new HashMap<>(),
                URLS.GET.APPLICATION.APPLICATION_INFORMATION,
                this, URLS.GET.APPLICATION.APPLICATION_INFORMATION, RequestMethod.GET
        ).invoke();
        return Application.decompile(response.body(), this);
    }

    /**
     * Returns the cache for storing users
     */
    @NotNull
    public Cache<User> getUserCache() {
        return userCache;
    }

    /**
     * Returns the event dispatcher
     */
    @NotNull
    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    /**
     * Returns the rate-limit info
     */
    @NotNull
    public HashMap<String, RateLimit> getRateLimits() {
        return rateLimits;
    }

    /**
     * Gets queued requests
     */
    @NotNull
    public List<DiscordRequest> getQueuedRequests() {
        return queuedRequests;
    }
    /**
     * Get channel cache
     */
    @NotNull
    public Cache<Channel> getChannelCache() {
        return channelCache;
    }
    /**
     * Adds an intent
     */
    public void addIntent(@NotNull Intent intent) {
        intents.add(intent);
    }

    private void registerCommand(Command command) {
        // validate command attributes
        if (!(command.name().length() > 1 && command.name().length() < 32)) throw new IllegalArgumentException("Command name must be within 1 and 32 characters!");
        if (!(command.description().length() > 1 && command.description().length() < 100)) throw new IllegalArgumentException("Command description must be within 1 and 100 characters!");
        if (command.options().size() > 25) throw new IllegalArgumentException("Application commands can only have up to 25 options!");

        for (CommandOption o:command.options()) {
            if (!(o.name().length() > 1 && o.name().length() < 32)) throw new IllegalArgumentException("Option name must be within 1 and 32 characters!");
            if (!(o.description().length() > 1 && o.description().length() < 100)) throw new IllegalArgumentException("Option description must be within 1 and 100 characters!");
            if (o.choices().size() > 25) throw new IllegalArgumentException("Command options can only have up to 25 choices!");

            for (CommandChoice c:o.choices()) {
                if (!(c.name().length() > 1 && c.name().length() < 100)) throw new IllegalArgumentException("Choice name must be within 1 and 100 characters!");
                if (!(c.value().length() > 1 && c.value().length() < 100)) throw new IllegalArgumentException("Choice value must be within 1 and 100 characters!");
            }
        }

        DiscordRequest commandReq = new DiscordRequest(
                command.compile(),
                new HashMap<>(),
                URLS.POST.COMMANDS.GLOBAL_COMMANDS.replace("{application.id}", getSelfInfo().id()),
                this,
                URLS.BASE_URL,
                RequestMethod.POST);
        commandReq.invoke();
    }

    /**
     * Clears all the global commands for this app. Cannot be undone.
     * @apiNote This currently does not work, don't use.
     */
    public void clearCommands() {
        DiscordRequest cmdDelReq = new DiscordRequest(
                new JSONObject("{[]}"),
                new HashMap<>(),
                URLS.POST.COMMANDS.GLOBAL_COMMANDS.replace("{application.id}", getSelfInfo().id()),
                this,
                URLS.BASE_URL,
                RequestMethod.PUT
        );
        cmdDelReq.invoke();
    }
}
