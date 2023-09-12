package com.seailz.discordjar.model.team.member;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.model.JSONProp;
import com.seailz.discordjar.utils.model.ModelDecoder;
import org.json.JSONObject;

public class TeamMember implements Compilerable {
    @JSONProp("membership_state")
    private MembershipState membershipState;
    @JSONProp("permissions")
    private String permissions;
    @JSONProp("team_id")
    private String teamId;
    @JSONProp("user")
    private User user;
    @JSONProp("role")
    private Role role;

    public TeamMember(
            MembershipState membershipState,
            String permissions,
            String teamId,
            User user,
            Role role
    ) {
        this.membershipState = membershipState;
        this.permissions = permissions;
        this.teamId = teamId;
        this.user = user;
        this.role = role;
    }

    public MembershipState membershipState() {
        return membershipState;
    }

    public String permissions() {
        return permissions;
    }

    public String teamId() {
        return teamId;
    }

    public User user() {
        return user;
    }

    public Role role() {
        return role;
    }
    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("membership_state", membershipState.getCode())
                .put("permissions", permissions)
                .put("team_id", teamId)
                .put("user", user.compile())
                .put("role", role.getValue());
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
