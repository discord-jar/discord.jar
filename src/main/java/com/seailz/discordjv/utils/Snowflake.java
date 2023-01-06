package com.seailz.discordjv.utils;

import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * This interface represents a Discord snowflake,
 * <br>a unique identifier for various objects in the Discord API.
 * <p>
 * Snowflakes are 64-bit integers that are generated using a combination of a
 * <ul>
 *     <li>timestamp</li>
 *     <li>the internal worker ID</li>
 *     <li>the internal process ID</li>
 *     <li>an incrementing counter</li>
 * </ul>
 *
 * @author Seailz
 */
public interface Snowflake {

    /**
     * Get the snowflake id
     */
    String id();

    /**
     * Get the snowflake id as a long
     */
    default long idAsLong() {
        return Long.parseLong(id());
    }

    /**
     * Milliseconds since Discord Epoch, the first second of 2015 or 1420070400000.
     * <br>This is usually the time this snowflake was created.
     * <p>
     * This method is usually the same as when a message was sent,
     * <br>or a user signed up for example.
     *
     * @return {@link Long}
     * @author Seailz
     */
    default long timestampRaw() {
        return (idAsLong() >> 22) + 1420070400000L;
    }

    /**
     * Returns {@link #timestampRaw()} as an {@link OffsetDateTime}.
     */
    default OffsetDateTime timestampAsOffsetDateTime() {
        Calendar timezone = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        timezone.setTimeInMillis(timestampRaw());

        return OffsetDateTime.ofInstant(timezone.toInstant(), timezone.getTimeZone().toZoneId());
    }

    /**
     * Returns the internal worker id
     *
     * @return {@link Long}
     */
    default long internalWorkerId() {
        return (idAsLong() & 0x3E0000) >> 17;
    }

    /**
     * Returns the internal process id
     *
     * @return {@link Long}
     */
    default long process() {
        return (idAsLong() & 0x1F000) >> 12;
    }

    /**
     * For every ID that is generated on that {@link #process}, this number is incremented
     */
    default long increment() {
        return idAsLong() & 0xFFF;
    }

}
