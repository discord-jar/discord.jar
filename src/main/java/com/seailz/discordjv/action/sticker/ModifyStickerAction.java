package com.seailz.discordjv.action.sticker;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.emoji.sticker.Sticker;
import com.seailz.discordjv.utils.URLS;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class ModifyStickerAction {

    private final String stickerId;
    private final DiscordJv discordJv;
    private String name;
    private String description;
    private String tags;

    public ModifyStickerAction(String stickerId, DiscordJv discordJv) {
        this.stickerId = stickerId;
        this.discordJv = discordJv;
    }

    public ModifyStickerAction setName(String name) {
        this.name = name;
        return this;
    }

    public ModifyStickerAction setDescription(String description) {
        this.description = description;
        return this;
    }

    public ModifyStickerAction setTags(String tags) {
        this.tags = tags;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTags() {
        return tags;
    }

    public CompletableFuture<Sticker> run() {
        CompletableFuture<Sticker> stickerCompletableFuture = new CompletableFuture<>();
        stickerCompletableFuture.completeAsync(() -> Sticker.decompile(
                new DiscordRequest(
                        new JSONObject()
                                .put("name", name != null ? name : JSONObject.NULL)
                                .put("description", description != null ? description : JSONObject.NULL)
                                .put("tags", tags != null ? tags : JSONObject.NULL),
                        new HashMap<>(),
                        URLS.PATCH.GUILD.STICKER.MODIFY_GUILD_STICKER.replace(
                                "{sticker_id}",
                                stickerId
                        ),
                        discordJv,
                        URLS.PATCH.GUILD.STICKER.MODIFY_GUILD_STICKER,
                        RequestMethod.PATCH
                ).invoke().body(),
                discordJv
        ));
        return stickerCompletableFuture;
    }

}
