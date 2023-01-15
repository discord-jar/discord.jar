package com.seailz.discordjv.command.permissions;

import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.utils.Snowflake;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used to represent the data of an application command permission.
 *
 * @param id         The id of the role, user, or channel.
 * @param type       The type of the permission.
 * @param permission true to allow, false, to disallow
 * @author Seailz
 * @see ApplicationCommandPermissions
 * @since 1.0
 */
public record ApplicationCommandPermissionsData(
        String id,
        ApplicationCommandPermissionType type,
        boolean permission
) implements Compilerable, Snowflake {

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("id", id)
                .put("type", type.getCode())
                .put("permission", permission);
    }

    @NotNull
    public static ApplicationCommandPermissionsData decompile(@NotNull JSONObject obj) {
        String id;
        ApplicationCommandPermissionType type;
        boolean permission;

        try {
            id = obj.getString("id");
        } catch (JSONException e) {
            id = null;
        }

        try {
            type = ApplicationCommandPermissionType.getType(obj.getInt("type"));
        } catch (JSONException e) {
            type = null;
        }

        try {
            permission = obj.getBoolean("permission");
        } catch (JSONException e) {
            permission = false;
        }

        return new ApplicationCommandPermissionsData(id, type, permission);
    }
}
