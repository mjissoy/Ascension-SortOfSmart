package net.thejadeproject.ascension.guis.easygui.screens;

import net.lucent.easygui.elements.containers.View;
import net.lucent.easygui.elements.controls.buttons.ColorButton;
import net.lucent.easygui.elements.other.Image;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.screens.EasyGuiScreen;
import net.lucent.easygui.templating.EasyGuiBuilder;
import net.lucent.easygui.templating.actions.Action;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.CultivationData;
import net.thejadeproject.ascension.cultivation.player.PlayerData;
import net.thejadeproject.ascension.guis.easygui.ModActions;
import net.thejadeproject.ascension.guis.easygui.elements.main_menu.MainMenuContainer;
import net.thejadeproject.ascension.util.ModAttachments;

import java.io.IOException;
@OnlyIn(Dist.CLIENT)
public class MainScreen extends EasyGuiScreen {
    public MainScreen(Component title) throws IOException {
        super(title);
        System.out.println("path data:");
        for(CultivationData.PathData pathData : Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPaths()){
            System.out.println(pathData.toString());
        }

        /*
        (new EasyGuiBuilder(ResourceLocation.fromNamespaceAndPath(
                AscensionCraft.MOD_ID,
                "easy_gui/templates/main_menu.json"
        ))).build(this);
        */
        View view = new View(this);
        addView(view);
        view.setUseMinecraftScale(true);
        view.setCustomScale(2);
        MainMenuContainer container = new MainMenuContainer(this,view.getScaledWidth()/2-71,view.getScaledHeight()/2-71);
        view.addChild(container);
        container.setSticky(true);
        container.setID("main_menu_container");

    }
}
