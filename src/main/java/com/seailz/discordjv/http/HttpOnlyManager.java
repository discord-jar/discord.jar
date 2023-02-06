package com.seailz.discordjv.http;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.command.CommandType;
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
import com.seailz.discordjv.gateway.GatewayFactory;
import com.seailz.discordjv.model.component.ComponentType;
import com.seailz.discordjv.model.interaction.Interaction;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.DecoderException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
public class HttpOnlyManager {

    private static DiscordJv discordJv;
    private static String endpoint;
    private static String applicationPublicKey;

    public static void init(DiscordJv discordJv, String endpoint, String applicationPublicKey) {
        HttpOnlyManager.discordJv = discordJv;
        HttpOnlyManager.endpoint = endpoint;
        HttpOnlyManager.applicationPublicKey = applicationPublicKey;
    }

    @PostMapping("/*")
    public ResponseEntity<String> get(HttpServletRequest request) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, DecoderException {
        String path = request.getRequestURI();
        if (!path.endsWith(endpoint)) {
            return ResponseEntity.notFound().build();
        }

        // Retrieve the signature, timestamp, and body from the headers
        String signatureFromHeaders = request.getHeader("X-Signature-Ed25519");
        String timestampFromHeaders = request.getHeader("X-Signature-Timestamp");
        String body = request.getReader().lines().collect(Collectors.joining());

        boolean verified = SecurityManager.verify(applicationPublicKey, signatureFromHeaders, timestampFromHeaders, body);

        if (!verified) {
            // The signature is invalid
            return ResponseEntity.status(401).build();
        }

        // the signature is valid, continue.


        // handle interaction request
        Interaction interaction = Interaction.decompile(new JSONObject(body), discordJv);
        switch (interaction.type()) {
            case PING -> {
                return ResponseEntity.ok("{\"type\": 1}");
            }
            case APPLICATION_COMMAND -> {
                CommandInteractionEvent event = null;

                switch (CommandType.fromCode(new JSONObject(interaction.raw()).getJSONObject("data").getInt("type"))) {
                    case SLASH_COMMAND -> {
                        event = new SlashCommandInteractionEvent(discordJv, GatewayFactory.sequence, new JSONObject().put("d", new JSONObject(body)));
                    }
                    case USER ->
                            event = new UserContextCommandInteractionEvent(discordJv, GatewayFactory.sequence, new JSONObject().put("d", new JSONObject(body)));
                    case MESSAGE ->
                            event = new MessageContextCommandInteractionEvent(discordJv, GatewayFactory.sequence, new JSONObject().put("d", new JSONObject(body)));
                }

                discordJv.getCommandDispatcher().dispatch(new JSONObject(interaction.raw()).getJSONObject("data").getString("name"), event);
            }
            case MESSAGE_COMPONENT -> {
                switch (ComponentType.getType(new JSONObject().put("d", new JSONObject(body)).getJSONObject("d").getJSONObject("data").getInt("component_type"))) {
                    case BUTTON -> {
                        ButtonInteractionEvent event = new ButtonInteractionEvent(discordJv, 0L, new JSONObject().put("d", new JSONObject(body)));
                        discordJv.getEventDispatcher().dispatchEvent(event, ButtonInteractionEvent.class, discordJv);
                    }
                    case STRING_SELECT -> {
                        StringSelectMenuInteractionEvent event = new StringSelectMenuInteractionEvent(discordJv, 0L, new JSONObject().put("d", new JSONObject(body)));
                        discordJv.getEventDispatcher().dispatchEvent(event, StringSelectMenuInteractionEvent.class, discordJv);
                    }
                    case ROLE_SELECT -> {
                        RoleSelectMenuInteractionEvent event = new RoleSelectMenuInteractionEvent(discordJv, 0L, new JSONObject().put("d", new JSONObject(body)));
                        discordJv.getEventDispatcher().dispatchEvent(event, RoleSelectMenuInteractionEvent.class, discordJv);
                    }
                    case USER_SELECT -> {
                        UserSelectMenuInteractionEvent event = new UserSelectMenuInteractionEvent(discordJv, 0L, new JSONObject().put("d", new JSONObject(body)));
                        discordJv.getEventDispatcher().dispatchEvent(event, UserSelectMenuInteractionEvent.class, discordJv);
                    }
                    case CHANNEL_SELECT -> {
                        ChannelSelectMenuInteractionEvent event = new ChannelSelectMenuInteractionEvent(discordJv, 0L, new JSONObject().put("d", new JSONObject(body)));
                        discordJv.getEventDispatcher().dispatchEvent(event, ChannelSelectMenuInteractionEvent.class, discordJv);
                    }

                }
            }
            case APPLICATION_COMMAND_AUTOCOMPLETE -> {

            }
            case MODAL_SUBMIT -> {
                ModalInteractionEvent event = new ModalInteractionEvent(discordJv, 0L, new JSONObject().put("d", new JSONObject(body)));
                discordJv.getEventDispatcher().dispatchEvent(event, ModalInteractionEvent.class, discordJv);
            }
            case UNKNOWN -> {
                Logger.getLogger("DispatchedEvents").warning(
                        "[DISCORD.JV] Unknown interaction type: " + new JSONObject().put("d", new JSONObject(body)).getJSONObject("d").getInt("type") + ". This is usually because of an outdated framework version. Please update discord.jv");
                return null;
            }
        }


        return ResponseEntity.ok("{\"type\": 1}");
    }

}

