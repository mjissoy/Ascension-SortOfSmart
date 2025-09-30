package net.thejadeproject.ascension;

import io.netty.util.Attribute;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.thejadeproject.ascension.cultivation.CultivationSystem;

import java.util.*;


public class Config {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static ModConfigSpec SPEC;


    private static boolean validateHeatItemEntry(Object entry) {
        if (!(entry instanceof String)) return false;
        String entryStr = (String) entry;
        String[] parts = entryStr.split(",");
        if (parts.length != 2) return false;
        try {
            Integer.parseInt(parts[1].trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static class Common {


        public static ModConfigSpec.ConfigValue<List<? extends String>> PILL_CAULDRON_HEAT_ITEMS;

        public static ModConfigSpec.DoubleValue MAX_BASE_MAX_HEALTH;

        public static ModConfigSpec.IntValue REPAIR_AMOUNT;
        public static ModConfigSpec.IntValue REPAIR_INTERVAL;



        public static ModConfigSpec.DoubleValue ESSENCE_PATH_CULTIVATION_MODIFIER;
        public static ModConfigSpec.DoubleValue INTENT_PATH_CULTIVATION_MODIFIER;
        public static ModConfigSpec.DoubleValue BODY_PATH_CULTIVATION_MODIFIER;



        public static ModConfigSpec.DoubleValue PROGRESS_SPEED;
        public static ModConfigSpec.DoubleValue MINOR_REALM_MULTIPLIER;
        public static ModConfigSpec.DoubleValue MAJOR_REALM_MULTIPLIER;
        public static ModConfigSpec.IntValue FLIGHT_REALM;
        public static ModConfigSpec.DoubleValue MAX_SPEED_MULT;

        public static ModConfigSpec.ConfigValue<List<? extends String>> AVAILABLE_STARTING_ESSENCE_PHYSIQUE;
        public static ModConfigSpec.ConfigValue<List<? extends String>> AVAILABLE_STARTING_BODY_PHYSIQUE;
        public static ModConfigSpec.ConfigValue<List<? extends String>> AVAILABLE_STARTING_INTENT_PHYSIQUE;


        public static ModConfigSpec.DoubleValue MINOR_REALM_ATTACK_DAMAGE_INCREASE;
        public static ModConfigSpec.DoubleValue MAJOR_REALM_ATTACK_DAMAGE_INCREASE;
        public static ModConfigSpec.ConfigValue<List<? extends Integer>> ATTACK_DAMAGE_APPLICABLE_REALMS;

        public static ModConfigSpec.DoubleValue MINOR_REALM_ATTACK_SPEED_INCREASE;
        public static ModConfigSpec.DoubleValue MAJOR_REALM_ATTACK_SPEED_INCREASE;
        public static ModConfigSpec.ConfigValue<List<? extends Integer>> ATTACK_SPEED_APPLICABLE_REALMS;

        public static ModConfigSpec.DoubleValue MINOR_REALM_MAX_HEALTH_INCREASE;
        public static ModConfigSpec.DoubleValue MAJOR_REALM_MAX_HEALTH_INCREASE;
        public static ModConfigSpec.ConfigValue<List<? extends Integer>>MAX_HEALTH_APPLICABLE_REALMS;

        public static ModConfigSpec.DoubleValue MINOR_REALM_JUMP_STRENGTH_INCREASE;
        public static ModConfigSpec.DoubleValue MAJOR_REALM_JUMP_STRENGTH_INCREASE;
        public static ModConfigSpec.ConfigValue<List<? extends Integer>>JUMP_STRENGTH_APPLICABLE_REALMS;

        public static ModConfigSpec.DoubleValue MINOR_REALM_MOVEMENT_SPEED_INCREASE;
        public static ModConfigSpec.DoubleValue MAJOR_REALM_MOVEMENT_SPEED_INCREASE;
        public static ModConfigSpec.ConfigValue<List<? extends Integer>>MOVEMENT_SPEED_APPLICABLE_REALMS;
        static {
            BUILDER.push("PillCauldron");
            BUILDER.comment("Heat items for Pill Cauldron",
                    "Format: [\"modid:item_id,heat_value\", \"modid:item_id,heat_value\"]",
                    "Example: [\"minecraft:coal,8\", \"minecraft:coal_block,72\", \"ascension:crimson_lotus_flame,300\"]");
            PILL_CAULDRON_HEAT_ITEMS = BUILDER.defineList("heat_items",
                    () -> new ArrayList<>(Arrays.asList(
                            "minecraft:coal,8",
                            "minecraft:coal_block,72",
                            "ascension:crimson_lotus_flame,300"
                    )),
                    Config::validateHeatItemEntry
            );
            BUILDER.pop();



            BUILDER.push("CultivationModifiers");

            BUILDER.comment("Path Modifiers");
            ESSENCE_PATH_CULTIVATION_MODIFIER = BUILDER.defineInRange("essence_path_modifier",1,1,Double.MAX_VALUE);
            INTENT_PATH_CULTIVATION_MODIFIER = BUILDER.defineInRange("intent_path_modifier",1,1,Double.MAX_VALUE);
            BODY_PATH_CULTIVATION_MODIFIER = BUILDER.defineInRange("body_path_modifier",1,1,Double.MAX_VALUE);


            BUILDER.pop();

            BUILDER.push("Artifacts");
            BUILDER.comment("Artifact Modifiers");
            REPAIR_AMOUNT = BUILDER
                    .comment("Interval in ticks between Repairs [Default: 100]")
                    .defineInRange("repairInterval", 100, 1, Integer.MAX_VALUE);
            REPAIR_AMOUNT = BUILDER
                    .comment("Amount of durability to Repair each interval [Default: 2]")
                    .defineInRange("repairAmount", 2, 1, Integer.MAX_VALUE);
            BUILDER.pop();

            BUILDER.push("StartingPhysiqueOptions");
                BUILDER.comment("Intent options");
                AVAILABLE_STARTING_INTENT_PHYSIQUE = BUILDER.defineList("intent_options",()->
                        new ArrayList<>(){{
                            add("ascension:pure_sword_body");
                            add("ascension:pure_spear_body");
                            add("ascension:pure_bow_body");
                            add("ascension:pure_blade_body");
                            add("ascension:pure_fist_body");
                        }},(item)->true);
                BUILDER.comment("Body options");
                AVAILABLE_STARTING_BODY_PHYSIQUE =  BUILDER.defineList("body_options",()->
                    new ArrayList<>(){{
                        add("ascension:iron_bone_physique");
                        add("ascension:burning_skin_physique");
                        add("ascension:hundred_poison_physique");
                        add("ascension:sacred_sapling_physique");
                        add("ascension:suppressed_yin_physique");
                    }},(item)->true);
                BUILDER.comment("Essence options");
                AVAILABLE_STARTING_ESSENCE_PHYSIQUE = BUILDER.defineList("essence_options",()->
                        new ArrayList<>(){{
                            add("ascension:pure_water_body");
                            add("ascension:pure_fire_body");
                            add("ascension:pure_earth_body");
                            add("ascension:pure_metal_body");
                            add("ascension:pure_wood_body");
                        }},(item)->true);
            BUILDER.pop();

            BUILDER.push("Multipliers");
                BUILDER.push("CultivationMultipliers");
                    BUILDER.push("AttackDamageMultipliers");
                        BUILDER.comment(" Attack increase per minor realm");
                        MINOR_REALM_ATTACK_DAMAGE_INCREASE = BUILDER.defineInRange("minor_realm_attack_damage_increase",1,0,Double.MAX_VALUE);
                        BUILDER.comment(" Attack increase per major realm");
                        MAJOR_REALM_ATTACK_DAMAGE_INCREASE = BUILDER.defineInRange("major_realm_attack_damage_increase",1,0,Double.MAX_VALUE);
                        BUILDER.comment(" realms this increase applies too");
                        ATTACK_DAMAGE_APPLICABLE_REALMS = BUILDER.defineListAllowEmpty("attack_damage_applicable_realms", CultivationSystem::getRealmsIdList,(item)->true);
                    BUILDER.pop();

                    BUILDER.push("AttackSpeedMultipliers");
                        BUILDER.comment(" Attack Speed increase per minor realm");
                        MINOR_REALM_ATTACK_SPEED_INCREASE = BUILDER.defineInRange("minor_realm_attack_speed_increase",1,0,Double.MAX_VALUE);
                        BUILDER.comment(" Attack Speed increase per major realm");
                        MAJOR_REALM_ATTACK_SPEED_INCREASE = BUILDER.defineInRange("major_realm_attack_speed_increase",1,0,Double.MAX_VALUE);
                        BUILDER.comment(" Applicable realms");
                        ATTACK_SPEED_APPLICABLE_REALMS = BUILDER.defineListAllowEmpty("attack_speed_applicable_realms", CultivationSystem::getRealmsIdList,(item)->true);
                    BUILDER.pop();

                    BUILDER.push("JumpStrengthMultipliers");
                        BUILDER.comment(" Jump Strength increase per minor realm");
                        MINOR_REALM_JUMP_STRENGTH_INCREASE = BUILDER.defineInRange("minor_realm_jump_strength_increase",0.1,0,Double.MAX_VALUE);
                        BUILDER.comment(" Jump Strength increase per major realm");
                        MAJOR_REALM_JUMP_STRENGTH_INCREASE = BUILDER.defineInRange("major_realm_jump_strength_increase",0.1,0,Double.MAX_VALUE);
                        BUILDER.comment(" Applicable realms");
                        JUMP_STRENGTH_APPLICABLE_REALMS = BUILDER.defineListAllowEmpty("jump_strength_applicable_realms", CultivationSystem::getRealmsIdList,(item)->true);
                    BUILDER.pop();

                    BUILDER.push("MaxHealthMultipliers");
                        BUILDER.comment(" Max Health increase per minor realm");
                        MINOR_REALM_MAX_HEALTH_INCREASE = BUILDER.defineInRange("minor_realm_max_health_increase",1,0,Double.MAX_VALUE);
                        BUILDER.comment(" Max Health increase per major realm");
                        MAJOR_REALM_MAX_HEALTH_INCREASE = BUILDER.defineInRange("major_realm_max_health_increase",1,0,Double.MAX_VALUE);
                        BUILDER.comment(" Applicable realms");
                        MAX_HEALTH_APPLICABLE_REALMS = BUILDER.defineListAllowEmpty("attack_speed_applicable_realms", CultivationSystem::getRealmsIdList,(item)->true);
                    BUILDER.pop();
                    BUILDER.push("MovementSpeedMultipliers");
                        BUILDER.comment(" Movement Speed increase per minor realm");
                        MINOR_REALM_MOVEMENT_SPEED_INCREASE = BUILDER.defineInRange("minor_realm_movement_speed_increase",1,0,Double.MAX_VALUE);
                        BUILDER.comment(" Movement Speed increase per major realm");
                        MAJOR_REALM_MOVEMENT_SPEED_INCREASE = BUILDER.defineInRange("major_realm_movement_speed_increase",1,0,Double.MAX_VALUE);
                        BUILDER.comment(" Applicable realms");
                        MOVEMENT_SPEED_APPLICABLE_REALMS = BUILDER.defineListAllowEmpty("movement_speed_applicable_realms", CultivationSystem::getRealmsIdList,(item)->true);
                    BUILDER.pop();

                    BUILDER.comment(" The amount to increase cultivation progress when cultivating [Default: 30]");
                    PROGRESS_SPEED = BUILDER.defineInRange("Cultivation_Speed", 1, 1, Double.MAX_VALUE);

                    BUILDER.comment(" The amount to increase stats between Minor Realms [Default: 30]");
                    MINOR_REALM_MULTIPLIER = BUILDER.defineInRange("Minor_Cultivation_Stats_Multiplier", 30, 1f, Float.MAX_VALUE);

                    BUILDER.comment(" The amount to increase stats between Major Realms [Default: 30]");
                    MAJOR_REALM_MULTIPLIER = BUILDER.defineInRange("Major_Cultivation_Stats_Multiplier", 30, 1f, Double.MAX_VALUE);

                    BUILDER.comment("Which Major Realm should give Creative Flight 0 = Mortal, 10 = Golden Immortal");
                    FLIGHT_REALM = BUILDER.defineInRange("Flight_Realm", 3, 0, CultivationSystem.RealmAmount-1);
                BUILDER.pop();

                BUILDER.push("AttributeMultipliers");

                    BUILDER.comment(" The multiplier amount at which speed stops increasing [Default: 30]");
                    MAX_SPEED_MULT = BUILDER.defineInRange("Speed_Multiplier_Max", 0.5, 1f, Double.MAX_VALUE);
        }

        public Common(ModConfigSpec.Builder builder) {

        }
    }



    public static Map<String, Integer> getHeatItems() {
        Map<String, Integer> heatItems = new HashMap<>();
        for (String entry : Common.PILL_CAULDRON_HEAT_ITEMS.get()) {
            String[] parts = entry.split(",");
            if (parts.length == 2) {
                try {
                    String itemId = parts[0].trim();
                    int heatValue = Integer.parseInt(parts[1].trim());
                    heatItems.put(itemId, heatValue);
                } catch (NumberFormatException e) {
                    AscensionCraft.LOGGER.warn("Invalid heat item entry: {}", entry);
                }
            }
        }
        return heatItems;
    }

    static {
        new Common(BUILDER);
        SPEC = BUILDER.build();
    }

}
