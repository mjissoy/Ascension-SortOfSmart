package net.thejadeproject.ascension.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.guis.blockentity.PillCauldronLowHumanEntity;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, AscensionCraft.MOD_ID);

    public static final Supplier<BlockEntityType<PillCauldronLowHumanEntity>> PILL_CAULDRON_HUMAN =
            BLOCK_ENTITIES.register("pill_cauldron_human", () -> BlockEntityType.Builder.of(
                    PillCauldronLowHumanEntity::new, ModBlocks.HUMAN_CAULDRON.get()).build(null));





    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
