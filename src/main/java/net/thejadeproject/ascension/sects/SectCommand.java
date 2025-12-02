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
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
                .then(Commands.literal("join")
                        .then(Commands.argument("sectname", StringArgumentType.string())
                                .executes(SectCommand::joinSect)))
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
                .then(Commands.literal("open")
                        .executes(SectCommand::openSect))
                .then(Commands.literal("chat")
                        .then(Commands.argument("msg", StringArgumentType.greedyString())
                                .executes(SectCommand::sectChat)))
                .then(Commands.literal("togglechat")
                        .executes(SectCommand::toggleChat))
                        .then(Commands.literal("ally")
                                .then(Commands.argument("sectname", StringArgumentType.string())
                                        .suggests(SectCommand::suggestSectNames)  // Add this line
                                        .executes(SectCommand::allySect))
                                .then(Commands.literal("accept")
                                        .then(Commands.argument("sectname", StringArgumentType.string())
                                                .suggests(SectCommand::suggestSectNames)  // Add this line
                                                .executes(SectCommand::acceptAlly)))
                                .then(Commands.literal("decline")
                                        .then(Commands.argument("sectname", StringArgumentType.string())
                                                .suggests(SectCommand::suggestSectNames)  // Add this line
                                                .executes(SectCommand::declineAlly)))
                        .then(Commands.literal("requests")
                                .executes(SectCommand::listAllyRequests)))
                .then(Commands.literal("info")
                        .executes(SectCommand::sectInfo)
                        .then(Commands.argument("target", StringArgumentType.string())
                                .suggests((context, builder) -> {  // Replace with new combined suggester
                                    ServerPlayer player = context.getSource().getPlayer();
                                    if (player != null) {
                                        // Suggest sect names
                                        SectManager manager = getManager(context);
                                        if (manager != null) {
                                            for (String sectName : manager.getAllSects().keySet()) {
                                                builder.suggest(sectName);
                                            }
                                        }

                                        // Suggest player names
                                        Collection<ServerPlayer> onlinePlayers = context.getSource().getServer().getPlayerList().getPlayers();
                                        for (ServerPlayer onlinePlayer : onlinePlayers) {
                                            builder.suggest(onlinePlayer.getGameProfile().getName());
                                        }
                                    }
                                    return builder.buildFuture();
                                })
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
                .then(Commands.literal("enemy")
                        .then(Commands.argument("sectname", StringArgumentType.string())
                                .suggests(SectCommand::suggestSectNames)  // Add this line
                                .executes(SectCommand::setEnemy)))
                .then(Commands.literal("help")
                        .executes(SectCommand::help))
        );
    }

    private static SectManager getManager(CommandContext<CommandSourceStack> context) {
        return AscensionCraft.getSectManager(context.getSource().getServer());
    }

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

        SectMember member = sect.getMember(player.getUUID());
        if (member == null || (member.getRank() != SectRank.SECT_MASTER && member.getRank() != SectRank.ELDER)) {
            context.getSource().sendFailure(Component.literal("§cOnly Elders and Sect Masters can set the sect description!"));
            return 0;
        }

        if (description.length() > 200) {
            context.getSource().sendFailure(Component.literal("§cDescription too long! Maximum 200 characters."));
            return 0;
        }

        sect.setDescription(description);
        manager.setDirty();

        context.getSource().sendSuccess(() -> Component.literal("§aSect description updated!"), true);

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

        ServerPlayer targetPlayer = context.getSource().getServer().getPlayerList().getPlayerByName(targetName);
        if (targetPlayer == null) {
            context.getSource().sendFailure(Component.literal("§cPlayer '" + targetName + "' is not online!"));
            return 0;
        }

        if (manager.getPlayerSect(targetPlayer.getUUID()) != null) {
            context.getSource().sendFailure(Component.literal("§c" + targetName + " is already in a sect!"));
            return 0;
        }

        if (targetPlayer.getUUID().equals(player.getUUID())) {
            context.getSource().sendFailure(Component.literal("§cYou cannot invite yourself!"));
            return 0;
        }

        manager.addInvite(sect.getName(), targetName);
        context.getSource().sendSuccess(() -> Component.literal("§aInvited " + targetName + " to join your sect!"), false);

        sendEnhancedInvitationMessage(targetPlayer, sect, player);

        return 1;
    }

    private static void sendEnhancedInvitationMessage(ServerPlayer targetPlayer, Sect sect, ServerPlayer inviter) {
        String sectName = sect.getName().replace(" ", "_");

        MutableComponent inviteMessage = Component.literal("§6§lSECT INVITATION\n§r")
                .append(Component.literal("§eYou have been invited to join §b" + sect.getName() + "§e!\n"))
                .append(Component.literal("§7Invited by: §f" + inviter.getScoreboardName() + "\n\n"));

        MutableComponent acceptButton = Component.literal("[§a✔ ACCEPT§r]")
                .withStyle(Style.EMPTY
                        .withColor(ChatFormatting.GREEN)
                        .withBold(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sect join " + sectName))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.literal("§aClick to join §b" + sect.getName() + "§a!")))
                );

        MutableComponent declineButton = Component.literal("[§c✖ DECLINE§r]")
                .withStyle(Style.EMPTY
                        .withColor(ChatFormatting.RED)
                        .withBold(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sect decline " + sectName))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.literal("§cClick to decline the invitation from §b" + sect.getName() + "§c!")))
                );

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

        List<String> pendingInvites = manager.getPendingInvites(player.getScoreboardName());
        String exactSectName = pendingInvites.stream().filter(invitedSect -> invitedSect.equalsIgnoreCase(sectName)).findFirst().orElse(null);

        if (exactSectName == null) {
            context.getSource().sendFailure(Component.literal("§cYou don't have an invitation from §e" + sectName + "§c!"));
            return 0;
        }

        Set<String> invites = manager.pendingInvites.get(exactSectName);
        if (invites != null) {
            invites.remove(player.getScoreboardName().toLowerCase());
            if (invites.isEmpty()) {
                manager.pendingInvites.remove(exactSectName);
            }
        }

        context.getSource().sendSuccess(() -> Component.literal("§eYou have declined the invitation from §b" + exactSectName + "§e."), false);

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

        if (currentRank == newRank) {
            context.getSource().sendFailure(Component.literal("§cPlayer '" + targetName + "' cannot be demoted further!"));
            return 0;
        }

        if (currentRank == SectRank.SECT_MASTER) {
            context.getSource().sendFailure(Component.literal("§cYou cannot demote the Sect Master!"));
            return 0;
        }

        SectMember executorMember = sect.getMember(player.getUUID());
        if (executorMember.getRank() == SectRank.ELDER && currentRank == SectRank.ELDER) {
            context.getSource().sendFailure(Component.literal("§cElders cannot demote other Elders! Only the Sect Master can."));
            return 0;
        }

        sect.setMemberRank(targetPlayer.getUUID(), newRank);
        context.getSource().sendSuccess(() -> Component.literal("§aDemoted " + targetName + " to " + newRank.getDisplayName() + "!"), true);

        if (targetPlayer.isAlive()) {
            targetPlayer.sendSystemMessage(Component.literal("§cYou have been demoted to " + newRank.getDisplayName() + " in " + sect.getName() + "!"));
        }

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

        Sect sect = manager.getSect(sectName);
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

            broadcastToSect(sect, context.getSource().getServer(), "§e" + player.getScoreboardName() + " has joined the sect!");
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("§cYou are not invited to this sect and it's not open!"));
            return 0;
        }
    }

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
            playerSect.addAlly(targetSectName);
            targetSect.addAlly(playerSect.getName());
            manager.removeAllyRequest(targetSectName, playerSect.getName());

            broadcastToSect(playerSect, context.getSource().getServer(), "§aAlliance formed with " + targetSectName + "!");
            broadcastToSect(targetSect, context.getSource().getServer(), "§aAlliance formed with " + playerSect.getName() + "!");

            context.getSource().sendSuccess(() -> Component.literal("§aAlliance formed with " + targetSectName + "!"), false);
        } else {
            manager.addAllyRequest(playerSect.getName(), targetSectName);
            context.getSource().sendSuccess(() -> Component.literal("§aAlly request sent to " + targetSectName), false);

            sendEnhancedAllyRequest(targetSect, playerSect, player, context.getSource().getServer());
        }

        return 1;
    }

    private static void sendEnhancedAllyRequest(Sect targetSect, Sect requestingSect, ServerPlayer requester, net.minecraft.server.MinecraftServer server) {
        String requestingSectName = requestingSect.getName();
        String targetSectName = targetSect.getName();

        MutableComponent allyMessage = Component.literal("§6§lALLY REQUEST\n§r")
                .append(Component.literal("§eYou have received an alliance request from §b" + requestingSectName + "§e!\n"))
                .append(Component.literal("§7Requested by: §f" + requester.getScoreboardName() + "\n\n"));

        MutableComponent acceptButton = Component.literal("[§a✔ ACCEPT§r]")
                .withStyle(Style.EMPTY
                        .withColor(ChatFormatting.GREEN)
                        .withBold(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sect ally accept " + requestingSectName.replace(" ", "_")))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.literal("§aClick to accept alliance with §b" + requestingSectName + "§a!")))
                );

        MutableComponent declineButton = Component.literal("[§c✖ DECLINE§r]")
                .withStyle(Style.EMPTY
                        .withColor(ChatFormatting.RED)
                        .withBold(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sect ally decline " + requestingSectName.replace(" ", "_")))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.literal("§cClick to decline alliance with §b" + requestingSectName + "§c!")))
                );

        allyMessage.append(Component.literal("§eClick to respond: "))
                .append(acceptButton)
                .append(Component.literal(" §8| "))
                .append(declineButton)
                .append(Component.literal("\n§7Or type: §a/sect ally accept " + requestingSectName.replace(" ", "_")));

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

        playerSect.addAlly(requestingSectName);
        requestingSect.addAlly(playerSect.getName());
        manager.removeAllyRequest(requestingSectName, playerSect.getName());

        String allianceMessage1 = "§aAlliance formed with " + requestingSectName + "!";
        String allianceMessage2 = "§aAlliance formed with " + playerSect.getName() + "!";

        broadcastToSect(playerSect, context.getSource().getServer(), allianceMessage1);
        broadcastToSect(requestingSect, context.getSource().getServer(), allianceMessage2);

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

        if (!sect.getOwnerId().equals(player.getUUID())) {
            context.getSource().sendFailure(Component.literal("§cOnly the sect owner can disband the sect!"));
            return 0;
        }

        String sectName = sect.getName();

        for (String allyName : sect.getAllies()) {
            Sect allySect = manager.getSect(allyName);
            if (allySect != null) {
                allySect.removeAlly(sectName);
                broadcastToSect(allySect, context.getSource().getServer(),
                        "§cYour alliance with §e" + sectName + "§c has been dissolved because they disbanded.");
            }
        }

        for (String targetSectName : new ArrayList<>(manager.allyRequests.keySet())) {
            Set<String> requests = manager.allyRequests.get(targetSectName);
            if (requests != null) {
                requests.remove(sectName);
                if (requests.isEmpty()) {
                    manager.allyRequests.remove(targetSectName);
                }
            }
        }

        manager.allyRequests.remove(sectName);

        broadcastToSect(sect, context.getSource().getServer(), "§cThe sect has been disbanded by the owner!");

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

        if (sect.getOwnerId().equals(player.getUUID())) {
            context.getSource().sendFailure(Component.literal(
                    "§cAs the sect owner, you cannot leave the sect! " +
                            "Use §6/sect master <player>§c to transfer ownership, or §6/sect disband§c to delete the sect."
            ));
            return 0;
        }

        String sectName = sect.getName();
        String playerName = player.getScoreboardName();

        sect.removeMember(player.getUUID());
        manager.removePlayerFromSect(player.getUUID());

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

        if (!sect.getOwnerId().equals(player.getUUID())) {
            context.getSource().sendFailure(Component.literal("§cOnly the sect owner can transfer ownership!"));
            return 0;
        }

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

        if (targetPlayer.getUUID().equals(player.getUUID())) {
            context.getSource().sendFailure(Component.literal("§cYou are already the sect owner!"));
            return 0;
        }

        String sectName = sect.getName();
        String oldOwnerName = player.getScoreboardName();

        sect.setMemberRank(player.getUUID(), SectRank.ELDER);
        sect.setMemberRank(targetPlayer.getUUID(), SectRank.SECT_MASTER);
        sect.setOwner(targetPlayer.getUUID());

        manager.setDirty();

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

        SectMember executorMember = sect.getMember(player.getUUID());
        if (executorMember == null || (executorMember.getRank() != SectRank.SECT_MASTER && executorMember.getRank() != SectRank.ELDER)) {
            context.getSource().sendFailure(Component.literal("§cOnly Elders and Sect Masters can kick members!"));
            return 0;
        }

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

        if (targetPlayer.getUUID().equals(player.getUUID())) {
            context.getSource().sendFailure(Component.literal("§cYou cannot kick yourself! Use §6/sect leave§c instead."));
            return 0;
        }

        if (targetMember.getRank() == SectRank.SECT_MASTER) {
            context.getSource().sendFailure(Component.literal("§cYou cannot kick the Sect Master!"));
            return 0;
        }

        if (executorMember.getRank() == SectRank.ELDER && targetMember.getRank() == SectRank.ELDER) {
            context.getSource().sendFailure(Component.literal("§cElders cannot kick other Elders! Only the Sect Master can."));
            return 0;
        }

        String sectName = sect.getName();
        String targetPlayerName = targetPlayer.getScoreboardName();

        sect.removeMember(targetPlayer.getUUID());
        manager.removePlayerFromSect(targetPlayer.getUUID());

        targetPlayer.sendSystemMessage(Component.literal("§cYou have been kicked from the sect '" + sectName + "' by " + player.getScoreboardName() + "!"));

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
            sect = manager.getPlayerSect(player.getUUID());
            if (sect == null) {
                context.getSource().sendFailure(Component.literal("§cYou are not in a sect!"));
                return 0;
            }
            displayName = "Your Sect";
        } else {
            sect = manager.getSect(target);
            if (sect != null) {
                displayName = "Sect '" + target + "'";
            } else {
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


        context.getSource().sendSuccess(() -> Component.literal("§6=== " + displayName + " Info ==="), false);
        Sect finalSect = sect;
        context.getSource().sendSuccess(() -> Component.literal("§eSect: §f" + finalSect.getName()), false);
        context.getSource().sendSuccess(() -> Component.literal("§eDescription: §7" + finalSect.getDescription()), false);

        String openStatus = sect.isOpen() ? "§aOpen" : "§cClosed";
        context.getSource().sendSuccess(() -> Component.literal("§eStatus: " + openStatus), false);

        StringBuilder allies = new StringBuilder("§eAllies: §f");
        if (sect.getAllies().isEmpty()) {
            allies.append("None");
        } else {
            allies.append(String.join(", ", sect.getAllies()));
        }
        context.getSource().sendSuccess(() -> Component.literal(allies.toString()), false);

        StringBuilder enemies = new StringBuilder("§eEnemies: §c");
        if (sect.getEnemies().isEmpty()) {
            enemies.append("None");
        } else {
            enemies.append(String.join("§c, §c", sect.getEnemies()));
        }
        context.getSource().sendSuccess(() -> Component.literal(enemies.toString()), false);

        net.minecraft.server.MinecraftServer server = context.getSource().getServer();

        List<SectMember> onlineMembers = new ArrayList<>();
        List<SectMember> offlineMembers = new ArrayList<>();

        for (SectMember member : sect.getMembers().values()) {
            ServerPlayer onlinePlayer = server.getPlayerList().getPlayer(member.getPlayerId());
            if (onlinePlayer != null) {
                onlineMembers.add(member);
            } else {
                offlineMembers.add(member);
            }
        }


        Comparator<SectMember> rankComparator = Comparator.comparingInt(m -> {
            switch (m.getRank()) {
                case SECT_MASTER: return 0;
                case ELDER: return 1;
                case INNER: return 2;
                case OUTER: return 3;
                default: return 4;
            }
        });

        onlineMembers.sort(rankComparator);
        offlineMembers.sort(rankComparator);

        Sect finalSect1 = sect;
        context.getSource().sendSuccess(() -> Component.literal("§eMembers (" + finalSect1.getMembers().size() + "):"), false);

        if (!onlineMembers.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("§aOnline (" + onlineMembers.size() + "):"), false);
            for (SectMember member : onlineMembers) {
                String rankColor = getRankColor(member.getRank());
                String title = member.getTitle().isEmpty() ? "" : " §7" + member.getTitle();
                String memberInfo = "  §7- " + rankColor + member.getPlayerName() + " §8[" + member.getRank().getDisplayName() + "§8]" + title;
                context.getSource().sendSuccess(() -> Component.literal(memberInfo), false);
            }
        }

        if (!offlineMembers.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("§7Offline (" + offlineMembers.size() + "):"), false);
            for (SectMember member : offlineMembers) {
                String rankColor = getRankColor(member.getRank());
                String title = member.getTitle().isEmpty() ? "" : " §7" + member.getTitle();
                String memberInfo = "  §7- " + rankColor + member.getPlayerName() + " §8[" + member.getRank().getDisplayName() + "§8]" + title;
                context.getSource().sendSuccess(() -> Component.literal(memberInfo), false);
            }
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

            if (!isAccepted) {
                validMissions.add(mission);
                continue;
            }

            if (progress != null) {
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

                String status;
                if (canComplete && isAccepted) {
                    status = "§a[CLAIM REWARD]";
                } else if (isAccepted) {
                    status = "§e[IN PROGRESS]";
                } else {
                    status = "§7[AVAILABLE]";
                }

                MutableComponent missionComponent = Component.literal(status + " " + mission.getDisplayName());

                missionComponent.withStyle(style -> style
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, createMissionHoverText(mission, progress))));

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
        text.append(Component.literal("§7Duration: " + mission.getTimeRemaining() + "\n\n"));

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
                Map<Integer, Integer> currentProgress = progress.getProgress();
                int currentAmount = currentProgress.getOrDefault(i, 0);
                requirementText += " §8(" + currentAmount + "/" + req.getCount() + ")";
            }

            text.append(Component.literal("§7- " + requirementText + "\n"));
        }

        text.append(Component.literal("\n§6Reward: §e" + mission.getRewardMerit() + " Merit Points"));

        if (!mission.getRewardItem().isEmpty()) {
            String itemName = mission.getRewardItem().getHoverName().getString();
            text.append(Component.literal(" §a+ " + itemName));
        }

        return text;
    }

    private static long parseDuration(String durationStr) {
        if (durationStr == null || durationStr.equalsIgnoreCase("none")) {
            return 0;
        }

        try {
            char unit = durationStr.charAt(durationStr.length() - 1);
            String numberStr = durationStr.substring(0, durationStr.length() - 1);
            int value = Integer.parseInt(numberStr);

            switch (Character.toLowerCase(unit)) {
                case 'm':
                    return System.currentTimeMillis() + (value * 60 * 1000L);
                case 'h':
                    return System.currentTimeMillis() + (value * 60 * 60 * 1000L);
                case 'd':
                    return System.currentTimeMillis() + (value * 24 * 60 * 60 * 1000L);
                default:
                    return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    private static int createMissionAdvanced(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String displayName = StringArgumentType.getString(context, "displayName");
        String rankStr = StringArgumentType.getString(context, "targetRank");
        int meritReward = IntegerArgumentType.getInteger(context, "meritReward");
        String durationStr = StringArgumentType.getString(context, "duration");
        boolean useHandItem = BoolArgumentType.getBool(context, "useHandItem");
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

        SectRank targetRank;
        try {
            targetRank = SectRank.valueOf(rankStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            context.getSource().sendFailure(Component.literal("§cInvalid rank! Use: OUTER, INNER, ELDER, or SECT_MASTER"));
            return 0;
        }

        SectMember executor = sect.getMember(player.getUUID());
        if (executor.getRank().getLevel() < targetRank.getLevel()) {
            context.getSource().sendFailure(Component.literal("§cYou cannot create missions for a rank higher than your own!"));
            return 0;
        }

        long expirationTime = parseDuration(durationStr);
        if (expirationTime == 0 && !durationStr.equalsIgnoreCase("none")) {
            context.getSource().sendFailure(Component.literal("§cInvalid duration format! Use: 60m, 6h, 6d, or none"));
            context.getSource().sendFailure(Component.literal("§cExamples: 30m (30 minutes), 2h (2 hours), 7d (7 days), none (no expiration)"));
            return 0;
        }

        ItemStack handItem = ItemStack.EMPTY;
        if (useHandItem) {
            handItem = player.getMainHandItem();
            if (handItem.isEmpty()) {
                context.getSource().sendFailure(Component.literal("§cYou must hold an item in your main hand to use as a reward!"));
                return 0;
            }

            handItem = handItem.copy();
            handItem.setCount(1);
        }

        try {
            List<MissionRequirement> requirements = parseRequirements(requirementsStr);

            if (requirements.isEmpty()) {
                context.getSource().sendFailure(Component.literal("§cNo valid requirements found!"));
                context.getSource().sendFailure(Component.literal("§cFormat: TYPE:namespace:id:count"));
                context.getSource().sendFailure(Component.literal("§cExample: ITEM:minecraft:diamond:10"));
                context.getSource().sendFailure(Component.literal("§cExample: KILL_MOB:minecraft:zombie:5"));
                context.getSource().sendFailure(Component.literal("§cYour input: " + requirementsStr));
                return 0;
            }

            SectMission mission = new SectMission(
                    displayName,
                    targetRank,
                    requirements,
                    meritReward,
                    player.getUUID()
            );

            mission.setExpirationTime(expirationTime);

            if (useHandItem) {
                mission.setRewardItem(handItem);
                player.getMainHandItem().shrink(1);
            }

            sect.addMission(mission);
            manager.setDirty();

            context.getSource().sendSuccess(() -> Component.literal("§aMission '" + displayName + "' created!"), false);
            context.getSource().sendSuccess(() -> Component.literal("§7Target Rank: §e" + mission.getTargetRank().getDisplayName()), false);
            context.getSource().sendSuccess(() -> Component.literal("§7Duration: " + mission.getTimeRemaining()), false);

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

        String cleanedStr = requirementsStr.replaceAll("\\s+repeatable\\s*$", "").trim();

        if (cleanedStr.isEmpty()) {
            return requirements;
        }

        try {
            String[] requirementParts = cleanedStr.split("\\s+");

            for (String requirementPart : requirementParts) {
                String[] segments = requirementPart.split(":");
                if (segments.length < 4) {
                    continue;
                }

                String typeStr = segments[0].toUpperCase();
                String countStr = segments[segments.length - 1];

                StringBuilder targetBuilder = new StringBuilder(segments[1]);
                for (int i = 2; i < segments.length - 1; i++) {
                    targetBuilder.append(":").append(segments[i]);
                }
                String target = targetBuilder.toString();

                int count;
                try {
                    count = Integer.parseInt(countStr);
                    if (count <= 0) {
                        continue;
                    }
                } catch (NumberFormatException e) {
                    continue;
                }

                MissionRequirement.RequirementType type;
                try {
                    type = MissionRequirement.RequirementType.valueOf(typeStr);
                } catch (IllegalArgumentException e) {
                    continue;
                }

                requirements.add(new MissionRequirement(type, target, count));
            }

        } catch (Exception e) {
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
            UUID missionId = UUID.fromString(missionIdStr);
            SectMission mission = sect.getMission(missionId);

            if (mission == null) {
                context.getSource().sendFailure(Component.literal("§cMission not found!"));
                return 0;
            }

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

        SectMember recommender = sect.getMember(player.getUUID());
        if (recommender == null || (recommender.getRank() != SectRank.ELDER && recommender.getRank() != SectRank.SECT_MASTER)) {
            context.getSource().sendFailure(Component.literal("§cOnly Elders and Sect Masters can recommend players for Elder promotion!"));
            return 0;
        }

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

        if (targetMember.getRank() != SectRank.INNER) {
            context.getSource().sendFailure(Component.literal("§cYou can only recommend Inner members for Elder promotion!"));
            return 0;
        }

        Set<UUID> existingRecommendations = sect.getRecommendations(targetPlayer.getUUID());
        if (existingRecommendations.contains(player.getUUID())) {
            context.getSource().sendFailure(Component.literal("§cYou have already recommended " + targetName + " for Elder promotion!"));
            return 0;
        }

        sect.addRecommendation(targetPlayer.getUUID(), player.getUUID());
        manager.setDirty();

        int totalRecommendations = sect.getRecommendationCount(targetPlayer.getUUID());
        int neededRecommendations = 3;

        context.getSource().sendSuccess(() -> Component.literal("§aYou have recommended " + targetName + " for Elder promotion! (§e" + totalRecommendations + "§a/§6" + neededRecommendations + "§a recommendations)"), true);

        targetPlayer.sendSystemMessage(Component.literal("§6" + player.getScoreboardName() + " has recommended you for Elder promotion! (§e" + totalRecommendations + "§6/§e" + neededRecommendations + "§6 recommendations)"));

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

        if (member.getRank() == SectRank.OUTER) {
            showFancyProgressBar(context, currentMerit, 2500, "Inner Sect", "§2", "§a", "§8");
            context.getSource().sendSuccess(() -> Component.literal("§7Next Rank: §aInner Sect §7(§62500§7 merit required)"), false);
        } else if (member.getRank() == SectRank.INNER) {
            showFancyProgressBar(context, currentMerit, 10000, "Elder", "§6", "§e", "§8");

            int recommendationCount = sect.getRecommendationCount(player.getUUID());
            int neededRecommendations = 3;

            context.getSource().sendSuccess(() -> Component.literal("§7Next Rank: §6Elder §7(§610000§7 merit + §6" + neededRecommendations + "§7 recommendations)"), false);
            context.getSource().sendSuccess(() -> Component.literal("§7Elder Recommendations: §e" + recommendationCount + "§7/§6" + neededRecommendations), false);

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
        int barLength = 30;
        float progress = Math.min((float) current / required, 1.0f);
        int filledChars = (int) (progress * barLength);

        StringBuilder progressBar = new StringBuilder();
        progressBar.append("§7[");

        for (int i = 0; i < filledChars; i++) {
            float segmentProgress = (float) i / barLength;
            String segmentColor = getGradientColor(segmentProgress, startColor, filledColor);
            progressBar.append(segmentColor).append("█");
        }

        for (int i = filledChars; i < barLength; i++) {
            progressBar.append(emptyColor).append("▒");
        }

        progressBar.append("§7] §e").append(String.format("%.1f", progress * 100)).append("%");

        context.getSource().sendSuccess(() -> Component.literal(progressBar.toString()), false);

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

        Map<String, Integer> itemCounts = new HashMap<>();
        Map<String, String> itemDisplayNames = new HashMap<>();

        List<UUID> missionIdsToRemove = new ArrayList<>();

        for (Map.Entry<UUID, List<ItemStack>> entry : submissions.entrySet()) {
            UUID missionId = entry.getKey();
            List<ItemStack> items = entry.getValue();

            SectMission mission = sect.getMission(missionId);
            String missionName = mission != null ? mission.getDisplayName() : "Unknown Mission";

            for (ItemStack stack : items) {
                if (!stack.isEmpty()) {
                    totalStacks++;
                    totalItems += stack.getCount();

                    String itemId = stack.getItem().toString();
                    String displayName = stack.getHoverName().getString();

                    itemCounts.put(itemId, itemCounts.getOrDefault(itemId, 0) + stack.getCount());
                    itemDisplayNames.put(itemId, displayName);

                    if (!player.getInventory().add(stack.copy())) {
                        player.drop(stack.copy(), false);
                    }
                }
            }

            missionIdsToRemove.add(missionId);
        }

        for (UUID missionId : missionIdsToRemove) {
            sect.removeMissionSubmission(missionId);
        }

        manager.setDirty();

        int finalTotalItems = totalItems;
        int finalTotalStacks = totalStacks;
        context.getSource().sendSuccess(() -> Component.literal("§aClaimed §e" + finalTotalItems + "§a items (§e" + finalTotalStacks + "§a stacks) from mission submissions!"), false);

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

        if (!submissions.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("§7All submitted items have been added to your inventory."), false);
            context.getSource().sendSuccess(() -> Component.literal("§7If your inventory was full, items were dropped on the ground."), false);
        }

        return 1;
    }

    private static int listSects(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        SectManager manager = getManager(context);
        if (manager == null) {
            context.getSource().sendFailure(Component.literal("§cSect system not available!"));
            return 0;
        }

        Map<String, Sect> allSects = manager.getAllSects();

        if (allSects.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("§7No sects have been created on this world yet."), false);
            context.getSource().sendSuccess(() -> Component.literal("§7Use §e/sect create <name>§7 to create one!"), false);
            return 1;
        }

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

            SectMember owner = sect.getMember(sect.getOwnerId());
            String ownerName = owner != null ? owner.getPlayerName() : "Unknown";

            int memberCount = sect.getMembers().size();
            String openStatus = sect.isOpen() ? "§aOpen" : "§cClosed";
            String statusColor = sect.isOpen() ? "§a" : "§c";

            MutableComponent sectLine = Component.literal("§e- " + sectName)
                    .append(Component.literal(" §7(Members: §e" + memberCount + "§7, Status: " + statusColor + (sect.isOpen() ? "Open" : "Closed") + "§7)"));

            MutableComponent hoverText = Component.literal("§6" + sectName + "\n")
                    .append(Component.literal("§cSect Master: §f" + ownerName + "\n"))
                    .append(Component.literal("§bMembers: §e" + memberCount + "\n"))
                    .append(Component.literal("§eStatus: " + statusColor + (sect.isOpen() ? "Open to Join" : "Invite Only") + "\n"))
                    .append(Component.literal("§aAllies: §f" + (sect.getAllies().isEmpty() ? "None" : String.join(", ", sect.getAllies())) + "\n"))
                    .append(Component.literal("§cEnemies: §f" + (sect.getEnemies().isEmpty() ? "None" : String.join(", ", sect.getEnemies())) + "\n")) // NEW
                    .append(Component.literal("§aDescription: §f" + sect.getDescription() + "\n\n"))
                    .append(Component.literal("§aClick to view detailed info"));

            sectLine.withStyle(style -> style
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText))
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sect info " + sectName)));

            context.getSource().sendSuccess(() -> sectLine, false);
        }

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

        if (!oldSect.getOwnerId().equals(player.getUUID())) {
            context.getSource().sendFailure(Component.literal("§cOnly the Sect Master can rename the sect!"));
            return 0;
        }

        if (newName.length() > 40) {
            context.getSource().sendFailure(Component.literal("§cSect name too long! Maximum 40 characters."));
            return 0;
        }

        if (!newName.matches("[a-zA-Z0-9 _]+")) {
            context.getSource().sendFailure(Component.literal("§cSect name can only contain letters, numbers, and spaces."));
            return 0;
        }

        if (manager.getSect(newName) != null) {
            context.getSource().sendFailure(Component.literal("§cA sect with that name already exists!"));
            return 0;
        }

        String oldName = oldSect.getName();

        Set<String> allies = new HashSet<>(oldSect.getAllies());

        Sect newSect = new Sect(newName, oldSect.getOwnerId(), "Transfer");
        copySectData(oldSect, newSect);

        for (String allyName : allies) {
            Sect allySect = manager.getSect(allyName);
            if (allySect != null) {
                allySect.removeAlly(oldName);
                allySect.addAlly(newName);

                broadcastToSect(allySect, context.getSource().getServer(),
                        "§eYour ally §b" + oldName + "§e has been renamed to §b" + newName + "§e!");
            }
        }

        Set<String> oldRequests = manager.allyRequests.remove(oldName);
        if (oldRequests != null) {
            manager.allyRequests.put(newName, oldRequests);
        }

        for (Map.Entry<String, Set<String>> entry : manager.allyRequests.entrySet()) {
            Set<String> requests = entry.getValue();
            if (requests.remove(oldName)) {
                requests.add(newName);
            }
        }

        manager.removeSect(oldName);
        manager.sects.put(newName, newSect);

        for (SectMember member : newSect.getMembers().values()) {
            manager.playerSects.put(member.getPlayerId(), newName);
        }

        manager.setDirty();

        broadcastToSect(newSect, context.getSource().getServer(),
                "§eThe sect has been renamed to §b" + newName + "§e by the Sect Master!");

        context.getSource().sendSuccess(() -> Component.literal("§aSect renamed to '" + newName + "'!"), true);

        return 1;
    }

    private static void copySectData(Sect source, Sect destination) {
        destination.setOpen(source.isOpen());
        destination.setDescription(source.getDescription());
        destination.setFriendlyFire(source.isFriendlyFire());

        for (SectMember member : source.getMembers().values()) {
            destination.addMember(member.getPlayerId(), member.getPlayerName(), member.getRank());
            if (!member.getTitle().isEmpty()) {
                destination.setMemberTitle(member.getPlayerId(), member.getTitle());
            }
        }

        for (String ally : source.getAllies()) {
            destination.addAlly(ally);
        }

        for (SectMission mission : source.getMissions()) {
            destination.addMission(mission);
        }

        for (Map.Entry<UUID, Integer> entry : source.playerMerit.entrySet()) {
            destination.playerMerit.put(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<UUID, Set<UUID>> entry : source.elderRecommendations.entrySet()) {
            destination.elderRecommendations.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }

        for (Map.Entry<UUID, List<ItemStack>> entry : source.getMissionSubmissions().entrySet()) {
            destination.getMissionSubmissions().put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
    }

    private static CompletableFuture<Suggestions> suggestSectNames(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        SectManager manager = getManager(context);
        if (manager != null) {
            for (String sectName : manager.getAllSects().keySet()) {
                builder.suggest(sectName);
            }
        }
        return builder.buildFuture();
    }

    // NEW: Set enemy command (restored without territory claims)
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

        SectMember member = sect.getMember(player.getUUID());
        if (member == null || (member.getRank() != SectRank.ELDER && member.getRank() != SectRank.SECT_MASTER)) {
            context.getSource().sendFailure(Component.literal("§cOnly Elders and Sect Masters can set enemies!"));
            return 0;
        }

        Sect enemySect = manager.getSect(enemySectName);
        if (enemySect == null) {
            context.getSource().sendFailure(Component.literal("§cSect '" + enemySectName + "' does not exist!"));
            return 0;
        }

        if (sect.getName().equals(enemySectName)) {
            context.getSource().sendFailure(Component.literal("§cYou cannot set your own sect as an enemy!"));
            return 0;
        }

        if (sect.isEnemy(enemySectName)) {
            context.getSource().sendFailure(Component.literal("§c" + enemySectName + " is already your enemy!"));
            return 0;
        }

        sect.addEnemy(enemySectName);
        manager.setDirty();

        context.getSource().sendSuccess(() -> Component.literal(
                "§cMarked §e" + enemySectName + "§c as an enemy!"
        ), true);

        broadcastToSect(sect, context.getSource().getServer(),
                "§c" + enemySectName + " has been marked as an enemy by " + player.getScoreboardName() + "!");

        return 1;
    }

    // NEW: Help command (restored)
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

        sendHelpEntry(player, "/sect enemy <sect>",
                "Mark another sect as an enemy",
                "Usage: /sect enemy <sectName>\nMark a sect as hostile (for roleplay purposes)",
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
        player.sendSystemMessage(Component.literal("§7- §bInner §7→ §cElder: §610000 merit points, and 3 Recommendations from 3 elders and above"));
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

    private static CompletableFuture<Suggestions> suggestInnerMembers(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null) {
            SectManager manager = getManager(context);
            if (manager != null) {
                Sect sect = manager.getPlayerSect(player.getUUID());
                if (sect != null) {
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