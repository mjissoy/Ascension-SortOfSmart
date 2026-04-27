package net.thejadeproject.ascension.refactor_packages.techniques.custom.elemental;

import net.minecraft.network.chat.Component;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.skills.custom.ModSkills;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.ElementalEssenceTechnique;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.stat_change_handlers.BasicStatChangeHandler;

public class EarthEssenceTechnique extends ElementalEssenceTechnique {

    public EarthEssenceTechnique(BasicStatChangeHandler statChangeHandler) {
        super(
                Component.literal("Earth Essence Technique"),
                ModPaths.EARTH.getId(),
                ModSkills.EARTH_ESSENCE_CULTIVATION_SKILL.getId(),
                statChangeHandler
        );
    }

    @Override
    public Component getShortDescription() {
        return Component.literal("Cultivates Essence through earth, stone, and underground exploration.");
    }

    @Override
    public Component getDescription() {
        return Component.literal(
                "An Earth-aligned Essence technique. It cultivates steadily at rest, faster on natural ground, and fastest while deep underground."
        );
    }

}
