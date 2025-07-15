package net.thejadeproject.ascension.skills;

public abstract class AbstractActiveSkill implements ISkill{
    public String path;
    public double qiCost;


    public double getQiCost(){
        return qiCost;
    };

    abstract CastType castType();


    abstract void cast();

}
