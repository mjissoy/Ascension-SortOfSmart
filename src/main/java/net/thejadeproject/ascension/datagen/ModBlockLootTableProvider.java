package net.thejadeproject.ascension.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.items.ModItems;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.JADE_BLOCK.get());
        dropSelf(ModBlocks.PILL_CAULDRON_HUMAN_LOW.get());
        add(ModBlocks.GOLDEN_SUN_LEAF_BLOCK.get(),
                block -> createSingleItemTable(ModItems.GOLDEN_SUN_LEAF.get()));
        add(ModBlocks.JADE_ORE.get(),
                block -> createOreDrop(ModBlocks.JADE_ORE.get(), ModItems.RAW_JADE.get()));

        this.dropSelf(ModBlocks.GOLDEN_PALM_LOG.get());
        this.dropSelf(ModBlocks.GOLDEN_PALM_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_GOLDEN_PALM_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_GOLDEN_PALM_LOG.get());
        this.dropSelf(ModBlocks.GOLDEN_PALM_PLANKS.get());
        this.dropSelf(ModBlocks.GOLDEN_PALM_SAPLING.get());

        this.add(ModBlocks.GOLDEN_PALM_LEAVES.get(), block ->
                createLeavesDrops(block, ModBlocks.GOLDEN_PALM_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCK.getEntries().stream().map(Holder::value)::iterator;
    }
}