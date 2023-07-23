package com.seailz.discordjar.model.mentions;

import com.seailz.discordjar.core.Compilerable;
import org.jetbrains.annotations.NotNull;
import com.seailz.discordjar.utils.json.SJSONArray;
import com.seailz.discordjar.utils.json.SJSONObject;

import java.util.List;
import java.util.logging.Logger;

/**
 * Represents allowed mentions
 * See <a href="https://discord.com/developers/docs/resources/channel#allowed-mentions-object">Allowed Mentions</a>
 * @param parse The types of mentions to parse from the content
 * @param roles The role ids to mention
 * @param users The user ids to mention
 * @param pingReply Whether to ping the author of the message being replied to
 *
 * None of these values may be null, but the lists can be empty.
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
    public SJSONObject compile() {
        SJSONObject json = new SJSONObject();
        if (parse == null) {
            throw new IllegalArgumentException("Parse cannot be null");
        }

        if (roles == null) {
            throw new IllegalArgumentException("Roles cannot be null");
        }

        if (users == null) {
            throw new IllegalArgumentException("Users cannot be null");
        }

        SJSONArray parseArray = new SJSONArray();
        if (parse.contains(Type.EVERYONE_MENTIONS) && !allowEveryone) {
            parse.remove(Type.EVERYONE_MENTIONS);
            Logger.getLogger(AllowedMentions.class.getName()).warning("Cannot parse everyone mentions if allowEveryone is false. Removing from parse list.");
        }
        for (Type type : parse) {
            parseArray.put(type.value());
        }

        json.put("parse", parseArray);

        SJSONArray rolesArray = new SJSONArray();
        for (String role : roles) {
            rolesArray.put(role);
        }

        json.put("roles", rolesArray);

        SJSONArray usersArray = new SJSONArray();
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

        public String value() {
            return value;
        }

        public static Type fromValue(String value) {
            for (Type type : Type.values()) {
                if (type.value().equals(value)) return type;
            }
            return null;
        }
    }
}
