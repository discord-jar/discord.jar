package com.seailz.discordjar.events.model.automod.rule;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.guild.GuildEvent;
import com.seailz.discordjar.model.automod.AutomodRule;
import com.seailz.discordjar.utils.json.SJSONObject;
import org.jetbrains.annotations.NotNull;

public class AutoModRuleEvent extends GuildEvent {

    public AutoModRuleEvent(@NotNull DiscordJar bot, long sequence, @NotNull SJSONObject data) {
        super(bot, sequence, data);
    }

    @NotNull
    public AutomodRule getRule() {
        return AutomodRule.decompile(getJson().getJSONObject("d"), getBot());
    }
}
