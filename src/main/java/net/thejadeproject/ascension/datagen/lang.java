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



        //Commands
        add("sect.info.power", "Power: %s/%s");
        add("sect.info.total_deposited", "Total Deposited: %s");
        add("sect.claim.insufficient_power", "Your sect doesn't have enough power to claim this chunk");
        add("sect.claim.cannot_overclaim", "Cannot overclaim %s sect! You need more than %s power (you have %s)");


        add("sect.deposit.opening", "Opening Deposit Vault");
        add("container.sect_deposit", "Sect Deposit Vault");


        //Advancements
        add("advancements.ascension.root.install", "Ascension Loaded");
        add("advancements.ascension.root.welcome", "Welcome to the world of Ascension! We hope you will cultivate to immortality!");


        //Death msgs
        add("death.attack.pill_residue", "%1$s finally learned not to eat Pill Residue leftover in the Pill Cauldron");

        //GuideBook
        add("book.title.ascension.art_of_ascension", "Ascension Guide");
        add("book.subtitle.ascension.art_of_ascension", "Guide to Ascendance");

        //Attributes & Technical Shit


        //Items
        add("item.ascension.jade_slip", "Jade Slip");

        //Tools and Swords and Armor
        add("item.ascension.wooden_blade", "Wooden Blade");
        add("item.ascension.stone_blade", "Stone Blade");
        add("item.ascension.iron_blade", "Iron Blade");
        add("item.ascension.gold_blade", "Gold Blade");
        add("item.ascension.diamond_blade", "Diamond Blade");
        add("item.ascension.netherite_blade", "Netherite Blade");
        add("item.ascension.jade_blade", "Jade Blade");
        add("item.ascension.searing_blade", "Searing Blade");

        add("item.ascension.wooden_spear", "Wooden Spear");
        add("item.ascension.stone_spear", "Stone Spear");
        add("item.ascension.iron_spear", "Iron Spear");
        add("item.ascension.gold_spear", "Gold Spear");
        add("item.ascension.diamond_spear", "Diamond Spear");
        add("item.ascension.netherite_spear", "Netherite Spear");
        add("item.ascension.jade_spear", "Jade Spear");


        //Misc
        add("item.ascension.spiritual_stone", "Spiritual Stone");
        add("item.ascension.raw_black_iron", "Raw Black Iron");
        add("item.ascension.black_iron_ingot", "Black Iron Ingot");
        add("item.ascension.black_iron_nugget", "Black Iron Nugget");

        add("item.ascension.raw_frost_silver", "Raw Frost Silver");
        add("item.ascension.frost_silver_ingot", "Frost Silver Ingot");
        add("item.ascension.frost_silver_nugget", "Frost Silver Nugget");

        add("item.ascension.jade", "Jade");
        add("item.ascension.jade_nugget", "Jade Nugget");
        add("item.ascension.ascension_icon", "Ascension Icon");
        add("item.ascension.undead_core", "Undead Core");
        add("item.ascension.living_core", "Living Core");


        //Spiritual Fires
        add("item.ascension.crimson_lotus_flame", "Crimson Lotus Flame");
        add("item.ascension.flame", "Flame");
        add("item.ascension.soul_flame", "Soul Flame");



        //Physiques
        add("item.ascension.physique_slip", "Physique Slip");




        add("item.ascension.rat_spawn_egg", "Treasure Rat Spawn Egg");

        //Mobs
        add("entity.ascension.treasure_rat", "Treasure Rat");


        //Villagers
        add("entity.minecraft.villager.ascension.herbalist", "Herbalist");
        add("entity.minecraft.villager.herbalist", "Herbalist");


        //Pills
        add("item.ascension.pill_residue", "Pill Residue");
        add("item.ascension.regeneration_pill", "Regeneration Pill");
        add("item.ascension.neutrality_pill", "Neutrality Pill");
        add("item.ascension.rebirth_pill", "Rebirth Pill");
        add("item.ascension.body_amnesia_pill", "Body Amnesia Pill");
        add("item.ascension.antidote_pill_qdppill", "Antidote Pill");
        add("item.ascension.antidote_pill_t2", "Antidote Pill");
        add("item.ascension.antidote_pill_t3", "Antidote Pill");
        add("item.ascension.fasting_pill_t1", "Fasting Pill");
        add("item.ascension.fasting_pill_t2", "Fasting Pill");
        add("item.ascension.fasting_pill_t3", "Fasting Pill");
        add("item.ascension.cleansing_pill_t1", "Cleansing Pill");
        add("item.ascension.cleansing_pill_t2", "Cleansing Pill");
        add("item.ascension.cleansing_pill_t3", "Cleansing Pill");
        add("item.ascension.cleansing_pill_t4", "Cleansing Pill");

        add("item.ascension.inner_reinforcement_pill_t1", "Inner Reinforcement Pill");
        add("item.ascension.inner_reinforcement_pill_t2", "Inner Reinforcement Pill");
        add("item.ascension.inner_reinforcement_pill_t3", "Inner Reinforcement Pill");

        add("item.ascension.essence_gathering_pill_t1", "Essence Gathering Pill");
        add("item.ascension.essence_gathering_pill_t2", "Essence Gathering Pill");
        add("item.ascension.essence_gathering_pill_t3", "Essence Gathering Pill");

        add("item.ascension.spirit_focus_pill_t1", "Spirit Focus Pill");
        add("item.ascension.spirit_focus_pill_t2", "Spirit Focus Pill");
        add("item.ascension.spirit_focus_pill_t3", "Spirit Focus Pill");



        //Tooltips
        add("ascension.tooltip.waste", "Waste");
        add("ascension.tooltip.medicinal", "Medicinal");
        add("ascension.tooltip.cultivation", "Cultivation");
        add("ascension.tooltip.poisonous", "Poisonous");
        add("ascension.tooltip.srtt1", "2.5k x 2.5k Range │ 60 min cooldown");
        add("ascension.tooltip.srtt2", "5k x 5k Range │ 40 min cooldown");
        add("ascension.tooltip.srtt3", "7.5k x 7.5k Range │ 20 min cooldown");
        add("ascension.tooltip.srt", "5 min cooldown");
        add("ascension.tooltip.wat", "5 min cooldown");
        add("ascension.tooltip.vmt", "30 sec cooldown");
        add("ascension.tooltip.qdppill", "Qi Devouring Parasite Antidote");
        add("item.ascension.soulstead_return_talisman.cooldown", "Soulstead Return Talisman §7(%dm %ds§7)");
        add("item.ascension.void_marking_talisman.cooldown", "Void Marking Talisman §7(%dm %ds§7)");
        add("item.ascension.world_axis_talisman.cooldown", "World Axis Talisman §7(%dm %ds§7)");
        add("ascension.tooltip.teleporting_in_seconds", "§eTeleporting in %d seconds");
        add("ascension.tooltip.teleported_to_spawn", "§aTeleported to your spawn point!");
        add("ascension.tooltip.teleported_to_world_spawn", "§aTeleported to world spawn!");
        add("ascension.tooltip.teleported_to_saved", "§aTeleported to saved location!");
        add("ascension.tooltip.teleported_to_location", "§aTeleported to a random location!");
        add("ascension.tooltip.teleport_cancelled", "§cTeleport cancelled: %s");
        add("ascension.tooltip.damage_taken", "damage taken");
        add("ascension.tooltip.movement_detected", "movement detected");
        add("ascension.tooltip.location_saved", "§aLocation saved!");
        add("ascension.tooltip.no_location_saved", "§cNo location saved! Shift-right-click to save current position.");
        add("ascension.tooltip.failed_read_location", "§cFailed to read saved location!");
        add("ascension.tooltip.cannot_teleport_dimension", "§cCannot teleport to saved dimension!");
        add("ascension.tooltip.no_safe_location", "§cNo safe teleport location found!");
        add("ascension.tooltip.saved_location", "§7Saved Location:");
        add("ascension.tooltip.coordinates", "§7X: %s §7Y: %s §7Z: %s");
        add("ascension.tooltip.dimension", "§7Dimension: %s");
        add("item.ascension.spatial_rupture_talisman_t1.cooldown", "Spatial Rupture Talisman §7(%dm %ds§7)");
        add("item.ascension.spatial_rupture_talisman_t2.cooldown", "Spatial Rupture Talisman §7(%dm %ds§7)");
        add("item.ascension.spatial_rupture_talisman_t3.cooldown", "Spatial Rupture Talisman §7(%dm %ds§7)");
        add("item.ascension.tablet_of_destruction_human.cooldown", "Ability is on cooldown!");
        add("item.ascension.tablet_of_destruction_heaven.cooldown", "Item is on cooldown!");
        add("item.ascension.tablet_of_destruction_heaven.drop_blocks", "Drop Blocks = ");
        add("ascension.tablet.drop_blocks", "Drop Blocks = ");
        add("item.ascension.tablet_of_destruction_heaven.toggle_mode", "Press 'M' to toggle mode");
        add("ascension.tablet.toggle_mode", "Press 'M' to toggle mode ");
        add("item.ascension.tablet_of_destruction_heaven.link_instruction", "Shift+Right-Click chest/barrel to link");
        add("item.ascension.tablet_of_destruction_heaven.unlinked", "Unlinked from chest or barrel.");
        add("item.ascension.tablet_of_destruction_heaven.linked", "Linked to %s at (%d, %d, %d)");
        add("item.ascension.tablet_of_destruction_heaven.linked_tooltip", "Linked to %s at %s");
        add("item.ascension.tablet_of_destruction_heaven.chest", "chest");
        add("item.ascension.tablet_of_destruction_heaven.barrel", "barrel");
        add("item.ascension.tablet_of_destruction_heaven.container", "container");
        add("ascension.tooltip.tablet.coordinates", "§7X: %s, Y: %s, Z: %s");
        add("item.ascension.jade_slip.already_linked", "This Jade Slip is already linked to %s and cannot be re-linked!");
        add("item.ascension.jade_slip.linked", "Jade Slip linked to %s");
        add("item.ascension.jade_slip.tooltip.linked", "Linked to: %s");
        add("ascension.tooltip.rgb.jade_slip", "Sect Identification");
        add("ascension.tooltip.rgb.rebirth_warning", "Be Cautious This Pill Resets Everything!");




        //Poison Pills
        add("item.ascension.qi_devouring_parasite_pill", "Qi Devouring Parasite Pill");

        //manuals

        //Essence
        add("item.ascension.pure_fire_technique","Pure Fire Technique");
        add("item.ascension.pure_water_technique","Pure Water Technique");
        add("item.ascension.pure_wood_technique","Pure Wood Technique");
        add("item.ascension.pure_earth_technique","Pure Earth Technique");
        add("item.ascension.pure_metal_technique","Pure Metal Technique");
        add("item.ascension.void_swallowing_technique","Void Swallowing Technique");

        //Intent
        add("item.ascension.pure_sword_intent","Pure Sword Intent Technique");
        add("item.ascension.pure_fist_intent","Pure Fist Intent Technique");
        add("item.ascension.pure_axe_intent","Pure Axe Intent Technique");
        add("item.ascension.pure_spear_intent","Pure Spear Intent Technique");
        add("item.ascension.pure_blade_intent","Pure Blade Intent Technique");
        add("item.ascension.fist_king_intent","Fist Kings Technique");

        //Body

        add("item.ascension.wood_elemental_technique","Wood Elemental Technique");
        add("item.ascension.water_elemental_technique","Water Elemental Technique");
        add("item.ascension.earth_elemental_technique","Earth Elemental Technique");
        add("item.ascension.metal_elemental_technique","Metal Elemental Technique");
        add("item.ascension.fire_elemental_technique","Fire Elemental Technique");
        add("item.ascension.divine_phoenix_technique","Divine Phoenix Technique");


        add("ascension.learnt_technique","You have learned the %s");


        //Herbs
        add("item.ascension.golden_sun_leaf", "Golden Sun Leaf");
        add("item.ascension.ironwood_sprout", "Ironwood Sprout");
        add("item.ascension.white_jade_orchid", "White Jade Orchid");

        add("item.ascension.hundred_year_ginseng", "Hundred Year Ginseng");
        add("item.ascension.hundred_year_snow_ginseng", "Hundred Year Snow Ginseng");
        add("item.ascension.hundred_year_fire_ginseng", "Hundred Year Fire Ginseng");
        add("item.ascension.jade_bamboo_of_serenity", "Jade Bamboo of Serenity");


        add("block.ascension.hundred_year_ginseng_crop", "Hundred Year Ginseng");
        add("block.ascension.hundred_year_fire_ginseng_crop", "Hundred Year Fire Ginseng");
        add("block.ascension.hundred_year_snow_ginseng_crop", "Hundred Year Snow Ginseng");
        add("block.ascension.ironwood_sprout_crop", "Ironwood Sprout");
        add("block.ascension.white_jade_orchid_crop", "White Jade Orchid");

        //Blocks
        add("block.ascension.pill_cauldron", "Pill Cauldron");
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




        add("block.ascension.black_iron_ore", "Black Iron Ore");
        add("block.ascension.black_iron_block", "Black Iron Block");
        add("block.ascension.frost_silver_ore", "Frost Silver Ore");
        add("block.ascension.frost_silver_block", "Frost Silver Block");

        add("block.ascension.jade_ore", "Jade Ore");
        add("block.ascension.spiritual_stone_cluster", "Spiritual Stone Cluster");
        add("block.ascension.jade_block", "Jade Block");
        add("block.ascension.spiritual_stone_block", "Spiritual Stone Block");
        add("block.ascension.pill_cauldron_low_human", "Pill Cauldron");


        //Fires
        add("block.ascension.crimson_lotus_fire", "Crimson Lotus Fire");


        //Artifacts
        add("item.ascension.iron_spatial_ring", "Iron Spatial Ring");
        add("item.ascension.gold_spatial_ring", "Gold Spatial Ring");
        add("item.ascension.diamond_spatial_ring", "Diamond Spatial Ring");
        add("item.ascension.netherite_spatial_ring", "Netherite Spatial Ring");
        add("item.ascension.jade_spatial_ring", "Jade Spatial Ring");
        add("item.ascension.spiritual_stone_spatial_ring", "Spiritual Spatial Ring");
        add("item.ascension.fire_gourd", "Gourd O Fire");
        add("item.ascension.spatial_rupture_talisman_t1", "Lesser Spatial Rupture Talisman");
        add("item.ascension.spatial_rupture_talisman_t2", "Standard Spatial Rupture Talisman");
        add("item.ascension.spatial_rupture_talisman_t3", "Greater Spatial Rupture Talisman");
        add("item.ascension.soulstead_return_talisman", "Soulstead Return Talisman");
        add("item.ascension.world_axis_talisman", "World Axis Talisman");
        add("item.ascension.void_marking_talisman", "Void-Marking Talisman");
        add("item.ascension.todh", "Tablet Of Destruction");
        add("item.ascension.tode", "Tablet Of Destruction");
        add("item.ascension.todhe", "Tablet Of Destruction");
        add("item.ascension.repair_slip", "Repair Slip");
        add("item.ascension.ender_pouch_gui", "Ender Pouch");
        add("item.ascension.ender_pouch", "Ender Pouch");


        //Crafting Ingredient
        add("item.ascension.talisman_paper", "Blank Talisman");


        //GUI & Other Stuff
        add("creativetab.ascension.items", "Ascension Items");
        add("creativetab.ascension.artifacts", "Ascension Artifacts");
        add("creativetab.ascension.tools", "Ascension Tools");
        add("creativetab.ascension.blocks", "Ascension Blocks");
        add("creativetab.ascension.herbs", "Ascension Herbs");
        add("creativetab.ascension.pills", "Ascension Pills");
        add("creativetab.ascension.manuals", "Ascension Manuals");




        //JADE
        add("config.jade.plugin_ascension.pill_cauldron", "Pill Cauldron");
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
        add("effect.ascension.neutrality", "Neutrality");
        add("effect.ascension.qi_devouring_parasite", "Devouring Parasite");

        add("category.ascension.cultivation", "Ascension");
        add("key.ascension.cultivate", "Cultivate");
        add("key.ascension.introspection", "Introspection");
        add("key.ascension.open_spatial_ring", "Open Spatial Ring");
        add("key.ascension.toggle_artifact_mode", "Toggle Mode");
        add("key.ascension.cast_skill", "Cast Skill");
        add("key.ascension.skill_menu", "Open Skill Menu");
        add("key.ascension.skill_wheel", "Skill Wheel");



        //CONFIG
        add("ascension.configuration.Multipliers", "Stats Multipliers");
        add("ascension.configuration.CultivationMultipliers", "Cultivation Multipliers");
        add("ascension.configuration.Cultivation_Speed", "Cultivation Speed");
        add("ascension.configuration.Minor_Cultivation_Stats_Multiplier", "Minor Cultivation Stats Multiplier");
        add("ascension.configuration.Major_Cultivation_Stats_Multiplier", "Major Cultivation Stats Multiplier");
        add("ascension.configuration.Flight_Realm", "Flight Realm");

        add("ascension.configuration.AttributeMultipliers", "Attributes Multipliers");
        add("ascension.configuration.Speed_Multiplier_Max", "Max Speed");

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

        add("ascension.configuration.PillCauldron","Pill Cauldron");
        add("ascension.configuration.heat_items","Heat Items");
        add("ascension.configuration.heat_loss_interval","Heat Loss Interval");
        add("ascension.configuration.heat_loss_amount","Heat Loss Per Interval");
        add("ascension.configuration.max_heat","Max Heat");

        add("ascension.configuration.Artifacts","Artifacts");
        add("ascension.configuration.repairInterval","Repair Interval");
        add("ascension.configuration.repairAmount","Repair Amount");


        //messages
        add("message.ascension.milk_denied","§cMilk buckets are not drinkable in this mod. Use antidote pills instead!");


        //tags'



        //Skills
        add("ascension.physique.passive.stonehide", "Stonehide");
        add("ascension.physique.passive.stonehide.desc1", "§6Stone Monkey's Primitive Defense");
        add("ascension.physique.passive.stonehide.desc2", "§7Your flesh hardens like unhewn stone.");
        add("ascension.physique.passive.stonehide.desc3", "");
        add("ascension.physique.passive.stonehide.desc4", "§e◆ §f+5% Damage Reduction");
        add("ascension.physique.passive.stonehide.desc5", "§e◆ §f+100% Knockback Resistance");
        add("ascension.physique.passive.stonehide.desc6", "§e◆ §fScales with Body Path cultivation");

        add("ascension.physique.passive.diamond_adamant", "Diamond Adamant");
        add("ascension.physique.passive.diamond_adamant.desc1", "§6Heaven-Forged Resilience\"");
        add("ascension.physique.passive.diamond_adamant.desc2", "§7Your body becomes as hard as diamond.");
        add("ascension.physique.passive.diamond_adamant.desc3", "");
        add("ascension.physique.passive.diamond_adamant.desc4", "§e◆ §fCritical hits deal 50% less damage");
        add("ascension.physique.passive.diamond_adamant.desc5", "§e◆ §fChance to survive fatal blows at 1 HP");
        add("ascension.physique.passive.diamond_adamant.desc6", "§c◆ §f5 minute cooldown on survival");

        add("ascension.physique.active.indestructible_vajra", "Indestructible Vajra");
        add("ascension.physique.active.indestructible_vajra.desc1", "§6Ultimate Stone Monkey Defense");
        add("ascension.physique.active.indestructible_vajra.desc2", "§7Become truly invulnerable for a short time.");
        add("ascension.physique.active.indestructible_vajra.desc3", "");
        add("ascension.physique.active.indestructible_vajra.desc4", "§e◆ §fBecome completely invulnerable");
        add("ascension.physique.active.indestructible_vajra.desc5", "§e◆ §fStore all damage taken");
        add("ascension.physique.active.indestructible_vajra.desc6", "§e◆ §fRelease stored damage as shockwave");
        add("ascension.physique.active.indestructible_vajra.desc7", "§c◆ §fCost: 100 Qi");
        add("ascension.physique.active.indestructible_vajra.desc8", "§c◆ §fCooldown: 2 minutes");

    }
}
