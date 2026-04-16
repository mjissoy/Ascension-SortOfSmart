package net.thejadeproject.ascension.runic_path;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.forms.forms.ModForms;
import net.thejadeproject.ascension.refactor_packages.skills.custom.ModSkills;

public final class RunicPathHelper {

    private static final String RUNE_TAG = "ascension_runes";

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

        entityData.getAttachedEntity().getPersistentData().remove("ascension_runes");
    }

    public static void clearRunes(IEntityData entityData) {
        if (entityData == null) return;

        clearRuneData(entityData);

        RunicRuneData clearedData = getRuneData(entityData);
        refreshFleshRuneSkills(entityData, clearedData);
    }

    // CHECKS
    public static boolean hasRune(IEntityData entityData, ResourceLocation runeId) {
        return getRuneData(entityData).hasRune(runeId);
    }

    public static boolean hasSelectedRune(IEntityData entityData, int majorRealm, ResourceLocation runeId) {
        ResourceLocation selected = getRuneData(entityData).getSelectedRune(majorRealm);
        return runeId.equals(selected);
    }

    // SELECTION
    public static boolean autoSelectRuneIfEmpty(RunicRuneData runeData, ResourceLocation runeId) {
        Rune rune = Runes.get(runeId);
        if (rune == null) return false;

        int realm = rune.getMajorRealm();

        if (runeData.getSelectedRune(realm) != null) {
            return false;
        }

        return runeData.setSelectedRune(realm, runeId);
    }

    // Refresh Skills
    public static void refreshSkill(IEntityData entityData, ResourceLocation skillId, boolean shouldHave) {
        if (entityData == null) return;

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

    public static void refreshFleshRuneSkills(IEntityData entityData, RunicRuneData runeData) {
        if (entityData == null || runeData == null) return;

        ResourceLocation selected = runeData.getSelectedRune(0);

        // Armor Rune → Fortification
        refreshSkill(
                entityData,
                ModSkills.RUNIC_FORTIFICATION.getId(),
                Runes.ARMOR.getId().equals(selected)
        );

        // Strength Rune → Runic Strength
        refreshSkill(
                entityData,
                ModSkills.RUNIC_STRENGTH.getId(),
                Runes.STRENGTH.getId().equals(selected)
        );
    }


}