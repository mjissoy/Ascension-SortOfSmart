package net.thejadeproject.ascension.blocks.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.thejadeproject.ascension.Config;
import net.thejadeproject.ascension.blocks.entity.ModBlockEntities;
import net.thejadeproject.ascension.blocks.entity.PillCauldronLowHumanEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;

public class PillCauldronLowHumanBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 11, 14);
    public static final MapCodec<PillCauldronLowHumanBlock> CODEC = simpleCodec(PillCauldronLowHumanBlock::new);
    public static final DirectionProperty FACING;

    public PillCauldronLowHumanBlock(Properties properties) {
        super(properties.sound(SoundType.ANVIL).strength(3.5f, 4.5f).requiresCorrectToolForDrops());
        this.registerDefaultState((BlockState) ((BlockState) ((BlockState) this.stateDefinition.any()).setValue(FACING, Direction.NORTH)));
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
    }

    @NotNull
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.setValue(FACING, rotation.rotate((Direction) state.getValue(FACING)));
    }

    @NotNull
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation((Direction) state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PillCauldronLowHumanEntity(blockPos, blockState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState pState, Level plevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = plevel.getBlockEntity(pPos);
            if (blockEntity instanceof PillCauldronLowHumanEntity pillCauldronLowHumanEntity) {
                pillCauldronLowHumanEntity.drops();
            }
        }

        super.onRemove(pState, plevel, pPos, pNewState, pIsMoving);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos,
                                              Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof PillCauldronLowHumanEntity pillCauldronLowHumanEntity) {
                // Check if player is holding a heat-adding item from config
                Map<String, Integer> heatItems = Config.COMMON.getHeatItems();
                String itemId = pStack.getItem().builtInRegistryHolder().key().location().toString();

                if (heatItems.containsKey(itemId)) {
                    // Check if cauldron is already at maximum heat
                    int currentHeat = pillCauldronLowHumanEntity.getHeatLevel();
                    int maxHeat = pillCauldronLowHumanEntity.getMaxHeat();

                    if (currentHeat >= maxHeat) {
                        // Cauldron is at maximum heat, don't allow adding more
                        pPlayer.displayClientMessage(Component.literal("Maximum Heat Reached"), true);
                        return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
                    }

                    int heatToAdd = heatItems.get(itemId);
                    pillCauldronLowHumanEntity.addHeat(heatToAdd);

                    spawnFireParticles(pLevel, pPos);

                    if (!pPlayer.getAbilities().instabuild) {
                        pStack.shrink(1);
                    }
                    return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
                } else {

                    ((ServerPlayer) pPlayer).openMenu(new SimpleMenuProvider(pillCauldronLowHumanEntity, Component.literal("Pill Cauldron")), pPos);
                }
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    private void spawnFireParticles(Level level, BlockPos pos) {
        if (level.isClientSide()) {
            return;
        }

        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 1.5;
            double y = pos.getY() + 0.8 + random.nextDouble() * 0.5;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 1.5;

            if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.FLAME,
                        x, y, z,
                        1,
                        0, 0, 0,
                        0.01
                );
            }
        }


        for (int i = 0; i < 4; i++) {
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 1.2;
            double y = pos.getY() + 1.0 + random.nextDouble() * 0.3;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 1.2;

            if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.SMOKE,
                        x, y, z,
                        1,
                        0, 0.01, 0,
                        0.02
                );
            }
        }
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) {
            return null;
        }

        return createTickerHelper(blockEntityType, ModBlockEntities.PILL_CAULDRON_LOW_HUMAN.get(),
                (level1, blockPos, blockState, blockEntity) -> blockEntity.tick(level1, blockPos, blockState));
    }
}