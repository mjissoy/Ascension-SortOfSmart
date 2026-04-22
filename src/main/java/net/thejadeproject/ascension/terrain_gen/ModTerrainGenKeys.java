package net.thejadeproject.ascension.terrain_gen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.thejadeproject.ascension.AscensionCraft;

public class ModTerrainGenKeys {

    // World Preset
    public static final ResourceKey<WorldPreset> ASCENSION_WORLD_PRESET =
            ResourceKey.create(Registries.WORLD_PRESET,
                    ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension"));

    // Dimension Type
    public static final ResourceKey<DimensionType> ASCENSION_OVERWORLD_TYPE =
            ResourceKey.create(Registries.DIMENSION_TYPE,
                    ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_dimension_type_overworld"));


    // Noise
    public static final ResourceKey<NoiseGeneratorSettings> ASCENSION_OVERWORLD =
            ResourceKey.create(Registries.NOISE_SETTINGS,
                    ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_overworld"));


    public static final ResourceKey<NormalNoise.NoiseParameters> ASCENSION_CONTINENTS =
            ResourceKey.create(Registries.NOISE,
                    ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_continents_noise"));

    public static final ResourceKey<NormalNoise.NoiseParameters> ASCENSION_MOUNTAINS =
            ResourceKey.create(Registries.NOISE,
                    ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_mountains_noise"));

    public static final ResourceKey<NormalNoise.NoiseParameters> ASCENSION_DETAIL =
            ResourceKey.create(Registries.NOISE,
                    ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_detail_noise"));


    // Density Functions
    public static final ResourceKey<DensityFunction> ASCENSION_CONTINENTS_DF =
            ResourceKey.create(Registries.DENSITY_FUNCTION,
                    ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_continents_df"));

    public static final ResourceKey<DensityFunction> ASCENSION_DEPTH_DF =
            ResourceKey.create(Registries.DENSITY_FUNCTION,
                    ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_depth_df"));

    public static final ResourceKey<DensityFunction> ASCENSION_EROSION_DF =
            ResourceKey.create(Registries.DENSITY_FUNCTION,
                    ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_erosion_df"));

    public static final ResourceKey<DensityFunction> ASCENSION_RIDGES_DF =
            ResourceKey.create(Registries.DENSITY_FUNCTION,
                    ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_ridges_df"));

    public static final ResourceKey<DensityFunction> ASCENSION_TEMPERATURE_DF =
            ResourceKey.create(Registries.DENSITY_FUNCTION,
                    ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_temperature_df"));

    public static final ResourceKey<DensityFunction> ASCENSION_VEGETATION_DF =
            ResourceKey.create(Registries.DENSITY_FUNCTION,
                    ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_vegetation_df"));

    public static final ResourceKey<DensityFunction> ASCENSION_BASE_TERRAIN_DF =
            ResourceKey.create(Registries.DENSITY_FUNCTION,
                    ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_base_terrain_df"));

    public static final ResourceKey<DensityFunction> ASCENSION_CAVES_DF =
            ResourceKey.create(Registries.DENSITY_FUNCTION,
                    ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_caves_df"));

    public static final ResourceKey<DensityFunction> ASCENSION_FINAL_DENSITY_DF =
            ResourceKey.create(Registries.DENSITY_FUNCTION,
                    ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_final_density_df"));

    public static final ResourceKey<DensityFunction> ASCENSION_OFFSET_DF =
            ResourceKey.create(Registries.DENSITY_FUNCTION,
                    ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_offset_df"));

    public static final ResourceKey<DensityFunction> ASCENSION_WINDSWEPT_DF =
            ResourceKey.create(Registries.DENSITY_FUNCTION,
                    ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_windswept_df"));

    public static final ResourceKey<DensityFunction> ASCENSION_JAGGEDNESS_DF =
            ResourceKey.create(Registries.DENSITY_FUNCTION,
                    ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_jaggedness_df"));
}
