package net.thejadeproject.ascension.cultivation;

import net.minecraft.nbt.CompoundTag;

public class ClientCultivationData {
    private static CompoundTag cultivationData = new CompoundTag();

    public static void update(CompoundTag data) {
        cultivationData = data;
    }

    public static int getMajorRealm() {
        return cultivationData.getInt("MajorRealm");
    }

    public static int getMinorRealm() {
        return cultivationData.getInt("MinorRealm");
    }

    public static float getCultivationProgress() {
        return cultivationData.getInt("CultivationProgress");
    }
}
