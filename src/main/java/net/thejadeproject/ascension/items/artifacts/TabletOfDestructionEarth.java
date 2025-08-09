package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class TabletOfDestructionEarth extends Item {
    private static final String DROP_BLOCKS_TAG = "DropBlocks";
    private static final int COOLDOWN_TICKS = 200; // 10 seconds (20 ticks/second)

    public TabletOfDestructionEarth(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        Player player = context.getPlayer();
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();

        // Check cooldown using Minecraft's system
        if (player != null && player.getCooldowns().isOnCooldown(this)) {
            if (!level.isClientSide) {
                player.sendSystemMessage(Component.literal("Item is on cooldown!"));
            }
            return InteractionResult.FAIL;
        }

        BlockPos pos = context.getClickedPos();
        if (!level.isClientSide && player != null) {
            // Set cooldown using Minecraft's system
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);

            Vec3 lookVec = player.getViewVector(1.0F);
            Vec3 playerPos = player.position();
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            boolean dropBlocks = customData != null && customData.getUnsafe().getBoolean(DROP_BLOCKS_TAG);
            this.clearArea(level, pos, lookVec, playerPos, dropBlocks);
            stack.shrink(1);
            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.ITEM_BREAK, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
                CompoundTag tag = customData.copyTag();
                boolean currentValue = tag.getBoolean(DROP_BLOCKS_TAG);
                tag.putBoolean(DROP_BLOCKS_TAG, !currentValue);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

                // Create colored message
                Component status = Component.literal(String.valueOf(!currentValue))
                        .withStyle(!currentValue ? ChatFormatting.GREEN : ChatFormatting.RED);
                player.sendSystemMessage(Component.literal("Drop Blocks = ").append(status));
            }
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        return customData != null && customData.getUnsafe().getBoolean(DROP_BLOCKS_TAG);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        boolean dropBlocks = customData != null && customData.getUnsafe().getBoolean(DROP_BLOCKS_TAG);

        // Create colored tooltip
        Component status = Component.literal(String.valueOf(dropBlocks))
                .withStyle(dropBlocks ? ChatFormatting.GREEN : ChatFormatting.RED);
        tooltipComponents.add(Component.literal("Drop Blocks = ").append(status));
    }

    private void clearArea(Level level, BlockPos startPos, Vec3 direction, Vec3 playerPos, boolean dropBlocks) {
        if (level.isClientSide) return; // Prevent client-side execution
        int width = 3;
        int height = 5;
        int depth = 18;
        int dx = Math.abs(direction.x) > Math.abs(direction.z) ? (int) Math.signum(direction.x) : 0;
        int dz = Math.abs(direction.z) > Math.abs(direction.x) ? (int) Math.signum(direction.z) : 0;

        // Collect and remove blocks first
        List<BlockStatePos> droppedBlocks = new ArrayList<>();

        for (int x = -width; x <= width; ++x) {
            for (int z = 0; z <= depth; ++z) {
                BlockPos currentPos = startPos.offset(dx * z + dz * x, 0, dz * z + dx * x);

                for (int y = -1; y <= height; ++y) {
                    BlockPos posToClear = currentPos.offset(0, y, 0);
                    if (this.shouldRemoveBlock(level, posToClear)) {
                        BlockState state = level.getBlockState(posToClear);
                        level.setBlock(posToClear, Blocks.AIR.defaultBlockState(), 3);
                        if (dropBlocks) {
                            droppedBlocks.add(new BlockStatePos(posToClear, state)); // Store position and original state
                        }
                    }
                }
            }
        }

        // Place torches after block removal
        for (int z = 0; z <= depth; z += 6) {
            BlockPos basePos = startPos.offset(dx * z, -2, dz * z);
            BlockPos torchPos = basePos.above(); // Place torch one block above the base
            BlockPos belowPos = torchPos.below(); // Check the block below

            if (level.getBlockState(torchPos).isAir() && level.getBlockState(belowPos).isFaceSturdy(level, belowPos, net.minecraft.core.Direction.UP)) {
                level.setBlock(torchPos, Blocks.TORCH.defaultBlockState(), 3);
            }
        }

        // Drop collected blocks one block in front of the player if dropBlocks is true
        if (dropBlocks && !droppedBlocks.isEmpty()) {
            Vec3 dropPos = playerPos.add(direction.x, 0, direction.z); // One block in front, same y-level
            for (BlockStatePos blockData : droppedBlocks) {
                BlockPos blockPos = blockData.pos;
                BlockState state = blockData.state; // Use the captured original state
                ItemStack drop = state.getBlock().getCloneItemStack(level, blockPos, state); // Get the dropped item
                if (!drop.isEmpty()) {
                    net.minecraft.world.entity.item.ItemEntity itemEntity = new net.minecraft.world.entity.item.ItemEntity(
                            (ServerLevel) level, dropPos.x, dropPos.y, dropPos.z, drop);
                    level.addFreshEntity(itemEntity);
                }
            }
        }
    }

    private boolean shouldRemoveBlock(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.is(Blocks.STONE)
                || state.is(Blocks.DIRT)
                || state.is(Blocks.GRAVEL)
                || state.is(Blocks.SAND)
                || state.is(Blocks.COBBLESTONE)
                || state.is(Blocks.ANDESITE)
                || state.is(Blocks.DIORITE)
                || state.is(Blocks.GRANITE)
                || state.is(Blocks.DEEPSLATE)
                || state.is(Blocks.TUFF);
    }

    // Helper class to store BlockPos and BlockState together
    private static class BlockStatePos {
        final BlockPos pos;
        final BlockState state;

        BlockStatePos(BlockPos pos, BlockState state) {
            this.pos = pos;
            this.state = state;
        }
    }
}