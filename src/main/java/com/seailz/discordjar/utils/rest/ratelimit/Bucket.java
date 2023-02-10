package com.seailz.discordjar.utils.rest.ratelimit;

import java.util.ArrayList;
import java.util.List;

public class Bucket {
    private List<String> affectedRoutes = new ArrayList<>();
    private final String id;
    private final int remaining;
    private final double resetAfter;

    public Bucket(String id, int remaining, double resetAfter) {
        this.id = id;
        this.remaining = remaining;
        this.resetAfter = resetAfter;
    }

    public double getResetAfter() {
        return resetAfter;
    }

    public int getRemaining() {
        return remaining;
    }

    public List<String> getAffectedRoutes() {
        return affectedRoutes;
    }

    public Bucket setAffectedRoutes(List<String> affectedRoutes) {
        this.affectedRoutes = affectedRoutes;
        return this;
    }

    public String getId() {
        return id;
    }
}
