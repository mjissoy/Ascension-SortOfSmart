package net.thejadeproject.ascension.progression.skills.active_skills.physique_skills.kitsune_skills;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.data.CastType;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;
import net.thejadeproject.ascension.util.ModAttachments;
import org.joml.Vector3f;

import java.util.*;

public class FoxfireManipulationActiveSkill extends AbstractActiveSkill {

    private static final Map<UUID, FoxfireData> activeFoxfirePlayers = new HashMap<>();

    private static final DustParticleOptions FOXFIRE_BLUE = new DustParticleOptions(new Vector3f(0.3f, 0.4f, 1.0f), 1.0f);
    private static final DustParticleOptions FOXFIRE_PURPLE = new DustParticleOptions(new Vector3f(0.6f, 0.2f, 1.0f), 1.0f);
    private static final DustParticleOptions FOXFIRE_WHITE = new DustParticleOptions(new Vector3f(0.9f, 0.9f, 1.0f), 1.0f);

    private static class FoxfireData {
        List<FoxfireOrb> orbs = new ArrayList<>();
        int remainingTicks = 0;
        int maxOrbs = 3;
        double orbitRadius = 2.0;
        double orbitSpeed = 0.1;
    }

    private static class FoxfireOrb {
        double angle = 0;
        double heightOffset = 0;
        int attackCooldown = 0;
        boolean isActive = true;
    }

    public FoxfireManipulationActiveSkill() {
        super(Component.translatable("ascension.physique.active.foxfire_manipulation"));
        this.path = "ascension:essence";
        this.qiCost = 15.0;

        NeoForge.EVENT_BUS.addListener(this::onPlayerTick);
    }

    @Override
    public boolean isPrimarySkill() {
        return true;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public int getCooldown() {
        return 20 * 45;
    }

    @Override
    public int maxCastingTicks() {
        return 0;
    }

    @Override
    public ISkillData getSkillData() {
        return null;
    }

    @Override
    public ISkillData decode(RegistryFriendlyByteBuf buf) {
        return null;
    }

    @Override
    public void cast(int castingTicksElapsed, Level level, Player player) {
        if (level.isClientSide()) return;

        UUID playerId = player.getUUID();
        FoxfireData data = new FoxfireData();

        int essenceRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(this.path).majorRealm;

        data.remainingTicks = 20 * (30 + (essenceRealm * 5));

        data.maxOrbs = 3 + (essenceRealm / 2);

        for (int i = 0; i < data.maxOrbs; i++) {
            FoxfireOrb orb = new FoxfireOrb();
            orb.angle = (2 * Math.PI * i) / data.maxOrbs;
            orb.heightOffset = (level.random.nextDouble() - 0.5) * 1.0;
            data.orbs.add(orb);
        }

        activeFoxfirePlayers.put(playerId, data);

        spawnInitialFoxfire(level, player.position(), data.maxOrbs);
    }

    @Override
    public void onPreCast() {
    }

    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        UUID playerId = player.getUUID();
        FoxfireData data = activeFoxfirePlayers.get(playerId);

        if (data != null) {
            data.remainingTicks--;

            updateFoxfireOrbs(player, data);

            attackNearbyEnemies(player, data);

            spawnOrbitParticles(player.level(), player.position(), data);

            if (data.remainingTicks <= 0) {
                spawnFadeOutParticles(player.level(), player.position(), data.maxOrbs);
                activeFoxfirePlayers.remove(playerId);
            }
        }
    }

    private void updateFoxfireOrbs(Player player, FoxfireData data) {
        double time = player.level().getGameTime() * 0.05;

        for (int i = 0; i < data.orbs.size(); i++) {
            FoxfireOrb orb = data.orbs.get(i);

            orb.angle += data.orbitSpeed;
            if (orb.angle > 2 * Math.PI) orb.angle -= 2 * Math.PI;

            orb.heightOffset = Math.sin(time + i) * 0.5;

            if (orb.attackCooldown > 0) orb.attackCooldown--;
        }
    }

    private void attackNearbyEnemies(Player player, FoxfireData data) {
        if (player.level().isClientSide() || !(player.level() instanceof ServerLevel serverLevel)) return;

        AABB searchArea = player.getBoundingBox().inflate(10.0);
        List<LivingEntity> nearbyEnemies = player.level().getEntitiesOfClass(
                LivingEntity.class,
                searchArea,
                entity -> entity != player && entity.isAlive() && entity.isAttackable()
        );

        if (nearbyEnemies.isEmpty()) return;

        for (FoxfireOrb orb : data.orbs) {
            if (orb.attackCooldown <= 0 && orb.isActive) {
                LivingEntity target = null;
                double closestDistance = Double.MAX_VALUE;

                for (LivingEntity enemy : nearbyEnemies) {
                    double distance = enemy.distanceToSqr(player);
                    if (distance < closestDistance && distance < 100.0) {
                        closestDistance = distance;
                        target = enemy;
                    }
                }

                if (target != null) {
                    shootFoxfire(serverLevel, player, target, orb, data);
                    orb.attackCooldown = 20;
                }
            }
        }
    }

