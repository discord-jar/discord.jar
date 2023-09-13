package com.seailz.discordjar.utils.model;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.guild.GuildFeature;
import com.seailz.discordjar.utils.flag.BitwiseUtil;
import com.seailz.discordjar.utils.flag.Bitwiseable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Given any JSON object & its corresponding schema/class, ModelDecoder will decode the JSON object into a Java object.
 * @author Seailz
 */
public class ModelDecoder {

    public static Model decodeObject(JSONObject json, Class<? extends Model> model, DiscordJar discordJar) {
        long start = System.currentTimeMillis();
        boolean debug = discordJar.isDebug();

        Constructor<? extends Model> constructor = null;
        try {
            constructor = model.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            Logger.getLogger("DiscordJar").severe("[Decoder] Could not find default constructor for " + model.getName() + " - please make an issue report on the GitHub page.");
            throw new RuntimeException(e);
        }
        constructor.setAccessible(true);
        Model inst = null;
        try {
            inst = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        // We want to make sure we set the DiscordJar instance for any fields that need it.
        for (Field field : model.getDeclaredFields()) {
            if (field.isAnnotationPresent(DiscordJarProp.class)) {
                field.setAccessible(true);
                try {
                    field.set(inst, discordJar);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        for (Field field : model.getDeclaredFields()) {
            if (field.isAnnotationPresent(JSONProp.class)) {
                JSONProp prop = field.getAnnotation(JSONProp.class);
                field.setAccessible(true);

                if (inst.customDecoders().containsKey(prop.value())) {
                    try {
                        field.set(inst, inst.customDecoders().get(prop.value()).apply(json));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }

                // If field is not a JSON compatible type, we want to try to decode it.
                // If it's an array, we want to decode each element.

                if (field.getType().isEnum()) {
                    if (!json.has(prop.value())) continue;
                    Enum<?> decodedEnum = null;
                    try {
                        decodedEnum = decodeEnum((Class<? extends Enum>) field.getType(), get(json, prop.value(), field.getType()));
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    if (decodedEnum == null) continue;
                    try {
                        field.set(inst, decodedEnum);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (field.getType() != String.class && field.getType() != int.class && field.getType() != long.class && field.getType() != boolean.class && field.getType() != double.class && field.getType() != float.class && field.getType() != JSONObject.class) {
                    if (field.getType().isArray() || field.getType() == List.class || field.getType() == ArrayList.class || field.getType() == EnumSet.class) {
                        if (!json.has(prop.value())) continue;

                        if (field.getType() == EnumSet.class) {
                            Type genericType = field.getGenericType();
                            ParameterizedType paramType = (ParameterizedType) genericType;
                            Type[] argTypes = paramType.getActualTypeArguments();
                            Class<? extends Enum> enumClass = (Class<? extends Enum>) argTypes[0];

                            if (enumClass.equals(GuildFeature.class)) {
                                EnumSet<GuildFeature> set = null;
                                String[] arr = json.getJSONArray(prop.value()).toList().toArray(
                                        new String[0]
                                );

                                set = GuildFeature.getGuildFeatures(arr);
                                try {
                                    field.set(inst, set);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                                continue;
                            }

                            // If enumClass extends Bitwiseable, we want to decode it as a Bitwiseable.
                            if (Bitwiseable.class.isAssignableFrom(enumClass)) {
                                BitwiseUtil bitwiseUtil = new BitwiseUtil();
                                EnumSet<?> set = bitwiseUtil.get(json.getInt(prop.value()), (Class<? extends Enum>) enumClass);
                                try {
                                    field.set(inst, set);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                                continue;
                            }

                            EnumSet set = EnumSet.noneOf(enumClass);
                            JSONArray arr = json.getJSONArray(prop.value());
                            for (int i = 0; i < arr.length(); i++) {
                                Enum<?> decodedEnum = null;
                                try {
                                    decodedEnum = decodeEnum(enumClass, arr.get(i));
                                } catch (NoSuchFieldException | IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                                if (decodedEnum == null) continue;
                                set.add(decodedEnum);
                            }

                            try {
                                field.set(inst, set);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                            continue;
                        }

                        if (!(get(json, prop.value(), field.getType()) instanceof JSONArray)) continue;

                        List<?> ls = null;
                        try {
                            ls = decodeArray(json.getJSONArray(prop.value()), field, discordJar);
                        } catch (NoSuchFieldException | InvocationTargetException | InstantiationException |
                                 IllegalAccessException | NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            field.set(inst, ls);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        if (!json.has(prop.value())) continue;
                        if (!(get(json, prop.value(), field.getType()) instanceof JSONObject)) continue;
                        try {
                            field.set(inst, decodeObject(json.getJSONObject(prop.value()), (Class<? extends Model>) field.getType(), discordJar));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    continue;
                }

                if (json.has(prop.value())) {
                    try {
                        field.set(inst, get(json, prop.value(), field.getType()));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        if (debug) Logger.getLogger("ObjDecoder")
                .info("[ModelDecoder] Decoded " + model.getSimpleName() + " in " + (System.currentTimeMillis() - start) + "ms");

        return inst;
    }

    private static boolean fieldExists(Class<?> clazz, String fieldName) {
        try {
            clazz.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
    private static Enum<?> decodeEnum(Class<? extends Enum> enumClass, Object value) throws NoSuchFieldException, IllegalAccessException {
        if (value instanceof String) {
            return Enum.valueOf(enumClass, (String) value);
        } else if (value instanceof Integer) {
            // If it's an integer, we'll want to check if the enum has a "value" or "code" field.
            // If it does, we'll want to use that to decode the enum, and if not, we'll want to use the ordinal.
            String valueFieldName;
            if (fieldExists(enumClass, "value")) valueFieldName = "value";
            else if (fieldExists(enumClass, "code")) valueFieldName = "code";
            else valueFieldName = null;

            if (valueFieldName == null) {
                try {
                    return enumClass.getEnumConstants()[(int) value];
                } catch (IndexOutOfBoundsException err) {
                    Logger.getLogger("ModelDecoder").warning("Could not decode enum " + enumClass.getName() + " from " + value + " - index out of bounds");
                    return null;
                }
            } else {
                for (Object e : enumClass.getEnumConstants()) {
                    Field field = e.getClass().getDeclaredField(valueFieldName);
                    field.setAccessible(true);
                    if (field.getInt(e) == (int) value) {
                        return (Enum<?>) e;
                    }
                }
            }
        } else {
            Logger.getLogger("ModelDecoder").warning("Could not decode enum " + enumClass.getName() + " from " + value);
            return null;
        }
        return null;
    }
    private static List<?> decodeArray(JSONArray arr, Field field, DiscordJar djar) throws NoSuchFieldException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Object[] arr1 = (Object[]) arr.toList().toArray();
        for (int i = 0; i < arr1.length; i++) {
            Object o = arr1[i];
            if (o instanceof JSONObject) {
                arr1[i] = decodeObject((JSONObject) o, (Class<? extends Model>) field.getType().getComponentType(), djar);
            } else if (o instanceof JSONArray) {
                arr1[i] = decodeArray((JSONArray) o, field, djar);
            }
        }
        return Arrays.asList(arr1);
    }
    private static Object get(JSONObject in, String key, Class<?> expectedType) {
        Object nullValue = null;

        // If expectedType is a primitive, set nullValue to the default value for that primitive.
        if (expectedType == boolean.class) {
            nullValue = false;
        } else if (expectedType == byte.class
                || expectedType == short.class
                || expectedType == int.class
                || expectedType == long.class
                || expectedType == float.class
                || expectedType == double.class) {
            nullValue = 0;
        } else if (expectedType == char.class) {
            nullValue = '\u0000';
        }

        if (!in.has(key)) return nullValue;
        if (in.get(key).equals(JSONObject.NULL)) return nullValue;
        return in.get(key);
    }
}
