package net.thejadeproject.ascension.guis.easygui.overlay_views;

import net.lucent.easygui.elements.containers.View;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.thejadeproject.ascension.guis.easygui.elements.cultivation_progress_overlay.PathProgressBarContainer;

public class CultivationProgressBarsView extends View {


    public CultivationProgressBarsView(IEasyGuiScreen screen,int x, int y){
        super(screen,x,y);
        setUseMinecraftScale(true);

        addChild(new PathProgressBarContainer(screen,getScaledWidth(),getScaledHeight()));

    }
}
