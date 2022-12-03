package com.seailz.discordjv.model.channel.internal;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.channel.DMChannel;
import com.seailz.discordjv.model.channel.UserDM;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.user.User;

import java.util.List;

/**
 * Impl of {@link DMChannel}
 */
public class DMChannelImpl extends ChannelImpl implements DMChannel, UserDM {

    private final String lastMessageId;
    private final List<User> recipients;
    private final DiscordJv discordJv;

    public DMChannelImpl(String id, ChannelType type, String name, String lastMessageId, List<User> recipients, DiscordJv discordJv) {
        super(id, type, name);
        this.lastMessageId = lastMessageId;
        this.recipients = recipients;
        this.discordJv = discordJv;
    }

    @Override
    public String lastMessageId() {
        return lastMessageId;
    }

    @Override
    public List<User> recipients() {
        return recipients;
    }

    @Override
    public DiscordJv discordJv() {
        return discordJv;
    }
}
