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
import net.thejadeproject.ascension.cultivation.player.CultivationData;
import net.thejadeproject.ascension.cultivation.player.PlayerData;
import net.thejadeproject.ascension.events.custom.CultivateEvent;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.events.custom.MajorRealmChangeEvent;
import net.thejadeproject.ascension.events.custom.MinorRealmChangeEvent;
import net.thejadeproject.ascension.network.clientBound.SyncAttackDamageAttribute;
import net.thejadeproject.ascension.network.clientBound.SyncPathDataPayload;
import net.thejadeproject.ascension.progression.techniques.stability_handlers.StabilityHandler;
import net.thejadeproject.ascension.util.ModAttachments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CultivationSystem {
    private static final float MAJOR_REALM_MULTIPLIER = 0.3f;
    private static final float MINOR_REALM_PROGRESS_MULTIPLIER = 0.35f;
    private static final float MAJOR_REALM_PROGRESS_MULTIPLIER = 1.37f;

    private static final double BASE_PROGRESS_INCREASE = 0.2;

    private static final HashMap<String,String[]> realmNameMap = new HashMap<>(){{
        put("ascension:essence",new String[]{   "Mortal", "Qi Condensation", "Foundation Establishment",
                "Core Formation", "Nascent Soul", "Soul Formation",
                "Soul Transformation", "Spirit Severing", "Immortal Ascension",
                "True Immortal", "Golden Immortal", "Universe Creation Realm"});
        put("ascension:intent",new String[]{ "Awakened Will","Focused Mind","Sharpened Desire",
                "Unwavering Resolve","Trinity Convergence","Pentagonal Balance",
                "Aura of Intent","Soul Pressure","Spatial Lock",
                "Domain Seed", "Law Embodiment", "Heavenly Decree"});
        put("ascension:body",new String[]{
                "Skin Tempering","Sinew Weaving","Bone Forging",
                "Heartkindle Stage","Meridian Flow","Core Consolidation",
                "Vessel Refinement", "Energy Harmonization","Nether Transformation",
                "Stellar Genesis","World Resonance","Primordial Awakening"
        });
    }};
    public static int getRealmNumber(String pathId){
        return realmNameMap.get(pathId).length;
    }
    public static String getPathMajorRealmName(String pathId,int majorRealm){
        return realmNameMap.get(pathId)[majorRealm];
    }

    public static void stabiliseRealm(StabilityHandler handler, Player player, String path, List<String> attributes, double minCultivationTickRate){
        CultivationData.PathData pathData = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(path);
        double currentStabilityTicks = pathData.stabilityCultivationTicks;

        if(currentStabilityTicks == handler.getMaxCultivationTicks()) return;
        if(pathData.majorRealm >= realmNameMap.get(path).length-1) return;

        GatherEfficiencyModifiersEvent effEvent = new GatherEfficiencyModifiersEvent(player,path,attributes);
        NeoForge.EVENT_BUS.post(effEvent);

        System.out.println("stabilising realm with effectiveness: "+effEvent.getTotalEfficiencyMultiplier());

        double cultivationTicks = currentStabilityTicks+ 1*effEvent.getTotalEfficiencyMultiplier();
        System.out.println("stabilising realm with "+cultivationTicks +" cultivation ticks");
        System.out.println("current Stability = "+handler.getStability(cultivationTicks));
        pathData.stabilityCultivationTicks = Math.min(cultivationTicks, handler.getMaxCultivationTicks());
        PacketDistributor.sendToPlayer((ServerPlayer) player,new SyncPathDataPayload(path, pathData.majorRealm, pathData.minorRealm, pathData.pathProgress,pathData.technique, pathData.stabilityCultivationTicks));

    }

    //returns false if realm is not increased
    public static boolean cultivate(Player player, String path,Double baseRate,List<String> attributes){
        //TODO change to use technique for base rate
        //TODO fire is temp
        CultivationData.PathData pathData = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(path);
        System.out.println("path Progress:" + pathData.pathProgress);
        //TODO store the max realm some where
        if(pathData.majorRealm >= realmNameMap.get(path).length-1 && pathData.minorRealm >= 9) return false;
        GatherEfficiencyModifiersEvent effEvent = new GatherEfficiencyModifiersEvent(player,path,attributes);
        NeoForge.EVENT_BUS.post(effEvent);

        System.out.println("cultivating with effectiveness: "+effEvent.getTotalEfficiencyMultiplier());
        CultivateEvent cultivateEvent = new CultivateEvent(player,baseRate,path,attributes);
        NeoForge.EVENT_BUS.post(cultivateEvent);

        double progressIncrement = (cultivateEvent.baseRate+cultivateEvent.flatBaseRateIncrease)
                *(1+cultivateEvent.multiplier)
                *(effEvent.getTotalEfficiencyMultiplier())
                +cultivateEvent.flatFinalRateIncrease;
        System.out.println("base: "+cultivateEvent.baseRate);
        System.out.println(progressIncrement);
        //TODO work out what the hell i want to do with this
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
                return false;
            }else{
                NeoForge.EVENT_BUS.post(new MinorRealmChangeEvent(player,path, pathData.minorRealm,minorRealm ));
                pathData.minorRealm = minorRealm;
            }
        }
        pathData.pathProgress = progress;
        System.out.println(pathData.pathProgress);
        PacketDistributor.sendToPlayer((ServerPlayer) player,new SyncPathDataPayload(path, pathData.majorRealm, pathData.minorRealm, pathData.pathProgress,pathData.technique, pathData.stabilityCultivationTicks));
        return true;
    }



    private static final String[] majorRealmNames = {
            "Mortal", "Qi Condensation", "Foundation Establishment",
            "Core Formation", "Nascent Soul", "Spirit Severing",
            "Soul Formation", "Soul Transformation", "Immortal Ascension",
            "True Immortal", "Golden Immortal", "Universe Creation Realm"
    };
    //wtf is this
    public static List<Integer> getRealmsIdList(){
        List<Integer> list = new ArrayList<>();
        for(int i = 0;i < majorRealmNames.length;i++){
            list.add(i);
        }
        return list;
    }

    public static final int RealmAmount = majorRealmNames.length;






    public static String getMajorRealmName(int majorRealm){
        if(majorRealm >= majorRealmNames.length) return "Boundless";
        return majorRealmNames[majorRealm];
    }


}
