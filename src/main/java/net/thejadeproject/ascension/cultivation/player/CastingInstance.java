package net.thejadeproject.ascension.cultivation.player;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.progression.skills.data.CastType;
import net.thejadeproject.ascension.progression.skills.data.ICastData;
import net.thejadeproject.ascension.registries.AscensionRegistries;

//built like this so i can have multiple casting instances
public class CastingInstance {
    public CastType castType;
    public ICastData castData;// additional info the spell might need
    public int castTickElapsed = 0;
    public ResourceLocation skillId;


    public CastingInstance(ISkill skill){
        castType = skill.getCastType();
        skillId = AscensionRegistries.Skills.SKILL_REGISTRY.getKey(skill);
    }
}
