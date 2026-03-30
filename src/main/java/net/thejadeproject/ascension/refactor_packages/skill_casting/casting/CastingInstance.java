package net.thejadeproject.ascension.refactor_packages.skill_casting.casting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastableSkill;

public class CastingInstance {

    private ResourceLocation skillKey;
    private ICastData castData;
    private int ticksElapsed; //how many ticks it has been since cast was started

    public CastingInstance(){

    }

    public ICastableSkill getCastableSkill(){return null;}//TODO

    // returns null if no cast was cancelled and a CastEndData instance otherwise
    public CastEndData startCast(Entity entity,ResourceLocation skillKey){
        CastEndData previousSkill = endCast(entity,CastEndReason.CANCELLED);
        this.skillKey = skillKey;
        this.castData = getCastableSkill().freshCastData();

       return previousSkill;
    }
    public CastEndData endCast(Entity entity,CastEndReason reason){
        CastEndData castEndData = skillKey == null ? null :  new CastEndData(skillKey,reason,castData,ticksElapsed);
        if(skillKey != null){
            getCastableSkill().finalCast(castEndData,entity,castData);
        }
        skillKey = null;
        castData = null;
        ticksElapsed = 0;
        return castEndData;
    }

    //first make sure we have a skill, try cast it and if the cast is ended call endCast
    public CastEndData castTick(Entity entity){
        ticksElapsed++;
        if(getCastableSkill() != null && !getCastableSkill().continueCasting(ticksElapsed,entity,castData))return endCast(entity,CastEndReason.ENDED);
        return null;
    }


    public boolean isCasting(){
        return skillKey != null;
    }
}
