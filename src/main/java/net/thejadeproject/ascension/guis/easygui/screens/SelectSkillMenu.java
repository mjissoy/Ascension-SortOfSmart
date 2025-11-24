package net.thejadeproject.ascension.guis.easygui.screens;

import net.lucent.easygui.elements.BaseRenderable;
import net.lucent.easygui.elements.containers.View;
import net.lucent.easygui.screens.EasyGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.thejadeproject.ascension.guis.easygui.elements.select_skill_active_menu.SkillWheelContainer;

public class SelectSkillMenu extends EasyGuiScreen {
    private static SelectSkillMenu instance = null;

    public static void open(Component title){
        instance = new SelectSkillMenu(title);
        Minecraft.getInstance().setScreen(instance);
        System.out.println("created new Skill Wheel Menu");
    }
    public static void close(){
        //TODO send packet for spell selection
        instance.onClose();
        instance = null;
    }
    public static boolean hasInstance(){
        return instance != null;
    }



    private SelectSkillMenu(Component title) {
        super(title);
        View view = new View(this);
        addView(view);
        view.setUseMinecraftScale(false);
        SkillWheelContainer container = new SkillWheelContainer(this,view.getScaledWidth()/2-32*7,view.getScaledHeight()/2-32*7);
        view.addChild(container);
        container.setCustomScale(7);
        System.out.println("Select skill menu created");


    }
}
