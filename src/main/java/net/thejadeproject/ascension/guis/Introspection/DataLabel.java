package net.thejadeproject.ascension.guis.Introspection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;


import java.util.function.Supplier;

public class DataLabel implements Renderable {
    Component title;

    Supplier<Component> dataSupplier;

    public int x;
    public int y;
    public DataLabel(Component title,Supplier<Component> dataSupplier,int x,int y){
        this.title = title;
        this.dataSupplier = dataSupplier;
        this.y = y;
        this.x = x;

    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int i1, float v) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x,y,0);
        guiGraphics.drawString(Minecraft.getInstance().font, title,0,0,-16777216,false);
        guiGraphics.drawString(Minecraft.getInstance().font, dataSupplier.get(),0,10,-16777216,false);
        guiGraphics.pose().popPose();

    }
}
