package net.thejadeproject.ascension.progression.skills.skill_lists;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.skills.ISkill;

public record AcquirableSkillData(String path, int minorRealm, int majorRealm, String skill, boolean fixed) {


    public String asString(){
        ISkill skillData = AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(skill,':'));

        return skillData.getSkillTitle() + " : "+ CultivationSystem.getPathMajorRealmName(path,majorRealm) + "("+minorRealm+")";
    }

    public Component asComponent(){
        ISkill skillData = AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(skill,':'));

        return Component.empty()
                .append(skillData.getSkillTitle())
                .append(CultivationSystem.getPathMajorRealmName(path,majorRealm))
                .append("("+minorRealm+")");

    }
}
