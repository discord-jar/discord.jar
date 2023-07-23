package com.seailz.discordjar.events.model.automod;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.guild.GuildEvent;
import com.seailz.discordjar.model.automod.AutomodRule;
import com.seailz.discordjar.model.channel.GuildChannel;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.json.SJSONObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AutoModExecutionEvent extends GuildEvent {
    public AutoModExecutionEvent(@NotNull DiscordJar bot, long sequence, @NotNull SJSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * The action that was taken on the message.
     */
    @NotNull
    public AutomodRule.Action getAction() {
        return AutomodRule.Action.decompile(
                getJson().getJSONObject("d").getJSONObject("action")
        );
    }

    /**
     * The rule that was triggered.
     */
    @Nullable
    public AutomodRule getRule() {
        if (getGuild() == null) return null;
        return getGuild().getAutomodRuleById(
                getJson().getJSONObject("d").getString("rule_id")
        );
    }

    @NotNull
    public AutomodRule.TriggerType getTriggerType() {
        return AutomodRule.TriggerType.fromCode(
                getJson().getJSONObject("d").getInt("trigger_type")
        );
    }

    @Nullable
    public GuildChannel getChannel() {
        return getBot().getChannelById(getJson().getJSONObject("d").getString("channel_id")).asGuildChannel();
    }

    @Nullable
    public User getUser() {
        return getBot().getUserById(getJson().getJSONObject("d").getString("user_id"));
    }

    /**
     * Returns the message that triggered the AutoMod rule.
     * If the message was blocked, this will return null.
     */
    @Nullable
    public String getMessageId() {
        if (!getJson().getJSONObject("d").has("message_id")) return null;
        return getJson().getJSONObject("d").getString("message_id");
    }

    /**
     * Returns the message that triggered the AutoMod rule.
     * If you don't have the `MESSAGE_CONTENT` intent enabled, this will return null or an empty string.
     */
    @Nullable
    public String getContent() {
        if (!getJson().getJSONObject("d").has("content")) return null;
        return getJson().getJSONObject("d").getString("content");
    }

    /**
     * The keyword or phrase that triggered the rule.
     */
    @NotNull
    public String match() {
        return getJson().getJSONObject("d")
                .getString("matched_keyword");
    }

    /**
     * Returns the substring of the message that triggered the rule.
     * Again, this will return null (or an empty string) if you don't have the `MESSAGE_CONTENT` intent enabled.
     */
    @Nullable
    public String matchedSubstring() {
        if (!getJson().getJSONObject("d").has("matched_content")) return null;
        return getJson().getJSONObject("d").getString("matched_content");
    }

    @Nullable
    public String alertSystemMessageId() {
        if (!getJson().getJSONObject("d").has("alert_system_message_id")) return null;
        return getJson().getJSONObject("d").getString("alert_system_message_id");
    }
}
