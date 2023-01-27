package com.seailz.discordjv.model.channel;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.channel.internal.GroupDMImpl;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.user.User;
import com.seailz.discordjv.utils.image.ImageUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a group DM channel.
 * <br>A group DM channel is a private channel between multiple users.
 *
 * @author Seailz
 * @since  1.0
 * @see    User
 * @see    User#createDM()
 * @see    DMChannel
 */
public interface GroupDM extends DMChannel {

    User owner();
    String iconUrl();

    @NotNull
    @Contract("_, _ -> new")
    static GroupDM decompile(@NotNull JSONObject obj, @NotNull DiscordJv discordJv) {
        String lastMessageId = obj.has("last_message_id") ? obj.getString("last_message_id") : null;

        List<User> recipients = new ArrayList<>();
        JSONArray recipientsArray = obj.getJSONArray("recipients");
        recipientsArray.forEach(o -> recipients.add(User.decompile((JSONObject) o, discordJv)));

        String name = obj.has("name") ? obj.getString("name") : recipients.get(0).username();
        User owner = recipients.stream().filter(u -> u.id().equals(obj.getString("owner_id"))).findFirst().orElse(null);
        String iconUrl = obj.has("icon") ? ImageUtils.getUrl(obj.getString("icon"), ImageUtils.ImageType.DM_ICON, obj.getString("id")) : null;
        return new GroupDMImpl(obj.getString("id"), ChannelType.GROUP_DM, name, lastMessageId, recipients, discordJv, owner, iconUrl, obj);
    }

}