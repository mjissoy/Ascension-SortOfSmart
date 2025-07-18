package net.thejadeproject.ascension.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.thejadeproject.ascension.AscensionCraft;

public class lang extends LanguageProvider {
    public lang(PackOutput output, String locale) {
        super(output, AscensionCraft.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {

        //GuideBook
        add("book.title.ascension.art_of_ascension", "Ascension Guide");
        add("book.subtitle.ascension.art_of_ascension", "Guide to Ascendance");


        //Items
        add("item.ascension.jade", "Jade");
        add("item.ascension.jade_nugget", "Jade Nugget");
        add("item.ascension.ascension_icon", "Ascension Icon");
        add("item.ascension.raw_jade", "Raw Jade");
        add("item.ascension.undead_core", "Undead Core");
        add("item.ascension.living_core", "Living Core");
        add("item.ascension.crimson_lotus_flame", "Crimson Lotus Flame");
        add("item.ascension.rat_spawn_egg", "Treasure Rat Spawn Egg");

        //Mobs
        add("entity.mcourse.rat", "Treasure Rat");


        //Pills
        add("item.ascension.regeneration_pill", "Regeneration Pill");



        //Herbs
        add("item.ascension.golden_sun_leaf", "Golden Sun Leaf");
        add("item.ascension.ironwood_sprout", "Ironwood Sprout");
        add("item.ascension.white_jade_orchid", "White Jade Orchid");
        add("block.ascension.golden_sun_leaf_block", "Golden Sun Leaf");
        add("block.ascension.ironwood_sprout_block", "Ironwood Sprout");
        add("block.ascension.white_jade_orchid_block", "White Jade Orchid");

        //Blocks
        add("block.ascension.golden_palm_log", "Golden Palm Log");
        add("block.ascension.golden_palm_wood", "Golden Palm Wood");
        add("block.ascension.stripped_golden_palm_log", "Stripped Golden Palm Log");
        add("block.ascension.stripped_golden_palm_wood", "Stripped Golden Palm Wood");
        add("block.ascension.golden_palm_planks", "Golden Palm Planks");
        add("block.ascension.golden_palm_leaves", "Golden Palm Leaves");
        add("block.ascension.golden_palm_sapling", "Golden Palm Sapling");
        add("block.ascension.golden_palm_slab", "Golden Palm Slab");
        add("block.ascension.golden_palm_stairs", "Golden Palm Stair");
        add("block.ascension.golden_palm_button", "Golden Palm Button");
        add("block.ascension.golden_palm_pressure_plate", "Golden Palm Pressure Plate");
        add("block.ascension.golden_palm_fence", "Golden Palm Fence");
        add("block.ascension.golden_palm_fence_gate", "Golden Palm Fence Gate");
        add("block.ascension.golden_palm_door", "Golden Palm Door");
        add("block.ascension.golden_palm_trapdoor", "Golden Palm Trapdoor");




        add("block.ascension.jade_ore", "Jade Ore");
        add("block.ascension.spiritual_stone_cluster", "Spiritual Stone Cluster");
        add("block.ascension.jade_block", "Jade Block");
        add("block.ascension.pill_cauldron_low_human", "Pill Cauldron");

        //GUI & Other Stuff
        add("creativetab.ascension.items", "Ascension Items");
        add("creativetab.ascension.blocks", "Ascension Blocks");
        add("creativetab.ascension.herbs", "Ascension Herbs");


        add("effect.ascension.qi_enhanced_regeneration", "Qi Enhanced Regen");
        add("effect.ascension.cleansing", "Cleansing");

        add("category.ascension.cultivation", "Ascension");
        add("key.ascension.cultivate", "Cultivate");
        add("key.ascension.introspection", "Introspection");




        add("ascension.configuration.Multipliers", "Stats Multipliers");
        add("ascension.configuration.CultivationMultipliers", "Cultivation Multipliers");
        add("ascension.configuration.Cultivation_Speed", "Cultivation Speed");
        add("ascension.configuration.Minor_Cultivation_Stats_Multiplier", "Minor Cultivation Stats Multiplier");
        add("ascension.configuration.Major_Cultivation_Stats_Multiplier", "Major Cultivation Stats Multiplier");
        add("ascension.configuration.Flight_Realm", "Flight Realm");

        add("ascension.configuration.AttributeMultipliers", "Attributes Multipliers");
        add("ascension.configuration.Speed_Multiplier_Max", "Max Speed");
        add("block.ascension.pill_cauldron", "Pill Cauldron");

        add("ascension.configuration.AttackDamageMultipliers","Attack Damage Multipliers");
        add("ascension.configuration.minor_realm_attack_damage_increase","Minor Realm Increase");
        add("ascension.configuration.major_realm_attack_damage_increase","Major Realm Increase");
        add("ascension.configuration.attack_damage_applicable_realms","Applicable Realms");

        add("ascension.configuration.AttackSpeedMultipliers","Attack Speed Multipliers");
        add("ascension.configuration.minor_realm_attack_speed_increase","Minor Realm Increase");
        add("ascension.configuration.major_realm_attack_speed_increase","Major Realm Increase");
        add("ascension.configuration.attack_speed_applicable_realms","Applicable Realms");

        add("ascension.configuration.MaxHealthMultipliers","Max Health Multipliers");
        add("ascension.configuration.minor_realm_max_health_increase","Minor Realm Increase");
        add("ascension.configuration.major_realm_max_health_increase","Major Realm Increase");
        add("ascension.configuration.max_health_applicable_realms","Applicable Realms");

        add("ascension.configuration.JumpStrengthMultipliers","Jump Strength Multipliers");
        add("ascension.configuration.minor_realm_jump_strength_increase","Minor Realm Increase");
        add("ascension.configuration.major_realm_jump_strength_increase","Major Realm Increase");
        add("ascension.configuration.jump_strength_applicable_realms","Applicable Realms");

        add("ascension.configuration.MovementSpeedMultipliers","Movement Speed Multipliers");
        add("ascension.configuration.minor_realm_movement_speed_increase","Minor Realm Increase");
        add("ascension.configuration.major_realm_movement_speed_increase","Major Realm Increase");
        add("ascension.configuration.movement_speed_applicable_realms","Applicable Realms");


        add("ascension.configuration.StartingPhysiqueOptions","Starting Physique Options");
        add("ascension.configuration.intent_options","Intent Options");
        add("ascension.configuration.body_options","Body Options");
        add("ascension.configuration.essence_options","Essence Options");

        add("ascension.configuration.CultivationModifiers","Cultivation Modifiers");
        add("ascension.configuration.essence_path_modifier","Essence Options");
        add("ascension.configuration.body_path_modifier","Body Options");
        add("ascension.configuration.intent_path_modifier","Intent Options");


    }
}
