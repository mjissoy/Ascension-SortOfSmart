package net.thejadeproject.ascension.events.api;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class SpatialRuptureAPI {

    public static CompletableFuture<Boolean> randomTeleport(ServerPlayer player, ServerLevel world, int radius) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                BlockPos safePos = findSafeLocationInRadius(world, player.blockPosition(), radius);
                if (safePos != null) {
                    // Teleport the player on the main thread
                    world.getServer().execute(() -> {
                        teleportPlayer(player, world, safePos);
                    });
                    return true;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public static BlockPos findSafeLocationInRadius(ServerLevel level, BlockPos center, int radius) {
        Random random = new Random();
        WorldBorder border = level.getWorldBorder();

        // Try multiple attempts to find a safe location within the radius
        for (int i = 0; i < 50; i++) {
            int x = center.getX() + random.nextInt(radius * 2) - radius;
            int z = center.getZ() + random.nextInt(radius * 2) - radius;

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

    private static BlockPos findSafeGroundPosition(ServerLevel level, BlockPos surfacePos) {
        for (int y = surfacePos.getY(); y > level.getMinBuildHeight() + 5; y--) {
            BlockPos checkPos = new BlockPos(surfacePos.getX(), y, surfacePos.getZ());
            BlockPos standingPos = checkPos.above();
            BlockPos headPos = standingPos.above();

            if (isSolidGround(level, checkPos) &&
                    isSafeStandingPosition(level, standingPos) &&
                    !isDangerous(level, checkPos) &&
                    !isDangerous(level, standingPos) &&
                    !isDangerous(level, headPos)) {
                return standingPos;
            }
        }
        return null;
    }

    private static boolean isSolidGround(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).isSolid() &&
                level.getBlockState(pos).isCollisionShapeFullBlock(level, pos);
    }

    private static boolean isSafeStandingPosition(ServerLevel level, BlockPos pos) {
        BlockPos aboveHead = pos.above();
        return !level.getBlockState(pos).isSolid() &&
                !level.getBlockState(aboveHead).isSolid() &&
                level.getBlockState(pos).getFluidState().isEmpty() &&
                level.getBlockState(aboveHead).getFluidState().isEmpty();
    }

    private static boolean isDangerous(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).is(net.minecraft.world.level.block.Blocks.LAVA);
    }

    private static void teleportPlayer(ServerPlayer player, ServerLevel level, BlockPos targetPos) {
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
        player.fallDistance = 0;
    }
}