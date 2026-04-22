package net.thejadeproject.ascension.terrain_gen;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class ModTerrainNoises {

    public static void bootstrap(BootstrapContext<NormalNoise.NoiseParameters> context) {
        context.register(ModTerrainGenKeys.ASCENSION_CONTINENTS,
                new NormalNoise.NoiseParameters(-8, 1.0, 1.0, 0.5, 0.25));

        context.register(ModTerrainGenKeys.ASCENSION_MOUNTAINS,
                new NormalNoise.NoiseParameters(-6, 1.0, 1.0, 0.5, 0.25));

        context.register(ModTerrainGenKeys.ASCENSION_DETAIL,
                new NormalNoise.NoiseParameters(-4, 1.0, 0.5, 0.25));
    }

}
