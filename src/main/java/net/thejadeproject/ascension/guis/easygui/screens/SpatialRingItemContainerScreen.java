package net.thejadeproject.ascension.guis.easygui.screens;

import net.lucent.easygui.elements.containers.View;
import net.lucent.easygui.screens.EasyGuiContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.thejadeproject.ascension.guis.easygui.elements.spiritual_ring.item_screen.SpiritualRingItemContainer;
import net.thejadeproject.ascension.guis.menu.SpatialRingItemContainerMenu;

public class SpatialRingItemContainerScreen extends EasyGuiContainerScreen<SpatialRingItemContainerMenu> {
    public SpatialRingItemContainerScreen(SpatialRingItemContainerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        View view = new View(this);
        addView(view);
        view.setUseMinecraftScale(true);
        view.addChild(new SpiritualRingItemContainer(this,0,0));
    }
}
