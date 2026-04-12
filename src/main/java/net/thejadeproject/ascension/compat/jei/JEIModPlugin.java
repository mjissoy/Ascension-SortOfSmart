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
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.recipe.LowHumanPillCauldronRecipe;
import net.thejadeproject.ascension.recipe.ModRecipes;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        // JEI calls this after the client has joined a world and synced recipes.
        // Defensive null-check: if called before level is available, register nothing.
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.level.getRecipeManager() == null) {
            registration.addRecipes(PillCauldronRecipeCategory.CAULDRON_RECIPE_TYPE,
                    Collections.emptyList());
            return;
        }

        try {
            List<LowHumanPillCauldronRecipe> recipes = mc.level.getRecipeManager()
                    .getAllRecipesFor(ModRecipes.CAULDRON_LOW_HUMAN_TYPE.get())
                    .stream()
                    .map(RecipeHolder::value)
                    .collect(Collectors.toList());

            registration.addRecipes(PillCauldronRecipeCategory.CAULDRON_RECIPE_TYPE, recipes);
        } catch (Exception e) {
            AscensionCraft.LOGGER.warn("JEI: Failed to load pill cauldron recipes: {}", e.getMessage());
            registration.addRecipes(PillCauldronRecipeCategory.CAULDRON_RECIPE_TYPE,
                    Collections.emptyList());
        }
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