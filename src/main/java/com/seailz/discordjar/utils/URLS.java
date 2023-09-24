package com.seailz.discordjar.utils;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.api.APIRelease;
import com.seailz.discordjar.model.api.version.APIVersion;

/**
 * A list of all endpoints used by discord.jar
 *
 * @author Seailz
 * @see APIVersion
 * @since 1.0
 */
public final class URLS {

    public static APIVersion version = APIVersion.getLatest();
    public static APIRelease release = APIRelease.STABLE;

    public URLS(APIRelease release, APIVersion version) {
        URLS.version = version;
        URLS.release = release;
    }

    public static final String BASE_URL = "https://" + release.getBaseUrlPrefix() + "discord.com/api/v" + version.getCode();

    public static class POST {
        public static class INTERACTIONS {
            public static final String CALLBACK = "/interactions/{interaction.id}/{interaction.token}/callback";
            public static final String FOLLOWUP = "/webhooks/application.id/interaction.token";

        }

        public static class COMMANDS {

            /**
             * Endpoint for global commands.
             *
             * @param {application.id} The ID of the application to add a global command to
             */
            public static final String GLOBAL_COMMANDS = "/applications/{application.id}/commands";
            /**
             * Endpoint for guild commands.
             *
             * @param {application.id} The ID of the application to add a guild command to
             * @param {guild.id} The guild to add the command to
             */
            public static final String GUILD_COMMANDS = "/applications/{application.id}/guilds/{guild.id}/commands";
        }

        public static class MESSAGES {
            public static final String SEND = "/channels/{channel.id}/messages";
            public static final String EDIT = "/channels/{channel.id}/messages/{message.id}";
            public static final String START_THREAD_FORUM = "/channels/{channel.id}/threads";
        }

        public static class GUILDS {
            /**
             * Returns the number of members that would be pruned from the guild under specific circumstances.
             * @param id The id of the guild
             */
            public static final String PRUNE = "/guilds/{guild.id}/prune";
            public static final String UPDATE_MFA = "/guilds/{guild.id}/mfa";
            public static class AUTOMOD {
                /**
                 * Creates an automod rule
                 *
                 * @param {guild.id} The guild to create the rule in
                 */
                public static final String CREATE_AUTO_MOD_RULE = "/guilds/{guild.id}/auto-moderation/rules";
             }

            public static class CHANNELS {
                /**
                 * Creates a channel
                 */
                public static final String CREATE = "/guilds/{guild.id}/channels";
            }

            public static class SCHEDULED_EVENTS {
                public static final String CREATE_GUILD_SCHEDULED_EVENT = "/guilds/{guild.id}/scheduled-events";
            }
        }

        public static class CHANNELS {
            public static final String TRIGGER_TYPING_INDICATOR = "/channels/{channel.id}/typing";
            public static final String CREATE_CHANNEL_INVITE = "/channels/{channel.id}/invites";

            public static class MESSAGES {
                public static String BULK_DELETE = "/channels/{channel.id}/messages/bulk-delete";
                public static class THREADS {
                    public static String START_THREAD_FROM_MESSAGE = "/channels/{channel.id}/messages/{message.id}/threads";
                }
            }
        }

        public static class USERS {
            /**
             * Opens a DM channel with a user
             * <p>
             * You should not use this endpoint to DM everyone in a server about something.
             * DMs should generally be initiated by a user action.
             * If you open a significant amount of DMs too quickly, your bot may be rate limited or blocked from opening new ones.
             *
             * @param recipitent_id The user id to open a DM channel with.
             * @see <a href="https://discord.com/developers/docs/resources/user#create-dm">Create DM Documentation</a>
             */
            public static final String CREATE_DM = "/users/@me/channels";
        }
    }

    public static class GET {

