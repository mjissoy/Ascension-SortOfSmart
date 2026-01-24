package net.thejadeproject.ascension.menus.spatialrings;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.thejadeproject.ascension.AscensionCraft;

public class SpatialRingStorageScreen extends AbstractContainerScreen<SpatialRingStorageContainer> {
    private static final ResourceLocation CHEST_GUI = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/spatial_rings/spatial_ring.png");
    private static final ResourceLocation SCROLLBAR_TEXTURE = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/spatial_rings/scroll_bar.png");

    private static final int SCROLLBAR_X = 175;
    private static final int SCROLLBAR_Y = 18;
    private static final int SCROLLBAR_TRACK_HEIGHT = 108;
    private static final int SCROLLBAR_WIDTH = 12;
    private static final int SCROLLBAR_THUMB_HEIGHT = 15;

    private static final int UP_ARROW_U = 0;
    private static final int UP_ARROW_V = 0;
    private static final int UP_ARROW_WIDTH = 8;
    private static final int UP_ARROW_HEIGHT = 8;

    private static final int DOWN_ARROW_U = 0;
    private static final int DOWN_ARROW_V = 83;
    private static final int DOWN_ARROW_WIDTH = 8;
    private static final int DOWN_ARROW_HEIGHT = 8;

    private static final int THUMB_U = 11;
    private static final int THUMB_V = 11;
    private static final int THUMB_WIDTH = 6;
    private static final int THUMB_HEIGHT = 16;

    private static final int TRACK_U = 0;
    private static final int TRACK_V = 10;
    private static final int TRACK_WIDTH = 8;
    private static final int TRACK_TEXTURE_HEIGHT = 71;
    private static final int TRACK_TOP_HEIGHT = 1;
    private static final int TRACK_MIDDLE_HEIGHT = 69;
    private static final int TRACK_BOTTOM_HEIGHT = 1;

    private static final int VISIBLE_ROWS = 6;
    private static final int GAP = 2;

    private boolean isDraggingScrollbar = false;
    private boolean isMouseOverUpArrow = false;
    private boolean isMouseOverDownArrow = false;
    private boolean isMouseOverThumb = false;

    private long lastArrowClickTime = 0;
    private static final long ARROW_CLICK_DELAY = 150;

    public SpatialRingStorageScreen(SpatialRingStorageContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.imageWidth = 176;
        int visibleRows = Math.min(container.getTotalRows(), VISIBLE_ROWS);
        this.imageHeight = 17 + visibleRows * 18 + 97;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CHEST_GUI);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        int totalRows = this.menu.getTotalRows();
        int visibleRows = Math.min(totalRows, VISIBLE_ROWS);

        guiGraphics.blit(CHEST_GUI, x, y, 0, 0, this.imageWidth, 17);

        for (int i = 0; i < visibleRows; i++) {
            guiGraphics.blit(CHEST_GUI, x, y + 17 + i * 18, 0, 17, this.imageWidth, 18);
        }

        int playerInventoryY = y + 17 + visibleRows * 18;
        guiGraphics.blit(CHEST_GUI, x, playerInventoryY, 0, 125, this.imageWidth, 97);

