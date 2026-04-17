package net.thejadeproject.ascension.runic_path.skills.passive.helpers;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.skills.custom.ModSkills;
import net.thejadeproject.ascension.runic_path.RunicScalingHelper;

public final class CultivationModifierHelper {

    private CultivationModifierHelper() {}

    public static double getGlobalMultiplier(IEntityData entityData) {
        if (entityData == null) return 1.0;

        double multiplier = 1.0;

        multiplier *= getRunicCultivationRuneMultiplier(entityData);

        return multiplier;
    }

    public static double getMultiplierForPath(IEntityData entityData, ResourceLocation pathId) {
        if (entityData == null || pathId == null) return 1.0;

        double multiplier = getGlobalMultiplier(entityData);

        return multiplier;
    }

    private static double getRunicCultivationRuneMultiplier(IEntityData entityData) {
        if (!entityData.hasSkill(ModSkills.RUNIC_CULTIVATION_BOOST.getId())) {
            return 1.0;
        }

        int realm = RunicScalingHelper.getMajorRealm(entityData);

        return 1.10 + (0.05 * realm);
    }
}