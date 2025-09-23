package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static net.thejadeproject.ascension.util.ModTags.Blocks.DESTRUCTIBLE_BLOCKS;

public class TabletOfDestructionHuman extends Item {
    private static final int COOLDOWN_TICKS = 400; // 20 seconds (20 ticks/second)

    public TabletOfDestructionHuman(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();

        if (player == null) {
            return InteractionResult.PASS;
        }

        // Check cooldown
        if (player.getCooldowns().isOnCooldown(this)) {
            if (!level.isClientSide) {
                player.sendSystemMessage(Component.literal("Ability is on cooldown!"));
            }
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide) {
            // Set cooldown
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);

            Vec3 lookVec = player.getViewVector(1.0F);
            this.clearArea(level, pos, lookVec);
            ItemStack itemStack = context.getItemInHand();
            itemStack.shrink(1);
            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.ITEM_BREAK, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return InteractionResult.SUCCESS;
    }

    private void clearArea(Level level, BlockPos startPos, Vec3 direction) {
        if (level.isClientSide) return; // Prevent client-side execution
        int width = 2;
        int height = 3;
        int depth = 15;
        int dx = Math.abs(direction.x) > Math.abs(direction.z) ? (int) Math.signum(direction.x) : 0;
        int dz = Math.abs(direction.z) > Math.abs(direction.x) ? (int) Math.signum(direction.z) : 0;

        for (int x = -width; x <= width; ++x) {
            for (int z = 0; z <= depth; ++z) {
                BlockPos currentPos = startPos.offset(dx * z + dz * x, 0, dz * z + dx * x);

                for (int y = -1; y <= height; ++y) {
                    BlockPos posToClear = currentPos.offset(0, y, 0);
                    if (this.shouldRemoveBlock(level, posToClear)) {
                        level.setBlock(posToClear, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }

        // Place torches every 6 blocks in the middle
        for (int z = 0; z <= depth; z += 6) {
            // Calculate the middle position along the tunnel
            BlockPos basePos = startPos.offset(dx * z, -2, dz * z);
            BlockPos torchPos = basePos.above(); // Place torch one block above the base
            BlockPos belowPos = torchPos.below(); // Check the block below

            // Debug: Ensure we're on the server
            if (level.getBlockState(torchPos).isAir() && level.getBlockState(belowPos).isFaceSturdy(level, belowPos, net.minecraft.core.Direction.UP)) {
                level.setBlock(torchPos, Blocks.TORCH.defaultBlockState(), 3);
            }
        }
    }

    private boolean shouldRemoveBlock(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.is(DESTRUCTIBLE_BLOCKS);
    }
}
