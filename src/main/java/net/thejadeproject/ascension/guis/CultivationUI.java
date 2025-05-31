package net.thejadeproject.ascension.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CultivationUI {
    private static boolean visible = false;
    private static float progress = 0;

    public static void render(GuiGraphics gui, float partialTicks) {
        if (!visible) return;

        int width = gui.guiWidth();
        int height = gui.guiHeight();

        gui.fill(width - 120, 10, width - 10, 60, 0x66000000);

        int barWidth = (int)(100 * progress);
        gui.fill(width - 110, 40, width - 110 + barWidth, 50, 0xFF00FF00);

        gui.drawString(
                Minecraft.getInstance().font,
                "Cultivating...",
                width - 105,
                15,
                0xFFFFFF
        );

        gui.drawString(
                Minecraft.getInstance().font,
                String.format("%.0f%%", progress * 100),
                width - 105,
                25,
                0xFFFFFF
        );
    }

    public static void setVisible(boolean visible) {
        CultivationUI.visible = visible;
    }

    public static void setProgress(float progress) {
        CultivationUI.progress = progress;
    }
}
