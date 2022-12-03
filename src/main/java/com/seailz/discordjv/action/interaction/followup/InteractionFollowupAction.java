package com.seailz.discordjv.action.interaction.followup;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.component.DisplayComponent;
import com.seailz.discordjv.model.interaction.callback.InteractionHandler;
import com.seailz.discordjv.model.interaction.reply.InteractionMessageResponse;
import com.seailz.discordjv.model.interaction.reply.InteractionReply;
import com.seailz.discordjv.model.message.Attachment;
import com.seailz.discordjv.utils.URLS;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;

public class InteractionFollowupAction {

    private final InteractionReply reply;
    private final String token;
    private final String id;
    private final DiscordJv discordJv;

    public InteractionFollowupAction(String content, String token, String id, DiscordJv discordJv) {
        this.reply = new InteractionMessageResponse(content);
        this.token = token;
        this.id = id;
        this.discordJv = discordJv;
    }

    /**
     * Enables or disables an ephemeral message.
     *
     * @param ephemeral Whether the message should be ephemeral or not.
     */
    public InteractionFollowupAction setEphemeral(boolean ephemeral) {
        ((InteractionFollowupAction) this.getReply()).setEphemeral(ephemeral);
        return this;
    }

    /**
     * Enables or disables TTS for the message.
     *
     * @param tts Whether the message should be TTS or not.
     */
    public InteractionFollowupAction setTTS(boolean tts) {
        ((InteractionFollowupAction) this.getReply()).setTTS(tts);
        return this;
    }

    /**
     * Sets the content of the message.
     *
     * @param content The content of the message.
     */
    public InteractionFollowupAction setContent(String content) {
        ((InteractionFollowupAction) this.getReply()).setContent(content);
        return this;
    }

    /**
     * Sets the {@link com.seailz.discordjv.model.component.DisplayComponent DisplayComponents} of the message.
     *
     * @param components The components of the message.
     */
    public InteractionFollowupAction setComponents(List<DisplayComponent> components) {
        ((InteractionFollowupAction) this.getReply()).setComponents(components);
        return this;
    }

    /**
     * Adds a {@link com.seailz.discordjv.model.component.DisplayComponent DisplayComponent} to the message.
     *
     * @param component The component to add
     */
    public InteractionFollowupAction addComponent(DisplayComponent component) {
        ((InteractionFollowupAction) this.getReply()).addComponent(component);
        return this;
    }

    /**
     * Removes a {@link com.seailz.discordjv.model.component.DisplayComponent DisplayComponent} from the message.
     *
     * @param component The component to remove
     */
    public InteractionFollowupAction removeComponent(DisplayComponent component) {
        ((InteractionFollowupAction) this.getReply()).removeComponent(component);
        return this;
    }

    /**
     * Sets the {@link com.seailz.discordjv.model.component.DisplayComponent DisplayComponents} of the message.
     *
     * @param components The components of the message.
     */
    public InteractionFollowupAction setComponents(DisplayComponent... components) {
        return setComponents(List.of(components));
    }

    /**
     * Adds {@link com.seailz.discordjv.model.component.DisplayComponent DisplayComponents} to the message.
     *
     * @param components The components to add
     */
    public InteractionFollowupAction addComponents(List<DisplayComponent> components) {
        components.forEach(this::addComponent);
        return this;
    }

    /**
     * Adds {@link com.seailz.discordjv.model.component.DisplayComponent DisplayComponents} to the message.
     *
     * @param components The components to add
     */
    public InteractionFollowupAction addComponents(DisplayComponent... components) {
        return addComponents(List.of(components));
    }

    // attachments

    public InteractionFollowupAction setAttachments(Attachment... attachments) {
        ((InteractionFollowupAction) this.getReply()).setAttachments(List.of(attachments));
        return this;
    }

    public InteractionFollowupAction setAttachments(List<Attachment> attachments) {
        ((InteractionFollowupAction) this.getReply()).setAttachments(attachments);
        return this;
    }

    public InteractionReply getReply() {
        return reply;
    }

    public InteractionHandler run() {
        new DiscordRequest(
                getReply().compile(),
                new HashMap<>(),
                URLS.POST.INTERACTIONS.FOLLOWUP
                        .replaceAll("application.id", discordJv.getSelfInfo().id())
                        .replaceAll("interaction.token", token),
                discordJv,
                URLS.POST.INTERACTIONS.FOLLOWUP,
                RequestMethod.POST
        ).invoke();
        return InteractionHandler.from(token, id, discordJv);
    }
}
