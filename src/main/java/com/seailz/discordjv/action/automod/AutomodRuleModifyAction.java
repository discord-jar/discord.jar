package com.seailz.discordjv.action.automod;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.automod.AutomodRule;
import com.seailz.discordjv.model.channel.Channel;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.role.Role;
import com.seailz.discordjv.utils.Checker;
import com.seailz.discordjv.utils.URLS;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AutomodRuleModifyAction {

    private String name;
    private final Guild guild;
    private AutomodRule.EventType eventType;
    private AutomodRule.TriggerType triggerType;
    private AutomodRule.TriggerMetadata triggerMetadata;
    private List<AutomodRule.Action> actions;
    private boolean enabled = true;
    private List<Role> exemptRoles;
    private List<Channel> exemptChannels;
    private final DiscordJv discordJv;

    public AutomodRuleModifyAction(String id, Guild g, DiscordJv discordJv) {
        this.guild = g;
        this.discordJv = discordJv;
        this.name = id;
    }

    public AutomodRuleModifyAction(String name, AutomodRule.EventType eventType, AutomodRule.TriggerType triggerType, AutomodRule.TriggerMetadata triggerMetadata, List<AutomodRule.Action> actions, boolean enabled, List<Role> exemptRoles, List<Channel> exemptChannels, Guild guild, DiscordJv discordJv) {
        this.name = name;
        this.eventType = eventType;
        this.triggerType = triggerType;
        this.triggerMetadata = triggerMetadata;
        this.actions = actions;
        this.enabled = enabled;
        this.exemptRoles = exemptRoles;
        this.exemptChannels = exemptChannels;
        this.guild = guild;
        this.discordJv = discordJv;
    }

    public AutomodRuleModifyAction(String name, AutomodRule.EventType eventType, AutomodRule.TriggerType triggerType, List<AutomodRule.Action> actions, Guild guild, DiscordJv discordJv) {
        this.name = name;
        this.eventType = eventType;
        this.triggerType = triggerType;
        this.actions = actions;
        this.discordJv = discordJv;
        this.guild = guild;
    }

    public AutomodRuleModifyAction(String name, AutomodRule.EventType eventType, AutomodRule.TriggerType triggerType, AutomodRule.TriggerMetadata metadata, List<AutomodRule.Action> actions, Guild guild, DiscordJv discordJv) {
        this.name = name;
        this.eventType = eventType;
        this.triggerType = triggerType;
        this.actions = actions;
        this.triggerMetadata = metadata;
        this.discordJv = discordJv;
        this.guild = guild;
    }

    public String name() {
        return name;
    }

    public AutomodRule.EventType eventType() {
        return eventType;
    }

    public AutomodRule.TriggerType triggerType() {
        return triggerType;
    }

    public AutomodRule.TriggerMetadata triggerMetadata() {
        return triggerMetadata;
    }

    public List<AutomodRule.Action> actions() {
        return actions;
    }

    public boolean enabled() {
        return enabled;
    }

    public List<Role> exemptRoles() {
        return exemptRoles;
    }

    public List<Channel> exemptChannels() {
        return exemptChannels;
    }

    public AutomodRuleModifyAction setName(String name) {
        this.name = name;
        return this;
    }

    public AutomodRuleModifyAction setEventType(AutomodRule.EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public AutomodRuleModifyAction setTriggerType(AutomodRule.TriggerType triggerType) {
        this.triggerType = triggerType;
        return this;
    }

    public AutomodRuleModifyAction setTriggerMetadata(AutomodRule.TriggerMetadata triggerMetadata) {
        this.triggerMetadata = triggerMetadata;
        return this;
    }

    public AutomodRuleModifyAction setActions(List<AutomodRule.Action> actions) {
        this.actions = actions;
        return this;
    }

    public AutomodRuleModifyAction setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public AutomodRuleModifyAction setExemptRoles(List<Role> exemptRoles) {
        this.exemptRoles = exemptRoles;
        return this;
    }

    public AutomodRuleModifyAction setExemptChannels(List<Channel> exemptChannels) {
        this.exemptChannels = exemptChannels;
        return this;
    }

    public AutomodRuleModifyAction addExemptChannel(Channel channel) {
        this.exemptChannels.add(channel);
        return this;
    }

    public AutomodRuleModifyAction addExemptRole(Role role) {
        this.exemptRoles.add(role);
        return this;
    }


    public CompletableFuture<AutomodRule> run() {
        CompletableFuture<AutomodRule> future = new CompletableFuture<>();
        future.completeAsync(() -> {
            JSONObject payload = new JSONObject();
            payload.put("name", name);
            payload.put("event_type", eventType.getCode());
            payload.put("trigger_type", triggerType.getCode());
            if (triggerMetadata != null) payload.put("trigger_metadata", triggerMetadata.compile());

            JSONArray actionsArray = new JSONArray();
            actions.stream().map(AutomodRule.Action::compile).forEach(actionsArray::put);

            payload.put("actions", actionsArray);
            payload.put("enabled", enabled);

            if (exemptChannels != null) {
                JSONArray exemptChannelsArray = new JSONArray();
                exemptChannels.stream().map(Channel::id).forEach(exemptChannelsArray::put);
                payload.put("exempt_channels", exemptChannelsArray);
            }

            if (exemptRoles != null) {
                JSONArray exemptRolesArray = new JSONArray();
                exemptRoles.stream().map(Role::id).forEach(exemptRolesArray::put);
                payload.put("exempt_roles", exemptRolesArray);
            }

            DiscordRequest request = new DiscordRequest(
                    payload,
                    new HashMap<>(),
                    URLS.PATCH.GUILD.AUTOMOD.UPDATE_AUTOMOD_RULE.replace(
                            "{guild.id}",
                            guild.id()
                    ),
                    discordJv,
                    URLS.PATCH.GUILD.AUTOMOD.UPDATE_AUTOMOD_RULE,
                    RequestMethod.POST
            );

            return AutomodRule.decompile(request.invoke().body(), discordJv);
        });
        Checker.notNull(name, "name");
        Checker.notNull(eventType, "eventType");
        Checker.notNull(triggerType, "triggerType");
        Checker.notNull(actions, "actions");

        if (exemptChannels != null) Checker.check(exemptChannels.size() <= 50, "exemptChannels cannot be more than 50");
        if (exemptRoles != null) Checker.check(exemptRoles.size() <= 20, "exemptRoles cannot be more than 20");

        return future;
    }

}
