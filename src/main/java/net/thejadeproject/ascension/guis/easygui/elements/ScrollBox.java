package net.thejadeproject.ascension.guis.easygui.elements;

import com.mojang.blaze3d.platform.InputConstants;
import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.interfaces.ContainerRenderable;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.events.*;
import net.lucent.easygui.util.math.BoundChecker;
import net.minecraft.client.gui.GuiGraphics;

public abstract class ScrollBox extends EmptyContainer implements MouseScrollListener, Clickable, MouseReleaseListener, MouseMovedListener {

    int yOffset =0;
    boolean dragging = false;

    public int getTotalHeight(){
        int maxY = 0;
        for(ContainerRenderable child: getChildren()){
            int childYAffect = (int) (child.getY()+child.getHeight()*child.getCustomScale());
            if(childYAffect > maxY){
                maxY = childYAffect;
            }
        }
        return Math.max(maxY,getHeight());
    }

    public int getMaxYOffset(){
        return getTotalHeight()-getHeight();
    }

    public int getInnerScrollBarHeight(){
        return (int) Math.max((((double) getHeight())/getTotalHeight())*getScrollBarHeight(),2);
    }

    public int transformRelativeYToOffset(int y){
        int barOffset =  Math.clamp(y,0,getScrollBarHeight()- getInnerScrollBarHeight());
        return getTotalHeight()*(barOffset/getScrollBarHeight());
    }

    public ScrollBox(IEasyGuiScreen screen, int x, int y, int width,int height){
        super(screen,x,y,width,height);
    }
    public void modifyScrollOffset(int amount){
        yOffset = Math.clamp(yOffset+amount,0,getMaxYOffset());
    }
    public int getInnerScrollBarRelativeY(){

        return (int) Math.min(  (((double) yOffset/getMaxYOffset()))*(getScrollBarHeight()-getInnerScrollBarHeight()),getScrollBarHeight()-getInnerScrollBarHeight());
    }

    public abstract int getScrollBarHeight();
    public abstract int getScrollBarStartX();
    public abstract int getScrollBarStartY();
    public abstract int getScrollBarWidth();
    public abstract int getScrollRate();
    //================= SCROLL CONTROL ==================
    @Override
    public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
        if(clicked && InputConstants.MOUSE_BUTTON_LEFT == button){
            //HANDLE
            //TODO debug
            BoundChecker.Vec2 point =  screenToLocalPoint(mouseX,mouseY);
            if((getScrollBarStartX() < point.x && point.x< getScrollBarStartX()+getScrollBarWidth())
                && (getScrollBarStartY() < point.y && point.y < getScrollBarStartY()+getScrollBarHeight())
            ){
                yOffset = transformRelativeYToOffset(point.y-getScrollBarStartY());
                dragging = true;
            }
        }
    }


    @Override
    public void onMouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
    }

    @Override
    public void renderChildren(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0,-yOffset,0);
        guiGraphics.enableScissor(getGlobalPoint().x,getGlobalPoint().y, (int) (getGlobalPoint().x+getScaledWidth()), (int) (getGlobalPoint().y+getScaledHeight()));
        super.renderChildren(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.disableScissor();
        guiGraphics.pose().popPose();
    }

    @Override
    public void onMouseScroll(double mouseX, double mouseY, double scrollX, double scrollY) {
        modifyScrollOffset((int) (Math.signum(scrollY)*getScrollRate())*-1);

    }

    @Override
    public void onMouseMoved(double mouseX, double mouseY) {
        if(dragging){
            BoundChecker.Vec2 point =  screenToLocalPoint(mouseX,mouseY);
            if((getScrollBarStartY() < point.y && point.y < getScrollBarStartY()+getScrollBarHeight())){
                yOffset = transformRelativeYToOffset(point.y-getScrollBarStartY());
                dragging = true;
            }
        }
    }
}
