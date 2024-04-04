package com.seailz.discordjar.command.annotation;

import com.seailz.discordjar.model.interaction.InteractionContextType;
import com.seailz.discordjar.utils.permission.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines info about a specific context command.
 *
 * @author Seailz
 * @see    com.seailz.discordjar.command.listeners.MessageContextCommandListener
 * @see    com.seailz.discordjar.command.listeners.UserContextCommandListener
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContextCommandInfo {
    /**
     * The name of the context command
     */
    String value();

    Locale[] nameLocalizations() default {};

    Locale[] descriptionLocalizations() default {};

    Permission[] defaultMemberPermissions() default {};

    /**
     * @deprecated Use {@link #contexts()} instead.
     */
    @Deprecated
    boolean canUseInDms() default true;

    boolean nsfw() default false;

    /**
     * Contexts where this command can be used. Default is all contexts.
     */
    InteractionContextType[] contexts() default {InteractionContextType.BOT_DM, InteractionContextType.GUILD, InteractionContextType.PRIVATE_CHANNEL};
}
