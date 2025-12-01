package net.thejadeproject.ascension.progression.skills.skill_lists;

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


    public List<Pair<String,Boolean>> getSkillsBetweenRealmsExcludingMin(int majorRealm1,int majorRealm2,int minorRealm1,int minorRealm2){
        if(majorRealm1 > majorRealm2) return List.of();
        if(majorRealm1 == majorRealm2 && minorRealm1 > minorRealm2) return List.of();
        List<AcquirableSkillData> skillDataList = getSkillList();
        List<Pair<String,Boolean>> validSkills = new ArrayList<>();
        for(AcquirableSkillData skillData: skillDataList){
            boolean inMajorRealmRange = skillData.majorRealm()>=majorRealm1 && skillData.majorRealm() <= majorRealm2;
            boolean inBetweenMajorRealms = inMajorRealmRange && skillData.majorRealm() != majorRealm1 && minorRealm2 != skillData.majorRealm();
            boolean inLowerMajorAndGreaterOrEqualToMinor = inMajorRealmRange && skillData.majorRealm() == majorRealm1 && skillData.minorRealm() >minorRealm1;
            boolean inHigherMajorAndLessOrEqualToMinor = inMajorRealmRange && skillData.majorRealm() == majorRealm2 && skillData.minorRealm() <= minorRealm2;


            if(inBetweenMajorRealms || inLowerMajorAndGreaterOrEqualToMinor || inHigherMajorAndLessOrEqualToMinor) {
                validSkills.add(new Pair<>(skillData.skill(),skillData.fixed()));
            }
        }
        return validSkills;
    }

    public List<Pair<String,Boolean>> getSkillsBetweenRealmsIncludingMin(int minMajorRealm,int maxMajorRealm,int minMinorRealm,int maxMinorRealm){
        if(minMajorRealm > maxMajorRealm) return List.of();
        if(minMajorRealm == maxMajorRealm && minMinorRealm > maxMinorRealm) return List.of();
        List<AcquirableSkillData> skillDataList = getSkillList();
        List<Pair<String,Boolean>> validSkills = new ArrayList<>();
        for(AcquirableSkillData skillData: skillDataList){
            boolean inMajorRealmRange = skillData.majorRealm()>=minMajorRealm && skillData.majorRealm() <= maxMajorRealm;
            boolean inBetweenMajorRealms = inMajorRealmRange && skillData.majorRealm() != minMajorRealm && maxMinorRealm != skillData.majorRealm();
            boolean inLowerMajorAndGreaterOrEqualToMinor = inMajorRealmRange && skillData.majorRealm() == minMajorRealm && skillData.minorRealm() >=minMinorRealm;
            boolean inHigherMajorAndLessOrEqualToMinor = inMajorRealmRange && skillData.majorRealm() == maxMajorRealm && skillData.minorRealm() <= maxMinorRealm;


            if(inBetweenMajorRealms || inLowerMajorAndGreaterOrEqualToMinor || inHigherMajorAndLessOrEqualToMinor) {
                validSkills.add(new Pair<>(skillData.skill(),skillData.fixed()));
            }
        }
        return validSkills;
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
