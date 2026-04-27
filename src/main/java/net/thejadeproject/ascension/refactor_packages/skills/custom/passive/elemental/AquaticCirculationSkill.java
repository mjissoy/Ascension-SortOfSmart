package net.thejadeproject.ascension.refactor_packages.skills.custom.passive.elemental;

import net.thejadeproject.ascension.refactor_packages.skills.custom.passive.SimplePassiveSkill;

public class AquaticCirculationSkill extends SimplePassiveSkill {

    @Override
    protected String getName() {
        return "Aquatic Circulation";
    }

    @Override
    protected String getTooltip() {
        return "Grants conduit-like power while underwater.";
    }

    @Override
    protected String getIconPath() {
        return "textures/spells/icon/placeholder.png";
    }
}