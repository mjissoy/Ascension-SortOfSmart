package net.thejadeproject.ascension.cultivation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class CultivationSystem {
    private static final float MINOR_REALM_MULTIPLIER = 0.03f;
    private static final float MAJOR_REALM_MULTIPLIER = 0.3f;
    private static final float MINOR_REALM_PROGRESS_MULTIPLIER = 0.5f;
    private static final float MAJOR_REALM_PROGRESS_MULTIPLIER = 2.0f;

    private static final String[] majorRealmNames = {
            "Mortal", "Qi Condensation", "Foundation Establishment",
            "Core Formation", "Nascent Soul", "Spirit Severing",
            "Soul Formation", "Soul Transformation", "Immortal Ascension",
            "True Immortal", "Golden Immortal"
    };

    public static void initPlayerCultivation(Player player) {
        CultivationData.player = player;
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag cultivationData = persistentData.getCompound("Cultivation");

        if (!cultivationData.contains("MajorRealm")) {
            cultivationData.putInt("MajorRealm", 0);
            cultivationData.putInt("MinorRealm", 0);
            cultivationData.putInt("CultivationProgress", 0);
            cultivationData.putBoolean("CultivationState",false);
            persistentData.put("Cultivation", cultivationData);

            updatePlayerAttributes(player);
        }
    }




    public static void cultivate(Player player) {
        CompoundTag cultivationData = player.getPersistentData().getCompound("Cultivation");
        int majorRealmProgress = cultivationData.getInt("MajorRealm");
        int minorRealmProgress = cultivationData.getInt("MinorRealm");
        //Cultivation Stage Max
        float CultivationStageMax = 1.0f +
                (majorRealmProgress * MAJOR_REALM_PROGRESS_MULTIPLIER) *
                (minorRealmProgress * MINOR_REALM_PROGRESS_MULTIPLIER);

        if (player.level().isClientSide) return;

        float progress = cultivationData.getFloat("CultivationProgress");
        if(player.getPersistentData().getCompound("Cultivation").getInt("MajorRealm") >= majorRealmNames.length) return;
        if (player.getPersistentData().getCompound("Cultivation").getInt("MajorRealm") == majorRealmNames.length-1 && player.getPersistentData().getCompound("Cultivation").getInt("MinorRealm") == 9) {
            return;
        }
        progress += 10.01f;
        if (progress >= CultivationStageMax) {
            progress = 0;
            int minorRealm = cultivationData.getInt("MinorRealm");
            minorRealm++;

            if (minorRealm >= 10) {
                minorRealm = 0;
                cultivationData.putInt("MajorRealm", cultivationData.getInt("MajorRealm") + 1);
            }

            cultivationData.putInt("MinorRealm", minorRealm);
            updatePlayerAttributes(player);


        }

        cultivationData.putFloat("CultivationProgress", progress);
        player.getPersistentData().put("Cultivation", cultivationData);
    }

    public static void updatePlayerAttributes(Player player) {
        CompoundTag cultivationData = player.getPersistentData().getCompound("Cultivation");
        int majorRealm = cultivationData.getInt("MajorRealm");
        int minorRealm = cultivationData.getInt("MinorRealm");

        float totalMultiplier = 1.0f +
                (majorRealm * MAJOR_REALM_MULTIPLIER) +
                (minorRealm * MINOR_REALM_MULTIPLIER);

        player.getAttribute(Attributes.MAX_HEALTH)
                .setBaseValue(20.0 * totalMultiplier);
        player.getAttribute(Attributes.ATTACK_DAMAGE)
                .setBaseValue(1.0 * totalMultiplier);
        player.getAttribute(Attributes.ATTACK_SPEED)
                .setBaseValue(4.0 * totalMultiplier);
        player.getAttribute(Attributes.MOVEMENT_SPEED)
                .setBaseValue(0.1 * totalMultiplier);
        player.getAttribute(Attributes.JUMP_STRENGTH)
                .setBaseValue(0.42 * totalMultiplier);

        player.setHealth(player.getMaxHealth());
    }

    public static String getMajorRealmName(int majorRealm){
        if(majorRealm >= majorRealmNames.length) return "Boundless";
        return majorRealmNames[majorRealm];
    }

    public static String getRealmName(int majorRealm, int minorRealm) {


        String name = majorRealm < majorRealmNames.length ?
                majorRealmNames[majorRealm] :
                "Realm " + (majorRealm + 1);
        return name + " Stage " + minorRealm;
    }
}
