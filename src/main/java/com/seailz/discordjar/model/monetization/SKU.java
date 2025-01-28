package com.seailz.discordjar.model.monetization;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.action.sku.ListEntitlementRequest;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.flag.Bitwiseable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

/**
 * Represents an application SKU (stock keeping unit).
 */
public class SKU implements Compilerable {
    private String id;
    private Type type;
    private String applicationId;
    private String name;
    private String slug;
    private List<Flag> flags;
    private DiscordJar discordJar;

    private SKU(String id, Type type, String applicationId, String name, String slug, List<Flag> flags, DiscordJar discordJar) {
        this.id = id;
        this.type = type;
        this.applicationId = applicationId;
        this.name = name;
        this.slug = slug;
        this.flags = flags;
        this.discordJar = discordJar;
    }

    /**
     * Returns the ID of this SKU.
     */
    @NotNull
    public String id() {
        return id;
    }

    /**
     * Returns the type of this SKU.
     */
    @NotNull
    public Type type() {
        return type;
    }

    /**
     * Returns the ID of the parent application for this SKU.
     */
    @NotNull
    public String applicationId() {
        return applicationId;
    }

    /**
     * Returns the name of this SKU.
     */
    @NotNull
    public String name() {
        return name;
    }

    /**
     * Returns the system-generated URL slug for this SKU.
     */
    @NotNull
    public String slug() {
        return slug;
    }

    /**
     * Returns the flags for this SKU.
     */
    @NotNull
    public List<Flag> flags() {
        return flags;
    }

    public boolean isGuildSubscription() {
        return flags.contains(Flag.GUILD_SUB);
    }

    public boolean isUserSubscription() {
        return flags.contains(Flag.USER_SUB);
    }

    public boolean hasUserPurchased(String userId) {
        return !new ListEntitlementRequest(discordJar)
                .setUserId(userId)
                .setLimit(1)
                .setExcludeEnded(true)
                .setSkuIds(List.of(id))
                .run().awaitCompleted().isEmpty();
    }

    public boolean hasGuildPurchased(String guildId) {
        return !new ListEntitlementRequest(discordJar)
                .setGuildId(guildId)
                .setLimit(1)
                .setExcludeEnded(true)
                .setSkuIds(List.of(id))
                .run().awaitCompleted().isEmpty();
    }

    @Override
    public JSONObject compile() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("type", type.getCode());
        object.put("application_id", applicationId);
        object.put("name", name);
        object.put("slug", slug);

        int flags = 0;
        for (Flag flag : this.flags) {
            flags |= flag.getLeftShiftId();
        }
        object.put("flags", flags);
        return object;
    }

    @NotNull
    @Contract("_ -> new")
    public static SKU decompile(@NotNull JSONObject obj, @NotNull DiscordJar discordJar) {
        return new SKU(
                obj.getString("id"),
                Type.fromCode(obj.getInt("type")),
                obj.getString("application_id"),
                obj.getString("name"),
                obj.getString("slug"),
                (List<Flag>) Flag.UNKNOWN.decode(obj.getInt("flags")),
                discordJar
        );
    }

    public enum Type {
        DURABLE(2),
        CONSUMABLE(3),
        SUBSCRIPTION(5),
        SUBSCRIPTION_GROUP(6),
        UNKNOWN(-1);
        private int code;

        Type(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static Type fromCode(int code) {
            for (Type type : values()) {
                if (type.getCode() == code) return type;
            }
            return UNKNOWN;
        }
    }
    public enum Flag implements Bitwiseable {

        AVAILABLE(2),
        GUILD_SUB(7),
        USER_SUB(8),
        UNKNOWN(-1);

        private final int id;

        Flag(int id) {
            this.id = id;
        }

        @Override
        public int getLeftShiftId() {
            return 1 << id;
        }

        @Override
        public int id() {
            return id;
        }
    }
}
