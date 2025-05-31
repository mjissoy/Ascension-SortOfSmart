package net.thejadeproject.ascension.cultivation;

import net.minecraft.nbt.CompoundTag;

public class ClientCultivationData {
    private static CompoundTag data = new CompoundTag();

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
}