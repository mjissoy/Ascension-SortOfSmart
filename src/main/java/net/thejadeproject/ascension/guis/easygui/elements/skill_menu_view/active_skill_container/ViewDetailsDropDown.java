package net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.active_skill_container;

import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.thejadeproject.ascension.guis.easygui.elements.EmptyButton;

public class ViewDetailsDropDown extends EmptyButton {
    public final Component details = Component.literal("View Details");

    public ViewDetailsDropDown(IEasyGuiScreen screen, int x, int y) {
        super(screen, x, y, 0, 0);
        setWidth(Minecraft.getInstance().font.width(details)+6);
        setHeight(Minecraft.getInstance().font.lineHeight+6);
        useCustomScaling = true;
        setCustomScale(0.5);
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        guiGraphics.fill(0,0,getWidth(),getHeight(),-14209995);
        guiGraphics.drawString(Minecraft.getInstance().font,details,3,3,-1,false);
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
    }
}
