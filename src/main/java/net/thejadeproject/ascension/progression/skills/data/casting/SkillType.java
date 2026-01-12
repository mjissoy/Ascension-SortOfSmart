package net.thejadeproject.ascension.progression.skills.data.casting;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.registries.AscensionRegistries;

public enum SkillType {
    Passive,
    Active;


    public static SkillType getSkillType(ResourceLocation skillId){
        if(AscensionRegistries.Skills.SKILL_REGISTRY.get(skillId) instanceof AbstractActiveSkill){
            return Active;
        }
        return Passive;
    }
    public static SkillType getSkillType(String skillId){
        return getSkillType(ResourceLocation.bySeparator(skillId,':'));
    }
}
