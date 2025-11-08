package net.thejadeproject.ascension.blocks.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

public class CrimsonLotusFire extends BaseFireBlock {
    private final float fireDamage;

    // Add the MapCodec for registration
    public static final MapCodec<CrimsonLotusFire> CODEC = simpleCodec(properties ->
            new CrimsonLotusFire(properties, 1.0f));

    public CrimsonLotusFire(Properties properties, float fireDamage) {
        super(properties, fireDamage);
        this.fireDamage = fireDamage;
    }

    @Override
    protected MapCodec<? extends BaseFireBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean canBurn(BlockState blockState) {
        // Define which blocks your Crimson Lotus Fire can burn
        // You can customize this list based on your needs
        return blockState.is(Blocks.TNT) ||
                blockState.is(Blocks.VINE) ||
                blockState.is(Blocks.BAMBOO) ||
                blockState.is(Blocks.DEAD_BUSH) ||
                blockState.is(Blocks.SHORT_GRASS) ||
                blockState.is(Blocks.FERN) ||
                blockState.is(Blocks.DANDELION) ||
                blockState.is(Blocks.POPPY) ||
                blockState.is(Blocks.BLUE_ORCHID) ||
                blockState.is(Blocks.ALLIUM) ||
                blockState.is(Blocks.AZURE_BLUET) ||
                blockState.is(Blocks.RED_TULIP) ||
                blockState.is(Blocks.ORANGE_TULIP) ||
                blockState.is(Blocks.WHITE_TULIP) ||
                blockState.is(Blocks.PINK_TULIP) ||
                blockState.is(Blocks.OXEYE_DAISY) ||
                blockState.is(Blocks.CORNFLOWER) ||
                blockState.is(Blocks.LILY_OF_THE_VALLEY) ||
                blockState.is(Blocks.WITHER_ROSE) ||
                blockState.is(Blocks.SUNFLOWER) ||
                blockState.is(Blocks.LILAC) ||
                blockState.is(Blocks.ROSE_BUSH) ||
                blockState.is(Blocks.PEONY);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!entity.fireImmune()) {
            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
            if (entity.getRemainingFireTicks() == 0) {
                entity.igniteForSeconds(8); // Crimson fire duration
            }

            // Custom damage logic for Crimson Lotus Fire
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.hurt(level.damageSources().inFire(), this.fireDamage);

                // Add custom effects for Crimson Lotus Fire
                if (level.random.nextFloat() < 0.3f) {
                    // Example: Apply wither effect
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 1));
                }
            }
        }
        super.entityInside(state, level, pos, entity);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return this.canSurvive(state, level, pos) ? this.defaultBlockState() : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        // Custom survival conditions for Crimson Lotus Fire
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        // Can survive on specific blocks that fit the "Crimson Lotus" theme
        return belowState.isFaceSturdy(level, belowPos, Direction.UP) ||
                isValidCrimsonFireBase(level, belowPos, belowState);
    }

    private boolean isValidCrimsonFireBase(LevelReader level, BlockPos pos, BlockState state) {
        // Define what blocks Crimson Lotus Fire can exist on
        Block block = state.getBlock();
        return block == Blocks.NETHERRACK ||
                block == Blocks.CRIMSON_NYLIUM ||
                block == Blocks.MAGMA_BLOCK ||
                block == Blocks.SOUL_SAND ||
                block == Blocks.SOUL_SOIL ||
                state.isFlammable(level, pos, Direction.UP);
    }


    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Custom particles and sounds for Crimson Lotus Fire
        if (random.nextInt(24) == 0) {
            level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(),
                    random.nextFloat() * 0.7F + 0.3F, false);
        }

        // Crimson-colored fire particles
        for (int i = 0; i < 3; ++i) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble() * 0.5 + 0.5;
            double z = pos.getZ() + random.nextDouble();
            double motionX = random.nextDouble() * 0.1 - 0.05;
            double motionY = random.nextDouble() * 0.1;
            double motionZ = random.nextDouble() * 0.1 - 0.05;

            // You can create custom particles here
            level.addParticle(ParticleTypes.FLAME, x, y, z, motionX, motionY, motionZ);
        }

        // Additional custom particles for lotus theme
        if (random.nextInt(10) == 0) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);

        // Custom behavior when placed - you can add lotus-themed effects
        if (!level.isClientSide) {
            // Play a custom sound or trigger effects
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS,
                    0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);
        }
    }

}
