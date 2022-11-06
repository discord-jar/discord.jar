package com.seailz.javadiscordwrapper.model.interation;

import com.seailz.javadiscordwrapper.core.Compilerable;
import com.seailz.javadiscordwrapper.model.User;
import com.seailz.javadiscordwrapper.model.guild.Member;
import com.seailz.javadiscordwrapper.model.interation.utils.InteractionType;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

public record MessageInteraction(
        String id,
        InteractionType type,
        String name,
        User user,
        Member member
) implements Compilerable {

    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("type", type.getCode());
        obj.put("name", name);
        obj.put("user", user.compile());
        obj.put("member", member.compile());
        return obj;
    }

    @NonNull
    public static MessageInteraction decompile(JSONObject obj) {
        String id;
        InteractionType type;
        String name;
        User user;
        Member member;

        try {
            id = obj.getString("id");
        } catch (Exception e) {
            id = null;
        }

        try {
            type = InteractionType.fromCode(obj.getInt("type"));
        } catch (Exception e) {
            type = null;
        }

        try {
            name = obj.getString("name");
        } catch (Exception e) {
            name = null;
        }

        try {
            user = User.decompile(obj.getJSONObject("user"));
        } catch (Exception e) {
            user = null;
        }

        try {
            member = Member.decompile(obj.getJSONObject("member"));
        } catch (Exception e) {
            member = null;
        }
        return new MessageInteraction(id, type, name, user, member);
    }
}
