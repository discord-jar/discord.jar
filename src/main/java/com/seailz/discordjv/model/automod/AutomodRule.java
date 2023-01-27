package com.seailz.discordjv.model.automod;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.model.channel.Channel;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.role.Role;
import com.seailz.discordjv.model.user.User;
import com.seailz.discordjv.utils.Checker;
import com.seailz.discordjv.utils.Snowflake;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a rule in the automod system
 *
 * @param id              the id of this rule
 * @param guild           the {@link Guild guild} which this rule belongs to
 * @param name            the rule name
 * @param creator         the {@link User user} which first created this rule
 * @param event           the rule {@link EventType event type}
 * @param trigger         the rule {@link TriggerType trigger type}
 * @param triggerMetadata the rule {@link TriggerMetadata trigger metadata}
 * @param actions         {@link List list} of {@link Action actions} to be executed when the rule is triggered
 * @param enabled         whether the rule is enabled
 * @param exemptRoles     the {@link Role roles} that should not be affected by the rule (Maximum of 20)
 * @param exemptChannels  the {@link Channel channels} that should not be affected by the rule (Maximum of 50)
 */
public record AutomodRule(
        String id,
        Guild guild,
        String name,
        User creator,
        EventType event,
        TriggerType trigger,
        TriggerMetadata triggerMetadata,
        List<Action> actions,
        boolean enabled,
        List<Role> exemptRoles,
        List<Channel> exemptChannels
) implements Compilerable, Snowflake {

    @NotNull
    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();

        obj.put("id", id);
        obj.put("guild_id", guild.id());
        obj.put("name", name);
        obj.put("creator_id", creator.id());
        obj.put("event", event.code);
        obj.put("trigger", trigger.code);
        obj.put("trigger_metadata", triggerMetadata.compile());

        JSONArray actionsArray = new JSONArray();
        for (Action action : actions) {
            actionsArray.put(action.compile());
        }

        obj.put("actions", actionsArray);
        obj.put("enabled", enabled);

        JSONArray exemptRolesArray = new JSONArray();
        Checker.check(exemptChannels.size() < 20, "Exempt roles cannot be more than 20");
        for (Role role : exemptRoles) {
            exemptRolesArray.put(role.id());
        }

        obj.put("exempt_roles", exemptRolesArray);

        JSONArray exemptChannelsArray = new JSONArray();
        Checker.check(exemptChannels.size() < 50, "Exempt channels cannot be more than 50");
        for (Channel channel : exemptChannels) {
            exemptChannelsArray.put(channel.id());
        }

        obj.put("exempt_channels", exemptChannelsArray);
        return obj;
    }

    @NotNull
    @Contract("_, _ -> new")
    public static AutomodRule decompile(@NotNull JSONObject obj, @NotNull DiscordJv discordJv) {
        String id;
        Guild guild;
        String name;
        User creator;
        EventType event;
        TriggerType trigger;
        TriggerMetadata triggerMetadata;
        List<Action> actions;
        boolean enabled;
        List<Role> exemptRoles;
        List<Channel> exemptChannels;

        id = obj.getString("id");
        guild = discordJv.getGuildById(obj.getString("guild_id"));
        name = obj.getString("name");
        creator = discordJv.getUserById(obj.getString("creator_id"));
        event = EventType.fromCode(obj.getInt("event_type"));
        trigger = TriggerType.fromCode(obj.getInt("trigger_type"));
        triggerMetadata = TriggerMetadata.decompile(obj.getJSONObject("trigger_metadata"));

        JSONArray actionsArray = obj.getJSONArray("actions");
        actions = new ArrayList<>();
        for (int i = 0; i < actionsArray.length(); i++) {
            actions.add(Action.decompile(actionsArray.getJSONObject(i)));
        }

        enabled = obj.getBoolean("enabled");

        JSONArray exemptRolesArray = obj.getJSONArray("exempt_roles");
        exemptRoles = new ArrayList<>();
        for (int i = 0; i < exemptRolesArray.length(); i++) {
            int finalI = i;
            guild.roles().forEach(role -> {
                if (role.id().equals(exemptRolesArray.getString(finalI))) {
                    exemptRoles.add(role);
                }
            });
        }

        JSONArray exemptChannelsArray = obj.getJSONArray("exempt_channels");
        exemptChannels = new ArrayList<>();
        for (int i = 0; i < exemptChannelsArray.length(); i++) {
            exemptChannels.add(discordJv.getChannelById(exemptChannelsArray.getString(i)));
        }

        return new AutomodRule(id, guild, name, creator, event, trigger, triggerMetadata, actions, enabled, exemptRoles, exemptChannels);
    }

    @NotNull
    public static List<AutomodRule> decompileList(@NotNull JSONArray array, @NotNull DiscordJv discordJv) {
        List<AutomodRule> rules = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            rules.add(decompile(array.getJSONObject(i), discordJv));
        }
        return rules;
    }

    /**
     * Indicates in what event context a rule should be checked.
     */
    public enum EventType {

        MESSAGE_SEND(1),
        UNKNOWN(-1),

        ;

        private final int code;

        EventType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static EventType fromCode(int code) {
            for (EventType type : values()) {
                if (type.code == code) {
                    return type;
                }
            }
            return UNKNOWN;
        }
    }

    /**
     * Characterizes the type of content which can trigger the rule.
     */
    public enum TriggerType {

        KEYWORD(1),
        SPAM(3),
        KEYWORD_PRESET(4),
        MENTION_SPAM(5),
        UNKNOWN(-1),

        ;

        private final int code;

        TriggerType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static TriggerType fromCode(int code) {
            for (TriggerType type : values()) {
                if (type.code == code) {
                    return type;
                }
            }
            return UNKNOWN;
        }

    }

    /**
     * Additional data used to determine whether a rule should be triggered. Different fields are relevant based on the value of trigger_type.
     */
    public record TriggerMetadata(
            List<String> keywords,
            List<String> regexes,
            List<KeywordPresetType> presets,
            List<String> allowList,
            int mentionTotalLimit
    ) implements Compilerable {
        @NotNull
        @Override
        public JSONObject compile() {
            JSONObject obj = new JSONObject();

            if (keywords != null) {
                Checker.check(keywords.size() < 1000, "keywords list must be less than 1000");
                JSONArray array = new JSONArray();
                for (String keyword : keywords) {
                    array.put(keyword);
                }
            }

            if (regexes != null) {
                Checker.check(regexes.size() < 10, "regexes list must be less than 10");
                JSONArray array = new JSONArray();
                for (String regex : regexes) {
                    array.put(regex);
                }
            }

            if (presets != null) {
                JSONArray array = new JSONArray();
                for (KeywordPresetType preset : presets) {
                    array.put(preset.getCode());
                }
            }

            if (allowList != null) {
                JSONArray array = new JSONArray();
                for (String allow : allowList) {
                    array.put(allow);
                }
            }

            if (mentionTotalLimit != 0) {
                Checker.check(mentionTotalLimit < 50, "mentionTotalLimit must be less than 50");
                obj.put("mention_total_limit", mentionTotalLimit);
            }
            return obj;
        }

        @Contract("_ -> new")
        @NotNull
        public static TriggerMetadata decompile(@NotNull JSONObject obj) {
            List<String> keywords = new ArrayList<>();
            List<String> regexes = new ArrayList<>();
            List<KeywordPresetType> presets = new ArrayList<>();
            List<String> allowList = new ArrayList<>();
            int mentionTotalLimit = -1;

            if (obj.has("keywords")) {
                JSONArray array = obj.getJSONArray("keywords");
                array.toList().forEach(o -> keywords.add((String) o));
            }

            if (obj.has("regexes")) {
                JSONArray array = obj.getJSONArray("regexes");
                array.toList().forEach(o -> regexes.add((String) o));
            }

            if (obj.has("presets")) {
                JSONArray array = obj.getJSONArray("presets");
                array.toList().forEach(o -> presets.add(KeywordPresetType.fromCode((int) o)));
            }

            if (obj.has("allow_list")) {
                JSONArray array = obj.getJSONArray("allow_list");
                array.toList().forEach(o -> allowList.add((String) o));
            }

            if (obj.has("mention_total_limit")) {
                mentionTotalLimit = obj.getInt("mention_total_limit");
            }

            return new TriggerMetadata(keywords, regexes, presets, allowList, mentionTotalLimit);
        }

        public enum KeywordPresetType {

            PROFANITY(1),
            SEXUAL_CONTENT(2),
            SLURS(3),
            UNKNOWN(-1),

            ;

            private final int code;

            KeywordPresetType(int code) {
                this.code = code;
            }

            public int getCode() {
                return code;
            }

            public static KeywordPresetType fromCode(int code) {
                for (KeywordPresetType type : values()) {
                    if (type.code == code) {
                        return type;
                    }
                }
                return UNKNOWN;
            }

        }
    }

    /**
     * An action which will execute whenever a rule is triggered.
     */
    public record Action(
            ActionType type,
            ActionMetadata metadata
    ) implements Compilerable {
        @NotNull
        @Override
        public JSONObject compile() {
            JSONObject obj = new JSONObject();
            obj.put("type", type.getCode());
            obj.put("metadata", metadata.compile());
            return obj;
        }

        @NotNull
        public static Action decompile(@NotNull JSONObject obj) {
            ActionType type = ActionType.fromCode(obj.getInt("type"));
            ActionMetadata metadata = ActionMetadata.decompile(obj.getJSONObject("metadata"));
            return new Action(type, metadata);
        }

        public enum ActionType {

            BLOCK_MESSAGE(1),
            SEND_ALERT_MESSAGE(2),
            TIMEOUT(3),
            UNKNOWN(-1),

            ;

            private final int code;

            ActionType(int code) {
                this.code = code;
            }

            public int getCode() {
                return code;
            }

            public static ActionType fromCode(int code) {
                for (ActionType type : values()) {
                    if (type.code == code) {
                        return type;
                    }
                }
                return UNKNOWN;
            }
        }

        /**
         * Additional data used when an action is executed. Different fields are relevant based on the value of action type.
         */
        public interface ActionMetadata extends Compilerable {

            @Nullable
            @Unmodifiable
            @Contract(pure = true, value = "_ -> new")
            static ActionMetadata decompile(@NotNull JSONObject obj) {
                if (obj.has("channel_id"))
                    return SendAlertActionMetadata.decompile(obj);
                else if (obj.has("duration_seconds"))
                    return TimeoutActionMetadata.decompile(obj);
                return null;
            }
        }

        public record SendAlertActionMetadata(
                String channelId
        ) implements ActionMetadata {
            @Override
            public @NotNull JSONObject compile() {
                JSONObject obj = new JSONObject();
                Checker.isSnowflake(channelId, "channelId must be a snowflake");
                obj.put("channel_id", channelId);
                return obj;
            }

            @NotNull
            @Contract("_ -> new")
            public static SendAlertActionMetadata decompile(@NotNull JSONObject obj) {
                String channelId = obj.getString("channel_id");
                return new SendAlertActionMetadata(channelId);
            }
        }

        public record TimeoutActionMetadata(
                int duration
        ) implements ActionMetadata {
            @Override
            public @NotNull JSONObject compile() {
                JSONObject obj = new JSONObject();
                obj.put("duration_seconds", duration);
                Checker.check(duration < 2419200, "duration must be less than 2419200 (4 weeks)");
                return obj;
            }

            @NotNull
            @Contract("_ -> new")
            public static TimeoutActionMetadata decompile(@NotNull JSONObject obj) {
                int duration = obj.getInt("duration_seconds");
                return new TimeoutActionMetadata(duration);
            }
        }
    }

}
