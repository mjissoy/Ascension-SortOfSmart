// StabilityCheckBreakthroughHandler.java
package net.thejadeproject.ascension.progression.breakthrough.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.network.clientBound.SyncPathDataPayload;
import net.thejadeproject.ascension.progression.breakthrough.IBreakthroughData;
import net.thejadeproject.ascension.progression.breakthrough.IBreakthroughHandler;
import net.thejadeproject.ascension.progression.techniques.ITechnique;

import java.util.concurrent.ThreadLocalRandom;

public class StabilityCheckBreakthroughHandler implements IBreakthroughHandler {
    private static final int TRIBULATION_START_MAJOR_REALM = 3;
    private static final float BASE_TRIBULATION_SURVIVAL_CHANCE = 0.7f;
    private static final float TRIBULATION_CHANCE_DECREASE_PER_REALM = 0.05f;
    private static final int BASE_LIGHTNING_COUNT = 3;
    private static final int LIGHTNING_INCREASE_PER_REALM = 2;

    @Override
    public IBreakthroughData getBreakthroughData(CompoundTag tag) {
        return null;
    }

    @Override
    public void attemptBreakthrough(Player player, String pathId, ITechnique technique) {
        if (player.level().isClientSide) return;

        CultivationData.PathData pathData = player.getData(net.thejadeproject.ascension.util.ModAttachments.PLAYER_DATA).getCultivationData().getPathData(pathId);
        double stability = technique.getStabilityHandler().getStability(pathData.stabilityCultivationTicks);
        double random = ThreadLocalRandom.current().nextDouble(0, 1);

        if (random <= stability) {
            // Check if this breakthrough requires tribulation
            if (shouldTriggerTribulation(pathData)) {
                triggerHeavenlyTribulation((ServerPlayer) player, pathId, pathData.majorRealm);
            } else {
                completeBreakthrough(player, pathId);
            }
        } else {
            failBreakthrough(player, pathId);
        }
    }

    @Override
    public void failBreakthrough(Player player, String pathId) {
        if (player.level().isClientSide) return;

        CultivationData.PathData data = player.getData(net.thejadeproject.ascension.util.ModAttachments.PLAYER_DATA).getCultivationData().getPathData(pathId);
        data.pathProgress = 0;
        data.stabilityCultivationTicks = 0;
        data.breakingThrough = false;
        PacketDistributor.sendToPlayer((ServerPlayer) player, SyncPathDataPayload.fromPathData(data));
    }

    @Override
    public void completeBreakthrough(Player player, String pathId) {
        if (player.level().isClientSide) return;

        // Call parent implementation
        IBreakthroughHandler.super.completeBreakthrough(player, pathId);

        // Spawn celebration particles
        if (player.level() instanceof ServerLevel serverLevel) {
            spawnBreakthroughParticles(serverLevel, player);
        }
    }

    private boolean shouldTriggerTribulation(CultivationData.PathData pathData) {
        // Tribulation only happens when going from minor realm 9 to next major realm
        // And only starting from 3rd major realm
        return pathData.minorRealm == 9 &&
                pathData.majorRealm >= TRIBULATION_START_MAJOR_REALM - 1;
    }

    private void triggerHeavenlyTribulation(ServerPlayer player, String pathId, int currentMajorRealm) {
        ServerLevel level = player.serverLevel();
        final float survivalChance = calculateTribulationSurvivalChance(currentMajorRealm);
        final float random = ThreadLocalRandom.current().nextFloat();

        // Start tribulation sequence
        player.displayClientMessage(
                net.minecraft.network.chat.Component.literal("§cHeavenly Tribulation Approaches!").withStyle(net.minecraft.ChatFormatting.BOLD, net.minecraft.ChatFormatting.RED),
                true
        );

        // Calculate number of lightning strikes based on realm
        final int lightningCount = BASE_LIGHTNING_COUNT +
                (currentMajorRealm - TRIBULATION_START_MAJOR_REALM + 1) * LIGHTNING_INCREASE_PER_REALM;

        // Start the tribulation sequence
        startTribulationSequence(player, pathId, currentMajorRealm, level, survivalChance, random, lightningCount, 0);
    }

