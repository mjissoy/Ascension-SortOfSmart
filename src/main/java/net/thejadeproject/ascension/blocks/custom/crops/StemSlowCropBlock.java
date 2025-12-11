package net.thejadeproject.ascension.blocks.custom.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.thejadeproject.ascension.items.ModItems;

public class StemSlowCropBlock extends CropBlock {
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(7.0, 0.0, 7.0, 9.0, 2.0, 9.0),
            Block.box(7.0, 0.0, 7.0, 9.0, 4.0, 9.0),
            Block.box(7.0, 0.0, 7.0, 9.0, 6.0, 9.0),
            Block.box(7.0, 0.0, 7.0, 9.0, 8.0, 9.0)};

    private final ItemLike seedItem;
    private final float growthChance;

    // Factory method for the original crop
    public static StemSlowCropBlock createStemCrop(Properties properties) {
        return new StemSlowCropBlock(properties, ModItems.HUNDRED_YEAR_GINSENG, 0.001f);
    }

    // Generic constructor
    public StemSlowCropBlock(Properties properties, ItemLike seedItem, float growthChance) {
        super(properties);
        this.seedItem = seedItem;
        this.growthChance = growthChance;
    }

    // Constructor for default growth chance
    public StemSlowCropBlock(Properties properties, ItemLike seedItem) {
        this(properties, seedItem, 0.001f);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(AGE)];
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return seedItem;
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return;

        if (level.getRawBrightness(pos, 0) >= 9) {
            int currentAge = this.getAge(state);

            if (currentAge < this.getMaxAge()) {
                if (random.nextFloat() < growthChance) {
                    level.setBlock(pos, this.getStateForAge(currentAge + 1), 2);
                }
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    protected int getBonemealAgeIncrease(Level level) {
        return 0;
    }
}