package net.thejadeproject.ascension.cultivation;

import net.minecraft.nbt.CompoundTag;

public class ClientCultivationData {
    private static CompoundTag data = new CompoundTag();
    public static boolean isCultivating = false;

    public static void update(CompoundTag newData) {
        data = newData;
    }

    public static int getMajorRealm() {
        return data.getInt("MajorRealm");
    }

    public static int getMinorRealm() {
        return data.getInt("MinorRealm");
    }

    public static float getProgress() {
        return data.getFloat("Progress");
    }

    public static void startCultivation(CompoundTag newData) {
        data = newData;
    }

    public static void startCultivation(CompoundTag newData) {
        data = newData;
    }

    public static void setCultivating(boolean cultivating) {
        isCultivating = cultivating;
    }

    public static boolean isCultivating() {
        return isCultivating;
    }

}