// StabilityCheckBreakthroughHandler.java
package net.thejadeproject.ascension.progression.breakthrough.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.network.clientBound.*;
import net.thejadeproject.ascension.progression.breakthrough.IBreakthroughData;
import net.thejadeproject.ascension.progression.breakthrough.IBreakthroughHandler;
import net.thejadeproject.ascension.progression.techniques.ITechnique;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * Handles breakthroughs with Heavenly Tribulation.
 * Features multiphase trials, cinematic effects, and world-altering impacts.
 */
public class StabilityCheckBreakthroughHandler implements IBreakthroughHandler {
    // Tribulation Configuration Constants
    private static final int TRIBULATION_START_MAJOR_REALM = 3;
    private static final float BASE_TRIBULATION_SURVIVAL_CHANCE = 0.7f;
    private static final float TRIBULATION_CHANCE_DECREASE_PER_REALM = 0.05f;
    private static final int LIGHTNINGS_PER_WAVE = 3;
    private static final int STRIKES_PER_WAVE_INTERVAL = 100; // 5 seconds between strikes
    private static final int WAVE_INTERVAL_TICKS = 200; // 10 seconds between waves
    private static final float EVASION_DAMAGE_MULTIPLIER = 0.15f; // 15% more damage per evaded wave
    private static final int TRIBULATION_PREPARATION_TIME = 60; // 3 seconds preparation
    private static final int POST_TRIBULATION_WAIT = 20; // 1 second after final strike

    // Track tribulation state per player
    private static final java.util.Map<String, TribulationSession> activeTribulations = new java.util.concurrent.ConcurrentHashMap<>();

    static class TribulationSession {
        final String pathId;
        final int realmLevel;
        final float survivalChance;
        final float randomCheck;
        final int totalWaves;
        final int strikesPerWave;
        final boolean[] strikeResults;
        final boolean[] waveEvaded;
        float totalDamageTaken = 0f;
        boolean hasPerfectClear = true;
        boolean deathMessageSent = false;
        int evasionCount = 0;

        TribulationSession(String pathId, int realmLevel, float survivalChance, float randomCheck,
                           int totalWaves, int strikesPerWave) {
            this.pathId = pathId;
            this.realmLevel = realmLevel;
            this.survivalChance = survivalChance;
            this.randomCheck = randomCheck;
            this.totalWaves = totalWaves;
            this.strikesPerWave = strikesPerWave;
            this.strikeResults = new boolean[totalWaves * strikesPerWave];
            this.waveEvaded = new boolean[totalWaves];
        }

        int getWaveNumber(int strikeIndex) {
            return strikeIndex / strikesPerWave;
        }
    }

    @Override
    public IBreakthroughData getBreakthroughData(CompoundTag tag) {
        return new IBreakthroughData() {
            @Override
            public CompoundTag writeBreakthroughData() { return new CompoundTag(); }
            @Override
            public void readBreakthroughData(CompoundTag tag) {}
            @Override
            public void encode(RegistryFriendlyByteBuf buf) {}
            @Override
            public void decode(RegistryFriendlyByteBuf buf) {}
            @Override
            public CompoundTag serialize() { return new CompoundTag(); }
            @Override
            public void deserialize(CompoundTag tag) {}
        };
    }

    @Override
    public Supplier<IBreakthroughData> getBreakthroughDataInstance() {
        return () -> new IBreakthroughData() {
            @Override
            public CompoundTag writeBreakthroughData() { return new CompoundTag(); }
            @Override
            public void readBreakthroughData(CompoundTag tag) {}
            @Override
            public void encode(RegistryFriendlyByteBuf buf) {}
            @Override
            public void decode(RegistryFriendlyByteBuf buf) {}
            @Override
            public CompoundTag serialize() { return new CompoundTag(); }
            @Override
            public void deserialize(CompoundTag tag) {}
        };
    }

