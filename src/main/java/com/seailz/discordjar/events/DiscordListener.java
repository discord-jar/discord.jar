package com.seailz.discordjar.events;

import com.seailz.discordjar.command.listeners.CommandListener;
import com.seailz.discordjar.events.model.Event;
import com.seailz.discordjar.events.model.automod.AutoModExecutionEvent;
import com.seailz.discordjar.events.model.automod.rule.AutoModRuleCreateEvent;
import com.seailz.discordjar.events.model.automod.rule.AutoModRuleUpdateEvent;
import com.seailz.discordjar.events.model.channel.ChannelPinsUpdateEvent;
import com.seailz.discordjar.events.model.channel.edit.ChannelCreateEvent;
import com.seailz.discordjar.events.model.channel.edit.ChannelUpdateEvent;
import com.seailz.discordjar.events.model.command.CommandPermissionUpdateEvent;
import com.seailz.discordjar.events.model.gateway.GatewayResumedEvent;
import com.seailz.discordjar.events.model.general.ReadyEvent;
import com.seailz.discordjar.events.model.guild.GuildCreateEvent;
import com.seailz.discordjar.events.model.guild.GuildDeleteEvent;
import com.seailz.discordjar.events.model.guild.GuildUpdateEvent;
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
import com.seailz.discordjar.events.model.message.MessageDeleteEvent;
import com.seailz.discordjar.events.model.message.MessageUpdateEvent;
import com.seailz.discordjar.events.model.message.TypingStartEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to listen for events that are fired by the Discord API.
 * This class can be extended to listen for events.
 * Once extended, you can override the methods you want to listen for.
 * Then, just register them in your main class.
 *
 * <br><br>To implement a listener for a method that is not present in this class, such as {@link com.seailz.discordjar.events.model.interaction.InteractionEvent InteractionEvent},
 * you can use create your own method, and it should work provided you still use the {@link com.seailz.discordjar.events.annotation.EventMethod EventMethod} annotation.
 * Example:
 * <code>
 *     <br>public class MyListener extends DiscordListener {
 *     <br>    @EventMethod
 *     <br>    public void onInteractionEvent(@NotNull InteractionEvent event) {
 *     <br>        // Do something
 *     <br>    }
 *     <br>}
 *     <br>
 * </code>
 *
 * @author Seailz
 * @see com.seailz.discordjar.events.EventDispatcher
 * @see com.seailz.discordjar.events.annotation.EventMethod
 * @since 1.0
 */
public abstract class DiscordListener {
    // General Events

    /**
     * Any event that is fired by the Discord API will be sent to this method.
     */
    public void onEvent(@NotNull Event event) {
    }

    public void onReady(@NotNull ReadyEvent event) {
    }

    public void onGatewayResume(@NotNull GatewayResumedEvent event) {
    }

    // Message Events
    public void onMessageReceived(@NotNull MessageCreateEvent event) {
    }

    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
    }

    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
    }

    public void onTypingStart(@NotNull TypingStartEvent event) {
    }

    // Guild Events
    public void onGuildCreate(@NotNull GuildCreateEvent event) {
    }

    public void onGuildUpdate(@NotNull GuildUpdateEvent event) {
    }

    public void onGuildDelete(@NotNull GuildDeleteEvent event) {
    }

    // Guild Member Events
    public void onGuildMemberAdd(@NotNull GuildMemberAddEvent event) {
    }

    public void onGuildMemberUpdate(@NotNull GuildMemberUpdateEvent event) {
    }

    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
    }

    // Channel Events
    public void onChannelCreate(@NotNull ChannelCreateEvent event) {
    }

    public void onChannelUpdate(@NotNull ChannelUpdateEvent event) {
    }

     public void onChannelDelete(@NotNull ChannelCreateEvent event) {
     }

     public void onChannelPinsUpdate(@NotNull ChannelPinsUpdateEvent event) {
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


    // Automod
    public void onAutoModRuleCreate(@NotNull AutoModRuleCreateEvent event) {
    }

    public void onAutoModRuleUpdate(@NotNull AutoModRuleUpdateEvent event) {
    }

    public void onAutoModRuleDelete(@NotNull AutoModRuleUpdateEvent event) {
    }

    public void onAutoModActionExecution(@NotNull AutoModExecutionEvent event) {
    }

}
