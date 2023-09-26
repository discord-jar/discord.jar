package com.seailz.discordjar.model.component.select;

import com.seailz.discordjar.model.component.MessageComponent;

public interface SelectMenu extends MessageComponent {

    String placeholder();

    int minValues();

    int maxValues();

}
