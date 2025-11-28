package net.thejadeproject.ascension.progression.techniques;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.PlayerAttributeManager;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.cultivation.player.realm_change_handlers.StandardStatRealmChange;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.items.technique_manuals.GenericTechniqueManual;
import net.thejadeproject.ascension.progression.skills.skill_lists.AcquirableSkillData;
import net.thejadeproject.ascension.progression.skills.skill_lists.SkillList;
import net.thejadeproject.ascension.progression.techniques.stability_handlers.GenericCalculatedStabilityHandler;
import net.thejadeproject.ascension.progression.techniques.stability_handlers.LnStabilityHandler;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.techniques.path_techniques.body.SingleAttributeTechnique;
import net.thejadeproject.ascension.progression.techniques.path_techniques.essence.SingleElementTechnique;
import net.thejadeproject.ascension.progression.techniques.path_techniques.intent.SingleIntentTechnique;
import net.thejadeproject.ascension.util.ModAttributes;
import org.checkerframework.checker.units.qual.A;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class ModTechniques {

    public static class TechniqueHolder{

        public DeferredHolder<ITechnique,? extends ITechnique> technique;
        public DeferredItem<GenericTechniqueManual> manual;

        public TechniqueHolder(DeferredHolder<ITechnique,? extends ITechnique> technique,DeferredItem<GenericTechniqueManual> manual){
            this.technique = technique;
            this.manual = manual;
        }

    }

    public static HashMap<Holder<Attribute>,Double> modifyGenericStatMap(HashMap<Holder<Attribute>,Double> map,Holder<Attribute> key,Double stat){
        HashMap<Holder<Attribute>,Double> newMap = new HashMap<>(map);
        newMap.put(key,stat);
        return newMap;
    }

    public static HashMap<Holder<Attribute>,Double> GENERIC_MINOR_REALM_STATS_1 = new HashMap<>(){{
        put(Attributes.MAX_HEALTH,5.0);
        put(Attributes.ATTACK_DAMAGE,1.0);
        put(Attributes.MOVEMENT_SPEED,0.01);
        put(Attributes.JUMP_STRENGTH,0.01);
        put(Attributes.STEP_HEIGHT,0.01);
        put(Attributes.SAFE_FALL_DISTANCE,0.6);

    }};
    public static HashMap<Holder<Attribute>,Double> ENHANCED_MINOR_REALM_STATS_1 = new HashMap<>(){{
        put(Attributes.MAX_HEALTH,10.0);
        put(Attributes.ATTACK_DAMAGE,1.8);
        put(Attributes.MOVEMENT_SPEED,0.2);
        put(Attributes.JUMP_STRENGTH,0.04);
        put(Attributes.STEP_HEIGHT,0.04);
        put(Attributes.SAFE_FALL_DISTANCE,1.2);

    }};

    public static HashMap<Holder<Attribute>,Double> GENERIC_MAJOR_REALM_STATS_1 = new HashMap<>(){{
        put(Attributes.MAX_HEALTH,10.0);
        put(Attributes.ATTACK_DAMAGE,2.0);
        put(Attributes.MOVEMENT_SPEED,0.5);
        put(Attributes.JUMP_STRENGTH,0.02);
        put(Attributes.STEP_HEIGHT,0.02);
        put(Attributes.SAFE_FALL_DISTANCE,0.6);
        put(ModAttributes.PLAYER_QI_REGEN_RATE,0.2);
        put(ModAttributes.PLAYER_QI_INSTANCE, 100.0);
    }};
        public static HashMap<Holder<Attribute>,Double> ENHANCED_MAJOR_REALM_STATS_1 = new HashMap<>(){{
        put(Attributes.MAX_HEALTH,10.0);
        put(Attributes.ATTACK_DAMAGE,1.8);
        put(Attributes.MOVEMENT_SPEED,0.2);
        put(Attributes.JUMP_STRENGTH,0.04);
        put(Attributes.STEP_HEIGHT,0.04);
        put(Attributes.SAFE_FALL_DISTANCE,1.2);
        put(ModAttributes.PLAYER_QI_REGEN_RATE,0.4);
        put(ModAttributes.PLAYER_QI_INSTANCE, 100.0);
    }};
    public static final DeferredRegister<ITechnique> TECHNIQUES =DeferredRegister.create(AscensionRegistries.Techniques.TECHNIQUES_REGISTRY, AscensionCraft.MOD_ID);


    //Essence Techniques
    public static final TechniqueHolder PURE_FIRE_TECHNIQUE = createTechnique("pure_fire_technique",
            ()->new SingleElementTechnique(
                        "Pure Fire Technique","ascension:fire",
                        2.0,new LnStabilityHandler()
                        ,new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:fire",2.0);
                    }})
                    .setSkillList(List.of(
                        new AcquirableSkillData("ascension:essence",0,0,"ascension:basic_fire_ball",false),
                        new AcquirableSkillData("ascension:essence",0,0,"ascension:large_fire_ball",false),
                        new AcquirableSkillData("ascension:essence",0,0,"ascension:delayed_fire_launch",false)
                    ))
                    .setDescription(List.of(
                            Component.literal("breathe in pure ").append("§4fire ").append("and purify ones essence")
                    )));

    public static final TechniqueHolder PURE_WATER_TECHNIQUE = createTechnique("pure_water_technique",
            ()->new SingleElementTechnique(
                    "Pure Water Technique","ascension:water",
                    2.0,new LnStabilityHandler(),
                    new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1, GENERIC_MAJOR_REALM_STATS_1))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:water",2.0);
                    }}));
    public static final TechniqueHolder PURE_WOOD_TECHNIQUE = createTechnique("pure_wood_technique",
            ()->new SingleElementTechnique(
                    "Pure Wood Technique","ascension:wood",
                    2.0,new LnStabilityHandler(),
                    new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:wood",2.0);
                    }}));
    public static final TechniqueHolder PURE_EARTH_TECHNIQUE = createTechnique("pure_earth_technique",
            ()->new SingleElementTechnique(
                    "Pure Earth Technique","ascension:earth",
                    2.0,new LnStabilityHandler(),
                    new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:earth",2.0);
                    }}));
    public static final TechniqueHolder PURE_METAL_TECHNIQUE = createTechnique("pure_metal_technique",
            ()->new SingleElementTechnique(
                    "Pure Metal Technique","ascension:metal",
                    2.0,new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            modifyGenericStatMap(
                                    GENERIC_MINOR_REALM_STATS_1,
                                    Attributes.WATER_MOVEMENT_EFFICIENCY,0.01),
                            modifyGenericStatMap(
                                    GENERIC_MAJOR_REALM_STATS_1,
                                    Attributes.WATER_MOVEMENT_EFFICIENCY,0.02)))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:metal",2.0);
                    }}));
    public static final TechniqueHolder VOID_SWALLOWING_TECHNIQUE = createTechnique("void_swallowing_technique",
            ()->new SingleElementTechnique(
                    "Void Swallowing Technique","ascension:void",
                    8.0,new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        new HashMap<>(){{
                            put(Attributes.MAX_HEALTH,23.0);
                            put(Attributes.ATTACK_DAMAGE,5.4);
                            put(Attributes.MOVEMENT_SPEED,1.0);
                            put(Attributes.JUMP_STRENGTH,0.06);
                            put(Attributes.STEP_HEIGHT,0.06);
                        }}, new HashMap<>(){{
                            put(Attributes.MAX_HEALTH,5.0);
                            put(Attributes.ATTACK_DAMAGE,2.4);
                            put(Attributes.MOVEMENT_SPEED,0.1);
                            put(Attributes.JUMP_STRENGTH,0.02);
                            put(Attributes.STEP_HEIGHT,0.06);
                        }}))
                    //new StandardStatRealmChange(ENHANCED_MINOR_REALM_STATS_1,ENHANCED_MAJOR_REALM_STATS_1))
                    .setEfficiencyAttributes(new HashMap<>(){{

                        put("ascension:void",2.0);
                    }}));



    //Intent Physique
    public static final TechniqueHolder PURE_SWORD_INTENT = createTechnique("pure_sword_intent",
            ()->new SingleIntentTechnique(
                    "Pure Sword Technique",8.0,
                    "ascension:sword_intent",new LnStabilityHandler(),
                    new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:sword_intent",2.0);
                    }})
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:intent",0,3,"ascension:sword_intent_skill",true)
                    )));
    public static final TechniqueHolder PURE_AXE_INTENT = createTechnique("pure_axe_intent",
            ()->new SingleIntentTechnique(
                    "Pure Axe Technique",8.0,
                    "ascension:axe_intent",new LnStabilityHandler(),
                    new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:axe_intent",2.0);
                    }})
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:intent",0,3,"ascension:axe_intent_skill",true)
                    )));
    public static final TechniqueHolder PURE_BLADE_INTENT = createTechnique("pure_blade_intent",
            ()->new SingleIntentTechnique(
                        "Pure Blade Technique",8.0,"ascension:blade_intent",
                        new LnStabilityHandler(),
                        new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:blade_intent",2.0);
                    }}).setSkillList(List.of(
                            new AcquirableSkillData("ascension:intent",0,3,"ascension:blade_intent_skill",true)
                    )));
    public static final TechniqueHolder PURE_SPEAR_INTENT = createTechnique("pure_spear_intent",
            ()->new SingleIntentTechnique(
                    "Pure Blade Technique",8.0,"ascension:spear_intent",
                    new LnStabilityHandler(),
                    new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:spear_intent",2.0);
                    }})
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:intent",0,3,"ascension:spear_intent_skill",true)
                    )));
    public static final TechniqueHolder PURE_FIST_INTENT = createTechnique("pure_fist_intent",
            ()->new SingleIntentTechnique(
                    "Pure Fist Technique",8.0,
                    "ascension:fist_intent",new LnStabilityHandler(),
                    new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:fist_intent",2.0);
                    }}).setSkillList(List.of(
                    new AcquirableSkillData("ascension:intent",0,0,"ascension:fist_aura_skill",true)
            )).setDescription(List.of(
                            Component.literal("what heavens? under my").append(" §8Fist ").append("all is equal"),
                            Component.literal("and all is weak")
                    )));

    public static final TechniqueHolder FIST_KINGS_TECHNIQUE = createTechnique("fist_king_intent",
            ()->new SingleIntentTechnique(
                    "Fist Kings Technique",8.0,"ascension:fist_intent",
                    new LnStabilityHandler(),
                    new StandardStatRealmChange(modifyGenericStatMap(
                            modifyGenericStatMap(GENERIC_MAJOR_REALM_STATS_1, Attributes.MOVEMENT_SPEED,0.2),
                            Attributes.MAX_HEALTH,11.0

                    ), new HashMap<>(){{
                        put(Attributes.MAX_HEALTH,23.0);
                        put(Attributes.ATTACK_DAMAGE,5.4);
                        put(Attributes.MOVEMENT_SPEED,1.0);
                        put(Attributes.JUMP_STRENGTH,0.06);
                        put(Attributes.STEP_HEIGHT,0.06);
                    }}))
                    .setEfficiencyAttributes(new HashMap<>(){{

                        put("ascension:fist_intent",4.8);
                    }}));


    //Body Techniques
    public static final TechniqueHolder WOOD_ELEMENTAL_TECHNIQUE = createTechnique("wood_elemental_technique",
            ()->new SingleAttributeTechnique("Wood Elemental Technique",8.0,"ascension:wood",new LnStabilityHandler(),
                    new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:wood",2.0);
                    }}));
    public static final TechniqueHolder FIRE_ELEMENTAL_TECHNIQUE = createTechnique("fire_elemental_technique",
            ()->new SingleAttributeTechnique(
                    "Fire Elemental Technique",8.0,"ascension:fire",
                    new LnStabilityHandler(),
                    new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:fire",2.0);
                    }}));
    public static final TechniqueHolder EARTH_ELEMENTAL_TECHNIQUE = createTechnique("earth_elemental_technique",
            ()->new SingleAttributeTechnique("Earth Elemental Technique",8.0,"ascension:earth",new LnStabilityHandler(),
                    new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:earth",2.0);
                    }}));
    public static final TechniqueHolder METAL_ELEMENTAL_TECHNIQUE = createTechnique("metal_elemental_technique",
            ()->new SingleAttributeTechnique("Metal Elemental Technique",8.0,"ascension:metal",new LnStabilityHandler(),
                    new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:metal",2.0);
                    }}));
    public static final TechniqueHolder WATER_ELEMENTAL_TECHNIQUE = createTechnique("water_elemental_technique",
            ()->new SingleAttributeTechnique("Water Elemental Technique",8.0,"ascension:water",new LnStabilityHandler(),
                    new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:water",2.0);
                    }}));

    public static final TechniqueHolder DIVINE_PHOENIX_TECHNIQUE = createTechnique("divine_phoenix_technique",
            ()->new SingleAttributeTechnique("Divine Phoenix Technique",8.0,"ascension:phoenix_fire",new LnStabilityHandler(),
                    new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
                    .setEfficiencyAttributes(new HashMap<>(){{

                        put("ascension:phoenix_fire",2.0);
                    }}));

    public static TechniqueHolder createTechnique(String id, Supplier<? extends ITechnique> supplier){
        DeferredHolder<ITechnique,? extends ITechnique> techniqueHolder = TECHNIQUES.register(id,supplier);
        DeferredItem<GenericTechniqueManual> manualHolder = ModItems.ITEMS.register(id,()-> new GenericTechniqueManual(
                new Item.Properties(),
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, id)
                ));
        return new TechniqueHolder(techniqueHolder,manualHolder);
    }
    public static TechniqueHolder createTechnique(String id, Supplier<? extends ITechnique> supplier, Supplier<? extends GenericTechniqueManual> itemSupplier){
        DeferredHolder<ITechnique,? extends ITechnique> techniqueHolder = TECHNIQUES.register(id,supplier);
        DeferredItem<GenericTechniqueManual> manualHolder = ModItems.ITEMS.register(id,itemSupplier);
        return new TechniqueHolder(techniqueHolder,manualHolder);
    }

    public static void register(IEventBus eventBus){
        TECHNIQUES.register(eventBus);
    }
}
