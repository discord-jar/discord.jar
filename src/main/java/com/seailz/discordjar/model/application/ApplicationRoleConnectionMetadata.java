package com.seailz.discordjar.model.application;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.model.JSONProp;
import com.seailz.discordjar.utils.model.Model;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.function.Function;

/**
 * A representation of role connection metadata for an application. <p>
 * When a guild has added a bot and that bot has configured its
 * {@code role_connections_verification_url} (in the developer portal) the application
 * will render as a potential verification method in the guild's role verification configuration. <p>
 * If an application has configured role connection metadata,
 * its metadata will appear in the role verification configuration when the
 * application has been added as a verification method to the role. <p>
 *
 * When a user connects their account using the bots role_connections_verification_url,
 * the bot will update a user's role connection with metadata
 * using the OAuth2 {@code role_connections.write} scope.
 *
 * @author Seailz
 * @since  1.0
 * @see    Application
 */

public class ApplicationRoleConnectionMetadata implements Model {
    @JSONProp("type")
    private Type type;
    @JSONProp("key")
    private String key;
    @JSONProp("name")
    private String name;
    @JSONProp("name_localizations")
    private HashMap<String, String> nameLocalizations;
    @JSONProp("description")
    private String description;
    @JSONProp("description_localizations")
    private HashMap<String, String> descriptionLocalizations;

    @Override
    public HashMap<String, Function<JSONObject, ?>> customDecoders() {
        return new HashMap<>(){{
            put("name_localizations", (json) -> {
                if (json.has("name_localizations")) {
                    JSONObject nameLocalizationsJson = json.getJSONObject("name_localizations");
                    HashMap<String, String> nameLocalizations = new HashMap<>();
                    for (String key1 : nameLocalizationsJson.keySet()) {
                        nameLocalizations.put(key1, nameLocalizationsJson.getString(key1));
                    }
                    return nameLocalizations;
                }
                return null;
            });

            put("description_localizations", (json) -> {
                if (json.has("description_localizations")) {
                    JSONObject descriptionLocalizationsJson = json.getJSONObject("description_localizations");
                    HashMap<String, String> descriptionLocalizations = new HashMap<>();
                    for (String key1 : descriptionLocalizationsJson.keySet()) {
                        descriptionLocalizations.put(key1, descriptionLocalizationsJson.getString(key1));
                    }
                    return descriptionLocalizations;
                }
                return null;
            });
        }};
    }

    public Type type() {
        return type;
    }

    public String key() {
        return key;
    }

    public String name() {
        return name;
    }

    public HashMap<String, String> nameLocalizations() {
        return nameLocalizations;
    }

    public String description() {
        return description;
    }

    public HashMap<String, String> descriptionLocalizations() {
        return descriptionLocalizations;
    }

    public ApplicationRoleConnectionMetadata(Type type, String key, String name, HashMap<String, String> nameLocalizations, String description, HashMap<String, String> descriptionLocalizations) {
        this.type = type == null ? Type.UNKNOWN : type;
        this.key = key;
        this.name = name;
        this.nameLocalizations = nameLocalizations;
        this.description = description;
        this.descriptionLocalizations = descriptionLocalizations;
    }

    public ApplicationRoleConnectionMetadata(Type type, String key, String name, String description) {
        this(type, key, name, new HashMap<>(), description, new HashMap<>());
    }

    private ApplicationRoleConnectionMetadata() {}

    public ApplicationRoleConnectionMetadata addNameLocalization(String language, String name) {
        this.nameLocalizations.put(language, name);
        return this;
    }

    public ApplicationRoleConnectionMetadata addDescriptionLocalization(String language, String description) {
        this.descriptionLocalizations.put(language, description);
        return this;
    }

    /**
     * Represents the type of role connection metadata.
     *
     * @author Seailz
     * @since  1.0
     * @see    ApplicationRoleConnectionMetadata
     */
    public enum Type {
        INTEGER_LESS_THAN_OR_EQUAL(1),
        INTEGER_GREATER_THAN_OR_EQUAL(2),
        INTEGER_EQUAL(3),
        INTEGER_NOT_EQUAL(4),
        DATETIME_LESS_THAN_OR_EQUAL(5),
        DATETIME_GREATER_THAN_OR_EQUAL(6),
        BOOLEAN_EQUAL(7),
        BOOLEAN_NOT_EQUAL(8),

        UNKNOWN(-1);

        private final int code;

        Type(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static Type fromCode(int code) {
            for (Type type : values()) {
                if (type.getCode() == code) {
                    return type;
                }
            }
            return null;
        }
    }
}
