package net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.passive_skill_container;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.events.MouseScrollListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerSkillData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.MainSkillContainer;

import java.util.ArrayList;
import java.util.List;

public class PassiveSkillScrollContainer extends EmptyContainer implements MouseScrollListener {
    int slotOffset = 0;
    int rows;

    public PassiveSkillSlot hovered;
    List<PassiveSkillSlot> slots = new ArrayList<>();
    public PassiveSkillScrollContainer(IEasyGuiScreen screen,int x,int y,int rows){
        super(screen,x,y,102,184);
        this.rows = rows;
        for(int i = 0;i<rows;i++){
            slots.add(new PassiveSkillSlot(screen,4,4+22*i,92,20){
                @Override
                public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                    super.onClick(mouseX, mouseY, button, clicked);
                    if(clicked) selectSkill(this,button);

                }
                @Override
                public void onMouseOver(boolean state) {
                    if(state && !isHovered) setHoveredSkill(this);
                    if(!state && isHovered) undoHoverSkill(this);
                    super.onMouseOver(state);

                }
            });
            addChild(slots.get(i));
        }
        refresh();


    }

    public void selectSkill(PassiveSkillSlot slot,int button){
        //todo open View details
        ((MainSkillContainer) getParent().getParent()).createSkillInfoPanel(slot.getCurrentSkill());
    }
    public void undoHoverSkill(PassiveSkillSlot slot){
        if(this.hovered == slot) slot = null;
    }
    public void setHoveredSkill(PassiveSkillSlot slot){
        hovered = slot;
    }

    public List<PlayerSkillData.SkillMetaData> getPassiveSkills(){
        return Minecraft.getInstance().player.getData(ModAttachments.PLAYER_SKILL_DATA).getPassiveSkills();
    }
    public void refresh(){
        for(PassiveSkillSlot slot:slots){
            slot.setCurrentSkill(null);
        }

        List<PlayerSkillData.SkillMetaData> skills = getPassiveSkills();


        for(int i = slotOffset;i<skills.size() && i<slots.size()+slotOffset;i++){

            slots.get(i-slotOffset).setCurrentSkill(ResourceLocation.bySeparator(skills.get(i).skillId,':'));
        }
    }
    @Override
    public void onMouseScroll(double mouseX, double mouseY, double scrollX, double scrollY) {
        //TODO
        double offsetDirection =  (scrollY*-1);
        if(offsetDirection > 0 && slotOffset >= getPassiveSkills().size()-rows) return;
        if(offsetDirection < 0 && slotOffset == 0) return;

        slotOffset += (int) Math.signum(offsetDirection);
        refresh();
    }


}
