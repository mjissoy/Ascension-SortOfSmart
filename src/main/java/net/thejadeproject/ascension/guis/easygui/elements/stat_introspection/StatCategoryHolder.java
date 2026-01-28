package net.thejadeproject.ascension.guis.easygui.elements.stat_introspection;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.ContainerRenderable;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.properties.Positioning;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.thejadeproject.ascension.AscensionCraft;

import java.util.List;


public class StatCategoryHolder extends EmptyContainer {
    private TextureDataSubSection lineTexture = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            151,163,207,165
    );

    public StatCategoryHolder(IEasyGuiScreen screen, int x, int y, int width, Component title){
        super(screen,x,y,width,12);
        Label label = new Label(screen,0,8,title);
        label.setXPositioning(Positioning.CENTER);
        label.setCustomScale(0.8);
        label.centered = true;
        label.setY(8);
        label.useCustomScaling = true;
        label.text =title;
        label.textColor = -1;
        addChild(label);


    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
        lineTexture.renderTexture(guiGraphics,getWidth()/2-lineTexture.getWidth()/2,0);
        
    }

    @Override
    public int getHeight() {

        int maxHeight = 2;
        if(!childrenBuffer.isEmpty()){
            for(ContainerRenderable renderable : childrenBuffer){
                if(renderable.getY()+renderable.getHeight()*renderable.getCustomScale() > maxHeight){
                    maxHeight = (int) (renderable.getY()+ renderable.getHeight()*renderable.getCustomScale());
                }
            }
        }
        for(ContainerRenderable renderable: getChildren()){
            if(renderable.getY()+renderable.getHeight()*renderable.getCustomScale() > maxHeight){
                maxHeight = (int) (renderable.getY()+ renderable.getHeight()*renderable.getCustomScale());
            }
        }
        return maxHeight;
    }
}
