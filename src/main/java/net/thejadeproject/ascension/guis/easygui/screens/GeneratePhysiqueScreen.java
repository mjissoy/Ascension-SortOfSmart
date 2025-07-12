package net.thejadeproject.ascension.guis.easygui.screens;

import net.lucent.easygui.elements.containers.View;
import net.lucent.easygui.screens.EasyGuiScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.network.serverBound.TriggerGeneratePhysique;

import java.util.Arrays;


public class GeneratePhysiqueScreen extends EasyGuiScreen {
    public boolean hasSent = false;
    public GeneratePhysiqueScreen(Component title) {
        super(title);
        View view = new View(this,0,0);
        addView(view);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if(!hasSent){
            PacketDistributor.sendToServer(new TriggerGeneratePhysique("Intent",4));
            hasSent = true;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    public void updateGeneratedPhysiques(String generated_physique, String[] other_physique){
        //TODO
        System.out.println("generated physique: "+ generated_physique);
        System.out.println("with extra : " + Arrays.toString(other_physique));
    }
}
