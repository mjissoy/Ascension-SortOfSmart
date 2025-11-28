package net.thejadeproject.ascension.guis.easygui.elements.player_qi_display;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.interfaces.complex_events.Sticky;
import net.lucent.easygui.util.textures.TextureData;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.util.ModAttachments;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class QiBar extends EmptyContainer implements Sticky {

    private final ITextureData background = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/overlay/gui_all_for_olli.png"),
            256,
            256,
            72,
            245,
            139,
            254
    );
    private final ITextureData progressBar = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/overlay/gui_all_for_olli.png"),
            256,
            256,
            2,224,
            66,230);

    public QiBar(IEasyGuiScreen screen,int scaledWidth,int scaledHeight){
        super(screen,scaledWidth/2+25,scaledHeight-49,0,0);
    }

    @Override
    public void recalculatePos(int oldWidth, int oldHeight) {
        setX(getRoot().getScaledWidth()/2+25);
        setY(getRoot().getScaledHeight()-49);

    }
    public double lastQi;
    public boolean display;
    public int ticksElapsed;
    public int MAX_TICKS_ELAPSED = 40;

    public double getProgress(){
        display = false;
        if(Minecraft.getInstance().player == null) return 0.0;
        PlayerData data = Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA);
        if(data == null) return 0.0;

        if(lastQi != data.getCurrentQi()) lastQi = data.getCurrentQi(); ticksElapsed = 0; display = true;
        return data.getCurrentQi()/data.getPlayerMaxQi();
    }

    @Override
    public boolean isSticky() {
        return true;
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        double progress = getProgress();
        if(display){

            background.renderTexture(guiGraphics);
            progressBar.renderTexture(guiGraphics,1,1,2,224, (int) (69*progress),7);
            PlayerData data = Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA);

            NumberFormat formater = new DecimalFormat("#0.00");
            Component text = Component.literal(formater.format(data.getCurrentQi())+"/"+formater.format(data.getPlayerMaxQi()));
            int length = Minecraft.getInstance().font.width(text);
            int height = Minecraft.getInstance().font.lineHeight;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(71/2.0-length/4.0,4.5-height/4.0,0);
            guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);


            guiGraphics.drawString(Minecraft.getInstance().font,text,0,0,-1,false);
            guiGraphics.pose().popPose();
            ticksElapsed += 1;
            if(ticksElapsed >= MAX_TICKS_ELAPSED){
                display = false;
                ticksElapsed = 0;
            }
        }
    }
}
