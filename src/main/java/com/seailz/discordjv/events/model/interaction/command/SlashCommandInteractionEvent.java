package com.seailz.discordjv.events.model.interaction.command;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.interaction.data.command.ResolvedCommandOption;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

public class SlashCommandInteractionEvent extends CommandInteractionEvent {

    public SlashCommandInteractionEvent(@NotNull DiscordJv bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * Returns the options that were passed to the command.
     *
     * @return List of {@link ResolvedCommandOption} objects containing the options.
     */
    @NotNull
    public List<ResolvedCommandOption> getOptions() {
        return getCommandData().options();
    }

}
