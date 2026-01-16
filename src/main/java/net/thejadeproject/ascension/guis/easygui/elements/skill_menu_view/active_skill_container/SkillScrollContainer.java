package net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.active_skill_container;

import com.mojang.blaze3d.platform.InputConstants;
import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.events.Clickable;
import net.lucent.easygui.interfaces.events.MouseScrollListener;
import net.lucent.easygui.util.math.BoundChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerSkillData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.ActiveSkillSlot;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.MainSkillContainer;
import net.thejadeproject.ascension.progression.skills.ISkill;

import java.util.ArrayList;
import java.util.List;

public class SkillScrollContainer extends EmptyContainer implements MouseScrollListener, Clickable {

    int slotOffset;
    public int rows;
    public int columns;
    List<List<ActiveSkillSlot>> skillSlots = new ArrayList<>();
    ActiveSkillSlot hovered;
    public ViewDetailsDropDown dropDown;
    public ISkill selectedSkill;
    public SkillScrollContainer(IEasyGuiScreen screen,int x,int y,int rows,int columns){
        super(screen,x,y,columns*18,rows*18);
        System.out.println("creating skill scroll container");
        for (int row = 0;row<rows;row++){
            skillSlots.add(new ArrayList<>());
            for(int column = 0;column < columns;column++){
                skillSlots.get(row).add(new ActiveSkillSlot(screen,column*18,row*18,18,18){
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
                addChild(skillSlots.get(row).get(column));
            }
        }
        this.rows = rows;
        this.columns = columns;
        refresh();
        dropDown = new ViewDetailsDropDown(screen,0,0){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == InputConstants.MOUSE_BUTTON_LEFT) viewSkillDetails();
            }
        };
        dropDown.setActive(false);
        addChild(dropDown);
        System.out.println("finished creating skill scroll container");
    }
    public void viewSkillDetails(){
        System.out.println("View details for skill: "+selectedSkill.getSkillTitle().toString());
    }
    public void openViewDetailsSelection(double mouseX,double mouseY){
        BoundChecker.Vec2 pos = screenToLocalPoint(mouseX,mouseY);
        dropDown.setActive(true);
        dropDown.setX(pos.x+5);
        dropDown.setY(pos.y+2);
    }

    public void refresh(){
        for(List<ActiveSkillSlot> row: skillSlots){
            for(ActiveSkillSlot slot : row){
                slot.setCurrentSkill(null);
            }
        }
        List<PlayerSkillData.SkillMetaData> data =  Minecraft.getInstance().player.getData(ModAttachments.PLAYER_SKILL_DATA).getActiveSkills();

        for (int i = slotOffset*columns;i<data.size();i++){
            int row = (int) (i / columns);
            int column = i%columns;
            skillSlots.get(row).get(column).setCurrentSkill(ResourceLocation.bySeparator(data.get(i).skillId,':'));
        }
    }
    public void selectSkill(ActiveSkillSlot slot,int button){
        //TODO update title bar
        ((ActiveSkillsContainer) getParent()).setSelectedSkill(slot.getSkill());
        if(button == InputConstants.MOUSE_BUTTON_LEFT)((MainSkillContainer)getParent().getParent()).setHeldActiveSkill(slot.getSkill());
        selectedSkill = slot.getSkill();
    }
    public void undoHoverSkill(ActiveSkillSlot slot){
        if(this.hovered == slot) this.hovered = null;
    }
    public void setHoveredSkill(ActiveSkillSlot slot){
        hovered = slot;
    }
    public int getTotalRowsNeeded(){
        List<PlayerSkillData.SkillMetaData> data =  Minecraft.getInstance().player.getData(ModAttachments.PLAYER_SKILL_DATA).getSkills();
        return (int) skillSlots.size()/columns;
    }
    @Override
    public void onMouseScroll(double mouseX, double mouseY, double scrollX, double scrollY) {
        //TODO
        double offsetDirection =  (scrollY*-1);
        if(offsetDirection > 0 && slotOffset >= getTotalRowsNeeded()-rows) return;
        if(offsetDirection < 0 && slotOffset == 0) return;

        slotOffset += (int) Math.signum(offsetDirection);
        refresh();
    }
    @Override
    public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
        dropDown.setActive(false);
        System.out.println(hovered == null ? "no hovered": (hovered.getCurrentSkill() == null ? "no hovered": hovered.getCurrentSkill().toString()));
        if(button == InputConstants.MOUSE_BUTTON_RIGHT && hovered != null && hovered.getCurrentSkill() != null) openViewDetailsSelection(mouseX,mouseY);
    }
}
