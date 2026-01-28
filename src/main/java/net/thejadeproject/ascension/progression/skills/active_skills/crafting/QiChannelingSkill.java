package net.thejadeproject.ascension.progression.skills.active_skills.crafting;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.data.casting.CastType;
import net.thejadeproject.ascension.progression.skills.data.casting.ICastData;
import net.thejadeproject.ascension.recipe.ModRecipes;
import net.thejadeproject.ascension.recipe.QiChannelingRecipe;
import net.thejadeproject.ascension.recipe.QiChannelingRecipeInput;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class QiChannelingSkill extends AbstractActiveSkill {

    private static final Map<UUID, ChannelingSession> activeSessions = new HashMap<>();

    private static class ChannelingSession {
        QiChannelingRecipe recipe;

        ChannelingSession(QiChannelingRecipe recipe) {
            this.recipe = recipe;
        }
    }

    public QiChannelingSkill(String name) {
        super(Component.literal(name));
        this.path = "ascension:essence";

        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(this::onPlayerTick);
    }

    @Override
    public boolean isPrimarySkill() {
        return true;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public int maxCastingTicks() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean continueCasting(int castingTicksElapsed, Level level, Player player, ICastData castData) {
        System.out.println("CASTING SKILL");
        UUID playerId = player.getUUID();

        if (!activeSessions.containsKey(playerId)) {
            return false;
        }

        ChannelingSession session = activeSessions.get(playerId);

        // Check if cast should be cancelled
        if (!super.continueCasting(castingTicksElapsed, level, player,castData)) {
            cleanupSession(playerId);
            return false;
        }

        // Verify player is still holding the correct item in main hand
        QiChannelingRecipeInput input = new QiChannelingRecipeInput(player);
        if (!session.recipe.matches(input, level)) {
            if (input.getMainHandItem().isEmpty()) {
                player.displayClientMessage(
                        Component.literal("§cItem removed from main hand! Channeling cancelled!§r"),
                        true
                );
            } else {
                player.displayClientMessage(
                        Component.literal("§cMissing required qi or incorrect item!§r"),
                        true
                );
            }
            cleanupSession(playerId);
            return false;
        }

        // Check completion
        if (castingTicksElapsed >= session.recipe.getTicksRequired()) {
            completeChanneling(player, session.recipe);
            cleanupSession(playerId);
            return false;
        }

        // Show progress every second
        if (castingTicksElapsed % 20 == 0) {
            int progress = (int)((castingTicksElapsed / (float)session.recipe.getTicksRequired()) * 100);
            player.displayClientMessage(
                    Component.literal("§aChanneling... " + progress + "%"),
                    true
            );
        }

        return player.getData(ModAttachments.INPUT_STATES).isHeld("cast_skill_input");
    }

    @Override
    public void cast(int castingTicksElapsed, Level level, Player player, ICastData castData) {
        if (level.isClientSide()) return;
        if (castingTicksElapsed > 0) return; // Only handle initial cast

        UUID playerId = player.getUUID();
        PlayerData playerData = player.getData(ModAttachments.PLAYER_DATA);

        // Check if already channeling
        if (activeSessions.containsKey(playerId)) {
            player.displayClientMessage(
                    Component.literal("§cAlready channeling qi!§r"),
                    true
            );
            return;
        }

        // Check if skill is on cooldown using PlayerData system
        ResourceLocation skillId = AscensionRegistries.Skills.SKILL_REGISTRY.getKey(this);
        if (playerData.isSkillOnCooldown(skillId)) {
            return; // Message already displayed by isSkillOnCooldown
        }

        // Check main hand item
        ItemStack mainHandItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (mainHandItem.isEmpty()) {
            player.displayClientMessage(
                    Component.literal("§cYou must hold an item in your main hand to channel!§r"),
                    true
            );
            return;
        }

        QiChannelingRecipeInput input = new QiChannelingRecipeInput(player);

        // Extract recipe from RecipeHolder properly
        Optional<RecipeHolder<QiChannelingRecipe>> recipeHolder = level.getRecipeManager()
                .getRecipeFor(ModRecipes.QI_CHANNELING_TYPE.get(), input, level);

        if (recipeHolder.isEmpty()) {
            player.displayClientMessage(
                    Component.literal("§cNo valid recipe found for " + mainHandItem.getHoverName().getString() + "!§r"),
                    true
            );
            return;
        }

        QiChannelingRecipe recipe = recipeHolder.get().value();

        // Check qi cost
        if (playerData == null || playerData.getCurrentQi() < recipe.getRequiredQi()) {
            player.displayClientMessage(
                    Component.literal("§cNot enough qi! Required: " + String.format("%.1f", recipe.getRequiredQi())),
                    true
            );
            return;
        }

        // Start channeling
        activeSessions.put(playerId, new ChannelingSession(recipe));

        player.displayClientMessage(
                Component.literal("§aBeginning Qi Channeling: " + recipe.getOutput().getHoverName().getString()),
                true
        );
    }

    @Override
    public void onPreCast() {
        // No special pre-cast logic needed
    }

    @Override
    public int getCooldown() {
        return 0; // Cooldown is handled per-recipe
    }

    private void completeChanneling(Player player, QiChannelingRecipe recipe) {
        ServerPlayer serverPlayer = (ServerPlayer) player;
        PlayerData playerData = serverPlayer.getData(ModAttachments.PLAYER_DATA);

        // Consume qi
        playerData.tryConsumeQi(recipe.getRequiredQi());

        // Consume item from main hand
        QiChannelingRecipeInput input = new QiChannelingRecipeInput(player);
        input.consumeIngredients(recipe.getSizedIngredients());

        // Create output item with optional components
        ItemStack output = recipe.assemble(input, serverPlayer.registryAccess());

        // Give to player or drop if inventory full
        if (!player.getInventory().add(output)) {
            player.drop(output, false);
        }

        // Set cooldown based on recipe ticks - THIS USES YOUR EXISTING SYSTEM
        ResourceLocation skillId = AscensionRegistries.Skills.SKILL_REGISTRY.getKey(this);
        playerData.putSkillOnCooldown(skillId, recipe.getTicksRequired());

        player.displayClientMessage(
                Component.literal("§aQi Channeling complete! Obtained " + output.getHoverName().getString()),
                true
        );
    }

    private void cleanupSession(UUID playerId) {
        activeSessions.remove(playerId);
    }

    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        UUID playerId = player.getUUID();
        if (activeSessions.containsKey(playerId)) {
            if (!player.isAlive() || player.isLocalPlayer()) {
                cleanupSession(playerId);
            }
        }
    }
}