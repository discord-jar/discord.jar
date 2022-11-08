package com.seailz.javadiscordwrapper.model.status.activity;

import com.seailz.javadiscordwrapper.core.Compilerable;
import com.seailz.javadiscordwrapper.model.emoji.Emoji;
import com.seailz.javadiscordwrapper.model.status.Status;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.EnumSet;

/**
 * Represents an activity
 *
 * @author Seailz
 * @since  1.0
 * @see    Status
 */
public class Activity implements Compilerable {

    private String name;
    private ActivityType type;

    private String streamUrl;
    private String applicationId;
    private Emoji emoji;
    private boolean instance;
    private EnumSet<ActivityFlags> flags;

    public Activity(String name, ActivityType type) {
        this.name = name;
        this.type = type;
    }

    public Activity setStreamUrl(String url) {
        this.streamUrl = url;
        return this;
    }

    public Activity setApplicationId(String applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public Activity setEmoji(Emoji emoji) {
        this.emoji = emoji;
        return this;
    }

    public Activity setInstance(boolean instance) {
        this.instance = instance;
        return this;
    }

    public Activity setFlags(EnumSet<ActivityFlags> flags) {
        this.flags = flags;
        return this;
    }

    public String name() {
        return name;
    }

    public ActivityType type() {
        return type;
    }

    public String streamUrl() {
        return streamUrl;
    }

    public String applicationId() {
        return applicationId;
    }

    public Emoji emoji() {
        return emoji;
    }

    public boolean instance() {
        return instance;
    }

    public EnumSet<ActivityFlags> flags() {
        return flags;
    }

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("name", name)
                .put("type", type.getCode())
                .put("url", streamUrl)
                .put("application_id", applicationId)
                .put("emoji", emoji == null ? JSONObject.NULL : emoji.compile())
                .put("instance", instance)
                .put("flags", flags == null ? JSONObject.NULL : new JSONObject().put("flags", flags));
    }

    @NotNull
    public static Activity decompile(JSONObject obj) {
        String name;
        ActivityType type;
        String url;
        String applicationId;
        Emoji emoji;
        boolean instance;
        EnumSet<ActivityFlags> flags;

        if (obj.has("name")) name = obj.getString("name");
        else throw new IllegalArgumentException("Activity must have a name");

        if (obj.has("type")) type = ActivityType.fromCode(obj.getInt("type"));
        else throw new IllegalArgumentException("Activity must have a type");

        if (obj.has("url")) url = obj.getString("url");
        else url = null;

        if (obj.has("application_id")) applicationId = obj.getString("application_id");
        else applicationId = null;

        if (obj.has("emoji")) emoji = Emoji.decompile(obj.getJSONObject("emoji"));
        else emoji = null;

        if (obj.has("instance")) instance = obj.getBoolean("instance");
        else instance = false;

        if (obj.has("flags")) flags = ActivityFlags.fromInt(obj.getInt("flags"));
        else flags = null;

        return new Activity(name, type)
                .setStreamUrl(url)
                .setApplicationId(applicationId)
                .setEmoji(emoji)
                .setInstance(instance)
                .setFlags(flags);
    }
}