package net.thejadeproject.ascension.progression.skills.active_skills;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.thejadeproject.ascension.progression.skills.data.casting.ICastData;
import org.joml.Vector3f;

import java.util.*;

public class IndestructibleVajraActiveSkill extends AbstractActiveSkill {

    private static final String SKILL_ID = "ascension:indestructible_vajra";

    // Transient active sessions - not persisted (runtime only)
    private static final Map<UUID, VajraSession> ACTIVE_SESSIONS = new HashMap<>();

    // Golden colors for particles
    private static final DustParticleOptions GOLDEN_DUST = new DustParticleOptions(new Vector3f(1.0f, 0.8f, 0.0f), 1.0f);
    private static final DustParticleOptions GOLDEN_ORANGE_DUST = new DustParticleOptions(new Vector3f(1.0f, 0.6f, 0.1f), 1.0f);
    private static final DustParticleOptions GOLDEN_YELLOW_DUST = new DustParticleOptions(new Vector3f(1.0f, 1.0f, 0.3f), 1.0f);

    // Transient session data (not saved)
    private static class VajraSession {
        float storedDamage = 0;
        int remainingTicks = 0;
        List<UUID> damageSources = new ArrayList<>();
        boolean hasReleased = false;
        int particleTick = 0;
    }

    // Persistent data for cooldown
    public static class VajraData implements IPersistentSkillData {
        private int cooldownTicks = 0;

        @Override
        public CompoundTag writeData() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("Cooldown", cooldownTicks);
            return tag;
        }

        @Override
        public void readData(CompoundTag tag) {
            this.cooldownTicks = tag.getInt("Cooldown");
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf) {
            buf.writeInt(cooldownTicks);
        }

        @Override
        public void decode(RegistryFriendlyByteBuf buf) {
            this.cooldownTicks = buf.readInt();
        }

