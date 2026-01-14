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
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.guis.easygui.ModActions;
import net.thejadeproject.ascension.guis.easygui.elements.main_menu.buttons.BreakthroughButton;
import net.thejadeproject.ascension.progression.paths.IPath;
import net.thejadeproject.ascension.progression.paths.ModPaths;
import net.thejadeproject.ascension.progression.techniques.ITechnique;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

import java.text.DecimalFormat;
import java.text.NumberFormat;

@OnlyIn(Dist.CLIENT)
public class DisplayPathDataContainer extends EmptyContainer {

    public CultivationData cultivationData;
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
        cultivationData = Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA).getCultivationData();
        pathData = Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(pathId);
        ResourceLocation pathResource = ResourceLocation.bySeparator(pathId,':');
        ResourceLocation techniqueResource = ResourceLocation.bySeparator(pathData.technique,':');

        Component name = AscensionRegistries.Paths.PATHS_REGISTRY.get(pathResource).getPathDisplayName();



        Component majorRealmTitleComponent = Component.literal("Major Realm:").withStyle(ChatFormatting.BOLD);
        Component minorRealmTitleComponent = Component.literal("Minor Realm:").withStyle(ChatFormatting.BOLD);
        Component progressTitleComponent =Component.literal("Progress:").withStyle(ChatFormatting.BOLD);
        Component techniqueTitleComponent = Component.literal("Technique:").withStyle(ChatFormatting.BOLD);
        Font font = Minecraft.getInstance().font;
        pathTitle = (new Label.Builder()).screen(easyGuiScreen).x(getWidth()/2).y(10).centered(true).text(name).customScaling(0.5).build();
        pathTitle.setID("path_title");

        Label majorRealmTitle = (new Label.Builder()).screen(easyGuiScreen).x(0).y(15).text(majorRealmTitleComponent).customScaling(0.5).build();
        majorRealmData = (new Label.Builder()).screen(easyGuiScreen).x(0).y(20).text(CultivationSystem.getMajorRealmName(techniqueResource,pathResource,pathData.majorRealm)).customScaling(0.5).build();
        majorRealmData.setID("major_realm_data");

        breakthroughButton = new BreakthroughButton(getScreen(), (int) (getScreen().getElementByID("major_realm_data").getWidth()*getScreen().getElementByID("major_realm_data").getCustomScale()), getScreen().getElementByID("major_realm_data").getY());
        breakthroughButton.setID("breakthrough_button");

        breakthroughButton.setActive(false);

        Label minorRealmTitle = (new Label.Builder()).screen(easyGuiScreen).x(0).y(25).text(minorRealmTitleComponent).customScaling(0.5).build();
        minorRealmData = (new Label.Builder()).screen(easyGuiScreen).x(0).y(30).text(Component.literal(String.valueOf(pathData.minorRealm))).customScaling(0.5).build();
        minorRealmData.setID("minor_realm_data");

        Label progressTitle = (new Label.Builder()).screen(easyGuiScreen).x(0).y(35).text(progressTitleComponent).customScaling(0.5).build();
        progressData = (new Label.Builder()).screen(easyGuiScreen).x(0).y(40).text(Component.literal(String.valueOf(pathData.pathProgress))).customScaling(0.5).build();
        progressData.setID("progress_data");

        Component techniqueName = Component.literal("none");
        if(!pathData.technique.equals("ascension:none")) techniqueName = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(techniqueResource).getDisplayTitle();
        Label techniqueTitle = (new Label.Builder()).screen(easyGuiScreen).x(0).y(45).text(techniqueTitleComponent).customScaling(0.5).build();
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
        addChild(breakthroughButton);

    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);


        ITechnique technique = null;
        Component majorRealmName;
        Component minorRealmName;
        double maxQi = 0;
        if(!pathData.technique.equals("ascension:none")) {
            technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(pathData.technique, ':'));
            majorRealmName = technique.getMajorRealmName(pathData.majorRealm);
            minorRealmName = technique.getMinorRealmName(pathData.majorRealm,pathData.minorRealm);
            maxQi = technique.getQiForRealm(pathData.majorRealm,pathData.minorRealm);
        }else{
            IPath path = ModPaths.getPath(pathData.pathId);
            majorRealmName = path.getMajorRealmName(pathData.majorRealm);
            minorRealmName = path.getMinorRealmName(pathData.majorRealm,pathData.minorRealm);
        }


        techniqueData.text =(technique == null) ?  Component.literal("none") : technique.getDisplayTitle();
        techniqueData.width = Minecraft.getInstance().font.width( techniqueData.text);

        majorRealmData.text = majorRealmName;
        majorRealmData.width = Minecraft.getInstance().font.width(majorRealmData.text);

        if(pathData.stabilityCultivationTicks > 0 && !pathData.breakingThrough){
            majorRealmData.text = Component.empty().append(majorRealmName).append(" ("+String.format("%.2f",technique.getStabilityHandler().getStability(pathData.stabilityCultivationTicks)*100)+"%)");
            majorRealmData.width = Minecraft.getInstance().font.width(majorRealmData.text);
            breakthroughButton.setActive(true);

            breakthroughButton.setX((int) (majorRealmData.getWidth()*majorRealmData.getCustomScale()));
            breakthroughButton.setY(majorRealmData.getY());
        }else breakthroughButton.setActive(false);


        minorRealmData.text = minorRealmName;
        minorRealmData.width = Minecraft.getInstance().font.width(minorRealmName);
        NumberFormat format = new DecimalFormat("#0.00");
        progressData.text =Component.literal(format.format(pathData.pathProgress)+"/"+format.format(maxQi));
        progressData.width = Minecraft.getInstance().font.width(progressData.text);



    }

    public void setPath(String pathId){
        //TODO if realm is at breakthrough make button visible in OuterPathData Container

        IPath path = ModPaths.getPath(pathId);
        Component name = path.getPathDisplayName();

        pathTitle.text = name;
        pathTitle.width = Minecraft.getInstance().font.width(name);


    }
}
