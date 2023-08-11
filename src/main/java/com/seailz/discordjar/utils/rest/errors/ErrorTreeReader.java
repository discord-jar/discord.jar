package com.seailz.discordjar.utils.rest.errors;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * Reads error trees from Discord's API and returns a formatted string.
 * @see com.seailz.discordjar.utils.rest.Response
 * @see com.seailz.discordjar.utils.rest.DiscordRequest
 * @author Seailz
 */
public class ErrorTreeReader {

    public static String readErrorTree(JSONObject tree, int errorCode) {
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(errorCode).append("] ");

        if (tree.has("message")) builder.append(tree.getString("message"));
        if (!tree.has("errors")) return builder.toString();

        builder.append(" - Errors:\n");
        JSONObject errors = tree.getJSONObject("errors");
        appendErrors("", errors, builder, 1);

        return builder.toString();
    }

    private static void appendErrors(String prefix, JSONObject errors, StringBuilder builder, int depth) {
        for (String key : errors.keySet()) {
            Object raw = errors.get(key);
            if (raw instanceof JSONObject) {
                String newPrefix = prefix + (isInt(key) ? "[" + key + "]" : formatKey(key));

                JSONObject nextJsonObj = (JSONObject) raw;
                boolean nextKeyIsInt = !nextJsonObj.keySet().isEmpty() && isInt((String) nextJsonObj.keySet().toArray()[0]);

                if (!nextKeyIsInt) {
                    StringBuilder tabbingBuilder = new StringBuilder();
                    tabbingBuilder.append("\t".repeat(Math.max(0, depth)));
                    newPrefix += ":\n" + tabbingBuilder;
                }


                appendErrors(newPrefix, (JSONObject) raw, builder, depth + 1);

            } else if (raw instanceof JSONArray && key.equals("_errors")) {
                JSONArray array = (JSONArray) raw;
                for (Object o : array.toList()) {
                    JSONObject object = new JSONObject((Map<?, ?>) o);
                    builder.append(prefix)
                            .append(object.getString("code"))
                            .append(": ")
                            .append(object.getString("message"))
                            .append("\n");
                }
            }
        }
    }

    private static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    private static String formatKey(String key) {
        return key.replaceAll("_", " ");
    }

}
