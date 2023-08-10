package com.seailz.discordjar.model.guild;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.resolve.Resolvable;
import com.seailz.discordjar.model.role.Role;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.Checker;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.flag.BitwiseUtil;
import com.seailz.discordjar.utils.permission.Permission;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents a member of a guild
 */
public class Member implements Compilerable, Resolvable {

    private User user;
    private String nick;
    private String avatar;
    private Role[] roles;
    private List<String> roleIds;
    private String joinedAt;
    private String premiumSince;
    private boolean deaf;
    private boolean mute;
    private boolean pending;
    private List<Permission> permissions;
    private String communicationDisabledUntil;
    private String guildId;
    private List<MemberFlags> flags;
    private int flagsRaw;
    private DiscordJar discordJar;

    public Member(User user, String nick, String avatar, List<String> roles, String joinedAt, String premiumSince, boolean deaf, boolean mute, boolean pending, List<Permission> permissions, String communicationDisabledUntil, String guildId, List<MemberFlags> flags, int flagsRaw, DiscordJar discordJar) {
        this.user = user;
        this.nick = nick;
        this.avatar = avatar;
        this.roleIds = roles;
        this.roles = new Role[roles.size()];
        this.joinedAt = joinedAt;
        this.premiumSince = premiumSince;
        this.deaf = deaf;
        this.mute = mute;
        this.pending = pending;
        this.permissions = permissions;
        this.communicationDisabledUntil = communicationDisabledUntil;
        this.guildId = guildId;
        this.flags = flags;
        this.flagsRaw = flagsRaw;
        this.discordJar = discordJar;
    }

    @Override
    public JSONObject compile() {
        int permissions = 0;
        for (Permission permission : this.permissions) {
            permissions += permission.getLeftShiftId();
        }
        JSONObject obj = new JSONObject();
        obj.put("user", user.compile());
        obj.put("nick", nick);
        obj.put("avatar", avatar);
        obj.put("roles", roleIds);
        obj.put("joined_at", joinedAt);
        obj.put("premium_since", premiumSince);
        obj.put("deaf", deaf);
        obj.put("mute", mute);
        obj.put("pending", pending);
        obj.put("permissions", permissions);
        obj.put("communication_disabled_until", communicationDisabledUntil);
        obj.put("flags", flagsRaw);
        return obj;
    }

    @NonNull
    @Contract("_, _, _, _ -> new")
    public static Member decompile(@NotNull JSONObject obj, @NotNull DiscordJar discordJar, String guildId, Guild guild) {
        User user = null;
        String nick = null;
        String avatar = null;
        List<String> roles = null;
        String joinedAt = null;
        String premiumSince = null;
        boolean deaf = false;
        boolean mute = false;
        boolean pending = false;
        List<MemberFlags> flags = null;
        int flagsRaw = 0;
        List<Permission> permissions = null;
        String communicationDisabledUntil = null;

        if (obj.has("user") && obj.get("user") != JSONObject.NULL) user = User.decompile(obj.getJSONObject("user"), discordJar);
        if (obj.has("nick") && obj.get("nick") != JSONObject.NULL) nick = obj.getString("nick");
        if (obj.has("avatar") && obj.get("avatar") != JSONObject.NULL) avatar = obj.getString("avatar");
        if (obj.has("roles")) {
            if (guild != null) {
                List<String> rolesList = new ArrayList<>();
                for (Object o : obj.getJSONArray("roles")) {
                    rolesList.add((String) o);
                }
                roles = rolesList;
            }
        }
        if (obj.has("joined_at") && obj.get("joined_at") != JSONObject.NULL) joinedAt = obj.getString("joined_at");
        if (obj.has("premium_since") && obj.get("premium_since") != JSONObject.NULL) premiumSince = obj.getString("premium_since");
        if (obj.has("deaf") && obj.get("deaf") != JSONObject.NULL) deaf = obj.getBoolean("deaf");
        if (obj.has("mute") && obj.get("mute") != JSONObject.NULL) mute = obj.getBoolean("mute");
        if (obj.has("pending") && obj.get("pending") != JSONObject.NULL) pending = obj.getBoolean("pending");

        try {
            BitwiseUtil<Permission> bitwiseUtil = new BitwiseUtil<>();
            List<Permission> permissionsList = new ArrayList<>(bitwiseUtil.get(
                    Long.parseLong(obj.getString("permissions")), Permission.class));
            permissions = permissionsList;
        } catch (JSONException e) {
            permissions = null;
        }

        if (obj.has("flags")) {
            flagsRaw = obj.getInt("flags");
            BitwiseUtil<MemberFlags> bitwiseUtil = new BitwiseUtil<>();
            flags = new ArrayList<>(bitwiseUtil.get(flagsRaw, MemberFlags.class));
        }

        if (obj.has("communication_disabled_until") && obj.get("communication_disabled_until") != JSONObject.NULL)
            communicationDisabledUntil = obj.getString("communication_disabled_until");
        return new Member(user, nick, avatar, roles, joinedAt, premiumSince, deaf, mute, pending, permissions, communicationDisabledUntil, guildId, flags, flagsRaw, discordJar);
    }

