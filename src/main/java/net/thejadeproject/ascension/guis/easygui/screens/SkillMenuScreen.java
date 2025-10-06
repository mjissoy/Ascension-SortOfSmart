package net.thejadeproject.ascension.guis.easygui.screens;

import net.lucent.easygui.elements.containers.View;
import net.lucent.easygui.elements.containers.panels.Panel;
import net.lucent.easygui.elements.other.Image;
import net.lucent.easygui.screens.EasyGuiScreen;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu.ActiveSkillBar;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu.SelectedSkillInfoPanel;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu.SkillListPanel;

//if a skill slot is selected display skill in info panel. with button to unslot. if skill slot is selected and you select another skill
//from skill list display skill info and have a slot button (also do not show button for passive skills)
public class SkillMenuScreen extends EasyGuiScreen {
    public SkillMenuScreen(Component title) {
        super(title);
        View view = new View(this);
        addView(view);
        view.setUseMinecraftScale(false);
        view.setCustomScale(4);
        SkillListPanel skillList = new SkillListPanel(this,
                view.getScaledWidth()/2-112, view.getScaledHeight()/2-40);
        ActiveSkillBar activeSkillBar = new ActiveSkillBar(this,
                view.getScaledWidth()/2-112,view.getScaledHeight()/2-120);
        SelectedSkillInfoPanel selectedSkillInfoPanel = new SelectedSkillInfoPanel(this,
                view.getScaledWidth()/2+120,view.getScaledHeight()/2-120);
        selectedSkillInfoPanel.setID("selected_skill_info_panel");

        view.addChild(skillList);
        view.addChild(activeSkillBar);
        view.addChild(selectedSkillInfoPanel);
    }
}
