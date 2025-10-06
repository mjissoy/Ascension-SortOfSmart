package net.thejadeproject.ascension.guis.easygui.elements.skill_menu.skill_slots;

import net.lucent.easygui.elements.controls.buttons.TextureButton;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;

public class SkillBarSkillSlot extends TextureButton {
    public final ResourceLocation skillId;
    public SkillBarSkillSlot(IEasyGuiScreen easyGuiScreen, int x, int y, ResourceLocation skillId){
        super(easyGuiScreen,x,y,
                new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_slots.png"),112,96,0,0,47,49),
                new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_slots.png"),112,96,48,0,96,49),
                new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_slots.png"),112,96,48,0,96,49),
                new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_slots.png"),112,96,48,0,96,49));
        this.skillId = skillId;
    }
}
