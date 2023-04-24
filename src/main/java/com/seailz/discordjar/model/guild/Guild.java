package com.seailz.discordjar.model.guild;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.action.automod.AutomodRuleCreateAction;
import com.seailz.discordjar.action.automod.AutomodRuleModifyAction;
import com.seailz.discordjar.action.guild.channel.CreateGuildChannelAction;
import com.seailz.discordjar.action.guild.members.RequestGuildMembersAction;
import com.seailz.discordjar.action.sticker.ModifyStickerAction;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.automod.AutomodRule;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.model.channel.GuildChannel;
import com.seailz.discordjar.model.channel.MessagingChannel;
import com.seailz.discordjar.model.channel.VoiceChannel;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.emoji.Emoji;
import com.seailz.discordjar.model.emoji.sticker.Sticker;
import com.seailz.discordjar.model.guild.filter.ExplicitContentFilterLevel;
import com.seailz.discordjar.model.guild.mfa.MFALevel;
import com.seailz.discordjar.model.guild.notification.DefaultMessageNotificationLevel;
import com.seailz.discordjar.model.guild.premium.PremiumTier;
import com.seailz.discordjar.model.guild.verification.VerificationLevel;
import com.seailz.discordjar.model.guild.welcome.WelcomeScreen;
import com.seailz.discordjar.model.invite.Invite;
import com.seailz.discordjar.model.invite.internal.InviteImpl;
import com.seailz.discordjar.model.role.Role;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.*;
import com.seailz.discordjar.cache.JsonCache;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
 *
 * @param id                              The id of the guild
 * @param name                            The name of the guild
 * @param icon                            The icon hash of the guild
 * @param iconHash                        The icon hash of the guild (included with template object)
 * @param splash                          The splash hash of the guild
 * @param discoverySplash                 The discovery splash hash of the guild
 * @param isOwner                         Whether the user is the owner of the guild
 * @param owner                           The owner of the guild
 * @param permissions                     The total permissions of the user in the guild (excludes overwrites)
 * @param afkChannel                      The afk channel of the guild
 * @param afkTimeout                      The afk timeout of the guild
 * @param isWidgetEnabled                 Whether the widget is enabled for the guild
 * @param widgetChannel                   The widget channel of the guild
 * @param verificationLevel               The verification level of the guild
 * @param defaultMessageNotificationLevel The default message notification level of the guild
 * @param explicitContentFilterLevel      The explicit content filter level of the guild
 * @param roles                           The roles of the guild
 * @param emojis                          The emojis of the guild
 * @param features                        The features of the guild
 * @param mfaLevel                        The mfa level of the guild
 * @param applicationId                   The application id of the guild
 * @param systemChannel                   The system channel of the guild
 * @param maxPresences                    The maximum presences of the guild
 * @param maxMembers                      The maximum members of the guild
 * @param vanityUrlCode                   The vanity url code of the guild
 * @param description                     The description of the guild
 * @param banner                          The banner hash of the guild
 * @param premiumTier                     The premium tier of the guild
 * @param premiumSubscriptionCount        The premium subscription count of the guild
 * @param preferredLocale                 The preferred locale of the guild
 * @param publicUpdatesChannel            The public updates channel of the guild
 * @param maxVideoChannelUsers            The maximum video channel users of the guild
 * @param approximateMemberCount          The approximate member count of the guild
 * @param approximatePresenceCount        The approximate presence count of the guild
 * @param welcomeScreen                   The welcome screen of the guild
 * @param stickers                        The stickers of the guild
 * @param premiumProgressBarEnabled       Whether the premium progress bar is enabled for the guild
 */
