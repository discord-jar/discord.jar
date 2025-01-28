package com.seailz.discordjar.model.monetization;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import com.seailz.discordjar.utils.rest.Response;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

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
    private boolean deleted;
    private DateTime startsAt;
    private DateTime endsAt;
    private boolean consumed;

    private DiscordJar jar;
    private Guild guild;
    private SKU sku;
    private User user;

    private Entitlement(String id, String skuId, String guildId, String userId, String applicationId, Type type, boolean deleted, DateTime startsAt, DateTime endsAt, boolean consumed, DiscordJar jar) {
        this.id = id;
        this.skuId = skuId;
        this.guildId = guildId;
        this.userId = userId;
        this.applicationId = applicationId;
        this.type = type;
        this.jar = jar;
        this.deleted = deleted;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.consumed = consumed;
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

    /**
     * If this is a consumable entitlement, this will consume it.
     * @return A {@link Response} object that will be completed when the request is finished.
     */
    public Response<Void> consume() {
        Response<Void> res = new Response<>();
        new Thread(() -> {
            DiscordRequest req = new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.POST.APPLICATIONS.CONSUME_ENTITLEMENT
                            .replace("{application_id}", applicationId)
                            .replace("{entitlement_id}", id),
                    jar,
                    URLS.POST.APPLICATIONS.CONSUME_ENTITLEMENT,
                    RequestMethod.POST
            );

            try {
                req.invoke();
                res.complete(null);
            } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
                res.completeError(new Response.Error(
                        e.getCode(),
                        e.getMessage(),
                        e.getBody()
                ));
            }
        }, "djar--entitlement-consume").start();
        return res;
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
        obj.put("deleted", deleted);
        obj.put("starts_at", startsAt.toString());
        obj.put("ends_at", endsAt.toString());
        obj.put("consumed", consumed);
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
                obj.optBoolean("deleted"),
                obj.has("starts_at") ? DateTime.parse(obj.getString("starts_at")) : null,
                obj.has("ends_at") ? DateTime.parse(obj.getString("ends_at")) : null,
                obj.optBoolean("consumed"),
                jar
        );
    }

    public enum Type {
        PURCHASE(1),
        PREMIUM_SUBSCRIPTION(2),
        DEVELOPER_GIFT(3),
        TEST_MODE_PURCHASE(4),
        FREE_PURCHASE(5),
        USER_GIFT(6),
        PREMIUM_PURCHASE(7),
        APPLICATION_SUBSCRIPTION(8),
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
