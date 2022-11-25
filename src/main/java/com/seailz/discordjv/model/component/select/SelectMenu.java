package com.seailz.discordjv.model.component.select;

import com.seailz.discordjv.model.component.MessageComponent;

public interface SelectMenu extends MessageComponent {

    String placeholder();

    int minValues();

    int maxValues();

}
