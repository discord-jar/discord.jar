package com.seailz.discordjar.model.mentions;

import com.seailz.discordjar.core.Compilerable;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.logging.Logger;

/**
 * Represents allowed mentions
 * See <a href="https://discord.com/developers/docs/resources/channel#allowed-mentions-object">Allowed Mentions</a>
 *
 * @param parse     The types of mentions to parse from the content
 * @param roles     The role ids to mention
 * @param users     The user ids to mention
 * @param pingReply Whether to ping the author of the message being replied to
 *                  <p>
 *                  None of these values may be null, but the lists can be empty.
 * @author Seailz
 */
public record AllowedMentions(
        @NotNull List<Type> parse,
        @NotNull List<String> roles,
        @NotNull List<String> users,
        boolean allowEveryone,
        boolean pingReply
) implements Compilerable {

    /**
     * Creates an {@link AllowedMentions} object that will suppress all mentions
     */
    public static AllowedMentions none() {
        return new AllowedMentions(List.of(), List.of(), List.of(), false, false);
    }

    @Override
    public JSONObject compile() {
        JSONObject json = new JSONObject();
        if (parse == null) {
            throw new IllegalArgumentException("Parse cannot be null");
        }

        if (roles == null) {
            throw new IllegalArgumentException("Roles cannot be null");
        }

        if (users == null) {
            throw new IllegalArgumentException("Users cannot be null");
        }

        JSONArray parseArray = new JSONArray();
        if (parse.contains(Type.EVERYONE_MENTIONS) && !allowEveryone) {
            parse.remove(Type.EVERYONE_MENTIONS);
            Logger.getLogger(AllowedMentions.class.getName()).warning("Cannot parse everyone mentions if allowEveryone is false. Removing from parse list.");
        }
        for (Type type : parse) {
            parseArray.put(type.value());
        }

        json.put("parse", parseArray);

        JSONArray rolesArray = new JSONArray();
        for (String role : roles) {
            rolesArray.put(role);
        }

        json.put("roles", rolesArray);

        JSONArray usersArray = new JSONArray();
        for (String user : users) {
            usersArray.put(user);
        }

        json.put("users", usersArray);
        return json;
    }

    public enum Type {
        ROLE_MENTIONS("roles"), // @<role>
        USER_MENTIONS("users"), // @<user>
        EVERYONE_MENTIONS("everyone"); // @everyone and @here

        private final String value;

        Type(String value) {
            this.value = value;
        }

        public static Type fromValue(String value) {
            for (Type type : Type.values()) {
                if (type.value().equals(value)) return type;
            }
            return null;
        }

        public String value() {
            return value;
        }
    }
}
