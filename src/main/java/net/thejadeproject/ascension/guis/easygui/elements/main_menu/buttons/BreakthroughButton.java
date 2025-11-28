package net.thejadeproject.ascension.guis.easygui.elements.main_menu.buttons;

import net.lucent.easygui.elements.controls.buttons.AbstractButton;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.guis.easygui.elements.main_menu.path_data.DisplayPathDataContainer;
import net.thejadeproject.ascension.network.serverBound.TriggerMajorRealmBreakthrough;
@OnlyIn(Dist.CLIENT)
public class BreakthroughButton extends AbstractButton {

    public ITextureData textureData = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/gui_all.png"),
            256,
            256,
            56,153,
            56+6,153+4
    );
    public ITextureData hoverTexture = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,
                    "textures/gui/screen/gui_all.png"),
            256,
            256,
            56, 158,
            56+6, 158+4
    );

    public BreakthroughButton(IEasyGuiScreen screen,int x, int y){
        super(screen,x,y,6,4);

    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(isHovered()) hoverTexture.renderTexture(guiGraphics);
        else textureData.renderTexture(guiGraphics);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
        super.onClick(mouseX, mouseY, button, clicked);
        if (clicked) PacketDistributor.sendToServer(new TriggerMajorRealmBreakthrough(((DisplayPathDataContainer) screen.getElementByID("path_data_display_container")).pathData.pathId.toString()));
    }
}
