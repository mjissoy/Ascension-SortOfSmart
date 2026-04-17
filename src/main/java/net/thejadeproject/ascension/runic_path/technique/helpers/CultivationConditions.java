package net.thejadeproject.ascension.runic_path.technique.helpers;

import net.minecraft.tags.BiomeTags;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;

public final class CultivationConditions {

    private CultivationConditions() {}

    public static CultivationCondition none() {
        return new CultivationCondition() {
            @Override
            public boolean canCultivate(IEntityData entityData) {
                return true;
            }

            @Override
            public double getMultiplier(IEntityData entityData) {
                return 1.0;
            }
        };
    }

    // Specific Conditions

    // In Ocean Biome
    public static CultivationCondition oceanOnly(double multiplier) {
        return new CultivationCondition() {
            @Override
            public boolean canCultivate(IEntityData entityData) {
                if (entityData == null || entityData.getAttachedEntity() == null) return false;
                var entity = entityData.getAttachedEntity();
                return entity.level().getBiome(entity.blockPosition()).is(BiomeTags.IS_OCEAN);
            }

            @Override
            public double getMultiplier(IEntityData entityData) {
                return multiplier;
            }
        };
    }

    // Above a certain height
    public static CultivationCondition minHeight(double minY, double multiplier) {
        return new CultivationCondition() {
            @Override
            public boolean canCultivate(IEntityData entityData) {
                if (entityData == null || entityData.getAttachedEntity() == null) return false;
                return entityData.getAttachedEntity().getY() >= minY;
            }

            @Override
            public double getMultiplier(IEntityData entityData) {
                return multiplier;
            }
        };
    }

    // === More to be Added === //





}