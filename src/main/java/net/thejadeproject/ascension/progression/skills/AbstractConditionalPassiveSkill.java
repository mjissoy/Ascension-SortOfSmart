package net.thejadeproject.ascension.progression.skills;

import net.minecraft.network.chat.Component;

public abstract class AbstractConditionalPassiveSkill extends AbstractPassiveSkill{

    public AbstractConditionalPassiveSkill(Component title){
        super(title);

    }
    @Override
    public Component getSkillTitle() {
        return title;
    }
    abstract double getQiCost();
}