    private void shootFoxfire(ServerLevel serverLevel, Player player, LivingEntity target, FoxfireOrb orb, FoxfireData data) {
        double x = player.getX() + data.orbitRadius * Math.cos(orb.angle);
        double y = player.getY() + 1.5 + orb.heightOffset;
        double z = player.getZ() + data.orbitRadius * Math.sin(orb.angle);

        double dx = target.getX() - x;
        double dy = (target.getY() + target.getEyeHeight() / 2) - y;
        double dz = target.getZ() - z;
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (distance > 0) {
            dx /= distance;
            dy /= distance;
            dz /= distance;
        }

        for (int i = 0; i < 20; i++) {
            double progress = i / 20.0;
            double trailX = x + dx * distance * progress;
            double trailY = y + dy * distance * progress;
            double trailZ = z + dz * distance * progress;

            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                    trailX, trailY, trailZ,
                    1, 0, 0, 0, 0.05);

            if (i % 3 == 0) {
                DustParticleOptions color = serverLevel.random.nextBoolean() ? FOXFIRE_BLUE : FOXFIRE_PURPLE;
                serverLevel.sendParticles(color,
                        trailX, trailY, trailZ,
                        1, 0.02, 0.02, 0.02, 0.01);
            }
        }

        int essenceRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(this.path).majorRealm;
        float damage = 2.0f + (essenceRealm * 0.5f);

        target.hurt(player.damageSources().magic(), damage);

        target.setRemainingFireTicks(100);

        for (int i = 0; i < 15; i++) {
            double offsetX = (serverLevel.random.nextDouble() - 0.5) * 0.8;
            double offsetY = serverLevel.random.nextDouble() * 1.2;
            double offsetZ = (serverLevel.random.nextDouble() - 0.5) * 0.8;

            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                    target.getX() + offsetX, target.getY() + offsetY, target.getZ() + offsetZ,
                    1, 0, 0.1, 0, 0.1);
        }
    }

    private void spawnOrbitParticles(Level level, Vec3 position, FoxfireData data) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        for (FoxfireOrb orb : data.orbs) {
            double x = position.x + data.orbitRadius * Math.cos(orb.angle);
            double y = position.y + 1.5 + orb.heightOffset;
            double z = position.z + data.orbitRadius * Math.sin(orb.angle);

            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                    x, y, z,
                    1, 0, 0, 0, 0);

            if (level.random.nextInt(3) == 0) {
                DustParticleOptions color = FOXFIRE_PURPLE;
                serverLevel.sendParticles(color,
                        x, y, z,
                        1, 0.1, 0.1, 0.1, 0.05);
            }

            double trailAngle = orb.angle - 0.2;
            double trailX = position.x + data.orbitRadius * Math.cos(trailAngle);
            double trailY = position.y + 1.5 + orb.heightOffset;
            double trailZ = position.z + data.orbitRadius * Math.sin(trailAngle);

            serverLevel.sendParticles(ParticleTypes.ENCHANT,
                    trailX, trailY, trailZ,
                    1, 0, 0, 0, 0.02);
        }
    }

    private void spawnInitialFoxfire(Level level, Vec3 position, int orbCount) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        for (int i = 0; i < 50; i++) {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double radius = level.random.nextDouble() * 2.0;
            double x = position.x + radius * Math.cos(angle);
            double z = position.z + radius * Math.sin(angle);
            double y = position.y + level.random.nextDouble() * 2.0;

            DustParticleOptions color;
            double rand = level.random.nextDouble();
            if (rand < 0.33) color = FOXFIRE_BLUE;
            else if (rand < 0.66) color = FOXFIRE_PURPLE;
            else color = FOXFIRE_WHITE;

            serverLevel.sendParticles(color,
                    x, y, z,
                    1, 0, 0.1, 0, 0.1);
        }

        for (int i = 0; i < 360; i += 10) {
            double angle = Math.toRadians(i);
            double radius = 2.5;
            double x = position.x + radius * Math.cos(angle);
            double z = position.z + radius * Math.sin(angle);

            serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT,
                    x, position.y + 1.0, z,
                    1, 0, 0.2, 0, 0.05);
        }
    }

    private void spawnFadeOutParticles(Level level, Vec3 position, int orbCount) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        for (int i = 0; i < 30; i++) {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double radius = level.random.nextDouble() * 2.0;
            double x = position.x + radius * Math.cos(angle);
            double z = position.z + radius * Math.sin(angle);
            double y = position.y + level.random.nextDouble() * 2.0;

            serverLevel.sendParticles(ParticleTypes.CLOUD,
                    x, y, z,
                    1, 0, 0.05, 0, 0.05);

            if (i % 3 == 0) {
                serverLevel.sendParticles(ParticleTypes.WITCH,
                        x, y, z,
                        1, 0, 0, 0, 0.1);
            }
        }
    }

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        activeFoxfirePlayers.remove(player.getUUID());
    }

    public static void clearPlayerFoxfire(UUID playerId) {
        activeFoxfirePlayers.remove(playerId);
    }
}