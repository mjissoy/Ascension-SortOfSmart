package net.thejadeproject.ascension.sects;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SectCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sect")
                .then(Commands.literal("create")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(SectCommand::createSect)))
                .then(Commands.literal("invite")
                        .then(Commands.argument("playername", StringArgumentType.string())
                                .executes(SectCommand::invitePlayer)))
                .then(Commands.literal("promote")
                        .then(Commands.argument("playername", StringArgumentType.string())
                                .executes(SectCommand::promotePlayer)))
                .then(Commands.literal("demote")
                        .then(Commands.argument("playername", StringArgumentType.string())
                                .executes(SectCommand::demotePlayer)))
                .then(Commands.literal("title")
                        .then(Commands.argument("playername", StringArgumentType.string())
                                .then(Commands.argument("title", StringArgumentType.greedyString())
                                        .executes(SectCommand::setTitle))))
                .then(Commands.literal("join")
                        .then(Commands.argument("sectname", StringArgumentType.string())
                                .executes(SectCommand::joinSect)))
                .then(Commands.literal("open")
                        .executes(SectCommand::openSect))
                .then(Commands.literal("chat")
                        .then(Commands.argument("msg", StringArgumentType.greedyString())
                                .executes(SectCommand::sectChat)))
                .then(Commands.literal("togglechat")
                        .executes(SectCommand::toggleChat))
                .then(Commands.literal("ally")
                        .then(Commands.argument("sectname", StringArgumentType.string())
                                .executes(SectCommand::allySect)))
                .then(Commands.literal("info")
                        .executes(SectCommand::sectInfo)
                        .then(Commands.argument("target", StringArgumentType.string())
                                .executes(SectCommand::sectInfoTarget)))
                .then(Commands.literal("disband")
                        .executes(SectCommand::disbandSect))
                .then(Commands.literal("leave")
                        .executes(SectCommand::leaveSect))
                .then(Commands.literal("master")
                        .then(Commands.argument("playername", StringArgumentType.string())
                                .executes(SectCommand::transferMaster)))
                // NEW KICK COMMAND:
                .then(Commands.literal("kick")
                        .then(Commands.argument("playername", StringArgumentType.string())
                                .executes(SectCommand::kickPlayer)))
        );
    }

    private static SectManager getManager(CommandContext<CommandSourceStack> context) {
        return SectManager.get(context.getSource().getServer());
    }

    private static int createSect(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String name = StringArgumentType.getString(context, "name").replace('_', ' ');

        if (name.length() > 40) {
            context.getSource().sendFailure(Component.literal("§cSect name too long! Maximum 40 characters."));
            return 0;
        }

        if (!name.matches("[a-zA-Z0-9 _]+")) {
            context.getSource().sendFailure(Component.literal("§cSect name can only contain letters, numbers, and spaces."));
            return 0;
        }

        SectManager manager = getManager(context);
        if (manager.createSect(name, player.getUUID(), player.getScoreboardName())) {
            context.getSource().sendSuccess(() -> Component.literal("§aSect '" + name + "' created successfully!"), false);
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("§cSect name already exists or you're already in a sect!"));
            return 0;
        }
    }

    private static int invitePlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String targetName = StringArgumentType.getString(context, "playername");

        SectManager manager = getManager(context);
        Sect sect = manager.getPlayerSect(player.getUUID());

        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        if (!sect.hasPermission(player.getUUID(), SectPermission.INVITE)) {
            context.getSource().sendFailure(Component.literal("§cYou don't have permission to invite players!"));
            return 0;
        }

        manager.addInvite(sect.getName(), targetName);
        context.getSource().sendSuccess(() -> Component.literal("§aInvited " + targetName + " to join your sect!"), false);

        return 1;
    }

    private static int promotePlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String targetName = StringArgumentType.getString(context, "playername");

        SectManager manager = getManager(context);
        Sect sect = manager.getPlayerSect(player.getUUID());

        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        if (!sect.hasPermission(player.getUUID(), SectPermission.PROMOTE)) {
            context.getSource().sendFailure(Component.literal("§cYou don't have permission to promote players!"));
            return 0;
        }

        // Find target player by name
        ServerPlayer targetPlayer = context.getSource().getServer().getPlayerList().getPlayerByName(targetName);
        if (targetPlayer == null) {
            context.getSource().sendFailure(Component.literal("§cPlayer '" + targetName + "' not found or not online!"));
            return 0;
        }

        SectMember targetMember = sect.getMember(targetPlayer.getUUID());
        if (targetMember == null) {
            context.getSource().sendFailure(Component.literal("§cPlayer '" + targetName + "' is not in your sect!"));
            return 0;
        }

        SectRank currentRank = targetMember.getRank();
        if (!currentRank.canPromoteTo(currentRank.getNextRank())) {
            context.getSource().sendFailure(Component.literal("§cPlayer '" + targetName + "' cannot be promoted further!"));
            return 0;
        }

        SectRank newRank = currentRank.getNextRank();
        sect.setMemberRank(targetPlayer.getUUID(), newRank);
        context.getSource().sendSuccess(() -> Component.literal("§aPromoted " + targetName + " to " + newRank.getDisplayName() + "!"), true);

        // Notify the promoted player
        if (targetPlayer.isAlive()) {
            targetPlayer.sendSystemMessage(Component.literal("§aYou have been promoted to " + newRank.getDisplayName() + " in " + sect.getName() + "!"));
        }

        return 1;
    }
    private static int demotePlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String targetName = StringArgumentType.getString(context, "playername");

        SectManager manager = getManager(context);
        Sect sect = manager.getPlayerSect(player.getUUID());

        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        if (!sect.hasPermission(player.getUUID(), SectPermission.PROMOTE)) {
            context.getSource().sendFailure(Component.literal("§cYou don't have permission to demote players!"));
            return 0;
        }

        // Find target player by name
        ServerPlayer targetPlayer = context.getSource().getServer().getPlayerList().getPlayerByName(targetName);
        if (targetPlayer == null) {
            context.getSource().sendFailure(Component.literal("§cPlayer '" + targetName + "' not found or not online!"));
            return 0;
        }

        SectMember targetMember = sect.getMember(targetPlayer.getUUID());
        if (targetMember == null) {
            context.getSource().sendFailure(Component.literal("§cPlayer '" + targetName + "' is not in your sect!"));
            return 0;
        }

        SectRank currentRank = targetMember.getRank();
        SectRank newRank = currentRank.getPreviousRank();

        // Check if demotion is possible
        if (currentRank == newRank) {
            context.getSource().sendFailure(Component.literal("§cPlayer '" + targetName + "' cannot be demoted further!"));
            return 0;
        }

        // Prevent demoting the Sect Master
        if (currentRank == SectRank.SECT_MASTER) {
            context.getSource().sendFailure(Component.literal("§cYou cannot demote the Sect Master!"));
            return 0;
        }

        // Check if executor has permission to demote this rank
        SectMember executorMember = sect.getMember(player.getUUID());
        if (executorMember.getRank() == SectRank.ELDER && currentRank == SectRank.ELDER) {
            context.getSource().sendFailure(Component.literal("§cElders cannot demote other Elders! Only the Sect Master can."));
            return 0;
        }

        sect.setMemberRank(targetPlayer.getUUID(), newRank);
        context.getSource().sendSuccess(() -> Component.literal("§aDemoted " + targetName + " to " + newRank.getDisplayName() + "!"), true);

        // Notify the demoted player
        if (targetPlayer.isAlive()) {
            targetPlayer.sendSystemMessage(Component.literal("§cYou have been demoted to " + newRank.getDisplayName() + " in " + sect.getName() + "!"));
        }

        // Notify sect members about the demotion
        broadcastToSect(sect, context.getSource().getServer(),
                "§c" + targetName + " has been demoted to " + newRank.getDisplayName() + " by " + player.getScoreboardName() + "!");

        return 1;
    }

    private static int setTitle(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String targetName = StringArgumentType.getString(context, "playername");
        String title = StringArgumentType.getString(context, "title");

        SectManager manager = getManager(context);
        Sect sect = manager.getPlayerSect(player.getUUID());

        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        if (!sect.hasPermission(player.getUUID(), SectPermission.SET_TITLE)) {
            context.getSource().sendFailure(Component.literal("§cYou don't have permission to set titles!"));
            return 0;
        }

        // Find target player by name
        ServerPlayer targetPlayer = context.getSource().getServer().getPlayerList().getPlayerByName(targetName);
        if (targetPlayer == null) {
            context.getSource().sendFailure(Component.literal("§cPlayer '" + targetName + "' not found or not online!"));
            return 0;
        }

        SectMember targetMember = sect.getMember(targetPlayer.getUUID());
        if (targetMember == null) {
            context.getSource().sendFailure(Component.literal("§cPlayer '" + targetName + "' is not in your sect!"));
            return 0;
        }

        sect.setMemberTitle(targetPlayer.getUUID(), title);
        context.getSource().sendSuccess(() -> Component.literal("§aSet title for " + targetName + " to: " + title), false);

        // Notify the player
        if (targetPlayer.isAlive()) {
            targetPlayer.sendSystemMessage(Component.literal("§aYour sect title has been set to: " + title));
        }

        return 1;
    }

    private static int joinSect(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String sectName = StringArgumentType.getString(context, "sectname");

        SectManager manager = getManager(context);
        Sect sect = manager.getSect(sectName);

        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cSect '" + sectName + "' does not exist!"));
            return 0;
        }

        if (manager.getPlayerSect(player.getUUID()) != null) {
            context.getSource().sendFailure(Component.literal("§cYou are already in a sect!"));
            return 0;
        }

        if (sect.isOpen() || manager.hasInvite(sectName, player.getScoreboardName())) {
            manager.addPlayerToSect(player.getUUID(), player.getScoreboardName(), sectName);
            context.getSource().sendSuccess(() -> Component.literal("§aJoined sect '" + sectName + "'!"), false);

            // Notify sect members
            broadcastToSect(sect, context.getSource().getServer(), "§e" + player.getScoreboardName() + " has joined the sect!");
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("§cYou are not invited to this sect and it's not open!"));
            return 0;
        }
    }

    private static int openSect(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();

        SectManager manager = getManager(context);
        Sect sect = manager.getPlayerSect(player.getUUID());

        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        if (!sect.hasPermission(player.getUUID(), SectPermission.SET_OPEN)) {
            context.getSource().sendFailure(Component.literal("§cYou don't have permission to change sect openness!"));
            return 0;
        }

        sect.setOpen(!sect.isOpen());
        String status = sect.isOpen() ? "§aopen" : "§cclosed";
        context.getSource().sendSuccess(() -> Component.literal("§eSect is now " + status + " to public joining!"), true);

        // Notify sect members
        String message = sect.isOpen() ?
                "§eThe sect is now open for public joining!" :
                "§eThe sect is now closed to public joining!";
        broadcastToSect(sect, context.getSource().getServer(), message);

        return 1;
    }

    private static int sectChat(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String message = StringArgumentType.getString(context, "msg");

        SectManager manager = getManager(context);
        Sect sect = manager.getPlayerSect(player.getUUID());

        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        // Broadcast to all online sect members
        sendSectChatMessage(sect, player, message, context.getSource().getServer());

        return 1;
    }

    private static int toggleChat(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();

        SectManager manager = getManager(context);
        boolean newState = !manager.isChatToggled(player.getUUID());
        manager.setChatToggle(player.getUUID(), newState);

        String status = newState ? "§asect chat" : "§6global chat";
        context.getSource().sendSuccess(() -> Component.literal("§eChat mode set to: " + status), true);
        return 1;
    }

    private static int allySect(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String targetSectName = StringArgumentType.getString(context, "sectname");

        SectManager manager = getManager(context);
        Sect playerSect = manager.getPlayerSect(player.getUUID());

        if (playerSect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        if (!playerSect.hasPermission(player.getUUID(), SectPermission.ALLY)) {
            context.getSource().sendFailure(Component.literal("§cYou don't have permission to send ally requests!"));
            return 0;
        }

        Sect targetSect = manager.getSect(targetSectName);
        if (targetSect == null) {
            context.getSource().sendFailure(Component.literal("§cSect '" + targetSectName + "' does not exist!"));
            return 0;
        }

        if (playerSect.getName().equals(targetSectName)) {
            context.getSource().sendFailure(Component.literal("§cYou cannot ally with your own sect!"));
            return 0;
        }

        if (playerSect.getAllies().contains(targetSectName)) {
            context.getSource().sendFailure(Component.literal("§cYour sect is already allied with " + targetSectName + "!"));
            return 0;
        }

        if (manager.hasAllyRequest(targetSectName, playerSect.getName())) {
            // Mutual ally request - form alliance
            playerSect.addAlly(targetSectName);
            targetSect.addAlly(playerSect.getName());
            manager.removeAllyRequest(targetSectName, playerSect.getName());

            // Notify both sects
            broadcastToSect(playerSect, context.getSource().getServer(), "§aAlliance formed with " + targetSectName + "!");
            broadcastToSect(targetSect, context.getSource().getServer(), "§aAlliance formed with " + playerSect.getName() + "!");

            context.getSource().sendSuccess(() -> Component.literal("§aAlliance formed with " + targetSectName + "!"), false);
        } else {
            // Send ally request
            manager.addAllyRequest(playerSect.getName(), targetSectName);
            context.getSource().sendSuccess(() -> Component.literal("§aAlly request sent to " + targetSectName), false);
            broadcastToSect(targetSect, context.getSource().getServer(),
                    "§eAlly request received from " + playerSect.getName() + "! Use /sect ally " + playerSect.getName() + " to accept.");
        }

        return 1;
    }

    private static int sectInfo(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        return displaySectInfo(context, player, null);
    }

    private static int disbandSect(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();

        SectManager manager = getManager(context);
        Sect sect = manager.getPlayerSect(player.getUUID());

        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        // Check if player is the owner
        if (!sect.getOwnerId().equals(player.getUUID())) {
            context.getSource().sendFailure(Component.literal("§cOnly the sect owner can disband the sect!"));
            return 0;
        }

        String sectName = sect.getName();

        // Notify all members before disbanding
        broadcastToSect(sect, context.getSource().getServer(), "§cThe sect has been disbanded by the owner!");

        // Use the new public method to remove the sect
        manager.removeSect(sectName);

        context.getSource().sendSuccess(() -> Component.literal("§aSect '" + sectName + "' has been disbanded!"), true);
        return 1;
    }

    private static int leaveSect(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();

        SectManager manager = getManager(context);
        Sect sect = manager.getPlayerSect(player.getUUID());

        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        // Check if player is the owner
        if (sect.getOwnerId().equals(player.getUUID())) {
            context.getSource().sendFailure(Component.literal(
                    "§cAs the sect owner, you cannot leave the sect! " +
                            "Use §6/sect master <player>§c to transfer ownership, or §6/sect disband§c to delete the sect."
            ));
            return 0;
        }

        String sectName = sect.getName();
        String playerName = player.getScoreboardName();

        // Remove player from sect
        sect.removeMember(player.getUUID());

        // Use the new public method to remove player from manager
        manager.removePlayerFromSect(player.getUUID());

        // Notify sect members
        broadcastToSect(sect, context.getSource().getServer(), "§e" + playerName + " has left the sect!");

        context.getSource().sendSuccess(() -> Component.literal("§aYou have left the sect '" + sectName + "'!"), false);
        return 1;
    }

    private static int transferMaster(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String targetName = StringArgumentType.getString(context, "playername");

        SectManager manager = getManager(context);
        Sect sect = manager.getPlayerSect(player.getUUID());

        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        // Check if player is the owner
        if (!sect.getOwnerId().equals(player.getUUID())) {
            context.getSource().sendFailure(Component.literal("§cOnly the sect owner can transfer ownership!"));
            return 0;
        }

        // Find target player by name
        ServerPlayer targetPlayer = context.getSource().getServer().getPlayerList().getPlayerByName(targetName);
        if (targetPlayer == null) {
            context.getSource().sendFailure(Component.literal("§cPlayer '" + targetName + "' not found or not online!"));
            return 0;
        }

        // Check if target is in the same sect
        SectMember targetMember = sect.getMember(targetPlayer.getUUID());
        if (targetMember == null) {
            context.getSource().sendFailure(Component.literal("§cPlayer '" + targetName + "' is not in your sect!"));
            return 0;
        }

        // Check if trying to transfer to self
        if (targetPlayer.getUUID().equals(player.getUUID())) {
            context.getSource().sendFailure(Component.literal("§cYou are already the sect owner!"));
            return 0;
        }

        String sectName = sect.getName();
        String oldOwnerName = player.getScoreboardName();

        // Update ranks: old owner becomes elder, new owner becomes SECT_MASTER
        sect.setMemberRank(player.getUUID(), SectRank.ELDER);
        sect.setMemberRank(targetPlayer.getUUID(), SectRank.SECT_MASTER);

        // Update sect owner reference
        sect.setOwner(targetPlayer.getUUID());

        manager.setDirty();

        // Notify both players and the sect
        context.getSource().sendSuccess(() -> Component.literal("§aOwnership transferred to " + targetName + "!"), false);
        targetPlayer.sendSystemMessage(Component.literal("§aYou are now the owner of the sect '" + sectName + "'!"));

        broadcastToSect(sect, context.getSource().getServer(),
                "§6" + oldOwnerName + " has transferred sect ownership to " + targetName + "!");

        return 1;
    }

    private static int kickPlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String targetName = StringArgumentType.getString(context, "playername");

        SectManager manager = getManager(context);
        Sect sect = manager.getPlayerSect(player.getUUID());

        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        // Check if player has permission to kick (Elder or Sect Master)
        SectMember executorMember = sect.getMember(player.getUUID());
        if (executorMember == null || (executorMember.getRank() != SectRank.SECT_MASTER && executorMember.getRank() != SectRank.ELDER)) {
            context.getSource().sendFailure(Component.literal("§cOnly Elders and Sect Masters can kick members!"));
            return 0;
        }

        // Find target player by name
        ServerPlayer targetPlayer = context.getSource().getServer().getPlayerList().getPlayerByName(targetName);
        if (targetPlayer == null) {
            context.getSource().sendFailure(Component.literal("§cPlayer '" + targetName + "' not found or not online!"));
            return 0;
        }

        // Check if target is in the same sect
        SectMember targetMember = sect.getMember(targetPlayer.getUUID());
        if (targetMember == null) {
            context.getSource().sendFailure(Component.literal("§cPlayer '" + targetName + "' is not in your sect!"));
            return 0;
        }

        // Check if trying to kick self
        if (targetPlayer.getUUID().equals(player.getUUID())) {
            context.getSource().sendFailure(Component.literal("§cYou cannot kick yourself! Use §6/sect leave§c instead."));
            return 0;
        }

        // Check if trying to kick the Sect Master
        if (targetMember.getRank() == SectRank.SECT_MASTER) {
            context.getSource().sendFailure(Component.literal("§cYou cannot kick the Sect Master!"));
            return 0;
        }

        // Check rank hierarchy: Elders cannot kick other Elders, only Sect Master can
        if (executorMember.getRank() == SectRank.ELDER && targetMember.getRank() == SectRank.ELDER) {
            context.getSource().sendFailure(Component.literal("§cElders cannot kick other Elders! Only the Sect Master can."));
            return 0;
        }

        String sectName = sect.getName();
        String targetPlayerName = targetPlayer.getScoreboardName();

        // Remove player from sect
        sect.removeMember(targetPlayer.getUUID());
        manager.removePlayerFromSect(targetPlayer.getUUID());

        // Notify the kicked player
        targetPlayer.sendSystemMessage(Component.literal("§cYou have been kicked from the sect '" + sectName + "' by " + player.getScoreboardName() + "!"));

        // Notify sect members
        broadcastToSect(sect, context.getSource().getServer(), "§c" + targetPlayerName + " has been kicked from the sect by " + player.getScoreboardName() + "!");

        context.getSource().sendSuccess(() -> Component.literal("§aKicked " + targetPlayerName + " from the sect!"), false);
        return 1;
    }

    private static String getRankColor(SectRank rank) {
        switch (rank) {
            case SECT_MASTER: return "§c";
            case ELDER: return "§6";
            case INNER: return "§9";
            case OUTER: return "§a";
            default: return "§f";
        }
    }

    private static void broadcastToSect(Sect sect, net.minecraft.server.MinecraftServer server, String message) {
        Component msg = Component.literal(message);
        for (SectMember member : sect.getMembers().values()) {
            ServerPlayer onlinePlayer = server.getPlayerList().getPlayer(member.getPlayerId());
            if (onlinePlayer != null) {
                onlinePlayer.sendSystemMessage(msg);
            }
        }
    }

    private static void sendSectChatMessage(Sect sect, ServerPlayer sender, String message, net.minecraft.server.MinecraftServer server) {
        String formattedMessage = "§3[§b" + sect.getName() + "§3] §e" + sender.getScoreboardName() + "§7: §f" + message;
        Component chatMessage = Component.literal(formattedMessage);

        for (SectMember member : sect.getMembers().values()) {
            ServerPlayer onlinePlayer = server.getPlayerList().getPlayer(member.getPlayerId());
            if (onlinePlayer != null) {
                onlinePlayer.sendSystemMessage(chatMessage);
            }
        }
    }

    private static int sectInfoTarget(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String target = StringArgumentType.getString(context, "target");
        return displaySectInfo(context, player, target);
    }

    private static int displaySectInfo(CommandContext<CommandSourceStack> context, ServerPlayer player, String target) throws CommandSyntaxException {
        SectManager manager = getManager(context);
        Sect sect = null;
        String displayName;

        if (target == null) {
            // Show own sect info
            sect = manager.getPlayerSect(player.getUUID());
            if (sect == null) {
                context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
                return 0;
            }
            displayName = "Your Sect";
        } else {
            // Check if target is a sect name
            sect = manager.getSect(target);
            if (sect != null) {
                displayName = "Sect '" + target + "'";
            } else {
                // Check if target is a player name
                ServerPlayer targetPlayer = context.getSource().getServer().getPlayerList().getPlayerByName(target);
                if (targetPlayer != null) {
                    sect = manager.getPlayerSect(targetPlayer.getUUID());
                    if (sect != null) {
                        displayName = targetPlayer.getScoreboardName() + "'s Sect";
                    } else {
                        displayName = "";
                        context.getSource().sendFailure(Component.literal("§cPlayer '" + target + "' is not in a sect!"));
                        return 0;
                    }
                } else {
                    displayName = "";
                    context.getSource().sendFailure(Component.literal("§cNo sect or player found with name '" + target + "'!"));
                    return 0;
                }
            }
        }

        // Display sect information
        context.getSource().sendSuccess(() -> Component.literal("§6=== " + displayName + " Info ==="), false);
        Sect finalSect = sect;
        context.getSource().sendSuccess(() -> Component.literal("§eSect: §f" + finalSect.getName()), false);

        // Owner info
        SectMember owner = sect.getMember(sect.getOwnerId());
        if (owner != null) {
            context.getSource().sendSuccess(() -> Component.literal("§eOwner: §f" + owner.getPlayerName()), false);
        }

        // Open status
        String openStatus = sect.isOpen() ? "§aOpen" : "§cClosed";
        context.getSource().sendSuccess(() -> Component.literal("§eStatus: " + openStatus), false);

        // Allies
        StringBuilder allies = new StringBuilder("§eAllies: §f");
        if (sect.getAllies().isEmpty()) {
            allies.append("None");
        } else {
            allies.append(String.join(", ", sect.getAllies()));
        }
        context.getSource().sendSuccess(() -> Component.literal(allies.toString()), false);

        // Members
        Sect finalSect1 = sect;
        context.getSource().sendSuccess(() -> Component.literal("§eMembers (" + finalSect1.getMembers().size() + "):"), false);
        for (SectMember member : sect.getMembers().values()) {
            String rankColor = getRankColor(member.getRank());
            String title = member.getTitle().isEmpty() ? "" : " §7" + member.getTitle();
            String memberInfo = "  §7- " + rankColor + member.getPlayerName() + " §8[" + member.getRank().getDisplayName() + "§8]" + title;
            context.getSource().sendSuccess(() -> Component.literal(memberInfo), false);
        }

        return 1;
    }
}