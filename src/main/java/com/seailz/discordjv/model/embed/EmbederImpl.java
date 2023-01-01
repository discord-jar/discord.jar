package com.seailz.discordjv.model.embed;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;

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
        EmbedField[] fields = this.fields;
        EmbedField[] newFields = new EmbedField[fields.length + 1];
        System.arraycopy(fields, 0, newFields, 0, fields.length);
        newFields[fields.length] = field;
        this.fields = newFields;
        return this;
    }

    @Override
    public Embeder field(String name, String value, boolean inline) {
        return field(new EmbedField(name, value, inline));
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
