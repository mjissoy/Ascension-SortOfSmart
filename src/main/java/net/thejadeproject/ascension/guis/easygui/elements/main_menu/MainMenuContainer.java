package net.thejadeproject.ascension.guis.easygui.elements.main_menu;

import com.mojang.blaze3d.platform.InputConstants;
import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.other.Image;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.guis.easygui.elements.main_menu.buttons.SmallButton;
import net.thejadeproject.ascension.guis.easygui.elements.main_menu.path_data.OuterPathDataContainer;
@OnlyIn(Dist.CLIENT)
public class MainMenuContainer extends EmptyContainer {

    public MainMenuContainer(IEasyGuiScreen easyGuiScreen, int x, int y){
        super(easyGuiScreen,x,y,0,0);
        Image bg = new Image(
                easyGuiScreen,
                new TextureDataSubSection(
                        ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,
                                "textures/gui/screen/gui_all.png"),
                        256,
                        256,
                        0,0,
                        142,135
                )
                ,0
                ,0
        );
        this.addChild(bg);
        this.setWidth(bg.getWidth());
        this.setHeight(bg.getHeight());
        //path data button
        SmallButton pathButton = new SmallButton(easyGuiScreen,35,-3,
                new TextureDataSubSection(
                        ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,
                                "textures/gui/screen/gui_all.png"),
                        256,
                        256,
                        144,0,
                        144+32,19
                )){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == InputConstants.MOUSE_BUTTON_LEFT){
                    getScreen().getElementByID("outer_path_container").setVisible(true);
                    getScreen().getElementByID("stat_container").setVisible(false);
                    setFocused(true);
                    getScreen().getElementByID("stat_container_access_button").setFocused(false);
                }
            }
        };
        pathButton.setID("path_container_access_button");
        pathButton.setFocused(true);
        addChild(pathButton);

        //Stat data button
        SmallButton statButton = new SmallButton(easyGuiScreen,76,-3,
                new TextureDataSubSection(
                        ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,
                                "textures/gui/screen/gui_all.png"),
                        256,
                        256,
                        176,0,
                        176+32,19
                )){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);

                if(clicked && button == InputConstants.MOUSE_BUTTON_LEFT){
                    getScreen().getElementByID("stat_container").setVisible(true);
                    getScreen().getElementByID("outer_path_container").setVisible(false);

                    setFocused(true);
                    getScreen().getElementByID("path_container_access_button").setFocused(false);
                }
            }
        };
        statButton.setID("stat_container_access_button");
        addChild(statButton);
        //both containers
        StatContainer statContainer = new StatContainer(easyGuiScreen,11,17,121,109);
        addChild(statContainer);
        statContainer.setID("stat_container");
        statContainer.visible = false;

        OuterPathDataContainer outerPathDataContainer = new OuterPathDataContainer(easyGuiScreen,11,17,121,109);
        addChild(outerPathDataContainer);
        outerPathDataContainer.setID("outer_path_container");
    }
}
