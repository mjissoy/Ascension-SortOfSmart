package net.thejadeproject.ascension.progression.techniques.path_techniques.body;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.realm_change_handlers.IRealmChangeHandler;
import net.thejadeproject.ascension.progression.breakthrough.handlers.StabilityCheckBreakthroughHandler;
import net.thejadeproject.ascension.progression.skills.skill_lists.IAcquirableSkill;
import net.thejadeproject.ascension.progression.skills.skill_lists.SingleSkillAcquirableData;
import net.thejadeproject.ascension.progression.techniques.path_techniques.AbstractTechnique;
import net.thejadeproject.ascension.progression.techniques.stability_handlers.StabilityHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SingleAttributeTechnique extends AbstractTechnique {

        public String attribute;

    public SingleAttributeTechnique(String title, double baseRate, String attribute, StabilityHandler stabilityHandler, IRealmChangeHandler realmChangeHandler) {
            super(title, baseRate,"ascension:body",stabilityHandler,new StabilityCheckBreakthroughHandler(),realmChangeHandler);
            this.attribute = attribute;
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
            add(attribute);
        }};
    }


}
