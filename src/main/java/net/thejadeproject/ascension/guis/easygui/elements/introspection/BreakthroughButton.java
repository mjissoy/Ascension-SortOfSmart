package net.thejadeproject.ascension.guis.easygui.elements.introspection;

import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;

public class BreakthroughButton extends MainContainerEmptyButton {
    private final TextureDataSubSection buttonTexture =  new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            0,202,80,214
    );
    public BreakthroughButton(IEasyGuiScreen screen, int x, int y, MainContainer container) {
        super(screen, x, y, 80, 12, container);
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        buttonTexture.renderTexture(guiGraphics);
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);

    }
}
