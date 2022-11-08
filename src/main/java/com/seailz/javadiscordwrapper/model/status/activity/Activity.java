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

    private String url;
    private int createdAt;
    private ActivityTimestamp timestamps;
    private String applicationId;
    private String details;
    private String state;
    private Emoji emoji;
    private ActivityParty party;
    private ActivityAssets assets;
    private ActivitySecrets secrets;
    private boolean instance;
    private EnumSet<ActivityFlags> flags;
    private ActivityButton[] buttons;

    public Activity(String name, ActivityType type) {
        this.name = name;
        this.type = type;
    }

    public Activity setUrl(String url) {
        this.url = url;
        return this;
    }

    public Activity setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Activity setTimestamps(ActivityTimestamp timestamps) {
        this.timestamps = timestamps;
        return this;
    }

    public Activity setApplicationId(String applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public Activity setDetails(String details) {
        this.details = details;
        return this;
    }

    public Activity setState(String state) {
        this.state = state;
        return this;
    }

    public Activity setEmoji(Emoji emoji) {
        this.emoji = emoji;
        return this;
    }

    public Activity setParty(ActivityParty party) {
        this.party = party;
        return this;
    }

    public Activity setAssets(ActivityAssets assets) {
        this.assets = assets;
        return this;
    }

    public Activity setSecrets(ActivitySecrets secrets) {
        this.secrets = secrets;
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

    public Activity setButtons(ActivityButton[] buttons) {
        this.buttons = buttons;
        return this;
    }

    public String name() {
        return name;
    }

    public ActivityType type() {
        return type;
    }

    public String url() {
        return url;
    }

    public int createdAt() {
        return createdAt;
    }

    public ActivityTimestamp timestamps() {
        return timestamps;
    }

    public String applicationId() {
        return applicationId;
    }

    public String details() {
        return details;
    }

    public String state() {
        return state;
    }

    public Emoji emoji() {
        return emoji;
    }

    public ActivityParty party() {
        return party;
    }

    public ActivityAssets assets() {
        return assets;
    }

    public ActivitySecrets secrets() {
        return secrets;
    }

    public boolean instance() {
        return instance;
    }

    public EnumSet<ActivityFlags> flags() {
        return flags;
    }

    public ActivityButton[] buttons() {
        return buttons;
    }

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("name", name)
                .put("type", type.getCode())
                .put("url", url)
                .put("created_at", createdAt)
                .put("timestamps", timestamps == null ? JSONObject.NULL : timestamps.compile())
                .put("application_id", applicationId)
                .put("details", details)
                .put("state", state)
                .put("emoji", emoji == null ? JSONObject.NULL : emoji.compile())
                .put("party", party == null ? JSONObject.NULL : party.compile())
                .put("assets", assets == null ? JSONObject.NULL : assets.compile())
                .put("secrets", secrets == null ? JSONObject.NULL : secrets.compile())
                .put("instance", instance)
                .put("flags", flags == null ? JSONObject.NULL : new JSONObject().put("flags", flags))
                .put("buttons", buttons == null ? JSONObject.NULL : new JSONObject().put("buttons", buttons));
    }

    @NotNull
    public static Activity decompile(JSONObject obj) {
        String name;
        ActivityType type;
        String url;
        int createdAt;
        ActivityTimestamp timestamps;
        String applicationId;
        String details;
        String state;
        Emoji emoji;
        ActivityParty party;
        ActivityAssets assets;
        ActivitySecrets secrets;
        boolean instance;
        EnumSet<ActivityFlags> flags;
        ActivityButton[] buttons;

        if (obj.has("name")) name = obj.getString("name");
        else throw new IllegalArgumentException("Activity must have a name");

        if (obj.has("type")) type = ActivityType.fromCode(obj.getInt("type"));
        else throw new IllegalArgumentException("Activity must have a type");

        if (obj.has("url")) url = obj.getString("url");
        else url = null;

        if (obj.has("created_at")) createdAt = obj.getInt("created_at");
        else createdAt = 0;

        if (obj.has("timestamps")) timestamps = ActivityTimestamp.decompile(obj.getJSONObject("timestamps"));
        else timestamps = null;

        if (obj.has("application_id")) applicationId = obj.getString("application_id");
        else applicationId = null;

        if (obj.has("details")) details = obj.getString("details");
        else details = null;

        if (obj.has("state")) state = obj.getString("state");
        else state = null;

        if (obj.has("emoji")) emoji = Emoji.decompile(obj.getJSONObject("emoji"));
        else emoji = null;

        if (obj.has("party")) party = ActivityParty.decompile(obj.getJSONObject("party"));
        else party = null;

        if (obj.has("assets")) assets = ActivityAssets.decompile(obj.getJSONObject("assets"));
        else assets = null;

        if (obj.has("secrets")) secrets = ActivitySecrets.decompile(obj.getJSONObject("secrets"));
        else secrets = null;

        if (obj.has("instance")) instance = obj.getBoolean("instance");
        else instance = false;

        if (obj.has("flags")) flags = ActivityFlags.fromInt(obj.getInt("flags"));
        else flags = null;

        if (obj.has("buttons")) {
            JSONArray arr = obj.getJSONArray("buttons");
            buttons = new ActivityButton[arr.length()];
            for (int i = 0; i < arr.length(); i++) {
                buttons[i] = ActivityButton.decompile(arr.getJSONObject(i));
            }
        } else buttons = null;

        return new Activity(name, type)
                .setUrl(url)
                .setCreatedAt(createdAt)
                .setTimestamps(timestamps)
                .setApplicationId(applicationId)
                .setDetails(details)
                .setState(state)
                .setEmoji(emoji)
                .setParty(party)
                .setAssets(assets)
                .setSecrets(secrets)
                .setInstance(instance)
                .setFlags(flags)
                .setButtons(buttons);
    }
}