package net.thejadeproject.ascension.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;

import java.util.List;

import static net.thejadeproject.ascension.worldgen.ModOrePlacement.commonOrePlacement;
import static net.thejadeproject.ascension.worldgen.ModOrePlacement.rareOrePlacement;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> JADE_ORE_PLACED_KEY = registerKey("jade_ore");
    public static final ResourceKey<PlacedFeature> BLACK_IRON_ORE_PLACED_KEY = registerKey("black_iron_ore_placed");
    public static final ResourceKey<PlacedFeature> FROST_SILVER_ORE_PLACED_KEY = registerKey("frost_silver_ore_placed");

    public static final ResourceKey<PlacedFeature> GOLDEN_PALM_PLACED_KEY = registerKey("golden_palm_placed");

    public static final ResourceKey<PlacedFeature> ORE_MARBLE_UPPER = registerKey("ore_marble_upper");
    public static final ResourceKey<PlacedFeature> ORE_MARBLE_LOWER = registerKey("ore_marble_lower");



    public static final ResourceKey<PlacedFeature> IRONWOOD_SPROUT_PLACED = registerKey("ironwood_sprout_crop_placed");
    public static final ResourceKey<PlacedFeature> WHITE_JADE_ORCHID_PLACED = registerKey("white_jade_orchid_crop_placed");
    public static final ResourceKey<PlacedFeature> HUNDRED_YEAR_SNOW_GINSENG_PLACED = registerKey("hundred_year_snow_ginseng_crop_placed");
    public static final ResourceKey<PlacedFeature> HUNDRED_YEAR_FIRE_GINSENG_PLACED = registerKey("hundred_year_fire_ginseng_crop_placed");
    public static final ResourceKey<PlacedFeature> HUNDRED_YEAR_GINSENG_PLACED = registerKey("hundred_year_ginseng_crop_placed");






    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        HolderGetter<ConfiguredFeature<?, ?>> holdergetter = context.lookup(Registries.CONFIGURED_FEATURE);

        Holder<ConfiguredFeature<?, ?>> holder9 = holdergetter.getOrThrow(ModConfiguredFeatures.ORE_MARBLE);

        register(context, ORE_MARBLE_UPPER, holder9, rareOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128))));
        register(context, ORE_MARBLE_LOWER, holder9, commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(60))));


        register(context, JADE_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_JADE_ORE_KEY),
            commonOrePlacement(5, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(0))));

        register(context, BLACK_IRON_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_JADE_ORE_KEY),
            commonOrePlacement(5, HeightRangePlacement.triangle(VerticalAnchor.absolute(60), VerticalAnchor.absolute(300))));
        register(context, FROST_SILVER_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_FROST_SILVER_ORE_KEY),
            commonOrePlacement(5, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(0))));


        register(context, GOLDEN_PALM_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GOLDEN_PALM_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.1f, 1),
                        ModBlocks.GOLDEN_PALM_SAPLING.get()));


        //Herbs
        register(context, WHITE_JADE_ORCHID_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.WHITE_JADE_ORCHID_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(4), // Adjust rarity as needed
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome()
                ));
        register(context, IRONWOOD_SPROUT_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.IRONWOOD_SPROUT_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(10), // Adjust rarity as needed
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome()
                ));
        register(context, HUNDRED_YEAR_FIRE_GINSENG_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.HUNDRED_YEAR_FIRE_GINSENG_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(12), // Adjust rarity as needed
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome()
                ));
        register(context, HUNDRED_YEAR_SNOW_GINSENG_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.HUNDRED_YEAR_SNOW_GINSENG_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(6), // Adjust rarity as needed
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome()
                ));
        register(context, HUNDRED_YEAR_GINSENG_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.HUNDRED_YEAR_GINSENG_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(8), // Adjust rarity as needed
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP,
                        BiomeFilter.biome()
                ));

    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
