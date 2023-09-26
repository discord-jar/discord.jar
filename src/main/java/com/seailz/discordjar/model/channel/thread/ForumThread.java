package com.seailz.discordjar.model.channel.thread;

import java.util.List;

/**
 * Represents a {@link Thread} in a {@link com.seailz.discordjar.model.channel.ForumChannel ForumChannel}.
 * These threads have an <b>applied_tags</b> parameter. <b>applied_tags</b> is an array of snowflake ids.
 * <p>
 * These snowflakes are ids of {@link com.seailz.discordjar.model.channel.forum.ForumTag ForumTags} that have been applied to the thread.
 *
 * @author Seailz
 * @see Thread
 * @since 1.0
 */
public interface ForumThread extends Thread {
    /**
     * List of snowflakes of {@link com.seailz.discordjar.model.channel.forum.ForumTag ForumTags} that have been applied to the thread.
     *
     * @return {@link List} of {@link String}
     */
    List<String> appliedTags();
}
