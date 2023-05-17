package com.seailz.discordjar;

import com.seailz.discordjar.model.application.Intent;
import com.seailz.discordjar.utils.HTTPOnlyInfo;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import com.seailz.discordjar.utils.version.APIVersion;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Factory class for creating a {@link DiscordJar} instance.
 *
 * @author Seailz
 * @since 1.0.0
 */
public class DiscordJarBuilder {

    private final String token;
    private EnumSet<Intent> intents;
    private APIVersion apiVersion = APIVersion.getLatest();
    private boolean httpOnly;
    private HTTPOnlyInfo httpOnlyInfo;
    private boolean debug;
    private int shardId = -1;
    private int numShards = 1;

    public DiscordJarBuilder(String token) {
        this.token = token;
    }

    public DiscordJarBuilder setIntents(EnumSet<Intent> intents) {
        this.intents = intents;
        return this;
    }

    /**
     * Resets back to default intents.
     */
    public DiscordJarBuilder defaultIntents() {
        if (this.intents == null) this.intents = EnumSet.noneOf(Intent.class);
        this.intents.clear();
        this.intents.add(Intent.ALL);
        return this;
    }

    public DiscordJarBuilder addIntents(Intent... intents) {
        for (Intent intent : intents) {
            addIntent(intent);
        }
        return this;
    }

    public DiscordJarBuilder addIntent(Intent intent) {
        if (this.intents == null) {
            this.intents = EnumSet.noneOf(Intent.class);
            intents.add(Intent.ALL);
        };
        this.intents.add(intent);
        return this;
    }

    public DiscordJarBuilder removeIntent(Intent intent) {
        if (this.intents == null) return this;
        this.intents.remove(intent);
        return this;
    }

    public DiscordJarBuilder setAPIVersion(APIVersion apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    public DiscordJarBuilder setHTTPOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    public DiscordJarBuilder setHTTPOnlyInfo(HTTPOnlyInfo httpOnlyInfo) {
        this.httpOnlyInfo = httpOnlyInfo;
        return this;
    }

    public DiscordJarBuilder setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public DiscordJarBuilder setShardId(int shardId) {
        this.shardId = shardId;
        return this;
    }

    public DiscordJarBuilder setNumShards(int numShards) {
        this.numShards = numShards;
        return this;
    }

    public int getRecommendedShardCount() {
        DiscordRequest req = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.GATEWAY.GET_GATEWAY_BOT,
                null,
                URLS.GET.GATEWAY.GET_GATEWAY_BOT,
                RequestMethod.GET
        );

        DiscordResponse res = req.invokeNoDiscordJar(token);
        if (res == null) return -1;
        if (res.code() != 200) return -1;

        return res.body().getInt("shards");
    }

    @SuppressWarnings("deprecation") // Deprecation warning is suppressed here because the intended use of that constructor is here.
    public DiscordJar build() throws ExecutionException, InterruptedException {
        if (intents == null) defaultIntents();
        if (httpOnly && httpOnlyInfo == null) throw new IllegalStateException("HTTPOnly is enabled but no HTTPOnlyInfo was provided.");
        return new DiscordJar(token, intents, apiVersion, httpOnly, httpOnlyInfo, debug, shardId, numShards);
    }




}
