package net.thejadeproject.ascension.guis.easygui.elements.skill_menu;

import net.lucent.easygui.elements.other.Image;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;

public class SelectedSkillInfoPanel extends Image {
    public SelectedSkillInfoPanel(IEasyGuiScreen easyGuiScreen, int x, int y){
        super(easyGuiScreen,new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(
                AscensionCraft.MOD_ID,
                "textures/gui/screen/skill_select.png"
        ),320,240,225,0,301,137),x,y);
        this.setSticky(true);
    }
}
