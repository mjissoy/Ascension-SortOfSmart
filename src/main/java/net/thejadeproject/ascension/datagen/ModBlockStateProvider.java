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


        simpleBlockWithItem(ModBlocks.JADE_ORE);
        simpleBlockWithItem(ModBlocks.JADE_BLOCK);

        simpleBlockWithItem(ModBlocks.MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.MOSSY_MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.CRACKED_MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.MARBLE_TILES);
        simpleBlockWithItem(ModBlocks.MARBLE_CHISELED);
        simpleBlockWithItem(ModBlocks.RAW_MARBLE);
        simpleBlockWithItem(ModBlocks.POLISHED_MARBLE);
        simpleBlockWithItem(ModBlocks.POLISHED_BURNED_MARBLE);
        simpleBlockWithItem(ModBlocks.CHARRED_MARBLE);
        simpleBlockWithItem(ModBlocks.CHARRED_MARBLE_BRICKS);
        simpleBlockWithItem(ModBlocks.CHARRED_MARBLE_CHISELED);
        simpleBlockWithItem(ModBlocks.CHARRED_MARBLE_TILES);


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

        //Slabs
        slabBlock(ModBlocks.GOLDEN_PALM_SLAB.get(), blockTexture(ModBlocks.GOLDEN_PALM_PLANKS.get()), blockTexture(ModBlocks.GOLDEN_PALM_PLANKS.get()));
        slabBlock(ModBlocks.MARBLE_BRICK_SLABS.get(), blockTexture(ModBlocks.MARBLE_BRICKS.get()), blockTexture(ModBlocks.MARBLE_BRICKS.get()));
        slabBlock(ModBlocks.MARBLE_TILE_SLABS.get(), blockTexture(ModBlocks.MARBLE_TILES.get()), blockTexture(ModBlocks.MARBLE_TILES.get()));
        slabBlock(ModBlocks.CHARRED_MARBLE_BRICK_SLABS.get(), blockTexture(ModBlocks.CHARRED_MARBLE_BRICKS.get()), blockTexture(ModBlocks.CHARRED_MARBLE_BRICKS.get()));
        slabBlock(ModBlocks.CHARRED_MARBLE_TILE_SLABS.get(), blockTexture(ModBlocks.CHARRED_MARBLE_TILES.get()), blockTexture(ModBlocks.CHARRED_MARBLE_TILES.get()));

        //Walls
        wallBlock(ModBlocks.MARBLE_BRICK_WALLS.get(), blockTexture(ModBlocks.MARBLE_BRICKS.get()));
        wallBlock(ModBlocks.MARBLE_TILE_WALLS.get(), blockTexture(ModBlocks.MARBLE_TILES.get()));
        wallBlock(ModBlocks.CHARRED_MARBLE_BRICK_WALLS.get(), blockTexture(ModBlocks.CHARRED_MARBLE_BRICKS.get()));
        wallBlock(ModBlocks.CHARRED_MARBLE_TILE_WALLS.get(), blockTexture(ModBlocks.CHARRED_MARBLE_TILES.get()));

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


        blockItem(ModBlocks.GOLDEN_PALM_STAIRS);
        blockItem(ModBlocks.MARBLE_TILE_STAIRS);
        blockItem(ModBlocks.MARBLE_BRICK_STAIRS);
        blockItem(ModBlocks.CHARRED_MARBLE_TILE_STAIRS);
        blockItem(ModBlocks.CHARRED_MARBLE_BRICK_STAIRS);
        blockItem(ModBlocks.GOLDEN_PALM_SLAB);
        blockItem(ModBlocks.MARBLE_BRICK_SLABS);
        blockItem(ModBlocks.MARBLE_TILE_SLABS);
        blockItem(ModBlocks.CHARRED_MARBLE_BRICK_SLABS);
        blockItem(ModBlocks.CHARRED_MARBLE_TILE_SLABS);
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
