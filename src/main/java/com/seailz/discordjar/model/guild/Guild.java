package com.seailz.discordjar.model.guild;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.action.automod.AutomodRuleCreateAction;
import com.seailz.discordjar.action.automod.AutomodRuleModifyAction;
import com.seailz.discordjar.action.guild.channel.CreateGuildChannelAction;
import com.seailz.discordjar.action.guild.members.RequestGuildMembersAction;
import com.seailz.discordjar.action.guild.scheduledevents.CreateScheduledEventAction;
import com.seailz.discordjar.action.guild.scheduledevents.ModifyScheduledEventAction;
import com.seailz.discordjar.action.guild.onboarding.ModifyOnboardingGuild;
import com.seailz.discordjar.action.sticker.ModifyStickerAction;
import com.seailz.discordjar.cache.JsonCache;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.automod.AutomodRule;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.model.channel.GuildChannel;
import com.seailz.discordjar.model.channel.thread.Thread;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.emoji.Emoji;
import com.seailz.discordjar.model.emoji.sticker.Sticker;
import com.seailz.discordjar.model.guild.filter.ExplicitContentFilterLevel;
import com.seailz.discordjar.model.guild.incident.IncidentsData;
import com.seailz.discordjar.model.guild.mfa.MFALevel;
import com.seailz.discordjar.model.guild.notification.DefaultMessageNotificationLevel;
import com.seailz.discordjar.model.guild.premium.PremiumTier;
import com.seailz.discordjar.model.guild.scheduledevents.ScheduledEvent;
import com.seailz.discordjar.model.guild.verification.VerificationLevel;
import com.seailz.discordjar.model.guild.welcome.WelcomeScreen;
import com.seailz.discordjar.model.invite.Invite;
import com.seailz.discordjar.model.invite.InviteMetadata;
import com.seailz.discordjar.model.invite.internal.InviteMetadataImpl;
import com.seailz.discordjar.model.role.Role;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.*;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import com.seailz.discordjar.utils.rest.Response;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Represents a guild.
 */
public class Guild implements Compilerable, Snowflake, CDNAble {
    private final String id;
    private final String name;
    private final String icon;
    private final String iconHash;
    private final String splash;
    private final String discoverySplash;
    private final boolean isOwner;
    private final User owner;
    private final String permissions;
    private final Channel afkChannel;
    private final int afkTimeout;
    private final boolean isWidgetEnabled;
    private Channel widgetChannel = null;
    private final String widgetChannelId;
    private final VerificationLevel verificationLevel;
    private final DefaultMessageNotificationLevel defaultMessageNotificationLevel;
    private final ExplicitContentFilterLevel explicitContentFilterLevel;
    private final List<Role> roles;
    private final List<Emoji> emojis;
    private final EnumSet<GuildFeature> features;
    private final MFALevel mfaLevel;
    private final String applicationId;
    private Channel systemChannel;
    private final String systemChannelId;
    private final int maxPresences;
    private final int maxMembers;
    private final String vanityUrlCode;
    private final String description;
    private final String banner;
    private final PremiumTier premiumTier;
    private final int premiumSubscriptionCount;
    private final String preferredLocale;
    private Channel publicUpdatesChannel;
    private final String publicUpdatesChannelId;
    private final int maxVideoChannelUsers;
    private final int maxStageVideoChannelUsers;
    private final int approximateMemberCount;
    private final int approximatePresenceCount;
    private final WelcomeScreen welcomeScreen;
    private final List<Sticker> stickers;
    private final boolean premiumProgressBarEnabled;
    private final String safetyAlertChannelId;
    private Channel safetyAlertChannel = null;
    private final IncidentsData incidentsData;

    private final DiscordJar discordJar;
    private final JsonCache roleCache;

    public Guild(
            String id,
            String name,
            String icon,
            String iconHash,
            String splash,
            String discoverySplash,
            boolean isOwner,
            User owner,
            String permissions,
            Channel afkChannel,
            int afkTimeout,
            boolean isWidgetEnabled,
            String widgetChannelId,
            VerificationLevel verificationLevel,
            DefaultMessageNotificationLevel defaultMessageNotificationLevel,
            ExplicitContentFilterLevel explicitContentFilterLevel,
            List<Role> roles,
            List<Emoji> emojis,
            EnumSet<GuildFeature> features,
            MFALevel mfaLevel,
            String applicationId,
            Channel systemChannel,
            String systemChannelId,
            int maxPresences,
            int maxMembers,
            String vanityUrlCode,
            String description,
            String banner,
            PremiumTier premiumTier,
            int premiumSubscriptionCount,
            String preferredLocale,
            Channel publicUpdatesChannel,
            String publicUpdatesChannelId,
            int maxVideoChannelUsers,
            int maxStageVideoChannelUsers,
            int approximateMemberCount,
            int approximatePresenceCount,
            WelcomeScreen welcomeScreen,
            List<Sticker> stickers,
            boolean premiumProgressBarEnabled,
            String safetyAlertChannelId,
            IncidentsData incidentsData, DiscordJar discordJar,
            JsonCache roleCache
    ) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.iconHash = iconHash;
        this.splash = splash;
        this.discoverySplash = discoverySplash;
        this.isOwner = isOwner;
        this.owner = owner;
        this.permissions = permissions;
        this.afkChannel = afkChannel;
        this.afkTimeout = afkTimeout;
        this.isWidgetEnabled = isWidgetEnabled;
        this.widgetChannelId = widgetChannelId;
        this.verificationLevel = verificationLevel;
        this.defaultMessageNotificationLevel = defaultMessageNotificationLevel;
        this.explicitContentFilterLevel = explicitContentFilterLevel;
        this.roles = roles;
        this.emojis = emojis;
        this.features = features;
        this.mfaLevel = mfaLevel;
        this.applicationId = applicationId;
        this.systemChannel = systemChannel;
        this.systemChannelId = systemChannelId;
        this.maxPresences = maxPresences;
        this.maxMembers = maxMembers;
        this.vanityUrlCode = vanityUrlCode;
        this.description = description;
        this.banner = banner;
        this.premiumTier = premiumTier;
        this.premiumSubscriptionCount = premiumSubscriptionCount;
        this.preferredLocale = preferredLocale;
        this.publicUpdatesChannel = publicUpdatesChannel;
        this.publicUpdatesChannelId = publicUpdatesChannelId;
        this.maxVideoChannelUsers = maxVideoChannelUsers;
        this.maxStageVideoChannelUsers = maxStageVideoChannelUsers;
        this.approximateMemberCount = approximateMemberCount;
        this.approximatePresenceCount = approximatePresenceCount;
        this.welcomeScreen = welcomeScreen;
        this.stickers = stickers;
        this.premiumProgressBarEnabled = premiumProgressBarEnabled;
        this.safetyAlertChannelId = safetyAlertChannelId;
        this.incidentsData = incidentsData;
        this.discordJar = discordJar;
        this.roleCache = roleCache;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String icon() {
        return icon;
    }

    public String iconHash() {
        return iconHash;
    }

    public String splash() {
        return splash;
    }

