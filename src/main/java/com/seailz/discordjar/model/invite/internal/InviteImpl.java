package com.seailz.discordjar.model.invite.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.application.Application;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.invite.Invite;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.model.ModelDecoder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class InviteImpl implements Invite {

    private final String code;
    private final Guild guild;
    private final Channel channel;
    private final User inviter;
    private final VoiceInviteTargetType voiceInviteTarget;
    private final User targetUserStream;
    private final Application targetApplication;
    private final int approximatePresenceCount;
    private final int approximateMemberCount;
    private final String expiresAt;

    public InviteImpl(String code, Guild guild, Channel channel, User inviter, VoiceInviteTargetType voiceInviteTarget, User targetUserStream, Application targetApplication, int approximatePresenceCount, int approximateMemberCount, String expiresAt) {
        this.code = code;
        this.guild = guild;
        this.channel = channel;
        this.inviter = inviter;
        this.voiceInviteTarget = voiceInviteTarget;
        this.targetUserStream = targetUserStream;
        this.targetApplication = targetApplication;
        this.approximatePresenceCount = approximatePresenceCount;
        this.approximateMemberCount = approximateMemberCount;
        this.expiresAt = expiresAt;
    }

    public static InviteImpl decompile(JSONObject obj, DiscordJar discordJar) {
        String code = obj.has("code") ? obj.getString("code") : null;
        Guild guild = obj.has("guild") && obj.get("guild") != JSONObject.NULL ? Guild.decompile(obj.getJSONObject("guild"), discordJar) : null;
        Channel channel = obj.has("channel") && obj.get("channel") != JSONObject.NULL ? Channel.decompile(obj.getJSONObject("channel"), discordJar) : null;
        User inviter = obj.has("inviter") && obj.get("inviter") != JSONObject.NULL ? (User) ModelDecoder.decodeObject(obj.getJSONObject("inviter"), User.class, discordJar) : null;
        VoiceInviteTargetType voiceInviteTarget = obj.has("target_type") ? VoiceInviteTargetType.fromCode(obj.getInt("target_type")) : null;
        User targetUserStream = obj.has("target_user") && obj.get("target_user") == JSONObject.NULL ? (User) ModelDecoder.decodeObject(obj.getJSONObject("target_user"), User.class, discordJar) : null;
        Application targetApplication = obj.has("target_application") && obj.get("target_application") == JSONObject.NULL ? (Application) ModelDecoder.decodeObject(obj.getJSONObject("target_application"), Application.class, discordJar) : null;
        int approximatePresenceCount = obj.has("approximate_presence_count") ? obj.getInt("approximate_presence_count") : -1;
        int approximateMemberCount = obj.has("approximate_member_count") ? obj.getInt("approximate_member_count") : -1;
        String expiresAt = obj.has("expires_at") && obj.get("expires_at") != JSONObject.NULL ? obj.getString("expires_at") : null;
        return new InviteImpl(code, guild, channel, inviter, voiceInviteTarget, targetUserStream, targetApplication, approximatePresenceCount, approximateMemberCount, expiresAt);
    }

    @Override
    public JSONObject compile() {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("guild", guild.compile());
        json.put("channel", channel.compile());
        json.put("inviter", inviter.compile());
        json.put("target_type", voiceInviteTarget.getCode());
        json.put("target_user", targetUserStream.compile());
        json.put("target_application", targetApplication.compile());
        json.put("approximate_presence_count", approximatePresenceCount);
        json.put("approximate_member_count", approximateMemberCount);
        json.put("expires_at", expiresAt);
        return json;
    }

    @Override
    public @NotNull String code() {
        return code;
    }

    @Override
    public @Nullable Guild guild() {
        return guild;
    }

    @Override
    public Channel channel() {
        return channel;
    }

    @Override
    public @Nullable User inviter() {
        return inviter;
    }

    @Override
    public @Nullable VoiceInviteTargetType voiceInviteTarget() {
        return voiceInviteTarget;
    }

    @Override
    public @Nullable User targetUserStream() {
        return targetUserStream;
    }

    @Override
    public @Nullable Application targetApplication() {
        return targetApplication;
    }

    @Override
    public int approximatePresenceCount() {
        return approximatePresenceCount;
    }

    @Override
    public int approximateMemberCount() {
        return approximateMemberCount;
    }

    @Override
    public String expiresAt() {
        return expiresAt;
    }
}
