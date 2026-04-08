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
            new GenericPath(Component.literal("Essence"))
                    .addMajorRealmName("Mortal")
                    .addMajorRealmName("Qi Condensation")
                    .addMajorRealmName("Formation Establishment")
                    .addMajorRealmName("Golden Core")
                    .addMajorRealmName("Nascent Soul")
            );
    public static final DeferredHolder<IPath, ? extends GenericPath> BODY = PATHS.register("body",()->
            new GenericPath(Component.literal("Body"))
                    .addMajorRealmName("Mortal")
                    .addMajorRealmName("Skin Tempering")
                    .addMajorRealmName("Sinew Weaving")
                    .addMajorRealmName("Bone Forging")
                    .addMajorRealmName("Heart Kindling")
    );

    public static final DeferredHolder<IPath, ? extends GenericPath> FIRE = PATHS.register("fire",()->
            new ComprehensionPath(Component.literal("Fire"))
                    .addMajorRealmName("Kindling")
                    .addMajorRealmName("Ignition")
                    .addMajorRealmName("True Flame")
                    .addMajorRealmName("Origin Flame")
    );

    public static final DeferredHolder<IPath, ? extends GenericPath> SWORD = PATHS.register("sword",()->
            new ComprehensionPath(Component.literal("Sword"))
                    .addMajorRealmName("Initiate")
                    .addMajorRealmName("Intent")
                    .addMajorRealmName("Aura")
                    .addMajorRealmName("Unity")
    );
    public static void register(IEventBus modEventBus){
        PATHS.register(modEventBus);
    }
}
