package com.seailz.javadiscordwrapper.model.permission;

import com.seailz.javadiscordwrapper.core.Compilerable;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

public record PermissionOverwrite(
        String id,
        OverwriteType type,
        int allow,
        int deny
) implements Compilerable {

    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("type", type);
        obj.put("allow", allow);
        obj.put("deny", deny);
        return obj;
    }

    @NonNull
    public static PermissionOverwrite decompile(JSONObject obj) {
        String id;
        OverwriteType type;
        int allow;
        int deny;

        try {
            id = obj.getString("id");
        } catch (Exception e) {
            id = null;
        }

        try {
            type = OverwriteType.fromCode(obj.getInt("type"));
        } catch (Exception e) {
            type = null;
        }

        try {
            allow = obj.getInt("allow");
        } catch (Exception e) {
            allow = 0;
        }

        try {
            deny = obj.getInt("deny");
        } catch (Exception e) {
            deny = 0;
        }

        return new PermissionOverwrite(id, type, allow, deny);
    }
}
