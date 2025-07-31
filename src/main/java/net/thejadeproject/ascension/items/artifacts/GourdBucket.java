/*package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;
import java.util.List;

public class GourdBucket extends Item {
    private static final int CAPACITY = 50 * FluidType.BUCKET_VOLUME;

    public GourdBucket(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        Direction direction = context.getClickedFace();

        if (player == null) {
            return InteractionResult.PASS;
        }

        IFluidHandlerItem fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null) {
            return InteractionResult.FAIL;
        }

        BlockState state = level.getBlockState(pos);
        FluidStack fluidInTank = fluidHandler.getFluidInTank(0);

        // Try to fill from world
        if (state.getFluidState().getType() != Fluids.EMPTY) {
            // First click determines the filter
            if (fluidInTank.isEmpty()) {
                Fluid fluid = state.getFluidState().getType();
                if (state.getFluidState().isSource()) {
                    // Set filter and fill
                    FluidStack newFluid = new FluidStack(fluid, FluidType.BUCKET_VOLUME);
                    if (fluidHandler.fill(newFluid, IFluidHandlerItem.FluidAction.EXECUTE) > 0) {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
                        level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            // Subsequent clicks only fill if it matches the filter
            else if (state.getFluidState().getType() == fluidInTank.getFluid() &&
                    state.getFluidState().isSource()) {
                FluidStack newFluid = new FluidStack(fluidInTank.getFluid(), FluidType.BUCKET_VOLUME);
                if (fluidHandler.fill(newFluid, IFluidHandlerItem.FluidAction.EXECUTE) > 0) {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        // Try to empty into world
        else if (!fluidInTank.isEmpty() && fluidInTank.getAmount() >= FluidType.BUCKET_VOLUME) {
            BlockPos placePos = pos.relative(direction);
            if (level.mayInteract(player, placePos) && level.isEmptyBlock(placePos)) {
                var fluidBlock = fluidInTank.getFluid().defaultFluidState().createLegacyBlock();
                if (fluidBlock != null && !fluidBlock.isAir()) {
                    if (level.setBlock(placePos, fluidBlock, 11)) {
                        fluidHandler.drain(FluidType.BUCKET_VOLUME, IFluidHandlerItem.FluidAction.EXECUTE);
                        level.playSound(null, placePos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (handler != null) {
            FluidStack fluid = handler.getFluidInTank(0);
            if (!fluid.isEmpty()) {
                tooltip.add(Component.translatable(
                        "tooltip.ascension.fluid_amount",
                        fluid.getDisplayName().getString(),
                        fluid.getAmount() / 1000,
                        CAPACITY / 1000
                ));
            } else {
                tooltip.add(Component.translatable("tooltip.ascension.empty", CAPACITY / 1000));
            }
        }
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.setCapability(Capabilities.FluidHandler.ITEM, new GourdBucketFluidHandler(stack));
        return stack;
    }

    private static class GourdBucketFluidHandler extends FluidBucketWrapper {
        public GourdBucketFluidHandler(ItemStack container) {
            super(container);
        }

        @Override
        public int getTankCapacity(int tank) {
            return CAPACITY;
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            // Only allow one type of fluid at a time
            if (getFluidInTank(tank).isEmpty()) {
                return true;
            }
            return getFluidInTank(tank).getFluid() == stack.getFluid();
        }
    }
}*/