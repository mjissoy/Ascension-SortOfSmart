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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysique;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;

import java.util.ArrayList;
import java.util.List;

public class SetCultivationCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("cultivation")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("set")
                        .then(Commands.argument("target", EntityArgument.players())
                                .then(Commands.argument("path", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            List<String> suggestions = new ArrayList<>();
                                            AscensionRegistries.Paths.PATHS_REGISTRY.keySet()
                                                    .forEach(loc -> suggestions.add(loc.toString()));
                                            return SharedSuggestionProvider.suggest(suggestions, builder);
                                        })
                                        .then(Commands.argument("majorRealm", IntegerArgumentType.integer(0, 11))
                                                .then(Commands.argument("minorRealm", IntegerArgumentType.integer(0, 9))
                                                        .executes(SetCultivationCommand::setCultivationRealm)
                                                        .then(Commands.argument("progress", IntegerArgumentType.integer(0, 100))
                                                                .executes(SetCultivationCommand::setCultivationRealm)
                                                        )
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
        String inputPath = StringArgumentType.getString(context, "path").toLowerCase();
        int majorRealm = IntegerArgumentType.getInteger(context, "majorRealm");
        int minorRealm = IntegerArgumentType.getInteger(context, "minorRealm");

        int progressPercent = -1;
        try {
            progressPercent = IntegerArgumentType.getInteger(context, "progress");
        } catch (IllegalArgumentException e) {
            // Not provided — fine
        }

        ResourceLocation pathId = normalizePath(inputPath);

        if (pathId == null || !isValidPath(pathId)) {
            context.getSource().sendFailure(
                    Component.literal("Invalid path '" + inputPath + "'. Use a registered path ID (e.g. ascension:essence).")
            );
            return 0;
        }

        int successCount = 0;
        for (ServerPlayer player : players) {
            if (setPlayerCultivationRealm(player, pathId, majorRealm, minorRealm, progressPercent, context.getSource())) {
                successCount++;
            }
        }
        return successCount;
    }

    private static int getCultivationInfo(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "target");
        var entityData = player.getData(ModAttachments.ENTITY_DATA);

        // Header
        context.getSource().sendSuccess(() ->
                Component.translatable("command.ascension.cultivation.info.header",
                        player.getName().getString()), false);

        // Physique display — get ID from registry key
        IPhysique physique = entityData.getPhysique();
        if (physique != null) {
            ResourceLocation physiqueId = AscensionRegistries.Physiques.PHSIQUES_REGISTRY.getKey(physique);
            if (physiqueId != null && !physiqueId.toString().equals("minecraft:none")) {
                Component physiqueName = Component.translatable(
                        "ascension.physiques." + physiqueId.getPath());
                context.getSource().sendSuccess(() ->
                        Component.translatable("command.ascension.cultivation.info.physique",
                                physiqueName), false);
            }
        }

        boolean hasAnyPath = false;

        // Iterate all registered paths — new paths automatically appear here
        for (var entry : AscensionRegistries.Paths.PATHS_REGISTRY.entrySet()) {
            ResourceLocation pathId = entry.getKey().location();
            PathData data = entityData.getPathData(pathId);

            if (data == null || data.getLastUsedTechnique() == null) continue;

            hasAnyPath = true;
            var path = entry.getValue();

            // Path name from lang (e.g., ascension.path.essence)
            Component pathName = Component.translatable(
                    "ascension.path." + pathId.getPath());

            // Realm name from the path's registered realm keys
            Component realmName = path.getMajorRealmName(data.getMajorRealm());

            // Technique name from lang (e.g., ascension.technique.basic_cultivation_technique)
            ResourceLocation techniqueId = data.getLastUsedTechnique();
            Component techniqueName = Component.translatable(
                    "ascension.technique." + techniqueId.getPath());

            // Path header: "Essence Path — Qi Condensation 1.3"
            context.getSource().sendSuccess(() ->
                    Component.translatable("command.ascension.cultivation.info.path_header",
                            pathName, realmName, data.getMajorRealm(), data.getMinorRealm()), false);

            // Technique
            context.getSource().sendSuccess(() ->
                    Component.translatable("command.ascension.cultivation.info.technique",
                            techniqueName), false);

            // Progress
            context.getSource().sendSuccess(() ->
                    Component.translatable("command.ascension.cultivation.info.progress",
                            String.format("%.2f", data.getCurrentRealmProgress())), false);

            // Status
            context.getSource().sendSuccess(() ->
                    Component.translatable(data.isCultivating()
                            ? "command.ascension.cultivation.info.cultivating.yes"
                            : "command.ascension.cultivation.info.cultivating.no"), false);
        }

        if (!hasAnyPath) {
            context.getSource().sendSuccess(() ->
                    Component.translatable("command.ascension.cultivation.info.no_paths"), false);
        }

        return 1;
    }

    /**
     * Normalizes path input:
     *   "essence"           -> ascension:essence
     *   "ascension:essence" -> ascension:essence
     *   "mymod:somepath"    -> mymod:somepath
     * Returns null if the input cannot be parsed as a ResourceLocation.
     */
    private static ResourceLocation normalizePath(String input) {
        try {
            if (input.contains(":")) {
                return ResourceLocation.parse(input);
            }
            return ResourceLocation.fromNamespaceAndPath("ascension", input);
        } catch (Exception e) {
            return null;
        }
    }

    /** Validates against the live registry so any registered path is accepted. */
    private static boolean isValidPath(ResourceLocation path) {
        return AscensionRegistries.Paths.PATHS_REGISTRY.containsKey(
                ResourceKey.create(AscensionRegistries.Paths.PATHS_REGISTRY_KEY, path)
        );
    }

    private static boolean setPlayerCultivationRealm(ServerPlayer player, ResourceLocation pathId,
                                                     int newMajorRealm, int newMinorRealm,
                                                     int progressPercent,
                                                     CommandSourceStack source) {
        try {
            PathData data = player.getData(ModAttachments.ENTITY_DATA).getPathData(pathId);

            if (data == null) {
                source.sendFailure(Component.literal(
                        player.getName().getString() + " has no data for path " + pathId));
                return false;
            }

            // handleRealmChange is a no-op when lastUsedTechnique is null —
            // warn the operator rather than silently doing nothing.
            if (data.getLastUsedTechnique() == null) {
                source.sendFailure(Component.literal(
                        player.getName().getString() + " has no technique set on path " + pathId
                                + ". Assign a technique first before setting realm."));
                return false;
            }

            // Capture old values before mutation for feedback message
            int oldMajor = data.getMajorRealm();
            int oldMinor = data.getMinorRealm();

            data.handleRealmChange(newMajorRealm, newMinorRealm,
                    player.getData(ModAttachments.ENTITY_DATA));

            // Set progress if provided — scaled against the technique's max Qi for the
            // realm the player actually ended up at (may be bounded by technique max)
            if (progressPercent >= 0) {
                var technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY
                        .get(data.getLastUsedTechnique());
                double maxQi = technique.getMaxQiForRealm(data.getMajorRealm(), data.getMinorRealm());
                data.setCurrentRealmProgress(maxQi * (progressPercent / 100.0));
            }

            String progressStr = (progressPercent >= 0)
                    ? String.format(" with %d%% progress", progressPercent) : "";

            String feedbackToSource = String.format(
                    "Set %s's %s cultivation to realm %d.%d (was %d.%d)%s",
                    player.getName().getString(),
                    pathId,
                    data.getMajorRealm(), data.getMinorRealm(),
                    oldMajor, oldMinor,
                    progressStr
            );
            source.sendSuccess(() -> Component.literal(feedbackToSource), true);

            if (source.getPlayer() != player) {
                String feedbackToPlayer = String.format(
                        "Your %s cultivation has been set to realm %d.%d%s",
                        pathId, data.getMajorRealm(), data.getMinorRealm(), progressStr
                );
                player.sendSystemMessage(Component.literal(feedbackToPlayer));
            }

            return true;

        } catch (Exception e) {
            source.sendFailure(Component.literal(
                    "Failed to set cultivation for " + player.getName().getString()
                            + ": " + e.getMessage()));
            return false;
        }
    }
}