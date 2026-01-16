package net.thejadeproject.ascension.guis.easygui.elements.skill_menu_view.passive_skill_container;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;

public class PassiveSkillsContainer extends EmptyContainer {
    private final TextureDataSubSection background =  new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/screen/skill_stuff/skill_menu.png"),
            320,256,
            198,0,320,208
    );
    public PassiveSkillsContainer(IEasyGuiScreen screen){
        super(screen,0,0,0,0);
        setWidth(122);
        setHeight(208);
        setX(198);
        addChild(new PassiveSkillScrollContainer(screen,11,15,8));
    }



    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
        background.renderTexture(guiGraphics);
    }
}
