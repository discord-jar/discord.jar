package com.seailz.discordjar.utils.registry.components;

import com.seailz.discordjar.model.component.select.string.StringSelectMenu;
import com.seailz.discordjar.utils.registry.Registry;

public class StringSelectRegistry extends Registry<StringSelectMenu.StringSelectAction> {

    private static StringSelectRegistry instance;

    private StringSelectRegistry() {
    }

    /**
     * Gets the instance of the string select registry.
     *
     * @return The instance of the string select registry, or a new instance if it doesn't exist.
     */
    public static StringSelectRegistry getInstance() {
        if (instance == null) {
            instance = new StringSelectRegistry();
        }
        return instance;
    }
}
