package com.seailz.discordjar;

import com.seailz.discordjar.model.application.Intent;
import com.seailz.discordjar.utils.HTTPOnlyInfo;
import com.seailz.discordjar.utils.version.APIVersion;

import java.util.EnumSet;
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
        if (this.intents == null) this.intents = EnumSet.noneOf(Intent.class);
        this.intents.remove(Intent.ALL);
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

    @SuppressWarnings("deprecation") // Deprecation warning is suppressed here because the intended use of that constructor is here.
    public DiscordJar build() throws ExecutionException, InterruptedException {
        if (intents == null) defaultIntents();
        if (httpOnly && httpOnlyInfo == null) throw new IllegalStateException("HTTPOnly is enabled but no HTTPOnlyInfo was provided.");
        return new DiscordJar(token, intents, apiVersion, httpOnly, httpOnlyInfo, debug);
    }




}
