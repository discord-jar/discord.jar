package com.seailz.discordjv.command;

import com.seailz.discordjv.command.listeners.CommandListener;
import com.seailz.discordjv.events.model.interaction.command.CommandInteractionEvent;

import java.util.HashMap;

/**
 * Simple class for dispatching commands to their respective listeners.
 *
 * @author Seailz
 * @see CommandListener
 * @since 1.0
 */
public class CommandDispatcher {

    private final HashMap<String, CommandListener> listeners = new HashMap<>();

    public void registerCommand(String name, CommandListener listener) {
        listeners.put(name, listener);
    }

    public void dispatch(String name, CommandInteractionEvent event) {
        listeners.get(name).onCommand(event);
    }

}
