package com.seailz.discordjar.model.channel.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.DMChannel;
import com.seailz.discordjar.model.channel.UserDM;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.user.User;
import org.json.JSONObject;

import java.util.List;

/**
 * Impl of {@link DMChannel}
 */
public class DMChannelImpl extends ChannelImpl implements DMChannel, UserDM {

    private final String lastMessageId;
    private final List<User> recipients;
    private final DiscordJar discordJar;

    public DMChannelImpl(String id, ChannelType type, String name, String lastMessageId, List<User> recipients, DiscordJar discordJar, JSONObject raw) {
        super(id, type, name, raw, discordJar);
        this.lastMessageId = lastMessageId;
        this.recipients = recipients;
        this.discordJar = discordJar;
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
    public DiscordJar discordJv() {
        return discordJar;
    }
}
