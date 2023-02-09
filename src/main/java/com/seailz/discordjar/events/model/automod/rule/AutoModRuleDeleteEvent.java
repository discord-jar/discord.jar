package com.seailz.discordjar.events.model.automod.rule;

import com.seailz.discordjar.DiscordJar;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class AutoModRuleDeleteEvent extends AutoModRuleEvent {
    public AutoModRuleDeleteEvent(@NotNull DiscordJar bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }
}
