package net.thejadeproject.ascension.progression.skills.skill_lists;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.events.custom.skills.PlayerSkillRemoveEvent;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class SkillList {
    public List<IAcquirableSkill> skills = new ArrayList<>();

    public SkillList(List<IAcquirableSkill> skills){
        this.skills = skills;

    }

    public void tryRemoveSkill(PlayerSkillRemoveEvent event, ResourceLocation path){
        for(IAcquirableSkill skill : skills){
            if(!skill.tryRemoveSkill(event,path)) return;
        }
    }

    public List<IAcquirableSkill> getSkillList(){
        return skills;
    }

    public void onRealmChange(RealmChangeEvent event){
        System.out.println("looping through skills");
        for(IAcquirableSkill skill : skills){
            System.out.println("trying skill");
            skill.onRealmChange(event);
        }
    }
}
