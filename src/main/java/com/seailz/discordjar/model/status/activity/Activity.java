package com.seailz.discordjar.model.status.activity;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.emoji.Emoji;
import com.seailz.discordjar.model.status.Status;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.EnumSet;

/**
 * Represents an activity
 *
 * @author Seailz
 * @see Status
 * @since 1.0
 */
public class Activity implements Compilerable {

    private final String name;
    private final ActivityType type;
    private String state;

    private String streamUrl;
    private String applicationId;
    private Emoji emoji;
    private boolean instance;
    private EnumSet<ActivityFlags> flags;

    public Activity(String name, ActivityType type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Allows a bot to define a custom status.
     * Please note: Emojis are not supported in custom statuses for bots.
     */
    public Activity(String customStatus) {
        this(customStatus, ActivityType.CUSTOM);
        setState(customStatus);
    }

    @NotNull
    public static Activity decompile(JSONObject obj, DiscordJar discordJar) {
        String name;
        ActivityType type;
        String url;
        String applicationId;
        String state;
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

        if (obj.has("emoji")) emoji = Emoji.decompile(obj.getJSONObject("emoji"), discordJar);
        else emoji = null;

        if (obj.has("instance")) instance = obj.getBoolean("instance");
        else instance = false;

        if (obj.has("flags")) flags = ActivityFlags.fromInt(obj.getInt("flags"));
        else flags = null;

        if (obj.has("state")) state = obj.getString("state");
        else state = null;


        return new Activity(name, type)
                .setStreamUrl(url)
                .setApplicationId(applicationId)
                .setEmoji(emoji)
                .setInstance(instance)
                .setFlags(flags);
    }

    /**
     * If you've selected an Activity Type that's not 4 (custom), you can set the state as extra info to your activity.
     * User's current party status.
     */
    public Activity setState(String state) {
        this.state = state;
        return this;
    }

    public Activity setStreamUrl(String url) {
        this.streamUrl = url;
        return this;
    }

    public Activity setApplicationId(String applicationId) {
        this.applicationId = applicationId;
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

    public Activity setEmoji(Emoji emoji) {
        this.emoji = emoji;
        return this;
    }

    public String name() {
        return name;
    }

    public ActivityType type() {
        return type;
    }

    public String state() {
        return state;
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
                .put("state", state);
    }
}