package com.seailz.discordjar.events;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.annotation.EventMethod;
import com.seailz.discordjar.events.model.Event;
import com.seailz.discordjar.events.model.interaction.CustomIdable;
import com.seailz.discordjar.utils.annotation.RequireCustomId;
import com.seailz.discordjar.utils.thread.DiscordJarThreadAllocator;
import org.jetbrains.annotations.NotNull;

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
        final String customId;

        ListenerMethodPair(DiscordListener listener, Method method, String customId) {
            this.listener = listener;
            this.method = method;
            this.customId = customId;
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
                    Class<?> maybeEventType = method.getParameterTypes()[0];

                    if (!Event.class.isAssignableFrom(maybeEventType))
                        throw new IllegalArgumentException(String.format("%s first arg is not of Event", method));
                    else if (method.getParameterTypes().length > 1)
                        throw new IllegalArgumentException(String.format("%s#%s is an invalid listener", method.getDeclaringClass(), method.getName()));

                    @SuppressWarnings("unchecked")
                    Class<? extends Event> eventType = (Class<? extends Event>) maybeEventType;
                    EventMethod eventMethod = method.getAnnotation(EventMethod.class);

                    String customId = null;
                    if (eventMethod.requireCustomId() != null && !eventMethod.requireCustomId().equals("")) customId = eventMethod.requireCustomId();

                    listenersByEventType.computeIfAbsent(eventType, k -> new ArrayList<>()).add(new ListenerMethodPair(listener, method, customId));
                }
            }
        }
    }

    /**
     * Returns a list of all listeners that are registered to the dispatcher relating to the given event type.
     * <br>This includes superclasses, so if someone registered a listener for {@link Event}, they'll get all events, or {@link com.seailz.discordjar.events.model.interaction.InteractionEvent InteractionEvents} and they'll get all interaction events, etc.
     * @param eventType The type of event to get listeners for
     * @return A list of all listeners that are registered to the dispatcher relating to the given event type
     */
    @NotNull
    private List<ListenerMethodPair> findListenersForEvent(@NotNull Class<? extends Event> eventType) {
        List<ListenerMethodPair> listeners = new ArrayList<>();
        listenersByEventType.forEach((event, listenerMethodPairs) -> {
            if (event.isAssignableFrom(eventType)) {
                listeners.addAll(listenerMethodPairs);
            }
        });
        return listeners;
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
        if (event == null) return;
        event.setType(type);
        new Thread(() -> {
            long start = System.currentTimeMillis();
            List<ListenerMethodPair> listenersForEventType = findListenersForEvent(type);
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

                if (listenerMethodPair.customId != null) {
                    if (event instanceof CustomIdable) {
                        if (((CustomIdable) event).getCustomId() == null) {
                            continue;
                        }

                        if (!((CustomIdable) event).getCustomId().matches(listenerMethodPair.customId)) {
                            continue;
                        }
                    }
                }

                method.setAccessible(true);
                DiscordJarThreadAllocator.requestThread(() -> {
                    try {
                        method.invoke(listenerMethodPair.listener, event);
                    } catch (IllegalAccessException | ArrayIndexOutOfBoundsException e) {
                        // If we're unable to invoke the method, we'll just ignore it to avoid any issues.
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        // Method threw an exception. We'll print the stack trace and continue.
                        System.out.println(method.getDeclaringClass().getSimpleName() + "#" + method.getName() + " threw an exception while being invoked.");
                        e.getCause().printStackTrace();
                    }
                }, "djar--EventDispatcher-inner");
            }
        }, "djar--EventDispatcher").start();
    }
}