package net.thejadeproject.ascension.guis.easygui.elements.main_menu.path_data;

import com.mojang.blaze3d.platform.InputConstants;
import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.guis.easygui.elements.main_menu.buttons.BreakthroughButton;
import net.thejadeproject.ascension.guis.easygui.elements.main_menu.buttons.SmallButton;

//TODO
public class OuterPathDataContainer extends EmptyContainer {
    public OuterPathDataContainer(IEasyGuiScreen easyGuiScreen, int x, int y, int width, int height){
        super(easyGuiScreen,x,y,width,height);

        DisplayPathDataContainer container = new DisplayPathDataContainer(easyGuiScreen,0,30,getWidth(),83,"ascension:essence");
        addChild(container);
        container.setID("path_data_display_container");
        SmallButton essencePathButton = new SmallButton(easyGuiScreen,3,7,
                new TextureDataSubSection(
                        ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,
                                "textures/gui/screen/gui_all.png"),
                        256,
                        256,
                        144, 55,
                        144+32, 55+19
                )){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == InputConstants.MOUSE_BUTTON_LEFT){
                    container.setPath("ascension:essence");
                    setFocused(true);
                    getScreen().getElementByID("body_path_button").setFocused(false);
                    getScreen().getElementByID("intent_path_button").setFocused(false);
                }
            }
        };
        essencePathButton.setID("essence_path_button");
        addChild(essencePathButton);

        SmallButton bodyPathButton = new SmallButton(easyGuiScreen,86,7,
                new TextureDataSubSection(
                        ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,
                                "textures/gui/screen/gui_all.png"),
                        256,
                        256,
                        144, 55,
                        144+32, 55+19
                )){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == InputConstants.MOUSE_BUTTON_LEFT){
                    container.setPath("ascension:body");
                    setFocused(true);
                    getScreen().getElementByID("intent_path_button").setFocused(false);
                    getScreen().getElementByID("essence_path_button").setFocused(false);
                }
            }
        };
        bodyPathButton.setID("body_path_button");
        addChild(bodyPathButton);
        SmallButton intentPathButton = new SmallButton(easyGuiScreen,getWidth()/2-16,7,
                new TextureDataSubSection(
                        ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,
                                "textures/gui/screen/gui_all.png"),
                        256,
                        256,
                        144, 55,
                        144+32, 55+19
                )){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == InputConstants.MOUSE_BUTTON_LEFT){
                    container.setPath("ascension:intent");
                    setFocused(true);
                    getScreen().getElementByID("body_path_button").setFocused(false);
                    getScreen().getElementByID("essence_path_button").setFocused(false);
                }
            }
        };
        intentPathButton.setID("intent_path_button");
        addChild(intentPathButton);


        essencePathButton.setFocused(true);
    }

}
