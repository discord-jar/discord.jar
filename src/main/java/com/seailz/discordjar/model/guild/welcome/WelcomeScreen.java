package com.seailz.discordjar.model.guild.welcome;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a welcome screen.
 *
 * @param description     The description of the welcome screen
 * @param welcomeChannels The channels shown in the welcome screen
 */
public record WelcomeScreen(
        String description,
        WelcomeScreenChannel[] welcomeChannels
) implements Compilerable {

    public static WelcomeScreen decompile(JSONObject obj, DiscordJar discordJar) {
        List<WelcomeScreenChannel> welcomeChannels = new ArrayList<>();
        JSONArray welcomeChannelsArray = obj.getJSONArray("welcome_channels");
        for (int i = 0; i < welcomeChannelsArray.length(); i++) {
            welcomeChannels.add(WelcomeScreenChannel.decompile(welcomeChannelsArray.getJSONObject(i), discordJar));
        }

        return new WelcomeScreen(
                obj.getString("description"),
                welcomeChannels.toArray(new WelcomeScreenChannel[0])
        );
    }

    @Override
    public JSONObject compile() {
        JSONArray welcomeChannels = new JSONArray();
        for (WelcomeScreenChannel channel : welcomeChannels()) {
            welcomeChannels.put(channel.compile());
        }

        return new JSONObject()
                .put("description", description)
                .put("welcome_channels", welcomeChannels);
    }

}
