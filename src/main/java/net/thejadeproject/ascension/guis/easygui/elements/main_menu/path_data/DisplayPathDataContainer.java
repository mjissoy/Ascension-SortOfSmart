package net.thejadeproject.ascension.guis.easygui.elements.main_menu.path_data;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.other.ClickableLabel;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.templating.actions.Action;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.cultivation.player.CultivationData;
import net.thejadeproject.ascension.cultivation.player.PlayerData;
import net.thejadeproject.ascension.guis.easygui.ModActions;
import net.thejadeproject.ascension.guis.easygui.elements.main_menu.buttons.BreakthroughButton;
import net.thejadeproject.ascension.progression.techniques.ITechnique;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttachments;
@OnlyIn(Dist.CLIENT)
public class DisplayPathDataContainer extends EmptyContainer {

    public CultivationData.PathData pathData;
    public BreakthroughButton breakthroughButton;
    public Label pathTitle;
    public Label majorRealmData;
    public Label minorRealmData;
    public Label progressData;
    public ClickableLabel techniqueData;

    //TODO create a label that reads a field from a data attachment. or update these one every tick
    public DisplayPathDataContainer(IEasyGuiScreen easyGuiScreen, int x, int y, int width, int height,String pathId) {
        super(easyGuiScreen, x, y, width, height);
        setID("path_data_container");
        pathData = Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(pathId);
        String name = "Essence Path";
        if(pathData.pathId.equals("ascension:body")) name = "Body Path";
        if(pathData.pathId.equals("ascension:intent")) name = "Intent Path";

        Component nameComponent = Component.literal(name).withStyle(ChatFormatting.BOLD);
        Component majorRealmTitleComponent = Component.literal("Major Realm:").withStyle(ChatFormatting.BOLD);
        Component minorRealmTitleComponent = Component.literal("Minor Realm:").withStyle(ChatFormatting.BOLD);
        Component progressTitleComponent =Component.literal("Progress:").withStyle(ChatFormatting.BOLD);
        Component techniqueTitleComponent = Component.literal("Technique:").withStyle(ChatFormatting.BOLD);
        Font font = Minecraft.getInstance().font;
        pathTitle = (new Label.Builder()).screen(easyGuiScreen).x(getWidth()/2).y(10).centered(true).text(majorRealmTitleComponent).width(font.width(nameComponent)).customScaling(0.5).build();
        pathTitle.setID("path_title");

        Label majorRealmTitle = (new Label.Builder()).screen(easyGuiScreen).x(0).y(15).text(majorRealmTitleComponent).customScaling(0.5).width(font.width(majorRealmTitleComponent)).build();
        majorRealmData = (new Label.Builder()).screen(easyGuiScreen).x(0).y(20).text(Component.literal(CultivationSystem.getPathMajorRealmName(pathId,pathData.majorRealm))).customScaling(0.5).build();
        majorRealmData.setID("major_realm_data");

        breakthroughButton = new BreakthroughButton(getScreen(), (int) (getScreen().getElementByID("major_realm_data").getWidth()*getScreen().getElementByID("major_realm_data").getCustomScale()), getScreen().getElementByID("major_realm_data").getY());
        breakthroughButton.setID("breakthrough_button");
        addChild(breakthroughButton);
        breakthroughButton.setActive(false);

        Label minorRealmTitle = (new Label.Builder()).screen(easyGuiScreen).x(0).y(25).text(minorRealmTitleComponent).width(font.width(minorRealmTitleComponent)).customScaling(0.5).build();
        minorRealmData = (new Label.Builder()).screen(easyGuiScreen).x(0).y(30).text(Component.literal(String.valueOf(pathData.minorRealm))).customScaling(0.5).build();
        minorRealmData.setID("minor_realm_data");

        Label progressTitle = (new Label.Builder()).screen(easyGuiScreen).x(0).y(35).text(progressTitleComponent).width(font.width(progressTitleComponent)).customScaling(0.5).build();
        progressData = (new Label.Builder()).screen(easyGuiScreen).x(0).y(40).text(Component.literal(String.valueOf(pathData.pathProgress))).customScaling(0.5).build();
        progressData.setID("progress_data");

        String techniqueName = "none";
        if(!pathData.technique.equals("ascension:none")) techniqueName = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(pathData.technique,':')).getDisplayTitle();
        Label techniqueTitle = (new Label.Builder()).screen(easyGuiScreen).x(0).y(45).text(techniqueTitleComponent).customScaling(0.5).width(font.width(techniqueTitleComponent)).build();
        techniqueData = (new ClickableLabel.Builder()).screen(easyGuiScreen).x(0).y(50).text(techniqueName).customScaling(0.5).hoverColor(1686472069).build();
        techniqueData.clickAction = new Action(ModActions.CREATE_CONTAINER.get(),new Object[]{"technique_data"});
        techniqueData.setID("technique_data");

        addChild(pathTitle);

        addChild(majorRealmTitle);
        addChild(majorRealmData);

        addChild(minorRealmTitle);
        addChild(minorRealmData);

        addChild(progressTitle);
        addChild(progressData);

        addChild(techniqueTitle);
        addChild(techniqueData);


    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);


        ITechnique technique = null;
        if(!pathData.technique.equals("ascension:none")) {
            technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(pathData.technique, ':'));

        }


        techniqueData.text =Component.literal((technique == null) ? "none" : technique.getDisplayTitle());
        techniqueData.width = Minecraft.getInstance().font.width( techniqueData.text);

        majorRealmData.text = Component.literal(CultivationSystem.getPathMajorRealmName(pathData.pathId,pathData.majorRealm));
        majorRealmData.width = Minecraft.getInstance().font.width(majorRealmData.text);

        if(pathData.stabilityCultivationTicks > 0 && !pathData.breakingThrough){
            majorRealmData.text = Component.literal(CultivationSystem.getPathMajorRealmName(pathData.pathId,pathData.majorRealm)+" ("+String.format("%.2f",technique.getStabilityHandler().getStability(pathData.stabilityCultivationTicks)*100)+"%)");
            majorRealmData.width = Minecraft.getInstance().font.width(majorRealmData.text);
            breakthroughButton.setActive(true);
            System.out.println(majorRealmData.text);
            breakthroughButton.setX((int) (majorRealmData.getWidth()*majorRealmData.getCustomScale()));
            breakthroughButton.setY(majorRealmData.getY());
        }else breakthroughButton.setActive(false);


        minorRealmData.text =Component.literal(String.valueOf(pathData.minorRealm));
        minorRealmData.width = Minecraft.getInstance().font.width(String.valueOf(pathData.minorRealm));

        progressData.text =Component.literal(String.valueOf(pathData.pathProgress));
        progressData.width = Minecraft.getInstance().font.width(String.valueOf(pathData.pathProgress));



    }

    public void setPath(String pathId){
        //TODO if realm is at breakthrough make button visible in OuterPathData Container
        pathData = Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(pathId);

        String name = "Essence Path";
        if(pathData.pathId.equals("ascension:body")) name = "Body Path";
        if(pathData.pathId.equals("ascension:intent")) name = "Intent Path";
        pathTitle.text = Component.literal(name).withStyle(ChatFormatting.BOLD);
        pathTitle.width = Minecraft.getInstance().font.width(Component.literal(name).withStyle(ChatFormatting.BOLD));


    }
}
