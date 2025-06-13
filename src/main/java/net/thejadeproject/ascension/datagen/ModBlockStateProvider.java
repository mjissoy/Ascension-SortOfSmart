package net.thejadeproject.ascension.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.thejadeproject.ascension.AscensionCraft;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.thejadeproject.ascension.blocks.ModBlocks;

import java.util.function.Function;


public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper){
        super(output, AscensionCraft.MOD_ID, exFileHelper);
    }
    @Override
    protected void registerStatesAndModels() {
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
