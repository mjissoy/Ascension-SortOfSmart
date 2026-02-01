package net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.passive_skill_container;

import com.mojang.blaze3d.platform.InputConstants;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.interfaces.events.Clickable;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.guis.easygui.elements.EmptyButton;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.registries.AscensionRegistries;

public class PassiveSkillSlot extends EmptyButton  {
    ResourceLocation currentSkill;
    ISkill skill;
    public boolean isHovered;
    public ITextureData backgroundTexture =  new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_stuff/skill_menu.png"),
            320,256,
            0,236,92,256
    );
    public PassiveSkillSlot(IEasyGuiScreen screen, int x, int y, int width, int height) {
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
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        if(skill != null){
            backgroundTexture.renderTexture(guiGraphics);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(2,2,0);
            guiGraphics.pose().scale(0.5f,0.5f,1);

            if(skill.skillIcon() != null) skill.skillIcon().renderTexture(guiGraphics);
            guiGraphics.pose().popPose();
            guiGraphics.pose().pushPose();;
            guiGraphics.pose().translate(60,8,0);
            guiGraphics.pose().scale(0.5f,0.5f,1);
            Component text = skill.getSkillTitle();
            int width = Minecraft.getInstance().font.width(text);
            int height = Minecraft.getInstance().font.lineHeight;
            guiGraphics.drawString(Minecraft.getInstance().font,text,-width/2,-height/2,-1,false);
            guiGraphics.pose().popPose();
            super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
        }

    }

    @Override
    public void onMouseOver(boolean state) {
        super.onMouseOver(state);
        isHovered = state;
    }

}
