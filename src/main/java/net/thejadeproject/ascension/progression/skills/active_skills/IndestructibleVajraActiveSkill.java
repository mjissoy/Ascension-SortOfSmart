package net.thejadeproject.ascension.progression.skills.active_skills;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.data.casting.CastType;
import net.thejadeproject.ascension.progression.skills.data.IPersistentSkillData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;


import net.thejadeproject.ascension.data_attachments.ModAttachments;
import org.joml.Vector3f;

import java.util.*;

public class IndestructibleVajraActiveSkill extends AbstractActiveSkill {

    private static final Map<UUID, VajraData> activeVajraPlayers = new HashMap<>();

    // Golden color for particles (RGB: 1.0, 0.8, 0.0)
    private static final DustParticleOptions GOLDEN_DUST = new DustParticleOptions(new Vector3f(1.0f, 0.8f, 0.0f), 1.0f);
    private static final DustParticleOptions GOLDEN_ORANGE_DUST = new DustParticleOptions(new Vector3f(1.0f, 0.6f, 0.1f), 1.0f);
    private static final DustParticleOptions GOLDEN_YELLOW_DUST = new DustParticleOptions(new Vector3f(1.0f, 1.0f, 0.3f), 1.0f);

    private static class VajraData {
        float storedDamage = 0;
        int remainingTicks = 0;
        List<UUID> damageSources = new ArrayList<>();
        boolean hasReleased = false; // Prevent multiple releases
        int particleTick = 0; // Track particle animation
    }

    public IndestructibleVajraActiveSkill() {
        super(Component.translatable("ascension.physique.active.indestructible_vajra"));
        this.path = "ascension:body";
        this.qiCost = 10.0;

        NeoForge.EVENT_BUS.addListener(this::onLivingDamageEvent);
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
        return 20 * 120;
    }

    @Override
    public int maxCastingTicks() {
        return 0;
    }




    @Override
    public void cast(int castingTicksElapsed, Level level, Player player) {
        if (level.isClientSide()) return;

        UUID playerId = player.getUUID();
        VajraData data = new VajraData();

        //Duration 5 Sec + 1 sec per major realm
        int majorRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(this.path).majorRealm;
        data.remainingTicks = 20 * (5 + majorRealm);

        activeVajraPlayers.put(playerId, data);

        // Don't set invulnerableTime - it's managed by Minecraft
        // We'll handle damage cancellation in onLivingDamageEvent

        // Maybe remove all neg effects on skill use
        // player.removeAllEffects();

        // Initial burst of particles on cast
        spawnInitialParticles(level, player.position());
    }

    @Override
    public void onPreCast() {

    }

    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        UUID playerId = player.getUUID();
        VajraData data = activeVajraPlayers.get(playerId);

