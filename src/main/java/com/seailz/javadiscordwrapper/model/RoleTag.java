package com.seailz.javadiscordwrapper.model;

import com.seailz.javadiscordwrapper.core.Compilerable;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

public record RoleTag(
        String botId,
        String integrationId,
        boolean isPremiumSubscriber
) implements Compilerable {

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("bot_id", botId)
                .put("integration_id", integrationId)
                .put("is_premium_subscriber", isPremiumSubscriber);
    }

    @NonNull
    public static RoleTag decompile(JSONObject obj) {
        String botId;
        String integrationId;
        boolean isPremiumSubscriber;

        try {
            botId = obj.getString("bot_id");
        } catch (Exception e) {
            botId = null;
        }

        try {
            integrationId = obj.getString("integration_id");
        } catch (Exception e) {
            integrationId = null;
        }

        try {
            isPremiumSubscriber = obj.getBoolean("is_premium_subscriber");
        } catch (Exception e) {
            isPremiumSubscriber = false;
        }

        return new RoleTag(botId, integrationId, isPremiumSubscriber);
    }

}
