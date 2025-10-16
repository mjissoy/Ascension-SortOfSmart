package net.thejadeproject.ascension.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_JADE_ORE_KEY = registerKey("jade_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_BLACK_IRON_ORE_KEY = registerKey("black_iron_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_FROST_SILVER_ORE_KEY = registerKey("frost_silver_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_MARBLE = registerKey("raw_marble");

    public static final ResourceKey<ConfiguredFeature<?, ?>> GOLDEN_PALM_KEY = registerKey("golden_palm");

    public static final ResourceKey<ConfiguredFeature<?, ?>> IRONWOOD_SPROUT_KEY = registerKey("ironwood_sprout_crop");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WHITE_JADE_ORCHID_KEY = registerKey("white_jade_orchid_crop");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HUNDRED_YEAR_SNOW_GINSENG_KEY = registerKey("hundred_year_snow_ginseng_crop");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HUNDRED_YEAR_FIRE_GINSENG_KEY = registerKey("hundred_year_fire_ginseng_crop");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HUNDRED_YEAR_GINSENG_KEY = registerKey("hundred_year_ginseng_crop");


    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest ruletest = new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD);


        register(context, ORE_MARBLE, Feature.ORE, new OreConfiguration(ruletest, ModBlocks.RAW_MARBLE.get().defaultBlockState(), 64));

        List<OreConfiguration.TargetBlockState> overworldJadeOres = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.JADE_ORE.get().defaultBlockState()));
        List<OreConfiguration.TargetBlockState> overworldBlackIronOres = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.BLACK_IRON_ORE.get().defaultBlockState()));
        List<OreConfiguration.TargetBlockState> overworldFrostSilverOres = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.BLACK_IRON_ORE.get().defaultBlockState()));

        register(context, OVERWORLD_JADE_ORE_KEY, Feature.ORE, new OreConfiguration(overworldJadeOres, 4));
        register(context, OVERWORLD_BLACK_IRON_ORE_KEY, Feature.ORE, new OreConfiguration(overworldBlackIronOres, 4));
        register(context, OVERWORLD_FROST_SILVER_ORE_KEY, Feature.ORE, new OreConfiguration(overworldFrostSilverOres, 4));
        register(context, GOLDEN_PALM_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.GOLDEN_PALM_LOG.get()),
                new ForkingTrunkPlacer(4, 4, 3),

                BlockStateProvider.simple(ModBlocks.GOLDEN_PALM_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(3), 3),

                new TwoLayersFeatureSize(1, 0, 2)).dirt(BlockStateProvider.simple(Blocks.SAND)).build());



        //Herbs
        register(context, WHITE_JADE_ORCHID_KEY, Feature.RANDOM_PATCH, new RandomPatchConfiguration(
                32, // tries
                7,  // xzSpread
                3,  // ySpread
                PlacementUtils.filtered(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WHITE_JADE_ORCHID_CROP.get().defaultBlockState())),
                        BlockPredicate.matchesBlocks(ModBlocks.JADE_ORE.get()) // Only spawn on Jade Ore
                )
        ));
        register(context, IRONWOOD_SPROUT_KEY, Feature.RANDOM_PATCH, new RandomPatchConfiguration(
                32, // tries
                7,  // xzSpread
                3,  // ySpread
                PlacementUtils.filtered(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.IRONWOOD_SPROUT_CROP.get().defaultBlockState())),
                        BlockPredicate.matchesBlocks(Blocks.STONE) // Only spawn on Stone
                )
        ));
        register(context, HUNDRED_YEAR_SNOW_GINSENG_KEY, Feature.RANDOM_PATCH, new RandomPatchConfiguration(
                32, // tries
                7,  // xzSpread
                3,  // ySpread
                PlacementUtils.filtered(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.HUNDRED_YEAR_SNOW_GINSENG_CROP.get().defaultBlockState())),
                        BlockPredicate.matchesBlocks(Blocks.SNOW_BLOCK) // Only spawn on Snow Block
                )
        ));
        register(context, HUNDRED_YEAR_FIRE_GINSENG_KEY, Feature.RANDOM_PATCH, new RandomPatchConfiguration(
                32, // tries
                7,  // xzSpread
                3,  // ySpread
                PlacementUtils.filtered(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.HUNDRED_YEAR_FIRE_GINSENG_CROP.get().defaultBlockState())),
                        BlockPredicate.matchesBlocks(Blocks.NETHERRACK) // Only spawn on NetherRack
                )
        ));
        register(context, HUNDRED_YEAR_GINSENG_KEY, Feature.RANDOM_PATCH, new RandomPatchConfiguration(
                32, // tries
                7,  // xzSpread
                3,  // ySpread
                PlacementUtils.filtered(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.HUNDRED_YEAR_GINSENG_CROP.get().defaultBlockState())),
                        BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK) // Only spawn on GrassBlocks
                )
        ));

    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
