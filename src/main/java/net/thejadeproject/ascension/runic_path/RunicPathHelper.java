package net.thejadeproject.ascension.runic_path;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.forms.forms.ModForms;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.skills.custom.ModSkills;
import net.thejadeproject.ascension.runic_path.technique.RunicTechnique;

import java.util.List;
import java.util.Map;

public final class RunicPathHelper {

    private static final String RUNE_TAG = "ascension_runes";
    private static final int DEFAULT_MINOR_REALMS_PER_MAJOR = 9;

    private static final Map<ResourceLocation, ResourceLocation> RUNE_TO_SKILL = Map.ofEntries(
            Map.entry(Runes.ARMOR.getId(), ModSkills.RUNIC_ARMOR.getId()),
            Map.entry(Runes.STRENGTH.getId(), ModSkills.RUNIC_STRENGTH.getId()),
            Map.entry(Runes.VITALITY.getId(), ModSkills.RUNIC_VITALITY.getId()),

            Map.entry(Runes.ESSENCE.getId(), ModSkills.RUNIC_CULTIVATION_BOOST.getId()),
            Map.entry(Runes.REGEN.getId(), ModSkills.RUNIC_HEALTH_REGEN.getId()),

            Map.entry(Runes.SPEED.getId(), ModSkills.RUNIC_SPEED.getId()),
            Map.entry(Runes.PRECISION.getId(), ModSkills.RUNIC_PRECISION.getId())

    );

    private RunicPathHelper() {}

    // DATA ACCESS
    public static RunicRuneData getRuneData(IEntityData entityData) {
        if (entityData == null || entityData.getAttachedEntity() == null) {
            return new RunicRuneData();
        }

        CompoundTag tag = entityData.getAttachedEntity()
                .getPersistentData()
                .getCompound(RUNE_TAG);

        return RunicRuneData.read(tag);
    }

    public static void saveRuneData(IEntityData entityData, RunicRuneData runeData) {
        if (entityData == null || entityData.getAttachedEntity() == null || runeData == null) return;

        entityData.getAttachedEntity()
                .getPersistentData()
                .put(RUNE_TAG, runeData.write());
    }

    public static void clearRuneData(IEntityData entityData) {
        if (entityData == null || entityData.getAttachedEntity() == null) return;
        entityData.getAttachedEntity().getPersistentData().remove(RUNE_TAG);
    }

    public static void clearRunes(IEntityData entityData) {
        if (entityData == null) return;

        clearRuneData(entityData);
        refreshAllRuneSkills(entityData);
    }

    // CHECKS
    public static boolean hasRune(IEntityData entityData, ResourceLocation runeId) {
        return getRuneData(entityData).hasRune(runeId);
    }

    public static List<ResourceLocation> getSelectedRunes(IEntityData entityData, int majorRealm) {
        return getRuneData(entityData).getSelectedRunes(majorRealm);
    }

    public static boolean hasSelectedRune(IEntityData entityData, int majorRealm, ResourceLocation runeId) {
        return getSelectedRunes(entityData, majorRealm).contains(runeId);
    }

    public static int getSelectedRuneCount(IEntityData entityData, int majorRealm) {
        return getRuneData(entityData).getSelectedRuneCount(majorRealm);
    }

    // TECHNIQUE LOOKUP
    public static RunicTechnique getRunicTechnique(IEntityData entityData) {
        if (entityData == null || !entityData.hasPath(ModPaths.RUNIC.getId())) return null;

        ResourceLocation techniqueId = entityData.getTechnique(ModPaths.RUNIC.getId());
        if (techniqueId == null) return null;

        var technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(techniqueId);
        if (technique instanceof RunicTechnique runicTechnique) {
            return runicTechnique;
        }

        return null;
    }

    public static int getMaxRuneSelectionsForRealm(IEntityData entityData, int majorRealm) {
        RunicTechnique technique = getRunicTechnique(entityData);
        if (technique == null) return 0;
        return technique.getMaxRunesForRealm(majorRealm);
    }

    public static int getCurrentMinorRealm(IEntityData entityData, int majorRealm) {
        if (entityData == null || !entityData.hasPath(ModPaths.RUNIC.getId())) return 0;

        var pathData = entityData.getPathData(ModPaths.RUNIC.getId());
        if (pathData == null) return 0;

        if (pathData.getMajorRealm() != majorRealm) {
            if (pathData.getMajorRealm() > majorRealm) {
                return DEFAULT_MINOR_REALMS_PER_MAJOR - 1;
            }
            return 0;
        }

        return pathData.getMinorRealm();
    }

    public static int getUnlockedRuneSelectionsForRealm(IEntityData entityData, int majorRealm) {
        int maxSelections = getMaxRuneSelectionsForRealm(entityData, majorRealm);
        if (maxSelections <= 0) return 0;

        int currentMinorRealm = getCurrentMinorRealm(entityData, majorRealm);

        // DEBUG override
        if (currentMinorRealm >= 8) {
            return maxSelections;
        }

        int maxMinorRealm = DEFAULT_MINOR_REALMS_PER_MAJOR - 1;
        int unlocked = 1 + (currentMinorRealm * (maxSelections - 1)) / maxMinorRealm;

        return Math.min(unlocked, maxSelections);
    }



    public static boolean canSelectMoreRunes(IEntityData entityData, int majorRealm) {
        return getSelectedRuneCount(entityData, majorRealm) < getUnlockedRuneSelectionsForRealm(entityData, majorRealm);
    }

