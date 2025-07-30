package net.thejadeproject.ascension.worldgen.biome;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import terrablender.api.Regions;

public class ModTerrablender {
    public static void registerBiomes() {
        Regions.register(new ModOverworldRegion(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "overworld"), 3));
    }
}
