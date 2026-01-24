package net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view;

import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.guis.easygui.elements.EmptyButton;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.registries.AscensionRegistries;

public class ActiveSkillSlot extends EmptyButton {

    ResourceLocation currentSkill;
    ISkill skill;
    public boolean isHovered;

    public ActiveSkillSlot(IEasyGuiScreen screen, int x, int y, int width, int height) {
        super(screen, x, y, width, height);
    }
    public void setCurrentSkill(ResourceLocation skill){
        currentSkill = skill;
        this.skill = skill == null ? null : AscensionRegistries.Skills.SKILL_REGISTRY.get(skill);

    }
    public ResourceLocation getCurrentSkill(){
        return currentSkill;
    }
    public ISkill getSkill(){
        return skill;
    }

    @Override
    public void onMouseOver(boolean state) {
        super.onMouseOver(state);
        isHovered = state;
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.5f,0.5f,1);
        if(skill != null){
            skill.skillIcon().renderTexture(guiGraphics);
        }
        guiGraphics.pose().popPose();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(-1,-1,0);
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.pose().popPose();
    }
}
