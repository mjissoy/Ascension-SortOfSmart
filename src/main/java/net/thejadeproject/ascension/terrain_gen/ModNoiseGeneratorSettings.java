package net.thejadeproject.ascension.terrain_gen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;

import java.util.List;

public class ModNoiseGeneratorSettings {

    public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> context) {

        // world height -64 to 448 (need to fix build height)
        NoiseSettings noiseSettings = NoiseSettings.create(-64, 512, 4, 1);

        HolderGetter<DensityFunction> densityFunctions = context.lookup(Registries.DENSITY_FUNCTION);

        DensityFunction continents   = new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ModTerrainGenKeys.ASCENSION_CONTINENTS_DF));
        DensityFunction depth        = new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ModTerrainGenKeys.ASCENSION_DEPTH_DF));
        DensityFunction erosion      = new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ModTerrainGenKeys.ASCENSION_EROSION_DF));
        DensityFunction ridges       = new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ModTerrainGenKeys.ASCENSION_RIDGES_DF));
        DensityFunction temperature  = new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ModTerrainGenKeys.ASCENSION_TEMPERATURE_DF));
        DensityFunction vegetation   = new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ModTerrainGenKeys.ASCENSION_VEGETATION_DF));
        DensityFunction baseTerrain  = new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ModTerrainGenKeys.ASCENSION_BASE_TERRAIN_DF));
        DensityFunction finalDensity = new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ModTerrainGenKeys.ASCENSION_FINAL_DENSITY_DF));

        NoiseRouter noiseRouter = new NoiseRouter(
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                temperature,
                vegetation,
                continents,
                erosion,
                depth,
                ridges,
                baseTerrain,
                finalDensity,
                DensityFunctions.zero(),
                DensityFunctions.zero(),
                DensityFunctions.zero()
        );


        SurfaceRules.RuleSource oceanFloorRules = SurfaceRules.sequence(
                SurfaceRules.ifTrue(
                        SurfaceRules.ON_FLOOR,
                        SurfaceRules.state(Blocks.SAND.defaultBlockState())
                ),
                SurfaceRules.ifTrue(
                        SurfaceRules.UNDER_FLOOR,
                        SurfaceRules.state(Blocks.GRAVEL.defaultBlockState())
                ),
                SurfaceRules.state(Blocks.STONE.defaultBlockState())
        );

        SurfaceRules.RuleSource riverRules = SurfaceRules.sequence(
                SurfaceRules.ifTrue(
                        SurfaceRules.ON_FLOOR,
                        SurfaceRules.state(Blocks.WATER.defaultBlockState())
                ),
                SurfaceRules.ifTrue(
                        SurfaceRules.UNDER_FLOOR,
                        SurfaceRules.state(Blocks.SAND.defaultBlockState())
                ),
                SurfaceRules.state(Blocks.STONE.defaultBlockState())
        );

        SurfaceRules.RuleSource surfaceRule = SurfaceRules.sequence(

                SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(
                                net.minecraft.world.level.biome.Biomes.RIVER,
                                net.minecraft.world.level.biome.Biomes.FROZEN_RIVER
                        ),
                        riverRules
                ),

                SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(
                                net.minecraft.world.level.biome.Biomes.OCEAN,
                                net.minecraft.world.level.biome.Biomes.DEEP_OCEAN,
                                net.minecraft.world.level.biome.Biomes.COLD_OCEAN,
                                net.minecraft.world.level.biome.Biomes.DEEP_COLD_OCEAN,
                                net.minecraft.world.level.biome.Biomes.LUKEWARM_OCEAN,
                                net.minecraft.world.level.biome.Biomes.DEEP_LUKEWARM_OCEAN,
                                net.minecraft.world.level.biome.Biomes.WARM_OCEAN,
                                net.minecraft.world.level.biome.Biomes.FROZEN_OCEAN,
                                net.minecraft.world.level.biome.Biomes.DEEP_FROZEN_OCEAN
                        ),
                        oceanFloorRules
                ),

                SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(
                                net.minecraft.world.level.biome.Biomes.BEACH,
                                net.minecraft.world.level.biome.Biomes.SNOWY_BEACH
                        ),
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.state(Blocks.SAND.defaultBlockState())),
                                SurfaceRules.state(Blocks.STONE.defaultBlockState())
                        )
                ),

                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState())),
                SurfaceRules.state(Blocks.STONE.defaultBlockState())
        );



        NoiseGeneratorSettings settings = new NoiseGeneratorSettings(
                noiseSettings,
                Blocks.STONE.defaultBlockState(),
                Blocks.WATER.defaultBlockState(),
                noiseRouter,
                surfaceRule,
                List.of(),
                63,
                false,
                true,
                true,
                false
        );

        context.register(ModTerrainGenKeys.ASCENSION_OVERWORLD, settings);
    }
}