        public int getCooldown() { return cooldownTicks; }
        public void setCooldown(int cd) { this.cooldownTicks = cd; }
        public void decrementCooldown() { if (cooldownTicks > 0) cooldownTicks--; }
    }

    public IndestructibleVajraActiveSkill() {
        super(Component.translatable("ascension.physique.active.indestructible_vajra"));
        this.path = "ascension:body";
        this.qiCost = 10.0;

        NeoForge.EVENT_BUS.addListener(this::onLivingDamageEvent);
        NeoForge.EVENT_BUS.addListener(this::onPlayerTick);
    }

    private VajraData getData(Player player) {
        var skillData = player.getData(ModAttachments.PLAYER_SKILL_DATA);
        var metaData = skillData.getActiveSkill(SKILL_ID);
        if (metaData != null && metaData.data instanceof VajraData data) {
            return data;
        }
        return null;
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
        return 20 * 120; // 2 minutes
    }

    @Override
    public int maxCastingTicks() {
        return 0;
    }

    @Override
    public void cast(int castingTicksElapsed, Level level, Player player, ICastData castData) {
        if (level.isClientSide()) return;

        VajraData data = getData(player);
        if (data == null) return;

        if (data.getCooldown() > 0) {
            int seconds = (data.getCooldown() + 19) / 20;
            player.displayClientMessage(Component.literal("Indestructible Vajra on cooldown: " + seconds + "s"), true);
            return;
        }

        UUID playerId = player.getUUID();
        VajraSession session = new VajraSession();

        int majorRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(this.path).majorRealm;
        session.remainingTicks = 20 * (5 + majorRealm);

        ACTIVE_SESSIONS.put(playerId, session);

        data.setCooldown(getCooldown());
        player.syncData(ModAttachments.PLAYER_SKILL_DATA);

        spawnInitialParticles(level, player.position());
    }

    @Override
    public void onPreCast() {}

    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        // Handle cooldown
        VajraData data = getData(player);
        if (data != null && data.getCooldown() > 0) {
            data.decrementCooldown();
            if (data.getCooldown() == 0) {
                player.syncData(ModAttachments.PLAYER_SKILL_DATA);
            }
        }

        UUID playerId = player.getUUID();
        VajraSession session = ACTIVE_SESSIONS.get(playerId);

        if (session != null) {
            session.remainingTicks--;
            session.particleTick++;

            spawnGoldenBellParticles(player.level(), player.position());

            if (session.remainingTicks <= 0 && !session.hasReleased) {
                releaseShockwave(player, session);
                session.hasReleased = true;
                ACTIVE_SESSIONS.remove(playerId);
            }
        }
    }

    public void onLivingDamageEvent(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {
            UUID playerId = player.getUUID();
            VajraSession session = ACTIVE_SESSIONS.get(playerId);

            if (session != null && !session.hasReleased) {
                session.storedDamage += event.getOriginalDamage();

                Entity source = event.getSource().getEntity();
                if (source != null && !session.damageSources.contains(source.getUUID())) {
                    session.damageSources.add(source.getUUID());
                }

                event.setNewDamage(0.0F);
                spawnDamageAbsorbParticles(player.level(), player.position());
            }
        }
    }

    // ... keep all particle and helper methods the same ...

    private void spawnInitialParticles(Level level, Vec3 position) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        for (int ring = 0; ring <= 4; ring++) {
            double height = ring * 0.5;
            double radius = calculateBellRadius(height, 0, 2.2);

            for (int i = 0; i < 360; i += 15) {
                double angle = Math.toRadians(i);
                double x = position.x + radius * Math.cos(angle);
                double z = position.z + radius * Math.sin(angle);

                serverLevel.sendParticles(GOLDEN_DUST, x, position.y + height, z, 1, 0, 0.05, 0, 0);
            }
        }

        for (int i = 0; i < 30; i++) {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double radius = level.random.nextDouble() * 2.0;
            double x = position.x + radius * Math.cos(angle);
            double z = position.z + radius * Math.sin(angle);
            double y = position.y + level.random.nextDouble() * 2.5;

            serverLevel.sendParticles(ParticleTypes.FLAME, x, y, z, 1, 0, 0.05, 0, 0);
        }
    }

    private double calculateBellRadius(double height, double minHeight, double maxHeight) {
        double normalizedHeight = (height - minHeight) / (maxHeight - minHeight);
        if (normalizedHeight < 0.3) {
            return 1.5 + 0.5 * (normalizedHeight / 0.3);
        } else if (normalizedHeight < 0.7) {
            return 2.0;
        } else {
            return 2.0 - 0.8 * ((normalizedHeight - 0.7) / 0.3);
        }
    }

    private void spawnGoldenBellParticles(Level level, Vec3 position) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        for (double height = 0; height <= 2.2; height += 0.3) {
            double radius = calculateBellRadius(height, 0, 2.2);
            DustParticleOptions ringColor = (height % 0.6 < 0.3) ? GOLDEN_DUST : GOLDEN_ORANGE_DUST;

            for (int i = 0; i < 360; i += 20) {
                double angle = Math.toRadians(i);
                double x = position.x + radius * Math.cos(angle);
                double z = position.z + radius * Math.sin(angle);

                serverLevel.sendParticles(ringColor, x, position.y + height, z, 1, 0, 0, 0, 0);
            }
        }

        for (int meridian = 0; meridian < 12; meridian++) {
            double angle = Math.toRadians(meridian * 30);
            for (double height = 0; height <= 2.2; height += 0.15) {
                double radius = calculateBellRadius(height, 0, 2.2);
                double x = position.x + radius * Math.cos(angle);
                double z = position.z + radius * Math.sin(angle);

                serverLevel.sendParticles(GOLDEN_YELLOW_DUST, x, position.y + height, z, 1, 0, 0, 0, 0);
            }
        }

        for (double phi = 0; phi <= Math.PI / 2; phi += 0.2) {
            double topRadius = 0.8 * Math.sin(phi);
            double yOffset = 2.2 + 0.6 * Math.cos(phi);

            for (int i = 0; i < 360; i += 30) {
                double angle = Math.toRadians(i);
                double x = position.x + topRadius * Math.cos(angle);
                double z = position.z + topRadius * Math.sin(angle);

                serverLevel.sendParticles(ParticleTypes.FLAME, x, position.y + yOffset, z, 1, 0, 0, 0, 0);
            }
        }

        for (int i = 0; i < 360; i += 15) {
            double angle = Math.toRadians(i);
            double x = position.x + 2.2 * Math.cos(angle);
            double z = position.z + 2.2 * Math.sin(angle);
            serverLevel.sendParticles(ParticleTypes.FLAME, x, position.y - 0.1, z, 1, 0, 0, 0, 0);
        }
    }

    private void spawnDamageAbsorbParticles(Level level, Vec3 position) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        for (int i = 0; i < 12; i++) {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double radius = level.random.nextDouble() * 1.5 + 0.5;
            double x = position.x + radius * Math.cos(angle);
            double z = position.z + radius * Math.sin(angle);
            double y = position.y + level.random.nextDouble() * 2.2;
            double dx = (x - position.x) * 0.1;
            double dz = (z - position.z) * 0.1;

            DustParticleOptions color = level.random.nextBoolean() ? GOLDEN_DUST : GOLDEN_YELLOW_DUST;
            serverLevel.sendParticles(color, x, y, z, 1, dx, 0, dz, 0.05);
        }

        for (int meridian = 0; meridian < 4; meridian++) {
            double angle = Math.toRadians(meridian * 90);
            for (double height = 0.5; height <= 1.8; height += 0.3) {
                double radius = calculateBellRadius(height, 0, 2.2);
                double x = position.x + radius * Math.cos(angle);
                double z = position.z + radius * Math.sin(angle);
                serverLevel.sendParticles(ParticleTypes.FLAME, x, position.y + height, z, 1, 0, 0.05, 0, 0.02);
            }
        }

        for (int i = 0; i < 5; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * 0.5;
            double offsetZ = (level.random.nextDouble() - 0.5) * 0.5;
            double offsetY = level.random.nextDouble() * 2.0;
            serverLevel.sendParticles(ParticleTypes.FIREWORK, position.x + offsetX, position.y + offsetY, position.z + offsetZ, 1, 0, 0, 0, 0.1);
        }
    }

    private void releaseShockwave(Player player, VajraSession session) {
        Level level = player.level();
        if (level.isClientSide()) return;

        int majorRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(this.path).majorRealm;
        float radius = 5.0f + majorRealm;

        AABB aabb = player.getBoundingBox().inflate(radius);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb,
                livingEntity -> livingEntity != player && livingEntity.isAlive());

        float shockwaveDamage = session.storedDamage * 0.5f;
        if (shockwaveDamage < 2.0f) shockwaveDamage = 2.0f;

        if (level instanceof ServerLevel serverLevel) {
            spawnBellShatterEffect(serverLevel, player.position());
            spawnShockwaveParticles(serverLevel, player.position(), radius);
        }

        for (LivingEntity entity : entities) {
            double distance = entity.distanceTo(player);
            if (distance <= radius) {
                float distanceMultiplier = 1.0f - (float)(distance / radius);
                float finalDamage = shockwaveDamage * distanceMultiplier;

                entity.hurt(player.damageSources().magic(), finalDamage);

                double dx = entity.getX() - player.getX();
                double dz = entity.getZ() - player.getZ();
                double distance2D = Math.sqrt(dx * dx + dz * dz);
                if (distance2D > 0) {
                    double strength = (finalDamage / 5.0) * distanceMultiplier;
                    entity.setDeltaMovement((dx / distance2D) * strength, 0.3 * strength, (dz / distance2D) * strength);
                    entity.hurtMarked = true;
                }

                if (level instanceof ServerLevel serverLevel) {
                    spawnImpactParticles(serverLevel, entity.position());
                }
            }
        }
    }

    private void spawnBellShatterEffect(ServerLevel serverLevel, Vec3 center) {
        for (int piece = 0; piece < 40; piece++) {
            double angle = serverLevel.random.nextDouble() * Math.PI * 2;
            double height = serverLevel.random.nextDouble() * 2.5;
            double radius = calculateBellRadius(height, 0, 2.2);

            double x = center.x + radius * Math.cos(angle);
            double z = center.z + radius * Math.sin(angle);
            double y = center.y + height;

            double dx = (x - center.x) * 0.3;
            double dy = serverLevel.random.nextDouble() * 0.2;
            double dz = (z - center.z) * 0.3;

            DustParticleOptions color = serverLevel.random.nextBoolean() ? GOLDEN_DUST : GOLDEN_YELLOW_DUST;
            serverLevel.sendParticles(color, x, y, z, 1, dx, dy, dz, 0.1);
        }

        for (int i = 0; i < 20; i++) {
            double angle = serverLevel.random.nextDouble() * Math.PI * 2;
            double distance = serverLevel.random.nextDouble() * 0.5;
            double x = center.x + distance * Math.cos(angle);
            double z = center.z + distance * Math.sin(angle);
            double y = center.y + 2.5 + serverLevel.random.nextDouble() * 0.5;
            serverLevel.sendParticles(ParticleTypes.FIREWORK, x, y, z, 1, 0, 0, 0, 0.2);
        }
    }

    private void spawnShockwaveParticles(ServerLevel serverLevel, Vec3 center, float radius) {
        for (int ring = 0; ring <= 6; ring++) {
            double currentRadius = radius * (ring / 6.0);
            for (int i = 0; i < 360; i += 20) {
                double angle = Math.toRadians(i);
                double x = center.x + currentRadius * Math.cos(angle);
                double z = center.z + currentRadius * Math.sin(angle);

                for (double yOffset = 0; yOffset <= 2.0; yOffset += 0.5) {
                    DustParticleOptions color = (ring % 2 == 0) ? GOLDEN_DUST : GOLDEN_YELLOW_DUST;
                    serverLevel.sendParticles(color, x, center.y + yOffset, z, 1, 0, 0, 0, 0);
                }

                if (ring % 2 == 0) {
                    serverLevel.sendParticles(ParticleTypes.FLAME, x, center.y + 0.5, z, 1, 0, 0.1, 0, 0.03);
                }
            }
        }

        for (int i = 0; i < 50; i++) {
            double angle = serverLevel.random.nextDouble() * Math.PI * 2;
            double distance = serverLevel.random.nextDouble() * 1.0;
            double x = center.x + distance * Math.cos(angle);
            double z = center.z + distance * Math.sin(angle);
            double y = center.y + serverLevel.random.nextDouble() * 2.0;
            serverLevel.sendParticles(ParticleTypes.FIREWORK, x, y, z, 1, 0, 0, 0, 0.15);
        }
    }

    private void spawnImpactParticles(ServerLevel serverLevel, Vec3 position) {
        for (int i = 0; i < 6; i++) {
            double offsetX = (serverLevel.random.nextDouble() - 0.5) * 0.8;
            double offsetY = serverLevel.random.nextDouble() * 1.2;
            double offsetZ = (serverLevel.random.nextDouble() - 0.5) * 0.8;
            serverLevel.sendParticles(ParticleTypes.CRIT, position.x + offsetX, position.y + offsetY, position.z + offsetZ, 1, 0, 0.1, 0, 0.08);
        }

        for (int i = 0; i < 3; i++) {
            double offsetX = (serverLevel.random.nextDouble() - 0.5) * 0.5;
            double offsetY = serverLevel.random.nextDouble() * 1.0;
            double offsetZ = (serverLevel.random.nextDouble() - 0.5) * 0.5;
            serverLevel.sendParticles(GOLDEN_DUST, position.x + offsetX, position.y + offsetY, position.z + offsetZ, 1, 0, 0.05, 0, 0.05);
        }
    }

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        ACTIVE_SESSIONS.remove(player.getUUID());
    }

    @Override
    public IPersistentSkillData getPersistentDataInstance() {
        return new VajraData();
    }

    @Override
    public IPersistentSkillData getPersistentDataInstance(CompoundTag tag) {
        VajraData data = new VajraData();
        data.readData(tag);
        return data;
    }
}