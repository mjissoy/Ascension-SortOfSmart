package net.thejadeproject.ascension.guis.easygui.elements.main_menu.draggable_data;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.thejadeproject.ascension.guis.easygui.elements.HoverableLabel;
import net.thejadeproject.ascension.progression.skills.skill_lists.AcquirableSkillData;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.techniques.ITechnique;

import java.util.List;
@OnlyIn(Dist.CLIENT)
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

        List<Label> dao = technique.getDisplayDaoEfficiencies(easyGuiScreen);

        for (int i = 0; i<dao.size(); i++){

            dao.get(i).setY(lines.size()*5+5*i);
            descriptionContainer.addChild(dao.get(i));
        }

        addChild(
                (new Label.Builder())
                        .screen(easyGuiScreen)
                        .x(11).y(80).width(121)
                        .text(Component.literal("Skill List").withStyle(ChatFormatting.BOLD))
                        .customScaling(0.5)
                        .build()
        );


        EmptyContainer skillListContainer = new EmptyContainer(easyGuiScreen,11,85,121,45);
        addChild(skillListContainer);
        skillListContainer.setCull(true);
        List<AcquirableSkillData> skillList = technique.getSkillList().getSkillList();
        for(int i = 0; i<skillList.size(); i++){
            //TODO change text color if unlocked or locked
            skillListContainer.addChild(
                    (new Label.Builder())
                            .screen(easyGuiScreen)
                            .x(0).y(5*i)
                            .customScaling(0.5)
                            .text(Component.literal(skillList.get(i).asString()))
                            .build()
            );
        }
    }
}
