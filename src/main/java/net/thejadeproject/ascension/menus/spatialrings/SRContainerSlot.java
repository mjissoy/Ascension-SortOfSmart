package net.thejadeproject.ascension.menus.spatialrings;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.thejadeproject.ascension.items.artifacts.SpatialRingItem;

import javax.annotation.Nonnull;

public class SRContainerSlot extends SlotItemHandler {
    private int index;
    public SRContainerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.index = index;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        // Prevent spatial rings or Shulker box from being placed in spatial ring slots
        return  !(SpatialRingItem.isSpatialring(stack) || isShulkerBox(stack));
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return stack.getMaxStackSize();
    }

    //bandage till forge PR fixes this
    @Override
    public void initialize(@Nonnull ItemStack itemStack) {
        ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(index, itemStack);
    }

    private boolean isShulkerBox(ItemStack stack) {
        // Check if the item is a ShulkerBoxItem
        return stack.getItem() instanceof net.minecraft.world.item.BlockItem blockItem &&
                blockItem.getBlock() instanceof net.minecraft.world.level.block.ShulkerBoxBlock;
    }
}