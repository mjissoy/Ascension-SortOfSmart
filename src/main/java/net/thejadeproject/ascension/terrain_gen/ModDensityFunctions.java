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
                DensityFunctions.noise(mountainsNoise, 0.12, 0.0)
        );

        // large-scale terrain detail used for broad rolling hills
        DensityFunction broadDetail = DensityFunctions.flatCache(
                DensityFunctions.noise(detailNoise, 0.12, 0.0)
        );

        // smaller-scale terrain detail used for softer local variation
        DensityFunction fineDetail = DensityFunctions.flatCache(
                DensityFunctions.noise(detailNoise, 0.18, 0.0)
        );

        // light surface jitter noise used to break up overly smooth terrain
        DensityFunction surfaceJitterNoise = DensityFunctions.noise(detailNoise, 1.10, 0.20);

        // Scales and offsets the continent noise
        DensityFunction continents = DensityFunctions.add(
                DensityFunctions.mul(rawContinents, DensityFunctions.constant(1.10)),
                DensityFunctions.constant(0.28)
        );

        // Converts ridge noise into an absolute-value style function
        DensityFunction ridges = DensityFunctions.max(
                rawRidges,
                DensityFunctions.mul(rawRidges, DensityFunctions.constant(-1.0))
        );

        // offsets ridge values upward. bias all ridges
        DensityFunction ridgeGate = clamp(
                DensityFunctions.mul(
                        DensityFunctions.add(ridges, DensityFunctions.constant(-0.16)),
                        DensityFunctions.constant(4.0)
                ),
                0.0, 1.0
        );

        // stricter peak mask so only the strongest ridges get tall peak boosts
        DensityFunction peakMask = clamp(
                DensityFunctions.mul(
                        DensityFunctions.add(ridges, DensityFunctions.constant(-0.34)),
                        DensityFunctions.constant(3.8)
                ),
                0.0, 1.0
        );


        // smooth land mask so terrain effects fade in gradually from ocean to land
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

        // mountain height cap
        DensityFunction mountainCeilingFade =
                DensityFunctions.yClampedGradient(385, 430, 1.0, 0.0);

        // cubic ease on ridgeGate so the mountain base eases
        DensityFunction ridgeGateSq   = DensityFunctions.mul(ridgeGate, ridgeGate);
        DensityFunction ridgeGateCube = DensityFunctions.mul(ridgeGateSq, ridgeGate);

        // main mountain body from ridge strength and land presence
        DensityFunction mountainBody = DensityFunctions.mul(
                DensityFunctions.mul(
                        DensityFunctions.mul(ridgeGateCube, landMask),
                        mountainCeilingFade
                ),
                DensityFunctions.constant(100.0)
        );

        // extra height to the strongest mountain peaks
        DensityFunction peakMaskSq   = DensityFunctions.mul(peakMask, peakMask);
        DensityFunction peakMaskCube = DensityFunctions.mul(peakMaskSq, peakMask);
        DensityFunction peakBoost = DensityFunctions.mul(
                DensityFunctions.mul(
                        DensityFunctions.mul(peakMaskCube, landMask),
                        mountainCeilingFade
                ),
                DensityFunctions.constant(260.0)
        );

        // combines mountain bulk and peak boosts into the final mountain height
        DensityFunction mountainHeight = DensityFunctions.add(mountainBody, peakBoost);


        // Shifts continent values for the sea-to-land transition point
        DensityFunction continentsOffset = DensityFunctions.add(
                continents, DensityFunctions.constant(-0.02)
        );

        // ocean floor height
        DensityFunction oceanHeight = DensityFunctions.add(
                DensityFunctions.constant(50.0),
                DensityFunctions.mul(continentsOffset, DensityFunctions.constant(30.0))
        );

        // land height
        DensityFunction landHeight = DensityFunctions.add(
                DensityFunctions.constant(72.0),
                DensityFunctions.mul(continentsOffset, DensityFunctions.constant(30.0))
        );

        // chooses between ocean and land base height depending on continent value
        DensityFunction baseHeight = DensityFunctions.rangeChoice(
                continents, -10.0, -0.08,
                oceanHeight,
                landHeight
        );

        // softer detail mask so terrain detail fades in near coastlines
        DensityFunction oceanDetailMask = clamp(
                DensityFunctions.mul(
                        DensityFunctions.add(continents, DensityFunctions.constant(-0.08)),
                        DensityFunctions.constant(1.6)
                ),
                0.0, 1.0
        );

        //  combines broad and fine terrain detail
        DensityFunction detailHeight = DensityFunctions.mul(
                DensityFunctions.add(
                        DensityFunctions.mul(broadDetail, DensityFunctions.constant(1.6)),
                        DensityFunctions.mul(fineDetail, DensityFunctions.constant(0.25))
                ),
                oceanDetailMask
        );

        DensityFunction erosion = DensityFunctions.flatCache(
                DensityFunctions.noise(detailNoise, 0.055, 0.0)
        );

        DensityFunction riverDistance = DensityFunctions.max(
                erosion,
                DensityFunctions.mul(erosion, DensityFunctions.constant(-1.0))
        );

        DensityFunction riverMask = DensityFunctions.flatCache(
                clamp(
                        DensityFunctions.mul(
                                DensityFunctions.add(
                                        DensityFunctions.constant(0.055),
                                        DensityFunctions.mul(riverDistance, DensityFunctions.constant(-1.0))
                                ),
                                DensityFunctions.constant(18.0)
                        ),
                        0.0, 1.0
                )
        );

        DensityFunction riverBedMask = DensityFunctions.flatCache(
                clamp(
                        DensityFunctions.mul(
                                DensityFunctions.add(
                                        DensityFunctions.constant(0.022),
                                        DensityFunctions.mul(riverDistance, DensityFunctions.constant(-1.0))
                                ),
                                DensityFunctions.constant(45.0)
                        ),
                        0.0, 1.0
                )
        );

        DensityFunction riverLandMask = DensityFunctions.flatCache(
                clamp(
                        DensityFunctions.mul(
                                DensityFunctions.add(continents, DensityFunctions.constant(-0.18)),
                                DensityFunctions.constant(6.0)
                        ),
                        0.0, 1.0
                )
        );

        // suppresses rivers when mountains are present
        DensityFunction riverMountainGate = clamp(
                DensityFunctions.add(
                        DensityFunctions.constant(1.0),
                        DensityFunctions.mul(ridgeGate, DensityFunctions.constant(-4.0))
                ),
                0.0, 1.0
        );

        DensityFunction riverBankLift = DensityFunctions.constant(0.0);

        // carve the valley into the terrain height (negative = dig downward).
        DensityFunction riverCarving = DensityFunctions.add(
                DensityFunctions.mul(
                        DensityFunctions.mul(
                                DensityFunctions.mul(riverMask, riverLandMask),
                                riverMountainGate
                        ),
                        DensityFunctions.constant(-1.5)
                ),
                DensityFunctions.mul(
                        DensityFunctions.mul(
                                DensityFunctions.mul(riverBedMask, riverLandMask),
                                riverMountainGate
                        ),
                        DensityFunctions.constant(-42.0)
                )
        );


        //  combines base height, mountains, detail, and river valleys into the total terrain height shape
        DensityFunction terrainHeight = DensityFunctions.add(
                baseHeight,
                DensityFunctions.add(
                        DensityFunctions.add(
                                DensityFunctions.add(mountainHeight, detailHeight),
                                riverCarving
                        ),
                        riverBankLift
                )
        );

        // vertical falloff
        DensityFunction vertical = DensityFunctions.yClampedGradient(-64, 448, 64.0, -448.0);

        // terrain height with the vertical gradient, interpolated for better smoothing
        DensityFunction baseTerrain = DensityFunctions.interpolated(
                DensityFunctions.add(terrainHeight, vertical)
        );

        // scales the surface jitter noise into a small final terrain roughness
        DensityFunction surfaceJitter = DensityFunctions.mul(
                surfaceJitterNoise,
                DensityFunctions.constant(1.25)
        );

        // noise gated to mountain areas to break up horizontal banding on steep slopes
        DensityFunction slopeBreaker = DensityFunctions.mul(
                DensityFunctions.noise(mountainsNoise, 0.10, 0.08),
                DensityFunctions.mul(ridgeGate, DensityFunctions.constant(7.0))
        );


        // cave noise, cave noise downward, keeps only the carving part of the cave signal, Restricts cave carving
        DensityFunction caveNoise = DensityFunctions.noise(detailNoise, 0.28, 0.20);
        DensityFunction rawCave = DensityFunctions.add(caveNoise, DensityFunctions.constant(-0.24));
        DensityFunction carvingOnly = DensityFunctions.min(rawCave, DensityFunctions.constant(0.0));
        DensityFunction caveDepthGate = DensityFunctions.yClampedGradient(48, 64, 1.0, 0.0);

        // combines cave shape, depth gate, and land mask into the final cave carving
        DensityFunction caveContrib = DensityFunctions.mul(
                DensityFunctions.mul(
                        DensityFunctions.mul(carvingOnly, caveDepthGate),
                        landMask
                ),
                DensityFunctions.constant(16.0)
        );

        // temperature noise
        DensityFunction temperature = DensityFunctions.flatCache(
                DensityFunctions.noise(continentsNoise, 0.12, 0.0)
        );

        // vegetation noise
        DensityFunction vegetation = DensityFunctions.flatCache(
                DensityFunctions.noise(detailNoise, 0.10, 0.0)
        );

        // vertical depth gradient
        DensityFunction depthGradient = DensityFunctions.yClampedGradient(-64, 448, 1.0, -1.5);

        // offset bands that separate deep ocean, coast, inland, and higher terrain zones
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

        // combines the gradient and offset into the final depth
        DensityFunction depth = DensityFunctions.add(depthGradient, offset);

        // small surface jitter layer
        DensityFunction withJitter  = DensityFunctions.add(baseTerrain, surfaceJitter);

        // combines terrain, jitter, slope breaker, and caves into the final density
        DensityFunction finalDensity = DensityFunctions.add(
                DensityFunctions.add(withJitter, caveContrib),
                slopeBreaker
        );


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
