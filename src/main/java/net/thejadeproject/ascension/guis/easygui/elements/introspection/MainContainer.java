package net.thejadeproject.ascension.guis.easygui.elements.introspection;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.other.Image;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.properties.Positioning;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.registries.AscensionRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainContainer extends EmptyContainer {
    private TextureDataSubSection background = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
            356,256,
            0,6,142,143
    );
    public HashMap<ResourceLocation, ITextureData> pathTextureData = new HashMap<>(){{
        put(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"essence"),new TextureDataSubSection(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
                356,256,
                0,176,73,187
        ));
        put(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"intent"),new TextureDataSubSection(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
                356,256,
                0,189,73,200
        ));
        put(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"body"),
                new TextureDataSubSection(
                        ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/screen_all.png"),
                        356,256,
                        0,163,73,174
                ));
    }};
    private final Image titleBar;
    public final List<ResourceLocation> paths = new ArrayList<>();
    private int selectedPath = 0;

    public void changeSelectedPath(int increment){
        selectedPath += increment;
        if(selectedPath <0) selectedPath = paths.size()+selectedPath;
        selectedPath = Math.abs(selectedPath) % paths.size();

        titleBar.setTextureData(pathTextureData.get(paths.get(selectedPath)));
    }
    public ResourceLocation getSelectedPath(){
        return paths.get(selectedPath);
    }

    protected void initPaths(){
        paths.addAll(AscensionRegistries.Paths.PATHS_REGISTRY.keySet());
    }

    public MainContainer(IEasyGuiScreen screen){
        super(screen,0,0,142,137);
        this.setXPositioning(Positioning.CENTER);
        this.setYPositioning(Positioning.CENTER);
        setX(-(142/2));
        setY(-(157/2));
        Image bgElement = new Image(screen,background,0,0);
        addChild(bgElement);
        initPaths();
        // x = 34, y = -6
        Image titleBar = new Image(screen,pathTextureData.get(getSelectedPath()),34,-6);
        this.titleBar = titleBar;
        addChild(titleBar);
        MainContainerEmptyButton leftButton = new MainContainerEmptyButton(screen,10,10,7,7,this){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked){
                    container.changeSelectedPath(-1);
                }
            }
        };
        MainContainerEmptyButton rightButton = new MainContainerEmptyButton(screen,125,10,7,7,this){
            @Override
            public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
                super.onClick(mouseX, mouseY, button, clicked);
                if(clicked){
                    container.changeSelectedPath(1);
                }
            }
        };
        addChild(leftButton);
        addChild(rightButton);

        PathDataContainer pathDataContainer = new PathDataContainer(screen,3,37,136,97,this);
        addChild(pathDataContainer);

        RealmProgressContainer realmProgressContainer = new RealmProgressContainer(screen,3,3,136,32,this);
        addChild(realmProgressContainer);

    }
    public void createTechniqueContainer(){//TODO
        }
    public void startBreakthrough(){//TODO
         }
}
