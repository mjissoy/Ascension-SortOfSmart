package net.thejadeproject.ascension.guis.easygui.elements.skill_menu.buttons;

import net.lucent.easygui.elements.controls.buttons.AbstractButton;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu.ActiveSkillBar;
import net.thejadeproject.ascension.guis.easygui.elements.skill_menu.SelectedSkillInfoPanel;
import net.thejadeproject.ascension.network.serverBound.ChangeSkillSlotSpellPayload;

public class ChangeSkillSlotButton extends AbstractButton {

    public ITextureData slotTexture = new TextureDataSubSection(
      ResourceLocation.fromNamespaceAndPath( AscensionCraft.MOD_ID,
              "textures/gui/screen/skill_select.png"),
            320,240,
            224,176,
            268,192
    );
    public ITextureData removeTexture = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath( AscensionCraft.MOD_ID,
                    "textures/gui/screen/skill_select.png"),
            320,240,
            224,160,
            268,176
    );

    public ITextureData renderTexture = slotTexture;

    public ChangeSkillSlotButton(IEasyGuiScreen screen,int x, int y){
        super(screen,x,y,46,16);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button, boolean clicked) {
        super.onClick(mouseX, mouseY, button, clicked);
        if(!clicked || !isVisible()) return;

        SelectedSkillInfoPanel skillInfoPanel = (SelectedSkillInfoPanel) getParent();
        ResourceLocation skillId = skillInfoPanel.getSelectedSkillId();
        int slot = skillInfoPanel.activeSkillBar.getSelectedSkillSlotId();
        boolean slotSkill = renderTexture.equals(slotTexture);
        System.out.println("try to change active skill slots");
        ChangeSkillSlotSpellPayload payload = new ChangeSkillSlotSpellPayload(slot,skillId.toString(),slotSkill);
        PacketDistributor.sendToServer(payload);
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        //TODO check if skill IDs match
        renderTexture.renderTexture(guiGraphics);
    }
}
