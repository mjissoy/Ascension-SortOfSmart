package net.thejadeproject.ascension.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.entity.ModEntities;
import net.thejadeproject.ascension.items.artifacts.*;
import net.thejadeproject.ascension.items.herbs.HundredYearFireGinseng;
import net.thejadeproject.ascension.items.herbs.HundredYearSnowGinseng;
import net.thejadeproject.ascension.items.herbs.PlantableHerb;
import net.thejadeproject.ascension.items.pills.PillCooldownItem;
import net.thejadeproject.ascension.items.tools.BladeItem;
import net.thejadeproject.ascension.items.tools.SpearItem;
import net.thejadeproject.ascension.util.ItemUtil;
import net.thejadeproject.ascension.util.ToolTipsGradient;

import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AscensionCraft.MOD_ID);

    //public static final DeferredItem<Item> RAW_JADE = ITEMS.register("raw_jade",
            //() -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> JADE = ITEMS.register("jade",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> JADE_NUGGET = ITEMS.register("jade_nugget",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SPIRITUAL_STONE = ITEMS.register("spiritual_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));




    //Weapons & Tools
    public static final DeferredItem<BladeItem> JADE_BLADE = ITEMS.register("jade_blade",
            () -> new BladeItem(ModToolTiers.JADE, new Item.Properties()
                    .attributes(BladeItem.createAttributes(ModToolTiers.JADE, 6, -2.2f))));
    public static final DeferredItem<BladeItem> SEARING_BLADE = ITEMS.register("searing_blade",
            () -> new BladeItem(ModToolTiers.JADE, new Item.Properties()
                    .attributes(BladeItem.createAttributes(ModToolTiers.JADE, 7, -2f))));





    public static final DeferredItem<SpearItem> JADE_SPEAR = ITEMS.register("jade_spear",
            () -> new SpearItem(ModToolTiers.JADE, new Item.Properties()
                    .attributes(ItemUtil.createAscensionItemAttributes(ModToolTiers.JADE, 6, -2.8f,2))
                  ));




    //Artifacts
    public static final DeferredItem<Item> JADE_SLIP = ITEMS.register("jade_slip",
            () -> new JadeSlip(new Item.Properties().stacksTo(1)){
                private float time = 0;
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    if (context.level() != null && context.level().isClientSide()) {
                        String text = Component.translatable("tooltip.ascension.jade_slip").getString();
                        time += 0.001f;
                        if (time > 1.0f) time = 0;
                        tooltipComponents.add(ToolTipsGradient.RGBEachLetter(time, text, 0.01f));
                    } else {
                        tooltipComponents.add(Component.translatable("tooltip.ascension.jade_slip"));
                    }
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });
    public static final DeferredItem<Item> IRON_SPATIAL_RING = ITEMS.register("iron_spatial_ring",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON)));
    public static final DeferredItem<Item> GOLD_SPATIAL_RING = ITEMS.register("gold_spatial_ring",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON)));
    public static final DeferredItem<Item> DIAMOND_SPATIAL_RING = ITEMS.register("diamond_spatial_ring",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> NETHERITE_SPATIAL_RING = ITEMS.register("netherite_spatial_ring",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final DeferredItem<Item> JADE_SPATIAL_RING = ITEMS.register("jade_spatial_ring",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final DeferredItem<Item> SPIRITUAL_STONE_SPATIAL_RING = ITEMS.register("spiritual_stone_spatial_ring",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> TABLET_OF_DESTRUCTION_HUMAN = ITEMS.register("todh",
            () -> new TabletOfDestructionHuman(new Item.Properties()){
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {{
                        tooltipComponents.add(Component.translatable("tooltip.ascension.todh"));}
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });
    public static final DeferredItem<Item> TABLET_OF_DESTRUCTION_EARTH = ITEMS.register("tode",
            () -> new TabletOfDestructionEarth(new Item.Properties()){
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {{
                    tooltipComponents.add(Component.translatable("tooltip.ascension.tode"));}
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });
    public static final DeferredItem<Item> TABLET_OF_DESTRUCTION_HEAVEN = ITEMS.register("todhe",
            () -> new TabletOfDestructionHeaven(new Item.Properties()){
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {{
                    tooltipComponents.add(Component.translatable("tooltip.ascension.todhe"));}
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });


    public static final DeferredItem<Item> REPAIR_SLIP = ITEMS.register("repair_slip",
            () -> new RepairSlip(new Item.Properties()){
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    if(Screen.hasShiftDown()) {
                        tooltipComponents.add(Component.translatable("tooltip.ascension.repair_slip.shift_down1"));
                        tooltipComponents.add(Component.translatable("tooltip.ascension.repair_slip.shift_down2"));
                    } else {
                        tooltipComponents.add(Component.translatable("tooltip.ascension.repair_slip"));
                    }
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    public static final DeferredItem<Item> ENDER_POUCH = ITEMS.register("ender_pouch",
            () -> new EnderPouch(new Item.Properties()){
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    if(Screen.hasShiftDown()) {
                        tooltipComponents.add(Component.translatable("tooltip.ascension.ender_pouch.shift_down1"));
                        tooltipComponents.add(Component.translatable("tooltip.ascension.ender_pouch.shift_down2"));
                    } else {
                        tooltipComponents.add(Component.translatable("tooltip.ascension.ender_pouch"));
                    }
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });


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


    //Pills
    public static final DeferredItem<Item> REGENERATION_PILL = ITEMS.register("regeneration_pill",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.REGENERATION_PILL), 400));
    public static final DeferredItem<Item> CLEANSING_PILL = ITEMS.register("cleansing_pill",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.CLEANSING_PILL), 400));
    public static final DeferredItem<Item> INNER_REINFORCEMENT_PILL_T1 = ITEMS.register("inner_reinforcement_pill_t1",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.INNER_REINFORCEMENT_T1), 400 /*Will be 10 min later*/)
                    .addOnUse((item,level,entity)->{
                        CultivationSystem.cultivate((Player) entity,"ascension:body",20.0,List.of());
                    }));



    public static final DeferredItem<Item> REBIRTH_PILL = ITEMS.register("rebirth_pill",
            () -> new PillCooldownItem(new Item.Properties().food(ModFoodProperties.REBIRTH_PILL), 400 /*Going to be 72000 so 8 hour cooldown when released*/){
                private float time = 0;
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    if (context.level() != null && context.level().isClientSide()) {
                        String text = Component.translatable("tooltip.ascension.rebirth_pill").getString();
                        time += 0.001f;
                        if (time > 1.0f) time = 0;
                        tooltipComponents.add(ToolTipsGradient.RGBEachLetter(time, text, 0.01f));
                    } else {
                        tooltipComponents.add(Component.translatable("tooltip.ascension.rebirth_pill"));
                    }
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });




    public static final DeferredItem<Item> FASTING_PILL_T1 = ITEMS.register("fasting_pill_t1",
            () -> new Item(new Item.Properties().food(ModFoodProperties.FASTING_PILL_T1)));
    public static final DeferredItem<Item> FASTING_PILL_T2 = ITEMS.register("fasting_pill_t2",
            () -> new Item(new Item.Properties().food(ModFoodProperties.FASTING_PILL_T2)));
    public static final DeferredItem<Item> FASTING_PILL_T3 = ITEMS.register("fasting_pill_t3",
            () -> new Item(new Item.Properties().food(ModFoodProperties.FASTING_PILL_T3)));





    //Herbs
    public static final DeferredItem<Item> GOLDEN_SUN_LEAF = ITEMS.register("golden_sun_leaf",
            () -> new Item(new Item.Properties().food(ModFoodProperties.GOLDEN_SUN_LEAF)));
    public static final DeferredItem<Item> IRONWOOD_SPROUT = ITEMS.register("ironwood_sprout",
            () -> new Item(new Item.Properties().food(ModFoodProperties.IRONWOOD_SPROUT)));
    public static final DeferredItem<Item> WHITE_JADE_ORCHID = ITEMS.register("white_jade_orchid",
            () -> new Item(new Item.Properties().food(ModFoodProperties.WHITE_JADE_ORCHID)));
    public static final DeferredItem<Item> JADE_BAMBOO_OF_SERENITY = ITEMS.register("jade_bamboo_of_serenity",
            () -> new Item(new Item.Properties().food(ModFoodProperties.JADE_BAMBOO_OF_SERENITY)));
    public static final DeferredItem<Item> HUNDRED_YEAR_SNOW_GINSENG = ITEMS.register("hundred_year_snow_ginseng",
            () -> new HundredYearSnowGinseng(new Item.Properties().food(ModFoodProperties.HUNDRED_YEAR_SNOW_GINSENG)));
    public static final DeferredItem<Item> HUNDRED_YEAR_FIRE_GINSENG = ITEMS.register("hundred_year_fire_ginseng",
            () -> new HundredYearFireGinseng(new Item.Properties().food(ModFoodProperties.HUNDRED_YEAR_FIRE_GINSENG)));


    public static final DeferredItem<Item> HUNDRED_YEAR_GINSENG = ITEMS.register("hundred_year_ginseng",
            () -> new PlantableHerb(ModBlocks.HUNDRED_YEAR_GINSENG_CROP.get(),
                    new Item.Properties().food(ModFoodProperties.HUNDRED_YEAR_GINSENG)));


    //MobEggs
    public static final DeferredItem<Item> RAT_SPAWN_EGG = ITEMS.register("rat_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.RAT, 0x4F2242, 0x703240,
                    new Item.Properties()));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
