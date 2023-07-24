package com.seailz.discordjar.events.model.automod.rule;

import com.seailz.discordjar.DiscordJar;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class AutoModRuleCreateEvent extends AutoModRuleEvent {

    public AutoModRuleCreateEvent(@NotNull DiscordJar bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

}
