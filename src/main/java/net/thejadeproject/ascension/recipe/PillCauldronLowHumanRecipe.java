package net.thejadeproject.ascension.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.recipe.ModRecipes;

public record PillCauldronLowHumanRecipe(Ingredient inputItem, ItemStack output) implements Recipe<PillCauldronLowHumanRecipeInput> {
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        return list;
    }

    @Override
    public boolean matches(PillCauldronLowHumanRecipeInput pillCauldronLowHumanRecipeInput, Level level) {
        if (level.isClientSide()) {
            return false;
        }
        return inputItem.test(pillCauldronLowHumanRecipeInput.getItem(0));
    }

    @Override
    public ItemStack assemble(PillCauldronLowHumanRecipeInput pillCauldronLowHumanRecipeInput, HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CAULDRON_LOW_HUMAN_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CAULDRON_LOW_HUMAN_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<PillCauldronLowHumanRecipe> {
        public static final MapCodec<PillCauldronLowHumanRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(PillCauldronLowHumanRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(PillCauldronLowHumanRecipe::output)
        ).apply(inst, PillCauldronLowHumanRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, PillCauldronLowHumanRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, PillCauldronLowHumanRecipe::inputItem,
                        ItemStack.STREAM_CODEC, PillCauldronLowHumanRecipe::output,
                        PillCauldronLowHumanRecipe::new);

        @Override
        public MapCodec<PillCauldronLowHumanRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PillCauldronLowHumanRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
