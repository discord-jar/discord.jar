package com.seailz.discordjar.utils.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If you annotate a method with this and that method is a interaction event listener method,
 * <br>then the event will only run if the customid of the event == the value of this annotation.
 * <br>Example:
 * <br>
 * <code>
 *    <br>@EventMethod
 *    <br>@RequireCustomId("my_custom_id")
 *     <br>public void onModalInteractionEvent(@NotNull ModalInteractionEvent event) {
 *     <br>     // This will only run if the custom id of the event is "my_custom_id"
 *     <br>     event.reply("The custom ID is my_custom_id!").run();
 *     <br>}
 * </code>
 *
 * @author Seailz
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface RequireCustomId {
    String value();
}
