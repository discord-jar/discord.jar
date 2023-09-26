package com.seailz.discordjar.model.channel.forum;

import com.seailz.discordjar.core.Compilerable;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

public record DefaultReaction(
        String emojiId,
        String emojiName
) implements Compilerable {

    @NonNull
    public static DefaultReaction decompile(JSONObject obj) {
        String emojiId;
        String emojiName;

        try {
            emojiId = obj.getString("emoji_id");
        } catch (Exception e) {
            emojiId = null;
        }

        try {
            emojiName = obj.getString("emoji_name");
        } catch (Exception e) {
            emojiName = null;
        }

        return new DefaultReaction(emojiId, emojiName);
    }

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("emoji_id", emojiId)
                .put("emoji_name", emojiName);
    }
}
