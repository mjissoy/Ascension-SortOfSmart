package net.thejadeproject.ascension.runic_path.technique;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.forms.forms.ModForms;
import net.thejadeproject.ascension.refactor_packages.skills.custom.ModSkills;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechniqueData;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.GenericTechnique;
import net.thejadeproject.ascension.refactor_packages.techniques.custom.stat_change_handlers.BasicStatChangeHandler;
import net.thejadeproject.ascension.runic_path.RunicPathHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RunicTechnique extends GenericTechnique {

    private final Map<Integer, Integer> maxRunesPerRealm = new HashMap<>();
    private int maxMajorRealm = 0;

    public RunicTechnique(ResourceLocation path, Component title, double baseRate, Set<ResourceLocation> secondaryPaths) {
        super(path, title, baseRate, secondaryPaths);
    }

    @Override
    public void onTechniqueAdded(IEntityData heldEntity) {
        super.onTechniqueAdded(heldEntity);

        heldEntity.giveSkill(
                ModSkills.BASIC_RUNIC_CULTIVATION_SKILL.getId(),
                ModForms.MORTAL_VESSEL.getId()
        );

        RunicPathHelper.refreshAllRuneSkills(heldEntity);
    }

    @Override
    public void onTechniqueRemoved(IEntityData heldEntity, ITechniqueData techniqueData) {
        super.onTechniqueRemoved(heldEntity, techniqueData);

        heldEntity.removeSkill(
                ModSkills.BASIC_RUNIC_CULTIVATION_SKILL.getId(),
                ModForms.MORTAL_VESSEL.getId()
        );

        heldEntity.removeSkill(ModSkills.RUNIC_ARMOR.getId(), ModForms.MORTAL_VESSEL.getId());
        heldEntity.removeSkill(ModSkills.RUNIC_STRENGTH.getId(), ModForms.MORTAL_VESSEL.getId());
        heldEntity.removeSkill(ModSkills.RUNIC_VITALITY.getId(), ModForms.MORTAL_VESSEL.getId());
    }

    @Override
    public void onRealmChange(IEntityData entityData, int oldMajorRealm, int oldMinorRealm, int newMajorRealm, int newMinorRealm) {
        super.onRealmChange(entityData,oldMajorRealm,oldMinorRealm,newMajorRealm,newMinorRealm);

        RunicPathHelper.refreshAllRuneSkills(entityData);
    }

    public RunicTechnique setMaxRunesForRealm(int majorRealm, int maxRunes) {
        maxRunesPerRealm.put(majorRealm, maxRunes);
        return this;
    }

    public int getMaxRunesForRealm(int majorRealm) {
        return maxRunesPerRealm.getOrDefault(majorRealm, 0);
    }

    public boolean canUseRealm(int majorRealm) {
        return majorRealm <= getMaxMajorRealm() && getMaxRunesForRealm(majorRealm) > 0;
    }

    public RunicTechnique setMaxMajorRealm(int maxMajorRealm) {
        this.maxMajorRealm = maxMajorRealm;
        return this;
    }

    @Override
    public int getMaxMajorRealm() {
        return maxMajorRealm;
    }

    @Override
    public RunicTechnique setShortDescription(Component shortDescription) {
        super.setShortDescription(shortDescription);
        return this;
    }

    @Override
    public RunicTechnique setDescription(Component description) {
        super.setDescription(description);
        return this;
    }

    @Override
    public RunicTechnique setStatChangeHandler(BasicStatChangeHandler statChangeHandler) {
        super.setStatChangeHandler(statChangeHandler);
        return this;
    }

    public RunicTechnique setMaxRunesAllRealms(int maxRunes) {
        for (int i = 0; i <= getMaxMajorRealm(); i++) {
            setMaxRunesForRealm(i, maxRunes);
        }
        return this;
    }

}