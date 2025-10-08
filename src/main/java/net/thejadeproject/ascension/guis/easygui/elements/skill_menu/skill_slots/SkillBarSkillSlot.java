package net.thejadeproject.ascension.guis.easygui.elements.skill_menu.skill_slots;

import net.lucent.easygui.elements.controls.buttons.TextureButton;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu.ActiveSkillBar;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu.SelectedSkillInfoPanel;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.registries.AscensionRegistries;

public class SkillBarSkillSlot extends TextureButton {
    public ResourceLocation skillId;
    public ITextureData textureData;
    public SkillBarSkillSlot(IEasyGuiScreen easyGuiScreen, int x, int y, ResourceLocation skillId){
        super(easyGuiScreen,x,y,
                new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_slots.png"),112,96,0,0,47,49),
                new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_slots.png"),112,96,48,0,96,50),
                new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_slots.png"),112,96,48,0,96,50),
                new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_slots.png"),112,96,48,0,96,50));
        setSkillId(skillId);
    }
    public void setSkillId(ResourceLocation skillId){
        if(skillId == null){
            textureData = null;
            this.skillId = null;
            return;
        }
        ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(skillId);
        textureData = skill.skillIcon();
    }
    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        if(textureData != null) textureData.renderTexture(guiGraphics,10,10);
        if(isFocused() || isPressed() || isHovered()) hoveredTexture.renderTexture(guiGraphics);
        else defaultTexture.renderTexture(guiGraphics,1,1);

    }

    @Override
    public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
        super.onClick(mouseX, mouseY, button, clicked);
        if(!clicked) return;
        ((ActiveSkillBar) getParent().getParent().getParent()).selectSkillBarSkill(this);
    }

}
