package net.thejadeproject.ascension.menus.spatialrings;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.thejadeproject.ascension.items.artifacts.SpatialRingItem;

import javax.annotation.Nonnull;

public class ASCItemHandler extends ItemStackHandler {
    private int stackMultiplier = 1;

    public ASCItemHandler(int size) {
        super(size);
    }

    public void setStackMultiplier(int multiplier) {
        this.stackMultiplier = Math.max(1, multiplier);
    }

    @Override
    public int getSlotLimit(int slot) {
        int baseLimit = super.getSlotLimit(slot);
        return baseLimit * stackMultiplier;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (stack.getItem() instanceof SpatialRingItem) {
            return false;
        }
        return super.isItemValid(slot, stack);
    }

    @Override
    protected void onContentsChanged(int slot) {
        SpatialRingManager.get().setDirty();
    }

    public void upgrade(int slots) {
        if (slots <= this.stacks.size())
            return;
        NonNullList<ItemStack> oldStacks = this.stacks;
        this.stacks = NonNullList.withSize(slots, ItemStack.EMPTY);
        for (int i = 0; i < oldStacks.size(); i++) {
            this.stacks.set(i, oldStacks.get(i));
        }
    }
}