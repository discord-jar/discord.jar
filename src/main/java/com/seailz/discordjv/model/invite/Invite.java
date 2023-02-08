package com.seailz.discordjv.model.invite;

import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.model.application.Application;
import com.seailz.discordjv.model.channel.Channel;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.resolve.Resolvable;
import com.seailz.discordjv.model.user.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an invite.
 * <p>Invites are links that can be sent and allow
 * <br>anyone seeing that link to join a server or a group DM.
 * @author Seailz
 * @since 1.0
 * @see Guild
 * @see com.seailz.discordjv.model.channel.GroupDM
 */
public interface Invite extends Compilerable, Resolvable {

    /**
     * The unique invite code.
     */
    @NotNull
    String code();

    /**
     * The guild this invite is for.
     */
    @Nullable
    Guild guild();

    /**
     * The channel this invite is for.
     */
    Channel channel();

    /**
     * The user who created the invite.
     */
    @Nullable
    User inviter();

    /**
     * If this invite is for a voice channel, this will return
     * <br>the invite type.
     * <p>
     * Will return null if this is not a voice channel invite
     * or {@link VoiceInviteTargetType#UNKNOWN} if the value provided by Discord is not recognized.
     */
    @Nullable
    VoiceInviteTargetType voiceInviteTarget();

    /**
     * If this is a voice channel invite & {@link #voiceInviteTarget()} is {@link VoiceInviteTargetType#STREAM STREAM},
     * <br>then this will return the user whose stream this invite is for. Otherwise, null.
     */
    @Nullable
    User targetUserStream();

    /**
     * If this is a voice channel invite & {@link #voiceInviteTarget()} is {@link VoiceInviteTargetType#EMBEDDED_APPLICATION EMBEDDED_APPLICATION},
     * <br>then this will return the application that this invite is for.. Otherwise, null.
     */
    @Nullable
    Application targetApplication();

    /**
     * Approximate count of online members.
     * <p>Returned when withCounts is set to true while retrieving an invite.
     * <br>If this value is not present, this method will return -1.
     */
    int approximatePresenceCount();

    /**
     * Approximate count of total members.
     * <p>Returned when withCounts is set to true while retrieving an invite.
     * <br>If this value is not present, this method will return -1.
     */
    int approximateMemberCount();

    /**
     * The expiration date of this invite.
     * ISO8601 timestamp.
     */
    String expiresAt();

    // TODO: stage instance and guild_scheduled_event

    enum VoiceInviteTargetType {
        STREAM(1),
        EMBEDDED_APPLICATION(2), // This is for voice channel activities.

        UNKNOWN(-1);

        private final int code;

        VoiceInviteTargetType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static VoiceInviteTargetType fromCode(int i) {
            for (VoiceInviteTargetType value : values()) {
                if (value.getCode() == i) return value;
            }
            return UNKNOWN;
        }
    }

}
