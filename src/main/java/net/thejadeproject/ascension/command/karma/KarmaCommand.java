package net.thejadeproject.ascension.command.karma;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.thejadeproject.ascension.events.karma.KarmaManager;
import net.thejadeproject.ascension.events.karma.KarmaRank;

public class KarmaCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("karma")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("get")
                        .executes(context -> getKarma(context, context.getSource().getPlayerOrException()))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(context -> getKarma(context, EntityArgument.getPlayer(context, "player")))))
                .then(Commands.literal("set")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("value", IntegerArgumentType.integer(-100, 100))
                                        .executes(context -> setKarma(
                                                context,
                                                EntityArgument.getPlayer(context, "player"),
                                                IntegerArgumentType.getInteger(context, "value"))))))
                .then(Commands.literal("add")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("value", IntegerArgumentType.integer())
                                        .executes(context -> addKarma(
                                                context,
                                                EntityArgument.getPlayer(context, "player"),
                                                IntegerArgumentType.getInteger(context, "value"))))))
                .then(Commands.literal("reset")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(context -> resetKarma(
                                        context,
                                        EntityArgument.getPlayer(context, "player")))))
                .then(Commands.literal("info")
                        .executes(context -> {
                            context.getSource().sendSuccess(() -> Component.literal(
                                    "§6Karma System Info:\n" +
                                            "§7Range: -100 to 100\n" +
                                            "§4Demonic: -100 to -34\n" +
                                            "§fNeutral: -33 to 33\n" +
                                            "§2Saint: 34 to 100\n" +
                                            "§7Kill villagers/animals: - karma\n" +
                                            "§7Kill monsters/bosses: + karma\n" +
                                            "§7Kill Saint players: -15 karma\n" +
                                            "§7Kill Demonic players: +10 karma"
                            ), false);
                            return 1;
                        })));
    }

    private static int getKarma(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        int karma = KarmaManager.getKarma(player);
        KarmaRank rank = KarmaManager.getKarmaRank(player);

        context.getSource().sendSuccess(() -> Component.literal(
                String.format("§7%s's Karma: §f%d §7(%s§7)",
                        player.getName().getString(),
                        karma,
                        rank.getChatColor() + rank.getId().toUpperCase())
        ), false);
        return 1;
    }

    private static int setKarma(CommandContext<CommandSourceStack> context, ServerPlayer player, int value) {
        int oldValue = KarmaManager.getKarma(player);
        KarmaManager.setKarma(player, value);
        context.getSource().sendSuccess(() -> Component.literal(
                String.format("§aSet %s's karma from §f%d §ato §f%d",
                        player.getName().getString(), oldValue, value)
        ), true);
        return 1;
    }

    private static int addKarma(CommandContext<CommandSourceStack> context, ServerPlayer player, int value) {
        int oldValue = KarmaManager.getKarma(player);
        KarmaManager.addKarma(player, value);
        int newValue = KarmaManager.getKarma(player);
        context.getSource().sendSuccess(() -> Component.literal(
                String.format("§aAdded §f%d §akarma to %s (§f%d → %d§a)",
                        value, player.getName().getString(), oldValue, newValue)
        ), true);
        return 1;
    }

    private static int resetKarma(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        KarmaManager.setKarma(player, 0);
        context.getSource().sendSuccess(() -> Component.literal(
                String.format("§aReset %s's karma to 0", player.getName().getString())
        ), true);
        return 1;
    }
}
