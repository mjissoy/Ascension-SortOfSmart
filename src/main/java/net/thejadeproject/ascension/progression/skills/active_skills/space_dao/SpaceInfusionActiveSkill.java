package net.thejadeproject.ascension.progression.skills.active_skills.space_dao;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.data.CastType;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpaceInfusionActiveSkill extends AbstractActiveSkill {

    // Particle colors for purple effects
    private static final DustParticleOptions PURPLE_DUST =
            new DustParticleOptions(new Vector3f(0.5f, 0.0f, 0.8f), 1.0f);
    private static final DustParticleOptions DARK_PURPLE_DUST =
            new DustParticleOptions(new Vector3f(0.3f, 0.0f, 0.5f), 1.0f);

    // Track active skill instances
    private static final Map<UUID, SkillData> activeSkills = new HashMap<>();
    private static final Map<UUID, Integer> cooldowns = new HashMap<>();

    private static class SkillData {
        int ticksActive = 0;
        ItemEntity floatingItem;
        boolean hasSpawnedStone = false;
        Vec3 targetPosition;
        float rotation = 0.0f;
        float rotationSpeed = 1.0f;
        boolean firstUpdate = true;
    }

    public SpaceInfusionActiveSkill() {
        super(Component.translatable("ascension.skill.active.space_infusion"));
        this.path = "ascension:essence";
        this.qiCost = 25.0;

        // Register event listeners
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(this::onPlayerTick);
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(this::onServerTick);
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
        return 15 * 20; // 15 seconds in ticks
    }

    @Override
    public int maxCastingTicks() {
        return 0; // Instant cast
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

        // Check if skill is on cooldown
        if (isOnCooldown(playerId)) {
            int remainingTicks = cooldowns.get(playerId);
            int seconds = (remainingTicks + 19) / 20; // Round up
            player.displayClientMessage(
                    Component.literal("Space Infusion is on cooldown! (" + seconds + "s)"),
                    true
            );
            return;
        }

        // Check if player is in The End dimension
        if (!isInEndDimension(level)) {
            player.displayClientMessage(
                    Component.literal("§cThis skill can only be used in The End!§r"),
                    true
            );
            // Don't consume Qi when not in the right dimension
            return; // Return early without consuming Qi
        }

        // Check if player has spiritual stone
        ItemStack spiritualStone = findSpiritualStone(player);
        if (spiritualStone.isEmpty()) {
            player.displayClientMessage(
                    Component.literal("§cYou need a Spiritual Stone to use this skill!§r"),
                    true
            );
            return;
        }

        // Consume one spiritual stone
        spiritualStone.shrink(1);

        // Set cooldown
        cooldowns.put(playerId, getCooldown());

        // Calculate position 1 block in front at head level
        Vec3 lookVec = player.getLookAngle().normalize();
        Vec3 playerPos = player.position();
        Vec3 targetPosition = playerPos.add(lookVec.x, player.getEyeHeight() - 0.3, lookVec.z);

        // Create the floating item entity
        ItemStack floatingStone = new ItemStack(ModItems.SPIRITUAL_STONE.get(), 1);
        ItemEntity itemEntity = new ItemEntity(level,
                targetPosition.x, targetPosition.y, targetPosition.z,
                floatingStone);

        // Configure the item entity
        itemEntity.setNoGravity(true);
        itemEntity.setInvulnerable(true);
        itemEntity.setPickUpDelay(Integer.MAX_VALUE);
        // Don't set extended lifetime - let it rotate naturally

        // Add slight initial motion to make it spin
        itemEntity.setDeltaMovement(0, 0, 0);

        // Add to level
        level.addFreshEntity(itemEntity);

        // Create skill data
        SkillData data = new SkillData();
        data.floatingItem = itemEntity;
        data.targetPosition = targetPosition;

        activeSkills.put(playerId, data);

        // Initial particles
        spawnInitialParticles(level, targetPosition);

        player.displayClientMessage(
                Component.literal("§dSpace Infusion begins...§r"),
                true
        );
    }

    @Override
    public void onPreCast() {

    }

    private boolean isInEndDimension(Level level) {
        return level.dimension().location().getPath().equals("the_end");
    }

    private ItemStack findSpiritualStone(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() == ModItems.SPIRITUAL_STONE.get()) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private boolean isOnCooldown(UUID playerId) {
        return cooldowns.containsKey(playerId) && cooldowns.get(playerId) > 0;
    }

    public void onServerTick(net.neoforged.neoforge.event.tick.ServerTickEvent.Post event) {
        // Update cooldowns
        Map<UUID, Integer> newCooldowns = new HashMap<>();
        for (Map.Entry<UUID, Integer> entry : cooldowns.entrySet()) {
            int remaining = entry.getValue() - 1;
            if (remaining > 0) {
                newCooldowns.put(entry.getKey(), remaining);
            }
        }
        cooldowns.clear();
        cooldowns.putAll(newCooldowns);
    }

    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        UUID playerId = player.getUUID();
        Level level = player.level();

        if (!activeSkills.containsKey(playerId)) return;

        SkillData data = activeSkills.get(playerId);

        // Check if still in The End dimension
        if (!isInEndDimension(level)) {
            cleanupSkill(data);
            activeSkills.remove(playerId);
            player.displayClientMessage(
                    Component.literal("§cSpace Infusion fades as you leave The End§r"),
                    true
            );
            return;
        }

        // Update tick counter
        data.ticksActive++;

        // Update rotation for particle effects
        data.rotation += data.rotationSpeed;
        if (data.rotation > 360.0f) {
            data.rotation -= 360.0f;
        }

        // Increase rotation speed over time
        if (data.ticksActive < 100) {
            data.rotationSpeed = 1.0f + (9.0f * (data.ticksActive / 100.0f));
        }

        // Calculate new position 1 block in front of player
        Vec3 lookVec = player.getLookAngle().normalize();
        Vec3 playerPos = player.position();
        data.targetPosition = playerPos.add(lookVec.x, player.getEyeHeight() - 0.3, lookVec.z);

        // Update the floating item entity
        if (data.floatingItem != null && data.floatingItem.isAlive()) {
            // On first update, set the item to stay in place
            if (data.firstUpdate) {
                data.floatingItem.setPos(data.targetPosition);
                data.floatingItem.setDeltaMovement(0, 0, 0);
                data.firstUpdate = false;
            }

            // Move the item to the target position (following player)
            Vec3 currentPos = data.floatingItem.position();
            Vec3 toTarget = data.targetPosition.subtract(currentPos);

            // Move slowly toward target (lerp)
            Vec3 newPos = currentPos.add(toTarget.scale(0.3));
            data.floatingItem.setPos(newPos.x, newPos.y, newPos.z);

            // Add a small circular motion to make it appear to spin
            // This is subtle and won't make it float away
            double spinRadius = 0.05;
            double spinX = Math.cos(Math.toRadians(data.rotation)) * spinRadius * 0.1;
            double spinZ = Math.sin(Math.toRadians(data.rotation)) * spinRadius * 0.1;

            data.floatingItem.setDeltaMovement(spinX, 0, spinZ);

            // Spawn particles around the item
            if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
                // Spawn scarce purple particles being pulled in
                if (data.ticksActive % 3 == 0) { // Every 3 ticks = scarce particles
                    spawnParticlePullEffects(serverLevel, data);
                }

                // Add some glow particles around the item
                if (data.ticksActive % 5 == 0) {
                    spawnGlowParticles(serverLevel, data);
                }
            }
        } else {
            // Item entity was removed somehow, cancel skill
            activeSkills.remove(playerId);
            return;
        }

        // Check if 5 seconds have passed (100 ticks)
        if (data.ticksActive >= 100 && !data.hasSpawnedStone) {
            finishSkill(player, data);
            data.hasSpawnedStone = true;
        }

        // Clean up after completion
        if (data.ticksActive >= 110) { // Small delay after completion
            cleanupSkill(data);
            activeSkills.remove(playerId);
        }
    }

    private void spawnInitialParticles(Level level, Vec3 position) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        // Small burst of particles when starting
        for (int i = 0; i < 10; i++) {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double radius = 0.2 + level.random.nextDouble() * 0.2;
            double x = position.x + radius * Math.cos(angle);
            double z = position.z + radius * Math.sin(angle);
            double y = position.y + (level.random.nextDouble() - 0.5) * 0.2;

            serverLevel.sendParticles(PURPLE_DUST,
                    x, y, z,
                    1, 0, 0.02, 0, 0.01);
        }
    }

    private void spawnParticlePullEffects(ServerLevel serverLevel, SkillData data) {
        Vec3 center = data.floatingItem.position();

        // Very scarce particles - only 1-2 per spawn
        int particleCount = serverLevel.random.nextInt(2) + 1; // 1 or 2 particles

        for (int i = 0; i < particleCount; i++) {
            // Random position 2-4 blocks away
            double distance = 2.0 + serverLevel.random.nextDouble() * 2.0;
            double angle = serverLevel.random.nextDouble() * Math.PI * 2;
            double verticalAngle = serverLevel.random.nextDouble() * Math.PI;

            double x = center.x + distance * Math.cos(angle) * Math.sin(verticalAngle);
            double y = center.y + distance * Math.cos(verticalAngle);
            double z = center.z + distance * Math.sin(angle) * Math.sin(verticalAngle);

            // Direction towards center
            double dx = (center.x - x) * 0.05;
            double dy = (center.y - y) * 0.05;
            double dz = (center.z - z) * 0.05;

            // Random color between purple and dark purple
            DustParticleOptions color = serverLevel.random.nextBoolean() ? PURPLE_DUST : DARK_PURPLE_DUST;

            serverLevel.sendParticles(color,
                    x, y, z,
                    1, dx, dy, dz, 0.03);
        }
    }

    private void spawnGlowParticles(ServerLevel serverLevel, SkillData data) {
        Vec3 center = data.floatingItem.position();

        // Add a few glow particles around the item
        for (int i = 0; i < 2; i++) {
            double angle = Math.toRadians(data.rotation + (i * 180.0));
            double radius = 0.25;
            double x = center.x + radius * Math.cos(angle);
            double z = center.z + radius * Math.sin(angle);
            double y = center.y + 0.1 * Math.sin(angle * 2);

            serverLevel.sendParticles(ParticleTypes.GLOW,
                    x, y, z,
                    1, 0, 0, 0, 0.01);
        }
    }

    private void finishSkill(Player player, SkillData data) {
        Level level = player.level();

        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            Vec3 center = data.floatingItem.position();

            // Play the DING sound
            serverLevel.playSound(null,
                    player.blockPosition(),
                    SoundEvents.EXPERIENCE_ORB_PICKUP,
                    SoundSource.PLAYERS,
                    1.0f,
                    0.5f
            );

            // Final particle effect
            for (int i = 0; i < 15; i++) {
                double angle = serverLevel.random.nextDouble() * Math.PI * 2;
                double distance = serverLevel.random.nextDouble() * 0.5;
                double x = center.x + distance * Math.cos(angle);
                double z = center.z + distance * Math.sin(angle);
                double y = center.y + (serverLevel.random.nextDouble() - 0.5) * 0.3;

                serverLevel.sendParticles(ParticleTypes.GLOW,
                        x, y, z,
                        1, 0, 0.05, 0, 0.05);
            }

            // Spawn spatial_stone_tier_1 item
            ItemStack spatialStone = new ItemStack(ModItems.SPATIAL_STONE_TIER_1.get(), 1);
            ItemEntity droppedItem = new ItemEntity(level,
                    center.x, center.y, center.z,
                    spatialStone);
            droppedItem.setDefaultPickUpDelay();
            level.addFreshEntity(droppedItem);

            // Remove the floating spiritual stone
            if (data.floatingItem != null && data.floatingItem.isAlive()) {
                data.floatingItem.discard();
            }

            player.displayClientMessage(
                    Component.literal("§aSpace Infusion complete! A Spatial Stone has formed.§r"),
                    true
            );
        }
    }

    private void cleanupSkill(SkillData data) {
        if (data.floatingItem != null && data.floatingItem.isAlive()) {
            data.floatingItem.discard();
        }
    }

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        UUID playerId = player.getUUID();
        if (activeSkills.containsKey(playerId)) {
            cleanupSkill(activeSkills.get(playerId));
            activeSkills.remove(playerId);
        }
    }

    // Helper methods for external use
    public static boolean isPlayerUsingSkill(UUID playerId) {
        return activeSkills.containsKey(playerId);
    }

    public static int getRemainingCooldown(UUID playerId) {
        if (!cooldowns.containsKey(playerId)) return 0;
        return cooldowns.get(playerId);
    }

    public static void clearCooldown(UUID playerId) {
        cooldowns.remove(playerId);
    }

    public static void clearAll() {
        for (SkillData data : activeSkills.values()) {
            if (data.floatingItem != null && data.floatingItem.isAlive()) {
                data.floatingItem.discard();
            }
        }
        activeSkills.clear();
        cooldowns.clear();
    }
}