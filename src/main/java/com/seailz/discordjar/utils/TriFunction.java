package com.seailz.discordjar.utils;

import com.seailz.discordjar.utils.rest.DiscordRequest;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a function that accepts three arguments and produces a result.
 */
@FunctionalInterface
public interface TriFunction<A,B,C,R> {

    R apply(A a, B b, C c) throws DiscordRequest.UnhandledDiscordAPIErrorException;

    default <V> TriFunction<A, B, C, V> andThen(
            Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (A a, B b, C c) -> after.apply(apply(a, b, c));
    }
}