    public Role[] roles() {
        if (this.roles != null) return this.roles;
        Role[] roles = discordJar.getGuildById(guildId).roles().stream().filter(role -> roleIds.contains(role.id())).toArray(Role[]::new);
        this.roles = roles;
        return roles;
    }

    /**
     * Nickname the member
     * @param nick the nickname to set
     */
    public void nickname(String nick) {
        try {
            new DiscordRequest(
                    new JSONObject().put("nick", nick),
                    new HashMap<>(),
                    URLS.PATCH.GUILD.MEMBER.MODIFY_GUILD_MEMBER.replace("{guild.id}", guildId).replace("{user.id}", user.id()),
                    discordJar,
                    URLS.PATCH.GUILD.MEMBER.MODIFY_GUILD_MEMBER,
                    RequestMethod.PATCH
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * 	Will throw a 403 error if the user has the ADMINISTRATOR permission or is the owner of the guild
     * @param seconds the amount of seconds to add to the timeout
     */
    public void timeout(int seconds) {
        Checker.check(seconds > 2419200, "Timeout must be less than 28 days");
        Checker.check(seconds < 0, "Timeout must be greater than 0. To remove a timeout, use removeTimeout()");

        Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
        calendar.add(Calendar.SECOND, seconds);
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String timeout = dateFormat.format(date);


        try {
            new DiscordRequest(
                    new JSONObject().put("communication_disabled_until", timeout),
                    new HashMap<>(),
                    URLS.PATCH.GUILD.MEMBER.MODIFY_GUILD_MEMBER.replace("{guild.id}", guildId).replace("{user.id}", user.id()),
                    discordJar,
                    URLS.PATCH.GUILD.MEMBER.MODIFY_GUILD_MEMBER,
                    RequestMethod.PATCH
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }

    }

    public void removeTimeout() {
        try {
            new DiscordRequest(
                    new JSONObject().put("communication_disabled_until", JSONObject.NULL),
                    new HashMap<>(),
                    URLS.PATCH.GUILD.MEMBER.MODIFY_GUILD_MEMBER.replace("{guild.id}", guildId).replace("{user.id}", user.id()),
                    discordJar,
                    URLS.PATCH.GUILD.MEMBER.MODIFY_GUILD_MEMBER,
                    RequestMethod.PATCH
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * Adds a role to a member.
     * Requires `MANAGE_ROLES` permission.
     * @param role the role to add
     */
    public void addRole(Role role) {
        try {
            new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.PUT.GUILD.MEMBERS.ROLES.ADD_GUILD_MEMBER_ROLE.replace("{guild.id}", guildId).replace("{user.id}", user.id()).replace("{role.id}", role.id()),
                    discordJar,
                    URLS.PUT.GUILD.MEMBERS.ROLES.ADD_GUILD_MEMBER_ROLE,
                    RequestMethod.PUT
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    /**
     * Removes a role from a member.
     * Requires `MANAGE_ROLES` permission.
     * @param role the role to remove
     */
    public void removeRole(Role role) {
        try {
            new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.DELETE.GUILD.MEMBER.REMOVE_GUILD_MEMBER_ROLE.replace("{guild.id}", guildId).replace("{user.id}", user.id()).replace("{role.id}", role.id()),
                    discordJar,
                    URLS.DELETE.GUILD.MEMBER.REMOVE_GUILD_MEMBER_ROLE,
                    RequestMethod.DELETE
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    public boolean hasPermission(Permission perm) {
        return permissions.contains(perm);
    }

    public String guildId() {
        return guildId;
    }

    public DiscordJar discordJar() {
        return discordJar;
    }

    public int flagsRaw() {
        return flagsRaw;
    }

    public List<MemberFlags> flags() {
        return flags;
    }

    public List<Permission> permissions() {
        return permissions;
    }

    public String avatar() {
        return avatar;
    }

    public String communicationDisabledUntil() {
        return communicationDisabledUntil;
    }

    public String joinedAt() {
        return joinedAt;
    }

    public String nick() {
        return nick;
    }

    public String premiumSince() {
        return premiumSince;
    }

    public User user() {
        return user;
    }
}
