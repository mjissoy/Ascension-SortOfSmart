package net.thejadeproject.ascension.techniques.path_techniques.essence;

import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.skills.skill_lists.SkillList;
import net.thejadeproject.ascension.techniques.path_techniques.AbstractTechnique;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SingleElementTechnique extends AbstractTechnique {

    public String title;
    public String element;
    public Double baseRate;
    public Map<String,Double> efficiencyBonuses = new HashMap<>();
    public SingleElementTechnique(String title,String element,Double baseRate){
        this.title = title;
        this.element = element;
        this.baseRate = baseRate;
    }
    public SingleElementTechnique setEfficiencyAttributes(Map<String,Double> efficiencyBonuses){
        this.efficiencyBonuses = efficiencyBonuses;
        return this;
    }


    @Override
    public void onGatherEfficiencyModifiers(GatherEfficiencyModifiersEvent event) {
        if(!Objects.equals(event.pathID, getPath())) return;
        for(String attribute:event.ascensionAttributeID()){
            if(efficiencyBonuses.containsKey(attribute)) event.addMultiplier(efficiencyBonuses.get(attribute));
        }
    }

    @Override
    public ITextureData getTechniqueImage() {
        return null;
    }

    @Override
    public String getPath() {
        return "ascension:essence";
    }

    @Override
    public SkillList getSkillList() {
        return null;
    }

    @Override
    public String getDisplayTitle() {
        return title;
    }

    @Override
    public List<MutableComponent> getDescription() {
        return List.of();
    }

    @Override
    public List<String> getCultivationAttributes() {
        return List.of(element);
    }

    @Override
    public List<String> getEfficiencyAttributes() {
        return efficiencyBonuses.keySet().stream().toList();
    }

    @Override
    public void tryCultivate(Player player) {
        if(player.level().isClientSide()) return;
        CultivationSystem.cultivate(player,"ascension:essence",baseRate,List.of(element));
    }

    @Override
    public Double getEfficiencyValue(String attribute) {
        return efficiencyBonuses.get(attribute);
    }
}
