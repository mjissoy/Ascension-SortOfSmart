package net.thejadeproject.ascension.worldgen;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.entity.ModEntities;

import java.util.List;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_JADE_ORE = registerKey(("add_jade_ore"));
    public static final ResourceKey<BiomeModifier> ADD_BLACK_IRON_ORE = registerKey(("add_black_iron_ore"));
    public static final ResourceKey<BiomeModifier> ADD_FROST_SILVER_ORE = registerKey(("add_frost_silver_ore"));

    public static final ResourceKey<BiomeModifier> ADD_GOLDEN_PALM = registerKey("add_golden_palm");
    public static final ResourceKey<BiomeModifier> ADD_RAW_MARBLE = registerKey("add_raw_marble");


    public static final ResourceKey<BiomeModifier> ADD_WHITE_JADE_ORCHID = registerKey("add_white_jade_orchid");
    public static final ResourceKey<BiomeModifier> ADD_IRON_SPROUT = registerKey("add_iron_sprout");
    public static final ResourceKey<BiomeModifier> ADD_HUNDRED_YEAR_SNOW_GINSENG = registerKey("hundred_year_snow_ginseng");
    public static final ResourceKey<BiomeModifier> ADD_HUNDRED_YEAR_FIRE_GINSENG = registerKey("hundred_year_fire_ginseng");
    public static final ResourceKey<BiomeModifier> ADD_HUNDRED_YEAR_GINSENG = registerKey("hundred_year_ginseng");



    public static final ResourceKey<BiomeModifier> SPAWN_RAT = registerKey("spawn_rat");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_JADE_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.JADE_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_BLACK_IRON_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.BLACK_IRON_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_FROST_SILVER_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.ICE_SPIKES), biomes.getOrThrow(Biomes.FROZEN_PEAKS), biomes.getOrThrow(Biomes.SNOWY_PLAINS), biomes.getOrThrow(Biomes.SNOWY_TAIGA), biomes.getOrThrow(Biomes.SNOWY_SLOPES), biomes.getOrThrow(Biomes.SNOWY_BEACH), biomes.getOrThrow(Biomes.FROZEN_OCEAN), biomes.getOrThrow(Biomes.FROZEN_RIVER), biomes.getOrThrow(Biomes.DEEP_FROZEN_OCEAN)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.FROST_SILVER_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_RAW_MARBLE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.ORE_MARBLE_LOWER), placedFeatures.getOrThrow(ModPlacedFeatures.ORE_MARBLE_UPPER)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_GOLDEN_PALM, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.BEACH)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.GOLDEN_PALM_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(SPAWN_RAT, new BiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.FOREST), biomes.getOrThrow(Biomes.BIRCH_FOREST)),
                        List.of(new MobSpawnSettings.SpawnerData(ModEntities.RAT.get(), 5, 2, 4))));


        //Herbs
        context.register(ADD_WHITE_JADE_ORCHID, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.WHITE_JADE_ORCHID_PLACED)),
                GenerationStep.Decoration.UNDERGROUND_DECORATION));
        context.register(ADD_IRON_SPROUT, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.IRONWOOD_SPROUT_PLACED)),
                GenerationStep.Decoration.UNDERGROUND_DECORATION));
        context.register(ADD_HUNDRED_YEAR_FIRE_GINSENG, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.DESERT), biomes.getOrThrow(Biomes.BADLANDS)), // Or specify specific biomes like your ore
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.HUNDRED_YEAR_FIRE_GINSENG_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));
        context.register(ADD_HUNDRED_YEAR_SNOW_GINSENG, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.SNOWY_PLAINS), biomes.getOrThrow(Biomes.SNOWY_TAIGA), biomes.getOrThrow(Biomes.SNOWY_SLOPES), biomes.getOrThrow(Biomes.GROVE)), // Or specify specific biomes like your ore
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.HUNDRED_YEAR_SNOW_GINSENG_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_HUNDRED_YEAR_GINSENG, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.PLAINS), biomes.getOrThrow(Biomes.FOREST), biomes.getOrThrow(Biomes.FLOWER_FOREST), biomes.getOrThrow(Biomes.BIRCH_FOREST), biomes.getOrThrow(Biomes.DARK_FOREST), biomes.getOrThrow(Biomes.WINDSWEPT_FOREST), biomes.getOrThrow(Biomes.CHERRY_GROVE), biomes.getOrThrow(Biomes.BAMBOO_JUNGLE), biomes.getOrThrow(Biomes.JUNGLE), biomes.getOrThrow(Biomes.SPARSE_JUNGLE)), // Or specify specific biomes like your ore
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.HUNDRED_YEAR_GINSENG_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));


    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, name));
    }
}
