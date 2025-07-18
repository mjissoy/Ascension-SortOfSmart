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
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.PlayerData;
import net.thejadeproject.ascension.guis.easygui.ModActions;
import net.thejadeproject.ascension.util.ModAttachments;

import java.io.IOException;

public class MainScreen extends EasyGuiScreen {
    public MainScreen(Component title) throws IOException {
        super(title);
        System.out.println("path data:");
        for(PlayerData.PathData pathData : Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA).getPaths()){
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
        view.setCustomScale(4);
        Image bg = new Image(this,new TextureDataSubSection(
          ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,
                  "textures/gui/overlay/gui_all.png"),
                256,
                256,
                0,
                4,
                143,142+4
        ),view.getScaledWidth()/2-71,view.getScaledHeight()/2-71){
            @Override
            public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                //to fix a bug
                this.getTextureData().renderTexture(guiGraphics);
            }

            @Override
            public void recalculatePos(int oldWidth, int oldHeight) {
                System.out.println("recalculating position");
                setX(getRoot().getScaledWidth()/2-71);
                setY(getRoot().getScaledHeight()/2-71);
            }

            @Override
            public void onResize(int oldWidth, int oldHeight, double oldScale) {
                recalculatePos(0,0);
            }
        };
        bg.setSticky(true);
        view.addChild(bg);
        ColorButton btn1 = new ColorButton(this,37,4,24,11);
        btn1.setDefaultColor(0);
        btn1.setHoverColor(1686472069);
        btn1.setPressColor(-1601862267);
        btn1.setFocusColor(0);
        ColorButton btn2 = new ColorButton(this,82,4,24,11);
        btn2.setDefaultColor(0);
        btn2.setHoverColor(1686472069);
        btn2.setPressColor(-1601862267);
        btn2.setFocusColor(0);
        Label l1Title = (new Label.Builder()).screen(this).x(12).y(25).text(Component.literal("Progress:").withStyle(ChatFormatting.BOLD)).width(140).customScaling(0.5).build();
        Label l1 = (new Label.Builder()).screen(this).x(12).y(30).customScaling(0.5).width(140).build();
        Label l2Title = (new Label.Builder()).screen(this).x(12).y(35).text(Component.literal("Major Realm:").withStyle(ChatFormatting.BOLD)).width(140).customScaling(0.5).build();
        Label l2 = (new Label.Builder()).screen(this).x(12).y(40).customScaling(0.5).width(140).build();
        Label l3Title = (new Label.Builder()).screen(this).x(12).y(45).text(Component.literal("Minor Realm:").withStyle(ChatFormatting.BOLD)).width(140).customScaling(0.5).build();
        Label l3 = (new Label.Builder()).screen(this).x(12).y(50).customScaling(0.5).width(140).build();
        Label l4Title = (new Label.Builder()).screen(this).x(12).y(55).text(Component.literal("Max Health:").withStyle(ChatFormatting.BOLD)).width(140).customScaling(0.5).build();
        Label l4 = (new Label.Builder()).screen(this).x(12).y(60).customScaling(0.5).width(140).build();
        Label l5Title = (new Label.Builder()).screen(this).x(12).y(65).text(Component.literal("Health:").withStyle(ChatFormatting.BOLD)).width(140).customScaling(0.5).build();
        Label l5 = (new Label.Builder()).screen(this).x(12).y(70).customScaling(0.5).width(140).build();
        Label l6Title = (new Label.Builder()).screen(this).x(12).y(75).text(Component.literal("Armor:").withStyle(ChatFormatting.BOLD)).width(140).customScaling(0.5).build();
        Label l6 = (new Label.Builder()).screen(this).x(12).y(80).customScaling(0.5).width(140).build();
        Label l7Title = (new Label.Builder()).screen(this).x(12).y(85).text(Component.literal("Attack Damage:").withStyle(ChatFormatting.BOLD)).width(140).customScaling(0.5).build();
        Label l7 = (new Label.Builder()).screen(this).x(12).y(90).customScaling(0.5).width(140).build();
        Label l8Title = (new Label.Builder()).screen(this).x(12).y(95).text(Component.literal("Movement Speed:").withStyle(ChatFormatting.BOLD)).width(140).customScaling(0.5).build();
        Label l8 = (new Label.Builder()).screen(this).x(12).y(100).customScaling(0.5).width(140).build();
        Label l9Title = (new Label.Builder()).screen(this).x(12).y(105).text(Component.literal("Jump Strength:").withStyle(ChatFormatting.BOLD)).width(140).customScaling(0.5).build();
        Label l9 = (new Label.Builder()).screen(this).x(12).y(110).customScaling(0.5).width(140).build();
        Label l10Title = (new Label.Builder()).screen(this).x(12).y(115).text(Component.literal("Physique:").withStyle(ChatFormatting.BOLD)).width(140).customScaling(0.5).build();
        Label l10 = (new Label.Builder()).screen(this).x(12).y(120).customScaling(0.5).width(140).build();

        l1.setTickAction(new Action(ModActions.DISPLAY_ATTRIBUTE_VALUE.get(),new Object[]{"Progress"}));
        l2.setTickAction(new Action(ModActions.DISPLAY_ATTRIBUTE_VALUE.get(),new Object[]{"Major Realm"}));
        l3.setTickAction(new Action(ModActions.DISPLAY_ATTRIBUTE_VALUE.get(),new Object[]{"Minor Realm"}));
        l4.setTickAction(new Action(ModActions.DISPLAY_ATTRIBUTE_VALUE.get(),new Object[]{"Max Health"}));
        l5.setTickAction(new Action(ModActions.DISPLAY_ATTRIBUTE_VALUE.get(),new Object[]{"Health"}));
        l6.setTickAction(new Action(ModActions.DISPLAY_ATTRIBUTE_VALUE.get(),new Object[]{"Armor"}));
        l7.setTickAction(new Action(ModActions.DISPLAY_ATTRIBUTE_VALUE.get(),new Object[]{"Attack"}));
        l8.setTickAction(new Action(ModActions.DISPLAY_ATTRIBUTE_VALUE.get(),new Object[]{"Speed"}));
        l9.setTickAction(new Action(ModActions.DISPLAY_ATTRIBUTE_VALUE.get(),new Object[]{"Jump Strength"}));
        l10.setTickAction(new Action(ModActions.DISPLAY_ATTRIBUTE_VALUE.get(),new Object[]{"Physique"}));

        bg.addChild(btn1);
        bg.addChild(btn2);
        bg.addChild(l1Title);
        bg.addChild(l1);
        bg.addChild(l2Title);
        bg.addChild(l2);
        bg.addChild(l3Title);
        bg.addChild(l3);
        bg.addChild(l4Title);
        bg.addChild(l4);
        bg.addChild(l5Title);
        bg.addChild(l5);
        bg.addChild(l6Title);
        bg.addChild(l6);
        bg.addChild(l7Title);
        bg.addChild(l7);
        bg.addChild(l8Title);
        bg.addChild(l8);
        bg.addChild(l9Title);
        bg.addChild(l9);
        bg.addChild(l10Title);
        bg.addChild(l10);

    }
}
