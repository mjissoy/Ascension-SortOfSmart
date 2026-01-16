package net.thejadeproject.ascension.guis.easygui.elements.introspection;

import net.lucent.easygui.elements.controls.buttons.AbstractButton;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.thejadeproject.ascension.guis.easygui.elements.EmptyButton;

public class MainContainerEmptyButton extends EmptyButton {
    public MainContainer container;

    public MainContainerEmptyButton(IEasyGuiScreen screen, int x, int y, int width, int height, MainContainer container){
        super(screen,x,y,width,height);
        this.container = container;
    }

}
