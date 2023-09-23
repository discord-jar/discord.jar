package com.seailz.discordjar.utils.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Handles threading for discord.jar, just simplifies some things.
 * @author Seailz
 */
public class DiscordJarThreadAllocator {

    /**
     * Requests a thread to be created and started. <b>All threads returned from this method are started automatically.</b>
     * @param runnable The runnable to run on the thread.
     * @return The thread that was created.
     */
    public static Thread requestThread(Runnable runnable, String name) {
        Thread thread = new Thread(runnable, name);
        try {
            thread.start();
        } catch (OutOfMemoryError ex) {
            Logger.getLogger("discord.jar-threading")
                    .severe("Out of memory error while starting thread " + name + "!");
            printThreads();
            throw ex;
        }
        return thread;
    }

    private static void printThreads() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        List<ThreadInfo> djarThreads = new ArrayList<>();
        List<ThreadInfo> globalThreads = new ArrayList<>();
        for(Long threadID : threadMXBean.getAllThreadIds()) {
            ThreadInfo info = threadMXBean.getThreadInfo(threadID);
            if (info.getThreadName().startsWith("djar--")) djarThreads.add(info);
            globalThreads.add(info);
        }

        System.out.println("djar-thread-count: " + djarThreads.size());
        for (ThreadInfo djarThread : djarThreads) {
            System.out.println("djar-thread: " + djarThread.getThreadName());
        }
        System.out.println("global-thread-count: " + globalThreads.size());
    }

}
