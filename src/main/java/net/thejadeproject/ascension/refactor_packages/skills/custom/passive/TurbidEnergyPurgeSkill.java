package net.thejadeproject.ascension.refactor_packages.skills.custom.passive;

public class TurbidEnergyPurgeSkill extends SimplePassiveSkill {

    @Override
    protected String getName() {
        return "Turbid Energy Purge";
    }

    @Override
    protected String getTooltip() {
        return "Slowly burns away harmful effects through purified white martial energy.";
    }

    @Override
    protected String getIconPath() {
        return "textures/spells/icon/placeholder_white.png";
    }
}