package net.thejadeproject.ascension.guis.easygui.elements.main_menu;

import com.google.gson.JsonPrimitive;
import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.other.ClickableLabel;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.ContainerRenderable;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.templating.actions.Action;
import net.lucent.easygui.templating.actions.IAction;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.player.PlayerData;
import net.thejadeproject.ascension.guis.easygui.ModActions;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttachments;
import org.checkerframework.checker.units.qual.A;

//TODO
public class StatContainer extends EmptyContainer {
    public StatContainer(IEasyGuiScreen easyGuiScreen,int x, int y, int width,int height){
        super(easyGuiScreen,x,y,width,height);

        Label l4Title = (new Label.Builder()).screen(easyGuiScreen).x(0).y(0).text(Component.literal("Max Health:").withStyle(ChatFormatting.BOLD)).width(140).customScaling(0.5).build();
        Label l4 = (new Label.Builder()).screen(easyGuiScreen).x(0).y(5).customScaling(0.5).width(140).build();
        Label l5Title = (new Label.Builder()).screen(easyGuiScreen).x(0).y(10).text(Component.literal("Health:").withStyle(ChatFormatting.BOLD)).width(140).customScaling(0.5).build();
        Label l5 = (new Label.Builder()).screen(easyGuiScreen).x(0).y(15).customScaling(0.5).width(140).build();
        Label l6Title = (new Label.Builder()).screen(easyGuiScreen).x(0).y(20).text(Component.literal("Armor:").withStyle(ChatFormatting.BOLD)).width(140).customScaling(0.5).build();
        Label l6 = (new Label.Builder()).screen(easyGuiScreen).x(0).y(25).customScaling(0.5).width(140).build();
        Label l7Title = (new Label.Builder()).screen(easyGuiScreen).x(0).y(30).text(Component.literal("Attack Damage:").withStyle(ChatFormatting.BOLD)).width(140).customScaling(0.5).build();
        Label l7 = (new Label.Builder()).screen(easyGuiScreen).x(0).y(35).customScaling(0.5).width(140).build();
        Label l8Title = (new Label.Builder()).screen(easyGuiScreen).x(0).y(40).text(Component.literal("Movement Speed:").withStyle(ChatFormatting.BOLD)).width(140).customScaling(0.5).build();
        Label l8 = (new Label.Builder()).screen(easyGuiScreen).x(0).y(45).customScaling(0.5).width(140).build();
        Label l9Title = (new Label.Builder()).screen(easyGuiScreen).x(0).y(50).text(Component.literal("Jump Strength:").withStyle(ChatFormatting.BOLD)).width(140).customScaling(0.5).build();
        Label l9 = (new Label.Builder()).screen(easyGuiScreen).x(0).y(55).customScaling(0.5).width(140).build();
        Label l10Title = (new Label.Builder()).screen(easyGuiScreen).x(0).y(70).text(Component.literal("Physique:").withStyle(ChatFormatting.BOLD)).width(140).customScaling(0.5).build();
        ClickableLabel l10 = (new ClickableLabel.Builder()).screen(easyGuiScreen).x(0).y(75).customScaling(0.5).hoverColor(1686472069).build();

  
        l4.setTickAction(new Action(ModActions.DISPLAY_ATTRIBUTE_VALUE.get(),new Object[]{"Max Health"}));
        l5.setTickAction(new Action(ModActions.DISPLAY_ATTRIBUTE_VALUE.get(),new Object[]{"Health"}));
        l6.setTickAction(new Action(ModActions.DISPLAY_ATTRIBUTE_VALUE.get(),new Object[]{"Armor"}));
        l7.setTickAction(new Action(ModActions.DISPLAY_ATTRIBUTE_VALUE.get(),new Object[]{"Attack"}));
        l8.setTickAction(new Action(ModActions.DISPLAY_ATTRIBUTE_VALUE.get(),new Object[]{"Speed"}));
        l9.setTickAction(new Action(ModActions.DISPLAY_ATTRIBUTE_VALUE.get(),new Object[]{"Jump Strength"}));
        l10.setTickAction(new Action(new IAction(){
            @Override
            public void run(ContainerRenderable renderable, Object[] customArgs) {
                 Player player = Minecraft.getInstance().player;

                ClickableLabel label = ((ClickableLabel) renderable);
                if(player == null )return;
                String[] physique = player.getData(ModAttachments.PHYSIQUE).split(":");

                label.text = Component.literal(
                        AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(ResourceLocation.fromNamespaceAndPath(
                                physique[0],
                                physique[1]
                        )).getDisplayTitle()
                );

                label.setWidth(label.font.width(label.text));
            }
        },new Object[]{}));
        l10.clickAction = new Action(ModActions.CREATE_CONTAINER.get(),new Object[]{"physique_data"});
  
        addChild(l4Title);
        addChild(l4);
        addChild(l5Title);
        addChild(l5);
        addChild(l6Title);
        addChild(l6);
        addChild(l7Title);
        addChild(l7);
        addChild(l8Title);
        addChild(l8);
        addChild(l9Title);
        addChild(l9);
        addChild(l10Title);
        addChild(l10);
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);

    }
}
