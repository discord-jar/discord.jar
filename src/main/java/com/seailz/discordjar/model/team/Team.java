package com.seailz.discordjar.model.team;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.team.member.TeamMember;
import com.seailz.discordjar.utils.Snowflake;
import com.seailz.discordjar.utils.model.JSONProp;
import com.seailz.discordjar.utils.model.Model;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


public class Team implements Compilerable, Snowflake, Model {
    @JSONProp("icon")
    private String icon;
    @JSONProp("id")
    private String id;
    @JSONProp("name")
    private String name;
    @JSONProp("members")
    private List<TeamMember> members;
    @JSONProp("owner_user_id")
    private String ownerUserId;

    private Team() {}

    public String icon() {
        return icon;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public List<TeamMember> members() {
        return members;
    }

    public String ownerUserId() {
        return ownerUserId;
    }
    @Override
    public JSONObject compile() {
        JSONArray membersArray = new JSONArray();
        members.forEach(m -> {
            membersArray.put(m.compile());
        });
        return new JSONObject()
                .put("icon", icon)
                .put("id", id)
                .put("name", name)
                .put("members", membersArray.toString())
                .put("owner_user_id", ownerUserId);
    }
}
