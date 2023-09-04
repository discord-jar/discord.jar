package com.seailz.discordjar.utils.registry.components;

import com.seailz.discordjar.model.component.select.entity.ChannelSelectMenu;
import com.seailz.discordjar.model.component.select.string.StringSelectMenu;
import com.seailz.discordjar.utils.registry.Registry;

public class ChannelSelectRegistry extends Registry<ChannelSelectMenu.ChannelSelectAction> {

    private static ChannelSelectRegistry instance;

    private ChannelSelectRegistry() {
    }

    /**
     * Gets the instance of the string select registry.
     *
     * @return The instance of the string select registry, or a new instance if it doesn't exist.
     */
    public static ChannelSelectRegistry getInstance() {
        if (instance == null) {
            instance = new ChannelSelectRegistry();
        }
        return instance;
    }
}