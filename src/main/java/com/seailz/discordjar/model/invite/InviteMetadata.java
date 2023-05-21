package com.seailz.discordjar.model.invite;

/**
 * An extension of {@link Invite} that contains extra metadata about the invite.
 * @see Invite
 * @see <a href="https://discordapp.com/developers/docs/resources/invite#invite-metadata-object">Invite Metadata</a>
 * @author Seailz
 */
public interface InviteMetadata extends Invite {

    /**
     * How many times this invite has been used.
     */
    int uses();

    /**
     * Max number of times this invite can be used.
     */
    int maxUses();

    /**
     * Duration (in seconds) after which the invite expires.
     */
    int maxAge();

    /**
     * Whether this invite only grants temporary membership.
     */
    boolean temporary();

    /**
     * When this invite was created. (ISO8601 timestamp)
     */
    String createdAt();

}
