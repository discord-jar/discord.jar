package com.seailz.discordjv;

import com.seailz.discordjv.action.guild.GetCurrentUserGuildsAction;
import com.seailz.discordjv.command.Command;
import com.seailz.discordjv.command.CommandChoice;
import com.seailz.discordjv.command.CommandDispatcher;
import com.seailz.discordjv.command.CommandOption;
import com.seailz.discordjv.command.annotation.ContextCommandInfo;
import com.seailz.discordjv.command.annotation.SlashCommandInfo;
import com.seailz.discordjv.command.listeners.CommandListener;
import com.seailz.discordjv.command.listeners.MessageContextCommandListener;
import com.seailz.discordjv.command.listeners.UserContextCommandListener;
import com.seailz.discordjv.command.listeners.slash.SlashCommandListener;
import com.seailz.discordjv.command.listeners.slash.SlashSubCommand;
import com.seailz.discordjv.command.listeners.slash.SubCommandListener;
import com.seailz.discordjv.events.DiscordListener;
import com.seailz.discordjv.events.EventDispatcher;
import com.seailz.discordjv.gateway.GatewayFactory;
import com.seailz.discordjv.model.application.Application;
import com.seailz.discordjv.model.application.Intent;
import com.seailz.discordjv.model.channel.Channel;
import com.seailz.discordjv.model.channel.MessagingChannel;
import com.seailz.discordjv.model.channel.audio.VoiceRegion;
import com.seailz.discordjv.model.emoji.sticker.Sticker;
import com.seailz.discordjv.model.emoji.sticker.StickerPack;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.status.Status;
import com.seailz.discordjv.model.user.User;
import com.seailz.discordjv.utils.Checker;
import com.seailz.discordjv.utils.URLS;
import com.seailz.discordjv.utils.cache.Cache;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import com.seailz.discordjv.utils.discordapi.DiscordResponse;
import com.seailz.discordjv.utils.discordapi.RateLimit;
import com.seailz.discordjv.utils.discordapi.RequestQueueHandler;
import com.seailz.discordjv.utils.version.APIVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * The main class of the Discord.jv wrapper
 *
 * @author Seailz
 * @since 1.0
 */
public class DiscordJv {

    /**
     * The token of the bot
     */
    private final String token;
    /**
     * Used to manage the gateway connection
     */
    private final GatewayFactory gatewayFactory;
    /**
     * Stores the logger
     */
    private final Logger logger;
    /**
     * Intents the bot will use when connecting to the gateway
     */
    private final EnumSet<Intent> intents;
    /**
     * Used for caching guilds in memory
     */
    private final Cache<Guild> guildCache;
    /**
     * Used for caching users in memory
     */
    private final Cache<User> userCache;
    /**
     * Used for caching channels in memory
     */
    private final Cache<Channel> channelCache;
    /**
     * Manages dispatching events to listeners
     */
    private final EventDispatcher eventDispatcher;
    /**
     * Queued messages to be sent to the Discord API incase the rate-limits are hit
     */
    private final List<DiscordRequest> queuedRequests;
    /**
     * The rate-limits the bot is facing
     * Key: The endpoint
     * Value: The amount of requests left
     */
    private final HashMap<String, RateLimit> rateLimits;
    /**
     * The command dispatcher
     */
    protected final CommandDispatcher commandDispatcher;

    /**
     * Creates a new instance of the DiscordJv class
     * This will start the connection to the Discord gateway, set caches, set the event dispatcher, set the logger, set up eliminate handling, and initiates no shutdown
     *
     * @param token   The token of the bot
     * @param intents The intents the bot will use
     * @param version The version of the Discord API the bot will use
     * @throws ExecutionException   If an error occurs while connecting to the gateway
     * @throws InterruptedException If an error occurs while connecting to the gateway
     */
    public DiscordJv(String token, EnumSet<Intent> intents, APIVersion version) throws ExecutionException, InterruptedException {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        token = token.replaceAll("\\r\\n", "");
        new RequestQueueHandler(this);
        this.token = token;
        this.intents = intents;
        new URLS(version);
        logger = Logger.getLogger("DiscordJv");
        this.commandDispatcher = new CommandDispatcher();
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

        initiateNoShutdown();
        initiateShutdownHooks();
    }

    public DiscordJv(String token) throws ExecutionException, InterruptedException {
        this(token, EnumSet.of(Intent.GUILDS, Intent.GUILD_MESSAGES, Intent.GUILD_MESSAGE_REACTIONS), APIVersion.getLatest());
    }

