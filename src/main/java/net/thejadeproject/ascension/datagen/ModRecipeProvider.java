package net.thejadeproject.ascension.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.items.ModItems;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }


    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        List<ItemLike> JADE_SMELTABLES = List.of(ModBlocks.JADE_ORE);


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SPIRITUAL_STONE_BLOCK.get())
                .pattern("JJJ")
                .pattern("JJJ")
                .pattern("JJJ")
                .define('J', ModItems.SPIRITUAL_STONE.get())
                .unlockedBy("has_spiritual_stone", has(ModItems.SPIRITUAL_STONE)).save(recipeOutput, "ascension:shaped/ssb_from_spiritual_stone");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.JADE_BLOCK.get())
                .pattern("JJJ")
                .pattern("JJJ")
                .pattern("JJJ")
                .define('J', ModItems.JADE.get())
                .unlockedBy("has_jade_ingot1", has(ModItems.JADE)).save(recipeOutput, "ascension:shaped/jade_block_from_jade");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.JADE.get())
                .pattern("JJJ")
                .pattern("JJJ")
                .pattern("JJJ")
                .define('J', ModItems.JADE_NUGGET.get())
                .unlockedBy("has_jade_ingot", has(ModItems.JADE)).save(recipeOutput, "ascension:shaped/jade_ingot_from_nugget");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SPIRITUAL_STONE.get(), 9)
                .requires(ModBlocks.SPIRITUAL_STONE_BLOCK)
                .unlockedBy("has_ssb", has(ModBlocks.SPIRITUAL_STONE_BLOCK)).save(recipeOutput, "ascension:shapeless/ss_from_ssb");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.JADE.get(), 9)
                .requires(ModBlocks.JADE_BLOCK)
                .unlockedBy("has_block_of_jade", has(ModBlocks.JADE_BLOCK)).save(recipeOutput, "ascension:shapeless/jade_from_jade_block");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.JADE_NUGGET.get(), 9)
                .requires(ModItems.JADE)
                .unlockedBy("has_jade", has(ModItems.JADE)).save(recipeOutput, "ascension:shapeless/jade_nugget_from_jade");


        /** Marble Recipes */
        //Polished
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_MARBLE.get(), ModBlocks.RAW_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_LIGHT_GRAY_MARBLE.get(), ModBlocks.LIGHT_GRAY_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GRAY_POLISHED_MARBLE.get(), ModBlocks.GRAY_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_BURNED_MARBLE.get(), ModBlocks.CHARRED_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BROWN_POLISHED_MARBLE.get(), ModBlocks.BROWN_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RED_POLISHED_MARBLE.get(), ModBlocks.RED_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.ORANGE_POLISHED_MARBLE.get(), ModBlocks.ORANGE_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.YELLOW_POLISHED_MARBLE.get(), ModBlocks.YELLOW_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIME_POLISHED_MARBLE.get(), ModBlocks.LIME_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GREEN_POLISHED_MARBLE.get(), ModBlocks.GREEN_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CYAN_POLISHED_MARBLE.get(), ModBlocks.CYAN_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIGHT_BLUE_POLISHED_MARBLE.get(), ModBlocks.LIGHT_BLUE_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLUE_POLISHED_MARBLE.get(), ModBlocks.BLUE_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PURPLE_POLISHED_MARBLE.get(), ModBlocks.PURPLE_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MAGENTA_POLISHED_MARBLE.get(), ModBlocks.MAGENTA_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PINK_POLISHED_MARBLE.get(), ModBlocks.PINK_MARBLE.get());
        //Bricks
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MARBLE_BRICKS.get(), ModBlocks.POLISHED_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIGHT_GRAY_MARBLE_BRICKS.get(), ModBlocks.POLISHED_LIGHT_GRAY_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GRAY_MARBLE_BRICKS.get(), ModBlocks.GRAY_POLISHED_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHARRED_MARBLE_BRICKS.get(), ModBlocks.POLISHED_BURNED_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BROWN_MARBLE_BRICKS.get(), ModBlocks.BROWN_POLISHED_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RED_MARBLE_BRICKS.get(), ModBlocks.RED_POLISHED_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.ORANGE_MARBLE_BRICKS.get(), ModBlocks.ORANGE_POLISHED_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.YELLOW_MARBLE_BRICKS.get(), ModBlocks.YELLOW_POLISHED_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIME_MARBLE_BRICKS.get(), ModBlocks.LIME_POLISHED_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GREEN_MARBLE_BRICKS.get(), ModBlocks.GREEN_POLISHED_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CYAN_MARBLE_BRICKS.get(), ModBlocks.CYAN_POLISHED_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIGHT_BLUE_MARBLE_BRICKS.get(), ModBlocks.LIGHT_BLUE_POLISHED_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLUE_MARBLE_BRICKS.get(), ModBlocks.BLUE_POLISHED_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PURPLE_MARBLE_BRICKS.get(), ModBlocks.PURPLE_POLISHED_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MAGENTA_MARBLE_BRICKS.get(), ModBlocks.MAGENTA_POLISHED_MARBLE.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PINK_MARBLE_BRICKS.get(), ModBlocks.PINK_POLISHED_MARBLE.get());
        //Tiles
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MARBLE_TILES.get(), ModBlocks.MARBLE_BRICKS.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIGHT_GRAY_MARBLE_TILES.get(), ModBlocks.LIGHT_GRAY_MARBLE_BRICKS.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GRAY_MARBLE_TILES.get(), ModBlocks.GRAY_MARBLE_BRICKS.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHARRED_MARBLE_TILES.get(), ModBlocks.CHARRED_MARBLE_BRICKS.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BROWN_MARBLE_TILES.get(), ModBlocks.BROWN_MARBLE_BRICKS.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RED_MARBLE_TILES.get(), ModBlocks.RED_MARBLE_BRICKS.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.ORANGE_MARBLE_TILES.get(), ModBlocks.ORANGE_MARBLE_BRICKS.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.YELLOW_MARBLE_TILES.get(), ModBlocks.YELLOW_MARBLE_BRICKS.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIME_MARBLE_TILES.get(), ModBlocks.LIME_MARBLE_BRICKS.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GREEN_MARBLE_TILES.get(), ModBlocks.GREEN_MARBLE_BRICKS.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CYAN_MARBLE_TILES.get(), ModBlocks.CYAN_MARBLE_BRICKS.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIGHT_BLUE_MARBLE_TILES.get(), ModBlocks.LIGHT_BLUE_MARBLE_BRICKS.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLUE_MARBLE_TILES.get(), ModBlocks.BLUE_MARBLE_BRICKS.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PURPLE_MARBLE_TILES.get(), ModBlocks.PURPLE_MARBLE_BRICKS.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MAGENTA_MARBLE_TILES.get(), ModBlocks.MAGENTA_MARBLE_BRICKS.get());
        polished(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PINK_MARBLE_TILES.get(), ModBlocks.PINK_MARBLE_BRICKS.get());




