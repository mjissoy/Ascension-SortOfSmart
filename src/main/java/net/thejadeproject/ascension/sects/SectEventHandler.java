package net.thejadeproject.ascension.sects;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.thejadeproject.ascension.AscensionCraft;

public class SectEventHandler {

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        SectManager manager = AscensionCraft.getSectManager(player.getServer());

        if (manager == null) return;

        if (manager.isChatToggled(player.getUUID())) {
            // Cancel normal chat and send as command instead
            event.setCanceled(true);

            Sect sect = manager.getPlayerSect(player.getUUID());
            if (sect != null) {
                String message = event.getRawText();
                // Add space after "chat" and properly escape the message
                String command = "sect chat " + message;
                player.getServer().getCommands().performPrefixedCommand(player.createCommandSourceStack(), command);
            } else {
                // If no sect but chat is toggled, send error and untoggle
                player.sendSystemMessage(Component.literal("§cYou are not in a sect! Switching back to global chat."));
                manager.setChatToggle(player.getUUID(), false);
            }
        } else {
            // Add yellow sect prefix to global chat
            Sect sect = manager.getPlayerSect(player.getUUID());
            if (sect != null) {
                // Create a completely new message structure
                MutableComponent newMessage = Component.literal("")
                        .append(Component.literal("§e[" + sect.getName() + "] ")) // Sect prefix
                        .append(Component.literal(event.getRawText())); // The actual message

                event.setMessage(newMessage);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // Get manager from the player's server
            SectManager manager = AscensionCraft.getSectManager(player.getServer());
            if (manager == null) return;

            // Refresh tab list display
            updatePlayerDisplayName(player, manager);
        }
    }

    private void updatePlayerDisplayName(ServerPlayer player, SectManager manager) {
        Sect sect = manager.getPlayerSect(player.getUUID());

        if (sect != null) {
            SectMember member = sect.getMember(player.getUUID());
            if (member != null) {
                String title = member.getTitle().isEmpty() ? "" : " §7" + member.getTitle();
                Component displayName = Component.literal("§3[§b" + sect.getName() + "§3] §e" + player.getScoreboardName() + title);
                player.setCustomName(displayName);
                player.setCustomNameVisible(true);
                return;
            }
        }

        // Reset to default if no sect
        player.setCustomName(Component.literal(player.getScoreboardName()));
        player.setCustomNameVisible(false);
    }

    private void sendSectChatMessage(Sect sect, ServerPlayer sender, String message, net.minecraft.server.MinecraftServer server) {
        String formattedMessage = "§3[§b" + sect.getName() + "§3] §e" + sender.getScoreboardName() + "§7: §f" + message;
        Component chatMessage = Component.literal(formattedMessage);

        for (SectMember member : sect.getMembers().values()) {
            ServerPlayer onlinePlayer = server.getPlayerList().getPlayer(member.getPlayerId());
            if (onlinePlayer != null) {
                onlinePlayer.sendSystemMessage(chatMessage);
            }
        }
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent.Pre event) {
        // Get the target (entity being damaged)
        if (!(event.getEntity() instanceof Player targetPlayer)) return;

        // Get the attacker from the damage source
        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof Player attackerPlayer)) return;

        // Don't check if players are the same
        if (targetPlayer == attackerPlayer) return;

        SectManager manager = AscensionCraft.getSectManager(targetPlayer.getServer());
        if (manager == null) return;

        // Check if both players are in the same sect
        Sect targetSect = manager.getPlayerSect(targetPlayer.getUUID());
        Sect attackerSect = manager.getPlayerSect(attackerPlayer.getUUID());

        if (targetSect != null && targetSect == attackerSect) {
            // Players are in the same sect, check friendly fire setting
            if (!targetSect.isFriendlyFire()) {
                // Friendly fire is disabled, cancel the damage
                event.getEntity();

                // Optional: Send message to attacker
                attackerPlayer.displayClientMessage(
                        Component.literal("§cFriendly fire is disabled in your sect!"),
                        true
                );
            }
        }
    }
}