        if (data != null) {
            // Decrement ticks regardless of damage
            data.remainingTicks--;

            // Spawn golden bell particles around the player
            spawnGoldenBellParticles(player.level(), player.position());

            data.particleTick++;

            if (data.remainingTicks <= 0 && !data.hasReleased) {
                releaseShockwave(player, data);
                data.hasReleased = true;
                // Remove after a short delay to ensure shockwave releases
                activeVajraPlayers.remove(playerId);
            }
        }
    }

    public void onLivingDamageEvent(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {
            UUID playerId = player.getUUID();
            VajraData data = activeVajraPlayers.get(playerId);

            if (data != null && !data.hasReleased) {
                // Store Data for later reflection
                data.storedDamage += event.getOriginalDamage();

                // Record DMG Source
                Entity source = event.getSource().getEntity();
                if (source != null && !data.damageSources.contains(source.getUUID())) {
                    data.damageSources.add(source.getUUID());
                }

                // Cancel all damage while skill is active - use setNewDamage(0.0F) in NeoForge
                event.setNewDamage(0.0F);

                // Spawn particles when damage is absorbed
                spawnDamageAbsorbParticles(player.level(), player.position());
            }
        }
    }

    private void spawnInitialParticles(Level level, Vec3 position) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        // Create a shimmering effect when the skill starts - bell forming
        for (int ring = 0; ring <= 4; ring++) {
            double height = ring * 0.5; // From ground up
            double radius = calculateBellRadius(height, 0, 2.2);

            for (int i = 0; i < 360; i += 15) {
                double angle = Math.toRadians(i);
                double x = position.x + radius * Math.cos(angle);
                double z = position.z + radius * Math.sin(angle);

                serverLevel.sendParticles(GOLDEN_DUST,
                        x, position.y + height, z,
                        1, 0, 0.05, 0, 0);
            }
        }

        // Golden flash effect
        for (int i = 0; i < 30; i++) {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double radius = level.random.nextDouble() * 2.0;
            double x = position.x + radius * Math.cos(angle);
            double z = position.z + radius * Math.sin(angle);
            double y = position.y + level.random.nextDouble() * 2.5;

            serverLevel.sendParticles(ParticleTypes.FLAME,
                    x, y, z,
                    1, 0, 0.05, 0, 0);
        }
    }

    private double calculateBellRadius(double height, double minHeight, double maxHeight) {
        // Chinese bell shape: narrower at top, widest in middle, slightly narrower at bottom
        double normalizedHeight = (height - minHeight) / (maxHeight - minHeight);

        // Bell curve shape
        if (normalizedHeight < 0.3) {
            // Bottom part: start narrow, widen
            return 1.5 + 0.5 * (normalizedHeight / 0.3);
        } else if (normalizedHeight < 0.7) {
            // Middle: widest part
            return 2.0;
        } else {
            // Top: narrow towards the top
            return 2.0 - 0.8 * ((normalizedHeight - 0.7) / 0.3);
        }
    }

    private void spawnGoldenBellParticles(Level level, Vec3 position) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        // Create a Chinese Golden Bell shape around the player

        // Horizontal rings (from bottom to top) - alternating between two golden shades
        for (double height = 0; height <= 2.2; height += 0.3) {
            double radius = calculateBellRadius(height, 0, 2.2);
            DustParticleOptions ringColor = (height % 0.6 < 0.3) ? GOLDEN_DUST : GOLDEN_ORANGE_DUST;

            for (int i = 0; i < 360; i += 20) {
                double angle = Math.toRadians(i);
                double x = position.x + radius * Math.cos(angle);
                double z = position.z + radius * Math.sin(angle);

                serverLevel.sendParticles(ringColor,
                        x, position.y + height, z,
                        1, 0, 0, 0, 0);
            }
        }

        // Vertical meridians (lines from bottom to top) - golden yellow for highlights
        for (int meridian = 0; meridian < 12; meridian++) {
            double angle = Math.toRadians(meridian * 30);

            for (double height = 0; height <= 2.2; height += 0.15) {
                double radius = calculateBellRadius(height, 0, 2.2);
                double x = position.x + radius * Math.cos(angle);
                double z = position.z + radius * Math.sin(angle);

                serverLevel.sendParticles(GOLDEN_YELLOW_DUST,
                        x, position.y + height, z,
                        1, 0, 0, 0, 0);
            }
        }

        // Top of the bell (rounded dome) - use flame particles for a glowing effect
        for (double phi = 0; phi <= Math.PI / 2; phi += 0.2) {
            double topRadius = 0.8 * Math.sin(phi);
            double yOffset = 2.2 + 0.6 * Math.cos(phi);

            for (int i = 0; i < 360; i += 30) {
                double angle = Math.toRadians(i);
                double x = position.x + topRadius * Math.cos(angle);
                double z = position.z + topRadius * Math.sin(angle);

                serverLevel.sendParticles(ParticleTypes.FLAME,
                        x, position.y + yOffset, z,
                        1, 0, 0, 0, 0);
            }
        }

        // Bottom rim (slightly flared) - use fire particles for a molten gold look
        for (int i = 0; i < 360; i += 15) {
            double angle = Math.toRadians(i);
            double radius = 2.2; // Widest at bottom rim
            double x = position.x + radius * Math.cos(angle);
            double z = position.z + radius * Math.sin(angle);

            serverLevel.sendParticles(ParticleTypes.FLAME,
                    x, position.y - 0.1, z,
                    1, 0, 0, 0, 0);
        }

        // Chinese characters or patterns on the bell (simplified as lines) - use fire particles
        if (level.random.nextInt(5) == 0) {
            // Random decorative flashes
            double patternAngle = Math.toRadians(level.random.nextDouble() * 360);
            double patternHeight = level.random.nextDouble() * 2.0;
            double patternRadius = calculateBellRadius(patternHeight, 0, 2.2);
            double x = position.x + patternRadius * Math.cos(patternAngle);
            double z = position.z + patternRadius * Math.sin(patternAngle);

            serverLevel.sendParticles(ParticleTypes.FIREWORK,
                    x, position.y + patternHeight, z,
                    1, 0, 0.05, 0, 0.02);
        }

        // Floating golden particles inside the bell (subtle shimmer) - use golden dust
        for (int i = 0; i < 3; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * 1.5;
            double offsetZ = (level.random.nextDouble() - 0.5) * 1.5;
            double offsetY = level.random.nextDouble() * 1.8;

            DustParticleOptions shimmerColor = level.random.nextBoolean() ? GOLDEN_DUST : GOLDEN_YELLOW_DUST;
            serverLevel.sendParticles(shimmerColor,
                    position.x + offsetX, position.y + offsetY, position.z + offsetZ,
                    1, 0.02, 0.02, 0.02, 0.01);
        }
    }

    private void spawnDamageAbsorbParticles(Level level, Vec3 position) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        // Burst of particles when damage is absorbed - bell vibrates
        for (int i = 0; i < 12; i++) {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double radius = level.random.nextDouble() * 1.5 + 0.5;
            double x = position.x + radius * Math.cos(angle);
            double z = position.z + radius * Math.sin(angle);
            double y = position.y + level.random.nextDouble() * 2.2;

            // Vibrating effect - particles move outward
            double dx = (x - position.x) * 0.1;
            double dz = (z - position.z) * 0.1;

            DustParticleOptions color = level.random.nextBoolean() ? GOLDEN_DUST : GOLDEN_YELLOW_DUST;
            serverLevel.sendParticles(color,
                    x, y, z,
                    1, dx, 0, dz, 0.05);
        }

        // Create ripples on the bell surface
        for (int meridian = 0; meridian < 4; meridian++) {
            double angle = Math.toRadians(meridian * 90);

            for (double height = 0.5; height <= 1.8; height += 0.3) {
                double radius = calculateBellRadius(height, 0, 2.2);
                double x = position.x + radius * Math.cos(angle);
                double z = position.z + radius * Math.sin(angle);

                serverLevel.sendParticles(ParticleTypes.FLAME,
                        x, position.y + height, z,
                        1, 0, 0.05, 0, 0.02);
            }
        }

        // Small firework burst at the point of impact
        for (int i = 0; i < 5; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * 0.5;
            double offsetZ = (level.random.nextDouble() - 0.5) * 0.5;
            double offsetY = level.random.nextDouble() * 2.0;

            serverLevel.sendParticles(ParticleTypes.FIREWORK,
                    position.x + offsetX, position.y + offsetY, position.z + offsetZ,
                    1, 0, 0, 0, 0.1);
        }
    }

    private void releaseShockwave(Player player, VajraData data) {
        Level level = player.level();
        if (level.isClientSide()) return;

        // Shockwave radius base 5 blocks + 1 per major realm
        int majorRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(this.path).majorRealm;
        float radius = 5.0f + majorRealm;

        AABB aabb = player.getBoundingBox().inflate(radius);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb,
                livingEntity -> livingEntity != player && livingEntity.isAlive());

        // Calc dmg stored: stored dmg * multiplier
        float shockwaveDamage = data.storedDamage * 0.5f;

        // Apply minimum damage so skill isn't useless without taking damage
        if (shockwaveDamage < 2.0f) {
            shockwaveDamage = 2.0f;
        }

        // Spawn bell shattering effect
        if (level instanceof ServerLevel serverLevel) {
            spawnBellShatterEffect(serverLevel, player.position());
            spawnShockwaveParticles(serverLevel, player.position(), radius);
        }

        for (LivingEntity entity : entities) {
            // Damage falls off with distance
            double distance = entity.distanceTo(player);
            if (distance <= radius) {
                float distanceMultiplier = 1.0f - (float)(distance / radius);
                float finalDamage = shockwaveDamage * distanceMultiplier;

                // Apply damage
                entity.hurt(player.damageSources().magic(), finalDamage);

                // Knock away from player
                double dx = entity.getX() - player.getX();
                double dz = entity.getZ() - player.getZ();
                double distance2D = Math.sqrt(dx * dx + dz * dz);
                if (distance2D > 0) {
                    double strength = (finalDamage / 5.0) * distanceMultiplier;
                    entity.setDeltaMovement(
                            (dx / distance2D) * strength,
                            0.3 * strength,
                            (dz / distance2D) * strength
                    );
                    entity.hurtMarked = true;
                }

                // Spawn impact particles on hit entities
                if (level instanceof ServerLevel serverLevel) {
                    spawnImpactParticles(serverLevel, entity.position());
                }
            }
        }

        // Reset data
        data.storedDamage = 0;
        data.damageSources.clear();
    }

    private void spawnBellShatterEffect(ServerLevel serverLevel, Vec3 center) {
        // Bell shatters into golden pieces
        for (int piece = 0; piece < 40; piece++) {
            double angle = serverLevel.random.nextDouble() * Math.PI * 2;
            double height = serverLevel.random.nextDouble() * 2.5;
            double radius = calculateBellRadius(height, 0, 2.2);

            double x = center.x + radius * Math.cos(angle);
            double z = center.z + radius * Math.sin(angle);
            double y = center.y + height;

            // Calculate outward velocity
            double dx = (x - center.x) * 0.3;
            double dy = serverLevel.random.nextDouble() * 0.2;
            double dz = (z - center.z) * 0.3;

            DustParticleOptions color = serverLevel.random.nextBoolean() ? GOLDEN_DUST : GOLDEN_YELLOW_DUST;
            serverLevel.sendParticles(color,
                    x, y, z,
                    1, dx, dy, dz, 0.1);
        }

        // Firework explosion at the top of the bell
        for (int i = 0; i < 20; i++) {
            double angle = serverLevel.random.nextDouble() * Math.PI * 2;
            double distance = serverLevel.random.nextDouble() * 0.5;
            double x = center.x + distance * Math.cos(angle);
            double z = center.z + distance * Math.sin(angle);
            double y = center.y + 2.5 + serverLevel.random.nextDouble() * 0.5;

            serverLevel.sendParticles(ParticleTypes.FIREWORK,
                    x, y, z,
                    1, 0, 0, 0, 0.2);
        }
    }

    private void spawnShockwaveParticles(ServerLevel serverLevel, Vec3 center, float radius) {
        // Create expanding rings of golden particles from the shattered bell
        for (int ring = 0; ring <= 6; ring++) {
            double currentRadius = radius * (ring / 6.0);

            for (int i = 0; i < 360; i += 20) {
                double angle = Math.toRadians(i);
                double x = center.x + currentRadius * Math.cos(angle);
                double z = center.z + currentRadius * Math.sin(angle);

                // Golden particles for the shockwave at multiple heights
                for (double yOffset = 0; yOffset <= 2.0; yOffset += 0.5) {
                    DustParticleOptions color = (ring % 2 == 0) ? GOLDEN_DUST : GOLDEN_YELLOW_DUST;
                    serverLevel.sendParticles(color,
                            x, center.y + yOffset, z,
                            1, 0, 0, 0, 0);
                }

                // Add some flame particles for intensity
                if (ring % 2 == 0) {
                    serverLevel.sendParticles(ParticleTypes.FLAME,
                            x, center.y + 0.5, z,
                            1, 0, 0.1, 0, 0.03);
                }
            }
        }

        // Central burst - the bell's energy release
        for (int i = 0; i < 50; i++) {
            double angle = serverLevel.random.nextDouble() * Math.PI * 2;
            double distance = serverLevel.random.nextDouble() * 1.0;
            double x = center.x + distance * Math.cos(angle);
            double z = center.z + distance * Math.sin(angle);
            double y = center.y + serverLevel.random.nextDouble() * 2.0;

            serverLevel.sendParticles(ParticleTypes.FIREWORK,
                    x, y, z,
                    1, 0, 0, 0, 0.15);
        }
    }

    private void spawnImpactParticles(ServerLevel serverLevel, Vec3 position) {
        for (int i = 0; i < 6; i++) {
            double offsetX = (serverLevel.random.nextDouble() - 0.5) * 0.8;
            double offsetY = serverLevel.random.nextDouble() * 1.2;
            double offsetZ = (serverLevel.random.nextDouble() - 0.5) * 0.8;

            serverLevel.sendParticles(ParticleTypes.CRIT,
                    position.x + offsetX, position.y + offsetY, position.z + offsetZ,
                    1, 0, 0.1, 0, 0.08);
        }

        // Add golden dust impact
        for (int i = 0; i < 3; i++) {
            double offsetX = (serverLevel.random.nextDouble() - 0.5) * 0.5;
            double offsetY = serverLevel.random.nextDouble() * 1.0;
            double offsetZ = (serverLevel.random.nextDouble() - 0.5) * 0.5;

            serverLevel.sendParticles(GOLDEN_DUST,
                    position.x + offsetX, position.y + offsetY, position.z + offsetZ,
                    1, 0, 0.05, 0, 0.05);
        }
    }

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        activeVajraPlayers.remove(player.getUUID());
    }

    public static void clearPlayerVajra(UUID playerId) {
        activeVajraPlayers.remove(playerId);
    }
}