        public static class VOICE {
            public static class REGIONS {
                /**
                 * Retrieves a list of {@link com.seailz.discordjar.model.channel.audio.VoiceRegion VoiceRegion} objects for the given guild.
                 */
                public static final String GET_VOICE_REGIONS = "/voice/regions";
            }
        }

        public static class GATEWAY {
            /**
             * Returns the gateway URL
             */
            public static String GET_GATEWAY_URL = "/gateway";
            public static String GET_GATEWAY_BOT = "/gateway/bot";
        }

        public static class APPLICATION {
            /**
             * Requests info about the current application from the API
             * Returns the bot's application object
             */
            public static String APPLICATION_INFORMATION = "/applications/@me";

            public static class COMMANDS {
                public static String GET_GLOBAL_APPLICATION_COMMANDS = "/applications/{application.id}/commands";
                public static String GET_GLOBAL_APPLICATION_COMMAND = "/applications/{application.id}/commands/{command.id}";

                public static String GET_GUILD_APPLICATION_COMMANDS = "/applications/{application.id}/guilds/{guild.id}/commands";
                public static String GET_GUILD_APPLICATION_COMMAND = "/applications/{application.id}/guilds/{guild.id}/commands/{command.id}";
            }
        }

        public static class INVITES {
            public static String GET_INVITE = "/invites/{invite.code}";
        }

        public static class USER {
            /**
             * Returns info about a user
             *
             * @param id The id of the user
             */
            public static String GET_USER = "/users/{user.id}";
        }

        public static class CHANNELS {
            /**
             * Returns info about a channel
             *
             * @param id The id of the channel
             */
            public static String GET_CHANNEL = "/channels/{channel.id}";
            /**
             * Returns a message
             *
             * @param id The id of the channel
             * @param message.id The id of the message
             */
            public static String GET_MESSAGE = "/channels/{channel.id}/messages/{message.id}";

            public static class MESSAGES {
                /**
                 * Returns a list of messages
                 *
                 * @param id The id of the channel
                 */
                public static String GET_MESSAGES = "/channels/{channel.id}/messages";
                /**
                 * Returns a message
                 */
                public static String GET_MESSAGE = "/channels/{channel.id}/messages/{message.id}";
            }
        }

        public static class GUILDS {
            /**
             * Gets active threads in a guild.
             */
            public static final String GET_ACTIVE_THREADS = "/guilds/{guild.id}/threads/active";
            /**
             * Searches a guild's members with a specified filter.
             */
            public static final String SEARCH_MEMBERS = "/guilds/{guild.id}/members/search?query={filter}&limit={limit}";
            /**
             * Retrieves the guild onboarding flow for the guild.
             * <br>See {@link com.seailz.discordjar.model.guild.Guild.Onboarding Onboarding Object}
             */
            public static String GET_GUILD_ONBOARDING = "/guilds/{guild.id}/onboarding";

            /**
             * Returns a list of invites for this guild.
             * Requires the MANAGE_GUILD permission.
             */
            public static String GET_GUILD_INVITES = "/guilds/{guild.id}/invites";

            /**
             * Returns bans in a guild
             * @param id The id of the guild
             */
            public static final String BANS = "/guilds/{guild.id}/bans";
            /**
             * Returns a ban on a user in a guild
             * @param guild.id The id of the guild
             * @param user.id The id of the banned user
             */
            public static final String USER_BAN = "/guilds/{guild.id}/bans/{user.id}";
            /**
             * Returns the number of members that would be pruned from the guild under specific circumstances.
             * @param id The id of the guild
             */
            public static final String PRUNE = "/guilds/{guild.id}/prune";
            /**
             * Returns a {@link com.seailz.discordjar.model.guild.Guild} object containing information about the guild
             *
             * @param id The id of the guild
             */
            public static String GET_GUILD = "/guilds/{guild.id}?with_counts=true";

            /**
             * Read {@link DiscordJar#getGuilds()} for more information
             */
            public static String GET_CURRENT_USER_GUILDS = "/users/@me/guilds";

