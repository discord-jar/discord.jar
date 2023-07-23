package com.seailz.discordjar.command.permissions;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.Snowflake;
import org.jetbrains.annotations.NotNull;
import com.seailz.discordjar.utils.json.SJSONArray;
import org.json.JSONException;
import com.seailz.discordjar.utils.json.SJSONObject;

/**
 * This class is used to represent an application command permission.
 *
 * @param id            The id of the command or the application ID
 * @param applicationId The id of the application the command belongs to
 * @param guildId       The id of the guild
 * @param permissions   Permissions for the command in the guild, max of 100
 * @author Seailz
 * @see ApplicationCommandPermissionsData
 * @since 1.0
 */
public record ApplicationCommandPermissions(
        String id,
        String applicationId,
        String guildId,
        ApplicationCommandPermissionsData[] permissions
) implements Compilerable, Snowflake {

    @Override
    public SJSONObject compile() {
        SJSONArray permissionsArray = new SJSONArray();
        for (ApplicationCommandPermissionsData permission : permissions) {
            permissionsArray.put(permission.compile());
        }

        return new SJSONObject()
                .put("id", id)
                .put("application_id", applicationId)
                .put("guild_id", guildId)
                .put("permissions", permissionsArray);
    }

    @NotNull
    public static ApplicationCommandPermissions decompile(@NotNull SJSONObject obj) {
        String id;
        String applicationId;
        String guildId;
        ApplicationCommandPermissionsData[] permissions;

        try {
            id = obj.getString("id");
        } catch (JSONException e) {
            id = null;
        }

        try {
            applicationId = obj.getString("application_id");
        } catch (JSONException e) {
            applicationId = null;
        }

        try {
            guildId = obj.getString("guild_id");
        } catch (JSONException e) {
            guildId = null;
        }

        try {
            SJSONArray permissionsArray = obj.getJSONArray("permissions");
            permissions = new ApplicationCommandPermissionsData[permissionsArray.length()];
            for (int i = 0; i < permissionsArray.length(); i++) {
                permissions[i] = ApplicationCommandPermissionsData.decompile(permissionsArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            permissions = null;
        }

        return new ApplicationCommandPermissions(id, applicationId, guildId, permissions);
    }

}
