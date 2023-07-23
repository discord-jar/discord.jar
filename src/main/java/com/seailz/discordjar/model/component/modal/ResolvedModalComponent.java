package com.seailz.discordjar.model.component.modal;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.component.ComponentType;
import com.seailz.discordjar.utils.json.SJSONObject;
import org.jetbrains.annotations.NotNull;

public record ResolvedModalComponent(
        String customId,
        ComponentType type,
        String value
) implements Compilerable {
    @Override
    public SJSONObject compile() {
        return new SJSONObject()
                .put("custom_id", customId)
                .put("type", type.getCode())
                .put("value", value);
    }

    @NotNull
    public static ResolvedModalComponent decompile(SJSONObject obj) {
        return new ResolvedModalComponent(
                obj.getString("custom_id"),
                ComponentType.getType(obj.getInt("type")),
                obj.getString("value")
        );
    }
}