    public DiscordJv(String token, EnumSet<Intent> intents) throws ExecutionException, InterruptedException {
        this(token, intents, APIVersion.getLatest());
    }

    /**
     * Stops the bot from shutting down when processes are finished
     */
    protected void initiateNoShutdown() {
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

    protected void initiateShutdownHooks() {
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
     * @param status The status to set
     * @throws IOException If an error occurs while setting the status
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
     * @param id The id of the user
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
     * @param id The id of the user
     * @return The user
     */
    @Nullable
    public User getUserById(long id) {
        Checker.isSnowflake(String.valueOf(id), "Given id is not a snowflake");
        return getUserById(String.valueOf(id));
    }

    /**
     * Returns info about a {@link Channel}
     *
     * @param id The id of the channel
     * @return A {@link Channel} object
     */
    @Nullable
    public Channel getChannelById(String id) {
        Checker.isSnowflake(id, "Given id is not a snowflake");
        return getChannelCache().getById(id);
    }

    /**
     * Returns info about a {@link Channel}
     *
     * @param id The id of the channel
     * @return A {@link Channel} object
     */
    @Nullable
    public MessagingChannel getTextChannelById(String id) {
        Checker.isSnowflake(id, "Given id is not a snowflake");
        return MessagingChannel.decompile(getChannelCache().getFresh(id), this);
    }

    /**
     * Returns info about a {@link Guild}
     *
     * @param id The id of the guild
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
     * @param id The id of the guild
     * @return A {@link Guild} object
     */
    @Nullable
    public Guild getGuildById(String id) {
        Checker.isSnowflake(id, "Given id is not a snowflake");
        return getGuildCache().getById(id);
    }

    /**
     * Returns info about a {@link Sticker}
     *
     * @param id The id of the sticker
     * @return A {@link Sticker} object
     */
    @Nullable
    public Sticker getStickerById(String id) {
        Checker.isSnowflake(id, "Given id is not a snowflake");
        return Sticker.decompile(new DiscordRequest(
                new JSONObject(), new HashMap<>(),
                URLS.GET.STICKER.GET_STICKER.replace("{sticker.id}", id),
                this, URLS.GET.STICKER.GET_STICKER, RequestMethod.GET
        ).invoke().body(), this);
    }

    /**
     * Returns a list of {@link StickerPack StickerPacks} that nitro subscribers can use
     *
     * @return List of {@link StickerPack StickerPacks}
     */
    public List<StickerPack> getNitroStickerPacks() {
        return StickerPack.decompileList(new DiscordRequest(
                new JSONObject(), new HashMap<>(),
                URLS.GET.STICKER.GET_NITRO_STICKER_PACKS,
                this, URLS.GET.STICKER.GET_NITRO_STICKER_PACKS, RequestMethod.GET
        ).invoke().body(), this);
    }


    /**
     * Registers a listener, or multiple, with the event dispatcher
     *
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
     * Returns the command dispatcher
     */
    @NotNull
    public CommandDispatcher getCommandDispatcher() {
        return commandDispatcher;
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

    /**
     * Registers command(s) with Discord.
     *
     * @param listeners The listeners/commands to register
     * @throws IllegalArgumentException <ul>
     *                                  <li>If the command name is less than 1 character or more than 32 characters</li>
     *
     *                                  <li>If the command description is less than 1 character or more than 100 characters</li>
     *
     *                                  <li>If the command options are more than 25</li>
     *
     *                                  <li>If a command option name is less than 1 character or more than 32 characters</li>
     *
     *                                  <li>If a command option description is less than 1 character or more than 100 characters</li>
     *
     *                                  <li>If a command option choices are more than 25</li>
     *
     *                                  <li>If a command option choice name is less than 1 character or more than 100 characters</li>
     *
     *                                  <li>If a command option choice value is less than 1 character or more than 100 characters</li></ul>
     */
    public void registerCommands(CommandListener... listeners) {
        for (CommandListener listener : listeners) {
            Checker.check((listener instanceof SlashCommandListener) && !listener.getClass().isAnnotationPresent(SlashCommandInfo.class), "SlashCommandListener must have @SlashCommandInfo annotation");
            Checker.check((listener instanceof MessageContextCommandListener || listener instanceof UserContextCommandListener)
                    && !listener.getClass().isAnnotationPresent(ContextCommandInfo.class), "Context commands must have @ContextCommandInfo annotation");

            Annotation ann = listener.getClass().isAnnotationPresent(SlashCommandInfo.class) ? listener.getClass().getAnnotation(SlashCommandInfo.class) : listener.getClass().getAnnotation(ContextCommandInfo.class);
            String name = (ann instanceof SlashCommandInfo) ? ((SlashCommandInfo) ann).name() : ((ContextCommandInfo) ann).value();
            String description =  (ann instanceof SlashCommandInfo) ? ((SlashCommandInfo) ann).description() : "";
            registerCommand(
                    new Command(
                            name,
                            listener.getType(),
                            description,
                            (listener instanceof SlashCommandListener) ? ((SlashCommandListener) listener).getOptions() : new ArrayList<>()
                    )
            );
            commandDispatcher.registerCommand(name, listener);

            if (!(listener instanceof SlashCommandListener slashCommandListener)) return;
            if (slashCommandListener.getSubCommands().isEmpty()) return;

            for (SlashSubCommand subCommand : slashCommandListener.getSubCommands().keySet()) {
                SubCommandListener subListener =
                        slashCommandListener.getSubCommands().values().stream().toList().get(
                                slashCommandListener.getSubCommands().keySet().stream().toList()
                                        .indexOf(subCommand)
                        );
                commandDispatcher.registerSubCommand(slashCommandListener, subCommand, subListener);
            }
        }
    }

    protected void registerCommand(Command command) {
        Checker.check(!(command.name().length() > 1 && command.name().length() < 32), "Command name must be within 1 and 32 characters!");
        Checker.check(!Objects.equals(command.description(), "") && !(command.description().length() > 1 && command.description().length() < 100), "Command description must be within 1 and 100 characters!");
        Checker.check(command.options().size() > 25, "Application commands can only have up to 25 options!");

        for (CommandOption o : command.options()) {
            Checker.check(!(o.name().length() > 1 && o.name().length() < 32), "Option name must be within 1 and 32 characters!");
            if (o.description() != null) Checker.check(!(o.description().length() > 1 && o.description().length() < 100), "Option description must be within 1 and 100 characters!");
            if (o.choices() != null) Checker.check(o.choices().size() > 25, "Command options can only have up to 25 choices!");

            if (o.choices() != null) {
                for (CommandChoice c : o.choices()) {
                    Checker.check(!(c.name().length() > 1 && c.name().length() < 100), "Choice name must be within 1 and 100 characters!");
                    Checker.check(!(c.value().length() > 1 && c.value().length() < 100), "Choice value must be within 1 and 100 characters!");
                }
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
     */
    public void clearCommands() {
        DiscordRequest cmdDelReq = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.POST.COMMANDS.GLOBAL_COMMANDS.replace("{application.id}", getSelfInfo().id()),
                this,
                URLS.BASE_URL,
                RequestMethod.PUT
        );
        cmdDelReq.invoke(new JSONArray());
    }

    /**
     * Retrieves up to 200 guilds the bot is in.
     * <br>If you want to retrieve more guilds than that, you need to specify the last guild id in the <b>after</b> parameter.
     *<p>
     * Please be aware of the fact that this method is rate limited quite heavily.
     * <br>It is recommended that only smaller bots use this method.
     *<p>
     * If you need to retrieve a (possibly inaccurate) list of guilds as a larger bot, use {@link #getGuildCache()} instead.
     * <br>All guilds retrieved from this method will be cached.
     */
    public GetCurrentUserGuildsAction getGuilds() {
        return new GetCurrentUserGuildsAction(this);
    }

    /**
     * Retrieves all voice regions.
     * <br>They can be used to specify the rtc_region of a voice or stage channel.
     *
     * <p>You can find the RTC region of an {@link com.seailz.discordjv.model.channel.AudioChannel AudioChannel} by using {@link com.seailz.discordjv.model.channel.AudioChannel#region() AudioChannel#region()}.
     * <br>Avoid switching to deprecated regions.
     *
     * @return A list of all voice regions.
     */
    public List<VoiceRegion> getVoiceRegions() {
        DiscordRequest request = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.VOICE.REGIONS.GET_VOICE_REGIONS,
                this,
                URLS.GET.VOICE.REGIONS.GET_VOICE_REGIONS,
                RequestMethod.GET
        );
        JSONArray response = request.invoke().arr();
        List<VoiceRegion> regions = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            regions.add(VoiceRegion.decompile(response.getJSONObject(i)));
        }
        return regions;
    }

}
