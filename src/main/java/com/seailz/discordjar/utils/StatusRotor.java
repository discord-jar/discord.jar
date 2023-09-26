package com.seailz.discordjar.utils;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.status.Status;

import java.util.List;

/**
 * Simple util for a status rotor - switches between statuses every x seconds.
 *
 * @author Seailz
 */
public class StatusRotor {

    private final List<Status> statuses;
    private final long interval;
    private final DiscordJar dJar;
    private boolean running;

    /**
     * Creates a new status rotor.
     * Start the rotor by calling {@link #start()}
     *
     * @param status   The statuses to rotate between
     * @param interval The interval (in milliseconds) to switch between statuses
     * @param dJar     The DiscordJar instance
     */
    public StatusRotor(List<Status> status, long interval, DiscordJar dJar) {
        this.statuses = status;
        this.interval = interval;
        this.dJar = dJar;

        new Thread(() -> {
            int index = 0;
            while (true) {
                if (!running) continue;
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (!running) continue;
                if (statuses == null) continue;
                if (statuses.size() == 0) continue;

                dJar.setStatus(statuses.get(index));
                index++;
                if (index >= statuses.size()) index = 0;
            }
        }, "djar--Status Rotor").start();
    }

    /**
     * Starts the rotor
     */
    public void start() {
        this.running = true;
    }

    /**
     * Stops the rotor
     */
    public void stop() {
        this.running = false;
    }

    public DiscordJar getdJar() {
        return dJar;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public long getInterval() {
        return interval;
    }

    public StatusRotor addStatus(Status status) {
        statuses.add(status);
        return this;
    }
}
