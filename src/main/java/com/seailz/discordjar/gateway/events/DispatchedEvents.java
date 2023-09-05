package com.seailz.discordjar.gateway.events;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.Event;
import com.seailz.discordjar.events.model.automod.AutoModExecutionEvent;
import com.seailz.discordjar.events.model.automod.rule.AutoModRuleCreateEvent;
import com.seailz.discordjar.events.model.automod.rule.AutoModRuleDeleteEvent;
import com.seailz.discordjar.events.model.automod.rule.AutoModRuleUpdateEvent;
import com.seailz.discordjar.events.model.channel.ChannelPinsUpdateEvent;
import com.seailz.discordjar.events.model.channel.edit.ChannelCreateEvent;
import com.seailz.discordjar.events.model.channel.edit.ChannelDeleteEvent;
import com.seailz.discordjar.events.model.channel.edit.ChannelUpdateEvent;
import com.seailz.discordjar.events.model.command.CommandPermissionUpdateEvent;
import com.seailz.discordjar.events.model.gateway.GatewayResumedEvent;
import com.seailz.discordjar.events.model.general.ReadyEvent;
import com.seailz.discordjar.events.model.guild.GuildCreateEvent;
import com.seailz.discordjar.events.model.guild.GuildDeleteEvent;
import com.seailz.discordjar.events.model.guild.GuildUpdateEvent;
import com.seailz.discordjar.events.model.guild.member.GuildMemberAddEvent;
import com.seailz.discordjar.events.model.guild.member.GuildMemberRemoveEvent;
import com.seailz.discordjar.events.model.guild.member.GuildMemberUpdateEvent;
import com.seailz.discordjar.events.model.interaction.button.ButtonInteractionEvent;
import com.seailz.discordjar.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.discordjar.events.model.interaction.command.MessageContextCommandInteractionEvent;
import com.seailz.discordjar.events.model.interaction.command.SlashCommandInteractionEvent;
import com.seailz.discordjar.events.model.interaction.command.UserContextCommandInteractionEvent;
import com.seailz.discordjar.events.model.interaction.modal.ModalInteractionEvent;
import com.seailz.discordjar.events.model.interaction.select.StringSelectMenuInteractionEvent;
import com.seailz.discordjar.events.model.interaction.select.entity.ChannelSelectMenuInteractionEvent;
import com.seailz.discordjar.events.model.interaction.select.entity.RoleSelectMenuInteractionEvent;
import com.seailz.discordjar.events.model.interaction.select.entity.UserSelectMenuInteractionEvent;
import com.seailz.discordjar.events.model.message.MessageCreateEvent;
import com.seailz.discordjar.events.model.message.TypingStartEvent;
import com.seailz.discordjar.gateway.GatewayFactory;
import com.seailz.discordjar.command.CommandType;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.model.component.ComponentType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.guild.Member;
import com.seailz.discordjar.model.interaction.InteractionType;
import com.seailz.discordjar.model.interaction.callback.InteractionCallbackType;
import com.seailz.discordjar.utils.TriFunction;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.voice.model.VoiceServerUpdate;
import com.seailz.discordjar.voice.model.VoiceState;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
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
 * @see com.seailz.discordjar.gateway.events.GatewayEvents
 * @since 1.0
 */
public enum DispatchedEvents {

    /* GATEWAY */
    READY((p, g, d) -> {
        Logger.getLogger("Gateway")
                .info("[Gateway] Ready to receive events");
        return ReadyEvent.class;
    }),
    RESUMED((p, d, g) -> GatewayResumedEvent.class),

    /* COMMANDS */
    APPLICATION_COMMAND_PERMISSIONS_UPDATE((p, g, d) -> CommandPermissionUpdateEvent.class),

