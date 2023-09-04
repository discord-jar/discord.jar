import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.DiscordJarBuilder;
import com.seailz.discordjar.voice.model.provider.VoiceProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class VoiceTest {

    public static void main(String[] args) throws IOException {
        DiscordJar jar = new DiscordJarBuilder(Files.readString(new File("token.txt").toPath()))
                .defaultIntents()
                .setDebug(true)
                .build();

        jar.getVoiceChannelById("1092499443909136484")
                .connect(new VoiceProvider(jar.getGuildById("993461660792651826")) {
                    @Override
                    public byte[] provide20ms() {
                        return new byte[0];
                    }

                    @Override
                    public boolean canProvide() {
                        return false;
                    }
                });
    }

}
