package com.seailz.discordjv.command.annotation;

import com.seailz.discordjv.command.listeners.CommandListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines info about a specific {@link CommandListener}.
 * <p>
 * All {@link CommandListener CommandListeners} <b>must</b> have this annotation.
 * None of the fields are optional, and all of them must be filled out.
 *
 * @author Seailz
 * @see CommandListener
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {

    String name();

    String description();

}
