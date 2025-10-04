package net.thejadeproject.ascension.menus.custom.pill_cauldron;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.compat.jei.PillCauldronRecipeCategory;
import net.thejadeproject.ascension.compat.jei.JEIModPlugin;

public class PillCauldronLowHumanScreen extends AbstractContainerScreen<PillCauldronLowHumanMenu> {

    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/pill_cauldron_low_human/pill_cauldron_low_human_menu.png");
    private static final ResourceLocation ARROW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/arrow_progress.png");

    public PillCauldronLowHumanScreen(PillCauldronLowHumanMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(guiGraphics, x, y);
        renderHeatBar(guiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            guiGraphics.blit(ARROW_TEXTURE, x + 80, y + 33, 0, 0, 16, menu.getScaledArrowProgress(), 16, 21);
        }
    }

    private int getHeatColor(int percentage) {
        // Color gradient from blue (cold) to red (hot)
        if (percentage < 25) {
            return 0xFF0000FF; // Blue
        } else if (percentage < 50) {
            return 0xFF00FF00; // Green
        } else if (percentage < 75) {
            return 0xFFFFFF00; // Yellow
        } else {
            return 0xFFFF0000; // Red
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);

        String heatText = "Heat: " + menu.getHeatText();
        guiGraphics.drawString(this.font, heatText, 5, 41, 0x404040, false);
    }

    private void renderHeatBar(GuiGraphics guiGraphics, int x, int y) {
        int heatPercentage = menu.getHeatPercentage();
        int heatBarWidth = (int) (50 * (heatPercentage / 100.0));

        int barX = x + 5;
        int barY = y + 49;

        guiGraphics.fill(barX, barY, barX + 50, barY + 5, 0xFF555555);

        int color = getHeatColor(heatPercentage);
        guiGraphics.fill(barX, barY, barX + heatBarWidth, barY + 5, color);
    }

    @Override
    protected void renderSlot(GuiGraphics guiGraphics, Slot slot) {
        super.renderSlot(guiGraphics, slot);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        if (isHovering(5, 49, 50, 5, pMouseX, pMouseY)) {
            pGuiGraphics.renderTooltip(this.font,
                    Component.literal("Heat: " + menu.getHeatLevel() + "°C / " + menu.getMaxHeat() + "°C"),
                    pMouseX, pMouseY);
        }

        // Add tooltip for progress arrow
        if (isHovering(80, 33, 16, 21, pMouseX, pMouseY)) {
            pGuiGraphics.renderTooltip(this.font,
                    Component.literal("Click to view recipes"),
                    pMouseX, pMouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) { // Left click
            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;

            // Check if clicked on progress arrow
            if (isHovering(80, 33, 16, 21, mouseX, mouseY)) {
                showJeiRecipes();
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void showJeiRecipes() {
        // Get JEI runtime and show recipes for the pill cauldron
        try {
            IJeiRuntime jeiRuntime = JEIModPlugin.getJeiRuntime();
            if (jeiRuntime != null) {
                // Simple approach - just show all recipes for our category
                jeiRuntime.getRecipesGui().showTypes(
                        java.util.List.of(PillCauldronRecipeCategory.CAULDRON_RECIPE_TYPE)
                );
            }
        } catch (Exception e) {
            AscensionCraft.LOGGER.warn("Could not open JEI recipes: {}", e.getMessage());
        }
    }
}
