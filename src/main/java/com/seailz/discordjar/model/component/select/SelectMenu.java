package com.seailz.discordjar.model.component.select;

import com.seailz.discordjar.events.model.interaction.button.ButtonInteractionEvent;
import com.seailz.discordjar.model.component.MessageComponent;
import com.seailz.discordjar.model.component.button.Button;

import java.util.function.Consumer;

public interface SelectMenu extends MessageComponent {

    String placeholder();

    int minValues();

    int maxValues();

}
