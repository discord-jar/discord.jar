package com.seailz.discordjar.utils.rest.ratelimit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bucket {

    private int limit;
    private AtomicInteger atomicRemaining;
    private AtomicLong atomicReset;
    private String id;
    private int waitingRequests = 0;
    private final Lock lock = new ReentrantLock();
    private final List<String> affectedRoutes = new ArrayList<>();
    private boolean allowedToSendRequests = true;

    public Bucket(String id, int limit, int remaining, long reset, double resetAfter) {
        this.id = id;
        this.limit = limit;
        this.atomicRemaining = new AtomicInteger(remaining);
        this.atomicReset = new AtomicLong(reset); // Convert reset to milliseconds
        long resetAfterMillis = (long) (resetAfter * 1000); // Convert resetAfter to milliseconds

        // Start the reset thread
        new Thread(() -> {
            while (true) {
                if (remaining != 0) {
                    continue;
                }
                long currentReset = atomicReset.get();
                long currentTime = System.currentTimeMillis();

                if (currentReset > currentTime) {
                    try {
                        Thread.sleep(currentReset - currentTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (atomicReset.compareAndSet(currentReset, System.currentTimeMillis() + resetAfterMillis)) {
                    reset(); // Reset the bucket only if the currentReset is still the same as the one we fetched above.
                }
            }
        }).start();
    }

    public Bucket update(int limit, int remaining, double reset, float resetAfter) {
        if (remaining == 0) allowedToSendRequests = false;
        this.limit = limit;
        this.atomicRemaining.set(remaining);
        this.atomicReset.set((long) (reset * 1000)); // Convert reset to milliseconds
        return this;
    }

    public void reset() {
        // Reset the bucket.
        atomicRemaining.set(limit);
        allowedToSendRequests = true;
    }

    public String id() {
        return id;
    }

    public void addAffectedRoute(String route) {
        affectedRoutes.add(route);
    }

    public List<String> getAffectedRoutes() {
        return affectedRoutes;
    }

    public void waitYourTurn() throws InterruptedException {
        if (atomicRemaining.get() > 2) {
            return;
        } else {
            while (true) {
                if (atomicRemaining.get() > 2 && allowedToSendRequests) {
                    atomicRemaining.set(atomicRemaining.get() - 1);
                    return;
                }
                Thread.sleep(10);
            }
        }
    }
}
