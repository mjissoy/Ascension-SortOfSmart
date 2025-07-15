package net.thejadeproject.ascension.skills.skill_lists;

import net.thejadeproject.ascension.skills.ISkill;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class SkillList {
    public List<AcquirableSkillData> skills = new ArrayList<>();


    public SkillList(List<AcquirableSkillData> skills){
        this.skills = skills;

    }
    public List<AcquirableSkillData> getSkillList(){
        return List.copyOf(skills);
    }

    public List<Pair<String,Boolean>> getSkillsOfPathAndRealm(String path, int majorRealm, int minorRealm){
        List<Pair<String,Boolean>> skillList = new ArrayList<>();
        for(AcquirableSkillData acquirableSkillData: skills){
            if(acquirableSkillData.path().equals(path) && acquirableSkillData.majorRealm() == majorRealm && acquirableSkillData.minorRealm() == minorRealm) {
                skillList.add(new Pair<>(acquirableSkillData.skill(),acquirableSkillData.fixed()));
            }
        }
        return skillList;
    }
}
