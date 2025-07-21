package net.thejadeproject.ascension.progression.skills;

public abstract class AbstractPassiveSkill implements ISkill{
    public String path;
    public String title;
    public AbstractPassiveSkill(String title){
        this.title = title;
    }
    @Override
    public String getSkillTitle() {
        return title;
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

    @Override
    public String getSkillPath() {
        return path;
    }
}
