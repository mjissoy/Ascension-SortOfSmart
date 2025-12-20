package net.thejadeproject.ascension.guis.easygui.elements.skill_menu;

import net.lucent.easygui.elements.containers.list_containers.DynamicSizedListContainer;
import net.lucent.easygui.elements.containers.scroll_boxes.DynamicScrollBox;
import net.lucent.easygui.elements.other.Image;
import net.lucent.easygui.interfaces.ContainerRenderable;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerSkillData;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu.skill_slots.SkillBarSkillSlot;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

import java.util.List;

public class ActiveSkillBar extends Image {

    private int selectedSkill = 0;
    private boolean skillSelected = false;
    private final PlayerSkillData.ActiveSkillContainer activeSkillContainer;

    public String getSelectedSkillId(){
        return activeSkillContainer.getSkillIdList().get(selectedSkill);
    }

    public ActiveSkillBar(IEasyGuiScreen easyGuiScreen,int x, int y){
        super(easyGuiScreen,new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(
                        AscensionCraft.MOD_ID,
                        "textures/gui/screen/skill_select.png"
                ),320,240,0,0,225,72)
                ,x,y);
        
        DynamicScrollBox scrollBox = new DynamicScrollBox(easyGuiScreen,11,14,203,50){
            @Override
            public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                updateScrollData();
            }
        };
        
        DynamicSizedListContainer skillSlotListContainer = new DynamicSizedListContainer(easyGuiScreen,0,0,false){

            @Override
            public void updateDimensions() {
                for(int i = 0;i < getChildren().size(); i++){
                    if(getAlignment()) getChildren().get(i).setY(getUsedLength(i));
                    else getChildren().get(i).setX(getUsedLength(i));
                }
            }
        };
        skillSlotListContainer.setGap(2);
        skillSlotListContainer.setID("skill_slot_container_box");
        scrollBox.addChild(skillSlotListContainer);

        activeSkillContainer  = Minecraft.getInstance().player.getData(ModAttachments.PLAYER_SKILL_DATA).activeSkillContainer;
        

        addChild(scrollBox);
        setSticky(true);
        populateSkillSlots(skillSlotListContainer);
    }

    public boolean isSkillSlotSelected(){
        return skillSelected;
    }

    public ContainerRenderable getSelectedSkillSlot(){
        return getSkillSlots().get(selectedSkill);
    }
    public int getSelectedSkillSlotId(){
        return selectedSkill;
    }

    public List<ContainerRenderable> getSkillSlots(){
        return getScreen().getElementByID("skill_slot_container_box").getChildren();
    }
    public void selectSkillBarSkill(ContainerRenderable renderable){
        if(!(renderable instanceof SkillBarSkillSlot skillSlot)) return;
        List<ContainerRenderable> skillSlots = getSkillSlots();
        if(!skillSlots.contains(renderable)) return;
        if(skillSelected){
            getSelectedSkillSlot().setFocused(false);
        }

        selectedSkill = skillSlots.indexOf(skillSlot);
        skillSelected = true;
        skillSlot.setFocused(true);
        if(!getSelectedSkillId().isEmpty()) ((SelectedSkillInfoPanel) getScreen().getElementByID("selected_skill_info_panel")).selectSkill(ResourceLocation.bySeparator(getSelectedSkillId(),':'));

    }


    //TODO add some way to config how many slots are available
    public void populateSkillSlots(ContainerRenderable container){
        
        for(int i = 0;i<activeSkillContainer.MAX_SKILL_SLOTS;i++){
            SkillBarSkillSlot skillBarSkillSlot = new SkillBarSkillSlot(getScreen(),0,0,i,activeSkillContainer);
            container.addChild(skillBarSkillSlot);
        }

    }


    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);

    }
}
