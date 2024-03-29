package com.seailz.discordjar.command;

import com.seailz.discordjar.command.annotation.Locale;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.flag.BitwiseUtil;
import com.seailz.discordjar.utils.permission.Permission;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

/**
 * Represents an application command.
 *
 * @param name        The name of the app command
 * @param type        The type of the app command. See {@link CommandType}
 * @param description The description of the app command.
 * @param options     Any options the app command will have.
 * @author itstotallyjan
 */
public record Command(
        String name,
        CommandType type,
        String description,
        List<CommandOption> options,
        Locale[] nameLocalizations,
        Locale[] descriptionLocalizations,
        Permission[] defaultMemberPermissions,
        boolean canUseInDms,
        boolean nsfw
) implements Compilerable {
    @Override
    public JSONObject compile() {
        JSONArray optionsJson = new JSONArray();
        options.forEach((option) -> optionsJson.put(option.compile()));

        JSONObject nameLocales = new JSONObject();
        HashMap<String, String> nameLocalesMap = new HashMap<>();
        for (Locale locale : nameLocalizations) {
            nameLocalesMap.put(locale.locale(), locale.value());
        }
        for (String locale : nameLocalesMap.keySet()) {
            nameLocales.put(locale, nameLocalesMap.get(locale));
        }

        JSONObject descriptionLocales = new JSONObject();
        HashMap<String, String> descriptionLocalesMap = new HashMap<>();
        for (Locale locale : descriptionLocalizations) {
            descriptionLocalesMap.put(locale.locale(), locale.value());
        }
        for (String locale : descriptionLocalesMap.keySet()) {
            descriptionLocales.put(locale, descriptionLocalesMap.get(locale));
        }

        int permissions = -1;
        if (defaultMemberPermissions.length >= 1) permissions = 0;
        for (Permission permission : defaultMemberPermissions) {
            permissions |= (1 << permission.code());
        }

        JSONObject obj = new JSONObject()
                .put("name", name)
                .put("type", type.getCode())
                .put("description", description)
                .put("options", optionsJson);

        if (nameLocales.length() > 0) obj.put("name_localizations", nameLocales);

        if (descriptionLocales.length() > 0) obj.put("description_localizations", descriptionLocales);
        if (permissions != -1) obj.put("default_member_permissions", permissions);
        obj.put("dm_permission", canUseInDms);
        if (nsfw) obj.put("nsfw", true);

        return obj;
    }

    public static Command decompile(JSONObject obj) {
        String name = obj.has("name") ? obj.getString("name") : null;
        CommandType type = obj.has("type") ? CommandType.fromCode(obj.getInt("type")) : CommandType.UNKNOWN;
        String description = obj.has("description") ? obj.getString("description") : null;
        List<CommandOption> options = new ArrayList<>();
        HashMap<String, String> nameLocales = new HashMap<>();
        HashMap<String, String> descriptionLocales = new HashMap<>();
        Permission[] defaultMemberPermissions = new Permission[0];
        boolean canUseInDms = true;
        boolean nsfw = false;

        if (obj.has("name_localizations") && !obj.isNull("name_localizations")) {
            JSONObject nameLocalesJson = obj.getJSONObject("name_localizations");
            for (String locale : nameLocalesJson.keySet()) {
                nameLocales.put(locale, nameLocalesJson.getString(locale));
            }
        }

        if (obj.has("description_localizations") && !obj.isNull("description_localizations")) {
            JSONObject descriptionLocalesJson = obj.getJSONObject("description_localizations");
            for (String locale : descriptionLocalesJson.keySet()) {
                descriptionLocales.put(locale, descriptionLocalesJson.getString(locale));
            }
        }

        if (obj.has("default_member_permissions") && !obj.isNull("default_member_permissions")) {
            int permissions = obj.getInt("default_member_permissions");
            BitwiseUtil<Permission> util = new BitwiseUtil<>();
            EnumSet<Permission> permissionsList = util.get(permissions, Permission.class);
            defaultMemberPermissions = permissionsList.toArray(new Permission[0]);
        }

        if (obj.has("dm_permission")) {
            canUseInDms = obj.getBoolean("dm_permission");
        }

        if (obj.has("nsfw")) {
            nsfw = obj.getBoolean("nsfw");
        }

        if (obj.has("options")) {
            for (Object v : obj.getJSONArray("options")) {
                options.add(CommandOption.decompile((JSONObject) v));
            }
        }

        return new Command(
                name,
                type,
                description,
                options,
                nameLocales.entrySet().stream().map((entry) -> newLocale(entry.getKey(), entry.getValue())).toArray(Locale[]::new),
                descriptionLocales.entrySet().stream().map((entry) -> newLocale(entry.getKey(), entry.getValue())).toArray(Locale[]::new),
                defaultMemberPermissions,
                canUseInDms,
                nsfw
        );
    }

    private static Locale newLocale(String id, String value) {
        return new Locale() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Locale.class;
            }

            @Override
            public String locale() {
                return id;
            }

            @Override
            public String value() {
                return value;
            }
        };
    }
}
