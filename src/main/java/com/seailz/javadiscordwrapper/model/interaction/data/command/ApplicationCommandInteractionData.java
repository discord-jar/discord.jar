package com.seailz.javadiscordwrapper.model.interaction.data.command;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.model.guild.Guild;
import com.seailz.javadiscordwrapper.model.interaction.InteractionData;
import com.seailz.javadiscordwrapper.model.interaction.data.ResolvedData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Represents the data of an application command interaction
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.model.interaction.Interaction
 */
public class ApplicationCommandInteractionData extends InteractionData {

    private String id;
    private String name;
    private ResolvedData resolved;
    private List<ResolvedCommandOption> options;
    private Guild guild;
    private int targetId;

    /**
     * @param id The ID of the invoked command
     * @param name The name of the invoked command
     * @param resolved The resolved data
     * @param options The params + values from the user
     * @param guild The guild it was sent from
     * @param targetId The id of the target
     */
    public ApplicationCommandInteractionData(String id, String name, ResolvedData resolved, List<ResolvedCommandOption> options, Guild guild, int targetId) {
        this.id = id;
        this.name = name;
        this.resolved = resolved;
        this.options = options;
        this.guild = guild;
        this.targetId = targetId;
    }

    public ApplicationCommandInteractionData(JSONObject obj, DiscordJv discordJv) {
        id = obj.getString("id");
        name = obj.getString("name");
        resolved = ResolvedData.decompile(obj.getJSONObject("resolved"));

        JSONArray optionsArray = obj.getJSONArray("options");
        for (int i = 0; i < optionsArray.length(); i++) {
            options.add(ResolvedCommandOption.decompile(optionsArray.getJSONObject(i)));
        }

        guild = Guild.decompile(obj.getJSONObject("guild_id"), discordJv);
        targetId = obj.getInt("target_id");
    }





}
