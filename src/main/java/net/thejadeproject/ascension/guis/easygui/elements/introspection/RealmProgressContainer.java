package net.thejadeproject.ascension.guis.easygui.elements.introspection;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.progression.techniques.ITechnique;
import net.thejadeproject.ascension.registries.AscensionRegistries;

public class RealmProgressContainer extends EmptyContainer {
    public final MainContainer container;
    private final PlayerData playerData;
    private final BreakthroughButton breakthroughButton;
    public RealmProgressContainer(IEasyGuiScreen screen,int x,int y,int width,int height,MainContainer container){
        super(screen,x,y,width,height);
        this.container = container;
        this.playerData = Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA);
        //TODO add minor realm progress bar
        ProgressBar progressBar = new ProgressBar(screen,36,7,container);
        addChild(progressBar);

        BreakthroughButton breakthroughButton = new BreakthroughButton(screen,29,20,container){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked){
                    container.startBreakthrough();
                }
            }
        };
        breakthroughButton.setActive(false);
        this.breakthroughButton = breakthroughButton;
        addChild(breakthroughButton);
    }



    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        //if(Minecraft.getInstance().player == null) return;
        //handle breakthrough button
        CultivationData.PathData pathData = playerData.getCultivationData().getPathData(container.getSelectedPath().toString());
        if(pathData.technique.equals("ascension:none")){
            breakthroughButton.setActive(false);
        }else {
            ITechnique technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(pathData.technique, ':'));

            breakthroughButton.setActive(technique.canBreakthrough(Minecraft.getInstance().player,pathData.majorRealm, pathData.minorRealm, pathData.pathProgress));
        }

    }
}
