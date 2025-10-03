package net.thejadeproject.ascension.guis.easygui;

import net.lucent.easygui.elements.containers.View;
import net.lucent.easygui.elements.other.Image;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.elements.other.ProgressBar;
import net.lucent.easygui.overlays.EasyGuiOverlay;
import net.lucent.easygui.overlays.EasyGuiOverlayManager;
import net.lucent.easygui.templating.actions.Action;
import net.lucent.easygui.util.textures.TextureDataSubSection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.thejadeproject.ascension.AscensionCraft;
@OnlyIn(Dist.CLIENT)
public class ModOverlays {

    public static EasyGuiOverlay QI_TRACKER = new EasyGuiOverlay((eventHolder, overlay)->{
        View view = new View(overlay,0,0);
        overlay.addView(view);
        view.setUseMinecraftScale(true);
        view.setCustomScale(2);
        Image image = new Image(overlay,
                new TextureDataSubSection(
                        ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/overlay/gui_all.png"),
                        256,256,
                        0,147,
                        53,163
                ),
                0,0);
        Label progressLabel = (new Label.Builder()).screen(overlay).centered(true).x(26).y((163-147)/2).build();
        progressLabel.setTickAction(new Action(ModActions.DISPLAY_ATTRIBUTE_VALUE.get(),new Object[]{"Progress"}));
        image.addChild(progressLabel);

        view.addChild(image);
    });

    public static EasyGuiOverlay HEALTH_BAR = new EasyGuiOverlay((eventHolder, overlay) ->{
        View view = new View(overlay,0,0){};

        overlay.addView(view);
        view.setUseMinecraftScale(true);

        TextureDataSubSection background = new TextureDataSubSection(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/overlay/health_bar.png"),
                81,
                18,
                0,
                0,
                81,
                9
        );
        TextureDataSubSection bar = new TextureDataSubSection(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/overlay/health_bar.png"),
                81,
                18,
                0,
                9,
                81,
                18
        );

        ProgressBar progressBar = new ProgressBar(
                overlay,
                bar,
                background,
                view.getScaledWidth()/2-91,
                view.getScaledHeight()-39
        ){
            @Override
            public double getProgress() {
                if(Minecraft.getInstance().player == null) return 0;
                double currentHealth = Minecraft.getInstance().player.getHealth();
                double maxHealth = Minecraft.getInstance().player.getMaxHealth();
                return  currentHealth/maxHealth;
            }
            @Override
            public void recalculatePos(int oldWidth, int oldHeight) {

                setX((getRoot()).getScaledWidth()/2-91);
                setY((getRoot()).getScaledHeight() - 39);
            }


        };
        progressBar.setSticky(true);
        view.addChild(progressBar);
    });

    public static void register(){
        //EasyGuiOverlayManager.addLayer( ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"progress_layer"),QI_TRACKER);
        EasyGuiOverlayManager.registerVanillaOverlayOverride(VanillaGuiLayers.PLAYER_HEALTH,HEALTH_BAR);
    }
}
