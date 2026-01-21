package net.thejadeproject.ascension.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.constants.CultivationSource;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.effects.ModEffects;
import net.thejadeproject.ascension.entity.ModEntities;
import net.thejadeproject.ascension.items.artifacts.*;
import net.thejadeproject.ascension.items.formations.PlayerAccessItemToken;
import net.thejadeproject.ascension.items.herbs.HundredYearFireGinseng;
import net.thejadeproject.ascension.items.herbs.HundredYearSnowGinseng;
import net.thejadeproject.ascension.items.herbs.PlantableHerb;
import net.thejadeproject.ascension.items.physiques.PhysiqueTransferItem;
import net.thejadeproject.ascension.items.pills.*;
import net.thejadeproject.ascension.items.stones.SpatialStoneItem;
import net.thejadeproject.ascension.items.tools.BladeItem;
import net.thejadeproject.ascension.items.tools.SpearItem;
import net.thejadeproject.ascension.util.ItemUtil;

import java.util.HashSet;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AscensionCraft.MOD_ID);


    public static final DeferredItem<Item> RIFT_SUMMONER_DEBUG_STICK = ITEMS.register("rift_debug_stick",
            () -> new ShaderSummonerItem(new Item.Properties()));


    //Spatial Ring Stuff

    public static final DeferredItem<Item> SPATIAL_STONE_TIER_1 = ITEMS.register("spatial_stone_tier_1",
            () -> new SpatialStoneItem(1, "I", Rarity.COMMON));
    public static final DeferredItem<Item> SPATIAL_STONE_TIER_2 = ITEMS.register("spatial_stone_tier_2",
            () -> new SpatialStoneItem(2, "II", Rarity.UNCOMMON));

    // Stack Upgrades Todo Fix upgrades
    /*public static final DeferredItem<Item> STACK_UPGRADE_T1 = ITEMS.register("stack_upgrade_t1",
            () -> new UpgradeItem("stack_upgrade_t1", Rarity.UNCOMMON));
    public static final DeferredItem<Item> STACK_UPGRADE_T2 = ITEMS.register("stack_upgrade_t2",
            () -> new UpgradeItem("stack_upgrade_t2", Rarity.RARE));

    // Pickup Upgrade
    public static final DeferredItem<Item> PICKUP_UPGRADE = ITEMS.register("pickup_upgrade",
            () -> new UpgradeItem("pickup_upgrade", Rarity.RARE));*/


    public static final DeferredItem<Item> SPATIAL_RING = ITEMS.register("spatial_ring",
            () -> new SpatialRingItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

















    public static final DeferredItem<Item> RAW_BLACK_IRON = ITEMS.register("raw_black_iron",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLACK_IRON_NUGGET = ITEMS.register("black_iron_nugget",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLACK_IRON_INGOT = ITEMS.register("black_iron_ingot",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAW_FROST_SILVER = ITEMS.register("raw_frost_silver",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FROST_SILVER_NUGGET = ITEMS.register("frost_silver_nugget",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FROST_SILVER_INGOT = ITEMS.register("frost_silver_ingot",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> JADE = ITEMS.register("jade",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> JADE_NUGGET = ITEMS.register("jade_nugget",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SPIRITUAL_STONE = ITEMS.register("spiritual_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));




    //Weapons & Tools
    public static final DeferredItem<BladeItem> WOODEN_BLADE = ITEMS.register("wooden_blade",
            () -> new BladeItem(Tiers.WOOD, new Item.Properties().durability(69).attributes(BladeItem.createAttributes((Tier) Tiers.WOOD, 2, (float) -2))));
    public static final DeferredItem<BladeItem> STONE_BLADE = ITEMS.register("stone_blade",
            () -> new BladeItem(Tiers.STONE, new Item.Properties().durability(141).attributes(BladeItem.createAttributes((Tier) Tiers.STONE, 2, (float) -2))));
    public static final DeferredItem<BladeItem> IRON_BLADE = ITEMS.register("iron_blade",
            () -> new BladeItem(Tiers.IRON, new Item.Properties().durability(260).attributes(BladeItem.createAttributes((Tier) Tiers.IRON, 2, (float) -2))));
    public static final DeferredItem<BladeItem> GOLD_BLADE = ITEMS.register("gold_blade",
            () -> new BladeItem(Tiers.GOLD, new Item.Properties().durability(42).attributes(BladeItem.createAttributes((Tier) Tiers.GOLD, 2f, (float) -2))));
    public static final DeferredItem<BladeItem> DIAMOND_BLADE = ITEMS.register("diamond_blade",
            () -> new BladeItem(Tiers.DIAMOND, new Item.Properties().durability(1661).attributes(BladeItem.createAttributes((Tier) Tiers.DIAMOND, 2, (float) -2))));
    public static final DeferredItem<BladeItem> NETHERITE_BLADE = ITEMS.register("netherite_blade",
            () -> new BladeItem(Tiers.NETHERITE, new Item.Properties().durability(2131).attributes(BladeItem.createAttributes((Tier) Tiers.NETHERITE, 2, (float) -2))));
    public static final DeferredItem<BladeItem> JADE_BLADE = ITEMS.register("jade_blade",
            () -> new BladeItem(ModToolTiers.JADE, new Item.Properties()
                    .attributes(BladeItem.createAttributes(ModToolTiers.JADE, 6, -2.2f))));
    public static final DeferredItem<BladeItem> SEARING_BLADE = ITEMS.register("searing_blade",
            () -> new BladeItem(ModToolTiers.JADE, new Item.Properties()
                    .attributes(BladeItem.createAttributes(ModToolTiers.JADE, 7, -2f))));


    public static final DeferredItem<SpearItem> WOODEN_SPEAR = ITEMS.register("wooden_spear",
            () -> new SpearItem(Tiers.WOOD, new Item.Properties().durability(49).attributes(ItemUtil.createAscensionItemAttributes(Tiers.WOOD, 3, -2.4f,0.5))));
    public static final DeferredItem<SpearItem> STONE_SPEAR = ITEMS.register("stone_spear",
            () -> new SpearItem(Tiers.STONE, new Item.Properties().durability(121).attributes(ItemUtil.createAscensionItemAttributes(Tiers.STONE, 3, -2.4f,1))));
    public static final DeferredItem<SpearItem> IRON_SPEAR = ITEMS.register("iron_spear",
            () -> new SpearItem(Tiers.IRON, new Item.Properties().durability(240).attributes(ItemUtil.createAscensionItemAttributes(Tiers.IRON, 3, -2.4f,1.5))));
    public static final DeferredItem<SpearItem> GOLD_SPEAR = ITEMS.register("gold_spear",
            () -> new SpearItem(Tiers.GOLD, new Item.Properties().durability(22).attributes(ItemUtil.createAscensionItemAttributes(Tiers.GOLD, 3, -2.4f,1.5))));
    public static final DeferredItem<SpearItem> DIAMOND_SPEAR = ITEMS.register("diamond_spear",
            () -> new SpearItem(Tiers.DIAMOND, new Item.Properties().durability(1461).attributes(ItemUtil.createAscensionItemAttributes(Tiers.DIAMOND, 3, -2.4f,2))));
    public static final DeferredItem<SpearItem> NETHERITE_SPEAR = ITEMS.register("netherite_spear",
            () -> new SpearItem(Tiers.NETHERITE, new Item.Properties().durability(1931).attributes(ItemUtil.createAscensionItemAttributes(Tiers.NETHERITE, 3, -2.4f,2))));

    public static final DeferredItem<SpearItem> JADE_SPEAR = ITEMS.register("jade_spear",
            () -> new SpearItem(ModToolTiers.JADE, new Item.Properties()
                    .attributes(ItemUtil.createAscensionItemAttributes(ModToolTiers.JADE, 6, -2.4f,3))
                  ));


    //Formation Items

    public static final DeferredItem<Item> FORMATION_SLIP_ACACIA = ITEMS.register("formation_slip_acacia",
            () -> new PlayerAccessItemToken(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FORMATION_SLIP_BAMBOO = ITEMS.register("formation_slip_bamboo",
            () -> new PlayerAccessItemToken(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FORMATION_SLIP_BIRCH = ITEMS.register("formation_slip_birch",
            () -> new PlayerAccessItemToken(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FORMATION_SLIP_CHERRY = ITEMS.register("formation_slip_cherry",
            () -> new PlayerAccessItemToken(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FORMATION_SLIP_CRIMSON = ITEMS.register("formation_slip_crimson",
            () -> new PlayerAccessItemToken(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FORMATION_SLIP_DARK_OAK = ITEMS.register("formation_slip_dark_oak",
            () -> new PlayerAccessItemToken(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FORMATION_SLIP_GOLDEN_PALM = ITEMS.register("formation_slip_golden_palm",
            () -> new PlayerAccessItemToken(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FORMATION_SLIP_IRONWOOD = ITEMS.register("formation_slip_ironwood",
            () -> new PlayerAccessItemToken(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FORMATION_SLIP_JUNGLE = ITEMS.register("formation_slip_jungle",
            () -> new PlayerAccessItemToken(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FORMATION_SLIP_MANGROVE = ITEMS.register("formation_slip_mangrove",
            () -> new PlayerAccessItemToken(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FORMATION_SLIP_OAK = ITEMS.register("formation_slip_oak",
            () -> new PlayerAccessItemToken(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FORMATION_SLIP_SPRUCE = ITEMS.register("formation_slip_spruce",
            () -> new PlayerAccessItemToken(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FORMATION_SLIP_WARPED = ITEMS.register("formation_slip_warped",
            () -> new PlayerAccessItemToken(new Item.Properties().stacksTo(1)));









    //Artifacts
    public static final DeferredItem<Item> KARMIC_DEBT_LEDGER = ITEMS.register("karmic_debt_ledger",
            () -> new KarmicDebtLedgerItem());



    public static final DeferredItem<Item> SPATIAL_RUPTURE_TALISMAN_T1 = ITEMS.register("spatial_rupture_talisman_t1",
            () -> new SpatialRuptureTalismanT1(new Item.Properties()));
    public static final DeferredItem<Item> SPATIAL_RUPTURE_TALISMAN_T2 = ITEMS.register("spatial_rupture_talisman_t2",
            () -> new SpatialRuptureTalismanT2(new Item.Properties()));
    public static final DeferredItem<Item> SPATIAL_RUPTURE_TALISMAN_T3 = ITEMS.register("spatial_rupture_talisman_t3",
            () -> new SpatialRuptureTalismanT3(new Item.Properties()));
    public static final DeferredItem<Item> SOULSTEAD_RETURN_TALISMAN = ITEMS.register("soulstead_return_talisman",
            () -> new SoulsteadReturnTalisman(new Item.Properties()));
    public static final DeferredItem<Item> WORLD_AXIS_TALISMAN = ITEMS.register("world_axis_talisman",
            () -> new WorldAxisTalisman(new Item.Properties()));
    public static final DeferredItem<Item> VOID_MARKING_TALISMAN = ITEMS.register("void_marking_talisman",
            () -> new VoidMarkingTalisman(new Item.Properties()));


    public static final DeferredItem<Item> TABLET_OF_DESTRUCTION_HUMAN = ITEMS.register("todh",
            () -> new TabletOfDestructionHuman(new Item.Properties()));
    public static final DeferredItem<Item> TABLET_OF_DESTRUCTION_EARTH = ITEMS.register("tode",
            () -> new TabletOfDestructionEarth(new Item.Properties()));
    public static final DeferredItem<Item> TABLET_OF_DESTRUCTION_HEAVEN = ITEMS.register("todhe",
            () -> new TabletOfDestructionHeaven(new Item.Properties()));

    public static final DeferredItem<Item> FIRE_GOURD = ITEMS.register("fire_gourd",
            () -> new FlameGourd(new Item.Properties()));

    public static final DeferredItem<Item> REPAIR_SLIP = ITEMS.register("repair_slip",
            () -> new RepairSlip(new Item.Properties()));
    public static final DeferredItem<Item> ENDER_POUCH = ITEMS.register("ender_pouch",
            () -> new EnderPouch(new Item.Properties()));


    //NotUsedStuff
    public static final DeferredItem<Item> ASCENSION_ICON = ITEMS.register("ascension_icon",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));


    //Drops
    public static final DeferredItem<Item> LIVING_CORE = ITEMS.register("living_core",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> UNDEAD_CORE = ITEMS.register("undead_core",
            () -> new Item(new Item.Properties()));

    //Spiritual Fires
    public static final DeferredItem<Item> CRIMSON_LOTUS_FLAME = ITEMS.register("crimson_lotus_flame",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FLAME = ITEMS.register("flame",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SOUL_FLAME = ITEMS.register("soul_flame",
            () -> new Item(new Item.Properties()));

    //Crafting Ingredients
    public static final DeferredItem<Item> TALISMAN_PAPER = ITEMS.register("talisman_paper",
            () -> new Item(new Item.Properties()));


    //Pills
    public static final DeferredItem<Item> REGENERATION_PILL = ITEMS.register("regeneration_pill",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.REGENERATION_PILL), 400));

    public static final DeferredItem<Item> NEUTRALITY_PILL = ITEMS.register("neutrality_pill",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.NEUTRALITY_PILL), 400));
    public static final DeferredItem<Item> CLEANSING_PILL_T1 = ITEMS.register("cleansing_pill_t1",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.CLEANSING_PILL_T1), 400));
    public static final DeferredItem<Item> CLEANSING_PILL_T2 = ITEMS.register("cleansing_pill_t2",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.CLEANSING_PILL_T2), 400));
    public static final DeferredItem<Item> CLEANSING_PILL_T3 = ITEMS.register("cleansing_pill_t3",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.CLEANSING_PILL_T3), 400));
    public static final DeferredItem<Item> CLEANSING_PILL_T4 = ITEMS.register("cleansing_pill_t4",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.CLEANSING_PILL_T4), 400));
    public static final DeferredItem<Item> ANTIDOTE_PILL_T1 = ITEMS.register("antidote_pill_qdppill",
            () -> new PillAntidote(new Item.Properties().food(ModFoodProperties.ANTIDOTE_PILL), 400, ModEffects.PARASITE));
    public static final DeferredItem<Item> ANTIDOTE_PILL_T2 = ITEMS.register("antidote_pill_t2",
            () -> new PillAntidote(new Item.Properties().food(ModFoodProperties.ANTIDOTE_PILL), 400));
    public static final DeferredItem<Item> ANTIDOTE_PILL_T3 = ITEMS.register("antidote_pill_t3",
            () -> new PillAntidote(new Item.Properties().food(ModFoodProperties.ANTIDOTE_PILL), 400));
    public static final DeferredItem<Item> PILL_RESIDUE = ITEMS.register("pill_residue",
            () -> new PillResidue(new Item.Properties().food(ModFoodProperties.PILL_RESIDUE)));
    public static final DeferredItem<Item> FASTING_PILL_T1 = ITEMS.register("fasting_pill_t1",
            () -> new Item(new Item.Properties().food(ModFoodProperties.FASTING_PILL_T1)));
    public static final DeferredItem<Item> FASTING_PILL_T2 = ITEMS.register("fasting_pill_t2",
            () -> new Item(new Item.Properties().food(ModFoodProperties.FASTING_PILL_T2)));
    public static final DeferredItem<Item> FASTING_PILL_T3 = ITEMS.register("fasting_pill_t3",
            () -> new Item(new Item.Properties().food(ModFoodProperties.FASTING_PILL_T3)));


    //Important Pills
    public static final DeferredItem<Item> REBIRTH_PILL = ITEMS.register("rebirth_pill",
            () -> new RebirthPill(new Item.Properties().food(ModFoodProperties.REBIRTH_PILL), 400 /*Going to be 72000 so 8 hour cooldown when released*/));
    public static final DeferredItem<Item> BODY_AMNESIA_PILL = ITEMS.register("body_amnesia_pill",
            () -> new BodyTechniqueChangePill(new Item.Properties().food(ModFoodProperties.PILLS), 3 /*Going to be 72000 so 8 hour cooldown when released*/));

    //Cultivation Pills

    //Body
    public static final DeferredItem<Item> INNER_REINFORCEMENT_PILL_T1 = ITEMS.register("inner_reinforcement_pill_t1",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.CULT_PILL), 400 /*Will be 10 min later*/)
                    .addOnUse((item,level,entity)->{
                        CultivationSystem.cultivate((Player) entity,"ascension:body",500.0,new HashSet<>(), CultivationSource.CONSUMABLE);
                    }));
    public static final DeferredItem<Item> INNER_REINFORCEMENT_PILL_T2 = ITEMS.register("inner_reinforcement_pill_t2",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.CULT_PILL), 800 /*Will be 10 min later*/)
                    .addOnUse((item,level,entity)->{
                        CultivationSystem.cultivate((Player) entity,"ascension:body",1500.0,new HashSet<>(), CultivationSource.CONSUMABLE);
                    }));
    public static final DeferredItem<Item> INNER_REINFORCEMENT_PILL_T3 = ITEMS.register("inner_reinforcement_pill_t3",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.CULT_PILL), 1600 /*Will be 10 min later*/)
                    .addOnUse((item,level,entity)->{
                        CultivationSystem.cultivate((Player) entity,"ascension:body",4500.0,new HashSet<>(), CultivationSource.CONSUMABLE);
                    }));

    //Essence
    public static final DeferredItem<Item> ESSENCE_GATHERING_PILL_T1 = ITEMS.register("essence_gathering_pill_t1",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.CULT_PILL), 400 /*Will be 10 min later*/)
                    .addOnUse((item,level,entity)->{
                        CultivationSystem.cultivate((Player) entity,"ascension:essence",500.0,new HashSet<>(), CultivationSource.CONSUMABLE);
                    }));
    public static final DeferredItem<Item> ESSENCE_GATHERING_PILL_T2 = ITEMS.register("essence_gathering_pill_t2",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.CULT_PILL), 800 /*Will be 10 min later*/)
                    .addOnUse((item,level,entity)->{
                        CultivationSystem.cultivate((Player) entity,"ascension:essence",1500.0,new HashSet<>(), CultivationSource.CONSUMABLE);
                    }));
    public static final DeferredItem<Item> ESSENCE_GATHERING_PILL_T3 = ITEMS.register("essence_gathering_pill_t3",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.CULT_PILL), 1600 /*Will be 10 min later*/)
                    .addOnUse((item,level,entity)->{
                        CultivationSystem.cultivate((Player) entity,"ascension:essence",4500.0,new HashSet<>(), CultivationSource.CONSUMABLE);
                    }));

    //Intent
    public static final DeferredItem<Item> SPIRIT_FOCUS_PILL_T1 = ITEMS.register("spirit_focus_pill_t1",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.CULT_PILL), 400 /*Will be 10 min later*/)
                    .addOnUse((item,level,entity)->{
                        CultivationSystem.cultivate((Player) entity,"ascension:intent",500.0,new HashSet<>(), CultivationSource.CONSUMABLE);
                    }));
    public static final DeferredItem<Item> SPIRIT_FOCUS_PILL_T2 = ITEMS.register("spirit_focus_pill_t2",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.CULT_PILL), 800 /*Will be 10 min later*/)
                    .addOnUse((item,level,entity)->{
                        CultivationSystem.cultivate((Player) entity,"ascension:intent",1500.0,new HashSet<>(), CultivationSource.CONSUMABLE);
                    }));
    public static final DeferredItem<Item> SPIRIT_FOCUS_PILL_T3 = ITEMS.register("spirit_focus_pill_t3",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.CULT_PILL), 1600 /*Will be 10 min later*/)
                    .addOnUse((item,level,entity)->{
                        CultivationSystem.cultivate((Player) entity,"ascension:intent",4500.0,new HashSet<>(), CultivationSource.CONSUMABLE);
                    }));



    //Poison Pills
    public static final DeferredItem<Item> QI_DEVOURING_PARASITE_PILL = ITEMS.register("qi_devouring_parasite_pill",
            () -> new ThrowablePoisonPill(new Item.Properties().food(ModFoodProperties.QI_DEVOURING_PARASITE_PILL), new MobEffectInstance(ModEffects.PARASITE, 400, 1)));



    //Phys Stuff
    //Todo
    //Change it into a Blood Essence texture and also make it so blood essence drop then you can combine blood essences to make the purity 100% and when its 100% only then can you use it.
    public static final DeferredItem<Item> BLOOD_ESSENCE = ITEMS.register("blood_essence",
            () -> new PhysiqueTransferItem(new Item.Properties().stacksTo(1)));



    //Herbs
    public static final DeferredItem<Item> GOLDEN_SUN_LEAF = ITEMS.register("golden_sun_leaf",
            () -> new Item(new Item.Properties().food(ModFoodProperties.GOLDEN_SUN_LEAF)));
    public static final DeferredItem<Item> JADE_BAMBOO_OF_SERENITY = ITEMS.register("jade_bamboo_of_serenity",
            () -> new Item(new Item.Properties().food(ModFoodProperties.JADE_BAMBOO_OF_SERENITY)));

    public static final DeferredItem<Item> IRONWOOD_SPROUT = ITEMS.register("ironwood_sprout",
            () -> new PlantableHerb(ModBlocks.IRONWOOD_SPROUT_CROP.get(),
                    new Item.Properties().food(ModFoodProperties.IRONWOOD_SPROUT)));


    public static final DeferredItem<Item> WHITE_JADE_ORCHID = ITEMS.register("white_jade_orchid",
            () -> new ItemNameBlockItem(ModBlocks.WHITE_JADE_ORCHID_CROP.get(),
                    new Item.Properties().food(ModFoodProperties.WHITE_JADE_ORCHID)));
    public static final DeferredItem<Item> HUNDRED_YEAR_SNOW_GINSENG = ITEMS.register("hundred_year_snow_ginseng",
            () -> new HundredYearSnowGinseng(ModBlocks.HUNDRED_YEAR_SNOW_GINSENG_CROP.get(),
                    new Item.Properties().food(ModFoodProperties.HUNDRED_YEAR_SNOW_GINSENG)));
    public static final DeferredItem<Item> HUNDRED_YEAR_FIRE_GINSENG = ITEMS.register("hundred_year_fire_ginseng",
            () -> new HundredYearFireGinseng(ModBlocks.HUNDRED_YEAR_FIRE_GINSENG_CROP.get(),
                    new Item.Properties().food(ModFoodProperties.HUNDRED_YEAR_FIRE_GINSENG)));
    public static final DeferredItem<Item> HUNDRED_YEAR_GINSENG = ITEMS.register("hundred_year_ginseng",
            () -> new ItemNameBlockItem(ModBlocks.HUNDRED_YEAR_GINSENG_CROP.get(),
                    new Item.Properties().food(ModFoodProperties.HUNDRED_YEAR_GINSENG)));


    //MobEggs
    public static final DeferredItem<Item> RAT_SPAWN_EGG = ITEMS.register("rat_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.RAT, 0x4F2242, 0x703240,
                    new Item.Properties()));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
