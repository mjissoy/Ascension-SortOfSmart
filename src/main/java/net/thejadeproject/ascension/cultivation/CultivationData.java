package net.thejadeproject.ascension.cultivation;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.network.serverBound.SyncCultivationPayload;

public class CultivationData {
    public static Player player;


    public static void setCultivating(boolean cultivating) {
        if(cultivating) System.out.println("KeyDown");
        if(player.getPersistentData().getCompound("Cultivation").getBoolean("CultivationState") == cultivating) return;
        player.getPersistentData().getCompound("Cultivation").putBoolean("CultivationState",cultivating);


        PacketDistributor.sendToServer(new SyncCultivationPayload(cultivating));

    }

    public static boolean isCultivating() {
        return player.getPersistentData().getCompound("Cultivation").getBoolean("CultivationState");
    }
    public static class ClientCultivationData {


        public static boolean getCultivationState(){
            return player.getPersistentData().getCompound("Cultivation").getBoolean("CultivationState");
        }

        public static int getMajorRealm() {
            return player.getPersistentData().getCompound("Cultivation").getInt("MajorRealm");
        }

        public static int getMinorRealm() {
            return player.getPersistentData().getCompound("Cultivation").getInt("MinorRealm");
        }

        public static float getProgress() {
            return player.getPersistentData().getCompound("Cultivation").getFloat("CultivationProgress");
        }


        public static Component getProgressUI() {

            return Component.literal(String.valueOf(getProgress()));
        }

        public static Component getMajorRealmUI() {
            return Component.literal(CultivationSystem.getMajorRealmName(getMajorRealm()));
        }

        public static Component getMinorRealmUI( ) {
            return Component.literal(String.valueOf(getMinorRealm()));
        }
    }

    public static class PlayerCultivationData{
        public static int getMajorRealm(String path,Player player){
            return player.getPersistentData().getCompound("Cultivation").getCompound(path).getInt("MajorRealm");
        }
        public static int getMinorRealm(String path,Player player){
            return player.getPersistentData().getCompound("Cultivation").getCompound(path).getInt("MinorRealm");
        }
        public static int getProgress(String path,Player player){
            return player.getPersistentData().getCompound("Cultivation").getCompound(path).getInt("Progress");
        }
    }
}