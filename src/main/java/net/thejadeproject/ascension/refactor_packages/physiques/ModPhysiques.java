package net.thejadeproject.ascension.refactor_packages.physiques;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.paths.IPath;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.paths.custom.GenericPath;
import net.thejadeproject.ascension.refactor_packages.physiques.custom.GenericPhysique;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;

import java.util.List;

public class ModPhysiques {
    public static final DeferredRegister<IPhysique> PHYSIQUES =DeferredRegister.create(AscensionRegistries.Physiques.PHSIQUES_REGISTRY, AscensionCraft.MOD_ID);



    /*
    .addPath(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"essence")) allows you to choose what path to give the Physique what it can cultivate.
    Without a path it can not cultivate anything. You can add as many paths as you want or as little as you want.

    .addPathBonus(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"essence"),0.5) is what chooses how fast it can cultivate that specific path.
    It can be set to 0.1 - Max Integer and the higher the faster. -[number] reduces efficiency

     */


    public static final DeferredHolder<IPhysique,? extends GenericPhysique> MORTAL = PHYSIQUES.register("mortal",()->
            new GenericPhysique(Component.translatable("ascension.physiques.mortal"))
                    .addPath(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"body"))
                    .addPath(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"essence"))
                    .addPathBonus(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"body"),0.5)
                    .addPathBonus(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"essence"),0.5)
            );
    public static final DeferredHolder<IPhysique,? extends GenericPhysique> CRIPPLE = PHYSIQUES.register("cripple",()->
            new GenericPhysique(Component.translatable("ascension.physiques.cripple"))
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



    public static final DeferredHolder<IPhysique, ? extends GenericPhysique> TYRANT_BODY = PHYSIQUES.register("tyrant_body",()->
            new GenericPhysique(Component.translatable("ascension.physiques.tyrant_body"))
                    .addPath(ModPaths.BODY.getId())
                    .addPathBonus(ModPaths.BODY.getId(),1.2)
                    .addPath(ModPaths.FIST.getId())
                    .addPathBonus(ModPaths.FIST.getId(), 2.2)
    );
    public static void register(IEventBus modEventBus){
        PHYSIQUES.register(modEventBus);
    }

}
