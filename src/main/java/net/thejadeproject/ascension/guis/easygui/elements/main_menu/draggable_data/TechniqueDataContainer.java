package net.thejadeproject.ascension.guis.easygui.elements.main_menu.draggable_data;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.techniques.ITechnique;

import java.util.List;

public class TechniqueDataContainer extends DraggableDataContainer {
    public String techniqueId;
    public TechniqueDataContainer(IEasyGuiScreen easyGuiScreen, int x, int y,String techniqueId) {
        super(easyGuiScreen, x, y);
        this.techniqueId = techniqueId;
        ITechnique technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(techniqueId,':'));


        addChild((new Label.Builder())
                .screen(easyGuiScreen)
                .x(getWidth()/2).y(20).centered(true)
                .text(Component.literal(technique.getDisplayTitle()).withStyle(ChatFormatting.BOLD))
                .customScaling(0.5).build());
        addChild(
                (new Label.Builder())
                        .screen(easyGuiScreen)
                        .x(11).y(25).width(121)
                        .text(Component.literal("Description").withStyle(ChatFormatting.BOLD))
                        .customScaling(0.5)
                        .build()
        );
        //todo replace with a scroll box
        EmptyContainer descriptionContainer = new EmptyContainer(easyGuiScreen,11,30,121,50);
        addChild(descriptionContainer);
        descriptionContainer.setCull(true);
        List<MutableComponent> lines = technique.getDescription();
        for (int i = 0; i<lines.size(); i++){
            descriptionContainer.addChild(
                    (new Label.Builder())
                            .screen(easyGuiScreen)
                            .x(0).y(5*i)
                            .text(lines.get(i))
                            .customScaling(0.5)
                            .build()
            );
        }
        addChild(
                (new Label.Builder())
                        .screen(easyGuiScreen)
                        .x(11).y(80).width(121)
                        .text(Component.literal("Skill List").withStyle(ChatFormatting.BOLD))
                        .customScaling(0.5)
                        .build()
        );
    }
}
