package examples;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.DiscordJarBuilder;
import com.seailz.discordjar.utils.rest.DiscordRequest;

import java.util.concurrent.ExecutionException;

public class Bot {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new Bot(args[0]);
    }

    public Bot(String token) throws ExecutionException, InterruptedException {
        DiscordJar djar = new DiscordJarBuilder(token).build();

        try {
            // This is where you register your commands.
            djar.registerCommands(new ExampleCommand());
            // You can also register multiple commands at once, like this:
            djar.registerCommands(new ExampleCommand(), new ExampleMessageCommand());
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            // Handle any errors that occur during registration.
            throw new RuntimeException(e);
        }
    }
}