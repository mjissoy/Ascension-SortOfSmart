package net.thejadeproject.ascension.clients;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.thejadeproject.ascension.items.artifacts.FlameGourd.FlameGourdTooltip;

public class FlameGourdClientTooltip implements ClientTooltipComponent {
    private final FlameGourdTooltip tooltip;

    public FlameGourdClientTooltip(FlameGourdTooltip tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public int getHeight() {
        return 16; // Standard item sprite height
    }

    @Override
    public int getWidth(Font font) {
        // Calculate the width needed for the fire name + arrow + item sprite
        Component fireName = tooltip.fireBlock().getName().copy().withStyle(ChatFormatting.GRAY);
        Component arrow = Component.literal(" → ").withStyle(ChatFormatting.DARK_GRAY);

        int fireNameWidth = font.width(fireName);
        int arrowWidth = font.width(arrow);
        int itemSpriteWidth = 16; // Standard item sprite width

        return fireNameWidth + arrowWidth + itemSpriteWidth + 4; // Add some padding
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        // Get the fire name and arrow components
        Component fireName = tooltip.fireBlock().getName().copy().withStyle(ChatFormatting.GRAY);
        Component arrow = Component.literal(" → ").withStyle(ChatFormatting.DARK_GRAY);

        // Calculate positions
        int fireNameWidth = font.width(fireName);
        int arrowWidth = font.width(arrow);

        // Render the fire name
        guiGraphics.drawString(font, fireName, x, y + 4, 0xFFFFFF, false);

        // Render the arrow
        guiGraphics.drawString(font, arrow, x + fireNameWidth, y + 4, 0xFFFFFF, false);

        // Render the item sprite to the right of the arrow
        ItemStack itemStack = new ItemStack(tooltip.convertedItem());
        int spriteX = x + fireNameWidth + arrowWidth + 2; // Position sprite right after arrow with small gap
        int spriteY = y;

        guiGraphics.renderFakeItem(itemStack, spriteX, spriteY);
        guiGraphics.renderItemDecorations(font, itemStack, spriteX, spriteY);
    }
}