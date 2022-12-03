package com.seailz.discordjv.model.channel;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.action.MessageCreateAction;
import com.seailz.discordjv.model.channel.internal.DMChannelImpl;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.component.DisplayComponent;
import com.seailz.discordjv.model.user.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a DM channel.
 * <br>A DM channel is a private channel between two users, or in the case of a group DM, between multiple users.
 * <p>
 * You can send messages to a DM channel by using {@link #sendMessage(String)}.
 * <br>To create a DM channel, see {@link User#createDM()}
 *
 * @author Seailz
 * @since  1.0
 * @see    User
 * @see    User#createDM()
 * @see    MessageCreateAction
 * @see    DisplayComponent
 * @see    ChannelType#DM
 * @see    ChannelType#GROUP_DM
 */
public interface DMChannel extends Channel {

    String lastMessageId();
    List<User> recipients();
    DiscordJv discordJv();

    @NotNull
    @Override
    default ChannelType type() {
        return ChannelType.DM;
    }

    default MessageCreateAction sendMessage(String text) {
        return new MessageCreateAction(text, id(), discordJv());
    }
    default MessageCreateAction sendComponents(DisplayComponent... components) {
        return new MessageCreateAction(new ArrayList<>(List.of(components)), id(), discordJv());
    }

    @NotNull
    @Contract("_, _ -> new")
    static DMChannel decompile(@NotNull JSONObject obj, @NotNull DiscordJv djv) {
        String lastMessageId = obj.has("last_message_id") ? obj.getString("last_message_id") : null;

        List<User> recipients = new ArrayList<>();
        JSONArray recipientsArray = obj.getJSONArray("recipients");
        recipientsArray.forEach(o -> recipients.add(User.decompile((JSONObject) o, djv)));

        String name = obj.has("name") ? obj.getString("name") : recipients.get(0).username();
        return new DMChannelImpl(obj.getString("id"), ChannelType.DM, name, lastMessageId ,recipients, djv);
    }

    /**
     * Compiles this object
     */
    @Override
    default JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("id", id());
        obj.put("type", type().getCode());
        obj.put("last_message_id", lastMessageId());

        JSONArray array = new JSONArray();
        for (User user : recipients())
            array.put(user.compile());

        obj.put("recipients", array);
        return obj;
    }
}
