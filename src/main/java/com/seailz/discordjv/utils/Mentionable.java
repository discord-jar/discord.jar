package com.seailz.discordjv.utils;

/**
 * Represents a mentionable entity
 *
 * @author Seailz
 * @see com.seailz.discordjv.model.role.Role
 * @see com.seailz.discordjv.model.user.User
 * @since 1.0
 */
public interface Mentionable {

    String id();

    default String getAsMention() {
        return "<@" + id() + ">";
    }

}
