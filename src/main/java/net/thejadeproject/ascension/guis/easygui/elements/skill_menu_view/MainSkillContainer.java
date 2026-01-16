package net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view;

import com.mojang.blaze3d.platform.InputConstants;
import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.events.MouseReleaseListener;
import net.lucent.easygui.properties.Positioning;
import net.lucent.easygui.util.math.BoundChecker;
import net.minecraft.client.gui.GuiGraphics;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.passive_skill_container.PassiveSkillsContainer;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.slotted_skill_bar.SkillBarContainer;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.active_skill_container.ActiveSkillsContainer;
import net.thejadeproject.ascension.progression.skills.ISkill;

public class MainSkillContainer extends EmptyContainer implements MouseReleaseListener {
    ISkill heldActiveSkill = null;
    public MainSkillContainer(IEasyGuiScreen screen){
        super(screen,0,0,0,0);
        System.out.println("creating container");
        setXPositioning(Positioning.CENTER);
        setYPositioning(Positioning.CENTER);
        setWidth(320);
        setHeight(208);
        setX(-320/2);
        setY(-208/2);
        addChild(new ActiveSkillsContainer(screen));
        addChild(new PassiveSkillsContainer(screen));
        addChild(new SkillBarContainer(screen));
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);

    }

    public void setHeldActiveSkill(ISkill heldActiveSkill) {
        this.heldActiveSkill = heldActiveSkill;
    }

    @Override
    public void onMouseReleased(double mouseX, double mouseY, int button) {
        if(button != InputConstants.MOUSE_BUTTON_LEFT) return;
        if(heldActiveSkill == null) return;
        ((SkillBarContainer) getChildren().get(2)).setSkillSlot(heldActiveSkill);
        heldActiveSkill = null;
    }

    @Override
    public void renderChildren(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderChildren(guiGraphics, mouseX, mouseY, partialTick);
        BoundChecker.Vec2 point = screenToLocalPoint(mouseX,mouseY);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(point.x-8,point.y-8,0);
        guiGraphics.pose().scale(0.5f,0.5f,1);

        if(heldActiveSkill != null){
            heldActiveSkill.skillIcon().renderTexture(guiGraphics, 0,0);
        }
        guiGraphics.pose().popPose();
    }
}
