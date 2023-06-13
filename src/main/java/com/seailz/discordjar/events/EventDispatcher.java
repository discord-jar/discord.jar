package com.seailz.discordjar.events;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.annotation.EventMethod;
import com.seailz.discordjar.events.model.Event;
import com.seailz.discordjar.events.model.interaction.CustomIdable;
import com.seailz.discordjar.events.model.message.MessageCreateEvent;
import com.seailz.discordjar.utils.annotation.RequireCustomId;
import com.seailz.discordjar.utils.rest.DiscordRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to dispatch events to the correct listeners.
 * This class is an internal class and should not be used by the end user.
 *
 * @author Seailz
 * @since 1.0
 */
public class EventDispatcher {

    // Map: Event type -> List of pairs (Listener, Method)
    private final Map<Class<? extends Event>, List<ListenerMethodPair>> listenersByEventType = new HashMap<>();

    // Pair of listener instance and method to call
    private static class ListenerMethodPair {
        final DiscordListener listener;
        final Method method;

        ListenerMethodPair(DiscordListener listener, Method method) {
            this.listener = listener;
            this.method = method;
        }
    }


    public EventDispatcher(DiscordJar bot) {
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
                    Class<? extends Event> eventType = (Class<? extends Event>) method.getParameterTypes()[0];
                    listenersByEventType.computeIfAbsent(eventType, k -> new ArrayList<>()).add(new ListenerMethodPair(listener, method));
                }
            }
        }
    }

    /**
     * Dispatches an event to all registered listeners.
     * This method is called by the {@link DiscordJar} class & other internal classes and should not be called by the end user.
     * This method is called in a new thread so the bot can handle multiple events at once.
     *
     * @param event The event to dispatch
     * @since 1.0
     */
    public void dispatchEvent(Event event, Class<? extends Event> type, DiscordJar djv) {
        new Thread(() -> {
            long start = System.currentTimeMillis();
            List<ListenerMethodPair> listenersForEventType = listenersByEventType.get(type);
            if (listenersForEventType == null) {
                return;
            }

            for (ListenerMethodPair listenerMethodPair : listenersForEventType) {
                Method method = listenerMethodPair.method;
                if (method.isAnnotationPresent(RequireCustomId.class)) {
                    if (event instanceof CustomIdable) {
                        if (((CustomIdable) event).getCustomId() == null) {
                            continue;
                        }

                        if (!((CustomIdable) event).getCustomId().matches(method.getAnnotation(RequireCustomId.class).value())) {
                            continue;
                        }
                    }
                }

                method.setAccessible(true);
                new Thread(() -> {
                    try {
                        method.invoke(listenerMethodPair.listener, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        // If we're unable to invoke the method, we'll just ignore it to avoid any issues.
                        System.out.println(method.getDeclaringClass().getSimpleName() + "#" + method.getName() + " threw an exception while being invoked.");
                        e.printStackTrace();
                    }
                }).start();
            }
            System.out.println("Event " + event.getClass().getSimpleName() + " took " + (System.currentTimeMillis() - start) + "ms to dispatch.");
        }, "EventDispatcher").start();
    }
}