package net.thejadeproject.ascension.guis.easygui.elements.skill_menu.skill_description;

import net.lucent.easygui.elements.containers.scroll_boxes.DynamicScrollBox;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class SkillDescriptionContainer extends DynamicScrollBox {

    public SkillDescriptionContainer(IEasyGuiScreen easyGuiScreen,int x, int y){
        super(easyGuiScreen,x,y,54,54);
    }

    public void setTextContent(List<MutableComponent> textContent){
        removeChildren();
        //TODO make it a single component? instead of taking a list
        Component finalComponent = Component.empty();
        for(MutableComponent component: textContent){
            finalComponent = Component.empty().append(finalComponent).append(component);
        }
        addChild(
                (new Label.Builder())
                        .screen(getScreen())
                        .x(0).y(0)
                        .text(finalComponent)
                        .customScaling(0.5)
                        .width(getWidth()*2-2)
                        .build()
        );

    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

    }
}
