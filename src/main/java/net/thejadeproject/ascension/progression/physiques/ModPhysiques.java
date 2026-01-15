package net.thejadeproject.ascension.progression.physiques;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.skills.skill_lists.AcquirableSkillData;

import java.util.HashMap;
import java.util.List;

public class ModPhysiques {

    public static final DeferredRegister<IPhysique> PHYSIQUES =DeferredRegister.create(AscensionRegistries.Physiques.PHSIQUES_REGISTRY, AscensionCraft.MOD_ID);

    // ========== HUMAN TIER PHYSIQUES ==========

    // 1. Stone Skin Physique (Human Tier) - Balanced defense
    public static final DeferredHolder<IPhysique,GenericPhysique> STONE_SKIN_PHYSIQUE = PHYSIQUES.register("stone_skin_physique",
            ()-> new GenericPhysique("Stone Skin Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 0.3);
                        put("ascension:body", 1.0);
                        put("ascension:essence", 0.2);
                    }},
                    new HashMap<>(){{
                        put("ascension:earth", 1.5);
                        put("ascension:metal", 1.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§8Skin hardens like stone, providing natural armor\n\n")
                            .append("§e◆ §fGrowth: Take heavy blows without breaking\n")
                            .append("§e◆ §fPath: Endurance → Resilience → Immovability\n")
                            .append("§e◆ §fWeakness: Slow movement, vulnerable to water-based techniques")
                    )
    );

    // 2. Swift Wind Physique (Human Tier) - Speed focused
    public static final DeferredHolder<IPhysique,GenericPhysique> SWIFT_WIND_PHYSIQUE = PHYSIQUES.register("swift_wind_physique",
            ()-> new GenericPhysique("Swift Wind Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 0.8);
                        put("ascension:body", 0.3);
                        put("ascension:essence", 0.4);
                    }},
                    new HashMap<>(){{
                        put("ascension:wind", 1.8);
                        put("ascension:yang", 1.2);
                    }})
                    .setDescription(Component.empty()
                            .append("§8Body harmonizes with wind, granting incredible speed\n\n")
                            .append("§e◆ §fGrowth: Move faster than the eye can see\n")
                            .append("§e◆ §fPath: Speed → Precision → Afterimages\n")
                            .append("§e◆ §fWeakness: Fragile body, low defense")
                    )
    );

    // 3. Iron Marrow Physique (Human Tier) - Internal strength
    public static final DeferredHolder<IPhysique,GenericPhysique> IRON_MARROW_PHYSIQUE = PHYSIQUES.register("iron_marrow_physique",
            ()-> new GenericPhysique("Iron Marrow Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 0.2);
                        put("ascension:body", 0.9);
                        put("ascension:essence", 0.4);
                    }},
                    new HashMap<>(){{
                        put("ascension:metal", 1.6);
                        put("ascension:earth", 1.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§8Marrow turns to iron, strengthening bones from within\n\n")
                            .append("§e◆ §fGrowth: Refine bone marrow through combat\n")
                            .append("§e◆ §fPath: Endurance → Internal Strength → Unbreakable Bones\n")
                            .append("§e◆ §fWeakness: Heavy, limited agility")
                    )
    );

// ========== EARTH TIER PHYSIQUES ==========

    // 4. Jade Bone Physique (Earth Tier) - Defensive with luck
    public static final DeferredHolder<IPhysique,GenericPhysique> JADE_BONE_PHYSIQUE = PHYSIQUES.register("jade_bone_physique",
            ()-> new GenericPhysique("Jade Bone Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 0.4);
                        put("ascension:body", 1.2);
                        put("ascension:essence", 0.6);
                    }},
                    new HashMap<>(){{
                        put("ascension:earth", 1.8);
                        put("ascension:wood", 1.3);
                        put("ascension:life", 1.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§aBones transform into jade, radiating life energy\n\n")
                            .append("§e◆ §fGrowth: Absorb earth essence to strengthen bones\n")
                            .append("§e◆ §fPath: Defense → Regeneration → Jade Immortality\n")
                            .append("§e◆ §fWeakness: Vulnerable to metal-based attacks\n")
                    )
    );

    // 5. Crystal Meridian Physique (Earth Tier) - Pure essence channeling
    public static final DeferredHolder<IPhysique,GenericPhysique> CRYSTAL_MERIDIAN_PHYSIQUE = PHYSIQUES.register("crystal_meridian_physique",
            ()-> new GenericPhysique("Crystal Meridian Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 0.5);
                        put("ascension:body", 0.4);
                        put("ascension:essence", 1.3);
                    }},
                    new HashMap<>(){{
                        put("ascension:void", 1.5);
                        put("ascension:light", 1.2);
                        put("ascension:ice", 1.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§dMeridians crystallize, allowing pure essence flow\n\n")
                            .append("§e◆ §fGrowth: Purify essence through meditation\n")
                            .append("§e◆ §fPath: Clarity → Reflection → Crystalline Perfection\n")
                            .append("§e◆ §fWeakness: Brittle, shatters under extreme force"
                            ))
    );

    // 6. Thunderclap Physique (Earth Tier) - Lightning speed and power
    public static final DeferredHolder<IPhysique,GenericPhysique> THUNDERCLAP_PHYSIQUE = PHYSIQUES.register("thunderclap_physique",
            ()-> new GenericPhysique("Thunderclap Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 1.0);
                        put("ascension:body", 0.7);
                        put("ascension:essence", 0.5);
                    }},
                    new HashMap<>(){{
                        put("ascension:lightning", 2.0);
                        put("ascension:storm", 1.5);
                        put("ascension:yang", 1.3);
                    }})
                    .setDescription(Component.empty()
                            .append("§eBody resonates with thunder, strikes with lightning speed\n\n")
                            .append("§e◆ §fGrowth: Cultivate during thunderstorms\n")
                            .append("§e◆ §fPath: Speed → Power → Lightning Embodiment\n")
                            .append("§e◆ §fWeakness: Conducts electricity, weak to grounding")
                    )
    );

    // 7. Blood Jade Physique (Earth Tier) - Life and vitality
    public static final DeferredHolder<IPhysique,GenericPhysique> BLOOD_JADE_PHYSIQUE = PHYSIQUES.register("blood_jade_physique",
            ()-> new GenericPhysique("Blood Jade Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 0.6);
                        put("ascension:body", 0.9);
                        put("ascension:essence", 0.7);
                    }},
                    new HashMap<>(){{
                        put("ascension:blood", 1.8);
                        put("ascension:life", 1.5);
                        put("ascension:fire", 1.2);
                    }})
                    .setDescription(Component.empty()
                            .append("§cBlood contains jade particles, granting life force\n\n")
                            .append("§e◆ §fGrowth: Consume life essence from foes\n")
                            .append("§e◆ §fPath: Vitality → Absorption → Immortal Blood\n")
                            .append("§e◆ §fWeakness: Bleeds easily, weak to purification"
                            ))
    );

