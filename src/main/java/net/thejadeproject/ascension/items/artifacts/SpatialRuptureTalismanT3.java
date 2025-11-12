package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Random;

public class SpatialRuptureTalismanT3 extends Item {
    private static final int TELEPORT_RADIUS = 7500; // 7.5k blocks radius
    private static final int COOLDOWN_TICKS = 20 * 60 * 20; // 20 minutes in ticks
    private static final int MAX_ATTEMPTS = 100; // Increased attempts for better location finding

    private static final String GLOBAL_COOLDOWN_TAG = "SpatialRuptureCooldownT3";
    private static final String GLOBAL_COOLDOWN_TIME_TAG = "SpatialRuptureCooldownT3";

    public SpatialRuptureTalismanT3(Properties properties) {
        super(properties.stacksTo(16).rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (isOnGlobalCooldown(player)) {
            if (!level.isClientSide) {
                int remainingTicks = getRemainingGlobalCooldownTicks(player);
                int remainingSeconds = remainingTicks / 20;
                int minutes = remainingSeconds / 60;
                int seconds = remainingSeconds % 60;
                player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                        String.format("§cTalisman on cooldown! %d:%02d remaining", minutes, seconds)), true);
            }
            return InteractionResultHolder.fail(itemstack);
        }

        if (level.isClientSide) {
            return InteractionResultHolder.consume(itemstack);
        }

        ServerPlayer serverPlayer = (ServerPlayer) player;
        ServerLevel serverLevel = (ServerLevel) level;

        BlockPos safePos = findSafeTeleportLocation(serverLevel, serverPlayer.blockPosition());

        if (safePos != null) {
            setGlobalCooldown(serverPlayer, COOLDOWN_TICKS);

            player.getCooldowns().addCooldown(this, 10);

            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }

            serverLevel.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
            serverLevel.sendParticles(ParticleTypes.PORTAL,
                    player.getX(), player.getY() + 1, player.getZ(),
                    50, 0.5, 1, 0.5, 0.1);

            teleportPlayer(serverPlayer, serverLevel, safePos);

