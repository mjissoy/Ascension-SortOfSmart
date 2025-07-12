package net.thejadeproject.ascension.guis.easygui.elements;

import net.lucent.easygui.elements.containers.View;
import net.lucent.easygui.elements.controls.buttons.ColorButton;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.ContainerRenderable;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.network.serverBound.TriggerGeneratePhysique;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardHolderView extends View {
    public String path;
    public boolean physiqueGenerated = false;
    public ContainerRenderable selected;
    public CardHolderView(IEasyGuiScreen screen){
        super(screen);
        setUseMinecraftScale(true);
        ColorButton card1 = new ColorButton(screen,getScaledWidth()/2-120,getScaledHeight()/2-50,40,100){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == 0 && !((CardHolderView) getRoot()).physiqueGenerated){
                    ((CardHolderView) getRoot()).generatePhysique(this);
                }
            }
            @Override
            public void recalculatePos(int oldWidth, int oldHeight) {
                setX(getRoot().getScaledWidth()/2-120);
                setY(getRoot().getScaledHeight()/2-50);
            }
            public void onResize(int oldWidth, int oldHeight, double oldScale) {
                recalculatePos(0,0);
            }

        };
        card1.setSticky(true);
        addChild(card1);
        ColorButton card2 = new ColorButton(screen,getScaledWidth()/2-70,getScaledHeight()/2-50,40,100){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == 0 && !((CardHolderView) getRoot()).physiqueGenerated){
                    ((CardHolderView) getRoot()).generatePhysique(this);
                }
            }
            @Override
            public void recalculatePos(int oldWidth, int oldHeight) {
                setX(getRoot().getScaledWidth()/2-70);
                setY(getRoot().getScaledHeight()/2-50);
            }
            public void onResize(int oldWidth, int oldHeight, double oldScale) {
                recalculatePos(0,0);
            }

        };
        card2.setSticky(true);
        addChild(card2);
        ColorButton card3 = new ColorButton(screen,getScaledWidth()/2-20,getScaledHeight()/2-50,40,100){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == 0 && !((CardHolderView) getRoot()).physiqueGenerated){
                    ((CardHolderView) getRoot()).generatePhysique(this);
                }
            }
            @Override
            public void recalculatePos(int oldWidth, int oldHeight) {
                setX(getRoot().getScaledWidth()/2-20);
                setY(getRoot().getScaledHeight()/2-50);
            }
            public void onResize(int oldWidth, int oldHeight, double oldScale) {
                recalculatePos(0,0);
            }

        };
        card3.setSticky(true);
        addChild(card3);
        ColorButton card4 = new ColorButton(screen,getScaledWidth()/2+50,getScaledHeight()/2-50,40,100){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == 0 && !((CardHolderView) getRoot()).physiqueGenerated){
                    ((CardHolderView) getRoot()).generatePhysique(this);
                }
            }
            @Override
            public void recalculatePos(int oldWidth, int oldHeight) {
                setX(getRoot().getScaledWidth()/2+40);
                setY(getRoot().getScaledHeight()/2-50);
            }
            public void onResize(int oldWidth, int oldHeight, double oldScale) {
                recalculatePos(0,0);
            }

        };
        card4.setSticky(true);
        addChild(card4);
        ColorButton card5 = new ColorButton(screen,getScaledWidth()/2+100,getScaledHeight()/2-50,40,100){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == 0 && !((CardHolderView) getRoot()).physiqueGenerated){
                    ((CardHolderView) getRoot()).generatePhysique(this);
                }
            }

            @Override
            public void recalculatePos(int oldWidth, int oldHeight) {
                setX(getRoot().getScaledWidth()/2+90);
                setY(getRoot().getScaledHeight()/2-50);
            }
            public void onResize(int oldWidth, int oldHeight, double oldScale) {
                recalculatePos(0,0);
            }

        };
        card5.setSticky(true);
        addChild(card5);
    }
    public void updateGeneratedPhysiques(String generatedPhysique, String[] otherPhysiques){
        //TODO
        System.out.println("generated physique: "+ generatedPhysique);
        System.out.println("with extra : " + Arrays.toString(otherPhysiques));
        List<String> otherOptions = new ArrayList<>(Arrays.stream(otherPhysiques).toList());
        for(ContainerRenderable renderable:getChildren()){
            if(selected == renderable){
                ((ColorButton) renderable).setFocused(true);
                renderable.addChild(new Label.Builder().screen(screen).centered(true).width(40).x(20).y(50).text(generatedPhysique).build());
            }else{
                if(otherOptions.isEmpty()) continue;
                renderable.addChild(new Label.Builder().screen(screen).centered(true).width(40).x(20).y(50).text(otherOptions.removeFirst()).build());

            }
        }
    }
    public void generatePhysique(ContainerRenderable renderable){
        selected = renderable;
        physiqueGenerated = true;
        PacketDistributor.sendToServer(new TriggerGeneratePhysique(path,4));

    }
}
