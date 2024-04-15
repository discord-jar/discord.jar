package com.seailz.discordjar.model.interaction;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.application.Application;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.guild.Member;
import com.seailz.discordjar.model.message.Message;
import com.seailz.discordjar.model.monetization.Entitlement;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.flag.BitwiseUtil;
import com.seailz.discordjar.utils.permission.Permission;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

/**
 * Represents the data of an interaction
 *
 * @author Seailz
 * @see com.seailz.discordjar.model.interaction.Interaction
 * @since 1.0
 */
public class Interaction implements Compilerable {

    // The id of the interaction
    private final String id;
    // The id of the application the interaction is for
    private final Application application;
    // The type of interaction
    private final InteractionType type;
    // Interaction data payload
    private final InteractionData data;
    // The guild it was sent from
    private Guild guild;
    private final String guildId;
    // The channel it was sent from
    private final Channel channel;
    // Guild member data for the invoking user, including permissions
    private Member member;
    private final JSONObject memberJson;
    // User object for the invoking user, if invoked in a DM
    private final User user;
    // A continuation token for responding to the interaction
    private final String token;
    // Read-only property, always 1
    private final int version;
    // For components, the message they were attached to
    private final Message message;
    // Bitwise set of permissions the app or bot has within the channel the interaction was sent from
    private final EnumSet<Permission> appPermissions;
    // Selected language of the invoking user
    private final String locale;
    // Guild's preferred locale, if invoked in a guild
    private final String guildLocale;
    // For monetized apps, array of Entitlements for the invoking user
    private List<Entitlement> entitlements;
    private final String raw;
    private final DiscordJar djar;
    @Deprecated
    private final String channelId;
    private final InteractionContextType context;
    private final HashMap<IntegrationType, String> authorizingIntegrationOwners;

    public Interaction(String id, Application application, InteractionType type, InteractionData data, String guild, Channel channel, JSONObject member, User user, String token, int version, Message message, String appPermissions, String locale, String guildLocale, List<Entitlement> entitlements, String raw, String channelId, InteractionContextType context, HashMap<IntegrationType, String> authorizingIntegrationOwners, DiscordJar discordJar) {
        this.id = id;
        this.application = application;
        this.type = type;
        this.data = data;
        this.guildId = guild;
        this.channel = channel;
        this.memberJson = member;
        this.user = user;
        this.token = token;
        this.version = version;
        this.message = message;
        if (appPermissions != null) {
            BitwiseUtil<Permission> bitwiseUtil = new BitwiseUtil<>();
            this.appPermissions = bitwiseUtil.get(Long.parseLong(appPermissions), Permission.class);
        } else {
            this.appPermissions = EnumSet.noneOf(Permission.class);
        }
        this.locale = locale;
        this.guildLocale = guildLocale;
        this.raw = raw;
        this.channelId = channelId;
        this.djar = discordJar;
        this.entitlements = entitlements;
        this.context = context;
        this.authorizingIntegrationOwners = authorizingIntegrationOwners;
    }

    @Deprecated
    public String channelId() {
        return channelId;
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
        if (guild == null) {
            guild = djar.getGuildById(guildId);
        }
        return guild;
    }

    public Channel channel() {
        return channel;
    }

