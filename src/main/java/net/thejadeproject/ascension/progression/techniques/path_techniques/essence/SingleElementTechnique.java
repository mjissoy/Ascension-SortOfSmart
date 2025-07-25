package net.thejadeproject.ascension.progression.techniques.path_techniques.essence;

import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.progression.techniques.path_techniques.AbstractTechnique;

import java.util.List;

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
