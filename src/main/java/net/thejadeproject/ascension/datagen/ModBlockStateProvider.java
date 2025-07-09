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


        logBlock(((RotatedPillarBlock) ModBlocks.GOLDEN_PALM_LOG.get()));
        axisBlock(((RotatedPillarBlock) ModBlocks.GOLDEN_PALM_WOOD.get()), blockTexture(ModBlocks.GOLDEN_PALM_LOG.get()), blockTexture(ModBlocks.GOLDEN_PALM_LOG.get()));
        logBlock(((RotatedPillarBlock) ModBlocks.STRIPPED_GOLDEN_PALM_LOG.get()));
        axisBlock(((RotatedPillarBlock) ModBlocks.STRIPPED_GOLDEN_PALM_WOOD.get()), blockTexture(ModBlocks.STRIPPED_GOLDEN_PALM_LOG.get()), blockTexture(ModBlocks.STRIPPED_GOLDEN_PALM_LOG.get()));

        blockItem(ModBlocks.GOLDEN_PALM_LOG);
        blockItem(ModBlocks.GOLDEN_PALM_WOOD);
        blockItem(ModBlocks.STRIPPED_GOLDEN_PALM_LOG);
        blockItem(ModBlocks.STRIPPED_GOLDEN_PALM_WOOD);

        blockWithItem(ModBlocks.GOLDEN_PALM_PLANKS);

        leavesBlock(ModBlocks.GOLDEN_PALM_LEAVES);
        saplingBlock(ModBlocks.GOLDEN_PALM_SAPLING);

        stairsBlock(ModBlocks.GOLDEN_PALM_STAIRS.get(), blockTexture(ModBlocks.GOLDEN_PALM_PLANKS.get()));
        slabBlock(ModBlocks.GOLDEN_PALM_SLAB.get(), blockTexture(ModBlocks.GOLDEN_PALM_PLANKS.get()), blockTexture(ModBlocks.GOLDEN_PALM_PLANKS.get()));
        buttonBlock(ModBlocks.GOLDEN_PALM_BUTTON.get(), blockTexture(ModBlocks.GOLDEN_PALM_PLANKS.get()));
        pressurePlateBlock(ModBlocks.GOLDEN_PALM_PRESSURE_PLATE.get(), blockTexture(ModBlocks.GOLDEN_PALM_PLANKS.get()));
        fenceBlock(ModBlocks.GOLDEN_PALM_FENCE.get(), blockTexture(ModBlocks.GOLDEN_PALM_PLANKS.get()));
        fenceGateBlock(ModBlocks.GOLDEN_PALM_FENCE_GATE.get(), blockTexture(ModBlocks.GOLDEN_PALM_PLANKS.get()));
        doorBlockWithRenderType(ModBlocks.GOLDEN_PALM_DOOR.get(), modLoc("block/golden_palm_door_bottom"), modLoc("block/golden_palm_door_top"), "cutout");
        trapdoorBlockWithRenderType(ModBlocks.GOLDEN_PALM_TRAPDOOR.get(), modLoc("block/golden_palm_trapdoor"), true, "cutout");


        blockItem(ModBlocks.GOLDEN_PALM_STAIRS);
        blockItem(ModBlocks.GOLDEN_PALM_SLAB);
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
