package net.thejadeproject.ascension.guis.easygui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.lucent.easygui.elements.controls.buttons.ColorButton;
import net.lucent.easygui.elements.controls.buttons.TextureButton;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.util.math.BoundChecker;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.registries.AscensionRegistries;

public class CardButton extends TextureButton {




    public String cardPhysique = null;
    public ITextureData focusedBorder = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(
                    AscensionCraft.MOD_ID,
                    "textures/physiques/root_cards.png"
            ),
            512,
            512,
            254,
            104,
            254+80,104+106
    );
    public ITextureData hoveredSpecialTexture =  new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(
                    AscensionCraft.MOD_ID,
                    "textures/physiques/root_cards.png"
            ),
            512,
            512,
            340,
            105,
            340+78,209
    );

    public CardButton(IEasyGuiScreen screen,int x, int y){
        super(screen,x,y,
                new TextureDataSubSection(
                        ResourceLocation.fromNamespaceAndPath(
                                AscensionCraft.MOD_ID,
                                "textures/physiques/root_cards.png"
                        ),
                        512,
                        512,
                        170,
                        105,
                        170+78,209
                )
                ,
                new TextureDataSubSection(
                        ResourceLocation.fromNamespaceAndPath(
                                AscensionCraft.MOD_ID,
                                "textures/physiques/root_cards.png"
                        ),
                        512,
                        512,
                        170,
                        105,
                        170+78,209
                ),
                new TextureDataSubSection(
                        ResourceLocation.fromNamespaceAndPath(
                                AscensionCraft.MOD_ID,
                                "textures/physiques/root_cards.png"
                        ),
                        512,
                        512,
                        170,
                        105,
                        170+78,209
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

        if(isFocused()) focusedBorder.renderTexture(guiGraphics,-1,-1);
        if(isHovered()){
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            BoundChecker.Vec2 point = screenToLocalPoint(mouseX,mouseY);
            hoveredSpecialTexture.renderTexture(guiGraphics);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0,0,500);
            if(cardPhysique != null) {
                guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.literal(AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(
                        ResourceLocation.fromNamespaceAndPath(cardPhysique.split(":")[0],cardPhysique.split(":")[1])
                ).getDisplayTitle()),point.x,point.y);
            }
            else guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.literal("?????"),point.x,point.y);
            RenderSystem.disableBlend();
            guiGraphics.pose().popPose();
        }
    }

    public void setCardPhysique(String cardPhysique) {
        this.cardPhysique = cardPhysique;

        ITextureData texture =AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(
                ResourceLocation.fromNamespaceAndPath(cardPhysique.split(":")[0],cardPhysique.split(":")[1])
        ).getPhysiqueImage();
        if(texture != null) {
            setDefaultTexture(texture);
            setFocusedTexture(texture);
            setPressedTexture(texture);
            setHoveredTexture(texture);
        }

    }
}
