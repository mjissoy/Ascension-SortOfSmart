package net.thejadeproject.ascension.datagen;

import net.favouriteless.modopedia.common.init.MDataComponents;
import net.favouriteless.modopedia.common.init.MItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.recipe.crafting.CopySpatialringDataRecipeShaped;
import net.thejadeproject.ascension.util.NoAdvRecipeOutput;
import net.thejadeproject.ascension.util.RecipeInjector;
import org.jetbrains.annotations.NotNull;

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
        var consumer = new NoAdvRecipeOutput(recipeOutput);

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ModItems.DIAMOND_BLADE.get()), Ingredient.of(Items.NETHERITE_INGOT), RecipeCategory.MISC, ModItems.NETHERITE_BLADE.get());
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ModItems.DIAMOND_SPEAR.get()), Ingredient.of(Items.NETHERITE_INGOT), RecipeCategory.MISC, ModItems.NETHERITE_SPEAR.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SOULSTEAD_RETURN_TALISMAN.get())
                .pattern("DED")
                .pattern("ETE")
                .pattern("DED")
                .define('T', ModItems.TALISMAN_PAPER.get())
                .define('E', Items.ENDER_PEARL)
                .define('D', Items.WHITE_BED)
                .unlockedBy("has_talisman_paper", has(ModItems.TALISMAN_PAPER)).save(recipeOutput, "ascension:shaped/soulstead_return_talisman");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WORLD_AXIS_TALISMAN.get())
                .pattern("DED")
                .pattern("ETE")
                .pattern("DED")
                .define('T', ModItems.TALISMAN_PAPER.get())
                .define('E', Items.ENDER_PEARL)
                .define('D', Items.FEATHER)
                .unlockedBy("has_talisman_paper", has(ModItems.TALISMAN_PAPER)).save(recipeOutput, "ascension:shaped/world_axis_talisman");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VOID_MARKING_TALISMAN.get())
                .pattern("DED")
                .pattern("ETE")
                .pattern("DED")
                .define('T', ModItems.SPATIAL_RUPTURE_TALISMAN_T3.get())
                .define('E', Items.REDSTONE_TORCH)
                .define('D', Items.FEATHER)
                .unlockedBy("has_talisman_paper", has(ModItems.TALISMAN_PAPER)).save(recipeOutput, "ascension:shaped/void_marking_talisman");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SPATIAL_RUPTURE_TALISMAN_T1.get())
                .pattern("DED")
                .pattern("ETE")
                .pattern("DED")
                .define('T', ModItems.TALISMAN_PAPER.get())
                .define('D', Items.ENDER_PEARL)
                .define('E', Items.ENDER_EYE)
                .unlockedBy("has_talisman_paper", has(ModItems.TALISMAN_PAPER)).save(recipeOutput, "ascension:shaped/spatial_rupture_talisman_t1");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SPATIAL_RUPTURE_TALISMAN_T2.get())
                .pattern("DED")
                .pattern("ETE")
                .pattern("DED")
                .define('T', ModItems.SPATIAL_RUPTURE_TALISMAN_T1.get())
                .define('D', Items.ENDER_PEARL)
                .define('E', Items.ENDER_EYE)
                .unlockedBy("has_spatial_rupture_talisman_t1", has(ModItems.SPATIAL_RUPTURE_TALISMAN_T1)).save(recipeOutput, "ascension:shaped/spatial_rupture_talisman_t2");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SPATIAL_RUPTURE_TALISMAN_T3.get())
                .pattern("DED")
                .pattern("ETE")
                .pattern("DED")
                .define('T', ModItems.SPATIAL_RUPTURE_TALISMAN_T2.get())
                .define('D', Items.ENDER_PEARL)
                .define('E', Items.ENDER_EYE)
                .unlockedBy("has_spatial_rupture_talisman_t2", has(ModItems.SPATIAL_RUPTURE_TALISMAN_T2)).save(recipeOutput, "ascension:shaped/spatial_rupture_talisman_t3");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FIRE_GOURD.get())
                .pattern("   ")
                .pattern("MFM")
                .pattern(" M ")
                .define('F', ModItems.HUNDRED_YEAR_FIRE_GINSENG.get())
                .define('M', ModBlocks.RAW_MARBLE.get())
                .unlockedBy("has_marble", has(ModBlocks.RAW_MARBLE)).save(recipeOutput, "ascension:shaped/fire_gourd");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IRON_SPATIAL_RING.get())
                .pattern("FIF")
                .pattern("BCB")
                .pattern("FBF")
                .define('I', Items.IRON_INGOT)
                .define('C', Blocks.CHEST)
                .define('F', ModItems.FROST_SILVER_INGOT.get())
                .define('B', ModItems.BLACK_IRON_INGOT.get())
                .unlockedBy("has_frost_silver_ingot", has(ModItems.FROST_SILVER_INGOT)).save(recipeOutput, "ascension:shaped/iron_spatial_ring");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GOLD_SPATIAL_RING.get())
                .pattern("GGG")
                .pattern("GSG")
                .pattern("GGG")
                .define('G', Items.GOLD_INGOT)
                .define('S', ModItems.IRON_SPATIAL_RING.get())
                .unlockedBy("has_iron_spatial_ring", has(ModItems.IRON_SPATIAL_RING)).save(SpatialRingUpgrade(consumer));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DIAMOND_SPATIAL_RING.get())
                .pattern("DDD")
                .pattern("DSD")
                .pattern("DDD")
                .define('D', Items.DIAMOND)
                .define('S', ModItems.GOLD_SPATIAL_RING.get())
                .unlockedBy("has_gold_spatial_ring", has(ModItems.GOLD_SPATIAL_RING)).save(SpatialRingUpgrade(consumer));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.NETHERITE_SPATIAL_RING.get())
                .pattern("NNN")
                .pattern("NSN")
                .pattern("NTN")
                .define('N', Items.NETHERITE_INGOT)
                .define('T', Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                .define('S', ModItems.DIAMOND_SPATIAL_RING.get())
                .unlockedBy("has_diamond_spatial_ring", has(ModItems.DIAMOND_SPATIAL_RING)).save(SpatialRingUpgrade(consumer));



        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.JADE_SPATIAL_RING.get())
                .pattern("DDD")
                .pattern("DSD")
                .pattern("DDD")
                .define('D', ModItems.JADE.get())
                .define('S', ModItems.NETHERITE_SPATIAL_RING.get())
                .unlockedBy("has_netherite_spatial_ring", has(ModItems.NETHERITE_SPATIAL_RING)).save(SpatialRingUpgrade(consumer));


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TABLET_OF_DESTRUCTION_HUMAN.get(), 2)
                .pattern("GBG")
                .pattern("BGB")
                .pattern("GBG")
                .define('G', Items.IRON_INGOT)
                .define('B', ModBlocks.RAW_MARBLE.get())
                .unlockedBy("has_gunpowder", has(Items.GUNPOWDER)).save(recipeOutput, "ascension:shaped/tablet_of_destruction_human");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TABLET_OF_DESTRUCTION_EARTH.get(), 2)
                .pattern("TTT")
                .pattern("THT")
                .pattern("TTT")
                .define('T', Items.GUNPOWDER)
                .define('H', ModItems.TABLET_OF_DESTRUCTION_HUMAN.get())
                .unlockedBy("has_tablet_of_destruction_human", has(ModItems.TABLET_OF_DESTRUCTION_HUMAN)).save(recipeOutput, "ascension:shaped/tablet_of_destruction_earth");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TABLET_OF_DESTRUCTION_HEAVEN.get(), 2)
                .pattern("TTT")
                .pattern("THT")
                .pattern("TTT")
                .define('T', Items.GUNPOWDER)
                .define('H', ModItems.TABLET_OF_DESTRUCTION_EARTH.get())
                .unlockedBy("has_tablet_of_destruction_eart", has(ModItems.TABLET_OF_DESTRUCTION_EARTH)).save(recipeOutput, "ascension:shaped/tablet_of_destruction_heaven");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ENDER_POUCH.get())
                .pattern("LJL")
                .pattern("LEL")
                .pattern("LLL")
                .define('E', Items.ENDER_CHEST)
                .define('L', Items.LEATHER)
                .define('J', ModItems.JADE.get())
                .unlockedBy("has_ender_chest", has(Items.ENDER_CHEST)).save(recipeOutput, "ascension:shaped/ender_pouch");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.JADE_SLIP.get())
                .pattern(" SS")
                .pattern("JJ ")
                .pattern("JJ ")
                .define('S', Items.STRING)
                .define('J', ModItems.JADE.get())
                .unlockedBy("has_jade", has(ModItems.JADE)).save(recipeOutput, "ascension:shaped/jade_slip");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.REPAIR_SLIP.get())
                .pattern(" SS")
                .pattern("BB ")
                .pattern("BB ")
                .define('S', Items.STRING)
                .define('B', ModItems.BLACK_IRON_NUGGET.get())
                .unlockedBy("has_black_iron_nugget", has(ModItems.REPAIR_SLIP)).save(recipeOutput, "ascension:shaped/repair_slip");









        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TALISMAN_PAPER.get(), 6)
                .pattern("   ")
                .pattern("SPS")
                .pattern("   ")
                .define('S', Items.SUGAR_CANE)
                .define('P', ModItems.JADE_BAMBOO_OF_SERENITY.get())
                .unlockedBy("has_jade_bamboo_of_serenity", has(ModItems.JADE_BAMBOO_OF_SERENITY)).save(recipeOutput, "ascension:shaped/talisman_paper");


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WOODEN_BLADE.get())
                .pattern("JJ ")
                .pattern(" J ")
                .pattern(" S ")
                .define('J', ItemTags.PLANKS)
                .define('S', Items.STICK)
                .unlockedBy("has_wood", has(ItemTags.PLANKS)).save(recipeOutput, "ascension:shaped/wooden_blade");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STONE_BLADE.get())
                .pattern("JJ ")
                .pattern(" J ")
                .pattern(" S ")
                .define('J', Items.COBBLESTONE)
                .define('S', Items.STICK)
                .unlockedBy("has_cobble", has(Items.COBBLESTONE)).save(recipeOutput, "ascension:shaped/cobblestone_blade");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IRON_BLADE.get())
                .pattern("JJ ")
                .pattern(" J ")
                .pattern(" S ")
                .define('J', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_iron", has(Items.IRON_INGOT)).save(recipeOutput, "ascension:shaped/iron_blade");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GOLD_BLADE.get())
                .pattern("JJ ")
                .pattern(" J ")
                .pattern(" S ")
                .define('J', Items.GOLD_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_gold", has(Items.GOLD_INGOT)).save(recipeOutput, "ascension:shaped/gold_blade");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DIAMOND_BLADE.get())
                .pattern("JJ ")
                .pattern(" J ")
                .pattern(" S ")
                .define('J', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy("has_diamond", has(Items.DIAMOND)).save(recipeOutput, "ascension:shaped/diamond_blade");
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ModItems.DIAMOND_BLADE.get()), Ingredient.of(Items.NETHERITE_INGOT), RecipeCategory.MISC, ModItems.NETHERITE_BLADE.get()).unlocks(getHasName(Items.NETHERITE_INGOT), has(Items.NETHERITE_INGOT)).save(recipeOutput, "ascension:smithing/netherite_blade");


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.JADE_BLADE.get())
                .pattern("JJ ")
                .pattern(" J ")
                .pattern(" S ")
                .define('J', ModItems.JADE.get())
                .define('S', Items.STICK)
                .unlockedBy("has_jade", has(ModItems.JADE)).save(recipeOutput, "ascension:shaped/jade_blade");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SEARING_BLADE.get())
                .pattern("CN ")
                .pattern(" N ")
                .pattern(" S ")
                .define('C', ModItems.CRIMSON_LOTUS_FLAME.get())
                .define('N', Items.NETHERITE_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_crimson_lotus_flame", has(ModItems.CRIMSON_LOTUS_FLAME)).save(recipeOutput, "ascension:shaped/searing_blade");


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WOODEN_SPEAR.get())
                .pattern("  J")
                .pattern(" J ")
                .pattern("S  ")
                .define('J', ItemTags.PLANKS)
                .define('S', Items.STICK)
                .unlockedBy("has_wood", has(ItemTags.PLANKS)).save(recipeOutput, "ascension:shaped/wooden_spear");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STONE_SPEAR.get())
                .pattern("  J")
                .pattern(" J ")
                .pattern("S  ")
                .define('J', Items.COBBLESTONE)
                .define('S', Items.STICK)
                .unlockedBy("has_cobble", has(Items.COBBLESTONE)).save(recipeOutput, "ascension:shaped/stone_spear");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IRON_SPEAR.get())
                .pattern("  J")
                .pattern(" J ")
                .pattern("S  ")
                .define('J', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_iron", has(Items.IRON_INGOT)).save(recipeOutput, "ascension:shaped/iron_spear");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GOLD_SPEAR.get())
                .pattern("  J")
                .pattern(" J ")
                .pattern("S  ")
                .define('J', Items.GOLD_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_gold", has(Items.GOLD_INGOT)).save(recipeOutput, "ascension:shaped/gold_spear");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DIAMOND_SPEAR.get())
                .pattern("  J")
                .pattern(" J ")
                .pattern("S  ")
                .define('J', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy("has_diamond", has(Items.DIAMOND)).save(recipeOutput, "ascension:shaped/diamond_spear");
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ModItems.DIAMOND_SPEAR.get()), Ingredient.of(Items.NETHERITE_INGOT), RecipeCategory.MISC, ModItems.NETHERITE_SPEAR.get()).unlocks(getHasName(Items.NETHERITE_INGOT), has(Items.NETHERITE_INGOT)).save(recipeOutput, "ascension:smithing/netherite_spear");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.JADE_SPEAR.get())
                .pattern("  J")
                .pattern(" J ")
                .pattern("S  ")
                .define('J', ModItems.JADE.get())
                .define('S', Items.STICK)
                .unlockedBy("has_jade", has(ModItems.JADE)).save(recipeOutput, "ascension:shaped/jade_spear");




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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BLACK_IRON_BLOCK.get())
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .define('B', ModItems.BLACK_IRON_INGOT.get())
                .unlockedBy("has_black_iron_ingot", has(ModItems.BLACK_IRON_INGOT)).save(recipeOutput, "ascension:shaped/black_iron_block_from_ingot");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BLACK_IRON_INGOT.get())
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .define('B', ModItems.BLACK_IRON_NUGGET.get())
                .unlockedBy("has_black_iron_nugget", has(ModItems.BLACK_IRON_NUGGET)).save(recipeOutput, "ascension:shaped/black_iron_ingot_from_nugget");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FROST_SILVER_BLOCK.get())
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .define('B', ModItems.FROST_SILVER_INGOT.get())
                .unlockedBy("has_frost_silver_ingot", has(ModItems.FROST_SILVER_INGOT)).save(recipeOutput, "ascension:shaped/frost_silver_block_from_ingot");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FROST_SILVER_INGOT.get())
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .define('B', ModItems.FROST_SILVER_NUGGET.get())
                .unlockedBy("has_frost_silver_nugget", has(ModItems.FROST_SILVER_NUGGET)).save(recipeOutput, "ascension:shaped/frost_silver_ingot_from_nugget");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PILL_CAULDRON_HUMAN_LOW.get())
                .pattern("B B")
                .pattern("B B")
                .pattern("BBB")
                .define('B', ModItems.BLACK_IRON_INGOT.get())
                .unlockedBy("has_black_iron_ingot", has(ModItems.BLACK_IRON_INGOT)).save(recipeOutput, "ascension:shaped/pill_cauldron_from_black_iron");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.GOLDEN_PALM_PLANKS.get(), 4)
                .requires(ModBlocks.GOLDEN_PALM_LOG)
                .unlockedBy("has_golden_palm_log", has(ModBlocks.GOLDEN_PALM_LOG)).save(recipeOutput, "ascension:shapeless/palm_planks");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.GOLDEN_PALM_PLANKS.get(), 4)
                .requires(ModBlocks.GOLDEN_PALM_WOOD)
                .unlockedBy("has_golden_palm_wood", has(ModBlocks.GOLDEN_PALM_WOOD)).save(recipeOutput, "ascension:shapeless/palm_planks_from_wood");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.GOLDEN_PALM_PLANKS.get(), 4)
                .requires(ModBlocks.STRIPPED_GOLDEN_PALM_LOG)
                .unlockedBy("has_golden_palm_log_stripped", has(ModBlocks.STRIPPED_GOLDEN_PALM_LOG)).save(recipeOutput, "ascension:shapeless/palm_planks_stripped");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.GOLDEN_PALM_PLANKS.get(), 4)
                .requires(ModBlocks.STRIPPED_GOLDEN_PALM_WOOD)
                .unlockedBy("has_golden_palm_wood_stripped", has(ModBlocks.STRIPPED_GOLDEN_PALM_WOOD)).save(recipeOutput, "ascension:shapeless/palm_planks_from_wood_stripped");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SPIRITUAL_STONE.get(), 9)
                .requires(ModBlocks.SPIRITUAL_STONE_BLOCK)
                .unlockedBy("has_ssb", has(ModBlocks.SPIRITUAL_STONE_BLOCK)).save(recipeOutput, "ascension:shapeless/ss_from_ssb");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.JADE.get(), 9)
                .requires(ModBlocks.JADE_BLOCK)
                .unlockedBy("has_block_of_jade", has(ModBlocks.JADE_BLOCK)).save(recipeOutput, "ascension:shapeless/jade_from_jade_block");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.JADE_NUGGET.get(), 9)
                .requires(ModItems.JADE)
                .unlockedBy("has_jade", has(ModItems.JADE)).save(recipeOutput, "ascension:shapeless/jade_nugget_from_jade");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLACK_IRON_INGOT.get(), 9)
                .requires(ModBlocks.BLACK_IRON_BLOCK)
                .unlockedBy("has_block_of_black_iron", has(ModBlocks.BLACK_IRON_BLOCK)).save(recipeOutput, "ascension:shapeless/black_iron_ingot_from_block");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLACK_IRON_NUGGET.get(), 9)
                .requires(ModItems.BLACK_IRON_INGOT)
                .unlockedBy("has_black_iron_ingot", has(ModItems.BLACK_IRON_INGOT)).save(recipeOutput, "ascension:shapeless/black_iron_nugget_from_ingot");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.FROST_SILVER_INGOT.get(), 9)
                .requires(ModBlocks.FROST_SILVER_BLOCK)
                .unlockedBy("has_block_of_frost_silver", has(ModBlocks.FROST_SILVER_BLOCK)).save(recipeOutput, "ascension:shapeless/frost_silver_ingot_from_block");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.FROST_SILVER_NUGGET.get(), 9)
                .requires(ModItems.FROST_SILVER_INGOT)
                .unlockedBy("has_frost_silver_ingot", has(ModItems.FROST_SILVER_INGOT)).save(recipeOutput, "ascension:shapeless/frost_silver_nugget_from_ingot");


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








        oreSmelting(recipeOutput, ModBlocks.JADE_ORE.asItem(), RecipeCategory.MISC, ModItems.JADE.get(), 0.25f, 200, "jade");
        oreBlasting(recipeOutput, ModBlocks.JADE_ORE.asItem(), RecipeCategory.MISC, ModItems.JADE.get(), 0.30f, 100, "jade");
        oreSmelting(recipeOutput, ModItems.RAW_BLACK_IRON.get(), RecipeCategory.MISC, ModItems.BLACK_IRON_INGOT.get(), 0.25f, 200, "black_iron_ingot");
        oreBlasting(recipeOutput, ModItems.RAW_BLACK_IRON.get(), RecipeCategory.MISC, ModItems.BLACK_IRON_INGOT.get(), 0.30f, 100, "black_iron_ingot");

        oreSmelting(recipeOutput, ModItems.RAW_FROST_SILVER.get(), RecipeCategory.MISC, ModItems.FROST_SILVER_INGOT.get(), 0.25f, 200, "frost_silver_ingot");
        oreBlasting(recipeOutput, ModItems.RAW_FROST_SILVER.get(), RecipeCategory.MISC, ModItems.FROST_SILVER_INGOT.get(), 0.30f, 100, "frost_silver_ingot");



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



    protected static void oreSmelting(RecipeOutput recipeOutput, Item pIngredient, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, List.of(pIngredient), pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, Item pIngredient, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, List.of(pIngredient), pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }


    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, AscensionCraft.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }

    @NotNull
    private static RecipeInjector<ShapedRecipe> SpatialRingUpgrade(NoAdvRecipeOutput consumer) {
        return new RecipeInjector<>(consumer, CopySpatialringDataRecipeShaped::new);
    }
}
