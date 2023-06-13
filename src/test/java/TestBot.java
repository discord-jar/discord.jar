import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.DiscordJarBuilder;
import com.seailz.discordjar.cache.CacheType;
import com.seailz.discordjar.model.application.Intent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.EnumSet;

public class TestBot {
    public static void main(String[] args) throws IOException {
        String token = Files.readString(new File("token.txt").toPath());
        DiscordJar jar = new DiscordJarBuilder(token)
                .setCacheTypes(EnumSet.of(CacheType.ALL))
                .build();

        long start = System.currentTimeMillis();
        jar.getMemberById("993461660792651826","947691195658797167");
        System.out.println("Took " + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        jar.getMemberById("993461660792651826","947691195658797167");
        System.out.println("Took " + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        jar.getMemberById("993461660792651826","947691195658797167");
        System.out.println("Took " + (System.currentTimeMillis() - start) + "ms");
    }
}
