package net.thejadeproject.ascension.guis.Introspection;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.CultivationData;

import java.util.ArrayList;
import java.util.List;

public class Overlay extends Screen {
    LocalPlayer player;
    public Overlay(Component title, LocalPlayer player) {
        super(title);
        this.player = player;
        labels.add(new DataLabel(Component.literal("Progress"), CultivationData.ClientCultivationData::getProgressUI,0,0));
        labels.add(new DataLabel(Component.literal("Major Realm"), CultivationData.ClientCultivationData::getMajorRealmUI,0,0));
        labels.add(new DataLabel(Component.literal("Minor Realm"), CultivationData.ClientCultivationData::getMinorRealmUI,0,0));
    }
    public List<DataLabel> labels = new ArrayList<>();
    ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/overlay/gui_main_page.png");


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        int y = (height - 166) / 2;
        int x = (width - 176) / 2;


        guiGraphics.blit(TEXTURE,x,y,0,0,176,166,176,166);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x+50,y+20,0);
        for(DataLabel label:labels){
            label.render(guiGraphics,mouseX,mouseY,partialTick);
            guiGraphics.pose().translate(0,20,0);
        }
        guiGraphics.pose().popPose();
    }
}