// ========== HEAVEN TIER PHYSIQUES ==========

    // 8. Heavenly Lightning Physique (Heaven Tier)
    public static final DeferredHolder<IPhysique,GenericPhysique> HEAVENLY_LIGHTNING_PHYSIQUE = PHYSIQUES.register("heavenly_lightning_physique",
            ()-> new GenericPhysique("Heavenly Lightning Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 1.2);
                        put("ascension:body", 0.8);
                        put("ascension:essence", 0.7);
                    }},
                    new HashMap<>(){{
                        put("ascension:lightning", 2.5);
                        put("ascension:heaven", 2.0);
                        put("ascension:storm", 1.8);
                        put("ascension:yang", 1.5);
                    }})
                    .setDescription(Component.empty()
                            .append("§6Born with the blessing of thunder gods\n")
                            .append("§eCan call upon heavenly tribulation lightning\n\n")
                            .append("§e◆ §fGrowth: Survive heavenly tribulations\n")
                            .append("§e◆ §fPath: Thunder → Tribulation → Heavenly Wrath\n")
                            .append("§e◆ §fWeakness: Water conducts own lightning back")
                    )
    );

    // 9. Phoenix Nirvana Physique (Heaven Tier)
    public static final DeferredHolder<IPhysique,GenericPhysique> PHOENIX_NIRVANA_PHYSIQUE = PHYSIQUES.register("phoenix_nirvana_physique",
            ()-> new GenericPhysique("Phoenix Nirvana Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 0.7);
                        put("ascension:body", 1.1);
                        put("ascension:essence", 1.3);
                    }},
                    new HashMap<>(){{
                        put("ascension:fire", 2.3);
                        put("ascension:reincarnation", 2.0);
                        put("ascension:life", 1.8);
                        put("ascension:yang", 1.5);
                    }})
                    .setDescription(Component.empty()
                            .append("§cCarries the bloodline of the immortal phoenix\n")
                            .append("§6Can be reborn from ashes, stronger each time\n\n")
                            .append("§e◆ §fGrowth: Die and be reborn through fire\n")
                            .append("§e◆ §fPath: Fire → Rebirth → Phoenix God\n")
                            .append("§e◆ §fWeakness: Extreme cold stops rebirth cycle")
                    )
    );

    // 10. Dragon Vein Physique (Heaven Tier)
    public static final DeferredHolder<IPhysique,GenericPhysique> DRAGON_VEIN_PHYSIQUE = PHYSIQUES.register("dragon_vein_physique",
            ()-> new GenericPhysique("Dragon Vein Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 0.9);
                        put("ascension:body", 1.4);
                        put("ascension:essence", 0.8);
                    }},
                    new HashMap<>(){{
                        put("ascension:dragon", 2.4);
                        put("ascension:earth", 2.0);
                        put("ascension:fire", 1.8);
                        put("ascension:water", 1.8);
                    }})
                    .setDescription(Component.empty()
                            .append("§5Possesses ancient dragon bloodline\n")
                            .append("§dVeins pulse with primordial dragon energy\n\n")
                            .append("§e◆ §fGrowth: Absorb essence of dragon remains\n")
                            .append("§e◆ §fPath: Scale → Roar → Dragon God\n")
                            .append("§e◆ §fWeakness: Dragon-slaying techniques")
                    )
    );

    // 11. Void Walker Physique (Heaven Tier)
    public static final DeferredHolder<IPhysique,GenericPhysique> VOID_WALKER_PHYSIQUE = PHYSIQUES.register("void_walker_physique",
            ()-> new GenericPhysique("Void Walker Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 1.3);
                        put("ascension:body", 0.4);
                        put("ascension:essence", 1.1);
                    }},
                    new HashMap<>(){{
                        put("ascension:void", 2.5);
                        put("ascension:space", 2.2);
                        put("ascension:yin", 1.8);
                        put("ascension:darkness", 1.5);
                    }})
                    .setDescription(Component.empty()
                            .append("§8Born between realms, attuned to the void\n")
                            .append("§7Can walk through space and vanish from existence\n\n")
                            .append("§e◆ §fGrowth: Meditate in spatial rifts\n")
                            .append("§e◆ §fPath: Teleport → Spatial Cut → Void God\n")
                            .append("§e◆ §fWeakness: Light-based techniques disrupt void")
                    )
    );

    // 12. Solar Flare Physique (Heaven Tier)
    public static final DeferredHolder<IPhysique,GenericPhysique> SOLAR_FLARE_PHYSIQUE = PHYSIQUES.register("solar_flare_physique",
            ()-> new GenericPhysique("Solar Flare Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 1.1);
                        put("ascension:body", 0.9);
                        put("ascension:essence", 0.8);
                    }},
                    new HashMap<>(){{
                        put("ascension:sun", 2.4);
                        put("ascension:fire", 2.1);
                        put("ascension:light", 2.0);
                        put("ascension:yang", 1.9);
                    }})
                    .setDescription(Component.empty()
                            .append("§eBody contains a miniature sun within dantian\n")
                            .append("§6Can unleash solar flares of immense power\n\n")
                            .append("§e◆ §fGrowth: Absorb sunlight at noon\n")
                            .append("§e◆ §fPath: Heat → Light → Solar Deity\n")
                            .append("§e◆ §fWeakness: Night weakens power, lunar attacks")
                    )
    );

    // 13. Lunar Shadow Physique (Heaven Tier)
    public static final DeferredHolder<IPhysique,GenericPhysique> LUNAR_SHADOW_PHYSIQUE = PHYSIQUES.register("lunar_shadow_physique",
            ()-> new GenericPhysique("Lunar Shadow Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 1.2);
                        put("ascension:body", 0.5);
                        put("ascension:essence", 1.4);
                    }},
                    new HashMap<>(){{
                        put("ascension:moon", 2.5);
                        put("ascension:yin", 2.3);
                        put("ascension:shadow", 2.1);
                        put("ascension:ice", 1.8);
                    }})
                    .setDescription(Component.empty()
                            .append("§bBlessed by the moon goddess\n")
                            .append("§3Can manipulate shadows and moonlight\n\n")
                            .append("§e◆ §fGrowth: Cultivate under full moon\n")
                            .append("§e◆ §fPath: Shadow → Reflection → Lunar Deity\n")
                            .append("§e◆ §fWeakness: Sunlight weakens abilities")
                    )
    );

    // 14. Soul Anchor Physique (Heaven Tier)
    public static final DeferredHolder<IPhysique,GenericPhysique> SOUL_ANCHOR_PHYSIQUE = PHYSIQUES.register("soul_anchor_physique",
            ()-> new GenericPhysique("Soul Anchor Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 0.8);
                        put("ascension:body", 1.3);
                        put("ascension:essence", 1.2);
                    }},
                    new HashMap<>(){{
                        put("ascension:soul", 2.4);
                        put("ascension:life", 2.1);
                        put("ascension:spirit", 2.0);
                        put("ascension:reincarnation", 1.8);
                    }})
                    .setDescription(Component.empty()
                            .append("§dSoul is anchored to multiple existences\n")
                            .append("§5Can survive soul destruction and re-anchor\n\n")
                            .append("§e◆ §fGrowth: Survive soul-damaging attacks\n")
                            .append("§e◆ §fPath: Stability → Anchoring → Eternal Soul\n")
                            .append("§e◆ §fWeakness: Soul-severing techniques")
                    )
    );

