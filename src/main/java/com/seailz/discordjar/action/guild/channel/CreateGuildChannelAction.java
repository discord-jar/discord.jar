package com.seailz.discordjar.action.guild.channel;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.Category;
import com.seailz.discordjar.model.channel.GuildChannel;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.rest.DiscordRequest;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CreateGuildChannelAction {

    private final String name;
    private final ChannelType type;
    private String topic;
    private int position;
    private List<PermissionOverwrite> permissionOverwrites;
    private Category category;
    private final Guild guild;
    private final DiscordJar discordJar;

    public CreateGuildChannelAction(String name, ChannelType type, Guild guild, DiscordJar discordJar) {
        this.name = name;
        this.type = type;
        this.guild = guild;
        this.discordJar = discordJar;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setPermissionOverwrites(List<PermissionOverwrite> permissionOverwrites) {
        this.permissionOverwrites = permissionOverwrites;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public ChannelType getType() {
        return type;
    }

    public String getTopic() {
        return topic;
    }

    public int getPosition() {
        return position;
    }

    public List<PermissionOverwrite> getPermissionOverwrites() {
        return permissionOverwrites;
    }

    public Category getCategory() {
        return category;
    }

    public Guild getGuild() {
        return guild;
    }

    public CompletableFuture<GuildChannel> run() {
        CompletableFuture<GuildChannel> future = new CompletableFuture<>();
        future.completeAsync(() -> GuildChannel.decompile(
                new DiscordRequest(
                        new JSONObject()
                                .put("name", name)
                                .put("type", type.getCode())
                                .put("topic", topic)
                                .put("position", position)
                                .put("permission_overwrites", permissionOverwrites)
                                .put("parent_id", category.id()),
                        new HashMap<>(),
                        URLS.POST.GUILDS.CHANNELS.CREATE.replace("{guild.id}", guild.id()),
                        discordJar,
                        URLS.POST.GUILDS.CHANNELS.CREATE,
                        RequestMethod.POST
                ).invoke().body(),
                discordJar
        ));
        return future;
    }

}
