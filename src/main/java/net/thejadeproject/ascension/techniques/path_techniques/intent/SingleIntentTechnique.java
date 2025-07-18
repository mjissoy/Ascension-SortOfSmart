package net.thejadeproject.ascension.techniques.path_techniques.intent;

import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.skills.skill_lists.SkillList;
import net.thejadeproject.ascension.techniques.path_techniques.AbstractTechnique;

import java.util.List;

public class SingleIntentTechnique extends AbstractTechnique {

    public String intent;

    public SingleIntentTechnique(String title, double baseRate,String intent) {
        super(title, baseRate,"ascension:intent");
        this.intent = intent;
    }

    @Override
    public List<MutableComponent> getDescription() {
        return List.of();
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
