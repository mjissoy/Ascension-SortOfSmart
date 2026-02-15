package net.thejadeproject.ascension.refactor_packages.paths;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.breakthroughs.IBreakthroughInstance;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechniqueData;

import java.util.HashMap;

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
     */
    private final HashMap<Integer,Integer> realmStability = new HashMap<>();

    private boolean breakingThrough;
    //only for the current breakthrough
    private IBreakthroughInstance breakthroughData;

    /*
        stores a history of what techniques where used to reach each major realm
        useful for compatibility and balance change stuff
     */
    private final HashMap<Integer, ResourceLocation> techniqueHistory = new HashMap<>();
    private final HashMap<ResourceLocation, ITechniqueData> techniqueData = new HashMap<>();

}
