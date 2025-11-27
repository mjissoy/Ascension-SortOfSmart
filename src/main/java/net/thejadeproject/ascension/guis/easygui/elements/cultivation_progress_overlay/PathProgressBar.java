package net.thejadeproject.ascension.guis.easygui.elements.cultivation_progress_overlay;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.util.textures.TextureData;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.util.ModAttachments;

public class PathProgressBar extends EmptyContainer {
    private final ITextureData background = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/overlay/gui_all_for_olli.png"),
            256,
            256,
            72,
            245,
            139,
            254
    );
    private final ITextureData dragonOrnament = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/overlay/gui_all_for_olli.png"),
            256,
            256,
            76,
            214,
            134 ,
            235
    );
    private final ITextureData progressBar = new TextureData(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/overlay/gui_all_for_olli.png"),
            256,
            256);
    private int barBaseY = 224;
    private int barY = 0;
    String currentPath = null;
    double lastCheckedCultivationProgress;
    double ticksSinceChange = 0;
    public PathProgressBar(IEasyGuiScreen screen,int x,int y){
        super(screen,x,y,67,21);
    }
    public void setCurrentPath(String currentPath){
        this.currentPath = currentPath;
        switch (currentPath) {
            case "ascension:essence" -> barY = 224;
            case "ascension:intent" -> barY = 235;
            case "ascension:body" -> barY = 246;
        }
        ticksSinceChange = 0;
    }
    public String getCurrentPath(){
        return currentPath;
    }
    public double getCultivationProgress(){
        if(Minecraft.getInstance().player == null) return 0.0;
        CultivationData cultivationData = Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA).getCultivationData();
        double cultivationProgress = cultivationData.getPathData(currentPath).pathProgress;
        if(cultivationProgress != lastCheckedCultivationProgress){
            ticksSinceChange = 0;
            lastCheckedCultivationProgress = cultivationProgress;
        }
        return cultivationProgress/cultivationData.getMaxQiForRealm(currentPath);
    }


    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
        if(currentPath != null){


            background.renderTexture(guiGraphics);

            progressBar.renderTexture(guiGraphics,1,1,2,barY, (int) (69*getCultivationProgress()),7);
            dragonOrnament.renderTexture(guiGraphics,5,-10);
            ticksSinceChange += partialTick;
            //5s
            if(ticksSinceChange >= 100){
                currentPath = null;
            }
        }
    }
}