            public static class CHANNELS {
                /**
                 * Returns a list of {@link com.seailz.discordjar.model.channel.Channel} objects
                 *
                 * @param id The id of the guild
                 */
                public static String GET_GUILD_CHANNELS = "/guilds/{guild.id}/channels";
            }

            public static class EMOJIS {
                /**
                 * Gets guild emojis
                 * @param id The id of the guild
                 */
                public static String GUILD_EMOJIS = "/guilds/{guild.id}/emojis";
                /**
                 * Gets a guild emoji by id
                 * @param id The id of the guild
                 * @param emoji.id The id of the emoji
                 */
                public static String GET_GUILD_EMOJI = "/guilds/{guild.id}/emojis/{emoji.id}";
            }

            public static class SCHEDULED_EVENTS {
                public static String GET_SCHEDULED_EVENTS = "/guilds/{guild.id}/scheduled-events";
                public static String GET_SCHEDULED_EVENT = "/guilds/{guild.id}/scheduled-events/{event.id}";
            }

            public static class STICKERS {
                /**
                 * Returns a list of guild stickers
                 *
                 * @param id The id of the guild
                 */
                public static String GET_GUILD_STICKERS = "/guilds/{guild.id}/stickers";
                /**
                 * Returns a guild sticker
                 *
                 * @param id The id of the guild
                 * @param sticker.id The id of the sticker
                 */
                public static String GET_GUILD_STICKER = "/guilds/{guild.id}/stickers/{sticker.id}";
            }

            public static class AUTOMOD {
                /**
                 * Returns a list of guild automod settings
                 *
                 * @param id The id of the guild
                 */
                public static String LIST_AUTOMOD_RULES = "/guilds/{guild.id}/auto-moderation/rules";
                /**
                 * Returns an automod rule
                 *
                 * @param guild.id The id of the guild
                 * @param rule.id The id of the rule
                 */
                public static String GET_AUTOMOD_RULE = "/guilds/{guild.id}/auto-moderation/rules/{rule.id}";
            }

            public static class MEMBERS {
                /**
                 * Returns a list of guild members
                 *
                 * @param id The id of the guild
                 */
                public static String LIST_GUILD_MEMBERS = "/guilds/{guild.id}/members";
                /**
                 * Returns a guild member
                 *
                 * @param guild.id The id of the guild
                 * @param user.id The id of the user
                 */
                public static String GET_GUILD_MEMBER = "/guilds/{guild.id}/members/{user.id}";
            }

            public static class ROLES {
                public static String GET_GUILD_ROLES = "/guilds/{guild.id}/roles";
            }
        }

        public static class INTERACTIONS {
            public static String GET_ORIGINAL_INTERACTION_RESPONSE = "/webhooks/{application.id}/{interaction.token}/messages/@original";
            public static String GET_FOLLOWUP_MESSAGE = "/webhooks/{application.id}/{interaction.token}/messages/{message.id}";
        }

        public static class APPLICATIONS {
            /**
             * Returns an {@link com.seailz.discordjar.model.application.Application object containing information about the application of the bot selected}
             *
             * @param id the id of the bot
             */
            public static String GET_APPLICATION = "/applications/{bot.id}/rpc";
            /**
             * Returns a list of {@link com.seailz.discordjar.model.application.ApplicationRoleConnectionMetadata} objects containing information about the role connections the application has.
             * @param id the id of the app
             */
            public static String GET_APPLICATION_ROLE_CONNECTIONS = "/applications/{application.id}/role-connections/metadata";
        }

        public static class STICKER {
            /**
             * Returns a {@link com.seailz.discordjar.model.emoji.sticker.Sticker} object containing information about the sticker
             *
             * @param id The id of the sticker
             */
            public static String GET_STICKER = "/stickers/{sticker.id}";
            /**
             * Returns a list of {@link com.seailz.discordjar.model.emoji.sticker.StickerPack} objects containing information about the sticker packs Nitro users can use
             */
            public static String GET_NITRO_STICKER_PACKS = "/sticker-packs";
        }
    }

