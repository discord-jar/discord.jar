package com.seailz.javadiscordwrapper.gateway.events;

import com.seailz.javadiscordwrapper.events.model.Event;
import com.seailz.javadiscordwrapper.events.model.command.CommandPermissionUpdateEvent;
import com.seailz.javadiscordwrapper.events.model.gateway.GatewayResumedEvent;
import com.seailz.javadiscordwrapper.events.model.general.ReadyEvent;
import com.seailz.javadiscordwrapper.events.model.guild.GuildCreateEvent;
import com.seailz.javadiscordwrapper.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.javadiscordwrapper.events.model.interaction.select.entity.RoleSelectMenuInteractionEvent;
import com.seailz.javadiscordwrapper.events.model.interaction.select.StringSelectMenuInteractionEvent;
import com.seailz.javadiscordwrapper.events.model.message.MessageCreateEvent;
import com.seailz.javadiscordwrapper.model.component.ComponentType;
import com.seailz.javadiscordwrapper.model.interaction.InteractionType;
import org.json.JSONObject;

import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Represents a gateway event that is fired by the Discord API
 * These events are not things like "send heartbeat", "send identify", "hello", (which can be found here {@link GatewayEvents})
 * but a rather events that fall under the "dispatch" category.
 * These events are things like "message create", "message update", "message delete", etc.
 *
 * This is an internal class and should not be used by the end user.
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.gateway.events.GatewayEvents
 */
public enum DispatchedEvents {

    /* Sent when bot is ready to receive events */
    READY((p) -> ReadyEvent.class),
    /* Sent when a guild is created */
    GUILD_CREATE((p) -> GuildCreateEvent.class),
    /* Sent when a gateway connection is resumed */
    RESUMED((p) -> GatewayResumedEvent.class),
    /* Sent when a message is created */
    MESSAGE_CREATE((p) -> MessageCreateEvent.class),
    /* Sent when a command permission is updated */
    APPLICATION_COMMAND_PERMISSIONS_UPDATE((p) -> CommandPermissionUpdateEvent.class),
    /* Sent when an interaction is created */
    INTERACTION_CREATE((p) -> {
        switch (InteractionType.getType(p.getJSONObject("d").getInt("type"))) {
            case PING -> {

            }
            case APPLICATION_COMMAND -> {
                return CommandInteractionEvent.class;
            }
            case MESSAGE_COMPONENT -> {
                switch (ComponentType.getType(p.getJSONObject("d").getJSONObject("data").getInt("component_type"))) {
                    case BUTTON -> {

                    }
                    case STRING_SELECT -> {
                        return StringSelectMenuInteractionEvent.class;
                    }
                    case ROLE_SELECT -> {
                        return RoleSelectMenuInteractionEvent.class;
                    }

                }
            }
            case APPLICATION_COMMAND_AUTOCOMPLETE -> {

            }
            case MODAL_SUBMIT -> {

            }
            case UNKNOWN -> {
                Logger.getLogger("DispatchedEvents").warning(
                        "[DISCORD.JV] Unknown interaction type: " + p.getJSONObject("d").getInt("type") + ". This is usually because of an outdated framework version. Please update Discord.jv");
                return null;
            }
        }
        return null;
    }),
    /* Unknown */
    UNKNOWN((p) -> null),
    ;

    private final Function<JSONObject, Class<? extends Event>> event;

    DispatchedEvents(Function<JSONObject, Class<? extends Event>> event) {
        this.event = event;
    }

    public Function<JSONObject, Class<? extends Event>> getEvent() {
        return event;
    }

    public static DispatchedEvents getEventByName(String name) {
        for (DispatchedEvents event : values()) {
            if (event.name().equalsIgnoreCase(name)) {
                return event;
            }
        }
        return UNKNOWN;
    }

}
