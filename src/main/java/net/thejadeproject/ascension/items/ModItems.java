package net.thejadeproject.ascension.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.entity.ModEntities;
import net.thejadeproject.ascension.items.pills.PillCooldownItem;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AscensionCraft.MOD_ID);

    public static final DeferredItem<Item> RAW_JADE = ITEMS.register("raw_jade",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> JADE = ITEMS.register("jade",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> JADE_NUGGET = ITEMS.register("jade_nugget",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SPIRITUAL_STONE = ITEMS.register("spiritual_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));



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





    //Herbs
    public static final DeferredItem<Item> GOLDEN_SUN_LEAF = ITEMS.register("golden_sun_leaf",
            () -> new Item(new Item.Properties().food(ModFoodProperties.GOLDEN_SUN_LEAF)));
    public static final DeferredItem<Item> IRONWOOD_SPROUT = ITEMS.register("ironwood_sprout",
            () -> new Item(new Item.Properties().food(ModFoodProperties.IRONWOOD_SPROUT)));
    public static final DeferredItem<Item> WHITE_JADE_ORCHID = ITEMS.register("white_jade_orchid",
            () -> new Item(new Item.Properties().food(ModFoodProperties.WHITE_JADE_ORCHID)));


    //MobEggs
    public static final DeferredItem<Item> RAT_SPAWN_EGG = ITEMS.register("rat_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.RAT, 0x4F2242, 0x703240,
                    new Item.Properties()));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
