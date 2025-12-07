package net.thejadeproject.ascension.progression.physiques;

import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.realm_change_handlers.StandardStatRealmChange;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.skills.skill_lists.AcquirableSkillData;

import java.util.HashMap;
import java.util.List;

public class ModPhysiques {

    public static final DeferredRegister<IPhysique> PHYSIQUES =DeferredRegister.create(AscensionRegistries.Physiques.PHSIQUES_REGISTRY, AscensionCraft.MOD_ID);

    public static HashMap<Holder<Attribute>,Double> GENERIC_MINOR_REALM_STATS_1 = new HashMap<>(){{
        put(Attributes.MAX_HEALTH,5.0);
        put(Attributes.ATTACK_DAMAGE,1.0);
        put(Attributes.MOVEMENT_SPEED,0.01);
        put(Attributes.JUMP_STRENGTH,0.01);
        put(Attributes.STEP_HEIGHT,0.01);

    }};
    public static HashMap<Holder<Attribute>,Double> ENHANCED_MINOR_REALM_STATS_1 = new HashMap<>(){{
        put(Attributes.MAX_HEALTH,10.0);
        put(Attributes.ATTACK_DAMAGE,1.8);
        put(Attributes.MOVEMENT_SPEED,0.2);
        put(Attributes.JUMP_STRENGTH,0.04);
        put(Attributes.STEP_HEIGHT,0.04);

    }};

    public static HashMap<Holder<Attribute>,Double> GENERIC_MAJOR_REALM_STATS_1 = new HashMap<>(){{
        put(Attributes.MAX_HEALTH,10.0);
        put(Attributes.ATTACK_DAMAGE,2.0);
        put(Attributes.MOVEMENT_SPEED,0.5);
        put(Attributes.JUMP_STRENGTH,0.02);
        put(Attributes.STEP_HEIGHT,0.02);

    }};
    public static HashMap<Holder<Attribute>,Double> ENHANCED_MAJOR_REALM_STATS_1 = new HashMap<>(){{
        put(Attributes.MAX_HEALTH,10.0);
        put(Attributes.ATTACK_DAMAGE,1.8);
        put(Attributes.MOVEMENT_SPEED,0.2);
        put(Attributes.JUMP_STRENGTH,0.04);
        put(Attributes.STEP_HEIGHT,0.04);

    }};

