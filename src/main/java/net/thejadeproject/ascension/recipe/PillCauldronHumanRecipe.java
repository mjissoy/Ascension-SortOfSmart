package net.thejadeproject.ascension.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;

public class PillCauldronHumanRecipe implements Recipe<IItemHandler> {
    public static final RecipeType<PillCauldronHumanRecipe> TYPE = RecipeType.register("ascension:recipe/pill_cauldron_human");
    private final NonNullList<Ingredient> inputs;
    private final ItemStack[] outputs;
    private final int heatRequired;

    @Override
    public boolean matches(IItemHandler inv, Level level) {
        for (int i = 0; i < 3; i++) {
            if (!inputs.get(i).test(inv.getStackInSlot(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(IItemHandler inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 1;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY; // Not used, handled in block entity
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputs;
    }

    public int getHeatRequired() {
        return heatRequired;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PillCauldronHumanRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }

    public ItemStack[] getOutputs() {
    }
}

