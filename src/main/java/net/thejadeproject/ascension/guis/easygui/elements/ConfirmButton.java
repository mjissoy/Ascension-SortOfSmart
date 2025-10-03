package net.thejadeproject.ascension.guis.easygui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.lucent.easygui.elements.controls.buttons.TextureButton;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.util.math.BoundChecker;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.registries.AscensionRegistries;
@OnlyIn(Dist.CLIENT)
public class ConfirmButton extends TextureButton {
    public ConfirmButton(IEasyGuiScreen screen, int x, int y){
        super(screen,x,y,
                new TextureDataSubSection(
                        ResourceLocation.fromNamespaceAndPath(
                                AscensionCraft.MOD_ID,
                                "textures/physiques/root_cards.png"
                        ),
                        512,
                        512,
                        1,
                        108,
                        33,108+32
                )
                ,
                new TextureDataSubSection(
                        ResourceLocation.fromNamespaceAndPath(
                                AscensionCraft.MOD_ID,
                                "textures/physiques/root_cards.png"
                        ),
                        512,
                        512,
                        1,
                        108,
                        33,108+32
                ),
                new TextureDataSubSection(
                        ResourceLocation.fromNamespaceAndPath(
                                AscensionCraft.MOD_ID,
                                "textures/physiques/root_cards.png"
                        ),
                        512,
                        512,
                        1,
                        108,
                        33,108+32
                )
        );

    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        //temp to fix bug
        ITextureData finalTexture = defaultTexture;
        if(isFocused()) finalTexture = focusedTexture;
        if(isHovered()) finalTexture = hoveredTexture;
        if(isPressed()) finalTexture = pressedTexture;

        finalTexture.renderTexture(guiGraphics);
        //will be fixed soon
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        if(isHovered())guiGraphics.fill(5,5,getWidth()-5,getHeight()-5,2023792800);



    }
}
