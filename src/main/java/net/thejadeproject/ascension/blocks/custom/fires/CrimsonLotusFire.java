package net.thejadeproject.ascension.blocks.custom.fires;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CrimsonLotusFire extends BaseFireBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
    private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    // Custom properties you can modify
    private final float damage;
    private final int spreadDelay;
    private final int extinguishChance;

    public CrimsonLotusFire(Properties properties, float damage, int spreadDelay, int extinguishChance) {
        super(properties, damage);
        this.damage = damage;
        this.spreadDelay = spreadDelay;
        this.extinguishChance = extinguishChance;
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    public CrimsonLotusFire(Properties properties) {
        this(properties, 1.0f, 30, 5); // Default values
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!entity.fireImmune()) {
            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
            if (entity.getRemainingFireTicks() == 0) {
                entity.isOnFire();
            }
        }

        entity.hurt(level.damageSources().inFire(), this.damage);
        super.entityInside(state, level, pos, entity);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.scheduleTick(pos, this, getFireTickDelay(level.random));

        if (!level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            return;
        }

        // Chance to extinguish
        if (random.nextInt(extinguishChance) == 0) {
            level.removeBlock(pos, false);
            return;
        }

        int age = state.getValue(AGE);
        if (age < 15) {
            state = state.setValue(AGE, age + 1);
            level.setBlock(pos, state, 3);
        }

        this.trySpread(level, pos, random, age); // Pass age as parameter
    }

    private void trySpread(ServerLevel level, BlockPos pos, RandomSource random, int age) {
        if (!level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            return;
        }

        // Spread to nearby blocks
        for (Direction direction : Direction.values()) {
            BlockPos adjacentPos = pos.relative(direction);
            if (random.nextInt(spreadDelay) == 0) {
                this.checkBurn(level, adjacentPos, random, age);
            }
        }
    }

    private void checkBurn(Level level, BlockPos pos, RandomSource random, int age) {
        if (!level.getBlockState(pos).isFlammable(level, pos, Direction.UP)) {
            return;
        }

        BlockState fireState = this.defaultBlockState().setValue(AGE, Math.min(15, age + random.nextInt(3) / 2));
        if (canSurvive(fireState, level, pos)) {
            level.setBlock(pos, fireState, 3);
            level.gameEvent(null, GameEvent.BLOCK_PLACE, pos);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return this.canSurvive(state, level, pos) ? state : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSurviveOnBlock(level.getBlockState(pos.below()));
    }

    public static boolean canSurviveOnBlock(BlockState state) {
        return state.isFlammable((BlockGetter) Blocks.FIRE, null, null) ||
                state.is(Blocks.NETHERRACK) ||
                state.is(Blocks.MAGMA_BLOCK) ||
                CampfireBlock.isLitCampfire(state) ||
                state.is(Blocks.SOUL_SAND) ||
                state.is(Blocks.SOUL_SOIL);
    }

    @Override
    protected MapCodec<? extends BaseFireBlock> codec() {
        return null;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState();
    }

    @Override
    protected boolean canBurn(BlockState state) {
        return true;
    }

    public static void onBlockCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        // Custom behavior when a block catches fire
        if (igniter instanceof Player) {
            // You can add custom logic here when a player ignites something
        }
    }

    private static int getFireTickDelay(RandomSource random) {
        return 30 + random.nextInt(10);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        level.scheduleTick(pos, this, getFireTickDelay(level.random));
    }
}