package com.seailz.discordjar.model.scopes;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.flag.BitwiseUtil;
import com.seailz.discordjar.utils.json.SJSONObject;
import com.seailz.discordjar.utils.permission.Permission;
import com.seailz.discordjar.utils.json.SJSONArray;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public record InstallParams(EnumSet<Scope> scopes, EnumSet<Permission> permissions) implements Compilerable {

    @Override
    public SJSONObject compile() {
        int permissions = 0;

        for (Permission permission : this.permissions) {
            permissions += permission.code();
        }

        String scopes;

        if (this.scopes == null) scopes = null;
        else scopes = this.scopes.stream().map(Scope::name).collect(Collectors.joining(" "));

        return new SJSONObject()
                .put("scopes", scopes)
                .put("permissions", permissions);
    }

    public static InstallParams decompile(SJSONObject obj) {
        EnumSet<Scope> scopes = EnumSet.noneOf(Scope.class);
        EnumSet<Permission> permissions = EnumSet.noneOf(Permission.class);

        if (obj.has("scopes") && !obj.isNull("scopes")) {
            SJSONArray scopesArray = obj.getJSONArray("scopes");
            List<String> scopesList = new ArrayList<>();
            for (int i = 0; i < scopesArray.length(); i++) {
                scopesList.add(scopesArray.get(i).toString());
            }
            List<Scope> scopeEnums = new ArrayList<>();
            for (String scope : scopesList) {
                Scope scopeEnum;

                try {
                    scopeEnum = Scope.valueOf(scope.toUpperCase());
                } catch (IllegalArgumentException e) {
                    continue;
                }

                scopeEnums.add(scopeEnum);
            }
            scopes.addAll(scopeEnums);
        }

        if (obj.has("permissions") && !obj.isNull("permissions")) {
            BitwiseUtil<Permission> bitwiseUtil = new BitwiseUtil<>();
            permissions = bitwiseUtil.get(Long.parseLong(obj.getString("permissions")), Permission.class);
        }

        return new InstallParams(scopes, permissions);
    }
}
