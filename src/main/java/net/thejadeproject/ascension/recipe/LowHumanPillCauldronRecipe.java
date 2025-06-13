package net.thejadeproject.ascension.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LowHumanPillCauldronRecipe implements Recipe<PillCauldronInput> {
    private static final int MAX_INPUT_ITEMS = 3;
    final NonNullList<Ingredient> ingredients;
    final double chance;
    final ItemStack success;
    final ItemStack fail;

    public LowHumanPillCauldronRecipe(NonNullList<Ingredient> ingredients, ItemStack success,ItemStack fail,double chance){

        this.ingredients = ingredients;
        this.chance = chance;
        this.success = success;
        this.fail = fail;

    }

    public static class Serializer implements RecipeSerializer<LowHumanPillCauldronRecipe> {
        public static final MapCodec<LowHumanPillCauldronRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY
                        .listOf()
                        .fieldOf("ingredients")
                        .flatXmap(
                                inputs ->{

                                    Ingredient[] aingredient = inputs.toArray(Ingredient[]::new);

                                    if (aingredient.length == 0) {
                                        return DataResult.error(() -> "No ingredients for Low human pill cauldron recipe");
                                    } else {
                                        return aingredient.length > LowHumanPillCauldronRecipe.MAX_INPUT_ITEMS
                                                ? DataResult.error(() -> "Too many ingredients for Low human pill cauldron recipe. The maximum is: %s".formatted(LowHumanPillCauldronRecipe.MAX_INPUT_ITEMS))
                                                : DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
                                    }
                                },
                                DataResult::success
                        )
                        .forGetter(recipe ->recipe.ingredients),
                ItemStack.CODEC.fieldOf("success").forGetter(recipe ->recipe.success),
                ItemStack.CODEC.fieldOf("fail").forGetter(recipe ->recipe.fail),
                Codec.DOUBLE.fieldOf("chance").forGetter(recipe ->recipe.chance)
        ).apply(inst, LowHumanPillCauldronRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, LowHumanPillCauldronRecipe> STREAM_CODEC = StreamCodec.of(
                LowHumanPillCauldronRecipe.Serializer::toNetwork, LowHumanPillCauldronRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<LowHumanPillCauldronRecipe> codec() {
            return CODEC;
        }


        @Override
        public StreamCodec<RegistryFriendlyByteBuf, LowHumanPillCauldronRecipe> streamCodec() {
            return STREAM_CODEC;
        }
        private static LowHumanPillCauldronRecipe fromNetwork(RegistryFriendlyByteBuf buffer){

            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
            nonnulllist.replaceAll(ingredient -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            ItemStack itemStack = ItemStack.STREAM_CODEC.decode(buffer);
            ItemStack itemStack2 = ItemStack.STREAM_CODEC.decode(buffer);
            double chance = buffer.readDouble();
            return new LowHumanPillCauldronRecipe(nonnulllist,itemStack,itemStack2,chance);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, LowHumanPillCauldronRecipe recipe) {

            buffer.writeVarInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(buffer, recipe.getSuccess());
            ItemStack.STREAM_CODEC.encode(buffer,recipe.getFail());

            buffer.writeDouble(recipe.getChance());

        }
    }


    public ItemStack getSuccess() {
        return success;
    }

    public ItemStack getFail() {
        return fail;
    }

    public double getChance() {
        return chance;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public boolean matches(@NotNull PillCauldronInput pillCauldronInput, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        for(ItemStack input:pillCauldronInput.items){

            boolean result = false;

            for(Ingredient ingredient: getIngredients()){

                result = ingredient.test(input);
                if (result) break;
            }
            if(result) continue;
            else return false;

        }
        return true;
    }

    public ItemStack output(){
        if(ThreadLocalRandom.current().nextDouble(0,1) < getChance()){
            return getSuccess();
        }
        return getFail();
    }

    @Override
    public ItemStack assemble(PillCauldronInput pillCauldronInput, HolderLookup.Provider provider) {
        return output();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return null;
    }


    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CAULDRON_LOW_HUMAN_SERIALIZER.get();
    }




    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CAULDRON_LOW_HUMAN_TYPE.get();
    }

}
