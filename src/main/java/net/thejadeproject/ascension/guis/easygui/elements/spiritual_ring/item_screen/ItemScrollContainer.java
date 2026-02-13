package net.thejadeproject.ascension.guis.easygui.elements.spiritual_ring.item_screen;

import net.lucent.easygui.elements.BaseRenderable;
import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.interfaces.ContainerRenderable;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.events.MouseScrollListener;
import net.minecraft.client.gui.GuiGraphics;
import net.thejadeproject.ascension.guis.easygui.screens.SpatialRingItemContainerScreen;
import net.thejadeproject.ascension.guis.menu.SpatialRingItemContainerMenu;

public class ItemScrollContainer extends EmptyContainer implements MouseScrollListener {
    private int slotOffset;
    public ItemScrollContainer(IEasyGuiScreen screen,int x, int y, int height){
        super(screen,x,y,18*9,height);

    }
    public int getTotalOverflowRows(){
        SpatialRingItemContainerMenu menu = ((SpatialRingItemContainerScreen) getScreen()).getMenu();
        return Math.max(menu.getTotalRows()-menu.getVisibleRows(),0);
    }
    public int getVisibleRows(){
        SpatialRingItemContainerMenu menu = ((SpatialRingItemContainerScreen) getScreen()).getMenu();
        return Math.min(menu.getTotalRows(),menu.getVisibleRows());
    }
    @Override
    public void onMouseScroll(double mouseX, double mouseY, double scrollX, double scrollY) {
        //negative is up + is down
        int change = (int) Math.signum(scrollY)*-1;
        int oldOffset = slotOffset;
        slotOffset = (int) Math.clamp(slotOffset+Math.signum(scrollY)*-1,0,getTotalOverflowRows());
        if(oldOffset != slotOffset){
            updateYPos(change);
        }
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);

    }

    public void updateChildVisibility(){
        int yOffset = -slotOffset*18;

        for(int i= 0;i<getChildren().size(); i++) {
            ContainerRenderable renderable = getChildren().get(i);
            renderable.setVisible(!(renderable.getY() + renderable.getHeight() < 0 || renderable.getY() >= getVisibleRows() * 18));

            if (Math.floor(i / 9) > 5) {

            }

        }
    }
    public void updateYPos(int direction){
        for(ContainerRenderable renderable : getChildren()){
            renderable.setY(renderable.getY()-direction*18);
        }
    }
    @Override
    public void renderChildren(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();
        //guiGraphics.pose().translate(0,-slotOffset*18,0);
        //updateChildVisibility();
        super.renderChildren(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.pose().popPose();
    }
}
