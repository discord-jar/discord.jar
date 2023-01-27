package com.seailz.discordjv.model.channel.thread;

import java.util.List;

/**
 * Represents a {@link Thread} in a {@link com.seailz.discordjv.model.channel.ForumChannel ForumChannel}.
 * These threads have an <b>applied_tags</b> parameter. <b>applied_tags</b> is an array of snowflake ids.
 *
 * These snowflakes are ids of {@link com.seailz.discordjv.model.channel.forum.ForumTag ForumTags} that have been applied to the thread.
 * @author Seailz
 * @since  1.0
 * @see    Thread
 */
public interface ForumThread extends Thread {
    /**
     * List of snowflakes of {@link com.seailz.discordjv.model.channel.forum.ForumTag ForumTags} that have been applied to the thread.
     * @return {@link List} of {@link String}
     */
    List<String> appliedTags();
}
