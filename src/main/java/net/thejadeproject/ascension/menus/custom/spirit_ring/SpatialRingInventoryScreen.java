package net.thejadeproject.ascension.menus.custom.spirit_ring;

import net.lucent.easygui.screen.EasyContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.thejadeproject.ascension.refactor_packages.gui.elements.spirit_ring.SpatialRingInventoryElement;

public class SpatialRingInventoryScreen extends EasyContainerScreen<SpatialRingInventoryMenu> {
    public SpatialRingInventoryScreen(SpatialRingInventoryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        getUIFrame().setRoot(new SpatialRingInventoryElement(getUIFrame(),getMenu()));

    }
}
