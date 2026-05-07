package net.thejadeproject.ascension.refactor_packages.techniques.helpers;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.forms.forms.ModForms;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.skills.custom.ModSkills;

public final class RunicTechniqueSkillHelper {

    private RunicTechniqueSkillHelper() {
    }

    public static void refresh(IEntityData entityData, boolean shouldHaveRunicCastingSkill) {
        boolean shouldHave = shouldHaveRunicCastingSkill
                && entityData != null
                && entityData.hasPath(ModPaths.RUNIC.getId());

        refreshSkill(
                entityData,
                ModSkills.OPEN_RUNIC_CASTING.getId(),
                shouldHave
        );
    }

    public static void clear(IEntityData entityData) {
        refresh(entityData, false);
    }

    private static void refreshSkill(IEntityData entityData, ResourceLocation skillId, boolean shouldHave) {
        if (entityData == null) {
            return;
        }

        if (shouldHave) {
            if (!entityData.hasSkill(skillId)) {
                entityData.giveSkill(skillId, ModForms.MORTAL_VESSEL.getId());
            }

            return;
        }

        if (entityData.hasSkill(skillId)) {
            entityData.removeSkill(skillId, ModForms.MORTAL_VESSEL.getId());
        }
    }
}