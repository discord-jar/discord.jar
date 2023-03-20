package com.seailz.discordjar.model.channel.interfaces;

import com.seailz.discordjar.action.message.MessageCreateAction;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.model.component.DisplayComponent;
import com.seailz.discordjar.model.embed.Embeder;
import com.seailz.discordjar.model.message.Attachment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public interface Messageable extends Channel {

    default MessageCreateAction sendMessage(String text) {
        return new MessageCreateAction(text, id(), djv());
    }

    default MessageCreateAction sendComponents(DisplayComponent... components) {
        return new MessageCreateAction(new ArrayList<>(List.of(components)), id(), djv());
    }

    default MessageCreateAction sendEmbeds(Embeder... embeds) {
        return new MessageCreateAction(new ArrayList<>(List.of(embeds)), id(), djv());
    }

    default MessageCreateAction sendAttachments(Attachment... attachments) {
        return new MessageCreateAction(new LinkedList<>(List.of(attachments)), id(), djv());
    }

}
