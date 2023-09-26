package com.seailz.discordjar.model.role;

import com.seailz.discordjar.core.Compilerable;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

public record RoleTag(
        String botId,
        String integrationId,
        boolean isPremiumSubscriber,
        String subscriptionListingId, // the id of this role's subscription sku and listing
        boolean availableForPurchase, // whether this role is available for purchase
        boolean guildConnections // whether this role is a guild's linked role
) implements Compilerable {

    @NonNull
    public static RoleTag decompile(JSONObject obj) {
        String botId = null;
        String integrationId = null;
        boolean isPremiumSubscriber;
        String subscriptionListingId = null;
        boolean availableForPurchase = false;
        boolean guildConnections = false;

        if (obj.has("bot_id") && !obj.isNull("bot_id")) botId = obj.getString("bot_id");
        if (obj.has("integration_id") && !obj.isNull("integration_id")) integrationId = obj.getString("integration_id");
        if (obj.has("subscription_listing_id") && !obj.isNull("subscription_listing_id"))
            subscriptionListingId = obj.getString("subscription_listing_id");
        isPremiumSubscriber = obj.has("premium_subscriber");
        availableForPurchase = obj.has("available_for_purchase");
        guildConnections = obj.has("guild_connections");

        return new RoleTag(botId, integrationId, isPremiumSubscriber, subscriptionListingId, availableForPurchase, guildConnections);
    }

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("bot_id", botId)
                .put("integration_id", integrationId)
                .put("is_premium_subscriber", isPremiumSubscriber);
    }

}
