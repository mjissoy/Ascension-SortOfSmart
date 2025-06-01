package net.thejadeproject.ascension.cultivation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class CultivationData {
    private static CompoundTag data = new CompoundTag();
    public static boolean isCultivating = false;
    public static void startCultivation(CompoundTag newData) {
        data = newData;
    }

    public static void setCultivating(boolean cultivating) {
        isCultivating = cultivating;
    }

    public static boolean isCultivating() {
        return isCultivating;
    }
    public static class ClientCultivationData {

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


        public static Component getProgressUI() {
            return Component.literal(String.valueOf(data.getInt("Progress")));
        }

        public static Component getMajorRealmUI() {
            return Component.literal(String.valueOf(data.getInt("MajorRealm")));
        }

        public static Component getMinorRealmUI() {
            return Component.literal(String.valueOf(data.getInt("MinorRealm")));
        }
    }
}