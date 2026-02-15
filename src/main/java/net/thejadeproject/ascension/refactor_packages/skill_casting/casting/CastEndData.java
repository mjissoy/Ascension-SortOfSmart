package net.thejadeproject.ascension.refactor_packages.skill_casting.casting;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastableSkill;

//will be used by skills to determine cooldown
public record CastEndData(ResourceLocation skillId, CastEndReason reason, ICastData castData,int ticksElapsed){
    public ICastableSkill getSkill(){
        return null;
    }
}
