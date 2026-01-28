package net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.slotted_skill_bar;

import com.mojang.blaze3d.platform.InputConstants;
import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.events.Clickable;
import net.lucent.easygui.util.math.BoundChecker;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerSkillData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.guis.easygui.elements.EmptyButton;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.ActiveSkillSlot;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.MainSkillContainer;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.active_skill_container.ViewDetailsDropDown;
import net.thejadeproject.ascension.network.serverBound.ChangeSkillSlotSpellPayload;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.registries.AscensionRegistries;

import java.util.ArrayList;
import java.util.List;

public class SkillBarContainer extends EmptyContainer implements Clickable {
    private final TextureDataSubSection background =  new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_stuff/skill_menu.png"),
            320,256,
            0,0,192,83
    );
    public Label selectedSkillLabel;
    public List<ActiveSkillSlot> slots = new ArrayList<>();
    public ActiveSkillSlot hoveredSlot;
    public ActiveSkillSlot selectedSlot;
    public SkillBarDropDown dropDown;
    public SkillBarContainer(IEasyGuiScreen screen){
        super(screen,0,0,0,0);
        setWidth(192);
        setHeight(83);

        EmptyButton clearBtn = new EmptyButton(screen,145,19,32,10){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if (clicked){
                    clearSkillBar();
                }
            }
        };
        for(int i=0;i < 4; i++){
            ActiveSkillSlot slot = new ActiveSkillSlot(screen,16+i*18,33,18,18){
                @Override
                public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                    super.onClick(mouseX, mouseY, button, clicked);
                    if(clicked){
                        selectSkill(this,button);
                    }
                }

                @Override
                public void onMouseOver(boolean state) {
                    if(state && !isHovered) setHoveredSkill(this);
                    if(!state && isHovered) undoHoverSkill(this);
                    super.onMouseOver(state);

                }

            };
            addChild(slot);
            slots.add(slot);
            dropDown = new SkillBarDropDown(screen);
            dropDown.setActive(false);
            addChild(dropDown);
        }
        refresh();
        addChild(clearBtn);
        Label label = new Label(screen,56,25, Component.empty());
        label.centered = true;
        label.textColor = -1;
        label.useCustomScaling = true;
        label.setCustomScale(0.5);
        addChild(label);
        selectedSkillLabel = label;

    }
    public void selectSkill(ActiveSkillSlot slot,int button){
        if(slot.getSkill() == null){
            selectedSkillLabel.text = Component.empty();
            selectedSlot = null;
        }else {
            selectedSkillLabel.text = slot.getSkill().getSkillTitle();
            selectedSlot = slot;
            if(button == InputConstants.MOUSE_BUTTON_LEFT)((MainSkillContainer)getParent()).setHeldActiveSkill(slot.getSkill());
        }
    }
    public void undoHoverSkill(ActiveSkillSlot slot){
        if(this.hoveredSlot == slot) this.hoveredSlot = null;
    }
    public void setHoveredSkill(ActiveSkillSlot slot){
        hoveredSlot = slot;
    }

    public void clearSkillBar(){
        for(int i = 0;i<slots.size();i++){
            if(slots.get(i).getCurrentSkill() == null)continue;
            PacketDistributor.sendToServer(new ChangeSkillSlotSpellPayload(i,slots.get(i).getCurrentSkill().toString(),false));
            slots.get(i).setCurrentSkill(null);
        }
    }
    public void createSkillInfoPanel(){
        ((MainSkillContainer) getParent()).createSkillInfoPanel(selectedSlot.getCurrentSkill());
    }
    public void removeSkill(){
        PacketDistributor.sendToServer(new ChangeSkillSlotSpellPayload(slots.indexOf(selectedSlot),selectedSlot.getCurrentSkill().toString(),false));
        selectedSlot.setCurrentSkill(null);
        selectedSlot = null;
    }
    public void setSkillSlot(ISkill skill){
        if(hoveredSlot != null){
            ResourceLocation skillId = AscensionRegistries.Skills.SKILL_REGISTRY.getKey(skill);

            for(ActiveSkillSlot slot : slots){
                if(slot.getCurrentSkill() == null) continue;
                if(slot.getCurrentSkill().equals(skillId)) slot.setCurrentSkill(null);
            }
            //TODO update on server
            PacketDistributor.sendToServer(new ChangeSkillSlotSpellPayload(slots.indexOf(hoveredSlot),skillId.toString(),true));
            hoveredSlot.setCurrentSkill(skillId);
        }
    }
    public void refresh(){
        Player player = Minecraft.getInstance().player;
        List<PlayerSkillData.SkillSlot> slots =player.getData(ModAttachments.PLAYER_SKILL_DATA).activeSkillContainer.getSkillIdList();
        for(int i = 0; i<slots.size();i++){
            this.slots.get(i).setCurrentSkill(slots.get(i).skillId);
        }
    }
    public void openViewDetailsSelection(double mouseX,double mouseY){
        BoundChecker.Vec2 pos = screenToLocalPoint(mouseX,mouseY);
        dropDown.setActive(true);
        dropDown.setX(pos.x+5);
        dropDown.setY(pos.y+2);
    }
    public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
        dropDown.setActive(false);
        System.out.println(hoveredSlot == null ? "no hovered": (hoveredSlot.getCurrentSkill() == null ? "no hovered": hoveredSlot.getCurrentSkill().toString()));
        if(button == InputConstants.MOUSE_BUTTON_RIGHT && hoveredSlot != null && hoveredSlot.getCurrentSkill() != null) openViewDetailsSelection(mouseX,mouseY);
    }
    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
        background.renderTexture(guiGraphics);
    }
}
