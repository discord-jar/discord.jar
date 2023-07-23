package com.seailz.discordjar.model.team.member;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.json.SJSONObject;

public record TeamMember(MembershipState membershipState, String permissions, String teamId,
                         User user) implements Compilerable {
    @Override
    public SJSONObject compile() {
        return new SJSONObject()
                .put("membership_state", membershipState.getCode())
                .put("permissions", permissions)
                .put("team_id", teamId)
                .put("user", user.compile());
    }

    public static TeamMember decompile(SJSONObject obj, DiscordJar discordJar) {
        MembershipState membershipState;
        String permissions;
        String teamId;
        User user;

        try {
            membershipState = MembershipState.fromCode(obj.getInt("membership_state"));
        } catch (Exception e) {
            membershipState = null;
        }

        try {
            permissions = obj.getString("permissions");
        } catch (Exception e) {
            permissions = null;
        }

        try {
            teamId = obj.getString("team_id");
        } catch (Exception e) {
            teamId = null;
        }

        try {
            user = User.decompile(obj.getJSONObject("user"), discordJar);
        } catch (Exception e) {
            user = null;
        }

        return new TeamMember(membershipState, permissions, teamId, user);
    }
}
