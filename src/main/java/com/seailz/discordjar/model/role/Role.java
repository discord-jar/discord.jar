package com.seailz.discordjar.model.role;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.resolve.Resolvable;
import com.seailz.discordjar.utils.Mentionable;
import com.seailz.discordjar.utils.flag.BitwiseUtil;
import com.seailz.discordjar.utils.flag.Bitwiseable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.EnumSet;

/**
 * Represents a Discord role in a guild.
 *
 * @param id          The role's ID.
 * @param name        The role's name.
 * @param color       The role's color.
 * @param hoist       Whether the role is pinned in the user listing.
 * @param icon        The role's icon.
 * @param position    The role's position.
 * @param permissions The role's permissions.
 * @param managed     Whether the role is managed by an integration.
 * @param mentionable Whether the role is mentionable.
 * @param tags        The role's tags.
 */
public record Role(
        String id,
        String name,
        int color,
        boolean hoist,
        String icon,
        int position,
        int permissions,
        boolean managed,
        boolean mentionable,
        RoleTag tags,
        EnumSet<Flag> flags,
        int flagsRaw
) implements Compilerable, Resolvable, Mentionable {
    public static Role decompile(JSONObject obj) {
        String id;
        String name;
        int color;
        boolean hoist;
        String icon;
        int position;
        int permissions;
        boolean managed;
        boolean mentionable;
        RoleTag tags;
        EnumSet<Flag> flags = EnumSet.noneOf(Flag.class);
        int flagsRaw;

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
            color = obj.getInt("color");
        } catch (JSONException e) {
            color = 0;
        }

        try {
            hoist = obj.getBoolean("hoist");
        } catch (JSONException e) {
            hoist = false;
        }

        try {
            icon = obj.getString("icon");
        } catch (JSONException e) {
            icon = null;
        }

        try {
            position = obj.getInt("position");
        } catch (JSONException e) {
            position = 0;
        }

        try {
            permissions = obj.getInt("permissions");
        } catch (JSONException e) {
            permissions = 0;
        }

        try {
            managed = obj.getBoolean("managed");
        } catch (JSONException e) {
            managed = false;
        }

        try {
            mentionable = obj.getBoolean("mentionable");
        } catch (JSONException e) {
            mentionable = false;
        }

        try {
            tags = RoleTag.decompile(obj.getJSONObject("tags"));
        } catch (JSONException e) {
            tags = null;
        }

        try {
            flagsRaw = obj.getInt("flags");
            flags = new BitwiseUtil<Role.Flag>().get(flagsRaw, Role.Flag.class);
        } catch (JSONException e) {
            flagsRaw = 0;
        }

        return new Role(id, name, color, hoist, icon, position, permissions, managed, mentionable, tags, flags, flagsRaw);
    }

    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("name", name);
        obj.put("color", color);
        obj.put("hoist", hoist);
        obj.put("icon", icon);
        obj.put("position", position);
        obj.put("permissions", permissions);
        obj.put("managed", managed);
        obj.put("mentionable", mentionable);
        obj.put("tags", tags.compile());
        obj.put("flags", flags);
        return obj;
    }

    @Override
    public String getMentionablePrefix() {
        return "@&";
    }

    /**
     * Represents a flag on a role.
     *
     * @author Seailz
     */
    public enum Flag implements Bitwiseable {

        IN_PROMPT(0),
        UNKNOWN(-1);

        private final int value;

        Flag(int value) {
            this.value = value;
        }

        public static Flag fromValue(int value) {
            for (Flag flag : values()) {
                if (flag.id() == value) return flag;
            }
            return UNKNOWN;
        }

        @Override
        public int getLeftShiftId() {
            return 1 << value;
        }

        @Override
        public int id() {
            return value;
        }
    }
}
