package net.thejadeproject.ascension.events.custom;


import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

import java.util.List;

public class GatherEfficiencyModifiersEvent extends Event {
    public  final  Player player;
    public final String ascensionAttributeID;
    public final String pathID;
    private List<Double> multipliers;
    public GatherEfficiencyModifiersEvent(Player player,String pathID,String ascensionAttributeID){
        this.ascensionAttributeID =  ascensionAttributeID;
        this.player = player;
        this.pathID = pathID;
    }
    public void addMultiplier(Double mul){
        multipliers.add(mul);
    }
    public Double getTotalEfficiencyMultiplier(){
        Double total = 0.0;
        for(Double v : multipliers){
            total += v;
        }
        return total;
    }
}