    public String discoverySplash() {
        return discoverySplash;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public User owner() {
        return owner;
    }

    public String permissions() {
        return permissions;
    }

    public Channel afkChannel() {
        return afkChannel;
    }

    public int afkTimeout() {
        return afkTimeout;
    }

    public boolean isWidgetEnabled() {
        return isWidgetEnabled;
    }

    public VerificationLevel verificationLevel() {
        return verificationLevel;
    }

    public DefaultMessageNotificationLevel defaultMessageNotificationLevel() {
        return defaultMessageNotificationLevel;
    }

    public ExplicitContentFilterLevel explicitContentFilterLevel() {
        return explicitContentFilterLevel;
    }

    public List<Emoji> emojis() {
        return emojis;
    }

    public EnumSet<GuildFeature> features() {
        return features;
    }

    public MFALevel mfaLevel() {
        return mfaLevel;
    }

    public String applicationId() {
        return applicationId;
    }

    public String systemChannelId() {
        return systemChannelId;
    }

    public int maxPresences() {
        return maxPresences;
    }

    public int maxMembers() {
        return maxMembers;
    }

    public String vanityUrlCode() {
        return vanityUrlCode;
    }

    public String description() {
        return description;
    }

    public String banner() {
        return banner;
    }

    public PremiumTier premiumTier() {
        return premiumTier;
    }

    public int premiumSubscriptionCount() {
        return premiumSubscriptionCount;
    }

    public String preferredLocale() {
        return preferredLocale;
    }

    public int maxVideoChannelUsers() {
        return maxVideoChannelUsers;
    }

    public int maxStageVideoChannelUsers() {
        return maxStageVideoChannelUsers;
    }

    public int approximateMemberCount() {
        return approximateMemberCount;
    }

    public int approximatePresenceCount() {
        return approximatePresenceCount;
    }

    public WelcomeScreen welcomeScreen() {
        return welcomeScreen;
    }

    public List<Sticker> stickers() {
        return stickers;
    }

    public boolean premiumProgressBarEnabled() {
        return premiumProgressBarEnabled;
    }
    public IncidentsData incidentsData() {
        return incidentsData;
    }

    public DiscordJar discordJar() {
        return discordJar;
    }

    public JsonCache roleCache() {
        return roleCache;
    }


    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("id", id)
                .put("name", name)
                .put("icon", icon)
                .put("icon_hash", iconHash)
                .put("splash", splash)
                .put("discovery_splash", discoverySplash)
                .put("owner", isOwner)
                .put("owner_id", owner)
                .put("permissions", permissions)
                .put("afk_channel_id", afkChannel.id())
                .put("afk_timeout", afkTimeout)
                .put("widget_enabled", isWidgetEnabled)
                .put("widget_channel_id", widgetChannel.id())
                .put("verification_level", verificationLevel.getCode())
                .put("default_message_notifications", defaultMessageNotificationLevel.getCode())
                .put("explicit_content_filter", explicitContentFilterLevel.getCode())
                .put("roles", roles)
                .put("emojis", emojis)
                .put("features", features)
                .put("mfa_level", mfaLevel.getCode())
                .put("application_id", applicationId)
                .put("system_channel_id", systemChannelId)
                .put("max_presences", maxPresences)
                .put("max_members", maxMembers)
                .put("vanity_url_code", vanityUrlCode)
                .put("description", description)
                .put("banner", banner)
                .put("premium_tier", premiumTier.getCode())
                .put("premium_subscription_count", premiumSubscriptionCount)
                .put("preferred_locale", preferredLocale)
                .put("public_updates_channel_id", publicUpdatesChannelId)
                .put("max_video_channel_users", maxVideoChannelUsers)
                .put("max_stage_video_channel_users", maxStageVideoChannelUsers)
                .put("approximate_member_count", approximateMemberCount)
                .put("approximate_presence_count", approximatePresenceCount)
                .put("welcome_screen", welcomeScreen)
                .put("stickers", stickers)
                .put("premium_progress_bar_enabled", premiumProgressBarEnabled)
                .put("safety_alerts_channel_id", safetyAlertChannelId)
                .put("incidents_data", incidentsData.compile());
    }

    @NotNull
    public static Guild decompile(JSONObject obj, DiscordJar discordJar) {
        return decompile(obj, discordJar, false);
    }

