package com.seailz.discordjv.utils;

import com.seailz.discordjv.utils.annotation.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A utility class for checking arguments and throwing exceptions if they are invalid.
 *
 * @author Seailz
 * @since 1.0
 */
@UtilityClass
public class Checker {

    public static void check(boolean condition, @NotNull String message) {
        if (condition) illegalArgument(message);
    }

    public static void checkInvert(boolean condition, @NotNull String message) {
        if (condition) illegalArgument(message);
    }

    /**
     * Checks if a given string is a snowflake.
     *
     * @param id      The string to check.
     * @param message The message to throw if the string is not a snowflake.
     */
    @Contract("null, _ -> fail")
    public static void isSnowflake(@Nullable String id, @NotNull String message) {
        notNull(id, message);
        if (!id.matches("[0-9]{17,19}"))
            illegalArgument(message);
    }

    /**
     * Checks if a given object is null.
     *
     * @param object  The object to check.
     * @param message The message to throw if the object is null.
     */
    @Contract("null, _ -> fail")
    public static void notNull(@Nullable Object object, @NotNull String message) {
        if (object == null) illegalArgument(message);
    }

    /**
     * Checks if a given string matches a given regex.
     *
     * @param regex   The regex to match.
     * @param string  The string to check.
     * @param message The message to throw if the string does not match the regex.
     */
    public void matches(String regex, String string, String message, boolean invert) {
        if (string.matches(regex)) illegalArgument(message);
        if (invert)
            if (!string.matches(regex)) illegalArgument(message);
    }

    public void matches(String regex, String string, String message) {
        matches(regex, string, message, false);
    }

    /**
     * Checks the length of a given string.
     *
     * @param length  The length to check.
     * @param string  The string to check.
     * @param message The message to throw if the string is not the given length.
     */
    public void length(int length, String string, String message) {
        matches("{" + length + "}", string, message);
    }

    public void size(List<?> list, int size, String message) {
        if (list.size() != size) illegalArgument(message);
    }

    public static void sizeLessThan(List<?> list, int size, String message) {
        if (list.size() > size) illegalArgument(message);
    }

    public void sizeGreaterThan(List<?> list, int size, String message) {
        if (list.size() < size) illegalArgument(message);
    }

    private static void illegalArgument(String message) {
        throw new IllegalArgumentException(message);
    }

    private static void nullArgument(String message) {
        throw new NullArgumentException(message);
    }

    public static class NullArgumentException extends IllegalArgumentException {
        public NullArgumentException(String message) {
            super(message);
        }
    }
}
