package com.seailz.javadiscordwrapper.model.component.modal;

import com.seailz.javadiscordwrapper.core.Compilerable;
import com.seailz.javadiscordwrapper.model.component.ComponentType;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public record ResolvedModalComponent(
        String customId,
        ComponentType type,
        String value
) implements Compilerable {
    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("custom_id", customId)
                .put("type", type.getCode())
                .put("value", value);
    }

    @NotNull
    public static ResolvedModalComponent decompile(JSONObject obj) {
        return new ResolvedModalComponent(
                obj.getString("custom_id"),
                ComponentType.getType(obj.getInt("type")),
                obj.getString("value")
        );
    }
}
