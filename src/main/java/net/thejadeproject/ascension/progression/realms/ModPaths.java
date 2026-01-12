package net.thejadeproject.ascension.progression.realms;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.constants.LivingEntityState;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.registries.AscensionRegistries;

import java.util.List;
import java.util.Set;

public class ModPaths {
    public static final DeferredRegister<IPath> PATHS = DeferredRegister.create(AscensionRegistries.Paths.PATHS_REGISTRY, AscensionCraft.MOD_ID);

    public static final DeferredHolder<IPath, ? extends GenericPath> ESSENCE_PATH = PATHS.register("essence",()->
            new GenericPath(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"essence"), Component.literal("Essence"),
                    List.of(
                            Component.literal("Mortal"), Component.literal("Qi Condensation"), Component.literal("Foundation Establishment"),
                            Component.literal("Core Formation"), Component.literal("Nascent Soul"), Component.literal("Soul Formation"),
                            Component.literal("Soul Transformation"), Component.literal("Spirit Severing"), Component.literal("Immortal Ascension"),
                            Component.literal("True Immortal"), Component.literal("Golden Immortal"), Component.literal("Universe Creation Realm")
                    ))
            );
    public static final DeferredHolder<IPath, ? extends GenericPath> INTENT_PATH = PATHS.register("intent",()->
            (new GenericPath(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"intent"), Component.literal("Intent"),
                    List.of(
                            Component.literal("Awakened Will"), Component.literal("Focused Mind"), Component.literal("Sharpened Desire"),
                            Component.literal("Unwavering Resolve"), Component.literal("Trinity Convergence"), Component.literal("Pentagonal Balance"),
                            Component.literal("Aura of Intent"), Component.literal("\"Soul Pressure"), Component.literal("Spatial Lock"),
                            Component.literal("Domain Seed"), Component.literal("Law Embodiment"), Component.literal("Heavenly Decree")
                    ))).setPathLivingEntityStates(Set.of(LivingEntityState.PHYSICAL,LivingEntityState.SOUL))
    );

    public static final DeferredHolder<IPath, ? extends GenericPath> BODY_PATH = PATHS.register("body",()->
            new GenericPath(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"body"), Component.literal("Body"),
                    List.of(
                            Component.literal("Skin Tempering"), Component.literal("Sinew Weaving"), Component.literal("Bone Forging"),
                            Component.literal("Heartkindle Stage"), Component.literal("Meridian Flow"), Component.literal("Core Consolidation"),
                            Component.literal("Vessel Refinement"), Component.literal("Energy Harmonization"), Component.literal("Nether Transformation"),
                            Component.literal("Stellar Genesis"), Component.literal("World Resonance"), Component.literal("Primordial Awakening")
                    ))
    );

    public static IPath getPath(String path){
        return getPath(ResourceLocation.bySeparator(path,':'));
    }
    public static IPath getPath(ResourceLocation location){
        return AscensionRegistries.Paths.PATHS_REGISTRY.get(location);
    }
    public static void register(IEventBus eventBus){
        PATHS.register(eventBus);
    }
}