    // SELECTION
    public static boolean trySelectRune(IEntityData entityData, RunicRuneData runeData, ResourceLocation runeId) {
        if (entityData == null || runeData == null || runeId == null) return false;

        Rune rune = Runes.get(runeId);
        if (rune == null) return false;

        int realm = rune.getMajorRealm();

        if (!canSelectMoreRunes(entityData, realm)) {
            return false;
        }

        return runeData.addSelectedRune(realm, runeId);
    }

    public static boolean autoSelectRuneIfPossible(IEntityData entityData, RunicRuneData runeData, ResourceLocation runeId) {
        if (entityData == null || runeData == null || runeId == null) return false;

        Rune rune = Runes.get(runeId);
        if (rune == null) return false;

        int realm = rune.getMajorRealm();

        if (!canSelectMoreRunes(entityData, realm)) {
            return false;
        }

        if (runeData.hasSelectedRune(realm, runeId)) {
            return false;
        }

        return runeData.addSelectedRune(realm, runeId);
    }

    public static boolean deselectRune(RunicRuneData runeData, int majorRealm, ResourceLocation runeId) {
        if (runeData == null || runeId == null) return false;
        return runeData.removeSelectedRune(majorRealm, runeId);
    }

    public static boolean toggleRuneSelection(IEntityData entityData, RunicRuneData runeData, ResourceLocation runeId) {
        if (entityData == null || runeData == null || runeId == null) return false;

        Rune rune = Runes.get(runeId);
        if (rune == null) return false;

        int majorRealm = rune.getMajorRealm();

        if (runeData.hasSelectedRune(majorRealm, runeId)) {
            return runeData.removeSelectedRune(majorRealm, runeId);
        }

        if (!canSelectMoreRunes(entityData, majorRealm)) {
            return false;
        }

        return runeData.addSelectedRune(majorRealm, runeId);
    }

    // SKILL REFRESH
    public static void refreshSkill(IEntityData entityData, ResourceLocation skillId, boolean shouldHave) {
        if (entityData == null || skillId == null) return;

        if (shouldHave) {
            if (!entityData.hasSkill(skillId)) {
                entityData.giveSkill(skillId, ModForms.MORTAL_VESSEL.getId());
            } else {
                entityData.removeSkill(skillId, ModForms.MORTAL_VESSEL.getId());
                entityData.giveSkill(skillId, ModForms.MORTAL_VESSEL.getId());
            }
        } else {
            if (entityData.hasSkill(skillId)) {
                entityData.removeSkill(skillId, ModForms.MORTAL_VESSEL.getId());
            }
        }
    }

    public static void refreshAllRuneSkills(IEntityData entityData) {
        if (entityData == null) return;

        RunicRuneData runeData = getRuneData(entityData);

        RunicTechnique technique = getRunicTechnique(entityData);
        if (technique == null) return;

        for (int majorRealm = 0; majorRealm <= technique.getMaxMajorRealm(); majorRealm++) {
            refreshRuneSkillsForRealm(entityData, runeData, majorRealm);
        }
    }

    public static void refreshRuneSkillsForRealm(IEntityData entityData, RunicRuneData runeData, int majorRealm) {
        if (entityData == null || runeData == null) return;

        List<ResourceLocation> selected = runeData.getSelectedRunes(majorRealm);

        for (Map.Entry<ResourceLocation, ResourceLocation> entry : RUNE_TO_SKILL.entrySet()) {
            ResourceLocation runeId = entry.getKey();
            ResourceLocation skillId = entry.getValue();

            Rune rune = Runes.get(runeId);
            if (rune == null) continue;
            if (rune.getMajorRealm() != majorRealm) continue;

            refreshSkill(entityData, skillId, selected.contains(runeId));
        }
    }

    public static void applyForcedRunesIfNeeded(IEntityData entityData, RunicRuneData runeData) {
        RunicTechnique technique = getRunicTechnique(entityData);
        if (technique == null || !technique.isSpecialized()) return;

        for (int majorRealm = 0; majorRealm <= technique.getMaxMajorRealm(); majorRealm++) {
            List<ResourceLocation> forced = technique.getForcedRunesForRealm(majorRealm);

            runeData.clearSelectedRunes(majorRealm);

            int maxSelections = technique.getMaxRunesForRealm(majorRealm);
            int limit = Math.min(forced.size(), maxSelections);

            int applied = 0;
            for (ResourceLocation runeId : forced) {
                if (applied >= limit) break;

                runeData.unlockRune(runeId);
                runeData.addSelectedRune(majorRealm, runeId);
                applied++;
            }
        }
    }

    public static boolean meetsRunicPrerequisite(IEntityData entityData) {
        if (entityData == null) return false;

        return hasReachedMajorRealm(entityData, ModPaths.ESSENCE.getId(), 1)
                || hasReachedMajorRealm(entityData, ModPaths.BODY.getId(), 1)
                || hasReachedMajorRealm(entityData, ModPaths.SOUL.getId(), 1);
    }

    public static boolean hasReachedMajorRealm(IEntityData entityData, ResourceLocation pathId, int requiredMajorRealm) {
        if (entityData == null || pathId == null) return false;
        if (!entityData.hasPath(pathId)) return false;

        var pathData = entityData.getPathData(pathId);
        if (pathData == null) return false;

        return pathData.getMajorRealm() >= requiredMajorRealm;
    }


}