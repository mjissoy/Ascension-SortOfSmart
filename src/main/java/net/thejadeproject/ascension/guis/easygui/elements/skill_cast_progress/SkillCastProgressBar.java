package net.thejadeproject.ascension.guis.easygui.elements.skill_cast_progress;

import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.util.textures.TextureDataSubSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.CastingInstance;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

public class SkillCastProgressBar extends EmptyContainer {
    private final ITextureData background = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/overlay/skill_progress_bar.png"),
            80,
            20,
            0,
            0,
            80,
            13
    );
    private final ITextureData bar = new TextureDataSubSection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/overlay/skill_progress_bar.png"),
            80,
            20,
            3,
            13,
            76,
            20
    );
    public final int spellCastingSlot;
    public SkillCastProgressBar(IEasyGuiScreen screen, int x,int y,int spellCastingSlot){
        super(screen,x,y,80,13);
        this.spellCastingSlot = spellCastingSlot;
    }

    public boolean shouldRender(){
        if(Minecraft.getInstance().player == null) return false;
        PlayerData data = Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA);
        if(data == null) return false;
        CastingInstance instance = data.getSkillFromSlot(spellCastingSlot);
        if(instance == null) return false;
        ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(instance.skillId);
        return skill instanceof  AbstractActiveSkill activeSkill && activeSkill.shouldRenderCastingBar();
    }

    public double getProgress() {
        PlayerData data = Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA);
        if(data == null) return 0.0;
        CastingInstance instance = data.getSkillFromSlot(spellCastingSlot);

        ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(instance.skillId);
        double maxTime =((AbstractActiveSkill) skill).maxCastingTicks();
        return instance.castTickElapsed/maxTime;
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
        if(shouldRender()){
            
            
            //guiGraphics.fill(0,0,100,100,-16777216);
            background.renderTexture(guiGraphics);
            bar.renderTexture(guiGraphics,3,3,3,13, (int) (76*getProgress()),7);
        }
    }
}
