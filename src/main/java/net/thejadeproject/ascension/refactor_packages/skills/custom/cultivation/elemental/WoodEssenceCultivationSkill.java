package net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.elemental;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.ElementalEssenceCultivationSkill;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.ElementalEssenceTechnique;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.elemental.WoodEssenceTechnique;

public class WoodEssenceCultivationSkill extends ElementalEssenceCultivationSkill {

    @Override
    protected ResourceLocation getElementPath() {
        return ModPaths.WOOD.getId();
    }

    @Override
    protected double getEnvironmentMultiplier(Entity caster) {
        int plants = countNearbyBlocks(caster, 3, this::isWoodResonantBlock);

        if (plants >= 24) {
            return 1.35D;
        }

        if (plants >= 8) {
            return 1.15D;
        }

        return 0.85D;
    }

    private boolean isWoodResonantBlock(BlockState state) {
        return state.is(BlockTags.SAPLINGS)
                || state.is(BlockTags.LEAVES)
                || state.is(BlockTags.LOGS)
                || state.is(BlockTags.FLOWERS)
                || state.is(BlockTags.CROPS);
    }

    @Override
    protected Class<? extends ElementalEssenceTechnique> getTechniqueClass() {
        return WoodEssenceTechnique.class;
    }

    @Override
    protected Component getSkillTitle() {
        return Component.literal("Wood Essence Cultivation");
    }

    @Override
    protected Component getSkillDescription() {
        return Component.literal(
                "Cultivates Essence through nearby plants, trees, and living growth."
        );
    }
}