package net.thejadeproject.ascension.keybinds;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final KeyMapping CULTIVATE_KEY = new KeyMapping(
            "key.ascension.cultivate",
            GLFW.GLFW_KEY_C,
            "category.ascension.cultivation"
    );
}