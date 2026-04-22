package net.thejadeproject.ascension.terrain_gen;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;

public class ModWorldPresets {
    public static void bootstrap(BootstrapContext<WorldPreset> context) {
        HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseSettings = context.lookup(Registries.NOISE_SETTINGS);
        HolderGetter<MultiNoiseBiomeSourceParameterList> biomeParamLists =
                context.lookup(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        // Overworld
        Holder<NoiseGeneratorSettings> overworldNoise =
                noiseSettings.getOrThrow(ModTerrainGenKeys.ASCENSION_OVERWORLD);

        Holder<MultiNoiseBiomeSourceParameterList> overworldBiomeParams =
                biomeParamLists.getOrThrow(MultiNoiseBiomeSourceParameterLists.OVERWORLD);

        MultiNoiseBiomeSource overworldBiomeSource =
                MultiNoiseBiomeSource.createFromPreset(overworldBiomeParams);

        LevelStem overworldStem = new LevelStem(
                dimensionTypes.getOrThrow(ModTerrainGenKeys.ASCENSION_OVERWORLD_TYPE),
                new NoiseBasedChunkGenerator(overworldBiomeSource, overworldNoise)
        );

        // Nether
        Holder<NoiseGeneratorSettings> netherNoise =
                noiseSettings.getOrThrow(NoiseGeneratorSettings.NETHER);

        Holder<MultiNoiseBiomeSourceParameterList> netherBiomeParams =
                biomeParamLists.getOrThrow(MultiNoiseBiomeSourceParameterLists.NETHER);

        MultiNoiseBiomeSource netherBiomeSource =
                MultiNoiseBiomeSource.createFromPreset(netherBiomeParams);

        LevelStem netherStem = new LevelStem(
                dimensionTypes.getOrThrow(BuiltinDimensionTypes.NETHER),
                new NoiseBasedChunkGenerator(netherBiomeSource, netherNoise)
        );

        // End
        Holder<NoiseGeneratorSettings> endNoise =
                noiseSettings.getOrThrow(NoiseGeneratorSettings.END);

        LevelStem endStem = new LevelStem(
                dimensionTypes.getOrThrow(BuiltinDimensionTypes.END),
                new NoiseBasedChunkGenerator(TheEndBiomeSource.create(biomes), endNoise)
        );

        context.register(
                ModTerrainGenKeys.ASCENSION_WORLD_PRESET,
                new WorldPreset(ImmutableMap.of(
                        LevelStem.OVERWORLD, overworldStem,
                        LevelStem.NETHER, netherStem,
                        LevelStem.END, endStem
                ))
        );
    }
}