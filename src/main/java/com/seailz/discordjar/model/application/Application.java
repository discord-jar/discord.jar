package com.seailz.discordjar.model.application;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.scopes.InstallParams;
import com.seailz.discordjar.model.team.Team;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.Checker;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.flag.Bitwiseable;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import com.seailz.discordjar.utils.Snowflake;
import com.seailz.discordjar.utils.flag.BitwiseUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
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
        Guild guild,
        String primary_sku_id,
        String slug,
        String coverImage,
        EnumSet<Flag> flags,
        int flagsRaw,
        List<String> tags,
        String customInstallUrl,
        InstallParams installParams,
        String roleConnectionsVerificationUrl,
        int approximateGuildCount,
        HashMap<IntegrationTypes, IntegrationTypeConfiguration> integrationTypes,
        DiscordJar discordJar
) implements Compilerable, Snowflake {

    @Override
    public JSONObject compile() {
        JSONObject integrationTypes = new JSONObject();
        this.integrationTypes.forEach((key, val) -> {
            integrationTypes.put(String.valueOf(key.code), val.compile());
        });
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
                .put("role_connections_verification_url", roleConnectionsVerificationUrl)
                .put("approximate_guild_count", approximateGuildCount)
                .put("integration_types_config", integrationTypes)
                .put("guild", guild.compile());
    }

    public static Application decompile(JSONObject obj, DiscordJar discordJar) {
        if (obj == null) return new Application(null,  null, null, null, null, false, false, null, null, null, null, null, null, null, null, null, null, null, null, 0, null,  null, null, null, 0, null, discordJar);
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
        Guild guild;
        String primary_sku_id;
        String slug;
        String coverImage;
        EnumSet<Flag> flags = null;
        int flagsRaw;
        List<String> tags;
        String customInstallUrl;
        InstallParams installParams;
        String roleConnectionsVerificationUrl;
        int approximateGuildCount;
        HashMap<IntegrationTypes, IntegrationTypeConfiguration> integrationTypesConfiguration = null;

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
            owner = User.decompile(obj.getJSONObject("owner"), discordJar);
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
            team = Team.decompile(obj.getJSONObject("team"), discordJar);
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
            flags = flags = new BitwiseUtil<Flag>().get(flagsRaw, Flag.class);
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

        try {
            approximateGuildCount = obj.getInt("approximate_guild_count");
        } catch (JSONException e) {
            approximateGuildCount = 0;
        }

        try {
            guild = Guild.decompile(obj.getJSONObject("guild"), discordJar, false);
        } catch (JSONException e) {
            guild = null;
        }

        if (obj.has("integration_types_config")) {
            integrationTypesConfiguration = new HashMap<>();
            JSONObject integrationTypesConfig = obj.getJSONObject("integration_types_config");
            for (String code : integrationTypesConfig.keySet()) {
                integrationTypesConfiguration.put(IntegrationTypes.getByCode(Integer.parseInt(code)), IntegrationTypeConfiguration.decompile(integrationTypesConfig.getJSONObject(code)));
            }
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
                guild,
                primary_sku_id,
                slug,
                coverImage,
                flags,
                flagsRaw,
                tags,
                customInstallUrl,
                installParams,
                roleConnectionsVerificationUrl,
                approximateGuildCount,
                integrationTypesConfiguration,
                discordJar
        );
    }

    /**
     * Returns a list of application role connection metadata objects for the given application.
     * @return {@link List} of {@link ApplicationRoleConnectionMetadata}
     */
    @Nullable
    public List<ApplicationRoleConnectionMetadata> getRoleConnections() {
        DiscordResponse response = null;
        try {
            response = new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.GET.APPLICATIONS.GET_APPLICATION_ROLE_CONNECTIONS
                            .replace("{application.id}", id),
                    discordJar,
                    URLS.GET.APPLICATIONS.GET_APPLICATION_ROLE_CONNECTIONS,
                    RequestMethod.GET
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }

        if (response.code() == 200) {
            return new JSONArray(response.body()).toList().stream()
                    .map(o -> ApplicationRoleConnectionMetadata.decompile((JSONObject) o))
                    .toList();
        } else {
            return null;
        }
    }

    /**
     * Updates a list of application role connection metadata objects for the given application.
     * An application can have up to 5 metadata records.
     *
     * @param roleConnections The list of role connection metadata objects to update.
     *
     * @throws Checker.NullArgumentException if the list is null.
     * @throws IllegalArgumentException if the list has more than 5 elements.
     */
    public void setRoleConnections(@NotNull List<ApplicationRoleConnectionMetadata> roleConnections) {
        Checker.notNull(roleConnections, "Role connections must not be null");
        Checker.sizeLessThan(roleConnections, 5, "You can only have up to 5 role connections.");
        JSONArray roleConnectionsArray = new JSONArray();
        roleConnections.stream().map(ApplicationRoleConnectionMetadata::compile).forEach(roleConnectionsArray::put);

        try {
            new DiscordRequest(
                    roleConnectionsArray,
                    new HashMap<>(),
                    URLS.PUT.APPLICATIONS.MODIFY_APPLICATION_ROLE_CONNECTIONS
                            .replace("{application.id}", id),
                    discordJar,
                    URLS.PUT.APPLICATIONS.MODIFY_APPLICATION_ROLE_CONNECTIONS,
                    RequestMethod.PUT
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    public void setRoleConnections(ApplicationRoleConnectionMetadata... roleConnections) {
        setRoleConnections(Arrays.asList(roleConnections));
    }

    /**
     * Represents a flag on an application's account.
     * This is used to determine if an application has a certain feature enabled or not.
     *
     * @author Seailz
     * @since 1.0
     */
    @SuppressWarnings("rawtypes")
    public enum Flag implements Bitwiseable {
        // Indicates if an app uses the Auto Moderation API.
        APPLICATION_AUTO_MODERATION_RULE_CREATE_BADGE(6),
        // Intent required for bots in 100 or more servers to receive presence_update events
        GATEWAY_PRESENCE(12),
        // Intent required for bots in under 100 servers to receive presence_update events, found in Bot Settings
        GATEWAY_PRESENCE_LIMITED(13),
        // Intent required for bots in 100 or more servers to receive member-related events like guild_member_add. See list of member-related events under GUILD_MEMBERS
        GATEWAY_GUILD_MEMBERS(14),
        // Intent required for bots in under 100 servers to receive member-related events like guild_member_add, found in Bot Settings. See list of member-related events under GUILD_MEMBERS
        GATEWAY_GUILD_MEMBERS_LIMITED(15),
        // Indicates unusual growth of an app that prevents verification
        VERIFICATION_PENDING_GUILD_LIMIT(16),
        // Indicates if an app is embedded within the Discord client (currently unavailable publicly)
        EMBEDDED(17),
        // Intent required for bots in 100 or more servers to receive message content
        GATEWAY_MESSAGE_CONTENT(18),
        // Intent required for bots in under 100 servers to receive message content, found in Bot Settings
        GATEWAY_MESSAGE_CONTENT_LIMITED(19),
        // Indicates if an app has registered global application commands
        APPLICATION_COMMANDS_BADE(23),
        // considered an "active" bot, can be used for getting the "Active Developer" badge
        ACTIVE_BOT(24),
        ;

        private final int id;

        Flag(int id) {
            this.id = id;
        }

        @Override
        public int getLeftShiftId() {
            return 1 << id;
        }

        @Override
        public int id() {
            return id;
        }
    }


    public enum IntegrationTypes {

        GUILD_INSTALL(0),
        USER_INSTALL(1),
        UNKNOWN(-1)
        ;

        private final int code;

        IntegrationTypes(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static IntegrationTypes getByCode(int code) {
            for (IntegrationTypes value : values()) {
                if (value.getCode() == code) return value;
            }
            return UNKNOWN;
        }
    }

    public record IntegrationTypeConfiguration(
            InstallParams installParams
    ) implements Compilerable {

        @Override
        public JSONObject compile() {
            return new JSONObject()
                    .put("oauth2_install_params", installParams == null ? JSONObject.NULL : installParams.compile());
        }

        public static IntegrationTypeConfiguration decompile(JSONObject obj) {
            InstallParams oauth2InstallParams = null;
            if (obj.has("oauth2_install_params")) {
                oauth2InstallParams = InstallParams.decompile(obj.getJSONObject("oauth2_install_params"));
            }
            return new IntegrationTypeConfiguration(oauth2InstallParams);
        }
    }
}
