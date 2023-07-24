package com.seailz.discordjar.model.message.activity;

import com.seailz.discordjar.core.Compilerable;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

public record MessageActivity(
        MessageActivityType type,
        String partyId
) implements Compilerable {

    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("type", type.getCode());
        obj.put("party_id", partyId);
        return obj;
    }

    @NonNull
    public static MessageActivity decompile(JSONObject obj) {
        MessageActivityType type;
        String partyId;

        try {
            type = MessageActivityType.fromCode(obj.getInt("type"));
        } catch (Exception e) {
            type = null;
        }

        try {
            partyId = obj.getString("party_id");
        } catch (Exception e) {
            partyId = null;
        }
        return new MessageActivity(type, partyId);
    }
}
