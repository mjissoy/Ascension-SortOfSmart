package net.thejadeproject.ascension.guis.easygui.elements.skill_menu;

import net.lucent.easygui.elements.other.Image;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu.buttons.ChangeSkillSlotButton;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu.skill_description.SkillDescriptionContainer;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.progression.skills.data.casting.SkillType;
import net.thejadeproject.ascension.registries.AscensionRegistries;

public class SelectedSkillInfoPanel extends Image {
    public ResourceLocation selectedSkillId;
    public SkillType selectedSkillType;
    public final ActiveSkillBar activeSkillBar;
    public final ChangeSkillSlotButton button;
    public SelectedSkillInfoPanel(IEasyGuiScreen easyGuiScreen, int x, int y){
        super(easyGuiScreen,new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(
                AscensionCraft.MOD_ID,
                "textures/gui/screen/skill_select.png"
        ),320,240,225,0,301,152),x,y);
        this.setSticky(true);

        activeSkillBar = (ActiveSkillBar) getScreen().getElementByID("active_skill_bar_container");

        Image skillImage = new Image(easyGuiScreen,null,23,21){
            //TODO remove in new easy gui update
            @Override
            public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                if(this.getTextureData() != null) getTextureData().renderTexture(guiGraphics);
            }
        };
        Image skillBorder = new Image(easyGuiScreen,
                new TextureDataSubSection(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_slots.png"),112,96,2,51,42,91),
                18,17);
        Label titleLabel = new Label.Builder()
                .screen(easyGuiScreen)
                .x(40).y(65)
                .width(60).customScaling(0.7)
                .centered(true).build();

        SkillDescriptionContainer skillDescriptionContainer = new SkillDescriptionContainer(easyGuiScreen,12,72);

        ChangeSkillSlotButton changeSkillSlotButton = new ChangeSkillSlotButton(easyGuiScreen,0,125);
        button = changeSkillSlotButton;
        changeSkillSlotButton.setX(getWidth()/2-changeSkillSlotButton.getScaledWidth()/2);
        addChild(skillImage);
        addChild(skillBorder);
        addChild(titleLabel);
        addChild(skillDescriptionContainer);
        addChild(changeSkillSlotButton);
    }


    public void renderSkillDetails(){
        if(selectedSkillId == null) return;
        ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(selectedSkillId);
        ((Image)getChildren().get(0)).setTextureData(skill.skillIcon());
        ((Label)getChildren().get(2)).text = skill.getSkillTitle();
        ((Label)getChildren().get(2)).width = Minecraft.getInstance().font.width(skill.getSkillTitle());
        ((SkillDescriptionContainer) getChildren().get(3)).setTextContent(skill.getSkillDescription(Minecraft.getInstance().player));

    }

    public ResourceLocation getSelectedSkillId(){
        return selectedSkillId;
    }

    public void selectSkill(ResourceLocation skillId){
        this.selectedSkillId = skillId;
        this.selectedSkillType = SkillType.getSkillType(selectedSkillId);
        renderSkillDetails();
    }

    private boolean doesSelectedSkillSlotSkillMatch(){
        if(selectedSkillId == null) return false;
        String skillSlotId = activeSkillBar.getSelectedSkillId();

        return selectedSkillId.toString().equals(skillSlotId);
    }

    private boolean isActiveSkill(){
        return selectedSkillType == SkillType.Active;
    }
    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics,mouseX,mouseY,partialTick);

        button.setVisible(selectedSkillId != null && activeSkillBar.isSkillSlotSelected() && !doesSelectedSkillSlotSkillMatch() && isActiveSkill());
        if(activeSkillBar.isSkillSlotSelected() && !doesSelectedSkillSlotSkillMatch()){
           button.renderTexture = button.slotTexture;
        }else if(activeSkillBar.isSkillSlotSelected() && doesSelectedSkillSlotSkillMatch()){
            button.renderTexture = button.removeTexture;
        }
    }
}
