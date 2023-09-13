package com.seailz.discordjar.model.role;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.model.JSONProp;
import com.seailz.discordjar.utils.model.Model;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

public class RoleTag implements Model {
    @JSONProp("bot_id")
    private String botId;
    @JSONProp("integration_id")
    private String integrationId;
    @JSONProp("premium_subscriber")
    private boolean isPremiumSubscriber;
    @JSONProp("subscription_listing_id")
    private String subscriptionListingId;
    @JSONProp("available_for_purchase")
    private boolean availableForPurchase;
    @JSONProp("guild_connections")
    private boolean guildConnections;

    private RoleTag() {}

    public String botId() {
        return botId;
    }

    public String integrationId() {
        return integrationId;
    }

    public boolean isPremiumSubscriber() {
        return isPremiumSubscriber;
    }

    public String subscriptionListingId() {
        return subscriptionListingId;
    }

    public boolean availableForPurchase() {
        return availableForPurchase;
    }

    public boolean guildConnections() {
        return guildConnections;
    }
}
