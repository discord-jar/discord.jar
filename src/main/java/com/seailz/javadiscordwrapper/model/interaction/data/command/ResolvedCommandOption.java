package com.seailz.javadiscordwrapper.model.interaction.data.command;

import com.seailz.javadiscordwrapper.core.Compilerable;
import com.seailz.javadiscordwrapper.model.commands.CommandOptionType;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a resolved command option
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.model.interaction.data.command.ApplicationCommandInteractionData
 */
public class ResolvedCommandOption implements Compilerable {
    private String name;
    private JSONObject data;
    private CommandOptionType type;
    // Present if this option is a group or subcommand
    private List<ResolvedCommandOption> options;
    private boolean focused;

    public ResolvedCommandOption(String name, JSONObject data, CommandOptionType type, List<ResolvedCommandOption> options, boolean focused) {
        this.name = name;
        this.data = data;
        this.type = type;
        this.options = options;
        this.focused = focused;
    }

    @Override
    public JSONObject compile() {
        JSONArray options = new JSONArray();
        for (ResolvedCommandOption option : this.options)
            options.put(option.compile());

        return new JSONObject()
                .put("name", name)
                .put("value", data.get("value"))
                .put("type", type.getCode())
                .put("options", options)
                .put("focused", focused);
    }

    @NotNull
    public static ResolvedCommandOption decompile(JSONObject obj) {
        List<ResolvedCommandOption> options = new ArrayList<>();
        if (obj.has("options")) {
            JSONArray optionsArray = obj.getJSONArray("options");
            for (int i = 0; i < optionsArray.length(); i++) {
                options.add(decompile(optionsArray.getJSONObject(i)));
            }
        }

        return new ResolvedCommandOption(
                obj.getString("name"),
                obj.getJSONObject("value"),
                CommandOptionType.fromCode(obj.getInt("type")),
                options,
                obj.getBoolean("focused")
        );
    }

    public String name() {
        return name;
    }

    public String getAsString() {
        return data.getString("value");
    }

    public int getAsInt() {
        return data.getInt("value");
    }

    public boolean getAsBoolean() {
        return data.getBoolean("value");
    }

    public CommandOptionType type() {
        return type;
    }

    public List<ResolvedCommandOption> options() {
        return options;
    }

    public boolean focused() {
        return focused;
    }
}