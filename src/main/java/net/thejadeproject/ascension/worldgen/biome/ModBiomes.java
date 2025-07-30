package net.thejadeproject.ascension.worldgen.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.thejadeproject.ascension.AscensionCraft;

public class ModBiomes {
    public static final ResourceKey<Biome> JAGGED_QI_PEAKS_BIOME = ResourceKey.create(
            Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "jagged_qi_peaks_biome")
    );

    public static void bootstrap(BootstrapContext<Biome> context) {
        context.register(JAGGED_QI_PEAKS_BIOME, jaggedQiPeaksBiome(context));
    }

    public static Biome jaggedQiPeaksBiome(BootstrapContext<Biome> context) {
        // Mob Spawn Settings
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(spawnBuilder);
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);

        // Ambient (bats)
        spawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.BAT, 8, 8, 10));

        // Creature spawns (goats and llamas)
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.GOAT, 2, 4, 20));
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.LLAMA, 4, 4, 5));

        // Monster spawns
        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 4, 4, 100));
        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 4, 4, 95));
        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 4, 4, 100));
        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 4, 10));

        // Underground water creature (glow squid)
        spawnBuilder.addSpawn(MobCategory.UNDERGROUND_WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.GLOW_SQUID, 4, 6, 10));

        // Biome Generation Settings
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(
                context.lookup(Registries.PLACED_FEATURE),
                context.lookup(Registries.CONFIGURED_CARVER)
        );

        // More dramatic carvers for bigger mountains
        biomeBuilder.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE);
        biomeBuilder.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE_EXTRA_UNDERGROUND);
        biomeBuilder.addCarver(GenerationStep.Carving.AIR, Carvers.CANYON);
        biomeBuilder.addCarver(GenerationStep.Carving.AIR, Carvers.CANYON); // Extra canyon for more dramatic terrain

        // Add features in the correct order
        // Step 0: Underground lakes
        biomeBuilder.addFeature(GenerationStep.Decoration.LAKES, MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND);
        biomeBuilder.addFeature(GenerationStep.Decoration.LAKES, MiscOverworldPlacements.LAKE_LAVA_SURFACE);

        // Step 2: Monster rooms
        biomeBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.MONSTER_ROOM);
        biomeBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, CavePlacements.MONSTER_ROOM_DEEP);

        biomeBuilder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, CavePlacements.AMETHYST_GEODE);

        // Step 5: Ores
        biomeBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIRT);
        biomeBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GRAVEL);
        biomeBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_ANDESITE_UPPER);
        biomeBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_ANDESITE_LOWER);
        biomeBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_IRON_UPPER);
        biomeBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_IRON_MIDDLE);
        biomeBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_IRON_SMALL);
        biomeBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_EMERALD);

        // Step 8: Springs and infested blocks
        biomeBuilder.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, MiscOverworldPlacements.SPRING_WATER);
        biomeBuilder.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, MiscOverworldPlacements.SPRING_LAVA);
        biomeBuilder.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, OrePlacements.ORE_INFESTED);

        // Step 9: Vegetation - Modified for lush mountains
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.GLOW_LICHEN);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_WINDSWEPT_HILLS); // Base windswept trees
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_PLAINS); // Added oak trees
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_TAIGA); // Added spruce trees
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_BADLANDS); // Lush grass
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_TAIGA); // Taiga-style grass
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_TAIGA);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_TAIGA);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUGAR_CANE);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_PUMPKIN);

        // Taiga boulders
        //biomeBuilder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, VegetationPlacements);

        // Build the biome with more extreme mountain parameters
        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.3f)  // Slightly drier than plains
                .temperature(0.2f)  // Colder for higher altitudes
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0x3F76E4)
                        .waterFogColor(0x050533)
                        .skyColor(0x8080FF)  // High-altitude sky tint
                        .grassColorOverride(0x63C74D)  // Vibrant but natural green
                        .foliageColorOverride(0x58A518)
                        .fogColor(0xC0D8FF)  // Mountain fog
                        .ambientMoodSound(new AmbientMoodSettings(
                                SoundEvents.AMBIENT_CAVE,
                                6000,
                                8,
                                2.0
                        ))
                        .build())
                .build();
    }
}