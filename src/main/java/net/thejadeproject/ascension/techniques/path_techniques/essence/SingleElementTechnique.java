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


    public String element;


    public SingleElementTechnique(String title,String element,Double baseRate){
        super(title,baseRate,"ascension:essence");
        this.element = element;

    }




    @Override
    public List<String> getCultivationAttributes() {
        return List.of(element);
    }



    @Override
    public void tryCultivate(Player player) {
        if(player.level().isClientSide()) return;
        CultivationSystem.cultivate(player,"ascension:essence",baseRate,List.of(element));
    }


}
