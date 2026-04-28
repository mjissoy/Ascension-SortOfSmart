package net.thejadeproject.ascension.common.items.artifacts.bases;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.thejadeproject.ascension.util.ModTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTabletOfDestruction extends Item {
    private static final String DROP_BLOCKS_TAG = "DropBlocks";
    protected static final String LINKED_POS_TAG = "LinkedPos";
    protected static final String LINKED_DIMENSION_TAG = "LinkedDimension";

    public BaseTabletOfDestruction(Properties properties) {
        super(properties);
    }

    // Abstract methods for subclass configuration
    protected abstract int getCooldownTicks();
    protected abstract int getWidth();
    protected abstract int getHeight();
    protected abstract int getDepth();
    protected abstract boolean supportsDropBlocks();
    protected abstract boolean supportsContainerLinking();

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;

        ItemStack stack = context.getItemInHand();
        BlockPos clickedPos = context.getClickedPos();

        // Container linking on shift-click (only for supporting tablets)
        if (supportsContainerLinking() && player.isShiftKeyDown()) {
            return handleContainerLinking(level, player, stack, clickedPos);
        }

        // Check cooldown
        if (player.getCooldowns().isOnCooldown(this)) {
            if (!level.isClientSide) {
                player.displayClientMessage(getCooldownMessage(), true);
            }
            return InteractionResult.FAIL;
        }

        // Server-side execution
        if (!level.isClientSide) {
            player.getCooldowns().addCooldown(this, getCooldownTicks());

            Direction direction = player.getDirection();
            boolean dropBlocks = supportsDropBlocks() && isDropBlocksEnabled(stack);

            // Get linked container info if supported
            BlockPos linkedPos = null;
            String linkedDimension = null;
            if (supportsContainerLinking()) {
                var linkData = getLinkedContainer(stack);
                linkedPos = linkData.pos();
                linkedDimension = linkData.dimension();
            }

            // Clear area
            clearArea((ServerLevel) level, clickedPos, direction, player.position(),
                    dropBlocks, linkedPos, linkedDimension);

            // Consume item (unless in creative)
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            level.playSound(null, clickedPos, net.minecraft.sounds.SoundEvents.ITEM_BREAK,
                    net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        return InteractionResult.SUCCESS;
    }

    private InteractionResult handleContainerLinking(Level level, Player player, ItemStack stack, BlockPos pos) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockState state = level.getBlockState(pos);

        // Verify block is in linkable containers tag
        if (!state.is(ModTags.Blocks.LINKABLE_CONTAINERS)) {
            player.displayClientMessage(Component.translatable("item.ascension.tablet.link_invalid"), true);
            return InteractionResult.FAIL;
        }

        // Check for item handler capability at this position - NeoForge 1.21.1 returns IItemHandler directly
        IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
        if (itemHandler == null) {
            player.displayClientMessage(Component.translatable("item.ascension.tablet.link_invalid"), true);
            return InteractionResult.FAIL;
        }

        var currentLink = getLinkedContainer(stack);

        // Toggle linking
        if (currentLink.pos() != null && currentLink.pos().equals(pos) &&
                currentLink.dimension().equals(level.dimension().location().toString())) {
            clearLinkedContainer(stack);
            player.displayClientMessage(Component.translatable("item.ascension.tablet.unlink_success"), true);
        } else {
            setLinkedContainer(stack, pos, level.dimension().location().toString());
            player.displayClientMessage(Component.translatable("item.ascension.tablet.link_success",
                    state.getBlock().getName().getString(), pos.getX(), pos.getY(), pos.getZ()), true);
        }

        return InteractionResult.SUCCESS;
    }

    private void clearArea(ServerLevel level, BlockPos startPos, Direction direction, Vec3 playerPos,
                           boolean dropBlocks, @Nullable BlockPos linkedContainerPos, @Nullable String linkedDimension) {
        int width = getWidth();
        int height = getHeight();
        int depth = getDepth();
        int dx = direction.getStepX();
        int dz = direction.getStepZ();

        // Safety: check world bounds
        if (!isAreaSafe(level, startPos, width, height, depth, dx, dz)) {
            return;
        }

        List<BlockStatePos> blocksToDrop = new ArrayList<>();

        // Remove blocks
        for (int x = -width; x <= width; x++) {
            for (int z = 0; z <= depth; z++) {
                BlockPos currentPos = startPos.offset(dx * z + dz * x, 0, dz * z + dx * x);

                for (int y = -1; y <= height; y++) {
                    BlockPos targetPos = currentPos.above(y);

                    if (shouldRemoveBlock(level, targetPos)) {
                        BlockState state = level.getBlockState(targetPos);

                        if (dropBlocks) {
                            blocksToDrop.add(new BlockStatePos(targetPos, state));
                        }

                        level.removeBlock(targetPos, false);
                    }
                }
            }
        }

        // Place torches on tunnel floor
        placeTorches(level, startPos, direction, depth);

        // Handle drops
        if (dropBlocks && !blocksToDrop.isEmpty()) {
            handleBlockDrops(level, blocksToDrop, playerPos, direction, linkedContainerPos, linkedDimension);
        }
    }

    private boolean isAreaSafe(ServerLevel level, BlockPos startPos, int width, int height, int depth, int dx, int dz) {
        for (int x = -width; x <= width; x++) {
            for (int z = 0; z <= depth; z++) {
                for (int y = -1; y <= height; y++) {
                    if (!level.isInWorldBounds(startPos.offset(dx * z + dz * x, y, dz * z + dx * x))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void placeTorches(ServerLevel level, BlockPos startPos, Direction direction, int depth) {
        int dx = direction.getStepX();
        int dz = direction.getStepZ();

        for (int z = 0; z <= depth; z += 6) {
            // Torch at floor level (y=-1), placed on uncleared base block (y=-2)
            BlockPos torchPos = startPos.offset(dx * z, -1, dz * z);
            BlockPos floorPos = torchPos.below();

            if (level.getBlockState(torchPos).isAir() &&
                    level.getBlockState(floorPos).isFaceSturdy(level, floorPos, Direction.UP)) {
                level.setBlock(torchPos, Blocks.TORCH.defaultBlockState(), Block.UPDATE_ALL);
            }
        }
    }

    private void handleBlockDrops(ServerLevel level, List<BlockStatePos> blocks, Vec3 playerPos,
                                  Direction direction, @Nullable BlockPos linkedContainerPos,
                                  @Nullable String linkedDimension) {
        Vec3 dropPos = playerPos.add(direction.getStepX(), 0, direction.getStepZ());

        // Get linked item handler if available and in correct dimension - NeoForge returns directly
        IItemHandler linkedHandler = null;
        if (linkedContainerPos != null && linkedDimension != null &&
                linkedDimension.equals(level.dimension().location().toString())) {
            linkedHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, linkedContainerPos, null);
        }

        // Process drops
        for (BlockStatePos blockData : blocks) {
            var drops = Block.getDrops(blockData.state, level, blockData.pos,
                    level.getBlockEntity(blockData.pos), null, ItemStack.EMPTY);

            for (ItemStack drop : drops) {
                // Insert into linked container first
                if (linkedHandler != null) {
                    drop = insertIntoItemHandler(linkedHandler, drop);
                    if (drop.isEmpty()) continue;
                }

                // Drop remaining items
                level.addFreshEntity(new net.minecraft.world.entity.item.ItemEntity(
                        level, dropPos.x, dropPos.y, dropPos.z, drop));
            }
        }
    }

    private ItemStack insertIntoItemHandler(IItemHandler handler, ItemStack stack) {
        if (stack.isEmpty() || handler == null) return stack;

        ItemStack remaining = stack.copy();
        for (int slot = 0; slot < handler.getSlots(); slot++) {
            remaining = handler.insertItem(slot, remaining, false);
            if (remaining.isEmpty()) break;
        }
        return remaining;
    }

    // Linked container data management
    protected record LinkedContainerData(@Nullable BlockPos pos, @Nullable String dimension) {}

    protected LinkedContainerData getLinkedContainer(ItemStack stack) {
        var customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        var tag = customData.getUnsafe();

        if (tag.contains(LINKED_POS_TAG)) {
            long posLong = tag.getLong(LINKED_POS_TAG);
            String dimension = tag.getString(LINKED_DIMENSION_TAG);
            return new LinkedContainerData(BlockPos.of(posLong), dimension);
        }
        return new LinkedContainerData(null, null);
    }

    protected void setLinkedContainer(ItemStack stack, BlockPos pos, String dimension) {
        var customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        var tag = customData.copyTag();
        tag.putLong(LINKED_POS_TAG, pos.asLong());
        tag.putString(LINKED_DIMENSION_TAG, dimension);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    protected void clearLinkedContainer(ItemStack stack) {
        var customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        var tag = customData.copyTag();
        tag.remove(LINKED_POS_TAG);
        tag.remove(LINKED_DIMENSION_TAG);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    /**
     * Checks if drop blocks mode is enabled
     */
    protected boolean isDropBlocksEnabled(ItemStack stack) {
        if (!supportsDropBlocks()) return false;
        var customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return customData.getUnsafe().getBoolean(DROP_BLOCKS_TAG);
    }

    /**
     * Server-side toggle for drop blocks mode
     */
    public static void toggleDropModeServer(ItemStack stack, ServerPlayer player) {
        var customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        var tag = customData.copyTag();
        boolean currentValue = tag.getBoolean(DROP_BLOCKS_TAG);
        tag.putBoolean(DROP_BLOCKS_TAG, !currentValue);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

        Component status = Component.literal(String.valueOf(!currentValue))
                .withStyle(!currentValue ? ChatFormatting.GREEN : ChatFormatting.RED);
        player.displayClientMessage(Component.translatable("ascension.tablet.drop_blocks").append(status), true);
    }

    protected Component getCooldownMessage() {
        return Component.translatable("ascension.tablet.cooldown");
    }

    protected boolean shouldRemoveBlock(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).is(ModTags.Blocks.DESTRUCTIBLE_BLOCKS);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return supportsDropBlocks() && isDropBlocksEnabled(stack);
    }

    // Helper class
    protected static class BlockStatePos {
        final BlockPos pos;
        final BlockState state;

        BlockStatePos(BlockPos pos, BlockState state) {
            this.pos = pos;
            this.state = state;
        }
    }
}