package net.thejadeproject.ascension.command.cultivation;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.network.clientBound.SyncPathDataPayload;
import net.thejadeproject.ascension.progression.techniques.ITechnique;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttachments;

public class SetCultivationCommand {

    private static final String[] VALID_PATHS = {
            "ascension:body",
            "ascension:essence",
            "ascension:intent"
    };

    private static final String[] SIMPLE_PATH_NAMES = {
            "body",
            "essence",
            "intent",
    };

    @SubscribeEvent
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("cultivation")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("set")
                        .then(Commands.argument("target", EntityArgument.players())
                                .then(Commands.argument("path", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            // Suggest both simple names and full IDs
                                            return SharedSuggestionProvider.suggest(SIMPLE_PATH_NAMES, builder);
                                        })
                                        .then(Commands.argument("majorRealm", IntegerArgumentType.integer(0, 11))
                                                .then(Commands.argument("minorRealm", IntegerArgumentType.integer(0, 9))
                                                        .executes(SetCultivationCommand::setCultivationRealm)
                                                )
                                        )
                                )
                        )
                )
                .then(Commands.literal("get")
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(SetCultivationCommand::getCultivationInfo)
                        )
                )
        );
    }

    private static int setCultivationRealm(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var players = EntityArgument.getPlayers(context, "target");
        String inputPath = StringArgumentType.getString(context, "path");
        int majorRealm = IntegerArgumentType.getInteger(context, "majorRealm");
        int minorRealm = IntegerArgumentType.getInteger(context, "minorRealm");

        String normalizedPath = normalizePath(inputPath.toLowerCase());

        if (!isValidPath(normalizedPath)) {
            context.getSource().sendFailure(
                    Component.literal("Invalid path. Valid paths are: body, essence, intent")
            );
            return 0;
        }

        // Validate minor realm range
        if (minorRealm < 0 || minorRealm > 9) {
            context.getSource().sendFailure(
                    Component.literal("Minor realm must be between 0 and 9")
            );
            return 0;
        }

        // Validate major realm range
        if (majorRealm < 0 || majorRealm > 11) {
            context.getSource().sendFailure(
                    Component.literal("Major realm must be between 0 and 11")
            );
            return 0;
        }

        int successCount = 0;
        for (ServerPlayer player : players) {
            if (setPlayerCultivationRealm(player, normalizedPath, majorRealm, minorRealm, context.getSource())) {
                successCount++;
            }
        }

        return successCount;
    }

    private static int getCultivationInfo(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "target");
        PlayerData playerData = player.getData(ModAttachments.PLAYER_DATA);

        Component info = buildCultivationInfo(player, playerData);
        context.getSource().sendSuccess(() -> info, false);

        return 1;
    }

    private static String normalizePath(String input) {
        input = input.toLowerCase();

        if (input.contains(":")) {
            if (input.startsWith("ascension:")) {
                return input;
            }
            String[] parts = input.split(":");
            if (parts.length == 2) {
                String pathName = parts[1];
                if (isValidPathName(pathName)) {
                    return "ascension:" + pathName;
                }
            }
        }

        if (isValidPathName(input)) {
            return "ascension:" + input;
        }

        return input;
    }

    private static boolean isValidPathName(String pathName) {
        return pathName.equals("body") || pathName.equals("essence") || pathName.equals("intent");
    }

    private static boolean isValidPath(String path) {
        for (String validPath : VALID_PATHS) {
            if (validPath.equals(path)) {
                return true;
            }
        }
        return false;
    }

    private static boolean setPlayerCultivationRealm(ServerPlayer player, String pathId,
                                                     int newMajorRealm, int newMinorRealm,
                                                     CommandSourceStack source) {
        try {
            PlayerData playerData = player.getData(ModAttachments.PLAYER_DATA);
            CultivationData cultivationData = playerData.getCultivationData();
            CultivationData.PathData pathData = cultivationData.getPathData(pathId);

            int oldMajorRealm = pathData.majorRealm;
            int oldMinorRealm = pathData.minorRealm;
            String technique = pathData.technique;
            boolean wasCultivating = pathData.cultivating;
            boolean wasBreakingThrough = pathData.breakingThrough;

            RealmChangeEvent realmChangeEvent = new RealmChangeEvent(
                    player,
                    pathId,
                    oldMajorRealm,
                    newMajorRealm,
                    oldMinorRealm,
                    newMinorRealm,
                    pathData.stabilityCultivationTicks,
                    pathData.breakthroughData
            );

            net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(realmChangeEvent);

            pathData.majorRealm = newMajorRealm;
            pathData.minorRealm = newMinorRealm;

            if (oldMajorRealm != newMajorRealm || oldMinorRealm != newMinorRealm) {
                pathData.pathProgress = 0.0;
            }

            if (wasCultivating && (oldMajorRealm != newMajorRealm || oldMinorRealm != newMinorRealm)) {
                pathData.cultivating = false;
            }
            if (wasBreakingThrough && (oldMajorRealm != newMajorRealm || oldMinorRealm != newMinorRealm)) {
                pathData.breakingThrough = false;
                pathData.breakthroughData = null;
            }

            if (!technique.equals("ascension:none")) {
                ResourceLocation techniqueResource = ResourceLocation.bySeparator(technique, ':');
                ITechnique techniqueObj = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(techniqueResource);

                if (techniqueObj != null) {
                    techniqueObj.onTechniqueAcquisition(player);
                }
            }

            PacketDistributor.sendToPlayer(player, new SyncPathDataPayload(
                    pathId,
                    newMajorRealm,
                    newMinorRealm,
                    pathData.pathProgress,
                    technique,
                    (int) pathData.stabilityCultivationTicks
            ));

            String feedbackToSource = String.format(
                    "Set %s's %s cultivation to realm %d.%d (was %d.%d)",
                    player.getName().getString(),
                    getSimplePathName(pathId),
                    newMajorRealm,
                    newMinorRealm,
                    oldMajorRealm,
                    oldMinorRealm
            );

            source.sendSuccess(() -> Component.literal(feedbackToSource), true);
            if (source.getPlayer() != player) {
                String feedbackToPlayer = String.format(
                        "Your %s cultivation has been set to realm %d.%d",
                        getSimplePathName(pathId), newMajorRealm, newMinorRealm
                );
                player.sendSystemMessage(Component.literal(feedbackToPlayer));
            }

            return true;

        } catch (Exception e) {
            player.sendSystemMessage(
                    Component.literal("Error setting cultivation realm: " + e.getMessage())
            );
            source.sendFailure(
                    Component.literal("Failed to set cultivation for " + player.getName().getString())
            );
            return false;
        }
    }

    private static Component buildCultivationInfo(ServerPlayer player, PlayerData playerData) {
        StringBuilder info = new StringBuilder();
        info.append("=== Cultivation Info for ").append(player.getName().getString()).append(" ===\n");

        boolean hasData = false;
        for (CultivationData.PathData pathData : playerData.getCultivationData().getPaths()) {
            hasData = true;
            info.append("\n")
                    .append(getSimplePathName(pathData.pathId)).append(" Path\n")
                    .append("  Realm: ").append(pathData.majorRealm).append(".").append(pathData.minorRealm).append("\n")
                    .append("  Technique: ").append(getSimpleTechniqueName(pathData.technique)).append("\n")
                    .append("  Progress: ").append(String.format("%.2f", pathData.pathProgress)).append("\n")
                    .append("  Stability: ").append(String.format("%.1f", pathData.stabilityCultivationTicks)).append(" ticks\n")
                    .append("  Cultivating: ").append(pathData.cultivating).append("\n")
                    .append("  Breaking Through: ").append(pathData.breakingThrough).append("\n");
        }

        if (!hasData) {
            info.append("\nNo cultivation data found.\n");
        }

        info.append("\nCurrent Qi: ")
                .append(String.format("%.1f", playerData.getCurrentQi()))
                .append(" / ")
                .append(String.format("%.1f", playerData.getPlayerMaxQi()));

        return Component.literal(info.toString());
    }

    private static String getSimplePathName(String pathId) {
        if (pathId.contains(":")) {
            String[] parts = pathId.split(":");
            if (parts.length == 2) {
                String name = parts[1];
                return name.substring(0, 1).toUpperCase() + name.substring(1);
            }
        }
        return pathId;
    }

    private static String getSimpleTechniqueName(String techniqueId) {
        if (techniqueId.equals("ascension:none")) {
            return "None";
        }
        if (techniqueId.contains(":")) {
            String[] parts = techniqueId.split(":");
            if (parts.length == 2) {
                return parts[1];
            }
        }
        return techniqueId;
    }
}