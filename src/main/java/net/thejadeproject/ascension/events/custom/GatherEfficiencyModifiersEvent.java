package net.thejadeproject.ascension.events.custom;


import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.List;

public class GatherEfficiencyModifiersEvent extends Event {
    public  final  Player player;
    private final List<String> ascensionAttributeID;
    public final String pathID;
    private final List<Double> multipliers = new ArrayList<>();
    public GatherEfficiencyModifiersEvent(Player player,String pathID,List<String> ascensionAttributeID){
        this.ascensionAttributeID =  ascensionAttributeID;
        this.player = player;
        this.pathID = pathID;
    }
    public void addMultiplier(Double mul){
        multipliers.add(mul);
    }
    public Double getTotalEfficiencyMultiplier(){
        Double total = 1.0;
        for(Double v : multipliers){
            total *= v;
        }
        return total;
    }
    public List<String> ascensionAttributeID(){
        return List.copyOf(ascensionAttributeID);
    }
}
