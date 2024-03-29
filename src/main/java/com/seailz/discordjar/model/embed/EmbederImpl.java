package com.seailz.discordjar.model.embed;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.time.Instant;

public class EmbederImpl implements Embeder {

    private String title;
    private String description;
    private String url;
    private String timestamp;
    private EmbedField[] fields = new EmbedField[0];
    private int color = -1;
    private EmbedFooter footer;
    private EmbedImage image;
    private EmbedImage thumbnail;
    private EmbedAuthor author;

    public EmbederImpl() {
    }

    public EmbederImpl(String title, String description, String url, String timestamp, EmbedField[] fields, int color, EmbedFooter footer, EmbedImage image, EmbedImage thumbnail, EmbedAuthor author) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.timestamp = timestamp;
        this.fields = fields;
        this.color = color;
        this.footer = footer;
        this.image = image;
        this.thumbnail = thumbnail;
        this.author = author;
    }

    @Override
    public Embeder title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public Embeder description(String description) {
        this.description = description;
        return this;
    }

    @Override
    public Embeder url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public Embeder field(EmbedField field) {
        return field(field, fields.length);
    }

    @Override
    public Embeder field(EmbedField field, int index) {
        EmbedField[] fields = this.fields;
        EmbedField[] newFields = new EmbedField[fields.length + 1];
        System.arraycopy(fields, 0, newFields, 0, index);
        newFields[index] = field;
        System.arraycopy(fields, index, newFields, index + 1, fields.length - index);
        this.fields = newFields;
        return this;
    }

    @Override
    public Embeder field(String name, String value) {
        return field(new EmbedField(name, value, false));
    }

    @Override
    public Embeder field(String name, String value, int index) {
        return field(new EmbedField(name, value, false), index);
    }

    @Override
    public Embeder field(String name, String value, boolean inline) {
        return field(new EmbedField(name, value, inline));
    }

    @Override
    public Embeder field(String name, String value, boolean inline, int index) {
        return field(new EmbedField(name, value, inline), index);
    }

    @Override
    public Embeder footer(EmbedFooter footer) {
        this.footer = footer;
        return this;
    }

    @Override
    public Embeder footer(String text, String iconUrl) {
        return footer(new EmbedFooter(text, iconUrl, null));
    }

    @Override
    public Embeder image(EmbedImage image) {
        this.image = image;
        return this;
    }

    @Override
    public Embeder image(String url) {
        return image(new EmbedImage(url, null, 0, 0));
    }

    @Override
    public Embeder thumbnail(EmbedImage thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    @Override
    public Embeder thumbnail(String url) {
        return thumbnail(new EmbedImage(url, null, 0, 0));
    }

    @Override
    public Embeder author(EmbedAuthor author) {
        this.author = author;
        return this;
    }

    @Override
    public Embeder author(String name, String url, String iconUrl) {
        return author(new EmbedAuthor(name, url, iconUrl, null));
    }

    @Override
    public Embeder timestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public Embeder color(Color color) {
        this.color = color.getRGB();
        return this;
    }

    @Override
    public Embeder color(int color) {
        this.color = color;
        return this;
    }

    @Override
    public Embeder timestamp() {
        return timestamp(Instant.now().toString());
    }

    @Override
    public Embeder removeField(String name) {
        EmbedField[] fields = this.fields;
        EmbedField[] newFields = new EmbedField[fields.length - 1];
        int i = 0;
        for (EmbedField field : fields) {
            if (!field.name().equals(name)) {
                newFields[i++] = field;
            }
        }
        this.fields = newFields;
        return this;
    }


    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("title", title);
        obj.put("description", description);
        obj.put("url", url);
        obj.put("timestamp", timestamp);
        obj.put("color", color != -1 ? color + 0xFFFFFF : JSONObject.NULL);
        obj.put("footer", footer != null ? footer.compile() : JSONObject.NULL);
        obj.put("image", image != null ? image.compile() : JSONObject.NULL);
        obj.put("thumbnail", thumbnail != null ? thumbnail.compile() : JSONObject.NULL);
        obj.put("author", author != null ? author.compile() : JSONObject.NULL);

        // fields
        JSONArray fields = new JSONArray();
        for (EmbedField field : this.fields) {
            fields.put(field.compile());
        }
        obj.put("fields", fields);
        return obj;
    }


}
