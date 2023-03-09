package com.seailz.discordjar.model.guild;

public record GuildIntegration(
        long id,
        String name,
        String type,
        boolean enabled,
        boolean syncing,
        long roleId,
        boolean enableEmoticons,
        ExpireBehavior expireBehavior,
        int expireGracePeriod
) {
    public enum ExpireBehavior {
        REMOVE_ROLE(0),
        KICK(1);

        ExpireBehavior(int i) {
        }
    }
}
