package net.thejadeproject.ascension.progression.techniques.path_techniques.body;

import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.progression.techniques.path_techniques.AbstractTechnique;

import java.util.List;

public class SingleAttributeTechnique extends AbstractTechnique {

        public String attribute;

    public SingleAttributeTechnique(String title, double baseRate,String attribute) {
            super(title, baseRate,"ascension:body");
            this.attribute = attribute;
    }



    @Override
    public List<String> getCultivationAttributes() {
        return List.of(attribute);
    }

    @Override
    public void tryCultivate(Player player) {
        if(player.level().isClientSide()) return;
        System.out.println("Trying to cultivate body");
        CultivationSystem.cultivate(player,getPath(),baseRate,getCultivationAttributes());
    }
}
