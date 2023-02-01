package com.seailz.discordjv.utils.registry;

import com.seailz.discordjv.model.component.button.Button;
import com.seailz.discordjv.utils.Registry;

/**
 * Used for registering button actions.
 * <br>Singleton class.
 *
 * @author Seailz
 * @since 1.0
 */
public class ButtonRegistry extends Registry<Button.ButtonAction> {

    private static ButtonRegistry instance;

    private ButtonRegistry() {
    }

    /**
     * Gets the instance of the button registry.
     *
     * @return The instance of the button registry, or a new instance if it doesn't exist.
     */
    public static ButtonRegistry getInstance() {
        if (instance == null) {
            instance = new ButtonRegistry();
        }
        return instance;
    }
}
