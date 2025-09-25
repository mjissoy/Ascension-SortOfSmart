package net.thejadeproject.ascension.progression.techniques.path_techniques.essence;

import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.progression.techniques.path_techniques.AbstractTechnique;
import net.thejadeproject.ascension.progression.techniques.stability_handlers.StabilityHandler;

import java.util.List;

public class SingleElementTechnique extends AbstractTechnique {


    public String element;


    public SingleElementTechnique(String title, String element, Double baseRate, StabilityHandler stabilityHandler){
        super(title,baseRate,"ascension:essence",stabilityHandler);
        this.element = element;

    }




    @Override
    public List<String> getCultivationAttributes() {
        return List.of(element);
    }




}
