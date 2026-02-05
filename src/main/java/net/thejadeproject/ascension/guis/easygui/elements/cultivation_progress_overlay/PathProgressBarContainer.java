package net.thejadeproject.ascension.guis.easygui.elements.cultivation_progress_overlay;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.complex_events.Sticky;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.common.NeoForge;
import net.thejadeproject.ascension.events.custom.client.PlayerCultivationChanged;
import net.thejadeproject.ascension.events.custom.cultivation.CultivateEvent;
import net.thejadeproject.ascension.registries.AscensionRegistries;

import java.awt.desktop.SystemSleepEvent;

public class PathProgressBarContainer extends EmptyContainer implements Sticky {

    public PathProgressBarContainer(IEasyGuiScreen screen,int scaledWidth,int scaledHeight){
        super(screen,scaledWidth-(80),scaledHeight-(21+2)*3,67,(21+2)* AscensionRegistries.Paths.PATHS_REGISTRY.size());
        for (int i = 0; i<AscensionRegistries.Paths.PATHS_REGISTRY.size();i++){
            addChild(new PathProgressBar(screen,0,(21+2)*i));
        }

        NeoForge.EVENT_BUS.addListener(this::onPlayerCultivate);
    }


    public void tryDisplayBar(String path){
        if(getChildren().isEmpty()) return;
        PathProgressBar selectedBar = null;

        for(int i = AscensionRegistries.Paths.PATHS_REGISTRY.size()-1;i>=0;i--){
            if(((PathProgressBar) getChildren().get(i)).getCurrentPath() == null){
                if(selectedBar == null)selectedBar = (PathProgressBar) getChildren().get(i);
            }
            else if(((PathProgressBar) getChildren().get(i)).getCurrentPath().equals(path)) return;

        }
        if(selectedBar != null) selectedBar.setCurrentPath(path);

    }

    public void onPlayerCultivate(PlayerCultivationChanged event){
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
