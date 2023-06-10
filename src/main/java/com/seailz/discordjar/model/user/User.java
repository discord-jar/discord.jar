package com.seailz.discordjar.model.user;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.channel.DMChannel;
import com.seailz.discordjar.model.resolve.Resolvable;
import com.seailz.discordjar.utils.CDNAble;
import com.seailz.discordjar.utils.Mentionable;
import com.seailz.discordjar.utils.StringFormatter;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import com.seailz.discordjar.utils.flag.BitwiseUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.EnumSet;
import java.util.HashMap;

/**
 * Users in Discord are generally considered the base entity. Users can spawn across the entire platform, be members of guilds, participate in text and voice chat, and much more. Users are separated by a distinction of "bot" vs "normal." Although they are similar, bot users are automated users that are "owned" by another user. Unlike normal users, bot users do not have a limitation on the number of Guilds they can be a part of.
 * <p>
 * Usernames and Nicknames <p>
 * Discord enforces the following restrictions for usernames and nicknames:<p>
 * <p>
 * Names can contain most valid unicode characters. We limit some zero-width and non-rendering characters.<p>
 * Usernames must be between 2 and 32 characters long.<p>
 * Nicknames must be between 1 and 32 characters long.<p>
 * Names are sanitized and trimmed of leading, trailing, and excessive internal whitespace.<p>
 * The following restrictions are additionally enforced for usernames:<p>
 * <p>
 * Usernames cannot contain the following substrings: @, #, :, ```, discord<p>
 * Usernames cannot be: everyone, here<p>
 * There are other rules and restrictions not shared here for the sake of spam and abuse mitigation, but the majority of users won't encounter them. It's important to properly handle all error messages returned by Discord when editing or updating names.
 *
 * @param id             the user's id
 * @param username       the user's username, not unique across the platform
 * @param discriminator  the user's 4-digit discord-tag
 * @param avatarHash         the user's avatar hash
 * @param bot            whether the user is a bot
 * @param system         whether the user is an Official Discord System user (part of the urgent message system)
 * @param mfaEnabled     whether the user has two factor enabled on their account
 * @param locale         the user's chosen language option
 * @param verified       whether the email on this account has been verified
 * @param email          the user's email
 * @param flags          the flags on a user's account
 * @param flagsRaw       the raw flags on a user's account
 * @param premiumType    the type of Nitro subscription on a user's account
 * @param publicFlags    the public flags on a user's account
 * @param publicFlagsRaw the raw public flags on a user's account
 * @param discordJar      the discordJar instance
 * @author Seailz
 * @see <a href="https://discordapp.com/developers/docs/resources/user#user-object">User Object</a>
 * @see <a href="https://discordapp.com/developers/docs/resources/user#user-object-user-structure">User Structure</a>
 * @since 1.0
 */
