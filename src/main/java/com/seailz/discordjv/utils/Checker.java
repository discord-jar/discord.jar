package com.seailz.discordjv.utils;

import com.seailz.discordjv.utils.annotation.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A utility class for checking arguments and throwing exceptions if they are invalid.
 *
 * @author Seailz
 * @since 1.0
 */
@UtilityClass
public class Checker {

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
            throw new IllegalArgumentException(message);
    }

    /**
     * Checks if a given object is null.
     *
     * @param object  The object to check.
     * @param message The message to throw if the object is null.
     */
    @Contract("null, _ -> fail")
    public static void notNull(@Nullable Object object, @NotNull String message) {
        if (object == null) throw new IllegalArgumentException(message);
    }

}
