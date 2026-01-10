package net.thejadeproject.ascension.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.entity.custom.TreasureRatEntity;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, AscensionCraft.MOD_ID);


    public static final Supplier<EntityType<TreasureRatEntity>> RAT =
            ENTITY_TYPES.register("treasure_rat", () -> EntityType.Builder.of(TreasureRatEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 0.35f).build("treasure_rat"));



    public static final Supplier<EntityType<PoisonPillProjectile>> POISON_PILL =
            ENTITY_TYPES.register("poison_pill",
                    () -> EntityType.Builder.<PoisonPillProjectile>of(
                                    (entityType, level) -> new PoisonPillProjectile(entityType, level),
                                    MobCategory.MISC
                            )
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("poison_pill"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
