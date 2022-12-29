package com.seailz.discordjv.model.channel;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.channel.internal.DMChannelImpl;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.user.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A private DM between a user and the bot.
 * <p>
 * For now, it is recommended you don't use this class, as {@link DMChannel} does the same thing.
 * <br>This class is here for potential future use.
 * <p>
 * For that reason, this class is deprecated, and {@link DMChannel} is used throughout the library.
 * <br>If you're looking for a group DM, use {@link GroupDM}.
 * <p>
 * Although this class is deprecated, we will still offer support for it,
 * <br>so if any new features are added to both group DMs and DMs, this class will be updated.
 * <br>If a feature is added to DMs, but not group DMs, this class will be updated and the deprecated annotation will be removed.
 * <p>
 * {@code TL;DR}: <b>Class is deprecated, use {@link DMChannel} instead. This class will continue to be supported, however.</b>
 * @author Seailz
 * @since  1.0
 * @see    DMChannel
 */
@Deprecated(since = "1.0")
public interface UserDM extends DMChannel {

    @NotNull
    @Contract("_, _ -> new")
    static UserDM decompile(@NotNull JSONObject obj, @NotNull DiscordJv djv) {
        String lastMessageId = obj.has("last_message_id") ? obj.getString("last_message_id") : null;

        List<User> recipients = new ArrayList<>();
        JSONArray recipientsArray = obj.getJSONArray("recipients");
        recipientsArray.forEach(o -> recipients.add(User.decompile((JSONObject) o, djv)));

        String name = obj.has("name") ? obj.getString("name") : recipients.get(0).username();
        return new DMChannelImpl(obj.getString("id"), ChannelType.DM, name, lastMessageId ,recipients, djv, obj);
    }

}
