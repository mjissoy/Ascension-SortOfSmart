package net.thejadeproject.ascension.guis.easygui.elements.main_menu.draggable_data;

import com.mojang.blaze3d.platform.InputConstants;
import net.lucent.easygui.elements.containers.EmptyDraggableContainer;
import net.lucent.easygui.elements.other.Image;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.guis.easygui.elements.main_menu.buttons.BigButton;
@OnlyIn(Dist.CLIENT)
public class DraggableDataContainer extends EmptyDraggableContainer {
    public DraggableDataContainer(IEasyGuiScreen easyGuiScreen, int x, int y) {
        super(easyGuiScreen, x-142/2, y-136/2, 0, 0);
        setSticky(true);
        Image bg = new Image(
                easyGuiScreen,
                new TextureDataSubSection(
                        ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,
                                "textures/gui/screen/gui_all.png"),
                        256,
                        256,
                        0, 0,
                        142, 135
                )
                , 0
                , 0
        );
        addChild(bg);
        setWidth(bg.getWidth());
        setHeight(bg.getHeight());

        BigButton closeButton = new BigButton(easyGuiScreen,121,-4,
                new TextureDataSubSection(
                        ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,
                                "textures/gui/screen/gui_all.png"),
                        256,
                        256,
                        176, 20,
                        176+32, 20+32
                )
        ){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == InputConstants.MOUSE_BUTTON_LEFT){
                    getParent().getParent().removeChild(getParent());
                }
            }
        };

        addChild(closeButton);

    }


}
