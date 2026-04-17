package net.thejadeproject.ascension.refactor_packages.techniques;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.stats.custom.ModStats;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.GenericTechnique;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.stat_change_handlers.BasicStatChangeHandler;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ModifierOperation;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ValueContainerModifier;
import net.thejadeproject.ascension.runic_path.Runes;
import net.thejadeproject.ascension.runic_path.technique.RunicTechnique;
import net.thejadeproject.ascension.runic_path.technique.helpers.CultivationConditions;

import java.util.Set;

public class ModTechniques {
    public static final DeferredRegister<ITechnique> TECHNIQUES =DeferredRegister.create(AscensionRegistries.Techniques.TECHNIQUES_REGISTRY, AscensionCraft.MOD_ID);

    public static final ResourceLocation test = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"test");
    public static BasicStatChangeHandler testHandler = new BasicStatChangeHandler()
            .addMinorRealmStatModifier(ModStats.VITALITY.getId(),new ValueContainerModifier(1, ModifierOperation.ADD_BASE, test))
            .addMinorRealmAttributeModifier(Attributes.MAX_HEALTH,new ValueContainerModifier(0.2,ModifierOperation.MULTIPLY_BASE,test))
            .addMinorRealmStatModifier(ModStats.AGILITY.getId(),new ValueContainerModifier(5,ModifierOperation.ADD_BASE,test));

    public static final ResourceLocation test2 = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"test2");
    public static BasicStatChangeHandler testHandler2 = new BasicStatChangeHandler()
            .addMinorRealmStatModifier(ModStats.VITALITY.getId(), new ValueContainerModifier(1, ModifierOperation.ADD_BASE, test2))
            .addMinorRealmStatModifier(ModStats.STRENGTH.getId(), new ValueContainerModifier(1, ModifierOperation.ADD_BASE, test2))
            .addMinorRealmStatModifier(ModStats.AGILITY.getId(),new ValueContainerModifier(5,ModifierOperation.ADD_BASE,test2))
            .addMinorRealmAttributeModifier(Attributes.MAX_HEALTH, new ValueContainerModifier(0.2, ModifierOperation.MULTIPLY_BASE, test2))
            .addMinorRealmAttributeModifier(Attributes.ATTACK_DAMAGE, new ValueContainerModifier(0.15, ModifierOperation.MULTIPLY_BASE, test2))
            .addMinorRealmAttributeModifier(Attributes.ATTACK_SPEED, new ValueContainerModifier(0.05, ModifierOperation.MULTIPLY_BASE, test2))
            .addMinorRealmAttributeModifier(Attributes.MOVEMENT_SPEED, new ValueContainerModifier(0.03, ModifierOperation.MULTIPLY_BASE, test2))
            .addMinorRealmAttributeModifier(Attributes.ARMOR, new ValueContainerModifier(0.1, ModifierOperation.ADD_BASE, test2))
            .addMinorRealmAttributeModifier(Attributes.ARMOR_TOUGHNESS, new ValueContainerModifier(0.05, ModifierOperation.ADD_BASE, test2));

    public static final DeferredHolder<ITechnique, ? extends GenericTechnique> BASIC_CULTIVATION_TECHNIQUE = TECHNIQUES.register("basic_cultivation_technique",()->
            new GenericTechnique(ModPaths.ESSENCE.getId(),Component.literal("Basic Cultivation Technique"),2.0, Set.of())
                    .setStatChangeHandler(testHandler));
    public static final DeferredHolder<ITechnique, ? extends GenericTechnique> ADVANCED_CULTIVATION_TECHNIQUE = TECHNIQUES.register("advanced_cultivation_technique",()->
            new GenericTechnique(ModPaths.ESSENCE.getId(),Component.literal("Advanced Cultivation Technique"),10.0,Set.of())
                    .setStatChangeHandler(testHandler));
    public static final DeferredHolder<ITechnique, ? extends GenericTechnique> SWORD_COMPREHENSION_TECHNIQUE = TECHNIQUES.register("sword_comprehension_technique",()->
            new GenericTechnique(ModPaths.SWORD.getId(),Component.literal("Sword Comprehension Technique"),10.0,Set.of())
                    .setStatChangeHandler(testHandler));



    // ====================== Runic Techniques ====================== //
    public static final DeferredHolder<ITechnique, ? extends RunicTechnique> RUNE_MONARCH_TECHNIQUE =
            TECHNIQUES.register("rune_monarch_technique", () ->
                    new RunicTechnique(
                            ModPaths.RUNIC.getId(),
                            Component.translatable("ascension.technique.rune_monarch"),
                            10.0,
                            Set.of(ModPaths.ESSENCE.getId())
                    )
                            .setMaxMajorRealm(3)
                            .setMaxRunesAllRealms(4)
                            .setShortDescription(Component.translatable("ascension.technique.rune_monarch.short_description"))
                            .setDescription(Component.translatable("ascension.technique.rune_monarch.description"))
                            .setStatChangeHandler(testHandler2)
            );

    public static final DeferredHolder<ITechnique, ? extends RunicTechnique> RUNE_SERVANT_TECHNIQUE =
            TECHNIQUES.register("rune_servant_technique", () ->
                    new RunicTechnique(
                            ModPaths.RUNIC.getId(),
                            Component.translatable("ascension.technique.rune_servant"),
                            2.0,
                            Set.of()
                    )
                            .setMaxMajorRealm(1)
                            .setMaxRunesForRealm(0, 1)
                            .setMaxRunesForRealm(1, 1)
                            .setShortDescription(Component.translatable("ascension.technique.rune_servant.short_description"))
                            .setDescription(Component.translatable("ascension.technique.rune_servant.description"))
                            .setStatChangeHandler(testHandler2)
            );

    public static final DeferredHolder<ITechnique, ? extends RunicTechnique> LESSER_FATHOMLESS_TECHNIQUE =
            TECHNIQUES.register("lesser_fathomless_technique", () ->
                    new RunicTechnique(
                            ModPaths.RUNIC.getId(),
                            Component.translatable("ascension.technique.lesser_fathomless"),
                            5.0,
                            Set.of() // Water Path and Body Path if this actually works
                    )
                            .setMaxMajorRealm(2)
                            .setMaxRunesAllRealms(2)
                            .setSpecialized(true)
                            .setForcedRunesForRealm(0, Runes.VITALITY.getId(), Runes.ARMOR.getId())
                            .setForcedRunesForRealm(1, Runes.REGEN.getId(), Runes.ESSENCE.getId())
                            .setForcedRunesForRealm(2, Runes.SPEED.getId(), Runes.PRECISION.getId())
                            .setCultivationCondition(CultivationConditions.oceanOnly(1.5))
                            .setShortDescription(Component.translatable("ascension.technique.lesser_fathomless.short_description"))
                            .setDescription(Component.translatable("ascension.technique.lesser_fathomless.description"))
                            .setStatChangeHandler(testHandler2)
            );

    public static final DeferredHolder<ITechnique, ? extends RunicTechnique> LESSER_DIVINE_TECHNIQUE =
            TECHNIQUES.register("lesser_divine_technique", () ->
                    new RunicTechnique(
                            ModPaths.RUNIC.getId(),
                            Component.translatable("ascension.technique.lesser_divine"),
                            5.0,
                            Set.of() // Wind Path and Essence Path if this actually works
                    )
                            .setMaxMajorRealm(2)
                            .setMaxRunesAllRealms(2)
                            .setSpecialized(true)
                            .setForcedRunesForRealm(0, Runes.STRENGTH.getId(), Runes.ARMOR.getId())
                            .setForcedRunesForRealm(1, Runes.REGEN.getId(), Runes.ESSENCE.getId())
                            .setForcedRunesForRealm(2, Runes.SPEED.getId(), Runes.PRECISION.getId())
                            .setCultivationCondition(CultivationConditions.minHeight(210, 1.5))
                            .setShortDescription(Component.translatable("ascension.technique.lesser_divine.short_description"))
                            .setDescription(Component.translatable("ascension.technique.lesser_divine.description"))
                            .setStatChangeHandler(testHandler2)
            );



    public static void register(IEventBus modEventBus){
        TECHNIQUES.register(modEventBus);
    }
}
