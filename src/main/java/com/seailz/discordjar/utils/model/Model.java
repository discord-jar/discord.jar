package com.seailz.discordjar.utils.model;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Every model should implement this interface, as it's used for encoding & decoding to JSON.
 * @author Seailz
 */
public interface Model {

    default JSONObject compile() {
        JSONObject json = new JSONObject();
        for (Field field : getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(JSONProp.class)) {
                JSONProp prop = field.getAnnotation(JSONProp.class);
                try {
                    json.put(prop.value(), field.get(this));
                } catch (IllegalAccessException e) {
                    field.setAccessible(true);
                    try {
                        json.put(prop.value(), field.get(this));
                    } catch (IllegalAccessException illegalAccessException) {
                        illegalAccessException.printStackTrace();
                    }
                }
            }
        }
        return json;
    }

    /**
     * You can specify custom decoders for your model here.<br>
     * For each of your fields, if there's a present decoder, it will be used instead of the default decoding flow.
     * @return A map of <b><u>JSON key names</u></b> to decoders.
     */
    default HashMap<String, Function<JSONObject, ?>> customDecoders() {
        return new HashMap<>();
    }

}
