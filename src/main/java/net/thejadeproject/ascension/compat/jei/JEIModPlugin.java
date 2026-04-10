package net.thejadeproject.ascension.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.recipe.LowHumanPillCauldronRecipe;
import net.thejadeproject.ascension.recipe.ModRecipes;

import java.util.Collections;
import java.util.List;

@JeiPlugin
public class JEIModPlugin implements IModPlugin {

    private static IJeiRuntime jeiRuntime;

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new PillCauldronRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // Guard: level can be null during initial load before a world is joined.
        if (Minecraft.getInstance().level == null) {
            registration.addRecipes(PillCauldronRecipeCategory.CAULDRON_RECIPE_TYPE,
                    Collections.emptyList());
            return;
        }

        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<LowHumanPillCauldronRecipe> recipes = recipeManager
                .getAllRecipesFor(ModRecipes.CAULDRON_LOW_HUMAN_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();

        registration.addRecipes(PillCauldronRecipeCategory.CAULDRON_RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.PILL_CAULDRON_HUMAN_LOW.get()),
                PillCauldronRecipeCategory.CAULDRON_RECIPE_TYPE);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        jeiRuntime = runtime;
    }

    public static IJeiRuntime getJeiRuntime() {
        return jeiRuntime;
    }
}