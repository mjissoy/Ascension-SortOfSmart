package net.thejadeproject.ascension.blocks.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.blocks.entity.ores.FrostSilverOreBlockEntity;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, AscensionCraft.MOD_ID);



    public static final Supplier<BlockEntityType<PillCauldronLowHumanEntity>> PILL_CAULDRON_LOW_HUMAN =
            BLOCK_ENTITIES.register("pill_cauldron_low_human", () -> BlockEntityType.Builder.of(
                    PillCauldronLowHumanEntity::new, ModBlocks.PILL_CAULDRON_HUMAN_LOW.get()).build(null));

    public static final Supplier<BlockEntityType<FrostSilverOreBlockEntity>> FROST_SILVER_ORE_BE =
            BLOCK_ENTITIES.register("frost_silver_ore", () -> BlockEntityType.Builder.of(
                    FrostSilverOreBlockEntity::new, ModBlocks.FROST_SILVER_ORE.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
