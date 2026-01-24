package net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view;

import com.mojang.blaze3d.platform.InputConstants;
import net.lucent.easygui.elements.containers.EmptyDraggableContainer;
import net.lucent.easygui.elements.containers.scroll_boxes.DynamicScrollBox;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.ContainerRenderable;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.properties.Positioning;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.guis.easygui.elements.EmptyButton;
import net.thejadeproject.ascension.guis.easygui.elements.introspection.MainContainer;
import net.thejadeproject.ascension.guis.easygui.elements.introspection.TechniqueInfoPanel;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.registries.AscensionRegistries;

public class SkillInfoPanel extends EmptyDraggableContainer {
    private TextureDataSubSection background = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            214,6,356,143
    );
    private TextureDataSubSection panelTitle = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            77,176,150,187
    );
    public final ResourceLocation skillId;
    public final ISkill skill;
    public final MainSkillContainer container;
    public SkillInfoPanel(IEasyGuiScreen screen, int x, int y, ResourceLocation skill, MainSkillContainer container){
        super(screen,x,y,142,137);
        this.skillId = skill;
        this.skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(skill);
        this.container = container;

        addChild(skillTitle());
        DynamicScrollBox scrollBox = contentArea();

        description(scrollBox);

        addChild(scrollBox);
        EmptyButton closeButton = new EmptyButton(screen,133,2,7,7){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == InputConstants.MOUSE_BUTTON_LEFT) container.removeChild(getParent());
            }
        };
        addChild(closeButton);

    }
    public Label skillTitle(){
        Label titleLabel = new Label(getScreen(),0,10,skill.getSkillTitle());
        titleLabel.centered = true;
        titleLabel.useCustomScaling = true;
        titleLabel.setCustomScale(0.8);
        titleLabel.setXPositioning(Positioning.CENTER);
        titleLabel.textColor = -1;
        return titleLabel;
    }
    public void description(DynamicScrollBox area){
        Component text = skill.getSkillDescription(Minecraft.getInstance().player);
        Label description = new Label(getScreen(),0,0,text);
        description.setCustomScale(0.5);
        description.textColor = -1;
        description.setWrap(true);
        description.setWidth(area.getWidth()*2);
        area.addChild(description);

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
