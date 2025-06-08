package net.thejadeproject.ascension.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class PillCauldronHumanRecipeSerializer implements RecipeSerializer<PillCauldronHumanRecipe> {
    public static final PillCauldronHumanRecipeSerializer INSTANCE = new PillCauldronHumanRecipeSerializer();

    @Override
    public PillCauldronHumanRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        JsonArray inputArray = json.getAsJsonArray("inputs");
        NonNullList<Ingredient> inputs = NonNullList.withSize(3, Ingredient.EMPTY);
        for (int i =0; i < 3 && i < inputArray.size(); i++) {
            inputs.set(i, Ingredient.fromJson(inputArray.get(i)));
        }

        JsonArray outputArray = json.getAsJsonArray("outputs");
        ItemStack[] outputs = new ItemStack[2];
        for (int i = 0; i < 2 && i < outputArray.size(); i++) {
            outputs[i] = ShapedRecipe.itemStackFromJson(outputArray.get(i).getAsJsonObject());
        }

        int heatRequired = json.get("heat_required").getAsInt();
        return new PillCauldronHumanRecipe(inputs, outputs, heatRequired)
    }

    @Override
    public PillCauldronHumanRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        NonNullList<Ingredient> inputs = NonNullList.withSize(3, Ingredient.EMPTY);
        for (int i = 0; i < 3; i++) {
            inputs.set(i, Ingredient.fromNetwork(buffer));
        }

        ItemStack[] outputs = new ItemStack[2];
        for (int i = 0; i < 2; i++) {
            outputs[i] = buffer.readItem();
        }

        int heatRequired = buffer.readInt();
        return new PillCauldronHumanRecipe(inputs, outputs, heatRequired);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, PillCauldronHumanRecipe recipe) {
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.toNetwork(buffer);
        }
        for (ItemStack output : recipe.getOutputs()) {
            buffer.writeItem(output);
        }
        buffer.writeInt(recipe.getHeatRequired());
    }
}
