package com.seailz.discordjv.gateway.events;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.events.model.Event;
import com.seailz.discordjv.events.model.command.CommandPermissionUpdateEvent;
import com.seailz.discordjv.events.model.gateway.GatewayResumedEvent;
import com.seailz.discordjv.events.model.general.ReadyEvent;
import com.seailz.discordjv.events.model.guild.GuildCreateEvent;
import com.seailz.discordjv.events.model.interaction.button.ButtonInteractionEvent;
import com.seailz.discordjv.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.discordjv.events.model.interaction.command.MessageContextCommandInteractionEvent;
import com.seailz.discordjv.events.model.interaction.command.SlashCommandInteractionEvent;
import com.seailz.discordjv.events.model.interaction.command.UserContextCommandInteractionEvent;
import com.seailz.discordjv.events.model.interaction.modal.ModalInteractionEvent;
import com.seailz.discordjv.events.model.interaction.select.StringSelectMenuInteractionEvent;
import com.seailz.discordjv.events.model.interaction.select.entity.ChannelSelectMenuInteractionEvent;
import com.seailz.discordjv.events.model.interaction.select.entity.RoleSelectMenuInteractionEvent;
import com.seailz.discordjv.events.model.interaction.select.entity.UserSelectMenuInteractionEvent;
import com.seailz.discordjv.events.model.message.MessageCreateEvent;
import com.seailz.discordjv.gateway.GatewayFactory;
import com.seailz.discordjv.command.CommandType;
import com.seailz.discordjv.model.component.ComponentType;
import com.seailz.discordjv.model.guild.Member;
import com.seailz.discordjv.model.interaction.InteractionType;
import com.seailz.discordjv.model.interaction.callback.InteractionCallbackType;
import com.seailz.discordjv.utils.TriFunction;
import com.seailz.discordjv.utils.URLS;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a gateway event that is fired by the Discord API
 * These events are not things like "send heartbeat", "send identify", "hello", (which can be found here {@link GatewayEvents})
 * but a rather events that fall under the "dispatch" category.
 * These events are things like "message create", "message update", "message delete", etc.
 * <p>
 * This is an internal class and should not be used by the end user.
 *
 * @author Seailz
 * @see com.seailz.discordjv.gateway.events.GatewayEvents
 * @since 1.0
 */
public enum DispatchedEvents {

    /* Sent when bot is ready to receive events */
    READY((p, g, d) -> ReadyEvent.class),
    /* Sent when a guild is created */
    GUILD_CREATE((p, d, g) -> GuildCreateEvent.class),
    /* Sent when a gateway connection is resumed */
    RESUMED((p, d, g) -> GatewayResumedEvent.class),
    /* Sent when a message is created */
    MESSAGE_CREATE((p, d, g) -> MessageCreateEvent.class),
    /* Sent when a command permission is updated */
    APPLICATION_COMMAND_PERMISSIONS_UPDATE((p, g, d) -> CommandPermissionUpdateEvent.class),
    /* Sent when an interaction is created */
    INTERACTION_CREATE((p, g, d) -> {
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
                CommandInteractionEvent event = null;

                switch (CommandType.fromCode(p.getJSONObject("d").getJSONObject("data").getInt("type"))) {
                    case SLASH_COMMAND ->
                            event = new SlashCommandInteractionEvent(d, GatewayFactory.getLastSequence(), p);
                    case USER -> event = new UserContextCommandInteractionEvent(d, GatewayFactory.getLastSequence(), p);
                    case MESSAGE ->
                            event = new MessageContextCommandInteractionEvent(d, GatewayFactory.getLastSequence(), p);
                }

                d.getCommandDispatcher().dispatch(p.getJSONObject("d").getJSONObject("data").getString("name"),
                        event);
                return CommandInteractionEvent.class;
            }
            case MESSAGE_COMPONENT -> {
                switch (ComponentType.getType(p.getJSONObject("d").getJSONObject("data").getInt("component_type"))) {
                    case BUTTON -> {
                        return ButtonInteractionEvent.class;
                    }
                    case STRING_SELECT -> {
                        return StringSelectMenuInteractionEvent.class;
                    }
                    case ROLE_SELECT -> {
                        return RoleSelectMenuInteractionEvent.class;
                    }
                    case USER_SELECT -> {
                        return UserSelectMenuInteractionEvent.class;
                    }
                    case CHANNEL_SELECT -> {
                        return ChannelSelectMenuInteractionEvent.class;
                    }

                }
            }
            case APPLICATION_COMMAND_AUTOCOMPLETE -> {

            }
            case MODAL_SUBMIT -> {
                return ModalInteractionEvent.class;
            }
            case UNKNOWN -> {
                Logger.getLogger("DispatchedEvents").warning(
                        "[DISCORD.JV] Unknown interaction type: " + p.getJSONObject("d").getInt("type") + ". This is usually because of an outdated framework version. Please update Discord.jv");
                return null;
            }
        }
        return null;
    }),

    GUILD_MEMBERS_CHUNK((p, g, d) -> {
        JSONObject payload = p.getJSONObject("d");
        String nonce = payload.getString("nonce");

        if (nonce == null) {
            Logger.getLogger("DispatchedEvents").warning(
                    "[DISCORD.JV] Received a GUILD_MEMBER_CHUNK event with no nonce. This is extremely unusual - please report this " +
                            "on our GitHub page.");
            return null;
        }

        GatewayFactory.MemberChunkStorageWrapper wrapper = g.memberRequestChunks.get(nonce);
        if (wrapper == null) {
            Logger.getLogger("DispatchedEvents").warning("[DISCORD.JV] Received member chunk with unknown nonce: " + nonce);
            return null;
        }

        JSONArray members = payload.getJSONArray("members");
        members.forEach(member -> {
            wrapper.addMember(Member.decompile((JSONObject) member, d, payload.getString("guild_id"), d.getGuildById(payload.getString("guild_id"))));
        });

        int chunkCount = payload.getInt("chunk_count") - 1;
        int chunkIndex = payload.getInt("chunk_index");

        if (chunkCount == chunkIndex) {
            g.memberRequestChunks.get(nonce).complete();
            g.memberRequestChunks.remove(nonce);
        }
        return null;
    }),
    /* Unknown */
    UNKNOWN((p, g, d) -> null),
    ;

    private final TriFunction<JSONObject, GatewayFactory, DiscordJv, Class<? extends Event>> event;

    DispatchedEvents(TriFunction<JSONObject, GatewayFactory, DiscordJv, Class<? extends Event>> event) {
        this.event = event;
    }

    public TriFunction<JSONObject, GatewayFactory, DiscordJv, Class<? extends Event>> getEvent() {
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
