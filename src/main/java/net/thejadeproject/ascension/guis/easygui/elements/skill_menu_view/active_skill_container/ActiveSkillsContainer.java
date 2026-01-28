package net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.active_skill_container;

import com.mojang.blaze3d.platform.InputConstants;
import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.events.Clickable;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerSkillData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.ActiveSkillSlot;
import net.thejadeproject.ascension.progression.skills.ISkill;

public class ActiveSkillsContainer extends EmptyContainer {
    private final TextureDataSubSection background =  new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_stuff/skill_menu.png"),
            320,256,
            0,89,192,208
    );
    public Label selectedSkillLabel;

    public ActiveSkillsContainer(IEasyGuiScreen screen){
        super(screen,0,89,0,0);

        setWidth(192);
        setHeight(119);
        addChild(new SkillScrollContainer(screen,15,33,4,9));
        Label label = new Label(screen,56,25, Component.empty());
        label.centered = true;
        label.textColor = -1;
        label.useCustomScaling = true;
        label.setCustomScale(0.5);
        addChild(label);
        selectedSkillLabel = label;

    }

    public void setSelectedSkill(ISkill skill){
        if(skill == null){
            selectedSkillLabel.text = Component.empty();
        }else {
            selectedSkillLabel.text = skill.getSkillTitle();
        }

    }
    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
        background.renderTexture(guiGraphics);
    }




}
