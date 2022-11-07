package com.seailz.javadiscordwrapper.events.model.command;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.events.model.Event;
import com.seailz.javadiscordwrapper.model.commands.permissions.ApplicationCommandPermissions;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Sent when the permissions of an application command are updated.
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.events.DiscordListener
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
