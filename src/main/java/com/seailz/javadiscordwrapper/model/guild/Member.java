package com.seailz.javadiscordwrapper.model.guild;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.core.Compilerable;
import com.seailz.javadiscordwrapper.model.resolve.Resolvable;
import com.seailz.javadiscordwrapper.model.role.Role;
import com.seailz.javadiscordwrapper.model.user.User;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a member of a guild
 * @param user the user this guild member represents
 * @param nick this user's guild nickname
 * @param avatar the member's guild avatar hash //TODO: sort out avatars and images
 * @param roles The user's roles
 * @param joinedAt when the user joined the guild
 * @param premiumSince when the user started boosting the guild
 * @param deaf whether the user is deafened in voice channels
 * @param mute whether the user is muted in voice channels
 * @param pending whether the user has not yet passed the guild's Membership Screening requirements
 * @param permissions total permissions of the member in the channel, including overwrites, returned when in the interaction object //TODO: sort out permissions
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
        String permissions,
        String communicationDisabledUntil
) implements Compilerable, Resolvable {

    @Override
    public JSONObject compile() {
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
        return obj;
    }

    @NonNull
    public static Member decompile(JSONObject obj, DiscordJv discordJv) {
        User user;
        String nick;
        String avatar;
        Role[] roles;
        String joinedAt;
        String premiumSince;
        boolean deaf;
        boolean mute;
        boolean pending;
        String permissions;
        String communicationDisabledUntil;

        try {
            user = User.decompile(obj.getJSONObject("user"), discordJv);
        } catch (Exception e) {
            user = null;
        }

        try {
            nick = obj.getString("nick");
        } catch (Exception e) {
            nick = null;
        }

        try {
            avatar = obj.getString("avatar");
        } catch (Exception e) {
            avatar = null;
        }

        try {
            List<Role> rolesList = new ArrayList<>();
            for (Object o : obj.getJSONArray("roles")) {
                rolesList.add(Role.decompile((JSONObject) o));
            }
            roles = rolesList.toArray(new Role[0]);
        } catch (Exception e) {
            roles = null;
        }

        try {
            joinedAt = obj.getString("joined_at");
        } catch (Exception e) {
            joinedAt = null;
        }

        try {
            premiumSince = obj.getString("premium_since");
        } catch (Exception e) {
            premiumSince = null;
        }

        try {
            deaf = obj.getBoolean("deaf");
        } catch (Exception e) {
            deaf = false;
        }

        try {
            mute = obj.getBoolean("mute");
        } catch (Exception e) {
            mute = false;
        }

        try {
            pending = obj.getBoolean("pending");
        } catch (Exception e) {
            pending = false;
        }

        try {
            permissions = obj.getString("permissions");
        } catch (Exception e) {
            permissions = null;
        }

        try {
            communicationDisabledUntil = obj.getString("communication_disabled_until");
        } catch (Exception e) {
            communicationDisabledUntil = null;
        }
        return new Member(user, nick, avatar, roles, joinedAt, premiumSince, deaf, mute, pending, permissions, communicationDisabledUntil);
    }
}
