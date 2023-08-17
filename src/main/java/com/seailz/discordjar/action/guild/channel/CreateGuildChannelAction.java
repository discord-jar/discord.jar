package com.seailz.discordjar.action.guild.channel;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.Category;
import com.seailz.discordjar.model.channel.ForumChannel;
import com.seailz.discordjar.model.channel.GuildChannel;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import com.seailz.discordjar.utils.rest.Response;
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
    private String categoryId;
    private ForumChannel.DefaultForumLayout defaultForumLayout;
    private final Guild guild;
    private final DiscordJar discordJar;

    public CreateGuildChannelAction(String name, ChannelType type, Guild guild, DiscordJar discordJar) {
        this.name = name;
        this.type = type;
        this.guild = guild;
        this.discordJar = discordJar;
    }

    public CreateGuildChannelAction setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public CreateGuildChannelAction setPosition(int position) {
        this.position = position;
        return this;
    }

    public CreateGuildChannelAction setPermissionOverwrites(List<PermissionOverwrite> permissionOverwrites) {
        this.permissionOverwrites = permissionOverwrites;
        return this;
    }

    public CreateGuildChannelAction setDefaultForumLayout(ForumChannel.DefaultForumLayout defaultForumLayout) {
        this.defaultForumLayout = defaultForumLayout;
        return this;
    }

    public CreateGuildChannelAction setCategory(Category category) {
        this.category = category;
        return this;
    }

    /**
     * This is generally not recommended to use. If you set this, it will take priority over {@link #setCategory(Category)}.
     */
    public CreateGuildChannelAction setCategoryWithId(String id) {
        this.categoryId = id;
        return this;
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

    public Response<GuildChannel> run() {
        Response<GuildChannel> res = new Response<>();

        new Thread(() -> {
            String categoryId = null;
            if (this.categoryId != null) categoryId = this.categoryId;
            else if (this.category != null) categoryId = this.category.id();
            try {
                GuildChannel chan = GuildChannel.decompile(
                        new DiscordRequest(
                                new JSONObject()
                                        .put("name", name)
                                        .put("type", type.getCode())
                                        .put("topic", topic != null ? topic : JSONObject.NULL)
                                        .put("position", position)
                                        .put("permission_overwrites", permissionOverwrites)
                                        .put("parent_id", categoryId != null ? categoryId : JSONObject.NULL)
                                        .put("default_forum_layout", defaultForumLayout != null ? defaultForumLayout.getCode() : JSONObject.NULL),
                                new HashMap<>(),
                                URLS.POST.GUILDS.CHANNELS.CREATE.replace("{guild.id}", guild.id()),
                                discordJar,
                                URLS.POST.GUILDS.CHANNELS.CREATE,
                                RequestMethod.POST
                        ).invoke().body(),
                        discordJar
                );
                res.complete(chan);
            } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
                res.completeError(new Response.Error(
                        e.getCode(),
                        e.getMessage(),
                        e.getBody()
                ));
            }
        }).start();
        return res;
    }

}
