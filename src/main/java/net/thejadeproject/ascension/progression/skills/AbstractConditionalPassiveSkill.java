package net.thejadeproject.ascension.progression.skills;

public abstract class AbstractConditionalPassiveSkill extends AbstractPassiveSkill{
    public String title;
    public AbstractConditionalPassiveSkill(String title){
        super(title);

    }
    @Override
    public String getSkillTitle() {
        return title;
    }
    abstract double getQiCost();
}
