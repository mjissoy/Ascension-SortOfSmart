package net.thejadeproject.ascension.progression.skills;

import net.minecraft.nbt.CompoundTag;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;

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

    @Override
    public ISkillData getSkillData(CompoundTag tag) {
        return null;
    }

    @Override
    public ISkillData getSkillData() {
        return null;
    }
}
