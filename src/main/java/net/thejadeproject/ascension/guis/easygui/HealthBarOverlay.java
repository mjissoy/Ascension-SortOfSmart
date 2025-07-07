package net.thejadeproject.ascension.guis.easygui;

import net.lucent.easygui.holders.EasyGuiEventHolder;
import net.lucent.easygui.overlays.EasyGuiOverlay;

import java.util.function.BiConsumer;

public class HealthBarOverlay extends EasyGuiOverlay {
    public HealthBarOverlay(BiConsumer<EasyGuiEventHolder, EasyGuiOverlay> initialize) {
        super(initialize);
    }

}
