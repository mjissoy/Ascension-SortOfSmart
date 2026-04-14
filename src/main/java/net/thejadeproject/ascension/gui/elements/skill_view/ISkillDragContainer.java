package net.thejadeproject.ascension.gui.elements.skill_view;

import net.minecraft.resources.ResourceLocation;

public interface ISkillDragContainer {
    ResourceLocation getHeldSkill();
    void setHeldSkill(ResourceLocation skill);
}