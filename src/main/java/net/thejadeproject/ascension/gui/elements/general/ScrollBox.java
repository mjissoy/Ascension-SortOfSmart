package net.thejadeproject.ascension.gui.elements.general;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.events.EasyEvents;
import net.lucent.easygui.gui.events.type.EasyEvent;
import net.lucent.easygui.gui.events.type.EasyMouseEvent;
import net.minecraft.client.gui.GuiGraphics;

public class ScrollBox extends RenderableElement {

    int yOffset = 0;
    int scrollRate = 0;
    public ScrollBox(UIFrame frame,int scrollRate) {
        super(frame);
        this.scrollRate =scrollRate;
        setShouldCull(true);
        addEventListener(EasyEvents.MOUSE_SCROLL_EVENT,this::onMouseScroll);
    }
    //cancel event after use so higher elements don't scroll
    public void onMouseScroll(EasyEvent event){
        if(event.isCanceled()) return;
        if(!(event instanceof EasyMouseEvent mouseEvent)) return;
        if(mouseEvent.getDeltaY() == 0) return;
        scroll(mouseEvent.getDeltaY() < 0 ? -1 : 1);
        event.setCanceled(true);
    }

    /**
     * uses the max child y+height. + yOffset, since a yOffset is the same as y-10
     * @return the max scrollOffset
     */
    public int getMaxYScroll(){
        int maxY = 0;
        for(RenderableElement element : getChildren()){
            int y = element.getPositioning().getRawY() + element.getHeight() - yOffset;
            if(y > maxY) maxY = y;
        }
        return maxY;
    }

    @Override
    public void addChild(RenderableElement element) {
        if (!getChildren().isEmpty()) {
            RenderableElement lastChild = getChildren().getLast();

            element.getPositioning().setFromRawX(2);
            element.getPositioning().setFromRawY(lastChild.getPositioning().getRawY() + lastChild.getHeight() + 2);
        } else {
            element.getPositioning().setFromRawX(2);
            element.getPositioning().setFromRawY(0);
        }

        super.addChild(element);
        updateVisibility(element);
    }


    public void updateVisibility(RenderableElement element){

        //TODO can simplify into one boolean expression
        if(element.getPositioning().getY()+element.getHeight() <0) element.setVisible(false);
        else if(element.getPositioning().getY() > getHeight()) element.setVisible(false);
        else if(element.getPositioning().getX()+element.getWidth() < 0) element.setVisible(false);
        else if (element.getPositioning().getX()>getWidth()) element.setVisible(false);
        else element.setVisible(true);
    }
    public void updateChildrenY(int change){
        for (RenderableElement element: getChildren()){
            element.getPositioning().setY(element.getPositioning().getY()+change);
            updateVisibility(element);
        }

    }

    /**
     *  directly modifies the position of child elements. this is so "global" position calculations can be accurate
     *  -scroll = +y
     *  +scroll = -y
     * @param amount amount*scrollRate = pixels scrolled
     */
    public void scroll(int amount){
        int change = Math.abs(amount)*scrollRate;
        int oldYOffset=yOffset;
        if(amount < 0) {
            //increase yOffset
            yOffset = Math.min(yOffset+change,getMaxYScroll());
        }else {
            //decrease y offset
            yOffset = Math.max(0,yOffset-change);
        }

        //if yOffset increase we want to sub diff, if it goes down we add diff
        updateChildrenY(oldYOffset-yOffset);
    }

    @Override
    public void renderTick(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderTick(guiGraphics, mouseX, mouseY, partialTick);

    }

    public void clearScrollChildren() {
        getChildren().clear();
        yOffset = 0;
    }

}
