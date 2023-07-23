package com.seailz.discordjar.events.model.automod.rule;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.utils.json.SJSONObject;
import org.jetbrains.annotations.NotNull;

public class AutoModRuleDeleteEvent extends AutoModRuleEvent {
    public AutoModRuleDeleteEvent(@NotNull DiscordJar bot, long sequence, @NotNull SJSONObject data) {
        super(bot, sequence, data);
    }
}
