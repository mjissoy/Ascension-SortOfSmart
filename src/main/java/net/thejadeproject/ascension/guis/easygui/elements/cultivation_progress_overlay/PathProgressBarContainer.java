package net.thejadeproject.ascension.guis.easygui.elements.cultivation_progress_overlay;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.complex_events.Sticky;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.common.NeoForge;
import net.thejadeproject.ascension.events.custom.cultivation.CultivateEvent;

public class PathProgressBarContainer extends EmptyContainer implements Sticky {

    public PathProgressBarContainer(IEasyGuiScreen screen,int scaledWidth,int scaledHeight){
        super(screen,scaledWidth-(80),scaledHeight-(21+2)*3,67,(21+2)*3);

        PathProgressBar bar1 = new PathProgressBar(screen,0,0);
        PathProgressBar bar2 = new PathProgressBar(screen,0,21+2);
        PathProgressBar bar3 = new PathProgressBar(screen,0,42+2);

        addChild(bar1);

        addChild(bar2);

        addChild(bar3);
        NeoForge.EVENT_BUS.addListener(this::onPlayerCultivate);
    }

    public void tryDisplayBar(String path){
        PathProgressBar selectedBar = null;
        for(int i = 2;i>=0;i--){
            if(((PathProgressBar) getChildren().get(i)).getCurrentPath() == null){
                if(selectedBar == null) selectedBar = (PathProgressBar) getChildren().get(i);
            }else if(((PathProgressBar) getChildren().get(i)).getCurrentPath().equals(path)) return;

        }
        if(selectedBar != null) selectedBar.setCurrentPath(path);

    }

    public void onPlayerCultivate(CultivateEvent event){
        if(event.player.equals(Minecraft.getInstance().player)){
            //same player
            tryDisplayBar(event.path);
        }
    }

    @Override
    public void recalculatePos(int oldWidth, int oldHeight) {
        setX(getParent().getScaledWidth()-80);
        setY(getParent().getScaledHeight()-(21+2)*3);
    }

    @Override
    public boolean isSticky() {
        return true;
    }
}
