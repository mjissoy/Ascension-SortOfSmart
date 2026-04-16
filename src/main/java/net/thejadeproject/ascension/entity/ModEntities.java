package net.thejadeproject.ascension.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.entity.custom.CushionEntity;
import net.thejadeproject.ascension.entity.custom.form.PlayerBodyEntity;
import net.thejadeproject.ascension.entity.custom.shaders.RiftEntity;
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

    public static final DeferredHolder<EntityType<?>, EntityType<RiftEntity>> RIFT =
            ENTITY_TYPES.register("rift",
                    () -> EntityType.Builder.of(
                                    RiftEntity::new,
                                    MobCategory.MISC
                            )
                            .sized(0.5f, 3.0f)
                            .clientTrackingRange(64)
                            .updateInterval(1)
                            .build("rift")

            );

    public static final DeferredHolder<EntityType<?>,EntityType<PlayerBodyEntity>> FAKE_PLAYER =
            ENTITY_TYPES.register("player_body", () ->
                    EntityType.Builder.of(PlayerBodyEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.8f) // player size
                            .clientTrackingRange(8)
                            .updateInterval(2)
                            .build("player_body")
            );
    public static final Supplier<EntityType<CushionEntity>> CUSHION_ENTITY =
            ENTITY_TYPES.register("cushion_entity", () -> EntityType.Builder.of(CushionEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).build("cushion_entity"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
