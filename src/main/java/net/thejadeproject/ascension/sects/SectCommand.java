package net.thejadeproject.ascension.sects;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.sects.missions.MissionProgress;
import net.thejadeproject.ascension.sects.missions.MissionRequirement;
import net.thejadeproject.ascension.sects.missions.SectMission;
import net.thejadeproject.ascension.sects.missions.SectMissionEventHandler;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SectCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sect")
                .then(Commands.literal("create")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(SectCommand::createSect)))
                .then(Commands.literal("invite")
                        .then(Commands.argument("playername", StringArgumentType.string())
                                .suggests((context, builder) -> {
                                    ServerPlayer player = context.getSource().getPlayer();
                                    if (player != null) {
                                        SectManager manager = AscensionCraft.getSectManager(context.getSource().getServer());
                                        if (manager != null) {
                                            Sect sect = manager.getPlayerSect(player.getUUID());
                                            if (sect != null && sect.hasPermission(player.getUUID(), SectPermission.INVITE)) {
                                                // Get all online players and filter out those already in a sect
                                                Collection<ServerPlayer> onlinePlayers = context.getSource().getServer().getPlayerList().getPlayers();
                                                for (ServerPlayer onlinePlayer : onlinePlayers) {
                                                    // Don't suggest players who are already in a sect or the inviting player themselves
                                                    if (onlinePlayer != player && manager.getPlayerSect(onlinePlayer.getUUID()) == null) {
                                                        builder.suggest(onlinePlayer.getGameProfile().getName());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(SectCommand::invitePlayer)))
                .then(Commands.literal("merit")
                        .executes(SectCommand::showMerit))

                        .then(Commands.literal("rename")
                                .then(Commands.argument("new_name", StringArgumentType.string())
                                        .executes(SectCommand::renameSect)))

                .then(Commands.literal("recommend")
                        .then(Commands.argument("playername", StringArgumentType.string())
                                .suggests((context, builder) -> suggestInnerMembers(context, builder))
                                .executes(SectCommand::recommendPlayer)))
                .then(Commands.literal("claim")
                        .then(Commands.argument("radius", IntegerArgumentType.integer(1, 10))
                                .executes(SectCommand::claimChunksWithRadius))
                        .executes(SectCommand::claimSingleChunk))
                .then(Commands.literal("deposit")
                        .executes(SectCommand::depositMerit)) // No integer argument anymore
                .then(Commands.literal("enemy")
                        .then(Commands.argument("sectname", StringArgumentType.string())
                                .executes(SectCommand::setEnemy)))
                .then(Commands.literal("unclaim")
                        .executes(SectCommand::unclaimChunk)
                        .then(Commands.argument("radius", IntegerArgumentType.integer(1, 10))
                                .executes(SectCommand::unclaimChunks)))
                .then(Commands.literal("map")
                        .executes(SectCommand::showClaimMap))
                //sect settings
                .then(Commands.literal("settings")
                        .then(Commands.literal("friendlyfire")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(SectCommand::setFriendlyFire))))
                .then(Commands.literal("help")
                        .executes(SectCommand::help))
                .then(Commands.literal("decline")
                        .then(Commands.argument("sectname", StringArgumentType.string())
                                .executes(SectCommand::declineInvite)))
                .then(Commands.literal("promote")
                        .then(Commands.argument("playername", StringArgumentType.string())
                                .suggests((context, builder) -> suggestSectMembers(context, builder, false))
                                .executes(SectCommand::promotePlayer)))
                .then(Commands.literal("demote")
                        .then(Commands.argument("playername", StringArgumentType.string())
                                .suggests((context, builder) -> suggestSectMembers(context, builder, false))
                                .executes(SectCommand::demotePlayer)))
                .then(Commands.literal("title")
                        .then(Commands.argument("playername", StringArgumentType.string())
                                .suggests((context, builder) -> suggestSectMembers(context, builder, true))
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
                                        .executes(SectCommand::allySect))
                                .then(Commands.literal("accept")
                                        .then(Commands.argument("sectname", StringArgumentType.string())
                                                .executes(SectCommand::acceptAlly)))
                                .then(Commands.literal("decline")
                                        .then(Commands.argument("sectname", StringArgumentType.string())
                                                .executes(SectCommand::declineAlly)))
                                .then(Commands.literal("requests")
                                        .executes(SectCommand::listAllyRequests)))
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
                                .suggests((context, builder) -> suggestSectMembers(context, builder, false))
                                .executes(SectCommand::transferMaster)))
                .then(Commands.literal("kick")
                        .then(Commands.argument("playername", StringArgumentType.string())
                                .suggests((context, builder) -> suggestSectMembers(context, builder, false))
                                .executes(SectCommand::kickPlayer)))
                .then(Commands.literal("desc")
                        .then(Commands.argument("description", StringArgumentType.greedyString())
                                .executes(SectCommand::setDescription)))
                .then(Commands.literal("list")
                        .executes(SectCommand::listSects))
                .then(Commands.literal("missions")
                        .executes(SectCommand::listMissions))
                .then(Commands.literal("mission")
                        .then(Commands.literal("accept")
                                .then(Commands.argument("missionId", StringArgumentType.string())
                                        .executes(SectCommand::acceptMission)))
                        .then(Commands.literal("claim")
                                .then(Commands.literal("submissions")
                                        .executes(SectCommand::claimMissionSubmissions)))
                        .then(Commands.literal("complete")
                                .then(Commands.argument("missionId", StringArgumentType.string())
                                        .executes(SectCommand::completeMission)))
                        .then(Commands.literal("create")
                                .then(Commands.argument("displayName", StringArgumentType.string())
                                        .then(Commands.argument("targetRank", StringArgumentType.word())
                                                .then(Commands.argument("meritReward", IntegerArgumentType.integer(1))
                                                        .then(Commands.argument("duration", StringArgumentType.string())
                                                                .then(Commands.argument("useHandItem", BoolArgumentType.bool())
                                                                        .then(Commands.argument("requirements", StringArgumentType.greedyString())
                                                                                .executes(SectCommand::createMissionAdvanced)))))))))
                );
        debugCommandRegistration(dispatcher);
    }

    private static void debugCommandRegistration(CommandDispatcher<CommandSourceStack> dispatcher) {
        System.out.println("=== SECT COMMAND REGISTRATION DEBUG ===");

        // Check if /sect exists
        if (dispatcher.findNode(Collections.singletonList("sect")) != null) {
            System.out.println("✓ /sect command registered");

            // Check if /sect mission exists
            if (dispatcher.findNode(Arrays.asList("sect", "mission")) != null) {
                System.out.println("✓ /sect mission command registered");

                // Check if /sect mission accept exists
                if (dispatcher.findNode(Arrays.asList("sect", "mission", "accept")) != null) {
                    System.out.println("✓ /sect mission accept command registered");
                } else {
                    System.out.println("✗ /sect mission accept command NOT registered");
                }

                // Check if /sect mission complete exists
                if (dispatcher.findNode(Arrays.asList("sect", "mission", "complete")) != null) {
                    System.out.println("✓ /sect mission complete command registered");
                } else {
                    System.out.println("✗ /sect mission complete command NOT registered");
                }
            } else {
                System.out.println("✗ /sect mission command NOT registered");
            }
        } else {
            System.out.println("✗ /sect command NOT registered");
        }
        System.out.println("=====================================");
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

        // Find the target player
        ServerPlayer targetPlayer = context.getSource().getServer().getPlayerList().getPlayerByName(targetName);
        if (targetPlayer == null) {
            context.getSource().sendFailure(Component.literal("§cPlayer '" + targetName + "' is not online!"));
            return 0;
        }

        // Check if target is already in a sect
        if (manager.getPlayerSect(targetPlayer.getUUID()) != null) {
            context.getSource().sendFailure(Component.literal("§c" + targetName + " is already in a sect!"));
            return 0;
        }

        // Check if trying to invite yourself
        if (targetPlayer.getUUID().equals(player.getUUID())) {
            context.getSource().sendFailure(Component.literal("§cYou cannot invite yourself!"));
            return 0;
        }

        manager.addInvite(sect.getName(), targetName);
        context.getSource().sendSuccess(() -> Component.literal("§aInvited " + targetName + " to join your sect!"), false);

        // ENHANCED INVITATION MESSAGE WITH CLICKABLE BUTTONS
        sendEnhancedInvitationMessage(targetPlayer, sect, player);

        return 1;
    }

    private static void sendEnhancedInvitationMessage(ServerPlayer targetPlayer, Sect sect, ServerPlayer inviter) {
        // Get the sect name and ensure it's URL-safe
        String sectName = sect.getName().replace(" ", "_");

        // Create the main invitation message
        MutableComponent inviteMessage = Component.literal("§6§lSECT INVITATION\n§r")
                .append(Component.literal("§eYou have been invited to join §b" + sect.getName() + "§e!\n"))
                .append(Component.literal("§7Invited by: §f" + inviter.getScoreboardName() + "\n\n"));

        // Create clickable accept button with properly encoded sect name
        MutableComponent acceptButton = Component.literal("[§a✔ ACCEPT§r]")
                .withStyle(Style.EMPTY
                        .withColor(ChatFormatting.GREEN)
                        .withBold(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sect join " + sectName))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.literal("§aClick to join §b" + sect.getName() + "§a!")))
                );

        // Create clickable decline button with properly encoded sect name
        MutableComponent declineButton = Component.literal("[§c✖ DECLINE§r]")
                .withStyle(Style.EMPTY
                        .withColor(ChatFormatting.RED)
                        .withBold(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sect decline " + sectName))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.literal("§cClick to decline the invitation from §b" + sect.getName() + "§c!")))
                );

        // Combine everything
        inviteMessage.append(Component.literal("§eClick to respond: "))
                .append(acceptButton)
                .append(Component.literal(" §8| "))
                .append(declineButton)
                .append(Component.literal("\n§7Or type: §a/sect join " + sectName));

        targetPlayer.sendSystemMessage(inviteMessage);
    }

    private static int declineInvite(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String sectName = StringArgumentType.getString(context, "sectname").replace('_', ' ');

        SectManager manager = getManager(context);
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

        // Use the new getPendingInvites method to find the exact sect name
        List<String> pendingInvites = manager.getPendingInvites(player.getScoreboardName());
        String exactSectName = pendingInvites.stream().filter(invitedSect -> invitedSect.equalsIgnoreCase(sectName)).findFirst().orElse(null);

        if (exactSectName == null) {
            context.getSource().sendFailure(Component.literal("§cYou don't have an invitation from §e" + sectName + "§c!"));
            return 0;
        }

        // Remove the invite using the exact sect name
        Set<String> invites = manager.pendingInvites.get(exactSectName);
        if (invites != null) {
            invites.remove(player.getScoreboardName().toLowerCase());
            if (invites.isEmpty()) {
                manager.pendingInvites.remove(exactSectName);
            }
        }

        context.getSource().sendSuccess(() -> Component.literal("§eYou have declined the invitation from §b" + exactSectName + "§e."), false);

        // Optional: Notify the sect leader who sent the invite
        Sect sect = manager.getSect(exactSectName);
        if (sect != null) {
            ServerPlayer inviter = context.getSource().getServer().getPlayerList().getPlayer(sect.getOwnerId());
            if (inviter != null) {
                inviter.sendSystemMessage(Component.literal("§e" + player.getScoreboardName() + " §7has declined your sect invitation."));
            }
        }

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
        String sectName = StringArgumentType.getString(context, "sectname").replace('_', ' ');

        SectManager manager = getManager(context);
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

        // Try exact match first
        Sect sect = manager.getSect(sectName);

        // If exact match fails, try case-insensitive search
        if (sect == null) {
            sect = findSectByNameIgnoreCase(manager, sectName);
        }

        if (sect == null) {
            context.getSource().sendFailure(Component.literal("§cSect '" + sectName + "' does not exist!"));
            return 0;
        }

        if (manager.getPlayerSect(player.getUUID()) != null) {
            context.getSource().sendFailure(Component.literal("§cYou are already in a sect!"));
            return 0;
        }

        if (sect.isOpen() || manager.hasInvite(sectName, player.getScoreboardName())) {
            manager.addPlayerToSect(player.getUUID(), player.getScoreboardName(), sect.getName());
            Sect finalSect = sect;
            context.getSource().sendSuccess(() -> Component.literal("§aJoined sect '" + finalSect.getName() + "'!"), false);

            // Notify sect members
            broadcastToSect(sect, context.getSource().getServer(), "§e" + player.getScoreboardName() + " has joined the sect!");
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("§cYou are not invited to this sect and it's not open!"));
            return 0;
        }
    }

    // Helper method for case-insensitive sect search
    private static Sect findSectByNameIgnoreCase(SectManager manager, String sectName) {
        for (Sect sect : manager.getAllSects().values()) {
            if (sect.getName().equalsIgnoreCase(sectName)) {
                return sect;
            }
        }
        return null;
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

            // Send enhanced notification to target sect's Elders and Sect Master
            sendEnhancedAllyRequest(targetSect, playerSect, player, context.getSource().getServer());
        }

        return 1;
    }

    private static void sendEnhancedAllyRequest(Sect targetSect, Sect requestingSect, ServerPlayer requester, net.minecraft.server.MinecraftServer server) {
        String requestingSectName = requestingSect.getName();
        String targetSectName = targetSect.getName();

        // Create the main invitation message
        MutableComponent allyMessage = Component.literal("§6§lALLY REQUEST\n§r")
                .append(Component.literal("§eYou have received an alliance request from §b" + requestingSectName + "§e!\n"))
                .append(Component.literal("§7Requested by: §f" + requester.getScoreboardName() + "\n\n"));

        // Create clickable accept button
        MutableComponent acceptButton = Component.literal("[§a✔ ACCEPT§r]")
                .withStyle(Style.EMPTY
                        .withColor(ChatFormatting.GREEN)
                        .withBold(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sect ally accept " + requestingSectName.replace(" ", "_")))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.literal("§aClick to accept alliance with §b" + requestingSectName + "§a!")))
                );

        // Create clickable decline button
        MutableComponent declineButton = Component.literal("[§c✖ DECLINE§r]")
                .withStyle(Style.EMPTY
                        .withColor(ChatFormatting.RED)
                        .withBold(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sect ally decline " + requestingSectName.replace(" ", "_")))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.literal("§cClick to decline alliance with §b" + requestingSectName + "§c!")))
                );

        // Combine everything
        allyMessage.append(Component.literal("§eClick to respond: "))
                .append(acceptButton)
                .append(Component.literal(" §8| "))
                .append(declineButton)
                .append(Component.literal("\n§7Or type: §a/sect ally accept " + requestingSectName.replace(" ", "_")));

        // Send to all Elders and Sect Master in the target sect
        for (SectMember member : targetSect.getMembers().values()) {
            if (member.getRank() == SectRank.ELDER || member.getRank() == SectRank.SECT_MASTER) {
                ServerPlayer onlineMember = server.getPlayerList().getPlayer(member.getPlayerId());
                if (onlineMember != null) {
                    onlineMember.sendSystemMessage(allyMessage);
                }
            }
        }
    }

    private static int acceptAlly(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String requestingSectName = StringArgumentType.getString(context, "sectname").replace('_', ' ');

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
            context.getSource().sendFailure(Component.literal("§cYou don't have permission to accept ally requests!"));
            return 0;
        }

        Sect requestingSect = manager.getSect(requestingSectName);
        if (requestingSect == null) {
            context.getSource().sendFailure(Component.literal("§cSect '" + requestingSectName + "' does not exist!"));
            return 0;
        }

        if (!manager.hasAllyRequest(requestingSectName, playerSect.getName())) {
            context.getSource().sendFailure(Component.literal("§cNo pending ally request from §e" + requestingSectName + "§c!"));
            return 0;
        }

        // Form alliance
        playerSect.addAlly(requestingSectName);
        requestingSect.addAlly(playerSect.getName());
        manager.removeAllyRequest(requestingSectName, playerSect.getName());

        // FIX: Notify both sects with proper messages
        String allianceMessage1 = "§aAlliance formed with " + requestingSectName + "!";
        String allianceMessage2 = "§aAlliance formed with " + playerSect.getName() + "!";

        broadcastToSect(playerSect, context.getSource().getServer(), allianceMessage1);
        broadcastToSect(requestingSect, context.getSource().getServer(), allianceMessage2);

        // FIX: Also send success message to the player who accepted
        context.getSource().sendSuccess(() -> Component.literal("§aAlliance formed with " + requestingSectName + "!"), true);
        return 1;
    }

    private static int declineAlly(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String requestingSectName = StringArgumentType.getString(context, "sectname").replace('_', ' ');

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
            context.getSource().sendFailure(Component.literal("§cYou don't have permission to decline ally requests!"));
            return 0;
        }

        Sect requestingSect = manager.getSect(requestingSectName);
        if (requestingSect == null) {
            context.getSource().sendFailure(Component.literal("§cSect '" + requestingSectName + "' does not exist!"));
            return 0;
        }

        if (!manager.hasAllyRequest(requestingSectName, playerSect.getName())) {
            context.getSource().sendFailure(Component.literal("§cNo pending ally request from §e" + requestingSectName + "§c!"));
            return 0;
        }

        manager.removeAllyRequest(requestingSectName, playerSect.getName());
        context.getSource().sendSuccess(() -> Component.literal("§eYou have declined the alliance request from §b" + requestingSectName + "§e."), true);

        // Notify the requesting sect
        broadcastToSect(requestingSect, context.getSource().getServer(),
                "§e" + playerSect.getName() + " has declined your alliance request.");

        return 1;
    }

    private static int listAllyRequests(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
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

        if (!sect.hasPermission(player.getUUID(), SectPermission.ALLY)) {
            context.getSource().sendFailure(Component.literal("§cOnly Elders and Sect Masters can view ally requests!"));
            return 0;
        }

        List<String> pendingRequests = manager.getPendingAllyRequests(sect.getName());

        if (pendingRequests.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("§7No pending ally requests."), false);
        } else {
            context.getSource().sendSuccess(() -> Component.literal("§6=== Pending Ally Requests ==="), false);
            for (String requestingSect : pendingRequests) {
                MutableComponent requestLine = Component.literal("§e- " + requestingSect)
                        .append(Component.literal(" §7[")
                                .append(createClickableAllyComponent("§aAccept", "accept", requestingSect))
                                .append(Component.literal("§7|"))
                                .append(createClickableAllyComponent("§cDecline", "decline", requestingSect))
                                .append(Component.literal("§7]")));

                context.getSource().sendSuccess(() -> requestLine, false);
            }
        }

        return 1;
    }


    private static MutableComponent createClickableAllyComponent(String text, String action, String sectName) {
        return Component.literal(text)
                .withStyle(Style.EMPTY
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                "/sect ally " + action + " " + sectName.replace(" ", "_")))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.literal(action.equals("accept") ?
                                        "§aAccept alliance with §e" + sectName :
                                        "§cDecline alliance with §e" + sectName))));
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

        // FIX: Notify all allied sects before disbanding and remove from their ally lists
        for (String allyName : sect.getAllies()) {
            Sect allySect = manager.getSect(allyName);
            if (allySect != null) {
                allySect.removeAlly(sectName);
                // Notify the allied sect
                broadcastToSect(allySect, context.getSource().getServer(),
                        "§cYour alliance with §e" + sectName + "§c has been dissolved because they disbanded.");
            }
        }

        // FIX: Clean up any pending ally requests involving this sect
        // Remove requests this sect sent to others
        for (String targetSectName : new ArrayList<>(manager.allyRequests.keySet())) {
            Set<String> requests = manager.allyRequests.get(targetSectName);
            if (requests != null) {
                requests.remove(sectName);
                if (requests.isEmpty()) {
                    manager.allyRequests.remove(targetSectName);
                }
            }
        }

        // Remove requests others sent to this sect
        manager.allyRequests.remove(sectName);

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

    private static int claimSingleChunk(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return claimChunks(context, 0); // Radius 0 = only current chunk
    }

    private static int claimChunksWithRadius(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        int radius = IntegerArgumentType.getInteger(context, "radius");
        return claimChunks(context, radius);
    }

    private static int claimChunks(CommandContext<CommandSourceStack> context, int radius) throws CommandSyntaxException {
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

        // Check if player is Elder or above
        SectMember member = sect.getMember(player.getUUID());
        if (member == null || (member.getRank() != SectRank.ELDER && member.getRank() != SectRank.SECT_MASTER)) {
            context.getSource().sendFailure(Component.literal("§cOnly Elders and Sect Masters can claim chunks!"));
            return 0;
        }

        // Calculate chunk coordinates
        int centerChunkX = player.chunkPosition().x;
        int centerChunkZ = player.chunkPosition().z;

        // Track different types of claims
        int normalClaims = 0;
        int enemyOverclaims = 0;
        Set<Long> chunksToClaim = new HashSet<>();
        int totalCost = 0;

        for (int x = centerChunkX - radius; x <= centerChunkX + radius; x++) {
            for (int z = centerChunkZ - radius; z <= centerChunkZ + radius; z++) {
                long chunkPos = ChunkPos.asLong(x, z);

                if (sect.isChunkClaimed(chunkPos)) {
                    continue; // Already claimed by our sect
                }

                // Check if claimed by another sect
                boolean canClaim = true;
                boolean isOverclaim = false;
                String claimingSectName = null;

                // Check all sects to see who owns this chunk
                for (Sect otherSect : manager.getAllSects().values()) {
                    if (otherSect.isChunkClaimed(chunkPos)) {
                        if (sect.isEnemy(otherSect.getName())) {
                            // Check max power for overclaim
                            if (sect.getMaxPower() > otherSect.getMaxPower()) {
                                // Can overclaim enemy with lower max power
                                isOverclaim = true;
                                enemyOverclaims++;
                                claimingSectName = otherSect.getName();
                            } else {
                                canClaim = false;
                                context.getSource().sendFailure(Component.literal(
                                        "§cCannot claim chunk at [" + x + ", " + z + "] - enemy sect §e" +
                                                otherSect.getName() + "§c has equal or higher max power! " +
                                                "(§e" + sect.getMaxPower() + "§c vs §e" + otherSect.getMaxPower() + "§c)"
                                ));
                            }
                        } else {
                            canClaim = false;
                            context.getSource().sendFailure(Component.literal(
                                    "§cChunk at [" + x + ", " + z + "] is already claimed by §e" +
                                            otherSect.getName() + "§c and they are not your enemy!"
                            ));
                        }
                        break;
                    }
                }

                if (canClaim) {
                    chunksToClaim.add(chunkPos);
                    if (isOverclaim) {
                        totalCost += 100; // Overclaim cost
                    } else {
                        normalClaims++;
                        totalCost += 50; // Normal claim cost
                    }
                }
            }
        }

        int successfullyClaimed = normalClaims + enemyOverclaims;

        if (successfullyClaimed == 0) {
            context.getSource().sendFailure(Component.literal("§cNo chunks could be claimed!"));
            return 0;
        }

        // Check if we have enough power for the total cost
        if (!sect.usePower(totalCost)) {
            context.getSource().sendFailure(Component.literal(
                    "§cNot enough power! Need §e" + totalCost +
                            "§c power but only have §e" + sect.getCurrentPower() + "§c current power."
            ));
            return 0;
        }

        // Claim chunks
        for (long chunkPos : chunksToClaim) {
            sect.claimChunk(chunkPos, player.getUUID());
        }

        manager.setDirty();

        // Success message with cost breakdown
        MutableComponent successMessage = Component.literal("§aSuccessfully claimed §e" + successfullyClaimed + "§a chunks!");

        if (normalClaims > 0) {
            successMessage.append(Component.literal(" §7(§e" + normalClaims + "§7 normal @ §e50§7 power each)"));
        }

        if (enemyOverclaims > 0) {
            successMessage.append(Component.literal(" §6(§e" + enemyOverclaims + "§6 overclaims @ §e100§6 power each)"));
        }

        successMessage.append(Component.literal(" §7Total: §e" + totalCost + "§7 power"));

        context.getSource().sendSuccess(() -> successMessage, true);

        // Notify sect with detailed breakdown
        String broadcastMessage = "§e" + player.getScoreboardName() + " claimed " + successfullyClaimed + " chunks!";
        if (normalClaims > 0) {
            broadcastMessage += " §7(" + normalClaims + " normal)";
        }
        if (enemyOverclaims > 0) {
            broadcastMessage += " §6(" + enemyOverclaims + " overclaims)";
        }
        broadcastMessage += " §7(" + totalCost + " power)";

        broadcastToSect(sect, context.getSource().getServer(), broadcastMessage);

        return successfullyClaimed;
    }

    private static int depositMerit(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("This command can only be used by players"));
            return 0;
        }

        SectManager manager = AscensionCraft.getSectManager(player.getServer());
        if (manager == null) {
            source.sendFailure(Component.literal("Sect system not available"));
            return 0;
        }

        Sect playerSect = manager.getPlayerSect(player.getUUID());
        if (playerSect == null) {
            source.sendFailure(Component.literal("You are not in a sect"));
            return 0;
        }

        // Open the deposit GUI
        player.openMenu(new SimpleMenuProvider(
                (windowId, inv, p) -> new SectDepositMenu(windowId, inv),
                Component.translatable("container.sect_deposit")
        ));

        source.sendSuccess(() -> Component.translatable("sect.deposit.opening"), true);
        return 1;
    }

    private static int setEnemy(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String enemySectName = StringArgumentType.getString(context, "sectname");

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

        // Check if player is Elder or above
        SectMember member = sect.getMember(player.getUUID());
        if (member == null || (member.getRank() != SectRank.ELDER && member.getRank() != SectRank.SECT_MASTER)) {
            context.getSource().sendFailure(Component.literal("§cOnly Elders and Sect Masters can set enemies!"));
            return 0;
        }

        // Check if enemy sect exists
        Sect enemySect = manager.getSect(enemySectName);
        if (enemySect == null) {
            context.getSource().sendFailure(Component.literal("§cSect '" + enemySectName + "' does not exist!"));
            return 0;
        }

        // Check if trying to set own sect as enemy
        if (sect.getName().equals(enemySectName)) {
            context.getSource().sendFailure(Component.literal("§cYou cannot set your own sect as an enemy!"));
            return 0;
        }

        // Check if already enemies
        if (sect.isEnemy(enemySectName)) {
            context.getSource().sendFailure(Component.literal("§c" + enemySectName + " is already your enemy!"));
            return 0;
        }

        sect.addEnemy(enemySectName);
        manager.setDirty();

        context.getSource().sendSuccess(() -> Component.literal(
                "§cMarked §e" + enemySectName + "§c as an enemy! " +
                        "You can now overclaim their territory if you have more power."
        ), true);

        // Notify sect
        broadcastToSect(sect, context.getSource().getServer(),
                "§c" + enemySectName + " has been marked as an enemy by " + player.getScoreboardName() + "!");

        return 1;
    }

    private static int unclaimChunk(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return unclaimChunks(context, 0);
    }

    private static int unclaimChunks(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        int radius = 0;
        try {
            radius = IntegerArgumentType.getInteger(context, "radius");
        } catch (IllegalArgumentException e) {
            // Use default radius of 0 (single chunk)
        }
        return unclaimChunks(context, radius);
    }

    private static int unclaimChunks(CommandContext<CommandSourceStack> context, int radius) throws CommandSyntaxException {
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

        // Check if player is Elder or above
        SectMember member = sect.getMember(player.getUUID());
        if (member == null || (member.getRank() != SectRank.ELDER && member.getRank() != SectRank.SECT_MASTER)) {
            context.getSource().sendFailure(Component.literal("§cOnly Elders and Sect Masters can unclaim chunks!"));
            return 0;
        }

        int centerChunkX = player.chunkPosition().x;
        int centerChunkZ = player.chunkPosition().z;
        int unclaimedCount = 0;

        for (int x = centerChunkX - radius; x <= centerChunkX + radius; x++) {
            for (int z = centerChunkZ - radius; z <= centerChunkZ + radius; z++) {
                long chunkPos = ChunkPos.asLong(x, z);

                if (sect.isChunkClaimed(chunkPos)) {
                    sect.unclaimChunk(chunkPos);
                    unclaimedCount++;
                }
            }
        }

        if (unclaimedCount > 0) {
            manager.setDirty();

            // Refund power (1 power per chunk)
            sect.refundPower(unclaimedCount);

            int finalUnclaimedCount = unclaimedCount;
            context.getSource().sendSuccess(() -> Component.literal(
                    "§aUnclaimed §e" + finalUnclaimedCount + "§a chunks. Refunded §e" +
                            finalUnclaimedCount + "§a power."
            ), true);
        }
        return unclaimedCount;
    }

    private static int showClaimMap(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();

        SectManager manager = getManager(context);
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

        Sect playerSect = manager.getPlayerSect(player.getUUID());
        int centerChunkX = player.chunkPosition().x;
        int centerChunkZ = player.chunkPosition().z;

        context.getSource().sendSuccess(() -> Component.literal("§6=== Territory Map (7x7 chunks) ==="), false);
        context.getSource().sendSuccess(() -> Component.literal("§7Green: Your sect §a█ §7Red: Enemy §c█ §7White: Unclaimed §f█"), false);
        context.getSource().sendSuccess(() -> Component.literal("§7Yellow: Ally §e█ §7Gray: Other sect §8█ §b█: Your position"), false);
        context.getSource().sendSuccess(() -> Component.literal(""), false);

        for (int z = centerChunkZ - 3; z <= centerChunkZ + 3; z++) {
            StringBuilder row = new StringBuilder();
            for (int x = centerChunkX - 3; x <= centerChunkX + 3; x++) {
                long chunkPos = ChunkPos.asLong(x, z);
                String symbol = "§f█"; // Default: unclaimed

                if (x == centerChunkX && z == centerChunkZ) {
                    symbol = "§b█"; // Player position
                } else {
                    // Check all sects for this chunk
                    boolean claimed = false;
                    for (Sect sect : manager.getAllSects().values()) {
                        if (sect.isChunkClaimed(chunkPos)) {
                            claimed = true;
                            if (sect == playerSect) {
                                symbol = "§a█"; // Own sect
                            } else if (playerSect != null && playerSect.isEnemy(sect.getName())) {
                                symbol = "§c█"; // Enemy
                            } else if (playerSect != null && playerSect.getAllies().contains(sect.getName())) {
                                symbol = "§e█"; // Ally
                            } else {
                                symbol = "§8█"; // Other sect
                            }
                            break;
                        }
                    }
                }
                row.append(symbol);
            }
            final String finalRow = row.toString();
            context.getSource().sendSuccess(() -> Component.literal(finalRow), false);
        }

        if (playerSect != null) {
            context.getSource().sendSuccess(() -> Component.literal(""), false);
            context.getSource().sendSuccess(() -> Component.literal(
                    "§eYour Sect: §a" + playerSect.getClaimedChunkCount() +
                            "§e chunks claimed | §6Power: " + playerSect.getCurrentPower() + "/" + playerSect.getMaxPower() +
                            "§e | §aTotal Deposited: " + playerSect.getTotalDeposited()
            ), false);
        }

        return 1;
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

        Sect finalSect2 = sect;
        context.getSource().sendSuccess(() -> Component.literal(
                "§ePower: §6" + finalSect2.getCurrentPower() + "§e/§6" + finalSect2.getMaxPower() +
                        "§e (Total Deposited: §6" + finalSect2.getTotalDeposited() + "§e)"
        ), false);
        // Claimed Chunks
        Sect finalSect3 = sect;
        context.getSource().sendSuccess(() -> Component.literal("§eClaimed Chunks: §a" + finalSect3.getClaimedChunkCount()), false);

        // Enemies
        StringBuilder enemiesBuilder = new StringBuilder("§eEnemies: §c");
        if (sect.getEnemies().isEmpty()) {
            enemiesBuilder.append("None");
        } else {
            enemiesBuilder.append(String.join(", §c", sect.getEnemies()));
        }
        context.getSource().sendSuccess(() -> Component.literal(enemiesBuilder.toString()), false);

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
            availableMissions = new ArrayList<>(sect.getMissions());
        }

        // Remove expired missions (for Elders+)
        if (member.getRank().getLevel() >= SectRank.ELDER.getLevel()) {
            List<UUID> expiredMissions = new ArrayList<>();
            for (SectMission mission : availableMissions) {
                if (mission.isExpired()) {
                    expiredMissions.add(mission.getMissionId());
                }
            }
            for (UUID missionId : expiredMissions) {
                sect.removeMission(missionId);
            }
            if (!expiredMissions.isEmpty()) {
                manager.setDirty();
                context.getSource().sendSuccess(() -> Component.literal("§eRemoved " + expiredMissions.size() + " expired missions."), false);
            }
            // Refresh the list
            if (member.getRank() == SectRank.OUTER) {
                availableMissions = sect.getMissionsForRank(SectRank.OUTER);
            } else if (member.getRank() == SectRank.INNER) {
                availableMissions = sect.getMissionsForRank(SectRank.INNER);
            } else {
                availableMissions = new ArrayList<>(sect.getMissions());
            }
        }

        context.getSource().sendSuccess(() -> Component.literal("§6=== Available Missions ==="), false);

        List<SectMission> validMissions = new ArrayList<>();

        for (SectMission mission : availableMissions) {
            if (mission.isExpired()) continue;

            boolean isAccepted = mission.isAcceptedBy(player.getUUID());
            MissionProgress progress = mission.getProgress(player.getUUID());

            // If player hasn't accepted the mission, show it as available
            if (!isAccepted) {
                validMissions.add(mission);
                continue;
            }

            // If player has accepted it, check the progress
            if (progress != null) {
                // Show the mission if it's not completed OR if it can be completed (ready to claim)
                if (!progress.isCompleted() || progress.canComplete()) {
                    validMissions.add(mission);
                }
            }
        }

        if (validMissions.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("§7No missions available."), false);
        } else {
            for (SectMission mission : validMissions) {
                boolean isAccepted = mission.isAcceptedBy(player.getUUID());
                MissionProgress progress = mission.getProgress(player.getUUID());
                boolean canComplete = progress != null && progress.canComplete();
                boolean isCompleted = progress != null && progress.isCompleted();

                String status;
                if (canComplete && isAccepted) {
                    status = "§a[CLAIM REWARD]";
                } else if (isAccepted) {
                    status = "§e[IN PROGRESS]";
                } else {
                    status = "§7[AVAILABLE]";
                }

                // Create the mission component
                MutableComponent missionComponent = Component.literal(status + " " + mission.getDisplayName());

                // Add hover event
                missionComponent.withStyle(style -> style
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, createMissionHoverText(mission, progress))));

                // Add click event based on mission state
                if (canComplete && isAccepted) {
                    String completeCommand = "/sect mission complete " + mission.getMissionId().toString();
                    missionComponent.withStyle(style -> style
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, completeCommand)));
                } else if (!isAccepted) {
                    String acceptCommand = "/sect mission accept " + mission.getMissionId().toString();
                    missionComponent.withStyle(style -> style
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, acceptCommand)));
                }

                context.getSource().sendSuccess(() -> missionComponent, false);
            }
        }

        // Show merit points (existing code)
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
        MutableComponent text = Component.literal("§e" + mission.getDisplayName() + "\n\n");

        // Show time remaining
        text.append(Component.literal("§7Duration: " + mission.getTimeRemaining() + "\n\n"));

        // Show item reward if present
        if (!mission.getRewardItem().isEmpty()) {
            String itemName = mission.getRewardItem().getHoverName().getString();
            text.append(Component.literal("§aItem Reward: §f" + itemName + "\n\n"));
        }

        text.append(Component.literal("§7Requirements:\n"));

        for (int i = 0; i < mission.getRequirements().size(); i++) {
            MissionRequirement req = mission.getRequirements().get(i);
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
                // Get progress for this specific requirement
                Map<Integer, Integer> currentProgress = progress.getProgress();
                int currentAmount = currentProgress.getOrDefault(i, 0);
                requirementText += " §8(" + currentAmount + "/" + req.getCount() + ")";
            }

            text.append(Component.literal("§7- " + requirementText + "\n"));
        }

        text.append(Component.literal("\n§6Reward: §e" + mission.getRewardMerit() + " Merit Points"));

        // Add item reward to the tooltip
        if (!mission.getRewardItem().isEmpty()) {
            String itemName = mission.getRewardItem().getHoverName().getString();
            text.append(Component.literal(" §a+ " + itemName));
        }

        return text;
    }

    private static long parseDuration(String durationStr) {
        if (durationStr == null || durationStr.equalsIgnoreCase("none")) {
            return 0; // No expiration
        }

        try {
            char unit = durationStr.charAt(durationStr.length() - 1);
            String numberStr = durationStr.substring(0, durationStr.length() - 1);
            int value = Integer.parseInt(numberStr);

            switch (Character.toLowerCase(unit)) {
                case 'm': // minutes
                    return System.currentTimeMillis() + (value * 60 * 1000L);
                case 'h': // hours
                    return System.currentTimeMillis() + (value * 60 * 60 * 1000L);
                case 'd': // days
                    return System.currentTimeMillis() + (value * 24 * 60 * 60 * 1000L);
                default:
                    return 0; // Invalid unit, no expiration
            }
        } catch (Exception e) {
            return 0; // Invalid format, no expiration
        }
    }

    private static int createMissionAdvanced(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String displayName = StringArgumentType.getString(context, "displayName");
        String rankStr = StringArgumentType.getString(context, "targetRank");
        int meritReward = IntegerArgumentType.getInteger(context, "meritReward");
        String durationStr = StringArgumentType.getString(context, "duration");
        boolean useHandItem = BoolArgumentType.getBool(context, "useHandItem"); // Moved before requirements
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

        // Parse target rank
        SectRank targetRank;
        try {
            targetRank = SectRank.valueOf(rankStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            context.getSource().sendFailure(Component.literal("§cInvalid rank! Use: OUTER, INNER, ELDER, or SECT_MASTER"));
            return 0;
        }

        // Check if executor has permission to create missions for this rank
        SectMember executor = sect.getMember(player.getUUID());
        if (executor.getRank().getLevel() < targetRank.getLevel()) {
            context.getSource().sendFailure(Component.literal("§cYou cannot create missions for a rank higher than your own!"));
            return 0;
        }

        // Parse duration
        long expirationTime = parseDuration(durationStr);
        if (expirationTime == 0 && !durationStr.equalsIgnoreCase("none")) {
            context.getSource().sendFailure(Component.literal("§cInvalid duration format! Use: 60m, 6h, 6d, or none"));
            context.getSource().sendFailure(Component.literal("§cExamples: 30m (30 minutes), 2h (2 hours), 7d (7 days), none (no expiration)"));
            return 0;
        }

        // Check if using hand item and validate
        ItemStack handItem = ItemStack.EMPTY;
        if (useHandItem) {
            handItem = player.getMainHandItem();
            if (handItem.isEmpty()) {
                context.getSource().sendFailure(Component.literal("§cYou must hold an item in your main hand to use as a reward!"));
                return 0;
            }

            // Create a copy to avoid modifying the original stack
            handItem = handItem.copy();

            // Optional: Limit to one item to prevent duplication exploits
            handItem.setCount(1);
        }

        try {
            // Parse requirements from the string
            List<MissionRequirement> requirements = parseRequirements(requirementsStr);

            if (requirements.isEmpty()) {
                context.getSource().sendFailure(Component.literal("§cNo valid requirements found!"));
                context.getSource().sendFailure(Component.literal("§cFormat: TYPE:namespace:id:count"));
                context.getSource().sendFailure(Component.literal("§cExample: ITEM:minecraft:diamond:10"));
                context.getSource().sendFailure(Component.literal("§cExample: KILL_MOB:minecraft:zombie:5"));
                context.getSource().sendFailure(Component.literal("§cYour input: " + requirementsStr));
                return 0;
            }

            // Create the mission
            SectMission mission = new SectMission(
                    displayName,
                    targetRank,
                    requirements,
                    meritReward,
                    player.getUUID()
            );

            // Set expiration time
            mission.setExpirationTime(expirationTime);

            // Set reward item if using hand item
            if (useHandItem) {
                mission.setRewardItem(handItem);

                // Remove the item from player's hand (optional - you can choose to keep it or remove it)
                player.getMainHandItem().shrink(1);
                // Uncomment the line above if you want to consume the item from the creator's hand
            }

            // Add the mission to the sect
            sect.addMission(mission);
            manager.setDirty();

            context.getSource().sendSuccess(() -> Component.literal("§aMission '" + displayName + "' created!"), false);

            // Show mission details
            context.getSource().sendSuccess(() -> Component.literal("§7Target Rank: §e" + mission.getTargetRank().getDisplayName()), false);
            context.getSource().sendSuccess(() -> Component.literal("§7Duration: " + mission.getTimeRemaining()), false);

            // Show item reward if present
            if (useHandItem && !handItem.isEmpty()) {
                String itemName = handItem.getHoverName().getString();
                context.getSource().sendSuccess(() -> Component.literal("§7Item Reward: §e" + itemName), false);
            }

            context.getSource().sendSuccess(() -> Component.literal("§7Requirements:"), false);
            for (MissionRequirement req : requirements) {
                String reqText = switch (req.getType()) {
                    case ITEM -> "Collect " + req.getCount() + "x " + req.getTarget();
                    case KILL_MOB -> "Kill " + req.getCount() + "x " + req.getTarget();
                };
                context.getSource().sendSuccess(() -> Component.literal("§7- " + reqText), false);
            }
            context.getSource().sendSuccess(() -> Component.literal("§6Reward: §e" + mission.getRewardMerit() + " Merit Points" +
                    (useHandItem ? " §a+ Item Reward" : "")), false);
            context.getSource().sendSuccess(() -> Component.literal("§7Submitted items will be stored for sect leaders to collect"), false);

            return 1;

        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("§cError creating mission: " + e.getMessage()));
            return 0;
        }
    }

    private static List<MissionRequirement> parseRequirements(String requirementsStr) {
        List<MissionRequirement> requirements = new ArrayList<>();

        if (requirementsStr == null || requirementsStr.trim().isEmpty()) {
            return requirements;
        }

        // Remove the word "repeatable" from the end if it exists
        String cleanedStr = requirementsStr.replaceAll("\\s+repeatable\\s*$", "").trim();

        if (cleanedStr.isEmpty()) {
            return requirements;
        }

        try {
            // Split multiple requirements by spaces
            String[] requirementParts = cleanedStr.split("\\s+");

            for (String requirementPart : requirementParts) {
                // Format: TYPE:namespace:id:count
                String[] segments = requirementPart.split(":");
                if (segments.length < 4) {
                    continue; // Skip invalid format
                }

                String typeStr = segments[0].toUpperCase();

                // Handle the case where entity IDs might contain colons (like minecraft:ender_dragon)
                // The last segment should be the count, the first is type, the rest form the target
                String countStr = segments[segments.length - 1];

                // Reconstruct the target from segments[1] to segments[length-2]
                StringBuilder targetBuilder = new StringBuilder(segments[1]);
                for (int i = 2; i < segments.length - 1; i++) {
                    targetBuilder.append(":").append(segments[i]);
                }
                String target = targetBuilder.toString();

                int count;
                try {
                    count = Integer.parseInt(countStr);
                    if (count <= 0) {
                        continue; // Skip invalid count
                    }
                } catch (NumberFormatException e) {
                    continue; // Skip if count is not a number
                }

                MissionRequirement.RequirementType type;
                try {
                    type = MissionRequirement.RequirementType.valueOf(typeStr);
                } catch (IllegalArgumentException e) {
                    // Invalid type
                    continue;
                }

                requirements.add(new MissionRequirement(type, target, count));
            }

        } catch (Exception e) {
            // If any error occurs, return empty requirements
            return new ArrayList<>();
        }

        return requirements;
    }

    private static int acceptMission(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String missionIdStr = StringArgumentType.getString(context, "missionId");

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

        try {
            // Parse the UUID from string
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
                context.getSource().sendFailure(Component.literal("§cYou have already accepted this mission or it's expired!"));
                return 0;
            }

            mission.acceptMission(player.getUUID());
            manager.setDirty();

            context.getSource().sendSuccess(() -> Component.literal("§aMission '" + mission.getDisplayName() + "' accepted!"), false);
            return 1;

        } catch (IllegalArgumentException e) {
            context.getSource().sendFailure(Component.literal("§cInvalid mission ID format!"));
            return 0;
        }
    }

    private static int completeMission(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String missionIdStr = StringArgumentType.getString(context, "missionId");

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

        try {
            // Parse the UUID from string
            UUID missionId = UUID.fromString(missionIdStr);

            if (SectMissionEventHandler.claimMissionReward(player, missionId)) {
                context.getSource().sendSuccess(() -> Component.literal("§aMission completed and rewards claimed!"), false);
                return 1;
            } else {
                context.getSource().sendFailure(Component.literal("§cCannot complete mission! Make sure you have the required items and the mission is ready for completion."));
                return 0;
            }

        } catch (IllegalArgumentException e) {
            context.getSource().sendFailure(Component.literal("§cInvalid mission ID format!"));
            return 0;
        }
    }

    private static int recommendPlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
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

        // Check if recommender is Elder or Sect Master
        SectMember recommender = sect.getMember(player.getUUID());
        if (recommender == null || (recommender.getRank() != SectRank.ELDER && recommender.getRank() != SectRank.SECT_MASTER)) {
            context.getSource().sendFailure(Component.literal("§cOnly Elders and Sect Masters can recommend players for Elder promotion!"));
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

        // Check if target is Inner member
        if (targetMember.getRank() != SectRank.INNER) {
            context.getSource().sendFailure(Component.literal("§cYou can only recommend Inner members for Elder promotion!"));
            return 0;
        }

        // Check if already recommended this player
        Set<UUID> existingRecommendations = sect.getRecommendations(targetPlayer.getUUID());
        if (existingRecommendations.contains(player.getUUID())) {
            context.getSource().sendFailure(Component.literal("§cYou have already recommended " + targetName + " for Elder promotion!"));
            return 0;
        }

        // Add recommendation
        sect.addRecommendation(targetPlayer.getUUID(), player.getUUID());
        manager.setDirty();

        int totalRecommendations = sect.getRecommendationCount(targetPlayer.getUUID());
        int neededRecommendations = 3;

        context.getSource().sendSuccess(() -> Component.literal("§aYou have recommended " + targetName + " for Elder promotion! (§e" + totalRecommendations + "§a/§6" + neededRecommendations + "§a recommendations)"), true);

        // Notify the recommended player
        targetPlayer.sendSystemMessage(Component.literal("§6" + player.getScoreboardName() + " has recommended you for Elder promotion! (§e" + totalRecommendations + "§6/§e" + neededRecommendations + "§6 recommendations)"));

        // Check if player now qualifies for Elder promotion
        if (totalRecommendations >= neededRecommendations && sect.getPlayerMerit(targetPlayer.getUUID()) >= 10000) {
            sect.setMemberRank(targetPlayer.getUUID(), SectRank.ELDER);
            sect.clearRecommendations(targetPlayer.getUUID());
            broadcastToSect(sect, context.getSource().getServer(),
                    "§6" + targetName + " has been promoted to Elder for reaching 10000 merit points and receiving " + totalRecommendations + " recommendations!");
        }

        return 1;
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

            // NEW: Show recommendation status for Inner members
            int recommendationCount = sect.getRecommendationCount(player.getUUID());
            int neededRecommendations = 3;

            context.getSource().sendSuccess(() -> Component.literal("§7Next Rank: §6Elder §7(§610000§7 merit + §6" + neededRecommendations + "§7 recommendations)"), false);
            context.getSource().sendSuccess(() -> Component.literal("§7Elder Recommendations: §e" + recommendationCount + "§7/§6" + neededRecommendations), false);

            // Show who recommended you
            Set<UUID> recommenderIds = sect.getRecommendations(player.getUUID());
            if (!recommenderIds.isEmpty()) {
                List<String> recommenderNames = new ArrayList<>();
                for (UUID recommenderId : recommenderIds) {
                    SectMember recommender = sect.getMember(recommenderId);
                    if (recommender != null) {
                        recommenderNames.add(recommender.getPlayerName());
                    }
                }
                context.getSource().sendSuccess(() -> Component.literal("§7Recommended by: §e" + String.join("§7, §e", recommenderNames)), false);
            } else {
                context.getSource().sendSuccess(() -> Component.literal("§7Recommended by: §cNone yet"), false);
            }

            if (recommendationCount < neededRecommendations) {
                context.getSource().sendSuccess(() -> Component.literal("§7Ask Elders or Sect Master to use §e/sect recommend " + player.getScoreboardName()), false);
            }
        } else if (member.getRank() == SectRank.ELDER) {
            context.getSource().sendSuccess(() -> Component.literal("§6You are an Elder! Next rank: §cSect Master"), false);
            context.getSource().sendSuccess(() -> Component.literal("§7Promotion to Sect Master must be done manually by the current Sect Master."), false);
        } else if (member.getRank() == SectRank.SECT_MASTER) {
            context.getSource().sendSuccess(() -> Component.literal("§cYou are the Sect Master! This is the highest rank."), false);
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

        // NEW: Track specific items for detailed reporting
        Map<String, Integer> itemCounts = new HashMap<>();
        Map<String, String> itemDisplayNames = new HashMap<>();

        // FIX: Create a copy of the mission IDs to avoid ConcurrentModificationException
        List<UUID> missionIdsToRemove = new ArrayList<>();

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

                    // NEW: Track specific item types and counts
                    String itemId = stack.getItem().toString();
                    String displayName = stack.getHoverName().getString(); // Get the item's display name

                    itemCounts.put(itemId, itemCounts.getOrDefault(itemId, 0) + stack.getCount());
                    itemDisplayNames.put(itemId, displayName);

                    // Try to add to player inventory
                    if (!player.getInventory().add(stack.copy())) {
                        // Drop if inventory is full
                        player.drop(stack.copy(), false);
                    }
                }
            }

            // Mark this mission for removal instead of removing it during iteration
            missionIdsToRemove.add(missionId);
        }

        // Now remove all the marked mission submissions after iteration is complete
        for (UUID missionId : missionIdsToRemove) {
            sect.removeMissionSubmission(missionId);
        }

        manager.setDirty();

        int finalTotalItems = totalItems;
        int finalTotalStacks = totalStacks;
        context.getSource().sendSuccess(() -> Component.literal("§aClaimed §e" + finalTotalItems + "§a items (§e" + finalTotalStacks + "§a stacks) from mission submissions!"), false);

        // NEW: Show detailed breakdown of items received
        if (!itemCounts.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("§6=== Items Received ==="), false);

            for (Map.Entry<String, Integer> itemEntry : itemCounts.entrySet()) {
                String itemId = itemEntry.getKey();
                int count = itemEntry.getValue();
                String displayName = itemDisplayNames.getOrDefault(itemId, itemId);

                context.getSource().sendSuccess(() -> Component.literal("§7- §e" + count + "x §f" + displayName), false);
            }

            context.getSource().sendSuccess(() -> Component.literal("§6====================="), false);
        }

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

    private static int renameSect(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String newName = StringArgumentType.getString(context, "new_name").replace('_', ' ');

        SectManager manager = getManager(context);
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

        Sect oldSect = manager.getPlayerSect(player.getUUID());
        if (oldSect == null) {
            context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
            return 0;
        }

        // Check if player is the Sect Master
        if (!oldSect.getOwnerId().equals(player.getUUID())) {
            context.getSource().sendFailure(Component.literal("§cOnly the Sect Master can rename the sect!"));
            return 0;
        }

        // Validate new name
        if (newName.length() > 40) {
            context.getSource().sendFailure(Component.literal("§cSect name too long! Maximum 40 characters."));
            return 0;
        }

        if (!newName.matches("[a-zA-Z0-9 _]+")) {
            context.getSource().sendFailure(Component.literal("§cSect name can only contain letters, numbers, and spaces."));
            return 0;
        }

        // Check if name already exists
        if (manager.getSect(newName) != null) {
            context.getSource().sendFailure(Component.literal("§cA sect with that name already exists!"));
            return 0;
        }

        String oldName = oldSect.getName();

        // FIX: Get allies before creating new sect
        Set<String> allies = new HashSet<>(oldSect.getAllies());

        // Create a new sect with the same data but new name
        Sect newSect = new Sect(newName, oldSect.getOwnerId(), "Transfer");

        // Copy all data from old sect to new sect
        copySectData(oldSect, newSect);

        // FIX: Update ally relationships - notify allied sects and update their ally lists
        for (String allyName : allies) {
            Sect allySect = manager.getSect(allyName);
            if (allySect != null) {
                // Remove old name and add new name to ally's list
                allySect.removeAlly(oldName);
                allySect.addAlly(newName);

                // Notify the allied sect
                broadcastToSect(allySect, context.getSource().getServer(),
                        "§eYour ally §b" + oldName + "§e has been renamed to §b" + newName + "§e!");
            }
        }

        // FIX: Update ally requests
        // Update requests where this sect is the target
        Set<String> oldRequests = manager.allyRequests.remove(oldName);
        if (oldRequests != null) {
            manager.allyRequests.put(newName, oldRequests);
        }

        // Update requests where this sect is the requester
        for (Map.Entry<String, Set<String>> entry : manager.allyRequests.entrySet()) {
            Set<String> requests = entry.getValue();
            if (requests.remove(oldName)) {
                requests.add(newName);
            }
        }

        // Replace the old sect with the new one
        manager.removeSect(oldName);
        manager.sects.put(newName, newSect);

        // Update playerSects for all members
        for (SectMember member : newSect.getMembers().values()) {
            manager.playerSects.put(member.getPlayerId(), newName);
        }

        manager.setDirty();

        // Notify all sect members
        broadcastToSect(newSect, context.getSource().getServer(),
                "§eThe sect has been renamed to §b" + newName + "§e by the Sect Master!");

        context.getSource().sendSuccess(() -> Component.literal("§aSect renamed to '" + newName + "'!"), true);

        return 1;
    }

    // Helper method to copy sect data
    private static void copySectData(Sect source, Sect destination) {
        // Copy basic properties
        destination.setOpen(source.isOpen());
        destination.setDescription(source.getDescription());
        destination.setFriendlyFire(source.isFriendlyFire());

        destination.setMaxPower(source.getMaxPower());
        destination.setCurrentPower(source.getCurrentPower());
        destination.setTotalDeposited(source.getTotalDeposited());

        // Copy members
        for (SectMember member : source.getMembers().values()) {
            destination.addMember(member.getPlayerId(), member.getPlayerName(), member.getRank());
            if (!member.getTitle().isEmpty()) {
                destination.setMemberTitle(member.getPlayerId(), member.getTitle());
            }
        }

        // Copy allies
        for (String ally : source.getAllies()) {
            destination.addAlly(ally);
        }

        // Copy enemies
        for (String enemy : source.getEnemies()) {
            destination.addEnemy(enemy);
        }

        // Copy claimed chunks
        for (Map.Entry<Long, UUID> entry : source.getClaimedChunks().entrySet()) {
            destination.claimChunk(entry.getKey(), entry.getValue());
        }
    }

    private static void copySectPowerData(Sect source, Sect destination) {
        try {
            // Use reflection as a temporary solution, but you should add proper setters to Sect class
            java.lang.reflect.Field maxPowerField = Sect.class.getDeclaredField("maxPower");
            java.lang.reflect.Field currentPowerField = Sect.class.getDeclaredField("currentPower");
            java.lang.reflect.Field totalDepositedField = Sect.class.getDeclaredField("totalDeposited");

            maxPowerField.setAccessible(true);
            currentPowerField.setAccessible(true);
            totalDepositedField.setAccessible(true);

            maxPowerField.set(destination, maxPowerField.get(source));
            currentPowerField.set(destination, currentPowerField.get(source));
            totalDepositedField.set(destination, totalDepositedField.get(source));

        } catch (Exception e) {
            // If reflection fails, use the public methods available
            // This is a fallback - you should implement proper setters in Sect class
            System.out.println("Warning: Could not copy power data during sect rename");
        }
    }

    public static int help(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Player player = source.getPlayer();

        if (player == null) {
            source.sendSystemMessage(Component.literal("This command can only be used by players."));
            return 0;
        }

        // Header
        player.sendSystemMessage(Component.literal("§6§l=== Sect Commands Help ===§r").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        player.sendSystemMessage(Component.literal("§7Hover over commands for details§r\n"));

        // Rank Hierarchy Info
        player.sendSystemMessage(Component.literal("§eRank Hierarchy: §aOuter §7< §bInner §7< §cElder §7< §6Sect Master§r\n"));

        // Basic Commands (Available to all players)
        player.sendSystemMessage(Component.literal("§e§lBasic Commands:§r").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD));

        sendHelpEntry(player, "/sect create <name>",
                "Create a new sect",
                "Usage: /sect create <sectName>\nName can contain letters, numbers, and spaces",
                "Rank: §aAnyone (not in a sect)");

        sendHelpEntry(player, "/sect info [sect|player]",
                "View information about your sect or another sect/player",
                "Usage: /sect info [target]\nLeave target blank for your own sect",
                "Rank: §aAnyone");

        sendHelpEntry(player, "/sect list",
                "List all available sects on the server",
                "Usage: /sect list\nShows member count and status of all sects",
                "Rank: §aAnyone");

        sendHelpEntry(player, "/sect join <sect>",
                "Join a sect you've been invited to or that is open",
                "Usage: /sect join <sectName>",
                "Rank: §aAnyone (when invited or sect is open)");

        // Member Commands (Outer+)
        player.sendSystemMessage(Component.literal("\n§a§lOuter+ Commands:§r").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD));

        sendHelpEntry(player, "/sect leave",
                "Leave your current sect",
                "Usage: /sect leave\n§cWarning: You'll lose sect progress and need a new invite to rejoin!",
                "Rank: §aOuter§7+ (except Sect Master)");

        sendHelpEntry(player, "/sect chat <message>",
                "Send a message to your sect members",
                "Usage: /sect chat <message>\nAlternative: Use /sect togglechat then type normally",
                "Rank: §aOuter§7+");

        sendHelpEntry(player, "/sect togglechat",
                "Toggle between sect chat and global chat mode",
                "Usage: /sect togglechat\nWhen enabled, all chat goes to sect members only",
                "Rank: §aOuter§7+");

        sendHelpEntry(player, "/sect missions",
                "View available missions for your rank",
                "Usage: /sect missions\nShows missions you can accept and their progress",
                "Rank: §aOuter§7+");

        sendHelpEntry(player, "/sect mission accept <missionId>",
                "Accept a mission to start working on it",
                "Usage: /sect mission accept <missionId>\nGet missionId from /sect missions",
                "Rank: §aOuter§7+ (must meet mission rank requirement)");

        sendHelpEntry(player, "/sect mission complete <missionId>",
                "Complete a mission and claim rewards",
                "Usage: /sect mission complete <missionId>\nRequires mission to be finished",
                "Rank: §aOuter§7+");

        sendHelpEntry(player, "/sect merit",
                "Check your merit points and promotion progress",
                "Usage: /sect merit\nShows current merit and progress to next rank",
                "Rank: §aOuter§7+");

        // Elder Commands (Elder+)
        player.sendSystemMessage(Component.literal("\n§c§lElder Commands:§r").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));

        sendHelpEntry(player, "/sect claim [radius]",
                "Claim territory for your sect using power",
                "Usage: /sect claim [radius]\n" +
                        "- No radius: Claims only the current chunk (1 chunk)\n" +
                        "- With radius: Claims chunks in a square area (radius 1 = 3x3, radius 2 = 5x5)\n\n" +
                        "§6Costs:\n" +
                        "§7- Normal claim: §e50 power§7 per chunk\n" +
                        "§7- Overclaim enemy: §e100 power§7 per chunk\n\n" +
                        "§6Requirements:\n" +
                        "§7- Must be §cElder§7 or §6Sect Master\n" +
                        "§7- Must have enough current power\n" +
                        "§7- Can only overclaim enemy sects with lower max power",
                "Rank: §cElder§7+");

        sendHelpEntry(player, "/sect unclaim [radius]",
                "Unclaim territory from your sect and refund power",
                "Usage: /sect unclaim [radius]\n" +
                        "- No radius: Unclaims only the current chunk (1 chunk)\n" +
                        "- With radius: Unclaims chunks in a square area (radius 1 = 3x3, radius 2 = 5x5)\n\n" +
                        "§6Refund:\n" +
                        "§7- §e1 power§7 refunded per chunk unclaimed\n\n" +
                        "§6Requirements:\n" +
                        "§7- Must be §cElder§7 or §6Sect Master\n" +
                        "§7- Can only unclaim your own sect's territory\n\n" +
                        "§6Note:§7 Refunded power goes to current power, not max power",
                "Rank: §cElder§7+");

        sendHelpEntry(player, "/sect invite <player>",
                "Invite a player to join your sect",
                "Usage: /sect invite <playerName>\nPlayer must be online and not in a sect",
                "Rank: §cElder§7+");

        sendHelpEntry(player, "/sect kick <player>",
                "Remove a player from your sect",
                "Usage: /sect kick <playerName>\n§cCannot kick Sect Master or other Elders",
                "Rank: §cElder§7+");

        sendHelpEntry(player, "/sect description <text>",
                "Set your sect's public description",
                "Usage: /sect description <text>\nMax 200 characters, shown in /sect info",
                "Rank: §cElder§7+");

        sendHelpEntry(player, "/sect open",
                "Toggle whether anyone can join without an invite",
                "Usage: /sect open\nToggles between open and invite-only",
                "Rank: §cElder§7+");

        sendHelpEntry(player, "/sect ally <sect>",
                "Send an alliance request to another sect",
                "Usage: /sect ally <sectName>\nSends a request that target sect's Elders can accept/deny",
                "Rank: §cElder§7+");

        sendHelpEntry(player, "/sect ally accept <sect>",
                "Accept a pending alliance request",
                "Usage: /sect ally accept <sectName>\nAccept a pending alliance request from another sect",
                "Rank: §cElder§7+");

        sendHelpEntry(player, "/sect ally decline <sect>",
                "Decline a pending alliance request",
                "Usage: /sect ally decline <sectName>\nDecline a pending alliance request from another sect",
                "Rank: §cElder§7+");

        sendHelpEntry(player, "/sect ally requests",
                "List pending alliance requests for your sect",
                "Usage: /sect ally requests\nShows all pending alliance requests with accept/decline buttons",
                "Rank: §cElder§7+");

        sendHelpEntry(player, "/sect recommend <player>",
                "Recommend an Inner member for Elder promotion",
                "Usage: /sect recommend <playerName>\nRequires: Player must be Inner rank\nEach Inner member needs 3 recommendations to become Elder",
                "Rank: §cElder§7+");

        sendHelpEntry(player, "/sect claim mission submissions",
                "Claim all items submitted from completed missions",
                "Usage: /sect claim mission submissions\nCollects items members submitted for missions",
                "Rank: §cElder§7+");

        sendHelpEntry(player, "/sect mission create <name> <rank> <merit> <duration> <requirements>",
                "Create a new mission for specific rank with merit reward and time limit",
                "Usage: /sect mission create <name> <rank> <merit> <duration> <requirements>\n" +
                        "Rank: OUTER, INNER, ELDER, SECT_MASTER\n" +
                        "Merit: Number of merit points as reward\n" +
                        "Duration: 30m (30min), 2h (2hours), 7d (7days), none (no limit)\n" +
                        "Requirements: TYPE:namespace:id:count\n" +
                        "Example: /sect mission create \"Diamond Hunt\" OUTER 100 60m ITEM:minecraft:diamond:10\n" +
                        "Example: /sect mission create \"Zombie Slayer\" INNER 250 1d KILL_MOB:minecraft:zombie:10\n" +
                        "Note: All missions are one-time only and disappear after completion",
                "Rank: §cElder§7+");

        // Sect Master Commands
        player.sendSystemMessage(Component.literal("\n§6§lSect Master Commands:§r").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));

        sendHelpEntry(player, "/sect rename <new_name>",
                "Rename your sect",
                "Usage: /sect rename <newName>\nName can contain letters, numbers, and spaces\n§cWarning: This will update all references to your sect!",
                "Rank: §6Sect Master");

        sendHelpEntry(player, "/sect promote <player>",
                "Promote a member to higher rank",
                "Usage: /sect promote <playerName>\nRanks: Outer → Inner → Elder",
                "Rank: §6Sect Master");

        sendHelpEntry(player, "/sect demote <player>",
                "Demote a member to lower rank",
                "Usage: /sect demote <playerName>\nRanks: Elder → Inner → Outer\n§cCannot demote yourself",
                "Rank: §6Sect Master");

        sendHelpEntry(player, "/sect transfer <player>",
                "Transfer sect leadership to another member",
                "Usage: /sect transfer <playerName>\n§cWarning: You will become an Elder!",
                "Rank: §6Sect Master");

        sendHelpEntry(player, "/sect disband",
                "Permanently delete the sect",
                "Usage: /sect disband\n§cWarning: This cannot be undone! All data will be lost.",
                "Rank: §6Sect Master");

        sendHelpEntry(player, "/sect settings friendlyfire <true/false>",
                "Toggle whether sect members can damage each other",
                "Usage: /sect settings friendlyfire <true|false>\nfalse = members can't damage each other",
                "Rank: §6Sect Master");

        sendHelpEntry(player, "/sect title <player> <title>",
                "Set a custom title for a sect member",
                "Usage: /sect title <playerName> <customTitle>\nAppears next to player's name",
                "Rank: §6Sect Master");

        // Mission Requirements Format Help
        player.sendSystemMessage(Component.literal("\n§b§lMission Requirements Format:§r").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD));
        player.sendSystemMessage(Component.literal("§7ITEM:§emod:item:count§7 - Collect items (e.g. ITEM:minecraft:diamond:10)"));
        player.sendSystemMessage(Component.literal("§7KILL_MOB:§emod:entity:count§7 - Kill mobs (e.g. KILL_MOB:minecraft:zombie:5)"));
        player.sendSystemMessage(Component.literal("§7Separate multiple requirements with spaces"));

        // Merit & Auto-Promotion Info
        player.sendSystemMessage(Component.literal("\n§b§lMerit & Auto-Promotion:§r").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD));
        player.sendSystemMessage(Component.literal("§7- §aOuter §7→ §bInner: §62500 merit points"));
        player.sendSystemMessage(Component.literal("§7- §bInner §7→ §cElder: §610000 merit points, and 3 Recommandations from 3 elders and above"));
        player.sendSystemMessage(Component.literal("§7- Earn merit by completing missions"));
        player.sendSystemMessage(Component.literal("§7- Promotions to §6Sect Master §7must be done manually"));

        // Footer
        player.sendSystemMessage(Component.literal("\n§7Hover over commands for detailed information"));
        player.sendSystemMessage(Component.literal("§7Click commands to quickly insert them in chat"));

        return 1;
    }

    private static void sendHelpEntry(Player player, String command, String description, String usage, String rank) {
        Component hoverText = Component.literal("")
                .append(Component.literal("Description: ").withStyle(ChatFormatting.GOLD))
                .append(Component.literal(description + "\n").withStyle(ChatFormatting.WHITE))
                .append(Component.literal("\nUsage: ").withStyle(ChatFormatting.YELLOW))
                .append(Component.literal(usage + "\n").withStyle(ChatFormatting.GRAY))
                .append(Component.literal("\nRequired Rank: ").withStyle(ChatFormatting.GREEN))
                .append(Component.literal(rank).withStyle(ChatFormatting.WHITE));

        Component message = Component.literal("§e" + command)
                .withStyle(Style.EMPTY
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText))
                        .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command.split(" ")[0]))
                );

        player.sendSystemMessage(message);
    }

    /**
     * Suggests online members of the player's sect
     */
    private static CompletableFuture<Suggestions> suggestSectMembers(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder, boolean includeSelf) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null) {
            SectManager manager = getManager(context);
            if (manager != null) {
                Sect sect = manager.getPlayerSect(player.getUUID());
                if (sect != null) {
                    Collection<ServerPlayer> onlinePlayers = context.getSource().getServer().getPlayerList().getPlayers();
                    for (ServerPlayer onlinePlayer : onlinePlayers) {
                        if (!includeSelf && onlinePlayer == player) continue;

                        SectMember member = sect.getMember(onlinePlayer.getUUID());
                        if (member != null) {
                            builder.suggest(onlinePlayer.getGameProfile().getName());
                        }
                    }
                }
            }
        }
        return builder.buildFuture();
    }

    /**
     * Suggests online Inner members of the player's sect (for recommendation)
     */
    private static CompletableFuture<Suggestions> suggestInnerMembers(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null) {
            SectManager manager = getManager(context);
            if (manager != null) {
                Sect sect = manager.getPlayerSect(player.getUUID());
                if (sect != null) {
                    // Check if player has permission to recommend
                    SectMember recommender = sect.getMember(player.getUUID());
                    if (recommender != null && (recommender.getRank() == SectRank.ELDER || recommender.getRank() == SectRank.SECT_MASTER)) {
                        Collection<ServerPlayer> onlinePlayers = context.getSource().getServer().getPlayerList().getPlayers();
                        for (ServerPlayer onlinePlayer : onlinePlayers) {
                            if (onlinePlayer == player) continue;

                            SectMember targetMember = sect.getMember(onlinePlayer.getUUID());
                            if (targetMember != null && targetMember.getRank() == SectRank.INNER) {
                                builder.suggest(onlinePlayer.getGameProfile().getName());
                            }
                        }
                    }
                }
            }
        }
        return builder.buildFuture();
    }
}