package net.thejadeproject.ascension.refactor_packages.techniques.custom.body;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.forms.forms.ModForms;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.skills.custom.ModSkills;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechnique;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechniqueData;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.GenericTechnique;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.stat_change_handlers.BasicStatChangeHandler;

import java.util.Set;

public class IndestructibleVajraTechnique extends GenericTechnique {

    private static final int PURIFYING_MANTRA_UNLOCK_REALM = 4;

    public IndestructibleVajraTechnique(BasicStatChangeHandler statChangeHandler) {
        super(
                ModPaths.BODY.getId(),
                Component.translatable("ascension.technique.indestructible_vajra_scripture"),
                2.0D,
                Set.of(ModPaths.BUDDHIST.getId())
        );

        setStatChangeHandler(statChangeHandler);
    }

    @Override
    public Component getShortDescription() {
        return Component.translatable(
                "ascension.technique.indestructible_vajra_scripture.description.short"
        );
    }

    @Override
    public Component getDescription() {
        return Component.translatable(
                "ascension.technique.indestructible_vajra_scripture.description"
        );
    }

    @Override
    public void onTechniqueAdded(IEntityData heldEntity) {
        heldEntity.giveSkill(
                ModSkills.VAJRA_BREATHING_CULTIVATION_SKILL.getId(),
                ModForms.MORTAL_VESSEL.getId()
        );

        heldEntity.getPathBonusHandler().addPathBonus(ModPaths.BUDDHIST.getId(), 1.0D);
        ensurePathData(heldEntity, ModPaths.BUDDHIST.getId());

        PathData pathData = heldEntity.getPathData(getPath());

        refreshUniversalTechniqueSkills(heldEntity);
        refreshRealmUnlockSkills(
                heldEntity,
                pathData == null ? 0 : pathData.getMajorRealm()
        );
    }

    @Override
    public void onTechniqueRemoved(IEntityData heldEntity, ITechniqueData techniqueData) {
        PathData pathData = heldEntity.getPathData(getPath());

        if (pathData != null) {
            pathData.handleRealmChange(pathData.getMajorRealm(), 0, heldEntity);
        }

        heldEntity.removeSkill(
                ModSkills.VAJRA_BREATHING_CULTIVATION_SKILL.getId(),
                ModForms.MORTAL_VESSEL.getId()
        );

        heldEntity.getPathBonusHandler().removePathBonus(ModPaths.BUDDHIST.getId(), 1.0D);

        refreshRealmUnlockSkills(heldEntity, -1);
        refreshUniversalTechniqueSkills(heldEntity);
    }

    @Override
    public void onRealmChange(
            IEntityData entityData,
            int oldMajorRealm,
            int oldMinorRealm,
            int newMajorRealm,
            int newMinorRealm
    ) {
        super.onRealmChange(entityData, oldMajorRealm, oldMinorRealm, newMajorRealm, newMinorRealm);
        refreshRealmUnlockSkills(entityData, newMajorRealm);
    }

    private void refreshRealmUnlockSkills(IEntityData entityData, int majorRealm) {
        refreshSkill(
                entityData,
                ModSkills.PURIFYING_MANTRA.getId(),
                majorRealm >= PURIFYING_MANTRA_UNLOCK_REALM
        );
    }

    private void refreshSkill(IEntityData entityData, ResourceLocation skillId, boolean shouldHave) {
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

    @Override
    public boolean isCompatibleWith(ResourceLocation technique) {
        ITechnique otherTechnique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(technique);

        return !(otherTechnique instanceof IndestructibleVajraTechnique)
                && !(otherTechnique instanceof WhiteLightningTenStageTechnique)
                && !(otherTechnique instanceof FiveElementBodyTechnique)
                && !(otherTechnique instanceof BodyElementTechnique)
                && !(otherTechnique instanceof CombinedBodyElementTechnique);
    }

    private void ensurePathData(IEntityData entityData, ResourceLocation path) {
        if (entityData.getPathData(path) != null) return;

        entityData.addPathData(
                path,
                AscensionRegistries.Paths.PATHS_REGISTRY.get(path).freshPathData(entityData)
        );
    }
}