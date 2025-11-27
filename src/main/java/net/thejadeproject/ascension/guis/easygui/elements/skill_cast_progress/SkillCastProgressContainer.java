package net.thejadeproject.ascension.guis.easygui.elements.skill_cast_progress;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.complex_events.Sticky;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.thejadeproject.ascension.util.ModAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SkillCastProgressContainer extends EmptyContainer implements Sticky {


    public SkillCastProgressContainer(IEasyGuiScreen screen,int x,int scaledHeight){
        super(screen,x,0,80,100);

        setY(scaledHeight-40-20-100);
        System.out.println("Creating :"+((RangedAttribute)ModAttributes.MAX_CASTING_INSTANCES.value()).getMaxValue() +" bars");
        for(int i = 0;i< ((RangedAttribute)ModAttributes.MAX_CASTING_INSTANCES.value()).getMaxValue();i++){
            addChild(new SkillCastProgressBar(screen,0,100-i*15,i));
        }

    }

    @Override
    public boolean isSticky() {
        return true;
    }

    @Override
    public void recalculatePos(int oldWidth, int oldHeight) {

        setX((getRoot()).getScaledWidth()/2-40);
        setY((getRoot()).getScaledHeight()-40-20-100);
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

//guiGraphics.fill(0,0,100,100,-16777216);
    }
}
