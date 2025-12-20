package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

import static net.thejadeproject.ascension.util.ModTags.Blocks.DESTRUCTIBLE_BLOCKS;

public class TabletOfDestructionHeaven extends Item {
    private static final String DROP_BLOCKS_TAG = "DropBlocks";
    private static final int COOLDOWN_TICKS = 100; // 5 seconds (20 ticks/second)

    public TabletOfDestructionHeaven(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        if (level == null || player == null) return InteractionResult.PASS;
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        BlockState clickedState = level.getBlockState(pos);

        // Keep shift-click for chest/barrel linking
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide && (clickedState.getBlock() instanceof ChestBlock || clickedState.getBlock() instanceof BarrelBlock) && level.getBlockEntity(pos) instanceof Container) {
                CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
                CompoundTag tag = customData.copyTag();
                if (tag.contains("LinkedChestX") &&
                        tag.getInt("LinkedChestX") == pos.getX() &&
                        tag.getInt("LinkedChestY") == pos.getY() &&
                        tag.getInt("LinkedChestZ") == pos.getZ() &&
                        tag.getString("LinkedDimension").equals(level.dimension().location().toString())) {
                    // Unlink
                    tag.remove("LinkedChestX");
                    tag.remove("LinkedChestY");
                    tag.remove("LinkedChestZ");
                    tag.remove("LinkedDimension");
                    player.displayClientMessage(Component.translatable("item.ascension.tablet_of_destruction_heaven.unlinked"), true);
                } else {
                    // Link
                    tag.putInt("LinkedChestX", pos.getX());
                    tag.putInt("LinkedChestY", pos.getY());
                    tag.putInt("LinkedChestZ", pos.getZ());
                    tag.putString("LinkedDimension", level.dimension().location().toString());
                    String blockType = clickedState.getBlock() instanceof ChestBlock ? "chest" : "barrel";
                    player.displayClientMessage(Component.translatable("item.ascension.tablet_of_destruction_heaven.linked", blockType, pos.getX(), pos.getY(), pos.getZ()), true);
                }
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        // Check cooldown using Minecraft's system
        if (player.getCooldowns().isOnCooldown(this)) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("item.ascension.tablet_of_destruction_heaven.cooldown"), true);
            }
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide) {
            // Set cooldown using Minecraft's system
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);

            Vec3 lookVec = player.getViewVector(1.0F);
            Vec3 playerPos = player.position();
            CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            boolean dropBlocks = customData.getUnsafe().getBoolean(DROP_BLOCKS_TAG);
            BlockPos linkedChestPos = null;
            String linkedDimensionString = null;
            if (customData.getUnsafe().contains("LinkedChestX")) {
                CompoundTag tag = customData.getUnsafe();
                linkedChestPos = new BlockPos(
                        tag.getInt("LinkedChestX"),
                        tag.getInt("LinkedChestY"),
                        tag.getInt("LinkedChestZ")
                );
                linkedDimensionString = tag.getString("LinkedDimension");
            }
            this.clearArea(level, pos, lookVec, playerPos, dropBlocks, linkedChestPos, linkedDimensionString);
            stack.shrink(1);
            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.ITEM_BREAK, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return InteractionResult.SUCCESS;
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

        // Create colored tooltip for drop blocks
        Component status = Component.literal(String.valueOf(dropBlocks))
                .withStyle(dropBlocks ? ChatFormatting.GREEN : ChatFormatting.RED);
        tooltipComponents.add(Component.translatable("item.ascension.tablet_of_destruction_heaven.drop_blocks").append(status));
        tooltipComponents.add(Component.translatable("item.ascension.tablet_of_destruction_heaven.toggle_mode").withStyle(ChatFormatting.GRAY));
            tooltipComponents.add(Component.translatable("item.ascension.tablet_of_destruction_heaven.link_instruction").withStyle(ChatFormatting.GRAY));

        // Add linked chest or barrel info if present
        if (customData != null && customData.getUnsafe().contains("LinkedChestX")) {
            CompoundTag tag = customData.getUnsafe();
            int x = tag.getInt("LinkedChestX");
            int y = tag.getInt("LinkedChestY");
            int z = tag.getInt("LinkedChestZ");

            // Get block type
            String blockType = "container";
            if (context.level() != null) {
                BlockState state = context.level().getBlockState(new BlockPos(x, y, z));
                if (state.getBlock() instanceof ChestBlock) {
                    blockType = "chest";
                } else if (state.getBlock() instanceof BarrelBlock) {
                    blockType = "barrel";
                }
            }

            // Only show block type and coordinates, not dimension
            tooltipComponents.add(Component.translatable("item.ascension.tablet_of_destruction_heaven.linked_tooltip",
                    Component.translatable("item.ascension.tablet_of_destruction_heaven." + blockType),
                    Component.translatable("ascension.tooltip.tablet.coordinates", x, y, z)));
        }
    }

    // Server-side method to toggle drop mode
    public static void toggleDropModeServer(ItemStack stack, ServerPlayer player) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();
        boolean currentValue = tag.getBoolean(DROP_BLOCKS_TAG);
        tag.putBoolean(DROP_BLOCKS_TAG, !currentValue);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

        // Create colored message
        Component status = Component.literal(String.valueOf(!currentValue))
                .withStyle(!currentValue ? ChatFormatting.GREEN : ChatFormatting.RED);
        player.displayClientMessage(Component.translatable("item.ascension.tablet_of_destruction_heaven.drop_blocks").append(status), true);
    }

    private void clearArea(Level level, BlockPos startPos, Vec3 direction, Vec3 playerPos, boolean dropBlocks, BlockPos linkedChestPos, String linkedDimensionString) {
        if (level == null || level.isClientSide()) return; // Prevent client-side execution
        int width = 4;
        int height = 7;
        int depth = 22;
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

            if (level.getBlockState(torchPos).isAir() && level.getBlockState(belowPos).isFaceSturdy(level, belowPos, Direction.UP)) {
                level.setBlock(torchPos, Blocks.TORCH.defaultBlockState(), 3);
            }
        }

        // Handle dropping or inserting collected blocks
        if (dropBlocks && !droppedBlocks.isEmpty()) {
            Container container = null;
            if (linkedChestPos != null && linkedDimensionString != null && linkedDimensionString.equals(level.dimension().location().toString())) {
                BlockState state = level.getBlockState(linkedChestPos);
                if (state != null && (state.getBlock() instanceof ChestBlock || state.getBlock() instanceof BarrelBlock)) {
                    if (state.getBlock() instanceof ChestBlock) {
                        ChestType chestType = state.getValue(ChestBlock.TYPE);
                        if (chestType != ChestType.SINGLE) {
                            Direction facing = state.getValue(ChestBlock.FACING);
                            Direction offsetDir = chestType == ChestType.LEFT ? facing.getClockWise() : facing.getCounterClockWise();
                            BlockPos otherPos = linkedChestPos.relative(offsetDir);
                            if (level.getBlockState(otherPos).getBlock() instanceof ChestBlock) {
                                ChestBlockEntity thisChest = (ChestBlockEntity) level.getBlockEntity(linkedChestPos);
                                ChestBlockEntity otherChest = (ChestBlockEntity) level.getBlockEntity(otherPos);
                                if (thisChest != null && otherChest != null) {
                                    // Order them correctly: left first
                                    if (chestType == ChestType.RIGHT) {
                                        container = new CompoundContainer(otherChest, thisChest);
                                    } else {
                                        container = new CompoundContainer(thisChest, otherChest);
                                    }
                                }
                            }
                        } else {
                            if (level.getBlockEntity(linkedChestPos) instanceof Container cont) {
                                container = cont;
                            }
                        }
                    } else if (state.getBlock() instanceof BarrelBlock) {
                        if (level.getBlockEntity(linkedChestPos) instanceof Container cont) {
                            container = cont;
                        }
                    }
                }
            }
            Vec3 dropPos = playerPos.add(direction.x, 0, direction.z); // One block in front, same y-level
            for (BlockStatePos blockData : droppedBlocks) {
                BlockPos blockPos = blockData.pos;
                BlockState state = blockData.state; // Use the captured original state
                ItemStack drop = state.getBlock().getCloneItemStack(level, blockPos, state); // Get the dropped item
                if (!drop.isEmpty()) {
                    if (container != null) {
                        insertIntoContainer(container, drop);
                    }
                    if (!drop.isEmpty()) { // If there's remaining after insertion (or no container), drop as entity
                        ItemEntity itemEntity = new ItemEntity(
                                (ServerLevel) level, dropPos.x, dropPos.y, dropPos.z, drop.copy());
                        level.addFreshEntity(itemEntity);
                    }
                }
            }
            if (container != null) {
                container.setChanged();
            }
        }
    }

    private void insertIntoContainer(Container container, ItemStack stack) {
        if (stack.isEmpty() || container == null) return;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack slot = container.getItem(i);
            if (slot.isEmpty()) {
                container.setItem(i, stack.copy());
                stack.setCount(0);
                return;
            } else if (ItemStack.isSameItemSameComponents(slot, stack)) {
                int space = slot.getMaxStackSize() - slot.getCount();
                int add = Math.min(space, stack.getCount());
                slot.grow(add);
                stack.shrink(add);
                if (stack.isEmpty()) {
                    return;
                }
            }
        }
        // If we exit the loop, any remaining stack will be handled outside (dropped)
    }

    private boolean shouldRemoveBlock(Level level, BlockPos pos) {
        if (level == null) return false;
        BlockState state = level.getBlockState(pos);
        return state != null && state.is(DESTRUCTIBLE_BLOCKS);
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