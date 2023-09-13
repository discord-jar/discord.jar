package com.seailz.discordjar.model.emoji;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.role.Role;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.Snowflake;
import com.seailz.discordjar.utils.model.JSONProp;
import com.seailz.discordjar.utils.model.Model;
import com.seailz.discordjar.utils.model.ModelDecoder;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Discord Emoji
 */
public class Emoji implements Model, Snowflake {
    @JSONProp("id")
    private String id;
    @JSONProp("name")
    private String name;
    @JSONProp("roles")
    private List<Role> roles;
    @JSONProp("user")
    private User user;
    @JSONProp("require_colons")
    private boolean requireColons;
    @JSONProp("managed")
    private boolean managed;
    @JSONProp("animated")
    private boolean animated;
    @JSONProp("available")
    private boolean available;

    private Emoji() {}

    private Emoji(String id, String name, List<Role> roles, User user, boolean requireColons, boolean managed, boolean animated, boolean available) {
        this.id = id;
        this.name = name;
        this.roles = roles;
        this.user = user;
        this.requireColons = requireColons;
        this.managed = managed;
        this.animated = animated;
        this.available = available;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public List<Role> roles() {
        return roles;
    }

    public User user() {
        return user;
    }

    public boolean requireColons() {
        return requireColons;
    }

    public boolean managed() {
        return managed;
    }

    public boolean animated() {
        return animated;
    }

    public boolean available() {
        return available;
    }

    /**
     * Gets an emoji is a mention that can be added in a {@link com.seailz.discordjar.model.message.Message}
     * @return The emoji mention as a String
     */
    public String getAsMention() {
        StringBuilder mentionBuilder = new StringBuilder();
        mentionBuilder.append("<");
        if (animated) mentionBuilder.append("a");
        mentionBuilder.append(":").append(name).append(":").append(id).append(">");

        return mentionBuilder.toString();
    }

    public static Emoji from(String id, String name, boolean animated) {
        return new Emoji(id, name, null, null, false, false, animated, false);
    }

    public static Emoji from(String emoji) {
        String[] emojiString;
        emoji = emoji.replaceFirst("<:", "");
        emojiString = emoji.split(":");

        String name = emojiString[0];
        String id = emojiString[1].replaceFirst(">", "");
        boolean animated = false;

        if (emojiString.length == 3) {
            animated = true;
            name = emojiString[1];
            id = emojiString[2].replaceFirst(">", "");
        }

        return Emoji.from(id, name, animated);
    }
}
