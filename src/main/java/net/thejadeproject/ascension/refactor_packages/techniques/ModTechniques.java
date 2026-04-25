package net.thejadeproject.ascension.refactor_packages.techniques;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.stats.custom.ModStats;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.FiveElementCultivationTechnique;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.GenericTechnique;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.ScholarlySoulTechnique;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.stat_change_handlers.BasicStatChangeHandler;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ModifierOperation;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ValueContainerModifier;

import java.util.Set;

public class ModTechniques {
    public static final DeferredRegister<ITechnique> TECHNIQUES =DeferredRegister.create(AscensionRegistries.Techniques.TECHNIQUES_REGISTRY, AscensionCraft.MOD_ID);

    public static final ResourceLocation test = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"test");
    public static BasicStatChangeHandler testHandler = new BasicStatChangeHandler()
            .addMinorRealmStatModifier(ModStats.VITALITY.getId(),new ValueContainerModifier(2, ModifierOperation.ADD_BASE, test))
            .addMajorRealmStatModifier(ModStats.VITALITY.getId(),new ValueContainerModifier(0.2,ModifierOperation.MULTIPLY_FINAL,test))
            .addMinorRealmStatModifier(ModStats.AGILITY.getId(),new ValueContainerModifier(5,ModifierOperation.ADD_BASE,test));
    public static final DeferredHolder<ITechnique, ? extends GenericTechnique> BASIC_CULTIVATION_TECHNIQUE = TECHNIQUES.register("basic_cultivation_technique",()->
            new GenericTechnique(ModPaths.ESSENCE.getId(),Component.literal("Basic Cultivation Technique"),2.0, Set.of())
                    .setStatChangeHandler(testHandler));
    public static final DeferredHolder<ITechnique, ? extends GenericTechnique> ADVANCED_CULTIVATION_TECHNIQUE = TECHNIQUES.register("advanced_cultivation_technique",()->
            new GenericTechnique(ModPaths.ESSENCE.getId(),Component.literal("Advanced Cultivation Technique"),10.0,Set.of())
                    .setStatChangeHandler(testHandler));
    public static final DeferredHolder<ITechnique, ? extends GenericTechnique> SWORD_COMPREHENSION_TECHNIQUE = TECHNIQUES.register("sword_comprehension_technique",()->
            new GenericTechnique(ModPaths.SWORD.getId(),Component.literal("Sword Comprehension Technique"),10.0,Set.of()));

    public static final DeferredHolder<ITechnique,? extends FiveElementCultivationTechnique> FIVE_ELEMENT_CIRCULATION_METHOD = TECHNIQUES.register("five_element_cultivation_technique",(
            ()->new FiveElementCultivationTechnique(testHandler)));

    public static final DeferredHolder<ITechnique, ? extends ScholarlySoulTechnique> SCHOLARLY_SOUL_TECHNIQUE =
            TECHNIQUES.register("scholarly_soul_technique",
                    () -> new ScholarlySoulTechnique(testHandler));

    public static void register(IEventBus modEventBus){
        TECHNIQUES.register(modEventBus);
    }
}
