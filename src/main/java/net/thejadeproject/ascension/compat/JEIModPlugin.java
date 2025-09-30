package net.thejadeproject.ascension.compat;

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
        registration.addRecipeCategories(new PillCauldronRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<LowHumanPillCauldronRecipe> cauldronRecipes = recipeManager
                .getAllRecipesFor(ModRecipes.CAULDRON_LOW_HUMAN_TYPE.get()).stream()
                .map(RecipeHolder::value)
                .toList();

        registration.addRecipes(PillCauldronRecipeCategory.CAULDRON_RECIPE_TYPE, cauldronRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.PILL_CAULDRON_HUMAN_LOW.get()),
                PillCauldronRecipeCategory.CAULDRON_RECIPE_TYPE
        );
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        jeiRuntime = runtime;
    }

    public static IJeiRuntime getJeiRuntime() {
        return jeiRuntime;
    }
}
