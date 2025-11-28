package net.thejadeproject.ascension.guis.easygui.elements.select_skill_active_menu;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.controls.buttons.AbstractButton;
import net.lucent.easygui.elements.other.Image;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.interfaces.events.Clickable;
import net.lucent.easygui.util.textures.TextureData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;

public class SkillWheelSegment extends EmptyContainer {
    Image wedgeImage;
    ITextureData wedgeTexture = new TextureData(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/overlay/skill_wheel_segment.png"),
            64,
            32
    );
    int textureX;
    int textureY;

    int slot;
    ITextureData slotTexture;
    public SkillWheelSegment(IEasyGuiScreen screen,int x, int y,int textureX,int textureY,int slot,ITextureData slotTexture,double textureRotation){
        super(screen,x,y,44,32);
        this.textureX = textureX;
        this.textureY = textureY;
        this.slot = slot;
        this.slotTexture = slotTexture;


        wedgeImage = new Image(screen,wedgeTexture,textureX,textureY){
            @Override
            public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                if(getParent().isFocused()){
                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                    super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
                    RenderSystem.disableBlend();
                }
            }
        };
        addChild(wedgeImage);
        wedgeImage.setRotation(0,0,textureRotation);
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
        if(slotTexture != null) slotTexture.renderTexture(guiGraphics,getX()/2-slotTexture.getWidth()/2,getY()/2-slotTexture.getHeight()/2);

    }


}
