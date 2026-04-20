package net.thejadeproject.ascension.refactor_packages.gui.elements.hud;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.layout.positioning.rules.PositioningRules;

public class HudContainer extends RenderableElement {
    public HudContainer(UIFrame frame) {
        super(frame);
        PlayerModelDisplay modelDisplay = new PlayerModelDisplay(frame,64,64);
        HealthBar healthBar = new HealthBar(frame,80,11);
        healthBar.getPositioning().setX(modelDisplay.getWidth());
        addChild(modelDisplay);
        addChild(healthBar);
    }
}
