package com.seailz.discordjar.model.component.select.entity;

import org.jetbrains.annotations.NotNull;

public enum MentionableType {
    ROLE, USER, CHANNEL;

    public @NotNull String getId() {
        return this.name().toLowerCase();
    }

    public static @NotNull MentionableType fromId(@NotNull String id) {
        return MentionableType.valueOf(id.toUpperCase());
    }
}
