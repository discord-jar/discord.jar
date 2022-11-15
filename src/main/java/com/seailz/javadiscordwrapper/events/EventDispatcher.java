package com.seailz.javadiscordwrapper.events;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.events.annotation.EventMethod;
import com.seailz.javadiscordwrapper.events.model.Event;
import com.seailz.javadiscordwrapper.events.model.message.MessageCreateEvent;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class is used to dispatch events to the correct listeners.
 * This class is an internal class and should not be used by the end user.
 *
 * @author Seailz
 * @since  1.0
 */
public class EventDispatcher {

    private HashMap<DiscordListener, Method> listeners;

    public EventDispatcher(DiscordJv bot) {
        listeners = new HashMap<>();
    }

    /**
     * Registers a listener to the dispatcher.
     * When an event is fired, the dispatcher will call the listener method that is marked with the {@link EventMethod} annotation.
     *
     * @param listeners The listeners to register
     * @since 1.0
     */
    public void addListener(DiscordListener... listeners) {
        for (DiscordListener listener : listeners) {
            for (Method method : listener.getClass().getMethods()) {
                if (method.isAnnotationPresent(EventMethod.class)) {
                    this.listeners.put(listener, method);
                }
            }
        }
    }

    /**
     * Dispatches an event to all registered listeners.
     * This method is called by the {@link com.seailz.javadiscordwrapper.DiscordJv} class & other internal classes and should not be called by the end user.
     *
     * @param event The event to dispatch
     * @since 1.0
     */
    public void dispatchEvent(Event event, Class<? extends Event> type, DiscordJv djv) {
        if (event instanceof MessageCreateEvent) {
            if (((MessageCreateEvent) event).getMessage().author().id().equals(djv.getSelfUser().id()))
                return;
        }

            int index = 0;
            for (Method i : listeners.values()) {
                if (i.getParameterTypes()[0].equals(type)) {
                    try {
                        i.setAccessible(true);
                        i.invoke(listeners.keySet().toArray()[index], event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                index++;
        }
    }
}
