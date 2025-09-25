package net.thejadeproject.ascension.worldgen.biome.surface;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.thejadeproject.ascension.worldgen.biome.ModBiomes;


public class ModSurfaceRules {
    private static final SurfaceRules.RuleSource STONE = makeStateRule(Blocks.STONE);
    private static final SurfaceRules.RuleSource GRAVEL = makeStateRule(Blocks.GRAVEL);
    private static final SurfaceRules.RuleSource SNOW_BLOCK = makeStateRule(Blocks.SNOW_BLOCK);

    public static SurfaceRules.RuleSource makeRules() {
        SurfaceRules.ConditionSource isAtOrAboveWaterLevel = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.ConditionSource isHighAltitude = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(150), 0);

        // Amplified terrain surface rules
        SurfaceRules.RuleSource amplifiedSurface = SurfaceRules.sequence(
                SurfaceRules.ifTrue(isHighAltitude,
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SNOW_BLOCK),
                                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, STONE)
                        )),
                SurfaceRules.ifTrue(isAtOrAboveWaterLevel,
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, GRAVEL),
                                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, STONE)
                        )),
                STONE
        );

        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.JAGGED_QI_PEAKS_BIOME), amplifiedSurface)
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
