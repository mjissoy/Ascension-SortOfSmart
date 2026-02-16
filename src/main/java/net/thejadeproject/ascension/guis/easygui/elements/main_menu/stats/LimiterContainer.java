package net.thejadeproject.ascension.guis.easygui.elements.main_menu.stats;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.AscensionCraft;

public class LimiterContainer extends EmptyContainer {
    public final Holder<Attribute> attributeHolder;
    public final ResourceLocation limiterLocation = ResourceLocation.fromNamespaceAndPath(
            AscensionCraft.MOD_ID,
            "ascension_stat_limiter"
    );
    public LimiterContainer(IEasyGuiScreen screen, int x, int y, Holder<Attribute> attributeHolder){
        super(screen,x,y,0,0);
        this.attributeHolder = attributeHolder;
        //TODO button -> label -> button
        //TODO if limiter is not 100% add a release button to instantly stop limiting that stat
        //each button modifiers it by 1% with max 100% and min 1%
    }

    public int getLimiterAsPercentage(Player player){
        if(!player.getAttribute(attributeHolder).hasModifier(limiterLocation)) return 100;
        AttributeModifier modifier = player.getAttribute(attributeHolder).getModifier(limiterLocation);
        return (int) (modifier.amount()*100);
    }
}
