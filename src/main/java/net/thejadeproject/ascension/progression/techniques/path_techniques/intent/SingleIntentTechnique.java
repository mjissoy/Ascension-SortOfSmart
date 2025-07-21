package net.thejadeproject.ascension.progression.techniques.path_techniques.intent;

import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.progression.techniques.path_techniques.AbstractTechnique;

import java.util.List;

public class SingleIntentTechnique extends AbstractTechnique {

    public String intent;

    public SingleIntentTechnique(String title, double baseRate,String intent) {
        super(title, baseRate,"ascension:intent");
        this.intent = intent;
    }



    @Override
    public List<String> getCultivationAttributes() {
        return List.of(intent);
    }

    @Override
    public void tryCultivate(Player player) {
        if(player.level().isClientSide()) return;
        System.out.println("Trying to cultivate intent");
        CultivationSystem.cultivate(player,getPath(),baseRate,getCultivationAttributes());
    }
}
