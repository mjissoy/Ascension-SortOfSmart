package net.thejadeproject.ascension.common.command.cultivation;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.network.client_bound.entity_data.skills.SyncHeldSkills;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GiveSkillCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("skill")
                .requires(source -> source.hasPermission(2))

                /*
                    Commands:
                    /skill give <targets> <skill> [form]
                    /skill remove <targets> <skill> [form]
                                                            */

                .then(Commands.literal("give")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("skill", StringArgumentType.string())
                                        .suggests(GiveSkillCommand::suggestSkills)
                                        .executes(GiveSkillCommand::giveSkillToActiveForm)
                                        .then(Commands.argument("form", StringArgumentType.string())
                                                .suggests(GiveSkillCommand::suggestForms)
                                                .executes(GiveSkillCommand::giveSkillToSpecifiedForm)
                                        )
                                )
                        )
                )

                .then(Commands.literal("remove")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("skill", StringArgumentType.string())
                                        .suggests(GiveSkillCommand::suggestSkills)
                                        .executes(GiveSkillCommand::removeSkillFromActiveForm)
                                        .then(Commands.argument("form", StringArgumentType.string())
                                                .suggests(GiveSkillCommand::suggestForms)
                                                .executes(GiveSkillCommand::removeSkillFromSpecifiedForm)
                                        )
                                )
                        )
                )
        );
    }

    // give
    private static int giveSkillToActiveForm(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "targets");
        ResourceLocation skillId = normalizeId(StringArgumentType.getString(context, "skill"));

        if (!isValidSkill(skillId)) {
            context.getSource().sendFailure(Component.literal("Invalid skill: " + skillId));
            return 0;
        }

        int successCount = 0;

        for (ServerPlayer player : players) {
            IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);
            IEntityFormData activeFormData = entityData.getActiveFormData();

            if (activeFormData == null) {
                context.getSource().sendFailure(Component.literal(
                        player.getName().getString() + " has no active form."
                ));
                continue;
            }

            if (giveSkill(player, entityData, skillId, activeFormData.getEntityFormId(), context.getSource())) {
                successCount++;
            }
        }

        return successCount;
    }

    private static int giveSkillToSpecifiedForm(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "targets");

        ResourceLocation skillId = normalizeId(StringArgumentType.getString(context, "skill"));
        ResourceLocation formId = normalizeId(StringArgumentType.getString(context, "form"));

        if (!isValidSkill(skillId)) {
            context.getSource().sendFailure(Component.literal("Invalid skill: " + skillId));
            return 0;
        }

        int successCount = 0;

        for (ServerPlayer player : players) {
            IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);

            if (entityData.getEntityFormData(formId) == null) {
                context.getSource().sendFailure(Component.literal(
                        player.getName().getString() + " does not have form " + formId
                ));
                continue;
            }

            if (giveSkill(player, entityData, skillId, formId, context.getSource())) {
                successCount++;
            }
        }

        return successCount;
    }

    private static boolean giveSkill(ServerPlayer player,
                                     IEntityData entityData,
                                     ResourceLocation skillId,
                                     ResourceLocation formId,
                                     CommandSourceStack source) {
        try {
            IEntityFormData formData = entityData.getEntityFormData(formId);

            if (formData == null) {
                source.sendFailure(Component.literal(
                        player.getName().getString() + " does not have form " + formId
                ));
                return false;
            }

            if (formData.getHeldSkills().hasSkill(skillId)) {
                source.sendFailure(Component.literal(
                        player.getName().getString() + " already has skill " + skillId + " on form " + formId
                ));
                return false;
            }

            entityData.giveSkill(skillId, formId);

            syncHeldSkills(player, formId);

            source.sendSuccess(() -> Component.literal(
                    "Gave " + skillId + " to " + player.getName().getString() + " on form " + formId
            ), true);

            player.sendSystemMessage(Component.literal("You learned skill " + skillId));

            return true;

        } catch (Exception e) {
            source.sendFailure(Component.literal(
                    "Failed to give skill " + skillId + " to " + player.getName().getString()
                            + ": " + e.getMessage()
            ));
            return false;
        }
    }

    // remove
    private static int removeSkillFromActiveForm(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "targets");
        ResourceLocation skillId = normalizeId(StringArgumentType.getString(context, "skill"));

        if (!isValidSkill(skillId)) {
            context.getSource().sendFailure(Component.literal("Invalid skill: " + skillId));
            return 0;
        }

        int successCount = 0;

        for (ServerPlayer player : players) {
            IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);
            IEntityFormData activeFormData = entityData.getActiveFormData();

            if (activeFormData == null) {
                context.getSource().sendFailure(Component.literal(
                        player.getName().getString() + " has no active form."
                ));
                continue;
            }

            if (removeSkill(player, entityData, skillId, activeFormData.getEntityFormId(), context.getSource())) {
                successCount++;
            }
        }

        return successCount;
    }

    private static int removeSkillFromSpecifiedForm(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "targets");

        ResourceLocation skillId = normalizeId(StringArgumentType.getString(context, "skill"));
        ResourceLocation formId = normalizeId(StringArgumentType.getString(context, "form"));

        if (!isValidSkill(skillId)) {
            context.getSource().sendFailure(Component.literal("Invalid skill: " + skillId));
            return 0;
        }

        int successCount = 0;

        for (ServerPlayer player : players) {
            IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);

            if (entityData.getEntityFormData(formId) == null) {
                context.getSource().sendFailure(Component.literal(
                        player.getName().getString() + " does not have form " + formId
                ));
                continue;
            }

            if (removeSkill(player, entityData, skillId, formId, context.getSource())) {
                successCount++;
            }
        }

        return successCount;
    }

    private static boolean removeSkill(ServerPlayer player,
                                       IEntityData entityData,
                                       ResourceLocation skillId,
                                       ResourceLocation formId,
                                       CommandSourceStack source) {
        try {
            IEntityFormData formData = entityData.getEntityFormData(formId);

            if (formData == null) {
                source.sendFailure(Component.literal(
                        player.getName().getString() + " does not have form " + formId
                ));
                return false;
            }

            if (!formData.getHeldSkills().hasSkill(skillId)) {
                source.sendFailure(Component.literal(
                        player.getName().getString() + " does not have skill " + skillId + " on form " + formId
                ));
                return false;
            }

            entityData.removeSkill(skillId, formId);

            syncHeldSkills(player, formId);

            source.sendSuccess(() -> Component.literal(
                    "Removed " + skillId + " from " + player.getName().getString() + " on form " + formId
            ), true);

            player.sendSystemMessage(Component.literal("You forgot skill " + skillId));

            return true;

        } catch (Exception e) {
            source.sendFailure(Component.literal(
                    "Failed to remove skill " + skillId + " from " + player.getName().getString()
                            + ": " + e.getMessage()
            ));
            return false;
        }
    }

    private static void syncHeldSkills(ServerPlayer player, ResourceLocation formId) {
        IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);
        IEntityFormData formData = entityData.getEntityFormData(formId);

        if (formData == null) return;

        PacketDistributor.sendToPlayer(
                player,
                new SyncHeldSkills(formId.toString(), formData.getHeldSkills())
        );
    }

    private static CompletableFuture<Suggestions> suggestSkills(
            CommandContext<CommandSourceStack> context,
            SuggestionsBuilder builder
    ) {
        List<String> suggestions = new ArrayList<>();

        AscensionRegistries.Skills.SKILL_REGISTRY.keySet()
                .forEach(loc -> suggestions.add(loc.toString()));

        return SharedSuggestionProvider.suggest(suggestions, builder);
    }

    private static CompletableFuture<Suggestions> suggestForms(
            CommandContext<CommandSourceStack> context,
            SuggestionsBuilder builder
    ) {
        List<String> suggestions = new ArrayList<>();

        AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.keySet()
                .forEach(loc -> suggestions.add(loc.toString()));

        return SharedSuggestionProvider.suggest(suggestions, builder);
    }

    private static ResourceLocation normalizeId(String input) {
        try {
            if (input.contains(":")) {
                return ResourceLocation.parse(input);
            }

            return ResourceLocation.fromNamespaceAndPath("ascension", input);
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isValidSkill(ResourceLocation skillId) {
        if (skillId == null) return false;

        return AscensionRegistries.Skills.SKILL_REGISTRY.containsKey(
                ResourceKey.create(AscensionRegistries.Skills.SKILL_REGISTRY_KEY, skillId)
        );
    }
}