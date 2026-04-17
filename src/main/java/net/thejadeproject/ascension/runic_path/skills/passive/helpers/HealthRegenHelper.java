package net.thejadeproject.ascension.runic_path.skills.passive.helpers;

import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.skills.custom.ModSkills;
import net.thejadeproject.ascension.runic_path.RunicScalingHelper;

public class HealthRegenHelper {

    private HealthRegenHelper() {}

    public static double getRegenPerTick(IEntityData entityData) {
        if (entityData == null) return 0;

        double regen = 0;

        if (entityData.hasSkill(ModSkills.RUNIC_HEALTH_REGEN.getId())) {
            int realm = RunicScalingHelper.getMajorRealm(entityData);
            regen += 0.02 + (0.01 * realm);
        }

        return regen;
    }

}
