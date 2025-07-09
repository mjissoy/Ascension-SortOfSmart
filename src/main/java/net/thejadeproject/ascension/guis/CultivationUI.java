package net.thejadeproject.ascension.guis;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.thejadeproject.ascension.cultivation.realms.RealmRegistry;

@OnlyIn(Dist.CLIENT)
public class CultivationUI {
    @SubscribeEvent(
            priority = EventPriority.NORMAL
    )
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getGuiGraphics().guiWidth();
        int h = event.getGuiGraphics().guiHeight();
        Level world = null;
        double x = (double) 0.0F;
        double y = (double) 0.0F;
        double z = (double) 0.0F;
        Player entity = Minecraft.getInstance().player;
        if (entity != null) {
            world = entity.level();
            x = entity.getX();
            y = entity.getY();
            z = entity.getZ();
        }

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        //event.getGuiGraphics().blit(ResourceLocation.parse("ascension:textures/screens/pixelqibarnew.png"), 1, 2, 0.0F, 0.0F, 182, 22, 182, 22);
        //event.getGuiGraphics().blit(ResourceLocation.parse("ascension:textures/screens/pixelqibarglassnewtransparentmore.png"), 1, 2, 0.0F, 0.0F, 182, 22, 182, 22);
        //event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("gui.ascension.qidisplay.label_qi"), 13, 9, -16777165, false);
        //event.getGuiGraphics().drawString(Minecraft.getInstance().font, Component.translatable("gui.ascension..label_qi_23_50"), 12, 8, -590337, false);
        event.getGuiGraphics().drawString(Minecraft.getInstance().font, QitextguiformatProcedure.execute(entity), 28, 9, -16777165, false);
        event.getGuiGraphics().drawString(Minecraft.getInstance().font, QitextguiformatProcedure.execute(entity), 27, 8, -590337, false);
        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);


    }
    public class QitextguiformatProcedure {
        public static String execute(Entity entity) {
            if (entity == null) {
                return "";
            } else {
                String var10000 = String.valueOf(((Player) entity).getPersistentData().getCompound("Cultivation").getFloat("CultivationProgress"));
                return var10000 + "/"; //+ ((Player) entity).getPersistentData().getCompound("Cultivation").getFloat("")
            }
        }
    }
}
