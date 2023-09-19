package com.seailz.discordjar.utils.rest.ratelimit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
    private volatile boolean allowedToSendRequests = true;
    private AtomicLong resetAfter;
    private volatile boolean waiting = false;
    private boolean debug = false;

    public Bucket(String id, int limit, int remaining, long reset, double resetAfter, boolean debug) {
        this.id = id;
        this.limit = limit;
        this.atomicRemaining = new AtomicInteger(remaining);
        this.atomicReset = new AtomicLong(reset); // Convert reset to milliseconds
        long resetAfterMillis = (long) (resetAfter * 1000); // Convert resetAfter to milliseconds
        this.resetAfter = new AtomicLong(resetAfterMillis);
        this.debug = debug;
        // Start the reset thread
//        new Thread(() -> {
//            while (true) {
//                if (remaining != 0) {
//                    continue;
//                }
//                long currentReset = atomicReset.get();
//                long currentTime = System.currentTimeMillis();
//
//                if (currentReset > currentTime) {
//                    try {
//                        Thread.sleep(currentReset - currentTime);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (atomicReset.compareAndSet(currentReset, System.currentTimeMillis() + resetAfterMillis)) {
//                    if (debug) {
//                        System.out.println("Resetting requests for " + id);
//                    }
//                    reset(); // Reset the bucket only if the currentReset is still the same as the one we fetched above.
//                }
//            }
//        }, "djar--bucket-reset").start();
    }

    public synchronized  Bucket update(int limit, int remaining, double reset, float resetAfter) {
        if (remaining == 0) allowedToSendRequests = false;
        this.limit = limit;
        this.atomicRemaining.set(remaining);
        this.atomicReset.set((long) (reset * 1000)); // Convert reset to milliseconds
        this.resetAfter.set((long) (resetAfter * 1000)); // Convert resetAfter to milliseconds
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

    public Bucket addAffectedRoute(String route) {
        affectedRoutes.add(route);
        return this;
    }

    public List<String> getAffectedRoutes() {
        return affectedRoutes;
    }

    public void await(long ms) {
        new Thread(() -> {
            waiting = true;
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            waiting = false;
        }, "djar--bucket-await").start();
    }

    public synchronized UUID awaitClearance() throws InterruptedException {
//        while (!waiting) {
//            Thread.onSpinWait();
//        }
        long start = System.currentTimeMillis();
        waitingRequests++;
        while (!allowedToSendRequests) {
            Thread.onSpinWait();
        }


        if (debug) System.out.println("Clearing request for launch: WR:" + waitingRequests + " AR:" + atomicRemaining.get() + " BID: " + id + " RSA:" + resetAfter);
        atomicRemaining.getAndDecrement();
        if (atomicRemaining.get() < 1) {
            atomicRemaining.set(0);
            if (debug) System.out.println("Cancelling launch clearance: WR:" + waitingRequests + " AR:" + atomicRemaining.get() +" RA:" + resetAfter);
            long resetAfterFinal = resetAfter.get();
            Thread.sleep(resetAfter.get());
            if (resetAfterFinal != resetAfter.get()) {
                if (debug) System.out.println("ResetAfter changed from " + resetAfterFinal + " to " + resetAfter);
                // Wait the difference
                long diff = resetAfter.get() - resetAfterFinal;
                if (diff > 0) Thread.sleep(resetAfter.get() - resetAfterFinal);
            }
            atomicRemaining.set(limit - 1);
            if (debug) System.out.println("Clearing launch again: WR:" + waitingRequests + " AR:" + atomicRemaining.get());
        }

        waitingRequests--;
        UUID uuid = UUID.randomUUID();
        if (debug) System.out.println("Launched " + uuid + " in " + (System.currentTimeMillis() - start) + "ms");
        return uuid;
    }

    public void setAllowedToSendRequests(boolean allowedToSendRequests) {
        this.allowedToSendRequests = allowedToSendRequests;
    }

    public void setReset(long atomicReset) {
        this.atomicReset.set(atomicReset);
    }

    public void setRemaining(int atomicRemaining) {
        this.atomicRemaining.set(atomicRemaining);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setResetAfter(Long resetAfter) {
        this.resetAfter.set(resetAfter);
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public void setWaitingRequests(int waitingRequests) {
        this.waitingRequests = waitingRequests;
    }
}
