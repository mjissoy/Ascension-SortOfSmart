package net.thejadeproject.ascension.refactor_packages.techniques;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.paths.IPath;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysique;
import net.thejadeproject.ascension.refactor_packages.physiques.custom.GenericPhysique;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.stats.custom.ModStats;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.GenericTechnique;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.stat_change_handlers.BasicStatChangeHandler;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ModifierOperation;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ValueContainerModifier;

import java.util.Set;

public class ModTechniques {
    public static final DeferredRegister<ITechnique> TECHNIQUES =DeferredRegister.create(AscensionRegistries.Techniques.TECHNIQUES_REGISTRY, AscensionCraft.MOD_ID);

    public static final ResourceLocation test = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"test");
    public static BasicStatChangeHandler testHandler = new BasicStatChangeHandler()
            .addMinorRealmStatModifier(ModStats.VITALITY.getId(),new ValueContainerModifier(1, ModifierOperation.ADD_BASE, test))
            .addMinorRealmAttributeModifier(Attributes.MAX_HEALTH,new ValueContainerModifier(0.2,ModifierOperation.MULTIPLY_BASE,test));

    public static final DeferredHolder<ITechnique, ? extends GenericTechnique> BASIC_CULTIVATION_TECHNIQUE = TECHNIQUES.register("basic_cultivation_technique",()->
            new GenericTechnique(ModPaths.ESSENCE.getId(),Component.literal("Basic Cultivation Technique"),2.0, Set.of())
                    .setStatChangeHandler(testHandler));
    public static final DeferredHolder<ITechnique, ? extends GenericTechnique> ADVANCED_CULTIVATION_TECHNIQUE = TECHNIQUES.register("advanced_cultivation_technique",()->
            new GenericTechnique(ModPaths.ESSENCE.getId(),Component.literal("Advanced Cultivation Technique"),10.0,Set.of())
                    .setStatChangeHandler(testHandler));
    public static final DeferredHolder<ITechnique, ? extends GenericTechnique> SWORD_COMPREHENSION_TECHNIQUE = TECHNIQUES.register("sword_comprehension_technique",()->
            new GenericTechnique(ModPaths.SWORD.getId(),Component.literal("Sword Comprehension Technique"),10.0,Set.of()));



    public static void register(IEventBus modEventBus){
        TECHNIQUES.register(modEventBus);
    }
}
