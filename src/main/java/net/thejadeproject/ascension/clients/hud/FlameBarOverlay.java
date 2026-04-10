package net.thejadeproject.ascension.clients.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.thejadeproject.ascension.blocks.entity.FlameStandBlockEntity;
import net.thejadeproject.ascension.blocks.entity.PillCauldronLowHumanEntity;

/**
 * HUD overlay shown when looking at a Flame Stand or Pill Cauldron.
 *
 * Displays:
 *   ┌────────────────────────────────────────┐
 *   │  Temperature:  547°                    │
 *   │  ████████░░░░░░░░░░░░░░░░░░░░░░░░░░░  │
 *   │              ↑min        max↑          │  (green zone markers)
 *   │  ▓▓▓▓▓▓▓▓░░░░░░░░░░░░░░░   [progress] │
 *   │  ⚑ In range — keep fanning!            │
 *   └────────────────────────────────────────┘
 *
 * The temperature bar flickers smoothly (client-side interpolation).
 * Min/max recipe range markers are shown as tick marks on the bar.
 * Progress bar only visible when a recipe is active.
 *
 * Register with: NeoForge.EVENT_BUS.register(FlameBarOverlay.class)
 */
@OnlyIn(Dist.CLIENT)
public class FlameBarOverlay {

    // Client-side smooth temperature value for flicker animation
    private static float smoothTemp  = 0f;
    private static float smoothAlpha = 0f; // fade in/out

    @SubscribeEvent
    public static void onRenderGui(RenderGuiLayerEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        if (mc.options.hideGui) return;

        HitResult hit = mc.hitResult;
        if (!(hit instanceof BlockHitResult blockHit)) return;
        if (blockHit.getType() == HitResult.Type.MISS) return;

        BlockPos lookPos = blockHit.getBlockPos();
        FlameStandBlockEntity flameStand = null;
        PillCauldronLowHumanEntity cauldron = null;

        BlockEntity be = mc.level.getBlockEntity(lookPos);
        if (be instanceof FlameStandBlockEntity fs) {
            flameStand = fs;
        } else if (be instanceof PillCauldronLowHumanEntity ce) {
            cauldron   = ce;
            BlockEntity below = mc.level.getBlockEntity(lookPos.below());
            if (below instanceof FlameStandBlockEntity fs) flameStand = fs;
        }

        if (flameStand == null || !flameStand.isLit()) {
            smoothAlpha = Math.max(0f, smoothAlpha - 0.08f);
            if (smoothAlpha <= 0f) return;
        } else {
            smoothAlpha = Math.min(1f, smoothAlpha + 0.12f);
        }

        int screenW = mc.getWindow().getGuiScaledWidth();
        int screenH = mc.getWindow().getGuiScaledHeight();

        // ── Layout ───────────────────────────────────────────────
        int panelW  = 140;
        int panelH  = 52;
        int panelX  = (screenW - panelW) / 2;
        int panelY  = screenH / 2 + 16;

        int alpha   = (int)(smoothAlpha * 180);
        GuiGraphics g = event.getGuiGraphics();

        // Background panel
        g.fill(panelX - 2, panelY - 2, panelX + panelW + 2, panelY + panelH + 2,
                (alpha / 2) << 24 | 0x000000);

        if (flameStand == null) return;

        int rawTemp   = flameStand.getTemperature();
        float dispTemp = flameStand.getDisplayTemp(); // includes flicker

        // Smooth the display temp client-side for extra fluidity
        smoothTemp += (dispTemp - smoothTemp) * 0.15f;

        // Retrieve recipe range from cauldron ContainerData if available
        int minTemp = 0, maxTemp = 0, progress = 0, maxProgress = 0;
        if (cauldron != null) {
            minTemp     = cauldron.getRecipeMinTemp();
            maxTemp     = cauldron.getRecipeMaxTemp();
            progress    = cauldron.data.get(0);
            maxProgress = cauldron.data.get(1);
        }

        int barW  = 120;
        int barH  = 6;
        int barX  = panelX + 10;
        int barY  = panelY + 18;

        // ── Temperature label ────────────────────────────────────
        String tempLabel = "Temperature: " + rawTemp + "°";
        g.drawString(mc.font, tempLabel, panelX + 10, panelY + 4,
                argb(alpha, 255, 200, 100), true);

        // ── Temperature bar background ───────────────────────────
        g.fill(barX - 1, barY - 1, barX + barW + 1, barY + barH + 1,
                argb(alpha, 40, 40, 40));
        g.fill(barX, barY, barX + barW, barY + barH,
                argb(alpha, 80, 80, 80));

        // ── Green zone (recipe range) ────────────────────────────
        if (minTemp > 0 && maxTemp > minTemp) {
            int minX = barX + (int)(barW * (minTemp / (float) FlameStandBlockEntity.MAX_TEMP));
            int maxX = barX + (int)(barW * (maxTemp / (float) FlameStandBlockEntity.MAX_TEMP));
            g.fill(minX, barY, maxX, barY + barH, argb(alpha, 40, 160, 40));
        }

        // ── Temperature fill (with flicker) ─────────────────────
        int filled = (int)(barW * smoothTemp);
        int tempColor = getTempColor(rawTemp, minTemp, maxTemp, alpha);
        g.fill(barX, barY, barX + filled, barY + barH, tempColor);

        // ── Flicker highlight (bright edge) ─────────────────────
        if (filled > 2) {
            g.fill(barX + filled - 2, barY, barX + filled, barY + barH,
                    argb(Math.min(255, (int)(smoothAlpha * 255)), 255, 255, 200));
        }

        // ── Min / Max tick marks ─────────────────────────────────
        if (minTemp > 0 && maxTemp > minTemp) {
            int minX = barX + (int)(barW * (minTemp / (float) FlameStandBlockEntity.MAX_TEMP));
            int maxX = barX + (int)(barW * (maxTemp / (float) FlameStandBlockEntity.MAX_TEMP));
            g.fill(minX, barY - 3, minX + 1, barY + barH + 3, argb(alpha, 255, 255, 60));
            g.fill(maxX, barY - 3, maxX + 1, barY + barH + 3, argb(alpha, 255, 255, 60));
            g.drawString(mc.font, "▲", minX - 2, barY + barH + 2, argb(alpha, 255, 255, 60), false);
            g.drawString(mc.font, "▲", maxX - 2, barY + barH + 2, argb(alpha, 255, 255, 60), false);
        }

        // ── Progress bar (only when crafting) ───────────────────
        if (maxProgress > 0) {
            int progBarY  = barY + barH + 14;
            int progFilled = (int)(barW * (progress / (float) maxProgress));

            g.fill(barX - 1, progBarY - 1, barX + barW + 1, progBarY + 4, argb(alpha, 40, 40, 40));
            g.fill(barX, progBarY, barX + barW, progBarY + 3, argb(alpha, 60, 60, 60));
            g.fill(barX, progBarY, barX + progFilled, progBarY + 3, argb(alpha, 80, 200, 80));

            int pct = (int)(progress * 100f / maxProgress);
            int secLeft = (maxProgress - progress) / 20;
            String progLabel = "Crafting: " + pct + "%  (" + secLeft + "s)";
            g.drawString(mc.font, progLabel, panelX + 10, progBarY + 5,
                    argb(alpha, 180, 255, 180), false);
        }

        // ── Status text ──────────────────────────────────────────
        String status = getStatusText(rawTemp, minTemp, maxTemp,
                flameStand.needsFanning(), flameStand.isCritical());
        int statusY = panelY + panelH - 10;
        g.drawString(mc.font, status, panelX + 10, statusY, 0xFFFFFFFF, true);
    }

