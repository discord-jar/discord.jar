package com.seailz.discordjar.model.message;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.interaction.IntegrationType;
import com.seailz.discordjar.model.interaction.InteractionType;
import org.json.JSONObject;

import java.util.HashMap;

public record MessageInteractionMetadataObject(
        String id,
        InteractionType type,
        String userId,
        HashMap<IntegrationType, String> authorizingIntegrationOwners,
        String originalResponseMessageId,
        MessageInteractionMetadataObject triggeringInteractionMetadata
) implements Compilerable {
    @Override
    public JSONObject compile() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("type", type.getCode());
        object.put("user_id", userId);
        object.put("authorizing_integration_owners", authorizingIntegrationOwners);
        object.put("original_response_message_id", originalResponseMessageId);
        object.put("triggering_interaction_metadata", triggeringInteractionMetadata.compile());
        return object;
    }

    public static MessageInteractionMetadataObject decompile(JSONObject object) {
        HashMap<IntegrationType, String> authorizingIntegrationOwners = new HashMap<>();

        if (object.has("authorizing_integration_owners"))
            object.getJSONObject("authorizing_integration_owners").toMap()
                    .forEach((key, value) -> authorizingIntegrationOwners.put(IntegrationType.fromCode(Integer.parseInt(key)), String.valueOf(value)));


        return new MessageInteractionMetadataObject(
                object.has("id") ? object.getString("id") : null,
                object.has("type") ? InteractionType.getType(object.getInt("type")) : null,
                object.has("user_id") ? object.getString("user_id") : null,
                authorizingIntegrationOwners,
                object.has("original_response_message_id") ? object.getString("original_response_message_id") : null,
                object.has("triggering_interaction_metadata") ? decompile(object.getJSONObject("triggering_interaction_metadata")) : null
        );
    }
}
