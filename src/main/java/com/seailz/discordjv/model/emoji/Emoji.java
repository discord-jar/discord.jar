package com.seailz.discordjv.model.emoji;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.model.role.Role;
import com.seailz.discordjv.model.user.User;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

import java.util.ArrayList;

/**
 * Represents a Discord Emoji
 *
 * @param id            The emoji's ID.
 * @param name          The emoji's name.
 * @param roles         The roles that are allowed to use this emoji.
 * @param user          The user that created this emoji.
 * @param requireColons Whether this emoji must be wrapped in colons.
 * @param managed       Whether this emoji is managed.
 * @param animated      Whether this emoji is animated.
 * @param available     Whether this emoji can be used, may be false due to loss of Server Boosts.
 */
public record Emoji(
        String id,
        String name,
        Role[] roles,
        User user,
        boolean requireColons,
        boolean managed,
        boolean animated,
        boolean available
) implements Compilerable {
    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("name", name);
        obj.put("roles", roles);
        obj.put("user", user);
        obj.put("require_colons", requireColons);
        obj.put("managed", managed);
        obj.put("animated", animated);
        obj.put("available", available);
        return obj;
    }

    @NonNull
    public static Emoji decompile(JSONObject obj, DiscordJv discordJv) {
        String id;
        String name;
        Role[] roles;
        User user;
        boolean requireColons;
        boolean managed;
        boolean animated;
        boolean available;

        try {
            id = obj.getString("id");
        } catch (Exception e) {
            id = null;
        }

        try {
            name = obj.getString("name");
        } catch (Exception e) {
            name = null;
        }

        try {
            ArrayList<Role> rolesList = new ArrayList<>();
            for (Object o : obj.getJSONArray("roles")) {
                rolesList.add(Role.decompile((JSONObject) o));
            }
            roles = rolesList.toArray(new Role[0]);
        } catch (Exception e) {
            roles = null;
        }

        try {
            user = User.decompile(obj.getJSONObject("user"), discordJv);
        } catch (Exception e) {
            user = null;
        }

        try {
            requireColons = obj.getBoolean("require_colons");
        } catch (Exception e) {
            requireColons = false;
        }

        try {
            managed = obj.getBoolean("managed");
        } catch (Exception e) {
            managed = false;
        }

        try {
            animated = obj.getBoolean("animated");
        } catch (Exception e) {
            animated = false;
        }

        try {
            available = obj.getBoolean("available");
        } catch (Exception e) {
            available = false;
        }
        return new Emoji(id, name, roles, user, requireColons, managed, animated, available);
    }

    /**
     * Gets an emoji is a mention that can be added in a {@link com.seailz.discordjv.model.message.Message}
     * @return The emoji mention as a String
     */
    public String getAsMention() {
        if (animated) return "<a:" + name + ":" + id + ">";
        else return "<:" + name + ":" + id + ">";
    }
}
