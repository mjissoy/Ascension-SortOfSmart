package net.thejadeproject.ascension.guis.easygui.elements;

import net.lucent.easygui.elements.containers.View;
import net.lucent.easygui.elements.controls.buttons.ColorButton;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.ContainerRenderable;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.network.serverBound.TriggerGeneratePhysique;
import net.thejadeproject.ascension.registries.AscensionRegistries;

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
        CardButton card1 = new CardButton(screen,getScaledWidth()/2-120,getScaledHeight()/2-50,40,100){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == 0 && !((CardHolderView) getRoot()).physiqueGenerated){
                    if(isFocused()){
                        setFocused(false);
                        ((CardHolderView) getRoot()).setSelected(null);
                        screen.getElementByID("confirmBtn").setVisible(false);
                    }else{
                        setFocused(true);
                        if(((CardHolderView) getRoot()).selected != null) ((CardHolderView) getRoot()).selected.setFocused(false);
                        ((CardHolderView) getRoot()).setSelected(this);
                        screen.getElementByID("confirmBtn").setVisible(true);
                    }
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
        CardButton card2 = new CardButton(screen,getScaledWidth()/2-70,getScaledHeight()/2-50,40,100){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == 0 && !((CardHolderView) getRoot()).physiqueGenerated){
                    if(isFocused()){
                        setFocused(false);
                        ((CardHolderView) getRoot()).setSelected(null);
                        screen.getElementByID("confirmBtn").setVisible(false);
                    }else{
                        setFocused(true);
                        if(((CardHolderView) getRoot()).selected != null) ((CardHolderView) getRoot()).selected.setFocused(false);
                        ((CardHolderView) getRoot()).setSelected(this);
                        screen.getElementByID("confirmBtn").setVisible(true);
                    }
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
        CardButton card3 = new CardButton(screen,getScaledWidth()/2-20,getScaledHeight()/2-50,40,100){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == 0 && !((CardHolderView) getRoot()).physiqueGenerated){
                    if(isFocused()){
                        setFocused(false);
                        ((CardHolderView) getRoot()).setSelected(null);
                        screen.getElementByID("confirmBtn").setVisible(false);
                    }else{
                        setFocused(true);
                        if(((CardHolderView) getRoot()).selected != null) ((CardHolderView) getRoot()).selected.setFocused(false);
                        ((CardHolderView) getRoot()).setSelected(this);
                        screen.getElementByID("confirmBtn").setVisible(true);
                    }
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
        CardButton card4 = new CardButton(screen,getScaledWidth()/2+30,getScaledHeight()/2-50,40,100){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == 0 && !((CardHolderView) getRoot()).physiqueGenerated){
                    if(isFocused()){
                        setFocused(false);
                        ((CardHolderView) getRoot()).setSelected(null);
                        screen.getElementByID("confirmBtn").setVisible(false);
                    }else{
                        setFocused(true);
                        if(((CardHolderView) getRoot()).selected != null) ((CardHolderView) getRoot()).selected.setFocused(false);
                        ((CardHolderView) getRoot()).setSelected(this);
                        screen.getElementByID("confirmBtn").setVisible(true);
                    }
                }
            }
            @Override
            public void recalculatePos(int oldWidth, int oldHeight) {
                setX(getRoot().getScaledWidth()/2+30);
                setY(getRoot().getScaledHeight()/2-50);
            }
            public void onResize(int oldWidth, int oldHeight, double oldScale) {
                recalculatePos(0,0);
            }

        };
        card4.setSticky(true);
        addChild(card4);
        CardButton card5 = new CardButton(screen,getScaledWidth()/2+80,getScaledHeight()/2-50,40,100){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == 0 && !((CardHolderView) getRoot()).physiqueGenerated){
                    if(isFocused()){
                        setFocused(false);
                        ((CardHolderView) getRoot()).setSelected(null);
                        screen.getElementByID("confirmBtn").setVisible(false);
                    }else{
                        setFocused(true);
                        if(((CardHolderView) getRoot()).selected != null) ((CardHolderView) getRoot()).selected.setFocused(false);
                        ((CardHolderView) getRoot()).setSelected(this);

                        screen.getElementByID("confirmBtn").setVisible(true);
                    }
                }
            }

            @Override
            public void recalculatePos(int oldWidth, int oldHeight) {
                setX(getRoot().getScaledWidth()/2+80);
                setY(getRoot().getScaledHeight()/2-50);
            }
            public void onResize(int oldWidth, int oldHeight, double oldScale) {
                recalculatePos(0,0);
            }

        };
        card5.setSticky(true);
        addChild(card5);

        ColorButton confirmButton = new ColorButton(screen, getScaledWidth()/2-100,getScaledHeight()/2+100,200,20){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == 0 && !((CardHolderView) getRoot()).physiqueGenerated){
                    ((CardHolderView) getRoot()).generatePhysique();
                    this.visible = false;
                }
            }

            @Override
            public void recalculatePos(int oldWidth, int oldHeight) {
                setX(getRoot().getScaledWidth()/2-100);
                setY(getRoot().getScaledHeight()/2+100);
            }
            public void onResize(int oldWidth, int oldHeight, double oldScale) {
                recalculatePos(0,0);
            }
        };
        confirmButton.addChild(new Label.Builder().screen(screen).text("CONFIRM").centered(true).x(100).y(10).build());
        confirmButton.setID("confirmBtn");
        confirmButton.visible = false;
        confirmButton.setSticky(true);
        addChild(confirmButton);


    }
    public void updateGeneratedPhysiques(String generatedPhysique, String[] otherPhysiques){
        //TODO
        System.out.println("generated physique: "+ generatedPhysique);
        System.out.println("with extra : " + Arrays.toString(otherPhysiques));
        List<String> otherOptions = new ArrayList<>(Arrays.stream(otherPhysiques).toList());
        for(ContainerRenderable renderable:getChildren()){
            if(selected == renderable){
                ((CardButton) renderable).cardPhysique = generatedPhysique;
                renderable.addChild(new Label.Builder().screen(screen).centered(true).width(40).x(20).y(50)
                        .text(AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(
                                ResourceLocation.fromNamespaceAndPath(generatedPhysique.split(":")[0],generatedPhysique.split(":")[1])
                        ).getDisplayTitle()).build());
            }else{
                if(otherOptions.isEmpty()) continue;
                String physique = otherOptions.removeFirst();
                ((CardButton) renderable).cardPhysique = physique;
                renderable.addChild(new Label.Builder().screen(screen).centered(true).width(40).x(20).y(50).text(AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(
                        ResourceLocation.fromNamespaceAndPath(physique.split(":")[0],physique.split(":")[1])
                ).getDisplayTitle()).build());

            }
        }
    }
    public void setSelected(ContainerRenderable renderable){
        selected = renderable;
    }
    public void generatePhysique(){

        physiqueGenerated = true;
        PacketDistributor.sendToServer(new TriggerGeneratePhysique(path,4));

    }
}
