package net.thejadeproject.ascension.guis.easygui.elements.introspection;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.tooltips.EasyTooltip;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.progression.techniques.ITechnique;
import net.thejadeproject.ascension.registries.AscensionRegistries;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

public class ProgressBar extends EmptyContainer {

    public final MainContainer container;
    private boolean isHovered;
    NumberFormat formatter = new DecimalFormat("#0.00");
    public final HashMap<ResourceLocation, TextureDataSubSection> minorRealmProgressBar = new HashMap<>(){{
        put(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"intent"),new TextureDataSubSection(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/overlay/overlays_all.png"),
                256,256,
                0,7,65,14
        ));
        put(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"body"),new TextureDataSubSection(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/overlay/overlays_all.png"),
                256,256,
                0,14,65,21
        ));
        put(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"essence"),new TextureDataSubSection(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/overlay/overlays_all.png"),
                256,256,
                0,0,65,7
        ));
    }};
    public final TextureDataSubSection stabilityProgress = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            0,216,65,223
    );
    public ProgressBar(IEasyGuiScreen screen,int x,int y,MainContainer container){
        super(screen,x,y,65,7);
        this.container = container;

    }

    public boolean canBreakthrough(Player player){
        CultivationData.PathData pathData = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(container.getSelectedPath().toString());
        if(pathData.technique.equals("ascension:none")){
            return false;
        }
        ITechnique technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(pathData.technique,':'));
        return technique.canBreakthrough(player,pathData.majorRealm,pathData.minorRealm,pathData.pathProgress);
    }

    public double stabilityProgress(Player player){
        CultivationData.PathData pathData = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(container.getSelectedPath().toString());
        if(pathData.technique.equals("ascension:none")){
            return 0.0;
        }
        ITechnique technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(pathData.technique,':'));
        return pathData.stabilityCultivationTicks/technique.getStabilityHandler().getMaxCultivationTicks();
    }
    public double realmProgress(Player player){
        CultivationData.PathData pathData = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(container.getSelectedPath().toString());
        if(pathData.technique.equals("ascension:none")){
            return 0.0;
        }
        ITechnique technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(pathData.technique,':'));
        return pathData.pathProgress/technique.getQiForRealm(pathData.majorRealm,pathData.minorRealm);
    }
    public  Component realmTooltip(Player player){
        CultivationData.PathData pathData = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(container.getSelectedPath().toString());
        if(pathData.technique.equals("ascension:none")){
            return Component.literal(formatter.format(pathData.pathProgress)+"/???");
        }
        ITechnique technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(pathData.technique,':'));
        return Component.literal(formatter.format( pathData.pathProgress) + "/" +formatter.format(technique.getQiForRealm(pathData.majorRealm,pathData.minorRealm)));
    }
    public void renderTooltip(Player player,int mouseX,int mouseY){
        //TODO
        Component tooltipComponent;
        if(canBreakthrough(player)){
            tooltipComponent = Component.literal(formatter.format(stabilityProgress(player)*100)+"%");
        }else {
            tooltipComponent = realmTooltip(player);
        }
        EasyTooltip tooltip = new EasyTooltip(mouseX,mouseY, List.of(tooltipComponent));
        getScreen().setTooltip(tooltip);
    }

    @Override
    public void onMouseOver(boolean state) {
        super.onMouseOver(state);
        isHovered = state;
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Player player = Minecraft.getInstance().player;
        double realmProgress = realmProgress(player);

        TextureDataSubSection barTexture = minorRealmProgressBar.get(container.getSelectedPath());

        barTexture.renderTexture(guiGraphics,0,0,0,0, (int) (barTexture.getWidth()*realmProgress),barTexture.getHeight()
        );
        if(canBreakthrough(player)) stabilityProgress.renderTexture(guiGraphics,0,0,0,0, (int) (stabilityProgress.getWidth()*stabilityProgress(player)),stabilityProgress.getHeight());

        if(isHovered){
            renderTooltip(player,mouseX,mouseY);
        }

    }
}
