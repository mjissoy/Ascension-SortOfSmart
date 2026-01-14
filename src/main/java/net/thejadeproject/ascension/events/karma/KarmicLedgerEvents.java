package net.thejadeproject.ascension.events.karma;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.world.item.ItemStack;
import net.thejadeproject.ascension.items.ModItems;
import net.neoforged.neoforge.event.entity.player.TradeWithVillagerEvent;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.thejadeproject.ascension.items.artifacts.KarmicDebtLedgerItem;

import java.util.HashMap;
import java.util.Map;

public class KarmicLedgerEvents {

    // Track player statistics
    private static final Map<Player, PlayerKarmaStats> PLAYER_STATS = new HashMap<>();

    @SubscribeEvent
    public void onEntityKilled(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player killer) {
            LivingEntity victim = event.getEntity();

            // Get entity type ID
            String entityType = victim.getType().toString();

            // Record the kill in our stats
            recordKill(killer, entityType);

            // Also record in the ledger item if player has it
            KarmicDebtLedgerItem.recordKill(killer, entityType);
        }
    }

    @SubscribeEvent
    public void onTradeWithVillager(TradeWithVillagerEvent event) {
        Player player = event.getEntity();

        // Check if player is holding the ledger
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        boolean hasLedger = mainHand.getItem() == ModItems.KARMIC_DEBT_LEDGER.get() ||
                offHand.getItem() == ModItems.KARMIC_DEBT_LEDGER.get();

        if (hasLedger && !player.level().isClientSide()) {
            // Apply karma-based price modifier
            // Note: In NeoForge, we can't directly modify villager trades in this event
            // We'll handle the price adjustment differently - see below
            applyKarmaTradeEffects(player);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        // Check if player is holding the ledger
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        boolean hasLedger = mainHand.getItem() == ModItems.KARMIC_DEBT_LEDGER.get() ||
                offHand.getItem() == ModItems.KARMIC_DEBT_LEDGER.get();

        if (hasLedger && !player.level().isClientSide()) {
            // Apply luck effect based on karma
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        // Initialize player stats if needed
        PLAYER_STATS.computeIfAbsent(player, p -> new PlayerKarmaStats());
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        // Clean up player stats to prevent memory leak
        PLAYER_STATS.remove(player);
    }

    private void recordKill(Player player, String entityType) {
        PlayerKarmaStats stats = PLAYER_STATS.computeIfAbsent(player, p -> new PlayerKarmaStats());

        if (entityType.equals("player")) {
            stats.playerKills++;
        } else {
            stats.mobKills.put(entityType, stats.mobKills.getOrDefault(entityType, 0) + 1);
        }
    }

    private void applyKarmaTradeEffects(Player player) {
        // Since we can't directly modify villager prices in TradeWithVillagerEvent,
        // we'll apply the karma effects through other means

        // Alternative approach: Apply discounts/price increases through reputation
        // or use a custom trading system

        // For now, we'll just notify the player of the karma effect
        float multiplier = KarmicDebtLedgerItem.getTradePriceMultiplier(player);
        String effect = multiplier < 1.0f ? "§a25% cheaper" : multiplier > 1.0f ? "§c25% more expensive" : "§7normal";

        if (player.tickCount % 100 == 0) { // Every 5 seconds
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                    "§7Your karma affects trade prices: " + effect
            ));
        }
    }

    // Getter for player stats (for GUI)
    public static PlayerKarmaStats getPlayerStats(Player player) {
        return PLAYER_STATS.getOrDefault(player, new PlayerKarmaStats());
    }

    // Inner class to store player statistics
    public static class PlayerKarmaStats {
        public Map<String, Integer> mobKills = new HashMap<>();
        public int playerKills = 0;

        public Map<String, Integer> getMobKills() {
            return new HashMap<>(mobKills);
        }

        public int getPlayerKills() {
            return playerKills;
        }
    }
}