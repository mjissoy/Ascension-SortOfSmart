package net.thejadeproject.ascension.guis.easygui.elements.skill_menu;

import net.lucent.easygui.elements.containers.scroll_boxes.FixedSizedWrappedSingleItemListScrollBox;
import net.lucent.easygui.elements.other.Image;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerSkillData;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu.skill_slots.BasicSkillSlot;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

public class SkillListPanel extends Image {
    FixedSizedWrappedSingleItemListScrollBox skillListBox;
    public SkillListPanel(IEasyGuiScreen easyGuiScreen,int x, int y){
        super(easyGuiScreen,new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(
                AscensionCraft.MOD_ID,
                "textures/gui/screen/skill_select.png"
        ),320,240,0,72,225,225),x,y);
        this.setSticky(true);

        FixedSizedWrappedSingleItemListScrollBox skillListBox = new FixedSizedWrappedSingleItemListScrollBox(
                easyGuiScreen,
                11,
                45,
                202,
                2,
                42,42,
                11

        ){
            @Override
            public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

            }
        };
        skillListBox.setBackgroundColor(65535);
        addChild(skillListBox);
        this.skillListBox = skillListBox;
        loadSkills();
    }
    public void loadSkills(){
        Player player = Minecraft.getInstance().player;
        
        for(PlayerSkillData.SkillMetaData data : player.getData(ModAttachments.PLAYER_SKILL_DATA).getSkills()){
            
            skillListBox.addChild(new BasicSkillSlot(getScreen(),0,0,ResourceLocation.bySeparator(data.skillId,':')));
        }

    }
}
