package net.thejadeproject.ascension.guis.easygui.elements;

import net.lucent.easygui.elements.controls.buttons.ColorButton;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.util.math.BoundChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class CardButton extends ColorButton {

    public String cardPhysique = null;

    public CardButton(IEasyGuiScreen screen, int width, int height,int x, int y){
        super(screen,width,height,x,y);
    }


    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
        if(isHovered()){
            BoundChecker.Vec2 point = screenToLocalPoint(mouseX,mouseY);

            if(cardPhysique != null) guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.literal(cardPhysique.split(":")[1]),point.x,point.y);
            else guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.literal("???????"),point.x,point.y);
        }
    }
}
