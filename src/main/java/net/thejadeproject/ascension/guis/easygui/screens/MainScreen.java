package net.thejadeproject.ascension.guis.easygui.screens;

import net.lucent.easygui.elements.containers.View;
import net.lucent.easygui.properties.Positioning;
import net.lucent.easygui.screens.EasyGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.guis.easygui.elements.main_menu.MainMenuContainer;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

import java.io.IOException;
@OnlyIn(Dist.CLIENT)
public class MainScreen extends EasyGuiScreen {
    public MainScreen(Component title) throws IOException {
        super(title);
        
        for(CultivationData.PathData pathData : Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPaths()){
            
        }

        /*
        (new EasyGuiBuilder(ResourceLocation.fromNamespaceAndPath(
                AscensionCraft.MOD_ID,
                "easy_gui/templates/main_menu.json"
        ))).build(this);
        */
        View view = new View(this);
        addView(view);
        view.setUseMinecraftScale(false);
        view.setCustomScale(3);
        MainMenuContainer container = new MainMenuContainer(this,-71,-71);
        container.setXPositioning(Positioning.CENTER);
        container.setYPositioning(Positioning.CENTER);
        view.addChild(container);
        container.setSticky(false);
        container.setID("main_menu_container");

    }
}
