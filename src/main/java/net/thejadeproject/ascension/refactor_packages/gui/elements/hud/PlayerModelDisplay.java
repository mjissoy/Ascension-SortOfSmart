package net.thejadeproject.ascension.refactor_packages.gui.elements.hud;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.minecraft.client.gui.GuiGraphics;

public class PlayerModelDisplay extends RenderableElement {
    public PlayerModelDisplay(UIFrame frame,int width,int height) {
        super(frame);
        setWidth(width);
        setHeight(height);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.fill(0,0,getWidth(),getHeight(),0xFF000000);
        guiGraphics.fill(1,1,getWidth()-1,getHeight()-1,0xFF6E6E6E);
    }
}
