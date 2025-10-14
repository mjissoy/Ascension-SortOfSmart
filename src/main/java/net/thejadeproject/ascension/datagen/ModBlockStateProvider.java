package net.thejadeproject.ascension.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.thejadeproject.ascension.AscensionCraft;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.thejadeproject.ascension.blocks.ModBlocks;


public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper){
        super(output, AscensionCraft.MOD_ID, exFileHelper);
    }
    @Override
    protected void registerStatesAndModels() {


        //Herbs Blocks
        simpleBlock(ModBlocks.IRONWOOD_SPROUT_BLOCK.get(),
                models().cross("ironwood_sprout_block", blockTexture(ModBlocks.IRONWOOD_SPROUT_BLOCK.get()))
                        .renderType("cutout"));
        simpleBlock(ModBlocks.WHITE_JADE_ORCHID_BLOCK.get(),
                models().cross("white_jade_orchid_block", blockTexture(ModBlocks.WHITE_JADE_ORCHID_BLOCK.get()))
                        .renderType("cutout"));
        herbsBlock(ModBlocks.HUNDRED_YEAR_GINSENG_CROP);


        simpleBlockWithItem(ModBlocks.BLACK_IRON_ORE);
        simpleBlockWithItem(ModBlocks.BLACK_IRON_BLOCK);
        simpleBlockWithItem(ModBlocks.JADE_ORE);
        simpleBlockWithItem(ModBlocks.JADE_BLOCK);
        simpleBlockWithItem(ModBlocks.SPIRITUAL_STONE_BLOCK);


        /** Marble */
        simpleBlockWithItem(ModBlocks.MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.MARBLE_TILES);
        simpleBlockWithItem(ModBlocks.MARBLE_CHISELED);
        simpleBlockWithItem(ModBlocks.RAW_MARBLE);
        simpleBlockWithItem(ModBlocks.POLISHED_MARBLE);
        simpleBlockWithItem(ModBlocks.POLISHED_BURNED_MARBLE);
        simpleBlockWithItem(ModBlocks.CHARRED_MARBLE);
        simpleBlockWithItem(ModBlocks.CHARRED_MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.CHARRED_MARBLE_CHISELED);
        simpleBlockWithItem(ModBlocks.CHARRED_MARBLE_TILES);

        simpleBlockWithItem(ModBlocks.BLUE_MARBLE);
        simpleBlockWithItem(ModBlocks.BLUE_MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.BLUE_MARBLE_CHISELED);
        simpleBlockWithItem(ModBlocks.BLUE_MARBLE_TILES);
        simpleBlockWithItem(ModBlocks.BLUE_POLISHED_MARBLE);
        simpleBlockWithItem(ModBlocks.BROWN_MARBLE);
        simpleBlockWithItem(ModBlocks.BROWN_MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.BROWN_MARBLE_CHISELED);
        simpleBlockWithItem(ModBlocks.BROWN_MARBLE_TILES);
        simpleBlockWithItem(ModBlocks.BROWN_POLISHED_MARBLE);
        simpleBlockWithItem(ModBlocks.CYAN_MARBLE);
        simpleBlockWithItem(ModBlocks.CYAN_MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.CYAN_MARBLE_CHISELED);
        simpleBlockWithItem(ModBlocks.CYAN_MARBLE_TILES);
        simpleBlockWithItem(ModBlocks.CYAN_POLISHED_MARBLE);
        simpleBlockWithItem(ModBlocks.GRAY_MARBLE);
        simpleBlockWithItem(ModBlocks.GRAY_MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.GRAY_MARBLE_CHISELED);
        simpleBlockWithItem(ModBlocks.GRAY_MARBLE_TILES);
        simpleBlockWithItem(ModBlocks.GRAY_POLISHED_MARBLE);
        simpleBlockWithItem(ModBlocks.GREEN_MARBLE);
        simpleBlockWithItem(ModBlocks.GREEN_MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.GREEN_MARBLE_CHISELED);
        simpleBlockWithItem(ModBlocks.GREEN_MARBLE_TILES);
        simpleBlockWithItem(ModBlocks.GREEN_POLISHED_MARBLE);
        simpleBlockWithItem(ModBlocks.LIGHT_BLUE_MARBLE);
        simpleBlockWithItem(ModBlocks.LIGHT_BLUE_MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.LIGHT_BLUE_MARBLE_CHISELED);
        simpleBlockWithItem(ModBlocks.LIGHT_BLUE_MARBLE_TILES);
        simpleBlockWithItem(ModBlocks.LIGHT_BLUE_POLISHED_MARBLE);
        simpleBlockWithItem(ModBlocks.LIGHT_GRAY_MARBLE);
        simpleBlockWithItem(ModBlocks.LIGHT_GRAY_MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.LIGHT_GRAY_MARBLE_CHISELED);
        simpleBlockWithItem(ModBlocks.LIGHT_GRAY_MARBLE_TILES);
        simpleBlockWithItem(ModBlocks.POLISHED_LIGHT_GRAY_MARBLE);
        simpleBlockWithItem(ModBlocks.LIME_MARBLE);
        simpleBlockWithItem(ModBlocks.LIME_MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.LIME_MARBLE_CHISELED);
        simpleBlockWithItem(ModBlocks.LIME_MARBLE_TILES);
        simpleBlockWithItem(ModBlocks.LIME_POLISHED_MARBLE);
        simpleBlockWithItem(ModBlocks.MAGENTA_MARBLE);
        simpleBlockWithItem(ModBlocks.MAGENTA_MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.MAGENTA_MARBLE_CHISELED);
        simpleBlockWithItem(ModBlocks.MAGENTA_MARBLE_TILES);
        simpleBlockWithItem(ModBlocks.MAGENTA_POLISHED_MARBLE);
        simpleBlockWithItem(ModBlocks.ORANGE_MARBLE);
        simpleBlockWithItem(ModBlocks.ORANGE_MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.ORANGE_MARBLE_CHISELED);
        simpleBlockWithItem(ModBlocks.ORANGE_MARBLE_TILES);
        simpleBlockWithItem(ModBlocks.ORANGE_POLISHED_MARBLE);
        simpleBlockWithItem(ModBlocks.PINK_MARBLE);
        simpleBlockWithItem(ModBlocks.PINK_MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.PINK_MARBLE_CHISELED);
        simpleBlockWithItem(ModBlocks.PINK_MARBLE_TILES);
        simpleBlockWithItem(ModBlocks.PINK_POLISHED_MARBLE);
        simpleBlockWithItem(ModBlocks.PURPLE_MARBLE);
        simpleBlockWithItem(ModBlocks.PURPLE_MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.PURPLE_MARBLE_CHISELED);
        simpleBlockWithItem(ModBlocks.PURPLE_MARBLE_TILES);
        simpleBlockWithItem(ModBlocks.PURPLE_POLISHED_MARBLE);
        simpleBlockWithItem(ModBlocks.RED_MARBLE);
        simpleBlockWithItem(ModBlocks.RED_MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.RED_MARBLE_CHISELED);
        simpleBlockWithItem(ModBlocks.RED_MARBLE_TILES);
        simpleBlockWithItem(ModBlocks.RED_POLISHED_MARBLE);
        simpleBlockWithItem(ModBlocks.YELLOW_MARBLE);
        simpleBlockWithItem(ModBlocks.YELLOW_MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.YELLOW_MARBLE_CHISELED);
        simpleBlockWithItem(ModBlocks.YELLOW_MARBLE_TILES);
        simpleBlockWithItem(ModBlocks.YELLOW_POLISHED_MARBLE);


        logBlock(((RotatedPillarBlock) ModBlocks.GOLDEN_PALM_LOG.get()));
        axisBlock(((RotatedPillarBlock) ModBlocks.GOLDEN_PALM_WOOD.get()), blockTexture(ModBlocks.GOLDEN_PALM_LOG.get()), blockTexture(ModBlocks.GOLDEN_PALM_LOG.get()));
        logBlock(((RotatedPillarBlock) ModBlocks.STRIPPED_GOLDEN_PALM_LOG.get()));
        axisBlock(((RotatedPillarBlock) ModBlocks.STRIPPED_GOLDEN_PALM_WOOD.get()), blockTexture(ModBlocks.STRIPPED_GOLDEN_PALM_LOG.get()), blockTexture(ModBlocks.STRIPPED_GOLDEN_PALM_LOG.get()));

        blockItem(ModBlocks.GOLDEN_PALM_LOG);
        blockItem(ModBlocks.GOLDEN_PALM_WOOD);
        blockItem(ModBlocks.STRIPPED_GOLDEN_PALM_LOG);
        blockItem(ModBlocks.STRIPPED_GOLDEN_PALM_WOOD);

        blockWithItem(ModBlocks.GOLDEN_PALM_PLANKS);

        //Leaves
        leavesBlock(ModBlocks.GOLDEN_PALM_LEAVES);

        //Saplings
        saplingBlock(ModBlocks.GOLDEN_PALM_SAPLING);

        //Stairs
        stairsBlock(ModBlocks.GOLDEN_PALM_STAIRS.get(), blockTexture(ModBlocks.GOLDEN_PALM_PLANKS.get()));
        stairsBlock(ModBlocks.MARBLE_BRICK_STAIRS.get(), blockTexture(ModBlocks.MARBLE_BRICKS.get()));
        stairsBlock(ModBlocks.MARBLE_TILE_STAIRS.get(), blockTexture(ModBlocks.MARBLE_TILES.get()));
        stairsBlock(ModBlocks.CHARRED_MARBLE_BRICK_STAIRS.get(), blockTexture(ModBlocks.CHARRED_MARBLE_BRICKS.get()));
        stairsBlock(ModBlocks.CHARRED_MARBLE_TILE_STAIRS.get(), blockTexture(ModBlocks.CHARRED_MARBLE_TILES.get()));
        stairsBlock(ModBlocks.BLUE_MARBLE_BRICK_STAIRS.get(), blockTexture(ModBlocks.BLUE_MARBLE_BRICKS.get()));
        stairsBlock(ModBlocks.BLUE_MARBLE_TILE_STAIRS.get(), blockTexture(ModBlocks.BLUE_MARBLE_TILES.get()));
        stairsBlock(ModBlocks.BROWN_MARBLE_BRICK_STAIRS.get(), blockTexture(ModBlocks.BROWN_MARBLE_BRICKS.get()));
        stairsBlock(ModBlocks.BROWN_MARBLE_TILE_STAIRS.get(), blockTexture(ModBlocks.BROWN_MARBLE_TILES.get()));
        stairsBlock(ModBlocks.CYAN_MARBLE_BRICK_STAIRS.get(), blockTexture(ModBlocks.CYAN_MARBLE_BRICKS.get()));
        stairsBlock(ModBlocks.CYAN_MARBLE_TILE_STAIRS.get(), blockTexture(ModBlocks.CYAN_MARBLE_TILES.get()));
        stairsBlock(ModBlocks.GRAY_MARBLE_BRICK_STAIRS.get(), blockTexture(ModBlocks.GRAY_MARBLE_BRICKS.get()));
        stairsBlock(ModBlocks.GRAY_MARBLE_TILE_STAIRS.get(), blockTexture(ModBlocks.GRAY_MARBLE_TILES.get()));
        stairsBlock(ModBlocks.GREEN_MARBLE_BRICK_STAIRS.get(), blockTexture(ModBlocks.GREEN_MARBLE_BRICKS.get()));
        stairsBlock(ModBlocks.GREEN_MARBLE_TILE_STAIRS.get(), blockTexture(ModBlocks.GREEN_MARBLE_TILES.get()));
        stairsBlock(ModBlocks.LIGHT_BLUE_MARBLE_BRICK_STAIRS.get(), blockTexture(ModBlocks.LIGHT_BLUE_MARBLE_BRICKS.get()));
        stairsBlock(ModBlocks.LIGHT_BLUE_MARBLE_TILE_STAIRS.get(), blockTexture(ModBlocks.LIGHT_BLUE_MARBLE_TILES.get()));
        stairsBlock(ModBlocks.LIGHT_GRAY_MARBLE_BRICK_STAIRS.get(), blockTexture(ModBlocks.LIGHT_GRAY_MARBLE_BRICKS.get()));
        stairsBlock(ModBlocks.LIGHT_GRAY_MARBLE_TILE_STAIRS.get(), blockTexture(ModBlocks.LIGHT_GRAY_MARBLE_TILES.get()));
        stairsBlock(ModBlocks.LIME_MARBLE_BRICK_STAIRS.get(), blockTexture(ModBlocks.LIME_MARBLE_BRICKS.get()));
        stairsBlock(ModBlocks.LIME_MARBLE_TILE_STAIRS.get(), blockTexture(ModBlocks.LIME_MARBLE_TILES.get()));
        stairsBlock(ModBlocks.MAGENTA_MARBLE_BRICK_STAIRS.get(), blockTexture(ModBlocks.MAGENTA_MARBLE_BRICKS.get()));
        stairsBlock(ModBlocks.MAGENTA_MARBLE_TILE_STAIRS.get(), blockTexture(ModBlocks.MAGENTA_MARBLE_TILES.get()));
        stairsBlock(ModBlocks.ORANGE_MARBLE_BRICK_STAIRS.get(), blockTexture(ModBlocks.ORANGE_MARBLE_BRICKS.get()));
        stairsBlock(ModBlocks.ORANGE_MARBLE_TILE_STAIRS.get(), blockTexture(ModBlocks.ORANGE_MARBLE_TILES.get()));
        stairsBlock(ModBlocks.PINK_MARBLE_BRICK_STAIRS.get(), blockTexture(ModBlocks.PINK_MARBLE_BRICKS.get()));
        stairsBlock(ModBlocks.PINK_MARBLE_TILE_STAIRS.get(), blockTexture(ModBlocks.PINK_MARBLE_TILES.get()));
        stairsBlock(ModBlocks.PURPLE_MARBLE_BRICK_STAIRS.get(), blockTexture(ModBlocks.PURPLE_MARBLE_BRICKS.get()));
        stairsBlock(ModBlocks.PURPLE_MARBLE_TILE_STAIRS.get(), blockTexture(ModBlocks.PURPLE_MARBLE_TILES.get()));
        stairsBlock(ModBlocks.RED_MARBLE_BRICK_STAIRS.get(), blockTexture(ModBlocks.RED_MARBLE_BRICKS.get()));
        stairsBlock(ModBlocks.RED_MARBLE_TILE_STAIRS.get(), blockTexture(ModBlocks.RED_MARBLE_TILES.get()));
        stairsBlock(ModBlocks.YELLOW_MARBLE_BRICK_STAIRS.get(), blockTexture(ModBlocks.YELLOW_MARBLE_BRICKS.get()));
        stairsBlock(ModBlocks.YELLOW_MARBLE_TILE_STAIRS.get(), blockTexture(ModBlocks.YELLOW_MARBLE_TILES.get()));

        //Slabs
        slabBlock(ModBlocks.GOLDEN_PALM_SLAB.get(), blockTexture(ModBlocks.GOLDEN_PALM_PLANKS.get()), blockTexture(ModBlocks.GOLDEN_PALM_PLANKS.get()));
        slabBlock(ModBlocks.MARBLE_BRICK_SLABS.get(), blockTexture(ModBlocks.MARBLE_BRICKS.get()), blockTexture(ModBlocks.MARBLE_BRICKS.get()));
        slabBlock(ModBlocks.MARBLE_TILE_SLABS.get(), blockTexture(ModBlocks.MARBLE_TILES.get()), blockTexture(ModBlocks.MARBLE_TILES.get()));
        slabBlock(ModBlocks.CHARRED_MARBLE_BRICK_SLABS.get(), blockTexture(ModBlocks.CHARRED_MARBLE_BRICKS.get()), blockTexture(ModBlocks.CHARRED_MARBLE_BRICKS.get()));
        slabBlock(ModBlocks.CHARRED_MARBLE_TILE_SLABS.get(), blockTexture(ModBlocks.CHARRED_MARBLE_TILES.get()), blockTexture(ModBlocks.CHARRED_MARBLE_TILES.get()));
        slabBlock(ModBlocks.BLUE_MARBLE_BRICK_SLABS.get(), blockTexture(ModBlocks.BLUE_MARBLE_BRICKS.get()), blockTexture(ModBlocks.BLUE_MARBLE_BRICKS.get()));
        slabBlock(ModBlocks.BLUE_MARBLE_TILE_SLABS.get(), blockTexture(ModBlocks.BLUE_MARBLE_TILES.get()), blockTexture(ModBlocks.BLUE_MARBLE_TILES.get()));
        slabBlock(ModBlocks.BROWN_MARBLE_BRICK_SLABS.get(), blockTexture(ModBlocks.BROWN_MARBLE_BRICKS.get()), blockTexture(ModBlocks.BROWN_MARBLE_BRICKS.get()));
        slabBlock(ModBlocks.BROWN_MARBLE_TILE_SLABS.get(), blockTexture(ModBlocks.BROWN_MARBLE_TILES.get()), blockTexture(ModBlocks.BROWN_MARBLE_TILES.get()));
        slabBlock(ModBlocks.CYAN_MARBLE_BRICK_SLABS.get(), blockTexture(ModBlocks.CYAN_MARBLE_BRICKS.get()), blockTexture(ModBlocks.CYAN_MARBLE_BRICKS.get()));
        slabBlock(ModBlocks.CYAN_MARBLE_TILE_SLABS.get(), blockTexture(ModBlocks.CYAN_MARBLE_TILES.get()), blockTexture(ModBlocks.CYAN_MARBLE_TILES.get()));
        slabBlock(ModBlocks.GRAY_MARBLE_BRICK_SLABS.get(), blockTexture(ModBlocks.GRAY_MARBLE_BRICKS.get()), blockTexture(ModBlocks.GRAY_MARBLE_BRICKS.get()));
        slabBlock(ModBlocks.GRAY_MARBLE_TILE_SLABS.get(), blockTexture(ModBlocks.GRAY_MARBLE_TILES.get()), blockTexture(ModBlocks.GRAY_MARBLE_TILES.get()));
        slabBlock(ModBlocks.GREEN_MARBLE_BRICK_SLABS.get(), blockTexture(ModBlocks.GREEN_MARBLE_BRICKS.get()), blockTexture(ModBlocks.GREEN_MARBLE_BRICKS.get()));
        slabBlock(ModBlocks.GREEN_MARBLE_TILE_SLABS.get(), blockTexture(ModBlocks.GREEN_MARBLE_TILES.get()), blockTexture(ModBlocks.GREEN_MARBLE_TILES.get()));
        slabBlock(ModBlocks.LIGHT_BLUE_MARBLE_BRICK_SLABS.get(), blockTexture(ModBlocks.LIGHT_BLUE_MARBLE_BRICKS.get()), blockTexture(ModBlocks.LIGHT_BLUE_MARBLE_BRICKS.get()));
        slabBlock(ModBlocks.LIGHT_BLUE_MARBLE_TILE_SLABS.get(), blockTexture(ModBlocks.LIGHT_BLUE_MARBLE_TILES.get()), blockTexture(ModBlocks.LIGHT_BLUE_MARBLE_TILES.get()));
        slabBlock(ModBlocks.LIGHT_GRAY_MARBLE_BRICK_SLABS.get(), blockTexture(ModBlocks.LIGHT_GRAY_MARBLE_BRICKS.get()), blockTexture(ModBlocks.LIGHT_GRAY_MARBLE_BRICKS.get()));
        slabBlock(ModBlocks.LIGHT_GRAY_MARBLE_TILE_SLABS.get(), blockTexture(ModBlocks.LIGHT_GRAY_MARBLE_TILES.get()), blockTexture(ModBlocks.LIGHT_GRAY_MARBLE_TILES.get()));
        slabBlock(ModBlocks.LIME_MARBLE_BRICK_SLABS.get(), blockTexture(ModBlocks.LIME_MARBLE_BRICKS.get()), blockTexture(ModBlocks.LIME_MARBLE_BRICKS.get()));
        slabBlock(ModBlocks.LIME_MARBLE_TILE_SLABS.get(), blockTexture(ModBlocks.LIME_MARBLE_TILES.get()), blockTexture(ModBlocks.LIME_MARBLE_TILES.get()));
        slabBlock(ModBlocks.MAGENTA_MARBLE_BRICK_SLABS.get(), blockTexture(ModBlocks.MAGENTA_MARBLE_BRICKS.get()), blockTexture(ModBlocks.MAGENTA_MARBLE_BRICKS.get()));
        slabBlock(ModBlocks.MAGENTA_MARBLE_TILE_SLABS.get(), blockTexture(ModBlocks.MAGENTA_MARBLE_TILES.get()), blockTexture(ModBlocks.MAGENTA_MARBLE_TILES.get()));
        slabBlock(ModBlocks.ORANGE_MARBLE_BRICK_SLABS.get(), blockTexture(ModBlocks.ORANGE_MARBLE_BRICKS.get()), blockTexture(ModBlocks.ORANGE_MARBLE_BRICKS.get()));
        slabBlock(ModBlocks.ORANGE_MARBLE_TILE_SLABS.get(), blockTexture(ModBlocks.ORANGE_MARBLE_TILES.get()), blockTexture(ModBlocks.ORANGE_MARBLE_TILES.get()));
        slabBlock(ModBlocks.PINK_MARBLE_BRICK_SLABS.get(), blockTexture(ModBlocks.PINK_MARBLE_BRICKS.get()), blockTexture(ModBlocks.PINK_MARBLE_BRICKS.get()));
        slabBlock(ModBlocks.PINK_MARBLE_TILE_SLABS.get(), blockTexture(ModBlocks.PINK_MARBLE_TILES.get()), blockTexture(ModBlocks.PINK_MARBLE_TILES.get()));
        slabBlock(ModBlocks.PURPLE_MARBLE_BRICK_SLABS.get(), blockTexture(ModBlocks.PURPLE_MARBLE_BRICKS.get()), blockTexture(ModBlocks.PURPLE_MARBLE_BRICKS.get()));
        slabBlock(ModBlocks.PURPLE_MARBLE_TILE_SLABS.get(), blockTexture(ModBlocks.PURPLE_MARBLE_TILES.get()), blockTexture(ModBlocks.PURPLE_MARBLE_TILES.get()));
        slabBlock(ModBlocks.RED_MARBLE_BRICK_SLABS.get(), blockTexture(ModBlocks.RED_MARBLE_BRICKS.get()), blockTexture(ModBlocks.RED_MARBLE_BRICKS.get()));
        slabBlock(ModBlocks.RED_MARBLE_TILE_SLABS.get(), blockTexture(ModBlocks.RED_MARBLE_TILES.get()), blockTexture(ModBlocks.RED_MARBLE_TILES.get()));
        slabBlock(ModBlocks.YELLOW_MARBLE_BRICK_SLABS.get(), blockTexture(ModBlocks.YELLOW_MARBLE_BRICKS.get()), blockTexture(ModBlocks.YELLOW_MARBLE_BRICKS.get()));
        slabBlock(ModBlocks.YELLOW_MARBLE_TILE_SLABS.get(), blockTexture(ModBlocks.YELLOW_MARBLE_TILES.get()), blockTexture(ModBlocks.YELLOW_MARBLE_TILES.get()));

        //Walls
        wallBlock(ModBlocks.MARBLE_BRICK_WALLS.get(), blockTexture(ModBlocks.MARBLE_BRICKS.get()));
        wallBlock(ModBlocks.MARBLE_TILE_WALLS.get(), blockTexture(ModBlocks.MARBLE_TILES.get()));
        wallBlock(ModBlocks.CHARRED_MARBLE_BRICK_WALLS.get(), blockTexture(ModBlocks.CHARRED_MARBLE_BRICKS.get()));
        wallBlock(ModBlocks.CHARRED_MARBLE_TILE_WALLS.get(), blockTexture(ModBlocks.CHARRED_MARBLE_TILES.get()));
        wallBlock(ModBlocks.BLUE_MARBLE_BRICK_WALLS.get(), blockTexture(ModBlocks.BLUE_MARBLE_BRICKS.get()));
        wallBlock(ModBlocks.BLUE_MARBLE_TILE_WALLS.get(), blockTexture(ModBlocks.BLUE_MARBLE_TILES.get()));
        wallBlock(ModBlocks.BROWN_MARBLE_BRICK_WALLS.get(), blockTexture(ModBlocks.BROWN_MARBLE_BRICKS.get()));
        wallBlock(ModBlocks.BROWN_MARBLE_TILE_WALLS.get(), blockTexture(ModBlocks.BROWN_MARBLE_TILES.get()));
        wallBlock(ModBlocks.CYAN_MARBLE_BRICK_WALLS.get(), blockTexture(ModBlocks.CYAN_MARBLE_BRICKS.get()));
        wallBlock(ModBlocks.CYAN_MARBLE_TILE_WALLS.get(), blockTexture(ModBlocks.CYAN_MARBLE_TILES.get()));
        wallBlock(ModBlocks.GRAY_MARBLE_BRICK_WALLS.get(), blockTexture(ModBlocks.GRAY_MARBLE_BRICKS.get()));
        wallBlock(ModBlocks.GRAY_MARBLE_TILE_WALLS.get(), blockTexture(ModBlocks.GRAY_MARBLE_TILES.get()));
        wallBlock(ModBlocks.GREEN_MARBLE_BRICK_WALLS.get(), blockTexture(ModBlocks.GREEN_MARBLE_BRICKS.get()));
        wallBlock(ModBlocks.GREEN_MARBLE_TILE_WALLS.get(), blockTexture(ModBlocks.GREEN_MARBLE_TILES.get()));
        wallBlock(ModBlocks.LIGHT_BLUE_MARBLE_BRICK_WALLS.get(), blockTexture(ModBlocks.LIGHT_BLUE_MARBLE_BRICKS.get()));
        wallBlock(ModBlocks.LIGHT_BLUE_MARBLE_TILE_WALLS.get(), blockTexture(ModBlocks.LIGHT_BLUE_MARBLE_TILES.get()));
        wallBlock(ModBlocks.LIGHT_GRAY_MARBLE_BRICK_WALLS.get(), blockTexture(ModBlocks.LIGHT_GRAY_MARBLE_BRICKS.get()));
        wallBlock(ModBlocks.LIGHT_GRAY_MARBLE_TILE_WALLS.get(), blockTexture(ModBlocks.LIGHT_GRAY_MARBLE_TILES.get()));
        wallBlock(ModBlocks.LIME_MARBLE_BRICK_WALLS.get(), blockTexture(ModBlocks.LIME_MARBLE_BRICKS.get()));
        wallBlock(ModBlocks.LIME_MARBLE_TILE_WALLS.get(), blockTexture(ModBlocks.LIME_MARBLE_TILES.get()));
        wallBlock(ModBlocks.MAGENTA_MARBLE_BRICK_WALLS.get(), blockTexture(ModBlocks.MAGENTA_MARBLE_BRICKS.get()));
        wallBlock(ModBlocks.MAGENTA_MARBLE_TILE_WALLS.get(), blockTexture(ModBlocks.MAGENTA_MARBLE_TILES.get()));
        wallBlock(ModBlocks.ORANGE_MARBLE_BRICK_WALLS.get(), blockTexture(ModBlocks.ORANGE_MARBLE_BRICKS.get()));
        wallBlock(ModBlocks.ORANGE_MARBLE_TILE_WALLS.get(), blockTexture(ModBlocks.ORANGE_MARBLE_TILES.get()));
        wallBlock(ModBlocks.PINK_MARBLE_BRICK_WALLS.get(), blockTexture(ModBlocks.PINK_MARBLE_BRICKS.get()));
        wallBlock(ModBlocks.PINK_MARBLE_TILE_WALLS.get(), blockTexture(ModBlocks.PINK_MARBLE_TILES.get()));
        wallBlock(ModBlocks.PURPLE_MARBLE_BRICK_WALLS.get(), blockTexture(ModBlocks.PURPLE_MARBLE_BRICKS.get()));
        wallBlock(ModBlocks.PURPLE_MARBLE_TILE_WALLS.get(), blockTexture(ModBlocks.PURPLE_MARBLE_TILES.get()));
        wallBlock(ModBlocks.RED_MARBLE_BRICK_WALLS.get(), blockTexture(ModBlocks.RED_MARBLE_BRICKS.get()));
        wallBlock(ModBlocks.RED_MARBLE_TILE_WALLS.get(), blockTexture(ModBlocks.RED_MARBLE_TILES.get()));
        wallBlock(ModBlocks.YELLOW_MARBLE_BRICK_WALLS.get(), blockTexture(ModBlocks.YELLOW_MARBLE_BRICKS.get()));
        wallBlock(ModBlocks.YELLOW_MARBLE_TILE_WALLS.get(), blockTexture(ModBlocks.YELLOW_MARBLE_TILES.get()));

        //Buttons
        buttonBlock(ModBlocks.GOLDEN_PALM_BUTTON.get(), blockTexture(ModBlocks.GOLDEN_PALM_PLANKS.get()));

        //PresurePlates
        pressurePlateBlock(ModBlocks.GOLDEN_PALM_PRESSURE_PLATE.get(), blockTexture(ModBlocks.GOLDEN_PALM_PLANKS.get()));

        //Fences
        fenceBlock(ModBlocks.GOLDEN_PALM_FENCE.get(), blockTexture(ModBlocks.GOLDEN_PALM_PLANKS.get()));

        //FenceGates
        fenceGateBlock(ModBlocks.GOLDEN_PALM_FENCE_GATE.get(), blockTexture(ModBlocks.GOLDEN_PALM_PLANKS.get()));

        //Doors
        doorBlockWithRenderType(ModBlocks.GOLDEN_PALM_DOOR.get(), modLoc("block/golden_palm_door_bottom"), modLoc("block/golden_palm_door_top"), "cutout");

        //TrapDoors
        trapdoorBlockWithRenderType(ModBlocks.GOLDEN_PALM_TRAPDOOR.get(), modLoc("block/golden_palm_trapdoor"), true, "cutout");


        //BlockItems Stairs
        blockItem(ModBlocks.GOLDEN_PALM_STAIRS);
        blockItem(ModBlocks.MARBLE_TILE_STAIRS);
        blockItem(ModBlocks.MARBLE_BRICK_STAIRS);
        blockItem(ModBlocks.CHARRED_MARBLE_TILE_STAIRS);
        blockItem(ModBlocks.CHARRED_MARBLE_BRICK_STAIRS);
        blockItem(ModBlocks.LIGHT_GRAY_MARBLE_BRICK_STAIRS);
        blockItem(ModBlocks.LIGHT_GRAY_MARBLE_TILE_STAIRS);
        blockItem(ModBlocks.GRAY_MARBLE_BRICK_STAIRS);
        blockItem(ModBlocks.GRAY_MARBLE_TILE_STAIRS);
        blockItem(ModBlocks.BROWN_MARBLE_BRICK_STAIRS);
        blockItem(ModBlocks.BROWN_MARBLE_TILE_STAIRS);
        blockItem(ModBlocks.RED_MARBLE_BRICK_STAIRS);
        blockItem(ModBlocks.RED_MARBLE_TILE_STAIRS);
        blockItem(ModBlocks.ORANGE_MARBLE_BRICK_STAIRS);
        blockItem(ModBlocks.ORANGE_MARBLE_TILE_STAIRS);
        blockItem(ModBlocks.YELLOW_MARBLE_BRICK_STAIRS);
        blockItem(ModBlocks.YELLOW_MARBLE_TILE_STAIRS);
        blockItem(ModBlocks.LIME_MARBLE_BRICK_STAIRS);
        blockItem(ModBlocks.LIME_MARBLE_TILE_STAIRS);
        blockItem(ModBlocks.GREEN_MARBLE_BRICK_STAIRS);
        blockItem(ModBlocks.GREEN_MARBLE_TILE_STAIRS);
        blockItem(ModBlocks.CYAN_MARBLE_BRICK_STAIRS);
        blockItem(ModBlocks.CYAN_MARBLE_TILE_STAIRS);
        blockItem(ModBlocks.LIGHT_BLUE_MARBLE_BRICK_STAIRS);
        blockItem(ModBlocks.LIGHT_BLUE_MARBLE_TILE_STAIRS);
        blockItem(ModBlocks.BLUE_MARBLE_BRICK_STAIRS);
        blockItem(ModBlocks.BLUE_MARBLE_TILE_STAIRS);
        blockItem(ModBlocks.PURPLE_MARBLE_BRICK_STAIRS);
        blockItem(ModBlocks.PURPLE_MARBLE_TILE_STAIRS);
        blockItem(ModBlocks.MAGENTA_MARBLE_BRICK_STAIRS);
        blockItem(ModBlocks.MAGENTA_MARBLE_TILE_STAIRS);
        blockItem(ModBlocks.PINK_MARBLE_BRICK_STAIRS);
        blockItem(ModBlocks.PINK_MARBLE_TILE_STAIRS);

        //BlockItems Slabs
        blockItem(ModBlocks.GOLDEN_PALM_SLAB);
        blockItem(ModBlocks.MARBLE_BRICK_SLABS);
        blockItem(ModBlocks.MARBLE_TILE_SLABS);
        blockItem(ModBlocks.CHARRED_MARBLE_BRICK_SLABS);
        blockItem(ModBlocks.CHARRED_MARBLE_TILE_SLABS);
        blockItem(ModBlocks.LIGHT_GRAY_MARBLE_BRICK_SLABS);
        blockItem(ModBlocks.LIGHT_GRAY_MARBLE_TILE_SLABS);
        blockItem(ModBlocks.GRAY_MARBLE_BRICK_SLABS);
        blockItem(ModBlocks.GRAY_MARBLE_TILE_SLABS);
        blockItem(ModBlocks.BROWN_MARBLE_BRICK_SLABS);
        blockItem(ModBlocks.BROWN_MARBLE_TILE_SLABS);
        blockItem(ModBlocks.RED_MARBLE_BRICK_SLABS);
        blockItem(ModBlocks.RED_MARBLE_TILE_SLABS);
        blockItem(ModBlocks.ORANGE_MARBLE_BRICK_SLABS);
        blockItem(ModBlocks.ORANGE_MARBLE_TILE_SLABS);
        blockItem(ModBlocks.YELLOW_MARBLE_BRICK_SLABS);
        blockItem(ModBlocks.YELLOW_MARBLE_TILE_SLABS);
        blockItem(ModBlocks.LIME_MARBLE_BRICK_SLABS);
        blockItem(ModBlocks.LIME_MARBLE_TILE_SLABS);
        blockItem(ModBlocks.GREEN_MARBLE_BRICK_SLABS);
        blockItem(ModBlocks.GREEN_MARBLE_TILE_SLABS);
        blockItem(ModBlocks.CYAN_MARBLE_BRICK_SLABS);
        blockItem(ModBlocks.CYAN_MARBLE_TILE_SLABS);
        blockItem(ModBlocks.LIGHT_BLUE_MARBLE_BRICK_SLABS);
        blockItem(ModBlocks.LIGHT_BLUE_MARBLE_TILE_SLABS);
        blockItem(ModBlocks.BLUE_MARBLE_BRICK_SLABS);
        blockItem(ModBlocks.BLUE_MARBLE_TILE_SLABS);
        blockItem(ModBlocks.PURPLE_MARBLE_BRICK_SLABS);
        blockItem(ModBlocks.PURPLE_MARBLE_TILE_SLABS);
        blockItem(ModBlocks.MAGENTA_MARBLE_BRICK_SLABS);
        blockItem(ModBlocks.MAGENTA_MARBLE_TILE_SLABS);
        blockItem(ModBlocks.PINK_MARBLE_BRICK_SLABS);
        blockItem(ModBlocks.PINK_MARBLE_TILE_SLABS);



        //BlockItem Redstone Stuff for now
        blockItem(ModBlocks.GOLDEN_PALM_PRESSURE_PLATE);
        blockItem(ModBlocks.GOLDEN_PALM_FENCE_GATE);
        blockItem(ModBlocks.GOLDEN_PALM_TRAPDOOR, "_bottom");
    }

    private void simpleBlockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    private void saplingBlock(DeferredBlock<Block> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(),
                models().cross(BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void herbsBlock(DeferredBlock<Block> blockRegistryObject) {
        Block block = blockRegistryObject.get();
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
        String blockName = blockId.getPath();
        ResourceLocation textureLoc = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "block/herbs/" + blockName);

        simpleBlock(block,
                models().crop(blockId.getPath(), textureLoc).renderType("cutout"));
    }

    private void leavesBlock(DeferredBlock<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().singleTexture(BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath(), ResourceLocation.parse("minecraft:block/leaves"),
                        "all", blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("ascension:block/" + deferredBlock.getId().getPath()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock, String appendix) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("ascension:block/" + deferredBlock.getId().getPath() + appendix));
    }


}
