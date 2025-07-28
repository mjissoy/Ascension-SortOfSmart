package net.thejadeproject.ascension.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.items.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }


    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        List<ItemLike> JADE_SMELTABLES = List.of(ModItems.RAW_JADE,
                ModBlocks.JADE_ORE);


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.JADE_BLOCK.get())
                .pattern("JJJ")
                .pattern("JJJ")
                .pattern("JJJ")
                .define('J', ModItems.JADE_INGOT.get())
                .unlockedBy("has_jade_ingot1", has(ModItems.JADE_INGOT)).save(recipeOutput, "ascension:shaped/jade_block_from_jade");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.JADE_INGOT.get())
                .pattern("JJJ")
                .pattern("JJJ")
                .pattern("JJJ")
                .define('J', ModItems.JADE_NUGGET.get())
                .unlockedBy("has_jade_ingot", has(ModItems.JADE_INGOT)).save(recipeOutput, "ascension:shaped/jade_ingot_from_nugget");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.JADE_INGOT.get(), 9)
                .requires(ModBlocks.JADE_BLOCK)
                .unlockedBy("has_block_of_jade", has(ModBlocks.JADE_BLOCK)).save(recipeOutput, "ascension:shapeless/jade_from_jade_block");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.JADE_NUGGET.get(), 9)
                .requires(ModItems.JADE_INGOT)
                .unlockedBy("has_jade", has(ModItems.JADE_INGOT)).save(recipeOutput, "ascension:shapeless/jade_nugget_from_jade");


        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_MARBLE.get(), ModBlocks.RAW_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_BURNED_MARBLE.get(), ModBlocks.CHARRED_MARBLE.get());



        oreSmelting(recipeOutput, JADE_SMELTABLES, RecipeCategory.MISC, ModItems.JADE_INGOT.get(), 0.25f, 200, "jade");
        oreBlasting(recipeOutput, JADE_SMELTABLES, RecipeCategory.MISC, ModItems.JADE_INGOT.get(), 0.30f, 100, "jade");

        stairBuilder(ModBlocks.GOLDEN_PALM_STAIRS.get(), Ingredient.of(ModBlocks.GOLDEN_PALM_PLANKS)).group("golden_palm_planks")
                .unlockedBy("has_golden_palm_planks", has(ModBlocks.GOLDEN_PALM_PLANKS)).save(recipeOutput);

        stairBuilder(ModBlocks.MARBLE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.MARBLE_TILES)).group("marble_tiles")
                .unlockedBy("has_marble_tiles", has(ModBlocks.MARBLE_TILES)).save(recipeOutput);
        stairBuilder(ModBlocks.MARBLE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.MARBLE_BRICKS)).group("marble_bricks")
                .unlockedBy("has_marble_bricks", has(ModBlocks.MARBLE_BRICKS)).save(recipeOutput);
        stairBuilder(ModBlocks.CHARRED_MARBLE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.CHARRED_MARBLE_TILES)).group("burned_marble_tiles")
                .unlockedBy("has_marble_tiles", has(ModBlocks.CHARRED_MARBLE_TILES)).save(recipeOutput);
        stairBuilder(ModBlocks.CHARRED_MARBLE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.CHARRED_MARBLE_BRICKS)).group("burned_marble_bricks")
                .unlockedBy("has_marble_bricks", has(ModBlocks.CHARRED_MARBLE_BRICKS)).save(recipeOutput);


        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLDEN_PALM_SLAB.get(), ModBlocks.GOLDEN_PALM_PLANKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MARBLE_TILE_SLABS.get(), ModBlocks.MARBLE_TILES.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MARBLE_BRICK_SLABS.get(), ModBlocks.MARBLE_BRICKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHARRED_MARBLE_TILE_SLABS.get(), ModBlocks.CHARRED_MARBLE_TILES.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHARRED_MARBLE_BRICK_SLABS.get(), ModBlocks.CHARRED_MARBLE_BRICKS.get());

        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MARBLE_BRICK_WALLS.get(), ModBlocks.MARBLE_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MARBLE_TILE_WALLS.get(), ModBlocks.MARBLE_TILES.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BURNED_MARBLE_BRICK_WALLS.get(), ModBlocks.CHARRED_MARBLE_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BURNED_MARBLE_TILE_WALLS.get(), ModBlocks.CHARRED_MARBLE_TILES.get());

        buttonBuilder(ModBlocks.GOLDEN_PALM_BUTTON.get(), Ingredient.of(ModBlocks.GOLDEN_PALM_PLANKS.get())).group("golden_palm_planks")
                .unlockedBy("has_golden_palm_planks", has(ModBlocks.GOLDEN_PALM_PLANKS.get())).save(recipeOutput);
        pressurePlate(recipeOutput, ModBlocks.GOLDEN_PALM_PRESSURE_PLATE.get(), ModBlocks.GOLDEN_PALM_PLANKS.get());

        fenceBuilder(ModBlocks.GOLDEN_PALM_FENCE.get(), Ingredient.of(ModBlocks.GOLDEN_PALM_PLANKS.get())).group("golden_palm_planks")
                .unlockedBy("has_golden_palm_planks", has(ModBlocks.GOLDEN_PALM_PLANKS.get())).save(recipeOutput);
        fenceGateBuilder(ModBlocks.GOLDEN_PALM_FENCE_GATE.get(), Ingredient.of(ModBlocks.GOLDEN_PALM_PLANKS.get())).group("golden_palm_planks")
                .unlockedBy("has_golden_palm_planks", has(ModBlocks.GOLDEN_PALM_PLANKS.get())).save(recipeOutput);

        doorBuilder(ModBlocks.GOLDEN_PALM_DOOR.get(), Ingredient.of(ModBlocks.GOLDEN_PALM_PLANKS.get())).group("golden_palm_planks")
                .unlockedBy("has_golden_palm_planks", has(ModBlocks.GOLDEN_PALM_PLANKS.get())).save(recipeOutput);
        trapdoorBuilder(ModBlocks.GOLDEN_PALM_TRAPDOOR.get(), Ingredient.of(ModBlocks.GOLDEN_PALM_PLANKS.get())).group("golden_palm_planks")
                .unlockedBy("has_golden_palm_planks", has(ModBlocks.GOLDEN_PALM_PLANKS.get())).save(recipeOutput);


    }


    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, AscensionCraft.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
