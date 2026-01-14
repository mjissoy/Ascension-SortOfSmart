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

import static net.thejadeproject.ascension.progression.physiques.CustomAttributesPhysiques.*;

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
                    }}, new StandardStatRealmChange(STONE_SKIN_MINOR_STATS, STONE_SKIN_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Common defensive physique"),
                            Component.literal("§8Skin hardens like stone, providing natural armor"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Take heavy blows without breaking"),
                            Component.literal("§e◆ §fPath: Endurance → Resilience → Immovability"),
                            Component.literal("§e◆ §fWeakness: Slow movement, vulnerable to water-based techniques")
                    ))
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
                    }}, new StandardStatRealmChange(SWIFT_WIND_MINOR_STATS, SWIFT_WIND_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Common speed physique"),
                            Component.literal("§8Body harmonizes with wind, granting incredible speed"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Move faster than the eye can see"),
                            Component.literal("§e◆ §fPath: Speed → Precision → Afterimages"),
                            Component.literal("§e◆ §fWeakness: Fragile body, low defense")
                    ))
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
                    }}, new StandardStatRealmChange(IRON_MARROW_MINOR_STATS, IRON_MARROW_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Internal strength physique"),
                            Component.literal("§8Marrow turns to iron, strengthening bones from within"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Refine bone marrow through combat"),
                            Component.literal("§e◆ §fPath: Endurance → Internal Strength → Unbreakable Bones"),
                            Component.literal("§e◆ §fWeakness: Heavy, limited agility")
                    ))
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
                    }}, new StandardStatRealmChange(JADE_BONE_MINOR_STATS, JADE_BONE_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§2Earth Tier - Rare defensive physique"),
                            Component.literal("§aBones transform into jade, radiating life energy"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Absorb earth essence to strengthen bones"),
                            Component.literal("§e◆ §fPath: Defense → Regeneration → Jade Immortality"),
                            Component.literal("§e◆ §fWeakness: Vulnerable to metal-based attacks")
                    ))
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
                    }}, new StandardStatRealmChange(CRYSTAL_MERIDIAN_MINOR_STATS, CRYSTAL_MERIDIAN_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§2Earth Tier - Essence channeling physique"),
                            Component.literal("§dMeridians crystallize, allowing pure essence flow"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Purify essence through meditation"),
                            Component.literal("§e◆ §fPath: Clarity → Reflection → Crystalline Perfection"),
                            Component.literal("§e◆ §fWeakness: Brittle, shatters under extreme force")
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
                    }}, new StandardStatRealmChange(THUNDERCLAP_MINOR_STATS, THUNDERCLAP_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§2Earth Tier - Lightning affinity physique"),
                            Component.literal("§eBody resonates with thunder, strikes with lightning speed"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Cultivate during thunderstorms"),
                            Component.literal("§e◆ §fPath: Speed → Power → Lightning Embodiment"),
                            Component.literal("§e◆ §fWeakness: Conducts electricity, weak to grounding")
                    ))
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
                    }}, new StandardStatRealmChange(BLOOD_JADE_MINOR_STATS, BLOOD_JADE_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§2Earth Tier - Vitality-based physique"),
                            Component.literal("§cBlood contains jade particles, granting life force"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Consume life essence from foes"),
                            Component.literal("§e◆ §fPath: Vitality → Absorption → Immortal Blood"),
                            Component.literal("§e◆ §fWeakness: Bleeds easily, weak to purification")
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
                    }}, new StandardStatRealmChange(HEAVENLY_LIGHTNING_MINOR_STATS, HEAVENLY_LIGHTNING_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§bHeaven Tier - Divine lightning physique"),
                            Component.literal("§6Born with the blessing of thunder gods"),
                            Component.literal("§eCan call upon heavenly tribulation lightning"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Survive heavenly tribulations"),
                            Component.literal("§e◆ §fPath: Thunder → Tribulation → Heavenly Wrath"),
                            Component.literal("§e◆ §fWeakness: Water conducts own lightning back")
                    ))
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
                    }}, new StandardStatRealmChange(PHOENIX_NIRVANA_MINOR_STATS, PHOENIX_NIRVANA_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§bHeaven Tier - Rebirth physique"),
                            Component.literal("§cCarries the bloodline of the immortal phoenix"),
                            Component.literal("§6Can be reborn from ashes, stronger each time"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Die and be reborn through fire"),
                            Component.literal("§e◆ §fPath: Fire → Rebirth → Phoenix God"),
                            Component.literal("§e◆ §fWeakness: Extreme cold stops rebirth cycle")
                    ))
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
                    }}, new StandardStatRealmChange(DRAGON_VEIN_MINOR_STATS, DRAGON_VEIN_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§bHeaven Tier - Draconic physique"),
                            Component.literal("§5Possesses ancient dragon bloodline"),
                            Component.literal("§dVeins pulse with primordial dragon energy"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Absorb essence of dragon remains"),
                            Component.literal("§e◆ §fPath: Scale → Roar → Dragon God"),
                            Component.literal("§e◆ §fWeakness: Dragon-slaying techniques")
                    ))
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
                    }}, new StandardStatRealmChange(VOID_WALKER_MINOR_STATS, VOID_WALKER_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§bHeaven Tier - Spatial physique"),
                            Component.literal("§8Born between realms, attuned to the void"),
                            Component.literal("§7Can walk through space and vanish from existence"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Meditate in spatial rifts"),
                            Component.literal("§e◆ §fPath: Teleport → Spatial Cut → Void God"),
                            Component.literal("§e◆ §fWeakness: Light-based techniques disrupt void")
                    ))
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
                    }}, new StandardStatRealmChange(SOLAR_FLARE_MINOR_STATS, SOLAR_FLARE_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§bHeaven Tier - Solar physique"),
                            Component.literal("§eBody contains a miniature sun within dantian"),
                            Component.literal("§6Can unleash solar flares of immense power"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Absorb sunlight at noon"),
                            Component.literal("§e◆ §fPath: Heat → Light → Solar Deity"),
                            Component.literal("§e◆ §fWeakness: Night weakens power, lunar attacks")
                    ))
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
                    }}, new StandardStatRealmChange(LUNAR_SHADOW_MINOR_STATS, LUNAR_SHADOW_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§bHeaven Tier - Lunar physique"),
                            Component.literal("§bBlessed by the moon goddess"),
                            Component.literal("§3Can manipulate shadows and moonlight"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Cultivate under full moon"),
                            Component.literal("§e◆ §fPath: Shadow → Reflection → Lunar Deity"),
                            Component.literal("§e◆ §fWeakness: Sunlight weakens abilities")
                    ))
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
                    }}, new StandardStatRealmChange(SOUL_ANCHOR_MINOR_STATS, SOUL_ANCHOR_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§bHeaven Tier - Soul-based physique"),
                            Component.literal("§dSoul is anchored to multiple existences"),
                            Component.literal("§5Can survive soul destruction and re-anchor"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Survive soul-damaging attacks"),
                            Component.literal("§e◆ §fPath: Stability → Anchoring → Eternal Soul"),
                            Component.literal("§e◆ §fWeakness: Soul-severing techniques")
                    ))
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
                    }}, new StandardStatRealmChange(HEAVENLY_DEMON_MINOR_STATS, HEAVENLY_DEMON_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§dAscension Tier - Demonic cultivation physique"),
                            Component.literal("§4Born from the union of heavenly and demonic energies"),
                            Component.literal("§cCan defy heaven itself, cultivating through slaughter"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Kill powerful beings and absorb their essence"),
                            Component.literal("§e◆ §fPath: Slaughter → Demonic Energy → Heavenly Demon"),
                            Component.literal("§e◆ §fWeakness: Heavenly tribulations are 10x stronger")
                    ))
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
                    }}, new StandardStatRealmChange(COSMIC_YINYANG_MINOR_STATS, COSMIC_YINYANG_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§dAscension Tier - Balance physique"),
                            Component.literal("§fEmbodies the perfect balance of cosmic forces"),
                            Component.literal("§7Can manipulate both creation and destruction"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Balance opposing forces within"),
                            Component.literal("§e◆ §fPath: Harmony → Balance → Cosmic Unity"),
                            Component.literal("§e◆ §fWeakness: Chaos disrupts the balance")
                    ))
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
                    }}, new StandardStatRealmChange(PRIMORDIAL_CHAOS_MINOR_STATS, PRIMORDIAL_CHAOS_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§dAscension Tier - Chaos origin physique"),
                            Component.literal("§5Born from the primordial chaos before creation"),
                            Component.literal("§dCan manipulate the fabric of reality itself"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Create and destroy worlds"),
                            Component.literal("§e◆ §fPath: Chaos → Primordial → Reality Weaver"),
                            Component.literal("§e◆ §fWeakness: Order-based divine techniques")
                    ))
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
                    }}, new StandardStatRealmChange(ETERNAL_REINCARNATION_MINOR_STATS, ETERNAL_REINCARNATION_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§dAscension Tier - Reincarnation mastery"),
                            Component.literal("§3Has lived through countless lifetimes"),
                            Component.literal("§bRemembers all past lives and their skills"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Awaken past life memories"),
                            Component.literal("§e◆ §fPath: Memory → Rebirth → Eternal Cycle"),
                            Component.literal("§e◆ §fWeakness: Karma accumulates from past lives")
                    ))
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
                    }}, new StandardStatRealmChange(SWORD_SAINT_MINOR_STATS, SWORD_SAINT_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§dAscension Tier - Ultimate sword physique"),
                            Component.literal("§8Every cell resonates with sword intent"),
                            Component.literal("§7Can cut through space, time, and concepts"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Comprehend deeper sword laws"),
                            Component.literal("§e◆ §fPath: Sword → Domain → Sword Saint"),
                            Component.literal("§e◆ §fWeakness: Completely focused on sword, lacks versatility")
                    ))
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
                    }}, new StandardStatRealmChange(UNIVERSE_DEVOURER_MINOR_STATS, UNIVERSE_DEVOURER_MAJOR_STATS))
                    .setDescription(List.of(
                            Component.literal("§dAscension Tier - Ultimate devouring physique"),
                            Component.literal("§0Can devour stars, worlds, and eventually universes"),
                            Component.literal("§8Grows stronger by consuming everything"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Consume higher quality energy sources"),
                            Component.literal("§e◆ §fPath: Consumption → Black Hole → Universe Devourer"),
                            Component.literal("§e◆ §fWeakness: Divine seals and spatial locks")
                    ))
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




    public static final DeferredHolder<IPhysique,GenericPhysique> EMPTY_VESSEL = PHYSIQUES.register("empty_vessel",
            ()-> new GenericPhysique("Empty Vessel",
                    new HashMap<>(){{
                        put("ascension:intent",0.1);
                        put("ascension:body",0.1);
                        put("ascension:essence",0.1);
                    }},
                    new HashMap<>())
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
                    }})
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
                    .setDescription(Component.literal("my fist is my body and my body is my fist"))
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
                    }})
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
                    }})

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
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Durable bone structure"),
                            Component.literal("§8Bones strengthened with metal essence"),
                            Component.literal("§7Resistant to breaking and fractures"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Strengthen bones through impact"),
                            Component.literal("§e◆ §fPath: Durability → Resilience → Unbreakable Bones"),
                            Component.literal("§e◆ §fWeakness: Heavy, less agile")
                    ))
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
                    }}))
                    .setDescription(List.of(
                            Component.literal("§7Human Tier - Fire-resistant skin"),
                            Component.literal("§cSkin naturally resistant to heat and flames"),
                            Component.literal("§7Can withstand high temperatures"),
                            Component.literal(""),
                            Component.literal("§e◆ §fGrowth: Expose skin to increasing heat"),
                            Component.literal("§e◆ §fPath: Heat Resistance → Fire Immunity → Flame Body"),
                            Component.literal("§e◆ §fWeakness: Vulnerable to cold")
                    ))
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
                        put("ascension:poison",1.2);
                    }})
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
    );

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
    );

    public static void register(IEventBus modEventBus){
        PHYSIQUES.register(modEventBus);
    }
}
