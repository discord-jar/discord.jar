package com.seailz.javadiscordwrapper.model.guild;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.core.Compilerable;
import com.seailz.javadiscordwrapper.model.role.Role;
import com.seailz.javadiscordwrapper.model.user.User;
import com.seailz.javadiscordwrapper.model.channel.Channel;
import com.seailz.javadiscordwrapper.model.emoji.Emoji;
import com.seailz.javadiscordwrapper.model.emoji.sticker.Sticker;
import com.seailz.javadiscordwrapper.model.guild.filter.ExplicitContentFilterLevel;
import com.seailz.javadiscordwrapper.model.guild.mfa.MFALevel;
import com.seailz.javadiscordwrapper.model.guild.notification.DefaultMessageNotificationLevel;
import com.seailz.javadiscordwrapper.model.guild.premium.PremiumTier;
import com.seailz.javadiscordwrapper.model.guild.verification.VerificationLevel;
import com.seailz.javadiscordwrapper.model.guild.welcome.WelcomeScreen;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Represents a guild.
 * @param id The id of the guild
 * @param name The name of the guild
 * @param icon The icon hash of the guild
 * @param iconHash The icon hash of the guild (included with template object)
 * @param splash The splash hash of the guild
 * @param discoverySplash The discovery splash hash of the guild
 * @param isOwner Whether the user is the owner of the guild
 * @param owner The owner of the guild
 * @param permissions The total permissions of the user in the guild (excludes overwrites)
 * @param afkChannel The afk channel of the guild
 * @param afkTimeout The afk timeout of the guild
 * @param isWidgetEnabled Whether the widget is enabled for the guild
 * @param widgetChannel The widget channel of the guild
 * @param verificationLevel The verification level of the guild
 * @param defaultMessageNotificationLevel The default message notification level of the guild
 * @param explicitContentFilterLevel The explicit content filter level of the guild
 * @param roles The roles of the guild
 * @param emojis The emojis of the guild
 * @param features The features of the guild
 * @param mfaLevel The mfa level of the guild
 * @param applicationId The application id of the guild
 * @param systemChannel The system channel of the guild
 * @param maxPresences The maximum presences of the guild
 * @param maxMembers The maximum members of the guild
 * @param vanityUrlCode The vanity url code of the guild
 * @param description The description of the guild
 * @param banner The banner hash of the guild
 * @param premiumTier The premium tier of the guild
 * @param premiumSubscriptionCount The premium subscription count of the guild
 * @param preferredLocale The preferred locale of the guild
 * @param publicUpdatesChannel The public updates channel of the guild
 * @param maxVideoChannelUsers The maximum video channel users of the guild
 * @param approximateMemberCount The approximate member count of the guild
 * @param approximatePresenceCount The approximate presence count of the guild
 * @param welcomeScreen The welcome screen of the guild
 * @param stickers The stickers of the guild
 * @param premiumProgressBarEnabled Whether the premium progress bar is enabled for the guild
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
        boolean premiumProgressBarEnabled
) implements Compilerable {


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
            owner = User.decompile(obj.getJSONObject("owner"));
        } catch (JSONException e) {
            owner = null;
        }

        try {
            permissions = obj.getString("permissions");
        } catch (JSONException e) {
            permissions = null;
        }

        try {
            afkChannel = Channel.decompile(obj.getJSONObject("afk_channel"));
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
                emojis.add(Emoji.decompile(emojisArray.getJSONObject(i)));
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
        } catch (JSONException e) {
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
            publicUpdatesChannel = discordJv.getChannelById(obj.getString("public_updates_channel_id"));
        } catch (JSONException e) {
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
                stickers.add(Sticker.decompile(stickersArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            stickers = null;
        }

        try {
            premiumProgressBarEnabled = obj.getBoolean("premium_progress_bar_enabled");
        } catch (JSONException e) {
            premiumProgressBarEnabled = false;
        }

        return new Guild(
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
                premiumProgressBarEnabled
        );
    }
}
