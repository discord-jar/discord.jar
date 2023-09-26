package com.seailz.discordjar.events.model.interaction.command;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.interaction.data.command.ResolvedCommandOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SlashCommandInteractionEvent extends CommandInteractionEvent {

    public SlashCommandInteractionEvent(@NotNull DiscordJar bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * Returns the options that were passed to the command.
     *
     * @return List of {@link ResolvedCommandOption} objects containing the options.
     */
    @Nullable
    public List<ResolvedCommandOption> getOptions() {
        if (!getJson().getJSONObject("d").getJSONObject("data").has("options")) return null;
        JSONArray options = getJson().getJSONObject("d").getJSONObject("data").getJSONArray("options");

        try {
            options = options.getJSONObject(0).getJSONArray("options");
        } catch (JSONException ignored) {
        }

        List<ResolvedCommandOption> decompiled = new ArrayList<>();
        for (Object option : options) {
            decompiled.add(ResolvedCommandOption.decompile((JSONObject) option, getCommandData().resolved()));
        }
        return decompiled;
    }

    /**
     * This is an internal method for the library. It is not recommended to use this method.
     * As such, you will not be given support if you use this method.
     *
     * @return List of {@link ResolvedCommandOption} objects that were passed to the top level command, including sub commands.
     */
    public List<ResolvedCommandOption> getOptionsInternal() {
        if (!getJson().getJSONObject("d").getJSONObject("data").has("options")) return null;
        JSONArray options = getJson().getJSONObject("d").getJSONObject("data").getJSONArray("options");

        List<ResolvedCommandOption> decompiled = new ArrayList<>();
        for (Object option : options) {
            decompiled.add(ResolvedCommandOption.decompile((JSONObject) option, getCommandData().resolved()));
        }
        return decompiled;
    }

    /**
     * Returns the option with the given name.
     *
     * @param name The name of the option.
     * @return The {@link ResolvedCommandOption} object with the given name.
     */
    public ResolvedCommandOption getOption(String name) {
        if (getOptions() == null) return null;
        return getOptions().stream().filter(option -> option.name().equals(name)).findFirst().orElse(null);
    }

}
