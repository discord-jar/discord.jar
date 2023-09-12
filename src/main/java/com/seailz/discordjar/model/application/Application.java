package com.seailz.discordjar.model.application;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.scopes.InstallParams;
import com.seailz.discordjar.model.team.Team;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.*;
import com.seailz.discordjar.utils.flag.Bitwiseable;
import com.seailz.discordjar.utils.image.ImageUtils;
import com.seailz.discordjar.utils.model.DiscordJarProp;
import com.seailz.discordjar.utils.model.JSONProp;
import com.seailz.discordjar.utils.model.Model;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
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
import java.util.function.Function;

/**
 * Represents a Discord application, typically a container for a bot.
 */
public class Application implements Model, Snowflake, CDNAble {

    @JSONProp("id")
    private String id;

    @JSONProp("name")
    private String name;

    @JSONProp("icon")
    private String iconHash;

    @JSONProp("description")
    private String description;

    @JSONProp("rpc_origins")
    private List<String> rpcOrigins;

    @JSONProp("bot_public")
    private boolean botPublic;

    @JSONProp("bot_require_code_grant")
    private boolean botRequireCodeGrant;

    @JSONProp("terms_of_service_url")
    private String termsOfServiceUrl;

    @JSONProp("privacy_policy_url")
    private String privacyPolicyUrl;

    @JSONProp("owner")
    private User owner;

    @Deprecated
    @JSONProp("summary")
    private String summary;

    @JSONProp("verify_key")
    private String verifyKey;

    @JSONProp("team")
    private Team team;

    @JSONProp("guild_id")
    private String guildId;

    @JSONProp("guild")
    private Guild guild;

    @JSONProp("primary_sku_id")
    private String primarySkuId;

    @JSONProp("slug")
    private String slug;

    @JSONProp("cover_image")
    private String coverImage;

    @JSONProp("flags")
    private EnumSet<Flag> flags;

    @JSONProp("flags")
    private int flagsRaw;

    @JSONProp("tags")
    private List<String> tags;

    @JSONProp("custom_install_url")
    private String customInstallUrl;

    @JSONProp("install_params")
    private InstallParams installParams;

    @JSONProp("role_connections_verification_url")
    private String roleConnectionsVerificationUrl;

    @JSONProp("approximate_guild_count")
    private int approximateGuildCount;

    @DiscordJarProp
    private DiscordJar discordJar;

    public Application(String id, String name, String iconHash, String description, List<String> rpcOrigins, boolean botPublic,
                       boolean botRequireCodeGrant, String termsOfServiceUrl, String privacyPolicyUrl, User owner,
                       String summary, String verifyKey, Team team, String guildId, Guild guild, String primarySkuId,
                       String slug, String coverImage, EnumSet<Flag> flags, int flagsRaw, List<String> tags,
                       String customInstallUrl, InstallParams installParams, String roleConnectionsVerificationUrl,
                       int approximateGuildCount, DiscordJar discordJar) {
        this.id = id;
        this.name = name;
        this.iconHash = iconHash;
        this.description = description;
        this.rpcOrigins = rpcOrigins;
        this.botPublic = botPublic;
        this.botRequireCodeGrant = botRequireCodeGrant;
        this.termsOfServiceUrl = termsOfServiceUrl;
        this.privacyPolicyUrl = privacyPolicyUrl;
        this.owner = owner;
        this.summary = summary;
        this.verifyKey = verifyKey;
        this.team = team;
        this.guildId = guildId;
        this.guild = guild;
        this.primarySkuId = primarySkuId;
        this.slug = slug;
        this.coverImage = coverImage;
        this.flags = flags;
        this.flagsRaw = flagsRaw;
        this.tags = tags;
        this.customInstallUrl = customInstallUrl;
        this.installParams = installParams;
        this.roleConnectionsVerificationUrl = roleConnectionsVerificationUrl;
        this.approximateGuildCount = approximateGuildCount;
        this.discordJar = discordJar;
    }

    public String id() { return id; }

    public String name() { return name; }

    public String iconHash() { return iconHash; }

    public String description() { return description; }

    public List<String> rpcOrigins() { return rpcOrigins; }

    public boolean botPublic() { return botPublic; }

    public boolean botRequireCodeGrant() { return botRequireCodeGrant; }

    public String termsOfServiceUrl() { return termsOfServiceUrl; }

    public String privacyPolicyUrl() { return privacyPolicyUrl; }

    public User owner() { return owner; }

    public String summary() { return summary; }

    public String verifyKey() { return verifyKey; }

    public Team team() { return team; }

    public String guildId() { return guildId; }

    public Guild guild() { return guild; }

    public String primarySkuId() { return primarySkuId; }

    public String slug() { return slug; }

    public String coverImage() { return coverImage; }

    public EnumSet<Flag> flags() { return flags; }

    public int flagsRaw() { return flagsRaw; }

    public List<String> tags() { return tags; }

    public String customInstallUrl() { return customInstallUrl; }

    public InstallParams installParams() { return installParams; }

    public String roleConnectionsVerificationUrl() { return roleConnectionsVerificationUrl; }

    public int approximateGuildCount() { return approximateGuildCount; }

    public DiscordJar discordJar() { return discordJar; }

    @Override
    public HashMap<String, Function<JSONObject, ?>> customDecoders() {
        return new HashMap<>(){{
            put("team", o -> Team.decompile(o, discordJar));
            put("guild", o -> Guild.decompile(o, discordJar));
        }};
    }

    private Application() {}

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
     * @throws com.seailz.discordjar.utils.Checker.NullArgumentException if the list is null.
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

    @Override
    public StringFormatter formatter() {
        return new StringFormatter("app-icons", id, iconHash);
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
}
