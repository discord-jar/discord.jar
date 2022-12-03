package com.seailz.discordjv.model.channel.internal;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.channel.GroupDM;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.user.User;

import java.util.List;

/**
 * Impl of {@link GroupDM}
 */
public class GroupDMImpl extends DMChannelImpl implements GroupDM {

    private final User owner;
    private final String iconUrl;

    public GroupDMImpl(String id, ChannelType type, String name, String lastMessageId, List<User> recipients, DiscordJv discordJv, User owner, String iconUrl) {
        super(id, type, name, lastMessageId, recipients, discordJv);
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
