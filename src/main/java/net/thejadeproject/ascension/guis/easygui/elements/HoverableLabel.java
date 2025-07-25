package net.thejadeproject.ascension.guis.easygui.elements;

import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.elements.other.SquareRenderable;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.events.Hoverable;
import net.lucent.easygui.templating.actions.Action;
import net.lucent.easygui.util.math.BoundChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.thejadeproject.ascension.progression.dao.IDao;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Optional;

//TODO remove when update jit pack
public class HoverableLabel extends Label implements Hoverable {
    public boolean hovered;
    public IDao dao;

    public void setDao(IDao dao) {

        this.dao = dao;

    }

    private HoverableLabel(IEasyGuiScreen easyGuiScreen, Font font, Component text, int x, int y, int width, int height, boolean centered, int textColor){
        setScreen(easyGuiScreen);
        this.font = font;
        this.text = text;
        setY(y);
        setX(x);
        setWidth(width);
        setHeight(height);
        this.centered = centered;

        this.textColor = textColor;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {

        if(getTransform() == null) return false;
        Vector3f p1 = getTransform().transformPosition(new Vector3f(0,0,0));;
        Vector3f p2 = getTransform().transformPosition(new Vector3f(getWidth(),0,0));
        Vector3f p3 = getTransform().transformPosition(new Vector3f(getWidth(),getHeight(),0));
        Vector3f p4 = getTransform().transformPosition(new Vector3f(0,getHeight(),0));
        BoundChecker.Vec2 mousePos = new BoundChecker.Vec2((int)mouseX,(int)mouseY);
        if(getParent() != null && getParent().getActiveCullRegion() != null){

            BoundChecker.Rec2d rec = getParent().getActiveCullRegion();
            if(!BoundChecker.containsPoint(getParent().getActiveCullRegion(),mousePos)) return false;
        }
        return BoundChecker.containsPoint(new BoundChecker.Vec2(p1),new BoundChecker.Vec2(p2),new BoundChecker.Vec2(p3),new BoundChecker.Vec2(p4),mousePos);

    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);

        if(hovered){
            BoundChecker.Vec2 point = screenToLocalPoint(mouseX,mouseY);
            System.out.println(dao);
            guiGraphics.renderTooltip(Minecraft.getInstance().font,dao.getDescription(), Optional.empty(),point.x,point.y);
        }
    }

    @Override
    public void onMouseOver(boolean state) {
        Hoverable.super.onMouseOver(state);
        hovered = state;
    }
    public static class Builder{

        private IEasyGuiScreen easyGuiScreen = null;

        private  int textColor = -16777216;
        private  boolean centered = false;
        private  Integer width;
        private  Integer height;
        private Component text = Component.literal("");
        private Font font = Minecraft.getInstance().font;
        private  boolean cull = false;
        private  boolean useCustomScaling = false;
        private  double customScaling = 1;
        private  int x = 0;
        private  int y = 0;
        private IDao dao;
        public Builder screen(IEasyGuiScreen easyGuiScreen){this.easyGuiScreen = easyGuiScreen; return this;}
        public Builder textColor(int color){textColor = color; return  this;}
        public Builder textColor(Color color){textColor = color.getRGB(); return  this;}
        public Builder centered(boolean centered){this.centered = centered; return  this;}
        public Builder width(int width){this.width = width; return this;}
        public Builder height(int height){this.height = height; return this;}
        public Builder text(Component component){this.text = component; return this;}
        public Builder text(String text){this.text = Component.literal(text); return this;}
        public Builder translatableText(String key){this.text = Component.translatable(key); return this;}
        public Builder font(Font font){this.font = font; return this;}
        public Builder cull(boolean cull){this.cull = cull;return this;}
        public Builder dao(IDao dao){this.dao = dao;return this;}
        public Builder customScaling(double scale){
            useCustomScaling = true;
            customScaling = scale;return this;}
        public Builder x(int x){this.x = x;return this;}
        public Builder y(int y){this.y = y;return this;}

        public HoverableLabel build(){

            if(easyGuiScreen == null){
                throw new IllegalArgumentException("Label must have a valid screen");
            }

            if(width == null){
                width = font.width(text);
            }
            if(height == null){
                height = font.lineHeight;
            }

            HoverableLabel label = new HoverableLabel(easyGuiScreen,font,text,x,y,width,height,centered,textColor);
            label.useCustomScaling = useCustomScaling;
            label.setCustomScale(customScaling);
            label.setCull(cull);
            label.setDao(dao);
            return label;

        }



    }
}