    /* AUTOMOD */
    AUTO_MODERATION_RULE_CREATE((p, g, d) -> AutoModRuleCreateEvent.class),
    AUTO_MODERATION_RULE_UPDATE((p, g, d) -> AutoModRuleUpdateEvent.class),
    AUTO_MODERATION_RULE_DELETE((p, g, d) -> AutoModRuleDeleteEvent.class),
    AUTO_MODERATION_ACTION_EXECUTION((p, g, d) -> AutoModExecutionEvent.class),

    /* CHANNELS */
    CHANNEL_CREATE((p, g, d) -> {
        // cache
        Channel channel = Channel.decompile(p.getJSONObject("d"), d);
        d.getChannelCache().cache(channel);

        return ChannelCreateEvent.class;
    }),
    CHANNEL_UPDATE((p, g, d) -> {
        // modify cached channel, if it exists
        Channel channel = Channel.decompile(p.getJSONObject("d"), d);
        d.getChannelCache().cache(channel);

        return ChannelUpdateEvent.class;
    }),
    CHANNEL_DELETE((p, g, d) -> {
        // remove cached channel, if it exists
        Channel channel = Channel.decompile(p.getJSONObject("d"), d);
        d.getChannelCache().remove(channel);

        return ChannelDeleteEvent.class;
    }),
    CHANNEL_PINS_UPDATE((p, g, d) -> ChannelPinsUpdateEvent.class),

    //TODO: threads

