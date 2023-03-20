package com.seailz.discordjar.action.channel.invites;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.invite.Invite;
import com.seailz.discordjar.model.invite.internal.InviteImpl;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class CreateChannelInviteAction {

    private final DiscordJar discordJar;
    private final String channelId;

    // Defaults to 86400 (24 hours)
    private int maxAge = -1;
    // Defaults to 0 (unlimited)
    private int maxUses = -1;
    private boolean temporary;
    private boolean unique;
    private Invite.VoiceInviteTargetType targetType;
    private String targetUser;
    private String targetApplication;

    public CreateChannelInviteAction(DiscordJar discordJar, String channelId) {
        this.discordJar = discordJar;
        this.channelId = channelId;
    }

    /**
     * Duration of invite in seconds before expiry, or 0 for never. Defaults to 86400 (24 hours).
     * This must be between 0 and 604800 (7 days).
     */
    public CreateChannelInviteAction setMaxAge(int maxAge) {
        if (maxAge > 604800 || maxAge < 0)
            throw new IllegalArgumentException("Max age must be between 0 and 604800 (7 days)");
        this.maxAge = maxAge;
        return this;
    }

    /**
     * Max number of times this invite can be used, or 0 for unlimited. Defaults to 0.
     * Must be between 0 and 100.
     */
    public CreateChannelInviteAction setMaxUses(int maxUses) {
        if (maxUses > 100 || maxUses < 0)
            throw new IllegalArgumentException("Max uses must be between 0 and 100");
        this.maxUses = maxUses;
        return this;
    }

    /**
     * The ID of the embedded application to open for this voice channel invite.
     * <br>This is required if {@link #targetType} is set to {@link Invite.VoiceInviteTargetType#EMBEDDED_APPLICATION}.
     *
     * <p>The application must have the {@code EMBEDDED} flag in its flags field.
     */
    public CreateChannelInviteAction setTargetApplication(String targetApplication) {
        this.targetApplication = targetApplication;
        return this;
    }

    /**
     * If this is a voice channel invite, this sets the type of target for this voice channel invite.
     */
    public CreateChannelInviteAction setTargetType(Invite.VoiceInviteTargetType targetType) {
        this.targetType = targetType;
        return this;
    }

    /**
     * If this is a voice channel invite and {@link #targetType} is set to {@link Invite.VoiceInviteTargetType#STREAM},
     * <br>then this sets the user whose stream to display for this invite.
     * <p>
     *     The user must be streaming in the channel.
     * </p>
     */
    public CreateChannelInviteAction setTargetUser(String targetUser) {
        this.targetUser = targetUser;
        return this;
    }

    /**
     * Whether this invite only grants temporary membership. Defaults to false.
     */
    public CreateChannelInviteAction setTemporary(boolean temporary) {
        this.temporary = temporary;
        return this;
    }

    /**
     * Whether this invite should be unique. If true, don't try to reuse a similar invite (useful for creating many unique one time use invites).
     * Defaults to false.
     */
    public CreateChannelInviteAction setUnique(boolean unique) {
        this.unique = unique;
        return this;
    }

    public CompletableFuture<Invite> run() {
        CompletableFuture<Invite> future = new CompletableFuture<>();
        future.completeAsync(() -> {
            if (targetType == Invite.VoiceInviteTargetType.STREAM && targetUser == null)
                throw new IllegalStateException("Target user must be set if target type is set to STREAM");

            if (targetType == Invite.VoiceInviteTargetType.EMBEDDED_APPLICATION && targetApplication == null)
                throw new IllegalStateException("Target application must be set if target type is set to EMBEDDED_APPLICATION");


            JSONObject body = new JSONObject();
            if (maxAge != -1) body.put("max_age", maxAge);
            if (maxUses != -1) body.put("max_uses", maxUses);
            if (temporary) body.put("temporary", true);
            if (unique) body.put("unique", true);
            if (targetType != null) body.put("target_type", targetType.getCode());
            if (targetUser != null) body.put("target_user_id", targetUser);
            if (targetApplication != null) body.put("target_application_id", targetApplication);

            DiscordResponse res;
            try {
                res = new DiscordRequest(
                        body,
                        new HashMap<>(),
                        URLS.POST.CHANNELS.CREATE_CHANNEL_INVITE.replace("{channel.id}", channelId),
                        discordJar,
                        URLS.POST.CHANNELS.CREATE_CHANNEL_INVITE,
                        RequestMethod.POST
                ).invoke();
            } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
                future.completeExceptionally(e);
                return null;
            }

            return InviteImpl.decompile(res.body(), discordJar);
        });
        return future;
    }

}
