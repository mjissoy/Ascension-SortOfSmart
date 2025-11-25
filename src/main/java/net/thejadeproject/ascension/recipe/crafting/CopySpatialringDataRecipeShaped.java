package net.thejadeproject.ascension.recipe.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.items.artifacts.SpatialRingItem;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class CopySpatialringDataRecipeShaped extends ShapedRecipe {
    public CopySpatialringDataRecipeShaped(final String group, CraftingBookCategory category, ShapedRecipePattern pattern, final ItemStack recipeOutput) {
        super(group, category, pattern, recipeOutput);
    }

    public CopySpatialringDataRecipeShaped(ShapedRecipe shapedRecipe) {
        super(shapedRecipe.getGroup(), shapedRecipe.category(), shapedRecipe.pattern, shapedRecipe.getResultItem(RegistryAccess.EMPTY));
    }

    @Override
    @Nonnull
    public ItemStack assemble(@Nonnull CraftingInput inv, @Nonnull HolderLookup.Provider provider) {
        final ItemStack craftingResult = super.assemble(inv, provider);
        ItemStack dataSource = ItemStack.EMPTY;
        if (!craftingResult.isEmpty()) {
            for (int i = 0; i < inv.size(); i++) {
                final ItemStack item = inv.getItem(i);
                if (!item.isEmpty() && item.getItem() instanceof SpatialRingItem) {
                    dataSource = item;
                    break;
                }
            }

            if (!dataSource.isEmpty()) {
                if (dataSource.has(AscensionCraft.SPATIALRING_UUID.get())) {
                    craftingResult.set(AscensionCraft.SPATIALRING_UUID.get(), dataSource.get(AscensionCraft.SPATIALRING_UUID.get()));
                }
                else if (dataSource.has(DataComponents.CUSTOM_DATA)){ //Legacy support
                    if (dataSource.get(DataComponents.CUSTOM_DATA).contains("UUID")){
                        craftingResult.set(AscensionCraft.SPATIALRING_UUID.get(), dataSource.get(DataComponents.CUSTOM_DATA).copyTag().getUUID("UUID"));
                    }
                }
            }
        }

        return craftingResult;
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return AscensionCraft.COPYRECIPE.get();
    }

    public static class Serializer implements RecipeSerializer<CopySpatialringDataRecipeShaped> {
        private static final MapCodec<CopySpatialringDataRecipeShaped> CODEC = ShapedRecipe.Serializer.CODEC.xmap(CopySpatialringDataRecipeShaped::new, $ -> $);
        private static final StreamCodec<RegistryFriendlyByteBuf, CopySpatialringDataRecipeShaped> STREAM_CODEC = RecipeSerializer.SHAPED_RECIPE.streamCodec().map(CopySpatialringDataRecipeShaped::new, CopySpatialringDataRecipeShaped::new);
        @Override
        public @NotNull MapCodec<CopySpatialringDataRecipeShaped> codec() {
            return CODEC;
        }
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, CopySpatialringDataRecipeShaped> streamCodec() {
            return STREAM_CODEC;
        }
    }

}