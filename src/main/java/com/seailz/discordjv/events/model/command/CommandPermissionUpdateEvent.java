package com.seailz.discordjv.events.model.command;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.events.model.Event;
import com.seailz.discordjv.command.permissions.ApplicationCommandPermissions;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Sent when the permissions of an application command are updated.
 *
 * @author Seailz
 * @see com.seailz.discordjv.events.DiscordListener
 * @since 1.0
 */
public class CommandPermissionUpdateEvent extends Event {
    public CommandPermissionUpdateEvent(@NotNull DiscordJv bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    @NotNull
    public ApplicationCommandPermissions getPermissions() {
        return ApplicationCommandPermissions.decompile(getJson().getJSONObject("d"));
    }

}
