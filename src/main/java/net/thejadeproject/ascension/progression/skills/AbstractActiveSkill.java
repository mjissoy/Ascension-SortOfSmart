package net.thejadeproject.ascension.progression.skills;

import net.minecraft.nbt.CompoundTag;
import net.thejadeproject.ascension.progression.skills.data.CastType;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;

public abstract class AbstractActiveSkill implements ISkill{
    public String path;
    public double qiCost;
    public String title;
    public AbstractActiveSkill(String title){
        this.title = title;
    }
    @Override
    public String getSkillTitle() {
        return title;
    }
    @Override
    public String getSkillPath() {
        return path;
    }

    @Override
    public boolean isFixedSkill() {
        //todo uses nbt
        return false;
    }
    @Override
    public void setFixedSkill(boolean fixedSkill) {
        //todo uses nbt data.
    }

    public double getQiCost(){
        return qiCost;
    };

    protected abstract CastType castType();
    @Override
    public ISkillData getSkillData(CompoundTag tag) {
        return null;
    }
    public boolean continueCasting(int castingTicksElapsed){
        return false;
    }

    abstract void cast();

}
