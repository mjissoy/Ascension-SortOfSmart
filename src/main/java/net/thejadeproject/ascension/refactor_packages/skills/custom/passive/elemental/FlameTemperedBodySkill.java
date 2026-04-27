package net.thejadeproject.ascension.refactor_packages.skills.custom.passive.elemental;

import net.thejadeproject.ascension.refactor_packages.skills.custom.passive.SimplePassiveSkill;

public class FlameTemperedBodySkill extends SimplePassiveSkill {

    @Override
    protected String getName() {
        return "Flame Tempered Body";
    }

    @Override
    protected String getTooltip() {
        return "Negates fire and lava damage.";
    }

    @Override
    protected String getIconPath() {
        return "textures/spells/icon/placeholder.png";
    }
}