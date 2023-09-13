package com.seailz.discordjar.model.guild.welcome;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.model.JSONProp;
import com.seailz.discordjar.utils.model.Model;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a welcome screen.
 *
 */
public class WelcomeScreen implements Model {
    @JSONProp("description")
    private String description;
    @JSONProp("welcome_channels")
    private List<WelcomeScreenChannel> welcomeChannels;

    private WelcomeScreen () {}

    public String description() {
        return description;
    }

    public List<WelcomeScreenChannel> welcomeChannels() {
        return welcomeChannels;
    }

}
