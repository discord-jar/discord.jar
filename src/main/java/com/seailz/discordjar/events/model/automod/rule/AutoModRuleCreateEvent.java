package com.seailz.discordjar.events.model.automod.rule;

import com.seailz.discordjar.DiscordJar;
import org.jetbrains.annotations.NotNull;
import com.seailz.discordjar.utils.json.SJSONObject;

public class AutoModRuleCreateEvent extends AutoModRuleEvent {

    public AutoModRuleCreateEvent(@NotNull DiscordJar bot, long sequence, @NotNull SJSONObject data) {
        super(bot, sequence, data);
    }

}
