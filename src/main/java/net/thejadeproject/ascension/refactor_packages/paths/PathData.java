package net.thejadeproject.ascension.refactor_packages.paths;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.breakthroughs.IBreakthroughInstance;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechniqueData;
import org.checkerframework.checker.units.qual.C;
import oshi.util.tuples.Pair;

import java.util.*;

/*
    holds the instanced data of a path
    like cultivation progress and technique used for that progress
    *DOES NOT STORE CURRENT TECHNIQUE*
 */
public class PathData {
    private ResourceLocation path;

    private int majorRealm;
    private int minorRealm;
    private double currentRealmProgress;
    private int currentRealmStability;
    private boolean cultivating;
    private ResourceLocation lastUsedTechnique; //the current technique

    public PathData(ResourceLocation path){
        this.path = path;
    }


    /*
        stores the stability of each realm, the reason im not just storing the current stability
        is that this makes it easier to handle balance changes
        stores the stability of the realm im breaking into
        e.g if im 0,9-1,0 with 100 stability then it is stored under 1
    */
    private final HashMap<Integer,Integer> realmStability = new HashMap<>();

    private boolean breakingThrough;
    //only for the current breakthrough
    private IBreakthroughInstance breakthroughData;

    /*
        stores a history of what techniques where used to reach each major realm
        useful for compatibility and balance change stuff
        does not store the current major realm and current technique
     */
    private final HashMap<Integer, ResourceLocation> techniqueHistory = new HashMap<>();
    private final HashMap<ResourceLocation, ITechniqueData> techniqueData = new HashMap<>();
    public Set<Integer> getTechniqueRealms(ResourceLocation technique){
        HashSet<Integer> realms = new HashSet<>();
        techniqueHistory.forEach((key,val)->{
            if(val.equals(technique)) realms.add(key);
        });
        return realms;
    }
    public Set<Pair<Integer,Integer>> getTechniqueBreakthroughs(ResourceLocation technique){
        HashSet<Pair<Integer,Integer>> realms = new HashSet<>();
        techniqueHistory.forEach((key,val)->{
            if(val.equals(technique) && realmStability.containsKey(key)) {
                realms.add(new Pair<>(key,realmStability.get(key)));
            };
        });
        return realms;
    }

    public int getMajorRealm(){return majorRealm;}
    public int getMinorRealm(){return minorRealm;}
    public double getCurrentRealmProgress(){return currentRealmProgress;}
    public int getCurrentRealmStability(){return currentRealmStability;}
    public ResourceLocation getLastUsedTechnique(){return lastUsedTechnique;}
    public ResourceLocation getPath(){return path;}

    public void onFormRemoved(IEntityData heldEntity, IEntityFormData removedFormData){
        for(ResourceLocation technique : techniqueData.keySet()){
            AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(technique).onFormRemoved(heldEntity,removedFormData,this);
        }
    };
    public void onFormAdded(IEntityData heldEntity, IEntityFormData addedFormData){
        for(ResourceLocation technique : techniqueData.keySet()){
            AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(technique).onFormAdded(heldEntity,addedFormData,this);
        }
    };


    public CompoundTag write(){
        CompoundTag tag = new CompoundTag();

        return tag;
    }




}
