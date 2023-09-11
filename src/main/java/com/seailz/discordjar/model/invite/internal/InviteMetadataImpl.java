package com.seailz.discordjar.model.invite.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.application.Application;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.invite.InviteMetadata;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.model.ModelDecoder;
import org.json.JSONObject;

public class InviteMetadataImpl extends InviteImpl implements InviteMetadata {

    private final int uses;
    private final int maxUses;
    private final int maxAge;
    private final boolean temporary;
    private final String createdAt;

    public InviteMetadataImpl(String code, Guild guild, Channel channel, User inviter, VoiceInviteTargetType voiceInviteTarget, User targetUserStream, Application targetApplication, int approximatePresenceCount, int approximateMemberCount, String expiresAt, int uses, int maxUses, int maxAge, boolean temporary, String createdAt) {
        super(code, guild, channel, inviter, voiceInviteTarget, targetUserStream, targetApplication, approximatePresenceCount, approximateMemberCount, expiresAt);
        this.uses = uses;
        this.maxUses = maxUses;
        this.maxAge = maxAge;
        this.temporary = temporary;
        this.createdAt = createdAt;
    }

    @Override
    public int uses() {
        return uses;
    }

    @Override
    public int maxUses() {
        return maxUses;
    }

    @Override
    public int maxAge() {
        return maxAge;
    }

    @Override
    public boolean temporary() {
        return temporary;
    }

    @Override
    public String createdAt() {
        return createdAt;
    }

    public static InviteMetadataImpl decompile(JSONObject obj, DiscordJar discordJar) {
        String code = obj.has("code") ? obj.getString("code") : null;
        Guild guild = obj.has("guild") && obj.get("guild") != JSONObject.NULL ? Guild.decompile(obj.getJSONObject("guild"), discordJar) : null;
        Channel channel = obj.has("channel") && obj.get("channel") != JSONObject.NULL ? Channel.decompile(obj.getJSONObject("channel"), discordJar) : null;
        User inviter = obj.has("inviter") && obj.get("inviter") != JSONObject.NULL ? User.decompile(obj.getJSONObject("inviter"), discordJar) : null;
        VoiceInviteTargetType voiceInviteTarget = obj.has("target_type") ? VoiceInviteTargetType.fromCode(obj.getInt("target_type")) : null;
        User targetUserStream = obj.has("target_user") && obj.get("target_user") == JSONObject.NULL ? User.decompile(obj.getJSONObject("target_user"), discordJar) : null;
        Application targetApplication = obj.has("target_application") && obj.get("target_application") == JSONObject.NULL ? (Application) ModelDecoder.decodeObject(obj.getJSONObject("target_application"), Application.class, discordJar) : null;
        int approximatePresenceCount = obj.has("approximate_presence_count") ? obj.getInt("approximate_presence_count") : -1;
        int approximateMemberCount = obj.has("approximate_member_count") ? obj.getInt("approximate_member_count") : -1;
        String expiresAt = obj.has("expires_at") && obj.get("expires_at") != JSONObject.NULL ? obj.getString("expires_at") : null;
        int uses = obj.has("uses") ? obj.getInt("uses") : -1;
        int maxUses = obj.has("max_uses") ? obj.getInt("max_uses") : -1;
        int maxAge = obj.has("max_age") ? obj.getInt("max_age") : -1;
        boolean temporary = obj.has("temporary") && obj.getBoolean("temporary");
        String createdAt = obj.has("created_at") && obj.get("created_at") != JSONObject.NULL ? obj.getString("created_at") : null;
        return new InviteMetadataImpl(code, guild, channel, inviter, voiceInviteTarget, targetUserStream, targetApplication, approximatePresenceCount, approximateMemberCount, expiresAt, uses, maxUses, maxAge, temporary, createdAt);
    }

    @Override
    public JSONObject compile() {
        JSONObject json = new JSONObject();
        json.put("code", code());
        json.put("guild", guild().compile());
        json.put("channel", channel().compile());
        json.put("inviter", inviter().compile());
        json.put("target_type", voiceInviteTarget().getCode());
        json.put("target_user", targetUserStream().compile());
        json.put("target_application", targetApplication().compile());
        json.put("approximate_presence_count", approximatePresenceCount());
        json.put("approximate_member_count", approximateMemberCount());
        json.put("expires_at", expiresAt());
        json.put("uses", uses());
        json.put("max_uses", maxUses());
        json.put("max_age", maxAge());
        json.put("temporary", temporary());
        json.put("created_at", createdAt());
        return json;
    }
}
