package net.thejadeproject.ascension.progression.techniques.path_techniques.body;

import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.progression.techniques.path_techniques.AbstractTechnique;
import net.thejadeproject.ascension.progression.techniques.stability_handlers.StabilityHandler;

import java.util.List;

public class SingleAttributeTechnique extends AbstractTechnique {

        public String attribute;

    public SingleAttributeTechnique(String title, double baseRate, String attribute, StabilityHandler stabilityHandler) {
            super(title, baseRate,"ascension:body",stabilityHandler);
            this.attribute = attribute;
    }



    @Override
    public List<String> getCultivationAttributes() {
        return List.of(attribute);
    }


}