public record Guild(
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
        Channel widgetChannel,
        VerificationLevel verificationLevel,
        DefaultMessageNotificationLevel defaultMessageNotificationLevel,
        ExplicitContentFilterLevel explicitContentFilterLevel,
        List<Role> roles,
        List<Emoji> emojis,
        EnumSet<GuildFeature> features,
        MFALevel mfaLevel,
        String applicationId,
        Channel systemChannel,
        int maxPresences,
        int maxMembers,
        String vanityUrlCode,
        String description,
        String banner,
        PremiumTier premiumTier,
        int premiumSubscriptionCount,
        String preferredLocale,
        Channel publicUpdatesChannel,
        int maxVideoChannelUsers,
        int maxStageVideoChannelUsers,
        int approximateMemberCount,
        int approximatePresenceCount,
        WelcomeScreen welcomeScreen,
        List<Sticker> stickers,
        boolean premiumProgressBarEnabled,
        DiscordJar discordJar,
        JsonCache roleCache
) implements Compilerable, Snowflake, CDNAble {


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
                .put("system_channel_id", systemChannel.id())
                .put("max_presences", maxPresences)
                .put("max_members", maxMembers)
                .put("vanity_url_code", vanityUrlCode)
                .put("description", description)
                .put("banner", banner)
                .put("premium_tier", premiumTier.getCode())
                .put("premium_subscription_count", premiumSubscriptionCount)
                .put("preferred_locale", preferredLocale)
                .put("public_updates_channel_id", publicUpdatesChannel.id())
                .put("max_video_channel_users", maxVideoChannelUsers)
                .put("max_stage_video_channel_users", maxStageVideoChannelUsers)
                .put("approximate_member_count", approximateMemberCount)
                .put("approximate_presence_count", approximatePresenceCount)
                .put("welcome_screen", welcomeScreen)
                .put("stickers", stickers)
                .put("premium_progress_bar_enabled", premiumProgressBarEnabled);
    }

    @NotNull
    public static Guild decompile(JSONObject obj, DiscordJar discordJar) {
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
        Channel widgetChannel;
        VerificationLevel verificationLevel;
        DefaultMessageNotificationLevel defaultMessageNotificationLevel;
        ExplicitContentFilterLevel explicitContentFilterLevel;
        List<Role> roles;
        List<Emoji> emojis;
        EnumSet<GuildFeature> features;
        MFALevel mfaLevel;
        String applicationId;
        Channel systemChannel;
        int maxPresences;
        int maxMembers;
        String vanityUrlCode;
        String description;
        String banner;
        PremiumTier premiumTier;
        int premiumSubscriptionCount;
        String preferredLocale;
        Channel publicUpdatesChannel;
        int maxVideoChannelUsers;
        int maxStageVideoChannelUsers = 0;
        int approximateMemberCount;
        int approximatePresenceCount;
        WelcomeScreen welcomeScreen;
        List<Sticker> stickers;
        boolean premiumProgressBarEnabled;

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
            widgetChannel = discordJar.getChannelById(obj.getString("widget_channel_id"));
        } catch (JSONException e) {
            widgetChannel = null;
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
            systemChannel = discordJar.getChannelById(obj.getString("system_channel_id"));
        } catch (IllegalArgumentException | JSONException e) {
            systemChannel = null;
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
            publicUpdatesChannel =
                    discordJar.getChannelById(obj.getString("public_updates_channel_id"));
        } catch (Exception e) {
            publicUpdatesChannel = null;
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
                widgetChannel,
                verificationLevel,
                defaultMessageNotificationLevel,
                explicitContentFilterLevel,
                roles,
                emojis,
                features,
                mfaLevel,
                applicationId,
                systemChannel,
                maxPresences,
                maxMembers,
                vanityUrlCode,
                description,
                banner,
                premiumTier,
                premiumSubscriptionCount,
                preferredLocale,
                publicUpdatesChannel,
                maxVideoChannelUsers,
                maxStageVideoChannelUsers,
                approximateMemberCount,
                approximatePresenceCount,
                welcomeScreen,
                stickers,
                premiumProgressBarEnabled,
                discordJar,
                JsonCache.newc(new DiscordRequest(
                        new JSONObject(),
                        new HashMap<>(),
                        URLS.GET.GUILDS.ROLES.GET_GUILD_ROLES.replace("{guild.id}", id),
                        discordJar,
                        URLS.GET.GUILDS.ROLES.GET_GUILD_ROLES,
                        RequestMethod.GET
                ))
        );
        g.roleCache.reset(60000);
        return g;
    }

    /**
     * Leaves a guild
     */
    public void leave() throws DiscordRequest.UnhandledDiscordAPIErrorException {

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
    }

    /**
     * Lists the stickers in the guild
     */
    public List<Sticker> getStickers() throws DiscordRequest.UnhandledDiscordAPIErrorException {
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
    }

    /**
     * Gets a sticker by id
     *
     * @param stickerId The sticker id
     */
    public Sticker getStickerById(String stickerId) throws DiscordRequest.UnhandledDiscordAPIErrorException {
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
    public void deleteSticker(String stickerId) throws DiscordRequest.UnhandledDiscordAPIErrorException {
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
    }

    /**
     * Returns a list of {@link AutomodRule automod rules} that the guild has
     */
    @NotNull
    public List<AutomodRule> getAutomodRules() throws DiscordRequest.UnhandledDiscordAPIErrorException {
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
    }

    @NotNull
    @Contract("_ -> new")
    public AutomodRule getAutomodRuleById(String id) throws DiscordRequest.UnhandledDiscordAPIErrorException {
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
    }

    @NotNull
    @Contract("_ -> new")
    public AutomodRule getAutomodRuleById(long id) throws DiscordRequest.UnhandledDiscordAPIErrorException {
        return getAutomodRuleById(Long.toString(id));
    }

    public AutomodRuleCreateAction createAutomodRule(String name, AutomodRule.EventType eventType, AutomodRule.TriggerType triggerType, List<AutomodRule.Action> actions) {
        return new AutomodRuleCreateAction(name, eventType, triggerType, actions, this, discordJar);
    }

    public void deleteAutoModRule(String id) throws DiscordRequest.UnhandledDiscordAPIErrorException {
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
    }

    public AutomodRuleModifyAction modifyAutomodRule(String id) {
        return new AutomodRuleModifyAction(id, this, discordJar);
    }

    public Member getMemberById(String id) throws DiscordRequest.UnhandledDiscordAPIErrorException {
        DiscordResponse req = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.GUILDS.MEMBERS.GET_GUILD_MEMBER.replace(
                        "{guild.id}",
                        this.id
                ).replace(
                        "{user.id}",
                        id
                ),
                discordJar,
                URLS.GET.GUILDS.MEMBERS.GET_GUILD_MEMBER,
                RequestMethod.GET
        ).invoke();

        if (req.body() == null) return null;
        return Member.decompile(
                req.body(),
                discordJar,
                this.id,
                this
        );
    }

    public List<Member> getMembers(int limit, String after) throws DiscordRequest.UnhandledDiscordAPIErrorException {
        Checker.check(limit <= 0, "Limit must be greater than 0");
        Checker.check(limit > 1000, "Limit must be less than or equal to 1000");
        JSONArray arr = new DiscordRequest(
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

        List<Member> members = new ArrayList<>();
        for (Object obj : arr) {
            members.add(Member.decompile((JSONObject) obj, discordJar, id, this));
        }

        return members;
    }


    public List<Member> getMembers() throws DiscordRequest.UnhandledDiscordAPIErrorException {
        JSONArray arr = new DiscordRequest(
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
    public List<Emoji> getEmojis() throws DiscordRequest.UnhandledDiscordAPIErrorException {
        List<Emoji> emojis = new ArrayList<>();

        new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.GUILDS.EMOJIS.GUILD_EMOJIS.replace("{guild.id}", id),
                discordJar,
                URLS.GET.GUILDS.EMOJIS.GUILD_EMOJIS,
                RequestMethod.GET
        ).invoke().arr().forEach((object) -> emojis.add(Emoji.decompile((JSONObject) object, discordJar)));

        return emojis;
    }

    /**
     * Gets a guild emoji by its id.
     * @param emojiId The id of the emoji to get.
     * @return The emoji if it exists. Returns {@code null} if it does not exist.
     */
    public Emoji getEmojiById(@NotNull String emojiId) throws DiscordRequest.UnhandledDiscordAPIErrorException {
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
    }

    /**
     * Gets a guild emoji by its id.
     * @param emojiId The id of the emoji to get.
     * @return The emoji if it exists. Returns {@code null} if it does not exist.
     */
    public @NotNull Emoji getEmojiById(long emojiId) throws DiscordRequest.UnhandledDiscordAPIErrorException {
        return getEmojiById(String.valueOf(emojiId));
    }

    /**
     * Returns a list of {@link com.seailz.discordjar.model.channel.GuildChannel GuildChannels} that the guild has.
     * Does not include threads.
     */
    @NotNull
    public List<GuildChannel> getChannels() throws DiscordRequest.UnhandledDiscordAPIErrorException {
        List<GuildChannel> channels = new ArrayList<>();
        DiscordResponse req = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.GUILDS.CHANNELS.GET_GUILD_CHANNELS.replace("{guild.id}", id),
                discordJar,
                URLS.GET.GUILDS.CHANNELS.GET_GUILD_CHANNELS,
                RequestMethod.GET
        ).invoke();
        JSONArray res = req.arr();
        if (res == null) {
            Logger.getLogger("DiscordJar").warning("Failed to get channels for guild " + req.code());
            return new ArrayList<>();
        }
        res.forEach(o -> {
            try {
                channels.add(GuildChannel.decompile((JSONObject) o, discordJar));
            } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
                throw new RuntimeException(e);
            }
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
        try {
            res = req.invoke().arr();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new RuntimeException(e);
        }
        res.forEach(o -> roles.add(Role.decompile((JSONObject) o)));

        if (roleCache != null) {
            roleCache.update(new JSONObject().put("data", res));
        }
        return roles;
    }

    /**
     * Used to request a list of all members in a guild.
     * This method can take a <b>LONG</b> time to complete, so it is not recommended to use this often.
     * <p>
     * If you don't have the <b>GUILD_PRESENCES</b> intent enabled, or the guild is over 75k members,
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
    public List<Invite> getInvites() throws DiscordRequest.UnhandledDiscordAPIErrorException {
        List<Invite> invites = new ArrayList<>();
        DiscordRequest req = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.GUILDS.GET_GUILD_INVITES.replace("{guild.id}", id),
                discordJar,
                URLS.GET.GUILDS.GET_GUILD_INVITES,
                RequestMethod.GET
        );
        JSONArray res;
        res = req.invoke().arr();
        res.forEach(o -> invites.add(InviteImpl.decompile((JSONObject) o, discordJar)));
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
            throw new RuntimeException(e);
        }
        if (response.code() == 401) throw new IllegalAccessException("(Received 401) Application is not authorized to delete the guild. Make sure the application owns the guild before doing this.");
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
            throw new RuntimeException(e);
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
        if (!isOwner()) {
            Logger.getLogger("DiscordJar").warning("You are attempting to set the MFA Level of a Guild your app does not own. This is not allowed per the Discord API.");
            return;
        }
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
        return GuildBan.decompile(response.body(), discordJar);
    }

    /**
     * Gets a ban on a user in the guild. Returns {@code null} if no ban could be found. Requires the {@code BAN_MEMBERS} permission.
     * @param userId The id of the banned user
     * @return The ban on the user, if applicable
     */
    public GuildBan getBan(long userId) {
        DiscordResponse response;
        try {
            response = new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.GET.GUILDS.USER_BAN.replace("{guild.id}", id).replace("{user.id}", String.valueOf(userId)),
                    discordJar,
                    URLS.GET.GUILDS.USER_BAN,
                    RequestMethod.GET
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new RuntimeException(e);
        }
        return GuildBan.decompile(response.body(), discordJar);
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
            throw new RuntimeException(e);
        }
    }

    /**
     * Bans a user from the guild. Requires the {@code BAN_MEMBERS} permission.
     * @param userId The id of the user to ban
     */
    public void banUser(long userId) {
        try {
            new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.PUT.GUILD.BAN_USER.replace("{guild.id}", id).replace("{user.id}", String.valueOf(userId)),
                    discordJar,
                    URLS.PUT.GUILD.BAN_USER,
                    RequestMethod.PUT
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new RuntimeException(e);
        }
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
            throw new RuntimeException(e);
        }
    }

    /**
     * Unban a user from the guild. Requires the {@code BAN_MEMBERS} permission.
     * @param userId The id of the user to unban
     */
    public void unbanUser(long userId) {
        try {
            new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.PUT.GUILD.BAN_USER.replace("{guild.id}", id).replace("{user.id}", String.valueOf(userId)),
                    discordJar,
                    URLS.PUT.GUILD.BAN_USER,
                    RequestMethod.DELETE
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new RuntimeException(e);
        }
    }

    private GuildChannel getChannelById(String channelId) {
        try {
            for (GuildChannel c:getChannels()) {
                if (c.id().equals(channelId)) return c;
            }
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public MessagingChannel getTextChannelById(long channelId) {
        return getTextChannelById(String.valueOf(channelId));
    }

    public MessagingChannel getTextChannelById(String channelId) {
        return getChannelById(channelId).asMessagingChannel();
    }

    public VoiceChannel getVoiceChannelById(long channelId) {
        return getVoiceChannelById(String.valueOf(channelId));
    }

    public VoiceChannel getVoiceChannelById(String channelId) {
        try {
            return VoiceChannel.decompile(getChannelById(channelId).compile(), discordJar);
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new RuntimeException(e);
        }
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
            throw new RuntimeException(e);
        }
    }

    /**
     * Begins a prune operation on the guild. This requires the {@code KICK_MEMBERS} permission.
     * @param days The amount of days to check users against, from 1-30.
     * @return The amount of pruned users
     */
    public int prune(int days) {
        if (!Checker.inRange(1, 30, days, () -> Logger.getLogger("[DISCORD.JAR]").severe("Days cannot be outside 1-30!"))) return 0;

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
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds how many members would be pruned from the guild in the case of a prune. This requires the {@code KICK_MEMBERS} permission.
     * @param days The amount of days to check users against, from 1-30.
     * @param includedRoles A list of roles to include in the count.
     * @return The number of members that would be pruned in the prune operation.
     */
    public int getPruneCount(int days, List<Role> includedRoles) {
        if (!Checker.inRange(1, 30, days, () -> Logger.getLogger("[DISCORD.JAR]").severe("Days cannot be outside of 1-30!"))) return 0;

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

        System.out.println(new JSONObject()
                .put("days", days)
                .put("include_roles", commaDelimitedSnowflakesString).toString());
        System.out.println(snowflakes);

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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Returns the onboarding flow for this guild.
     * @return {@link Onboarding}
     * @throws DiscordRequest.UnhandledDiscordAPIErrorException If the request fails.
     */
    public @NotNull Onboarding getOnboarding() throws DiscordRequest.UnhandledDiscordAPIErrorException {
        DiscordRequest req = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.GUILDS.GET_GUILD_ONBOARDING.replace("{guild.id}", id),
                discordJar,
                URLS.GET.GUILDS.GET_GUILD_ONBOARDING,
                RequestMethod.GET
        );
        return Onboarding.decompile(req.invoke().body(), this, discordJar);
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
            boolean enabled
    ) implements Compilerable {

        @NotNull
        @Override
        public JSONObject compile() {
            JSONObject obj = new JSONObject();
            obj.put("enabled", enabled);
            obj.put("default_channel_ids", defaultChannelIds);
            obj.put("prompts", prompts.stream().map(Prompt::compile).collect(Collectors.toList()));
            obj.put("guild_id", guild.id());
            return obj;
        }

        @NotNull
        @Contract("_, _, _ -> new")
        public static Onboarding decompile(@NotNull JSONObject obj, @NotNull Guild guild, @NotNull DiscordJar djar) {
            return new Onboarding(
                    guild,
                    obj.getJSONArray("prompts").toList().stream().map(o -> Prompt.decompile((JSONObject) o, djar)).collect(Collectors.toList()),
                    obj.getJSONArray("default_channel_ids").toList().stream().map(o -> (String) o).collect(Collectors.toList()),
                    obj.getBoolean("enabled")
            );
        }

        /**
         * Represents a prompt shown during onboarding and in customize community.
         *
         * @param id           ID of the prompt
         * @param type         Type of prompt
         * @param options      Options available within the prompt
         * @param title        Title of the prompt
         * @param singleSelect Indicates whether users are limited to selecting one option for the prompt
         * @param required     Indicates whether the prompt is required before a user completes the onboarding flow
         * @param inOnboarding Indicates whether the prompt is present in the onboarding flow. If `false`, the prompt will only appear
         *                     in the Channels & Roles tab.
         */
        public record Prompt(
                String id,
                Type type,
                List<Option> options,
                String title,
                boolean singleSelect,
                boolean required,
                boolean inOnboarding
        ) implements Compilerable {

            @NotNull
            @Override
            public JSONObject compile() {
                JSONObject obj = new JSONObject();
                obj.put("id", id);
                obj.put("type", type.getCode());
                obj.put("options", options.stream().map(Option::compile).collect(Collectors.toList()));
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
                        obj.getJSONArray("options").toList().stream().map(o -> Option.decompile((JSONObject) o, djar)).collect(Collectors.toList()),
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
             *
             * @param id          ID of the option
             * @param channelIds  IDs for channels a member is added to when the option is selected
             * @param roleIds     IDs for roles assigned to a member when the option is selected
             * @param emoji       Emoji for the option
             * @param title       Title of the option
             * @param description Description of the option. This may be null or an empty string.
             */
            public record Option(
                    String id,
                    List<String> channelIds,
                    List<String> roleIds,
                    Emoji emoji,
                    String title,
                    String description
            ) implements Compilerable {
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
                            obj.getString("id"),
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
