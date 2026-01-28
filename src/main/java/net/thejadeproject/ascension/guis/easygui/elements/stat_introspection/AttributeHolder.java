package net.thejadeproject.ascension.guis.easygui.elements.stat_introspection;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.other.Image;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.properties.Positioning;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class AttributeHolder extends EmptyContainer {
    public final Holder<Attribute> attribute;
    public NumberFormat formater = new DecimalFormat("#0.00");
    public Label statValue;
    public ITextureData icon;
    public Component displayName;
    public AttributeHolder(IEasyGuiScreen screen, int y, int width, ITextureData icon,Component displayName, Holder<Attribute> attribute) {
        super(screen,0,y,width,8);//TODO modify height once i know what it will be
        this.icon = icon;
        this.displayName = displayName;
        this.attribute = attribute;

        Label attributeName = new Label(screen,8,0,displayName);
        attributeName.setYPositioning(Positioning.CENTER);

        attributeName.textColor = -1;
        attributeName.setCustomScale(0.5);
        attributeName.useCustomScaling = true;
        attributeName.setY((int) (-attributeName.getHeight()*attributeName.getCustomScale()/2));
        addChild(attributeName);

        Image statIcon = new Image(screen,icon,0,0);
        statIcon.setYPositioning(Positioning.CENTER);

        statIcon.setCustomScale(0.5);
        statIcon.setY((int) (-statIcon.getHeight()*statIcon.getCustomScale()/2));
        statIcon.useCustomScaling = true;
        addChild(statIcon);

        statValue = new Label(screen, (int) (attributeName.getWidth()*attributeName.getCustomScale()+10),0,Component.empty());
        statValue.setYPositioning(Positioning.CENTER);

        statValue.setCustomScale(0.5);
        statValue.useCustomScaling = true;
        statValue.setY((int) (-statValue.getHeight()*statValue.getCustomScale()/2));
        statValue.textColor = -1;
        addChild(statValue);
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
        Player player = Minecraft.getInstance().player;
        statValue.text = Component.literal(formater.format(player.getAttribute(attribute).getValue()));
    }
}