    public static class DELETE {

        public static class GUILD {
            /**
             * Deletes a guild.
             * @param id The id of the guild
             */
            public static final String DELETE_GUILD = "/guilds/{guild.id}";
            /**
             * Deletes a role from a guild.
             * @param guild.id The id of the guild
             * @param role.id The id of the role
             */
            public static final String ROLES = "/guilds/{guild.id}/roles/{role.id}";
            /**
             * Leaves a guild
             *
             * @param id The id of the guild
             */
            public static String LEAVE_GUILD = "/users/@me/guilds/{guild.id}";

            public static class SCHEDULED_EVENTS {
                public static String DELETE_SCHEDULED_EVENT = "/guilds/{guild.id}/scheduled-events/{event.id}";
            }

            public static class MEMBER {
                /**
                 * Kicks a member from a Guild.
                 * @param guild.id The id of the guild.
                 * @param user.id The id of the user.
                 */
                public static final String KICK_MEMBER = "/guilds/{guild.id}/members/{user.id}";
                /**
                 * Removes a role from a member.
                 */
                public static String REMOVE_GUILD_MEMBER_ROLE = "/guilds/{guild.id}/members/{user.id}/roles/{role.id}";
            }

            public static class AUTOMOD {
                /**
                 * Deletes an automod rule
                 *
                 * @param guild.id The id of the guild
                 * @param rule.id The id of the rule
                 */
                public static String DELETE_AUTOMOD_RULE = "/guilds/{guild.id}/auto-moderation/rules/{rule.id}";
            }

            public static class STICKER {
                /**
                 * Deletes a guild sticker
                 *
                 * @param id The id of the guild
                 * @param sticker.id The id of the sticker
                 */
                public static String DELETE_GUILD_STICKER = "/guilds/{guild.id}/stickers/{sticker.id}";
            }
        }

        public static class INTERACTION {
            public static String DELETE_FOLLOWUP_MESSAGE = "/webhooks/{application.id}/{interaction.token}/messages/{message.id}";
            public static String DELETE_ORIGINAL_INTERACTION_RESPONSE = "/webhooks/{application.id}/{interaction.token}/messages/@original";
        }

        public static class CHANNEL {
            public static class PINS {
                public static String UNPIN_MESSAGE = "/channels/{channel.id}/pins/{message.id}";
            }
            public static class MESSAGE {
                public static String DELETE_MESSAGE = "/channels/{channel.id}/messages/{message.id}";
            }
            /**
             * Deletes a channel
             *
             * @param id The id of the channel
             */
            public static String DELETE_CHANNEL = "/channels/{channel.id}";

            public static class THREAD_MEMBERS {
                public static String REMOVE_THREAD_MEMBER = "/channels/{channel.id}/thread-members/{user.id}";
            }
        }

        public static class INVITE {
            public static String DELETE_INVITE = "/invites/{invite.code}";
        }

        public static class APPLICATION {
            public static class COMMANDS {
                public static String DELETE_GLOBAL_COMMAND = "/applications/{application.id}/commands/{command.id}";
                public static String DELETE_GUILD_COMMAND = "/applications/{application.id}/guilds/{guild.id}/commands/{command.id}";
            }
        }
    }

