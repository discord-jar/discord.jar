package com.seailz.discordjv.command;

import com.seailz.discordjv.command.annotation.SlashCommandInfo;
import com.seailz.discordjv.command.listeners.CommandListener;
import com.seailz.discordjv.command.listeners.slash.SlashCommandListener;
import com.seailz.discordjv.command.listeners.slash.SlashSubCommand;
import com.seailz.discordjv.command.listeners.slash.SubCommandListener;
import com.seailz.discordjv.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.discordjv.events.model.interaction.command.SlashCommandInteractionEvent;
import com.seailz.discordjv.model.interaction.data.command.ResolvedCommandOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Simple class for dispatching commands to their respective listeners.
 *
 * @author Seailz
 * @see CommandListener
 * @since 1.0
 */
public class CommandDispatcher {

    private final HashMap<String, CommandListener> listeners = new HashMap<>();
    private final HashMap<SlashCommandListener, SlashSubCommandDetails> subListeners = new HashMap<>();

    public void registerCommand(String name, CommandListener listener) {
        listeners.put(name, listener);
    }
    public void registerSubCommand(SlashCommandListener top, SlashSubCommand sub, SubCommandListener listener) {
        subListeners.put(top, new SlashSubCommandDetails(sub, listener));
    }

    public void dispatch(String name, CommandInteractionEvent event) {
        if ((event instanceof SlashCommandInteractionEvent) && ((SlashCommandInteractionEvent) event).getOptions() != null && !((SlashCommandInteractionEvent) event).getOptions().isEmpty()) {
            for (ResolvedCommandOption option : ((SlashCommandInteractionEvent) event).getOptions()) {
                if (option.type() == CommandOptionType.SUB_COMMAND) {
                    System.out.println("option was sub command");
                    for (SlashSubCommandDetails details : subListeners.values()) {
                        System.out.println("checking sub command " + option.name());
                        System.out.println(details.sub.getName());
                        if (details.sub.getName().equals(option.name())) {
                            System.out.println("found sub command " + option.name());
                            SlashCommandListener top
                                    = subListeners.keySet().stream()
                                    .toList().get(
                                            subListeners.values().stream()
                                                    .toList().indexOf(details)
                                    );

                            details.listener.onCommand(event);
                            return;
                            /*if (event.getName().startsWith(top.getClass().getAnnotation(SlashCommandInfo.class).name())) {
                            }*/
                        }
                    }
                } else if (option.type() == CommandOptionType.SUB_COMMAND_GROUP) {
                    List<ResolvedCommandOption> subOptions = new ArrayList<>();

                    for (int i = 1; i < option.options().size(); i++) {
                        subOptions.add(option.options().get(i));
                    }

                    for (ResolvedCommandOption subs : option.options()) {
                        for (SlashSubCommandDetails details : subListeners.values()) {
                            if (details.sub.getName().equals(subs.name())) {
                                SlashCommandListener top
                                        = subListeners.keySet().stream()
                                                .toList().get(
                                                        subListeners.values().stream()
                                                                .toList().indexOf(details)
                                                );

                                if (top.getClass().getAnnotation(SlashCommandInfo.class).name().equals(event.getName())) {
                                    details.listener.onCommand(event);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        listeners.get(name).onCommand(event);
    }

    record SlashSubCommandDetails(
            SlashSubCommand sub,
            SubCommandListener listener
    ) {}
}
