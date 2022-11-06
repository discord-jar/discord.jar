package com.seailz.javadiscordwrapper.events.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark methods that should be called when an event is fired.
 * If a listener method isn't marked with this annotation, it will not be called.
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.events.DiscordListener
 * @see    com.seailz.javadiscordwrapper.events.EventDispatcher
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventMethod {
}
