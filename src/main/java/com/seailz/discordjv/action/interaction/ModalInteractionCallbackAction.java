package com.seailz.discordjv.action.interaction;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.component.DisplayComponent;
import com.seailz.discordjv.model.interaction.callback.InteractionCallbackType;
import com.seailz.discordjv.model.interaction.reply.InteractionModalResponse;

import java.util.List;

public class ModalInteractionCallbackAction extends InteractionCallbackAction {

    public ModalInteractionCallbackAction(InteractionCallbackType type, InteractionModalResponse reply, String token, String id, DiscordJv discordJv) {
        super(type, reply, token, id, discordJv);
    }

    public ModalInteractionCallbackAction setTitle(String title) {
        ((InteractionModalResponse) this.getReply()).setTitle(title);
        return this;
    }

    public ModalInteractionCallbackAction setCustomId(String customId) {
        ((InteractionModalResponse) this.getReply()).setCustomId(customId);
        return this;
    }

    public ModalInteractionCallbackAction setComponents(List<DisplayComponent> components) {
        ((InteractionModalResponse) this.getReply()).setComponents(components);
        return this;
    }

    public ModalInteractionCallbackAction addComponent(DisplayComponent component) {
        ((InteractionModalResponse) this.getReply()).addComponents(component);
        return this;
    }

    public ModalInteractionCallbackAction addComponents(DisplayComponent... components) {
        ((InteractionModalResponse) this.getReply()).addComponents(components);
        return this;
    }

    public ModalInteractionCallbackAction removeComponent(DisplayComponent component) {
        ((InteractionModalResponse) this.getReply()).removeComponent(component);
        return this;
    }

    public ModalInteractionCallbackAction removeComponents(DisplayComponent... components) {
        ((InteractionModalResponse) this.getReply()).removeComponents(components);
        return this;
    }

}
