package net.thejadeproject.ascension.physiques;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//if you want some sort of extra progress data separate from standard use direct nbt write
//make sure to remove it in onRemoveSpiritRoot


public class GenericPhysique implements IPhysique{

    public Map<String,Double> pathBonuses;
    public Map<String,Double> otherBonuses;
    public final String title;
    public GenericPhysique(String title, Map<String,Double> pathBonuses, Map<String,Double> otherBonuses){
        this.pathBonuses = pathBonuses;
        this.otherBonuses = otherBonuses;
        this.title = title;
    }

    @Override
    public List<String> getDisplayPathBonuses() {
        List<String> bonuses = new ArrayList<>();
        for(Map.Entry<String,Double> entry : pathBonuses.entrySet()){
            bonuses.add(entry.getKey() + ": "+entry.getValue().toString());
        }
        return bonuses;
    }

    @Override
    public List<String> getDisplayOtherBonuses() {
        List<String> bonuses = new ArrayList<>();
        for(Map.Entry<String,Double> entry : otherBonuses.entrySet()){
            bonuses.add(entry.getKey() + ": "+entry.getValue().toString());
        }
        return bonuses;
    }

    @Override
    public void onGatherEfficiencyModifiers(GatherEfficiencyModifiersEvent event) {

        if(event.pathID != null && pathBonuses.containsKey(event.pathID)) event.addMultiplier(pathBonuses.get(event.pathID));
        if(event.ascensionAttributeID != null && otherBonuses.containsKey(event.ascensionAttributeID)) event.addMultiplier(otherBonuses.get(event.ascensionAttributeID));

    }

    @Override
    public ResourceLocation getPhysiqueImage() {
        return null;
    }

    @Override
    public String getDisplayTitle() {
        return title;
    }
}
