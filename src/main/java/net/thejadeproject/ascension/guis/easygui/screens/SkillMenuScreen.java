package net.thejadeproject.ascension.guis.easygui.screens;

import net.lucent.easygui.elements.containers.View;
import net.lucent.easygui.elements.containers.panels.Panel;
import net.lucent.easygui.elements.other.Image;
import net.lucent.easygui.screens.EasyGuiScreen;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;

import net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.MainSkillContainer;

//if a skill slot is selected display skill in info panel. with button to unslot. if skill slot is selected and you select another skill
//from skill list display skill info and have a slot button (also do not show button for passive skills)
public class SkillMenuScreen extends EasyGuiScreen {
    public SkillMenuScreen(Component title) {
        super(title);
        View view = new View(this);
        addView(view);
        view.setUseMinecraftScale(true);
        view.addChild(new MainSkillContainer(this));
    }
}
