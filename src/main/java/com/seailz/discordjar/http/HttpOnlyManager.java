package com.seailz.discordjar.http;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.command.CommandType;
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
import com.seailz.discordjar.gateway.GatewayFactory;
import com.seailz.discordjar.model.component.ComponentType;
import com.seailz.discordjar.model.interaction.Interaction;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.DecoderException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    private static DiscordJar discordJar;
    private static String endpoint;
    private static String applicationPublicKey;

    public static void init(DiscordJar discordJar, String endpoint, String applicationPublicKey) {
        HttpOnlyManager.discordJar = discordJar;
        HttpOnlyManager.endpoint = endpoint;
        HttpOnlyManager.applicationPublicKey = applicationPublicKey;
    }

    @PostMapping("/*")
    @GetMapping("/*")
    public ResponseEntity<String> get(HttpServletRequest request) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, DecoderException, DiscordRequest.UnhandledDiscordAPIErrorException {
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
        Interaction interaction = Interaction.decompile(new JSONObject(body), discordJar);
        switch (interaction.type()) {
            case PING -> {
                return ResponseEntity.ok("{\"type\": 1}");
            }
            case APPLICATION_COMMAND -> {
                CommandInteractionEvent event = null;

                switch (CommandType.fromCode(new JSONObject(interaction.raw()).getJSONObject("data").getInt("type"))) {
                    case SLASH_COMMAND -> {
                        event = new SlashCommandInteractionEvent(discordJar, GatewayFactory.sequence, new JSONObject().put("d", new JSONObject(body)));
                    }
                    case USER ->
                            event = new UserContextCommandInteractionEvent(discordJar, GatewayFactory.sequence, new JSONObject().put("d", new JSONObject(body)));
                    case MESSAGE ->
                            event = new MessageContextCommandInteractionEvent(discordJar, GatewayFactory.sequence, new JSONObject().put("d", new JSONObject(body)));
                }

                discordJar.getCommandDispatcher().dispatch(new JSONObject(interaction.raw()).getJSONObject("data").getString("name"), event);
            }
            case MESSAGE_COMPONENT -> {
                switch (ComponentType.getType(new JSONObject().put("d", new JSONObject(body)).getJSONObject("d").getJSONObject("data").getInt("component_type"))) {
                    case BUTTON -> {
                        ButtonInteractionEvent event = new ButtonInteractionEvent(discordJar, 0L, new JSONObject().put("d", new JSONObject(body)));
                        discordJar.getEventDispatcher().dispatchEvent(event, ButtonInteractionEvent.class, discordJar);
                    }
                    case STRING_SELECT -> {
                        StringSelectMenuInteractionEvent event = new StringSelectMenuInteractionEvent(discordJar, 0L, new JSONObject().put("d", new JSONObject(body)));
                        discordJar.getEventDispatcher().dispatchEvent(event, StringSelectMenuInteractionEvent.class, discordJar);
                    }
                    case ROLE_SELECT -> {
                        RoleSelectMenuInteractionEvent event = new RoleSelectMenuInteractionEvent(discordJar, 0L, new JSONObject().put("d", new JSONObject(body)));
                        discordJar.getEventDispatcher().dispatchEvent(event, RoleSelectMenuInteractionEvent.class, discordJar);
                    }
                    case USER_SELECT -> {
                        UserSelectMenuInteractionEvent event = new UserSelectMenuInteractionEvent(discordJar, 0L, new JSONObject().put("d", new JSONObject(body)));
                        discordJar.getEventDispatcher().dispatchEvent(event, UserSelectMenuInteractionEvent.class, discordJar);
                    }
                    case CHANNEL_SELECT -> {
                        ChannelSelectMenuInteractionEvent event = new ChannelSelectMenuInteractionEvent(discordJar, 0L, new JSONObject().put("d", new JSONObject(body)));
                        discordJar.getEventDispatcher().dispatchEvent(event, ChannelSelectMenuInteractionEvent.class, discordJar);
                    }

                }
            }
            case APPLICATION_COMMAND_AUTOCOMPLETE -> {

            }
            case MODAL_SUBMIT -> {
                ModalInteractionEvent event = new ModalInteractionEvent(discordJar, 0L, new JSONObject().put("d", new JSONObject(body)));
                discordJar.getEventDispatcher().dispatchEvent(event, ModalInteractionEvent.class, discordJar);
            }
            case UNKNOWN -> {
                Logger.getLogger("DispatchedEvents").warning(
                        "[discord.jar] Unknown interaction type: " + new JSONObject().put("d", new JSONObject(body)).getJSONObject("d").getInt("type") + ". This is usually because of an outdated framework version. Please update discord.jar");
                return null;
            }
        }


        return ResponseEntity.ok("{\"type\": 1}");
    }

}

