package net.thejadeproject.ascension.progression.techniques.path_techniques.intent;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.realm_change_handlers.IRealmChangeHandler;
import net.thejadeproject.ascension.progression.breakthrough.handlers.StabilityCheckBreakthroughHandler;
import net.thejadeproject.ascension.progression.skills.skill_lists.IAcquirableSkill;
import net.thejadeproject.ascension.progression.skills.skill_lists.SingleSkillAcquirableData;
import net.thejadeproject.ascension.progression.skills.skill_lists.SkillList;
import net.thejadeproject.ascension.progression.techniques.path_techniques.AbstractTechnique;
import net.thejadeproject.ascension.progression.techniques.stability_handlers.StabilityHandler;

import java.util.ArrayList;
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
    public AbstractTechnique setSkillList(List<IAcquirableSkill> skillList) {
        ArrayList<IAcquirableSkill> finalList = new ArrayList<>();

        finalList.add(new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "spiritual_sense"), 2, 5, true, false));
        finalList.add(new SingleSkillAcquirableData(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "flight_passive_skill"), 4, 0, true, false));

        finalList.addAll(skillList);
        return super.setSkillList(finalList);
    }

    @Override
    public Set<String> getCultivationAttributes() {
        return new HashSet<>(){{
            add(intent);
        }};
    }


}
