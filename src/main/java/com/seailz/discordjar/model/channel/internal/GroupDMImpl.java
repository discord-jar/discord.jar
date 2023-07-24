package com.seailz.discordjar.model.channel.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.GroupDM;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.user.User;
import org.json.JSONObject;

import java.util.List;

/**
 * Impl of {@link GroupDM}
 */
public class GroupDMImpl extends DMChannelImpl implements GroupDM {

    private final User owner;
    private final String iconUrl;

    public GroupDMImpl(String id, ChannelType type, String name, String lastMessageId, List<User> recipients, DiscordJar discordJar, User owner, String iconUrl, JSONObject raw) {
        super(id, type, name, lastMessageId, recipients, discordJar, raw);
        this.owner = owner;
        this.iconUrl = iconUrl;
    }

    @Override
    public User owner() {
        return owner;
    }

    @Override
    public String iconUrl() {
        return iconUrl;
    }
}