public record User(
        String id, String username, String discriminator, String avatarHash, boolean bot,
        boolean system, boolean mfaEnabled, String locale, boolean verified, String email,
        EnumSet<UserFlag> flags, int flagsRaw, PremiumType premiumType, EnumSet<UserFlag> publicFlags,
        int publicFlagsRaw, String displayName,
        DiscordJar discordJar
) implements Compilerable, Resolvable, Mentionable, CDNAble {

    /**
     * Converts this User object to a JSONObject
     *
     * @return a JSONObject consisting of the data in this User object
     */
    @Override
    public @NotNull JSONObject compile() {
        JSONObject obj = new JSONObject()
                .put("id", id)
                .put("username", username)
                .put("discriminator", discriminator)
                .put("avatar", avatarHash)
                .put("bot", bot)
                .put("system", system)
                .put("mfa_enabled", mfaEnabled)
                .put("locale", locale)
                .put("verified", verified)
                .put("email", email)
                .put("flags", flags)
                .put("public_flags", publicFlags)
                .put("global_name", displayName);

        if (premiumType == null)
            obj.put("premium_type", JSONObject.NULL);
        else
            obj.put("premium_type", premiumType.getId());
        return obj;
    }

    /**
     * Converts a JSONObject to a User object
     *
     * @param obj the JSONObject to convert
     * @return a User object consisting of the data in the JSONObject
     */
    @Contract("_, _ -> new")
    public static @NotNull User decompile(@NotNull JSONObject obj, DiscordJar discordJar) {
        String id;
        String username;
        String discriminator;
        String avatar;
        boolean bot;
        boolean system;
        boolean mfaEnabled;
        String locale;
        boolean verified;
        String email;
        EnumSet<UserFlag> flags;
        PremiumType premiumType;
        EnumSet<UserFlag> publicFlags;
        int publicFlagsRaw = 0;
        int flagsRaw = 0;
        String displayName;

        try {
            id = obj.getString("id");
        } catch (JSONException e) {
            id = null;
        }

        try {
            username = obj.getString("username");
        } catch (JSONException e) {
            username = null;
        }

        try {
            discriminator = obj.getString("discriminator");
        } catch (JSONException e) {
            discriminator = null;
        }

        try {
            avatar = obj.getString("avatar");
        } catch (JSONException e) {
            avatar = null;
        }

        try {
            bot = obj.getBoolean("bot");
        } catch (JSONException e) {
            bot = false;
        }

        try {
            system = obj.getBoolean("system");
        } catch (JSONException e) {
            system = false;
        }

        try {
            mfaEnabled = obj.getBoolean("mfa_enabled");
        } catch (JSONException e) {
            mfaEnabled = false;
        }

        try {
            locale = obj.getString("locale");
        } catch (JSONException e) {
            locale = null;
        }

        try {
            verified = obj.getBoolean("verified");
        } catch (JSONException e) {
            verified = false;
        }

        try {
            email = obj.getString("email");
        } catch (JSONException e) {
            email = null;
        }

        try {
            flags = new BitwiseUtil<UserFlag>().get(obj.getInt("flags"), UserFlag.class);
            flagsRaw = obj.getInt("flags");
        } catch (JSONException e) {
            flags = null;
        }

        try {
            premiumType = PremiumType.fromId(obj.getInt("premium_type"));
        } catch (JSONException e) {
            premiumType = null;
        }

        try {
            publicFlags = flags = new BitwiseUtil<UserFlag>().get(obj.getInt("public_flags"), UserFlag.class);
            publicFlagsRaw = obj.getInt("public_flags");
        } catch (JSONException e) {
            publicFlags = null;
        }

        if (obj.has("global_name") && !obj.isNull("global_name"))
            displayName = obj.getString("global_name");
        else
            displayName = null;

        return new User(id, username, discriminator, avatar, bot, system, mfaEnabled, locale, verified, email, flags, flagsRaw, premiumType, publicFlags, publicFlagsRaw, displayName, discordJar);
    }

    /**
     * Opens a DM channel with this user. <p>
     * <p>
     * You should not use this endpoint to DM everyone in a server about something.
     * DMs should generally be initiated by a user action.
     * If you open a significant amount of DMs too quickly, your bot may be rate limited or blocked from opening new ones.
     *
     * @return {@link DMChannel} object
     */
    @Nullable
    public DMChannel createDM() {
        JSONObject obj = new JSONObject()
                .put("recipient_id", id);
        DiscordResponse resp = null;
        try {
            resp = new DiscordRequest(
                    obj,
                    new HashMap<>(),
                    URLS.POST.USERS.CREATE_DM,
                    discordJar,
                    URLS.POST.USERS.CREATE_DM,
                    RequestMethod.POST
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }

        return resp != null && resp.body() != null ? DMChannel.decompile(resp.body(), discordJar) : null;
    }

    /**
     * Returns the user's default avatar URL. This is the avatar shown when the user has no custom avatar set.
     */
    public String getDefaultAvatarUrl() {
        return URLS.CDN.DEFAULT_USER_AVATAR.formatted((Long.parseLong(id) >> 22) % 6);
    }

    /**
     * Returns the user's avatar URL. If they do not have a custom avatar set, this will return their default avatar URL.
     */
    public String getEffectiveAvatarUrl() {
        return imageUrl() == null ? getDefaultAvatarUrl() : imageUrl();
    }

    /**
     * Returns the effective name of the user. This is either their username, or their display name (if they have one set).
     */
    public String getEffectiveName() {
        return displayName != null ? displayName : username;
    }

    /**
     * This defines if a user is using the new Pomelo username system (@usernames instead of username#0000).
     * <br>Please see the <a href="https://support.discord.com/hc/en-us/articles/12620128861463-New-Usernames-Display-Names">help center article</a> for more information.
     */
    public boolean isUsingPomelo() {
        return discriminator.equals("0");
    }


    @Override
    public String getMentionablePrefix() {
        return "@";
    }

    @Override
    public StringFormatter formatter() {
        return new StringFormatter("avatars", id, avatarHash());
    }

    @Override
    public String iconHash() {
        return avatarHash;
    }
}
