package net.thejadeproject.ascension.cultivation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.server.command.NeoForgeCommand;
import net.thejadeproject.ascension.Config;

public class CultivationSystem {
    private static final float MAJOR_REALM_MULTIPLIER = 0.3f;
    private static final float MINOR_REALM_PROGRESS_MULTIPLIER = 0.5f;
    private static final float MAJOR_REALM_PROGRESS_MULTIPLIER = 2.0f;


    private static final String[] majorRealmNames = {
            "Mortal", "Qi Condensation", "Foundation Establishment",
            "Core Formation", "Nascent Soul", "Spirit Severing",
            "Soul Formation", "Soul Transformation", "Immortal Ascension",
            "True Immortal", "Golden Immortal"
    };

    public static final int RealmAmount = majorRealmNames.length;

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


        if (player.level().isClientSide()) return;

        float progress = cultivationData.getFloat("CultivationProgress");
        if(player.getPersistentData().getCompound("Cultivation").getInt("MajorRealm") >= majorRealmNames.length) return;
        if (player.getPersistentData().getCompound("Cultivation").getInt("MajorRealm") == majorRealmNames.length-1 && player.getPersistentData().getCompound("Cultivation").getInt("MinorRealm") == 9) {
            return;
        }
        progress +=  Config.Common.PROGRESS_SPEED.get();
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

        float totalMultiplier = (float) ((majorRealm * Config.Common.MAJOR_REALM_MULTIPLIER.get()) +
                        (minorRealm * Config.Common.MINOR_REALM_MULTIPLIER.get()));

        player.getAttribute(Attributes.MAX_HEALTH)
                .setBaseValue(20.0 + 1.0 * totalMultiplier);
        player.getAttribute(Attributes.ATTACK_DAMAGE)
                .setBaseValue(2.0 + 1.0 * totalMultiplier);
        player.getAttribute(Attributes.ATTACK_SPEED)
                .setBaseValue(4.0 + 4.0 * totalMultiplier);
        player.getAttribute(Attributes.JUMP_STRENGTH)
                .setBaseValue(0.42 + 0.42 * totalMultiplier);
        player.getAttribute(Attributes.SAFE_FALL_DISTANCE)
                .setBaseValue( 3 + 5 * totalMultiplier);
        if (0.1 + 0.1 * totalMultiplier < Config.Common.MAX_SPEED_MULT.get()) {
            player.getAttribute(Attributes.MOVEMENT_SPEED)
                    .setBaseValue(0.1 + 0.1 * totalMultiplier);
        }

        player.setHealth(player.getMaxHealth());

        if (majorRealm >= Config.Common.FLIGHT_REALM.get()) {
            player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT)
                    .setBaseValue(1);
        } else {
            player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT)
                    .setBaseValue(0);
        }
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
