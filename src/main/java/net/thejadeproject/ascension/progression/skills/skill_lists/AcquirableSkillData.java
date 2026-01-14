package net.thejadeproject.ascension.progression.skills.skill_lists;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.skills.ISkill;

//TODO ignore this since it WILL be changed later, or make it a Client side only thing
public record AcquirableSkillData(ResourceLocation skillId,boolean fixed,boolean permanent) {


    public Component asComponent(){
        ISkill skillData = AscensionRegistries.Skills.SKILL_REGISTRY.get(skillId);

        return skillData.getSkillTitle();

    }
}
