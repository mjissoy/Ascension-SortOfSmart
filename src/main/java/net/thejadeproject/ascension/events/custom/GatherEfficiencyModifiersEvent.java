package net.thejadeproject.ascension.events.custom;


import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.thejadeproject.ascension.progression.dao.DaoInteractionType;
import net.thejadeproject.ascension.progression.dao.IDao;
import net.thejadeproject.ascension.registries.AscensionRegistries;

import java.util.*;

public class GatherEfficiencyModifiersEvent extends Event {

    public  final  Player player;
    private final Set<String> ascensionAttributeID;
    public final String pathID;
    private final HashMap<String,Double> daoMultipliers = new HashMap<>();

    private  Double pathMultipliers = 0.0;

    private final HashMap<String,HashMap<String, Pair<Double,Double>>> generativeValues = new HashMap<>();
    private final HashMap<String,HashMap<String, Pair<Double,Double>>> destructiveValues = new HashMap<>();


    public GatherEfficiencyModifiersEvent(Player player,String pathID,Set<String> ascensionAttributeID){
        this.ascensionAttributeID =  ascensionAttributeID;
        this.player = player;
        this.pathID = pathID;
        for(String key: ascensionAttributeID){
            daoMultipliers.put(key,0.01);//0.01 is the base dao bonus
        }
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
            HashMap<String, Pair<Double,Double>> attributeDestructiveValues = destructiveValues.get(attribute);
            for(String destructiveDao : attributeDestructiveValues.keySet()){
                total += (attributeDestructiveValues.get(destructiveDao).getFirst()/daoMultipliers.get(attribute))*attributeDestructiveValues.get(destructiveDao).getSecond();
            }
        }
        return total;
    }
    public double getTotalEfficiencyMultiplier(){
        return (getTotalDaoEfficiencyMultiplier()*(1+getTotalGenerativeMultiplier()))/(1+getTotalDestructiveMultiplier());
    }
    public void addPathMultiplier(Double mul){
        pathMultipliers += mul;
    }
    public Double getTotalPathEfficiencyMultiplier(){
        return pathMultipliers;
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

    public void tryAddDao(String daoId,Double mul){
        if(ascensionAttributeID.contains(daoId)){
            addDaoMultiplier(daoId,mul);
            return;
        }

        for(String attribute : ascensionAttributeID){

            IDao dao = AscensionRegistries.Dao.getDaoFromKey(attribute);

            Set<DaoInteractionType> interactionTypes = dao.getInteractionTypesOfDao(daoId);
            System.out.println("adding dao with interaction types : "+ interactionTypes);
            System.out.println("dao :"+daoId);
            if(interactionTypes.contains(DaoInteractionType.Destructive)){
                addDestructiveMultiplier(attribute,daoId,mul,dao.getDestructiveValue(daoId));
            }
            if(interactionTypes.contains(DaoInteractionType.Generative)){
                addGenerativeMultiplier(attribute,daoId,mul,dao.getGenerativeValue(daoId));
            }
            if(interactionTypes.contains(DaoInteractionType.Related)){
                addDaoMultiplier(attribute,mul*dao.getRelatedValue(daoId));
            }
        }
    }
}
