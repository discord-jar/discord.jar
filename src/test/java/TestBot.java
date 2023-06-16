import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.DiscordJarBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestBot {

    public static void main(String[] args) throws IOException {
        String token = Files.readString(new File("token.txt").toPath());
        DiscordJar bot = new DiscordJarBuilder(token).build();
        String mId = bot.getTextChannelById("1091078593527959594")
                .sendMessage("Nonce retention check")
                .setTts(true)
                .run().join().id();

        System.out.println(bot.getTextChannelById("1091078593527959594")
                .getMessageById(mId).nonce());

    }

}