    @Override
    public void attemptBreakthrough(Player player, String pathId, ITechnique technique) {
        if (player.level().isClientSide) return;

        CultivationData.PathData pathData = player.getData(ModAttachments.PLAYER_DATA)
                .getCultivationData().getPathData(pathId);

        if (pathData == null) {
            failBreakthrough(player, pathId);
            return;
        }

        String sessionKey = player.getStringUUID() + ":" + pathId;
        if (activeTribulations.containsKey(sessionKey)) {
            return; // Prevent duplicate tribulations
        }

        double stability = technique.getStabilityHandler().getStability(pathData.stabilityCultivationTicks);
        double random = ThreadLocalRandom.current().nextDouble(0, 1);

        if (random <= stability) {
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

        CultivationData.PathData data = player.getData(ModAttachments.PLAYER_DATA)
                .getCultivationData().getPathData(pathId);

        if (data == null) return;

        data.pathProgress = 0;
        data.stabilityCultivationTicks = 0;
        data.breakingThrough = false;

        PacketDistributor.sendToPlayer((ServerPlayer) player,
                SyncPathDataPayload.fromPathData(data));

        activeTribulations.remove(player.getStringUUID() + ":" + pathId);
    }

    @Override
    public void completeBreakthrough(Player player, String pathId) {
        if (player.level().isClientSide) return;

        IBreakthroughHandler.super.completeBreakthrough(player, pathId);

        if (player.level() instanceof ServerLevel serverLevel) {
            spawnBreakthroughParticles(serverLevel, player);
            playTribulationSuccessSound(serverLevel, player);
        }

        String sessionKey = player.getStringUUID() + ":" + pathId;
        TribulationSession session = activeTribulations.get(sessionKey);
        if (session != null) {
            grantTribulationRewards((ServerPlayer) player, session);
        }

        activeTribulations.remove(sessionKey);
    }

    private boolean shouldTriggerTribulation(CultivationData.PathData pathData) {
        return pathData.minorRealm == 9 && pathData.majorRealm >= TRIBULATION_START_MAJOR_REALM - 1;
    }

    private void triggerHeavenlyTribulation(ServerPlayer player, String pathId, int currentMajorRealm) {
        ServerLevel level = player.serverLevel();

        final int upcomingRealm = currentMajorRealm + 1;
        final float survivalChance = calculateDynamicSurvivalChance(player, upcomingRealm);
        final float randomCheck = ThreadLocalRandom.current().nextFloat();

        // Calculate number of waves (1 wave at realm 3, +1 per realm)
        final int totalWaves = Math.max(1, upcomingRealm - TRIBULATION_START_MAJOR_REALM + 1);

        TribulationSession session = new TribulationSession(pathId, upcomingRealm,
                survivalChance, randomCheck, totalWaves, LIGHTNINGS_PER_WAVE);
        activeTribulations.put(player.getStringUUID() + ":" + pathId, session);

        level.getServer().tell(new net.minecraft.server.TickTask(
                level.getServer().getTickCount(),
                () -> {
                    prepareTribulationAtmosphere(player, level);
                }
        ));

        scheduleLightningStrikes(player, pathId, session);

        if (upcomingRealm >= 5) {
            scheduleInnerDemonPhase(player, pathId, upcomingRealm);
        }
    }

    private void prepareTribulationAtmosphere(ServerPlayer player, ServerLevel level) {
        BlockPos pos = player.blockPosition();

        level.setWeatherParameters(0, 1200, true, true);

        player.displayClientMessage(
                net.minecraft.network.chat.Component.translatable("tribulation.approaches"),
                true
        );

        level.playSound(null, pos, SoundEvents.LIGHTNING_BOLT_IMPACT,
                SoundSource.WEATHER, 15.0f, 0.5f);

        spawnCloudRing(level, pos, 35);

        // Ensure screen shake runs on main thread
        level.getServer().tell(new net.minecraft.server.TickTask(
                level.getServer().getTickCount(),
                () -> PacketDistributor.sendToPlayer(player, new ScreenShakePayload(TRIBULATION_PREPARATION_TIME, 0.8f))
        ));

        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,
                TRIBULATION_PREPARATION_TIME + 20, 2, false, false));
    }

    private void spawnCloudRing(ServerLevel level, BlockPos center, int radius) {
        for (int i = 0; i < 150; i++) {
            double angle = Math.PI * 2 * i / 150.0;
            double r = radius + ThreadLocalRandom.current().nextDouble() * 15;
            double x = center.getX() + Math.cos(angle) * r;
            double z = center.getZ() + Math.sin(angle) * r;
            double y = center.getY() + ThreadLocalRandom.current().nextInt(15, 35);

            level.sendParticles(ParticleTypes.POOF, x, y, z, 30, 4, 2, 4, 0.08);
        }
    }

    private void scheduleLightningStrikes(ServerPlayer player, String pathId, TribulationSession session) {
        ServerLevel level = player.serverLevel();

        // Schedule each lightning strike with proper wave timing
        for (int i = 0; i < session.totalWaves * session.strikesPerWave; i++) {
            final int strikeIndex = i;
            final int waveNumber = i / session.strikesPerWave;
            final int strikeInWave = i % session.strikesPerWave;

            int delayTicks = TRIBULATION_PREPARATION_TIME +
                    (waveNumber * WAVE_INTERVAL_TICKS) +
                    (strikeInWave * STRIKES_PER_WAVE_INTERVAL);

            level.getServer().tell(new net.minecraft.server.TickTask(
                    level.getServer().getTickCount() + delayTicks,
                    () -> {
                        if (!player.isAlive()) {
                            handlePlayerDeathDuringTribulation(player, pathId);
                            return;
                        }

                        String key = player.getStringUUID() + ":" + pathId;
                        if (!activeTribulations.containsKey(key)) {
                            return;
                        }

                        summonEnhancedLightning(level, player, session, strikeIndex);
                    }
            ));
        }

        // Schedule result check after all waves complete
        int finalDelay = TRIBULATION_PREPARATION_TIME +
                (session.totalWaves * WAVE_INTERVAL_TICKS) +
                ((session.strikesPerWave - 1) * STRIKES_PER_WAVE_INTERVAL) +
                POST_TRIBULATION_WAIT;

        level.getServer().tell(new net.minecraft.server.TickTask(
                level.getServer().getTickCount() + finalDelay,
                () -> checkTribulationResult(player, pathId, session)
        ));
    }

    private void summonEnhancedLightning(ServerLevel level, ServerPlayer player,
                                         TribulationSession session, int strikeIndex) {
        BlockPos playerPos = player.blockPosition();
        int realmLevel = session.realmLevel;
        int waveNumber = session.getWaveNumber(strikeIndex);

        // Calculate evasion multiplier
        float damageMultiplier = 1.0f;
        for (int i = 0; i < waveNumber; i++) {
            if (session.waveEvaded[i]) {
                damageMultiplier += EVASION_DAMAGE_MULTIPLIER;
            }
        }

        // Warning flash
        level.getServer().tell(new net.minecraft.server.TickTask(
                level.getServer().getTickCount() + 8,
                () -> {
                    level.sendParticles(ParticleTypes.FLASH,
                            playerPos.getX(), playerPos.getY() + 25, playerPos.getZ(),
                            1, 0, 0, 0, 0);
                    level.playSound(null, playerPos, SoundEvents.LIGHTNING_BOLT_IMPACT,
                            SoundSource.WEATHER, 8f, 0.5f);
                }
        ));

        // Main Lightning Strikes
        for (int bolt = 0; bolt < session.strikesPerWave; bolt++) {
            LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
            if (lightning == null) continue;

            double angle = Math.PI * 2 * bolt / session.strikesPerWave;
            double offsetX = Math.cos(angle) * 8 + (ThreadLocalRandom.current().nextDouble() - 0.5) * 5;
            double offsetZ = Math.sin(angle) * 8 + (ThreadLocalRandom.current().nextDouble() - 0.5) * 5;

            lightning.moveTo(playerPos.getX() + offsetX, playerPos.getY() + 30, playerPos.getZ() + offsetZ);
            lightning.setVisualOnly(false);
            level.addFreshEntity(lightning);

            if (bolt == 0) {
                Vec3 impactPos = new Vec3(playerPos.getX() + offsetX * 0.3, playerPos.getY(), playerPos.getZ() + offsetZ * 0.3);

                float explosionPower = (6.0f + (realmLevel - TRIBULATION_START_MAJOR_REALM + 1) * 3.5f) * damageMultiplier;

                createCircularExplosion(level, impactPos, explosionPower, realmLevel);

                // Shockwave ring
                for (int angleDeg = 0; angleDeg < 360; angleDeg += 8) {
                    double rad = Math.toRadians(angleDeg);
                    level.sendParticles(ParticleTypes.SONIC_BOOM,
                            impactPos.x + Math.cos(rad) * 3,
                            impactPos.y + 0.5,
                            impactPos.z + Math.sin(rad) * 3,
                            1, 0, 0, 0, 0
                    );
                }

                createScorchMark(level, BlockPos.containing(impactPos), realmLevel);
            }
        }

        float baseDamage = 5 + realmLevel * 2;
        float damage = (float)(baseDamage + ThreadLocalRandom.current().nextDouble() * 4) * damageMultiplier;
        player.hurt(level.damageSources().magic(), damage);

        boolean wasHit = damage > 0;
        session.totalDamageTaken += Math.min(damage, player.getHealth());
        if (wasHit) session.hasPerfectClear = false;
        session.strikeResults[strikeIndex] = player.getHealth() > 0;

        // Mark wave as evaded if no hits taken in this wave
        int waveStart = waveNumber * session.strikesPerWave;
        boolean anyHitInWave = false;
        for (int i = waveStart; i < waveStart + session.strikesPerWave; i++) {
            if (i < session.strikeResults.length && session.strikeResults[i]) {
                anyHitInWave = true;
                break;
            }
        }
        session.waveEvaded[waveNumber] = !anyHitInWave;

        level.playSound(null, playerPos, SoundEvents.LIGHTNING_BOLT_THUNDER,
                SoundSource.WEATHER, 12f, 0.8f + ThreadLocalRandom.current().nextFloat() * 0.4f);
    }

    private void createCircularExplosion(ServerLevel level, Vec3 center, float power, int realmLevel) {
        int radius = (int)(power * 0.8f);
        BlockPos centerPos = BlockPos.containing(center);

        for (BlockPos pos : BlockPos.betweenClosed(
                centerPos.offset(-radius, -radius/2, -radius),
                centerPos.offset(radius, radius/2, radius)
        )) {
            double distance = Math.sqrt(pos.distToCenterSqr(center.x, center.y, center.z));
            if (distance <= radius) {
                float damageFactor = (float)(1.0 - distance / radius);
                if (level.random.nextFloat() < damageFactor * 0.7f) {
                    BlockState state = level.getBlockState(pos);
                    if (!state.is(Blocks.BEDROCK)) {
                        level.destroyBlock(pos, false);
                    }
                }
            }
        }
    }

    private void createScorchMark(ServerLevel level, BlockPos center, int realmLevel) {
        int radius = 2 + realmLevel / 3;
        BlockState scorch = Blocks.OBSIDIAN.defaultBlockState();

        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-radius, -1, -radius),
                center.offset(radius, 1, radius)
        )) {
            if (pos.distSqr(center) <= radius * radius && level.getBlockState(pos).is(Blocks.STONE)) {
                level.setBlock(pos, scorch, 3);
            }
        }
    }

    private void scheduleInnerDemonPhase(ServerPlayer player, String pathId, int realmLevel) {
        ServerLevel level = player.serverLevel();
        // Schedule inner demons to start after first wave
        int delayTicks = TRIBULATION_PREPARATION_TIME + WAVE_INTERVAL_TICKS / 2;

        level.getServer().tell(new net.minecraft.server.TickTask(
                level.getServer().getTickCount() + delayTicks,
                () -> {
                    if (!player.isAlive()) return;

                    // Verify tribulation is still active
                    String key = player.getStringUUID() + ":" + pathId;
                    if (!activeTribulations.containsKey(key)) return;

                    player.displayClientMessage(
                            net.minecraft.network.chat.Component.translatable("tribulation.inner_demons"),
                            true
                    );

                    player.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, realmLevel - 3, false, true));
                    player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 0, false, true));
                    player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, false, true));

                    level.sendParticles(ParticleTypes.SOUL,
                            player.getX(), player.getY() + 1, player.getZ(),
                            250, 4, 4, 4, 0.06);
                }
        ));
    }

    private void checkTribulationResult(ServerPlayer player, String pathId, TribulationSession session) {
        String sessionKey = player.getStringUUID() + ":" + pathId;
        activeTribulations.remove(sessionKey);

        if (!player.isAlive()) {
            handlePlayerDeathDuringTribulation(player, pathId);
            return;
        }

        if (session.randomCheck <= session.survivalChance) {
            player.displayClientMessage(
                    net.minecraft.network.chat.Component.translatable("tribulation.success"),
                    true
            );

            broadcastTribulationSuccess(player);
            grantTribulationRewards(player, session);
            IBreakthroughHandler.super.completeBreakthrough(player, pathId);

            if (player.level() instanceof ServerLevel serverLevel) {
                spawnBreakthroughParticles(serverLevel, player);
                playTribulationSuccessSound(serverLevel, player);
                createSuccessAura(serverLevel, player.blockPosition());
            }
        } else {
            player.displayClientMessage(
                    net.minecraft.network.chat.Component.translatable("tribulation.failed"),
                    true
            );

            if (player.level() instanceof ServerLevel serverLevel) {
                createMinorScar(serverLevel, player.blockPosition(), session.realmLevel);
            }

            failBreakthrough(player, pathId);
        }
    }

    private void handlePlayerDeathDuringTribulation(ServerPlayer player, String pathId) {
        String sessionKey = player.getStringUUID() + ":" + pathId;
        TribulationSession session = activeTribulations.get(sessionKey);

        if (session != null && !session.deathMessageSent) {
            session.deathMessageSent = true;

            player.displayClientMessage(
                    net.minecraft.network.chat.Component.translatable("tribulation.death"),
                    false
            );

            if (player.level() instanceof ServerLevel serverLevel) {
                createPermanentTribulationScar(serverLevel, player.blockPosition(), pathId);
            }
        }

        activeTribulations.remove(sessionKey);
    }

    private void broadcastTribulationSuccess(ServerPlayer player) {
        // FIXED: Anonymous broadcast without player name
        for (ServerPlayer onlinePlayer : player.getServer().getPlayerList().getPlayers()) {
            onlinePlayer.displayClientMessage(
                    net.minecraft.network.chat.Component.translatable("tribulation.broadcast_success"),
                    false
            );
        }
    }

    private void grantTribulationRewards(ServerPlayer player, TribulationSession session) {
        if (session.hasPerfectClear && session.totalDamageTaken == 0) {
            player.displayClientMessage(
                    net.minecraft.network.chat.Component.translatable("tribulation.perfect_clear"),
                    true
            );

            player.getData(ModAttachments.PLAYER_DATA).getCultivationData()
                    .addDaoComprehension(session.pathId, session.realmLevel * 15.0);
        } else if (session.hasPerfectClear) {
            player.getData(ModAttachments.PLAYER_DATA).getCultivationData()
                    .addDaoComprehension(session.pathId, session.realmLevel * 8.0);
        }

        player.getData(ModAttachments.PLAYER_DATA).addTribulationMark(session.realmLevel);
    }

    private void createSuccessAura(ServerLevel level, BlockPos pos) {
        for (int y = 0; y < 50; y++) {
            level.sendParticles(ParticleTypes.END_ROD,
                    pos.getX() + 0.5, pos.getY() + y, pos.getZ() + 0.5,
                    5, 0.1, 0.1, 0.1, 0.02);
        }

        level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                pos.getX() + 0.5, pos.getY() + 20, pos.getZ() + 0.5,
                200, 3, 3, 3, 0.1);
    }

    private void createMinorScar(ServerLevel level, BlockPos center, int realmLevel) {
        int radius = 5 + realmLevel;
        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-radius, -3, -radius),
                center.offset(radius, 1, radius)
        )) {
            if (pos.distSqr(center) < radius * radius && level.random.nextBoolean()) {
                if (level.getBlockState(pos).is(Blocks.GRASS_BLOCK)) {
                    level.setBlock(pos, Blocks.COARSE_DIRT.defaultBlockState(), 3);
                } else if (level.getBlockState(pos).is(Blocks.STONE)) {
                    level.setBlock(pos, Blocks.GRAVEL.defaultBlockState(), 3);
                }
            }
        }
    }

    private void createPermanentTribulationScar(ServerLevel level, BlockPos center, String pathId) {
        int radius = 15;
        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-radius, -8, -radius),
                center.offset(radius, 3, radius)
        )) {
            if (pos.distSqr(center) < radius * radius) {
                if (level.getBlockState(pos).is(Blocks.GRASS_BLOCK) ||
                        level.getBlockState(pos).is(Blocks.DIRT)) {
                    level.setBlock(pos, Blocks.SOUL_SAND.defaultBlockState(), 3);
                } else if (level.getBlockState(pos).is(Blocks.STONE)) {
                    level.setBlock(pos, Blocks.MAGMA_BLOCK.defaultBlockState(), 3);
                }
            }
        }
    }

    private float calculateDynamicSurvivalChance(ServerPlayer player, int realmLevel) {
        float chance = BASE_TRIBULATION_SURVIVAL_CHANCE;
        int realmOffset = realmLevel - TRIBULATION_START_MAJOR_REALM + 1;

        chance -= (realmOffset * TRIBULATION_CHANCE_DECREASE_PER_REALM);

        if (isArrayFormationPresent(player)) {
            chance += 0.10f;
        }

        CultivationData.PathData pathData = player.getData(ModAttachments.PLAYER_DATA)
                .getCultivationData().getPathData("main");
        if (pathData != null) {
            double stability = (double)pathData.stabilityCultivationTicks / 10000.0;
            chance += (float)(stability * 0.05);
        }

        return Math.max(0.05f, Math.min(0.95f, chance));
    }

    private boolean isArrayFormationPresent(ServerPlayer player) {
        return false;
    }

    private void spawnBreakthroughParticles(ServerLevel level, Player player) {
        BlockPos pos = player.blockPosition();

        for (int i = 0; i < 80; i++) {
            double offsetX = (ThreadLocalRandom.current().nextDouble() - 0.5) * 3;
            double offsetY = ThreadLocalRandom.current().nextDouble() * 3;
            double offsetZ = (ThreadLocalRandom.current().nextDouble() - 0.5) * 3;

            level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                    pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                    1, offsetX, offsetY, offsetZ, 0.12);

            if (i % 3 == 0) {
                level.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                        pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                        2, offsetX * 0.5, offsetY * 0.5, offsetZ * 0.5, 0.08);
            }
        }

        for (int angle = 0; angle < 360; angle += 12) {
            double rad = Math.toRadians(angle);
            level.sendParticles(ParticleTypes.END_ROD,
                    pos.getX() + Math.cos(rad) * 2,
                    pos.getY() + 0.5,
                    pos.getZ() + Math.sin(rad) * 2,
                    1, 0, 0, 0, 0);
        }
    }

    private void playTribulationSuccessSound(ServerLevel level, Player player) {
        BlockPos pos = player.blockPosition();

        level.playSound(null, pos, SoundEvents.TOTEM_USE,
                SoundSource.PLAYERS, 1.5f, 1.0f);

        level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE,
                SoundSource.PLAYERS, 1.0f, 1.2f);

        level.playSound(null, pos, SoundEvents.LIGHTNING_BOLT_THUNDER,
                SoundSource.PLAYERS, 1.0f, 1.0f);
    }
}