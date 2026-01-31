package net.thejadeproject.ascension.progression.skills.active_skills.physique_skills.kitsune_skills;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.thejadeproject.ascension.progression.skills.data.IPersistentSkillData;
import net.thejadeproject.ascension.progression.skills.data.casting.CastType;
import net.thejadeproject.ascension.progression.skills.data.casting.ICastData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.util.ModAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FoxfireManipulationActiveSkill extends AbstractActiveSkill {

    private static final String SKILL_ID = "ascension:foxfire_manipulation";

    private static final Map<UUID, ActiveState> ACTIVE_STATES = new HashMap<>();

    private static class ActiveState {
        int remainingTicks = 0;
        int maxOrbs = 3;
        double orbitRadius = 2.0;
        double orbitSpeed = 0.1;
        List<FoxfireOrb> orbs = new ArrayList<>();
    }

    private static class FoxfireOrb {
        double angle = 0;
        double heightOffset = 0;
        int attackCooldown = 0;
        float damageMultiplier = 1.0f; // Random damage multiplier per orb
    }

    public static class FoxfireData implements IPersistentSkillData {
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

    public FoxfireManipulationActiveSkill() {
        super(Component.translatable("ascension.physique.active.foxfire_manipulation"));
        this.path = "ascension:essence";
        this.qiCost = 15.0;

        NeoForge.EVENT_BUS.addListener(this::onPlayerTick);
    }

    private FoxfireData getData(Player player) {
        var skillData = player.getData(ModAttachments.PLAYER_SKILL_DATA);
        var metaData = skillData.getActiveSkill(SKILL_ID);
        if (metaData != null && metaData.data instanceof FoxfireData data) {
            return data;
        }
        return null;
    }

    /**
     * Calculates the final skill damage multiplier based on:
     * - Base attribute value (SKILL_DAMAGE_MULTIPLIER)
     * - +0.8 per major realm
     * - +0.2 per minor realm
     */
    private double getSkillDamageMultiplier(Player player) {
        // Get base multiplier from attribute (default is 1.0)
        double baseMult = player.getAttributeValue(ModAttributes.SKILL_DAMAGE_MULTIPLIER);
        if (baseMult <= 0) baseMult = 1.0; // Safety check

        // Get realm data
        var cultivationData = player.getData(ModAttachments.PLAYER_DATA).getCultivationData();
        var pathData = cultivationData.getPathData(this.path);
        int majorRealm = pathData.majorRealm;
        int minorRealm = pathData.minorRealm; // Assumes minorRealm field exists in PathData

        // Calculate realm bonuses: Major = +0.8, Minor = +0.2
        double realmBonus = (majorRealm * 0.8) + (minorRealm * 0.2);

        return baseMult + realmBonus;
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
    public void cast(int castingTicksElapsed, Level level, Player player, ICastData castData) {
        if (level.isClientSide()) return;

        FoxfireData data = getData(player);
        if (data == null) {
            player.displayClientMessage(Component.literal("§cError: Skill data not found!"), true);
            return;
        }

        if (data.getCooldown() > 0) {
            int seconds = (data.getCooldown() + 19) / 20;
            player.displayClientMessage(Component.literal("Foxfire on cooldown: " + seconds + "s"), true);
            return;
        }

        // Get realm data for initial scaling
        var pathData = player.getData(ModAttachments.PLAYER_DATA)
                .getCultivationData().getPathData(this.path);
        int majorRealm = pathData.majorRealm;
        int minorRealm = pathData.minorRealm;

        // Show damage multiplier info to player for feedback
        double currentMult = getSkillDamageMultiplier(player);
        player.displayClientMessage(Component.literal(
                String.format("§dFoxfire Damage Multiplier: %.1fx§r", currentMult)), true);

        ActiveState state = new ActiveState();
        state.remainingTicks = 20 * (30 + (majorRealm * 5));
        state.maxOrbs = 3 + (majorRealm / 2);

        for (int i = 0; i < state.maxOrbs; i++) {
            FoxfireOrb orb = new FoxfireOrb();
            orb.angle = (2 * Math.PI * i) / state.maxOrbs;
            orb.heightOffset = (level.random.nextDouble() - 0.5) * 1.0;
            // Randomize damage multiplier between 0.7 and 1.3
            orb.damageMultiplier = 0.7f + (level.random.nextFloat() * 0.6f);
            state.orbs.add(orb);
        }

        ACTIVE_STATES.put(player.getUUID(), state);

        data.setCooldown(getCooldown());
        player.syncData(ModAttachments.PLAYER_SKILL_DATA);

        spawnInitialFoxfire(level, player.position(), state.maxOrbs);
    }

    @Override
    public void onPreCast() {}

    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        FoxfireData data = getData(player);
        if (data != null && data.getCooldown() > 0) {
            data.decrementCooldown();
            if (data.getCooldown() == 0) {
                player.syncData(ModAttachments.PLAYER_SKILL_DATA);
            }
        }

        ActiveState state = ACTIVE_STATES.get(player.getUUID());
        if (state == null) return;

        state.remainingTicks--;
        updateFoxfireOrbs(player, state);
        attackNearbyEnemies(player, state);
        spawnOrbitParticles(player.level(), player.position(), state);

        if (state.remainingTicks <= 0) {
            spawnFadeOutParticles(player.level(), player.position(), state.maxOrbs);
            ACTIVE_STATES.remove(player.getUUID());
        }
    }

    private void updateFoxfireOrbs(Player player, ActiveState state) {
        double time = player.level().getGameTime() * 0.05;

        for (int i = 0; i < state.orbs.size(); i++) {
            FoxfireOrb orb = state.orbs.get(i);
            orb.angle += state.orbitSpeed;
            if (orb.angle > 2 * Math.PI) orb.angle -= 2 * Math.PI;
            orb.heightOffset = Math.sin(time + i) * 0.5;
            if (orb.attackCooldown > 0) orb.attackCooldown--;
        }
    }

    private void attackNearbyEnemies(Player player, ActiveState state) {
        if (!(player.level() instanceof ServerLevel serverLevel)) return;

        AABB searchArea = player.getBoundingBox().inflate(10.0);
        List<LivingEntity> nearbyEnemies = player.level().getEntitiesOfClass(
                LivingEntity.class,
                searchArea,
                entity -> entity != player && entity.isAlive() && entity.isAttackable()
        );

        if (nearbyEnemies.isEmpty()) return;

        for (FoxfireOrb orb : state.orbs) {
            if (orb.attackCooldown <= 0 && !nearbyEnemies.isEmpty()) {
                // Randomly select a target from nearby enemies
                LivingEntity target = nearbyEnemies.get(serverLevel.random.nextInt(nearbyEnemies.size()));
                shootFoxfire(serverLevel, player, target, orb, state);
                orb.attackCooldown = 20;
            }
        }
    }

    private void shootFoxfire(ServerLevel serverLevel, Player player, LivingEntity target, FoxfireOrb orb, ActiveState state) {
        double x = player.getX() + state.orbitRadius * Math.cos(orb.angle);
        double y = player.getY() + 1.5 + orb.heightOffset;
        double z = player.getZ() + state.orbitRadius * Math.sin(orb.angle);

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

            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, trailX, trailY, trailZ,
                    1, 0, 0, 0, 0.05);
        }

        // DAMAGE CALCULATION WITH ORB MULTIPLIER
        var pathData = player.getData(ModAttachments.PLAYER_DATA)
                .getCultivationData().getPathData(this.path);
        int majorRealm = pathData.majorRealm;
        int minorRealm = pathData.minorRealm;

        // Base damage scales with major realm
        float baseDamage = 2.0f + (majorRealm * 0.5f);

        // Apply skill damage multiplier (includes attribute + realm bonuses)
        double damageMult = getSkillDamageMultiplier(player);

        // Apply orb-specific damage multiplier
        float finalDamage = (float)(baseDamage * damageMult * orb.damageMultiplier);

        // Ensure minimum damage of 1.0
        if (finalDamage < 1.0f) finalDamage = 1.0f;

        target.hurt(player.damageSources().magic(), finalDamage);
        target.setRemainingFireTicks(100);

        // Show damage with orb multiplier info
        player.displayClientMessage(Component.literal(
                String.format("§d⚡ Foxfire hit for %.1f damage! (x%.1f)",
                        finalDamage, orb.damageMultiplier)), true);

        for (int i = 0; i < 15; i++) {
            double offsetX = (serverLevel.random.nextDouble() - 0.5) * 0.8;
            double offsetY = serverLevel.random.nextDouble() * 1.2;
            double offsetZ = (serverLevel.random.nextDouble() - 0.5) * 0.8;

            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                    target.getX() + offsetX, target.getY() + offsetY, target.getZ() + offsetZ,
                    1, 0, 0.1, 0, 0.1);
        }
    }

    private void spawnOrbitParticles(Level level, Vec3 position, ActiveState state) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        for (FoxfireOrb orb : state.orbs) {
            double x = position.x + state.orbitRadius * Math.cos(orb.angle);
            double y = position.y + 1.5 + orb.heightOffset;
            double z = position.z + state.orbitRadius * Math.sin(orb.angle);

            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 1, 0, 0, 0, 0);

            // Trail particles
            double trailAngle = orb.angle - 0.2;
            double trailX = position.x + state.orbitRadius * Math.cos(trailAngle);
            double trailY = position.y + 1.5 + orb.heightOffset;
            double trailZ = position.z + state.orbitRadius * Math.sin(trailAngle);

            serverLevel.sendParticles(ParticleTypes.ENCHANT, trailX, trailY, trailZ, 1, 0, 0, 0, 0.02);
        }
    }

    private void spawnInitialFoxfire(Level level, Vec3 position, int orbCount) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        for (int i = 0; i < 50; i++) {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double radius = level.random.nextDouble() * 2.0;
            double x = position.x + radius * Math.cos(angle);
            double z = position.z + radius * Math.sin(angle);
            double y = position.y + level.random.nextDouble() * 2.0;

            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 1, 0, 0.1, 0, 0.1);
        }

        for (int i = 0; i < 360; i += 10) {
            double angle = Math.toRadians(i);
            double radius = 2.5;
            double x = position.x + radius * Math.cos(angle);
            double z = position.z + radius * Math.sin(angle);

            serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT, x, position.y + 1.0, z,
                    1, 0, 0.2, 0, 0.05);
        }
    }

    private void spawnFadeOutParticles(Level level, Vec3 position, int orbCount) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        for (int i = 0; i < 30; i++) {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double radius = level.random.nextDouble() * 2.0;
            double x = position.x + radius * Math.cos(angle);
            double z = position.z + radius * Math.sin(angle);
            double y = position.y + level.random.nextDouble() * 2.0;

            serverLevel.sendParticles(ParticleTypes.CLOUD, x, y, z, 1, 0, 0.05, 0, 0.05);

            if (i % 3 == 0) {
                serverLevel.sendParticles(ParticleTypes.WITCH, x, y, z, 1, 0, 0, 0, 0.1);
            }
        }
    }

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        ACTIVE_STATES.remove(player.getUUID());
    }

    @Override
    public IPersistentSkillData getPersistentDataInstance() {
        return new FoxfireData();
    }

    @Override
    public IPersistentSkillData getPersistentDataInstance(CompoundTag tag) {
        FoxfireData data = new FoxfireData();
        data.readData(tag);
        return data;
    }
}