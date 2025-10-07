package net.thejadeproject.ascension.guis.easygui.elements.skill_menu.skill_slots;

import net.lucent.easygui.elements.controls.buttons.TextureButton;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.util.textures.TextureData;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.registries.AscensionRegistries;

//10 px gap. 1 px from green on left
public class BasicSkillSlot extends TextureButton {
    public final ResourceLocation skillId;
    public ITextureData textureData;
    public BasicSkillSlot(IEasyGuiScreen easyGuiScreen,int x,int y,ResourceLocation skillId){
        super(easyGuiScreen,x,y,
                new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_slots.png"),112,96,2,51,42,91),
                new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_slots.png"),112,96,1,50,43,92),
                new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_slots.png"),112,96,1,50,43,92),
                new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_slots.png"),112,96,1,50,43,92));
        this.skillId = skillId;
        ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(skillId);
        textureData = skill.skillIcon();
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(textureData != null) textureData.renderTexture(guiGraphics,5,5);
        if(isFocused() || isPressed() || isHovered()) hoveredTexture.renderTexture(guiGraphics);
        else defaultTexture.renderTexture(guiGraphics,1,1);

    }
}
