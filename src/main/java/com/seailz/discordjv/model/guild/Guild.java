package com.seailz.discordjv.model.guild;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.action.automod.AutomodRuleCreateAction;
import com.seailz.discordjv.action.automod.AutomodRuleModifyAction;
import com.seailz.discordjv.action.guild.channel.CreateGuildChannelAction;
import com.seailz.discordjv.action.guild.members.RequestGuildMembersAction;
import com.seailz.discordjv.action.sticker.ModifyStickerAction;
import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.model.automod.AutomodRule;
import com.seailz.discordjv.model.channel.Channel;
import com.seailz.discordjv.model.channel.GuildChannel;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.emoji.Emoji;
import com.seailz.discordjv.model.emoji.sticker.Sticker;
import com.seailz.discordjv.model.guild.filter.ExplicitContentFilterLevel;
import com.seailz.discordjv.model.guild.mfa.MFALevel;
import com.seailz.discordjv.model.guild.notification.DefaultMessageNotificationLevel;
import com.seailz.discordjv.model.guild.premium.PremiumTier;
import com.seailz.discordjv.model.guild.verification.VerificationLevel;
import com.seailz.discordjv.model.guild.welcome.WelcomeScreen;
import com.seailz.discordjv.model.role.Role;
import com.seailz.discordjv.model.user.User;
import com.seailz.discordjv.utils.*;
import com.seailz.discordjv.utils.cache.JsonCache;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
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
        int approximateMemberCount,
        int approximatePresenceCount,
        WelcomeScreen welcomeScreen,
        List<Sticker> stickers,
        boolean premiumProgressBarEnabled,
        DiscordJv discordJv,
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
                .put("approximate_member_count", approximateMemberCount)
                .put("approximate_presence_count", approximatePresenceCount)
                .put("welcome_screen", welcomeScreen)
                .put("stickers", stickers)
                .put("premium_progress_bar_enabled", premiumProgressBarEnabled);
    }

    @NotNull
    public static Guild decompile(JSONObject obj, DiscordJv discordJv) {
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
            owner = User.decompile(obj.getJSONObject("owner"), discordJv);
        } catch (JSONException e) {
            owner = null;
        }

        try {
            permissions = obj.getString("permissions");
        } catch (JSONException e) {
            permissions = null;
        }

        try {
            afkChannel = Channel.decompile(obj.getJSONObject("afk_channel"), discordJv);
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
            widgetChannel = discordJv.getChannelById(obj.getString("widget_channel_id"));
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
                emojis.add(Emoji.decompile(emojisArray.getJSONObject(i), discordJv));
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
            systemChannel = discordJv.getChannelById(obj.getString("system_channel_id"));
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
                    discordJv.getChannelById(obj.getString("public_updates_channel_id"));
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
            approximatePresenceCount = obj.getInt("approximate_presence_count");
        } catch (JSONException e) {
            approximatePresenceCount = 0;
        }

        try {
            welcomeScreen = WelcomeScreen.decompile(obj.getJSONObject("welcome_screen"), discordJv);
        } catch (JSONException e) {
            welcomeScreen = null;
        }

        try {
            JSONArray stickersArray = obj.getJSONArray("stickers");
            stickers = new ArrayList<>();
            for (int i = 0; i < stickersArray.length(); i++) {
                stickers.add(Sticker.decompile(stickersArray.getJSONObject(i), discordJv));
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
                approximateMemberCount,
                approximatePresenceCount,
                welcomeScreen,
                stickers,
                premiumProgressBarEnabled,
                discordJv,
                JsonCache.newc(new DiscordRequest(
                        new JSONObject(),
                        new HashMap<>(),
                        URLS.GET.GUILDS.ROLES.GET_GUILD_ROLES.replace("{guild.id}", id),
                        discordJv,
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
    public void leave() {

        new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.DELETE.GUILD.LEAVE_GUILD.replace(
                        "{guild.id}",
                        id
                ),
                discordJv,
                URLS.DELETE.GUILD.LEAVE_GUILD,
                RequestMethod.DELETE
        ).invoke();
    }

    /**
     * Lists the stickers in the guild
     */
    public List<Sticker> getStickers() {
        return Sticker.decompileList(
                new DiscordRequest(
                        new JSONObject(),
                        new HashMap<>(),
                        URLS.GET.GUILDS.STICKERS.GET_GUILD_STICKERS.replace(
                                "{guild.id}",
                                id
                        ),
                        discordJv,
                        URLS.GET.GUILDS.STICKERS.GET_GUILD_STICKERS,
                        RequestMethod.GET
                ).invoke().arr(),
                discordJv
        );
    }

    /**
     * Gets a sticker by id
     *
     * @param stickerId The sticker id
     */
    public Sticker getStickerById(String stickerId) {
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
                        discordJv,
                        URLS.GET.GUILDS.STICKERS.GET_GUILD_STICKER,
                        RequestMethod.GET
                ).invoke().body(),
                discordJv
        );
    }

    /**
     * Modifies a sticker's properties
     */
    public ModifyStickerAction modifySticker(String stickerId) {
        return new ModifyStickerAction(stickerId, discordJv);
    }

    /**
     * Deletes a sticker
     */
    public void deleteSticker(String stickerId) {
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
                discordJv,
                URLS.DELETE.GUILD.STICKER.DELETE_GUILD_STICKER,
                RequestMethod.DELETE
        ).invoke();
    }

    /**
     * Returns a list of {@link AutomodRule automod rules} that the guild has
     */
    @NotNull
    public List<AutomodRule> getAutomodRules() {
        return AutomodRule.decompileList(
                new DiscordRequest(
                        new JSONObject(),
                        new HashMap<>(),
                        URLS.GET.GUILDS.AUTOMOD.LIST_AUTOMOD_RULES.replace(
                                "{guild.id}",
                                id
                        ),
                        discordJv,
                        URLS.GET.GUILDS.AUTOMOD.LIST_AUTOMOD_RULES,
                        RequestMethod.GET
                ).invoke().arr(),
                discordJv
        );
    }

    @NotNull
    @Contract("_ -> new")
    public AutomodRule getAutomodRuleById(String id) {
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
                        discordJv,
                        URLS.GET.GUILDS.AUTOMOD.GET_AUTOMOD_RULE,
                        RequestMethod.GET
                ).invoke().body(),
                discordJv
        );
    }

    @NotNull
    @Contract("_ -> new")
    public AutomodRule getAutomodRuleById(long id)  {
        return getAutomodRuleById(Long.toString(id));
    }

    public AutomodRuleCreateAction createAutomodRule(String name, AutomodRule.EventType eventType, AutomodRule.TriggerType triggerType, List<AutomodRule.Action> actions) {
        return new AutomodRuleCreateAction(name, eventType, triggerType, actions, this, discordJv);
    }

    public void deleteAutoModRule(String id) {
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
                discordJv,
                URLS.DELETE.GUILD.AUTOMOD.DELETE_AUTOMOD_RULE,
                RequestMethod.DELETE
        ).invoke();
    }

    public AutomodRuleModifyAction modifyAutomodRule(String id) {
        return new AutomodRuleModifyAction(id, this, discordJv);
    }

    public Member getMemberById(String id) {
        return Member.decompile(
                new DiscordRequest(
                        new JSONObject(),
                        new HashMap<>(),
                        URLS.GET.GUILDS.MEMBERS.GET_GUILD_MEMBER.replace(
                                "{guild.id}",
                                this.id
                        ).replace(
                                "{user.id}",
                                id
                        ),
                        discordJv,
                        URLS.GET.GUILDS.MEMBERS.GET_GUILD_MEMBER,
                        RequestMethod.GET
                ).invoke().body(),
                discordJv,
                this.id,
                this
        );
    }

    public List<Member> getMembers(int limit, String after) {
        Checker.check(limit <= 0, "Limit must be greater than 0");
        Checker.check(limit > 1000, "Limit must be less than or equal to 1000");
        JSONArray arr = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.GUILDS.MEMBERS.LIST_GUILD_MEMBERS.replace(
                        "{guild.id}",
                        id
                ) + "?limit=" + limit + (after == null ? "" : "&after=" + after),
                discordJv,
                URLS.GET.GUILDS.MEMBERS.LIST_GUILD_MEMBERS,
                RequestMethod.GET
        ).invoke().arr();

        List<Member> members = new ArrayList<>();
        for (Object obj : arr) {
            members.add(Member.decompile((JSONObject) obj, discordJv, id, this));
        }
        System.out.println(arr);

        return members;
    }


    public List<Member> getMembers() {
        JSONArray arr = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.GUILDS.MEMBERS.LIST_GUILD_MEMBERS.replace(
                        "{guild.id}",
                        id
                ),
                discordJv,
                URLS.GET.GUILDS.MEMBERS.LIST_GUILD_MEMBERS,
                RequestMethod.GET
        ).invoke().arr();

        List<Member> members = new ArrayList<>();
        for (Object obj : arr) {
            members.add(Member.decompile((JSONObject) obj, discordJv, id, this));
        }
        System.out.println(arr);

        return members;
    }

    /**
     * Lists the guild's custom emojis.
     * @return A list of the guild emojis.
     */
    public List<Emoji> getEmojis() {
        List<Emoji> emojis = new ArrayList<>();

        new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.GUILDS.EMOJIS.GUILD_EMOJIS.replace("{guild.id}", id),
                discordJv,
                URLS.GET.GUILDS.EMOJIS.GUILD_EMOJIS,
                RequestMethod.GET
        ).invoke().arr().forEach((object) -> emojis.add(Emoji.decompile((JSONObject) object, discordJv)));

        return emojis;
    }

    /**
     * Gets a guild emoji by its id.
     * @param emojiId The id of the emoji to get.
     * @return The emoji if it exists. Returns {@code null} if it does not exist.
     */
    public Emoji getEmojiById(@NotNull String emojiId) {
        return Emoji.decompile(
                new DiscordRequest(
                        new JSONObject(),
                        new HashMap<>(),
                        URLS.GET.GUILDS.EMOJIS.GET_GUILD_EMOJI.replace("{guild.id}", this.id).replace("{emoji.id}", emojiId),
                        discordJv,
                        URLS.GET.GUILDS.GET_GUILD,
                        RequestMethod.GET
                ).invoke().body(),
                discordJv
        );
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
     * Returns a list of {@link com.seailz.discordjv.model.channel.GuildChannel GuildChannels} that the guild has.
     * Does not include threads.
     */
    @NotNull
    public List<GuildChannel> getChannels() {
        List<GuildChannel> channels = new ArrayList<>();
        JSONArray res = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.GUILDS.CHANNELS.GET_GUILD_CHANNELS.replace("{guild.id}", id),
                discordJv,
                URLS.GET.GUILDS.CHANNELS.GET_GUILD_CHANNELS,
                RequestMethod.GET
        ).invoke().arr();
        res.forEach(o -> channels.add(GuildChannel.decompile((JSONObject) o, discordJv)));
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
                discordJv,
                URLS.GET.GUILDS.ROLES.GET_GUILD_ROLES,
                RequestMethod.GET
        );
        JSONArray res = req.invoke().arr();
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
     *
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
     * @return A {@link CompletableFuture<List>} of {@link Member Members}, but first a {@link com.seailz.discordjv.action.guild.members.RequestGuildMembersAction RequestGuildMembersAction} is returned,
     * for you to set the parameters of the request.
     */
    public RequestGuildMembersAction requestAllMembers() {
        return new RequestGuildMembersAction(id, discordJv);
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
        return new CreateGuildChannelAction(name, type, this, discordJv);
    }


    @Override
    public StringFormatter formatter() {
        return new StringFormatter("icons/", id, iconHash());
    }
}
