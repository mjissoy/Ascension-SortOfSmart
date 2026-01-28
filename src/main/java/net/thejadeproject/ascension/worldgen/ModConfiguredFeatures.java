package net.thejadeproject.ascension.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.blocks.custom.SpiritVeinBlock;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_JADE_ORE_KEY = registerKey("jade_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_BLACK_IRON_ORE_KEY = registerKey("black_iron_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_FROST_SILVER_ORE_KEY = registerKey("frost_silver_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_MARBLE = registerKey("raw_marble");

    public static final ResourceKey<ConfiguredFeature<?, ?>> GOLDEN_PALM_KEY = registerKey("golden_palm");
    public static final ResourceKey<ConfiguredFeature<?, ?>> IRONWOOD_KEY = registerKey("ironwood");

    public static final ResourceKey<ConfiguredFeature<?, ?>> IRONWOOD_SPROUT_KEY = registerKey("ironwood_sprout_crop");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WHITE_JADE_ORCHID_KEY = registerKey("white_jade_orchid_crop");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HUNDRED_YEAR_SNOW_GINSENG_KEY = registerKey("hundred_year_snow_ginseng_crop");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HUNDRED_YEAR_FIRE_GINSENG_KEY = registerKey("hundred_year_fire_ginseng_crop");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HUNDRED_YEAR_GINSENG_KEY = registerKey("hundred_year_ginseng_crop");





    public static final ResourceKey<ConfiguredFeature<?, ?>> SPIRIT_VEIN_FEATURE_KEY = registerKey("spirit_vein_feature");


    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest deepstoneReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest ruletest = new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD);


        register(context, ORE_MARBLE, Feature.ORE, new OreConfiguration(ruletest, ModBlocks.RAW_MARBLE.get().defaultBlockState(), 64));

        List<OreConfiguration.TargetBlockState> overworldJadeOres = List.of(
                OreConfiguration.target(deepstoneReplaceables, ModBlocks.JADE_ORE.get().defaultBlockState()));
        List<OreConfiguration.TargetBlockState> overworldBlackIronOres = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.BLACK_IRON_ORE.get().defaultBlockState()));
        List<OreConfiguration.TargetBlockState> overworldFrostSilverOres = List.of(
                OreConfiguration.target(deepstoneReplaceables, ModBlocks.FROST_SILVER_ORE.get().defaultBlockState()));

        register(context, OVERWORLD_JADE_ORE_KEY, Feature.ORE, new OreConfiguration(overworldJadeOres, 4));
        register(context, OVERWORLD_BLACK_IRON_ORE_KEY, Feature.ORE, new OreConfiguration(overworldBlackIronOres, 4));
        register(context, OVERWORLD_FROST_SILVER_ORE_KEY, Feature.ORE, new OreConfiguration(overworldFrostSilverOres, 4));


        register(context, GOLDEN_PALM_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.GOLDEN_PALM_LOG.get()),
                new ForkingTrunkPlacer(4, 4, 3),

                BlockStateProvider.simple(ModBlocks.GOLDEN_PALM_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(3), 3),

                new TwoLayersFeatureSize(1, 0, 2)).dirt(BlockStateProvider.simple(Blocks.SAND)).build());

        register(context, IRONWOOD_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.IRONWOOD_LOG.get()),
                new DarkOakTrunkPlacer(
                        6,  // baseHeight - dark oak trunk height
                        2,  // heightRandA - height variation
                        0   // heightRandB - additional variation
                ),
                BlockStateProvider.simple(ModBlocks.IRONWOOD_LEAVES.get()),
                new DarkOakFoliagePlacer(
                        ConstantInt.of(0),  // radius
                        ConstantInt.of(0)   // offset
                ),
                new TwoLayersFeatureSize(1, 0, 2))
                .ignoreVines()
                .dirt(BlockStateProvider.simple(Blocks.GRASS_BLOCK))
                .build());



        //Spirit Veins
        register(context, SPIRIT_VEIN_FEATURE_KEY,
                ModFeatureRegistration.SPIRIT_VEIN_FEATURE.get(),
                FeatureConfiguration.NONE);



        //Herbs
        register(context, WHITE_JADE_ORCHID_KEY, Feature.RANDOM_PATCH, new RandomPatchConfiguration(
                8, // Tries per patch (sparse clusters)
                3, // xzSpread
                2, // ySpread
                PlacementUtils.filtered(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WHITE_JADE_ORCHID_CROP.get().defaultBlockState())),
                        BlockPredicate.allOf(
                                BlockPredicate.replaceable(), // Position must be air/replaceable
                                BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), Blocks.STONE, Blocks.DEEPSLATE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.TUFF, Blocks.CALCITE) // Below must be cave floor
                        )
                )
        ));



        register(context, IRONWOOD_SPROUT_KEY, Feature.RANDOM_PATCH, new RandomPatchConfiguration(
                8, // Tries per patch (sparse clusters)
                3, // xzSpread
                2, // ySpread
                PlacementUtils.filtered(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.IRONWOOD_SPROUT_CROP.get().defaultBlockState())),
                        BlockPredicate.allOf(
                                BlockPredicate.replaceable(), // Position must be air/replaceable
                                BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), Blocks.STONE, Blocks.DEEPSLATE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.TUFF, Blocks.CALCITE) // Below must be cave floor
                        )
                )
        ));


        register(context, HUNDRED_YEAR_SNOW_GINSENG_KEY, Feature.RANDOM_PATCH, new RandomPatchConfiguration(
                3   , // 4 tries per patch for moderate density (adjust as needed)
                2, // xzSpread for natural spread
                1, // ySpread for precise placement
                PlacementUtils.filtered(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.HUNDRED_YEAR_SNOW_GINSENG_CROP.get().defaultBlockState())),
                        BlockPredicate.allOf(
                                BlockPredicate.replaceable(), // Must be air/replaceable above
                                BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), Blocks.GRASS_BLOCK, Blocks.SNOW_BLOCK) // Must be sand or red sand below
                        )
                )
        ));


        register(context, HUNDRED_YEAR_FIRE_GINSENG_KEY, Feature.RANDOM_PATCH, new RandomPatchConfiguration(
                3   , // 4 tries per patch for moderate density (adjust as needed)
                2, // xzSpread for natural spread
                1, // ySpread for precise placement
                PlacementUtils.filtered(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.HUNDRED_YEAR_FIRE_GINSENG_CROP.get().defaultBlockState())),
                        BlockPredicate.allOf(
                                BlockPredicate.replaceable(), // Must be air/replaceable above
                                BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), Blocks.SAND, Blocks.RED_SAND) // Must be sand or red sand below
                        )
                )
        ));


        register(context, HUNDRED_YEAR_GINSENG_KEY, Feature.RANDOM_PATCH, new RandomPatchConfiguration(
                3   , // 4 tries per patch for moderate density (adjust as needed)
                2, // xzSpread for natural spread
                1, // ySpread for precise placement
                PlacementUtils.filtered(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.HUNDRED_YEAR_GINSENG_CROP.get().defaultBlockState())),
                        BlockPredicate.allOf(
                                BlockPredicate.replaceable(), // Must be air/replaceable above
                                BlockPredicate.matchesBlocks(new BlockPos(0, -1, 0), Blocks.GRASS_BLOCK)
                        )
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
