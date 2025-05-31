package net.thejadeproject.ascension.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.thejadeproject.ascension.cultivation.ClientCultivationData;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.guis.CultivationUI;

@OnlyIn(Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        // Only show when cultivating
        if (!ClientCultivationData.isCultivating()) {
            return;
        }

        // Update UI progress
        CultivationUI.setProgress(ClientCultivationData.getProgress());

        // Render the custom UI
        CultivationUI.render(event.getGuiGraphics(), event.getPartialTick(0.5f, 0.2f));

        // Optional: Still show realm info
        String realmText = CultivationSystem.getRealmName(
                ClientCultivationData.getMajorRealm(),
                ClientCultivationData.getMinorRealm()
        );

        event.getGuiGraphics().drawString(
                Minecraft.getInstance().font,
                realmText,
                event.getGuiGraphics().guiWidth() - 150,
                70,  // Position below UI
                0xFFFFFF
        );
    }
}