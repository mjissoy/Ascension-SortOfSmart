package net.thejadeproject.ascension.sects;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class SectEventHandler {

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        SectManager manager = SectManager.getInstance();

        if (manager == null) return;

        if (manager.isChatToggled(player.getUUID())) {
            // Cancel normal chat and send to sect chat
            event.setCanceled(true);

            Sect sect = manager.getPlayerSect(player.getUUID());
            if (sect != null) {
                String message = event.getRawText();
                // Use the same format as /sect chat command
                sendSectChatMessage(sect, player, message, player.getServer());
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
            // Refresh tab list display
            updatePlayerDisplayName(player);
        }
    }

    private void updatePlayerDisplayName(ServerPlayer player) {
        SectManager manager = SectManager.getInstance();
        if (manager == null) return;

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
}