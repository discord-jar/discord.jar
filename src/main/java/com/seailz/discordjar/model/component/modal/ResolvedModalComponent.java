package com.seailz.discordjar.model.component.modal;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.component.ComponentType;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public record ResolvedModalComponent(
        String customId,
        ComponentType type,
        String value
) implements Compilerable {
    @NotNull
    public static ResolvedModalComponent decompile(JSONObject obj) {
        return new ResolvedModalComponent(
                obj.getString("custom_id"),
                ComponentType.getType(obj.getInt("type")),
                obj.getString("value")
        );
    }

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("custom_id", customId)
                .put("type", type.getCode())
                .put("value", value);
    }
}