        if (totalRows > visibleRows) {
            updateMouseOverState(x, y, mouseX, mouseY);
            drawScrollbar(guiGraphics, x, y, mouseX, mouseY);
        }
    }

    private void updateMouseOverState(int guiLeft, int guiTop, double mouseX, double mouseY) {
        isMouseOverUpArrow = false;
        isMouseOverDownArrow = false;
        isMouseOverThumb = false;

        int maxScroll = this.menu.getTotalRows() - VISIBLE_ROWS;
        float scrollPercent = maxScroll > 0 ? (float) this.menu.getScrollOffset() / maxScroll : 0;

        int middleHeight = SCROLLBAR_TRACK_HEIGHT - UP_ARROW_HEIGHT - DOWN_ARROW_HEIGHT - GAP * 2 - TRACK_TOP_HEIGHT - TRACK_BOTTOM_HEIGHT;
        int availableHeight = middleHeight - SCROLLBAR_THUMB_HEIGHT;

        int thumbY = guiTop + SCROLLBAR_Y + UP_ARROW_HEIGHT + GAP + TRACK_TOP_HEIGHT +
                (int) (scrollPercent * Math.max(0, availableHeight));

        if (mouseX >= guiLeft + SCROLLBAR_X && mouseX <= guiLeft + SCROLLBAR_X + SCROLLBAR_WIDTH &&
                mouseY >= guiTop + SCROLLBAR_Y && mouseY <= guiTop + SCROLLBAR_Y + UP_ARROW_HEIGHT) {
            isMouseOverUpArrow = true;
        }
        else if (mouseX >= guiLeft + SCROLLBAR_X && mouseX <= guiLeft + SCROLLBAR_X + SCROLLBAR_WIDTH &&
                mouseY >= guiTop + SCROLLBAR_Y + SCROLLBAR_TRACK_HEIGHT - DOWN_ARROW_HEIGHT &&
                mouseY <= guiTop + SCROLLBAR_Y + SCROLLBAR_TRACK_HEIGHT) {
            isMouseOverDownArrow = true;
        }
        else if (mouseX >= guiLeft + SCROLLBAR_X && mouseX <= guiLeft + SCROLLBAR_X + SCROLLBAR_WIDTH &&
                mouseY >= thumbY && mouseY <= thumbY + SCROLLBAR_THUMB_HEIGHT) {
            isMouseOverThumb = true;
        }
    }

    private void drawScrollbar(GuiGraphics guiGraphics, int guiLeft, int guiTop, int mouseX, int mouseY) {
        int maxScroll = this.menu.getTotalRows() - VISIBLE_ROWS;
        float scrollPercent = maxScroll > 0 ? (float) this.menu.getScrollOffset() / maxScroll : 0;

        int middleHeight = SCROLLBAR_TRACK_HEIGHT - UP_ARROW_HEIGHT - DOWN_ARROW_HEIGHT - GAP * 2 - TRACK_TOP_HEIGHT - TRACK_BOTTOM_HEIGHT;
        int availableHeight = Math.max(0, middleHeight - SCROLLBAR_THUMB_HEIGHT);

        int thumbY = guiTop + SCROLLBAR_Y + UP_ARROW_HEIGHT + GAP + TRACK_TOP_HEIGHT +
                (int) (scrollPercent * availableHeight);

        RenderSystem.setShaderTexture(0, SCROLLBAR_TEXTURE);

        int arrowXOffset = (SCROLLBAR_WIDTH - UP_ARROW_WIDTH) / 2;

        guiGraphics.blit(SCROLLBAR_TEXTURE,
                guiLeft + SCROLLBAR_X + arrowXOffset, guiTop + SCROLLBAR_Y,
                UP_ARROW_U, UP_ARROW_V,
                UP_ARROW_WIDTH, UP_ARROW_HEIGHT,
                40, 128);

        if (isMouseOverUpArrow) {
            guiGraphics.fill(guiLeft + SCROLLBAR_X + arrowXOffset, guiTop + SCROLLBAR_Y,
                    guiLeft + SCROLLBAR_X + arrowXOffset + UP_ARROW_WIDTH, guiTop + SCROLLBAR_Y + UP_ARROW_HEIGHT,
                    0x40FFFFFF);
        }

        int trackXOffset = (SCROLLBAR_WIDTH - TRACK_WIDTH) / 2;
        int trackYStart = guiTop + SCROLLBAR_Y + UP_ARROW_HEIGHT + GAP;

        guiGraphics.blit(SCROLLBAR_TEXTURE,
                guiLeft + SCROLLBAR_X + trackXOffset, trackYStart,
                TRACK_U, TRACK_V,
                TRACK_WIDTH, TRACK_TOP_HEIGHT,
                40, 128);

        for (int i = 0; i < middleHeight; i++) {
            float vCoord = TRACK_V + TRACK_TOP_HEIGHT + (float) i / middleHeight * TRACK_MIDDLE_HEIGHT;
            guiGraphics.blit(SCROLLBAR_TEXTURE,
                    guiLeft + SCROLLBAR_X + trackXOffset, trackYStart + TRACK_TOP_HEIGHT + i,
                    TRACK_U, (int) vCoord,
                    TRACK_WIDTH, 1,
                    40, 128);
        }

        guiGraphics.blit(SCROLLBAR_TEXTURE,
                guiLeft + SCROLLBAR_X + trackXOffset, trackYStart + TRACK_TOP_HEIGHT + middleHeight,
                TRACK_U, TRACK_V + TRACK_TOP_HEIGHT + TRACK_MIDDLE_HEIGHT,
                TRACK_WIDTH, TRACK_BOTTOM_HEIGHT,
                40, 128);

        int thumbXOffset = (SCROLLBAR_WIDTH - THUMB_WIDTH) / 2;
        guiGraphics.blit(SCROLLBAR_TEXTURE,
                guiLeft + SCROLLBAR_X + thumbXOffset, thumbY,
                THUMB_U, THUMB_V,
                THUMB_WIDTH, THUMB_HEIGHT,
                40, 128);

        if (isMouseOverThumb || isDraggingScrollbar) {
            guiGraphics.fill(guiLeft + SCROLLBAR_X + thumbXOffset, thumbY,
                    guiLeft + SCROLLBAR_X + thumbXOffset + THUMB_WIDTH, thumbY + THUMB_HEIGHT,
                    0x40FFFFFF);
        }

        guiGraphics.blit(SCROLLBAR_TEXTURE,
                guiLeft + SCROLLBAR_X + arrowXOffset, guiTop + SCROLLBAR_Y + SCROLLBAR_TRACK_HEIGHT - DOWN_ARROW_HEIGHT,
                DOWN_ARROW_U, DOWN_ARROW_V,
                DOWN_ARROW_WIDTH, DOWN_ARROW_HEIGHT,
                40, 128);

        if (isMouseOverDownArrow) {
            guiGraphics.fill(guiLeft + SCROLLBAR_X + arrowXOffset, guiTop + SCROLLBAR_Y + SCROLLBAR_TRACK_HEIGHT - DOWN_ARROW_HEIGHT,
                    guiLeft + SCROLLBAR_X + arrowXOffset + DOWN_ARROW_WIDTH, guiTop + SCROLLBAR_Y + SCROLLBAR_TRACK_HEIGHT,
                    0x40FFFFFF);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int guiLeft = (this.width - this.imageWidth) / 2;
        int guiTop = (this.height - this.imageHeight) / 2;

        if (this.menu.getTotalRows() > VISIBLE_ROWS) {
            updateMouseOverState(guiLeft, guiTop, mouseX, mouseY);

            if (isMouseOverUpArrow) {
                handleUpArrowClick();
                return true;
            }

            if (isMouseOverDownArrow) {
                handleDownArrowClick();
                return true;
            }

            if (isMouseOverThumb) {
                isDraggingScrollbar = true;
                return true;
            }

            if (mouseX >= guiLeft + SCROLLBAR_X && mouseX <= guiLeft + SCROLLBAR_X + SCROLLBAR_WIDTH &&
                    mouseY >= guiTop + SCROLLBAR_Y && mouseY <= guiTop + SCROLLBAR_Y + SCROLLBAR_TRACK_HEIGHT) {
                if (!isMouseOverUpArrow && !isMouseOverDownArrow && !isMouseOverThumb) {
                    handleTrackClick(mouseY, guiTop);
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void handleUpArrowClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastArrowClickTime > ARROW_CLICK_DELAY) {
            int newOffset = Math.max(0, this.menu.getScrollOffset() - 1);
            this.menu.setScrollOffset(newOffset);
            lastArrowClickTime = currentTime;
        }
    }

    private void handleDownArrowClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastArrowClickTime > ARROW_CLICK_DELAY) {
            int maxScroll = Math.max(0, this.menu.getTotalRows() - VISIBLE_ROWS);
            int newOffset = Math.min(maxScroll, this.menu.getScrollOffset() + 1);
            this.menu.setScrollOffset(newOffset);
            lastArrowClickTime = currentTime;
        }
    }

    private void handleTrackClick(double mouseY, int guiTop) {
        int maxScroll = this.menu.getTotalRows() - VISIBLE_ROWS;
        if (maxScroll <= 0) return;

        int middleHeight = SCROLLBAR_TRACK_HEIGHT - UP_ARROW_HEIGHT - DOWN_ARROW_HEIGHT - GAP * 2 - TRACK_TOP_HEIGHT - TRACK_BOTTOM_HEIGHT;
        int availableHeight = Math.max(0, middleHeight - SCROLLBAR_THUMB_HEIGHT);

        float mousePercent = (float) (mouseY - (guiTop + SCROLLBAR_Y + UP_ARROW_HEIGHT + GAP + TRACK_TOP_HEIGHT + SCROLLBAR_THUMB_HEIGHT / 2)) / availableHeight;
        mousePercent = Math.max(0, Math.min(1, mousePercent));

        int newOffset = Math.round(mousePercent * maxScroll);
        this.menu.setScrollOffset(newOffset);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isDraggingScrollbar) {
            isDraggingScrollbar = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isDraggingScrollbar) {
            int guiLeft = (this.width - this.imageWidth) / 2;
            int guiTop = (this.height - this.imageHeight) / 2;
            updateScrollFromMouse(mouseY, guiTop);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (this.menu.getTotalRows() > VISIBLE_ROWS) {
            int newOffset = this.menu.getScrollOffset() - (int) Math.signum(scrollY);
            this.menu.setScrollOffset(newOffset);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    private void updateScrollFromMouse(double mouseY, int guiTop) {
        int maxScroll = this.menu.getTotalRows() - VISIBLE_ROWS;
        if (maxScroll <= 0) return;

        int middleHeight = SCROLLBAR_TRACK_HEIGHT - UP_ARROW_HEIGHT - DOWN_ARROW_HEIGHT - GAP * 2 - TRACK_TOP_HEIGHT - TRACK_BOTTOM_HEIGHT;
        int availableHeight = Math.max(0, middleHeight - SCROLLBAR_THUMB_HEIGHT);

        float mousePercent = (float) (mouseY - (guiTop + SCROLLBAR_Y + UP_ARROW_HEIGHT + GAP + TRACK_TOP_HEIGHT + SCROLLBAR_THUMB_HEIGHT / 2)) / availableHeight;
        mousePercent = Math.max(0, Math.min(1, mousePercent));

        int newOffset = Math.round(mousePercent * maxScroll);
        this.menu.setScrollOffset(newOffset);
    }



    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}