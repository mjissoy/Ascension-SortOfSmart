package net.thejadeproject.ascension.refactor_packages.skills.custom.passive.elemental;

import net.thejadeproject.ascension.refactor_packages.skills.custom.passive.SimplePassiveSkill;

public class VerdantRecoverySkill extends SimplePassiveSkill {

    @Override
    protected String getName() {
        return "Verdant Recovery";
    }

    @Override
    protected String getTooltip() {
        return "Slowly regenerates health while surrounded by plant life.";
    }

    @Override
    protected String getIconPath() {
        return "textures/spells/icon/placeholder.png";
    }
}