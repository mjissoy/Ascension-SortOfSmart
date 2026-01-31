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
            event.setCanceled(true);
            Sect sect = manager.getPlayerSect(player.getUUID());

            if (sect != null) {
                String message = event.getRawText();
                if (message.startsWith("/")) {
                    player.sendSystemMessage(Component.literal("§cCannot send commands in sect chat toggle mode!"));
                    manager.setChatToggle(player.getUUID(), false);
                    return;
                }
                sendSectChatMessage(sect, player, message, player.getServer());
            } else {
                player.sendSystemMessage(Component.literal("§cYou are not in a sect! Switching back to global chat."));
                manager.setChatToggle(player.getUUID(), false);
            }
            return;
        }
        event.setCanceled(true);

        Sect sect = manager.getPlayerSect(player.getUUID());
        String playerName = player.getScoreboardName();
        String messageText = event.getRawText();

        MutableComponent formattedMessage;

        if (sect != null) {
            formattedMessage = Component.literal("")
                    .append(Component.literal("§e[" + sect.getName() + "] §r"))
                    .append(Component.literal(playerName))
                    .append(Component.literal(" §e➣§r "))
                    .append(Component.literal(messageText));
        } else {
            formattedMessage = Component.literal("")
                    .append(Component.literal(playerName))
                    .append(Component.literal(" §e➣§r "))
                    .append(Component.literal(messageText));
        }

        // Manually broadcast to all players (including sender)
        for (ServerPlayer onlinePlayer : player.getServer().getPlayerList().getPlayers()) {
            onlinePlayer.sendSystemMessage(formattedMessage);
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SectManager manager = AscensionCraft.getSectManager(player.getServer());
            if (manager == null) return;
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
        player.setCustomName(Component.literal(player.getScoreboardName()));
        player.setCustomNameVisible(false);
    }

    private void sendSectChatMessage(Sect sect, ServerPlayer sender, String message, net.minecraft.server.MinecraftServer server) {
        String formattedMessage = "§3[§b" + sect.getName() + "§3] §e" + sender.getScoreboardName() + " §e➣§f " + message;
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
        if (!(event.getEntity() instanceof Player targetPlayer)) return;

        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof Player attackerPlayer)) return;
        if (targetPlayer == attackerPlayer) return;

        SectManager manager = AscensionCraft.getSectManager(targetPlayer.getServer());
        if (manager == null) return;

        Sect targetSect = manager.getPlayerSect(targetPlayer.getUUID());
        Sect attackerSect = manager.getPlayerSect(attackerPlayer.getUUID());

        if (targetSect != null && targetSect == attackerSect) {
            if (!targetSect.isFriendlyFire()) {
                event.setNewDamage(0);
                attackerPlayer.displayClientMessage(
                        Component.literal("§cFriendly fire is disabled in your sect!"),
                        true
                );
            }
        }
    }
}