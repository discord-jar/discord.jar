package com.seailz.discordjar.action.interaction.followup;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.component.DisplayComponent;
import com.seailz.discordjar.model.embed.Embeder;
import com.seailz.discordjar.model.interaction.callback.InteractionHandler;
import com.seailz.discordjar.model.interaction.reply.InteractionMessageResponse;
import com.seailz.discordjar.model.mentions.AllowedMentions;
import com.seailz.discordjar.model.message.Attachment;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.Response;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;

public class InteractionFollowupAction {

    private final InteractionMessageResponse reply;
    private final String token;
    private final String id;
    private final DiscordJar discordJar;

    public InteractionFollowupAction(String content, String token, String id, DiscordJar discordJar) {
        this.reply = new InteractionMessageResponse(content);
        this.token = token;
        this.id = id;
        this.discordJar = discordJar;
    }

    /**
     * Enables or disables an ephemeral message.
     *
     * @param ephemeral Whether the message should be ephemeral or not.
     */
    public InteractionFollowupAction setEphemeral(boolean ephemeral) {
        this.getReply().setEphemeral(ephemeral);
        return this;
    }

    /**
     * Enables or disables TTS for the message.
     *
     * @param tts Whether the message should be TTS or not.
     */
    public InteractionFollowupAction setTTS(boolean tts) {
        this.getReply().setTts(tts);
        return this;
    }

    /**
     * Sets the content of the message.
     *
     * @param content The content of the message.
     */
    public InteractionFollowupAction setContent(String content) {
        this.getReply().setContent(content);
        return this;
    }

    /**
     * Sets the {@link com.seailz.discordjar.model.component.DisplayComponent DisplayComponents} of the message.
     *
     * @param components The components of the message.
     */
    public InteractionFollowupAction setComponents(List<DisplayComponent> components) {
        this.getReply().setComponents(components);
        return this;
    }

    /**
     * Adds a {@link com.seailz.discordjar.model.component.DisplayComponent DisplayComponent} to the message.
     *
     * @param component The component to add
     */
    public InteractionFollowupAction addComponent(DisplayComponent component) {
        this.getReply().addComponent(component);
        return this;
    }

    /**
     * Removes a {@link com.seailz.discordjar.model.component.DisplayComponent DisplayComponent} from the message.
     *
     * @param component The component to remove
     */
    public InteractionFollowupAction removeComponent(DisplayComponent component) {
        this.getReply().removeComponent(component);
        return this;
    }

    /**
     * Sets the {@link com.seailz.discordjar.model.component.DisplayComponent DisplayComponents} of the message.
     *
     * @param components The components of the message.
     */
    public InteractionFollowupAction setComponents(DisplayComponent... components) {
        return setComponents(List.of(components));
    }

    /**
     * Adds {@link com.seailz.discordjar.model.component.DisplayComponent DisplayComponents} to the message.
     *
     * @param components The components to add
     */
    public InteractionFollowupAction addComponents(List<DisplayComponent> components) {
        components.forEach(this::addComponent);
        return this;
    }

    /**
     * Adds {@link com.seailz.discordjar.model.component.DisplayComponent DisplayComponents} to the message.
     *
     * @param components The components to add
     */
    public InteractionFollowupAction addComponents(DisplayComponent... components) {
        return addComponents(List.of(components));
    }

    // attachments

    public InteractionFollowupAction setAttachments(Attachment... attachments) {
        this.getReply().setAttachments(List.of(attachments));
        return this;
    }

    public InteractionFollowupAction setAttachments(List<Attachment> attachments) {
        this.getReply().setAttachments(attachments);
        return this;
    }

    public InteractionFollowupAction setAllowedMentions(AllowedMentions allowedMentions) {
        this.getReply().setAllowedMentions(allowedMentions);
        return this;
    }

    public InteractionFollowupAction addEmbed(Embeder embed) {
        this.getReply().addEmbed(embed);
        return this;
    }

    public InteractionMessageResponse getReply() {
        return reply;
    }

    public Response<InteractionHandler> run() {
        Response<InteractionHandler> response = new Response<>();
        try {
            new DiscordRequest(
                    getReply().compile(),
                    new HashMap<>(),
                    URLS.POST.INTERACTIONS.FOLLOWUP
                            .replaceAll("application.id", discordJar.getSelfInfo().id())
                            .replaceAll("interaction.token", token),
                    discordJar,
                    URLS.POST.INTERACTIONS.FOLLOWUP,
                    RequestMethod.POST
            ).invoke();
            response.complete(InteractionHandler.from(token, id, discordJar));
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            response.completeError(new Response.Error(e.getCode(), e.getMessage(), e.getBody()));
        }
        return response;
    }
}