// Define color mappings
        Map<DyeColor, Supplier<Block>> COLORED_MARBLES = Map.ofEntries(
                Map.entry(DyeColor.LIGHT_GRAY, ModBlocks.LIGHT_GRAY_MARBLE),
                Map.entry(DyeColor.GRAY, ModBlocks.GRAY_MARBLE),
                Map.entry(DyeColor.BLACK, ModBlocks.CHARRED_MARBLE), // Using charred for black
                Map.entry(DyeColor.BROWN, ModBlocks.BROWN_MARBLE),
                Map.entry(DyeColor.RED, ModBlocks.RED_MARBLE),
                Map.entry(DyeColor.ORANGE, ModBlocks.ORANGE_MARBLE),
                Map.entry(DyeColor.YELLOW, ModBlocks.YELLOW_MARBLE),
                Map.entry(DyeColor.LIME, ModBlocks.LIME_MARBLE),
                Map.entry(DyeColor.GREEN, ModBlocks.GREEN_MARBLE),
                Map.entry(DyeColor.CYAN, ModBlocks.CYAN_MARBLE),
                Map.entry(DyeColor.LIGHT_BLUE, ModBlocks.LIGHT_BLUE_MARBLE),
                Map.entry(DyeColor.BLUE, ModBlocks.BLUE_MARBLE),
                Map.entry(DyeColor.PURPLE, ModBlocks.PURPLE_MARBLE),
                Map.entry(DyeColor.MAGENTA, ModBlocks.MAGENTA_MARBLE),
                Map.entry(DyeColor.PINK, ModBlocks.PINK_MARBLE)
        );

