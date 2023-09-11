package com.seailz.discordjar.utils.model;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a prop that should be set to the current discord.jar instance.
 */
@Target(java.lang.annotation.ElementType.FIELD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface DiscordJarProp {
}
