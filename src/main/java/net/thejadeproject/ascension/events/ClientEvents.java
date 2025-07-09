package net.thejadeproject.ascension.events;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.thejadeproject.ascension.cultivation.CultivationData;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.guis.CultivationUI;

@OnlyIn(Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        // Only show when cultivating
        if (!CultivationData.isCultivating()) {
            return;
        }


        // Update UI progress
        //CultivationUI.setProgress(CultivationData.ClientCultivationData.getProgress());

        // Render the custom UI
        //CultivationUI.render(event.getGuiGraphics(), 0.1f);

        // Optional: Still show realm info
        String realmText = CultivationSystem.getRealmName(
                CultivationData.ClientCultivationData.getMajorRealm(),
                CultivationData.ClientCultivationData.getMinorRealm()
        );

        event.getGuiGraphics().drawString(
                Minecraft.getInstance().font,
                realmText,
                event.getGuiGraphics().guiWidth() - 150,
                70,  // Position below UI
                0xFFFFFF
        );
    }
//    private void renderTabList(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
//        Scoreboard scoreboard = this.minecraft.level.getScoreboard();
//        Objective objective = scoreboard.getDisplayObjective(DisplaySlot.LIST);
//        if (!this.minecraft.options.keyPlayerList.isDown()
//                || this.minecraft.isLocalServer() && this.minecraft.player.connection.getListedOnlinePlayers().size() <= 1 && objective == null) {
//            this.tabList.setVisible(false);
//        } else {
//            this.tabList.setVisible(true);
//            this.tabList.render(guiGraphics, guiGraphics.guiWidth(), scoreboard, objective);
//        }
//    }
}