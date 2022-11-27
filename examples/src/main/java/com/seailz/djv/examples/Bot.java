package com.seailz.djv.examples;

import com.seailz.discordjv.DiscordJv;

import java.util.concurrent.ExecutionException;

public class Bot {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new Bot(args[0]);
    }

    public Bot(String token) throws ExecutionException, InterruptedException {
        DiscordJv djv = new DiscordJv(token);

        // This is where you register your commands.
        djv.registerCommands(new ExampleCommand());
        // You can also register multiple commands at once, like this:
        djv.registerCommands(new ExampleCommand(), new ExampleMessageCommand());
    }
}