package com.seailz.discordjar.events.model.command;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.command.permissions.ApplicationCommandPermissions;
import com.seailz.discordjar.events.model.Event;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Sent when the permissions of an application command are updated.
 *
 * @author Seailz
 * @see com.seailz.discordjar.events.DiscordListener
 * @since 1.0
 */
public class CommandPermissionUpdateEvent extends Event {
    public CommandPermissionUpdateEvent(@NotNull DiscordJar bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    @NotNull
    public ApplicationCommandPermissions getPermissions() {
        return ApplicationCommandPermissions.decompile(getJson().getJSONObject("d"));
    }

}
