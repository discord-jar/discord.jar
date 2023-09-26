package com.seailz.discordjar.utils.image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;

public class ImageUtils {

    public static String generateImageData(File file) {
        String fileType = null;
        try {
            fileType = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(fileType);
        return "data:" + fileType + ";base64," + Base64.getEncoder().encodeToString(getFileBytes(file));
    }

    private static byte[] getFileBytes(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUrl(String hash, ImageType type, String... params) {
        String urlWithBase = "https://cdn.discordapp.com/" + type.getUrl();
        String paramable = urlWithBase.replaceAll("%h", hash);

        Object[] paramsList = Arrays.stream(params).toArray();
        return String.format(paramable, paramsList);
    }

    public enum ImageType {
        CUSTOM_EMOJI("emojis/%h"),
        GUILD_ICON("icons/%s/%h"),
        GUILD_SPLASH("splashes/%s/%h"),
        GUILD_DISCOVERY_SPLASH("discovery-splashes/%s/%h"),
        GUILD_BANNER("banners/%s/%h"),
        USER_BANNER("banners/%s/%h"),
        DEFAULT_USER_AVATAR("embed/avatars/%h"),
        USER_AVATAR("avatars/%s/%h"),
        GUILD_MEMBER_AVATAR("guilds/%s/users/%s/avatars/%h"),
        USER_AVATAR_DECORATION("avatar-decorations/%s/%h"),
        APPLICATION_ICON("app-icons/%s/%h"),
        APPLICATION_COVER("app-icons/%s/%h"),
        APPLICATION_ASSET("app-assets/%s/%h"),
        ACHIEVEMENT_ICON("app-assets/%s/achievements/%s/icons/%h"),
        STICKER_PACK_BANNER("app-assets/710982414301790216/store/%s"),
        TEAM_ICON("team-icons/%s/%h"),
        STICKER("stickers/%s"),
        ROLE_ICON("role-icons/%s/%h"),
        GUILD_SCHEDULED_EVENT_COVER("guild-events/%s/%h"),
        GUILD_MEMBER_BANNER("guild/%s/users/%s/banners/%h"),
        DM_ICON("channel-icons/%s/%h");

        private final String url;

        ImageType(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

}
