package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.Lazy;
import net.thejadeproject.ascension.events.karma.KarmaManager;
import net.thejadeproject.ascension.events.karma.KarmaRank;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.network.clientBound.OpenKarmicLedgerScreen;

import java.util.HashMap;
import java.util.Map;

public class KarmicDebtLedgerItem extends Item {

    // Track statistics for each player
    private static final Lazy<Map<Player, PlayerKarmaStats>> PLAYER_STATS =
            Lazy.of(HashMap::new);

    public KarmicDebtLedgerItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
        );
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // Get player's karma data
            int karmaValue = KarmaManager.getKarma(player);
            KarmaRank rank = KarmaManager.getKarmaRank(player);

            // Get player statistics
            PlayerKarmaStats stats = getPlayerStats(player);

            // Send packet to open GUI with all data
            OpenKarmicLedgerScreen packet = new OpenKarmicLedgerScreen(
                    karmaValue,
                    rank.getId(),
                    stats.mobKills,
                    stats.playerKills
            );

            PacketDistributor.sendToPlayer((net.minecraft.server.level.ServerPlayer) player, packet);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    // Method to record a kill
    public static void recordKill(Player player, String entityType) {
        PlayerKarmaStats stats = getPlayerStats(player);

        if (entityType.equals("player")) {
            stats.playerKills++;
        } else {
            stats.mobKills.put(entityType, stats.mobKills.getOrDefault(entityType, 0) + 1);
        }
    }

    // Method to get player stats
    private static PlayerKarmaStats getPlayerStats(Player player) {
        return PLAYER_STATS.get().computeIfAbsent(player, p -> new PlayerKarmaStats());
    }

    // Method to get karma effects for villager trades
    public static float getTradePriceMultiplier(Player player) {
        KarmaRank rank = KarmaManager.getKarmaRank(player);

        switch (rank) {
            case SAINT:
                return 0.75f; // 25% decrease
            case DEMONIC:
                return 1.25f; // 25% increase
            case NEUTRAL:
            default:
                return 1.0f; // No change
        }
    }

    // Method to get luck effect
    public static int getLuckLevel(Player player) {
        KarmaRank rank = KarmaManager.getKarmaRank(player);

        switch (rank) {
            case SAINT:
                return 1; // Good luck
            case DEMONIC:
                return -1; // Bad luck
            case NEUTRAL:
            default:
                return 0; // Neutral
        }
    }

    // Inner class to store player statistics
    private static class PlayerKarmaStats {
        Map<String, Integer> mobKills = new HashMap<>();
        int playerKills = 0;
    }
}
