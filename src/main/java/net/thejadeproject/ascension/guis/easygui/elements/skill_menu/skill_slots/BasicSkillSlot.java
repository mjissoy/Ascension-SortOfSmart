package net.thejadeproject.ascension.guis.easygui.elements.skill_menu.skill_slots;

import net.lucent.easygui.elements.controls.buttons.TextureButton;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;

public class BasicSkillSlot extends TextureButton {
    public final ResourceLocation skillId;
    public BasicSkillSlot(IEasyGuiScreen easyGuiScreen,int x,int y,ResourceLocation skillId){
        super(easyGuiScreen,x,y,
                new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_slots.png"),112,96,2,51,41,90),
                new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_slots.png"),112,96,1,50,42,91),
                new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_slots.png"),112,96,1,50,42,91),
                new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_slots.png"),112,96,1,50,42,91));
        this.skillId = skillId;
    }
}
