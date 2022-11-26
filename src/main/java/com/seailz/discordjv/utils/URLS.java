package com.seailz.discordjv.utils;

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
        }

        public static class COMMANDS {

            /**
             * Endpoint for global commands.
             * @param {application.id} The ID of the application to add a global command to
             */
            public static final String GLOBAL_COMMANDS = "/applications/{application.id}/commands";
            /**
             * Endpoint for guild commands.
             * @param {application.id} The ID of the application to add a guild command to
             * @param {guild.id} The guild to add the command to
             */
            public static final String GUILD_COMMANDS = "/applications/{application.id}/guilds/{guild.id}/commands";
        }

        public static class MESSAGES {
            public static final String SEND = "/channels/{channel.id}/messages";
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
        }

        public static class APPLICATIONS {
            /**
             * Returns an {@link com.seailz.discordjv.model.application.Application object containing information about the application of the bot selected}
             *
             * @param id the id of the bot
             */
            public static String GET_APPLICATION = "/applications/{bot.id}/rpc";
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
    }

    public static class PATCH {
        public static class GUILD {
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

    public static class GATEWAY {
        public static String BASE_URL = "wss://gateway.discord.gg/?v=" + APIVersion.getLatest().getCode() + "&encoding=json";
    }

}
