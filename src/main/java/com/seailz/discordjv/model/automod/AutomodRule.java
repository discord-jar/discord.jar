package com.seailz.discordjv.model.automod;

import com.seailz.discordjv.model.guild.Guild;

public record AutomodRule(
        String id,
        Guild guild
) {
}
