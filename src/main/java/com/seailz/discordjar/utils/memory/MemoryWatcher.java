package com.seailz.discordjar.utils.memory;

import com.seailz.discordjar.DiscordJar;

import java.lang.management.ManagementFactory;

/**
 * This class is used to monitor the memory usage of the bot.
 * If it exceeds a certain threshold, then all caches will be cleared.
 *
 * @author Seailz
 * @since 1.0
 */
public class MemoryWatcher extends Thread {
    private static final long INTERVAL = 600; // Interval in milliseconds
    private static final double THRESHOLD = 0.85; // Memory usage threshold %
    private final DiscordJar bot;

    public MemoryWatcher(DiscordJar bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        while (true) {
            // Get memory usage information
            double usedMemory = getUsedMemory();
            double totalMemory = getTotalMemory();
            double usedPercent = usedMemory / totalMemory;
            System.out.println(usedPercent);

            // Check if memory usage is above threshold
            if (usedPercent > THRESHOLD) {
                clearCaches();
            }

            try {
                // Wait for the specified interval before checking again
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private double getUsedMemory() {
        // Get the current memory usage in bytes
        long usedMemoryBytes = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
        return (double) usedMemoryBytes / (1024 * 1024); // Convert to megabytes
    }

    private double getTotalMemory() {
        // Get the maximum memory allocation in bytes
        long maxMemoryBytes = Runtime.getRuntime().maxMemory();
        return (double) maxMemoryBytes / (1024 * 1024); // Convert to megabytes
    }

    private void clearCaches() {
        bot.getChannelCache().clear();
        bot.getGuildCache().clear();
        bot.getUserCache().clear();
    }
}
