package com.seailz.discordjar.model.role;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.application.Application;
import com.seailz.discordjar.model.resolve.Resolvable;
import com.seailz.discordjar.utils.Mentionable;
import com.seailz.discordjar.utils.flag.BitwiseUtil;
import com.seailz.discordjar.utils.flag.Bitwiseable;
import com.seailz.discordjar.utils.model.JSONProp;
import com.seailz.discordjar.utils.model.Model;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.EnumSet;
import java.util.List;

/**
 * Represents a Discord role in a guild.
 */
public class Role implements Resolvable, Mentionable, Model {
    @JSONProp("id")
    private String id;
    @JSONProp("name")
    private String name;
    @JSONProp("color")
    private int color;
    @JSONProp("hoist")
    private boolean hoist;
    @JSONProp("icon")
    private String icon;
    @JSONProp("position")
    private int position;
    @JSONProp("permissions")
    private int permissions; // TODO: make this a set of permissions
    @JSONProp("managed")
    private boolean managed;
    @JSONProp("mentionable")
    private boolean mentionable;
    @JSONProp("tags")
    private RoleTag tags;
    @JSONProp("flags")
    private EnumSet<Flag> flags;
    @JSONProp("flags")
    private int flagsRaw;

    private Role() {}

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public int color() {
        return color;
    }

    public boolean hoist() {
        return hoist;
    }

    public String icon() {
        return icon;
    }

    public int position() {
        return position;
    }

    public int permissions() {
        return permissions;
    }

    public boolean managed() {
        return managed;
    }

    public boolean mentionable() {
        return mentionable;
    }

    public RoleTag tags() {
        return tags;
    }

    public EnumSet<Flag> flags() {
        return flags;
    }

    public int flagsRaw() {
        return flagsRaw;
    }

    @Override
    public String getMentionablePrefix() {
        return "@&";
    }

    /**
     * Represents a flag on a role.
     * @author Seailz
     */
    public enum Flag implements Bitwiseable {

        IN_PROMPT(0),
        UNKNOWN(-1)
        ;

        private int value;

        Flag(int value) {
            this.value = value;
        }

        @Override
        public int getLeftShiftId() {
            return 1 << value;
        }

        @Override
        public int id() {
            return value;
        }

        public static Flag fromValue(int value) {
            for (Flag flag : values()) {
                if (flag.id() == value) return flag;
            }
            return UNKNOWN;
        }
    }
}
