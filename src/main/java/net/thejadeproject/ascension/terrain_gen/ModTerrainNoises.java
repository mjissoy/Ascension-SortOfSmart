package net.thejadeproject.ascension.terrain_gen;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class ModTerrainNoises {

    public static void bootstrap(BootstrapContext<NormalNoise.NoiseParameters> context) {
        context.register(ModTerrainGenKeys.ASCENSION_CONTINENTS,
                new NormalNoise.NoiseParameters(-8, 1.0, 0.75, 0.35, 0.15));

        context.register(ModTerrainGenKeys.ASCENSION_MOUNTAINS,
                new NormalNoise.NoiseParameters(-7, 1.0, 0.55, 0.22, 0.08));

        context.register(ModTerrainGenKeys.ASCENSION_DETAIL,
                new NormalNoise.NoiseParameters(-3, 1.0, 0.30, 0.12));
    }

}
