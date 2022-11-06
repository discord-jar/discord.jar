package com.seailz.javadiscordwrapper.model.message;

import java.util.EnumSet;

/**
 * Represents a flag on a message,
 * @author Seailz
 * @since 1.0
 */
public enum MessageFlag {

    // this message has been published to subscribed channels (via Channel Following)
    CROSSPOSTED(0),
    // this message originated from a message in another channel (via Channel Following)
    IS_CROSSPOST(1),
    // do not include any embeds when serializing this message
    SUPPRESS_EMBEDS(2),
    // the source message for this crosspost has been deleted (via Channel Following)
    SOURCE_MESSAGE_DELETED(3),
    // this message came from the urgent message system
    URGENT(4),
    // this message has an associated thread, with the same id as the message
    HAS_THREAD(5),
    // this message is only visible to the user who invoked the Interaction
    EPHEMERAL(6),
    // this message is an Interaction Response and the bot is "thinking"
    LOADING(7),
    // this message failed to mention some roles and add their members to the thread
    FAILED_THREAD_MEMBER_ADD(8),
    ;

    private int id;

    MessageFlag(int id) {
        this.id = id;
    }

    public int getLeftShiftId() {
        return 1 << id;
    }

    public static EnumSet<MessageFlag> getFlagsByInt(int flags) {
        EnumSet<MessageFlag> set = EnumSet.noneOf(MessageFlag.class);
        if (flags == 0)
            return set;
        for (MessageFlag flag : MessageFlag.values())
        {
            if ((flag.getLeftShiftId() & flags) == flag.getLeftShiftId())
                set.add(flag);
        }
        return set;
    }
}
