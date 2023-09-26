package com.seailz.discordjar.model.component;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.component.button.Button;
import com.seailz.discordjar.model.component.button.internal.ButtonImpl;
import com.seailz.discordjar.model.component.impl.UnknownRawComponent;
import com.seailz.discordjar.model.component.select.SelectMenu;
import com.seailz.discordjar.model.component.select.entity.ChannelSelectMenu;
import com.seailz.discordjar.model.component.select.entity.RoleSelectMenu;
import com.seailz.discordjar.model.component.select.string.StringSelectMenu;
import com.seailz.discordjar.model.component.text.TextInput;
import org.json.JSONObject;

/**
 * Component which can be inserted into a {@link DisplayComponent}
 *
 * @author Seailz
 * @see com.seailz.discordjar.model.interaction.data.message.MessageComponentInteractionData
 * @since 1.0
 */
public interface RawComponent extends Component {

    static RawComponent unknown(JSONObject obj) {
        return UnknownRawComponent.of(obj);
    }

    /**
     * @return The max amount of this component that can be in a row
     */
    default int maxPerRow() {
        return type().getMaxPerRow();
    }

    JSONObject raw();

    void setRaw(JSONObject raw);

    default Button asButton(DiscordJar discordJar) {
        if (!type().equals(ComponentType.BUTTON))
            throw new ClassCastException("This component is not a button");
        return ButtonImpl.decompile(raw(), discordJar);
    }

    default SelectMenu asChannelSelectMenu() {
        if (!type().equals(ComponentType.CHANNEL_SELECT))
            throw new ClassCastException("This component is not a channel select menu");
        return ChannelSelectMenu.decompile(raw());
    }

    default SelectMenu asRoleSelectMenu() {
        if (!type().equals(ComponentType.ROLE_SELECT))
            throw new ClassCastException("This component is not a role select menu");
        return RoleSelectMenu.decompile(raw());
    }

    default SelectMenu asUserSelectMenu() {
        if (!type().equals(ComponentType.USER_SELECT))
            throw new ClassCastException("This component is not a user select menu");
        return RoleSelectMenu.decompile(raw());
    }

    default SelectMenu asMentionableSelectMenu() {
        if (!type().equals(ComponentType.MENTIONABLE_SELECT))
            throw new ClassCastException("This component is not a mentionable select menu");
        return RoleSelectMenu.decompile(raw());
    }

    default SelectMenu asStringSelectMenu(DiscordJar discordJar) {
        if (!type().equals(ComponentType.STRING_SELECT))
            throw new ClassCastException("This component is not a channel select menu");
        return StringSelectMenu.decompile(raw(), discordJar);
    }

    default TextInput asTextInput() {
        if (!type().equals(ComponentType.TEXT_INPUT))
            throw new ClassCastException("This component is not a text input");
        return TextInput.decompile(raw());
    }

    /**
     * @return If this component is message compatible
     */
    @Override
    default boolean isMessageCompatible() {
        return type().isMessageCompatible();
    }

    /**
     * @return If this component is modal compatible
     */
    @Override
    default boolean isModalCompatible() {
        return type().isModalCompatible();
    }

}
