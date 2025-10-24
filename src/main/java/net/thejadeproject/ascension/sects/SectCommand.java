package net.thejadeproject.ascension.sects;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.sects.missions.MissionProgress;
import net.thejadeproject.ascension.sects.missions.MissionRequirement;
import net.thejadeproject.ascension.sects.missions.SectMission;
import net.thejadeproject.ascension.sects.missions.SectMissionEventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
                .then(Commands.literal("kick")
                        .then(Commands.argument("playername", StringArgumentType.string())
                                .executes(SectCommand::kickPlayer)))
                .then(Commands.literal("desc")
                        .then(Commands.argument("description", StringArgumentType.greedyString())
                                .executes(SectCommand::setDescription)))
                .then(Commands.literal("list")
                        .executes(SectCommand::listSects))
                .then(Commands.literal("missions")
                        .executes(SectCommand::listMissions))
                .then(Commands.literal("mission")
                        .then(Commands.literal("create")
                                .then(Commands.argument("displayName", StringArgumentType.string())
                                        .then(Commands.argument("requirements", StringArgumentType.greedyString())
                                                .executes(SectCommand::createMission)))))
                .then(Commands.literal("mission")
                        .then(Commands.literal("accept")
                                .then(Commands.argument("missionId", StringArgumentType.string())
                                        .executes(SectCommand::acceptMission)))
                        .then(Commands.literal("complete")
                                .then(Commands.argument("missionId", StringArgumentType.string())
                                        .executes(SectCommand::completeMission))))
                .then(Commands.literal("merit")
                        .executes(SectCommand::showMerit))
                .then(Commands.literal("claim")
                        .then(Commands.literal("mission")
                                .then(Commands.literal("submissions")
                                        .executes(SectCommand::claimMissionSubmissions))))
                //sect settings
                .then(Commands.literal("settings")
                        .then(Commands.literal("friendlyfire")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(SectCommand::setFriendlyFire))))
        );
    }

    private static SectManager getManager(CommandContext<CommandSourceStack> context) {
        return AscensionCraft.getSectManager(context.getSource().getServer());
    }

    // NEW: Set description command
    private static int setDescription(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String description = StringArgumentType.getString(context, "description");

        SectManager manager = getManager(context);
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

        Sect sect = manager.getPlayerSect(player.getUUID());
        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        // Check permissions - Elders and Sect Master can set description
        SectMember member = sect.getMember(player.getUUID());
        if (member == null || (member.getRank() != SectRank.SECT_MASTER && member.getRank() != SectRank.ELDER)) {
            context.getSource().sendFailure(Component.literal("§cOnly Elders and Sect Masters can set the sect description!"));
            return 0;
        }

        // Limit description length
        if (description.length() > 200) {
            context.getSource().sendFailure(Component.literal("§cDescription too long! Maximum 200 characters."));
            return 0;
        }

        sect.setDescription(description);
        manager.setDirty();

        context.getSource().sendSuccess(() -> Component.literal("§aSect description updated!"), true);

        // Notify sect members
        broadcastToSect(sect, context.getSource().getServer(),
                "§e" + player.getScoreboardName() + " updated the sect description!");

        return 1;
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
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

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
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

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
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

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
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

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
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

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
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

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
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

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
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

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
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

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
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

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
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

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
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

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
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

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
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

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

    static void broadcastToSect(Sect sect, net.minecraft.server.MinecraftServer server, String message) {
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
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

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

        // NEW: Display description
        context.getSource().sendSuccess(() -> Component.literal("§eDescription: §7" + finalSect.getDescription()), false);

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

    private static int listMissions(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();

        SectManager manager = getManager(context);
        Sect sect = manager.getPlayerSect(player.getUUID());

        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        SectMember member = sect.getMember(player.getUUID());
        List<SectMission> availableMissions;

        if (member.getRank() == SectRank.OUTER) {
            availableMissions = sect.getMissionsForRank(SectRank.OUTER);
        } else if (member.getRank() == SectRank.INNER) {
            availableMissions = sect.getMissionsForRank(SectRank.INNER);
        } else {
            // Elder and Sect Master can see all missions
            availableMissions = new ArrayList<>(sect.getMissions());
        }

        context.getSource().sendSuccess(() -> Component.literal("§6=== Available Missions ==="), false);

        if (availableMissions.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("§7No missions available."), false);
        } else {
            for (SectMission mission : availableMissions) {
                boolean isAccepted = mission.isAcceptedBy(player.getUUID());
                MissionProgress progress = mission.getProgress(player.getUUID());
                boolean canComplete = progress != null && progress.isCompleted();

                String status = canComplete ? "§a[COMPLETE]" : isAccepted ? "§e[IN PROGRESS]" : "§7[AVAILABLE]";

                Component missionText = Component.literal(status + " " + mission.getDisplayName())
                        .withStyle(style -> style
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, createMissionHoverText(mission, progress)))
                                .withClickEvent(canComplete ?
                                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sect mission complete " + mission.getMissionId()) :
                                        isAccepted ? null :
                                                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sect mission accept " + mission.getMissionId())));

                context.getSource().sendSuccess(() -> missionText, false);
            }
        }

        // Show current merit points
        int merit = sect.getPlayerMerit(player.getUUID());
        String nextPromotion;
        if (member.getRank() == SectRank.OUTER) {
            nextPromotion = " §7(§e" + merit + "§7/§a2500§7 for promotion to Inner)";
        } else if (member.getRank() == SectRank.INNER) {
            nextPromotion = " §7(§e" + merit + "§7/§610000§7 for promotion to Elder)";
        } else {
            nextPromotion = "";
        }

        context.getSource().sendSuccess(() -> Component.literal("§eYour Merit Points: §6" + merit + nextPromotion), false);

        return 1;
    }

    private static Component createMissionHoverText(SectMission mission, MissionProgress progress) {
        MutableComponent text = Component.literal("§e" + mission.getDisplayName() + "\n\n§7Requirements:\n");

        for (MissionRequirement req : mission.getRequirements()) {
            String requirementText = "";
            switch (req.getType()) {
                case ITEM:
                    requirementText = "Collect " + req.getCount() + "x " + req.getTarget();
                    break;
                case KILL_MOB:
                    requirementText = "Kill " + req.getCount() + "x " + req.getTarget();
                    break;
            }

            if (progress != null) {
                requirementText += " §8(" + progress.getProgress() + "/" + req.getCount() + ")";
            }

            text.append(Component.literal("§7- " + requirementText + "\n"));
        }

        text.append(Component.literal("\n§6Reward: §e" + mission.getRewardMerit() + " Merit Points"));
        if (!mission.getRewardItem().isEmpty()) {
            text.append(Component.literal(" + ").append(mission.getRewardItem().getDisplayName()));
        }

        if (mission.isRepeatable()) {
            text.append(Component.literal("\n§a✓ Repeatable"));
        }

        return text;
    }

    private static int createMission(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String displayName = StringArgumentType.getString(context, "displayName");
        String requirementsStr = StringArgumentType.getString(context, "requirements");

        SectManager manager = getManager(context);
        Sect sect = manager.getPlayerSect(player.getUUID());

        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        if (!sect.hasPermission(player.getUUID(), SectPermission.PROMOTE)) {
            context.getSource().sendFailure(Component.literal("§cOnly Elders and Sect Masters can create missions!"));
            return 0;
        }

        // Parse requirements (this is a simplified version - you'd want more robust parsing)
        String[] parts = requirementsStr.split(" ");
        // Implementation would parse the requirements and create the mission

        context.getSource().sendSuccess(() -> Component.literal("§aMission '" + displayName + "' created!"), false);
        return 1;
    }


    private static int acceptMission(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String missionIdStr = StringArgumentType.getString(context, "missionId");

        SectManager manager = getManager(context);
        Sect sect = manager.getPlayerSect(player.getUUID());

        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        try {
            UUID missionId = UUID.fromString(missionIdStr);
            SectMission mission = sect.getMission(missionId);

            if (mission == null) {
                context.getSource().sendFailure(Component.literal("§cMission not found!"));
                return 0;
            }

            // Check if player can accept this mission based on rank
            SectMember member = sect.getMember(player.getUUID());
            if (member.getRank().getLevel() < mission.getTargetRank().getLevel()) {
                context.getSource().sendFailure(Component.literal("§cYour rank is too low to accept this mission!"));
                return 0;
            }

            if (!mission.canAccept(player.getUUID())) {
                context.getSource().sendFailure(Component.literal("§cYou have already accepted this mission!"));
                return 0;
            }

            mission.acceptMission(player.getUUID());
            manager.setDirty();

            context.getSource().sendSuccess(() -> Component.literal("§aMission '" + mission.getDisplayName() + "' accepted!"), false);
            return 1;

        } catch (IllegalArgumentException e) {
            context.getSource().sendFailure(Component.literal("§cInvalid mission ID!"));
            return 0;
        }
    }

    private static int completeMission(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String missionIdStr = StringArgumentType.getString(context, "missionId");

        try {
            UUID missionId = UUID.fromString(missionIdStr);

            if (SectMissionEventHandler.claimMissionReward(player, missionId)) {
                context.getSource().sendSuccess(() -> Component.literal("§aMission completed and rewards claimed!"), false);
                return 1;
            } else {
                context.getSource().sendFailure(Component.literal("§cCannot complete mission! Make sure you have the required items and the mission is ready for completion."));
                return 0;
            }

        } catch (IllegalArgumentException e) {
            context.getSource().sendFailure(Component.literal("§cInvalid mission ID!"));
            return 0;
        }
    }

    private static int showMerit(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();

        SectManager manager = getManager(context);
        Sect sect = manager.getPlayerSect(player.getUUID());

        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        SectMember member = sect.getMember(player.getUUID());
        int currentMerit = sect.getPlayerMerit(player.getUUID());

        context.getSource().sendSuccess(() -> Component.literal("§6=== Your Merit Points ==="), false);
        context.getSource().sendSuccess(() -> Component.literal("§eCurrent Merit: §6" + currentMerit), false);

        // Show different progress bars based on rank
        if (member.getRank() == SectRank.OUTER) {
            showFancyProgressBar(context, currentMerit, 2500, "Inner Sect", "§2", "§a", "§8");
            context.getSource().sendSuccess(() -> Component.literal("§7Next Rank: §aInner Sect §7(§62500§7 merit required)"), false);
        } else if (member.getRank() == SectRank.INNER) {
            showFancyProgressBar(context, currentMerit, 10000, "Elder", "§6", "§e", "§8");
            context.getSource().sendSuccess(() -> Component.literal("§7Next Rank: §6Elder §7(§610000§7 merit required)"), false);
        } else if (member.getRank() == SectRank.ELDER) {
            context.getSource().sendSuccess(() -> Component.literal("§6You are an Elder! Next rank: §cSect Master"), false);
            context.getSource().sendSuccess(() -> Component.literal("§7Promotion to Sect Master must be done manually by the current Sect Master."), false);
        } else if (member.getRank() == SectRank.SECT_MASTER) {
            context.getSource().sendSuccess(() -> Component.literal("§cYou are the Sect Master! This is the highest rank."), false);
            // Show total merit for Sect Master
            context.getSource().sendSuccess(() -> Component.literal("§6Total Merit Points: §e" + currentMerit), false);
        }

        return 1;
    }

    private static void showFancyProgressBar(CommandContext<CommandSourceStack> context, int current, int required, String nextRank, String startColor, String filledColor, String emptyColor) {
        int barLength = 30; // Longer bar for more granularity
        float progress = Math.min((float) current / required, 1.0f);
        int filledChars = (int) (progress * barLength);

        // Create the fancy progress bar with gradient effect
        StringBuilder progressBar = new StringBuilder();
        progressBar.append("§7[");

        // Add gradient filled segments
        for (int i = 0; i < filledChars; i++) {
            float segmentProgress = (float) i / barLength;
            String segmentColor = getGradientColor(segmentProgress, startColor, filledColor);
            progressBar.append(segmentColor).append("█");
        }

        // Add empty segments
        for (int i = filledChars; i < barLength; i++) {
            progressBar.append(emptyColor).append("▒");
        }

        progressBar.append("§7] §e").append(String.format("%.1f", progress * 100)).append("%");

        context.getSource().sendSuccess(() -> Component.literal(progressBar.toString()), false);

        // Show detailed numbers
        int remaining = Math.max(0, required - current);
        String percentage = String.format("%.1f", progress * 100);

        if (remaining > 0) {
            context.getSource().sendSuccess(() -> Component.literal("§7Progress: §e" + current + "§7/§6" + required + " §7(§e" + percentage + "%§7)"), false);
            context.getSource().sendSuccess(() -> Component.literal("§7Need §e" + remaining + "§7 more merit for " + nextRank), false);
        } else {
            context.getSource().sendSuccess(() -> Component.literal("§a✓ You have reached the required merit for " + nextRank + "!"), false);
            context.getSource().sendSuccess(() -> Component.literal("§aYou will be automatically promoted soon."), false);
        }
    }

    private static String getGradientColor(float progress, String startColor, String endColor) {
        // Simple gradient from start color to end color
        // For more advanced gradients, you could interpolate between RGB values
        if (progress < 0.5f) {
            return startColor;
        } else {
            return endColor;
        }
    }

    private static int claimMissionSubmissions(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();

        SectManager manager = getManager(context);
        Sect sect = manager.getPlayerSect(player.getUUID());

        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        if (!sect.hasPermission(player.getUUID(), SectPermission.PROMOTE)) {
            context.getSource().sendFailure(Component.literal("§cOnly Elders and Sect Masters can claim mission submissions!"));
            return 0;
        }

        Map<UUID, List<ItemStack>> submissions = sect.getMissionSubmissions();
        if (submissions.isEmpty()) {
            context.getSource().sendFailure(Component.literal("§cThere are no mission submissions to claim!"));
            return 0;
        }

        int totalItems = 0;
        int totalStacks = 0;

        // Give all submitted items to the player
        for (Map.Entry<UUID, List<ItemStack>> entry : submissions.entrySet()) {
            UUID missionId = entry.getKey();
            List<ItemStack> items = entry.getValue();

            // Find mission name for display
            SectMission mission = sect.getMission(missionId);
            String missionName = mission != null ? mission.getDisplayName() : "Unknown Mission";

            for (ItemStack stack : items) {
                if (!stack.isEmpty()) {
                    totalStacks++;
                    totalItems += stack.getCount();

                    // Try to add to player inventory
                    if (!player.getInventory().add(stack.copy())) {
                        // Drop if inventory is full
                        player.drop(stack.copy(), false);
                    }
                }
            }

            // Remove this mission's submissions after claiming
            sect.removeMissionSubmission(missionId);
        }
        manager.setDirty();

        int finalTotalItems = totalItems;
        int finalTotalStacks = totalStacks;
        context.getSource().sendSuccess(() -> Component.literal("§aClaimed §e" + finalTotalItems + "§a items (§e" + finalTotalStacks + "§a stacks) from mission submissions!"), false);

        // Show breakdown of what was claimed
        if (!submissions.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("§7All submitted items have been added to your inventory."), false);
            context.getSource().sendSuccess(() -> Component.literal("§7If your inventory was full, items were dropped on the ground."), false);
        }

        return 1;
    }

    // NEW: List all sects command
    private static int listSects(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        SectManager manager = getManager(context);
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

        // Get all sects using the new public method
        Map<String, Sect> allSects = manager.getAllSects();

        if (allSects.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("§7No sects have been created on this world yet."), false);
            context.getSource().sendSuccess(() -> Component.literal("§7Use §e/sect create <name>§7 to create one!"), false);
            return 1;
        }

        // Sort sects by member count (descending) then by name
        List<Map.Entry<String, Sect>> sortedSects = allSects.entrySet().stream()
                .sorted((a, b) -> {
                    int memberCompare = Integer.compare(b.getValue().getMembers().size(), a.getValue().getMembers().size());
                    if (memberCompare != 0) return memberCompare;
                    return a.getKey().compareToIgnoreCase(b.getKey());
                })
                .toList();

        context.getSource().sendSuccess(() -> Component.literal("§6=== All Sects on this World (" + sortedSects.size() + ") ==="), false);

        for (Map.Entry<String, Sect> entry : sortedSects) {
            Sect sect = entry.getValue();
            String sectName = entry.getKey();

            // Get owner name
            SectMember owner = sect.getMember(sect.getOwnerId());
            String ownerName = owner != null ? owner.getPlayerName() : "Unknown";

            int memberCount = sect.getMembers().size();
            String openStatus = sect.isOpen() ? "§aOpen" : "§cClosed";
            String statusColor = sect.isOpen() ? "§a" : "§c";

            // Create the main sect line
            MutableComponent sectLine = Component.literal("§e- " + sectName)
                    .append(Component.literal(" §7(Members: §e" + memberCount + "§7, Status: " + statusColor + (sect.isOpen() ? "Open" : "Closed") + "§7)"));

            // Create hover text with detailed info
            MutableComponent hoverText = Component.literal("§6" + sectName + "\n")
                    .append(Component.literal("§cSect Master: §f" + ownerName + "\n"))
                    .append(Component.literal("§bMembers: §e" + memberCount + "\n"))
                    .append(Component.literal("§eStatus: " + statusColor + (sect.isOpen() ? "Open to Join" : "Invite Only") + "\n"))
                    .append(Component.literal("§aDescription: §f" + sect.getDescription() + "\n\n"))
                    .append(Component.literal("§aClick to view detailed info"));

            // Add click event to run /sect info command
            sectLine.withStyle(style -> style
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText))
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sect info " + sectName)));

            context.getSource().sendSuccess(() -> sectLine, false);
        }

        // Add helpful footer
        context.getSource().sendSuccess(() -> Component.literal("§7Click on a sect name to view more information."), false);
        if (context.getSource().getEntity() instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) context.getSource().getEntity();
            Sect playerSect = manager.getPlayerSect(player.getUUID());
            if (playerSect == null) {
                context.getSource().sendSuccess(() -> Component.literal("§7Use §e/sect join <name>§7 to join an open sect!"), false);
            }
        }

        return 1;
    }

    // NEW: Set friendly fire command
    private static int setFriendlyFire(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        boolean value = BoolArgumentType.getBool(context, "value");

        SectManager manager = getManager(context);
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

        Sect sect = manager.getPlayerSect(player.getUUID());
        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        // Check if player is the Sect Master
        if (!sect.getOwnerId().equals(player.getUUID())) {
            context.getSource().sendFailure(Component.literal("§cOnly the Sect Master can change sect settings!"));
            return 0;
        }

        sect.setFriendlyFire(value);
        manager.setDirty();

        String status = value ? "§aenabled" : "§cdisabled";
        context.getSource().sendSuccess(() -> Component.literal("§eFriendly fire is now " + status + " for your sect!"), true);

        // Notify sect members
        String message = value ?
                "§eFriendly fire has been §aenabled§e. Members can now damage each other." :
                "§eFriendly fire has been §cdisabled§e. Members can no longer damage each other.";
        broadcastToSect(sect, context.getSource().getServer(), message);

        return 1;
    }
}