package net.thejadeproject.ascension.worldgen.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.blocks.custom.SpiritVeinBlock;

public class SpiritVeinFeature extends Feature<NoneFeatureConfiguration> {
    public SpiritVeinFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        BlockState veinState = ModBlocks.SPIRIT_VEIN.get().defaultBlockState().setValue(SpiritVeinBlock.VEIN_LEVEL, 0);

        if (level.getBlockState(origin).canBeReplaced()) {
            level.setBlock(origin, veinState, 3);
            return true;
        }

        return false;
    }
}