    private void startTribulationSequence(ServerPlayer player, String pathId, int currentMajorRealm,
                                          ServerLevel level, float survivalChance, float random,
                                          int totalStrikes, int currentStrike) {
        if (currentStrike >= totalStrikes) {
            // All strikes completed, check result
            checkTribulationResult(player, pathId, survivalChance, random);
            return;
        }

        // Schedule this strike
        int delayTicks = currentStrike * 40; // 2 seconds between strikes

        // Use the server's executeLater method (if available) or schedule on main thread
        level.getServer().execute(() -> {
            // Wait for the delay
            try {
                Thread.sleep(delayTicks * 50); // 50ms per tick
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Execute on main thread
            level.getServer().execute(() -> {
                if (player.isAlive()) {
                    summonTribulationLightning(level, player, currentMajorRealm, currentStrike);
                }

                // Schedule next strike
                if (currentStrike + 1 < totalStrikes) {
                    startTribulationSequence(player, pathId, currentMajorRealm, level,
                            survivalChance, random, totalStrikes, currentStrike + 1);
                } else {
                    // After last strike, wait 1 second then check result
                    level.getServer().execute(() -> {
                        try {
                            Thread.sleep(1000); // 1 second
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }

                        level.getServer().execute(() -> {
                            checkTribulationResult(player, pathId, survivalChance, random);
                        });
                    });
                }
            });
        });
    }

    private void checkTribulationResult(ServerPlayer player, String pathId, float survivalChance, float random) {
        if (player.isAlive() && random <= survivalChance) {
            // Survived tribulation
            player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("§aYou have survived the Heavenly Tribulation!").withStyle(net.minecraft.ChatFormatting.BOLD, net.minecraft.ChatFormatting.GREEN),
                    true
            );
            // Complete the breakthrough
            IBreakthroughHandler.super.completeBreakthrough(player, pathId);
            if (player.level() instanceof ServerLevel serverLevel) {
                spawnBreakthroughParticles(serverLevel, player);
            }
        } else if (player.isAlive()) {
            // Failed tribulation but survived
            player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("§6You have failed the tribulation, but survived. Try again!").withStyle(net.minecraft.ChatFormatting.GOLD),
                    true
            );
            failBreakthrough(player, pathId);
        } else {
            // Player died during tribulation
            player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("§4You have perished in the Heavenly Tribulation!").withStyle(net.minecraft.ChatFormatting.BOLD, net.minecraft.ChatFormatting.DARK_RED),
                    false
            );
        }
    }

    private void summonTribulationLightning(ServerLevel level, ServerPlayer player, int realmLevel, int strikeIndex) {
        BlockPos playerPos = player.blockPosition();

        // Create a special lightning bolt for tribulation
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
        if (lightning != null) {
            // Position lightning near player with some randomness
            double offsetX = (ThreadLocalRandom.current().nextDouble() - 0.5) * 10;
            double offsetZ = (ThreadLocalRandom.current().nextDouble() - 0.5) * 10;
            lightning.moveTo(playerPos.getX() + offsetX, playerPos.getY(), playerPos.getZ() + offsetZ);

            // Make lightning more powerful based on realm
            lightning.setVisualOnly(false);

            // Add special effects
            level.addFreshEntity(lightning);

            // Create explosion based on realm level
            float explosionPower = 8.0f + (realmLevel - TRIBULATION_START_MAJOR_REALM) * 4f;
            level.explode(
                    null,
                    level.damageSources().lightningBolt(),
                    null,
                    playerPos.getX() + offsetX,
                    playerPos.getY(),
                    playerPos.getZ() + offsetZ,
                    explosionPower,
                    true,
                    net.minecraft.world.level.Level.ExplosionInteraction.BLOCK
            );

            // Play sound
            level.playSound(
                    null,
                    playerPos,
                    net.minecraft.sounds.SoundEvents.LIGHTNING_BOLT_THUNDER,
                    net.minecraft.sounds.SoundSource.WEATHER,
                    5.0f,
                    1.0f
            );
        }
    }

    private float calculateTribulationSurvivalChance(int currentMajorRealm) {
        // Higher realms = lower survival chance
        int realmOffset = currentMajorRealm - TRIBULATION_START_MAJOR_REALM + 1;
        float chance = BASE_TRIBULATION_SURVIVAL_CHANCE -
                (realmOffset * TRIBULATION_CHANCE_DECREASE_PER_REALM);
        return Math.max(0.1f, chance); // Minimum 10% chance
    }

    private void spawnBreakthroughParticles(ServerLevel level, Player player) {
        BlockPos pos = player.blockPosition();

        // Spawn celebration particles
        for (int i = 0; i < 50; i++) {
            double offsetX = (ThreadLocalRandom.current().nextDouble() - 0.5) * 2;
            double offsetY = ThreadLocalRandom.current().nextDouble() * 2;
            double offsetZ = (ThreadLocalRandom.current().nextDouble() - 0.5) * 2;

            level.sendParticles(
                    ParticleTypes.TOTEM_OF_UNDYING,
                    pos.getX() + 0.5,
                    pos.getY() + 1,
                    pos.getZ() + 0.5,
                    1,
                    offsetX,
                    offsetY,
                    offsetZ,
                    0.1
            );

            // Additional colorful particles
            level.sendParticles(
                    ParticleTypes.END_ROD,
                    pos.getX() + 0.5,
                    pos.getY() + 1,
                    pos.getZ() + 0.5,
                    1,
                    offsetX * 0.5,
                    offsetY * 0.5,
                    offsetZ * 0.5,
                    0.05
            );
        }

        // Play celebration sound
        level.playSound(
                null,
                pos,
                net.minecraft.sounds.SoundEvents.TOTEM_USE,
                net.minecraft.sounds.SoundSource.PLAYERS,
                1.0f,
                1.0f
        );
    }
}