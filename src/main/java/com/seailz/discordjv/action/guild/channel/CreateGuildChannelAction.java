package com.seailz.discordjv.action.guild.channel;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.channel.Category;
import com.seailz.discordjv.model.channel.GuildChannel;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.permission.PermissionOverwrite;
import com.seailz.discordjv.utils.URLS;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
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
    private final DiscordJv discordJv;

    public CreateGuildChannelAction(String name, ChannelType type, Guild guild, DiscordJv discordJv) {
        this.name = name;
        this.type = type;
        this.guild = guild;
        this.discordJv = discordJv;
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
                        discordJv,
                        URLS.POST.GUILDS.CHANNELS.CREATE,
                        RequestMethod.POST
                ).invoke().body(),
                discordJv
        ));
        return future;
    }

}