            return InteractionResultHolder.success(itemstack);
        } else {
            player.displayClientMessage(net.minecraft.network.chat.Component.literal("§cNo safe teleport location found!"), true);
            return InteractionResultHolder.fail(itemstack);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player) {
            updateGlobalCooldown(player);

            int remainingTicks = getRemainingGlobalCooldownTicks(player);
            if (remainingTicks > 0) {
                player.getCooldowns().addCooldown(this, Math.min(remainingTicks, 200));
            } else if (player.getCooldowns().isOnCooldown(this)) {
                player.getCooldowns().removeCooldown(this);
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return !isItemOnCooldown(stack) && super.isFoil(stack);
    }

    private boolean isItemOnCooldown(ItemStack stack) {
        return false;
    }

    private boolean isOnGlobalCooldown(Player player) {
        return getRemainingGlobalCooldownTicks(player) > 0;
    }

    private int getRemainingGlobalCooldownTicks(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        if (persistentData.contains(GLOBAL_COOLDOWN_TAG)) {
            return persistentData.getInt(GLOBAL_COOLDOWN_TAG);
        }
        return 0;
    }

    private void setGlobalCooldown(Player player, int cooldownTicks) {
        CompoundTag persistentData = player.getPersistentData();
        persistentData.putInt(GLOBAL_COOLDOWN_TAG, cooldownTicks);
        persistentData.putLong(GLOBAL_COOLDOWN_TIME_TAG, System.currentTimeMillis());
    }

    private void updateGlobalCooldown(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        if (persistentData.contains(GLOBAL_COOLDOWN_TAG)) {
            int remainingCooldown = persistentData.getInt(GLOBAL_COOLDOWN_TAG);
            if (remainingCooldown > 0) {
                long lastUpdateTime = persistentData.getLong(GLOBAL_COOLDOWN_TIME_TAG);
                long currentTime = System.currentTimeMillis();
                long timePassed = (currentTime - lastUpdateTime) / 50;

                if (timePassed > 0) {
                    remainingCooldown = Math.max(0, remainingCooldown - (int) timePassed);
                    persistentData.putInt(GLOBAL_COOLDOWN_TAG, remainingCooldown);
                    persistentData.putLong(GLOBAL_COOLDOWN_TIME_TAG, currentTime);
                }
            }
        }
    }

    private BlockPos findSafeTeleportLocation(ServerLevel level, BlockPos center) {
        Random random = new Random();
        WorldBorder border = level.getWorldBorder();

        // Try furthest distances first, then work our way closer
        for (int distanceMultiplier = 10; distanceMultiplier >= 1; distanceMultiplier--) {
            int currentRadius = (TELEPORT_RADIUS * distanceMultiplier) / 10;

            for (int i = 0; i < MAX_ATTEMPTS / 10; i++) {
                // Generate points on the circumference of circles with decreasing radius
                double angle = random.nextDouble() * 2 * Math.PI;
                int x = center.getX() + (int)(currentRadius * Math.cos(angle));
                int z = center.getZ() + (int)(currentRadius * Math.sin(angle));

                // Add some random variation to avoid always being exactly on the edge
                int variation = currentRadius / 20; // 5% variation
                x += random.nextInt(variation * 2) - variation;
                z += random.nextInt(variation * 2) - variation;

                if (!border.isWithinBounds(x, z)) {
                    continue;
                }

                BlockPos surfacePos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x, 0, z));
                BlockPos safePos = findSafeGroundPosition(level, surfacePos);

                if (safePos != null) {
                    return safePos;
                }
            }
        }

        // Fallback: original random search if edge search fails
        for (int i = 0; i < MAX_ATTEMPTS / 2; i++) {
            int x = center.getX() + random.nextInt(TELEPORT_RADIUS * 2) - TELEPORT_RADIUS;
            int z = center.getZ() + random.nextInt(TELEPORT_RADIUS * 2) - TELEPORT_RADIUS;

            if (!border.isWithinBounds(x, z)) {
                continue;
            }

            BlockPos surfacePos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x, 0, z));
            BlockPos safePos = findSafeGroundPosition(level, surfacePos);

            if (safePos != null) {
                return safePos;
            }
        }

        return null;
    }

    private BlockPos findSafeGroundPosition(ServerLevel level, BlockPos surfacePos) {
        for (int y = surfacePos.getY(); y > level.getMinBuildHeight() + 5; y--) {
            BlockPos checkPos = new BlockPos(surfacePos.getX(), y, surfacePos.getZ());
            BlockPos standingPos = checkPos.above();
            BlockPos headPos = standingPos.above();

            if (isSolidGround(level, checkPos) &&
                    isSafeStandingPosition(level, standingPos) &&
                    !isDangerous(level, checkPos) &&
                    !isDangerous(level, standingPos) &&
                    !isDangerous(level, headPos) &&
                    !isCaveLike(level, standingPos)) {
                return standingPos;
            }
        }
        return null;
    }

    private boolean isSolidGround(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).isSolid() &&
                level.getBlockState(pos).isCollisionShapeFullBlock(level, pos);
    }

    private boolean isSafeStandingPosition(ServerLevel level, BlockPos pos) {
        BlockPos aboveHead = pos.above();
        return !level.getBlockState(pos).isSolid() &&
                !level.getBlockState(aboveHead).isSolid() &&
                level.getBlockState(pos).getFluidState().isEmpty() &&
                level.getBlockState(aboveHead).getFluidState().isEmpty();
    }

    private boolean isDangerous(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).is(net.minecraft.world.level.block.Blocks.LAVA) ||
                level.getBlockState(pos).is(net.minecraft.world.level.block.Blocks.FIRE) ||
                level.getBlockState(pos).is(net.minecraft.world.level.block.Blocks.SOUL_FIRE) ||
                level.getBlockState(pos).is(net.minecraft.world.level.block.Blocks.CAMPFIRE) ||
                level.getBlockState(pos).is(net.minecraft.world.level.block.Blocks.MAGMA_BLOCK) ||
                level.getBlockState(pos).is(net.minecraft.world.level.block.Blocks.CACTUS);
    }

    private boolean isCaveLike(ServerLevel level, BlockPos pos) {
        int solidBlocksAbove = 0;
        for (int i = 1; i <= 5; i++) {
            if (level.getBlockState(pos.above(i)).isSolid()) {
                solidBlocksAbove++;
            }
        }
        return solidBlocksAbove > 2;
    }

    private void teleportPlayer(ServerPlayer player, ServerLevel level, BlockPos targetPos) {
        float yRot = player.getYRot();
        float xRot = player.getXRot();

        player.teleportTo(level,
                targetPos.getX() + 0.5,
                targetPos.getY(),
                targetPos.getZ() + 0.5,
                yRot, xRot);

        player.connection.resetPosition();
        player.setOnGround(true);
        player.hurtMarked = true;

        level.playSound(null, targetPos, SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
        level.sendParticles(ParticleTypes.PORTAL,
                targetPos.getX() + 0.5, targetPos.getY() + 1, targetPos.getZ() + 0.5,
                50, 0.5, 1, 0.5, 0.1);

        player.fallDistance = 0;

        player.displayClientMessage(net.minecraft.network.chat.Component.literal("§aTeleported to a random location!"), true);
    }
}