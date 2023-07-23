package com.seailz.discordjar.model.guild.welcome;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.json.SJSONArray;
import com.seailz.discordjar.utils.json.SJSONObject;

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

    @Override
    public SJSONObject compile() {
        SJSONArray welcomeChannels = new SJSONArray();
        for (WelcomeScreenChannel channel : welcomeChannels()) {
            welcomeChannels.put(channel.compile());
        }

        return new SJSONObject()
                .put("description", description)
                .put("welcome_channels", welcomeChannels);
    }

    public static WelcomeScreen decompile(SJSONObject obj, DiscordJar discordJar) {
        List<WelcomeScreenChannel> welcomeChannels = new ArrayList<>();
        SJSONArray welcomeChannelsArray = obj.getJSONArray("welcome_channels");
        for (int i = 0; i < welcomeChannelsArray.length(); i++) {
            welcomeChannels.add(WelcomeScreenChannel.decompile(welcomeChannelsArray.getJSONObject(i), discordJar));
        }

        return new WelcomeScreen(
                obj.getString("description"),
                welcomeChannels.toArray(new WelcomeScreenChannel[0])
        );
    }

}
