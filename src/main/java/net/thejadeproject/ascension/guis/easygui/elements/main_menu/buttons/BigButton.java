package net.thejadeproject.ascension.guis.easygui.elements.main_menu.buttons;

import net.lucent.easygui.elements.controls.buttons.AbstractButton;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BigButton extends AbstractButton {

    public ITextureData textureData;

    public BigButton(IEasyGuiScreen screen, int x, int y, ITextureData texture){
        super(screen,x,y,32,32);
        this.setCustomScale(0.4);
        this.setX(129);
        this.setY(7);
        this.textureData = texture;
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        textureData.renderTexture(guiGraphics);
        if(isHovered()) guiGraphics.fill(4,4,29,29,1686472069);
    }
}