package net.thejadeproject.ascension.events.custom;


import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GatherEfficiencyModifiersEvent extends Event {
    public  final  Player player;
    private final Set<String> ascensionAttributeID;
    public final String pathID;
    private final List<Double> daoMultipliers = new ArrayList<>();
    private final List<Double> pathMultipliers = new ArrayList<>();

    public GatherEfficiencyModifiersEvent(Player player,String pathID,Set<String> ascensionAttributeID){
        this.ascensionAttributeID =  ascensionAttributeID;
        this.player = player;
        this.pathID = pathID;
    }
    public void addDaoMultiplier(Double mul){
        daoMultipliers.add(mul);
    }
    public Double getTotalDaoEfficiencyMultiplier(){
        Double total = 1.0;
        for(Double v : daoMultipliers){
            total *= v;
        }
        return total;
    }
    public void addPathMultiplier(Double mul){
        pathMultipliers.add(mul);
    }
    public Double getTotalPathEfficiencyMultiplier(){
        Double total = 1.0;
        for(Double v : pathMultipliers){
            total *= v;
        }
        return total;
    }
    public List<String> ascensionAttributeID(){
        return List.copyOf(ascensionAttributeID);
    }
}
