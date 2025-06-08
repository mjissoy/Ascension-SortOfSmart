package net.thejadeproject.ascension.items.pills;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;

public class ItemColorProvider implements ItemColor {
    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if (stack.getItem() instanceof DynamicPillsSystem.PillItem pillItem && tintIndex == 0) {
            return pillItem.getColor();
        }
        return 0xFFFFFF; // Default white
    }
}
