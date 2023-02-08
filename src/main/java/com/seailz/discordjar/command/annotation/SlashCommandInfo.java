package com.seailz.discordjar.command.annotation;

import com.seailz.discordjar.command.listeners.slash.SlashCommandListener;
import com.seailz.discordjar.utils.permission.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines info about a specific {@link SlashCommandListener}.
 * <p>
 * All {@link com.seailz.discordjar.command.listeners.slash.SlashCommandListener SlashCommandListeners} <b>must</b> have this annotation.
 * None of the fields are optional, and all of them must be filled out.
 *
 * @author Seailz
 * @see SlashCommandListener
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SlashCommandInfo {
    /**
     * The name of the slash command
     */
    String name();

    /**
     * The description of the slash command
     */
    String description();

    Locale[] nameLocalizations() default {};

    Locale[] descriptionLocalizations() default {};

    Permission[] defaultMemberPermissions() default {};

    boolean canUseInDms() default true;

    boolean nsfw() default false;


}
