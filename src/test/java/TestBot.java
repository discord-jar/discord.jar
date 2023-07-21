import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.DiscordJarBuilder;
import com.seailz.discordjar.utils.rest.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestBot {

    public static void main(String[] args) throws IOException {
        String token = Files.readString(new File("token.txt").toPath());
        DiscordJar bot = new DiscordJarBuilder(token).build();
        bot.getTextChannelById("1091078593527959594")
                .sendMessage("Nonce retention check")
                .setTts(true)
                .run().onError((e) -> {
                    System.out.println("Errored");
                    for (Response.Error.ErroredResponse allErrorMessage : e.getAllErrorMessages()) {
                        System.out.println("allErrorMessage = " + allErrorMessage.getCode() + ":" + allErrorMessage.getMessage());
                    }
                });

    }

}
