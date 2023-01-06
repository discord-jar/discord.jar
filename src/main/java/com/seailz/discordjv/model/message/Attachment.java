package com.seailz.discordjv.model.message;

import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.model.resolve.Resolvable;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

import java.io.File;

/**
 * Represents a message attachment.
 *
 * @param id          The attachment ID.
 * @param fileName    The name of the file attached.
 * @param description The attachment description.
 * @param type        The attachment MIME type.
 * @param size        The size of the file in bytes.
 * @param url         The source URL of the file.
 * @param proxyUrl    A proxied URL of the file.
 * @param height      The height of the file (if image).
 * @param width       The width of the file (if image).
 * @param ephemeral   Whether this attachment is ephemeral.
 */
public record Attachment(
        int id,
        String fileName,
        String description,
        String type,
        int size,
        String url,
        String proxyUrl,
        int height,
        int width,
        boolean ephemeral
) implements Compilerable, Resolvable {


    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("filename", fileName);
        obj.put("description", description);
        if (type != null) obj.put("type", type);
        if (size != -1) obj.put("size", size);
        if (url != null) obj.put("url", url);
        if (proxyUrl != null) obj.put("proxy_url", proxyUrl);
        if (height != -1) obj.put("height", height);
        if (width != -1) obj.put("width", width);
        if (ephemeral) obj.put("ephemeral", true);
        return obj;
    }

    public static Attachment fromFile(int id, File file) {
        return new Attachment(
                id,
                file.getName(),
                "File",
                null,
                -1,
                null,
                null,
                -1,
                -1,
                false
        );
    }

    @NonNull
    public static Attachment decompile(JSONObject obj) {
        int id;
        String fileName;
        String description;
        String type;
        int size;
        String url;
        String proxyUrl;
        int height;
        int width;
        boolean ephemeral;

        try {
            id = obj.getInt("id");
        } catch (Exception e) {
            id = 0;
        }

        try {
            fileName = obj.getString("filename");
        } catch (Exception e) {
            fileName = null;
        }

        try {
            description = obj.getString("description");
        } catch (Exception e) {
            description = null;
        }

        try {
            type = obj.getString("type");
        } catch (Exception e) {
            type = null;
        }

        try {
            size = obj.getInt("size");
        } catch (Exception e) {
            size = 0;
        }

        try {
            url = obj.getString("url");
        } catch (Exception e) {
            url = null;
        }

        try {
            proxyUrl = obj.getString("proxy_url");
        } catch (Exception e) {
            proxyUrl = null;
        }

        try {
            height = obj.getInt("height");
        } catch (Exception e) {
            height = 0;
        }

        try {
            width = obj.getInt("width");
        } catch (Exception e) {
            width = 0;
        }

        try {
            ephemeral = obj.getBoolean("ephemeral");
        } catch (Exception e) {
            ephemeral = false;
        }

        return new Attachment(id, fileName, description, type, size, url, proxyUrl, height, width, ephemeral);
    }
}
