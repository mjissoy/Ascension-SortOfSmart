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

        //Attributes & Technical Shit


        //Items
        add("item.ascension.jade_slip", "Jade Slip");
        add("tooltip.ascension.jade_slip", "Sect Identification");

        //Tools and Swords and Armor
        add("item.ascension.jade_blade", "Jade Serpent's Fang");
        add("item.ascension.searing_blade", "Fang of the Phoenix");
        add("item.ascension.jade_spear", "Sting of the Jade Serpent");


        //Misc
        add("item.ascension.spiritual_stone", "Spiritual Stone");
        add("item.ascension.jade", "Jade");
        add("item.ascension.jade_nugget", "Jade Nugget");
        add("item.ascension.ascension_icon", "Ascension Icon");
        add("item.ascension.undead_core", "Undead Core");
        add("item.ascension.living_core", "Living Core");
        add("item.ascension.crimson_lotus_flame", "Crimson Lotus Flame");
        add("item.ascension.rat_spawn_egg", "Treasure Rat Spawn Egg");

        //Mobs
        add("entity.mcourse.rat", "Treasure Rat");


        //Pills
        add("item.ascension.regeneration_pill", "Regeneration Pill");
        add("item.ascension.cleansing_pill", "Cleansing Pill");
        add("item.ascension.rebirth_pill", "Rebirth Pill");
        add("tooltip.ascension.rebirth_pill", "Be Cautios This Pill Resets Everything!");
        add("item.ascension.fasting_pill_t1", "Fasting Pill");
        add("item.ascension.fasting_pill_t2", "Fasting Pill");
        add("item.ascension.fasting_pill_t3", "Fasting Pill");

        //manuals
        add("item.ascension.pure_fire_technique","Pure Fire Technique Manual");
        add("item.ascension.pure_water_technique","Pure Water Technique Manual");
        add("item.ascension.pure_sword_intent","Pure Sword Intent Manual");
        add("item.ascension.pure_fist_intent","Pure Fist Intent Manual");
        add("item.ascension.divine_phoenix_technique","Divine Phoenix Technique Manual");
        add("item.ascension.void_swallowing_technique","Void Swallowing Technique Manual");


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

        /** Marble */
        add("block.ascension.raw_marble", "Marble");
        add("block.ascension.polished_marble", "Polished Marble");
        add("block.ascension.marble_bricks", "Marble Bricks");
        add("block.ascension.marble_chiseled", "Chiseled Marble");
        add("block.ascension.marble_tiles", "Marble Tiles");
        add("block.ascension.marble_burned", "Black Marble");
        add("block.ascension.polished_burned_marble", "Polished Black Marble");
        add("block.ascension.marble_burned_bricks", "Black Marble Bricks");
        add("block.ascension.marble_burned_chiseled", "Black Chiseled Marble");
        add("block.ascension.marble_burned_tiles", "Black Marble Tiles");
        add("block.ascension.blue_marble", "Blue Marble");
        add("block.ascension.blue_marble_bricks", "Blue Marble Bricks");
        add("block.ascension.blue_marble_chiseled", "Chiseled Blue Marble");
        add("block.ascension.blue_marble_tiles", "Blue Marble Tiles");
        add("block.ascension.blue_polished_marble", "Polished Blue Marble");
        add("block.ascension.brown_marble", "Brown Marble");
        add("block.ascension.brown_marble_bricks", "Brown Marble Bricks");
        add("block.ascension.brown_marble_chiseled", "Chiseled Brown Marble");
        add("block.ascension.brown_marble_tiles", "Brown Marble Tiles");
        add("block.ascension.brown_polished_marble", "Polished Brown Marble");
        add("block.ascension.cyan_marble", "Cyan Marble");
        add("block.ascension.cyan_marble_bricks", "Cyan Marble Bricks");
        add("block.ascension.cyan_marble_chiseled", "Chiseled Cyan Marble");
        add("block.ascension.cyan_marble_tiles", "Cyan Marble Tiles");
        add("block.ascension.cyan_polished_marble", "Polished Cyan Marble");
        add("block.ascension.gray_marble", "Gray Marble");
        add("block.ascension.gray_marble_bricks", "Gray Marble Bricks");
        add("block.ascension.gray_marble_chiseled", "Chiseled Gray Marble");
        add("block.ascension.gray_marble_tiles", "Gray Marble Tiles");
        add("block.ascension.gray_polished_marble", "Polished Gray Marble");
        add("block.ascension.green_marble", "Green Marble");
        add("block.ascension.green_marble_bricks", "Green Marble Bricks");
        add("block.ascension.green_marble_chiseled", "Chiseled Green Marble");
        add("block.ascension.green_marble_tiles", "Green Marble Tiles");
        add("block.ascension.green_polished_marble", "Polished Green Marble");
        add("block.ascension.light_blue_marble", "Light Blue Marble");
        add("block.ascension.light_blue_marble_bricks", "Light Blue Marble Bricks");
        add("block.ascension.light_blue_marble_chiseled", "Chiseled Light Blue Marble");
        add("block.ascension.light_blue_marble_tiles", "Light Blue Marble Tiles");
        add("block.ascension.light_blue_polished_marble", "Polished Light Blue Marble");
        add("block.ascension.light_gray_marble", "Light Gray Marble");
        add("block.ascension.light_gray_marble_bricks", "Light Gray Marble Bricks");
        add("block.ascension.light_gray_marble_chiseled", "Chiseled Light Gray Marble");
        add("block.ascension.light_gray_marble_tiles", "Light Gray Marble Tiles");
        add("block.ascension.polished_light_gray_marble", "Polished Light Gray Marble");
        add("block.ascension.lime_marble", "Lime Marble");
        add("block.ascension.lime_marble_bricks", "Lime Marble Bricks");
        add("block.ascension.lime_marble_chiseled", "Chiseled Lime Marble");
        add("block.ascension.lime_marble_tiles", "Lime Marble Tiles");
        add("block.ascension.lime_polished_marble", "Polished Lime Marble");
        add("block.ascension.magenta_marble", "Magenta Marble");
        add("block.ascension.magenta_marble_bricks", "Magenta Marble Bricks");
        add("block.ascension.magenta_marble_chiseled", "Chiseled Magenta Marble");
        add("block.ascension.magenta_marble_tiles", "Magenta Marble Tiles");
        add("block.ascension.magenta_polished_marble", "Polished Magenta Marble");
        add("block.ascension.orange_marble", "Orange Marble");
        add("block.ascension.orange_marble_bricks", "Orange Marble Bricks");
        add("block.ascension.orange_marble_chiseled", "Chiseled Orange Marble");
        add("block.ascension.orange_marble_tiles", "Orange Marble Tiles");
        add("block.ascension.orange_polished_marble", "Polished Orange Marble");
        add("block.ascension.pink_marble", "Pink Marble");
        add("block.ascension.pink_marble_bricks", "Pink Marble Bricks");
        add("block.ascension.pink_marble_chiseled", "Chiseled Pink Marble");
        add("block.ascension.pink_marble_tiles", "Pink Marble Tiles");
        add("block.ascension.pink_polished_marble", "Polished Pink Marble");
        add("block.ascension.purple_marble", "Purple Marble");
        add("block.ascension.purple_marble_bricks", "Purple Marble Bricks");
        add("block.ascension.purple_marble_chiseled", "Chiseled Purple Marble");
        add("block.ascension.purple_marble_tiles", "Purple Marble Tiles");
        add("block.ascension.purple_polished_marble", "Polished Purple Marble");
        add("block.ascension.red_marble", "Red Marble");
        add("block.ascension.red_marble_bricks", "Red Marble Bricks");
        add("block.ascension.red_marble_chiseled", "Chiseled Red Marble");
        add("block.ascension.red_marble_tiles", "Red Marble Tiles");
        add("block.ascension.red_polished_marble", "Polished Red Marble");
        add("block.ascension.yellow_marble", "Yellow Marble");
        add("block.ascension.yellow_marble_bricks", "Yellow Marble Bricks");
        add("block.ascension.yellow_marble_chiseled", "Chiseled Yellow Marble");
        add("block.ascension.yellow_marble_tiles", "Yellow Marble Tiles");
        add("block.ascension.yellow_polished_marble", "Polished Yellow Marble");


        /** Slabs */
        add("block.ascension.marble_brick_slabs", "Marble Brick Slab");
        add("block.ascension.marble_tile_slabs", "Marble Tile Slab");
        add("block.ascension.burned_marble_brick_slabs", "Black Marble Brick Slab");
        add("block.ascension.burned_marble_tile_slabs", "Black Marble Tile Slab");
        add("block.ascension.blue_marble_brick_slabs", "Blue Marble Brick Slab");
        add("block.ascension.blue_marble_tile_slabs", "Blue Marble Tile Slab");
        add("block.ascension.brown_marble_brick_slabs", "Brown Marble Brick Slab");
        add("block.ascension.brown_marble_tile_slabs", "Brown Marble Tile Slab");
        add("block.ascension.cyan_marble_brick_slabs", "Cyan Marble Brick Slab");
        add("block.ascension.cyan_marble_tile_slabs", "Cyan Marble Tile Slab");
        add("block.ascension.gray_marble_brick_slabs", "Gray Marble Brick Slab");
        add("block.ascension.gray_marble_tile_slabs", "Gray Marble Tile Slab");
        add("block.ascension.green_marble_brick_slabs", "Green Marble Brick Slab");
        add("block.ascension.green_marble_tile_slabs", "Green Marble Tile Slab");
        add("block.ascension.light_blue_marble_brick_slabs", "Light Blue Marble Brick Slab");
        add("block.ascension.light_blue_marble_tile_slabs", "Light Blue Marble Tile Slab");
        add("block.ascension.light_gray_marble_brick_slabs", "Light Gray Marble Brick Slab");
        add("block.ascension.light_gray_marble_tile_slabs", "Light Gray Marble Tile Slab");
        add("block.ascension.lime_marble_brick_slabs", "Lime Marble Brick Slab");
        add("block.ascension.lime_marble_tile_slabs", "Lime Marble Tile Slab");
        add("block.ascension.magenta_marble_brick_slabs", "Magenta Marble Brick Slab");
        add("block.ascension.magenta_marble_tile_slabs", "Magenta Marble Tile Slab");
        add("block.ascension.orange_marble_brick_slabs", "Orange Marble Brick Slab");
        add("block.ascension.orange_marble_tile_slabs", "Orange Marble Tile Slab");
        add("block.ascension.pink_marble_brick_slabs", "Pink Marble Brick Slab");
        add("block.ascension.pink_marble_tile_slabs", "Pink Marble Tile Slab");
        add("block.ascension.purple_marble_brick_slabs", "Purple Marble Brick Slab");
        add("block.ascension.purple_marble_tile_slabs", "Purple Marble Tile Slab");
        add("block.ascension.red_marble_brick_slabs", "Red Marble Brick Slab");
        add("block.ascension.red_marble_tile_slabs", "Red Marble Tile Slab");
        add("block.ascension.yellow_marble_brick_slabs", "Yellow Marble Brick Slab");
        add("block.ascension.yellow_marble_tile_slabs", "Yellow Marble Tile Slab");

        /** Stairs */
        add("block.ascension.marble_brick_stairs", "Marble Brick Stair");
        add("block.ascension.marble_tile_stairs", "Marble Tile Stair");
        add("block.ascension.burned_marble_brick_stairs", "Black Marble Brick Stair");
        add("block.ascension.burned_marble_tile_stairs", "Black Marble Tile Stair");
        add("block.ascension.blue_marble_brick_stairs", "Blue Marble Brick Stairs");
        add("block.ascension.blue_marble_tile_stairs", "Blue Marble Tile Stairs");
        add("block.ascension.brown_marble_brick_stairs", "Brown Marble Brick Stairs");
        add("block.ascension.brown_marble_tile_stairs", "Brown Marble Tile Stairs");
        add("block.ascension.cyan_marble_brick_stairs", "Cyan Marble Brick Stairs");
        add("block.ascension.cyan_marble_tile_stairs", "Cyan Marble Tile Stairs");
        add("block.ascension.gray_marble_brick_stairs", "Gray Marble Brick Stairs");
        add("block.ascension.gray_marble_tile_stairs", "Gray Marble Tile Stairs");
        add("block.ascension.green_marble_brick_stairs", "Green Marble Brick Stairs");
        add("block.ascension.green_marble_tile_stairs", "Green Marble Tile Stairs");
        add("block.ascension.light_blue_marble_brick_stairs", "Light Blue Marble Brick Stairs");
        add("block.ascension.light_blue_marble_tile_stairs", "Light Blue Marble Tile Stairs");
        add("block.ascension.light_gray_marble_brick_stairs", "Light Gray Marble Brick Stairs");
        add("block.ascension.light_gray_marble_tile_stairs", "Light Gray Marble Tile Stairs");
        add("block.ascension.lime_marble_brick_stairs", "Lime Marble Brick Stairs");
        add("block.ascension.lime_marble_tile_stairs", "Lime Marble Tile Stairs");
        add("block.ascension.magenta_marble_brick_stairs", "Magenta Marble Brick Stairs");
        add("block.ascension.magenta_marble_tile_stairs", "Magenta Marble Tile Stairs");
        add("block.ascension.orange_marble_brick_stairs", "Orange Marble Brick Stairs");
        add("block.ascension.orange_marble_tile_stairs", "Orange Marble Tile Stairs");
        add("block.ascension.pink_marble_brick_stairs", "Pink Marble Brick Stairs");
        add("block.ascension.pink_marble_tile_stairs", "Pink Marble Tile Stairs");
        add("block.ascension.purple_marble_brick_stairs", "Purple Marble Brick Stairs");
        add("block.ascension.purple_marble_tile_stairs", "Purple Marble Tile Stairs");
        add("block.ascension.red_marble_brick_stairs", "Red Marble Brick Stairs");
        add("block.ascension.red_marble_tile_stairs", "Red Marble Tile Stairs");
        add("block.ascension.yellow_marble_brick_stairs", "Yellow Marble Brick Stairs");
        add("block.ascension.yellow_marble_tile_stairs", "Yellow Marble Tile Stairs");

        /** Walls */
        add("block.ascension.marble_brick_wall", "Marble Brick Wall");
        add("block.ascension.marble_tile_wall", "Marble Tile Wall");
        add("block.ascension.burned_marble_brick_wall", "Black Marble Brick Wall");
        add("block.ascension.burned_marble_tile_wall", "Black Marble Tile Wall");
        add("block.ascension.blue_marble_brick_wall", "Blue Marble Brick Wall");
        add("block.ascension.blue_marble_tile_wall", "Blue Marble Tile Wall");
        add("block.ascension.brown_marble_brick_wall", "Brown Marble Brick Wall");
        add("block.ascension.brown_marble_tile_wall", "Brown Marble Tile Wall");
        add("block.ascension.cyan_marble_brick_wall", "Cyan Marble Brick Wall");
        add("block.ascension.cyan_marble_tile_wall", "Cyan Marble Tile Wall");
        add("block.ascension.gray_marble_brick_wall", "Gray Marble Brick Wall");
        add("block.ascension.gray_marble_tile_wall", "Gray Marble Tile Wall");
        add("block.ascension.green_marble_brick_wall", "Green Marble Brick Wall");
        add("block.ascension.green_marble_tile_wall", "Green Marble Tile Wall");
        add("block.ascension.light_blue_marble_brick_wall", "Light Blue Marble Brick Wall");
        add("block.ascension.light_blue_marble_tile_wall", "Light Blue Marble Tile Wall");
        add("block.ascension.light_gray_marble_brick_wall", "Light Gray Marble Brick Wall");
        add("block.ascension.light_gray_marble_tile_wall", "Light Gray Marble Tile Wall");
        add("block.ascension.lime_marble_brick_wall", "Lime Marble Brick Wall");
        add("block.ascension.lime_marble_tile_wall", "Lime Marble Tile Wall");
        add("block.ascension.magenta_marble_brick_wall", "Magenta Marble Brick Wall");
        add("block.ascension.magenta_marble_tile_wall", "Magenta Marble Tile Wall");
        add("block.ascension.orange_marble_brick_wall", "Orange Marble Brick Wall");
        add("block.ascension.orange_marble_tile_wall", "Orange Marble Tile Wall");
        add("block.ascension.pink_marble_brick_wall", "Pink Marble Brick Wall");
        add("block.ascension.pink_marble_tile_wall", "Pink Marble Tile Wall");
        add("block.ascension.purple_marble_brick_wall", "Purple Marble Brick Wall");
        add("block.ascension.purple_marble_tile_wall", "Purple Marble Tile Wall");
        add("block.ascension.red_marble_brick_wall", "Red Marble Brick Wall");
        add("block.ascension.red_marble_tile_wall", "Red Marble Tile Wall");
        add("block.ascension.yellow_marble_brick_wall", "Yellow Marble Brick Wall");
        add("block.ascension.yellow_marble_tile_wall", "Yellow Marble Tile Wall");




        add("block.ascension.jade_ore", "Jade Ore");
        add("block.ascension.spiritual_stone_cluster", "Spiritual Stone Cluster");
        add("block.ascension.jade_block", "Jade Block");
        add("block.ascension.spiritual_stone_block", "Spiritual Stone Block");
        add("block.ascension.pill_cauldron_low_human", "Pill Cauldron");


        //Artifacts
        add("item.ascension.iron_spatial_ring", "Iron Spatial Ring");
        add("item.ascension.gold_spatial_ring", "Gold Spatial Ring");
        add("item.ascension.diamond_spatial_ring", "Diamond Spatial Ring");
        add("item.ascension.netherite_spatial_ring", "Netherite Spatial Ring");
        add("item.ascension.jade_spatial_ring", "Jade Spatial Ring");
        add("item.ascension.spiritual_stone_spatial_ring", "Spiritual Spatial Ring");

        add("item.ascension.todh", "Tablet Of Destruction");
        add("tooltip.ascension.todh", "§8[Human]");

        add("item.ascension.tode", "Tablet Of Destruction");
        add("tooltip.ascension.tode", "§6[Earth]");

        add("item.ascension.todhe", "Tablet Of Destruction");
        add("tooltip.ascension.todhe", "§b[Heaven]");

        add("item.ascension.repair_slip", "Repair Slip");
        add("tooltip.ascension.repair_slip.shift_down1", "§3Repairs items in your inventory");
        add("tooltip.ascension.repair_slip.shift_down2", "§3Every 100 Ticks it repairs items by 2 durability");
        add("tooltip.ascension.repair_slip", "§8Hold [§7Shift§8] for summary");

        add("item.ascension.ender_pouch_gui", "Ender Pouch");
        add("item.ascension.ender_pouch", "Ender Pouch");
        add("tooltip.ascension.ender_pouch.shift_down1", "§2The Ender Pouch");
        add("tooltip.ascension.ender_pouch.shift_down2", "§aIs linked to your own Ender Chest");
        add("tooltip.ascension.ender_pouch", "§8Hold [§7Shift§8] for summary");


        //GUI & Other Stuff
        add("creativetab.ascension.items", "Ascension Items");
        add("creativetab.ascension.artifacts", "Ascension Artifacts");
        add("creativetab.ascension.blocks", "Ascension Blocks");
        add("creativetab.ascension.herbs", "Ascension Herbs");
        add("creativetab.ascension.pills", "Ascension Pills");
        add("creativetab.ascension.manuals", "Ascension Manuals");




        //JADE
        add("tooltip.ascension.heat_level", "Heat: %s°C / %s°C");
        add("tooltip.ascension.progress", "Progress: %s%%");
        add("tooltip.ascension.time_remaining", "Time remaining: %ss");
        add("tooltip.ascension.finishing", "Finishing...");
        add("tooltip.ascension.output", "Output: %s");
        add("tooltip.ascension.inputs", "Inputs:");




        //JEI
        add("jei.ascension.pill_cauldron_low_human", "Pill Crafting");

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


        //tags

    }
}