    // ── Helpers ───────────────────────────────────────────────────

    private static int getTempColor(int temp, int minTemp, int maxTemp, int alpha) {
        if (maxTemp > 0 && temp >= minTemp && temp <= maxTemp) {
            // Inside the target range — bright green
            return argb(alpha, 80, 220, 80);
        }
        if (maxTemp > 0 && temp < minTemp) {
            // Too cold — blue-ish
            return argb(alpha, 80, 140, 220);
        }
        // Too hot or no recipe — gradient red/orange
        float fraction = Math.min(1f, temp / (float) FlameStandBlockEntity.MAX_TEMP);
        int r = (int)(200 + 55 * fraction);
        int gCol = (int)(100 * (1f - fraction));
        return argb(alpha, r, gCol, 20);
    }

    private static String getStatusText(int temp, int minTemp, int maxTemp,
                                        boolean needsFanning, boolean critical) {
        if (critical)      return "§c⚠ Critical! Fan immediately!";
        if (needsFanning)  return "§e⚡ Fan the flame!";
        if (maxTemp > 0) {
            if (temp < minTemp) return "§9❄ Too cold — fan to raise temperature";
            if (temp > maxTemp) return "§cToo hot — let it cool or wait";
            return "§a✔ In range — hold steady!";
        }
        return "§7Flame burning";
    }

    private static int argb(int alpha, int r, int g, int b) {
        return (Math.min(255, alpha) << 24) | (r << 16) | (g << 8) | b;
    }
}