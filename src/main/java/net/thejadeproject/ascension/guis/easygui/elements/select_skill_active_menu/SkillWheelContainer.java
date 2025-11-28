package net.thejadeproject.ascension.guis.easygui.elements.select_skill_active_menu;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.lucent.easygui.elements.containers.EmptyContainer;
import net.lucent.easygui.elements.other.Image;
import net.lucent.easygui.interfaces.ContainerRenderable;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.lucent.easygui.util.textures.TextureData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerSkillData;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttachments;
import net.thejadeproject.ascension.util.math.CircleBoundChecker;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class SkillWheelContainer extends EmptyContainer {
    public Integer hoveredSlot = null;
    private final PlayerSkillData.ActiveSkillContainer activeSkillContainer;
    public List<ContainerRenderable> wedges = new ArrayList<>();
    public ITextureData skillWheelBackground = new TextureData(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/gui/overlay/skill_wheel.png"),
            64,64
    );

    private void createSkillIconAt(int slot,int x,int y){
        
        if(activeSkillContainer.getSkillIdList().get(slot).isEmpty()) return;
        ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(activeSkillContainer.getSkillIdList().get(slot),':'));
        if(skill == null) return;
        Image skillIcon = new Image(getScreen(),skill.skillIcon(),x-skill.skillIcon().getWidth()/8,y-skill.skillIcon().getHeight()/8);
        skillIcon.setCustomScale(0.25);
        addChild(skillIcon);
    }
    public SkillWheelContainer(IEasyGuiScreen screen,int x,int y){
        super(screen,x,y,64,64);
        activeSkillContainer =  Minecraft.getInstance().player.getData(ModAttachments.PLAYER_SKILL_DATA).activeSkillContainer;
        SkillWheelSegment FirstSkillSlot = new SkillWheelSegment(screen,0,0,0,0,0,null,0);
        createSkillIconAt(0,32,16);

        SkillWheelSegment SecondSkillSlot = new SkillWheelSegment(screen,0,0,16,16,0,null,90);
        createSkillIconAt(1,45,32);


        SkillWheelSegment ThirdSkillSlot = new SkillWheelSegment(screen,0,0,0,32,0,null,180);
        createSkillIconAt(2,32,45);

        SkillWheelSegment FourthSkillSlot = new SkillWheelSegment(screen,0,0,-16,16,0,null,270);
        createSkillIconAt(3,16,32);

        addChild(FirstSkillSlot);
        addChild(SecondSkillSlot);
        addChild(ThirdSkillSlot);
        addChild(FourthSkillSlot);
        wedges.add(FirstSkillSlot);
        wedges.add(SecondSkillSlot);
        wedges.add(ThirdSkillSlot);
        wedges.add(FourthSkillSlot);

    }

    private void unsetFocus(){
        for(ContainerRenderable child:getChildren()){
            child.setFocused(false);
        }
    }
    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if(getTransform() == null) return false;
        Vector3f center =getTransform().transformPosition( new Vector3f((float) getWidth() /2, (float) getHeight() /2,0));
        Vec2 center2d = new Vec2(center.x,center.y);
        Vec2 point = new Vec2((float) mouseX, (float) mouseY);
        //if(!CircleBoundChecker.isPointInCircle(center2d,point, (float) Math.pow((32*getTotalCustomScaling()),2))) return false;
        double angle = CircleBoundChecker.angleBetweenPointCenterAndNormal(point,center2d);
        
        if(angle<45 || angle > 315) hoveredSlot = 1;
        if(angle > 45 && angle < 45+90) hoveredSlot = 2;
        if(angle > 45+90 && angle < 45+180) hoveredSlot = 3;
        if(angle > 45+180 && angle < 45+270) hoveredSlot = 0;
        return super.isMouseOver(mouseX, mouseY);
    }

    @Override
    public void renderSelf(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderSelf(guiGraphics, mouseX, mouseY, partialTick);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        skillWheelBackground.renderTexture(guiGraphics);
        unsetFocus();
        if(hoveredSlot != null) wedges.get(hoveredSlot).setFocused(true);

        RenderSystem.disableBlend();
    }
}
