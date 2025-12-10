package net.thejadeproject.ascension.guis.easygui.elements.main_menu.draggable_data;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.containers.scroll_boxes.DynamicScrollBox;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.ContainerRenderable;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
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
                return Math.max(0,maxYOffset-getHeight());
            }
        };
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
        titleLabel.setWidth(textArea.getWidth());
        titleLabel.setWrap(true);
        textArea.addChild(titleLabel);

        MutableComponent text = Component.empty().append("\nDescription")
                .withStyle(ChatFormatting.BOLD)
                .append(physique.getFullDescription());

        Label basicDescription = new Label(easyGuiScreen,0, (int) (titleLabel.getHeight()*titleLabel.getCustomScale()),text);
        basicDescription.useCustomScaling = true;
        basicDescription.setCustomScale(0.5);
        basicDescription.setWidth(textArea.getWidth());
        basicDescription.setWrap(true);


        textArea.addChild(basicDescription);

        int daoY = (int) (basicDescription.getHeight()*basicDescription.getCustomScale())+ (int) (titleLabel.getHeight()*titleLabel.getCustomScale())+5;
        List<Label> dao = physique.getDisplayEfficiencies(easyGuiScreen);
        for (Label label : dao) {
            label.setY(daoY);
            label.setWidth(textArea.getWidth());
            label.setWrap(true);
            textArea.addChild(label);
            daoY += (int) (label.getHeight() * label.getCustomScale());
        }

        MutableComponent skillListText = Component.empty()
                .append(Component.literal("Skill List").withStyle(ChatFormatting.BOLD));

        List<AcquirableSkillData> skillList = physique.getSkillList().getSkillList();
        for (AcquirableSkillData acquirableSkillData : skillList) {
            //TODO change text color if unlocked or locked
            //TODO replace aquirableSkillData
            skillListText.append("\n").append(acquirableSkillData.asComponent());
        }
        Label skillListLabel = new Label(easyGuiScreen,getWidth()/2,daoY+5,skillListText);
        skillListLabel.setWrap(true);
        skillListLabel.setWidth(121);
        skillListLabel.centered = true;
        skillListLabel.useCustomScaling = true;
        skillListLabel.setCustomScale(0.5);
        textArea.addChild(skillListLabel);

    }
}
