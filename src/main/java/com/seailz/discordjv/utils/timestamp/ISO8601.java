package com.seailz.discordjv.utils.timestamp;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * A utility class for converting ISO8601 timestamps to Java {@link Date} objects.
 *
 * @author Seailz
 * @since  1.0
 * @see    Date
 * @see    Calendar
 * @see    <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO 8601</a>
 */
public class ISO8601 implements Serializable {

    private Date date;
    private String timestamp;

    public ISO8601(Date date) {
        this.date = date;
        this.timestamp = date.toInstant().toString();
    }

    public ISO8601(String timestamp) {
        this.timestamp = timestamp;
        this.date = Date.from(java.time.Instant.parse(timestamp));
    }

    public Date getDate() {
        return date;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public static ISO8601 inSeconds(int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, seconds);
        return new ISO8601(calendar.getTime());
    }

    @Override
    public String toString() {
        return timestamp;
    }

}
