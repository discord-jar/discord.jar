package com.seailz.discordjv.utils.permission;

import com.seailz.discordjv.utils.flag.Bitwiseable;

import java.util.List;

/**
 * Represents a permission a user can have in a guild.
 * This class is also used to check a bot has permission to do something, before the library does it.
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.discordjv.model.guild.Guild
 */
public enum Permission implements Bitwiseable<Permission> {

    /* 	Allows creation of instant invites */
    CREATE_INSTANT_INVITE(0),
    /* 	Allows kicking members */
    KICK_MEMBERS(1),
    /* 	Allows banning members */
    BAN_MEMBERS(2),
    /* 	Allows all permissions and bypasses channel permission overwrites */
    ADMINISTRATOR(3),
    /* 	Allows management and editing of channels */
    MANAGE_CHANNELS(4),
    /* 	Allows management and editing of the guild */
    MANAGE_GUILD(5),
    /* 	Allows for the addition of reactions to messages */
    ADD_REACTIONS(6),
    /* 	Allows for viewing of audit logs */
    VIEW_AUDIT_LOG(7),
    /* 	Allows for using priority speaker in a voice channel */
    PRIORITY_SPEAKER(8),
    /* 	Allows the user to go live */
    STREAM(9),
    /* 	Allows guild members to view a channel, which includes reading messages in text channels and joining voice channels */
    VIEW_CHANNEL(10),
    /* 	Allows for sending messages in a channel */
    SEND_MESSAGES(11),
    /* 	Allows for sending of /tts messages */
    SEND_TTS_MESSAGES(12),
    /* 	Allows for deletion of other users messages */
    MANAGE_MESSAGES(13),
    /* 	Links sent by users with this permission will be auto-embedded */
    EMBED_LINKS(14),
    /* 	Allows for uploading images and files */
    ATTACH_FILES(15),
    /* 	Allows for reading of message history */
    READ_MESSAGE_HISTORY(16),
    /* 	Allows for using the @everyone tag to notify all users in a channel, and the @here tag to notify all online users in a channel */
    MENTION_EVERYONE(17),
    /* 	Allows the usage of custom emojis from other servers */
    USE_EXTERNAL_EMOJIS(18),
    /* 	Allows for viewing guild insights */
    VIEW_GUILD_INSIGHTS(19),
    /* 	Allows for joining of a voice channel */
    CONNECT(20),
    /* 	Allows for speaking in a voice channel */
    SPEAK(21),
    /* 	Allows for muting members in a voice channel */
    MUTE_MEMBERS(22),
    /* 	Allows for deafening of members in a voice channel */
    DEAFEN_MEMBERS(23),
    /* 	Allows for moving of members between voice channels */
    MOVE_MEMBERS(24),
    /* 	Allows for using voice-activity-detection in a voice channel */
    USE_VAD(25),
    /* 	Allows for modification of own nickname */
    CHANGE_NICKNAME(26),
    /* 	Allows for modification of other users nicknames */
    MANAGE_NICKNAMES(27),
    /*   Allows management and editing of roles */
    MANAGE_ROLES(28),
    /*   Allows management and editing of webhook */
    MANAGE_WEBHOOKS(29),
    /*	 Allows management and editing of emojis and stickers */
    MANAGE_EMOJIS_AND_STICKERS(30),
    /* 	 Allows members to use application commands, including slash commands and context menu commands. */
    USE_APPLICATION_COMMANDS(31),
    /* 	 Allows for requesting to speak in stage channels. (This permission is under active development and may be changed or removed.) */
    REQUEST_TO_SPEAK(32),
    /* 	Allows for creating, editing, and deleting scheduled events */
    MANAGE_EVENTS(33),
    /* 	 Allows for deleting and archiving threads, and viewing all private threads */
    MANAGE_THREADS(34),
    /* 	 Allows for creating and participating in threads */
    USE_PUBLIC_THREADS(35),
    /* 	 Allows for creating and participating in private threads */
    USE_PRIVATE_THREADS(36),
    /* 	 Allows the usage of custom stickers from other servers */
    USE_EXTERNAL_STICKERS(37),
    /*   Allows for sending messages in threads */
    SEND_MESSAGES_IN_THREADS(38),
    /*   Allows for using Activities (applications with the EMBEDDED flag) in a voice channel (Also known as voice chanel games/activities) */
    USE_EMBEDDED_ACTIVITIES(39),
    /* 	Allows for timing out users to prevent them from sending or reacting to messages in chat and threads, and from speaking in voice and stage channels */
    MODERATE_MEMBERS(40);

    private final int code;

    Permission(int code) {
        this.code = code;
    }

    /**
     * @return The code of the permission
     */
    public int code() {
        return code;
    }

    @Override
    public int getLeftShiftId() {
        return 1 << code;
    }

    @Override
    public int id() {
        return code;
    }

}
