package com.seailz.javadiscordwrapper.model.component.select;

import com.seailz.javadiscordwrapper.model.component.MessageComponent;

public interface SelectMenu extends MessageComponent {

    String placeholder();

    int minValues();

    int maxValues();

}
