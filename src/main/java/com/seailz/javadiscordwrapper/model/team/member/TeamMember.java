package com.seailz.javadiscordwrapper.model.team.member;

import com.seailz.javadiscordwrapper.core.Compilerable;
import com.seailz.javadiscordwrapper.model.user.User;
import org.json.JSONObject;

public record TeamMember(MembershipState membershipState, String permissions, String teamId, User user) implements Compilerable {
    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("membership_state", membershipState.getCode())
                .put("permissions", permissions)
                .put("team_id", teamId)
                .put("user", user.compile());
    }

    public static TeamMember decompile(JSONObject obj) {
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
            user = User.decompile(obj.getJSONObject("user"));
        } catch (Exception e) {
            user = null;
        }

        return new TeamMember(membershipState, permissions, teamId, user);
    }
}
