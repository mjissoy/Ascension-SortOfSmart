package net.thejadeproject.ascension.blocks.custom.functions;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.items.ModItems;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class FreezingEffectItems {

    // List of items that will cause freezing when in inventory
    private static final List<Item> FREEZING_ITEMS = new ArrayList<>();

    // This method should be called during mod setup after registration is complete
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        // This runs after all registrations are complete
        event.enqueueWork(() -> {
            // Now it's safe to access the registered blocks/items
            FREEZING_ITEMS.add(ModBlocks.FROST_SILVER_ORE.get().asItem());
            FREEZING_ITEMS.add(ModBlocks.FROST_SILVER_BLOCK.get().asItem());
            FREEZING_ITEMS.add(ModItems.FROST_SILVER_INGOT.get());
            FREEZING_ITEMS.add(ModItems.FROST_SILVER_NUGGET.get());
            FREEZING_ITEMS.add(ModItems.RAW_FROST_SILVER.get());

            // Add more items here as needed
        });
    }

    // Public method to add more items from other classes
    public static void addFreezingItem(Item item) {
        if (!FREEZING_ITEMS.contains(item)) {
            FREEZING_ITEMS.add(item);
        }
    }

    // Public method to remove items from the list
    public static void removeFreezingItem(Item item) {
        FREEZING_ITEMS.remove(item);
    }

    // Public method to check if an item is in the freezing list
    public static boolean isFreezingItem(Item item) {
        return FREEZING_ITEMS.contains(item);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();

        // Skip if creative/spectator or on client side
        if (player.isCreative() || player.isSpectator() || player.level().isClientSide()) {
            return;
        }

        // Check if player has any freezing items in their inventory
        boolean hasFreezingItem = false;
        for (ItemStack stack : player.getInventory().items) {
            if (FREEZING_ITEMS.contains(stack.getItem())) {
                hasFreezingItem = true;
                break;
            }
        }

        if (hasFreezingItem) {
            applyFreezingEffect(player);
        }
    }

    private static void applyFreezingEffect(Player player) {
        // Add freezing ticks (similar to powdered snow)
        player.setTicksFrozen(Math.min(player.getTicksFrozen() + 40, 300));

        // Apply damage when fully frozen
        if (player.isFullyFrozen()) {
            player.hurt(player.damageSources().freeze(), 1.0F);
        }

        // Optional: Add visual frost overlay
        if (player.level().isClientSide()) {
            // Client-side frost overlay effects
            player.setIsInPowderSnow(true);
        }
    }
}