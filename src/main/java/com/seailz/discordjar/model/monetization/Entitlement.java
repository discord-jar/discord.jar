package com.seailz.discordjar.model.monetization;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.user.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

/**
 * Represents an entitlement, which is generally defined as a purchase of a {@link SKU}.
 */
public class Entitlement implements Compilerable {

    private String id;
    private String skuId;
    private String guildId;
    private String userId;
    private String applicationId;
    private Type type;

    private DiscordJar jar;
    private Guild guild;
    private SKU sku;
    private User user;

    private Entitlement(String id, String skuId, String guildId, String userId, String applicationId, Type type, DiscordJar jar) {
        this.id = id;
        this.skuId = skuId;
        this.guildId = guildId;
        this.userId = userId;
        this.applicationId = applicationId;
        this.type = type;
        this.jar = jar;
    }

    /**
     * Returns the ID of this entitlement.
     */
    @NotNull
    public String id() {
        return id;
    }

    /**
     * Returns the ID of the SKU that this entitlement is for.
     */
    @NotNull
    public String skuId() {
        return skuId;
    }

    /**
     * Returns the {@link SKU} that this entitlement is for.
     */
    @NotNull
    public SKU sku() {
        if (sku == null) sku = jar.getSKUById(skuId);
        return sku;
    }

    /**
     * Returns the {@link User user} that this entitlement is for, or null if this entitlement is not for a user.
     */
    @Nullable
    public User getUser() {
        if (userId == null) return null;
        if (user == null) user = jar.getUserById(userId);
        return user;
    }

    /**
     * Returns the ID of the user that is granted access to this entitlement's SKU, or null if this entitlement is not for a user.
     */
    @Nullable
    public String userId() {
        return userId;
    }

    /**
     * Returns the {@link Guild guild} that is granted access to this entitlement's SKU, or null if this entitlement is not for a guild.
     */
    @Nullable
    public Guild getGuild() {
        if (guildId == null) return null;
        if (guild == null) guild = jar.getGuildById(guildId);
        return guild;
    }

    /**
     * Returns the ID of the guild that is granted access to this entitlement's SKU, or null if this entitlement is not for a guild.
     */
    @Nullable
    public String guildId() {
        return guildId;
    }

    /**
     * Returns the target ID of this entitlement. This is either the guild ID or the user ID, depending on the type of entitlement.
     */
    @NotNull
    public String getTargetId() {
        return guildId == null ? userId : guildId;
    }

    /**
     * Returns the class of the target of this entitlement. This is either {@link User} or {@link Guild}, depending on the type of entitlement.
     */
    @NotNull
    public Class<?> getTargetClass() {
        return guildId == null ? User.class : Guild.class;
    }

    @NotNull
    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("sku_id", skuId);
        obj.put("guild_id", guildId);
        obj.put("user_id", userId);
        obj.put("application_id", applicationId);
        obj.put("type", type.code());
        return obj;
    }

    @NotNull
    @Contract("_, _ -> new")
    public static Entitlement decompile(@NotNull DiscordJar jar, @NotNull JSONObject obj) {
        return new Entitlement(
                obj.getString("id"),
                obj.getString("sku_id"),
                obj.has("guild_id") ? obj.getString("guild_id") : null,
                obj.has("user_id") ? obj.getString("user_id") : null,
                obj.getString("application_id"),
                Type.fromCode(obj.getInt("type")),
                jar
        );
    }

    public enum Type {
        APPLICATION_SUBSCRIPTION(0),
        UNKNOWN(-1);

        private int code;

        Type(int code) {
            this.code = code;
        }

        public int code() {
            return code;
        }

        public static Type fromCode(int code) {
            for (Type type : values()) {
                if (type.code() == code) return type;
            }
            return UNKNOWN;
        }
    }

}
