package net.thejadeproject.ascension.gui.elements.skill_view;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.layout.positioning.rules.PositioningRules;
import net.lucent.easygui.gui.textures.ITextureData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;

public class SkillMenuContainer extends RenderableElement implements ISkillDragContainer {

    private ResourceLocation heldSkill;
    private ITextureData heldSkillIcon;

    private final PassiveSkillList passiveList;
    private final ActiveSkillList activeList;

    private boolean dirty = true;

    public SkillMenuContainer(UIFrame frame) {
        super(frame);
        getPositioning().setPositioningRule(PositioningRules.CENTER);
        setId("container");

        activeList = new ActiveSkillList(frame);
        RenderableElement activeSkillContainer = new ActiveSkillBar(frame);
        passiveList = new PassiveSkillList(frame);

        passiveList.getPositioning().setX(38);
        passiveList.getPositioning().setY(-passiveList.getHeight() / 2);

        activeSkillContainer.getPositioning().setX(-160);
        activeSkillContainer.getPositioning().setY(-passiveList.getHeight() / 2);

        activeList.getPositioning().setX(-160);
        activeList.getPositioning().setY(
                activeSkillContainer.getPositioning().getRawY() + 6 + activeSkillContainer.getHeight()
        );

        addChild(activeList);
        addChild(activeSkillContainer);
        addChild(passiveList);
    }

    public void setOnClose(Runnable onClose) {
        passiveList.setOnClose(onClose);
    }

    public void markDirty() {
        dirty = true;
    }

    public void refreshLists() {
        activeList.refreshSkills();
        passiveList.refreshSkills();
        dirty = false;
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);

        if (active) {
            SkillMenuState.setOpenMenu(this);
            markDirty();
        } else {
            SkillMenuState.clearOpenMenu(this);
        }
    }

    @Override
    public void renderTick(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (isActive() && dirty) {
            refreshLists();
        }
        super.renderTick(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void runChildren(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.runChildren(guiGraphics, mouseX, mouseY, partialTick);
        if (heldSkillIcon != null) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 1000);
            Vec2 global = getGlobalPoint();
            heldSkillIcon.renderAt(
                    guiGraphics,
                    (int)(mouseX - global.x - (float) heldSkillIcon.getWidth() / 2),
                    (int)(mouseY - global.y - (float) heldSkillIcon.getWidth() / 2)
            );
            guiGraphics.pose().popPose();
        }


    }

    public void setHeldSkill(ResourceLocation skill) {
        this.heldSkill = skill;
        var skillInstance = skill == null ? null : AscensionRegistries.Skills.SKILL_REGISTRY.get(skill);
        this.heldSkillIcon = skillInstance == null ? null : skillInstance.getIcon();
    }

    public ResourceLocation getHeldSkill() {
        return heldSkill;
    }
}