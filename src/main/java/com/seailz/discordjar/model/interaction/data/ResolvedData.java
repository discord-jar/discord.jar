package com.seailz.discordjar.model.interaction.data;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.model.guild.Member;
import com.seailz.discordjar.model.message.Attachment;
import com.seailz.discordjar.model.message.Message;
import com.seailz.discordjar.model.resolve.Resolvable;
import com.seailz.discordjar.model.role.Role;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.model.ModelDecoder;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

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
 * @see com.seailz.discordjar.model.interaction.Interaction
 * @since 1.0
 */
public record ResolvedData(
        HashMap<String, User> users,
        HashMap<String, Member> members,
        HashMap<String, Role> roles,
        HashMap<String, Channel> channels,
        HashMap<String, Message> messages,
        HashMap<String, Attachment> attachments
)// implements Compilerable
{

    /* @Override
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
    } */

    @NotNull
    public static ResolvedData decompile(JSONObject obj, DiscordJar discordJar) {
        HashMap<String, User> users = new HashMap<>();
        HashMap<String, Member> members = new HashMap<>();
        HashMap<String, Role> roles = new HashMap<>();
        HashMap<String, Channel> channels = new HashMap<>();
        HashMap<String, Message> messages = new HashMap<>();
        HashMap<String, Attachment> attachments = new HashMap<>();

        if (obj.has("users") && !obj.isNull("users")) {
            JSONObject usersObj = obj.getJSONObject("users");
            for (String s : usersObj.keySet()) {
                users.put(s, (User) ModelDecoder.decodeObject(usersObj.getJSONObject(s), User.class, discordJar));
            }
        } else {
            users = null;
        }

        if (obj.has("members") && !obj.isNull("members")) {
            JSONObject membersObj = obj.getJSONObject("members");
            for (String s : membersObj.keySet()) {
                members.put(s, Member.decompile(membersObj.getJSONObject(s), discordJar, null, null));
            }
        } else {
            members = null;
        }

        if (obj.has("roles") && !obj.isNull("roles")) {
            JSONObject rolesObj = obj.getJSONObject("roles");
            for (String s : rolesObj.keySet()) {
                roles.put(s, Role.decompile(rolesObj.getJSONObject(s)));
            }
        } else {
            roles = null;
        }

        if (obj.has("channels") && !obj.isNull("channels")) {
            JSONObject channelsObj = obj.getJSONObject("channels");
            for (String s : channelsObj.keySet()) {
                channels.put(s, Channel.decompile(channelsObj.getJSONObject(s), discordJar));
            }
        } else {
            channels = null;
        }

        if (obj.has("messages") && !obj.isNull("messages")) {
            JSONObject messagesObj = obj.getJSONObject("messages");
            for (String s : messagesObj.keySet()) {
                messages.put(s, Message.decompile(messagesObj.getJSONObject(s), discordJar));
            }
        } else {
            messages = null;
        }

        if (obj.has("attachments") && !obj.isNull("attachments")) {
            JSONObject attachmentsObj = obj.getJSONObject("attachments");
            for (String s : attachmentsObj.keySet()) {
                attachments.put(s, Attachment.decompile(attachmentsObj.getJSONObject(s)));
            }
        } else {
            attachments = null;
        }

        return new ResolvedData(users, members, roles, channels, messages, attachments);
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