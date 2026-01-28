package net.thejadeproject.ascension.guis.easygui.elements.stat_introspection;

import com.mojang.blaze3d.platform.InputConstants;
import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.properties.Positioning;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.guis.easygui.elements.EmptyButton;
import net.thejadeproject.ascension.network.serverBound.UpdateSuppressorValuePayload;

import java.awt.im.InputContext;

public class LimiterAttributeHolder  extends AttributeHolder {
    public ITextureData minusTexture =new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            67,154,71,156
    );
    public ITextureData plusTexture =new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            72,152,78,158
    );
    public EmptyButton minusButton;
    public EmptyButton plusButton;
    public Label suppressValue;
    public LimiterAttributeHolder(IEasyGuiScreen screen, int y, int width, ITextureData icon, Component displayName, Holder<Attribute> attribute) {
        super(screen,y, width,icon,displayName, attribute);

        minusButton = new EmptyButton(screen,0, 0,4,2){
            @Override
            public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                minusTexture.renderTexture(guiGraphics);


            }

            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == InputConstants.MOUSE_BUTTON_LEFT){
                    double currentValue = getCurrentSuppressValue();
                    ResourceLocation id = attribute.getKey().location();
                    double change = Screen.hasShiftDown() ? 0.1 : 0.01;
                    change = Screen.hasControlDown() ? 0.001 : change;
                    PacketDistributor.sendToServer(new UpdateSuppressorValuePayload(id.toString(),currentValue-change));

                }
            }
        };
        minusButton.setYPositioning(Positioning.CENTER);
        minusButton.setY(-minusButton.getHeight()/2);
        minusButton.setXPositioning(Positioning.END);
        minusButton.setX(-32);
        plusButton = new EmptyButton(screen,18,0,6,6){
            @Override
            public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                plusTexture.renderTexture(guiGraphics);



            }

            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == InputConstants.MOUSE_BUTTON_LEFT){
                    double currentValue = getCurrentSuppressValue();
                    ResourceLocation id = attribute.getKey().location();

                    double change = Screen.hasShiftDown() ? 0.1 : 0.01;
                    change = Screen.hasControlDown() ? 0.001 : change;
                    PacketDistributor.sendToServer(new UpdateSuppressorValuePayload(id.toString(),currentValue+change));

                }
            }
        };
        plusButton.setYPositioning(Positioning.CENTER);
        plusButton.setY(-plusButton.getHeight()/2);
        plusButton.setXPositioning(Positioning.END);
        plusButton.setX(-12);
        addChild(minusButton);
        addChild(plusButton);

        suppressValue = new Label(screen,12,0, Component.literal("100%"));
        suppressValue.setYPositioning(Positioning.CENTER);
        suppressValue.setXPositioning(Positioning.END);
        suppressValue.setX(-20);
        suppressValue.useCustomScaling = true;
        suppressValue.centered = true;
        suppressValue.setCustomScale(0.5);
        suppressValue.textColor = -1;


        addChild(suppressValue);
    }
    public double getCurrentSuppressValue(){
        ResourceLocation suppressor = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"suppression_modifier");
        Player player = Minecraft.getInstance().player;
        if(player.getAttribute(attribute).hasModifier(suppressor)){
            return 1+( player.getAttribute(attribute).getModifier(suppressor).amount());
        }

        return 1;
    }
    public double getSuppressValue(){
        ResourceLocation suppressor = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"suppression_modifier");
        Player player = Minecraft.getInstance().player;
        if(player.getAttribute(attribute).hasModifier(suppressor)){
            return ( (double)(int)( (1+player.getAttribute(attribute).getModifier(suppressor).amount())*1000))/10;
        }

        return 100;

    }
    public Component getUnSuppressedStat(){
        Player player = Minecraft.getInstance().player;
        AttributeModifier modifier = player.getAttribute(attribute).getModifier(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"suppression_modifier"));
        if(modifier == null) return Component.empty();
        player.getAttribute(attribute).removeModifier(modifier);

        double stat = player.getAttribute(attribute).getValue();

        player.getAttribute(attribute).addPermanentModifier(modifier);

        return Component.literal(" ("+formater.format(stat)+")");

    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Player player = Minecraft.getInstance().player;
        statValue.text = Component.literal(formater.format(player.getAttribute(attribute).getValue())).append(getUnSuppressedStat());
        suppressValue.text = Component.literal(getSuppressValue() +"%");
    }
}
