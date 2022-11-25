package com.seailz.discordjv.events.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark methods that should be called when an event is fired.
 * If a listener method isn't marked with this annotation, it will not be called.
 *
 * @author Seailz
 * @see com.seailz.discordjv.events.DiscordListener
 * @see com.seailz.discordjv.events.EventDispatcher
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventMethod {
}
