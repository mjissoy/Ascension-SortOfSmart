package net.thejadeproject.ascension.cultivation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.Config;
import net.thejadeproject.ascension.events.custom.CultivateEvent;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.events.custom.MajorRealmChangeEvent;
import net.thejadeproject.ascension.events.custom.MinorRealmChangeEvent;
import net.thejadeproject.ascension.network.clientBound.SyncAttackDamageAttribute;
import net.thejadeproject.ascension.network.clientBound.SyncPathDataPayload;
import net.thejadeproject.ascension.util.ModAttachments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CultivationSystem {
    private static final float MAJOR_REALM_MULTIPLIER = 0.3f;
    private static final float MINOR_REALM_PROGRESS_MULTIPLIER = 0.5f;
    private static final float MAJOR_REALM_PROGRESS_MULTIPLIER = 2.0f;

    private static final double BASE_PROGRESS_INCREASE = 0.2;

    private static final HashMap<String,String[]> realmNameMap = new HashMap<>(){{
        put("ascension:essence",new String[]{   "Mortal", "Qi Condensation", "Foundation Establishment",
                "Core Formation", "Nascent Soul", "Spirit Severing",
                "Soul Formation", "Soul Transformation", "Immortal Ascension",
                "True Immortal", "Golden Immortal"});
        put("ascension:intent",new String[]{ "Third rate","Second Rate","First Rate",
                "Peak Master","Three Flower Summit","Converging 5 Energies",
                "Pinnacle","Ultimate Pinnacle","First Stage Manifestation",
                "Treading Heaven","Heaven Ultimate Pinnacle"});
        put("ascension:body",new String[]{
                "mortal tempering","mortal inner tempering","mortal true tempering",
                "foundation tempering","foundation inner tempering","foundation true tempering",
                "immortal tempering", "immortal inner tempering","immortal true tempering",
                "true immortal tempering","true immortal inner tempering","true immortal true tempering"
        });
    }};

    public static String getPathMajorRealmName(String pathId,int majorRealm){
        return realmNameMap.get(pathId)[majorRealm];
    }

    private static final String[] essenceMajorRealmNames = {
            "Mortal", "Qi Condensation", "Foundation Establishment",
            "Core Formation", "Nascent Soul", "Spirit Severing",
            "Soul Formation", "Soul Transformation", "Immortal Ascension",
            "True Immortal", "Golden Immortal"
    };
    public static void cultivate(Player player, String path){
        //TODO change to use technique for base rate
        //TODO fire is temp
        PlayerData.PathData pathData = player.getData(ModAttachments.PLAYER_DATA).getPathData(path);
        System.out.println("path Progress:" + pathData.pathProgress);
        //TODO store the max realm some where
        if(pathData.majorRealm >= realmNameMap.get(path).length-1 && pathData.minorRealm >= 9) return;

        GatherEfficiencyModifiersEvent effEvent = new GatherEfficiencyModifiersEvent(player,path,"ascension:fire");
        NeoForge.EVENT_BUS.post(effEvent);
        CultivateEvent cultivateEvent = new CultivateEvent(player,BASE_PROGRESS_INCREASE,path,List.of("ascension:fire"));
        NeoForge.EVENT_BUS.post(cultivateEvent);

        double progressIncrement = (cultivateEvent.baseRate+cultivateEvent.flatBaseRateIncrease)
                *(1+cultivateEvent.multiplier)
                *(1+effEvent.getTotalEfficiencyMultiplier())
                +cultivateEvent.flatFinalRateIncrease;
        System.out.println(progressIncrement);
        float CultivationStageMax = 1.0f +
                (pathData.majorRealm * MAJOR_REALM_PROGRESS_MULTIPLIER) *
                        (pathData.minorRealm * MINOR_REALM_PROGRESS_MULTIPLIER);

        double progress = pathData.pathProgress + progressIncrement;

        if(progress >= CultivationStageMax){
            //minor realm increase
            progress = 0;
            int minorRealm = pathData.minorRealm;
            minorRealm ++;
            if(minorRealm >= 10){
                pathData.majorRealm += 1;
                pathData.minorRealm = 0;
                NeoForge.EVENT_BUS.post(new MajorRealmChangeEvent(player,path, pathData.majorRealm-1, pathData.majorRealm));
            }else{
                NeoForge.EVENT_BUS.post(new MinorRealmChangeEvent(player,path, pathData.minorRealm,minorRealm ));
                pathData.minorRealm = minorRealm;
            }
        }
        pathData.pathProgress = progress;
        System.out.println(pathData.pathProgress);
        PacketDistributor.sendToPlayer((ServerPlayer) player,new SyncPathDataPayload(path, pathData.majorRealm, pathData.minorRealm, pathData.pathProgress));
    }



    private static final String[] majorRealmNames = {
            "Mortal", "Qi Condensation", "Foundation Establishment",
            "Core Formation", "Nascent Soul", "Spirit Severing",
            "Soul Formation", "Soul Transformation", "Immortal Ascension",
            "True Immortal", "Golden Immortal"
    };
    public static List<Integer> getRealmsIdList(){
        List<Integer> list = new ArrayList<>();
        for(int i = 0;i < majorRealmNames.length;i++){
            list.add(i);
        }
        return list;
    }

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
                updateMajorRealm(player);
            }

            cultivationData.putInt("MinorRealm", minorRealm);
            //updatePlayerAttributes(player);
            updateMinorRealm(player);

        }

        cultivationData.putFloat("CultivationProgress", progress);
        player.getPersistentData().put("Cultivation", cultivationData);
    }
    public static void updateMajorRealm(Player player){
        CompoundTag cultivationData = player.getPersistentData().getCompound("Cultivation");
        int majorRealm = cultivationData.getInt("MajorRealm");
        if(Config.Common.MAX_HEALTH_APPLICABLE_REALMS.get().contains(majorRealm)) {
            incrementBaseValue(player, player.getAttribute(Attributes.MAX_HEALTH), Config.Common.MAJOR_REALM_MAX_HEALTH_INCREASE.get());
        }
        if(Config.Common.ATTACK_DAMAGE_APPLICABLE_REALMS.get().contains(majorRealm)) {
            incrementBaseValue(player, player.getAttribute(Attributes.ATTACK_DAMAGE), Config.Common.MAJOR_REALM_ATTACK_DAMAGE_INCREASE.get());
        }
        if(Config.Common.ATTACK_SPEED_APPLICABLE_REALMS.get().contains(majorRealm)) {
            incrementBaseValue(player, player.getAttribute(Attributes.ATTACK_SPEED), Config.Common.MAJOR_REALM_ATTACK_SPEED_INCREASE.get());
        }
        if(Config.Common.JUMP_STRENGTH_APPLICABLE_REALMS.get().contains(majorRealm)) {
            incrementBaseValue(player, player.getAttribute(Attributes.JUMP_STRENGTH), Config.Common.MAJOR_REALM_JUMP_STRENGTH_INCREASE.get());
        }
        if(Config.Common.MOVEMENT_SPEED_APPLICABLE_REALMS.get().contains(majorRealm)) {
            if (player.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue() < Config.Common.MAX_SPEED_MULT.get()) {
                incrementBaseValue(player, player.getAttribute(Attributes.MOVEMENT_SPEED), Config.Common.MAJOR_REALM_MOVEMENT_SPEED_INCREASE.get());

            }
            else{
                player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Config.Common.MAX_SPEED_MULT.get());
            }
            player.setData(ModAttachments.MOVEMENT_SPEED,player.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue());

        }
        PacketDistributor.sendToPlayer((ServerPlayer) player,new SyncAttackDamageAttribute(player.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));
    }
    public static void updateMinorRealm(Player player){
        CompoundTag cultivationData = player.getPersistentData().getCompound("Cultivation");

        int majorRealm = cultivationData.getInt("MajorRealm");
        if(Config.Common.MAX_HEALTH_APPLICABLE_REALMS.get().contains(majorRealm)) {
            incrementBaseValue(player, player.getAttribute(Attributes.MAX_HEALTH), Config.Common.MINOR_REALM_MAX_HEALTH_INCREASE.get());
        }
        if(Config.Common.ATTACK_DAMAGE_APPLICABLE_REALMS.get().contains(majorRealm)) {
            incrementBaseValue(player, player.getAttribute(Attributes.ATTACK_DAMAGE), Config.Common.MINOR_REALM_ATTACK_DAMAGE_INCREASE.get());
        }
        if(Config.Common.ATTACK_SPEED_APPLICABLE_REALMS.get().contains(majorRealm)) {
            incrementBaseValue(player, player.getAttribute(Attributes.ATTACK_SPEED), Config.Common.MINOR_REALM_ATTACK_SPEED_INCREASE.get());
        }
        if(Config.Common.JUMP_STRENGTH_APPLICABLE_REALMS.get().contains(majorRealm)) {
            incrementBaseValue(player, player.getAttribute(Attributes.JUMP_STRENGTH), Config.Common.MINOR_REALM_JUMP_STRENGTH_INCREASE.get());
        }
        if(Config.Common.MOVEMENT_SPEED_APPLICABLE_REALMS.get().contains(majorRealm)) {
            if (player.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue() < Config.Common.MAX_SPEED_MULT.get()) {
                incrementBaseValue(player, player.getAttribute(Attributes.MOVEMENT_SPEED), Config.Common.MINOR_REALM_MOVEMENT_SPEED_INCREASE.get());

            }else{
                player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Config.Common.MAX_SPEED_MULT.get());
            }
            player.setData(ModAttachments.MOVEMENT_SPEED,player.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue());
        }

        PacketDistributor.sendToPlayer((ServerPlayer) player,new SyncAttackDamageAttribute(player.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));

    }
    public static void incrementBaseValue(Player player, AttributeInstance attribute, Double value){
        attribute.setBaseValue(attribute.getBaseValue()+value);
    }
    public static void incrementBaseValue(AttributeInstance attribute,Integer value){
        attribute.setBaseValue(attribute.getBaseValue()+value);
    }
    public static void updatePlayerAttributes(Player player) {
        CompoundTag cultivationData = player.getPersistentData().getCompound("Cultivation");
        int majorRealm = cultivationData.getInt("MajorRealm");
        int minorRealm = cultivationData.getInt("MinorRealm");

        float totalMultiplier = (float) ((majorRealm * Config.Common.MAJOR_REALM_MULTIPLIER.get()) +
                        (minorRealm * Config.Common.MINOR_REALM_MULTIPLIER.get()));

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
