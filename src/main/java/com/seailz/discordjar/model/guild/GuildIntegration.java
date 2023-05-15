package com.seailz.discordjar.model.guild;

/**
 * Represents a Guild Integration.
 * @param id The id of the integration.
 * @param name The name of the integration.
 * @param type The type of the integration.
 * @param enabled The enabled state of the integration.
 * @param syncing The syncing state of the integration.
 * @param roleId The id of the role associated with the integration.
 * @param enableEmoticons Whether emoticons are enabled.
 * @param expireBehavior The expiry behavior of the integration.
 * @param expireGracePeriod The grace period time of the integration's expiration.
 */
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
