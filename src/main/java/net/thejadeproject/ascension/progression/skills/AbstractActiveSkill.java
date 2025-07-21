package net.thejadeproject.ascension.progression.skills;

import net.thejadeproject.ascension.progression.skills.data.CastType;

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


    abstract void cast();

}
