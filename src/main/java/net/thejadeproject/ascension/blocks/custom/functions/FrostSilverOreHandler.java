package net.thejadeproject.ascension.blocks.custom.functions;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.thejadeproject.ascension.AscensionCraft;

import static net.thejadeproject.ascension.blocks.ModBlocks.FROST_SILVER_ORE;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class FrostSilverOreHandler {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();

        // Skip if creative/spectator or on client side
        if (player.isCreative() || player.isSpectator() || player.level().isClientSide()) {
            return;
        }

        // Check if player has frost silver ore in their inventory
        boolean hasFrostOre = false;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == FROST_SILVER_ORE.get().asItem()) {
                hasFrostOre = true;
                break;
            }
        }

        if (hasFrostOre) {
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
