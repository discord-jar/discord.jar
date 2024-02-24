package com.seailz.discordjar.events.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark methods that should be called when an event is fired.
 * <br>If a listener method isn't marked with this annotation, it will not be called.
 *
 * @author Seailz
 * @see com.seailz.discordjar.events.DiscordListener
 * @see com.seailz.discordjar.events.EventDispatcher
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventMethod {


    /**
     * If this field is set, then the event will only run if the customid of the event == the value of this field.
     * <br>Example:
     * <br>
     * <code>
     *    <br>@EventMethod(requireCustomId="my_custom_id)
     *     <br>public void onModalInteractionEvent(@NotNull ModalInteractionEvent event) {
     *     <br>     // This will only run if the custom id of the event is "my_custom_id"
     *     <br>     event.reply("The custom ID is my_custom_id!").run();
     *     <br>}
     * </code>
     * <p></p>
     * It's also a regex, so you can do things like this: <code>@RequireCustomId("my_custom_id|my_custom_id2")</code> which will match both "my_custom_id" and "my_custom_id2" ,
     * or <code>@RequireCustomId("my_custom_id-.*")</code> which will match any custom id that starts with "my_custom_id-".
     * <p></p>
     * @author Seailz
     * @since 1.0
     */
    String requireCustomId() default "";

}
