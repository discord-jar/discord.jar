package com.seailz.javadiscordwrapper.gateway.events;

import com.seailz.javadiscordwrapper.DiscordJv;
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
import com.seailz.javadiscordwrapper.model.interaction.callback.InteractionCallbackType;
import com.seailz.javadiscordwrapper.utils.URLS;
import com.seailz.javadiscordwrapper.utils.discordapi.DiscordRequest;
import com.seailz.javadiscordwrapper.utils.discordapi.DiscordResponse;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Level;
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
    READY((p, d) -> ReadyEvent.class),
    /* Sent when a guild is created */
    GUILD_CREATE((p, d) -> GuildCreateEvent.class),
    /* Sent when a gateway connection is resumed */
    RESUMED((p, d) -> GatewayResumedEvent.class),
    /* Sent when a message is created */
    MESSAGE_CREATE((p, d) -> MessageCreateEvent.class),
    /* Sent when a command permission is updated */
    APPLICATION_COMMAND_PERMISSIONS_UPDATE((p, d) -> CommandPermissionUpdateEvent.class),
    /* Sent when an interaction is created */
    INTERACTION_CREATE((p, d) -> {
        switch (InteractionType.getType(p.getJSONObject("d").getInt("type"))) {
            case PING -> {
                Logger.getLogger("EventDispatcher")
                        .log(Level.WARNING, "[DISCORD.JV] Ping request received. This is unusual, will ACK anyway.");

                new DiscordRequest(
                        new JSONObject()
                                .put("type", InteractionCallbackType.PONG.getCode()),
                        new HashMap<>(),
                        URLS.POST.INTERACTIONS.CALLBACK.replace("{interaction.id}",
                                p.getJSONObject("d").getString("id").replace(
                                        "{interaction.token}", p.getJSONObject("d")
                                                .getString("token")
                                )),
                        d,
                        URLS.POST.INTERACTIONS.CALLBACK,
                        RequestMethod.POST
                ).invoke();
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
    UNKNOWN((p, d) -> null),
    ;

    private final BiFunction<JSONObject, DiscordJv, Class<? extends Event>> event;

    DispatchedEvents(BiFunction<JSONObject, DiscordJv, Class<? extends Event>> event) {
        this.event = event;
    }

    public BiFunction<JSONObject, DiscordJv, Class<? extends Event>> getEvent() {
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
