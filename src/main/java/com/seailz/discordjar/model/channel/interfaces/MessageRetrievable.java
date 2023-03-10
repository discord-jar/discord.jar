package com.seailz.discordjar.model.channel.interfaces;

import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.model.message.Message;
import com.seailz.discordjar.utils.Checker;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface MessageRetrievable extends Channel {

    default List<Message> messagesBefore(String before) {
        DiscordRequest request = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.CHANNELS.MESSAGES.GET_MESSAGES.replace("{channel.id}", id()) + "?before=" + before,
                djv(),
                URLS.GET.CHANNELS.MESSAGES.GET_MESSAGES,
                RequestMethod.GET
        );
        List<Message> messages = new ArrayList<>();
        request.invoke().arr().forEach(obj -> messages.add(Message.decompile((JSONObject) obj, djv())));
        return messages;
    }

    default List<Message> messagesBefore(String before, int limit) {
        Checker.check(limit > 100 || limit < 1, "Limit must be between 1 and 100");
        DiscordRequest request = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.CHANNELS.MESSAGES.GET_MESSAGES.replace("{channel.id}", id()) + "?before=" + before + "&limit=" + limit,
                djv(),
                URLS.GET.CHANNELS.MESSAGES.GET_MESSAGES,
                RequestMethod.GET
        );
        List<Message> messages = new ArrayList<>();
        request.invoke().arr().forEach(obj -> messages.add(Message.decompile((JSONObject) obj, djv())));
        return messages;
    }

    default List<Message> messagesAfter(String after, int limit) {
        Checker.check(limit > 100 || limit < 1, "Limit must be between 1 and 100");
        DiscordRequest request = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.CHANNELS.MESSAGES.GET_MESSAGES.replace("{channel.id}", id()) + "?after=" + after + "&limit=" + limit,
                djv(),
                URLS.GET.CHANNELS.MESSAGES.GET_MESSAGES,
                RequestMethod.GET
        );
        List<Message> messages = new ArrayList<>();
        request.invoke().arr().forEach(obj -> messages.add(Message.decompile((JSONObject) obj, djv())));
        return messages;
    }

    default List<Message> messagesAfter(String after) {
        DiscordRequest request = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.CHANNELS.MESSAGES.GET_MESSAGES.replace("{channel.id}", id()) + "?after=" + after,
                djv(),
                URLS.GET.CHANNELS.MESSAGES.GET_MESSAGES,
                RequestMethod.GET
        );
        List<Message> messages = new ArrayList<>();
        request.invoke().arr().forEach(obj -> messages.add(Message.decompile((JSONObject) obj, djv())));
        return messages;
    }

    default List<Message> messagesAround(String around) {
        DiscordRequest request = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.CHANNELS.MESSAGES.GET_MESSAGES.replace("{channel.id}", id()) + "?around=" + around,
                djv(),
                URLS.GET.CHANNELS.MESSAGES.GET_MESSAGES,
                RequestMethod.GET
        );
        List<Message> messages = new ArrayList<>();
        request.invoke().arr().forEach(obj -> messages.add(Message.decompile((JSONObject) obj, djv())));
        return messages;
    }

    default List<Message> messagesAround(String around, int limit) {
        Checker.check(limit > 100 || limit < 1, "Limit must be between 1 and 100");
        DiscordRequest request = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.CHANNELS.MESSAGES.GET_MESSAGES.replace("{channel.id}", id()) + "?around=" + around + "&limit=" + limit,
                djv(),
                URLS.GET.CHANNELS.MESSAGES.GET_MESSAGES,
                RequestMethod.GET
        );
        List<Message> messages = new ArrayList<>();
        request.invoke().arr().forEach(obj -> messages.add(Message.decompile((JSONObject) obj, djv())));
        return messages;
    }

    default List<Message> messages(int limit) {
        Checker.check(limit > 100 || limit < 1, "Limit must be between 1 and 100");
        DiscordRequest request = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.CHANNELS.MESSAGES.GET_MESSAGES.replace("{channel.id}", id()) + "?limit=" + limit,
                djv(),
                URLS.GET.CHANNELS.MESSAGES.GET_MESSAGES,
                RequestMethod.GET
        );
        List<Message> messages = new ArrayList<>();
        request.invoke().arr().forEach(obj -> messages.add(Message.decompile((JSONObject) obj, djv())));
        return messages;
    }

    default List<Message> messages() {
        DiscordRequest request = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.CHANNELS.MESSAGES.GET_MESSAGES,
                djv(),
                URLS.GET.CHANNELS.MESSAGES.GET_MESSAGES.replace("{channel.id}", id()),
                RequestMethod.GET
        );
        List<Message> messages = new ArrayList<>();
        request.invoke().arr().forEach(obj -> messages.add(Message.decompile((JSONObject) obj, djv())));
        return messages;
    }

    /**
     * Returns 500 of the last messages in the channel.
     *
     * @return
     */
    default List<Message> get500Messages() {
        List<Message> messages = new ArrayList<>();
        String lastMessageId = null;
        for (Message m : messages(100)) {
            messages.add(m);
            lastMessageId = m.id();
        }

        for (int i = 0; i < 4; i++) {
            for (Message m : messagesBefore(lastMessageId, 100)) {
                messages.add(m);
                lastMessageId = m.id();
            }
        }

        return messages;
    }

    default Message getMessageById(String id) {
        DiscordResponse response = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.GET.CHANNELS.MESSAGES.GET_MESSAGE.replace("{channel.id}", id()).replace("{message.id}", id),
                djv(),
                URLS.GET.CHANNELS.MESSAGES.GET_MESSAGE,
                RequestMethod.GET
        ).invoke();
        return Message.decompile(response.body(), djv());
    }

}
