package com.seailz.discordjar.model.message;

import java.util.EnumSet;

/**
 * Represents a flag on a message,
 *
 * @author Seailz
 * @since 1.0
 */
public enum MessageFlag {

    // this message has been published to subscribed channels (via Channel Following)
    CROSSPOSTED(0, false),
    // this message originated from a message in another channel (via Channel Following)
    IS_CROSSPOST(1, false),
    // do not include any embeds when serializing this message
    SUPPRESS_EMBEDS(2, true),
    // the source message for this crosspost has been deleted (via Channel Following)
    SOURCE_MESSAGE_DELETED(3, false),
    // this message came from the urgent message system
    URGENT(4, false),
    // this message has an associated thread, with the same id as the message
    HAS_THREAD(5, false),
    // this message is only visible to the user who invoked the Interaction
    EPHEMERAL(6, true),
    // this message is an Interaction Response and the bot is "thinking"
    LOADING(7, false),
    // this message failed to mention some roles and add their members to the thread
    FAILED_THREAD_MEMBER_ADD(8, false),
    // this message is "silent"
    SUPPRESS_NOTICICATIONS(12, true),
    IS_VOICE_MESSAGE(13, true);

    private final int id;
    private final boolean canBeSent;

    MessageFlag(int id, boolean canBeSent) {
        this.id = id;
        this.canBeSent = canBeSent;
    }

    public static EnumSet<MessageFlag> getFlagsByInt(int flags) {
        EnumSet<MessageFlag> set = EnumSet.noneOf(MessageFlag.class);
        if (flags == 0)
            return set;
        for (MessageFlag flag : MessageFlag.values()) {
            if ((flag.getLeftShiftId() & flags) == flag.getLeftShiftId())
                set.add(flag);
        }
        return set;
    }

    public int getLeftShiftId() {
        return 1 << id;
    }

    public boolean canBeSent() {
        return canBeSent;
    }
}
