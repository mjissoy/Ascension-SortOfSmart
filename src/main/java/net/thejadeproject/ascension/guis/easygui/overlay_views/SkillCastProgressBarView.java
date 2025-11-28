package net.thejadeproject.ascension.guis.easygui.overlay_views;

import net.lucent.easygui.elements.containers.View;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.thejadeproject.ascension.guis.easygui.elements.skill_cast_progress.SkillCastProgressContainer;

public class SkillCastProgressBarView extends View {

    public SkillCastProgressBarView(IEasyGuiScreen screen){
        super(screen,0,0);
        
        setUseMinecraftScale(true);
        addChild(new SkillCastProgressContainer(screen,getScaledWidth()/2-40,getScaledHeight()));
    }

}