    public Member member() {
        if (memberJson == null) return null;
        if (member == null) {
            member = Member.decompile(memberJson, djar, guildId, guild());
        }
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

    public EnumSet<Permission> appPermissions() {
        return appPermissions;
    }

    public String locale() {
        return locale;
    }

    public String guildLocale() {
        return guildLocale;
    }

    public String raw() {
        return raw;
    }

    public List<Entitlement> entitlements() {
        return entitlements;
    }
    public InteractionContextType context() {
        return context;
    }

    public HashMap<IntegrationType, String> authorizingIntegrationOwners() {
        return authorizingIntegrationOwners;
    }

    @Override
    public JSONObject compile() {
        JSONObject data = null;
        if (this.data != null) {
            Class<? extends InteractionData> dataClass = this.data.getClass();
            Method compileMethod = null;
            try {
                compileMethod = dataClass.getMethod("compile");
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

            try {
                data = (JSONObject) compileMethod.invoke(this.data);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        JSONArray entitlements = new JSONArray();
        if (this.entitlements != null) {
            for (Entitlement entitlement : this.entitlements) {
                entitlements.put(entitlement.compile());
            }
        }

        JSONObject authIntOwners = new JSONObject();
        this.authorizingIntegrationOwners.forEach((key, val) -> {
            authIntOwners.put(String.valueOf(key.getCode()), val);
        });

        return new JSONObject()
                .put("id", id)
                .put("application", application.id())
                .put("type", type.getCode())
                .put("data", data)
                .put("guild", guildId)
                .put("channel", channel.compile())
                .put("member", member.compile())
                .put("token", token)
                .put("version", version)
                .put("message", message.compile())
                .put("app_permissions", appPermissions)
                .put("locale", locale)
                .put("guild_locale", guildLocale)
                .put("channel_id", channelId)
                .put("entitlements", entitlements)
                .put("context", context.getCode())
                .put("authorizing_integration_owners", authIntOwners);
    }

    @NotNull
    public static Interaction decompile(JSONObject json, DiscordJar discordJar) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, DiscordRequest.UnhandledDiscordAPIErrorException {
        String id = json.has("id") ? json.getString("id") : null;
        Application application = json.has("application") ? Application.decompile(json.getJSONObject("application"), discordJar) : null;
        InteractionType type = json.has("type") ? InteractionType.getType(json.getInt("type")) : null;
        InteractionData data = json.has("data") ? InteractionData.decompile(type, json.getJSONObject("data"), discordJar) : null;
        String guildId = json.has("guild_id") ? json.getString("guild_id") : null;
        Channel channel = json.has("channel") ? Channel.decompile(json.getJSONObject("channel"), discordJar) : null;
        Member member = json.has("member") ? Member.decompile(json.getJSONObject("member"), discordJar, guildId, null) : null;
        User user = json.has("user") ? User.decompile(json.getJSONObject("user"), discordJar) : null;
        String token = json.has("token") ? json.getString("token") : null;
        int version = json.has("version") ? json.getInt("version") : 0;
        Message message = json.has("message") ? Message.decompile(json.getJSONObject("message"), discordJar) : null;
        String appPermissions = json.has("app_permissions") ? json.getString("app_permissions") : null;
        String locale = json.has("locale") ? json.getString("locale") : null;
        String guildLocale = json.has("guild_locale") ? json.getString("guild_locale") : null;
        String channelId = json.has("channel_id") ? json.getString("channel_id") : null;
        InteractionContextType context = json.has("context") ? InteractionContextType.fromCode(json.getInt("context")) : null;

        JSONArray entitlements = json.has("entitlements") ? json.getJSONArray("entitlements") : null;
        List<Entitlement> entitlementList = new ArrayList<>();
        if (entitlements != null) {
            for (int i = 0; i < entitlements.length(); i++) {
                entitlementList.add(Entitlement.decompile(discordJar, entitlements.getJSONObject(i)));
            }
        }

        HashMap<IntegrationType, String> authIntegrationOwners = null;

        if (json.has("authorizing_integration_owners")) {
            authIntegrationOwners = new HashMap<>();
            JSONObject authIntegrationOwnersJson = json.getJSONObject("authorizing_integration_owners");
            for (String code : authIntegrationOwnersJson.keySet()) {

                authIntegrationOwners.put(IntegrationType.fromCode(Integer.parseInt(code)), String.valueOf(authIntegrationOwnersJson.get(code)));
            }
        }

        return new Interaction(id, application, type, data, guildId, channel, json.has("member") ? json.getJSONObject("member") : null, user, token, version, message, appPermissions, locale, guildLocale, entitlementList, json.toString(), channelId, context, authIntegrationOwners, discordJar);
    }


}
