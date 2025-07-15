package net.thejadeproject.ascension.guis.easygui.screens;

import net.lucent.easygui.elements.containers.View;
import net.lucent.easygui.elements.controls.buttons.ColorButton;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.ContainerRenderable;
import net.lucent.easygui.interfaces.events.Clickable;
import net.lucent.easygui.screens.EasyGuiScreen;
import net.lucent.easygui.templating.actions.Action;
import net.lucent.easygui.templating.registry.EasyGuiRegistries;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.guis.easygui.ModActions;
import net.thejadeproject.ascension.guis.easygui.elements.CardHolderView;
import net.thejadeproject.ascension.network.serverBound.TriggerGeneratePhysique;

import java.util.Arrays;


public class GeneratePhysiqueScreen extends EasyGuiScreen {
    boolean physiqueGenerated = false;
    public GeneratePhysiqueScreen(Component title) {
        super(title);
        View view = new View(this,0,0);
        addView(view);
        view.setID("main_view");
        view.setUseMinecraftScale(true);
        ColorButton button1 = new ColorButton(this,view.getScaledWidth()/2-50,view.getScaledHeight()/2-15,100,30){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == 0){
                    CardHolderView view = (CardHolderView) screen.getElementByID("card_view");
                    view.path = "Intent";
                    screen.setActiveView(view);
                }
            }
            @Override
            public void recalculatePos(int oldWidth, int oldHeight) {
                setX(getRoot().getScaledWidth()/2-50);
                setY(getRoot().getScaledHeight()/2-15);
            }
            @Override
            public void onResize(int oldWidth, int oldHeight, double oldScale) {
                recalculatePos(0,0);
            }

        };
        button1.setSticky(true);
        button1.addChild(new Label.Builder().centered(true).x(50).y(15).screen(this).text("Intent Physique").build());
        view.addChild(button1);

        ColorButton button2 = new ColorButton(this,view.getScaledWidth()/2-50,view.getScaledHeight()/2-60,100,30){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == 0){
                    CardHolderView view = (CardHolderView) screen.getElementByID("card_view");
                    view.path = "Body";
                    screen.setActiveView(view);
                }
            }

            @Override
            public void recalculatePos(int oldWidth, int oldHeight) {
                setX(getRoot().getScaledWidth()/2-50);
                setY(getRoot().getScaledHeight()/2-60);
            }
            @Override
            public void onResize(int oldWidth, int oldHeight, double oldScale) {
                recalculatePos(0,0);
            }


        };
        button2.setSticky(true);
        button2.addChild(new Label.Builder().centered(true).x(50).y(15).screen(this).text("Body Physique").build());
        view.addChild(button2);

        ColorButton button3 = new ColorButton(this,view.getScaledWidth()/2-50,view.getScaledHeight()/2+30,100,30){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked && button == 0){
                    CardHolderView view = (CardHolderView) screen.getElementByID("card_view");
                    view.path = "Essence";
                    screen.setActiveView(view);
                }
            }

            @Override
            public void recalculatePos(int oldWidth, int oldHeight) {
                setX(getRoot().getScaledWidth()/2-50);
                setY(getRoot().getScaledHeight()/2+45);
            }

            @Override
            public void onResize(int oldWidth, int oldHeight, double oldScale) {
                recalculatePos(0,0);
            }

        };
        button3.setSticky(true);
        button3.addChild(new Label.Builder().centered(true).x(50).y(15).screen(this).text("Essence Physique").build());
        view.addChild(button3);


        CardHolderView view2 = new CardHolderView(this);
        view2.setID("card_view");
        addView(view2);

    }

    @Override
    public boolean shouldCloseOnEsc() {
        if(!physiqueGenerated) return false;
        return super.shouldCloseOnEsc();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);

    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    public void updateGeneratedPhysiques(String generated_physique, String[] other_physique){

        System.out.println("generated physique: "+ generated_physique);
        System.out.println("with extra : " + Arrays.toString(other_physique));
        physiqueGenerated = true;
        ((CardHolderView)getElementByID("card_view")).updateGeneratedPhysiques(generated_physique,other_physique);
    }
}
