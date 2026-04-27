package net.thejadeproject.ascension.refactor_packages.techniques.custom.elemental;

import net.minecraft.network.chat.Component;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.skills.custom.ModSkills;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.ElementalEssenceTechnique;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.stat_change_handlers.BasicStatChangeHandler;

public class WoodEssenceTechnique extends ElementalEssenceTechnique {

    public WoodEssenceTechnique(BasicStatChangeHandler statChangeHandler) {
        super(
                Component.literal("Wood Essence Technique"),
                ModPaths.WOOD.getId(),
                ModSkills.WOOD_ESSENCE_CULTIVATION_SKILL.getId(),
                statChangeHandler
        );
    }

    @Override
    public Component getShortDescription() {
        return Component.literal("Cultivates Essence through wood, nature, and life-force.");
    }

    @Override
    public Component getDescription() {
        return Component.literal(
                "A Wood-aligned Essence technique. It cultivates slowly at rest, faster near plant life, and fastest within dense natural growth."
        );
    }

}
