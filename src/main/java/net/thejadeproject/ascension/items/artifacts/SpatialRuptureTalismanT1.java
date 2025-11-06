package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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

import java.util.Random;

public class SpatialRuptureTalismanT1 extends Item {
    private static final int TELEPORT_RADIUS = 2500; // 2.5k blocks radius
    private static final int COOLDOWN_TICKS = 70 * 60 * 20; // 70 minutes in ticks
    private static final int MAX_ATTEMPTS = 50; // Max attempts to find safe location

    public SpatialRuptureTalismanT1(Properties properties) {
        super(properties.stacksTo(16).rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            ServerLevel serverLevel = (ServerLevel) level;

            // Apply cooldown
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);

            // Find safe teleport location
            BlockPos safePos = findSafeTeleportLocation(serverLevel, serverPlayer.blockPosition());

            if (safePos != null) {
                // Consume item
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                // Play teleport sound and particles at origin
                serverLevel.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                        SoundSource.PLAYERS, 1.0F, 1.0F);
                serverLevel.sendParticles(ParticleTypes.PORTAL,
                        player.getX(), player.getY() + 1, player.getZ(),
                        50, 0.5, 1, 0.5, 0.1);

                // Teleport player
                teleportPlayer(serverPlayer, serverLevel, safePos);

                return InteractionResultHolder.success(itemstack);
            } else {
                // Couldn't find safe location
                player.getCooldowns().removeCooldown(this);
                return InteractionResultHolder.fail(itemstack);
            }
        }

        return InteractionResultHolder.consume(itemstack);
    }

    private BlockPos findSafeTeleportLocation(ServerLevel level, BlockPos center) {
        Random random = new Random();
        WorldBorder border = level.getWorldBorder();

        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            // Generate random coordinates within radius
            int x = center.getX() + random.nextInt(TELEPORT_RADIUS * 2) - TELEPORT_RADIUS;
            int z = center.getZ() + random.nextInt(TELEPORT_RADIUS * 2) - TELEPORT_RADIUS;

            // Check if within world border
            if (!border.isWithinBounds(x, z)) {
                continue;
            }

            // Find safe Y position
            BlockPos safePos = findSafeYPosition(level, new BlockPos(x, level.getSeaLevel(), z));
            if (safePos != null) {
                return safePos;
            }
        }

        return null; // No safe location found after max attempts
    }

    private BlockPos findSafeYPosition(ServerLevel level, BlockPos pos) {
        // Start from sea level and search downwards for solid ground
        for (int y = Math.min(pos.getY(), level.getMaxBuildHeight() - 1); y > level.getMinBuildHeight(); y--) {
            BlockPos checkPos = new BlockPos(pos.getX(), y, pos.getZ());
            BlockPos abovePos = checkPos.above();

            // Check if this position is safe to stand on
            if (isSolidGround(level, checkPos) &&
                    isSafeStandingPosition(level, abovePos) &&
                    !isInLava(level, checkPos) &&
                    !isInLava(level, abovePos)) {
                return abovePos; // Return position where player should stand
            }
        }

        return null;
    }

    private boolean isSolidGround(ServerLevel level, BlockPos pos) {
        // Check if block is solid and can be stood upon
        return level.getBlockState(pos).isSolid() &&
                level.getBlockState(pos).isCollisionShapeFullBlock(level, pos);
    }

    private boolean isSafeStandingPosition(ServerLevel level, BlockPos pos) {
        // Check if standing position is safe (not solid and not lava)
        BlockPos aboveHead = pos.above();
        return !level.getBlockState(pos).isSolid() &&
                !level.getBlockState(aboveHead).isSolid() &&
                !isInLava(level, pos) &&
                !isInLava(level, aboveHead);
    }

    private boolean isInLava(ServerLevel level, BlockPos pos) {
        // Check if position contains lava
        return level.getBlockState(pos).liquid() ||
                level.getBlockState(pos).getBlock() == net.minecraft.world.level.block.Blocks.LAVA;
    }

    private void teleportPlayer(ServerPlayer player, ServerLevel level, BlockPos targetPos) {
        // Teleport the player
        player.teleportTo(level,
                targetPos.getX() + 0.5,
                targetPos.getY(),
                targetPos.getZ() + 0.5,
                player.getYRot(),
                player.getXRot());

        // Play teleport sound and particles at destination
        level.playSound(null, targetPos, SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
        level.sendParticles(ParticleTypes.PORTAL,
                targetPos.getX() + 0.5, targetPos.getY() + 1, targetPos.getZ() + 0.5,
                50, 0.5, 1, 0.5, 0.1);

        // Prevent fall damage
        player.fallDistance = 0;
    }
}
