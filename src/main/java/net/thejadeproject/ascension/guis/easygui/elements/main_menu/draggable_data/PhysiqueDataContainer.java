package net.thejadeproject.ascension.guis.easygui.elements.main_menu.draggable_data;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.containers.scroll_boxes.DynamicScrollBox;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.ContainerRenderable;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.properties.Positioning;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.thejadeproject.ascension.progression.physiques.IPhysique;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.skills.skill_lists.AcquirableSkillData;

import java.awt.*;
import java.util.List;
//TODO redo parts to be just one Component to render
@OnlyIn(Dist.CLIENT)
public class PhysiqueDataContainer extends DraggableDataContainer{
    public IPhysique physique;
    public PhysiqueDataContainer(IEasyGuiScreen easyGuiScreen, int x, int y,String physiqueId) {
        super(easyGuiScreen, x, y);
        setXPositioning(Positioning.CENTER);
        setYPositioning(Positioning.CENTER);
        DynamicScrollBox textArea = new DynamicScrollBox(easyGuiScreen,11,17,120,109){
            @Override
            public int getMaxXOffset() {
                return 0;
            }

            @Override
            public int getMaxYOffset() {
                int maxYOffset = 0;
                for(ContainerRenderable containerRenderable : getChildren()){
                    int yOffset = (int) (containerRenderable.getY()+containerRenderable.getHeight()*containerRenderable.getCustomScale());
                    if(yOffset > maxYOffset) maxYOffset = yOffset;

                }
                return maxYOffset;
            }
        };
        textArea.setZ(-1);
        addChild(textArea);
        textArea.useBackgroundColor = false;
        textArea.setBorderVisible(false);
        this.physique = AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(ResourceLocation.bySeparator(physiqueId,':'));
        setSticky(true);
        Component title = physique.getDisplayTitle();

        Label titleLabel = new Label(easyGuiScreen,textArea.getWidth()/2,5,title);
        titleLabel.useCustomScaling = true;
        titleLabel.setCustomScale(0.5);
        titleLabel.centered = true;
        textArea.addChild(titleLabel);

        MutableComponent text = Component.empty().append("Description\n")
                .withStyle(ChatFormatting.BOLD)
                .append(physique.getFullDescription());

        Label basicDescription = new Label(easyGuiScreen,0, (int) (titleLabel.getHeight()*titleLabel.getCustomScale())+5,text);
        basicDescription.useCustomScaling = true;
        basicDescription.setCustomScale(0.5);
        basicDescription.setWidth(textArea.getWidth()*2);
        basicDescription.setWrap(true);


        textArea.addChild(basicDescription);

        int daoY = (int) (basicDescription.getHeight()*basicDescription.getCustomScale())+ (int) (titleLabel.getHeight()*titleLabel.getCustomScale())+10;
        List<Label> dao = physique.getDisplayEfficiencies(easyGuiScreen);
        for (Label label : dao) {
            label.setY(daoY);
            label.setWidth(textArea.getWidth()*2);
            label.setWrap(true);
            textArea.addChild(label);
            daoY += (int) (label.getHeight() * label.getCustomScale());
        }
        Component skillListTitle = Component.empty()
                .append(Component.literal("Skill List").withStyle(ChatFormatting.BOLD));


        Label skillListTitleLabel = new Label(easyGuiScreen,textArea.getWidth()/2,daoY+5,skillListTitle);
        skillListTitleLabel.useCustomScaling = true;
        skillListTitleLabel.setCustomScale(0.5);
        skillListTitleLabel.centered = true;

        textArea.addChild(skillListTitleLabel);


        MutableComponent skillListText = Component.empty();
        List<AcquirableSkillData> skillList = physique.getSkillList().getSkillList();
        for (AcquirableSkillData acquirableSkillData : skillList) {
            //TODO change text color if unlocked or locked
            //TODO replace aquirableSkillData
            skillListText.append("\n").append(acquirableSkillData.asComponent());
        }
        Label skillListLabel = new Label(easyGuiScreen,0, (int) (daoY+skillListTitleLabel.getHeight()*0.5+5),skillListText);
        skillListLabel.setWrap(true);
        skillListLabel.setWidth(textArea.getWidth()*2);
        skillListLabel.useCustomScaling = true;
        skillListLabel.setCustomScale(0.5);
        textArea.addChild(skillListLabel);

    }
}