    public static class PATCH {
        public static class GUILD {
            public static class SCHEDULED_EVENTS {
                public static String MODIFY_GUILD_SCHEDULED_EVENT = "/guilds/{guild.id}/scheduled-events/{event.id}";
            }
            public static class MEMBER {
                /**
                 * Modifies a guild member
                 *
                 * @param guild.id The id of the guild
                 * @param user.id The id of the user
                 */
                public static String MODIFY_GUILD_MEMBER = "/guilds/{guild.id}/members/{user.id}";
            }
            public static class AUTOMOD {
                /**
                 * Updates an automod rule
                 *
                 * @param guild.id The id of the guild
                 * @param rule.id The id of the rule
                 */
                public static String UPDATE_AUTOMOD_RULE = "/guilds/{guild.id}/auto-moderation/rules/{rule.id}";
            }
            public static class STICKER {
                /**
                 * Modifies a guild sticker
                 *
                 * @param id The id of the guild
                 * @param sticker.id The id of the sticker
                 */
                public static String MODIFY_GUILD_STICKER = "/guilds/{guild.id}/stickers/{sticker.id}";
            }
        }

        public static class CHANNEL {
            public static String MODIFY_CHANNEL = "/channels/{channel.id}";

            public static class MESSAGE {
                public static String EDIT = "/channels/{channel.id}/messages/{message.id}";
            }
        }

        public static class INTERACTIONS {
            public static class MESSAGES {
                public static String MODIFY_FOLLOWUP_MESSAGE = "/webhooks/{application.id}/{interaction.token}/messages/{message.id}";
                public static String MODIFY_ORIGINAL_INTERACTION_RESPONSE = "/webhooks/{application.id}/{interaction.token}/messages/@original";
            }
        }

        public static class APPLICATIONS {
            public static class COMMANDS {
                public static String EDIT_GLOBAL_COMMAND = "/applications/{application.id}/commands/{command.id}";
                public static String EDIT_GUILD_COMMAND = "/applications/{application.id}/guilds/{guild.id}/commands/{command.id}";
            }
        }
    }


    public static class PUT {
        public static class APPLICATIONS {
            public static String MODIFY_APPLICATION_ROLE_CONNECTIONS = "/applications/{application.id}/role-connections/metadata";
        }

        public static class GUILD {
            /**
             * Bans a user from a guild.
             * @param guild.id The id of the guild
             * @param user.id The id of the user to ban
             */
            public static final String BAN_USER = "/guilds/{guild.id}/bans/{user.id}";

            public static final String MODIFY_GUILD_ONBOARDING = "/guilds/{guild.id}/onboarding";

            public static class MEMBERS {
                public static class ROLES {
                    /**
                     * Adds a role to a guild member
                     *
                     * @param guild.id The id of the guild
                     * @param user.id The id of the user
                     * @param role.id The id of the role
                     */
                    public static String ADD_GUILD_MEMBER_ROLE = "/guilds/{guild.id}/members/{user.id}/roles/{role.id}";
                }
            }
        }

        public static class MESSAGES {
            public static String ADD_REACTION = "/channels/{channel.id}/messages/{message.id}/reactions/{emoji}/@me";
        }

        public static class CHANNELS {
            public static class PERMISSIONS {
                public static String EDIT_CHANNEL_PERMS = "/channels/{channel.id}/permissions/{overwrite.id}";
            }
            public static class PINS {
                public static String PIN_MESSAGE = "/channels/{channel.id}/pins/{message.id}";
            }
        }
    }

    public static class OAUTH2 {
        /**
         * Token URL
         */
        public static String TOKEN_URL = "/oauth2/token";

        public static class PUT {
            public static class USERS {
                public static class APPLICATIONS {
                    public static class ROLE_CONNECTIONS {
                        public static String UPDATE_USER_APPLICATION_ROLE_CONNECTION = "/users/@me/applications/{application.id}/role-connection";
                    }
                }
            }
        }

        public static class GET {
            public static String GET_CURRENT_AUTH_INFO = "/oauth2/@me";
        }
    }

    public static class GATEWAY {
        public static String BASE_URL = "wss://gateway.discord.gg/";
    }

    public static class CDN {
        public static String BASE_URL = "https://cdn.discordapp.com";
        public static String DEFAULT_USER_AVATAR = BASE_URL + "/embed/avatars/%s.png";
    }

}
