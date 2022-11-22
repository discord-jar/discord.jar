package com.seailz.javadiscordwrapper.model.application;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.core.Compilerable;
import com.seailz.javadiscordwrapper.model.scopes.InstallParams;
import com.seailz.javadiscordwrapper.model.user.User;
import com.seailz.javadiscordwrapper.model.team.Team;
import com.seailz.javadiscordwrapper.utils.flag.FlagUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.EnumSet;
import java.util.List;

/**
 * Represents a Discord application, which is usually a bot.
 * @param id The id of the application.
 * @param name The name of the application.
 * @param iconUrl The icon url of the application.
 * @param description The description of the application.
 * @param rpcOrigins The rpc origins of the application.
 * @param botPublic Whether the bot is public or not.
 * @param botRequireCodeGrant Whether the bot requires code grant or not.
 * @param termsOfServiceUrl The terms of service url of the application.
 * @param privacyPolicyUrl The privacy policy url of the application.
 * @param owner The owner of the application.
 * @param summary The summary of the application.
 * @param verifyKey The verify key of the application.
 * @param team The team of the application.
 * @param guildId The guild id of the application.
 * @param primary_sku_id The primary sku id of the application.
 * @param slug The slug of the application.
 * @param coverImage The cover image of the application.
 * @param flags The flags of the application.
 * @param flagsRaw The raw flags of the application.
 * @param tags The tags of the application.
 * @param customInstallUrl The custom install url of the application.
 * @param installParams The install params of the application.
 * @param roleConnectionsVerificationUrl The role connections verification url of the application.
 */
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
        InstallParams installParams,
        String roleConnectionsVerificationUrl
) implements Compilerable {

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
                .put("custom_install_url", customInstallUrl)
                .put("role_connections_verification_url", roleConnectionsVerificationUrl);
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
        String roleConnectionsVerificationUrl;

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
            flags = FlagUtil.getApplicationFlagsByInt(flagsRaw);
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

        try {
            roleConnectionsVerificationUrl = obj.getString("role_connections_verification_url");
        } catch (JSONException e) {
            roleConnectionsVerificationUrl = null;
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
                installParams,
                roleConnectionsVerificationUrl
        );

    }
}
