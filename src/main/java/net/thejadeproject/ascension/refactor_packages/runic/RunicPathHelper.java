package net.thejadeproject.ascension.refactor_packages.runic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;
import net.thejadeproject.ascension.refactor_packages.runic.items.RunicBrushItem;
import net.thejadeproject.ascension.refactor_packages.runic.runes.IRunicRune;
import net.thejadeproject.ascension.refactor_packages.runic.runes.ModRunicRunes;

import java.util.Collection;
import java.util.List;

public final class RunicPathHelper {

    private static final String RUNIC_DATA_TAG = "ascension_runic";

    private RunicPathHelper() {
    }

    public static boolean hasEnteredRunicPath(LivingEntity entity) {
        if (entity == null || !entity.hasData(ModAttachments.ENTITY_DATA)) {
            return false;
        }

        return hasEnteredRunicPath(entity.getData(ModAttachments.ENTITY_DATA));
    }

    public static boolean hasEnteredRunicPath(IEntityData entityData) {
        return entityData != null && entityData.hasPath(ModPaths.RUNIC.getId());
    }

    public static int getRunicMajorRealm(IEntityData entityData) {
        if (!hasEnteredRunicPath(entityData)) {
            return 0;
        }

        PathData pathData = entityData.getPathData(ModPaths.RUNIC.getId());

        if (pathData == null) {
            return 0;
        }

        return pathData.getMajorRealm();
    }

    public static int getRuneSlotCount(IEntityData entityData) {
        if (!hasEnteredRunicPath(entityData)) {
            return 0;
        }

        int majorRealm = getRunicMajorRealm(entityData);

        return switch (majorRealm) {
            case 0, 1 -> 2;
            case 2, 3 -> 3;
            case 4, 5 -> 4;
            case 6, 7 -> 5;
            case 8 -> 6;
            default -> 7;
        };
    }

    public static int getRuneSlotCount(LivingEntity entity, boolean includeHeldBrush) {
        if (entity == null || !entity.hasData(ModAttachments.ENTITY_DATA)) {
            return 0;
        }

        int slots = getRuneSlotCount(entity.getData(ModAttachments.ENTITY_DATA));

        if (!includeHeldBrush) {
            return slots;
        }

        ItemStack mainHand = entity.getMainHandItem();

        if (mainHand.getItem() instanceof RunicBrushItem brushItem) {
            slots += Math.max(0, brushItem.getExtraRuneSlots());
        }

        return slots;
    }

    public static int getCastingDurationSeconds(IEntityData entityData) {
        if (!hasEnteredRunicPath(entityData)) {
            return 0;
        }

        int majorRealm = getRunicMajorRealm(entityData);
        return 3 + Math.max(0, majorRealm);
    }

    public static boolean canObserveRune(IEntityData entityData, ResourceLocation runeId) {
        IRunicRune rune = ModRunicRunes.get(runeId);

        if (rune == null || !hasEnteredRunicPath(entityData)) {
            return false;
        }

        return getRunicMajorRealm(entityData) >= rune.getMinimumRunicRealmToObserve();
    }

    public static boolean canUseRune(IEntityData entityData, ResourceLocation runeId) {
        IRunicRune rune = ModRunicRunes.get(runeId);

        if (rune == null || !hasEnteredRunicPath(entityData)) {
            return false;
        }

        return getRunicMajorRealm(entityData) >= rune.getMinimumRunicRealmToUse();
    }

    public static boolean canUseRune(LivingEntity entity, ResourceLocation runeId) {
        if (entity == null || !entity.hasData(ModAttachments.ENTITY_DATA)) {
            return false;
        }

        IEntityData entityData = entity.getData(ModAttachments.ENTITY_DATA);
        RunicPlayerData runicData = getRunicData(entity);

        return runicData.knowsRune(runeId) && canUseRune(entityData, runeId);
    }

    public static List<ResourceLocation> getUsableKnownRunes(LivingEntity entity) {
        if (entity == null || !entity.hasData(ModAttachments.ENTITY_DATA)) {
            return List.of();
        }

        IEntityData entityData = entity.getData(ModAttachments.ENTITY_DATA);
        RunicPlayerData runicData = getRunicData(entity);

        return runicData.getKnownRunes().stream()
                .filter(runeId -> canUseRune(entityData, runeId))
                .sorted((first, second) -> first.toString().compareTo(second.toString()))
                .toList();
    }

    public static boolean knowsRune(LivingEntity entity, ResourceLocation runeId) {
        return entity != null && getRunicData(entity).knowsRune(runeId);
    }

    public static RunicPlayerData getRunicData(LivingEntity entity) {
        RunicPlayerData data = new RunicPlayerData();

        if (entity == null) {
            return data;
        }

        CompoundTag persistentData = entity.getPersistentData();

        if (persistentData.contains(RUNIC_DATA_TAG)) {
            data.deserializeNBT(persistentData.getCompound(RUNIC_DATA_TAG));
        }

        return data;
    }

    public static void saveRunicData(LivingEntity entity, RunicPlayerData data) {
        if (entity == null || data == null) {
            return;
        }

        entity.getPersistentData().put(RUNIC_DATA_TAG, data.serializeNBT());
    }

    public static boolean learnRune(LivingEntity entity, ResourceLocation runeId) {
        if (entity == null || ModRunicRunes.get(runeId) == null) {
            return false;
        }

        RunicPlayerData data = getRunicData(entity);

        if (data.knowsRune(runeId)) {
            return false;
        }

        data.addKnownRune(runeId);
        saveRunicData(entity, data);
        return true;
    }

    public static int learnRunes(LivingEntity entity, Collection<ResourceLocation> runeIds) {
        if (entity == null || runeIds == null || runeIds.isEmpty()) {
            return 0;
        }

        RunicPlayerData data = getRunicData(entity);
        int learned = 0;

        for (ResourceLocation runeId : runeIds) {
            if (ModRunicRunes.get(runeId) == null) {
                continue;
            }

            if (data.knowsRune(runeId)) {
                continue;
            }

            data.addKnownRune(runeId);
            learned++;
        }

        if (learned > 0) {
            saveRunicData(entity, data);
        }

        return learned;
    }
}
