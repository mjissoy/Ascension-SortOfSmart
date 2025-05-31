package net.thejadeproject.ascension.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.thejadeproject.ascension.cultivation.ClientCultivationData;
import net.thejadeproject.ascension.cultivation.CultivationSystem;

@OnlyIn(Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        int width = event.getGuiGraphics().guiWidth();

        String realmText = CultivationSystem.getRealmName(
                ClientCultivationData.getMajorRealm(),
                ClientCultivationData.getMinorRealm()
        );

        String progressText = String.format("%.0f%%", ClientCultivationData.getProgress() * 100);

        guiGraphics.drawString(
                Minecraft.getInstance().font,
                realmText,
                width - 150,
                10,
                0xFFFFFF
        );

        guiGraphics.drawString(
                Minecraft.getInstance().font,
                progressText,
                width - 150,
                20,
                0xFFFFFF
        );
    }
}