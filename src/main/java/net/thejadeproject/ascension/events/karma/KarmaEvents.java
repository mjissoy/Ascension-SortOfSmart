// KarmaEvents.java - Remove ALL static keywords
package net.thejadeproject.ascension.events.karma;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class KarmaEvents {

    @SubscribeEvent
    public void onEntityKilled(LivingDeathEvent event) {  // Removed static
        if (event.getSource().getEntity() instanceof ServerPlayer killer) {
            LivingEntity victim = event.getEntity();

            // Initialize karma if needed
            KarmaManager.initializeKarma(killer);

            // Calculate karma change
            int karmaChange = KarmaManager.calculateKarmaChange(victim, killer);

            if (karmaChange != 0) {
                int oldKarma = KarmaManager.getKarma(killer);
                KarmaRank oldRank = KarmaManager.getKarmaRank(killer);

                // Apply karma change
                KarmaManager.addKarma(killer, karmaChange);

                int newKarma = KarmaManager.getKarma(killer);
                KarmaRank newRank = KarmaManager.getKarmaRank(killer);

                // Send feedback message
                if (karmaChange > 0) {
                    killer.sendSystemMessage(Component.literal(
                            "§a+" + karmaChange + " karma (§7" + oldKarma + " → " + newKarma + "§a)"
                    ));
                } else {
                    killer.sendSystemMessage(Component.literal(
                            "§c" + karmaChange + " karma (§7" + oldKarma + " → " + newKarma + "§c)"
                    ));
                }

                // Special messages for significant events
                if (victim instanceof Villager) {
                    killer.sendSystemMessage(Component.literal("§4Your sins weigh heavily upon you..."));
                } else if (victim instanceof Player victimPlayer &&
                        KarmaManager.getKarmaRank(victimPlayer) == KarmaRank.SAINT) {
                    killer.sendSystemMessage(Component.literal("§4You have committed a grave sin!"));
                } else if (victim instanceof Monster) {
                    killer.sendSystemMessage(Component.literal("§aYou have purified evil energy."));
                }

                // Sync to client
                syncKarmaToClient(killer);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {  // Removed static
        Player player = event.getEntity();

        // Initialize karma on first tick
        KarmaManager.initializeKarma(player);

        // Apply karma effects every 5 seconds (100 ticks)
        if (!player.level().isClientSide && player.tickCount % 100 == 0) {
            KarmaManager.applyKarmaEffects(player);
        }
    }



    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {  // Removed static
        Player player = event.getEntity();
        if (!player.level().isClientSide) {
            KarmaManager.initializeKarma(player);
            syncKarmaToClient((ServerPlayer) player);
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {  // Removed static
        if (event.isWasDeath()) {
            Player original = event.getOriginal();
            Player newPlayer = event.getEntity();

            // Copy karma data from original player
            if (KarmaManager.hasKarma(original)) {
                int karmaValue = KarmaManager.getKarma(original);
                KarmaManager.setKarma(newPlayer, karmaValue);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {  // Removed static
        Player player = event.getEntity();
        if (!player.level().isClientSide) {
            // Small karma adjustment on respawn
            int currentKarma = KarmaManager.getKarma(player);
            if (currentKarma > 20) {
                // Saints get a small boost
                KarmaManager.addKarma(player, 1);
            } else if (currentKarma < -20) {
                // Demons get a small penalty
                KarmaManager.addKarma(player, -1);
            }
            syncKarmaToClient((ServerPlayer) player);
        }
    }

    private void syncKarmaToClient(ServerPlayer player) {
        if (player == null || player.connection == null) return;

        KarmaSyncPayload packet = new KarmaSyncPayload(
                KarmaManager.getKarma(player),
                KarmaManager.getKarmaRank(player).getId()
        );
        KarmaNetworkHandler.sendToPlayer(packet, player);
    }
}