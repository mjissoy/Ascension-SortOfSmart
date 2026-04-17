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
import net.thejadeproject.ascension.runic_path.RunicRuneData;
import net.thejadeproject.ascension.runic_path.technique.helpers.CultivationCondition;
import net.thejadeproject.ascension.runic_path.technique.helpers.CultivationConditions;

import java.util.*;

public class RunicTechnique extends GenericTechnique {

    private final Map<Integer, Integer> maxRunesPerRealm = new HashMap<>();
    private final Map<Integer, List<ResourceLocation>> forcedRunesPerRealm = new HashMap<>();
    private int maxMajorRealm = 0;
    private CultivationCondition cultivationCondition = CultivationConditions.none();
    private boolean specialized = false;

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

        RunicRuneData runeData = RunicPathHelper.getRuneData(heldEntity);
        RunicPathHelper.applyForcedRunesIfNeeded(heldEntity, runeData);
        RunicPathHelper.saveRuneData(heldEntity, runeData);
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
        heldEntity.removeSkill(ModSkills.RUNIC_CULTIVATION_BOOST.getId(), ModForms.MORTAL_VESSEL.getId());
        heldEntity.removeSkill(ModSkills.RUNIC_HEALTH_REGEN.getId(), ModForms.MORTAL_VESSEL.getId());
    }

    @Override
    public void onRealmChange(IEntityData entityData, int oldMajorRealm, int oldMinorRealm, int newMajorRealm, int newMinorRealm) {
        super.onRealmChange(entityData,oldMajorRealm,oldMinorRealm,newMajorRealm,newMinorRealm);

        RunicRuneData runeData = RunicPathHelper.getRuneData(entityData);
        RunicPathHelper.applyForcedRunesIfNeeded(entityData, runeData);
        RunicPathHelper.saveRuneData(entityData, runeData);
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

    public RunicTechnique setCultivationCondition(CultivationCondition cultivationCondition) {
        this.cultivationCondition = cultivationCondition == null ? CultivationConditions.none() : cultivationCondition;
        return this;
    }

    public boolean canCultivate(IEntityData entityData) {
        return cultivationCondition == null || cultivationCondition.canCultivate(entityData);
    }

    public double getCultivationMultiplier(IEntityData entityData) {
        return cultivationCondition == null ? 1.0 : cultivationCondition.getMultiplier(entityData);
    }

    public RunicTechnique setSpecialized(boolean specialized) {
        this.specialized = specialized;
        return this;
    }

    public boolean isSpecialized() {
        return specialized;
    }

    public RunicTechnique setForcedRunesForRealm(int majorRealm, ResourceLocation... runeIds) {
        forcedRunesPerRealm.put(majorRealm, new ArrayList<>(List.of(runeIds)));
        return this;
    }

    public List<ResourceLocation> getForcedRunesForRealm(int majorRealm) {
        return forcedRunesPerRealm.getOrDefault(majorRealm, List.of());
    }



}