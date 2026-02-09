package net.thejadeproject.ascension.formations;

import net.lucent.formation_arrays.api.formations.IFormation;
import net.lucent.formation_arrays.api.registries.FormationRegistry;
import net.lucent.formation_arrays.formations.GenericFormation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.formations.formation_nodes.*;
import net.thejadeproject.ascension.formations.formation_renderers.ModFormationRenderers;

public class ModFormations {
    public static final DeferredRegister<IFormation> FORMATIONS = DeferredRegister.create(FormationRegistry.FORMATION_REGISTRY, AscensionCraft.MOD_ID);

    public static final DeferredHolder<IFormation,  ? extends GenericFormation> BARRIER_FORMATION = FORMATIONS.register("barrier_formation",
            ()->new GenericFormation(Component.literal("Barrier Formation"),ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"barrier_renderer"),
                    (formation, blockPos, uuid)->
                    new BarrierFormation(formation,uuid,
                        15,
                        1000,
                        1,
                        2,
                        10
                        )

            )

    );

    public static final DeferredHolder<IFormation,? extends GenericFormation> WATER_BUBBLE_FORMATION = FORMATIONS.register("water_bubble_formation",
            ()->new GenericFormation(Component.literal("Water Barrier Formation"),null,
                    ((formation, pos, formationId) ->
                            new WaterBubbleFormation(formation,formationId,1)
                            )));


    public static final DeferredHolder<IFormation,? extends GenericFormation> QI_GATHERING_FORMATION = FORMATIONS.register("qi_gathering_formation",
            ()->new GenericFormation(Component.literal("Qi Gathering Formation"), ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"cultivation_bonus_renderer"),
                    ((formation, pos, formationId) ->
                            new CultivationBonusFormation(formation, formationId, ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"essence"),0.2,10,2)
                    )));

    public static final DeferredHolder<IFormation,? extends GenericFormation> ENLIGHTENMENT_FORMATION = FORMATIONS.register("enlightenment_formation",
            ()->new GenericFormation(Component.literal("Enlightenment Formation"), ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"cultivation_bonus_renderer"),
                    ((formation, pos, formationId) ->
                            new CultivationBonusFormation(formation, formationId, ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"intent"),0.2,10,2)
                    )));

    public static final DeferredHolder<IFormation,? extends GenericFormation> BODY_REINFORCEMENT_FORMATION = FORMATIONS.register("body_reinforcement_formation",
            ()->new GenericFormation(Component.literal("Body Reinforcement Formation"), ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"cultivation_bonus_renderer"),
                    ((formation, pos, formationId) ->
                            new CultivationBonusFormation(formation, formationId, ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"body"),0.2,10,2)
                    )));
    public static final DeferredHolder<IFormation,? extends GenericFormation> AREA_PROTECTION_FORMATION = FORMATIONS.register("area_protection_formation",
            ()->new GenericFormation(Component.literal("Gaia's Grace Formation"),null,
                    ((formation, pos, formationId) ->
                            new AreaProtectionFormation(formation,formationId,1,10)
                    )));

    public static final DeferredHolder<IFormation,? extends GenericFormation> QI_ABSORPTION_FORMATION = FORMATIONS.register("qi_absorption_formation",
            ()->new GenericFormation(Component.literal("Qi Absorption Formation"),null,
                    ((formation, pos, formationId) ->
                            new QiAbsorptionFormation(formation,formationId,10,1)
                    )));
    public static final DeferredHolder<IFormation,? extends GenericFormation> GROWTH_FORMATION = FORMATIONS.register("growth_formation",
            ()->new GenericFormation(Component.literal("Natures Blessing Formation"),ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"growth_formation_renderer"),
                    ((formation, pos, formationId) ->
                            new GrowthFormation(formation,formationId,10,50,20,60,2)
                    )));
    public static final DeferredHolder<IFormation, ? extends GenericFormation> OBFUSCATION_FORMATION = FORMATIONS.register("mist_obfuscation_formation",
            () -> new GenericFormation(Component.literal("Mist Obfuscation Formation"), ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "mist_renderer"),
                    (formation, pos, uuid) -> new MistObfuscationFormation(formation, uuid, 15.0, 8)));

    public static void register(IEventBus eventBus){
        FORMATIONS.register(eventBus);
    }
}
