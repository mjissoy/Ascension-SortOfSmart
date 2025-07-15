package net.thejadeproject.ascension.skills;

public abstract class AbstractActiveSkill implements ISkill{
    public String path;
    public double qiCost;

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

    abstract CastType castType();


    abstract void cast();

}
