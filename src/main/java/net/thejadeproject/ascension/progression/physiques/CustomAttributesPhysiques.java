package net.thejadeproject.ascension.progression.physiques;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashMap;

public class CustomAttributesPhysiques extends ModPhysiques{

    // ========== HUMAN TIER STATS ==========

    public static HashMap<Holder<Attribute>,Double> GENERIC_MINOR_REALM_STATS_1 = new HashMap<>(){{
        put(Attributes.MAX_HEALTH,5.0);
        put(Attributes.ATTACK_DAMAGE,1.0);
        put(Attributes.MOVEMENT_SPEED,0.01);
        put(Attributes.JUMP_STRENGTH,0.01);
        put(Attributes.STEP_HEIGHT,0.01);
    }};

    public static HashMap<Holder<Attribute>,Double> GENERIC_MAJOR_REALM_STATS_1 = new HashMap<>(){{
        put(Attributes.MAX_HEALTH,10.0);
        put(Attributes.ATTACK_DAMAGE,2.0);
        put(Attributes.MOVEMENT_SPEED,0.02);
        put(Attributes.JUMP_STRENGTH,0.02);
        put(Attributes.STEP_HEIGHT,0.02);
    }};

    // ========== EARTH TIER STATS ==========

    public static HashMap<Holder<Attribute>,Double> EARTH_TIER_MINOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 12.0);
        put(Attributes.ATTACK_DAMAGE, 1.8);
        put(Attributes.MOVEMENT_SPEED, 0.03);
        put(Attributes.JUMP_STRENGTH, 0.02);
        put(Attributes.STEP_HEIGHT, 0.02);
    }};

    public static HashMap<Holder<Attribute>,Double> EARTH_TIER_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 20.0);
        put(Attributes.ATTACK_DAMAGE, 3.0);
        put(Attributes.MOVEMENT_SPEED, 0.05);
        put(Attributes.JUMP_STRENGTH, 0.04);
        put(Attributes.STEP_HEIGHT, 0.04);
    }};

    // ========== HEAVEN TIER STATS ==========

    // Stone Skin Physique (Human Tier)
    public static HashMap<Holder<Attribute>,Double> STONE_SKIN_MINOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 8.0);
        put(Attributes.ARMOR, 3.0);
        put(Attributes.ATTACK_DAMAGE, 0.8);
        put(Attributes.MOVEMENT_SPEED, -0.01);
    }};
    public static HashMap<Holder<Attribute>,Double> STONE_SKIN_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 12.0);
        put(Attributes.ARMOR, 5.0);
        put(Attributes.ATTACK_DAMAGE, 1.2);
        put(Attributes.MOVEMENT_SPEED, -0.005);
    }};

    // Swift Wind Physique (Human Tier)
    public static HashMap<Holder<Attribute>,Double> SWIFT_WIND_MINOR_STATS = new HashMap<>(){{
        put(Attributes.MOVEMENT_SPEED, 0.08);
        put(Attributes.ATTACK_SPEED, 0.15);
        put(Attributes.MAX_HEALTH, 4.0);
        put(Attributes.ATTACK_DAMAGE, 0.6);
    }};
    public static HashMap<Holder<Attribute>,Double> SWIFT_WIND_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.MOVEMENT_SPEED, 0.15);
        put(Attributes.ATTACK_SPEED, 0.25);
        put(Attributes.MAX_HEALTH, 6.0);
        put(Attributes.ATTACK_DAMAGE, 1.0);
    }};

    // Iron Marrow Physique (Human Tier)
    public static HashMap<Holder<Attribute>,Double> IRON_MARROW_MINOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 7.0);
        put(Attributes.ARMOR_TOUGHNESS, 2.0);
        put(Attributes.KNOCKBACK_RESISTANCE, 0.3);
        put(Attributes.ATTACK_DAMAGE, 0.7);
    }};
    public static HashMap<Holder<Attribute>,Double> IRON_MARROW_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 10.0);
        put(Attributes.ARMOR_TOUGHNESS, 3.5);
        put(Attributes.KNOCKBACK_RESISTANCE, 0.5);
        put(Attributes.ATTACK_DAMAGE, 1.1);
    }};

    // Jade Bone Physique (Earth Tier)
    public static HashMap<Holder<Attribute>,Double> JADE_BONE_MINOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 12.0);
        put(Attributes.ARMOR, 2.0);
        put(Attributes.ARMOR_TOUGHNESS, 3.0);
        put(Attributes.ATTACK_DAMAGE, 1.0);
        put(Attributes.LUCK, 0.3);
    }};
    public static HashMap<Holder<Attribute>,Double> JADE_BONE_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 18.0);
        put(Attributes.ARMOR, 3.0);
        put(Attributes.ARMOR_TOUGHNESS, 5.0);
        put(Attributes.ATTACK_DAMAGE, 1.8);
        put(Attributes.LUCK, 0.5);
    }};

    // Crystal Meridian Physique (Earth Tier)
    public static HashMap<Holder<Attribute>,Double> CRYSTAL_MERIDIAN_MINOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 10.0);
        put(Attributes.ATTACK_DAMAGE, 1.2);
        put(Attributes.MOVEMENT_SPEED, 0.03);
        put(Attributes.ATTACK_SPEED, 0.1);
    }};
    public static HashMap<Holder<Attribute>,Double> CRYSTAL_MERIDIAN_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 15.0);
        put(Attributes.ATTACK_DAMAGE, 2.0);
        put(Attributes.MOVEMENT_SPEED, 0.06);
        put(Attributes.ATTACK_SPEED, 0.2);
    }};

    // Thunderclap Physique (Earth Tier)
    public static HashMap<Holder<Attribute>,Double> THUNDERCLAP_MINOR_STATS = new HashMap<>(){{
        put(Attributes.ATTACK_DAMAGE, 1.5);
        put(Attributes.ATTACK_SPEED, 0.2);
        put(Attributes.MOVEMENT_SPEED, 0.04);
        put(Attributes.MAX_HEALTH, 8.0);
    }};
    public static HashMap<Holder<Attribute>,Double> THUNDERCLAP_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.ATTACK_DAMAGE, 2.5);
        put(Attributes.ATTACK_SPEED, 0.35);
        put(Attributes.MOVEMENT_SPEED, 0.08);
        put(Attributes.MAX_HEALTH, 12.0);
    }};

    // Blood Jade Physique (Earth Tier)
    public static HashMap<Holder<Attribute>,Double> BLOOD_JADE_MINOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 14.0);
        put(Attributes.ATTACK_DAMAGE, 1.3);
        put(Attributes.KNOCKBACK_RESISTANCE, 0.2);
        put(Attributes.ATTACK_SPEED, 0.05);
    }};
    public static HashMap<Holder<Attribute>,Double> BLOOD_JADE_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 22.0);
        put(Attributes.ATTACK_DAMAGE, 2.2);
        put(Attributes.KNOCKBACK_RESISTANCE, 0.4);
        put(Attributes.ATTACK_SPEED, 0.1);
    }};

    // ========== HEAVEN TIER STATS ==========

    public static HashMap<Holder<Attribute>,Double> HEAVENLY_LIGHTNING_MINOR_STATS = new HashMap<>(){{
        put(Attributes.ATTACK_DAMAGE, 2.0);
        put(Attributes.ATTACK_SPEED, 0.3);
        put(Attributes.MOVEMENT_SPEED, 0.1);
        put(Attributes.MAX_HEALTH, 10.0);
        put(Attributes.LUCK, 0.4);
    }};
    public static HashMap<Holder<Attribute>,Double> HEAVENLY_LIGHTNING_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.ATTACK_DAMAGE, 3.5);
        put(Attributes.ATTACK_SPEED, 0.5);
        put(Attributes.MOVEMENT_SPEED, 0.2);
        put(Attributes.MAX_HEALTH, 15.0);
        put(Attributes.LUCK, 0.8);
    }};

    public static HashMap<Holder<Attribute>,Double> PHOENIX_NIRVANA_MINOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 15.0);
        put(Attributes.ATTACK_DAMAGE, 1.8);
        put(Attributes.MOVEMENT_SPEED, 0.06);
        put(Attributes.LUCK, 0.5);
        put(Attributes.JUMP_STRENGTH, 0.05);
    }};
    public static HashMap<Holder<Attribute>,Double> PHOENIX_NIRVANA_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 25.0);
        put(Attributes.ATTACK_DAMAGE, 3.2);
        put(Attributes.MOVEMENT_SPEED, 0.12);
        put(Attributes.LUCK, 1.0);
        put(Attributes.JUMP_STRENGTH, 0.1);
    }};

    public static HashMap<Holder<Attribute>,Double> DRAGON_VEIN_MINOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 20.0);
        put(Attributes.ATTACK_DAMAGE, 2.2);
        put(Attributes.ARMOR, 3.0);
        put(Attributes.ARMOR_TOUGHNESS, 2.0);
        put(Attributes.KNOCKBACK_RESISTANCE, 0.4);
    }};
    public static HashMap<Holder<Attribute>,Double> DRAGON_VEIN_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 35.0);
        put(Attributes.ATTACK_DAMAGE, 4.0);
        put(Attributes.ARMOR, 5.0);
        put(Attributes.ARMOR_TOUGHNESS, 4.0);
        put(Attributes.KNOCKBACK_RESISTANCE, 0.8);
    }};

    public static HashMap<Holder<Attribute>,Double> VOID_WALKER_MINOR_STATS = new HashMap<>(){{
        put(Attributes.MOVEMENT_SPEED, 0.15);
        put(Attributes.ATTACK_SPEED, 0.25);
        put(Attributes.MAX_HEALTH, 8.0);
        put(Attributes.ATTACK_DAMAGE, 1.5);
        put(Attributes.STEP_HEIGHT, 0.1);
    }};
    public static HashMap<Holder<Attribute>,Double> VOID_WALKER_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.MOVEMENT_SPEED, 0.3);
        put(Attributes.ATTACK_SPEED, 0.45);
        put(Attributes.MAX_HEALTH, 12.0);
        put(Attributes.ATTACK_DAMAGE, 2.5);
        put(Attributes.STEP_HEIGHT, 0.2);
    }};

    public static HashMap<Holder<Attribute>,Double> SOLAR_FLARE_MINOR_STATS = new HashMap<>(){{
        put(Attributes.ATTACK_DAMAGE, 2.5);
        put(Attributes.MAX_HEALTH, 12.0);
        put(Attributes.MOVEMENT_SPEED, 0.05);
        put(Attributes.ATTACK_SPEED, 0.15);
    }};
    public static HashMap<Holder<Attribute>,Double> SOLAR_FLARE_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.ATTACK_DAMAGE, 4.5);
        put(Attributes.MAX_HEALTH, 18.0);
        put(Attributes.MOVEMENT_SPEED, 0.1);
        put(Attributes.ATTACK_SPEED, 0.3);
    }};

    public static HashMap<Holder<Attribute>,Double> LUNAR_SHADOW_MINOR_STATS = new HashMap<>(){{
        put(Attributes.MOVEMENT_SPEED, 0.12);
        put(Attributes.ATTACK_SPEED, 0.2);
        put(Attributes.MAX_HEALTH, 10.0);
        put(Attributes.ATTACK_DAMAGE, 1.8);
        put(Attributes.LUCK, 0.6);
    }};
    public static HashMap<Holder<Attribute>,Double> LUNAR_SHADOW_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.MOVEMENT_SPEED, 0.25);
        put(Attributes.ATTACK_SPEED, 0.4);
        put(Attributes.MAX_HEALTH, 16.0);
        put(Attributes.ATTACK_DAMAGE, 3.0);
        put(Attributes.LUCK, 1.2);
    }};

    public static HashMap<Holder<Attribute>,Double> SOUL_ANCHOR_MINOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 18.0);
        put(Attributes.KNOCKBACK_RESISTANCE, 0.6);
        put(Attributes.ATTACK_DAMAGE, 1.5);
        put(Attributes.LUCK, 0.7);
    }};
    public static HashMap<Holder<Attribute>,Double> SOUL_ANCHOR_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 30.0);
        put(Attributes.KNOCKBACK_RESISTANCE, 1.0);
        put(Attributes.ATTACK_DAMAGE, 2.8);
        put(Attributes.LUCK, 1.5);
    }};

    // ========== ASCENSION TIER STATS ==========

    public static HashMap<Holder<Attribute>,Double> HEAVENLY_DEMON_MINOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 25.0);
        put(Attributes.ATTACK_DAMAGE, 3.0);
        put(Attributes.ATTACK_SPEED, 0.25);
        put(Attributes.MOVEMENT_SPEED, 0.08);
        put(Attributes.KNOCKBACK_RESISTANCE, 0.5);
    }};
    public static HashMap<Holder<Attribute>,Double> HEAVENLY_DEMON_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 45.0);
        put(Attributes.ATTACK_DAMAGE, 6.0);
        put(Attributes.ATTACK_SPEED, 0.5);
        put(Attributes.MOVEMENT_SPEED, 0.16);
        put(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }};

    public static HashMap<Holder<Attribute>,Double> COSMIC_YINYANG_MINOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 20.0);
        put(Attributes.ATTACK_DAMAGE, 2.5);
        put(Attributes.MOVEMENT_SPEED, 0.07);
        put(Attributes.ATTACK_SPEED, 0.2);
        put(Attributes.LUCK, 1.0);
    }};
    public static HashMap<Holder<Attribute>,Double> COSMIC_YINYANG_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 35.0);
        put(Attributes.ATTACK_DAMAGE, 4.5);
        put(Attributes.MOVEMENT_SPEED, 0.15);
        put(Attributes.ATTACK_SPEED, 0.4);
        put(Attributes.LUCK, 2.0);
    }};

    public static HashMap<Holder<Attribute>,Double> PRIMORDIAL_CHAOS_MINOR_STATS = new HashMap<>(){{
        put(Attributes.ATTACK_DAMAGE, 3.5);
        put(Attributes.MAX_HEALTH, 15.0);
        put(Attributes.ATTACK_SPEED, 0.3);
        put(Attributes.MOVEMENT_SPEED, 0.1);
    }};
    public static HashMap<Holder<Attribute>,Double> PRIMORDIAL_CHAOS_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.ATTACK_DAMAGE, 7.0);
        put(Attributes.MAX_HEALTH, 25.0);
        put(Attributes.ATTACK_SPEED, 0.6);
        put(Attributes.MOVEMENT_SPEED, 0.2);
    }};

    public static HashMap<Holder<Attribute>,Double> ETERNAL_REINCARNATION_MINOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 30.0);
        put(Attributes.ATTACK_DAMAGE, 2.0);
        put(Attributes.LUCK, 1.5);
        put(Attributes.KNOCKBACK_RESISTANCE, 0.3);
    }};
    public static HashMap<Holder<Attribute>,Double> ETERNAL_REINCARNATION_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 60.0);
        put(Attributes.ATTACK_DAMAGE, 3.5);
        put(Attributes.LUCK, 3.0);
        put(Attributes.KNOCKBACK_RESISTANCE, 0.6);
    }};

    public static HashMap<Holder<Attribute>,Double> SWORD_SAINT_MINOR_STATS = new HashMap<>(){{
        put(Attributes.ATTACK_DAMAGE, 4.0);
        put(Attributes.ATTACK_SPEED, 0.35);
        put(Attributes.MOVEMENT_SPEED, 0.12);
        put(Attributes.MAX_HEALTH, 12.0);
    }};
    public static HashMap<Holder<Attribute>,Double> SWORD_SAINT_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.ATTACK_DAMAGE, 8.0);
        put(Attributes.ATTACK_SPEED, 0.7);
        put(Attributes.MOVEMENT_SPEED, 0.25);
        put(Attributes.MAX_HEALTH, 20.0);
    }};

    public static HashMap<Holder<Attribute>,Double> UNIVERSE_DEVOURER_MINOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 40.0);
        put(Attributes.ATTACK_DAMAGE, 3.8);
        put(Attributes.KNOCKBACK_RESISTANCE, 0.8);
        put(Attributes.ARMOR, 4.0);
    }};
    public static HashMap<Holder<Attribute>,Double> UNIVERSE_DEVOURER_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 80.0);
        put(Attributes.ATTACK_DAMAGE, 7.5);
        put(Attributes.KNOCKBACK_RESISTANCE, 1.5);
        put(Attributes.ARMOR, 8.0);
    }};

    // Stone Monkey (Ascension Tier - updated)
    public static HashMap<Holder<Attribute>,Double> STONE_MONKEY_MINOR_REALM_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 30.0);
        put(Attributes.ATTACK_DAMAGE, 3.5);
        put(Attributes.MOVEMENT_SPEED, 0.08);
        put(Attributes.JUMP_STRENGTH, 0.2);
        put(Attributes.KNOCKBACK_RESISTANCE, 0.8);
        put(Attributes.STEP_HEIGHT, 0.05);
    }};
    public static HashMap<Holder<Attribute>,Double> STONE_MONKEY_MAJOR_REALM_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 50.0);
        put(Attributes.ATTACK_DAMAGE, 6.0);
        put(Attributes.MOVEMENT_SPEED, 0.15);
        put(Attributes.JUMP_STRENGTH, 0.4);
        put(Attributes.KNOCKBACK_RESISTANCE, 1.5);
        put(Attributes.STEP_HEIGHT, 0.2);
    }};

    // Kitsune (Ascension Tier - updated)
    public static HashMap<Holder<Attribute>,Double> KITSUNE_BASE_MINOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 18.0);
        put(Attributes.ATTACK_DAMAGE, 2.2);
        put(Attributes.MOVEMENT_SPEED, 0.06);
        put(Attributes.JUMP_STRENGTH, 0.04);
        put(Attributes.STEP_HEIGHT, 0.03);
        put(Attributes.LUCK, 0.8);
    }};
    public static HashMap<Holder<Attribute>,Double> KITSUNE_BASE_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 30.0);
        put(Attributes.ATTACK_DAMAGE, 3.8);
        put(Attributes.MOVEMENT_SPEED, 0.12);
        put(Attributes.JUMP_STRENGTH, 0.08);
        put(Attributes.STEP_HEIGHT, 0.06);
        put(Attributes.LUCK, 1.5);
    }};
    public static HashMap<Holder<Attribute>,Double> KITSUNE_TAIL_BONUS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 3.0);
        put(Attributes.ATTACK_DAMAGE, 0.3);
        put(Attributes.MOVEMENT_SPEED, 0.01);
        put(Attributes.LUCK, 0.2);
    }};
}