package com.seailz.discordjv.model.permission;

import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.utils.Snowflake;
import com.seailz.discordjv.utils.flag.BitwiseUtil;
import com.seailz.discordjv.utils.permission.Permission;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

import java.util.EnumSet;

public record PermissionOverwrite(
        String id,
        OverwriteType type,
        EnumSet<Permission> allow,
        EnumSet<Permission> deny
) implements Compilerable, Snowflake {

    public PermissionOverwrite(String id, OverwriteType type) {
        this(id, type, EnumSet.noneOf(Permission.class), EnumSet.noneOf(Permission.class));
    }

    @Override
    public JSONObject compile() {
        int allowInt = 0;
        int denyInt = 0;

        for (Permission permission : allow) {
            allowInt += permission.getLeftShiftId();
        }

        for (Permission permission : deny) {
            denyInt += permission.getLeftShiftId();
        }

        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("type", type.getCode());
        obj.put("allow", allowInt);
        obj.put("deny", denyInt);
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

        EnumSet<Permission> allowList = null;
        EnumSet<Permission> denyList = null;

        BitwiseUtil<Permission> bitwiseUtil = new BitwiseUtil<>();
        allowList = bitwiseUtil.get(allow, Permission.class);
        denyList = bitwiseUtil.get(deny, Permission.class);


        return new PermissionOverwrite(id, type, allowList, denyList);
    }

    public PermissionOverwrite allow(Permission permission) {
        allow.add(permission);
        return this;
    }

    public PermissionOverwrite deny(Permission permission) {
        deny.add(permission);
        return this;
    }
}
