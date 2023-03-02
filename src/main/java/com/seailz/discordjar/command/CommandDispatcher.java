package com.seailz.discordjar.command;

import com.seailz.discordjar.command.annotation.SlashCommandInfo;
import com.seailz.discordjar.command.listeners.CommandListener;
import com.seailz.discordjar.command.listeners.slash.SlashCommandListener;
import com.seailz.discordjar.command.listeners.slash.SlashSubCommand;
import com.seailz.discordjar.command.listeners.slash.SubCommandListener;
import com.seailz.discordjar.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.discordjar.events.model.interaction.command.SlashCommandInteractionEvent;
import com.seailz.discordjar.model.interaction.data.command.ResolvedCommandOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Simple class for dispatching commands to their respective listeners.
 *
 * @author Seailz
 * @see CommandListener
 * @since 1.0
 */
public class CommandDispatcher {

    private final HashMap<String, CommandListener> listeners = new HashMap<>();
    private final HashMap<SlashCommandListener, ArrayList<SlashSubCommandDetails>> subListeners = new HashMap<>();

    public void registerCommand(String name, CommandListener listener) {
        listeners.put(name, listener);
    }
    public void registerSubCommand(SlashCommandListener top, SlashSubCommand sub, SubCommandListener listener) {
        if (subListeners.containsKey(top)) {
            ArrayList<SlashSubCommandDetails> exist = subListeners.get(top);
            exist.add(new SlashSubCommandDetails(sub, listener));
            subListeners.put(top, exist);
            return;
        }
        ArrayList<SlashSubCommandDetails> list = new ArrayList<>();
        list.add(new SlashSubCommandDetails(sub, listener));
        subListeners.put(top, list);
    }

    public void dispatch(String name, CommandInteractionEvent event) {
        if ((event instanceof SlashCommandInteractionEvent) && ((SlashCommandInteractionEvent) event).getOptionsInternal() != null && !((SlashCommandInteractionEvent) event).getOptionsInternal().isEmpty()) {
            for (ResolvedCommandOption option : ((SlashCommandInteractionEvent) event).getOptionsInternal()) {
                if (option.type() == CommandOptionType.SUB_COMMAND) {
                    for (ArrayList<SlashSubCommandDetails> detailsList : subListeners.values()) {
                        for (SlashSubCommandDetails details : detailsList) {
                            if (details.sub.getName().equals(option.name())) {
                                SlashCommandListener top = subListeners.keySet().stream().toList()
                                        .get(subListeners.values().stream().toList().indexOf(detailsList));

                                if (Objects.equals(name, top.getClass().getAnnotation(SlashCommandInfo.class).name())) {
                                    details.listener().onCommand(event);
                                }
                                return;
                            /*if (event.getName().startsWith(top.getClass().getAnnotation(SlashCommandInfo.class).name())) {
                            }*/
                            }
                        }
                    }
                } else if (option.type() == CommandOptionType.SUB_COMMAND_GROUP) {
                    List<ResolvedCommandOption> subOptions = new ArrayList<>();

                    for (int i = 1; i < option.options().size(); i++) {
                        subOptions.add(option.options().get(i));
                    }

                    for (ResolvedCommandOption subs : option.options()) {
                        for (ArrayList<SlashSubCommandDetails> detailsList : subListeners.values()) {
                            for (SlashSubCommandDetails details : detailsList) {
                                if (details.sub.getName().equals(subs.name())) {
                                    SlashCommandListener top = subListeners.keySet().stream().toList()
                                            .get(subListeners.values().stream().toList().indexOf(detailsList));

                                    if (Objects.equals(name, top.getClass().getAnnotation(SlashCommandInfo.class).name())) {
                                        details.listener().onCommand(event);
                                    }
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
