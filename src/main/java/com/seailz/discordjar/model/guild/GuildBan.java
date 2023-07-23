package com.seailz.discordjar.model.guild;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.json.SJSONObject;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a ban on a user in a {@link Guild}.
 * @param reason The reason the user was banned
 * @param user The user that was banned
 */
public record GuildBan (
        String reason,
        @NotNull User user
) implements Compilerable {
    @Override
    public SJSONObject compile() {
        return new SJSONObject()
                .put("reason", reason)
                .put("user", user.compile());
    }

    public static GuildBan decompile(SJSONObject json, DiscordJar discordJar) {
        return new GuildBan(
                json.has("reason") ? json.getString("reason") : null,
                User.decompile(json.getJSONObject("user"), discordJar)
        );
    }
}
