package net.thejadeproject.ascension.gui.elements.general;

import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.screen.EasyScreen;
import net.minecraft.network.chat.Component;

public class CultivationScreen extends EasyScreen {

    public CultivationScreen(Component title, UIFrame frame) {
        super(title, frame);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}