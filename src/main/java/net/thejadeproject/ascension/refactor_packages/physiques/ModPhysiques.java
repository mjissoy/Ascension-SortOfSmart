package net.thejadeproject.ascension.refactor_packages.physiques;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.paths.IPath;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.paths.custom.GenericPath;
import net.thejadeproject.ascension.refactor_packages.physiques.custom.GenericPhysique;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;

import java.util.List;

public class ModPhysiques {
    public static final DeferredRegister<IPhysique> PHYSIQUES =DeferredRegister.create(AscensionRegistries.Physiques.PHSIQUES_REGISTRY, AscensionCraft.MOD_ID);


    public static final DeferredHolder<IPhysique,? extends GenericPhysique> MORTAL = PHYSIQUES.register("mortal",()->
            new GenericPhysique(Component.translatable("ascension.physiques.mortal"))
                    .addPath(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"body"))
                    .addPath(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"essence"))
                    .addPathBonus(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"body"),0.5)
                    .addPathBonus(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"essence"),0.5)
            );

    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> SEVERED_MERIDIANS = PHYSIQUES.register("severed_meridians",()->
            new GenericPhysique(Component.translatable("ascension.physiques.severed_meridians"))
                    .addPath(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"body"))
                    .addPathBonus(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"body"),0.5)
    );
    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> SWORD_BONE = PHYSIQUES.register("sword_bone",()->
            new GenericPhysique(Component.translatable("ascension.physiques.sword_bone"))
                    .addPath(ModPaths.BODY.getId())
                    .addPathBonus(ModPaths.BODY.getId(),0.5)
                    .addPath(ModPaths.ESSENCE.getId())
                    .addPathBonus(ModPaths.ESSENCE.getId(),0.5)
                    .addPath(ModPaths.SWORD.getId())
                    .addPathBonus(ModPaths.SWORD.getId(),2.0)
    );
    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> FLAME_TOUCHED = PHYSIQUES.register("flame_touched",()->
            new GenericPhysique(Component.translatable("ascension.physiques.flame_touched"))
                    .addPath(ModPaths.BODY.getId())
                    .addPath(ModPaths.ESSENCE.getId())
                    .addPathBonus(ModPaths.BODY.getId(),0.5)
                    .addPathBonus(ModPaths.ESSENCE.getId(),1.0)
                    .addPathBonus(ModPaths.FIRE.getId(),2.0)
    );

    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> SCHOLARS_SOUL = PHYSIQUES.register("scholars_soul",()->
            new GenericPhysique(Component.translatable("ascension.physiques.scholars_soul"))
                    .addPath(ModPaths.SOUL.getId())
                    .addPathBonus(ModPaths.SOUL.getId(),1.5)
    );

    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> WORLD_DOMINATOR =
            PHYSIQUES.register("world_dominator", () ->
                    new GenericPhysique(Component.translatable("ascension.physiques.world_dominator")) {

                        @Override
                        public void onPhysiqueAdded(
                                IEntityData heldEntity,
                                ResourceLocation oldPhysique,
                                IPhysiqueData oldPhysiqueData
                        ) {
                            super.onPhysiqueAdded(heldEntity, oldPhysique, oldPhysiqueData);

                            if (heldEntity.getAttachedEntity() instanceof ServerPlayer player) {
                                Component message = Component.translatable(
                                                "ascension.message.physique.world_dominator.acquired",
                                                player.getDisplayName()
                                        )
                                        .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD);

                                player.server.getPlayerList().broadcastSystemMessage(message, false);
                            }
                        }
                    }
                            .addPath(ModPaths.BODY.getId()).addPathBonus(ModPaths.BODY.getId(), 5.0)
            );




    public static void register(IEventBus modEventBus){
        PHYSIQUES.register(modEventBus);
    }

}
