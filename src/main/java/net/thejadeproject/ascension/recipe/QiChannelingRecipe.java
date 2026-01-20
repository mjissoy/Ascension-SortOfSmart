package net.thejadeproject.ascension.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class QiChannelingRecipe implements Recipe<QiChannelingRecipeInput> {
    private static final int MAX_INGREDIENTS = 1;

    final NonNullList<SizedIngredient> ingredients;
    final double requiredQi;
    final int ticksRequired;
    final ItemStack output;
    final NonNullList<DataComponentPatch> componentPatches;

    public QiChannelingRecipe(NonNullList<SizedIngredient> ingredients, double requiredQi, int ticksRequired,
                              ItemStack output, NonNullList<DataComponentPatch> componentPatches) {
        this.ingredients = ingredients;
        this.requiredQi = requiredQi;
        this.ticksRequired = ticksRequired;
        this.output = output;
        this.componentPatches = componentPatches;
    }

    public static class Serializer implements RecipeSerializer<QiChannelingRecipe> {
        // FIX: Use SizedIngredient.of() instead of SizedIngredient.EMPTY
        private static final SizedIngredient DEFAULT_INGREDIENT = new SizedIngredient(Ingredient.EMPTY, 1);

        public static final MapCodec<QiChannelingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                SizedIngredient.FLAT_CODEC.listOf().fieldOf("ingredients")
                        .flatXmap(
                                inputs -> {
                                    if (inputs.isEmpty()) {
                                        return DataResult.error(() -> "No ingredients specified");
                                    } else if (inputs.size() > MAX_INGREDIENTS) {
                                        return DataResult.error(() -> "Too many ingredients (max " + MAX_INGREDIENTS + ")");
                                    }
                                    NonNullList<SizedIngredient> list = NonNullList.create();
                                    list.addAll(inputs);
                                    return DataResult.success(list);
                                },
                                DataResult::success
                        ).forGetter(recipe -> recipe.ingredients),
                Codec.DOUBLE.fieldOf("required_qi").forGetter(recipe -> recipe.requiredQi),
                Codec.INT.fieldOf("ticks_required").forGetter(recipe -> recipe.ticksRequired),
                ItemStack.CODEC.fieldOf("output").forGetter(recipe -> recipe.output),
                DataComponentPatch.CODEC.listOf().optionalFieldOf("optional_component", List.of())
                        .xmap(
                                list -> {
                                    NonNullList<DataComponentPatch> patches = NonNullList.create();
                                    patches.addAll(list);
                                    return patches;
                                },
                                patches -> patches.stream().toList()
                        ).forGetter(recipe -> recipe.componentPatches)
        ).apply(inst, QiChannelingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, QiChannelingRecipe> STREAM_CODEC = StreamCodec.of(
                QiChannelingRecipe.Serializer::toNetwork, QiChannelingRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<QiChannelingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, QiChannelingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static QiChannelingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            int ingredientCount = buffer.readVarInt();
            // FIX: Use DEFAULT_INGREDIENT instead of EMPTY
            NonNullList<SizedIngredient> ingredients = NonNullList.withSize(ingredientCount, DEFAULT_INGREDIENT);
            for (int i = 0; i < ingredientCount; i++) {
                ingredients.set(i, SizedIngredient.STREAM_CODEC.decode(buffer));
            }

            double requiredQi = buffer.readDouble();
            int ticksRequired = buffer.readVarInt();
            ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);

            int patchCount = buffer.readVarInt();
            NonNullList<DataComponentPatch> patches = NonNullList.create();
            for (int i = 0; i < patchCount; i++) {
                patches.add(DataComponentPatch.STREAM_CODEC.decode(buffer));
            }

            return new QiChannelingRecipe(ingredients, requiredQi, ticksRequired, output, patches);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, QiChannelingRecipe recipe) {
            buffer.writeVarInt(recipe.ingredients.size());
            for (SizedIngredient ingredient : recipe.ingredients) {
                SizedIngredient.STREAM_CODEC.encode(buffer, ingredient);
            }

            buffer.writeDouble(recipe.requiredQi);
            buffer.writeVarInt(recipe.ticksRequired);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.output);

            buffer.writeVarInt(recipe.componentPatches.size());
            for (DataComponentPatch patch : recipe.componentPatches) {
                DataComponentPatch.STREAM_CODEC.encode(buffer, patch);
            }
        }
    }

    @Override
    public boolean matches(@NotNull QiChannelingRecipeInput input, @NotNull Level level) {
        if (level.isClientSide()) return false;

        if (input.getCurrentQi() < this.requiredQi) {
            return false;
        }

        if (this.ingredients.isEmpty()) return false;

        SizedIngredient required = this.ingredients.get(0);
        ItemStack mainHandItem = input.getMainHandItem();

        return !mainHandItem.isEmpty() && required.test(mainHandItem) && mainHandItem.getCount() >= required.count();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull QiChannelingRecipeInput input, @NotNull HolderLookup.Provider provider) {
        ItemStack result = this.output.copy();

        // Apply optional component patches if they exist
        if (!this.componentPatches.isEmpty() && !this.componentPatches.get(0).isEmpty()) {
            result.applyComponents(this.componentPatches.get(0));
        }

        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider provider) {
        return this.output;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.QI_CHANNELING_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipes.QI_CHANNELING_TYPE.get();
    }

    public NonNullList<SizedIngredient> getSizedIngredients() {
        return ingredients;
    }

    public double getRequiredQi() {
        return requiredQi;
    }

    public int getTicksRequired() {
        return ticksRequired;
    }

    public ItemStack getOutput() {
        return output;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> baseIngredients = NonNullList.create();
        this.ingredients.forEach(sizedIngredient -> baseIngredients.add(sizedIngredient.ingredient()));
        return baseIngredients;
    }
}