package com.seailz.discordjar.action.guild.onboarding;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import com.seailz.discordjar.utils.rest.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;

public class ModifyOnboardingGuild {

    private final Guild guild;
    private final DiscordJar discordJar;
    private List<Guild.Onboarding.Prompt> prompts;
    private List<String> defaultChannelIds;
    private int enabled = -1;
    private Guild.Onboarding.Mode mode;

    public ModifyOnboardingGuild(Guild guild, DiscordJar discordJar) {
        this.guild = guild;
        this.discordJar = discordJar;
    }

    public ModifyOnboardingGuild setDefaultChannelIds(List<String> defaultChannelIds) {
        this.defaultChannelIds = defaultChannelIds;
        return this;
    }

    public ModifyOnboardingGuild setEnabled(boolean enabled) {
        this.enabled = enabled ? 1 : 0;
        return this;
    }

    public ModifyOnboardingGuild setMode(Guild.Onboarding.Mode mode) {
        this.mode = mode;
        return this;
    }

    public ModifyOnboardingGuild setPrompts(List<Guild.Onboarding.Prompt> prompts) {
        this.prompts = prompts;
        return this;
    }

    public Response<Guild.Onboarding> run() {
        Response<Guild.Onboarding> res = new Response<>();
        new Thread(() -> {
            JSONObject body = new JSONObject();
            if (prompts != null) {
                JSONArray promptsJson = new JSONArray();
                for (Guild.Onboarding.Prompt prompt : prompts) {
                    promptsJson.put(prompt.compile());
                }
                body.put("prompts", promptsJson);
            }

            if (defaultChannelIds != null) {
                JSONArray defaultChannelIdsJson = new JSONArray();
                for (String defaultChannelId : defaultChannelIds) {
                    defaultChannelIdsJson.put(defaultChannelId);
                }
                body.put("default_channel_ids", defaultChannelIdsJson);
            }

            if (enabled != -1) {
                body.put("enabled", enabled == 1);
            }

            if (mode != null) {
                body.put("mode", mode.value());
            }

            DiscordRequest req = new DiscordRequest(
                    body,
                    new HashMap<>(),
                    URLS.PUT.GUILD.MODIFY_GUILD_ONBOARDING
                            .replace("{guild.id}", guild.id()),
                    discordJar,
                    URLS.PUT.GUILD.MODIFY_GUILD_ONBOARDING,
                    RequestMethod.PUT
            );

            try {
                DiscordResponse response = req.invoke();
                res.complete(Guild.Onboarding.decompile(response.body(), guild, discordJar));
            } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
                res.completeError(new Response.Error(
                        e.getCode(),
                        e.getMessage(),
                        e.getBody()
                ));
            }
        }, "djar--onboarding-modify").start();
        return res;
    }
}
