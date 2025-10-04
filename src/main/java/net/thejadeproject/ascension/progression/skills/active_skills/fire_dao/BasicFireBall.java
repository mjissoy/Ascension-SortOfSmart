package net.thejadeproject.ascension.progression.skills.active_skills.fire_dao;

import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.data.CastType;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;

public class BasicFireBall extends AbstractActiveSkill {
    public BasicFireBall(String title) {
        super(title);
    }

    @Override
    public boolean isPrimarySkill() {
        return true;
    }

    @Override
    public CastType castType() {
        return CastType.LONG;
    }

    @Override
    public ISkillData getSkillData() {
        return null;
    }

    @Override
    public void cast(int ticksElapsed) {
        //TODO create fireball
    }

    @Override
    public void onPreCast() {
    }

    @Override
    public boolean continueCasting(int castingTicksElapsed, Level level) {
        if(castingTicksElapsed >= 20) return false;
        return true;
    }
}
