package com.seailz.discordjv.model.channel;

import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.permission.PermissionOverwrite;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface GuildChannel extends Channel1 {

    @NotNull
    Guild guild();

    // Will return 0 if not found
    int position();

    @NotNull
    String topic();

    @Nullable
    List<PermissionOverwrite> permissionOverwrites();

    @Override
    default JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("id", id());
        obj.put("type", type().getCode());
        obj.put("name", name());
        obj.put("guild_id", guild().id());
        obj.put("topic", topic());
        obj.put("position", position());

        if (permissionOverwrites() != null) {
            JSONArray array = new JSONArray();
            for (PermissionOverwrite overwrite : permissionOverwrites())
                array.put(overwrite.compile());
        }

        obj.put("permission_overwrites", permissionOverwrites());
        return obj;
    }
}
