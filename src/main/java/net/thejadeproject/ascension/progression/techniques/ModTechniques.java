package net.thejadeproject.ascension.progression.techniques;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.constants.LivingEntityState;
import net.thejadeproject.ascension.cultivation.player.realm_change_handlers.StandardStatRealmChange;
import net.thejadeproject.ascension.data_attachments.attachments.AscensionAttributeWrapper;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.items.technique_manuals.GenericTechniqueManual;
import net.thejadeproject.ascension.progression.skills.skill_lists.SingleSkillAcquirableData;
import net.thejadeproject.ascension.progression.techniques.stability_handlers.LnStabilityHandler;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.techniques.path_techniques.body.SingleAttributeTechnique;
import net.thejadeproject.ascension.progression.techniques.path_techniques.essence.SingleElementTechnique;
import net.thejadeproject.ascension.progression.techniques.path_techniques.intent.SingleIntentTechnique;

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
    
    public static AscensionAttributeWrapper.AscensionAttributeModifier modifier(Holder<Attribute> attributeHolder,double val){
        return new AscensionAttributeWrapper.AscensionAttributeModifier(
                LivingEntityState.ALL,
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"realm_change_modifier"),
                attributeHolder,
                false,
                val
        );
    }

    public static final DeferredRegister<ITechnique> TECHNIQUES = DeferredRegister.create(AscensionRegistries.Techniques.TECHNIQUES_REGISTRY, AscensionCraft.MOD_ID);

    // Essence Techniques
    public static final TechniqueHolder PURE_FIRE_TECHNIQUE = createTechnique("pure_fire_technique",
            ()->new SingleElementTechnique(
                    "Pure Fire Technique", "ascension:fire", 2.0, new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 12.5),
                            modifier(Attributes.ATTACK_DAMAGE, 3.4),
                            modifier(Attributes.MOVEMENT_SPEED, 0.08),
                            modifier(Attributes.ARMOR, 0.5),
                            modifier(Attributes.JUMP_STRENGTH, 0.04),
                            modifier(Attributes.STEP_HEIGHT, 0.08),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 0.6)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 21.5),
                            modifier(Attributes.ATTACK_DAMAGE, 7.5),
                            modifier(Attributes.ARMOR, 1.5),
                            modifier(Attributes.MOVEMENT_SPEED, 0.8),
                            modifier(Attributes.JUMP_STRENGTH, 0.9),
                            modifier(Attributes.STEP_HEIGHT, 1.2),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        )
                    ))
                    .setSkillList(List.of(
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"flight_passive_skill"),1,5,false,false),
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"basic_fire_ball"),0,0,false,false),
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"large_fire_ball"),0,0,false,false),
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"delayed_fire_launch"),0,0,false,false)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:fire", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§7Human Tier - Essence Path\n")
                            .append("§cBody harmonizes with pure fire essence\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Cultivate in fiery environments\n")
                            .append("§e◆ §fPath: Flame → Destruction → Fire Mastery\n")
                            .append("§e◆ §fWeakness: Water-based techniques")
                    ));

    public static final TechniqueHolder PURE_WATER_TECHNIQUE = createTechnique("pure_water_technique",
            ()->new SingleElementTechnique(
                    "Pure Water Technique", "ascension:water", 2.0, new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 9.5),
                            modifier(Attributes.ATTACK_DAMAGE, 1.4),
                            modifier(Attributes.MOVEMENT_SPEED, 0.2),
                            modifier(Attributes.ARMOR, 0.3),
                            modifier(Attributes.JUMP_STRENGTH, 0.02),
                            modifier(Attributes.STEP_HEIGHT, 0.02),
                            modifier(Attributes.OXYGEN_BONUS, 0.1),
                            modifier(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.08),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 0.6)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 18.5),
                            modifier(Attributes.ATTACK_DAMAGE, 3.5),
                            modifier(Attributes.ARMOR, 1.1),
                            modifier(Attributes.MOVEMENT_SPEED, 0.6),
                            modifier(Attributes.JUMP_STRENGTH, 0.8),
                            modifier(Attributes.STEP_HEIGHT, 0.6),
                            modifier(Attributes.OXYGEN_BONUS, 0.4),
                            modifier(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.14),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        )
                    ))
                    .setSkillList(List.of(
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"flight_passive_skill"),1,5,false,false)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:water", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§7Human Tier - Essence Path\n")
                            .append("§9Body harmonizes with pure water essence\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Cultivate near water sources\n")
                            .append("§e◆ §fPath: Flow → Adaptation → Water Mastery\n")
                            .append("§e◆ §fWeakness: Earth-based techniques")
                    ));

    public static final TechniqueHolder PURE_WOOD_TECHNIQUE = createTechnique("pure_wood_technique",
            ()->new SingleElementTechnique(
                    "Pure Wood Technique", "ascension:wood", 2.0, new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 7.5),
                            modifier(Attributes.ATTACK_DAMAGE, 1.2),
                            modifier(Attributes.MOVEMENT_SPEED, 0.9),
                            modifier(Attributes.ARMOR, 0.2),
                            modifier(Attributes.JUMP_STRENGTH, 0.09),
                            modifier(Attributes.STEP_HEIGHT, 0.1),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 0.6)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 15.5),
                            modifier(Attributes.ATTACK_DAMAGE, 6.5),
                            modifier(Attributes.ARMOR, 0.4),
                            modifier(Attributes.MOVEMENT_SPEED, 1.8),
                            modifier(Attributes.JUMP_STRENGTH, 0.14),
                            modifier(Attributes.STEP_HEIGHT, 1.0),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        )
                    ))
                    .setSkillList(List.of(
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"flight_passive_skill"),1,5,false,false)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:wood", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§7Human Tier - Essence Path\n")
                            .append("§aBody harmonizes with pure wood essence\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Cultivate in forests\n")
                            .append("§e◆ §fPath: Growth → Healing → Wood Mastery\n")
                            .append("§e◆ §fWeakness: Fire and metal techniques")
                    ));

    public static final TechniqueHolder PURE_EARTH_TECHNIQUE = createTechnique("pure_earth_technique",
            ()->new SingleElementTechnique(
                    "Pure Earth Technique", "ascension:earth", 2.0, new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 17.5),
                            modifier(Attributes.ATTACK_DAMAGE, 2.2),
                            modifier(Attributes.MOVEMENT_SPEED, 0.1),
                            modifier(Attributes.ARMOR, 0.3),
                            modifier(Attributes.JUMP_STRENGTH, 0.01),
                            modifier(Attributes.STEP_HEIGHT, 0.01),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 0.6)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 33.5),
                            modifier(Attributes.ATTACK_DAMAGE, 9.5),
                            modifier(Attributes.ARMOR, 0.6),
                            modifier(Attributes.MOVEMENT_SPEED, 1.3),
                            modifier(Attributes.JUMP_STRENGTH, 0.03),
                            modifier(Attributes.STEP_HEIGHT, 0.5),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        )
                    ))
                    .setSkillList(List.of(
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"flight_passive_skill"),1,5,false,false)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:earth", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§7Human Tier - Essence Path\n")
                            .append("§eBody harmonizes with pure earth essence\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Cultivate on solid ground\n")
                            .append("§e◆ §fPath: Stability → Defense → Earth Mastery\n")
                            .append("§e◆ §fWeakness: Wood-based techniques")
                    ));

    public static final TechniqueHolder PURE_METAL_TECHNIQUE = createTechnique("pure_metal_technique",
            ()->new SingleElementTechnique(
                    "Pure Metal Technique", "ascension:metal", 2.0, new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 17.5),
                            modifier(Attributes.ATTACK_DAMAGE, 2.2),
                            modifier(Attributes.ARMOR, 0.6),
                            modifier(Attributes.MOVEMENT_SPEED, 0.1),
                            modifier(Attributes.JUMP_STRENGTH, 0.01),
                            modifier(Attributes.STEP_HEIGHT, 0.01),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 0.6)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 33.5),
                            modifier(Attributes.ATTACK_DAMAGE, 9.5),
                            modifier(Attributes.ARMOR, 1.5),
                            modifier(Attributes.MOVEMENT_SPEED, 1.3),
                            modifier(Attributes.JUMP_STRENGTH, 0.03),
                            modifier(Attributes.STEP_HEIGHT, 0.5),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        )
                    ))
                    .setSkillList(List.of(
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"flight_passive_skill"),1,5,false,false)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:metal", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§7Human Tier - Essence Path\n")
                            .append("§7Body harmonizes with pure metal essence\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Cultivate near metal deposits\n")
                            .append("§e◆ §fPath: Sharpness → Precision → Metal Mastery\n")
                            .append("§e◆ §fWeakness: Fire-based techniques")
                    ));

    public static final TechniqueHolder VOID_SWALLOWING_TECHNIQUE = createTechnique("void_swallowing_technique",
            ()->new SingleElementTechnique(
                    "Void Swallowing Technique", "ascension:void", 8.0, new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 13.0),
                            modifier(Attributes.ATTACK_DAMAGE, 3.2),
                            modifier(Attributes.ARMOR, 0.3),
                            modifier(Attributes.MOVEMENT_SPEED, 0.3),
                            modifier(Attributes.JUMP_STRENGTH, 0.03),
                            modifier(Attributes.STEP_HEIGHT, 0.03),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 0.6)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 26.4),
                            modifier(Attributes.ATTACK_DAMAGE, 11.4),
                            modifier(Attributes.ARMOR, 1.0),
                            modifier(Attributes.MOVEMENT_SPEED, 2.3),
                            modifier(Attributes.JUMP_STRENGTH, 0.06),
                            modifier(Attributes.STEP_HEIGHT, 0.8),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        )
                    ))
                    .setSkillList(List.of(
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"flight_passive_skill"),1,5,false,false)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:void", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§2Earth Tier - Essence Path\n")
                            .append("§8Body resonates with the void between realms\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Meditate in void-like environments\n")
                            .append("§e◆ §fPath: Void → Absorption → Void Mastery\n")
                            .append("§e◆ §fWeakness: Light-based techniques")
                    ));

    // Intent Path - Human Tier
    public static final TechniqueHolder PURE_SWORD_INTENT = createTechnique("pure_sword_intent",
            ()->new SingleIntentTechnique(
                    "Pure Sword Technique", 8.0, "ascension:sword_intent", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 7.5),
                            modifier(Attributes.ATTACK_DAMAGE, 2.3),
                            modifier(Attributes.ARMOR, 0.1),
                            modifier(Attributes.MOVEMENT_SPEED, 0.3),
                            modifier(Attributes.JUMP_STRENGTH, 0.03),
                            modifier(Attributes.STEP_HEIGHT, 0.03),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 0.6)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 26.4),
                            modifier(Attributes.ATTACK_DAMAGE, 7.4),
                            modifier(Attributes.ARMOR, 0.4),
                            modifier(Attributes.MOVEMENT_SPEED, 0.9),
                            modifier(Attributes.JUMP_STRENGTH, 0.06),
                            modifier(Attributes.STEP_HEIGHT, 0.6),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        )
                    ))
                    .setSkillList(List.of(
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"flight_passive_skill"),6,5,false,false),
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"sword_intent_skill"),0,3,true,false)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:sword_intent", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§7Human Tier - Intent Path\n")
                            .append("§fBody resonates with sword techniques\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Master sword techniques faster\n")
                            .append("§e◆ §fPath: Slashing → Cutting → Sword Mastery\n")
                            .append("§e◆ §fWeakness: Less effective against armor")
                    ));

    public static final TechniqueHolder PURE_AXE_INTENT = createTechnique("pure_axe_intent",
            ()->new SingleIntentTechnique(
                    "Pure Axe Technique", 8.0, "ascension:axe_intent", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 6.5),
                            modifier(Attributes.ATTACK_DAMAGE, 3.3),
                            modifier(Attributes.ARMOR, 0.2),
                            modifier(Attributes.MOVEMENT_SPEED, 0.2),
                            modifier(Attributes.JUMP_STRENGTH, 0.04),
                            modifier(Attributes.STEP_HEIGHT, 0.04),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 0.6)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 17.4),
                            modifier(Attributes.ATTACK_DAMAGE, 8.4),
                            modifier(Attributes.ARMOR, 0.6),
                            modifier(Attributes.MOVEMENT_SPEED, 0.8),
                            modifier(Attributes.JUMP_STRENGTH, 0.09),
                            modifier(Attributes.STEP_HEIGHT, 0.7),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        )
                    ))
                    .setSkillList(List.of(
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"flight_passive_skill"),6,5,false,false)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:axe_intent", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§7Human Tier - Intent Path\n")
                            .append("§fBody resonates with axe techniques\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Master axe techniques faster\n")
                            .append("§e◆ §fPath: Chopping → Cleaving → Axe Mastery\n")
                            .append("§e◆ §fWeakness: Slow attack speed")
                    ));

    public static final TechniqueHolder PURE_BLADE_INTENT = createTechnique("pure_blade_intent",
            ()->new SingleIntentTechnique(
                    "Pure Blade Technique", 8.0, "ascension:blade_intent", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 8.5),
                            modifier(Attributes.ATTACK_DAMAGE, 2.3),
                            modifier(Attributes.ARMOR, 0.5),
                            modifier(Attributes.MOVEMENT_SPEED, 0.5),
                            modifier(Attributes.JUMP_STRENGTH, 0.07),
                            modifier(Attributes.STEP_HEIGHT, 0.07),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 0.6)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 26.4),
                            modifier(Attributes.ATTACK_DAMAGE, 7.8),
                            modifier(Attributes.ARMOR, 1.2),
                            modifier(Attributes.MOVEMENT_SPEED, 1.1),
                            modifier(Attributes.JUMP_STRENGTH, 0.14),
                            modifier(Attributes.STEP_HEIGHT, 0.9),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        )
                    ))
                    .setSkillList(List.of(
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"flight_passive_skill"),6,5,false,false)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:blade_intent", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§7Human Tier - Intent Path\n")
                            .append("§fBody resonates with blade techniques\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Master blade techniques faster\n")
                            .append("§e◆ §fPath: Slashing → Cutting → Blade Mastery\n")
                            .append("§e◆ §fWeakness: Less effective against armor")
                    ));

    public static final TechniqueHolder PURE_SPEAR_INTENT = createTechnique("pure_spear_intent",
            ()->new SingleIntentTechnique(
                    "Pure Spear Technique", 8.0, "ascension:spear_intent", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 11.5),
                            modifier(Attributes.ATTACK_DAMAGE, 2.0),
                            modifier(Attributes.ARMOR, 0.3),
                            modifier(Attributes.MOVEMENT_SPEED, 0.3),
                            modifier(Attributes.JUMP_STRENGTH, 0.04),
                            modifier(Attributes.STEP_HEIGHT, 0.11),
                            modifier(Attributes.ENTITY_INTERACTION_RANGE, 0.2),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 0.6)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 29.4),
                            modifier(Attributes.ATTACK_DAMAGE, 4.8),
                            modifier(Attributes.ARMOR, 0.8),
                            modifier(Attributes.MOVEMENT_SPEED, 0.7),
                            modifier(Attributes.JUMP_STRENGTH, 0.08),
                            modifier(Attributes.STEP_HEIGHT, 0.7),
                            modifier(Attributes.ENTITY_INTERACTION_RANGE, 1.2),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        )
                    ))
                    .setSkillList(List.of(
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"flight_passive_skill"),6,5,false,false)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:spear_intent", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§7Human Tier - Intent Path\n")
                            .append("§fBody resonates with spear techniques\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Master spear techniques faster\n")
                            .append("§e◆ §fPath: Piercing → Thrusting → Spear Mastery\n")
                            .append("§e◆ §fWeakness: Vulnerable at close range")
                    ));

    public static final TechniqueHolder PURE_FIST_INTENT = createTechnique("pure_fist_intent",
            ()->new SingleIntentTechnique(
                    "Pure Fist Technique", 8.0, "ascension:fist_intent", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 12.5),
                            modifier(Attributes.ATTACK_DAMAGE, 4.0),
                            modifier(Attributes.ARMOR, 0.5),
                            modifier(Attributes.MOVEMENT_SPEED, 0.5),
                            modifier(Attributes.JUMP_STRENGTH, 0.02),
                            modifier(Attributes.STEP_HEIGHT, 0.02),
                            modifier(Attributes.ATTACK_KNOCKBACK, 0.2),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 0.6)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 29.4),
                            modifier(Attributes.ATTACK_DAMAGE, 4.8),
                            modifier(Attributes.ARMOR, 0.8),
                            modifier(Attributes.MOVEMENT_SPEED, 0.7),
                            modifier(Attributes.JUMP_STRENGTH, 0.08),
                            modifier(Attributes.STEP_HEIGHT, 0.7),
                            modifier(Attributes.ATTACK_KNOCKBACK, 0.8),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        )
                    ))
                    .setSkillList(List.of(
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"flight_passive_skill"),6,5,false,false),
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"fist_aura_skill"),0,0,true,false)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:fist_intent", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§7Human Tier - Intent Path\n")
                            .append("§fBody resonates with fist techniques\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Master martial arts and combat\n")
                            .append("§e◆ §fPath: Punch → Impact → Fist Mastery\n")
                            .append("§e◆ §fWeakness: Vulnerable to ranged attacks")
                    ));

    public static final TechniqueHolder FIST_KINGS_TECHNIQUE = createTechnique("fist_kings_technique",
            ()->new SingleIntentTechnique(
                    "Fist Kings Technique", 8.0, "ascension:fist_intent", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 15.5),
                            modifier(Attributes.ATTACK_DAMAGE, 5.0),
                            modifier(Attributes.ARMOR, 0.1),
                            modifier(Attributes.MOVEMENT_SPEED, 0.5),
                            modifier(Attributes.JUMP_STRENGTH, 0.02),
                            modifier(Attributes.STEP_HEIGHT, 0.02),
                            modifier(Attributes.ATTACK_KNOCKBACK, 0.6),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 0.6)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 34.4),
                            modifier(Attributes.ATTACK_DAMAGE, 11.8),
                            modifier(Attributes.ARMOR, 0.4),
                            modifier(Attributes.MOVEMENT_SPEED, 0.7),
                            modifier(Attributes.JUMP_STRENGTH, 0.08),
                            modifier(Attributes.STEP_HEIGHT, 0.7),
                            modifier(Attributes.ATTACK_KNOCKBACK, 1.4),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        )
                    ))
                    .setSkillList(List.of(
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"flight_passive_skill"),6,5,false,false)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:fist_intent", 4.8);
                    }})
                    .setDescription(Component.empty()
                            .append("§2Earth Tier - Intent Path\n")
                            .append("§8A technique that focuses intent into the fists\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Train in martial arts and combat\n")
                            .append("§e◆ §fPath: Fist → King's Fist → Fist Sovereign\n")
                            .append("§e◆ §fWeakness: Vulnerable to ranged attacks")
                    ));

    // Body Techniques
    public static final TechniqueHolder WOOD_ELEMENTAL_TECHNIQUE = createTechnique("wood_elemental_technique",
            ()->new SingleAttributeTechnique(
                    "Wood Elemental Technique", 8.0, "ascension:wood", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 12.5),
                            modifier(Attributes.ATTACK_DAMAGE, 4.4),
                            modifier(Attributes.MOVEMENT_SPEED, 0.1),
                            modifier(Attributes.ARMOR, 0.8),
                            modifier(Attributes.JUMP_STRENGTH, 0.07),
                            modifier(Attributes.STEP_HEIGHT, 0.08),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 0.6)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 21.5),
                            modifier(Attributes.ATTACK_DAMAGE, 8.5),
                            modifier(Attributes.ARMOR, 1.6),
                            modifier(Attributes.MOVEMENT_SPEED, 2.2),
                            modifier(Attributes.JUMP_STRENGTH, 1.2),
                            modifier(Attributes.STEP_HEIGHT, 1.2),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        )
                    ))
                    .setSkillList(List.of(
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"flight_passive_skill"),6,5,false,false)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:wood", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§7Human Tier - Body Path\n")
                            .append("§aBody harmonizes with wood essence\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Cultivate in forests\n")
                            .append("§e◆ §fPath: Growth → Healing → Wood Mastery\n")
                            .append("§e◆ §fWeakness: Vulnerable to fire and metal")
                    ));

    public static final TechniqueHolder FIRE_ELEMENTAL_TECHNIQUE = createTechnique("fire_elemental_technique",
            ()->new SingleAttributeTechnique(
                    "Fire Elemental Technique", 8.0, "ascension:fire", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 12.5),
                            modifier(Attributes.ATTACK_DAMAGE, 3.4),
                            modifier(Attributes.MOVEMENT_SPEED, 0.08),
                            modifier(Attributes.ARMOR, 0.5),
                            modifier(Attributes.JUMP_STRENGTH, 0.04),
                            modifier(Attributes.STEP_HEIGHT, 0.08),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 0.6)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 21.5),
                            modifier(Attributes.ATTACK_DAMAGE, 7.5),
                            modifier(Attributes.ARMOR, 1.5),
                            modifier(Attributes.MOVEMENT_SPEED, 0.8),
                            modifier(Attributes.JUMP_STRENGTH, 0.9),
                            modifier(Attributes.STEP_HEIGHT, 1.2),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        )
                    ))
                    .setSkillList(List.of(
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"flight_passive_skill"),6,5,false,false)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:fire", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§7Human Tier - Body Path\n")
                            .append("§cBody harmonizes with fire essence\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Cultivate in fiery environments\n")
                            .append("§e◆ §fPath: Heat → Destruction → Fire Mastery\n")
                            .append("§e◆ §fWeakness: Weak to water-based techniques")
                    ));

    public static final TechniqueHolder EARTH_ELEMENTAL_TECHNIQUE = createTechnique("earth_elemental_technique",
            ()->new SingleAttributeTechnique(
                    "Earth Elemental Technique", 8.0, "ascension:earth", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 17.5),
                            modifier(Attributes.ATTACK_DAMAGE, 2.2),
                            modifier(Attributes.MOVEMENT_SPEED, 0.1),
                            modifier(Attributes.ARMOR, 0.3),
                            modifier(Attributes.JUMP_STRENGTH, 0.01),
                            modifier(Attributes.STEP_HEIGHT, 0.01),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 0.6)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 33.5),
                            modifier(Attributes.ATTACK_DAMAGE, 9.5),
                            modifier(Attributes.ARMOR, 0.6),
                            modifier(Attributes.MOVEMENT_SPEED, 1.3),
                            modifier(Attributes.JUMP_STRENGTH, 0.03),
                            modifier(Attributes.STEP_HEIGHT, 0.5),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        )
                    ))
                    .setSkillList(List.of(
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"flight_passive_skill"),6,5,false,false)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:earth", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§7Human Tier - Body Path\n")
                            .append("§eBody harmonizes with earth essence\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Cultivate on solid ground\n")
                            .append("§e◆ §fPath: Stability → Defense → Earth Mastery\n")
                            .append("§e◆ §fWeakness: Weak to wood-based techniques")
                    ));

    public static final TechniqueHolder METAL_ELEMENTAL_TECHNIQUE = createTechnique("metal_elemental_technique",
            ()->new SingleAttributeTechnique(
                    "Metal Elemental Technique", 8.0, "ascension:metal", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 17.5),
                            modifier(Attributes.ATTACK_DAMAGE, 2.2),
                            modifier(Attributes.ARMOR, 0.6),
                            modifier(Attributes.MOVEMENT_SPEED, 0.1),
                            modifier(Attributes.JUMP_STRENGTH, 0.01),
                            modifier(Attributes.STEP_HEIGHT, 0.01),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 0.6)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 33.5),
                            modifier(Attributes.ATTACK_DAMAGE, 9.5),
                            modifier(Attributes.ARMOR, 1.5),
                            modifier(Attributes.MOVEMENT_SPEED, 1.3),
                            modifier(Attributes.JUMP_STRENGTH, 0.03),
                            modifier(Attributes.STEP_HEIGHT, 0.5),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        )
                    ))
                    .setSkillList(List.of(
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"flight_passive_skill"),6,5,false,false)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:metal", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§7Human Tier - Body Path\n")
                            .append("§7Body harmonizes with metal essence\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Cultivate near metal deposits\n")
                            .append("§e◆ §fPath: Sharpness → Precision → Metal Mastery\n")
                            .append("§e◆ §fWeakness: Weak to fire-based techniques")
                    ));

    public static final TechniqueHolder WATER_ELEMENTAL_TECHNIQUE = createTechnique("water_elemental_technique",
            ()->new SingleAttributeTechnique(
                    "Water Elemental Technique", 8.0, "ascension:water", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 9.5),
                            modifier(Attributes.ATTACK_DAMAGE, 1.4),
                            modifier(Attributes.MOVEMENT_SPEED, 0.2),
                            modifier(Attributes.ARMOR, 0.3),
                            modifier(Attributes.JUMP_STRENGTH, 0.02),
                            modifier(Attributes.STEP_HEIGHT, 0.02),
                            modifier(Attributes.OXYGEN_BONUS, 0.1),
                            modifier(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.08),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 0.6)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 18.5),
                            modifier(Attributes.ATTACK_DAMAGE, 3.5),
                            modifier(Attributes.ARMOR, 1.1),
                            modifier(Attributes.MOVEMENT_SPEED, 0.6),
                            modifier(Attributes.JUMP_STRENGTH, 0.8),
                            modifier(Attributes.STEP_HEIGHT, 0.6),
                            modifier(Attributes.OXYGEN_BONUS, 0.4),
                            modifier(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.14),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        )
                    ))
                    .setSkillList(List.of(
                            new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"flight_passive_skill"),6,5,false,false)
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:water", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§7Human Tier - Body Path\n")
                            .append("§9Body harmonizes with water essence\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Cultivate near water sources\n")
                            .append("§e◆ §fPath: Flow → Adaptation → Water Mastery\n")
                            .append("§e◆ §fWeakness: Weak to earth-based techniques")
                    ));

    // ========== EARTH TIER TECHNIQUES ==========

    // Body Path - Earth Tier
    public static final TechniqueHolder JADE_BONE_TECHNIQUE = createTechnique("jade_bone_technique",
            ()->new SingleAttributeTechnique(
                    "Jade Bone Technique", 6.0, "ascension:earth", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 20.0),
                            modifier(Attributes.ATTACK_DAMAGE, 2.5),
                            modifier(Attributes.ARMOR, 1.5),
                            modifier(Attributes.MOVEMENT_SPEED, 0.08),
                            modifier(Attributes.JUMP_STRENGTH, 0.04),
                            modifier(Attributes.STEP_HEIGHT, 0.1),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.0)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 35.0),
                            modifier(Attributes.ATTACK_DAMAGE, 6.0),
                            modifier(Attributes.ARMOR, 3.0),
                            modifier(Attributes.MOVEMENT_SPEED, 0.5),
                            modifier(Attributes.JUMP_STRENGTH, 0.15),
                            modifier(Attributes.STEP_HEIGHT, 0.6),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 2.0)
                        )
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:earth", 2.2);
                        put("ascension:wood", 1.3);
                        put("ascension:life", 0.8);
                    }})
                    .setDescription(Component.empty()
                            .append("§2Earth Tier - Body Path\n")
                            .append("§aTransforms bones into jade-like structures\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Absorb earth and wood essence\n")
                            .append("§e◆ §fPath: Defense → Regeneration → Jade Immortality\n")
                            .append("§e◆ §fWeakness: Vulnerable to metal-based attacks")
                    ));

    // Intent Path - Earth Tier
    public static final TechniqueHolder BLADE_DANCE_TECHNIQUE = createTechnique("blade_dance_technique",
            ()->new SingleIntentTechnique(
                    "Blade Dance Technique", 7.5, "ascension:blade_intent", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 10.0),
                            modifier(Attributes.ATTACK_DAMAGE, 3.0),
                            modifier(Attributes.MOVEMENT_SPEED, 0.15),
                            modifier(Attributes.JUMP_STRENGTH, 0.05),
                            modifier(Attributes.STEP_HEIGHT, 0.06),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 0.6)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 22.0),
                            modifier(Attributes.ATTACK_DAMAGE, 7.5),
                            modifier(Attributes.MOVEMENT_SPEED, 0.6),
                            modifier(Attributes.JUMP_STRENGTH, 0.2),
                            modifier(Attributes.STEP_HEIGHT, 0.4),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        )
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:blade_intent", 2.3);
                    }})
                    .setDescription(Component.empty()
                            .append("§2Earth Tier - Intent Path\n")
                            .append("§7Intent flows like a dancing blade\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Practice fluid motion and precision\n")
                            .append("§e◆ §fPath: Slashing → Dance → Blade Mastery\n")
                            .append("§e◆ §fWeakness: Less effective against armor")
                    ));

    // ========== HEAVEN TIER TECHNIQUES ==========

    // Body Path - Heaven Tier
    public static final TechniqueHolder CELESTIAL_BODY_TECHNIQUE = createTechnique("celestial_body_technique",
            ()->new SingleAttributeTechnique(
                    "Celestial Body Technique", 9.0, "ascension:heaven", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 25.0),
                            modifier(Attributes.ATTACK_DAMAGE, 4.0),
                            modifier(Attributes.ARMOR, 2.0),
                            modifier(Attributes.MOVEMENT_SPEED, 0.25),
                            modifier(Attributes.JUMP_STRENGTH, 0.15),
                            modifier(Attributes.STEP_HEIGHT, 0.15),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.5)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 45.0),
                            modifier(Attributes.ATTACK_DAMAGE, 9.0),
                            modifier(Attributes.ARMOR, 4.0),
                            modifier(Attributes.MOVEMENT_SPEED, 1.0),
                            modifier(Attributes.JUMP_STRENGTH, 0.4),
                            modifier(Attributes.STEP_HEIGHT, 0.8),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 3.0)
                        )
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:heaven", 2.8);
                        put("ascension:light", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§bHeaven Tier - Body Path\n")
                            .append("§6Body infused with celestial energy\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Absorb celestial energies at high altitudes\n")
                            .append("§e◆ §fPath: Divine → Celestial → Heavenly Form\n")
                            .append("§e◆ §fWeakness: Demonic techniques, defiance-based attacks")
                    ));

    // Essence Path - Heaven Tier
    public static final TechniqueHolder VOID_WALKER_TECHNIQUE = createTechnique("void_walker_technique",
            ()->new SingleElementTechnique(
                    "Void Walker Technique", "ascension:void", 10.0, new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 15.0),
                            modifier(Attributes.ATTACK_DAMAGE, 4.5),
                            modifier(Attributes.ARMOR, 0.5),
                            modifier(Attributes.MOVEMENT_SPEED, 0.3),
                            modifier(Attributes.JUMP_STRENGTH, 0.12),
                            modifier(Attributes.STEP_HEIGHT, 0.1),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.2)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 30.0),
                            modifier(Attributes.ATTACK_DAMAGE, 10.0),
                            modifier(Attributes.ARMOR, 1.5),
                            modifier(Attributes.MOVEMENT_SPEED, 1.2),
                            modifier(Attributes.JUMP_STRENGTH, 0.35),
                            modifier(Attributes.STEP_HEIGHT, 0.7),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 2.5)
                        )
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:void", 3.0);
                        put("ascension:space", 2.2);
                    }})
                    .setDescription(Component.empty()
                            .append("§bHeaven Tier - Essence Path\n")
                            .append("§8Born between realms, attuned to the void\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Meditate in spatial rifts\n")
                            .append("§e◆ §fPath: Teleport → Spatial Cut → Void God\n")
                            .append("§e◆ §fWeakness: Light-based techniques disrupt void")
                    ));

    // Intent Path - Heaven Tier
    public static final TechniqueHolder SWORD_SAINT_TECHNIQUE = createTechnique("sword_saint_technique",
            ()->new SingleIntentTechnique(
                    "Sword Saint Technique", 12.0, "ascension:sword_intent", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 16.0),
                            modifier(Attributes.ATTACK_DAMAGE, 6.0),
                            modifier(Attributes.ARMOR, 0.3),
                            modifier(Attributes.MOVEMENT_SPEED, 0.3),
                            modifier(Attributes.JUMP_STRENGTH, 0.1),
                            modifier(Attributes.STEP_HEIGHT, 0.15),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 1.0)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 32.0),
                            modifier(Attributes.ATTACK_DAMAGE, 15.0),
                            modifier(Attributes.ARMOR, 1.0),
                            modifier(Attributes.MOVEMENT_SPEED, 1.3),
                            modifier(Attributes.JUMP_STRENGTH, 0.6),
                            modifier(Attributes.STEP_HEIGHT, 1.0),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 3.5)
                        )
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:sword_intent", 3.5);
                        put("ascension:blade_intent", 3.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§bHeaven Tier - Intent Path\n")
                            .append("§8Every cell resonates with sword intent\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Comprehend deeper sword laws\n")
                            .append("§e◆ §fPath: Sword → Domain → Sword Saint\n")
                            .append("§e◆ §fWeakness: Completely focused on sword, lacks versatility")
                    ));

    // ========== ASCENSION TIER TECHNIQUES ==========

    // Essence Path - Ascension Tier
    public static final TechniqueHolder COSMIC_CREATION_ESSENCE_TECHNIQUE = createTechnique("cosmic_creation_essence_technique",
            ()->new SingleElementTechnique(
                    "Cosmic Creation Essence Technique", "ascension:cosmos", 18.0, new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 28.0),
                            modifier(Attributes.ATTACK_DAMAGE, 8.5),
                            modifier(Attributes.ARMOR, 2.2),
                            modifier(Attributes.MOVEMENT_SPEED, 0.45),
                            modifier(Attributes.JUMP_STRENGTH, 0.35),
                            modifier(Attributes.STEP_HEIGHT, 0.3),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 3.5)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 55.0),
                            modifier(Attributes.ATTACK_DAMAGE, 22.0),
                            modifier(Attributes.ARMOR, 4.5),
                            modifier(Attributes.MOVEMENT_SPEED, 2.2),
                            modifier(Attributes.JUMP_STRENGTH, 1.8),
                            modifier(Attributes.STEP_HEIGHT, 2.0),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 9.0)
                        )
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:cosmos", 2.0);
                        put("ascension:creation", 1.8);
                    }})
                    .setDescription(Component.empty()
                            .append("§dAscension Tier - Essence Path\n")
                            .append("§5Can create and destroy galaxies with a thought\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Create and nurture star systems\n")
                            .append("§e◆ §fPath: Star → Galaxy → Universe Creator\n")
                            .append("§e◆ §fWeakness: Absolute nothingness, anti-creation")
                    ));

    public static final TechniqueHolder INFINITE_TIME_ESSENCE_TECHNIQUE = createTechnique("infinite_time_essence_technique",
            ()->new SingleElementTechnique(
                    "Infinite Time Essence Technique", "ascension:time", 17.0, new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 25.0),
                            modifier(Attributes.ATTACK_DAMAGE, 7.8),
                            modifier(Attributes.ARMOR, 2.0),
                            modifier(Attributes.MOVEMENT_SPEED, 0.5),
                            modifier(Attributes.JUMP_STRENGTH, 0.4),
                            modifier(Attributes.STEP_HEIGHT, 0.35),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 4.0)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 50.0),
                            modifier(Attributes.ATTACK_DAMAGE, 20.0),
                            modifier(Attributes.ARMOR, 4.0),
                            modifier(Attributes.MOVEMENT_SPEED, 2.5),
                            modifier(Attributes.JUMP_STRENGTH, 2.0),
                            modifier(Attributes.STEP_HEIGHT, 2.2),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 10.0)
                        )
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:time", 2.0);
                        put("ascension:space", 1.5);
                    }})
                    .setDescription(Component.empty()
                            .append("§dAscension Tier - Essence Path\n")
                            .append("§5Can manipulate the flow of time and see all possibilities\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Experience and alter the timeline\n")
                            .append("§e◆ §fPath: Time → Eternity → Time Lord\n")
                            .append("§e◆ §fWeakness: Paradoxes, time locks, and fixed points")
                    ));

    // Body Path - Ascension Tier
    public static final TechniqueHolder PRIMORDIAL_CHAOS_BODY_TECHNIQUE = createTechnique("primordial_chaos_body_technique",
            ()->new SingleAttributeTechnique(
                    "Primordial Chaos Body Technique", 15.0, "ascension:chaos", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 30.0),
                            modifier(Attributes.ATTACK_DAMAGE, 7.0),
                            modifier(Attributes.ARMOR, 2.5),
                            modifier(Attributes.MOVEMENT_SPEED, 0.4),
                            modifier(Attributes.JUMP_STRENGTH, 0.2),
                            modifier(Attributes.STEP_HEIGHT, 0.2),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 2.0)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 60.0),
                            modifier(Attributes.ATTACK_DAMAGE, 18.0),
                            modifier(Attributes.ARMOR, 5.0),
                            modifier(Attributes.MOVEMENT_SPEED, 2.0),
                            modifier(Attributes.JUMP_STRENGTH, 1.0),
                            modifier(Attributes.STEP_HEIGHT, 1.5),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 5.0)
                        )
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:chaos", 3.8);
                        put("ascension:destruction", 3.5);
                    }})
                    .setDescription(Component.empty()
                            .append("§dAscension Tier - Body Path\n")
                            .append("§5Born from the primordial chaos before creation\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Create and destroy worlds\n")
                            .append("§e◆ §fPath: Chaos → Primordial → Reality Weaver\n")
                            .append("§e◆ §fWeakness: Order-based divine techniques")
                    ));

    // Body Path - Ascension Tier
    public static final TechniqueHolder DRAGON_KING_BODY_TECHNIQUE = createTechnique("dragon_king_body_technique",
            ()->new SingleAttributeTechnique(
                    "Dragon King Body Technique", 14.0, "ascension:dragon", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 35.0),
                            modifier(Attributes.ATTACK_DAMAGE, 8.0),
                            modifier(Attributes.ARMOR, 3.0),
                            modifier(Attributes.MOVEMENT_SPEED, 0.35),
                            modifier(Attributes.JUMP_STRENGTH, 0.25),
                            modifier(Attributes.STEP_HEIGHT, 0.25),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 2.5)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 65.0),
                            modifier(Attributes.ATTACK_DAMAGE, 20.0),
                            modifier(Attributes.ARMOR, 6.0),
                            modifier(Attributes.MOVEMENT_SPEED, 1.8),
                            modifier(Attributes.JUMP_STRENGTH, 1.2),
                            modifier(Attributes.STEP_HEIGHT, 1.8),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 6.0)
                        )
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:dragon", 3.6);
                        put("ascension:earth", 3.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§dAscension Tier - Body Path\n")
                            .append("§5Possesses ancient dragon bloodline\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Absorb essence of dragon remains\n")
                            .append("§e◆ §fPath: Scale → Roar → Dragon God\n")
                            .append("§e◆ §fWeakness: Dragon-slaying techniques")
                    ));

    // Intent Path - Ascension Tier
    public static final TechniqueHolder UNIVERSE_DEVOURER_INTENT_TECHNIQUE = createTechnique("universe_devourer_intent_technique",
            ()->new SingleIntentTechnique(
                    "Universe Devourer Intent Technique", 16.0, "ascension:devouring", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 25.0),
                            modifier(Attributes.ATTACK_DAMAGE, 9.0),
                            modifier(Attributes.ARMOR, 2.0),
                            modifier(Attributes.MOVEMENT_SPEED, 0.5),
                            modifier(Attributes.JUMP_STRENGTH, 0.3),
                            modifier(Attributes.STEP_HEIGHT, 0.3),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 3.0)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 50.0),
                            modifier(Attributes.ATTACK_DAMAGE, 25.0),
                            modifier(Attributes.ARMOR, 4.0),
                            modifier(Attributes.MOVEMENT_SPEED, 2.5),
                            modifier(Attributes.JUMP_STRENGTH, 1.5),
                            modifier(Attributes.STEP_HEIGHT, 2.0),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 8.0)
                        )
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:devouring", 4.0);
                        put("ascension:void", 3.8);
                    }})
                    .setDescription(Component.empty()
                            .append("§dAscension Tier - Intent Path\n")
                            .append("§0Can devour stars, worlds, and eventually universes\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Consume higher quality energy sources\n")
                            .append("§e◆ §fPath: Consumption → Black Hole → Universe Devourer\n")
                            .append("§e◆ §fWeakness: Divine seals and spatial locks")
                    ));

    // Intent Path - Ascension Tier
    public static final TechniqueHolder ETERNAL_REINCARNATION_INTENT_TECHNIQUE = createTechnique("eternal_reincarnation_intent_technique",
            ()->new SingleIntentTechnique(
                    "Eternal Reincarnation Intent Technique", 14.0, "ascension:reincarnation", new LnStabilityHandler(),
                    new StandardStatRealmChange(
                        List.of( //Minor Realm Stats
                            modifier(Attributes.MAX_HEALTH, 22.0),
                            modifier(Attributes.ATTACK_DAMAGE, 6.5),
                            modifier(Attributes.ARMOR, 1.8),
                            modifier(Attributes.MOVEMENT_SPEED, 0.4),
                            modifier(Attributes.JUMP_STRENGTH, 0.25),
                            modifier(Attributes.STEP_HEIGHT, 0.25),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 2.8)
                        ), List.of( //Major Realm Stats
                            modifier(Attributes.MAX_HEALTH, 45.0),
                            modifier(Attributes.ATTACK_DAMAGE, 18.0),
                            modifier(Attributes.ARMOR, 3.5),
                            modifier(Attributes.MOVEMENT_SPEED, 1.5),
                            modifier(Attributes.JUMP_STRENGTH, 1.0),
                            modifier(Attributes.STEP_HEIGHT, 1.2),
                            modifier(Attributes.SAFE_FALL_DISTANCE, 7.0)
                        )
                    ))
                    .setEfficiencyAttributes(new HashMap<>(){{
                        put("ascension:reincarnation", 3.8);
                        put("ascension:time", 3.5);
                    }})
                    .setDescription(Component.empty()
                            .append("§dAscension Tier - Intent Path\n")
                            .append("§3Has lived through countless lifetimes\n")
                            .append("\n")
                            .append("§e◆ §fGrowth: Awaken past life memories\n")
                            .append("§e◆ §fPath: Memory → Rebirth → Eternal Cycle\n")
                            .append("§e◆ §fWeakness: Karma accumulates from past lives")
                    ));

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