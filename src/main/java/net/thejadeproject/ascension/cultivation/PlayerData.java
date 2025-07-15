package net.thejadeproject.ascension.cultivation;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class PlayerData {
    public PlayerData(ServerPlayer player){

    }
    /********* CULTIVATION PROGRESS *******************************************************/

    public static class PathData{
        public String pathId;
        public int majorRealm;
        public int minorRealm;
        public double pathProgress;

        public PathData(String pathId, int majorRealm, int minorRealm,double pathProgress){
            this.pathId = pathId;
            this.majorRealm = majorRealm;
            this.minorRealm = minorRealm;
            this.pathProgress = pathProgress;

        }
        public CompoundTag writePathNBTData(){
            CompoundTag tag = new CompoundTag();
            tag.putString("path_id",pathId);
            tag.putInt("major_realm",majorRealm);
            tag.putInt("minor_realm",minorRealm);
            tag.putDouble("progress",pathProgress);
            return tag;

        }
        public void loadPathNBTData(CompoundTag compound, HolderLookup.Provider provider){
            pathId =  compound.getString("path_id");
            majorRealm = compound.getInt("major_realm");
            minorRealm = compound.getInt("minor_realm");
            pathProgress = compound.getDouble("progress");
        }
    }


    private final HashMap<String,PathData> pathDataHashMap = new HashMap<>();

    public PathData getPathData(String pathId){
        if(pathDataHashMap.containsKey(pathId)) pathDataHashMap.get(pathId);
        PathData data = new PathData(pathId,0,0,0);
        pathDataHashMap.put(pathId,data);
        return data;
    }

    public CompoundTag writePathNBTData(){
        CompoundTag tag = new CompoundTag();
        for(PathData dataEntry : pathDataHashMap.values()){
            tag.put(dataEntry.pathId,dataEntry.writePathNBTData());
        }
        return tag;
    }
    public void loadPathNBTData(CompoundTag compound, HolderLookup.Provider provider){

    }







    public void loadNBTData(CompoundTag tag, HolderLookup.Provider provider){

    }
    public void saveNBTData(CompoundTag tag,HolderLookup.Provider provider){

    }
}
