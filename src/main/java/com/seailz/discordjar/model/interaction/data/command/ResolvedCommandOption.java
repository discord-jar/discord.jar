package com.seailz.discordjar.model.interaction.data.command;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.command.CommandOptionType;
import com.seailz.discordjar.model.interaction.data.ResolvedData;
import com.seailz.discordjar.model.message.Attachment;
import com.seailz.discordjar.model.role.Role;
import com.seailz.discordjar.model.user.User;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a resolved command option
 *
 * @author Seailz
 * @see com.seailz.discordjar.model.interaction.data.command.ApplicationCommandInteractionData
 * @since 1.0
 */
public class ResolvedCommandOption implements Compilerable {
    private final String name;
    private final Object data;
    private final CommandOptionType type;
    // Present if this option is a group or subcommand
    private final List<ResolvedCommandOption> options;
    private final boolean focused;
    private ResolvedData resolved;

    public ResolvedCommandOption(String name, Object data, CommandOptionType type, List<ResolvedCommandOption> options, boolean focused) {
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
                .put("value", data)
                .put("type", type.getCode())
                .put("options", options)
                .put("focused", focused);
    }

    @NotNull
    public static ResolvedCommandOption decompile(JSONObject obj, ResolvedData resolvedData) {
        List<ResolvedCommandOption> options = new ArrayList<>();
        if (obj.has("options")) {
            JSONArray optionsArray = obj.getJSONArray("options");
            for (int i = 0; i < optionsArray.length(); i++) {
                options.add(decompile(optionsArray.getJSONObject(i), resolvedData));
            }
        }

        Object value = obj.has("value") ? obj.get("value") : null;
        ResolvedCommandOption res = new ResolvedCommandOption(
                obj.getString("name"),
                value,
                CommandOptionType.fromCode(obj.getInt("type")),
                options,
                obj.has("focused") && obj.getBoolean("focused")
        );
        res.resolved = resolvedData;
        return res;
    }

    public String name() {
        return name;
    }

    public String getAsString() {
        return (String) data;
    }

    public int getAsInt() {
        return (int) data;
    }

    public boolean getAsBoolean() {
        return (boolean) data;
    }
    public double getAsDouble() {
        BigDecimal bd = (BigDecimal) data;
        return bd.doubleValue();
    }

    /**
     * Returns a raw version of the data of the option.
     * <br>This can be used to case it to whatever value it needs to be used as.
     */
    public Object getData() {
        return data;
    }

    public Role getAsRole() {
        return this.resolved.roles().get(getAsString());
    }
    public User getAsUser() {
        return this.resolved.users().get(getAsString());
    }
    public Attachment getAsAttachment() {
        return this.resolved.attachments().get(getAsString());
    }

    public CommandOptionType type() {
        return type;
    }

    public List<ResolvedCommandOption> options() {
        return options;
    }

    public ResolvedData resolved() {
        return resolved;
    }

    public boolean focused() {
        return focused;
    }
}
