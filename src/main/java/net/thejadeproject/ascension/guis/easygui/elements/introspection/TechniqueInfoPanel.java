package net.thejadeproject.ascension.guis.easygui.elements.introspection;

import com.mojang.blaze3d.platform.InputConstants;
import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.containers.EmptyDraggableContainer;
import net.lucent.easygui.elements.containers.scroll_boxes.DynamicScrollBox;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.ContainerRenderable;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.properties.Positioning;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.guis.easygui.elements.EmptyButton;
import net.thejadeproject.ascension.progression.skills.skill_lists.IAcquirableSkill;
import net.thejadeproject.ascension.progression.techniques.ITechnique;
import net.thejadeproject.ascension.registries.AscensionRegistries;

import java.util.List;

public class TechniqueInfoPanel extends EmptyDraggableContainer {
    public final ResourceLocation techniqueId;
    public final ITechnique technique;
    private TextureDataSubSection background = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            214,6,356,143
    );
    private TextureDataSubSection panelTitle = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            77,163,150,174
    );

    public TechniqueInfoPanel(IEasyGuiScreen screen, int x, int y, ResourceLocation technique,MainContainer container){
        super(screen,x,y,142,137);
        this.techniqueId = technique;
        EmptyButton closeButton = new EmptyButton(screen,133,2,7,7){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == InputConstants.MOUSE_BUTTON_LEFT) container.removeChild(getParent());
            }
        };
        addChild(closeButton);
        this.technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(technique);
        System.out.println("drawing container");
        addChild(techniqueTitle());
        DynamicScrollBox textArea = contentArea();
        Label techniqueDescr = techniqueDescription(textArea);
        textArea.addChild(techniqueDescr);

        int yOffset = techniqueDao( textArea, (int) (techniqueDescr.getY()+techniqueDescr.getHeight()*0.5));

        skillList(textArea,yOffset);
        addChild(textArea);

    }
    public Label techniqueTitle(){
        Label titleLabel = new Label(getScreen(),0,10,technique.getDisplayTitle());
        titleLabel.centered = true;
        titleLabel.useCustomScaling = true;
        titleLabel.setCustomScale(0.8);
        titleLabel.setXPositioning(Positioning.CENTER);
        titleLabel.textColor = -1;
        return titleLabel;
    }
    public DynamicScrollBox contentArea(){
        DynamicScrollBox textArea = new DynamicScrollBox(getScreen(),3,20,136,117){
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
        textArea.useBackgroundColor = false;
        textArea.setBorderVisible(false);

        return textArea;
    }
    public Label techniqueDescription(ContainerRenderable parent){
        MutableComponent text = Component.empty().append("Description\n")
                .withStyle(ChatFormatting.BOLD)
                .append(technique.getDescription());

        Label basicDescription = new Label(getScreen(),0, 0,text);
        basicDescription.useCustomScaling = true;
        basicDescription.setCustomScale(0.5);
        basicDescription.setWidth(parent.getWidth()*2);
        basicDescription.setWrap(true);
        basicDescription.textColor = -1;
        return basicDescription;
    }
    public int techniqueDao(ContainerRenderable parent,int y){
        List<Label> dao = technique.getDisplayDaoEfficiencies(getScreen());
        int daoY = y+5;
        for (Label label : dao) {
            label.setY(daoY);
            label.textColor = -1;
            parent.addChild(label);
            daoY += (int) (label.getHeight() * label.getCustomScale());
        }
        return daoY;
    }
    public void skillList(ContainerRenderable parent,int y){
        //===TITLE===

        Component skillListTitle = Component.empty()
                .append(Component.literal("Skill List").withStyle(ChatFormatting.BOLD));

        Label skillListTitleLabel = new Label(getScreen(),0,y+5,skillListTitle);
        skillListTitleLabel.centered = true;
        skillListTitleLabel.textColor = -1;
        skillListTitleLabel.useCustomScaling = true;
        skillListTitleLabel.setCustomScale(0.5);
        skillListTitleLabel.setXPositioning(Positioning.CENTER);
        parent.addChild(skillListTitleLabel);

        //===Content===
        MutableComponent skillListText = Component.empty();
        List<IAcquirableSkill> skillList = technique.getSkillList().getSkillList();
        CultivationData.PathData cultivationData = Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(technique.getPath());
        for (IAcquirableSkill acquirableSkillData : skillList) {
            //TODO change text color if unlocked or locked
            //TODO replace aquirableSkillData
            skillListText.append("\n")
                    .append(acquirableSkillData.asComponent(
                    techniqueId,
                    ResourceLocation.bySeparator(technique.getPath(),':'),
                    cultivationData.majorRealm,
                    cultivationData.minorRealm

            ));
        }
        Label skillListLabel = new Label(getScreen(),0, (int) (y+skillListTitleLabel.getHeight()*0.5+5),skillListText);
        skillListLabel.setWrap(true);
        skillListLabel.setWidth(parent.getWidth()*2);
        skillListLabel.useCustomScaling = true;
        skillListLabel.setCustomScale(0.5);
        skillListLabel.textColor = -1;

        parent.addChild(skillListLabel);
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);


        background.renderTexture(guiGraphics);
        panelTitle.renderTexture(guiGraphics,getWidth()/2-panelTitle.getWidth()/2,-6);

    }
}
