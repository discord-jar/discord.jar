package com.seailz.discordjar.events;

import com.seailz.discordjar.command.listeners.CommandListener;
import com.seailz.discordjar.events.model.command.CommandPermissionUpdateEvent;
import com.seailz.discordjar.events.model.general.ReadyEvent;
import com.seailz.discordjar.events.model.guild.GuildCreateEvent;
import com.seailz.discordjar.events.model.guild.member.GuildMemberAddEvent;
import com.seailz.discordjar.events.model.guild.member.GuildMemberRemoveEvent;
import com.seailz.discordjar.events.model.guild.member.GuildMemberUpdateEvent;
import com.seailz.discordjar.events.model.interaction.button.ButtonInteractionEvent;
import com.seailz.discordjar.events.model.interaction.command.SlashCommandInteractionEvent;
import com.seailz.discordjar.events.model.interaction.modal.ModalInteractionEvent;
import com.seailz.discordjar.events.model.interaction.select.StringSelectMenuInteractionEvent;
import com.seailz.discordjar.events.model.interaction.select.entity.ChannelSelectMenuInteractionEvent;
import com.seailz.discordjar.events.model.interaction.select.entity.RoleSelectMenuInteractionEvent;
import com.seailz.discordjar.events.model.interaction.select.entity.UserSelectMenuInteractionEvent;
import com.seailz.discordjar.events.model.message.MessageCreateEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to listen for events that are fired by the Discord API.
 * This class can be extended to listen for events.
 * Once extended, you can override the methods you want to listen for.
 * Then, just register them in your main class.
 *
 * @author Seailz
 * @see com.seailz.discordjar.events.EventDispatcher
 * @see com.seailz.discordjar.events.annotation.EventMethod
 * @since 1.0
 */
public abstract class DiscordListener {
    // General Events
    public void onReady(@NotNull ReadyEvent event) {
    }

    // Message Events
    public void onMessageReceived(@NotNull MessageCreateEvent event) {
    }

    // Guild Events
    public void onGuildCreated(@NotNull GuildCreateEvent event) {
    }

    // Guild Member Events
    public void onGuildMemberAdd(@NotNull GuildMemberAddEvent event) {
    }

    public void onGuildMemberUpdate(@NotNull GuildMemberUpdateEvent event) {
    }

    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
    }

    // Command Events
    public void onCommandPermissionUpdate(@NotNull CommandPermissionUpdateEvent event) {
    }

    /**
     * <b>It is not recommend to use this method.</b> Instead, where possible, you should use {@link com.seailz.discordjar.command.listeners.slash.SlashCommandListener SLashCommandListener} with a
     * {@link com.seailz.discordjar.command.annotation.SlashCommandInfo SlashCommandInfo} annotation and then register it using {@link com.seailz.discordjar.DiscordJar#registerCommands(CommandListener...)}
     * @param event The event
     */
    @Deprecated
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    }

    // Message Component Events
    // Select Menu Events
    public void onStringSelectMenuInteraction(@NotNull StringSelectMenuInteractionEvent event) {
    }

    public void onRoleSelectMenuInteraction(@NotNull RoleSelectMenuInteractionEvent event) {
    }

    public void onUserSelectMenuInteraction(@NotNull UserSelectMenuInteractionEvent event) {
    }

    public void onChannelSelectMenuInteraction(@NotNull ChannelSelectMenuInteractionEvent event) {
    }

    // Button Events
    public void onButtonClickInteractionEvent(@NotNull ButtonInteractionEvent event) {
    }

    // Modal Events
    public void onModalInteractionEvent(@NotNull ModalInteractionEvent event) {
    }

}
