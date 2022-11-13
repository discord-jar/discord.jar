package com.seailz.javadiscordwrapper.model.automod;

import com.seailz.javadiscordwrapper.model.guild.Guild;

public record AutomodRule(
        String id,
        Guild guild
) {
}
