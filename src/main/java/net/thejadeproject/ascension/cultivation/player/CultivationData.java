package net.thejadeproject.ascension.cultivation.player;

import net.minecraft.nbt.CompoundTag;

import java.util.Collection;
import java.util.HashMap;

public class CultivationData {

    public static class PathData{
        public String pathId;
        public int majorRealm;
        public int minorRealm;
        public double pathProgress; //todo replace with big decimal
        public String technique;
        public double stabilityCultivationTicks;

        public boolean cultivating = false;

        public boolean isCultivating(){return cultivating;}
        public void setCultivating(boolean state){
            cultivating = state;
        }

        public PathData(String pathId, int majorRealm, int minorRealm,double pathProgress,String technique,double stabilityCultivationTicks){
            this.pathId = pathId;
            this.majorRealm = majorRealm;
            this.minorRealm = minorRealm;
            this.pathProgress = pathProgress;
            this.technique = technique;
            this.stabilityCultivationTicks=stabilityCultivationTicks;

        }
        public PathData(){}
        public CompoundTag writeNBTData(){
            CompoundTag tag = new CompoundTag();
            tag.putString("path_id",pathId);
            tag.putInt("major_realm",majorRealm);
            tag.putInt("minor_realm",minorRealm);
            tag.putDouble("progress",pathProgress);
            tag.putString("technique",technique);
            tag.putDouble("stability_cultivation_ticks",stabilityCultivationTicks);
            return tag;

        }
        public static PathData loadNBTData(CompoundTag compound){
            PathData pathData = new PathData();
            pathData.pathId =  compound.getString("path_id");
            pathData.majorRealm = compound.getInt("major_realm");
            pathData.minorRealm = compound.getInt("minor_realm");
            pathData.pathProgress = compound.getDouble("progress");
            pathData.technique = compound.getString("technique");
            pathData.stabilityCultivationTicks = compound.getDouble("stability_cultivation_ticks");
            return pathData;
        }

        @Override
        public String toString() {
            return "PathData{" +
                    "pathId='" + pathId + '\'' +
                    ", majorRealm=" + majorRealm+ '\'' +
                    ", minorRealm=" + minorRealm + '\''+
                    ", pathProgress=" + pathProgress + '\'' +
                    ", technique='" + technique + '\'' +
                    ", stability_cultivation_ticks="+stabilityCultivationTicks + '\'' +
                    ", cultivating=" + cultivating +
                    '}';
        }
    }


    private final HashMap<String, PathData> pathDataHashMap = new HashMap<>();

    public PathData getPathData(String pathId){

        if(pathDataHashMap.containsKey(pathId)) return pathDataHashMap.get(pathId);
        PathData data = new PathData(pathId,0,0,0,"ascension:none",0);
        pathDataHashMap.put(pathId,data);
        return data;
    }

    public Collection<PathData> getPaths(){
        return pathDataHashMap.values();
    }

    public void setPathProgress(String pathId,double pathProgress){
        pathDataHashMap.get(pathId).pathProgress = pathProgress;
    }

    public void setPathMajorRealm(String pathId,int majorRealm){
        pathDataHashMap.get(pathId).majorRealm = majorRealm;
    }
    public void setPathMinorRealm(String pathId,int minorRealm){
        pathDataHashMap.get(pathId).minorRealm = minorRealm;
    }

    public void setPathTechnique(String pathId,String technique){pathDataHashMap.get(pathId).technique = technique;}

    public CompoundTag writeNBTData(){
        CompoundTag tag = new CompoundTag();
        for(PathData dataEntry : pathDataHashMap.values()){
            tag.put(dataEntry.pathId,dataEntry.writeNBTData());
        }
        return tag;
    }
    public void loadNBTData(CompoundTag compound){
        for(String key:compound.getAllKeys()){
            pathDataHashMap.put(key, PathData.loadNBTData(compound.getCompound(key)));
        }
    }

}
