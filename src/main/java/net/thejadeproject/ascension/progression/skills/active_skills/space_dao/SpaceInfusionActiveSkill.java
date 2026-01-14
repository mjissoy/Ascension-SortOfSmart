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
import net.thejadeproject.ascension.progression.skills.data.casting.CastType;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpaceInfusionActiveSkill extends AbstractActiveSkill {

    private static final DustParticleOptions PURPLE_DUST =
            new DustParticleOptions(new Vector3f(0.5f, 0.0f, 0.8f), 1.0f);
    private static final DustParticleOptions DARK_PURPLE_DUST =
            new DustParticleOptions(new Vector3f(0.3f, 0.0f, 0.5f), 1.0f);

    private static final Map<UUID, SkillData> activeSkills = new HashMap<>();
    private static final Map<UUID, Integer> cooldowns = new HashMap<>();

    private static class SkillData {
        int ticksActive = 0;
        ItemEntity floatingItem;
        boolean hasCompleted = false;
        Vec3 fixedPosition;
        float rotation = 0.0f;
        float rotationSpeed = 1.0f;
    }

    public SpaceInfusionActiveSkill() {
        super(Component.translatable("ascension.skill.active.space_infusion"));
        this.path = "ascension:essence";
        this.qiCost = 25.0;

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
        return 15 * 20;
    }

    @Override
    public int maxCastingTicks() {
        return 0;
    }


    @Override
    public void cast(int castingTicksElapsed, Level level, Player player) {
        if (level.isClientSide()) return;

        UUID playerId = player.getUUID();

        if (isOnCooldown(playerId)) {
            int remainingTicks = cooldowns.get(playerId);
            int seconds = (remainingTicks + 19) / 20;
            player.displayClientMessage(
                    Component.literal("Space Infusion is on cooldown! (" + seconds + "s)"),
                    true
            );
            return;
        }

        if (!isInEndDimension(level)) {
            player.displayClientMessage(
                    Component.literal("§cThis skill can only be used in The End!§r"),
                    true
            );
            return;
        }

        ItemStack spiritualStone = findSpiritualStone(player);
        if (spiritualStone.isEmpty()) {
            player.displayClientMessage(
                    Component.literal("§cYou need a Spiritual Stone to use this skill!§r"),
                    true
            );
            return;
        }

        spiritualStone.shrink(1);

        cooldowns.put(playerId, getCooldown());

        Vec3 lookVec = player.getLookAngle().normalize();
        Vec3 playerPos = player.position();
        Vec3 fixedPosition = playerPos.add(lookVec.x, player.getEyeHeight() - 0.3, lookVec.z);

        ItemStack floatingStone = new ItemStack(ModItems.SPIRITUAL_STONE.get(), 1);
        ItemEntity itemEntity = new ItemEntity(level,
                fixedPosition.x, fixedPosition.y, fixedPosition.z,
                floatingStone);

        itemEntity.setNoGravity(true);
        itemEntity.setInvulnerable(true);
        itemEntity.setPickUpDelay(Integer.MAX_VALUE);
        itemEntity.setDeltaMovement(0, 0, 0);

        level.addFreshEntity(itemEntity);

        SkillData data = new SkillData();
        data.floatingItem = itemEntity;
        data.fixedPosition = fixedPosition;

        activeSkills.put(playerId, data);

        spawnInitialParticles(level, fixedPosition);

        player.displayClientMessage(
                Component.literal("§dSpace Infusion begins..."),
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

        if (!isInEndDimension(level)) {
            cleanupSkill(data);
            activeSkills.remove(playerId);
            player.displayClientMessage(
                    Component.literal("§cSpace Infusion fades as you leave The End§r"),
                    true
            );
            return;
        }

        if (data.hasCompleted) {
            data.ticksActive++;
            if (data.ticksActive >= 110) {
                cleanupSkill(data);
                activeSkills.remove(playerId);
            }
            return;
        }

        data.ticksActive++;

        data.rotation += data.rotationSpeed;
        if (data.rotation > 360.0f) {
            data.rotation -= 360.0f;
        }

        if (data.ticksActive < 100) {
            data.rotationSpeed = 1.0f + (9.0f * (data.ticksActive / 100.0f));
        }

        if (data.floatingItem != null && data.floatingItem.isAlive()) {
            data.floatingItem.setPos(data.fixedPosition.x, data.fixedPosition.y, data.fixedPosition.z);

            data.floatingItem.setDeltaMovement(0, 0, 0);

            if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
                if (data.ticksActive % 3 == 0) {
                    spawnParticlePullEffects(serverLevel, data);
                }

                if (data.ticksActive % 5 == 0) {
                    spawnGlowParticles(serverLevel, data);
                }
            }
        } else {
            cleanupSkill(data);
            activeSkills.remove(playerId);
            player.displayClientMessage(
                    Component.literal("§cThe Spiritual Stone was destroyed!§r"),
                    true
            );
            return;
        }

        if (data.ticksActive >= 100 && !data.hasCompleted) {
            completeSkill(player, data);
            data.hasCompleted = true;
            data.ticksActive = 0;
        }
    }

    private void spawnInitialParticles(Level level, Vec3 position) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

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
        Vec3 center = data.fixedPosition;

        int particleCount = serverLevel.random.nextInt(2) + 1;

        for (int i = 0; i < particleCount; i++) {
            double distance = 2.0 + serverLevel.random.nextDouble() * 2.0;
            double angle = serverLevel.random.nextDouble() * Math.PI * 2;
            double verticalAngle = serverLevel.random.nextDouble() * Math.PI;

            double x = center.x + distance * Math.cos(angle) * Math.sin(verticalAngle);
            double y = center.y + distance * Math.cos(verticalAngle);
            double z = center.z + distance * Math.sin(angle) * Math.sin(verticalAngle);

            double dx = (center.x - x) * 0.05;
            double dy = (center.y - y) * 0.05;
            double dz = (center.z - z) * 0.05;

            DustParticleOptions color = serverLevel.random.nextBoolean() ? PURPLE_DUST : DARK_PURPLE_DUST;

            serverLevel.sendParticles(color,
                    x, y, z,
                    1, dx, dy, dz, 0.03);
        }
    }

    private void spawnGlowParticles(ServerLevel serverLevel, SkillData data) {
        Vec3 center = data.fixedPosition;

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

    private void completeSkill(Player player, SkillData data) {
        Level level = player.level();

        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            Vec3 center = data.fixedPosition;

            serverLevel.playSound(null,
                    player.blockPosition(),
                    SoundEvents.EXPERIENCE_ORB_PICKUP,
                    SoundSource.PLAYERS,
                    1.0f,
                    0.5f
            );

            for (int i = 0; i < 20; i++) {
                double angle = serverLevel.random.nextDouble() * Math.PI * 2;
                double distance = serverLevel.random.nextDouble() * 0.8;
                double x = center.x + distance * Math.cos(angle);
                double z = center.z + distance * Math.sin(angle);
                double y = center.y + (serverLevel.random.nextDouble() - 0.5) * 0.8;

                serverLevel.sendParticles(ParticleTypes.GLOW,
                        x, y, z,
                        1, 0, 0.05, 0, 0.05);
            }

            ItemStack spatialStone = new ItemStack(ModItems.SPATIAL_STONE_TIER_1.get(), 1);
            ItemEntity droppedItem = new ItemEntity(level,
                    center.x, center.y, center.z,
                    spatialStone);
            droppedItem.setDefaultPickUpDelay();
            level.addFreshEntity(droppedItem);

            if (data.floatingItem != null && data.floatingItem.isAlive()) {
                data.floatingItem.discard();
                data.floatingItem = null;
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
            data.floatingItem = null;
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