package com.seailz.discordjar.action.sticker;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.emoji.sticker.Sticker;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.json.SJSONObject;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class ModifyStickerAction {

    private final String stickerId;
    private final DiscordJar discordJar;
    private String name;
    private String description;
    private String tags;

    public ModifyStickerAction(String stickerId, DiscordJar discordJar) {
        this.stickerId = stickerId;
        this.discordJar = discordJar;
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
        stickerCompletableFuture.completeAsync(() -> {
            try {
                return Sticker.decompile(
                        new DiscordRequest(
                                new SJSONObject()
                                        .put("name", name != null ? name : SJSONObject.NULL)
                                        .put("description", description != null ? description : SJSONObject.NULL)
                                        .put("tags", tags != null ? tags : SJSONObject.NULL),
                                new HashMap<>(),
                                URLS.PATCH.GUILD.STICKER.MODIFY_GUILD_STICKER.replace(
                                        "{sticker_id}",
                                        stickerId
                                ),
                                discordJar,
                                URLS.PATCH.GUILD.STICKER.MODIFY_GUILD_STICKER,
                                RequestMethod.PATCH
                        ).invoke().body(),
                        discordJar
                );
            } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
                stickerCompletableFuture.completeExceptionally(e);
                return null;
            }
        });
        return stickerCompletableFuture;
    }

}
