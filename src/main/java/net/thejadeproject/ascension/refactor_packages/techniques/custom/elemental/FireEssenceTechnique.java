package net.thejadeproject.ascension.refactor_packages.techniques.custom.elemental;

import net.minecraft.network.chat.Component;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.skills.custom.ModSkills;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.ElementalEssenceTechnique;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.stat_change_handlers.BasicStatChangeHandler;

public class FireEssenceTechnique extends ElementalEssenceTechnique {

    public FireEssenceTechnique(BasicStatChangeHandler statChangeHandler) {
        super(
                Component.literal("Fire Essence Technique"),
                ModPaths.FIRE.getId(),
                ModSkills.FIRE_ESSENCE_CULTIVATION_SKILL.getId(),
                statChangeHandler
        );
    }

    @Override
    public Component getShortDescription() {
        return Component.literal("Cultivates Essence through flame, burning, and lava immersion.");
    }

    @Override
    public Component getDescription() {
        return Component.literal(
                "A Fire-aligned Essence technique. It cultivates slowly at rest, faster while burning, and fastest while submerged in lava."
        );
    }
}