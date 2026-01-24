package net.thejadeproject.ascension.guis.easygui.elements.introspection;

import com.mojang.blaze3d.platform.InputConstants;
import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.properties.Positioning;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.progression.paths.ModPaths;
import net.thejadeproject.ascension.progression.techniques.ITechnique;
import net.thejadeproject.ascension.registries.AscensionRegistries;

public class PathDataContainer extends EmptyContainer {
    public final MainContainer container;

    private final Label majorRealmNameLabel;
    private final Label minorRealmNameLabel;
    private final Label techniqueNameLabel;
    private final MainContainerEmptyButton openTechniqueBtn;


    public void setTextPosition(Label label){
        label.setX(getWidth()/2 - label.getWidth()/8);
    }

    public PathDataContainer(IEasyGuiScreen screen,int x,int y,int width,int height,MainContainer container){
        super(screen,x,y,width,height);
        this.container=container;
        Font font =   Minecraft.getInstance().font;

        Label majorRealmTitle = new Label(screen,0,4, Component.literal("Major Realm").withStyle(ChatFormatting.BOLD));
        majorRealmTitle.setXPositioning(Positioning.CENTER);
        majorRealmTitle.centered = true;
        majorRealmTitle.textColor = -1;
        majorRealmTitle.useCustomScaling = true;
        majorRealmTitle.setCustomScale(0.5);
        addChild(majorRealmTitle);
        Label majorRealmName = new Label(screen,0,12,Component.literal("Mortal"));
        majorRealmName.setXPositioning(Positioning.CENTER);
        majorRealmName.centered = true;
        majorRealmName.textColor = -1;
        majorRealmName.useCustomScaling = true;
        majorRealmName.setCustomScale(0.5);

        addChild(majorRealmName);
        this.majorRealmNameLabel = majorRealmName;

        Label minorRealmTitle = new Label(screen,0,20, Component.literal("Minor Realm").withStyle(ChatFormatting.BOLD));
        minorRealmTitle.setXPositioning(Positioning.CENTER);
        minorRealmTitle.centered = true;
        minorRealmTitle.textColor = -1;
        minorRealmTitle.useCustomScaling = true;
        minorRealmTitle.setCustomScale(0.5);

        addChild(minorRealmTitle);
        Label minorRealmName = new Label(screen,0,28,Component.literal("0"));
        minorRealmName.setXPositioning(Positioning.CENTER);
        minorRealmName.centered = true;
        minorRealmName.textColor = -1;
        minorRealmName.useCustomScaling = true;
        minorRealmName.setCustomScale(0.5);

        addChild(minorRealmName);
        this.minorRealmNameLabel = minorRealmName;

        Label techniqueTitle = new Label(screen,0,36, Component.literal("Technique").withStyle(ChatFormatting.BOLD));
        techniqueTitle.setXPositioning(Positioning.CENTER);
        techniqueTitle.centered = true;
        techniqueTitle.textColor = -1;
        techniqueTitle.useCustomScaling = true;
        techniqueTitle.setCustomScale(0.5);

        addChild(techniqueTitle);

        Label techniqueName = new Label(screen, 0,44,Component.literal("None"));
        techniqueName.setXPositioning(Positioning.CENTER);
        techniqueName.centered = true;
        techniqueName.textColor = -1;
        techniqueName.useCustomScaling = true;
        techniqueName.setCustomScale(0.5);

        addChild(techniqueName);

        this.techniqueNameLabel = techniqueName;

        MainContainerEmptyButton openTechnique = new MainContainerEmptyButton(screen,0,44-techniqueName.getHeight()/4,techniqueName.getWidth(), techniqueName.getHeight(),container){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == InputConstants.MOUSE_BUTTON_LEFT){
                    ResourceLocation technique = ResourceLocation.bySeparator(
                            Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(container.getSelectedPath().toString()).technique,
                            ':'
                    );
                    container.createTechniqueContainer(technique);
                }
            }

        };
        openTechnique.setXPositioning(Positioning.CENTER);
        openTechnique.setX(-techniqueName.getWidth()/4);
        openTechnique.useCustomScaling = true;
        openTechnique.setCustomScale(0.5);
        addChild(openTechnique);
        openTechnique.setActive(false);
        this.openTechniqueBtn = openTechnique;
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
        CultivationData.PathData pathData = Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA).getCultivationData()
                .getPathData(container.getSelectedPath().toString());
        Component majorRealm;
        Component minorRealm;
        Component technique;
        if(pathData.technique.equals("ascension:none")){
            technique = Component.literal("None");
            majorRealm = ModPaths.getPath(container.getSelectedPath()).getMajorRealmName(pathData.majorRealm);
            minorRealm = ModPaths.getPath(container.getSelectedPath()).getMinorRealmName(pathData.majorRealm,pathData.minorRealm);
            openTechniqueBtn.setActive(false);

        }else{
            ITechnique techniqueObj = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(pathData.technique,':'));
            technique = techniqueObj.getDisplayTitle();
            majorRealm = techniqueObj.getMajorRealmName(pathData.majorRealm);
            minorRealm = techniqueObj.getMinorRealmName(pathData.majorRealm,pathData.minorRealm);
            openTechniqueBtn.setActive(true);
        }
        majorRealmNameLabel.text = majorRealm;
        minorRealmNameLabel.text = minorRealm;
        techniqueNameLabel.text = technique;

        openTechniqueBtn.setWidth(techniqueNameLabel.getWidth());
        openTechniqueBtn.setY(44-techniqueNameLabel.getHeight()/4);
        openTechniqueBtn.setX(-techniqueNameLabel.getWidth()/4);
    }
}
