package net.thejadeproject.ascension.guis.easygui.elements;

import net.lucent.easygui.elements.controls.buttons.AbstractButton;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.thejadeproject.ascension.guis.easygui.elements.introspection.MainContainer;

public class EmptyButton extends AbstractButton {

    public EmptyButton(IEasyGuiScreen screen, int x, int y, int width, int height){
        super(screen,x,y,width,height);

    }
    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        if(isHovered() && isActive() && isVisible()){
            guiGraphics.fill(0,0,getWidth(),getHeight(),1686472069);
        }
    }
}
