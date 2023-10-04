package com.seailz.discordjar.model.component.select;

import java.util.List;

/**
 * Represents a select menu that is auto populated, such as a {@link com.seailz.discordjar.model.component.select.entity.ChannelSelectMenu ChannelSelectMenu}.
 * @author Seailz
 * @see AutoPopulatedSelect
*/
public interface AutoPopulatedSelect extends SelectMenu {

    List<String> defaultValues();

}
