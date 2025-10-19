package net.thejadeproject.ascension.menus.spatialrings;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class SRContainerSlot extends SlotItemHandler {
    private int index;
    public SRContainerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.index = index;
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
}

