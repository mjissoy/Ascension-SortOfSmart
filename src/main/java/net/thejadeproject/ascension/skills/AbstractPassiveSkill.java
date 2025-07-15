package net.thejadeproject.ascension.skills;

import net.minecraft.world.entity.player.Player;

public abstract class AbstractPassiveSkill implements ISkill{
    public String path;


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
