package com.seailz.discordjar.model.scopes;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.flag.BitwiseUtil;
import com.seailz.discordjar.utils.permission.Permission;
import jakarta.el.MethodReference;
import org.json.JSONObject;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public record InstallParams(EnumSet<Scope> scopes, EnumSet<Permission> permissions) implements Compilerable {

    @Override
    public JSONObject compile() {
        int permissions = 0;

        for (Permission permission : this.permissions) {
            permissions += permission.code();
        }

        String scopes;

        if (this.scopes == null) scopes = null;
        else scopes = this.scopes.stream().map(Scope::name).collect(Collectors.joining(" "));

        return new JSONObject()
                .put("scopes", scopes)
                .put("permissions", permissions);
    }

    public static InstallParams decompile(JSONObject obj) {
        EnumSet<Scope> scopes = EnumSet.noneOf(Scope.class);
        EnumSet<Permission> permissions = EnumSet.noneOf(Permission.class);

        if (obj.has("scopes") && !obj.isNull("scopes")) scopes = obj.getJSONArray("scopes").toList().stream().map(Object::toString).toList().
                stream().map(Scope::valueOf).collect(Collectors.toCollection(() -> EnumSet.noneOf(Scope.class)));

        if (obj.has("permissions") && !obj.isNull("permissions")) {
            BitwiseUtil<Permission> bitwiseUtil = new BitwiseUtil<>();
            permissions = bitwiseUtil.get(Long.parseLong(obj.getString("permissions")), Permission.class);
        }

        return new InstallParams(scopes, permissions);
    }
}
