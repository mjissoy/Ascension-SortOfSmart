package net.thejadeproject.ascension.refactor_packages.gui.elements.hud;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.elements.built_in.EasyLabel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class HealthBar extends RenderableElement {
    public HealthBar(UIFrame frame,int width,int height) {
        super(frame);
        setWidth(width);
        setHeight(height);


    }

    DecimalFormat format = new DecimalFormat("#.0");

    public double getProgress(){
        Player player = Minecraft.getInstance().player;
        IEntityData entityData = Minecraft.getInstance().player.getData(ModAttachments.ENTITY_DATA);

        if(getChildren().isEmpty()){
            EasyLabel label = new EasyLabel(getUiFrame());
            label.setWidth(getWidth()-2);
            label.setHeight(getHeight()-2);
            label.getPositioning().setX(1);
            label.getPositioning().setY(1);
            label.setTextPositioningX(EasyLabel.TextPositionRule.CENTER);
            label.setTextPositioningY(EasyLabel.TextPositionRule.CENTER);
            label.setTextColor(-1);

            addChild(label);
            label.setScaleToFit(true);
        }

        EasyLabel label = (EasyLabel) getChildren().getFirst();

        if(getAbsorptionProgress() != 0)label.setText(Component.literal(
                format.format(player.getHealth())+"/"+
                        format.format(player.getMaxHealth())+
                        "+("+ format.format(+player.getAbsorptionAmount())+")"));
        else label.setText(Component.literal(
                format.format(player.getHealth())+"/"+
                        format.format(player.getMaxHealth())));

        return entityData.getHealth()/player.getMaxHealth();

    }
    public double getAbsorptionProgress(){
        Player player = Minecraft.getInstance().player;
        return Math.clamp(player.getAbsorptionAmount()/player.getMaxHealth(),0,1);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        //render background

        guiGraphics.fill(0,0,getWidth(),getHeight(),0xFF000000);
        if(getProgress() != 0) guiGraphics.fill(1,1, (int) ((getWidth()-1)*getProgress()),getHeight()-1,0xFFFA0000);
        if(getAbsorptionProgress() != 0) guiGraphics.fill(1,1, (int) ((getWidth()-1)*getAbsorptionProgress()),getHeight()-1,0xFFFFBB00);
    }
}
