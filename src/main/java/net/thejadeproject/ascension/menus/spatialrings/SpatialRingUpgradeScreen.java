package net.thejadeproject.ascension.menus.spatialrings;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.thejadeproject.ascension.AscensionCraft;

public class SpatialRingUpgradeScreen extends AbstractContainerScreen<SpatialRingUpgradeContainer> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/gui/spatial_rings/spatial_ring_upgrades.png");

    public SpatialRingUpgradeScreen(SpatialRingUpgradeContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 222;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        drawTextLabels(guiGraphics, x, y);
    }

    private void drawTextLabels(GuiGraphics guiGraphics, int x, int y) {
        Component spatialStonesText = Component.translatable("container.ascension.spatial_stones");
        int spatialStonesX = x + 54;
        int spatialStonesY = y + 20;
        guiGraphics.drawCenteredString(this.font, spatialStonesText, spatialStonesX, spatialStonesY, 0x404040);

        Component otherUpgradesText = Component.translatable("container.ascension.other_upgrades");
        int otherUpgradesX = x + this.imageWidth / 4;
        int otherUpgradesY = y + 77;
        guiGraphics.drawCenteredString(this.font, otherUpgradesText, otherUpgradesX, otherUpgradesY, 0x404040);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}