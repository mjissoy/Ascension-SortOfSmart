package net.thejadeproject.ascension.util.ToolTips;

import net.minecraft.network.chat.MutableComponent;

public class GradientComponentWrapper {
    private final String text;
    private float hue = 0.0f;
    private float min = 0.1f;
    private float max = 0.9f;
    private float speed = 0.01f;
    private boolean useRGB = false;

    private GradientComponentWrapper(String text) {
        this.text = text;
    }

    /**
     * Create a new gradient component wrapper
     */
    public static GradientComponentWrapper of(String text) {
        return new GradientComponentWrapper(text);
    }

    /**
     * Create a new gradient component wrapper from a translation key
     */
    public static GradientComponentWrapper translatable(String key) {
        return new GradientComponentWrapper(net.minecraft.client.resources.language.I18n.get(key));
    }

    /**
     * Set the base hue for the gradient (0.0 - 1.0)
     */
    public GradientComponentWrapper hue(float hue) {
        this.hue = hue;
        return this;
    }

    /**
     * Set the minimum hue value for gradient effect
     */
    public GradientComponentWrapper min(float min) {
        this.min = min;
        return this;
    }

    /**
     * Set the maximum hue value for gradient effect
     */
    public GradientComponentWrapper max(float max) {
        this.max = max;
        return this;
    }

    /**
     * Set the speed of the color transition
     */
    public GradientComponentWrapper speed(float speed) {
        this.speed = speed;
        return this;
    }

    /**
     * Use RGB mode instead of gradient mode
     */
    public GradientComponentWrapper rgb() {
        this.useRGB = true;
        return this;
    }

    /**
     * Build the final component with gradient applied
     */
    public MutableComponent build() {
        if (useRGB) {
            return ToolTipsGradient.RGBEachLetter(hue, text, speed);
        } else {
            return ToolTipsGradient.Gradient(hue, text, min, max);
        }
    }

    /**
     * Shortcut method to directly get the gradient component
     */
    public static MutableComponent withGradient(String text) {
        return of(text).build();
    }

    /**
     * Shortcut method with custom hue
     */
    public static MutableComponent withGradient(String text, float hue) {
        return of(text).hue(hue).build();
    }

    /**
     * Shortcut method for RGB mode
     */
    public static MutableComponent withRGB(String text) {
        return of(text).rgb().build();
    }
}
