package net.thejadeproject.ascension.guis.easygui.elements.main_menu.path_data;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.other.ClickableLabel;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.templating.actions.Action;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.cultivation.player.PlayerData;
import net.thejadeproject.ascension.guis.easygui.ModActions;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttachments;

public class DisplayPathDataContainer extends EmptyContainer {

    public PlayerData.PathData pathData;
    public DisplayPathDataContainer(IEasyGuiScreen easyGuiScreen, int x, int y, int width, int height,String pathId) {
        super(easyGuiScreen, x, y, width, height);
        setID("path_data_container");
        pathData = Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA).getPathData(pathId);
        String name = "Essence Path";
        if(pathData.pathId.equals("ascension:body")) name = "Body Path";
        if(pathData.pathId.equals("ascension:intent")) name = "Intent Path";


        Label PathTitle = (new Label.Builder()).screen(easyGuiScreen).x(getWidth()/2).y(10).centered(true).text(Component.literal(name).withStyle(ChatFormatting.BOLD)).customScaling(0.5).build();
        PathTitle.setID("path_title");

        Label majorRealmTitle = (new Label.Builder()).screen(easyGuiScreen).x(0).y(15).text(Component.literal("Major Realm:").withStyle(ChatFormatting.BOLD)).customScaling(0.5).build();
        Label majorRealmData = (new Label.Builder()).screen(easyGuiScreen).x(0).y(20).text(Component.literal(CultivationSystem.getPathMajorRealmName(pathId,pathData.majorRealm))).customScaling(0.5).build();
        majorRealmData.setID("major_realm_data");

        Label minorRealmTitle = (new Label.Builder()).screen(easyGuiScreen).x(0).y(25).text(Component.literal("Minor Realm:").withStyle(ChatFormatting.BOLD)).customScaling(0.5).build();
        Label minorRealmData = (new Label.Builder()).screen(easyGuiScreen).x(0).y(30).text(Component.literal(String.valueOf(pathData.minorRealm))).customScaling(0.5).build();
        minorRealmData.setID("minor_realm_data");

        Label progressTitle = (new Label.Builder()).screen(easyGuiScreen).x(0).y(35).text(Component.literal("Progress:").withStyle(ChatFormatting.BOLD)).customScaling(0.5).build();
        Label progressData = (new Label.Builder()).screen(easyGuiScreen).x(0).y(40).text(Component.literal(String.valueOf(pathData.pathProgress))).customScaling(0.5).build();
        progressData.setID("progress_data");

        String techniqueName = "none";
        if(!pathData.technique.equals("ascension:none")) techniqueName = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(pathData.technique,':')).getDisplayTitle();
        Label techniqueTitle = (new Label.Builder()).screen(easyGuiScreen).x(0).y(45).text(Component.literal("Technique:").withStyle(ChatFormatting.BOLD)).customScaling(0.5).build();
        ClickableLabel techniqueData = (new ClickableLabel.Builder()).screen(easyGuiScreen).x(0).y(50).text(techniqueName).customScaling(0.5).hoverColor(1686472069).build();
        techniqueData.clickAction = new Action(ModActions.CREATE_CONTAINER.get(),new Object[]{"technique_data"});
        techniqueData.setID("technique_data");

        addChild(PathTitle);

        addChild(majorRealmTitle);
        addChild(majorRealmData);

        addChild(minorRealmTitle);
        addChild(minorRealmData);

        addChild(progressTitle);
        addChild(progressData);

        addChild(techniqueTitle);
        addChild(techniqueData);


    }

    public void setPath(String pathId){
        pathData = Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA).getPathData(pathId);
        String name = "Essence Path";
        if(pathData.pathId.equals("ascension:body")) name = "Body Path";
        if(pathData.pathId.equals("ascension:intent")) name = "Intent Path";
        ((Label) getScreen().getElementByID("path_title")).text = Component.literal(name).withStyle(ChatFormatting.BOLD);
        ((Label) getScreen().getElementByID("path_title")).width = Minecraft.getInstance().font.width(Component.literal(name).withStyle(ChatFormatting.BOLD));



        ((Label) getScreen().getElementByID("major_realm_data")).text = Component.literal(CultivationSystem.getPathMajorRealmName(pathId,pathData.majorRealm));
        ((Label)  getScreen().getElementByID("major_realm_data")).width = Minecraft.getInstance().font.width(CultivationSystem.getPathMajorRealmName(pathId,pathData.majorRealm));


        ((Label) getScreen().getElementByID("minor_realm_data")).text =Component.literal(String.valueOf(pathData.minorRealm));
        ((Label) getScreen().getElementByID("minor_realm_data")).width = Minecraft.getInstance().font.width(String.valueOf(pathData.minorRealm));

        ((Label) getScreen().getElementByID("progress_data")).text =Component.literal(String.valueOf(pathData.pathProgress));
        ((Label) getScreen().getElementByID("progress_data")).width = Minecraft.getInstance().font.width(String.valueOf(pathData.pathProgress));

        String techniqueName = "none";
        if(!pathData.technique.equals("ascension:none")) techniqueName = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(pathData.technique,':')).getDisplayTitle();


        ((ClickableLabel) getScreen().getElementByID("technique_data")).text =Component.literal(techniqueName);
        ((ClickableLabel) getScreen().getElementByID("technique_data")).width = Minecraft.getInstance().font.width(techniqueName);


    }
}
