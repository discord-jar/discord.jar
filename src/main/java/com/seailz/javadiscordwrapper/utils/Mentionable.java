package com.seailz.javadiscordwrapper.utils;

/**
 * Represents a mentionable entity
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.model.role.Role
 * @see    com.seailz.javadiscordwrapper.model.user.User
 */
public interface Mentionable {

    String id();

    default String getAsMention() {
        return "<@" + id() + ">";
    }

}