// ========== ASCENSION TIER PHYSIQUES ==========

    // 15. Heavenly Demon Physique (Ascension Tier)
    public static final DeferredHolder<IPhysique,GenericPhysique> HEAVENLY_DEMON_PHYSIQUE = PHYSIQUES.register("heavenly_demon_physique",
            ()-> new GenericPhysique("Heavenly Demon Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 1.4);
                        put("ascension:body", 1.5);
                        put("ascension:essence", 0.9);
                    }},
                    new HashMap<>(){{
                        put("ascension:chaos", 2.8);
                        put("ascension:demonic", 2.5);
                        put("ascension:blood", 2.3);
                        put("ascension:fire", 2.0);
                        put("ascension:defiance", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§4Born from the union of heavenly and demonic energies\n")
                            .append("§cCan defy heaven itself, cultivating through slaughter\n\n")
                            .append("§e◆ §fGrowth: Kill powerful beings and absorb their essence\n")
                            .append("§e◆ §fPath: Slaughter → Demonic Energy → Heavenly Demon\n")
                            .append("§e◆ §fWeakness: Heavenly tribulations are 10x stronger")
                    )
    );

    // 16. Cosmic Yin-Yang Physique (Ascension Tier)
    public static final DeferredHolder<IPhysique,GenericPhysique> COSMIC_YINYANG_PHYSIQUE = PHYSIQUES.register("cosmic_yinyang_physique",
            ()-> new GenericPhysique("Cosmic Yin-Yang Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 1.1);
                        put("ascension:body", 1.1);
                        put("ascension:essence", 1.1);
                    }},
                    new HashMap<>(){{
                        put("ascension:yin", 2.6);
                        put("ascension:yang", 2.6);
                        put("ascension:cosmos", 2.4);
                        put("ascension:balance", 2.3);
                        put("ascension:void", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§fEmbodies the perfect balance of cosmic forces\n")
                            .append("§7Can manipulate both creation and destruction\n\n")
                            .append("§e◆ §fGrowth: Balance opposing forces within\n")
                            .append("§e◆ §fPath: Harmony → Balance → Cosmic Unity\n")
                            .append("§e◆ §fWeakness: Chaos disrupts the balance")
                    )
    );

    // 17. Primordial Chaos Physique (Ascension Tier)
    public static final DeferredHolder<IPhysique,GenericPhysique> PRIMORDIAL_CHAOS_PHYSIQUE = PHYSIQUES.register("primordial_chaos_physique",
            ()-> new GenericPhysique("Primordial Chaos Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 1.5);
                        put("ascension:body", 1.0);
                        put("ascension:essence", 1.2);
                    }},
                    new HashMap<>(){{
                        put("ascension:chaos", 3.0);
                        put("ascension:primordial", 2.8);
                        put("ascension:void", 2.5);
                        put("ascension:creation", 2.0);
                        put("ascension:destruction", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§5Born from the primordial chaos before creation\n")
                            .append("§dCan manipulate the fabric of reality itself\n\n")
                            .append("§e◆ §fGrowth: Create and destroy worlds\n")
                            .append("§e◆ §fPath: Chaos → Primordial → Reality Weaver\n")
                            .append("§e◆ §fWeakness: Order-based divine techniques")
                    )
    );

    // 18. Eternal Reincarnation Physique (Ascension Tier)
    public static final DeferredHolder<IPhysique,GenericPhysique> ETERNAL_REINCARNATION_PHYSIQUE = PHYSIQUES.register("eternal_reincarnation_physique",
            ()-> new GenericPhysique("Eternal Reincarnation Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 0.9);
                        put("ascension:body", 1.2);
                        put("ascension:essence", 1.6);
                    }},
                    new HashMap<>(){{
                        put("ascension:reincarnation", 3.0);
                        put("ascension:time", 2.7);
                        put("ascension:soul", 2.5);
                        put("ascension:life", 2.3);
                        put("ascension:memory", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§3Has lived through countless lifetimes\n")
                            .append("§bRemembers all past lives and their skills\n\n")
                            .append("§e◆ §fGrowth: Awaken past life memories\n")
                            .append("§e◆ §fPath: Memory → Rebirth → Eternal Cycle\n")
                            .append("§e◆ §fWeakness: Karma accumulates from past lives")
                    )
    );

    // 19. Sword Saint Physique (Ascension Tier)
    public static final DeferredHolder<IPhysique,GenericPhysique> SWORD_SAINT_PHYSIQUE = PHYSIQUES.register("sword_saint_physique",
            ()-> new GenericPhysique("Sword Saint Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 1.8);
                        put("ascension:body", 0.9);
                        put("ascension:essence", 0.5);
                    }},
                    new HashMap<>(){{
                        put("ascension:sword_intent", 3.0);
                        put("ascension:blade_intent", 2.8);
                        put("ascension:sharpness", 2.5);
                        put("ascension:cutting", 2.5);
                        put("ascension:metal", 2.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§8Every cell resonates with sword intent\n")
                            .append("§7Can cut through space, time, and concepts\n\n")
                            .append("§e◆ §fGrowth: Comprehend deeper sword laws\n")
                            .append("§e◆ §fPath: Sword → Domain → Sword Saint\n")
                            .append("§e◆ §fWeakness: Completely focused on sword, lacks versatility")
                    )
    );

    // 20. Universe Devourer Physique (Ascension Tier)
    public static final DeferredHolder<IPhysique,GenericPhysique> UNIVERSE_DEVOURER_PHYSIQUE = PHYSIQUES.register("universe_devourer_physique",
            ()-> new GenericPhysique("Universe Devourer Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 1.3);
                        put("ascension:body", 1.7);
                        put("ascension:essence", 1.5);
                    }},
                    new HashMap<>(){{
                        put("ascension:devouring", 3.2);
                        put("ascension:void", 3.0);
                        put("ascension:chaos", 2.8);
                        put("ascension:space", 2.6);
                        put("ascension:time", 2.5);
                    }})
                    .setDescription(Component.empty()
                            .append("§0Can devour stars, worlds, and eventually universes\n")
                            .append("§8Grows stronger by consuming everything\n\n")
                            .append("§e◆ §fGrowth: Consume higher quality energy sources\n")
                            .append("§e◆ §fPath: Consumption → Black Hole → Universe Devourer\n")
                            .append("§e◆ §fWeakness: Divine seals and spatial locks")
                    )
    );

    // ========== ORIGINAL PHYSIQUES UPDATED WITH TIERS ==========

    // Ascension Tier - Stone Monkey (updated from Heavenborn Stone Monkey)
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
                    }})
                    .setSkillList(List.of(
                            new AcquirableSkillData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"stonehide_passive"),false,false),
                            new AcquirableSkillData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"diamond_adamant_passive"),false,false),
                            new AcquirableSkillData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"indestructible_vajra_active"),false,false)
                    ))
                    .setDescription(
                            Component.empty()
                                    .append("§6Born from chaos, defying heaven's will.\n")
                                    .append("§7A physique that grows through conflict and rebellion.\n\n")
                                    .append("§e◆ §fGrowth: Survive what should break you\n")
                                    .append("§e◆ §fPath: Conflict → Resilience → Defiance\n")
                                    .append("§e◆ §fWeakness: Susceptible to order-based attacks")
                    )
    );

    // Ascension Tier - Nine-Tailed Kitsune
    public static final DeferredHolder<IPhysique,GenericPhysique> NINE_TAILED_KITSUNE_PHYSIQUE = PHYSIQUES.register("nine_tailed_kitsune_physique",
            ()-> new GenericPhysique("Nine-Tailed Kitsune Physique",
                    new HashMap<>(){{
                        put("ascension:intent", 0.7);    // High intent for illusions
                        put("ascension:body", 0.6);      // Moderate body focus
                        put("ascension:essence", 1.2);   // High essence focus (fox nature)
                    }},
                    new HashMap<>(){{
                        put("ascension:yin", 3.0);           // Yin-aligned, moon affinity
                        put("ascension:fire", 1.5);          // Foxfire/divine flames
                    }})
                    .setSkillList(List.of(
                            new AcquirableSkillData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"kitsune_illusion_basic"),false,false),
                            new AcquirableSkillData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"foxfire_manipulation"),false,false),
                            new AcquirableSkillData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"tail_multiplier_passive"),false,false)
                    ))
                    .setDescription(Component.empty()
                            .append("§dBorn under the celestial moon, blessed by ancient fox spirits.\n")
                            .append("§7A physique that grows through wisdom, age, and spiritual cultivation.\n\n")
                            .append("§e◆ §fGrowth: Gain one tail per major realm starting from Realm 4\n")
                            .append("§e◆ §fPath: Wisdom → Illusion → Celestial Transcendence\n")
                            .append("§e◆ §fWeakness: Vulnerable to pure yang and purification techniques\n\n")
                            .append("§5[Tail Progression]\n")
                            .append("§7Realm 4-12: Gain +1 tail per realm (9 tails max)\n")
                            .append("§7Each tail grants bonus stats and unlocks abilities")
                    )
    );

    // Human Tier - Empty Vessel
    public static final DeferredHolder<IPhysique,GenericPhysique> EMPTY_VESSEL = PHYSIQUES.register("empty_vessel",
            ()-> new GenericPhysique("Empty Vessel",
                    new HashMap<>(){{
                        put("ascension:intent",0.1);
                        put("ascension:body",0.1);
                        put("ascension:essence",0.1);
                    }},
                    new HashMap<>())
                    .setDescription(Component.empty()
                            .append("§8No inherent advantages but no weaknesses either\n")
                            .append("§7Can be shaped by any cultivation path\n\n")
                            .append("§e◆ §fGrowth: Adapt to any cultivation method\n")
                            .append("§e◆ §fPath: Blank Slate → Flexibility → Any Specialization\n")
                            .append("§e◆ §fWeakness: No natural strengths, must work harder")
                    )
    );

    // Earth Tier - Pure Sword Body (updated)
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_SWORD_BODY = PHYSIQUES.register("pure_sword_body",
            ()-> new GenericPhysique("Pure Sword Body",
                    new HashMap<>(){{
                        put("ascension:intent",1.0);
                        put("ascension:body",0.4);
                        put("ascension:essence",0.1);
                    }},
                    new HashMap<>(){{
                        put("ascension:sword_intent",3.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§aBody resonates perfectly with sword intent\n")
                            .append("§7Naturally drawn to sword cultivation techniques\n\n")
                            .append("§e◆ §fGrowth: Master sword techniques faster\n")
                            .append("§e◆ §fPath: Sword Sense → Sword Intent → Sword Mastery\n")
                            .append("§e◆ §fWeakness: Weak against blunt force attacks")
                    )
    );

    // Earth Tier - Pure Spear Body (updated)
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_SPEAR_BODY = PHYSIQUES.register("pure_spear_body",
            ()-> new GenericPhysique("Pure Spear Body",
                    new HashMap<>(){{
                        put("ascension:intent",1.0);
                        put("ascension:body",0.4);
                        put("ascension:essence",0.1);
                    }},
                    new HashMap<>(){{
                        put("ascension:spear_intent",3.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§aBody resonates with spear techniques\n")
                            .append("§7Excels at thrusting and piercing attacks\n\n")
                            .append("§e◆ §fGrowth: Master spear techniques faster\n")
                            .append("§e◆ §fPath: Piercing → Thrusting → Spear Mastery\n")
                            .append("§e◆ §fWeakness: Vulnerable at close range")
                    )
    );

    // Earth Tier - Pure Axe Body (updated)
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_AXE_BODY = PHYSIQUES.register("pure_axe_body",
            ()-> new GenericPhysique("Pure Axe Body",
                    new HashMap<>(){{
                        put("ascension:intent",1.0);
                        put("ascension:body",0.4);
                        put("ascension:essence",0.1);
                    }},
                    new HashMap<>(){{
                        put("ascension:axe_intent",3.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§aBody resonates with axe techniques\n")
                            .append("§7Excels at powerful cleaving attacks\n\n")
                            .append("§e◆ §fGrowth: Master axe techniques faster\n")
                            .append("§e◆ §fPath: Cleaving → Chopping → Axe Mastery\n")
                            .append("§e◆ §fWeakness: Slow attack speed")
                    )
    );

    // Earth Tier - Pure Blade Body (updated)
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_BLADE_BODY = PHYSIQUES.register("pure_blade_body",
            ()-> new GenericPhysique("Pure Blade Body",
                    new HashMap<>(){{
                        put("ascension:intent",1.0);
                        put("ascension:body",0.4);
                        put("ascension:essence",0.1);
                    }},
                    new HashMap<>(){{
                        put("ascension:blade_intent",3.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§aBody resonates with blade techniques\n")
                            .append("§7Excels at slashing and cutting attacks\n\n")
                            .append("§e◆ §fGrowth: Master blade techniques faster\n")
                            .append("§e◆ §fPath: Slashing → Cutting → Blade Mastery\n")
                            .append("§e◆ §fWeakness: Less effective against armor")
                    )
    );

    // Earth Tier - Pure Fist Body (updated)
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_FIST_BODY = PHYSIQUES.register("pure_fist_body",
            ()-> new GenericPhysique("Pure Fist Body",
                    new HashMap<>(){{
                        put("ascension:intent",1.0);
                        put("ascension:body",0.4);
                        put("ascension:essence",0.1);
                    }},
                    new HashMap<>(){{
                        put("ascension:fist_intent",3.0);
                    }})
                    .setSkillList(List.of(
                            new AcquirableSkillData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"fist_aura_skill"),false,false)
                    ))
                    .setDescription(Component.empty()
                            .append("§aMy fist is my body and my body is my fist\n")
                            .append("§7Body is a weapon, no need for external arms\n\n")
                            .append("§e◆ §fGrowth: Strengthen fists through combat\n")
                            .append("§e◆ §fPath: Fist → Body Weapon → Unarmed Mastery\n")
                            .append("§e◆ §fWeakness: Limited range")
                    )
    );

    // Earth Tier - Pure Water Body (updated)
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_WATER_BODY = PHYSIQUES.register("pure_water_body",
            ()-> new GenericPhysique("Pure Water Body",
                    new HashMap<>(){{
                        put("ascension:intent",0.4);
                        put("ascension:body",0.1);
                        put("ascension:essence",1.0);
                    }},
                    new HashMap<>(){{
                        put("ascension:water",3.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§9Body harmonizes with water essence\n")
                            .append("§7Excels at fluid, adaptive techniques\n\n")
                            .append("§e◆ §fGrowth: Cultivate near water sources\n")
                            .append("§e◆ §fPath: Flow → Adaptation → Water Mastery\n")
                            .append("§e◆ §fWeakness: Weak to earth-based techniques")
                    )
    );

    // Earth Tier - Pure Earth Body (updated)
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_EARTH_BODY = PHYSIQUES.register("pure_earth_body",
            ()-> new GenericPhysique("Pure Earth Body",
                    new HashMap<>(){{
                        put("ascension:intent",0.4);
                        put("ascension:body",0.1);
                        put("ascension:essence",1.0);
                    }},
                    new HashMap<>(){{
                        put("ascension:earth",3.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§eBody harmonizes with earth essence\n")
                            .append("§7Excels at defensive, stable techniques\n\n")
                            .append("§e◆ §fGrowth: Cultivate on solid ground\n")
                            .append("§e◆ §fPath: Stability → Defense → Earth Mastery\n")
                            .append("§e◆ §fWeakness: Weak to wood-based techniques")
                    )
    );

    // Earth Tier - Pure Fire Body (updated)
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_FIRE_BODY = PHYSIQUES.register("pure_fire_body",
            ()-> new GenericPhysique("Pure Fire Body",
                    new HashMap<>(){{
                        put("ascension:intent",0.4);
                        put("ascension:body",0.1);
                        put("ascension:essence",1.0);
                    }},
                    new HashMap<>(){{
                        put("ascension:fire",3.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§cBody harmonizes with fire essence\n")
                            .append("§7Excels at explosive, aggressive techniques\n\n")
                            .append("§e◆ §fGrowth: Cultivate in fiery environments\n")
                            .append("§e◆ §fPath: Heat → Destruction → Fire Mastery\n")
                            .append("§e◆ §fWeakness: Weak to water-based techniques")
                    )
    );

    // Earth Tier - Pure Wood Body (updated)
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_WOOD_BODY = PHYSIQUES.register("pure_wood_body",
            ()-> new GenericPhysique("Pure Wood Body",
                    new HashMap<>(){{
                        put("ascension:intent",0.4);
                        put("ascension:body",0.1);
                        put("ascension:essence",1.0);
                    }},
                    new HashMap<>(){{
                        put("ascension:wood",3.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§aBody harmonizes with wood essence\n")
                            .append("§7Excels at healing, growing techniques\n\n")
                            .append("§e◆ §fGrowth: Cultivate in forests\n")
                            .append("§e◆ §fPath: Growth → Healing → Wood Mastery\n")
                            .append("§e◆ §fWeakness: Weak to metal-based techniques")
                    )
    );

    // Earth Tier - Pure Metal Body (updated)
    public static final DeferredHolder<IPhysique,GenericPhysique> PURE_METAL_BODY = PHYSIQUES.register("pure_metal_body",
            ()-> new GenericPhysique("Pure Metal Body",
                    new HashMap<>(){{
                        put("ascension:intent",0.4);
                        put("ascension:body",0.1);
                        put("ascension:essence",1.0);
                    }},
                    new HashMap<>(){{
                        put("ascension:metal",3.0);
                    }})
                    .setDescription(Component.empty()
                            .append("§7Body harmonizes with metal essence\n")
                            .append("§fExcels at sharp, precise techniques\n\n")
                            .append("§e◆ §fGrowth: Cultivate near metal deposits\n")
                            .append("§e◆ §fPath: Sharpness → Precision → Metal Mastery\n")
                            .append("§e◆ §fWeakness: Weak to fire-based techniques")
                    )
    );

    // Human Tier - Iron Bone Physique (updated)
    public static final DeferredHolder<IPhysique,GenericPhysique> IRON_BONE_PHYSIQUE = PHYSIQUES.register("iron_bone_physique",
            ()-> new GenericPhysique("Iron Bone Physique",
                    new HashMap<>(){{
                        put("ascension:intent",0.1);
                        put("ascension:body",1.0);
                        put("ascension:essence",0.4);
                    }},
                    new HashMap<>(){{
                        put("ascension:metal",1.2);
                    }})
                    .setSkillList( List.of(
                            new AcquirableSkillData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"iron_bones_passive_skill"),false,false)
                    ))
                    .setDescription(Component.empty()
                            .append("§8Bones strengthened with metal essence\n")
                            .append("§7Resistant to breaking and fractures\n\n")
                            .append("§e◆ §fGrowth: Strengthen bones through impact\n")
                            .append("§e◆ §fPath: Durability → Resilience → Unbreakable Bones\n")
                            .append("§e◆ §fWeakness: Heavy, less agile")
                    )
    );

    // Human Tier - Burning Skin Physique (updated)
    public static final DeferredHolder<IPhysique,GenericPhysique> BURNING_SKIN_PHYSIQUE = PHYSIQUES.register("burning_skin_physique",
            ()-> new GenericPhysique("Burning Skin Physique",
                    new HashMap<>(){{
                        put("ascension:intent",0.1);
                        put("ascension:body",1.0);
                        put("ascension:essence",0.4);
                    }},
                    new HashMap<>(){{
                        put("ascension:fire",1.2);
                    }})
                    .setDescription(Component.empty()
                            .append("§cSkin naturally resistant to heat and flames\n")
                            .append("§7Can withstand high temperatures\n\n")
                            .append("§e◆ §fGrowth: Expose skin to increasing heat\n")
                            .append("§e◆ §fPath: Heat Resistance → Fire Immunity → Flame Body\n")
                            .append("§e◆ §fWeakness: Vulnerable to cold")
                    )
    );

    // Earth Tier - Hundred Poison Physique (updated)
    public static final DeferredHolder<IPhysique,GenericPhysique> HUNDRED_POISON_PHYSIQUE = PHYSIQUES.register("hundred_poison_physique",
            ()-> new GenericPhysique("Hundred Poison Physique",
                    new HashMap<>(){{
                        put("ascension:intent",0.1);
                        put("ascension:body",1.0);
                        put("ascension:essence",0.4);
                    }},
                    new HashMap<>(){{
                        put("ascension:poison",1.8);
                    }})
                    .setDescription(Component.empty()
                            .append("§5Body resistant to and capable of producing poisons\n")
                            .append("§7Can cultivate poison-based techniques\n\n")
                            .append("§e◆ §fGrowth: Consume and resist various poisons\n")
                            .append("§e◆ §fPath: Poison Resistance → Poison Creation → Poison Mastery\n")
                            .append("§e◆ §fWeakness: Vulnerable to purification techniques")
                    )
    );

    // Earth Tier - Sacred Sapling Physique (updated)
    public static final DeferredHolder<IPhysique,GenericPhysique> SACRED_SAPLING_PHYSIQUE = PHYSIQUES.register("sacred_sapling_physique",
            ()-> new GenericPhysique("Sacred Sapling Physique",
                    new HashMap<>(){{
                        put("ascension:intent",0.1);
                        put("ascension:body",1.0);
                        put("ascension:essence",0.4);
                    }},
                    new HashMap<>(){{
                        put("ascension:wood",1.8);
                        put("ascension:life",1.5);
                    }})
                    .setDescription(Component.empty()
                            .append("§aBody connected to plant life and growth\n")
                            .append("§7Can cultivate nature-based techniques\n\n")
                            .append("§e◆ §fGrowth: Cultivate in natural environments\n")
                            .append("§e◆ §fPath: Plant Connection → Growth → Nature Mastery\n")
                            .append("§e◆ §fWeakness: Vulnerable to fire and metal")
                    )
    );

    // Earth Tier - Suppressed Yin Physique (updated)
    public static final DeferredHolder<IPhysique,GenericPhysique> SUPPRESSED_YIN_PHYSIQUE = PHYSIQUES.register("suppressed_yin_physique",
            ()-> new GenericPhysique("Suppressed Yin Physique",
                    new HashMap<>(){{
                        put("ascension:intent",0.1);
                        put("ascension:body",1.0);
                        put("ascension:essence",0.4);
                    }},
                    new HashMap<>(){{
                        put("ascension:yin",1.8);
                        put("ascension:ice",1.5);
                    }})
                    .setDescription(Component.empty()
                            .append("§bBody resonates with yin and cold energies\n")
                            .append("§7Excels at ice and shadow techniques\n\n")
                            .append("§e◆ §fGrowth: Cultivate during night or in cold places\n")
                            .append("§e◆ §fPath: Cold Resistance → Yin Mastery → Ice Control\n")
                            .append("§e◆ §fWeakness: Vulnerable to yang and fire")
                    )
    );

    public static void register(IEventBus modEventBus){
        PHYSIQUES.register(modEventBus);
    }
}