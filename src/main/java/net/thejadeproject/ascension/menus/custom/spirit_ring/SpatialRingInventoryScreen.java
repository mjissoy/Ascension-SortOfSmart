package net.thejadeproject.ascension.menus.custom.spirit_ring;

import net.lucent.easygui.screen.EasyContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.phys.Vec2;
import net.thejadeproject.ascension.refactor_packages.gui.elements.spatial_ring.SpatialRingInventoryElement;
import org.w3c.dom.css.Rect;

public class SpatialRingInventoryScreen extends EasyContainerScreen<SpatialRingInventoryMenu> {
    public SpatialRingInventoryScreen(SpatialRingInventoryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        getUIFrame().setRoot(new SpatialRingInventoryElement(getUIFrame(),getMenu()));

    }

    public Rect2i getUsedArea(){
        Vec2 point = getUIFrame().getRoot().getGlobalPoint();
        return new Rect2i((int) point.x, (int) point.y,getUIFrame().getRoot().getWidth(),getUIFrame().getRoot().getHeight());
    }
}
