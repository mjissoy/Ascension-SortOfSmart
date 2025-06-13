package net.thejadeproject.ascension.items;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AscensionCraft.MOD_ID);

    public static final DeferredItem<Item> RAW_JADE = ITEMS.register("raw_jade",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> JADE = ITEMS.register("jade",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> REGENERATION_PILL = ITEMS.register("regeneration_pill",
            () -> new Item(new Item.Properties().food(ModFoodProperties.REGENERATION_PILL)));





    //Herbs
    public static final DeferredItem<Item> GOLDEN_SUN_LEAF = ITEMS.register("golden_sun_leaf",
            () -> new Item(new Item.Properties().food(ModFoodProperties.GOLDEN_SUN_LEAF)));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