// Generate recipes for all colors
        COLORED_MARBLES.forEach((dyeColor, coloredMarble) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, coloredMarble.get(), 8)
                    .pattern("MMM")
                    .pattern("MDM")
                    .pattern("MMM")
                    .define('D', DyeItem.byColor(dyeColor))
                    .define('M', ModBlocks.RAW_MARBLE.get())
                    .unlockedBy("has_marble", has(ModBlocks.RAW_MARBLE.get()))
                    .save(recipeOutput, "ascension:marble_coloring/" + dyeColor.getName());
        });








        oreSmelting(recipeOutput, JADE_SMELTABLES, RecipeCategory.MISC, ModItems.JADE.get(), 0.25f, 200, "jade");
        oreBlasting(recipeOutput, JADE_SMELTABLES, RecipeCategory.MISC, ModItems.JADE.get(), 0.30f, 100, "jade");



        //Stairs
        stairBuilder(ModBlocks.GOLDEN_PALM_STAIRS.get(), Ingredient.of(ModBlocks.GOLDEN_PALM_PLANKS)).group("golden_palm_planks")
                .unlockedBy("has_golden_palm_planks", has(ModBlocks.GOLDEN_PALM_PLANKS)).save(recipeOutput);

        stairBuilder(ModBlocks.MARBLE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.MARBLE_BRICKS)).group("marble_bricks")
                .unlockedBy("has_marble_bricks", has(ModBlocks.MARBLE_BRICKS)).save(recipeOutput);
        stairBuilder(ModBlocks.MARBLE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.MARBLE_TILES)).group("marble_tiles")
                .unlockedBy("has_marble_tiles", has(ModBlocks.MARBLE_TILES)).save(recipeOutput);
        stairBuilder(ModBlocks.LIGHT_GRAY_MARBLE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.LIGHT_GRAY_MARBLE_BRICKS)).group("light_gray_marble_bricks")
                .unlockedBy("has_marble_bricks", has(ModBlocks.LIGHT_GRAY_MARBLE_BRICKS)).save(recipeOutput);
        stairBuilder(ModBlocks.LIGHT_GRAY_MARBLE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.LIGHT_GRAY_MARBLE_TILES)).group("light_gray_marble_tiles")
                .unlockedBy("has_marble_tiles", has(ModBlocks.LIGHT_GRAY_MARBLE_TILES)).save(recipeOutput);
        stairBuilder(ModBlocks.GRAY_MARBLE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.GRAY_MARBLE_BRICKS)).group("gray_marble_bricks")
                .unlockedBy("has_marble_bricks", has(ModBlocks.GRAY_MARBLE_BRICKS)).save(recipeOutput);
        stairBuilder(ModBlocks.GRAY_MARBLE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.GRAY_MARBLE_TILES)).group("gray_marble_tiles")
                .unlockedBy("has_marble_tiles", has(ModBlocks.GRAY_MARBLE_TILES)).save(recipeOutput);
        stairBuilder(ModBlocks.CHARRED_MARBLE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.CHARRED_MARBLE_BRICKS)).group("burned_marble_bricks")
                .unlockedBy("has_marble_bricks", has(ModBlocks.CHARRED_MARBLE_BRICKS)).save(recipeOutput);
        stairBuilder(ModBlocks.CHARRED_MARBLE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.CHARRED_MARBLE_TILES)).group("burned_marble_tiles")
                .unlockedBy("has_marble_tiles", has(ModBlocks.CHARRED_MARBLE_TILES)).save(recipeOutput);
        stairBuilder(ModBlocks.BROWN_MARBLE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.BROWN_MARBLE_BRICKS)).group("brown_marble_bricks")
                .unlockedBy("has_marble_bricks", has(ModBlocks.BROWN_MARBLE_BRICKS)).save(recipeOutput);
        stairBuilder(ModBlocks.BROWN_MARBLE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.BROWN_MARBLE_TILES)).group("brown_marble_tiles")
                .unlockedBy("has_marble_tiles", has(ModBlocks.BROWN_MARBLE_TILES)).save(recipeOutput);
        stairBuilder(ModBlocks.RED_MARBLE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.RED_MARBLE_BRICKS)).group("red_marble_bricks")
                .unlockedBy("has_marble_bricks", has(ModBlocks.RED_MARBLE_BRICKS)).save(recipeOutput);
        stairBuilder(ModBlocks.RED_MARBLE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.RED_MARBLE_TILES)).group("red_marble_tiles")
                .unlockedBy("has_marble_tiles", has(ModBlocks.RED_MARBLE_TILES)).save(recipeOutput);
        stairBuilder(ModBlocks.ORANGE_MARBLE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.ORANGE_MARBLE_BRICKS)).group("orange_marble_bricks")
                .unlockedBy("has_marble_bricks", has(ModBlocks.ORANGE_MARBLE_BRICKS)).save(recipeOutput);
        stairBuilder(ModBlocks.ORANGE_MARBLE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.ORANGE_MARBLE_TILES)).group("orange_marble_tiles")
                .unlockedBy("has_marble_tiles", has(ModBlocks.ORANGE_MARBLE_TILES)).save(recipeOutput);
        stairBuilder(ModBlocks.YELLOW_MARBLE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.YELLOW_MARBLE_BRICKS)).group("yellow_marble_bricks")
                .unlockedBy("has_marble_bricks", has(ModBlocks.YELLOW_MARBLE_BRICKS)).save(recipeOutput);
        stairBuilder(ModBlocks.YELLOW_MARBLE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.YELLOW_MARBLE_TILES)).group("yellow_marble_tiles")
                .unlockedBy("has_marble_tiles", has(ModBlocks.YELLOW_MARBLE_TILES)).save(recipeOutput);
        stairBuilder(ModBlocks.LIME_MARBLE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.LIME_MARBLE_BRICKS)).group("lime_marble_bricks")
                .unlockedBy("has_marble_bricks", has(ModBlocks.LIME_MARBLE_BRICKS)).save(recipeOutput);
        stairBuilder(ModBlocks.LIME_MARBLE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.LIME_MARBLE_TILES)).group("lime_marble_tiles")
                .unlockedBy("has_marble_tiles", has(ModBlocks.LIME_MARBLE_TILES)).save(recipeOutput);
        stairBuilder(ModBlocks.GREEN_MARBLE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.GREEN_MARBLE_BRICKS)).group("green_marble_bricks")
                .unlockedBy("has_marble_bricks", has(ModBlocks.GREEN_MARBLE_BRICKS)).save(recipeOutput);
        stairBuilder(ModBlocks.GREEN_MARBLE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.GREEN_MARBLE_TILES)).group("green_marble_tiles")
                .unlockedBy("has_marble_tiles", has(ModBlocks.GREEN_MARBLE_TILES)).save(recipeOutput);
        stairBuilder(ModBlocks.CYAN_MARBLE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.CYAN_MARBLE_BRICKS)).group("cyan_marble_bricks")
                .unlockedBy("has_marble_bricks", has(ModBlocks.CYAN_MARBLE_BRICKS)).save(recipeOutput);
        stairBuilder(ModBlocks.CYAN_MARBLE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.CYAN_MARBLE_TILES)).group("cyan_marble_tiles")
                .unlockedBy("has_marble_tiles", has(ModBlocks.CYAN_MARBLE_TILES)).save(recipeOutput);
        stairBuilder(ModBlocks.LIGHT_BLUE_MARBLE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.LIGHT_BLUE_MARBLE_BRICKS)).group("light_blue_marble_bricks")
                .unlockedBy("has_marble_bricks", has(ModBlocks.LIGHT_BLUE_MARBLE_BRICKS)).save(recipeOutput);
        stairBuilder(ModBlocks.LIGHT_BLUE_MARBLE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.LIGHT_BLUE_MARBLE_TILES)).group("light_blue_marble_tiles")
                .unlockedBy("has_marble_tiles", has(ModBlocks.LIGHT_BLUE_MARBLE_TILES)).save(recipeOutput);
        stairBuilder(ModBlocks.BLUE_MARBLE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.BLUE_MARBLE_BRICKS)).group("blue_marble_bricks")
                .unlockedBy("has_marble_bricks", has(ModBlocks.BLUE_MARBLE_BRICKS)).save(recipeOutput);
        stairBuilder(ModBlocks.BLUE_MARBLE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.BLUE_MARBLE_TILES)).group("blue_marble_tiles")
                .unlockedBy("has_marble_tiles", has(ModBlocks.BLUE_MARBLE_TILES)).save(recipeOutput);
        stairBuilder(ModBlocks.PURPLE_MARBLE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.PURPLE_MARBLE_BRICKS)).group("purple_marble_bricks")
                .unlockedBy("has_marble_bricks", has(ModBlocks.PURPLE_MARBLE_BRICKS)).save(recipeOutput);
        stairBuilder(ModBlocks.PURPLE_MARBLE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.PURPLE_MARBLE_TILES)).group("purple_marble_tiles")
                .unlockedBy("has_marble_tiles", has(ModBlocks.PURPLE_MARBLE_TILES)).save(recipeOutput);
        stairBuilder(ModBlocks.MAGENTA_MARBLE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.MAGENTA_MARBLE_BRICKS)).group("magenta_marble_bricks")
                .unlockedBy("has_marble_bricks", has(ModBlocks.MAGENTA_MARBLE_BRICKS)).save(recipeOutput);
        stairBuilder(ModBlocks.MAGENTA_MARBLE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.MAGENTA_MARBLE_TILES)).group("magenta_marble_tiles")
                .unlockedBy("has_marble_tiles", has(ModBlocks.MAGENTA_MARBLE_TILES)).save(recipeOutput);
        stairBuilder(ModBlocks.PINK_MARBLE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.PINK_MARBLE_BRICKS)).group("pink_marble_bricks")
                .unlockedBy("has_marble_bricks", has(ModBlocks.PINK_MARBLE_BRICKS)).save(recipeOutput);
        stairBuilder(ModBlocks.PINK_MARBLE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.PINK_MARBLE_TILES)).group("pink_marble_tiles")
                .unlockedBy("has_marble_tiles", has(ModBlocks.PINK_MARBLE_TILES)).save(recipeOutput);

        //Slabs
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLDEN_PALM_SLAB.get(), ModBlocks.GOLDEN_PALM_PLANKS.get());

        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MARBLE_BRICK_SLABS.get(), ModBlocks.MARBLE_BRICKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MARBLE_TILE_SLABS.get(), ModBlocks.MARBLE_TILES.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIGHT_GRAY_MARBLE_BRICK_SLABS.get(), ModBlocks.LIGHT_GRAY_MARBLE_BRICKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIGHT_GRAY_MARBLE_TILE_SLABS.get(), ModBlocks.LIGHT_GRAY_MARBLE_TILES.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GRAY_MARBLE_BRICK_SLABS.get(), ModBlocks.GRAY_MARBLE_BRICKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GRAY_MARBLE_TILE_SLABS.get(), ModBlocks.GRAY_MARBLE_TILES.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHARRED_MARBLE_BRICK_SLABS.get(), ModBlocks.CHARRED_MARBLE_BRICKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHARRED_MARBLE_TILE_SLABS.get(), ModBlocks.CHARRED_MARBLE_TILES.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BROWN_MARBLE_BRICK_SLABS.get(), ModBlocks.BROWN_MARBLE_BRICKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BROWN_MARBLE_TILE_SLABS.get(), ModBlocks.BROWN_MARBLE_TILES.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RED_MARBLE_BRICK_SLABS.get(), ModBlocks.RED_MARBLE_BRICKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RED_MARBLE_TILE_SLABS.get(), ModBlocks.RED_MARBLE_TILES.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.ORANGE_MARBLE_BRICK_SLABS.get(), ModBlocks.ORANGE_MARBLE_BRICKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.ORANGE_MARBLE_TILE_SLABS.get(), ModBlocks.ORANGE_MARBLE_TILES.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.YELLOW_MARBLE_BRICK_SLABS.get(), ModBlocks.YELLOW_MARBLE_BRICKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.YELLOW_MARBLE_TILE_SLABS.get(), ModBlocks.YELLOW_MARBLE_TILES.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIME_MARBLE_BRICK_SLABS.get(), ModBlocks.LIME_MARBLE_BRICKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIME_MARBLE_TILE_SLABS.get(), ModBlocks.LIME_MARBLE_TILES.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GREEN_MARBLE_BRICK_SLABS.get(), ModBlocks.GREEN_MARBLE_BRICKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GREEN_MARBLE_TILE_SLABS.get(), ModBlocks.GREEN_MARBLE_TILES.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CYAN_MARBLE_BRICK_SLABS.get(), ModBlocks.CYAN_MARBLE_BRICKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CYAN_MARBLE_TILE_SLABS.get(), ModBlocks.CYAN_MARBLE_TILES.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIGHT_BLUE_MARBLE_BRICK_SLABS.get(), ModBlocks.LIGHT_BLUE_MARBLE_BRICKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIGHT_BLUE_MARBLE_TILE_SLABS.get(), ModBlocks.LIGHT_BLUE_MARBLE_TILES.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLUE_MARBLE_BRICK_SLABS.get(), ModBlocks.BLUE_MARBLE_BRICKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLUE_MARBLE_TILE_SLABS.get(), ModBlocks.BLUE_MARBLE_TILES.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PURPLE_MARBLE_BRICK_SLABS.get(), ModBlocks.PURPLE_MARBLE_BRICKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PURPLE_MARBLE_TILE_SLABS.get(), ModBlocks.PURPLE_MARBLE_TILES.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MAGENTA_MARBLE_BRICK_SLABS.get(), ModBlocks.MAGENTA_MARBLE_BRICKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MAGENTA_MARBLE_TILE_SLABS.get(), ModBlocks.MAGENTA_MARBLE_TILES.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PINK_MARBLE_BRICK_SLABS.get(), ModBlocks.PINK_MARBLE_BRICKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PINK_MARBLE_TILE_SLABS.get(), ModBlocks.PINK_MARBLE_TILES.get());

        //Walls
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MARBLE_BRICK_WALLS.get(), ModBlocks.MARBLE_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MARBLE_TILE_WALLS.get(), ModBlocks.MARBLE_TILES.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIGHT_GRAY_MARBLE_BRICK_WALLS.get(), ModBlocks.LIGHT_GRAY_MARBLE_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIGHT_GRAY_MARBLE_TILE_WALLS.get(), ModBlocks.LIGHT_GRAY_MARBLE_TILES.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GRAY_MARBLE_BRICK_WALLS.get(), ModBlocks.GRAY_MARBLE_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GRAY_MARBLE_TILE_WALLS.get(), ModBlocks.GRAY_MARBLE_TILES.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHARRED_MARBLE_BRICK_WALLS.get(), ModBlocks.CHARRED_MARBLE_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHARRED_MARBLE_TILE_WALLS.get(), ModBlocks.CHARRED_MARBLE_TILES.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BROWN_MARBLE_BRICK_WALLS.get(), ModBlocks.BROWN_MARBLE_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BROWN_MARBLE_TILE_WALLS.get(), ModBlocks.BROWN_MARBLE_TILES.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RED_MARBLE_BRICK_WALLS.get(), ModBlocks.RED_MARBLE_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RED_MARBLE_TILE_WALLS.get(), ModBlocks.RED_MARBLE_TILES.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.ORANGE_MARBLE_BRICK_WALLS.get(), ModBlocks.ORANGE_MARBLE_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.ORANGE_MARBLE_TILE_WALLS.get(), ModBlocks.ORANGE_MARBLE_TILES.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.YELLOW_MARBLE_BRICK_WALLS.get(), ModBlocks.YELLOW_MARBLE_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.YELLOW_MARBLE_TILE_WALLS.get(), ModBlocks.YELLOW_MARBLE_TILES.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIME_MARBLE_BRICK_WALLS.get(), ModBlocks.LIME_MARBLE_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIME_MARBLE_TILE_WALLS.get(), ModBlocks.LIME_MARBLE_TILES.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GREEN_MARBLE_BRICK_WALLS.get(), ModBlocks.GREEN_MARBLE_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GREEN_MARBLE_TILE_WALLS.get(), ModBlocks.GREEN_MARBLE_TILES.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CYAN_MARBLE_BRICK_WALLS.get(), ModBlocks.CYAN_MARBLE_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CYAN_MARBLE_TILE_WALLS.get(), ModBlocks.CYAN_MARBLE_TILES.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIGHT_BLUE_MARBLE_BRICK_WALLS.get(), ModBlocks.LIGHT_BLUE_MARBLE_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIGHT_BLUE_MARBLE_TILE_WALLS.get(), ModBlocks.LIGHT_BLUE_MARBLE_TILES.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLUE_MARBLE_BRICK_WALLS.get(), ModBlocks.BLUE_MARBLE_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLUE_MARBLE_TILE_WALLS.get(), ModBlocks.BLUE_MARBLE_TILES.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PURPLE_MARBLE_BRICK_WALLS.get(), ModBlocks.PURPLE_MARBLE_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PURPLE_MARBLE_TILE_WALLS.get(), ModBlocks.PURPLE_MARBLE_TILES.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MAGENTA_MARBLE_BRICK_WALLS.get(), ModBlocks.MAGENTA_MARBLE_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MAGENTA_MARBLE_TILE_WALLS.get(), ModBlocks.MAGENTA_MARBLE_TILES.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PINK_MARBLE_BRICK_WALLS.get(), ModBlocks.PINK_MARBLE_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PINK_MARBLE_TILE_WALLS.get(), ModBlocks.PINK_MARBLE_TILES.get());

        //Chiseled Blocks
        chiseled(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MARBLE_CHISELED.get(), ModBlocks.MARBLE_BRICK_SLABS.get());
        chiseled(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIGHT_GRAY_MARBLE_CHISELED.get(), ModBlocks.LIGHT_GRAY_MARBLE_BRICK_SLABS.get());
        chiseled(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GRAY_MARBLE_CHISELED.get(), ModBlocks.GRAY_MARBLE_BRICK_SLABS.get());
        chiseled(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHARRED_MARBLE_CHISELED.get(), ModBlocks.CHARRED_MARBLE_BRICK_SLABS.get());
        chiseled(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BROWN_MARBLE_CHISELED.get(), ModBlocks.BROWN_MARBLE_BRICK_SLABS.get());
        chiseled(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RED_MARBLE_CHISELED.get(), ModBlocks.RED_MARBLE_BRICK_SLABS.get());
        chiseled(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.ORANGE_MARBLE_CHISELED.get(), ModBlocks.ORANGE_MARBLE_BRICK_SLABS.get());
        chiseled(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.YELLOW_MARBLE_CHISELED.get(), ModBlocks.YELLOW_MARBLE_BRICK_SLABS.get());
        chiseled(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIME_MARBLE_CHISELED.get(), ModBlocks.LIME_MARBLE_BRICK_SLABS.get());
        chiseled(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GREEN_MARBLE_CHISELED.get(), ModBlocks.GREEN_MARBLE_BRICK_SLABS.get());
        chiseled(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CYAN_MARBLE_CHISELED.get(), ModBlocks.CYAN_MARBLE_BRICK_SLABS.get());
        chiseled(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.LIGHT_BLUE_MARBLE_CHISELED.get(), ModBlocks.LIGHT_BLUE_MARBLE_BRICK_SLABS.get());
        chiseled(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLUE_MARBLE_CHISELED.get(), ModBlocks.BLUE_MARBLE_BRICK_SLABS.get());
        chiseled(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PURPLE_MARBLE_CHISELED.get(), ModBlocks.PURPLE_MARBLE_BRICK_SLABS.get());
        chiseled(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MAGENTA_MARBLE_CHISELED.get(), ModBlocks.MAGENTA_MARBLE_BRICK_SLABS.get());
        chiseled(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PINK_MARBLE_CHISELED.get(), ModBlocks.PINK_MARBLE_BRICK_SLABS.get());

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
