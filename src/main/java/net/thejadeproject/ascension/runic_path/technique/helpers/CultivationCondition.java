package net.thejadeproject.ascension.runic_path.technique.helpers;

import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;

public interface CultivationCondition {
    boolean canCultivate(IEntityData entityData);
    double getMultiplier(IEntityData entityData);
}
