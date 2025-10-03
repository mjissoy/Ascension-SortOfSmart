package net.thejadeproject.ascension.guis.easygui.elements.main_menu.buttons;

import net.lucent.easygui.elements.controls.buttons.AbstractButton;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.thejadeproject.ascension.AscensionCraft;
@OnlyIn(Dist.CLIENT)
public class SmallButton extends AbstractButton {

    public ITextureData textureData;
    public ITextureData focusedBorder = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,
                    "textures/gui/screen/gui_all.png"),
            256,
            256,
            176, 54,
            176+34, 54+21
    );
    public SmallButton(IEasyGuiScreen screen, int x, int y,ITextureData texture){
        super(screen,x,y,32,19);
        this.textureData = texture;

    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        textureData.renderTexture(guiGraphics);
        if(isHovered()) guiGraphics.fill(4,4,29,16,1686472069);
        if(isFocused()) focusedBorder.renderTexture(guiGraphics,-1,-1);

     }
}
