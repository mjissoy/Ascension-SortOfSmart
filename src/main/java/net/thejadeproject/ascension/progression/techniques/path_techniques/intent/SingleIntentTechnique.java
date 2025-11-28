package net.thejadeproject.ascension.progression.techniques.path_techniques.intent;

import net.thejadeproject.ascension.cultivation.player.realm_change_handlers.IRealmChangeHandler;
import net.thejadeproject.ascension.progression.breakthrough.handlers.StabilityCheckBreakthroughHandler;
import net.thejadeproject.ascension.progression.techniques.path_techniques.AbstractTechnique;
import net.thejadeproject.ascension.progression.techniques.stability_handlers.StabilityHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SingleIntentTechnique extends AbstractTechnique {

    public String intent;

    public SingleIntentTechnique(String title, double baseRate, String intent, StabilityHandler stabilityHandler, IRealmChangeHandler realmChangeHandler) {
        super(title, baseRate,"ascension:intent",stabilityHandler,new StabilityCheckBreakthroughHandler(),realmChangeHandler);
        this.intent = intent;
    }



    @Override
    public Set<String> getCultivationAttributes() {
        return new HashSet<>(){{
            add(intent);
        }};
    }


}
