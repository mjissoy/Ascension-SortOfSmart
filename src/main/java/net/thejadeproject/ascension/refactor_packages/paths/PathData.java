package net.thejadeproject.ascension.refactor_packages.paths;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.breakthroughs.IBreakthroughInstance;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechniqueData;
import oshi.util.tuples.Pair;

import java.util.*;

/*
    holds the instanced data of a path
    like cultivation progress and technique used for that progress
    *DOES NOT STORE CURRENT TECHNIQUE*
 */
public class PathData {

    private int majorRealm;
    private int minorRealm;
    private double currentRealmProgress;
    private boolean cultivating;



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
    public void onEntityUntethered(IEntityData heldEntity,IEntityData tetheredEntity){
        //TODO
        /*
            go through technique history,
            if major realm is not equal to current use getMaxMinorRealm in the technique

            the "remove" methodology here is handled slightly differently since the cultivation data still exists
            and we are not trying to delete that so do not call remove() on the technique
            but instead removeFromTetheredEntity(), well technically old tethered
            essentially treat it like we are not removing the technique (since we are not) but instead removing the cultivation
            so some sort of removeProgressBonuses method?
         */
    }
    public void onEntityTethered(IEntityData heldEntity,IEntityData tetheredEntity){
        //TODO
        /*
            go through technique history,
            if major realm is not equal to current use getMaxMinorRealm in the technique
            then use correct apply method with current progress


           U IS NOT THE SAME AS LEVELING UP WITH THE TECHNIQUE
            SO DO NOT APPLY ANY SKILLS ONLY TO BONUSES

            SO I NEED TO MAKE SURE I HAVE A METHOD FOR WHEN PROGRESSING TO A NEW REALM WITH THAT TECHNIQUE

            AND ONE FOR WHEN IM APPLYING THE CULTIVATION BONUSES FOR A BODY THAT SHOULD ALREADY BE AT THAT REALM
         */
    }




}
