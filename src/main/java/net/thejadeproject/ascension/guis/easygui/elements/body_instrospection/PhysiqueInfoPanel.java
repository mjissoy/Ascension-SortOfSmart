package net.thejadeproject.ascension.guis.easygui.elements.body_instrospection;

import com.mojang.blaze3d.platform.InputConstants;
import net.lucent.easygui.elements.containers.EmptyDraggableContainer;
import net.lucent.easygui.elements.containers.scroll_boxes.DynamicScrollBox;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.ContainerRenderable;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
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
import net.thejadeproject.ascension.progression.physiques.IPhysique;
import net.thejadeproject.ascension.progression.skills.skill_lists.IAcquirableSkill;
import net.thejadeproject.ascension.registries.AscensionRegistries;

import java.util.List;

public class PhysiqueInfoPanel extends EmptyDraggableContainer {

    IPhysique physique;
    private TextureDataSubSection background = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            214,6,356,143
    );
    private TextureDataSubSection panelTitle = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            77,189,150,200
    );
    public PhysiqueInfoPanel(IEasyGuiScreen screen,int x,int y,ContainerRenderable container){
        super(screen,x,y,142,137);
        ResourceLocation physiqueId = Minecraft.getInstance().player.getData(ModAttachments.PHYSIQUE).getPhysiqueId();
        if(physiqueId.toString().equals("ascension:none")){
            container.removeChild(this);
            return;
        }
        physique = AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(physiqueId);
        EmptyButton closeButton = new EmptyButton(screen,133,2,7,7){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == InputConstants.MOUSE_BUTTON_LEFT) getParent().getParent().removeChild(getParent());
            }
        };
        addChild(closeButton);

        addChild(physiqueTitle());
        DynamicScrollBox scrollBox = contentArea();
        int offset =description(scrollBox);
        dao(scrollBox,offset);
        addChild(scrollBox);
    }
    public Label physiqueTitle(){
        Label titleLabel = new Label(getScreen(),0,10,physique.getDisplayTitle());
        titleLabel.centered = true;
        titleLabel.useCustomScaling = true;
        titleLabel.setCustomScale(0.6);
        titleLabel.setXPositioning(Positioning.CENTER);
        titleLabel.textColor = -1;
        return titleLabel;
    }
    public int description(DynamicScrollBox scrollBox){

        MutableComponent text = Component.empty().append("Description\n")
                .withStyle(ChatFormatting.BOLD)
                .append(physique.getFullDescription());

        Label basicDescription = new Label(getScreen(),0, 0,text);
        basicDescription.useCustomScaling = true;
        basicDescription.setCustomScale(0.5);
        basicDescription.setWidth(scrollBox.getWidth()*2);
        basicDescription.setWrap(true);
        basicDescription.textColor = -1;
        scrollBox.addChild(basicDescription);
        return (int) (basicDescription.getHeight()*0.5);
    }
    public int dao(DynamicScrollBox scrollBox,int y){
        List<Label> dao = physique.getDisplayEfficiencies(getScreen());
        int daoY = y+5;
        for (Label label : dao) {
            label.setY(daoY);
            label.textColor = -1;
            scrollBox.addChild(label);
            daoY += (int) (label.getHeight() * label.getCustomScale());
        }
        return daoY;
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
    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);

        background.renderTexture(guiGraphics);
        panelTitle.renderTexture(guiGraphics,getWidth()/2-panelTitle.getWidth()/2,-6);
    }
}
