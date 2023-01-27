package com.seailz.discordjv.model.guild;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.model.resolve.Resolvable;
import com.seailz.discordjv.model.role.Role;
import com.seailz.discordjv.model.user.User;
import com.seailz.discordjv.utils.Checker;
import com.seailz.discordjv.utils.URLS;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import com.seailz.discordjv.utils.flag.BitwiseUtil;
import com.seailz.discordjv.utils.permission.Permission;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents a member of a guild
 *
 * @param user                       the user this guild member represents
 * @param nick                       this user's guild nickname
 * @param avatar                     the member's guild avatar hash //TODO: sort out avatars and images
 * @param roles                      The user's roles
 * @param joinedAt                   when the user joined the guild
 * @param premiumSince               when the user started boosting the guild
 * @param deaf                       whether the user is deafened in voice channels
 * @param mute                       whether the user is muted in voice channels
 * @param pending                    whether the user has not yet passed the guild's Membership Screening requirements
 * @param permissions                total permissions of the member in the channel, including overwrites, returned when in the interaction object //TODO: sort out permissions
 * @param communicationDisabledUntil when the user's timeout will expire and the user will be able to communicate in the guild again, null or a time in the past if the user is not timed out
 */
public record Member(
        User user,
        String nick,
        String avatar,
        Role[] roles,
        String joinedAt,
        String premiumSince,
        boolean deaf,
        boolean mute,
        boolean pending,
        List<Permission> permissions,
        String communicationDisabledUntil,
        String guildId,
        List<MemberFlags> flags,
        int flagsRaw,
        DiscordJv discordJv
) implements Compilerable, Resolvable {

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
        obj.put("roles", roles);
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
    public static Member decompile(@NotNull JSONObject obj, @NotNull DiscordJv discordJv, String guildId, Guild guild) {
        User user = null;
        String nick = null;
        String avatar = null;
        Role[] roles = new Role[0];
        String joinedAt = null;
        String premiumSince = null;
        boolean deaf = false;
        boolean mute = false;
        boolean pending = false;
        List<MemberFlags> flags = null;
        int flagsRaw = 0;
        List<Permission> permissions = null;
        String communicationDisabledUntil = null;

        if (obj.has("user")) user = User.decompile(obj.getJSONObject("user"), discordJv);
        if (obj.has("nick")) nick = obj.getString("nick");
        if (obj.has("avatar")) avatar = obj.getString("avatar");
        if (obj.has("roles")) {
            if (guild != null) {
                List<Role> rolesList = new ArrayList<>();
                List<Role> guildRoles = guild.roles();
                for (Object o : obj.getJSONArray("roles")) {
                    guildRoles.stream()
                            .filter(role -> role.id().equals(o.toString()))
                            .findFirst()
                            .ifPresent(rolesList::add);
                }
                roles = rolesList.toArray(new Role[0]);
            }
        }
        if (obj.has("joined_at")) joinedAt = obj.getString("joined_at");
        if (obj.has("premium_since")) premiumSince = obj.getString("premium_since");
        if (obj.has("deaf")) deaf = obj.getBoolean("deaf");
        if (obj.has("mute")) mute = obj.getBoolean("mute");
        if (obj.has("pending")) pending = obj.getBoolean("pending");

        /*try {
            BitwiseUtil<Permission> bitwiseUtil = new BitwiseUtil<>();
            List<Permission> permissionsList = new ArrayList<>(bitwiseUtil.get(
                    Integer.parseInt(obj.getString("permissions")), Permission.class));
            permissions = permissionsList;
        } catch (JSONException e) {
            permissions = null;
        }*/

        if (obj.has("flags")) {
            flagsRaw = obj.getInt("flags");
            BitwiseUtil<MemberFlags> bitwiseUtil = new BitwiseUtil<>();
            flags = new ArrayList<>(bitwiseUtil.get(flagsRaw, MemberFlags.class));
        }

        if (obj.has("communication_disabled_until"))
            communicationDisabledUntil = obj.getString("communication_disabled_until");
        return new Member(user, nick, avatar, roles, joinedAt, premiumSince, deaf, mute, pending, permissions, communicationDisabledUntil, guildId, flags, flagsRaw, discordJv);
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


        new DiscordRequest(
                new JSONObject().put("communication_disabled_until", timeout),
                new HashMap<>(),
                URLS.PATCH.GUILD.MEMBER.MODIFY_GUILD_MEMBER.replace("{guild.id}", guildId).replace("{user.id}", user.id()),
                discordJv,
                URLS.PATCH.GUILD.MEMBER.MODIFY_GUILD_MEMBER,
                RequestMethod.PATCH
        ).invoke();

    }

    public void removeTimeout() {
        new DiscordRequest(
                new JSONObject().put("communication_disabled_until", JSONObject.NULL),
                new HashMap<>(),
                URLS.PATCH.GUILD.MEMBER.MODIFY_GUILD_MEMBER.replace("{guild.id}", guildId).replace("{user.id}", user.id()),
                discordJv,
                URLS.PATCH.GUILD.MEMBER.MODIFY_GUILD_MEMBER,
                RequestMethod.PATCH
        ).invoke();
    }

    /**
     * Adds a role to a member.
     * Requires `MANAGE_ROLES` permission.
     * @param role the role to add
     */
    public void addRole(Role role) {
        new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.PUT.GUILD.MEMBERS.ROLES.ADD_GUILD_MEMBER_ROLE.replace("{guild.id}", guildId).replace("{user.id}", user.id()).replace("{role.id}", role.id()),
                discordJv,
                URLS.PUT.GUILD.MEMBERS.ROLES.ADD_GUILD_MEMBER_ROLE,
                RequestMethod.PUT
        ).invoke();
    }

    public boolean hasPermission(Permission perm) {
        return permissions.contains(perm);
    }
}