    @NotNull
    public static Guild decompile(JSONObject obj, DiscordJar discordJar, boolean bypassCache) {
        System.out.println("decomp guild");

        // If the cache happens to contain the current guild, we'll use that instead of creating a new one.
        // Helpful for slight memory optimization & performance.
        Guild cachedGuild = discordJar.getGuildCache().returnFromCache(obj.getString("id"));
        if (cachedGuild != null && !bypassCache) {
            return cachedGuild;
        }

        long nano = System.nanoTime();
        String id;
        String name;
        String icon;
        String iconHash;
        String splash;
        String discoverySplash;
        boolean isOwner;
        User owner;
        String permissions;
        Channel afkChannel;
        int afkTimeout;
        boolean isWidgetEnabled;
        String widgetChannelId;
        VerificationLevel verificationLevel;
        DefaultMessageNotificationLevel defaultMessageNotificationLevel;
        ExplicitContentFilterLevel explicitContentFilterLevel;
        List<Role> roles;
        List<Emoji> emojis;
        EnumSet<GuildFeature> features;
        MFALevel mfaLevel;
        String applicationId;
        String systemChannelId;
        int maxPresences;
        int maxMembers;
        String vanityUrlCode;
        String description;
        String banner;
        PremiumTier premiumTier;
        int premiumSubscriptionCount;
        String preferredLocale;
        String publicUpdatesChannelId;
        int maxVideoChannelUsers;
        int maxStageVideoChannelUsers = 0;
        int approximateMemberCount;
        int approximatePresenceCount;
        WelcomeScreen welcomeScreen;
        List<Sticker> stickers;
        boolean premiumProgressBarEnabled;
        String safetyAlertsChannelId = null;

        try {
            id = obj.getString("id");
        } catch (JSONException e) {
            id = null;
        }

        try {
            name = obj.getString("name");
        } catch (JSONException e) {
            name = null;
        }

        try {
            icon = obj.getString("icon");
        } catch (JSONException e) {
            icon = null;
        }

        try {
            iconHash = obj.getString("icon_hash");
        } catch (JSONException e) {
            iconHash = null;
        }

        try {
            splash = obj.getString("splash");
        } catch (JSONException e) {
            splash = null;
        }

        try {
            discoverySplash = obj.getString("discovery_splash");
        } catch (JSONException e) {
            discoverySplash = null;
        }

        try {
            isOwner = obj.getBoolean("owner");
        } catch (JSONException e) {
            isOwner = false;
        }

        try {
            owner = User.decompile(obj.getJSONObject("owner"), discordJar);
        } catch (JSONException e) {
            owner = null;
        }

        try {
            permissions = obj.getString("permissions");
        } catch (JSONException e) {
            permissions = null;
        }

        try {
            afkChannel = Channel.decompile(obj.getJSONObject("afk_channel"), discordJar);
        } catch (JSONException e) {
            afkChannel = null;
        }

        try {
            afkTimeout = obj.getInt("afk_timeout");
        } catch (JSONException e) {
            afkTimeout = 0;
        }

        try {
            isWidgetEnabled = obj.getBoolean("widget_enabled");
        } catch (JSONException e) {
            isWidgetEnabled = false;
        }

        try {
            widgetChannelId =obj.getString("widget_channel_id");
        } catch (JSONException e) {
            widgetChannelId = null;
        }

        try {
            verificationLevel = VerificationLevel.getVerificationLevel(obj.getInt("verification_level"));
        } catch (JSONException e) {
            verificationLevel = null;
        }

        try {
            defaultMessageNotificationLevel = DefaultMessageNotificationLevel.getDefaultMessageNotificationLevel(obj.getInt("default_message_notifications"));
        } catch (JSONException e) {
            defaultMessageNotificationLevel = null;
        }

        try {
            explicitContentFilterLevel = ExplicitContentFilterLevel.getExplicitContentFilterLevel(obj.getInt("explicit_content_filter"));
        } catch (JSONException e) {
            explicitContentFilterLevel = null;
        }

        try {
            JSONArray rolesArray = obj.getJSONArray("roles");
            roles = new ArrayList<>();
            for (int i = 0; i < rolesArray.length(); i++) {
                roles.add(Role.decompile(rolesArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            roles = null;
        }

        try {
            JSONArray emojisArray = obj.getJSONArray("emojis");
            emojis = new ArrayList<>();
            for (int i = 0; i < emojisArray.length(); i++) {
                emojis.add(Emoji.decompile(emojisArray.getJSONObject(i), discordJar));
            }
        } catch (JSONException e) {
            emojis = null;
        }

        try {
            features = GuildFeature.getGuildFeatures(obj.getJSONArray("features").toList().toArray(new String[0]));
        } catch (JSONException e) {
            features = null;
        }

        try {
            mfaLevel = MFALevel.getMFALevel(obj.getInt("mfa_level"));
        } catch (JSONException e) {
            mfaLevel = null;
        }

        try {
            applicationId = obj.getString("application_id");
        } catch (JSONException e) {
            applicationId = null;
        }

        try {
            systemChannelId = obj.getString("system_channel_id");
        } catch (IllegalArgumentException | JSONException e) {
            systemChannelId = null;
        }

        try {
            maxPresences = obj.getInt("max_presences");
        } catch (JSONException e) {
            maxPresences = 0;
        }

        try {
            maxMembers = obj.getInt("max_members");
        } catch (JSONException e) {
            maxMembers = 0;
        }

        try {
            vanityUrlCode = obj.getString("vanity_url_code");
        } catch (JSONException e) {
            vanityUrlCode = null;
        }

        try {
            description = obj.getString("description");
        } catch (JSONException e) {
            description = null;
        }

        try {
            banner = obj.getString("banner");
        } catch (JSONException e) {
            banner = null;
        }

        try {
            premiumTier = PremiumTier.getPremiumTier(obj.getInt("premium_tier"));
        } catch (JSONException e) {
            premiumTier = null;
        }

        try {
            premiumSubscriptionCount = obj.getInt("premium_subscription_count");
        } catch (JSONException e) {
            premiumSubscriptionCount = 0;
        }

        try {
            preferredLocale = obj.getString("preferred_locale");
        } catch (JSONException e) {
            preferredLocale = null;
        }

        try {
            publicUpdatesChannelId = obj.getString("public_updates_channel_id");
        } catch (Exception e) {
            publicUpdatesChannelId = null;
        }

        try {
            maxVideoChannelUsers = obj.getInt("max_video_channel_users");
        } catch (JSONException e) {
            maxVideoChannelUsers = 0;
        }
        
        try {
            approximateMemberCount = obj.getInt("approximate_member_count");
        } catch (JSONException e) {
            approximateMemberCount = 0;
        }

        try {
            approximateMemberCount = obj.getInt("approximate_member_count");
        } catch (JSONException e) {
            approximateMemberCount = 0;
        }

        try {
            approximatePresenceCount = obj.getInt("approximate_presence_count");
        } catch (JSONException e) {
            approximatePresenceCount = 0;
        }

        try {
            welcomeScreen = WelcomeScreen.decompile(obj.getJSONObject("welcome_screen"), discordJar);
        } catch (JSONException e) {
            welcomeScreen = null;
        }

        try {
            JSONArray stickersArray = obj.getJSONArray("stickers");
            stickers = new ArrayList<>();
            for (int i = 0; i < stickersArray.length(); i++) {
                stickers.add(Sticker.decompile(stickersArray.getJSONObject(i), discordJar));
            }
        } catch (JSONException e) {
            stickers = null;
        }

        try {
            premiumProgressBarEnabled = obj.getBoolean("premium_progress_bar_enabled");
        } catch (JSONException e) {
            premiumProgressBarEnabled = false;
        }

        if (obj.has("safety_alerts_channel_id") && !obj.isNull("safety_alerts_channel_id")) {
            safetyAlertsChannelId = obj.getString("safety_alerts_channel_id");
        }

        IncidentsData incidentsData = obj.has("incidents_data") && !obj.isNull("incidents_data") ? IncidentsData.decompile(obj.getJSONObject("incidents_data")) : null;

        Guild g = new Guild(
                id,
                name,
                icon,
                iconHash,
                splash,
                discoverySplash,
                isOwner,
                owner,
                permissions,
                afkChannel,
                afkTimeout,
                isWidgetEnabled,
                widgetChannelId,
                verificationLevel,
                defaultMessageNotificationLevel,
                explicitContentFilterLevel,
                roles,
                emojis,
                features,
                mfaLevel,
                applicationId,
                null,
                systemChannelId,
                maxPresences,
                maxMembers,
                vanityUrlCode,
                description,
                banner,
                premiumTier,
                premiumSubscriptionCount,
                preferredLocale,
                null,
                publicUpdatesChannelId,
                maxVideoChannelUsers,
                maxStageVideoChannelUsers,
                approximateMemberCount,
                approximatePresenceCount,
                welcomeScreen,
                stickers,
                premiumProgressBarEnabled,
                safetyAlertsChannelId,
                incidentsData, discordJar,
                JsonCache.newc(new DiscordRequest(
                        new JSONObject(),
                        new HashMap<>(),
                        URLS.GET.GUILDS.ROLES.GET_GUILD_ROLES.replace("{guild.id}", id),
                        discordJar,
                        URLS.GET.GUILDS.ROLES.GET_GUILD_ROLES,
                        RequestMethod.GET
                ))
        );
        return g;
    }

    public Channel systemChannel() {
        if (systemChannelId == null) return null;
        if (this.systemChannel != null) return this.systemChannel;
        this.systemChannel = discordJar.getChannelById(systemChannelId);
        return this.systemChannel;
    }

    public Channel publicUpdatesChannel() {
        if (publicUpdatesChannelId == null) return null;
        if (this.publicUpdatesChannel != null) return this.publicUpdatesChannel;
        this.publicUpdatesChannel = discordJar.getChannelById(publicUpdatesChannelId);
        return this.publicUpdatesChannel;
    }

    public Channel widgetChannel() {
        if (widgetChannelId == null) return null;
        if (this.widgetChannel != null) return this.widgetChannel;
        this.widgetChannel = discordJar.getChannelById(widgetChannelId);
        return this.widgetChannel;
    }

    public Channel safetyAlertsChannel() {
        if (safetyAlertChannelId == null) return null;
        if (this.safetyAlertChannel != null) return this.safetyAlertChannel;
        this.safetyAlertChannel = discordJar.getChannelById(safetyAlertChannelId);
        return this.safetyAlertChannel;
    }

    /**
     * Leaves a guild
     */
    public void leave() {
        try {
            new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.DELETE.GUILD.LEAVE_GUILD.replace(
                            "{guild.id}",
                            id
                    ),
                    discordJar,
                    URLS.DELETE.GUILD.LEAVE_GUILD,
                    RequestMethod.DELETE
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * Lists the stickers in the guild
     */
    public List<Sticker> getStickers() {
        try {
            return Sticker.decompileList(
                    new DiscordRequest(
                            new JSONObject(),
                            new HashMap<>(),
                            URLS.GET.GUILDS.STICKERS.GET_GUILD_STICKERS.replace(
                                    "{guild.id}",
                                    id
                            ),
                            discordJar,
                            URLS.GET.GUILDS.STICKERS.GET_GUILD_STICKERS,
                            RequestMethod.GET
                    ).invoke().arr(),
                    discordJar
            );
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * Gets a sticker by id
     *
     * @param stickerId The sticker id
     */
    public Sticker getStickerById(String stickerId) {
        try {
            return Sticker.decompile(
                    new DiscordRequest(
                            new JSONObject(),
                            new HashMap<>(),
                            URLS.GET.GUILDS.STICKERS.GET_GUILD_STICKER.replace(
                                    "{guild.id}",
                                    id
                            ).replace(
                                    "{sticker.id}",
                                    stickerId
                            ),
                            discordJar,
                            URLS.GET.GUILDS.STICKERS.GET_GUILD_STICKER,
                            RequestMethod.GET
                    ).invoke().body(),
                    discordJar
            );
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * Modifies a sticker's properties
     */
    public ModifyStickerAction modifySticker(String stickerId) {
        return new ModifyStickerAction(stickerId, discordJar);
    }

    /**
     * Deletes a sticker
     */
    public void deleteSticker(String stickerId) {
        try {
            new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.DELETE.GUILD.STICKER.DELETE_GUILD_STICKER.replace(
                            "{guild.id}",
                            id
                    ).replace(
                            "{sticker.id}",
                            stickerId
                    ),
                    discordJar,
                    URLS.DELETE.GUILD.STICKER.DELETE_GUILD_STICKER,
                    RequestMethod.DELETE
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * Returns a list of {@link AutomodRule automod rules} that the guild has
     */
    @NotNull
    public List<AutomodRule> getAutomodRules() {
        try {
            return AutomodRule.decompileList(
                    new DiscordRequest(
                            new JSONObject(),
                            new HashMap<>(),
                            URLS.GET.GUILDS.AUTOMOD.LIST_AUTOMOD_RULES.replace(
                                    "{guild.id}",
                                    id
                            ),
                            discordJar,
                            URLS.GET.GUILDS.AUTOMOD.LIST_AUTOMOD_RULES,
                            RequestMethod.GET
                    ).invoke().arr(),
                    discordJar
            );
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    @NotNull
    @Contract("_ -> new")
    public AutomodRule getAutomodRuleById(String id) {
        try {
            return AutomodRule.decompile(
                    new DiscordRequest(
                            new JSONObject(),
                            new HashMap<>(),
                            URLS.GET.GUILDS.AUTOMOD.GET_AUTOMOD_RULE.replace(
                                    "{guild.id}",
                                    this.id
                            ).replace(
                                    "{rule.id}",
                                    id
                            ),
                            discordJar,
                            URLS.GET.GUILDS.AUTOMOD.GET_AUTOMOD_RULE,
                            RequestMethod.GET
                    ).invoke().body(),
                    discordJar
            );
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    @NotNull
    @Contract("_ -> new")
    public AutomodRule getAutomodRuleById(long id) {
        return getAutomodRuleById(Long.toString(id));
    }

    public AutomodRuleCreateAction createAutomodRule(String name, AutomodRule.EventType eventType, AutomodRule.TriggerType triggerType, List<AutomodRule.Action> actions) {
        return new AutomodRuleCreateAction(name, eventType, triggerType, actions, this, discordJar);
    }

    public void deleteAutoModRule(String id) {
        try {
            new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.DELETE.GUILD.AUTOMOD.DELETE_AUTOMOD_RULE.replace(
                            "{guild.id}",
                            this.id
                    ).replace(
                            "{rule.id}",
                            id
                    ),
                    discordJar,
                    URLS.DELETE.GUILD.AUTOMOD.DELETE_AUTOMOD_RULE,
                    RequestMethod.DELETE
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    public AutomodRuleModifyAction modifyAutomodRule(String id) {
        return new AutomodRuleModifyAction(id, this, discordJar);
    }

    public Member getMemberById(String id) {
        return discordJar.getMemberById(this.id, id);
    }

    public List<Member> getMembers(int limit, String after) {
        Checker.check(limit <= 0, "Limit must be greater than 0");
        Checker.check(limit > 1000, "Limit must be less than or equal to 1000");
        JSONArray arr = null;
        try {
            arr = new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.GET.GUILDS.MEMBERS.LIST_GUILD_MEMBERS.replace(
                            "{guild.id}",
                            id
                    ) + "?limit=" + limit + (after == null ? "" : "&after=" + after),
                    discordJar,
                    URLS.GET.GUILDS.MEMBERS.LIST_GUILD_MEMBERS,
                    RequestMethod.GET
            ).invoke().arr();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }

        List<Member> members = new ArrayList<>();
        for (Object obj : arr) {
            members.add(Member.decompile((JSONObject) obj, discordJar, id, this));
        }

        return members;
    }

    public List<Member> getMembers() {
        JSONArray arr = null;
        try {
            arr = new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.GET.GUILDS.MEMBERS.LIST_GUILD_MEMBERS.replace(
                            "{guild.id}",
                            id
                    ),
                    discordJar,
                    URLS.GET.GUILDS.MEMBERS.LIST_GUILD_MEMBERS,
                    RequestMethod.GET
            ).invoke().arr();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }

        List<Member> members = new ArrayList<>();
        for (Object obj : arr) {
            members.add(Member.decompile((JSONObject) obj, discordJar, id, this));
        }

        return members;
    }

    /**
     * Lists the guild's custom emojis.
     * @return A list of the guild emojis.
     */
    public List<Emoji> getEmojis() {
        List<Emoji> emojis = new ArrayList<>();

        try {
            new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.GET.GUILDS.EMOJIS.GUILD_EMOJIS.replace("{guild.id}", id),
                    discordJar,
                    URLS.GET.GUILDS.EMOJIS.GUILD_EMOJIS,
                    RequestMethod.GET
            ).invoke().arr().forEach((object) -> emojis.add(Emoji.decompile((JSONObject) object, discordJar)));
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }

        return emojis;
    }

    /**
     * Gets a guild emoji by its id.
     * @param emojiId The id of the emoji to get.
     * @return The emoji if it exists. Returns {@code null} if it does not exist.
     */
    public Emoji getEmojiById(@NotNull String emojiId) {
        try {
            return Emoji.decompile(
                    new DiscordRequest(
                            new JSONObject(),
                            new HashMap<>(),
                            URLS.GET.GUILDS.EMOJIS.GET_GUILD_EMOJI.replace("{guild.id}", this.id).replace("{emoji.id}", emojiId),
                            discordJar,
                            URLS.GET.GUILDS.GET_GUILD,
                            RequestMethod.GET
                    ).invoke().body(),
                    discordJar
            );
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * Gets a guild emoji by its id.
     * @param emojiId The id of the emoji to get.
     * @return The emoji if it exists. Returns {@code null} if it does not exist.
     */
    public @NotNull Emoji getEmojiById(long emojiId) {
        return getEmojiById(String.valueOf(emojiId));
    }

    /**
     * Returns a list of {@link com.seailz.discordjar.model.channel.GuildChannel GuildChannels} that the guild has.
     * Does not include threads.
     */
    @NotNull
    public List<GuildChannel> getChannels() {
        List<GuildChannel> channels = new ArrayList<>();
        DiscordResponse req = null;
        try {
            req = new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.GET.GUILDS.CHANNELS.GET_GUILD_CHANNELS.replace("{guild.id}", id),
                    discordJar,
                    URLS.GET.GUILDS.CHANNELS.GET_GUILD_CHANNELS,
                    RequestMethod.GET
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            return new ArrayList<>();
        }
        JSONArray res = req.arr();
        if (res == null) {
            Logger.getLogger("DiscordJar").warning("Failed to get channels for guild " + req.code());
            return new ArrayList<>();
        }
        res.forEach(o -> {
            channels.add(GuildChannel.decompile((JSONObject) o, discordJar));
        });
        return channels;
    }

    /**
     * Retrieves all the roles of a guild.
     * This method uses caching, the cache will reset every 1 minute.
     *
     * @return
     */
    public List<Role> roles() {
        if (roleCache != null && !roleCache.isEmpty()) {
            List<Role> roles = new ArrayList<>();
            roleCache.get().getJSONArray("data").forEach(
                    o -> roles.add(Role.decompile((JSONObject) o))
            );
            return roles;
        }

        List<Role> roles = new ArrayList<>();
        DiscordRequest req = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.GUILDS.ROLES.GET_GUILD_ROLES.replace("{guild.id}", id),
                discordJar,
                URLS.GET.GUILDS.ROLES.GET_GUILD_ROLES,
                RequestMethod.GET
        );
        JSONArray res;
        DiscordResponse response = null;
        try {
            response = req.invoke();
               res = response.arr();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
        if (res == null) {
            System.out.println(response.code() + " " + (response.body() == null ? "null" : response.body().toString()));
        }

        res.forEach(o -> roles.add(Role.decompile((JSONObject) o)));

        if (roleCache != null) {
            roleCache.update(new JSONObject().put("data", res));
            roleCache.resetSingle(60000, "roles");
        }
        return roles;
    }

    /**
     * Used to request a list of all members in a guild.
     * This method can take a <b>LONG</b> time to complete, so it is not recommended to use this often.
     * <p>
     * If you don't have the <b>GUILD_MEMBERS</b> intent enabled, or the guild is over 75k members,
     * <br>this will only send members who are in voice, plus the member for you (the bot user).
     * <p>
     * If a guild has over a certain threshold of members, this will only send members wo are online,
     * <br>have a role, have a nickname, or are in a voice channel, and if it has under the threshold,
     * <br>it will send all members.
     * <p>
     * Limitations put in place by Discord for this method:
     * <ul>
     *     <li><b>GUILD_PRESENCES</b> intent is required to set presences to true, otherwise it will always be false.</li>
     *     <li><b>GUILD_MEMBERS</b> intent is required to request the entire member list.</li>
     *     <li>You will be limited to requesting 1 <b>guild</b> per request.</li>
     *     <li>Requesting a prefix (query parameter) will return a maximum of 100 members.</li>
     *     <li>Requesting user ids will continue to be limited to returning 100 members.</li>
     *  </ul>
     *
     * This method is done over the Gateway.
     * @return A {@link CompletableFuture<List>} of {@link Member Members}, but first a {@link com.seailz.discordjar.action.guild.members.RequestGuildMembersAction RequestGuildMembersAction} is returned,
     * for you to set the parameters of the request.
     */
    public RequestGuildMembersAction requestAllMembers() {
        return new RequestGuildMembersAction(id, discordJar);
    }

    /**
     * Returns the <b>@everyone</b> {@link Role role} of the guild, or if
     * <br>nothing can be found, returns null.
     * <p>
     * Discord assigns the {@link Guild guild} id to the <b>@everyone</b> {@link Role role} id,
     * <br>so this method loops through all the roles of the guild and checks if the id matches
     * <br>using Java Streams.
     *
     * @return {@link Role role} or <b>null</b>
     */
    @Nullable
    public Role getEveryoneRole() {
        return roles().stream().filter(r -> r.id().equals(id())).findFirst().orElse(null);
    }

    public CreateGuildChannelAction createChannel(String name, ChannelType type) {
        return new CreateGuildChannelAction(name, type, this, discordJar);
    }

    /**
     * Returns the invite objects for this guild.
     * <br>This method requires the <b>MANAGE_GUILD</b> permission.
     * @return A list of {@link Invite invites} for this guild.
     */
    public List<InviteMetadata> getInvites() {
        List<InviteMetadata> invites = new ArrayList<>();
        DiscordRequest req = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.GUILDS.GET_GUILD_INVITES.replace("{guild.id}", id),
                discordJar,
                URLS.GET.GUILDS.GET_GUILD_INVITES,
                RequestMethod.GET
        );
        JSONArray res;
        try {
            res = req.invoke().arr();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
        res.forEach(o -> invites.add(InviteMetadataImpl.decompile((JSONObject) o, discordJar)));
        return invites;
    }


    @Override
    public StringFormatter formatter() {
        return new StringFormatter("icons/", id, iconHash());
    }

    /**
     * Deletes the guild. The application must own the guild in order to perform this action.
     * <p/>
     * <b>This action is irreversible!</b>
     */
    public void delete() throws IllegalAccessException {
        DiscordResponse response;
        try {
            response = new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.DELETE.GUILD.DELETE_GUILD.replace("{guild.id}", id),
                    discordJar,
                    URLS.DELETE.GUILD.DELETE_GUILD,
                    RequestMethod.DELETE
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
        if (response.code() != 200) throw new RuntimeException("An error occurred when deleting the guild. Make sure the application owns the guild before doing this.");
    }

    /**
     * Deletes a role from the guild.
     * This requires your application to have the {@code MANAGE_ROLES} permission.
     * @param role The role to delete.
     */
    public void deleteRole(Role role) {
        try {
            new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.DELETE.GUILD.ROLES.replace("{guild.id}", id).replace("{role.id}", role.id()),
                    discordJar,
                    URLS.DELETE.GUILD.ROLES,
                    RequestMethod.DELETE
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * Gets a role in the guild by its id if it exists.
     * @param id The id of the role to find.
     * @return The role if it exists, {@code null} if it does not.
     */
    public Role getRoleById(long id) {
        return getRoleById(String.valueOf(id));
    }

    /**
     * Gets a role in the guild by its id if it exists.
     * @param id The id of the role to find.
     * @return The role if it exists, {@code null} if it does not.
     */
    public Role getRoleById(String id) {
        for (Role r : roles()) {
            if (r.id().equals(id)) return r;
        }
        return null;
    }

    /**
     * Gets a list of roles from the guild with the given name.
     * @param name A name to find.
     * @return A list of roles with the given name, which may be empty.
     */
    public List<Role> getRolesByName(String name) {
        List<Role> roles = new ArrayList<>();
        roles().forEach(r -> {if (r.name().equals(name)) roles.add(r);});
        return roles;
    }

    /**
     * Modifies the MFA level of a Guild. This requires Guild ownership.
     * @param level The new MFA level to set.
     */
    public void modifyMFALevel(MFALevel level) {
        try {
            new DiscordRequest(
                    new JSONObject("level", String.valueOf(level.getCode())),
                    new HashMap<>(),
                    URLS.POST.GUILDS.UPDATE_MFA.replace("{guild.id}", id),
                    discordJar,
                    URLS.POST.GUILDS.UPDATE_MFA,
                    RequestMethod.POST
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * Gets a list of all currently banned users on the guild. Requires the {@code BAN_MEMBERS} permission.
     * @return Current guild bans
     */
    public List<GuildBan> getBans() {
        DiscordResponse response;
        try {
            response = new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.GET.GUILDS.BANS.replace("{guild.id}", id),
                    discordJar,
                    URLS.GET.GUILDS.BANS,
                    RequestMethod.GET
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }

        List<GuildBan> bans = new ArrayList<>();
        response.arr().forEach((object) -> bans.add(GuildBan.decompile((JSONObject) object, discordJar)));

        return bans;
    }

    /**
     * Gets a ban on a user in the guild. Returns {@code null} if no ban could be found. Requires the {@code BAN_MEMBERS} permission.
     * @param userId The id of the banned user
     * @return The ban on the user, if applicable
     */
    public GuildBan getBan(String userId) {
        DiscordResponse response;
        try {
            response = new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.GET.GUILDS.USER_BAN.replace("{guild.id}", id).replace("{user.id}", userId),
                    discordJar,
                    URLS.GET.GUILDS.USER_BAN,
                    RequestMethod.GET
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
        return GuildBan.decompile(response.body(), discordJar);
    }

    /**
     * Gets a ban on a user in the guild. Returns {@code null} if no ban could be found. Requires the {@code BAN_MEMBERS} permission.
     * @param userId The id of the banned user
     * @return The ban on the user, if applicable
     */
    public GuildBan getBan(long userId) {
        return getBan(String.valueOf(userId));
    }

    /**
     * Bans a user from the guild. Requires the {@code BAN_MEMBERS} permission.
     * @param userId The id of the user to ban
     */
    public void banUser(String userId) {
        try {
            new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.PUT.GUILD.BAN_USER.replace("{guild.id}", id).replace("{user.id}", userId),
                    discordJar,
                    URLS.PUT.GUILD.BAN_USER,
                    RequestMethod.PUT
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * Bans a user from the guild. Requires the {@code BAN_MEMBERS} permission.
     * @param userId The id of the user to ban
     */
    public void banUser(long userId) {
        banUser(String.valueOf(userId));
    }

    /**
     * Unbans a user from the guild. Requires the {@code BAN_MEMBERS} permission.
     * @param userId The id of the user to unban
     */
    public void unbanUser(String userId) {
        try {
            new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.PUT.GUILD.BAN_USER.replace("{guild.id}", id).replace("{user.id}", userId),
                    discordJar,
                    URLS.PUT.GUILD.BAN_USER,
                    RequestMethod.PUT
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * Unban a user from the guild. Requires the {@code BAN_MEMBERS} permission.
     * @param userId The id of the user to unban
     */
    public void unbanUser(long userId) {
        unbanUser(String.valueOf(userId));
    }

    /**
     * Finds how many members would be pruned from the guild in the case of a prune. This requires the {@code KICK_MEMBERS} permission.
     * </p>
     * By default, this tests for 7 days.
     * @return The number of members that would be pruned in the prune operation.
     */
    public int getPruneCount() {
        return getPruneCount(7);
    }

    /**
     * Finds how many members would be pruned from the guild in the case of a prune. This requires the {@code KICK_MEMBERS} permission.
     * @param days The amount of days to check users against, from 1-30.
     * @return The number of members that would be pruned in the prune operation.
     */
    public int getPruneCount(int days) {
        if (!Checker.inRange(1, 30, days, () -> Logger.getLogger("[DISCORD.JAR]").severe("Days cannot be outside of 1-30!"))) return 0;

        DiscordRequest req = new DiscordRequest(
                new JSONObject()
                        .put("days", days),
                new HashMap<>(),
                URLS.GET.GUILDS.PRUNE.replace("{guild.id}", id),
                discordJar,
                URLS.GET.GUILDS.PRUNE,
                RequestMethod.GET
        );
        try {
            return req.invoke().body().getInt("pruned");
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * Begins a prune operation on the guild. This requires the {@code KICK_MEMBERS} permission.
     * @param days The amount of days to check users against, from 1-30.
     * @return The amount of pruned users
     */
    public int prune(int days) {
        if (!Checker.inRange(1, 30, days, () -> {throw new RuntimeException("Days cannot be outside 1-30!");})) return 0;

        try {
            DiscordResponse response = new DiscordRequest(
                    new JSONObject()
                            .put("days", days),
                    new HashMap<>(),
                    URLS.POST.GUILDS.PRUNE.replace("{guild.id}", id),
                    discordJar,
                    URLS.POST.GUILDS.PRUNE,
                    RequestMethod.POST
            ).invoke();
            return response.body().getInt("pruned");
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * Finds how many members would be pruned from the guild in the case of a prune. This requires the {@code KICK_MEMBERS} permission.
     * @param days The amount of days to check users against, from 1-30.
     * @param includedRoles A list of roles to include in the count.
     * @return The number of members that would be pruned in the prune operation.
     */
    public int getPruneCount(int days, List<Role> includedRoles) {
        if (!Checker.inRange(1, 30, days, () -> {throw new RuntimeException("Days cannot be outisde 1-30!");})) return 0;

        StringBuilder commaDelimitedSnowflakesString = new StringBuilder("[");
        if (includedRoles.size() == 1) commaDelimitedSnowflakesString.append(includedRoles.get(0).id());
        else {
            for (Role r : includedRoles) {
                commaDelimitedSnowflakesString.append(r.id());
                commaDelimitedSnowflakesString.append(",");
            }
        }
        commaDelimitedSnowflakesString.append("]");

        JSONArray snowflakes = new JSONArray();
        roles.forEach(r -> snowflakes.put(r.id()));

        try {
            DiscordResponse req = new DiscordRequest(
                    new JSONObject()
                            .put("days", days)
                            .put("include_roles", commaDelimitedSnowflakesString),
                    new HashMap<>(),
                    URLS.GET.GUILDS.PRUNE.replace("{guild.id}", id),
                    discordJar,
                    URLS.GET.GUILDS.PRUNE,
                    RequestMethod.GET
            ).invoke();
            return req.body().getInt("pruned");
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * Begins a prune operation on the guild. This requires the {@code KICK_MEMBERS} permission.
     * @param days The amount of days to check users against, from 1-30.
     */
    public void prune(int days, List<Role> includedRoles) {
        if (!Checker.inRange(1, 30, days, () -> Logger.getLogger("[DISCORD.JAR]").severe("Days cannot be outside 1-30!"))) return;

        StringBuilder commaDelimitedSnowflakesString = new StringBuilder();
        if (includedRoles.size() == 1) commaDelimitedSnowflakesString.append(includedRoles.get(0).id());
        else {
            for (Role r : includedRoles) {
                commaDelimitedSnowflakesString.append(r.id());
                commaDelimitedSnowflakesString.append(",");
            }
        }

        try {
            new DiscordRequest(
                    new JSONObject()
                            .put("days", days)
                            .put("include_roles", commaDelimitedSnowflakesString),
                    new HashMap<>(),
                    URLS.POST.GUILDS.PRUNE.replace("{guild.id}", id),
                    discordJar,
                    URLS.POST.GUILDS.PRUNE,
                    RequestMethod.POST
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * Returns a list of Guild {@link Member}s whose names or nicknames contain a filter string.
     * @param filter The username filter string.
     * @return A list of Guild {@link Member}s.
     */
    public List<Member> getMembersByName(String filter) {
        return getMembersByName(filter, 1);
    }

    /**
     * Returns a list of Guild {@link Member}s whose names or nicknames contain a filter string.
     * @param filter The username filter string.
     * @param limit The limit of members to get. Must be 1-1000.
     * @return A list of Guild {@link Member}s.
     */
    public List<Member> getMembersByName(String filter, int limit) {
        if (!Checker.inRange(1, 1000, limit, () -> {throw new RuntimeException("Limit must be within 1-1000!");})) return null;
        try {
            List<Member> namedMembers = new ArrayList<>();
            new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.GET.GUILDS.SEARCH_MEMBERS.replace("{guild.id}", id()).replace("{filter}", filter).replace("{limit}", String.valueOf(limit)),
                    discordJar,
                    URLS.GET.GUILDS.SEARCH_MEMBERS,
                    RequestMethod.GET
            ).invoke().arr().forEach((memberObject) -> namedMembers.add(Member.decompile((JSONObject) memberObject, discordJar, id(), this)));
            return namedMembers;
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * Kicks a {@link Member} from the Guild. This requires the {@code KICK_MEMBERS} permission.
     * @param member The member to kick.
     */
    public void kickMember(Member member) {
        try {
            new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.DELETE.GUILD.MEMBER.KICK_MEMBER.replace("{guild.id}", id()).replace("{user.id}", member.user().id()),
                    discordJar,
                    URLS.DELETE.GUILD.MEMBER.KICK_MEMBER,
                    RequestMethod.DELETE
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * Returns all active threads in the guild, including public and private threads.
     * @return A List of active threads.
     */
    public List<Thread> getActiveThreads() {
        try {
            List<Thread> threads = new ArrayList<>();
            new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.GET.GUILDS.GET_ACTIVE_THREADS.replace("{guild.id}", id()),
                    discordJar,
                    URLS.GET.GUILDS.GET_ACTIVE_THREADS,
                    RequestMethod.GET
            ).invoke().body().getJSONArray("threads").forEach((thread) -> {
                Thread decompiledThread = Thread.decompile((JSONObject) thread, discordJar);
                threads.add(decompiledThread);
            });
            return threads;
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * Gets the URL for the PNG image widget for the guild. Requires no permissions or authentication.
     * @return A URL as a String, pointing to the widget image.
     */
    public String getWidgetImageURL() {
        return getWidgetImageURL("shield");
    }

    /**
     * Gets the URL for the PNG image widget for the guild. Requires no permissions or authentication.
     * @param style The style for the widget image. One of {@code shield}, {@code banner1}, {@code banner2}, {@code banner3}, {@code banner4}
     * @return A URL as a String, pointing to the widget image.
     */
    public String getWidgetImageURL(String style) {
        return "https://discord.com/api/guilds/%s/widget.png?style=%s".formatted(id(), style);
    }
    
    /**
     * Returns the onboarding flow for this guild.
     * @return {@link Onboarding}
     * @throws DiscordRequest.DiscordAPIErrorException If the request fails.
     */
    public @NotNull Onboarding getOnboarding() {
        DiscordRequest req = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.GUILDS.GET_GUILD_ONBOARDING.replace("{guild.id}", id),
                discordJar,
                URLS.GET.GUILDS.GET_GUILD_ONBOARDING,
                RequestMethod.GET
        );
        try {
            return Onboarding.decompile(req.invoke().body(), this, discordJar);
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    public @NotNull List<ScheduledEvent> getScheduledEvents(boolean withUserCount) {
        DiscordRequest req = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.GUILDS.SCHEDULED_EVENTS.GET_SCHEDULED_EVENTS.replace("{guild.id}", id) + "?with_user_count=" + withUserCount,
                discordJar,
                URLS.GET.GUILDS.SCHEDULED_EVENTS.GET_SCHEDULED_EVENTS,
                RequestMethod.GET
        );

        try {
            List<ScheduledEvent> events = new ArrayList<>();
            req.invoke().arr().forEach((event) -> events.add(ScheduledEvent.decompile((JSONObject) event, discordJar)));
            return events;
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    public @NotNull CreateScheduledEventAction createScheduledEvent(String name, ScheduledEvent.PrivacyLevel privacyLevel, DateTime scheduledStartTime, ScheduledEvent.EntityType entityType) {
        return new CreateScheduledEventAction(name, privacyLevel, scheduledStartTime, entityType, id, discordJar);
    }

    public @NotNull List<ScheduledEvent> getScheduledEvents() {
        return getScheduledEvents(false);
    }

    public @Nullable ScheduledEvent getScheduledEvent(String id, boolean withUserCount) {
        DiscordRequest req = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.GUILDS.SCHEDULED_EVENTS.GET_SCHEDULED_EVENT.replace("{guild.id}", id).replace("{guild_scheduled_event.id", id) + "?with_user_count=" + withUserCount,
                discordJar,
                URLS.GET.GUILDS.SCHEDULED_EVENTS.GET_SCHEDULED_EVENT,
                RequestMethod.GET
        );

        try {
            return ScheduledEvent.decompile(req.invoke().body(), discordJar);
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    public @Nullable ScheduledEvent getScheduledEvent(String id) {
        return getScheduledEvent(id, false);
    }

    public Response<Void> deleteScheduledEvent(String id) {
        Response<Void> res = new Response<>();
        new java.lang.Thread(() -> {
            try {
                new DiscordRequest(
                        new JSONObject(),
                        new HashMap<>(),
                        URLS.DELETE.GUILD.SCHEDULED_EVENTS.DELETE_SCHEDULED_EVENT.replace("{guild.id}", this.id).replace("{event.id}", id),
                        discordJar,
                        URLS.DELETE.GUILD.SCHEDULED_EVENTS.DELETE_SCHEDULED_EVENT,
                        RequestMethod.DELETE
                ).invoke();
                res.complete(null);
            } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
                res.completeError(new Response.Error(
                        e.getCode(),
                        e.getMessage(),
                        e.getBody()
                ));
            }
        }).start();
        return res;
    }

    public Response<Void> modifyGuildIncidentActions(@NotNull IncidentsData incidentsData) {
        Response<Void> res = new Response<>();
        new java.lang.Thread(() -> {
            try {
                new DiscordRequest(
                        incidentsData.compile(),
                        new HashMap<>(),
                        URLS.PATCH.GUILD.MODIFY_GUILD_INCIDENT_ACTIONS.replace("{guild.id}", this.id),
                        discordJar,
                        URLS.PATCH.GUILD.MODIFY_GUILD_INCIDENT_ACTIONS,
                        RequestMethod.PUT
                ).invoke();
                res.complete(null);
            } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
                res.completeError(new Response.Error(
                        e.getCode(),
                        e.getMessage(),
                        e.getBody()
                ));
            }
        }).start();
        return res;
    }

    public @NotNull ModifyScheduledEventAction modifyScheduledEvent(String id) {
        return new ModifyScheduledEventAction(discordJar, this.id, id);
    }

    // TODO: implement https://discord.com/developers/docs/resources/guild-scheduled-event#get-guild-scheduled-event-users
    public ModifyOnboardingGuild modifyOnboarding() {
        return new ModifyOnboardingGuild(this, discordJar);
    }

    /**
     * Represents the <a href="https://support.discord.com/hc/en-us/articles/11074987197975-Community-Onboarding-FAQ">onboarding</a> flow for a guild.
     * @param guild The guild this onboarding flow is for.
     * @param prompts Prompts shown during onboarding and in customize community.
     * @param defaultChannelIds Channel IDs that members get opted into automatically.
     * @param enabled Whether onboarding is enabled in the guild.
     */
    public record Onboarding(
            Guild guild,
            List<Prompt> prompts,
            List<String> defaultChannelIds,
            boolean enabled,
            Mode mode
    ) implements Compilerable {

        public enum Mode {
            ONBOARDING_DEFAULT(0),
            ONBOARDING_ADVANCED(1),
            UNKNOWN(-1)
            ;

            private int value;

            Mode(int value) {
                this.value = value;
            }

            public int value() {
                return value;
            }

            public static Mode fromValue(int value) {
                for (Mode mode : values()) {
                    if (mode.value == value) return mode;
                }
                return UNKNOWN;
            }
        }

        @NotNull
        @Override
        public JSONObject compile() {
            JSONObject obj = new JSONObject();
            obj.put("enabled", enabled);
            obj.put("default_channel_ids", defaultChannelIds);
            obj.put("prompts", prompts.stream().map(Prompt::compile).collect(Collectors.toList()));
            obj.put("guild_id", guild.id());
            obj.put("mode", mode.value());
            return obj;
        }

        @NotNull
        @Contract("_, _, _ -> new")
        public static Onboarding decompile(@NotNull JSONObject obj, @NotNull Guild guild, @NotNull DiscordJar djar) {
            return new Onboarding(
                    guild,
                    obj.getJSONArray("prompts").toList().stream().map(o -> {
                        HashMap<String, Object> map = (HashMap<String, Object>) o;
                        JSONObject promptObj = new JSONObject(map);
                        return Prompt.decompile(promptObj, djar);
                    }).collect(Collectors.toList()),
                    obj.getJSONArray("default_channel_ids").toList().stream().map(o -> (String) o).collect(Collectors.toList()),
                    obj.getBoolean("enabled"),
                    Mode.fromValue(obj.getInt("mode"))
            );
        }

        /**
         * Represents a prompt shown during onboarding and in customize community.
         */
        public static class Prompt implements Compilerable {
            private String id;
            private Type type;
            private List<Option> options;
            private String title;
            private boolean singleSelect;
            private boolean required;
            private boolean inOnboarding;

            private Prompt(String id, Type type, List<Option> options, String title, boolean singleSelect, boolean required, boolean inOnboarding) {
                this.id = id;
                this.type = type;
                this.options = options;
                this.title = title;
                this.singleSelect = singleSelect;
                this.required = required;
                this.inOnboarding = inOnboarding;
            }

            public Prompt(String id, Type type, List<Option> options, String title) {
                this(id, type, options, title, false, false, false);
            }

            public Prompt(String id, Type type, List<Option> options, String title, boolean inOnboarding) {
                this(id, type, options, title, false, false, inOnboarding);
            }

            public void setInOnboarding(boolean inOnboarding) {
                this.inOnboarding = inOnboarding;
            }

            public void setType(Type type) {
                this.type = type;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setSingleSelect(boolean singleSelect) {
                this.singleSelect = singleSelect;
            }

            public void setOptions(List<Option> options) {
                this.options = options;
            }

            public void setRequired(boolean required) {
                this.required = required;
            }

            @NotNull
            @Override
            public JSONObject compile() {
                JSONObject obj = new JSONObject();
                obj.put("id", id);
                obj.put("type", type.getCode());
                JSONArray options = new JSONArray();
                if (this.options != null) {
                    for (Option option : this.options) {
                        options.put(option.compile());
                    }
                }
                obj.put("options", options);
                obj.put("title", title);
                obj.put("single_select", singleSelect);
                obj.put("required", required);
                obj.put("in_onboarding", inOnboarding);
                return obj;
            }

            @NotNull
            @Contract("_, _ -> new")
            public static Prompt decompile(@NotNull JSONObject obj, @NotNull DiscordJar djar) {
                return new Prompt(
                        obj.getString("id"),
                        Type.fromCode(obj.getInt("type")),
                        obj.getJSONArray("options").toList().stream().map(o -> Option.decompile(new JSONObject((HashMap<String, Object>) o), djar)).collect(Collectors.toList()),
                        obj.getString("title"),
                        obj.getBoolean("single_select"),
                        obj.getBoolean("required"),
                        obj.getBoolean("in_onboarding")
                );
            }

            public enum Type {
                MULTIPLE_CHOICE(0),
                DROPDOWN(1),
                UNKNOWN(-1);

                private final int code;

                Type(int code) {
                    this.code = code;
                }

                public int getCode() {
                    return code;
                }

                public static Type fromCode(int code) {
                    for (Type type : values()) {
                        if (type.code == code) {
                            return type;
                        }
                    }
                    return UNKNOWN;
                }
            }

            /**
             * Represents an option available within a prompt.
             */
            public static class Option implements Compilerable {
                private String id;
                private List<String> channelIds;
                private List<String> roleIds;
                private Emoji emoji;
                private String title;
                private String description;

                protected Option(String id, List<String> channelIds, List<String> roleIds, Emoji emoji, String title, String description) {
                    this.id = id;
                    this.channelIds = channelIds;
                    this.roleIds = roleIds;
                    this.emoji = emoji;
                    this.title = title;
                    this.description = description;
                }

                public Option(List<String> channelIds, List<String> roleIds, Emoji emoji, String title, String description) {
                    this(null, channelIds, roleIds, emoji, title, description);
                }

                public Option(List<String> channelIds, List<String> roleIds, Emoji emoji, String title) {
                    this(null, channelIds, roleIds, emoji, title, null);
                }


                public void setChannelIds(List<String> channelIds) {
                    this.channelIds = channelIds;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public void setEmoji(Emoji emoji) {
                    this.emoji = emoji;
                }

                public void setRoleIds(List<String> roleIds) {
                    this.roleIds = roleIds;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                @NotNull
                @Override
                public JSONObject compile() {
                    JSONObject obj = new JSONObject();
                    obj.put("id", id);
                    obj.put("channel_ids", channelIds);
                    obj.put("role_ids", roleIds);
                    obj.put("emoji", emoji.compile());
                    obj.put("title", title);
                    obj.put("description", description);
                    return obj;
                }

                @NotNull
                @Contract("_, _ -> new")
                public static Option decompile(@NotNull JSONObject obj, DiscordJar discordJar) {
                    return new Option(
                            obj.has("id") ? obj.getString("id") : null,
                            obj.getJSONArray("channel_ids").toList().stream().map(o -> (String) o).collect(Collectors.toList()),
                            obj.getJSONArray("role_ids").toList().stream().map(o -> (String) o).collect(Collectors.toList()),
                            Emoji.decompile(obj.getJSONObject("emoji"), discordJar),
                            obj.getString("title"),
                            obj.getString("description")
                    );
                }
            }
        }
    }
}
