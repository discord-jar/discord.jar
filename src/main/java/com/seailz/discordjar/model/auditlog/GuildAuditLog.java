package com.seailz.discordjar.model.auditlog;

import com.seailz.discordjar.command.Command;
import com.seailz.discordjar.model.automod.AutomodRule;
import com.seailz.discordjar.model.channel.thread.Thread;
import com.seailz.discordjar.model.guild.GuildIntegration;
import com.seailz.discordjar.model.guild.scheduledevents.ScheduledEvent;
import com.seailz.discordjar.model.user.User;

import java.util.List;

public record GuildAuditLog(
        List<Command> referencedApplicationCommands,
        List<AuditLogEntry> entries,
        List<AutomodRule> referencedAutomodRules,
        List<ScheduledEvent> referencedScheduledEvents,
        List<GuildIntegration> referencedIntegrations,
        List<Thread> referencedThreads,
        List<User> referencedUsers
        // TODO: Webhooks - https://github.com/discord-jar/discord.jar/pull/147
) {
    public record AuditLogEntry() {}
}
