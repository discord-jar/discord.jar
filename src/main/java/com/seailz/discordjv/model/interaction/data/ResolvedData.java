package com.seailz.discordjv.model.interaction.data;

import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.model.channel.Channel;
import com.seailz.discordjv.model.guild.Member;
import com.seailz.discordjv.model.message.Attachment;
import com.seailz.discordjv.model.message.Message;
import com.seailz.discordjv.model.resolve.Resolvable;
import com.seailz.discordjv.model.role.Role;
import com.seailz.discordjv.model.user.User;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents resolved data from an interaction
 *
 * @param users       A map of user ids to users
 * @param members     A map of member ids to members
 * @param roles       A map of role ids to roles
 * @param channels    A map of channel ids to channels
 * @param messages    A map of message ids to messages
 * @param attachments A map of attachment ids to attachments
 * @author Seailz
 * @see com.seailz.discordjv.model.interaction.Interaction
 * @since 1.0
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
            JSONObject resolved = obj.getJSONObject("users");
            users = new HashMap<>();
            HashMap<String, User> finalUsers = users;
            resolved.toMap().forEach((key, value) -> finalUsers.put(key, (User) value));
        } catch (Exception e) {
            users = null;
        }

        try {
            JSONObject resolved = obj.getJSONObject("members");
            members = new HashMap<>();
            HashMap<String, Member> finalMembers = members;
            resolved.toMap().forEach((key, value) -> finalMembers.put(key, (Member) value));
        } catch (Exception e) {
            members = null;
        }

        try {
            JSONObject rolesObj = obj.getJSONObject("roles");
            roles = new HashMap<>();
            HashMap<String, Role> finalRoles = roles;
            rolesObj.toMap().forEach((key, value) -> finalRoles.put(key, (Role) value));
        } catch (Exception e) {
            roles = null;
        }

        try {
            JSONObject channelsObj = obj.getJSONObject("channels");
            channels = new HashMap<>();
            HashMap<String, Channel> finalChannels = channels;
            channelsObj.toMap().forEach((key, value) -> finalChannels.put(key, (Channel) value));
        } catch (Exception e) {
            channels = null;
        }

        try {
            JSONObject messagesObj = obj.getJSONObject("messages");
            messages = new HashMap<>();
            HashMap<String, Message> finalMessages = messages;
            messagesObj.toMap().forEach((key, value) -> finalMessages.put(key, (Message) value));
        } catch (Exception e) {
            messages = null;
        }

        try {
            JSONObject attachmentsObj = obj.getJSONObject("attachments");
            attachments = new HashMap<>();
            HashMap<String, Attachment> finalAttachments = attachments;
            attachmentsObj.toMap().forEach((key, value) -> finalAttachments.put(key, (Attachment) value));
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