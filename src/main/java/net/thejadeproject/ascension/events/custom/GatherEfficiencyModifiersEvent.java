package net.thejadeproject.ascension.events.custom;


import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

import java.util.*;

public class GatherEfficiencyModifiersEvent extends Event {
    public  final  Player player;
    private final Set<String> ascensionAttributeID;
    public final String pathID;
    private final HashMap<String,Double> daoMultipliers = new HashMap<>();


    private final HashMap<String,Double> pathMultipliers = new HashMap<>();

    private final HashMap<String,HashMap<String, Pair<Double,Double>>> generativeValues = new HashMap<>();
    private final HashMap<String,HashMap<String, Pair<Double,Double>>> destructiveValues = new HashMap<>();


    public GatherEfficiencyModifiersEvent(Player player,String pathID,Set<String> ascensionAttributeID){
        this.ascensionAttributeID =  ascensionAttributeID;
        this.player = player;
        this.pathID = pathID;
    }
    public void addDaoMultiplier(String attribute,Double mul){
        daoMultipliers.put(attribute,mul);
    }
    public Double getTotalDaoEfficiencyMultiplier(){
        Double total = 0.0;
        for(Double multiplier : daoMultipliers.values()){
            total += multiplier;
        }
        return total;
    }
    public double getTotalGenerativeMultiplier(){
        double total = 0.0;
        for(String attribute : generativeValues.keySet()){
            HashMap<String, Pair<Double,Double>> attributeGenerativeValues = generativeValues.get(attribute);
            for(String generativeDao : attributeGenerativeValues.keySet()){
                total += (attributeGenerativeValues.get(generativeDao).getFirst()/daoMultipliers.get(attribute))*attributeGenerativeValues.get(generativeDao).getSecond();
            }
        }
        return total;
    }
    public double getTotalDestructiveMultiplier(){
        double total = 0.0;
        for(String attribute : destructiveValues.keySet()){
            HashMap<String, Pair<Double,Double>> attributeGenerativeValues = destructiveValues.get(attribute);
            for(String destructiveDao : attributeGenerativeValues.keySet()){
                total += (attributeGenerativeValues.get(destructiveDao).getFirst()/daoMultipliers.get(attribute))*attributeGenerativeValues.get(destructiveDao).getSecond();
            }
        }
        return total;
    }
    public void addPathMultiplier(String pathID,Double mul){
        pathMultipliers.put(pathID,mul);
    }
    public Double getTotalPathEfficiencyMultiplier(){
        Double total = 0.0;
        for(Double multiplier : pathMultipliers.values()){
            total += multiplier;
        }
        return total;
    }
    public List<String> ascensionAttributeID(){
        return List.copyOf(ascensionAttributeID);
    }
    public void addGenerativeMultiplier(String attribute,String interactionDao,double interactionDaoBonus,double generativeBonus){
        if(!generativeValues.containsKey(attribute)) generativeValues.put(attribute,new HashMap<>());
        HashMap<String,Pair<Double,Double>> interactionDaoMap = generativeValues.get(attribute);
        double finalInteractionDaoBonus = interactionDaoBonus;
        if(interactionDaoMap.containsKey(interactionDao)) finalInteractionDaoBonus+= interactionDaoMap.get(interactionDao).getFirst();
        interactionDaoMap.put(interactionDao,new Pair<>(finalInteractionDaoBonus,generativeBonus));
    }
    public void addDestructiveMultiplier(String attribute,String interactionDao,double interactionDaoBonus,double destructiveBonus){
        if(!destructiveValues.containsKey(attribute)) destructiveValues.put(attribute,new HashMap<>());
        HashMap<String,Pair<Double,Double>> interactionDaoMap = destructiveValues.get(attribute);
        double finalInteractionDaoBonus = interactionDaoBonus;
        if(interactionDaoMap.containsKey(interactionDao)) finalInteractionDaoBonus+= interactionDaoMap.get(interactionDao).getFirst();
        interactionDaoMap.put(interactionDao,new Pair<>(finalInteractionDaoBonus,destructiveBonus));
    }
}
