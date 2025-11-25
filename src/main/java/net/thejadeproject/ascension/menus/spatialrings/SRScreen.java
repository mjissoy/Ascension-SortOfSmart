package net.thejadeproject.ascension.menus.spatialrings;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SRScreen extends AbstractContainerScreen<SRContainer> {
    private final ResourceLocation texture;
    private final int inventoryLabelX;
    private final int inventoryLabelY;

    public SRScreen(SRContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.texture = container.getTier().texture;
        this.imageWidth = container.getTier().xSize;
        this.imageHeight = container.getTier().ySize;

        // Use custom label positions based on the tier
        this.inventoryLabelX = container.getTier().inventoryLabelX;
        this.inventoryLabelY = container.getTier().inventoryLabelY;

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(this.texture, x, y, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Render the title (centered at the top)
        int titleWidth = this.font.width(this.title);
        int titleX = (this.imageWidth - titleWidth) / 2;
        guiGraphics.drawString(this.font, this.title, titleX, this.titleLabelY, 0x404040, false);

        // Render the player inventory label at custom position
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}