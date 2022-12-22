package com.seailz.discordjv.utils;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.utils.version.APIVersion;

/**
 * A list of all endpoints used by Discord.jv
 *
 * @author Seailz
 * @see com.seailz.discordjv.utils.version.APIVersion
 * @since 1.0
 */
public final class URLS {

    public static APIVersion version = APIVersion.getLatest();

    public URLS(APIVersion version) {
        URLS.version = version;
    }

    public static final String BASE_URL = "https://discord.com/api/v" + version.getCode();

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
        }

        public static class GUILDS {
            public static class AUTOMOD {
                /**
                 * Creates an automod rule
                 * @param {guild.id} The guild to create the rule in
                 */
                public static final String CREATE_AUTO_MOD_RULE = "/guilds/{guild.id}/auto-moderation/rules";
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
                 * Retrieves a list of {@link com.seailz.discordjv.model.channel.audio.VoiceRegion VoiceRegion} objects for the given guild.
                 */
                public static final String GET_VOICE_REGIONS = "/voice/regions";
            }
        }

        public static class GATEWAY {
            /**
             * Returns the gateway URL
             */
            public static String GET_GATEWAY_URL = "/gateway";
        }

        public static class APPLICATION {
            /**
             * Requests info about the current application from the API
             * Returns the bot's application object
             */
            public static String APPLICATION_INFORMATION = "/oauth2/applications/@me";
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
        }

        public static class GUILDS {
            /**
             * Returns a {@link com.seailz.discordjv.model.guild.Guild} object containing information about the guild
             *
             * @param id The id of the guild
             */
            public static String GET_GUILD = "/guilds/{guild.id}";

            /**
             * Read {@link DiscordJv#getGuilds()} for more information
             */
            public static String GET_CURRENT_USER_GUILDS = "/users/@me/guilds";

            public static class CHANNELS {
                /**
                 * Returns a list of {@link com.seailz.discordjv.model.channel.Channel} objects
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
                public static String GET_GUILD_ROLE = "/guilds/{guild.id}/role/{role.id}";
            }
        }

        public static class INTERACTIONS {
            public static String GET_ORIGINAL_INTERACTION_RESPONSE = "/webhooks/{application.id}/{interaction.token}/messages/@original";
            public static String GET_FOLLOWUP_MESSAGE = "/webhooks/{application.id}/{interaction.token}/messages/{message.id}";
        }

        public static class APPLICATIONS {
            /**
             * Returns an {@link com.seailz.discordjv.model.application.Application object containing information about the application of the bot selected}
             *
             * @param id the id of the bot
             */
            public static String GET_APPLICATION = "/applications/{bot.id}/rpc";
            /**
             * Returns a list of {@link com.seailz.discordjv.model.application.ApplicationRoleConnectionMetadata} objects containing information about the role connections the application has.
             * @param id the id of the app
             */
            public static String GET_APPLICATION_ROLE_CONNECTIONS = "/applications/{application.id}/role-connections/metadata";
        }

        public static class STICKER {
            /**
             * Returns a {@link com.seailz.discordjv.model.emoji.sticker.Sticker} object containing information about the sticker
             *
             * @param id The id of the sticker
             */
            public static String GET_STICKER = "/stickers/{sticker.id}";
            /**
             * Returns a list of {@link com.seailz.discordjv.model.emoji.sticker.StickerPack} objects containing information about the sticker packs Nitro users can use
             */
            public static String GET_NITRO_STICKER_PACKS = "/sticker-packs";
        }
    }

    public static class DELETE {
        public static class GUILD {
            /**
             * Leaves a guild
             *
             * @param id The id of the guild
             */
            public static String LEAVE_GUILD = "/users/@me/guilds/{guild.id}";

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
    }

    public static class PATCH {
        public static class GUILD {
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
    }

    public static class PUT {
        public static class APPLICATIONS {
            public static String MODIFY_APPLICATION_ROLE_CONNECTIONS = "/applications/{application.id}/role-connections/metadata";
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
        public static String BASE_URL = "wss://gateway.discord.gg/?v=" + APIVersion.getLatest().getCode() + "&encoding=json";
    }

}
