package com.seailz.discordjar.model.team.member;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.model.ModelDecoder;
import org.json.JSONObject;

public record TeamMember(MembershipState membershipState, String permissions, String teamId,
                         User user, Role role) implements Compilerable {
    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("membership_state", membershipState.getCode())
                .put("permissions", permissions)
                .put("team_id", teamId)
                .put("user", user.compile())
                .put("role", role.getValue());
    }

    public static TeamMember decompile(JSONObject obj, DiscordJar discordJar) {
        MembershipState membershipState;
        String permissions;
        String teamId;
        User user;
        Role role;

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
            user = (User) ModelDecoder.decodeObject(obj.getJSONObject("user"), User.class, discordJar);
        } catch (Exception e) {
            user = null;
        }

        role = obj.has("role") && !obj.isNull("role") ? Role.fromValue(obj.getString("role")) : null;


        return new TeamMember(membershipState, permissions, teamId, user, role);
    }

    public enum Role {
        OWNER("owner"),
        ADMIN("admin"),
        DEVELOPER("developer"),
        READ_ONLY("read_only"),
        UNKNOWN("unknown")
        ;

        private String value;

        Role(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Role fromValue(String value) {
            for (Role role : values()) {
                if (role.getValue().equals(value)) {
                    return role;
                }
            }
            return UNKNOWN;
        }

    }
}
