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

    public static Thread requestVirtualThread(Runnable runnable, String name) {
        // First, let's check if we can actually use a virtual thread due to the JDK version.
        int javaRelease = Integer.parseInt(System.getProperty("java.specification.version"));
        if (javaRelease < 21) {
            Logger.getLogger("discord.jar-threading")
                    .warning("discord.jar virtual threads are only supported on JDK 21 and above, falling back to normal threads. It's recommended, if possible, that you upgrade your JDK to 21 or above.");
            return requestThread(runnable, name);
        }

        Thread virtualThread = Thread.ofVirtual()
                .start(runnable);
        virtualThread.setName(name);
        return virtualThread;
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
