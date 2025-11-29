package net.thejadeproject.ascension;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.thejadeproject.ascension.cultivation.CultivationSystem;

import java.util.ArrayList;
import java.util.List;

public class AscensionCultivationConfig {
    // Cultivation Modifiers
    public final ModConfigSpec.DoubleValue ESSENCE_PATH_CULTIVATION_MODIFIER;
    public final ModConfigSpec.DoubleValue INTENT_PATH_CULTIVATION_MODIFIER;
    public final ModConfigSpec.DoubleValue BODY_PATH_CULTIVATION_MODIFIER;

    // Multipliers
    public final ModConfigSpec.DoubleValue PROGRESS_SPEED;
    public final ModConfigSpec.DoubleValue MINOR_REALM_MULTIPLIER;
    public final ModConfigSpec.DoubleValue MAJOR_REALM_MULTIPLIER;
    public final ModConfigSpec.IntValue FLIGHT_REALM;
    public final ModConfigSpec.DoubleValue MAX_SPEED_MULT;

    // Starting Physique Options
    public final ModConfigSpec.ConfigValue<List<? extends String>> AVAILABLE_STARTING_ESSENCE_PHYSIQUE;
    public final ModConfigSpec.ConfigValue<List<? extends String>> AVAILABLE_STARTING_BODY_PHYSIQUE;
    public final ModConfigSpec.ConfigValue<List<? extends String>> AVAILABLE_STARTING_INTENT_PHYSIQUE;

    // Attack Damage Multipliers
    public final ModConfigSpec.DoubleValue MINOR_REALM_ATTACK_DAMAGE_INCREASE;
    public final ModConfigSpec.DoubleValue MAJOR_REALM_ATTACK_DAMAGE_INCREASE;
    public final ModConfigSpec.ConfigValue<List<? extends Integer>> ATTACK_DAMAGE_APPLICABLE_REALMS;

    // Attack Speed Multipliers
    public final ModConfigSpec.DoubleValue MINOR_REALM_ATTACK_SPEED_INCREASE;
    public final ModConfigSpec.DoubleValue MAJOR_REALM_ATTACK_SPEED_INCREASE;
    public final ModConfigSpec.ConfigValue<List<? extends Integer>> ATTACK_SPEED_APPLICABLE_REALMS;

    // Max Health Multipliers
    public final ModConfigSpec.DoubleValue MINOR_REALM_MAX_HEALTH_INCREASE;
    public final ModConfigSpec.DoubleValue MAJOR_REALM_MAX_HEALTH_INCREASE;
    public final ModConfigSpec.ConfigValue<List<? extends Integer>> MAX_HEALTH_APPLICABLE_REALMS;

    // Jump Strength Multipliers
    public final ModConfigSpec.DoubleValue MINOR_REALM_JUMP_STRENGTH_INCREASE;
    public final ModConfigSpec.DoubleValue MAJOR_REALM_JUMP_STRENGTH_INCREASE;
    public final ModConfigSpec.ConfigValue<List<? extends Integer>> JUMP_STRENGTH_APPLICABLE_REALMS;

    // Movement Speed Multipliers
    public final ModConfigSpec.DoubleValue MINOR_REALM_MOVEMENT_SPEED_INCREASE;
    public final ModConfigSpec.DoubleValue MAJOR_REALM_MOVEMENT_SPEED_INCREASE;
    public final ModConfigSpec.ConfigValue<List<? extends Integer>> MOVEMENT_SPEED_APPLICABLE_REALMS;

    // Health
    public final ModConfigSpec.DoubleValue MAX_BASE_MAX_HEALTH;

