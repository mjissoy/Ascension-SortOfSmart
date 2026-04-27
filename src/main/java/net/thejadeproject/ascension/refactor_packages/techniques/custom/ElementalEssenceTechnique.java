package net.thejadeproject.ascension.refactor_packages.techniques.custom;

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
import net.thejadeproject.ascension.refactor_packages.techniques.custom.stat_change_handlers.BasicStatChangeHandler;

import java.util.Set;

public abstract class ElementalEssenceTechnique extends GenericTechnique {

    private final ResourceLocation elementPath;
    private final ResourceLocation cultivationSkill;

    protected ElementalEssenceTechnique(
            Component title,
            ResourceLocation elementPath,
            ResourceLocation cultivationSkill,
            BasicStatChangeHandler statChangeHandler
    ) {
        super(
                ModPaths.ESSENCE.getId(),
                title,
                2.0D,
                Set.of(elementPath)
        );

        this.elementPath = elementPath;
        this.cultivationSkill = cultivationSkill;

        setStatChangeHandler(statChangeHandler);
    }

    public ResourceLocation getElementPath() {
        return elementPath;
    }

    @Override
    public void onTechniqueAdded(IEntityData heldEntity) {
        heldEntity.giveSkill(
                cultivationSkill,
                ModForms.MORTAL_VESSEL.getId()
        );

        heldEntity.giveSkill(
                ModSkills.ENTER_SPIRIT_FORM.getId(),
                ModForms.SOUL_FORM.getId()
        );

        heldEntity.getPathBonusHandler().addPathBonus(elementPath, 1.0D);
    }

    @Override
    public void onTechniqueRemoved(IEntityData heldEntity, ITechniqueData techniqueData) {
        PathData pathData = heldEntity.getPathData(getPath());

        if (pathData != null) {
            pathData.handleRealmChange(pathData.getMajorRealm(), 0, heldEntity);
        }

        heldEntity.removeSkill(
                cultivationSkill,
                ModForms.MORTAL_VESSEL.getId()
        );

        heldEntity.getPathBonusHandler().removePathBonus(elementPath, 1.0D);
    }

    @Override
    public boolean isCompatibleWith(ResourceLocation technique) {
        ITechnique otherTechnique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(technique);
        return otherTechnique instanceof ElementalEssenceTechnique;
    }
}