package com.seailz.javadiscordwrapper.model.interaction;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.core.Compilerable;
import com.seailz.javadiscordwrapper.model.application.Application;
import com.seailz.javadiscordwrapper.model.channel.Channel;
import com.seailz.javadiscordwrapper.model.guild.Guild;
import com.seailz.javadiscordwrapper.model.guild.Member;
import com.seailz.javadiscordwrapper.model.message.Message;
import com.seailz.javadiscordwrapper.model.user.User;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.lang.reflect.Method;

/**
 * Represents the data of an interaction
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.model.interaction.Interaction
 */
public class Interaction implements Compilerable {

    // The id of the interaction
    private String id;
    // The id of the application the interaction is for
    private Application application;
    // The type of interaction
    private InteractionType type;
    // Interaction data payload
    private InteractionData data;
    // The guild it was sent from
    private Guild guild;
    // The channel it was sent from
    private Channel channel;
    // Guild member data for the invoking user, including permissions
    private Member member;
    // User object for the invoking user, if invoked in a DM
    private User user;
    // A continuation token for responding to the interaction
    private String token;
    // Read-only property, always 1
    private int version;
    // For components, the message they were attached to
    private Message message;
    // Bitwise set of permissions the app or bot has within the channel the interaction was sent from
    private String appPermissions;
    // Selected language of the invoking user
    private String locale;
    // Guild's preferred locale, if invoked in a guild
    private String guildLocale;

    public Interaction(String id, Application application, InteractionType type, InteractionData data, Guild guild, Channel channel, Member member, User user, String token, int version, Message message, String appPermissions, String locale, String guildLocale) {
        this.id = id;
        this.application = application;
        this.type = type;
        this.data = data;
        this.guild = guild;
        this.channel = channel;
        this.member = member;
        this.user = user;
        this.token = token;
        this.version = version;
        this.message = message;
        this.appPermissions = appPermissions;
        this.locale = locale;
        this.guildLocale = guildLocale;
    }

    public String id() {
        return id;
    }

    public Application application() {
        return application;
    }

    public InteractionType type() {
        return type;
    }

    public InteractionData data() {
        return data;
    }

    public Guild guild() {
        return guild;
    }

    public Channel channel() {
        return channel;
    }

    public Member member() {
        return member;
    }

    public User user() {
        return user;
    }

    public String token() {
        return token;
    }

    public int version() {
        return version;
    }

    public Message message() {
        return message;
    }

    public String appPermissions() {
        return appPermissions;
    }

    public String locale() {
        return locale;
    }

    public String guildLocale() {
        return guildLocale;
    }

    @Override
    @SneakyThrows
    public JSONObject compile() {
        Class<? extends InteractionData> dataClass = data.getClass();
        Method compileMethod = dataClass.getMethod("compile");
        JSONObject data = (JSONObject) compileMethod.invoke(this.data);

        return new JSONObject()
                .put("id", id)
                .put("application", application.id())
                .put("type", type.getCode())
                .put("data", data)
                .put("guild", guild.id())
                .put("channel", channel.id())
                .put("member", member.compile())
                .put("user", user.compile())
                .put("token", token)
                .put("version", version)
                .put("message", message.compile())
                .put("appPermissions", appPermissions)
                .put("locale", locale)
                .put("guildLocale", guildLocale);
    }

    @NotNull
    public static Interaction decompile(JSONObject json, DiscordJv discordJv) {
        String id = json.has("id") ? json.getString("id") : null;
        Application application = json.has("application") ? Application.decompile(json.getJSONObject("application")) : null;
        InteractionType type = json.has("type") ? InteractionType.getType(json.getInt("type")) : null;
        InteractionData data = json.has("data") ? InteractionData.decompile(type, json.getJSONObject("data"), discordJv) : null;
        Guild guild = json.has("guild") ? Guild.decompile(json.getJSONObject("guild"), discordJv) : null;
        Channel channel = json.has("channel") ? Channel.decompile(json.getJSONObject("channel"), discordJv) : null;
        Member member = json.has("member") ? Member.decompile(json.getJSONObject("member")) : null;
        User user = json.has("user") ? User.decompile(json.getJSONObject("user")) : null;
        String token = json.has("token") ? json.getString("token") : null;
        int version = json.has("version") ? json.getInt("version") : 0;
        Message message = json.has("message") ? Message.decompile(json.getJSONObject("message"), discordJv) : null;
        String appPermissions = json.has("appPermissions") ? json.getString("appPermissions") : null;
        String locale = json.has("locale") ? json.getString("locale") : null;
        String guildLocale = json.has("guildLocale") ? json.getString("guildLocale") : null;

        return new Interaction(id, application, type, data, guild, channel, member, user, token, version, message, appPermissions, locale, guildLocale);
    }



}
