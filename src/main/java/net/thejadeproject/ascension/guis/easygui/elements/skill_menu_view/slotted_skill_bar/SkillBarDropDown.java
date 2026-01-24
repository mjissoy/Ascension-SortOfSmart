package net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.slotted_skill_bar;

import com.mojang.blaze3d.platform.InputConstants;
import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.thejadeproject.ascension.guis.easygui.elements.EmptyButton;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.MainSkillContainer;

public class SkillBarDropDown extends EmptyContainer {

    public final Component remove = Component.literal("Remove");
    public final Component details = Component.literal("View Details");

    public SkillBarDropDown(IEasyGuiScreen screen){
        super(screen,0,0,0,0);

        Font font = Minecraft.getInstance().font;

        addChild(
                new EmptyButton(screen,3,3,font.width(details),font.lineHeight){
                    @Override
                    public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                        super.onClick(mouseX, mouseY, button, clicked);
                        if(clicked && button == InputConstants.MOUSE_BUTTON_LEFT){
                            ((SkillBarContainer) getParent().getParent()).removeSkill();
                        }
                    }
                }
        );
        addChild(
                new EmptyButton(screen,3,Minecraft.getInstance().font.lineHeight+3+2,font.width(details),font.lineHeight){
                    @Override
                    public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                        super.onClick(mouseX, mouseY, button, clicked);
                        if(clicked && button == InputConstants.MOUSE_BUTTON_LEFT){
                            ((SkillBarContainer) getParent().getParent()).createSkillInfoPanel();
                        }
                    }
                }
        );
        useCustomScaling = true;
        setCustomScale(0.5);
        setWidth(Minecraft.getInstance().font.width(details)+6);
        setHeight(Minecraft.getInstance().font.lineHeight*2+8);
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.fill(0,0,getWidth(),getHeight(),-14209995);
        guiGraphics.drawString(Minecraft.getInstance().font,remove,3,3,-1,false);
        guiGraphics.drawString(Minecraft.getInstance().font,details,3,Minecraft.getInstance().font.lineHeight+3+2,-1,false);
    }
}
