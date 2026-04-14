package net.thejadeproject.ascension.gui.elements.cultivation;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.events.EasyEvents;
import net.lucent.easygui.gui.events.type.EasyEvent;
import net.lucent.easygui.gui.events.type.EasyMouseEvent;
import net.lucent.easygui.gui.textures.ITextureData;
import net.lucent.easygui.gui.textures.TextureDataSubsection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysique;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PhysiquePanel extends RenderableElement {
    private static final ITextureData BG = new TextureDataSubsection(
            ResourceLocation.fromNamespaceAndPath(
                    AscensionCraft.MOD_ID,
                    "textures/gui/screen/cultivation_physique_panel.png"
            ),
            160, 180, 0, 0, 160, 180
    );

    private static final int HEADER_X = 6;
    private static final int HEADER_Y = 20;
    private static final int HEADER_W = 148;
    private static final int HEADER_H = 14;

    private Runnable onClose;

    public PhysiquePanel(UIFrame frame) {
        super(frame);
        setWidth(160);
        setHeight(180);
        addEventListener(EasyEvents.MOUSE_DOWN_EVENT, this::onMouseDown);
    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }

    private void onMouseDown(EasyEvent event) {
        if (!(event instanceof EasyMouseEvent mouseEvent)) return;
        Vec2 local = globalToLocalPositionPoint((float) mouseEvent.getMouseX(), (float) mouseEvent.getMouseY());
        int bx = getWidth() - 12;
        if (local.x >= bx && local.x < bx + 10 && local.y >= 2 && local.y < 12) {
            if (onClose != null) onClose.run();
            event.setCanceled(true);
        }
    }

    private static void fillBorderedRect(GuiGraphics gfx, int x, int y, int w, int h, int bg, int border) {
        gfx.fill(x, y, x + w, y + h, bg);
        gfx.fill(x, y, x + w, y + 1, border);
        gfx.fill(x, y + h - 1, x + w, y + h, border);
        gfx.fill(x, y, x + 1, y + h, border);
        gfx.fill(x + w - 1, y, x + w, y + h, border);
    }

    private static List<String> wrapText(String text, Font font, int maxWidth) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) return lines;

        String[] words = text.split(" ");
        StringBuilder current = new StringBuilder();

        for (String word : words) {
            String candidate = current.isEmpty() ? word : current + " " + word;
            if (font.width(candidate) > maxWidth) {
                if (!current.isEmpty()) lines.add(current.toString());
                current = new StringBuilder(word);
            } else {
                current = new StringBuilder(candidate);
            }
        }

        if (!current.isEmpty()) lines.add(current.toString());
        return lines;
    }

    private static String capitalizePath(ResourceLocation pathId) {
        if (pathId == null) return "Unknown";
        String raw = pathId.getPath();
        if (raw.isEmpty()) return raw;
        return Character.toUpperCase(raw.charAt(0)) + raw.substring(1);
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        int w = getWidth();

        IEntityData entityData = mc.player == null ? null : mc.player.getData(ModAttachments.ENTITY_DATA);
        IPhysique physique = entityData == null ? null : entityData.getPhysique();

        BG.render(gfx);
        gfx.drawString(font, "PHYSIQUE", 5, 3, 0xFF4FC3F7, false);
        gfx.drawString(font, "\u00d7", w - 12 + (10 - font.width("\u00d7") + 1) / 2, 3, 0xFFFF5555, false);

        if (physique == null) {
            gfx.drawString(font, "No physique", 6, 30, 0xFF666666, false);
            gfx.drawString(font, "assigned.", 6, 40, 0xFF666666, false);
            super.render(gfx, mouseX, mouseY, partialTick);
            return;
        }

        fillBorderedRect(gfx, HEADER_X, HEADER_Y, HEADER_W, HEADER_H, 0x2E006396, 0xFF00D0FF);

        String title = physique.getDisplayTitle() != null
                ? physique.getDisplayTitle().getString()
                : "Unknown Physique";

        int maxHeaderWidth = HEADER_W - 6;
        int textWidth = font.width(title);
        float scale = textWidth <= maxHeaderWidth ? 1.0f : Math.max(0.6f, (float) maxHeaderWidth / textWidth);
        int scaledWidth = (int) (textWidth * scale);
        int centeredX = HEADER_X + (HEADER_W - scaledWidth) / 2;

        gfx.pose().pushPose();
        gfx.pose().scale(scale, scale, 1.0f);
        gfx.drawString(
                font,
                title,
                (int) (centeredX / scale),
                (int) ((HEADER_Y + 3) / scale),
                0xFFFFFFFF,
                false
        );
        gfx.pose().popPose();

        fillBorderedRect(gfx, 6, 40, 148, 132, 0x18050810, 0x44006396);

        int y = 46;

        gfx.drawString(font, "Description", 9, y, 0xFFAAAAAA, false);
        y += 10;

        String desc = physique.getShortDescription() != null
                ? physique.getShortDescription().getString()
                : "";

        if (desc.isEmpty()) {
            gfx.drawString(font, "No description", 9, y, 0xFF666666, false);
            y += 12;
        } else {
            for (String line : wrapText(desc, font, 140)) {
                gfx.drawString(font, line, 9, y, 0xFFCCCCCC, false);
                y += 10;
                if (y > 92) break;
            }
        }

        y += 4;
        gfx.fill(8, y, 152, y + 1, 0x55006396);
        y += 6;

        gfx.drawString(font, "Unlocked Paths", 9, y, 0xFFAAAAAA, false);
        y += 10;

        List<ResourceLocation> paths = new ArrayList<>(physique.paths());
        paths.sort(Comparator.comparing(ResourceLocation::toString));

        if (paths.isEmpty()) {
            gfx.drawString(font, "None", 9, y, 0xFF666666, false);
            y += 10;
        } else {
            for (ResourceLocation pathId : paths) {
                if (y > 132) break;
                gfx.drawString(font, "- " + capitalizePath(pathId), 9, y, 0xFFFFFFFF, false);
                y += 10;
            }
        }

        y += 4;
        gfx.fill(8, y, 152, y + 1, 0x55006396);
        y += 6;

        gfx.drawString(font, "Path Bonuses", 9, y, 0xFFAAAAAA, false);
        y += 10;

        List<Map.Entry<ResourceLocation, Double>> bonuses = new ArrayList<>(physique.pathBonuses().entrySet());
        bonuses.sort(Comparator.comparing(e -> e.getKey().toString()));

        if (bonuses.isEmpty()) {
            gfx.drawString(font, "None", 9, y, 0xFF666666, false);
        } else {
            for (Map.Entry<ResourceLocation, Double> entry : bonuses) {
                if (y > 166) break;
                String line = capitalizePath(entry.getKey()) + ": +" + String.format("%.2f", entry.getValue());
                gfx.drawString(font, line, 9, y, 0xFF99EEFF, false);
                y += 10;
            }
        }

        super.render(gfx, mouseX, mouseY, partialTick);
    }
}