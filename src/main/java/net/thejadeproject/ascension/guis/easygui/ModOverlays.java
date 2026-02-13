package net.thejadeproject.ascension.guis.easygui;

import net.lucent.easygui.elements.containers.View;
import net.lucent.easygui.elements.other.Image;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.elements.other.ProgressBar;
import net.lucent.easygui.overlays.EasyGuiOverlay;
import net.lucent.easygui.overlays.EasyGuiOverlayManager;
import net.lucent.easygui.templating.actions.Action;
import net.lucent.easygui.util.textures.TextureDataSubSection;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.telemetry.TelemetryProperty;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.guis.easygui.elements.player_qi_display.QiBar;
import net.thejadeproject.ascension.guis.easygui.elements.skill_cast_progress.SkillCastProgressContainer;
import net.thejadeproject.ascension.guis.easygui.overlay_views.CultivationProgressBarsView;
import net.thejadeproject.ascension.guis.easygui.overlay_views.SkillCastProgressBarView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

@OnlyIn(Dist.CLIENT)
public class ModOverlays {


    public static EasyGuiOverlay HEALTH_BAR = new EasyGuiOverlay((eventHolder, overlay) ->{
        View view = new View(overlay,0,0){
            @Override
            public void renderChildren(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                Minecraft.getInstance().gui.leftHeight += 10;
                if(Minecraft.getInstance().options.hideGui ||
                        (Minecraft.getInstance().gameMode != null &&
                                !Minecraft.getInstance().gameMode.canHurtPlayer())) return;


                super.renderChildren(guiGraphics, mouseX, mouseY, partialTick);

            }
        };

        overlay.addView(view);
        view.setUseMinecraftScale(true);

        TextureDataSubSection background = new TextureDataSubSection(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/overlay/overlays_all.png"),
                256,
                256,
                2,
                125,
                83,
                134
        );
        TextureDataSubSection bar = new TextureDataSubSection(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/overlay/overlays_all.png"),
                256,
                256,
                2,
                135,
                83,
                144
        );

        ProgressBar progressBar = new ProgressBar(
                overlay,
                bar,
                background,
                view.getScaledWidth()/2-91,
                view.getScaledHeight()-39 // Move it down to avoid armor bar
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
                setY((getRoot()).getScaledHeight() - 39); // Match the new Y position
            }

            @Override
            public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
                if(Minecraft.getInstance().player != null){
                    NumberFormat formater = new DecimalFormat("#0.00");
                    double currentHealth = Minecraft.getInstance().player.getHealth();
                    double maxHealth = Minecraft.getInstance().player.getMaxHealth();
                    Component text = Component.literal(formater.format(currentHealth)+"/"+formater.format(maxHealth)).withStyle(ChatFormatting.BOLD);
                    int length = Minecraft.getInstance().font.width(text);
                    int height = Minecraft.getInstance().font.lineHeight;
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().translate(45-39/3.0,4-height/4.0,0);
                    guiGraphics.pose().scale(0.6F, 0.6F, 1);

                    //black = -16777216 white = -1
                    guiGraphics.drawString(Minecraft.getInstance().font,text,0,0,-1,false);
                    guiGraphics.pose().popPose();
                }
            }
        };
        progressBar.setSticky(true);
        view.addChild(progressBar);
    });

    public static EasyGuiOverlay SKILL_CAST_PROGRESS = new EasyGuiOverlay(((eventHolder, easyGuiOverlay) -> {
        SkillCastProgressBarView view = new SkillCastProgressBarView(easyGuiOverlay){
            @Override
            public void renderChildren(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                if(Minecraft.getInstance().options.hideGui ||
                        (Minecraft.getInstance().gameMode != null &&
                                !Minecraft.getInstance().gameMode.canHurtPlayer())) return;


                super.renderChildren(guiGraphics, mouseX, mouseY, partialTick);
            }
        };

        easyGuiOverlay.addView(view);

    }));
    public static EasyGuiOverlay CULTIVATION_PROGRESS = new EasyGuiOverlay(((eventHolder, easyGuiOverlay) -> {
        CultivationProgressBarsView view = new CultivationProgressBarsView(easyGuiOverlay,0,0){
            @Override
            public void renderChildren(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                if(Minecraft.getInstance().options.hideGui ||
                        (Minecraft.getInstance().gameMode != null &&
                                Minecraft.getInstance().gameMode.getPlayerMode() == GameType.SPECTATOR)) return;


                super.renderChildren(guiGraphics, mouseX, mouseY, partialTick);
            }
        };

        easyGuiOverlay.addView(view);

    }));
    public static EasyGuiOverlay PLAYER_QI_BAR = new EasyGuiOverlay(((eventHolder, easyGuiOverlay) -> {
        View view = new View(easyGuiOverlay,0,0){
            @Override
            public void renderChildren(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                if(Minecraft.getInstance().options.hideGui ||
                        (Minecraft.getInstance().gameMode != null &&
                                !Minecraft.getInstance().gameMode.canHurtPlayer())) return;


                super.renderChildren(guiGraphics, mouseX, mouseY, partialTick);
            }
        };
        view.setUseMinecraftScale(true);
        view.addChild(new QiBar(easyGuiOverlay,view.getScaledWidth(),view.getScaledHeight()));
        easyGuiOverlay.addView(view);

    }));
    public static void register(){
        //EasyGuiOverlayManager.addLayer( ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"progress_layer"),QI_TRACKER);
        EasyGuiOverlayManager.registerVanillaOverlayOverride(VanillaGuiLayers.PLAYER_HEALTH,HEALTH_BAR);
        EasyGuiOverlayManager.addLayer(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"skill_cast_progress"),SKILL_CAST_PROGRESS);
        EasyGuiOverlayManager.addLayer(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"cultivation_progress"),CULTIVATION_PROGRESS);
        EasyGuiOverlayManager.addLayer(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"qi_bar"),PLAYER_QI_BAR);

    }
}
