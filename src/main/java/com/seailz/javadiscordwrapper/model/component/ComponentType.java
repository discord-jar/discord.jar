package com.seailz.javadiscordwrapper.model.component;

import com.seailz.javadiscordwrapper.model.component.button.ButtonImpl;
import com.seailz.javadiscordwrapper.model.component.select.entity.ChannelSelectMenu;
import com.seailz.javadiscordwrapper.model.component.select.entity.MentionableSelectMenu;
import com.seailz.javadiscordwrapper.model.component.select.entity.RoleSelectMenu;
import com.seailz.javadiscordwrapper.model.component.select.entity.UserSelectMenu;
import com.seailz.javadiscordwrapper.model.component.select.string.StringSelectMenu;

/**
 * Represents the type of a component
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.model.interaction.data.message.MessageComponentInteractionData
 */
public enum ComponentType {

    /** A container for other components */
    ACTION_ROW(1, 0, false, true, false, ActionRow.class),
    /* A button */
    BUTTON(2, 5, false, true, false, ButtonImpl.class) ,
    /* A select menu */
    STRING_SELECT(3, 1, true, true, false, StringSelectMenu.class),
    /* A text input */
    TEXT_INPUT(4, 1, false, false, true, null),
    /* A user select menu */
    USER_SELECT(5, 1, true, true, false, UserSelectMenu.class),
    /* A role select menu */
    ROLE_SELECT(6, 1, true, true, false, RoleSelectMenu.class),
    /* A mentionable select menu */
    MENTIONABLE_SELECT(7, 1, true, true, false, MentionableSelectMenu.class),
    /* A channel select menu */
    CHANNEL_SELECT(8, 1, true, true, false, ChannelSelectMenu.class),
    /* Unknown */
    UNKNOWN(-1, -1, false, false, false, null);

    ;

    private final int code;
    private final int maxPerRow;
    private final boolean isSelect;
    private final boolean isMessageCompatible;
    private final boolean isModalCompatible;
    private final Class<? extends Component> clazz;

    ComponentType(int code, int maxPerRow, boolean isSelect, boolean isMessageCompatible, boolean isModalCompatible, Class<? extends Component> clazz) {
        this.code = code;
        this.maxPerRow = maxPerRow;
        this.isSelect = isSelect;
        this.isMessageCompatible = isMessageCompatible;
        this.isModalCompatible = isModalCompatible;
        this.clazz = clazz;
    }

    /**
     * The code representing the component type
     *
     * @return An integer representing the component type
     */
    public int getCode() {
        return code;
    }

    /**
     * The maximum amount of components that can fit in an ActionRow
     *
     * @return An integer representing the maximum amount of components that can fit in an ActionRow
     */
    public int getMaxPerRow() {
        return maxPerRow;
    }

    /**
     * Whether the component is a {@link com.seailz.javadiscordwrapper.model.component.select.SelectMenu SelectMenu}
     *
     * @return A boolean representing whether the component is a select menu
     */
    public boolean isSelect() {
        return isSelect;
    }

    /**
     * Whether a component can be used in a {@link com.seailz.javadiscordwrapper.model.message.Message}
     *
     * @return If a component can be used in Messages
     */
    public boolean isMessageCompatible() {
        return isMessageCompatible;
    }

    /**
     * Whether a component can be used in a Modal TODO: Add link to Modal
     *
     * @return If a component can be used in Modals
     */
    public boolean isModalCompatible() {
        return isModalCompatible;
    }

    /**
     * The class of the component
     *
     * @return The class of the component
     */
    public Class<? extends Component> getClazz() {
        return clazz;
    }

    /**
     * Returns a component type based on the code
     *
     * @param code
     *        The code to get the component type from
     *
     * @return The component type
     */
    public static ComponentType getType(int code) {
        for(ComponentType type : values()) {
            if(type.getCode() == code)
                return type;
        }
        return UNKNOWN;
    }

}
