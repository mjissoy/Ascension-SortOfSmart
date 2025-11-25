package net.thejadeproject.ascension.menus.spatialrings;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ASCItemHandler extends ItemStackHandler {
    public ASCItemHandler(int size) {
        super(size);
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
