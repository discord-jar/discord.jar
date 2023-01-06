package com.seailz.discordjv.utils;

import java.util.List;

/**
 * Simple class that can be used to format strings.
 *
 * @author Seailz
 */
public class StringFormatter {

    private final List<Object> args;

    public StringFormatter(List<Object> args) {
        this.args = args;
    }

    public StringFormatter(Object... args) {
        this.args = List.of(args);
    }

    public String format(String string) {
        for (int i = 0; i < args.size(); i++) {
            string = string.replace("{" + i + "}", args.get(i).toString());
        }
        return string;
    }

    public static String format(String string, Object... args) {
        for (int i = 0; i < args.length; i++) {
            string = string.replace("{" + i + "}", args[i].toString());
        }
        return string;
    }
}
