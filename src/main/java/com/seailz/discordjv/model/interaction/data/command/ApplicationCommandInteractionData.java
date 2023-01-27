package com.seailz.discordjv.model.interaction.data.command;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.interaction.InteractionData;
import com.seailz.discordjv.model.interaction.data.ResolvedData;
import com.seailz.discordjv.utils.Snowflake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Represents the data of an application command interaction
 *
 * @author Seailz
 * @see com.seailz.discordjv.model.interaction.Interaction
 * @since 1.0
 */
public class ApplicationCommandInteractionData extends InteractionData implements Snowflake {

    private final String id;
    private final String name;
    private final ResolvedData resolved;
    private List<ResolvedCommandOption> options;
    private final Guild guild;
    private final String targetId;

    /**
     * @param id       The ID of the invoked command
     * @param name     The name of the invoked command
     * @param resolved The resolved data
     * @param options  The params + values from the user
     * @param guild    The guild it was sent from
     * @param targetId The id of the target
     */
    public ApplicationCommandInteractionData(String id, String name, ResolvedData resolved, List<ResolvedCommandOption> options, Guild guild, String targetId) {
        this.id = id;
        this.name = name;
        this.resolved = resolved;
        this.options = options;
        this.guild = guild;
        this.targetId = targetId;
    }

    public ApplicationCommandInteractionData(JSONObject obj, DiscordJv discordJv) {
        id = obj.has("id") ? obj.getString("id") : null;
        name = obj.has("name") ? obj.getString("name") : null;
        resolved = obj.has("resolved") ? ResolvedData.decompile(obj.getJSONObject("resolved"), discordJv) : null;

        if (obj.has("options") && this.options != null) {
            JSONArray optionsArray = obj.getJSONArray("options");
            for (int i = 0; i < optionsArray.length(); i++) {
                options.add(ResolvedCommandOption.decompile(optionsArray.getJSONObject(i), resolved));
            }
        }

        guild = obj.has("guild_id") ? discordJv.getGuildCache().getById(obj.getString("guild_id")) : null;
        targetId = obj.has("target_id") ? obj.getString("target_id") : null;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public ResolvedData resolved() {
        return resolved;
    }

    public List<ResolvedCommandOption> options() {
        return options;
    }

    public Guild guild() {
        return guild;
    }

    public String targetId() {
        return targetId;
    }

    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        JSONArray options = new JSONArray();
        for (ResolvedCommandOption option : this.options) {
            options.put(option.compile());
        }

        obj.put("id", id);
        obj.put("name", name);
        // obj.put("resolved", resolved.compile());
        obj.put("options", options);
        obj.put("guild_id", guild.id());
        obj.put("target_id", targetId);

        return obj;
    }

}
