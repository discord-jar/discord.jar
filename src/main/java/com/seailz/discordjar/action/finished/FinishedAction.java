package com.seailz.discordjar.action.finished;

import org.jetbrains.annotations.Nullable;

public class FinishedAction<T> {

    private final T result;

    public FinishedAction(T result) {
        this.result = result;
    }

    @Nullable
    public T get() {
        return result;
    }


}
