package net.thejadeproject.ascension.worldgen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.*;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBindings;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.thejadeproject.ascension.AscensionCraft;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModStructureGen {

    // Structure Keys
    public static final ResourceKey<Structure> CRIMSON_LOTUS_STRUCTURE = createStructureKey("crimson_lotus_structure");
    public static final ResourceKey<Structure> SPIRITUAL_CAVERN = createStructureKey("spiritual_cavern");
    public static final ResourceKey<Structure> SWORD_TOMB1 = createStructureKey("sword_tomb1");
    public static final ResourceKey<Structure> SWORD_TOMB2 = createStructureKey("sword_tomb2");
    public static final ResourceKey<Structure> SWORD_TOMB3 = createStructureKey("sword_tomb3");

    // Structure Set Keys
    public static final ResourceKey<StructureSet> CRIMSON_LOTUS_STRUCTURE_SET = createStructureSetKey("crimson_lotus_structure_set");
    public static final ResourceKey<StructureSet> SPIRITUAL_CAVERN_SET = createStructureSetKey("spiritual_cavern_set");
    public static final ResourceKey<StructureSet> SWORD_TOMB1_SET = createStructureSetKey("sword_tomb1_set");
    public static final ResourceKey<StructureSet> SWORD_TOMB2_SET = createStructureSetKey("sword_tomb2_set");
    public static final ResourceKey<StructureSet> SWORD_TOMB3_SET = createStructureSetKey("sword_tomb3_set");

    // Template Pool Keys (must match the paths in your JSONs)
    public static final ResourceKey<StructureTemplatePool> CRIMSON_LOTUS_POOL = createPoolKey("crimson_lotus_structures/crimson_lotus_structure_pool");
    public static final ResourceKey<StructureTemplatePool> SPIRITUAL_CAVERN_POOL = createPoolKey("spiritual_caverns/spiritual_cavern_pool");
    public static final ResourceKey<StructureTemplatePool> SWORD_TOMB1_POOL = createPoolKey("sword_tombs/sword_tomb1_pool");
    public static final ResourceKey<StructureTemplatePool> SWORD_TOMB2_POOL = createPoolKey("sword_tombs/sword_tomb2_pool");
    public static final ResourceKey<StructureTemplatePool> SWORD_TOMB3_POOL = createPoolKey("sword_tombs/sword_tomb3_pool");

    // Empty processor reference
    private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSORS = ResourceKey.create(
            Registries.PROCESSOR_LIST,
            ResourceLocation.withDefaultNamespace("empty")
    );

    private static ResourceKey<Structure> createStructureKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, name));
    }

    private static ResourceKey<StructureSet> createStructureSetKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, name));
    }

    private static ResourceKey<StructureTemplatePool> createPoolKey(String name) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, name));
    }

    public static void bootstrapStructures(BootstrapContext<Structure> context) {
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        context.register(SWORD_TOMB1, new JigsawStructure(
                new Structure.StructureSettings(
                        HolderSet.direct(
                                biomes.getOrThrow(Biomes.SAVANNA),
                                biomes.getOrThrow(Biomes.SAVANNA_PLATEAU),
                                biomes.getOrThrow(Biomes.WINDSWEPT_SAVANNA),
                                biomes.getOrThrow(Biomes.BADLANDS),
                                biomes.getOrThrow(Biomes.ERODED_BADLANDS),
                                biomes.getOrThrow(Biomes.WOODED_BADLANDS)),
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                ),
                pools.getOrThrow(SWORD_TOMB1_POOL),
                1,
                ConstantHeight.of(VerticalAnchor.absolute(1)),
                false,
                Heightmap.Types.WORLD_SURFACE_WG
        ));
        context.register(SWORD_TOMB2, new JigsawStructure(
                new Structure.StructureSettings(
                        HolderSet.direct(
                                biomes.getOrThrow(Biomes.PLAINS),
                                biomes.getOrThrow(Biomes.SNOWY_PLAINS),
                                biomes.getOrThrow(Biomes.SUNFLOWER_PLAINS),
                                biomes.getOrThrow(Biomes.CHERRY_GROVE),
                                biomes.getOrThrow(Biomes.GROVE),
                                biomes.getOrThrow(Biomes.FLOWER_FOREST),
                                biomes.getOrThrow(Biomes.OLD_GROWTH_PINE_TAIGA)),
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                ),
                pools.getOrThrow(SWORD_TOMB2_POOL),
                1,
                ConstantHeight.of(VerticalAnchor.absolute(1)),
                false,
                Heightmap.Types.WORLD_SURFACE_WG
        ));
        context.register(SWORD_TOMB3, new JigsawStructure(
                new Structure.StructureSettings(
                        HolderSet.direct(
                                biomes.getOrThrow(Biomes.WINDSWEPT_FOREST),
                                biomes.getOrThrow(Biomes.WINDSWEPT_HILLS),
                                biomes.getOrThrow(Biomes.WINDSWEPT_GRAVELLY_HILLS)
                                ),
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                ),
                pools.getOrThrow(SWORD_TOMB3_POOL),
                1,
                ConstantHeight.of(VerticalAnchor.absolute(1)),
                false,
                Heightmap.Types.WORLD_SURFACE_WG
        ));




        // Crimson Lotus - Surface structure with heightmap projection
        context.register(CRIMSON_LOTUS_STRUCTURE, new JigsawStructure(
                new Structure.StructureSettings(
                        biomes.getOrThrow(BiomeTags.IS_MOUNTAIN),
                        Map.of(),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                ),
                pools.getOrThrow(CRIMSON_LOTUS_POOL),
                1,  // maxDepth
                ConstantHeight.of(VerticalAnchor.absolute(1)),  // startHeight
                false,  // useExpansionHack
                Heightmap.Types.WORLD_SURFACE_WG  // <-- This constructor accepts the heightmap type directly!
        ));

        context.register(SPIRITUAL_CAVERN, new JigsawStructure(
                new Structure.StructureSettings(
                        biomes.getOrThrow(BiomeTags.HAS_MINESHAFT),
                        Map.of(),
                        GenerationStep.Decoration.UNDERGROUND_STRUCTURES,
                        TerrainAdjustment.NONE
                ),
                pools.getOrThrow(SPIRITUAL_CAVERN_POOL),
                1,
                UniformHeight.of(VerticalAnchor.absolute(-58), VerticalAnchor.absolute(0)),
                false
        ));
    }

    public static void bootstrapStructureSets(BootstrapContext<StructureSet> context) {
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);
        context.register(SWORD_TOMB1_SET, new StructureSet(
                structures.getOrThrow(SWORD_TOMB1),
                new RandomSpreadStructurePlacement(20, 12, RandomSpreadType.LINEAR, 12244326) // salt +1
        ));
        context.register(SWORD_TOMB2_SET, new StructureSet(
                structures.getOrThrow(SWORD_TOMB2),
                new RandomSpreadStructurePlacement(20, 12, RandomSpreadType.LINEAR, 12244327) // salt +2
        ));
        context.register(SWORD_TOMB3_SET, new StructureSet(
                structures.getOrThrow(SWORD_TOMB3),
                new RandomSpreadStructurePlacement(20, 12, RandomSpreadType.LINEAR, 12244328) // salt +3
        ));



        // Crimson Lotus Set: spacing 12, separation 6, salt 12244325
        context.register(CRIMSON_LOTUS_STRUCTURE_SET, new StructureSet(
                structures.getOrThrow(CRIMSON_LOTUS_STRUCTURE),
                new RandomSpreadStructurePlacement(12, 6, RandomSpreadType.LINEAR, 12244325)
        ));

        // Spiritual Cavern Set: spacing 20, separation 12, salt 1234567890
        context.register(SPIRITUAL_CAVERN_SET, new StructureSet(
                structures.getOrThrow(SPIRITUAL_CAVERN),
                new RandomSpreadStructurePlacement(20, 12, RandomSpreadType.LINEAR, 1234567890)
        ));
    }

    public static void bootstrapPools(BootstrapContext<StructureTemplatePool> context) {
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
        HolderGetter<StructureProcessorList> processors = context.lookup(Registries.PROCESSOR_LIST);
        var emptyPool = pools.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.withDefaultNamespace("empty")));
        var emptyProcessorList = processors.getOrThrow(EMPTY_PROCESSORS);


        context.register(SWORD_TOMB1_POOL, new StructureTemplatePool(
                emptyPool,
                List.of(
                        Pair.of(
                                StructurePoolElement.single("ascension:sword_tomb1", emptyProcessorList),
                                1
                        )
                ),
                StructureTemplatePool.Projection.RIGID
        ));
        context.register(SWORD_TOMB2_POOL, new StructureTemplatePool(
                emptyPool,
                List.of(
                        Pair.of(
                                StructurePoolElement.single("ascension:sword_tomb2", emptyProcessorList),
                                1
                        )
                ),
                StructureTemplatePool.Projection.RIGID
        ));
        context.register(SWORD_TOMB3_POOL, new StructureTemplatePool(
                emptyPool,
                List.of(
                        Pair.of(
                                StructurePoolElement.single("ascension:sword_tomb3", emptyProcessorList),
                                1
                        )
                ),
                StructureTemplatePool.Projection.RIGID
        ));


// Crimson Lotus Pool
        context.register(CRIMSON_LOTUS_POOL, new StructureTemplatePool(
                emptyPool,
                List.of(
                        Pair.of(
                                StructurePoolElement.single("ascension:crimson_lotus_structure", emptyProcessorList),
                                1  // weight
                        )
                ),
                StructureTemplatePool.Projection.RIGID
        ));

// Spiritual Cavern Pool
        context.register(SPIRITUAL_CAVERN_POOL, new StructureTemplatePool(
                emptyPool,
                List.of(
                        Pair.of(
                                StructurePoolElement.single("ascension:spiritual_cavern", emptyProcessorList),
                                1
                        )
                ),
                StructureTemplatePool.Projection.RIGID
        ));
    }
}
