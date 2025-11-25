package net.thejadeproject.ascension.progression.techniques.path_techniques.body;

import net.thejadeproject.ascension.progression.breakthrough.handlers.StabilityCheckBreakthroughHandler;
import net.thejadeproject.ascension.progression.techniques.path_techniques.AbstractTechnique;
import net.thejadeproject.ascension.progression.techniques.stability_handlers.StabilityHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SingleAttributeTechnique extends AbstractTechnique {

        public String attribute;

    public SingleAttributeTechnique(String title, double baseRate, String attribute, StabilityHandler stabilityHandler) {
            super(title, baseRate,"ascension:body",stabilityHandler,new StabilityCheckBreakthroughHandler());
            this.attribute = attribute;
    }



    @Override
    public Set<String> getCultivationAttributes() {
        return new HashSet<>(){{
            add(attribute);
        }};
    }


}
