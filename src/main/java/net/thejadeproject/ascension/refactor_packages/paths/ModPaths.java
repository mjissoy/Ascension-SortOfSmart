package net.thejadeproject.ascension.refactor_packages.paths;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.paths.custom.ComprehensionPath;
import net.thejadeproject.ascension.refactor_packages.paths.custom.GenericPath;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;

public class ModPaths {
    public static final DeferredRegister<IPath> PATHS =DeferredRegister.create(AscensionRegistries.Paths.PATHS_REGISTRY, AscensionCraft.MOD_ID);


    public static final DeferredHolder<IPath, ? extends GenericPath> ESSENCE = PATHS.register("essence",()->
            new GenericPath(Component.translatable("ascension.path.essence"))
                    .addMajorRealmName("ascension.path.essence.mortal")
                    .addMajorRealmName("ascension.path.essence.qi_condensation")
                    .addMajorRealmName("ascension.path.essence.formation_establishment")
                    .addMajorRealmName("ascension.path.essence.golden_core")
                    .addMajorRealmName("ascension.path.essence.nascent_core")
            );
    public static final DeferredHolder<IPath, ? extends GenericPath> BODY = PATHS.register("body",()->
            new GenericPath(Component.translatable("ascension.path.body"))
                    .addMajorRealmName("ascension.path.body.bortal")
                    .addMajorRealmName("ascension.path.body.skin_tempering")
                    .addMajorRealmName("ascension.path.body.sinew_weaving")
                    .addMajorRealmName("ascension.path.body.bone_forging")
                    .addMajorRealmName("ascension.path.body.heart_kindling")
    );

    public static final DeferredHolder<IPath, ? extends GenericPath> FIRE = PATHS.register("fire",()->
            new ComprehensionPath(Component.translatable("ascension.path.fire"))
                    .addMajorRealmName("ascension.path.fire.kindling")
                    .addMajorRealmName("ascension.path.fire.ignition")
                    .addMajorRealmName("ascension.path.fire.true_flame")
                    .addMajorRealmName("ascension.path.fire.origin_flame")
    );

    public static final DeferredHolder<IPath, ? extends GenericPath> SWORD = PATHS.register("sword",()->
            new ComprehensionPath(Component.translatable("ascension.path.sword"))
                    .addMajorRealmName("ascension.path.sword.initiate")
                    .addMajorRealmName("ascension.path.sword.intent")
                    .addMajorRealmName("ascension.path.sword.aura")
                    .addMajorRealmName("ascension.path.sword.unity")
    );
    public static void register(IEventBus modEventBus){
        PATHS.register(modEventBus);
    }
}
