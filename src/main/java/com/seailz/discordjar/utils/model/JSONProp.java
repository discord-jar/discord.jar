package com.seailz.discordjar.utils.model;

import org.json.JSONObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Each field in a model should be annotated with this, to specify the corresponding key in a JSON object.
 * <br>If not annotated, the field will be ignored.
 *
 * @author Seailz
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONProp {

    String value();

}
