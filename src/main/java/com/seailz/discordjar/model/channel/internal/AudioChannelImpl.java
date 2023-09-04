package com.seailz.discordjar.model.channel.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.gateway.GatewayFactory;
import com.seailz.discordjar.model.channel.AudioChannel;
import com.seailz.discordjar.model.channel.Category;
import com.seailz.discordjar.model.channel.audio.VoiceRegion;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.voice.model.provider.VoiceProvider;
import com.seailz.discordjar.voice.ws.VoiceGateway;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class AudioChannelImpl extends GuildChannelImpl implements AudioChannel {

    private final String lastMessageId;
    private final VoiceRegion region;
    private final int bitrate;
    private final Category category;

    public AudioChannelImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, boolean nsfw, String lastMessageId, VoiceRegion region, Category category, int bitrate, JSONObject raw, DiscordJar discordJar) {
        super(id, type, name, guild, position, permissionOverwrites, nsfw, raw, discordJar);
        this.lastMessageId = lastMessageId;
        this.region = region;
        this.bitrate = bitrate;
        this.category = category;
    }

    @Override
    public String lastMessageId() {
        return lastMessageId;
    }

    @Override
    public VoiceRegion region() {
        return region;
    }

    @Override
    public int bitrate() {
        return bitrate;
    }

    @Override
    public void connect(VoiceProvider vp, boolean mute, boolean deafen) {
        JSONObject gwPayload = new JSONObject();
        GatewayFactory gateway = discordJv().getGateway();

        gwPayload.put("op", 4);
        JSONObject data = new JSONObject();
        data.put("guild_id", guild().id());
        data.put("channel_id", id());
        data.put("self_mute", mute);
        data.put("self_deaf", deafen);
        gwPayload.put("d", data);

        AtomicBoolean receivedVoiceServerUpdate = new AtomicBoolean(false);
        AtomicBoolean receivedVoiceStateUpdate = new AtomicBoolean(false);

        AtomicReference<String> sessionId = new AtomicReference<>();
        AtomicReference<String> token = new AtomicReference<>();
        AtomicReference<String> endpoint = new AtomicReference<>();

        gateway.onVoiceServerUpdate((event) -> {
            System.out.println("VSEU");
            if (event.guildId().equals(guild().id())) {
                token.set(event.token());
                endpoint.set(event.endpoint());
                receivedVoiceServerUpdate.set(true);
            }
        });

        gateway.onVoiceStateUpdate((event) -> {
            System.out.println("VSU");
            if (event.guildId().equals(guild().id()) && event.userId().equals(discordJv().getSelfUser().id())) {
                sessionId.set(event.sessionId());
                receivedVoiceStateUpdate.set(true);
            }
        });

        gateway.queueMessage(gwPayload);

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (receivedVoiceServerUpdate.get() && receivedVoiceStateUpdate.get()) {
                    System.out.println("Establishing Voice WS Connection");
                    VoiceGateway voiceGateway = new VoiceGateway(sessionId.get(), token.get(), endpoint.get(), vp, discordJv(), discordJv().isDebug());
                    break;
                }
            }
        }).start();
    }

    @Override
    public Category owner() {
        return category;
    }

    @Override
    public String parentId() {
        return category.id();
    }
}
