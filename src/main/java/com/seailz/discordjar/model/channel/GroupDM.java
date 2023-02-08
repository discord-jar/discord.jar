package com.seailz.discordjar.model.channel;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.internal.GroupDMImpl;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.image.ImageUtils;
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
    static GroupDM decompile(@NotNull JSONObject obj, @NotNull DiscordJar discordJar) {
        String lastMessageId = obj.has("last_message_id") ? obj.getString("last_message_id") : null;

        List<User> recipients = new ArrayList<>();
        JSONArray recipientsArray = obj.getJSONArray("recipients");
        recipientsArray.forEach(o -> recipients.add(User.decompile((JSONObject) o, discordJar)));

        String name = obj.has("name") ? obj.getString("name") : recipients.get(0).username();
        User owner = recipients.stream().filter(u -> u.id().equals(obj.getString("owner_id"))).findFirst().orElse(null);
        String iconUrl = obj.has("icon") ? ImageUtils.getUrl(obj.getString("icon"), ImageUtils.ImageType.DM_ICON, obj.getString("id")) : null;
        return new GroupDMImpl(obj.getString("id"), ChannelType.GROUP_DM, name, lastMessageId, recipients, discordJar, owner, iconUrl, obj);
    }

}