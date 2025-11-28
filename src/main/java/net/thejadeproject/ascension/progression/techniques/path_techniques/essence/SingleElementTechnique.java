package net.thejadeproject.ascension.progression.techniques.path_techniques.essence;

import net.thejadeproject.ascension.cultivation.player.realm_change_handlers.IRealmChangeHandler;
import net.thejadeproject.ascension.progression.breakthrough.handlers.StabilityCheckBreakthroughHandler;
import net.thejadeproject.ascension.progression.techniques.path_techniques.AbstractTechnique;
import net.thejadeproject.ascension.progression.techniques.stability_handlers.StabilityHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SingleElementTechnique extends AbstractTechnique {


    public String element;


    public SingleElementTechnique(String title, String element, Double baseRate, StabilityHandler stabilityHandler, IRealmChangeHandler realmChangeHandler){
        super(title,baseRate,"ascension:essence",stabilityHandler,new StabilityCheckBreakthroughHandler(),realmChangeHandler);
        this.element = element;

    }




    @Override
    public Set<String> getCultivationAttributes() {
        return new HashSet<>(){{
            add(element);
        }};
    }




}
