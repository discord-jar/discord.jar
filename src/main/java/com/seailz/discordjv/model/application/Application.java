package com.seailz.discordjv.model.application;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.model.scopes.InstallParams;
import com.seailz.discordjv.model.team.Team;
import com.seailz.discordjv.model.user.User;
import com.seailz.discordjv.utils.Snowflake;
import com.seailz.discordjv.utils.flag.BitwiseUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.EnumSet;
import java.util.List;

public record Application(
        String id,
        String name,
        String iconUrl,
        String description,
        List<String> rpcOrigins,
        boolean botPublic,
        boolean botRequireCodeGrant,
        String termsOfServiceUrl,
        String privacyPolicyUrl,
        User owner,
        // This is scheduled for removal in v11
        @Deprecated String summary,
        String verifyKey,
        Team team,
        String guildId,
        String primary_sku_id,
        String slug,
        String coverImage,
        EnumSet<ApplicationFlag> flags,
        int flagsRaw,
        List<String> tags,
        String customInstallUrl,
        InstallParams installParams
) implements Compilerable, Snowflake {

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("id", id)
                .put("name", name)
                .put("icon", iconUrl)
                .put("description", description)
                .put("rpc_origins", rpcOrigins)
                .put("bot_public", botPublic)
                .put("bot_require_code_grant", botRequireCodeGrant)
                .put("terms_of_service_url", termsOfServiceUrl)
                .put("privacy_policy_url", privacyPolicyUrl)
                .put("owner", owner.compile())
                .put("summary", summary)
                .put("verify_key", verifyKey)
                .put("team", team.compile())
                .put("guild_id", guildId)
                .put("primary_sku_id", primary_sku_id)
                .put("slug", slug)
                .put("cover_image", coverImage)
                .put("flags", flagsRaw)
                .put("tags", tags)
                .put("custom_install_url", customInstallUrl);
    }

    public static Application decompile(JSONObject obj, DiscordJv discordJv) {
        String id;
        String name;
        String iconUrl;
        String description;
        List<String> rpcOrigins;
        boolean botPublic;
        boolean botRequireCodeGrant;
        String termsOfServiceUrl;
        String privacyPolicyUrl;
        User owner;
        String summary;
        String verifyKey;
        Team team;
        String guildId;
        String primary_sku_id;
        String slug;
        String coverImage;
        EnumSet<ApplicationFlag> flags = null;
        int flagsRaw;
        List<String> tags;
        String customInstallUrl;
        InstallParams installParams;

        try {
            id = obj.getString("id");
        } catch (JSONException e) {
            id = null;
        }

        try {
            name = obj.getString("name");
        } catch (JSONException e) {
            name = null;
        }

        try {
            iconUrl = obj.getString("icon");
        } catch (JSONException e) {
            iconUrl = null;
        }

        try {
            description = obj.getString("description");
        } catch (JSONException e) {
            description = null;
        }

        try {
            rpcOrigins = obj.getJSONArray("rpc_origins").toList().stream().map(Object::toString).toList();
        } catch (JSONException e) {
            rpcOrigins = null;
        }

        try {
            botPublic = obj.getBoolean("bot_public");
        } catch (JSONException e) {
            botPublic = false;
        }

        try {
            botRequireCodeGrant = obj.getBoolean("bot_require_code_grant");
        } catch (JSONException e) {
            botRequireCodeGrant = false;
        }

        try {
            termsOfServiceUrl = obj.getString("terms_of_service_url");
        } catch (JSONException e) {
            termsOfServiceUrl = null;
        }

        try {
            privacyPolicyUrl = obj.getString("privacy_policy_url");
        } catch (JSONException e) {
            privacyPolicyUrl = null;
        }

        try {
            owner = User.decompile(obj.getJSONObject("owner"), discordJv);
        } catch (JSONException e) {
            owner = null;
        }

        try {
            summary = obj.getString("summary");
        } catch (JSONException e) {
            summary = null;
        }

        try {
            verifyKey = obj.getString("verify_key");
        } catch (JSONException e) {
            verifyKey = null;
        }

        try {
            team = Team.decompile(obj.getJSONObject("team"), discordJv);
        } catch (JSONException e) {
            team = null;
        }

        try {
            guildId = obj.getString("guild_id");
        } catch (JSONException e) {
            guildId = null;
        }

        try {
            primary_sku_id = obj.getString("primary_sku_id");
        } catch (JSONException e) {
            primary_sku_id = null;
        }

        try {
            slug = obj.getString("slug");
        } catch (JSONException e) {
            slug = null;
        }

        try {
            coverImage = obj.getString("cover_image");
        } catch (JSONException e) {
            coverImage = null;
        }

        try {
            flagsRaw = obj.getInt("flags");
            flags = flags = new BitwiseUtil<ApplicationFlag>().get(flagsRaw, ApplicationFlag.class);
        } catch (JSONException e) {
            flagsRaw = 0;
        }

        try {
            tags = obj.getJSONArray("tags").toList().stream().map(Object::toString).toList();
        } catch (JSONException e) {
            tags = null;
        }

        try {
            customInstallUrl = obj.getString("custom_install_url");
        } catch (JSONException e) {
            customInstallUrl = null;
        }

        try {
            installParams = InstallParams.decompile(obj.getJSONObject("install_params"));
        } catch (JSONException e) {
            installParams = null;
        }

        return new Application(
                id,
                name,
                iconUrl,
                description,
                rpcOrigins,
                botPublic,
                botRequireCodeGrant,
                termsOfServiceUrl,
                privacyPolicyUrl,
                owner,
                summary,
                verifyKey,
                team,
                guildId,
                primary_sku_id,
                slug,
                coverImage,
                flags,
                flagsRaw,
                tags,
                customInstallUrl,
                installParams
        );

    }
}
