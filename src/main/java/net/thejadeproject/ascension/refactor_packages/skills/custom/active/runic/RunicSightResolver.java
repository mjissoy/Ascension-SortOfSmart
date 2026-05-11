package net.thejadeproject.ascension.refactor_packages.skills.custom.active.runic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.thejadeproject.ascension.refactor_packages.runic.runes.ModRunicRunes;

import java.util.ArrayList;
import java.util.List;

public final class RunicSightResolver {

    private RunicSightResolver() {}

    public static List<ResourceLocation> getRunesForBlock(BlockState state) {
        List<ResourceLocation> runes = new ArrayList<>();

        if (state.is(Blocks.FIRE) || state.is(Blocks.LAVA) || state.is(Blocks.MAGMA_BLOCK) || state.is(Blocks.CAMPFIRE)) {
            runes.add(ModRunicRunes.FLAME_RUNE.getId());
        }

        if (state.is(Blocks.WATER) || state.is(Blocks.KELP) || state.is(Blocks.SEAGRASS)) {
            runes.add(ModRunicRunes.WATER_RUNE.getId());
        }

        if (state.is(BlockTags.STONE_ORE_REPLACEABLES) || state.is(Blocks.STONE) || state.is(Blocks.COBBLESTONE) || state.is(Blocks.DEEPSLATE)) {
            runes.add(ModRunicRunes.EARTH_RUNE.getId());
            runes.add(ModRunicRunes.HEAVY_RUNE.getId());
        }

        if (state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES) || state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.MOSS_BLOCK)) {
            runes.add(ModRunicRunes.WOOD_RUNE.getId());
        }

        if (state.is(Blocks.IRON_BLOCK) || state.is(Blocks.IRON_ORE) || state.is(Blocks.DEEPSLATE_IRON_ORE)
                || state.is(Blocks.GOLD_BLOCK) || state.is(Blocks.GOLD_ORE) || state.is(Blocks.DEEPSLATE_GOLD_ORE)) {
            runes.add(ModRunicRunes.METAL_RUNE.getId());
        }

        if (state.is(Blocks.ICE) || state.is(Blocks.PACKED_ICE) || state.is(Blocks.BLUE_ICE) || state.is(Blocks.SNOW_BLOCK)) {
            runes.add(ModRunicRunes.FROST_RUNE.getId());
            runes.add(ModRunicRunes.STABILISE_RUNE.getId());
        }

        return runes;
    }

    public static List<ResourceLocation> getRunesForEntity(Entity entity) {
        List<ResourceLocation> runes = new ArrayList<>();

        if (entity instanceof Blaze) {
            runes.add(ModRunicRunes.FLAME_RUNE.getId());
            runes.add(ModRunicRunes.RELEASE_RUNE.getId());
        }

        if (entity instanceof Creeper) {
            runes.add(ModRunicRunes.LIGHTNING_RUNE.getId());
            runes.add(ModRunicRunes.VIOLENT_RUNE.getId());
        }

        if (entity instanceof IronGolem) {
            runes.add(ModRunicRunes.METAL_RUNE.getId());
            runes.add(ModRunicRunes.GUARD_RUNE.getId());
            runes.add(ModRunicRunes.HEAVY_RUNE.getId());
        }

        if (entity instanceof Wolf) {
            runes.add(ModRunicRunes.WIND_RUNE.getId());
            runes.add(ModRunicRunes.BIND_RUNE.getId());
        }

        return runes;
    }
}