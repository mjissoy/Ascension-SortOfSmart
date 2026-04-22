package net.thejadeproject.ascension.terrain_gen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class ModDensityFunctions {

    public static void bootstrap(BootstrapContext<DensityFunction> context) {
        HolderGetter<NormalNoise.NoiseParameters> noises = context.lookup(Registries.NOISE);

        Holder<NormalNoise.NoiseParameters> continentsNoise =
                noises.getOrThrow(ModTerrainGenKeys.ASCENSION_CONTINENTS);
        Holder<NormalNoise.NoiseParameters> mountainsNoise =
                noises.getOrThrow(ModTerrainGenKeys.ASCENSION_MOUNTAINS);
        Holder<NormalNoise.NoiseParameters> detailNoise =
                noises.getOrThrow(ModTerrainGenKeys.ASCENSION_DETAIL);


        DensityFunction rawContinents = DensityFunctions.flatCache(
                DensityFunctions.noise(continentsNoise, 0.08, 0.0)
        );

        DensityFunction rawRidges = DensityFunctions.flatCache(
                DensityFunctions.noise(mountainsNoise, 0.5, 0.0)
        );

        DensityFunction rawDetail = DensityFunctions.flatCache(
                DensityFunctions.noise(detailNoise, 2.0, 0.0)
        );


        DensityFunction continents = DensityFunctions.add(
                DensityFunctions.mul(rawContinents, DensityFunctions.constant(1.20)),
                DensityFunctions.constant(0.02)
        );

        DensityFunction ridges = DensityFunctions.max(
                rawRidges,
                DensityFunctions.mul(rawRidges, DensityFunctions.constant(-1.0))
        );

        DensityFunction ridgeGate = DensityFunctions.max(
                DensityFunctions.add(ridges, DensityFunctions.constant(0.08)),
                DensityFunctions.constant(0.0)
        );


        DensityFunction continentsOffset = DensityFunctions.add(
                continents, DensityFunctions.constant(-0.08)
        );

        DensityFunction oceanHeight = DensityFunctions.add(
                DensityFunctions.constant(63.0),
                DensityFunctions.mul(continentsOffset, DensityFunctions.constant(30.0))
        );

        DensityFunction landHeight = DensityFunctions.add(
                DensityFunctions.constant(63.0),
                DensityFunctions.mul(continentsOffset, DensityFunctions.constant(40.0))
        );

        DensityFunction baseHeight = DensityFunctions.rangeChoice(
                continents, -10.0, 0.08,
                oceanHeight,
                landHeight
        );


        DensityFunction landMask = DensityFunctions.max(
                DensityFunctions.constant(0.0),
                DensityFunctions.min(
                        DensityFunctions.constant(1.0),
                        DensityFunctions.mul(
                                DensityFunctions.add(continents, DensityFunctions.constant(-0.08)),
                                DensityFunctions.constant(5.0)
                        )
                )
        );


        DensityFunction mountainHeight = DensityFunctions.mul(
                DensityFunctions.mul(
                        DensityFunctions.mul(ridgeGate, ridgeGate),
                        landMask
                ),
                DensityFunctions.constant(80.0)
        );

        DensityFunction detailHeight = DensityFunctions.mul(
                rawDetail, DensityFunctions.constant(14.0)
        );


        DensityFunction terrainHeight = DensityFunctions.add(
                baseHeight,
                DensityFunctions.add(mountainHeight, detailHeight)
        );


        DensityFunction vertical = DensityFunctions.yClampedGradient(-64, 320, 64.0, -320.0);


        DensityFunction baseTerrain = DensityFunctions.add(terrainHeight, vertical);


        DensityFunction caveNoise = DensityFunctions.noise(detailNoise, 0.3, 0.25);

        DensityFunction rawCave = DensityFunctions.add(caveNoise, DensityFunctions.constant(-0.2));
        DensityFunction carvingOnly = DensityFunctions.min(rawCave, DensityFunctions.constant(0.0));

        DensityFunction caveDepthGate = DensityFunctions.yClampedGradient(42, 60, 1.0, 0.0);

        DensityFunction caveContrib = DensityFunctions.mul(
                DensityFunctions.mul(
                        DensityFunctions.mul(carvingOnly, caveDepthGate),
                        landMask
                ),
                DensityFunctions.constant(22.0)
        );



        DensityFunction temperature = DensityFunctions.flatCache(
                DensityFunctions.noise(continentsNoise, 0.12, 0.0)
        );
        DensityFunction vegetation = DensityFunctions.flatCache(
                DensityFunctions.noise(detailNoise, 0.10, 0.0)
        );
        DensityFunction erosion = DensityFunctions.flatCache(
                DensityFunctions.noise(detailNoise, 0.15, 0.0)
        );

        DensityFunction depthGradient = DensityFunctions.yClampedGradient(-64, 384, 1.0, -1.5);
        DensityFunction offset = DensityFunctions.rangeChoice(
                continents, -10.0, -0.35,
                DensityFunctions.constant(-1.2),
                DensityFunctions.rangeChoice(
                        continents, -0.35, -0.12,
                        DensityFunctions.constant(-0.4),
                        DensityFunctions.rangeChoice(
                                continents, -0.12, 0.2,
                                DensityFunctions.constant(0.25),
                                DensityFunctions.constant(0.9)
                        )
                )
        );
        DensityFunction depth = DensityFunctions.add(depthGradient, offset);


        DensityFunction finalDensity = DensityFunctions.add(baseTerrain, caveContrib);


        context.register(ModTerrainGenKeys.ASCENSION_CONTINENTS_DF,   continents);
        context.register(ModTerrainGenKeys.ASCENSION_DEPTH_DF,         depth);
        context.register(ModTerrainGenKeys.ASCENSION_EROSION_DF,       erosion);
        context.register(ModTerrainGenKeys.ASCENSION_RIDGES_DF,        ridges);
        context.register(ModTerrainGenKeys.ASCENSION_TEMPERATURE_DF,   temperature);
        context.register(ModTerrainGenKeys.ASCENSION_VEGETATION_DF,    vegetation);
        context.register(ModTerrainGenKeys.ASCENSION_BASE_TERRAIN_DF,  baseTerrain);
        context.register(ModTerrainGenKeys.ASCENSION_CAVES_DF,         caveContrib);
        context.register(ModTerrainGenKeys.ASCENSION_FINAL_DENSITY_DF, finalDensity);
        context.register(ModTerrainGenKeys.ASCENSION_OFFSET_DF,        offset);
        context.register(ModTerrainGenKeys.ASCENSION_WINDSWEPT_DF,     DensityFunctions.zero());
        context.register(ModTerrainGenKeys.ASCENSION_JAGGEDNESS_DF,    DensityFunctions.zero());
    }
}