    /* GUILDS */
    GUILD_CREATE((p, d, g) -> {
        // cache guild
        if (p.getJSONObject("d").has("unavailable") && p.getJSONObject("d").getBoolean("unavailable"))
            // Guild is unavailable, don't cache it
            return GuildCreateEvent.class;
        Guild guild = Guild.decompile(p.getJSONObject("d"), g);
        g.getGuildCache().cache(guild);

        JSONArray arr = p.getJSONObject("d").getJSONArray("channels");
        arr.forEach(o -> g.getChannelCache().cache(
                Channel.decompile((JSONObject) o, g)
        ));

        // Cache all members
        arr = p.getJSONObject("d").getJSONArray("members");
        long start = System.currentTimeMillis();
        arr.forEach(o -> {
            g.insertMemberCache(
                    guild.id(), Member.decompile(
                            (JSONObject) o,
                            g,
                            guild.id(),
                            guild
                    ),
                    guild
            );
        });
        if (g.isDebug()) Logger.getLogger("DiscordJar").log(Level.INFO, "Took " + (System.currentTimeMillis() - start) + "ms to cache all members");

        arr = p.getJSONObject("d").getJSONArray("voice_states");
        arr.forEach(o -> {
            JSONObject obj = (JSONObject) o;
            obj.put("guild_id", guild.id());
            g.addVoiceState(
                    VoiceState.decompile(obj, g)
            );
        });

        return GuildCreateEvent.class;
    }),
    GUILD_UPDATE((p, g, d) -> {
        // modify cached guild, if it exists
        Guild guild = Guild.decompile(p.getJSONObject("d"), d);
        d.getGuildCache().cache(guild);

        return GuildUpdateEvent.class;
    }),
    GUILD_DELETE((p, g, d) -> {
        // remove cached guild, if it exists
        Guild guild = Guild.decompile(p.getJSONObject("d"), d);
        d.getGuildCache().remove(guild);

        return GuildDeleteEvent.class;
    }),
    // TODO: other guild events
    GUILD_MEMBER_ADD((p, g, d) -> {
        String guildId = p.getJSONObject("d").getString("guild_id");
        d.insertMemberCache(guildId, Member.decompile(
                p.getJSONObject("d"),
                d,
                guildId,
                d.getGuildById(guildId)
        ), d.getGuildById(guildId));
        return GuildMemberAddEvent.class;
    }),
    GUILD_MEMBER_REMOVE((p, g, d) -> {
        String guildId = p.getJSONObject("d").getString("guild_id");
        d.removeMemberCache(guildId, p.getJSONObject("d").getJSONObject("user").getString("id"));
        return GuildMemberRemoveEvent.class;
    }),
    GUILD_MEMBER_UPDATE((p, g, d) -> {
        String guildId = p.getJSONObject("d").getString("guild_id");
        d.insertMemberCache(guildId, Member.decompile(
                p.getJSONObject("d"),
                d,
                guildId,
                d.getGuildById(guildId)
        ), d.getGuildById(guildId));
        return GuildMemberUpdateEvent.class;
    }),
    GUILD_MEMBERS_CHUNK((p, g, d) -> {
        JSONObject payload = p.getJSONObject("d");
        String nonce = payload.getString("nonce");

        if (nonce == null) {
            Logger.getLogger("DispatchedEvents").warning(
                    "[discord.jar] Received a GUILD_MEMBER_CHUNK event with no nonce. This is extremely unusual - please report this " +
                            "on our GitHub page.");
            return null;
        }

        GatewayFactory.MemberChunkStorageWrapper wrapper = g.memberRequestChunks.get(nonce);
        if (wrapper == null) {
            Logger.getLogger("DispatchedEvents").warning("[discord.jar] Received member chunk with unknown nonce: " + nonce);
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
    // TODO: other guild events

    /* INTERACTIONS */
    INTERACTION_CREATE((p, g, d) -> {
        switch (InteractionType.getType(p.getJSONObject("d").getInt("type"))) {
            case PING -> {
                Logger.getLogger("EventDispatcher")
                        .log(Level.WARNING, "[discord.jar] Ping request received. This is unusual, will ACK anyway.");

                try {
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
                } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
                    throw new DiscordRequest.DiscordAPIErrorException(e);
                }
            }
            case APPLICATION_COMMAND -> {
                CommandInteractionEvent event = null;

                switch (CommandType.fromCode(p.getJSONObject("d").getJSONObject("data").getInt("type"))) {
                    case SLASH_COMMAND ->
                            event = new SlashCommandInteractionEvent(d, GatewayFactory.sequence, p);
                    case USER -> event = new UserContextCommandInteractionEvent(d, GatewayFactory.sequence, p);
                    case MESSAGE ->
                            event = new MessageContextCommandInteractionEvent(d, GatewayFactory.sequence, p);
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
                        "[discord.jar] Unknown interaction type: " + p.getJSONObject("d").getInt("type") + ". This is usually because of an outdated framework version. Please update discord.jar");
                return null;
            }
        }
        return null;
    }),

    // TODO: integrations

    // TODO: invites

    /* MESSAGES */
    MESSAGE_CREATE((p, d, g) -> MessageCreateEvent.class),
    // TODO: other message events

    // TODO: Presence update

    // TODO: stage instance

    /* TYPING */
    TYPING_START((p, d, g) -> TypingStartEvent.class),

    // TODO: User update

    VOICE_STATE_UPDATE((p, g, d) -> {
        VoiceState update = VoiceState.decompile(p.getJSONObject("d"), d);
        g.getOnVoiceStateUpdateListeners().forEach(lis -> lis.accept(update));

        d.updateVoiceState(update);

        // TODO: Create a VoiceStateUpdateEvent
        return null;
    }),

    VOICE_SERVER_UPDATE((p, g, d) -> {
        VoiceServerUpdate update = VoiceServerUpdate.decompile(p.getJSONObject("d"));
        g.getOnVoiceServerUpdateListeners().forEach(lis -> lis.accept(update));
        // TODO: Create a VoiceServerUpdateEvent
        return null;
    }),

    // TODO: Webhooks

    /* Unknown */
    UNKNOWN((p, g, d) -> null),
    ;

    private final TriFunction<JSONObject, GatewayFactory, DiscordJar, Class<? extends Event>> event;

    DispatchedEvents(TriFunction<JSONObject, GatewayFactory, DiscordJar, Class<? extends Event>> event) {
        this.event = event;
    }

    public TriFunction<JSONObject, GatewayFactory, DiscordJar, Class<? extends Event>> getEvent() {
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
