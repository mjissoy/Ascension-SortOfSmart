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
                DensityFunctions.noise(mountainsNoise, 0.18, 0.0)
        );

        DensityFunction broadDetail = DensityFunctions.flatCache(
                DensityFunctions.noise(detailNoise, 0.35, 0.0)
        );

        DensityFunction fineDetail = DensityFunctions.flatCache(
                DensityFunctions.noise(detailNoise, 0.80, 0.0)
        );

        DensityFunction surfaceJitterNoise = DensityFunctions.noise(detailNoise, 1.5, 0.8);


        DensityFunction continents = DensityFunctions.add(
                DensityFunctions.mul(rawContinents, DensityFunctions.constant(1.15)),
                DensityFunctions.constant(0.02)
        );

        DensityFunction ridges = DensityFunctions.max(
                rawRidges,
                DensityFunctions.mul(rawRidges, DensityFunctions.constant(-1.0))
        );

        DensityFunction ridgeGate = DensityFunctions.max(
                DensityFunctions.add(ridges, DensityFunctions.constant(0.04)),
                DensityFunctions.constant(0.0)
        );

        DensityFunction peakMask = clamp(
                DensityFunctions.mul(
                        DensityFunctions.add(ridges, DensityFunctions.constant(-0.18)),
                        DensityFunctions.constant(2.2)
                ),
                0.0, 1.0
        );

        DensityFunction landMask = DensityFunctions.max(
                DensityFunctions.constant(0.0),
                DensityFunctions.min(
                        DensityFunctions.constant(1.0),
                        DensityFunctions.mul(
                                DensityFunctions.add(continents, DensityFunctions.constant(-0.04)),
                                DensityFunctions.constant(2.4)
                        )
                )
        );

        DensityFunction mountainBody = DensityFunctions.mul(
                DensityFunctions.mul(
                        DensityFunctions.mul(ridgeGate, ridgeGate),
                        landMask
                ),
                DensityFunctions.constant(220.0)
        );

        DensityFunction peakBoost = DensityFunctions.mul(
                DensityFunctions.mul(
                        DensityFunctions.mul(peakMask, peakMask),
                        landMask
                ),
                DensityFunctions.constant(190.0)
        );

        DensityFunction mountainHeight = DensityFunctions.add(mountainBody, peakBoost);


        DensityFunction continentsOffset = DensityFunctions.add(
                continents, DensityFunctions.constant(-0.05)
        );

        DensityFunction oceanHeight = DensityFunctions.add(
                DensityFunctions.constant(63.0),
                DensityFunctions.mul(continentsOffset, DensityFunctions.constant(43.0))
        );

        DensityFunction landHeight = DensityFunctions.add(
                DensityFunctions.constant(63.0),
                DensityFunctions.mul(continentsOffset, DensityFunctions.constant(55.0))
        );

        DensityFunction baseHeight = DensityFunctions.rangeChoice(
                continents, -10.0, 0.06,
                oceanHeight,
                landHeight
        );




        DensityFunction oceanDetailMask = clamp(
                DensityFunctions.mul(
                        DensityFunctions.add(continents, DensityFunctions.constant(-0.01)),
                        DensityFunctions.constant(4.0)
                ),
                0.0, 1.0
        );



        DensityFunction detailHeight = DensityFunctions.mul(
                DensityFunctions.add(
                        DensityFunctions.mul(broadDetail, DensityFunctions.constant(7.0)),
                        DensityFunctions.mul(fineDetail,  DensityFunctions.constant(1.25))
                ),
                oceanDetailMask
        );


        DensityFunction terrainHeight = DensityFunctions.add(
                baseHeight,
                DensityFunctions.add(mountainHeight, detailHeight)
        );



        DensityFunction vertical = DensityFunctions.yClampedGradient(-64, 448, 64.0, -448.0);
        DensityFunction baseTerrain = DensityFunctions.add(terrainHeight, vertical);


        DensityFunction surfaceJitter = DensityFunctions.mul(
                surfaceJitterNoise,
                DensityFunctions.constant(2.25)
        );


        DensityFunction caveNoise = DensityFunctions.noise(detailNoise, 0.28, 0.20);
        DensityFunction rawCave = DensityFunctions.add(caveNoise, DensityFunctions.constant(-0.24));
        DensityFunction carvingOnly = DensityFunctions.min(rawCave, DensityFunctions.constant(0.0));
        DensityFunction caveDepthGate = DensityFunctions.yClampedGradient(48, 64, 1.0, 0.0);

        DensityFunction caveContrib = DensityFunctions.mul(
                DensityFunctions.mul(
                        DensityFunctions.mul(carvingOnly, caveDepthGate),
                        landMask
                ),
                DensityFunctions.constant(16.0)
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

        DensityFunction depthGradient = DensityFunctions.yClampedGradient(-64, 448, 1.0, -1.5);
        DensityFunction offset = DensityFunctions.rangeChoice(
                continents, -10.0, -0.35,
                DensityFunctions.constant(-1.2),
                DensityFunctions.rangeChoice(
                        continents, -0.35, -0.12,
                        DensityFunctions.constant(-0.4),
                        DensityFunctions.rangeChoice(
                                continents, -0.12, 0.2,
                                DensityFunctions.constant(0.20),
                                DensityFunctions.constant(0.75)
                        )
                )
        );
        DensityFunction depth = DensityFunctions.add(depthGradient, offset);


        DensityFunction withJitter  = DensityFunctions.add(baseTerrain, surfaceJitter);
        DensityFunction finalDensity = DensityFunctions.add(withJitter, caveContrib);


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

    private static DensityFunction clamp(DensityFunction input, double min, double max) {
        return DensityFunctions.max(
                DensityFunctions.constant(min),
                DensityFunctions.min(
                        DensityFunctions.constant(max),
                        input
                )
        );
    }
}