    public static HashMap<Holder<Attribute>,Double> STONE_MONKEY_MINOR_REALM_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 20.0);  // High HP
        put(Attributes.ATTACK_DAMAGE, 2.5); // High damage
        put(Attributes.MOVEMENT_SPEED, 0.01); // Slow base, but compensated by abilities
        put(Attributes.JUMP_STRENGTH, 0.1); // Better jump
        put(Attributes.KNOCKBACK_RESISTANCE, 0.5); // Can't be knocked back easily
        put(Attributes.STEP_HEIGHT, 0.02);
    }};

    public static HashMap<Holder<Attribute>,Double> STONE_MONKEY_MAJOR_REALM_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 30.0);
        put(Attributes.ATTACK_DAMAGE, 4.0);
        put(Attributes.MOVEMENT_SPEED, 0.02);
        put(Attributes.JUMP_STRENGTH, 0.2);
        put(Attributes.KNOCKBACK_RESISTANCE, 1.0); // Complete knockback immunity at high realms
        put(Attributes.STEP_HEIGHT, 0.1);
    }};


    public static final DeferredHolder<IPhysique,GenericPhysique> HEAVENBORN_STONE_MONKEY_PHYSIQUE = PHYSIQUES.register("heavenborn_stone_monkey_physique",
            ()-> new GenericPhysique("Heaven-Born Stone Monkey Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 0.6);    // High intent for defiance
                        put("ascension:body", 1.5);      // Very high body focus (primary)
                        put("ascension:essence", 0.1);   // Lower essence focus
                    }},
                    new HashMap<>(){{
                        put("ascension:earth", 3.0);         // Stone origin
                        put("ascension:yang", 2.5);          // Active, aggressive nature
                        put("ascension:chaos", 2.0);         // Chaotic, heaven-defying
                        put("ascension:defiance", 2.0);      // Rebel against authority
                        put("ascension:fist_intent", 1.8);   // Prefers unarmed combat
                        put("ascension:earthshatter", 1.5);  // Breaking power
                        put("ascension:transformation", 1.0); // 72 Transformations
                    }},
                    new StandardStatRealmChange(STONE_MONKEY_MINOR_REALM_STATS, STONE_MONKEY_MAJOR_REALM_STATS))
                    .setSkillList(List.of(
                            new AcquirableSkillData("ascension:body", 5, 1, "ascension:stonehide_passive", true),

                            new AcquirableSkillData("ascension:body", 2, 2, "ascension:diamond_adamant_passive", true),

                            new AcquirableSkillData("ascension:body", 4, 5, "ascension:indestructible_vajra_active", false)
                    ))
                    .setDescription(List.of(
                            Component.literal("§6Born from chaos, defying heaven's will."),
                            Component.literal("§7A physique that grows through conflict and rebellion."),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Survive what should break you"),
                            Component.literal("§e◆ §fPath: Conflict → Resilience → Defiance"),
                            Component.literal("§e◆ §fWeakness: Susceptible to order-based attacks")
                    ))
    );







    public static final DeferredHolder<IPhysique,GenericPhysique> EMPTY_VESSEL = PHYSIQUES.register("empty_vessel",
            ()-> new GenericPhysique("Empty Vessel",
                    new HashMap<>(){{
                        put("ascension:intent",0.1);
                        put("ascension:body",0.1);
                        put("ascension:essence",0.1);
                    }},
                    new HashMap<>(), new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
    );

    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_SWORD_BODY = PHYSIQUES.register("pure_sword_body",
            ()-> new GenericPhysique("Pure Sword Body",
                    new HashMap<>(){{
                        put("ascension:intent",1.0);
                        put("ascension:body",0.4);
                        put("ascension:essence",0.1);
                    }},
                    new HashMap<>(){{
                        put("ascension:sword_intent",3.0);
                    }}, new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
            );
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_SPEAR_BODY = PHYSIQUES.register("pure_spear_body",
            ()-> new GenericPhysique("Pure Spear Body",
                    new HashMap<>(){{
                        put("ascension:intent",1.0);
                        put("ascension:body",0.4);
                        put("ascension:essence",0.1);
                    }},
                    new HashMap<>(){{
                        put("ascension:spear_intent",3.0);
                    }}, new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
    );
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_AXE_BODY = PHYSIQUES.register("pure_axe_body",
            ()-> new GenericPhysique("Pure Axe Body",
                    new HashMap<>(){{
                        put("ascension:intent",1.0);
                        put("ascension:body",0.4);
                        put("ascension:essence",0.1);
                    }},
                    new HashMap<>(){{
                        put("ascension:axe_intent",3.0);
                    }}, new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
    );
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_BLADE_BODY = PHYSIQUES.register("pure_blade_body",
            ()-> new GenericPhysique("Pure Blade Body",
                    new HashMap<>(){{
                        put("ascension:intent",1.0);
                        put("ascension:body",0.4);
                        put("ascension:essence",0.1);
                    }},
                    new HashMap<>(){{
                        put("ascension:blade_intent",3.0);
                    }}, new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
    );
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_FIST_BODY = PHYSIQUES.register("pure_fist_body",
            ()-> new GenericPhysique("Pure Fist Body",
                    new HashMap<>(){{
                        put("ascension:intent",1.0);
                        put("ascension:body",0.4);
                        put("ascension:essence",0.1);
                    }},
                    new HashMap<>(){{
                        put("ascension:fist_intent",3.0);
                    }}, new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1)).setSkillList(List.of(
                            new AcquirableSkillData("ascension:intent",0,0,"ascension:fist_aura_skill",true)
            )).setDescription(List.of( Component.literal("my fist is my body and my body is my fist")))
    );

    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_WATER_BODY = PHYSIQUES.register("pure_water_body",
            ()-> new GenericPhysique("Pure Water Body",
                    new HashMap<>(){{
                        put("ascension:intent",0.4);
                        put("ascension:body",0.1);
                        put("ascension:essence",1.0);
                    }},
                    new HashMap<>(){{
                        put("ascension:water",3.0);
                    }}, new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1)).setPhysiqueCard(new TextureDataSubSection(
                    ResourceLocation.fromNamespaceAndPath(
                            AscensionCraft.MOD_ID,
                            "textures/physiques/root_cards.png"
                    ),
                    512,
                    512,
                    340,
                    0,
                    340+78,104
            ))
    );
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_EARTH_BODY = PHYSIQUES.register("pure_earth_body",
            ()-> new GenericPhysique("Pure Earth Body",
                    new HashMap<>(){{
                        put("ascension:intent",0.4);
                        put("ascension:body",0.1);
                        put("ascension:essence",1.0);
                    }},
                    new HashMap<>(){{
                        put("ascension:earth",3.0);
                    }}, new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1)).setPhysiqueCard(new TextureDataSubSection(
                    ResourceLocation.fromNamespaceAndPath(
                            AscensionCraft.MOD_ID,
                            "textures/physiques/root_cards.png"
                    ),
                    512,
                    512,
                    0,
                    0,
                    78,104
            ))
    );
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_FIRE_BODY = PHYSIQUES.register("pure_fire_body",
            ()-> new GenericPhysique("Pure Fire Body",
                    new HashMap<>(){{
                        put("ascension:intent",0.4);
                        put("ascension:body",0.1);
                        put("ascension:essence",1.0);
                    }},
                    new HashMap<>(){{
                        put("ascension:fire",3.0);
                    }}, new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1)).setPhysiqueCard(new TextureDataSubSection(
                    ResourceLocation.fromNamespaceAndPath(
                            AscensionCraft.MOD_ID,
                            "textures/physiques/root_cards.png"
                    ),
                    512,
                    512,
                    170,
                    0,
                    170+78,104
            ))
    );
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_WOOD_BODY = PHYSIQUES.register("pure_wood_body",
            ()-> new GenericPhysique("Pure Wood Body",
                    new HashMap<>(){{
                        put("ascension:intent",0.4);
                        put("ascension:body",0.1);
                        put("ascension:essence",1.0);
                    }},
                    new HashMap<>(){{
                        put("ascension:wood",3.0);
                    }}, new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1)).setPhysiqueCard(new TextureDataSubSection(
                    ResourceLocation.fromNamespaceAndPath(
                            AscensionCraft.MOD_ID,
                            "textures/physiques/root_cards.png"
                    ),
                    512,
                    512,
                    255,
                    0,
                    255+78,104
            ))
    );
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_METAL_BODY = PHYSIQUES.register("pure_metal_body",
            ()-> new GenericPhysique("Pure Metal Body",
                    new HashMap<>(){{
                        put("ascension:intent",0.4);
                        put("ascension:body",0.1);
                        put("ascension:essence",1.0);
                    }},
                    new HashMap<>(){{
                        put("ascension:metal",3.0);
                    }}, new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1)).setPhysiqueCard(new TextureDataSubSection(
                    ResourceLocation.fromNamespaceAndPath(
                            AscensionCraft.MOD_ID,
                            "textures/physiques/root_cards.png"
                    ),
                    512,
                    512,
                    85,
                    0,
                    85+78,104
            ))
    );

    public static final DeferredHolder<IPhysique,GenericPhysique> IRON_BONE_PHYSIQUE = PHYSIQUES.register("iron_bone_physique",
            ()-> new GenericPhysique("Iron Bone Physique",
                    new HashMap<>(){{
                        put("ascension:intent",0.1);
                        put("ascension:body",1.0);
                        put("ascension:essence",0.4);
                    }},
                    new HashMap<>(){{
                        put("ascension:metal",1.2);
                    }}, new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
                    .setSkillList( List.of(
                            new AcquirableSkillData("ascension:body",0,0,"ascension:iron_bones_passive_skill",true)
                    ))
    );

    public static final DeferredHolder<IPhysique,GenericPhysique> BURNING_SKIN_PHYSIQUE = PHYSIQUES.register("burning_skin_physique",
            ()-> new GenericPhysique("Burning Skin Physique",
                    new HashMap<>(){{
                        put("ascension:intent",0.1);
                        put("ascension:body",1.0);
                        put("ascension:essence",0.4);
                    }},
                    new HashMap<>(){{
                        put("ascension:fire",1.2);
                    }}, new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
    );
    public static final DeferredHolder<IPhysique,GenericPhysique> HUNDRED_POISON_PHYSIQUE = PHYSIQUES.register("hundred_poison_physique",
            ()-> new GenericPhysique("Hundred Poison Physique",
                    new HashMap<>(){{
                        put("ascension:intent",0.1);
                        put("ascension:body",1.0);
                        put("ascension:essence",0.4);
                    }},
                    new HashMap<>(){{
                        put("ascension:poison",1.2);
                    }}, new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
    );

    public static final DeferredHolder<IPhysique,GenericPhysique> SACRED_SAPLING_PHYSIQUE = PHYSIQUES.register("sacred_sapling_physique",
            ()-> new GenericPhysique("Sacred Sapling Physique",
                    new HashMap<>(){{
                        put("ascension:intent",0.1);
                        put("ascension:body",1.0);
                        put("ascension:essence",0.4);
                    }},
                    new HashMap<>(){{
                        put("ascension:wood",1.2);
                    }}, new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
    );

    public static final DeferredHolder<IPhysique,GenericPhysique> SUPPRESSED_YIN_PHYSIQUE = PHYSIQUES.register("suppressed_yin_physique",
            ()-> new GenericPhysique("Suppressed Yin Physique",
                    new HashMap<>(){{
                        put("ascension:intent",0.1);
                        put("ascension:body",1.0);
                        put("ascension:essence",0.4);
                    }},
                    new HashMap<>(){{
                        put("ascension:yin",1.2);
                        put("ascension:ice",1.2);

                    }}, new StandardStatRealmChange(GENERIC_MINOR_REALM_STATS_1,GENERIC_MAJOR_REALM_STATS_1))
    );








    public static void register(IEventBus modEventBus){
        PHYSIQUES.register(modEventBus);
    }
}
