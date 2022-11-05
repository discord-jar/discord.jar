package com.seailz.javadiscordwrapper.model;

import com.seailz.javadiscordwrapper.core.Compilerable;
import com.seailz.javadiscordwrapper.utils.flag.FlagUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.EnumSet;

public record User(
        String id, String username, String discriminator, String avatar, boolean bot,
        boolean system, boolean mfaEnabled, String locale, boolean verified, String email,
        EnumSet<Flag> flags, int flagsRaw, PremiumType premiumType, EnumSet<Flag> publicFlags, int publicFlagsRaw
) implements Compilerable {

    /**
     * Converts this User object to a JSONObject
     * @return a JSONObject consisting of the data in this User object
     */
    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("id", id)
                .put("username", username)
                .put("discriminator", discriminator)
                .put("avatar", avatar)
                .put("bot", bot)
                .put("system", system)
                .put("mfa_enabled", mfaEnabled)
                .put("locale", locale)
                .put("verified", verified)
                .put("email", email)
                .put("flags", flags)
                .put("premium_type", premiumType.getId())
                .put("public_flags", publicFlags);
    }

    /**
     * Converts a JSONObject to a User object
     * @param obj the JSONObject to convert
     * @return a User object consisting of the data in the JSONObject
     */
    public static User decompile(JSONObject obj) {
        String id;
        String username;
        String discriminator;
        String avatar;
        boolean bot;
        boolean system;
        boolean mfaEnabled;
        String locale;
        boolean verified;
        String email;
        EnumSet<Flag> flags;
        PremiumType premiumType;
        EnumSet<Flag> publicFlags;
        int publicFlagsRaw = 0;
        int flagsRaw = 0;

        try {
            id = obj.getString("id");
        } catch (JSONException e) {
            id = null;
        }

        try {
            username = obj.getString("username");
        } catch (JSONException e) {
            username = null;
        }

        try {
            discriminator = obj.getString("discriminator");
        } catch (JSONException e) {
            discriminator = null;
        }

        try {
            avatar = obj.getString("avatar");
        } catch (JSONException e) {
            avatar = null;
        }

        try {
            bot = obj.getBoolean("bot");
        } catch (JSONException e) {
            bot = false;
        }

        try {
            system = obj.getBoolean("system");
        } catch (JSONException e) {
            system = false;
        }

        try {
            mfaEnabled = obj.getBoolean("mfa_enabled");
        } catch (JSONException e) {
            mfaEnabled = false;
        }

        try {
            locale = obj.getString("locale");
        } catch (JSONException e) {
            locale = null;
        }

        try {
            verified = obj.getBoolean("verified");
        } catch (JSONException e) {
            verified = false;
        }

        try {
            email = obj.getString("email");
        } catch (JSONException e) {
            email = null;
        }

        try {
            flags = FlagUtil.getFlagsByInt(obj.getInt("flags"));
            flagsRaw = obj.getInt("flags");
        } catch (JSONException e) {
            flags = null;
        }

        try {
            premiumType = PremiumType.fromId(obj.getInt("premium_type"));
        } catch (JSONException e) {
            premiumType = null;
        }

        try {
            publicFlags = FlagUtil.getFlagsByInt(obj.getInt("public_flags"));
            publicFlagsRaw = obj.getInt("public_flags");
        } catch (JSONException e) {
            publicFlags = null;
        }
        return new User(id, username, discriminator, avatar, bot, system, mfaEnabled, locale, verified, email, flags, flagsRaw, premiumType, publicFlags, publicFlagsRaw);
    }
}