    public AscensionCultivationConfig(ModConfigSpec.Builder builder) {
        builder.push("StartingPhysiqueOptions");
        builder.comment("Intent options");
        AVAILABLE_STARTING_INTENT_PHYSIQUE = builder.defineList("intent_options", () ->
                new ArrayList<>() {{
                    add("ascension:pure_sword_body");
                    add("ascension:pure_spear_body");
                    add("ascension:pure_axe_body");
                    add("ascension:pure_blade_body");
                    add("ascension:pure_fist_body");
                }}, (item) -> true);
        builder.comment("Body options");
        AVAILABLE_STARTING_BODY_PHYSIQUE = builder.defineList("body_options", () ->
                new ArrayList<>() {{
                    add("ascension:iron_bone_physique");
                    add("ascension:burning_skin_physique");
                    add("ascension:hundred_poison_physique");
                    add("ascension:sacred_sapling_physique");
                    add("ascension:suppressed_yin_physique");
                }}, (item) -> true);
        builder.comment("Essence options");
        AVAILABLE_STARTING_ESSENCE_PHYSIQUE = builder.defineList("essence_options", () ->
                new ArrayList<>() {{
                    add("ascension:pure_water_body");
                    add("ascension:pure_fire_body");
                    add("ascension:pure_earth_body");
                    add("ascension:pure_metal_body");
                    add("ascension:pure_wood_body");
                }}, (item) -> true);
        builder.pop();

        builder.push("CultivationModifiers");
        builder.comment("Path Modifiers");
        ESSENCE_PATH_CULTIVATION_MODIFIER = builder.defineInRange("essence_path_modifier", 1, 1, Double.MAX_VALUE);
        INTENT_PATH_CULTIVATION_MODIFIER = builder.defineInRange("intent_path_modifier", 1, 1, Double.MAX_VALUE);
        BODY_PATH_CULTIVATION_MODIFIER = builder.defineInRange("body_path_modifier", 1, 1, Double.MAX_VALUE);
        builder.pop();

        builder.push("Multipliers");
        builder.push("CultivationMultipliers");
        builder.push("AttackDamageMultipliers");
        builder.comment(" Attack increase per minor realm");
        MINOR_REALM_ATTACK_DAMAGE_INCREASE = builder.defineInRange("minor_realm_attack_damage_increase", 1, 0, Double.MAX_VALUE);
        builder.comment(" Attack increase per major realm");
        MAJOR_REALM_ATTACK_DAMAGE_INCREASE = builder.defineInRange("major_realm_attack_damage_increase", 1, 0, Double.MAX_VALUE);
        builder.comment(" realms this increase applies too");
        ATTACK_DAMAGE_APPLICABLE_REALMS = builder.defineListAllowEmpty("attack_damage_applicable_realms", CultivationSystem::getRealmsIdList, (item) -> true);
        builder.pop();

        builder.push("AttackSpeedMultipliers");
        builder.comment(" Attack Speed increase per minor realm");
        MINOR_REALM_ATTACK_SPEED_INCREASE = builder.defineInRange("minor_realm_attack_speed_increase", 1, 0, Double.MAX_VALUE);
        builder.comment(" Attack Speed increase per major realm");
        MAJOR_REALM_ATTACK_SPEED_INCREASE = builder.defineInRange("major_realm_attack_speed_increase", 1, 0, Double.MAX_VALUE);
        builder.comment(" Applicable realms");
        ATTACK_SPEED_APPLICABLE_REALMS = builder.defineListAllowEmpty("attack_speed_applicable_realms", CultivationSystem::getRealmsIdList, (item) -> true);
        builder.pop();

        builder.push("JumpStrengthMultipliers");
        builder.comment(" Jump Strength increase per minor realm");
        MINOR_REALM_JUMP_STRENGTH_INCREASE = builder.defineInRange("minor_realm_jump_strength_increase", 0.1, 0, Double.MAX_VALUE);
        builder.comment(" Jump Strength increase per major realm");
        MAJOR_REALM_JUMP_STRENGTH_INCREASE = builder.defineInRange("major_realm_jump_strength_increase", 0.1, 0, Double.MAX_VALUE);
        builder.comment(" Applicable realms");
        JUMP_STRENGTH_APPLICABLE_REALMS = builder.defineListAllowEmpty("jump_strength_applicable_realms", CultivationSystem::getRealmsIdList, (item) -> true);
        builder.pop();

        builder.push("MaxHealthMultipliers");
        builder.comment(" Max Health increase per minor realm");
        MINOR_REALM_MAX_HEALTH_INCREASE = builder.defineInRange("minor_realm_max_health_increase", 1, 0, Double.MAX_VALUE);
        builder.comment(" Max Health increase per major realm");
        MAJOR_REALM_MAX_HEALTH_INCREASE = builder.defineInRange("major_realm_max_health_increase", 1, 0, Double.MAX_VALUE);
        builder.comment(" Applicable realms");
        MAX_HEALTH_APPLICABLE_REALMS = builder.defineListAllowEmpty("max_health_applicable_realms", CultivationSystem::getRealmsIdList, (item) -> true);
        builder.pop();

        builder.push("MovementSpeedMultipliers");
        builder.comment(" Movement Speed increase per minor realm");
        MINOR_REALM_MOVEMENT_SPEED_INCREASE = builder.defineInRange("minor_realm_movement_speed_increase", 1, 0, Double.MAX_VALUE);
        builder.comment(" Movement Speed increase per major realm");
        MAJOR_REALM_MOVEMENT_SPEED_INCREASE = builder.defineInRange("major_realm_movement_speed_increase", 1, 0, Double.MAX_VALUE);
        builder.comment(" Applicable realms");
        MOVEMENT_SPEED_APPLICABLE_REALMS = builder.defineListAllowEmpty("movement_speed_applicable_realms", CultivationSystem::getRealmsIdList, (item) -> true);
        builder.pop();

        builder.comment(" The amount to increase cultivation progress when cultivating [Default: 30]");
        PROGRESS_SPEED = builder.defineInRange("Cultivation_Speed", 1, 1, Double.MAX_VALUE);

        builder.comment(" The amount to increase stats between Minor Realms [Default: 30]");
        MINOR_REALM_MULTIPLIER = builder.defineInRange("Minor_Cultivation_Stats_Multiplier", 30, 1f, Float.MAX_VALUE);

        builder.comment(" The amount to increase stats between Major Realms [Default: 30]");
        MAJOR_REALM_MULTIPLIER = builder.defineInRange("Major_Cultivation_Stats_Multiplier", 30, 1f, Double.MAX_VALUE);

        builder.comment("Which Major Realm should give Creative Flight 0 = Mortal, 10 = Golden Immortal");
        FLIGHT_REALM = builder.defineInRange("Flight_Realm", 3, 0, CultivationSystem.RealmAmount - 1);
        builder.pop();

        builder.push("AttributeMultipliers");
        builder.comment(" The multiplier amount at which speed stops increasing [Default: 30]");
        MAX_SPEED_MULT = builder.defineInRange("Speed_Multiplier_Max", 0.5, 1f, Double.MAX_VALUE);
        builder.pop();

        builder.push("Health");
        MAX_BASE_MAX_HEALTH = builder.defineInRange("max_base_max_health", 1024.0, 1.0, Double.MAX_VALUE);
        builder.pop();
    }
}