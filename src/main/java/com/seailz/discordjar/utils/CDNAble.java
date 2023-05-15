package com.seailz.discordjar.utils;

/**
 * A class that contains a hash that can be used to retrieve
 * <br>an image from the Discord CDN.
 *
 * @author Seailz
 */
public interface CDNAble {

    StringFormatter formatter();
    String iconHash();

    default String imageUrl() {
        if (iconHash() == null) return null;
        return formatter().format("https://cdn.discordapp.com/{0}/{1}/{2}.png");
    }

}
