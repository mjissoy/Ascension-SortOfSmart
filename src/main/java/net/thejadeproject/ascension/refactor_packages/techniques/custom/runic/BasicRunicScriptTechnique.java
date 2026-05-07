package net.thejadeproject.ascension.refactor_packages.techniques.custom.runic;

import net.minecraft.network.chat.Component;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechniqueData;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.GenericTechnique;
import net.thejadeproject.ascension.refactor_packages.techniques.helpers.RunicTechniqueSkillHelper;

import java.util.Set;

public class BasicRunicScriptTechnique extends GenericTechnique {

    public BasicRunicScriptTechnique() {
        super(
                ModPaths.RUNIC.getId(),
                Component.translatable("ascension.technique.basic_runic_script"),
                10.0D,
                Set.of()
        );
    }

    @Override
    public Component getShortDescription() {
        return Component.translatable("ascension.technique.basic_runic_script.description.short");
    }

    @Override
    public Component getDescription() {
        return Component.translatable("ascension.technique.basic_runic_script.description");
    }

    @Override
    public void onTechniqueAdded(IEntityData heldEntity) {
        super.onTechniqueAdded(heldEntity);

        RunicTechniqueSkillHelper.refresh(heldEntity, true);
    }

    @Override
    public void onTechniqueRemoved(IEntityData heldEntity, ITechniqueData techniqueData) {
        super.onTechniqueRemoved(heldEntity, techniqueData);

        RunicTechniqueSkillHelper.clear(heldEntity);
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

        RunicTechniqueSkillHelper.refresh(entityData, true);
    }
}