package com.seailz.javadiscordwrapper.model.scopes;

import com.seailz.javadiscordwrapper.core.Compilerable;
import org.json.JSONObject;

import java.util.List;

// TODO: make enum for scopes
// TODO: make enum for bot perms
public record InstallParams(List<String> scopes, String permissions) implements Compilerable {

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("scopes", scopes)
                .put("permissions", permissions);
    }

    public static InstallParams decompile(JSONObject obj) {
        List<String> scopes;
        String permissions;

        try {
            scopes = obj.getJSONArray("scopes").toList().stream().map(Object::toString).toList();
        } catch (Exception e) {
            scopes = null;
        }

        try {
            permissions = obj.getString("permissions");
        } catch (Exception e) {
            permissions = null;
        }

        return new InstallParams(scopes, permissions);
    }
}
