package com.seailz.discordjar.model.team;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.team.member.TeamMember;
import com.seailz.discordjar.utils.Snowflake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public record Team(
        String icon,
        String id,
        String name,
        List<TeamMember> members,
        String ownerUserId
) implements Compilerable, Snowflake {
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

    public static Team decompile(JSONObject obj, DiscordJar discordJar) {
        String icon;
        String id;
        String name;
        List<TeamMember> members;
        String ownerUserId;

        try {
            icon = obj.getString("icon");
        } catch (Exception e) {
            icon = null;
        }

        try {
            id = obj.getString("id");
        } catch (Exception e) {
            id = null;
        }

        try {
            name = obj.getString("name");
        } catch (Exception e) {
            name = null;
        }

        try {
            members = obj.getJSONArray("members").toList().stream().map(o -> TeamMember.decompile(new JSONObject(o.toString()), discordJar)).toList();
        } catch (Exception e) {
            members = null;
        }

        try {
            ownerUserId = obj.getString("owner_user_id");
        } catch (Exception e) {
            ownerUserId = null;
        }
        return new Team(icon, id, name, members, ownerUserId);
    }
}
