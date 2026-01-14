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

    public static double MaxSpeed = 0.4;
    public static double MaxJumpStrength = 2.0;

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

    // ========== HUMAN TIER TECHNIQUES ==========

    // Body Path - Human Tier
    public static final TechniqueHolder IRON_SKIN_TECHNIQUE = createTechnique("iron_skin_technique",
            ()->new SingleAttributeTechnique("Iron Skin Technique", 4.0, "ascension:metal", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{
                                put(Attributes.MAX_HEALTH, 15.0);
                                put(Attributes.ATTACK_DAMAGE, 1.5);
                                put(Attributes.ARMOR, 1.2);
                                put(Attributes.MOVEMENT_SPEED, 0.05);
                                put(Attributes.JUMP_STRENGTH, 0.01);
                                put(Attributes.STEP_HEIGHT, 0.02);
                                put(Attributes.SAFE_FALL_DISTANCE, 0.5);
                                put(Attributes.KNOCKBACK_RESISTANCE, 0.1);
                            }}, new HashMap<>(){{
                        put(Attributes.MAX_HEALTH, 25.0);
                        put(Attributes.ATTACK_DAMAGE, 3.0);
                        put(Attributes.ARMOR, 2.5);
                        put(Attributes.MOVEMENT_SPEED, 0.4);
                        put(Attributes.JUMP_STRENGTH, 0.08);
                        put(Attributes.STEP_HEIGHT, 0.5);
                        put(Attributes.SAFE_FALL_DISTANCE, 1.0);
                        put(Attributes.KNOCKBACK_RESISTANCE, 0.3);
                    }}))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:metal", 1.8);
                        put("ascension:earth", 0.5);
                    }})
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Body Path"),
                            Component.literal("§8Strengthens skin to iron-like durability"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Withstand repeated blows"),
                            Component.literal("§e◆ §fPath: Defense → Resilience → Unbreakable"),
                            Component.literal("§e◆ §fWeakness: Slow movement, vulnerable to lightning")
                    )));

    // Essence Path - Human Tier
    public static final TechniqueHolder SWIFT_BREEZE_TECHNIQUE = createTechnique("swift_breeze_technique",
            ()->new SingleElementTechnique("Swift Breeze Technique", "ascension:wind", 3.5, new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{
                                put(Attributes.MAX_HEALTH, 8.0);
                                put(Attributes.ATTACK_DAMAGE, 1.2);
                                put(Attributes.MOVEMENT_SPEED, 0.15);
                                put(Attributes.ARMOR, 0.1);
                                put(Attributes.JUMP_STRENGTH, 0.08);
                                put(Attributes.STEP_HEIGHT, 0.05);
                                put(Attributes.SAFE_FALL_DISTANCE, 0.8);
                            }}, new HashMap<>(){{
                        put(Attributes.MAX_HEALTH, 16.0);
                        put(Attributes.ATTACK_DAMAGE, 2.5);
                        put(Attributes.ARMOR, 0.5);
                        put(Attributes.MOVEMENT_SPEED, 0.6);
                        put(Attributes.JUMP_STRENGTH, 0.25);
                        put(Attributes.STEP_HEIGHT, 0.4);
                        put(Attributes.SAFE_FALL_DISTANCE, 1.5);
                    }}))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:wind", 2.0);
                        put("ascension:yang", 0.8);
                    }})
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Essence Path"),
                            Component.literal("§7Harnesses wind essence for speed and agility"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Move with the wind's flow"),
                            Component.literal("§e◆ §fPath: Speed → Precision → Wind Mastery"),
                            Component.literal("§e◆ §fWeakness: Fragile defense, weak to earth")
                    )));

    // Intent Path - Human Tier
    public static final TechniqueHolder FOCUSED_STRIKE_TECHNIQUE = createTechnique("focused_strike_technique",
            ()->new SingleIntentTechnique("Focused Strike Technique", 5.0, "ascension:precision", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{
                                put(Attributes.MAX_HEALTH, 9.0);
                                put(Attributes.ATTACK_DAMAGE, 2.0);
                                put(Attributes.ATTACK_SPEED, 0.1);
                                put(Attributes.MOVEMENT_SPEED, 0.05);
                                put(Attributes.JUMP_STRENGTH, 0.02);
                                put(Attributes.SAFE_FALL_DISTANCE, 0.4);
                            }}, new HashMap<>(){{
                        put(Attributes.MAX_HEALTH, 18.0);
                        put(Attributes.ATTACK_DAMAGE, 4.5);
                        put(Attributes.ATTACK_SPEED, 0.3);
                        put(Attributes.MOVEMENT_SPEED, 0.3);
                        put(Attributes.JUMP_STRENGTH, 0.1);
                        put(Attributes.SAFE_FALL_DISTANCE, 0.8);
                    }}))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:sharpness", 1.5);
                        put("ascension:cutting", 1.2);
                    }})
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Intent Path"),
                            Component.literal("§7Focuses intent into precise, powerful strikes"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Train concentration and precision"),
                            Component.literal("§e◆ §fPath: Focus → Precision → Perfect Strike"),
                            Component.literal("§e◆ §fWeakness: Easily disrupted by chaos")
                    )));

    // ========== EXISTING HUMAN TIER TECHNIQUES ==========

    // Essence Path - Human Tier
    public static final TechniqueHolder PURE_FIRE_TECHNIQUE = createTechnique("pure_fire_technique",
            ()->new SingleElementTechnique(
                    "Pure Fire Technique","ascension:fire",
                    2.0,new LnStabilityHandler()
                    ,new StandardStatRealmChange(
                    new HashMap<>(){{ //Minor Realm Stats
                        put(Attributes.MAX_HEALTH,12.5);
                        put(Attributes.ATTACK_DAMAGE,3.4);
                        put(Attributes.MOVEMENT_SPEED,0.08);
                        put(Attributes.ARMOR,0.5);
                        put(Attributes.JUMP_STRENGTH,0.04);
                        put(Attributes.STEP_HEIGHT,0.08);
                        put(Attributes.SAFE_FALL_DISTANCE,0.6);
                    }}, new HashMap<>(){{ //Major Realm Stats
                put(Attributes.MAX_HEALTH,21.5);
                put(Attributes.ATTACK_DAMAGE,7.5);
                put(Attributes.ARMOR,1.5);
                put(Attributes.MOVEMENT_SPEED,0.8);
                put(Attributes.JUMP_STRENGTH,0.9);
                put(Attributes.STEP_HEIGHT,1.2);
                put(Attributes.SAFE_FALL_DISTANCE,1.2);
            }}))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",1,5,"ascension:flight_passive_skill",false)))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:fire",2.0);
                    }})
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",4,0,"ascension:basic_fire_ball",false),
                            new AcquirableSkillData("ascension:essence",5,0,"ascension:large_fire_ball",false),
                            new AcquirableSkillData("ascension:essence",3,0,"ascension:delayed_fire_launch",false)
                    ))
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Essence Path"),
                            Component.literal("§cBreathe in pure fire and purify ones essence"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Cultivate in fiery environments"),
                            Component.literal("§e◆ §fPath: Heat → Destruction → Fire Mastery"),
                            Component.literal("§e◆ §fWeakness: Weak to water-based techniques")
                    )));

    public static final TechniqueHolder PURE_WATER_TECHNIQUE = createTechnique("pure_water_technique",
            ()->new SingleElementTechnique(
                    "Pure Water Technique","ascension:water",
                    2.0,new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{ //Minor Realm Stats
                                put(Attributes.MAX_HEALTH,9.5);
                                put(Attributes.ATTACK_DAMAGE,1.4);
                                put(Attributes.MOVEMENT_SPEED,0.2);
                                put(Attributes.ARMOR,0.3);
                                put(Attributes.JUMP_STRENGTH,0.02);
                                put(Attributes.STEP_HEIGHT,0.02);
                                put(Attributes.OXYGEN_BONUS,0.1);
                                put(Attributes.WATER_MOVEMENT_EFFICIENCY,0.08);
                                put(Attributes.SAFE_FALL_DISTANCE,0.6);
                            }}, new HashMap<>(){{ //Major Realm Stats
                        put(Attributes.MAX_HEALTH,18.5);
                        put(Attributes.ATTACK_DAMAGE,3.5);
                        put(Attributes.ARMOR,1.1);
                        put(Attributes.MOVEMENT_SPEED,0.6);
                        put(Attributes.JUMP_STRENGTH,0.8);
                        put(Attributes.STEP_HEIGHT,0.6);
                        put(Attributes.OXYGEN_BONUS,0.4);
                        put(Attributes.WATER_MOVEMENT_EFFICIENCY,0.14);
                        put(Attributes.SAFE_FALL_DISTANCE,1.2);
                    }}))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",1,5,"ascension:flight_passive_skill",false)))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:water",2.0);
                    }})
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Essence Path"),
                            Component.literal("§9Harmonizes with water essence"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Cultivate near water sources"),
                            Component.literal("§e◆ §fPath: Flow → Adaptation → Water Mastery"),
                            Component.literal("§e◆ §fWeakness: Weak to earth-based techniques")
                    )));

    public static final TechniqueHolder PURE_WOOD_TECHNIQUE = createTechnique("pure_wood_technique",
            ()->new SingleElementTechnique(
                    "Pure Wood Technique","ascension:wood",
                    2.0,new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{ //Minor Realm Stats
                                put(Attributes.MAX_HEALTH,7.5);
                                put(Attributes.ATTACK_DAMAGE,1.2);
                                put(Attributes.MOVEMENT_SPEED,0.9);
                                put(Attributes.ARMOR,0.2);
                                put(Attributes.JUMP_STRENGTH,0.09);
                                put(Attributes.STEP_HEIGHT,0.1);
                                put(Attributes.SAFE_FALL_DISTANCE,0.6);
                            }}, new HashMap<>(){{ //Major Realm Stats
                        put(Attributes.MAX_HEALTH,15.5);
                        put(Attributes.ATTACK_DAMAGE,6.5);
                        put(Attributes.ARMOR,0.4);
                        put(Attributes.MOVEMENT_SPEED,1.8);
                        put(Attributes.JUMP_STRENGTH,0.14);
                        put(Attributes.STEP_HEIGHT,1.0);
                        put(Attributes.SAFE_FALL_DISTANCE,1.2);
                    }}))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",1,5,"ascension:flight_passive_skill",false),
                            new AcquirableSkillData("ascension:essence", 1, 2, "ascension:rootwardens_call", false),
                            new AcquirableSkillData("ascension:essence", 4, 3, "ascension:spiritual_sense", false)))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:wood",2.0);
                    }})
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Essence Path"),
                            Component.literal("§aHarmonizes with wood essence"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Cultivate in forests"),
                            Component.literal("§e◆ §fPath: Growth → Healing → Wood Mastery"),
                            Component.literal("§e◆ §fWeakness: Weak to metal-based techniques")
                    )));

    public static final TechniqueHolder PURE_EARTH_TECHNIQUE = createTechnique("pure_earth_technique",
            ()->new SingleElementTechnique(
                    "Pure Earth Technique","ascension:earth",
                    2.0,new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{ //Minor Realm Stats
                                put(Attributes.MAX_HEALTH,17.5);
                                put(Attributes.ATTACK_DAMAGE,2.2);
                                put(Attributes.MOVEMENT_SPEED,0.1);
                                put(Attributes.ARMOR,0.3);
                                put(Attributes.JUMP_STRENGTH,0.01);
                                put(Attributes.STEP_HEIGHT,0.01);
                                put(Attributes.SAFE_FALL_DISTANCE,0.6);
                            }}, new HashMap<>(){{ //Major Realm Stats
                        put(Attributes.MAX_HEALTH,33.5);
                        put(Attributes.ATTACK_DAMAGE,9.5);
                        put(Attributes.ARMOR,0.6);
                        put(Attributes.MOVEMENT_SPEED,1.3);
                        put(Attributes.JUMP_STRENGTH,0.03);
                        put(Attributes.STEP_HEIGHT,0.5);
                        put(Attributes.SAFE_FALL_DISTANCE,1.2);
                    }}))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",1,5,"ascension:flight_passive_skill",false)))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:earth",2.0);
                    }})
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Essence Path"),
                            Component.literal("§eHarmonizes with earth essence"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Cultivate on solid ground"),
                            Component.literal("§e◆ §fPath: Stability → Defense → Earth Mastery"),
                            Component.literal("§e◆ §fWeakness: Weak to wood-based techniques")
                    )));

    public static final TechniqueHolder PURE_METAL_TECHNIQUE = createTechnique("pure_metal_technique",
            ()->new SingleElementTechnique(
                    "Pure Metal Technique","ascension:metal",
                    2.0,new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{ //Minor Realm Stats
                                put(Attributes.MAX_HEALTH,17.5);
                                put(Attributes.ATTACK_DAMAGE,2.2);
                                put(Attributes.ARMOR,0.6);
                                put(Attributes.MOVEMENT_SPEED,0.1);
                                put(Attributes.JUMP_STRENGTH,0.01);
                                put(Attributes.STEP_HEIGHT,0.01);
                                put(Attributes.SAFE_FALL_DISTANCE,0.6);
                            }}, new HashMap<>(){{ //Major Realm Stats
                        put(Attributes.MAX_HEALTH,33.5);
                        put(Attributes.ATTACK_DAMAGE,9.5);
                        put(Attributes.ARMOR,1.5);
                        put(Attributes.MOVEMENT_SPEED,1.3);
                        put(Attributes.JUMP_STRENGTH,0.03);
                        put(Attributes.STEP_HEIGHT,0.5);
                        put(Attributes.SAFE_FALL_DISTANCE,1.2);
                    }}))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",1,5,"ascension:flight_passive_skill",false),
                            new AcquirableSkillData("ascension:essence",2,3,"ascension:ore_sight_active",false)))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:metal",2.0);
                    }})
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Essence Path"),
                            Component.literal("§7Harmonizes with metal essence"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Cultivate near metal deposits"),
                            Component.literal("§e◆ §fPath: Sharpness → Precision → Metal Mastery"),
                            Component.literal("§e◆ §fWeakness: Weak to fire-based techniques")
                    )));

    // Intent Path - Human Tier
    public static final TechniqueHolder PURE_SWORD_INTENT = createTechnique("pure_sword_intent",
            ()->new SingleIntentTechnique(
                    "Pure Sword Technique",8.0,
                    "ascension:sword_intent",new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{ //Minor Realm Stats
                                put(Attributes.MAX_HEALTH,7.5);
                                put(Attributes.ATTACK_DAMAGE,2.3);
                                put(Attributes.ARMOR,0.1);
                                put(Attributes.MOVEMENT_SPEED,0.3);
                                put(Attributes.JUMP_STRENGTH,0.03);
                                put(Attributes.STEP_HEIGHT,0.03);
                                put(Attributes.SAFE_FALL_DISTANCE,0.6);
                            }}, new HashMap<>(){{ //Major Realm Stats
                        put(Attributes.MAX_HEALTH,26.4);
                        put(Attributes.ATTACK_DAMAGE,7.4);
                        put(Attributes.ARMOR,0.4);
                        put(Attributes.MOVEMENT_SPEED,0.9);
                        put(Attributes.JUMP_STRENGTH,0.06);
                        put(Attributes.STEP_HEIGHT,0.6);
                        put(Attributes.SAFE_FALL_DISTANCE,1.2);
                    }}))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",6,5,"ascension:flight_passive_skill",false),
                            new AcquirableSkillData("ascension:intent",0,3,"ascension:sword_intent_skill",true)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:sword_intent",2.0);
                    }})
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Intent Path"),
                            Component.literal("§aBody resonates perfectly with sword intent"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Master sword techniques faster"),
                            Component.literal("§e◆ §fPath: Sword Sense → Sword Intent → Sword Mastery"),
                            Component.literal("§e◆ §fWeakness: Weak against blunt force attacks")
                    )));

    public static final TechniqueHolder PURE_AXE_INTENT = createTechnique("pure_axe_intent",
            ()->new SingleIntentTechnique(
                    "Pure Axe Technique",8.0,
                    "ascension:axe_intent",new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{ //Minor Realm Stats
                                put(Attributes.MAX_HEALTH,6.5);
                                put(Attributes.ATTACK_DAMAGE,3.3);
                                put(Attributes.ARMOR,0.2);
                                put(Attributes.MOVEMENT_SPEED,0.2);
                                put(Attributes.JUMP_STRENGTH,0.04);
                                put(Attributes.STEP_HEIGHT,0.04);
                                put(Attributes.SAFE_FALL_DISTANCE,0.6);
                            }}, new HashMap<>(){{ //Major Realm Stats
                        put(Attributes.MAX_HEALTH,17.4);
                        put(Attributes.ATTACK_DAMAGE,8.4);
                        put(Attributes.ARMOR,0.6);
                        put(Attributes.MOVEMENT_SPEED,0.8);
                        put(Attributes.JUMP_STRENGTH,0.09);
                        put(Attributes.STEP_HEIGHT,0.7);
                        put(Attributes.SAFE_FALL_DISTANCE,1.2);
                    }}))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",6,5,"ascension:flight_passive_skill",false)))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:axe_intent",2.0);
                    }})
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Intent Path"),
                            Component.literal("§aBody resonates with axe techniques"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Master axe techniques faster"),
                            Component.literal("§e◆ §fPath: Cleaving → Chopping → Axe Mastery"),
                            Component.literal("§e◆ §fWeakness: Slow attack speed")
                    )));

    public static final TechniqueHolder PURE_BLADE_INTENT = createTechnique("pure_blade_intent",
            ()->new SingleIntentTechnique(
                    "Pure Blade Technique",8.0,"ascension:blade_intent",
                    new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{ //Minor Realm Stats
                                put(Attributes.MAX_HEALTH,8.5);
                                put(Attributes.ATTACK_DAMAGE,2.3);
                                put(Attributes.ARMOR,0.5);
                                put(Attributes.MOVEMENT_SPEED,0.5);
                                put(Attributes.JUMP_STRENGTH,0.07);
                                put(Attributes.STEP_HEIGHT,0.07);
                                put(Attributes.SAFE_FALL_DISTANCE,0.6);
                            }}, new HashMap<>(){{ //Major Realm Stats
                        put(Attributes.MAX_HEALTH,26.4);
                        put(Attributes.ATTACK_DAMAGE,7.8);
                        put(Attributes.ARMOR,1.2);
                        put(Attributes.MOVEMENT_SPEED,1.1);
                        put(Attributes.JUMP_STRENGTH,0.14);
                        put(Attributes.STEP_HEIGHT,0.9);
                        put(Attributes.SAFE_FALL_DISTANCE,1.2);
                    }}))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",6,5,"ascension:flight_passive_skill",false)))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:blade_intent",2.0);
                    }})
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Intent Path"),
                            Component.literal("§aBody resonates with blade techniques"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Master blade techniques faster"),
                            Component.literal("§e◆ §fPath: Slashing → Cutting → Blade Mastery"),
                            Component.literal("§e◆ §fWeakness: Less effective against armor")
                    )));

    public static final TechniqueHolder PURE_SPEAR_INTENT = createTechnique("pure_spear_intent",
            ()->new SingleIntentTechnique(
                    "Pure Spear Technique",8.0,"ascension:spear_intent",
                    new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{ //Minor Realm Stats
                                put(Attributes.MAX_HEALTH,11.5);
                                put(Attributes.ATTACK_DAMAGE,2.0);
                                put(Attributes.ARMOR,0.3);
                                put(Attributes.MOVEMENT_SPEED,0.3);
                                put(Attributes.JUMP_STRENGTH,0.04);
                                put(Attributes.STEP_HEIGHT,0.11);
                                put(Attributes.ENTITY_INTERACTION_RANGE,0.2);
                                put(Attributes.SAFE_FALL_DISTANCE,0.6);
                            }}, new HashMap<>(){{ //Major Realm Stats
                        put(Attributes.MAX_HEALTH,29.4);
                        put(Attributes.ATTACK_DAMAGE,4.8);
                        put(Attributes.ARMOR,0.8);
                        put(Attributes.MOVEMENT_SPEED,0.7);
                        put(Attributes.JUMP_STRENGTH,0.08);
                        put(Attributes.STEP_HEIGHT,0.7);
                        put(Attributes.ENTITY_INTERACTION_RANGE,1.2);
                        put(Attributes.SAFE_FALL_DISTANCE,1.2);
                    }}))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",6,5,"ascension:flight_passive_skill",false)))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:spear_intent",2.0);
                    }})
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Intent Path"),
                            Component.literal("§aBody resonates with spear techniques"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Master spear techniques faster"),
                            Component.literal("§e◆ §fPath: Piercing → Thrusting → Spear Mastery"),
                            Component.literal("§e◆ §fWeakness: Vulnerable at close range")
                    )));

    public static final TechniqueHolder PURE_FIST_INTENT = createTechnique("pure_fist_intent",
            ()->new SingleIntentTechnique(
                    "Pure Fist Technique",8.0,
                    "ascension:fist_intent",new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{ //Minor Realm Stats
                                put(Attributes.MAX_HEALTH,12.5);
                                put(Attributes.ATTACK_DAMAGE,4.0);
                                put(Attributes.ARMOR,0.5);
                                put(Attributes.MOVEMENT_SPEED,0.5);
                                put(Attributes.JUMP_STRENGTH,0.02);
                                put(Attributes.STEP_HEIGHT,0.02);
                                put(Attributes.ATTACK_KNOCKBACK, 0.2);
                                put(Attributes.SAFE_FALL_DISTANCE,0.6);
                            }}, new HashMap<>(){{ //Major Realm Stats
                        put(Attributes.MAX_HEALTH,29.4);
                        put(Attributes.ATTACK_DAMAGE,4.8);
                        put(Attributes.ARMOR,0.8);
                        put(Attributes.MOVEMENT_SPEED,0.7);
                        put(Attributes.JUMP_STRENGTH,0.08);
                        put(Attributes.STEP_HEIGHT,0.7);
                        put(Attributes.ATTACK_KNOCKBACK,0.8);
                        put(Attributes.SAFE_FALL_DISTANCE,1.2);
                    }}))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",6,5,"ascension:flight_passive_skill",false),
                            new AcquirableSkillData("ascension:intent",0,0,"ascension:fist_aura_skill",true)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:fist_intent",2.0);
                    }})
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Intent Path"),
                            Component.literal("§aMy fist is my body and my body is my fist"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Strengthen fists through combat"),
                            Component.literal("§e◆ §fPath: Fist → Body Weapon → Unarmed Mastery"),
                            Component.literal("§e◆ §fWeakness: Limited range")
                    )));

    // Body Path - Human Tier
    public static final TechniqueHolder WOOD_ELEMENTAL_TECHNIQUE = createTechnique("wood_elemental_technique",
            ()->new SingleAttributeTechnique("Wood Elemental Technique",8.0,"ascension:wood",new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{ //Minor Realm Stats
                                put(Attributes.MAX_HEALTH,12.5);
                                put(Attributes.ATTACK_DAMAGE,4.4);
                                put(Attributes.MOVEMENT_SPEED,0.1);
                                put(Attributes.ARMOR,0.8);
                                put(Attributes.JUMP_STRENGTH,0.07);
                                put(Attributes.STEP_HEIGHT,0.08);
                                put(Attributes.SAFE_FALL_DISTANCE,0.6);
                            }}, new HashMap<>(){{ //Major Realm Stats
                        put(Attributes.MAX_HEALTH,21.5);
                        put(Attributes.ATTACK_DAMAGE,8.5);
                        put(Attributes.ARMOR,1.6);
                        put(Attributes.MOVEMENT_SPEED,2.2);
                        put(Attributes.JUMP_STRENGTH,1.2);
                        put(Attributes.STEP_HEIGHT,1.2);
                        put(Attributes.SAFE_FALL_DISTANCE,1.2);
                    }}))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",6,5,"ascension:flight_passive_skill",false),
                            new AcquirableSkillData("ascension:body", 1, 2, "ascension:rootwardens_call", false)))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:wood",2.0);
                    }})
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Body Path"),
                            Component.literal("§aBody harmonizes with wood essence"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Cultivate in forests"),
                            Component.literal("§e◆ §fPath: Growth → Healing → Wood Mastery"),
                            Component.literal("§e◆ §fWeakness: Vulnerable to fire and metal")
                    )));

    public static final TechniqueHolder FIRE_ELEMENTAL_TECHNIQUE = createTechnique("fire_elemental_technique",
            ()->new SingleAttributeTechnique(
                    "Fire Elemental Technique",8.0,"ascension:fire",
                    new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{ //Minor Realm Stats
                                put(Attributes.MAX_HEALTH,12.5);
                                put(Attributes.ATTACK_DAMAGE,3.4);
                                put(Attributes.MOVEMENT_SPEED,0.08);
                                put(Attributes.ARMOR,0.5);
                                put(Attributes.JUMP_STRENGTH,0.04);
                                put(Attributes.STEP_HEIGHT,0.08);
                                put(Attributes.SAFE_FALL_DISTANCE,0.6);
                            }}, new HashMap<>(){{ //Major Realm Stats
                        put(Attributes.MAX_HEALTH,21.5);
                        put(Attributes.ATTACK_DAMAGE,7.5);
                        put(Attributes.ARMOR,1.5);
                        put(Attributes.MOVEMENT_SPEED,0.8);
                        put(Attributes.JUMP_STRENGTH,0.9);
                        put(Attributes.STEP_HEIGHT,1.2);
                        put(Attributes.SAFE_FALL_DISTANCE,1.2);
                    }}))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",6,5,"ascension:flight_passive_skill",false)))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:fire",2.0);
                    }})
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Body Path"),
                            Component.literal("§cBody harmonizes with fire essence"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Cultivate in fiery environments"),
                            Component.literal("§e◆ §fPath: Heat → Destruction → Fire Mastery"),
                            Component.literal("§e◆ §fWeakness: Weak to water-based techniques")
                    )));

    public static final TechniqueHolder EARTH_ELEMENTAL_TECHNIQUE = createTechnique("earth_elemental_technique",
            ()->new SingleAttributeTechnique("Earth Elemental Technique",8.0,"ascension:earth",new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{ //Minor Realm Stats
                                put(Attributes.MAX_HEALTH,17.5);
                                put(Attributes.ATTACK_DAMAGE,2.2);
                                put(Attributes.MOVEMENT_SPEED,0.1);
                                put(Attributes.ARMOR,0.3);
                                put(Attributes.JUMP_STRENGTH,0.01);
                                put(Attributes.STEP_HEIGHT,0.01);
                                put(Attributes.SAFE_FALL_DISTANCE,0.6);
                            }}, new HashMap<>(){{ //Major Realm Stats
                        put(Attributes.MAX_HEALTH,33.5);
                        put(Attributes.ATTACK_DAMAGE,9.5);
                        put(Attributes.ARMOR,0.6);
                        put(Attributes.MOVEMENT_SPEED,1.3);
                        put(Attributes.JUMP_STRENGTH,0.03);
                        put(Attributes.STEP_HEIGHT,0.5);
                        put(Attributes.SAFE_FALL_DISTANCE,1.2);
                    }}))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",6,5,"ascension:flight_passive_skill",false)))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:earth",2.0);
                    }})
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Body Path"),
                            Component.literal("§eBody harmonizes with earth essence"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Cultivate on solid ground"),
                            Component.literal("§e◆ §fPath: Stability → Defense → Earth Mastery"),
                            Component.literal("§e◆ §fWeakness: Weak to wood-based techniques")
                    )));

    public static final TechniqueHolder METAL_ELEMENTAL_TECHNIQUE = createTechnique("metal_elemental_technique",
            ()->new SingleAttributeTechnique("Metal Elemental Technique",8.0,"ascension:metal",new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{ //Minor Realm Stats
                                put(Attributes.MAX_HEALTH,17.5);
                                put(Attributes.ATTACK_DAMAGE,2.2);
                                put(Attributes.ARMOR,0.6);
                                put(Attributes.MOVEMENT_SPEED,0.1);
                                put(Attributes.JUMP_STRENGTH,0.01);
                                put(Attributes.STEP_HEIGHT,0.01);
                                put(Attributes.SAFE_FALL_DISTANCE,0.6);
                            }}, new HashMap<>(){{ //Major Realm Stats
                        put(Attributes.MAX_HEALTH,33.5);
                        put(Attributes.ATTACK_DAMAGE,9.5);
                        put(Attributes.ARMOR,1.5);
                        put(Attributes.MOVEMENT_SPEED,1.3);
                        put(Attributes.JUMP_STRENGTH,0.03);
                        put(Attributes.STEP_HEIGHT,0.5);
                        put(Attributes.SAFE_FALL_DISTANCE,1.2);
                    }}))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",6,5,"ascension:flight_passive_skill",false),
                            new AcquirableSkillData("ascension:body",2,3,"ascension:ore_sight_active",false)))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:metal",2.0);
                    }})
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Body Path"),
                            Component.literal("§7Body harmonizes with metal essence"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Cultivate near metal deposits"),
                            Component.literal("§e◆ §fPath: Sharpness → Precision → Metal Mastery"),
                            Component.literal("§e◆ §fWeakness: Weak to fire-based techniques")
                    )));

    public static final TechniqueHolder WATER_ELEMENTAL_TECHNIQUE = createTechnique("water_elemental_technique",
            ()->new SingleAttributeTechnique("Water Elemental Technique",8.0,"ascension:water",new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{ //Minor Realm Stats
                                put(Attributes.MAX_HEALTH,9.5);
                                put(Attributes.ATTACK_DAMAGE,1.4);
                                put(Attributes.MOVEMENT_SPEED,0.2);
                                put(Attributes.ARMOR,0.3);
                                put(Attributes.JUMP_STRENGTH,0.02);
                                put(Attributes.STEP_HEIGHT,0.02);
                                put(Attributes.OXYGEN_BONUS,0.1);
                                put(Attributes.WATER_MOVEMENT_EFFICIENCY,0.08);
                                put(Attributes.SAFE_FALL_DISTANCE,0.6);
                            }}, new HashMap<>(){{ //Major Realm Stats
                        put(Attributes.MAX_HEALTH,18.5);
                        put(Attributes.ATTACK_DAMAGE,3.5);
                        put(Attributes.ARMOR,1.1);
                        put(Attributes.MOVEMENT_SPEED,0.6);
                        put(Attributes.JUMP_STRENGTH,0.8);
                        put(Attributes.STEP_HEIGHT,0.6);
                        put(Attributes.OXYGEN_BONUS,0.4);
                        put(Attributes.WATER_MOVEMENT_EFFICIENCY,0.14);
                        put(Attributes.SAFE_FALL_DISTANCE,1.2);
                    }}))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",6,5,"ascension:flight_passive_skill",false)))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:water",2.0);
                    }})
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Body Path"),
                            Component.literal("§9Body harmonizes with water essence"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Cultivate near water sources"),
                            Component.literal("§e◆ §fPath: Flow → Adaptation → Water Mastery"),
                            Component.literal("§e◆ §fWeakness: Weak to earth-based techniques")
                    )));

    // ========== EARTH TIER TECHNIQUES ==========

    // Body Path - Earth Tier
    public static final TechniqueHolder JADE_BONE_TECHNIQUE = createTechnique("jade_bone_technique",
            ()->new SingleAttributeTechnique("Jade Bone Technique", 6.0, "ascension:earth", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{
                                put(Attributes.MAX_HEALTH, 20.0);
                                put(Attributes.ATTACK_DAMAGE, 2.5);
                                put(Attributes.ARMOR, 1.5);
                                put(Attributes.MOVEMENT_SPEED, 0.08);
                                put(Attributes.JUMP_STRENGTH, 0.04);
                                put(Attributes.STEP_HEIGHT, 0.1);
                                put(Attributes.SAFE_FALL_DISTANCE, 1.0);
                                put(Attributes.LUCK, 0.5);
                            }}, new HashMap<>(){{
                        put(Attributes.MAX_HEALTH, 35.0);
                        put(Attributes.ATTACK_DAMAGE, 6.0);
                        put(Attributes.ARMOR, 3.0);
                        put(Attributes.MOVEMENT_SPEED, 0.5);
                        put(Attributes.JUMP_STRENGTH, 0.15);
                        put(Attributes.STEP_HEIGHT, 0.6);
                        put(Attributes.SAFE_FALL_DISTANCE, 2.0);
                        put(Attributes.LUCK, 1.0);
                    }}))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:earth", 2.2);
                        put("ascension:wood", 1.3);
                        put("ascension:life", 0.8);
                    }})
                    .setDescription(List.of(
                            Component.literal("§2Earth Tier - Body Path"),
                            Component.literal("§aTransforms bones into jade-like structures"),
                            Component.literal("§7Grants durability and life essence"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Absorb earth and wood essence"),
                            Component.literal("§e◆ §fPath: Defense → Regeneration → Jade Immortality"),
                            Component.literal("§e◆ §fWeakness: Vulnerable to metal-based attacks")
                    )));



    // Intent Path - Earth Tier
    public static final TechniqueHolder BLADE_DANCE_TECHNIQUE = createTechnique("blade_dance_technique",
            ()->new SingleIntentTechnique("Blade Dance Technique", 7.5, "ascension:blade_intent", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{
                                put(Attributes.MAX_HEALTH, 10.0);
                                put(Attributes.ATTACK_DAMAGE, 3.0);
                                put(Attributes.ATTACK_SPEED, 0.2);
                                put(Attributes.MOVEMENT_SPEED, 0.15);
                                put(Attributes.JUMP_STRENGTH, 0.05);
                                put(Attributes.STEP_HEIGHT, 0.06);
                                put(Attributes.SAFE_FALL_DISTANCE, 0.6);
                            }}, new HashMap<>(){{
                        put(Attributes.MAX_HEALTH, 22.0);
                        put(Attributes.ATTACK_DAMAGE, 7.5);
                        put(Attributes.ATTACK_SPEED, 0.5);
                        put(Attributes.MOVEMENT_SPEED, 0.6);
                        put(Attributes.JUMP_STRENGTH, 0.2);
                        put(Attributes.STEP_HEIGHT, 0.4);
                        put(Attributes.SAFE_FALL_DISTANCE, 1.2);
                    }}))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:blade_intent", 2.3);
                        put("ascension:sharpness", 1.5);
                        put("ascension:cutting", 1.0);
                    }})
                    .setDescription(List.of(
                            Component.literal("§2Earth Tier - Intent Path"),
                            Component.literal("§7Intent flows like a dancing blade"),
                            Component.literal("§fGraceful yet deadly movements"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Practice fluid motion and precision"),
                            Component.literal("§e◆ §fPath: Slashing → Dance → Blade Mastery"),
                            Component.literal("§e◆ §fWeakness: Less effective against armor")
                    )));

    // ========== EXISTING EARTH TIER TECHNIQUES ==========

    // Intent Path - Earth Tier (Updated)
    public static final TechniqueHolder FIST_KINGS_TECHNIQUE = createTechnique("fist_king_intent",
            ()->new SingleIntentTechnique(
                    "Fist Kings Technique",8.0,"ascension:fist_intent",
                    new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{ //Minor Realm Stats
                                put(Attributes.MAX_HEALTH,15.5);
                                put(Attributes.ATTACK_DAMAGE,5.0);
                                put(Attributes.ARMOR,0.1);
                                put(Attributes.MOVEMENT_SPEED,0.5);
                                put(Attributes.JUMP_STRENGTH,0.02);
                                put(Attributes.STEP_HEIGHT,0.02);
                                put(Attributes.ATTACK_KNOCKBACK, 0.6);
                                put(Attributes.SAFE_FALL_DISTANCE,0.6);
                            }}, new HashMap<>(){{ //Major Realm Stats
                        put(Attributes.MAX_HEALTH,34.4);
                        put(Attributes.ATTACK_DAMAGE,11.8);
                        put(Attributes.ARMOR,0.4);
                        put(Attributes.MOVEMENT_SPEED,0.7);
                        put(Attributes.JUMP_STRENGTH,0.08);
                        put(Attributes.STEP_HEIGHT,0.7);
                        put(Attributes.ATTACK_KNOCKBACK,1.4);
                        put(Attributes.SAFE_FALL_DISTANCE,1.2);
                    }}))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",6,5,"ascension:flight_passive_skill",false)))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:fist_intent",4.8);
                    }})
                    .setDescription(List.of(
                            Component.literal("§2Earth Tier - Intent Path"),
                            Component.literal("§8A technique that focuses the intent into the fists"),
                            Component.literal("§7Making them as hard as a king's weapon"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Train in martial arts and combat"),
                            Component.literal("§e◆ §fPath: Fist → King's Fist → Fist Sovereign"),
                            Component.literal("§e◆ §fWeakness: Vulnerable to ranged attacks")
                    )));

    // Essence Path - Earth Tier (Updated)
    public static final TechniqueHolder VOID_SWALLOWING_TECHNIQUE = createTechnique("void_swallowing_technique",
            ()->new SingleElementTechnique(
                    "Void Swallowing Technique","ascension:void",
                    8.0,new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{ //Minor Realm Stats
                                put(Attributes.MAX_HEALTH,13.0);
                                put(Attributes.ATTACK_DAMAGE,3.2);
                                put(Attributes.ARMOR,0.3);
                                put(Attributes.MOVEMENT_SPEED,0.3);
                                put(Attributes.JUMP_STRENGTH,0.03);
                                put(Attributes.STEP_HEIGHT,0.03);
                                put(Attributes.SAFE_FALL_DISTANCE,0.6);
                            }}, new HashMap<>(){{ //Major Realm Stats
                        put(Attributes.MAX_HEALTH,26.4);
                        put(Attributes.ATTACK_DAMAGE,11.4);
                        put(Attributes.ARMOR,1.0);
                        put(Attributes.MOVEMENT_SPEED,2.3);
                        put(Attributes.JUMP_STRENGTH,0.06);
                        put(Attributes.STEP_HEIGHT,0.8);
                        put(Attributes.SAFE_FALL_DISTANCE,1.2);
                    }}))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",1,5,"ascension:flight_passive_skill",false),
                            new AcquirableSkillData("ascension:essence", 6, 3, "ascension:space_infusion", false)))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:void",2.0);
                    }})
                    .setDescription(List.of(
                            Component.literal("§2Earth Tier - Essence Path"),
                            Component.literal("§8Swallows the void to strengthen one's essence"),
                            Component.literal("§7Can manipulate spatial energies"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Meditate in void-like environments"),
                            Component.literal("§e◆ §fPath: Void → Absorption → Void Mastery"),
                            Component.literal("§e◆ §fWeakness: Light-based techniques")
                    )));

    public static final TechniqueHolder THUNDER_HEART_TECHNIQUE = createTechnique("thunder_heart_technique",
            ()->new SingleElementTechnique("Thunder Heart Technique", "ascension:lightning", 7.0, new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{
                                put(Attributes.MAX_HEALTH, 12.0);
                                put(Attributes.ATTACK_DAMAGE, 3.5);
                                put(Attributes.ATTACK_SPEED, 0.15);
                                put(Attributes.MOVEMENT_SPEED, 0.2);
                                put(Attributes.JUMP_STRENGTH, 0.1);
                                put(Attributes.STEP_HEIGHT, 0.08);
                                put(Attributes.SAFE_FALL_DISTANCE, 0.7);
                            }}, new HashMap<>(){{
                        put(Attributes.MAX_HEALTH, 25.0);
                        put(Attributes.ATTACK_DAMAGE, 8.0);
                        put(Attributes.ATTACK_SPEED, 0.4);
                        put(Attributes.MOVEMENT_SPEED, 0.8);
                        put(Attributes.JUMP_STRENGTH, 0.3);
                        put(Attributes.STEP_HEIGHT, 0.5);
                        put(Attributes.SAFE_FALL_DISTANCE, 1.5);
                    }}))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:lightning", 2.5);
                        put("ascension:storm", 1.8);
                        put("ascension:yang", 1.2);
                    }})
                    .setDescription(List.of(
                            Component.literal("§2Earth Tier - Essence Path"),
                            Component.literal("§eHeart beats with thunderous energy"),
                            Component.literal("§7Channels lightning through meridians"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Cultivate during thunderstorms"),
                            Component.literal("§e◆ §fPath: Speed → Power → Lightning Embodiment"),
                            Component.literal("§e◆ §fWeakness: Conducts electricity, weak to grounding")
                    )));


    // Body Path - Earth Tier (Updated)
    public static final TechniqueHolder DIVINE_PHOENIX_TECHNIQUE = createTechnique("divine_phoenix_technique",
            ()->new SingleAttributeTechnique("Divine Phoenix Technique",8.0,"ascension:phoenix_fire",new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{ //Minor Realm Stats
                                put(Attributes.MAX_HEALTH,25.5);
                                put(Attributes.ATTACK_DAMAGE,5.2);
                                put(Attributes.ARMOR,1.2);
                                put(Attributes.MOVEMENT_SPEED,0.6);
                                put(Attributes.JUMP_STRENGTH,0.06);
                                put(Attributes.STEP_HEIGHT,0.06);
                                put(Attributes.SAFE_FALL_DISTANCE,0.6);
                            }}, new HashMap<>(){{ //Major Realm Stats
                        put(Attributes.MAX_HEALTH,45.5);
                        put(Attributes.ATTACK_DAMAGE,11.5);
                        put(Attributes.ARMOR,2.5);
                        put(Attributes.MOVEMENT_SPEED,1.4);
                        put(Attributes.JUMP_STRENGTH,0.6);
                        put(Attributes.STEP_HEIGHT,0.5);
                        put(Attributes.SAFE_FALL_DISTANCE,1.2);
                    }}))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:essence",6,4,"ascension:flight_passive_skill",false)))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:phoenix_fire",2.0);
                    }})
                    .setDescription(List.of(
                            Component.literal("§2Earth Tier - Body Path"),
                            Component.literal("§6Body infused with phoenix fire"),
                            Component.literal("§7Grants rebirth capabilities and divine flames"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Survive near-death experiences"),
                            Component.literal("§e◆ §fPath: Fire → Rebirth → Phoenix God"),
                            Component.literal("§e◆ §fWeakness: Extreme cold")
                    )));

    // ========== HEAVEN TIER TECHNIQUES ==========

    // Body Path - Heaven Tier
    public static final TechniqueHolder CELESTIAL_BODY_TECHNIQUE = createTechnique("celestial_body_technique",
            ()->new SingleAttributeTechnique("Celestial Body Technique", 9.0, "ascension:heaven", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{
                                put(Attributes.MAX_HEALTH, 25.0);
                                put(Attributes.ATTACK_DAMAGE, 4.0);
                                put(Attributes.ARMOR, 2.0);
                                put(Attributes.MOVEMENT_SPEED, 0.25);
                                put(Attributes.JUMP_STRENGTH, 0.15);
                                put(Attributes.STEP_HEIGHT, 0.15);
                                put(Attributes.SAFE_FALL_DISTANCE, 1.5);
                                put(ModAttributes.PLAYER_QI_REGEN_RATE, 0.3);
                                put(ModAttributes.PLAYER_QI_INSTANCE, 150.0);
                            }}, new HashMap<>(){{
                        put(Attributes.MAX_HEALTH, 45.0);
                        put(Attributes.ATTACK_DAMAGE, 9.0);
                        put(Attributes.ARMOR, 4.0);
                        put(Attributes.MOVEMENT_SPEED, 1.0);
                        put(Attributes.JUMP_STRENGTH, 0.4);
                        put(Attributes.STEP_HEIGHT, 0.8);
                        put(Attributes.SAFE_FALL_DISTANCE, 3.0);
                        put(ModAttributes.PLAYER_QI_REGEN_RATE, 0.6);
                        put(ModAttributes.PLAYER_QI_INSTANCE, 300.0);
                    }}))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:heaven", 2.8);
                        put("ascension:light", 2.0);
                        put("ascension:order", 1.5);
                        put("ascension:yang", 1.2);
                    }})
                    .setDescription(List.of(
                            Component.literal("§bHeaven Tier - Body Path"),
                            Component.literal("§6Body infused with celestial energy"),
                            Component.literal("§eRadiates divine light and authority"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Absorb celestial energies at high altitudes"),
                            Component.literal("§e◆ §fPath: Divine → Celestial → Heavenly Form"),
                            Component.literal("§e◆ §fWeakness: Demonic techniques, defiance-based attacks")
                    )));

    // Essence Path - Heaven Tier
    public static final TechniqueHolder VOID_WALKER_TECHNIQUE = createTechnique("void_walker_technique",
            ()->new SingleElementTechnique("Void Walker Technique", "ascension:void", 10.0, new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{
                                put(Attributes.MAX_HEALTH, 15.0);
                                put(Attributes.ATTACK_DAMAGE, 4.5);
                                put(Attributes.ARMOR, 0.5);
                                put(Attributes.MOVEMENT_SPEED, 0.3);
                                put(Attributes.JUMP_STRENGTH, 0.12);
                                put(Attributes.STEP_HEIGHT, 0.1);
                                put(Attributes.SAFE_FALL_DISTANCE, 1.2);
                                put(ModAttributes.PLAYER_QI_REGEN_RATE, 0.4);
                                put(ModAttributes.PLAYER_QI_INSTANCE, 200.0);
                            }}, new HashMap<>(){{
                        put(Attributes.MAX_HEALTH, 30.0);
                        put(Attributes.ATTACK_DAMAGE, 10.0);
                        put(Attributes.ARMOR, 1.5);
                        put(Attributes.MOVEMENT_SPEED, 1.2);
                        put(Attributes.JUMP_STRENGTH, 0.35);
                        put(Attributes.STEP_HEIGHT, 0.7);
                        put(Attributes.SAFE_FALL_DISTANCE, 2.5);
                        put(ModAttributes.PLAYER_QI_REGEN_RATE, 0.8);
                        put(ModAttributes.PLAYER_QI_INSTANCE, 400.0);
                    }}))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:void", 3.0);
                        put("ascension:space", 2.2);
                        put("ascension:yin", 1.8);
                        put("ascension:darkness", 1.5);
                    }})
                    .setDescription(List.of(
                            Component.literal("§bHeaven Tier - Essence Path"),
                            Component.literal("§8Born between realms, attuned to the void"),
                            Component.literal("§7Can walk through space and vanish from existence"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Meditate in spatial rifts"),
                            Component.literal("§e◆ §fPath: Teleport → Spatial Cut → Void God"),
                            Component.literal("§e◆ §fWeakness: Light-based techniques disrupt void")
                    )));


    // Intent Path - Heaven Tier
    public static final TechniqueHolder SWORD_SAINT_TECHNIQUE = createTechnique("sword_saint_technique",
            ()->new SingleIntentTechnique("Sword Saint Technique", 12.0, "ascension:sword_intent", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{
                                put(Attributes.MAX_HEALTH, 16.0);
                                put(Attributes.ATTACK_DAMAGE, 6.0);
                                put(Attributes.ARMOR, 0.3);
                                put(Attributes.MOVEMENT_SPEED, 0.3);
                                put(Attributes.JUMP_STRENGTH, 0.1);
                                put(Attributes.STEP_HEIGHT, 0.15);
                                put(Attributes.SAFE_FALL_DISTANCE, 1.0);
                                put(ModAttributes.PLAYER_QI_REGEN_RATE, 0.35);
                                put(ModAttributes.PLAYER_QI_INSTANCE, 250.0);
                            }}, new HashMap<>(){{
                        put(Attributes.MAX_HEALTH, 32.0);
                        put(Attributes.ATTACK_DAMAGE, 15.0);
                        put(Attributes.ARMOR, 1.0);
                        put(Attributes.MOVEMENT_SPEED, 1.3);
                        put(Attributes.JUMP_STRENGTH, 0.6);
                        put(Attributes.STEP_HEIGHT, 1.0);
                        put(Attributes.SAFE_FALL_DISTANCE, 3.5);
                        put(ModAttributes.PLAYER_QI_REGEN_RATE, 0.7);
                        put(ModAttributes.PLAYER_QI_INSTANCE, 500.0);
                    }}))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:sword_intent", 3.5);
                        put("ascension:blade_intent", 3.0);
                        put("ascension:sharpness", 2.8);
                        put("ascension:cutting", 2.8);
                        put("ascension:metal", 2.2);
                    }})
                    .setDescription(List.of(
                            Component.literal("§bHeaven Tier - Intent Path"),
                            Component.literal("§8Every cell resonates with sword intent"),
                            Component.literal("§7Can cut through space, time, and concepts"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Comprehend deeper sword laws"),
                            Component.literal("§e◆ §fPath: Sword → Domain → Sword Saint"),
                            Component.literal("§e◆ §fWeakness: Completely focused on sword, lacks versatility")
                    )));

    // ========== ASCENSION TIER TECHNIQUES ==========

    // Essence Path - Ascension Tier
    public static final TechniqueHolder COSMIC_CREATION_ESSENCE_TECHNIQUE = createTechnique("cosmic_creation_essence_technique",
            ()->new SingleElementTechnique("Cosmic Creation Essence Technique", "ascension:cosmos", 18.0, new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{
                                put(Attributes.MAX_HEALTH, 28.0);
                                put(Attributes.ATTACK_DAMAGE, 8.5);
                                put(Attributes.ARMOR, 2.2);
                                put(Attributes.MOVEMENT_SPEED, 0.45);
                                put(Attributes.JUMP_STRENGTH, 0.35);
                                put(Attributes.STEP_HEIGHT, 0.3);
                                put(Attributes.SAFE_FALL_DISTANCE, 3.5);
                                put(ModAttributes.PLAYER_QI_REGEN_RATE, 0.75);
                                put(ModAttributes.PLAYER_QI_INSTANCE, 600.0);
                            }}, new HashMap<>(){{
                        put(Attributes.MAX_HEALTH, 55.0);
                        put(Attributes.ATTACK_DAMAGE, 22.0);
                        put(Attributes.ARMOR, 4.5);
                        put(Attributes.MOVEMENT_SPEED, 2.2);
                        put(Attributes.JUMP_STRENGTH, 1.8);
                        put(Attributes.STEP_HEIGHT, 2.0);
                        put(Attributes.SAFE_FALL_DISTANCE, 9.0);
                        put(ModAttributes.PLAYER_QI_REGEN_RATE, 1.5);
                        put(ModAttributes.PLAYER_QI_INSTANCE, 1200.0);
                    }}))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:cosmos", 2.0);  // Uses existing COSMOS_DAO
                        put("ascension:creation", 1.8); // Uses existing CREATION_DAO
                        put("ascension:space", 1.5);   // Uses existing SPACE_DAO
                        put("ascension:heaven", 1.2);  // Uses existing HEAVEN_DAO
                    }})
                    .setDescription(List.of(
                            Component.literal("§dAscension Tier - Essence Path"),
                            Component.literal("§5Can create and destroy galaxies with a thought"),
                            Component.literal("§dMaster of cosmic energy and universal laws"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Create and nurture star systems"),
                            Component.literal("§e◆ §fPath: Star → Galaxy → Universe Creator"),
                            Component.literal("§e◆ §fWeakness: Absolute nothingness, anti-creation"),
                            Component.literal("§e◆ §fDao Alignment: Cosmos, Creation, Space, Heaven")
                    )));

    public static final TechniqueHolder INFINITE_TIME_ESSENCE_TECHNIQUE = createTechnique("infinite_time_essence_technique",
            ()->new SingleElementTechnique("Infinite Time Essence Technique", "ascension:time", 17.0, new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{
                                put(Attributes.MAX_HEALTH, 25.0);
                                put(Attributes.ATTACK_DAMAGE, 7.8);
                                put(Attributes.ARMOR, 2.0);
                                put(Attributes.MOVEMENT_SPEED, 0.5);
                                put(Attributes.JUMP_STRENGTH, 0.4);
                                put(Attributes.STEP_HEIGHT, 0.35);
                                put(Attributes.SAFE_FALL_DISTANCE, 4.0);
                                put(ModAttributes.PLAYER_QI_REGEN_RATE, 0.8);
                                put(ModAttributes.PLAYER_QI_INSTANCE, 650.0);
                            }}, new HashMap<>(){{
                        put(Attributes.MAX_HEALTH, 50.0);
                        put(Attributes.ATTACK_DAMAGE, 20.0);
                        put(Attributes.ARMOR, 4.0);
                        put(Attributes.MOVEMENT_SPEED, 2.5);
                        put(Attributes.JUMP_STRENGTH, 2.0);
                        put(Attributes.STEP_HEIGHT, 2.2);
                        put(Attributes.SAFE_FALL_DISTANCE, 10.0);
                        put(ModAttributes.PLAYER_QI_REGEN_RATE, 1.6);
                        put(ModAttributes.PLAYER_QI_INSTANCE, 1300.0);
                    }}))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:time", 2.0);           // Uses existing TIME_DAO
                        put("ascension:space", 1.5);          // Uses existing SPACE_DAO
                        put("ascension:eternity", 1.2);       // New concept but related to time
                        put("ascension:reincarnation", 0.8);  // Uses existing REINCARNATION_DAO
                        put("ascension:memory", 0.5);         // Uses existing MEMORY_DAO
                    }})
                    .setDescription(List.of(
                            Component.literal("§dAscension Tier - Essence Path"),
                            Component.literal("§5Can manipulate the flow of time and see all possibilities"),
                            Component.literal("§dExists in all moments at once"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Experience and alter the timeline"),
                            Component.literal("§e◆ §fPath: Time → Eternity → Time Lord"),
                            Component.literal("§e◆ §fWeakness: Paradoxes, time locks, and fixed points"),
                            Component.literal("§e◆ §fDao Alignment: Time, Space, Reincarnation, Memory")
                    )));

    // Body Path - Ascension Tier
    public static final TechniqueHolder PRIMORDIAL_CHAOS_BODY_TECHNIQUE = createTechnique("primordial_chaos_body_technique",
            ()->new SingleAttributeTechnique("Primordial Chaos Body Technique", 15.0, "ascension:chaos", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{
                                put(Attributes.MAX_HEALTH, 30.0);
                                put(Attributes.ATTACK_DAMAGE, 7.0);
                                put(Attributes.ARMOR, 2.5);
                                put(Attributes.MOVEMENT_SPEED, 0.4);
                                put(Attributes.JUMP_STRENGTH, 0.2);
                                put(Attributes.STEP_HEIGHT, 0.2);
                                put(Attributes.SAFE_FALL_DISTANCE, 2.0);
                                put(ModAttributes.PLAYER_QI_REGEN_RATE, 0.6);
                                put(ModAttributes.PLAYER_QI_INSTANCE, 400.0);
                            }}, new HashMap<>(){{
                        put(Attributes.MAX_HEALTH, 60.0);
                        put(Attributes.ATTACK_DAMAGE, 18.0);
                        put(Attributes.ARMOR, 5.0);
                        put(Attributes.MOVEMENT_SPEED, 2.0);
                        put(Attributes.JUMP_STRENGTH, 1.0);
                        put(Attributes.STEP_HEIGHT, 1.5);
                        put(Attributes.SAFE_FALL_DISTANCE, 5.0);
                        put(ModAttributes.PLAYER_QI_REGEN_RATE, 1.2);
                        put(ModAttributes.PLAYER_QI_INSTANCE, 800.0);
                    }}))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:chaos", 3.8);
                        put("ascension:destruction", 3.5);
                        put("ascension:void", 3.0);
                        put("ascension:creation", 2.5);
                    }})
                    .setDescription(List.of(
                            Component.literal("§dAscension Tier - Body Path"),
                            Component.literal("§5Born from the primordial chaos before creation"),
                            Component.literal("§dCan manipulate the fabric of reality itself"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Create and destroy worlds"),
                            Component.literal("§e◆ §fPath: Chaos → Primordial → Reality Weaver"),
                            Component.literal("§e◆ §fWeakness: Order-based divine techniques")
                    )));

    // Body Path - Ascension Tier
    public static final TechniqueHolder DRAGON_KING_BODY_TECHNIQUE = createTechnique("dragon_king_body_technique",
            ()->new SingleAttributeTechnique("Dragon King Body Technique", 14.0, "ascension:dragon", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{
                                put(Attributes.MAX_HEALTH, 35.0);
                                put(Attributes.ATTACK_DAMAGE, 8.0);
                                put(Attributes.ARMOR, 3.0);
                                put(Attributes.MOVEMENT_SPEED, 0.35);
                                put(Attributes.JUMP_STRENGTH, 0.25);
                                put(Attributes.STEP_HEIGHT, 0.25);
                                put(Attributes.SAFE_FALL_DISTANCE, 2.5);
                                put(ModAttributes.PLAYER_QI_REGEN_RATE, 0.55);
                                put(ModAttributes.PLAYER_QI_INSTANCE, 350.0);
                            }}, new HashMap<>(){{
                        put(Attributes.MAX_HEALTH, 65.0);
                        put(Attributes.ATTACK_DAMAGE, 20.0);
                        put(Attributes.ARMOR, 6.0);
                        put(Attributes.MOVEMENT_SPEED, 1.8);
                        put(Attributes.JUMP_STRENGTH, 1.2);
                        put(Attributes.STEP_HEIGHT, 1.8);
                        put(Attributes.SAFE_FALL_DISTANCE, 6.0);
                        put(ModAttributes.PLAYER_QI_REGEN_RATE, 1.1);
                        put(ModAttributes.PLAYER_QI_INSTANCE, 700.0);
                    }}))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:dragon", 3.6);
                        put("ascension:earth", 3.0);
                        put("ascension:fire", 2.8);
                        put("ascension:water", 2.8);
                        put("ascension:primordial", 2.5);
                    }})
                    .setDescription(List.of(
                            Component.literal("§dAscension Tier - Body Path"),
                            Component.literal("§5Possesses ancient dragon bloodline"),
                            Component.literal("§dVeins pulse with primordial dragon energy"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Absorb essence of dragon remains"),
                            Component.literal("§e◆ §fPath: Scale → Roar → Dragon God"),
                            Component.literal("§e◆ §fWeakness: Dragon-slaying techniques")
                    )));

    // Intent Path - Ascension Tier
    public static final TechniqueHolder UNIVERSE_DEVOURER_INTENT_TECHNIQUE = createTechnique("universe_devourer_intent_technique",
            ()->new SingleIntentTechnique("Universe Devourer Intent Technique", 16.0, "ascension:devouring", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{
                                put(Attributes.MAX_HEALTH, 25.0);
                                put(Attributes.ATTACK_DAMAGE, 9.0);
                                put(Attributes.ARMOR, 2.0);
                                put(Attributes.MOVEMENT_SPEED, 0.5);
                                put(Attributes.JUMP_STRENGTH, 0.3);
                                put(Attributes.STEP_HEIGHT, 0.3);
                                put(Attributes.SAFE_FALL_DISTANCE, 3.0);
                                put(ModAttributes.PLAYER_QI_REGEN_RATE, 0.7);
                                put(ModAttributes.PLAYER_QI_INSTANCE, 500.0);
                            }}, new HashMap<>(){{
                        put(Attributes.MAX_HEALTH, 50.0);
                        put(Attributes.ATTACK_DAMAGE, 25.0);
                        put(Attributes.ARMOR, 4.0);
                        put(Attributes.MOVEMENT_SPEED, 2.5);
                        put(Attributes.JUMP_STRENGTH, 1.5);
                        put(Attributes.STEP_HEIGHT, 2.0);
                        put(Attributes.SAFE_FALL_DISTANCE, 8.0);
                        put(ModAttributes.PLAYER_QI_REGEN_RATE, 1.4);
                        put(ModAttributes.PLAYER_QI_INSTANCE, 1000.0);
                    }}))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:devouring", 4.0);
                        put("ascension:void", 3.8);
                        put("ascension:chaos", 3.5);
                        put("ascension:space", 3.3);
                        put("ascension:time", 3.2);
                    }})
                    .setDescription(List.of(
                            Component.literal("§dAscension Tier - Intent Path"),
                            Component.literal("§0Can devour stars, worlds, and eventually universes"),
                            Component.literal("§8Grows stronger by consuming everything"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Consume higher quality energy sources"),
                            Component.literal("§e◆ §fPath: Consumption → Black Hole → Universe Devourer"),
                            Component.literal("§e◆ §fWeakness: Divine seals and spatial locks")
                    )));

    // Intent Path - Ascension Tier
    public static final TechniqueHolder ETERNAL_REINCARNATION_INTENT_TECHNIQUE = createTechnique("eternal_reincarnation_intent_technique",
            ()->new SingleIntentTechnique("Eternal Reincarnation Intent Technique", 14.0, "ascension:reincarnation", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                            new HashMap<>(){{
                                put(Attributes.MAX_HEALTH, 22.0);
                                put(Attributes.ATTACK_DAMAGE, 6.5);
                                put(Attributes.ARMOR, 1.8);
                                put(Attributes.MOVEMENT_SPEED, 0.4);
                                put(Attributes.JUMP_STRENGTH, 0.25);
                                put(Attributes.STEP_HEIGHT, 0.25);
                                put(Attributes.SAFE_FALL_DISTANCE, 2.8);
                                put(ModAttributes.PLAYER_QI_REGEN_RATE, 0.65);
                                put(ModAttributes.PLAYER_QI_INSTANCE, 450.0);
                            }}, new HashMap<>(){{
                        put(Attributes.MAX_HEALTH, 45.0);
                        put(Attributes.ATTACK_DAMAGE, 18.0);
                        put(Attributes.ARMOR, 3.5);
                        put(Attributes.MOVEMENT_SPEED, 1.5);
                        put(Attributes.JUMP_STRENGTH, 1.0);
                        put(Attributes.STEP_HEIGHT, 1.2);
                        put(Attributes.SAFE_FALL_DISTANCE, 7.0);
                        put(ModAttributes.PLAYER_QI_REGEN_RATE, 1.3);
                        put(ModAttributes.PLAYER_QI_INSTANCE, 900.0);
                    }}))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:reincarnation", 3.8);
                        put("ascension:time", 3.5);
                        put("ascension:soul", 3.2);
                        put("ascension:life", 3.0);
                        put("ascension:memory", 2.8);
                    }})
                    .setDescription(List.of(
                            Component.literal("§dAscension Tier - Intent Path"),
                            Component.literal("§3Has lived through countless lifetimes"),
                            Component.literal("§bRemembers all past lives and their skills"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Awaken past life memories"),
                            Component.literal("§e◆ §fPath: Memory → Rebirth → Eternal Cycle"),
                            Component.literal("§e◆ §fWeakness: Karma accumulates from past lives")
                    )));

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