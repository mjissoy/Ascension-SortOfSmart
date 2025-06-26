package net.thejadeproject.ascension.datagen;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
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
        add(ModBlocks.IRONWOOD_SPROUT_BLOCK.get(),
                block -> createSingleItemTable(ModItems.IRONWOOD_SPROUT.get()));
        add(ModBlocks.WHITE_JADE_ORCHID_BLOCK.get(),
                block -> createSingleItemTable(ModItems.WHITE_JADE_ORCHID.get()));


        add(ModBlocks.JADE_ORE.get(),
                block -> createOreDrop(ModBlocks.JADE_ORE.get(), ModItems.RAW_JADE.get()));

        this.dropSelf(ModBlocks.GOLDEN_PALM_LOG.get());
        this.dropSelf(ModBlocks.GOLDEN_PALM_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_GOLDEN_PALM_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_GOLDEN_PALM_LOG.get());
        this.dropSelf(ModBlocks.GOLDEN_PALM_PLANKS.get());
        this.dropSelf(ModBlocks.GOLDEN_PALM_SAPLING.get());

        dropSelf(ModBlocks.GOLDEN_PALM_STAIRS.get());
        add(ModBlocks.GOLDEN_PALM_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.GOLDEN_PALM_SLAB.get()));

        dropSelf(ModBlocks.GOLDEN_PALM_PRESSURE_PLATE.get());
        dropSelf(ModBlocks.GOLDEN_PALM_BUTTON.get());
        dropSelf(ModBlocks.GOLDEN_PALM_FENCE.get());
        dropSelf(ModBlocks.GOLDEN_PALM_FENCE_GATE.get());
        dropSelf(ModBlocks.GOLDEN_PALM_TRAPDOOR.get());

        add(ModBlocks.GOLDEN_PALM_DOOR.get(),
                block -> createDoorTable(ModBlocks.GOLDEN_PALM_DOOR.get()));






        this.add(ModBlocks.GOLDEN_PALM_LEAVES.get(), block ->
                createLeavesDropsWithSecondary(
                        block,
                        ModBlocks.GOLDEN_PALM_SAPLING.get(),
                        ModItems.GOLDEN_SUN_LEAF.get(),
                        NORMAL_LEAVES_SAPLING_CHANCES,
                        0.005F
                ));


    }



    protected LootTable.Builder createLeavesDropsWithSecondary(Block leaves, Block sapling, Item secondaryItem, float[] saplingChances, float secondaryChance) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(sapling)
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(leaves)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(LeavesBlock.PERSISTENT, false)))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                .when(LootItemRandomChanceCondition.randomChance(saplingChances[0])))
                        .add(LootItem.lootTableItem(secondaryItem)
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(leaves)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(LeavesBlock.PERSISTENT, false)))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 1.0F)))
                                .when(LootItemRandomChanceCondition.randomChance(secondaryChance))))
                .apply(ApplyExplosionDecay.explosionDecay());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCK.getEntries().stream().map(Holder::value)::iterator;
    }
}