package com.seailz.discordjar.model.user;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.channel.DMChannel;
import com.seailz.discordjar.model.resolve.Resolvable;
import com.seailz.discordjar.utils.CDNAble;
import com.seailz.discordjar.utils.Mentionable;
import com.seailz.discordjar.utils.StringFormatter;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.image.ImageUtils;
import com.seailz.discordjar.utils.model.DiscordJarProp;
import com.seailz.discordjar.utils.model.JSONProp;
import com.seailz.discordjar.utils.model.Model;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import com.seailz.discordjar.utils.flag.BitwiseUtil;
import com.seailz.discordjar.voice.model.VoiceState;
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
 * <p>
 * id               &nbsp;the user's id<br>
 * username         &nbsp;the user's username, not unique across the platform<br>
 * discriminator    &nbsp;the user's 4-digit discord-tag<br>
 * avatarHash       &nbsp;the user's avatar hash<br>
 * bot              &nbsp;whether the user is a bot<br>
 * system           &nbsp;whether the user is an Official Discord System user (part of the urgent message system)<br>
 * mfaEnabled       &nbsp;whether the user has two factor enabled on their account<br>
 * locale           &nbsp;the user's chosen language option<br>
 * verified         &nbsp;whether the email on this account has been verified<br>
 * email            &nbsp;the user's email<br>
 * flags            &nbsp;the flags on a user's account<br>
 * flagsRaw         &nbsp;the raw flags on a user's account<br>
 * premiumType      &nbsp;the type of Nitro subscription on a user's account<br>
 * publicFlags      &nbsp;the public flags on a user's account<br>
 * publicFlagsRaw   &nbsp;the raw public flags on a user's account<br>
 * avatarDecoration &nbsp;the user's avatar decoration hash<br>
 * discordJar       &nbsp;the discordJar instance<br>
 * @author Seailz
 * @see <a href="https://discordapp.com/developers/docs/resources/user#user-object">User Object</a>
 * @see <a href="https://discordapp.com/developers/docs/resources/user#user-object-user-structure">User Structure</a>
 * @since 1.0
 */
public class User implements Model, Resolvable, Mentionable, CDNAble {
    @JSONProp("id")
    private String id;
    @JSONProp("username")
    private String username;
    @JSONProp("discriminator")
    private String discriminator;
    @JSONProp("avatar")
    private String avatarHash;
    @JSONProp("bot")
    private boolean bot;
    @JSONProp("system")
    private boolean system;
    @JSONProp("mfa_enabled")
    private boolean mfaEnabled;
    @JSONProp("locale")
    private String locale;
    @JSONProp("verified")
    private boolean verified;
    @JSONProp("email")
    private String email;
    @JSONProp("flags")
    private EnumSet<UserFlag> flags;
    @JSONProp("flags")
    private int flagsRaw;
    @JSONProp("premium_type")
    private PremiumType premiumType;
    @JSONProp("public_flags")
    private EnumSet<UserFlag> publicFlags;
    @JSONProp("avatar_decoration")
    private String avatarDecoration;
    @JSONProp("public_flags")
    private int publicFlagsRaw;
    @JSONProp("display_name")
    private String displayName;
    @DiscordJarProp
    private DiscordJar discordJar;

    public User(
            String id,
            String username,
            String discriminator,
            String avatarHash,
            boolean bot,
            boolean system,
            boolean mfaEnabled,
            String locale,
            boolean verified,
            String email,
            EnumSet<UserFlag> flags,
            int flagsRaw,
            PremiumType premiumType,
            EnumSet<UserFlag> publicFlags,
            String avatarDecoration,
            int publicFlagsRaw,
            String displayName,
            DiscordJar discordJar
    ) {
        this.id = id;
        this.username = username;
        this.discriminator = discriminator;
        this.avatarHash = avatarHash;
        this.bot = bot;
        this.system = system;
        this.mfaEnabled = mfaEnabled;
        this.locale = locale;
        this.verified = verified;
        this.email = email;
        this.flags = flags;
        this.flagsRaw = flagsRaw;
        this.premiumType = premiumType;
        this.publicFlags = publicFlags;
        this.avatarDecoration = avatarDecoration;
        this.publicFlagsRaw = publicFlagsRaw;
        this.displayName = displayName;
        this.discordJar = discordJar;
    }

    protected User() {}

    public String id() {
        return id;
    }

    public String username() {
        return username;
    }

    public String discriminator() {
        return discriminator;
    }

    public String avatarHash() {
        return avatarHash;
    }

    public boolean bot() {
        return bot;
    }

    public boolean system() {
        return system;
    }

    public boolean mfaEnabled() {
        return mfaEnabled;
    }

    public String locale() {
        return locale;
    }

    public boolean verified() {
        return verified;
    }

    public String email() {
        return email;
    }

    public EnumSet<UserFlag> flags() {
        return flags;
    }

    public int flagsRaw() {
        return flagsRaw;
    }

    public PremiumType premiumType() {
        return premiumType;
    }

    public EnumSet<UserFlag> publicFlags() {
        return publicFlags;
    }

    public String avatarDecoration() {
        return avatarDecoration;
    }

    public int publicFlagsRaw() {
        return publicFlagsRaw;
    }

    public String displayName() {
        return displayName;
    }

    public DiscordJar discordJar() {
        return discordJar;
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

    public String getAvatarDecorationUrl() {
        return ImageUtils.getUrl(avatarHash, ImageUtils.ImageType.USER_AVATAR_DECORATION, id);
    }

    public VoiceState getVoiceState() {
        if (bot) throw new IllegalArgumentException("Bots can have multiple voice states, so please use Member#getVoiceState() instead.");
        return discordJar.getVoiceStates().stream().filter(vs -> vs.userId().equals(id)).findFirst().orElse(null);
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
