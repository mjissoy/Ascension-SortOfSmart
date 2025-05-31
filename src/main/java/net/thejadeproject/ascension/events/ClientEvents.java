package net.thejadeproject.ascension.events.;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.thejadeproject.ascension.cultivation.CultivationSystem;

@OnlyIn(Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        int width = event.getGuiGraphics().guiWidth();

        String realmText = CultivationSystem.getRealmName(
                ClientData.getMajorRealm(),
                ClientData.getMinorRealm()
        );

        String progressText = String.format("%.0f%%", ClientData.getProgress() * 100);

        guiGraphics.drawString(
                event.getGuiGraphics()..getFont(),
                realmText,
                width - 150,
                10,
                0xFFFFFF
        );

        guiGraphics.drawString(
                event.getWindow().getFont(),
                progressText,
                width - 150,
                20,
                0xFFFFFF
        );
    }
}