package com.seailz.javadiscordwrapper.model.interaction.data;

import com.seailz.javadiscordwrapper.core.Compilerable;
import com.seailz.javadiscordwrapper.model.channel.Channel;
import com.seailz.javadiscordwrapper.model.guild.Member;
import com.seailz.javadiscordwrapper.model.message.Attachment;
import com.seailz.javadiscordwrapper.model.message.Message;
import com.seailz.javadiscordwrapper.model.resolve.Resolvable;
import com.seailz.javadiscordwrapper.model.role.Role;
import com.seailz.javadiscordwrapper.model.user.User;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents resolved data from an interaction
 *
 * @param users A map of user ids to users
 * @param members A map of member ids to members
 * @param roles A map of role ids to roles
 * @param channels A map of channel ids to channels
 * @param messages A map of message ids to messages
 * @param attachments A map of attachment ids to attachments
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.model.interaction.Interaction
 */
public record ResolvedData(
        HashMap<String, User> users,
        HashMap<String, Member> members,
        HashMap<String, Role> roles,
        HashMap<String, Channel> channels,
        HashMap<String, Message> messages,
        HashMap<String, Attachment> attachments
) implements Compilerable {

    @Override
    public JSONObject compile() {
        HashMap<String, Compilerable> users;
        HashMap<String, Compilerable> members;
        HashMap<String, Compilerable> roles;
        HashMap<String, Compilerable> channels;
        HashMap<String, Compilerable> messages;
        HashMap<String, Compilerable> attachments;

        if (this.users != null)
            users = new HashMap<>(this.users);
        else
            users = null;

        if (this.members != null)
            members = new HashMap<>(this.members);
        else
            members = null;

        if (this.roles != null)
            roles = new HashMap<>(this.roles);
        else
            roles = null;

        if (this.channels != null)
            channels = new HashMap<>(this.channels);
        else
            channels = null;

        if (this.messages != null)
            messages = new HashMap<>(this.messages);
        else
            messages = null;

        if (this.attachments != null)
            attachments = new HashMap<>(this.attachments);
        else
            attachments = null;

        return new JSONObject()
                .put("users", users == null ? JSONObject.NULL : mapToJson(users))
                .put("members", members == null ? JSONObject.NULL : mapToJson(members))
                .put("roles", roles == null ? JSONObject.NULL : mapToJson(roles))
                .put("channels", channels == null ? JSONObject.NULL : mapToJson(channels))
                .put("messages", messages == null ? JSONObject.NULL : mapToJson(messages))
                .put("attachments", attachments == null ? JSONObject.NULL : mapToJson(attachments));
    }

    @NotNull
    public static ResolvedData decompile(JSONObject obj) {
        HashMap<String, User> users = new HashMap<>();
        HashMap<String, Member> members;
        HashMap<String, Role> roles;
        HashMap<String, Channel> channels;
        HashMap<String, Message> messages;
        HashMap<String, Attachment> attachments;

        try {
            HashMap<String, Resolvable> usersMap = jsonToMap(obj.getJSONArray("users"));
            for (Map.Entry<String, Resolvable> entry : usersMap.entrySet())
                users.put(entry.getKey(), (User) entry.getValue());
        } catch (Exception e) {
            users = null;
        }

        try {
            HashMap<String, Resolvable> membersMap = jsonToMap(obj.getJSONArray("members"));
            members = new HashMap<>();
            for (Map.Entry<String, Resolvable> entry : membersMap.entrySet())
                members.put(entry.getKey(), (Member) entry.getValue());
        } catch (Exception e) {
            members = null;
        }

        try {
            HashMap<String, Resolvable> rolesMap = jsonToMap(obj.getJSONArray("roles"));
            roles = new HashMap<>();
            for (Map.Entry<String, Resolvable> entry : rolesMap.entrySet())
                roles.put(entry.getKey(), (Role) entry.getValue());
        } catch (Exception e) {
            roles = null;
        }

        try {
            HashMap<String, Resolvable> channelsMap = jsonToMap(obj.getJSONArray("channels"));
            channels = new HashMap<>();
            for (Map.Entry<String, Resolvable> entry : channelsMap.entrySet())
                channels.put(entry.getKey(), (Channel) entry.getValue());
        } catch (Exception e) {
            channels = null;
        }

        try {
            HashMap<String, Resolvable> messagesMap = jsonToMap(obj.getJSONArray("messages"));
            messages = new HashMap<>();
            for (Map.Entry<String, Resolvable> entry : messagesMap.entrySet())
                messages.put(entry.getKey(), (Message) entry.getValue());
        } catch (Exception e) {
            messages = null;
        }

        try {
            HashMap<String, Resolvable> attachmentsMap = jsonToMap(obj.getJSONArray("attachments"));
            attachments = new HashMap<>();
            for (Map.Entry<String, Resolvable> entry : attachmentsMap.entrySet())
                attachments.put(entry.getKey(), (Attachment) entry.getValue());
        } catch (Exception e) {
            attachments = null;
        }

        return new ResolvedData(users, members, roles, channels, messages, attachments);
    }

    public JSONArray mapToJson(HashMap<String, Compilerable> map) {
        JSONArray arr = new JSONArray();
        for (String key : map.keySet()) {
            JSONObject obj = new JSONObject();
            obj.put(key, map.get(key).compile());
            arr.put(obj);
        }
        return arr;
    }

    public static HashMap<String, Resolvable> jsonToMap(JSONArray arr) {
        HashMap<String, Resolvable> map = new HashMap<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            map.put(obj.keys().next(), (Resolvable) obj.getJSONObject(obj.keys().next()));
        }
        return map;
    }
}