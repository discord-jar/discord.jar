package com.seailz.discordjv.utils;

/**
 * A class that contains a hash that can be used to retrieve
 * <br>an image from the Discord CDN.
 *
 * @author Seailz
 */
public interface CDNAble {

    StringFormatter formatter();

    default String imageUrl() {
        return formatter().format("https://cdn.discordapp.com/{0}/{1}/{2}.png");
    }

}
