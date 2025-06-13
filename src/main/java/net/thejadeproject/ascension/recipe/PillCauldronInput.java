package net.thejadeproject.ascension.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public class PillCauldronInput implements RecipeInput {
    public int inputSlots;
    public List<ItemStack> items;
    public PillCauldronInput(int inputSlots, List<ItemStack> item){
        this.items = item;
        this.inputSlots = inputSlots;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        if(i >= items.size()) return null;
        return items.get(i);
    }

    @Override
    public int size() {
        return 0;
    }
}
