package com.seailz.javadiscordwrapper.utils;

import com.seailz.javadiscordwrapper.utils.version.APIVersion;

public final class URLS {

    public static final String BASE_URL = "https://discord.com/api/v" + APIVersion.getLatest().getCode();

    public static class POST {
        public static class INTERACTIONS {
            public static class COMMANDS {
            }
        }

        public static class MESSAGES {
            public static final String SEND = "/channels/{channel.id}/messages";
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
             * @param id The id of the user
             */
            public static String GET_USER = "/users/{user.id}";
        }

        public static class CHANNELS {
            /**
             * Returns info about a channel
             * @param id The id of the channel
             */
            public static String GET_CHANNEL = "/channels/{channel.id}";
            /**
             * Returns a message
             * @param id The id of the channel
             * @param message.id The id of the message
             */
            public static String GET_MESSAGE = "/channels/{channel.id}/messages/{message.id}";
        }

        public static class GUILDS {
            /**
             * Returns a {@link com.seailz.javadiscordwrapper.model.guild.Guild} object containing information about the guild
             * @param id The id of the guild
             */
            public static String GET_GUILD = "/guilds/{guild.id}";
        }

        public static class APPLICATIONS {
            /**
             * Returns an {@link com.seailz.javadiscordwrapper.model.application.Application object containing information about the application of the bot selected}
             * @param id the id of the bot
             */
            public static String GET_APPLICATION = "/applications/{bot.id}/rpc";
        }
    }

    public static class GATEWAY {
        public static String BASE_URL = "wss://gateway.discord.gg/?v=" + APIVersion.getLatest().getCode() + "&encoding=json";
    }

}
