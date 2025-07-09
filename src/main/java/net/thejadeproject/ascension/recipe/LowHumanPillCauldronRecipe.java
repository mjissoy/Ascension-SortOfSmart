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
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LowHumanPillCauldronRecipe implements Recipe<PillCauldronInput> {
    private static final int MAX_INPUT_ITEMS = 3;
    final NonNullList<SizedIngredient> ingredients;
    final double chance;
    final ItemStack success;
    final ItemStack fail;

    public LowHumanPillCauldronRecipe(NonNullList<SizedIngredient> ingredients, ItemStack success,ItemStack fail,double chance){

        this.ingredients = ingredients;
        this.chance = chance;
        this.success = success;
        this.fail = fail;

    }

    public static class Serializer implements RecipeSerializer<LowHumanPillCauldronRecipe> {
        public static final MapCodec<LowHumanPillCauldronRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                SizedIngredient.FLAT_CODEC
                        .listOf()
                        .fieldOf("ingredients")
                        .flatXmap(
                                inputs ->{

                                    if (inputs.isEmpty()) {
                                        return DataResult.error(() -> "No ingredients");
                                    } else if (inputs.size() > MAX_INPUT_ITEMS) {
                                        return DataResult.error(() -> "Too many ingredients");
                                    }
                                    NonNullList<SizedIngredient> list = NonNullList.create();
                                    list.addAll(inputs);
                                    return DataResult.success(list);
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
            //TODO
            int i = buffer.readVarInt();
            NonNullList<SizedIngredient> nonnulllist = NonNullList.withSize(i, new SizedIngredient(Ingredient.EMPTY,1));
            nonnulllist.replaceAll(ingredient -> SizedIngredient.STREAM_CODEC.decode(buffer));
            ItemStack itemStack = ItemStack.STREAM_CODEC.decode(buffer);
            ItemStack itemStack2 = ItemStack.STREAM_CODEC.decode(buffer);
            double chance = buffer.readDouble();
            return new LowHumanPillCauldronRecipe(nonnulllist,itemStack,itemStack2,chance);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, LowHumanPillCauldronRecipe recipe) {

            buffer.writeVarInt(recipe.getSizedIngredients().size());

            for (SizedIngredient ingredient : recipe.getSizedIngredients()) {
                SizedIngredient.STREAM_CODEC.encode(buffer,ingredient);
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


    public NonNullList<SizedIngredient> getSizedIngredients() {
        return ingredients;
    }

    //TODO
    @Override
    public boolean matches(@NotNull PillCauldronInput pillCauldronInput, Level level) {
        if (level.isClientSide()) {
            return false;
        }
        if(pillCauldronInput.items.isEmpty()) return false;
        for(ItemStack item:pillCauldronInput.items){

            boolean found = false;
            for(SizedIngredient ingredient: getSizedIngredients()){

                found = ingredient.test(item);
                if(found) break;
            }
            if(found) continue;
            return false;
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
