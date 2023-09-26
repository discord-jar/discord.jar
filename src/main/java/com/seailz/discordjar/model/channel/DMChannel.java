package com.seailz.discordjar.model.channel;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.action.message.MessageCreateAction;
import com.seailz.discordjar.model.channel.interfaces.MessageRetrievable;
import com.seailz.discordjar.model.channel.interfaces.Messageable;
import com.seailz.discordjar.model.channel.interfaces.Typeable;
import com.seailz.discordjar.model.channel.internal.DMChannelImpl;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.component.DisplayComponent;
import com.seailz.discordjar.model.user.User;
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
 * @see User
 * @see User#createDM()
 * @see MessageCreateAction
 * @see DisplayComponent
 * @see ChannelType#DM
 * @see ChannelType#GROUP_DM
 * @since 1.0
 */
public interface DMChannel extends Channel, Typeable, Messageable, MessageRetrievable {

    @NotNull
    @Contract("_, _ -> new")
    static DMChannel decompile(@NotNull JSONObject obj, @NotNull DiscordJar djv) {
        String lastMessageId = obj.has("last_message_id") && !obj.get("last_message_id").equals(JSONObject.NULL) ? obj.getString("last_message_id") : null;

        List<User> recipients = new ArrayList<>();
        JSONArray recipientsArray = obj.getJSONArray("recipients");
        recipientsArray.forEach(o -> recipients.add(User.decompile((JSONObject) o, djv)));

        String name = obj.has("name") ? obj.getString("name") : recipients.get(0).username();
        return new DMChannelImpl(obj.getString("id"), ChannelType.DM, name, lastMessageId, recipients, djv, obj);
    }

    String lastMessageId();

    List<User> recipients();

    DiscordJar discordJv();

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

    /**
     * Compiles this object
     */
    @Override
    default JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("id", id());
        obj.put("type", type().getCode());
        if (lastMessageId() != null) obj.put("last_message_id", lastMessageId());

        JSONArray array = new JSONArray();
        for (User user : recipients())
            array.put(user.compile());

        obj.put("recipients", array);
        return obj;
    }
}
