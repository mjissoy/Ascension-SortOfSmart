package net.thejadeproject.ascension.guis.easygui.elements.main_menu.stats;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.AscensionCraft;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class StatLabel extends EmptyContainer {
    public final Holder<Attribute> stat;
    public final NumberFormat formatter = new DecimalFormat("#0.00");
    public StatLabel(IEasyGuiScreen screen, int x, int y, Holder<Attribute> attributeHolder){
        super(screen,x,y,0,0);
        stat = attributeHolder;
        Player player = Minecraft.getInstance().player;
        //TODO make a map of stats and display components
        //TODO change to use labels
        Component titleText = Component.literal(stat.getRegisteredName()+":\n").withStyle(ChatFormatting.BOLD);
        //TODO add label for title
        Component valueText = getValue(player);
        //TODO add label for value

    }
    public Component getValue(Player player){
        AttributeInstance instance = player.getAttribute(stat);
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"ascension_stat_limiter");
        double currentValue = instance.getValue();
        if(!instance.hasModifier()) return Component.literal(String.valueOf(currentValue));
        //temp remove AttributeModifier
        AttributeModifier limiter = instance.getModifier(location);
        instance.removeModifier(location);
        double currentMax = instance.getValue();
        instance.addPermanentModifier(limiter);
        return Component.literal(currentMax + " ("+currentValue+")");
    }



}
