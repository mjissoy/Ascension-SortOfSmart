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
import net.thejadeproject.ascension.refactor_packages.paths.IPath;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechnique;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TechniquesPanel extends RenderableElement {

    private enum TechniqueState {
        CURRENT,
        LEARNED
    }

    private static final ITextureData BG = new TextureDataSubsection(
            ResourceLocation.fromNamespaceAndPath(
                    AscensionCraft.MOD_ID,
                    "textures/gui/screen/cultivation_techniques_panel.png"
            ),
            160, 180, 0, 0, 160, 180
    );

    private static final int LIST_X = 6;
    private static final int LIST_Y = 20;
    private static final int LIST_W = 62;
    private static final int LIST_H = 146;
    private static final int ROW_H = 14;

    private static final int DETAIL_X = 74;
    private static final int DETAIL_Y = 20;
    private static final int DETAIL_W = 80;
    private static final int DETAIL_H = 146;

    private Runnable onClose;

    private final List<ResourceLocation> visibleTechniques = new ArrayList<>();
    private ResourceLocation selectedTechniqueId = null;

    public TechniquesPanel(UIFrame frame) {
        super(frame);
        setWidth(160);
        setHeight(180);
        addEventListener(EasyEvents.MOUSE_DOWN_EVENT, this::onMouseDown);
    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }

    private void rebuildTechniqueList() {
        visibleTechniques.clear();

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        IEntityData entityData = mc.player.getData(ModAttachments.ENTITY_DATA);
        if (entityData == null) return;

        Set<ResourceLocation> collected = new LinkedHashSet<>();

        for (PathData pathData : entityData.getAllPathData()) {
            if (pathData == null) continue;

            ResourceLocation current = pathData.getLastUsedTechnique();
            if (current != null) {
                collected.add(current);
            }

            for (ResourceLocation past : pathData.getTechniqueHistory()) {
                if (past != null) {
                    collected.add(past);
                }
            }
        }

        visibleTechniques.addAll(collected);
        visibleTechniques.removeIf(id -> AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(id) == null);

        visibleTechniques.sort(Comparator.comparing(id -> {
            ITechnique technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(id);
            if (technique == null || technique.getDisplayTitle() == null) {
                return id.toString();
            }
            return technique.getDisplayTitle().getString();
        }));

        if (selectedTechniqueId == null || !visibleTechniques.contains(selectedTechniqueId)) {
            selectedTechniqueId = visibleTechniques.isEmpty() ? null : visibleTechniques.get(0);
        }
    }

    private TechniqueState getTechniqueState(IEntityData entityData, ResourceLocation techniqueId, ITechnique technique) {
        if (entityData == null || technique == null) return TechniqueState.LEARNED;

        PathData pathData = entityData.getPathData(technique.getPath());
        if (pathData == null) return TechniqueState.LEARNED;

        ResourceLocation current = pathData.getLastUsedTechnique();
        if (techniqueId.equals(current)) {
            return TechniqueState.CURRENT;
        }

        return TechniqueState.LEARNED;
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

    private static String truncateToWidth(String text, Font font, int maxWidth) {
        if (text == null) return "";
        if (font.width(text) <= maxWidth) return text;

        String out = text;
        while (!out.isEmpty() && font.width(out + "...") > maxWidth) {
            out = out.substring(0, out.length() - 1);
        }
        return out + "...";
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        int w = getWidth();

        rebuildTechniqueList();

        IEntityData entityData = mc.player == null ? null : mc.player.getData(ModAttachments.ENTITY_DATA);

        BG.render(gfx);
        gfx.drawString(font, "TECHNIQUES", 5, 3, 0xFF4FC3F7, false);
        gfx.drawString(font, "\u00d7", w - 12 + (10 - font.width("\u00d7") + 1) / 2, 3, 0xFFFF5555, false);

        fillBorderedRect(gfx, LIST_X - 1, LIST_Y - 1, LIST_W + 2, LIST_H, 0x18050810, 0x44006396);

        if (visibleTechniques.isEmpty()) {
            gfx.drawString(font, "No", LIST_X + 4, LIST_Y + 6, 0xFF666666, false);
            gfx.drawString(font, "techniques", LIST_X + 4, LIST_Y + 16, 0xFF666666, false);
        } else {
            for (int i = 0; i < visibleTechniques.size(); i++) {
                int rowY = LIST_Y + i * ROW_H;
                if (rowY + ROW_H > LIST_Y + LIST_H - 2) break;

                ResourceLocation techniqueId = visibleTechniques.get(i);
                ITechnique technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(techniqueId);
                if (technique == null) continue;

                TechniqueState state = getTechniqueState(entityData, techniqueId, technique);
                boolean selected = techniqueId.equals(selectedTechniqueId);

                int bgCol;
                int borderCol;
                int textCol;

                if (selected) {
                    bgCol = 0x2E006396;
                    borderCol = 0xFF00D0FF;
                    textCol = 0xFFFFFFFF;
                } else if (state == TechniqueState.CURRENT) {
                    bgCol = 0x2A00384A;
                    borderCol = 0xFF00D0FF;
                    textCol = 0xFF99EEFF;
                } else {
                    bgCol = 0x12000000;
                    borderCol = 0xFF444444;
                    textCol = 0xFF777777;
                }

                fillBorderedRect(gfx, LIST_X, rowY, LIST_W, ROW_H - 1, bgCol, borderCol);

                String name = technique.getDisplayTitle() != null
                        ? technique.getDisplayTitle().getString()
                        : techniqueId.getPath();
                name = truncateToWidth(name, font, LIST_W - 6);

                gfx.drawString(font, name, LIST_X + 3, rowY + 3, textCol, false);
            }
        }

        fillBorderedRect(gfx, DETAIL_X - 1, DETAIL_Y - 1, DETAIL_W + 2, DETAIL_H, 0x18050810, 0x44006396);

        if (selectedTechniqueId == null) {
            gfx.drawString(font, "Select a", DETAIL_X + 3, DETAIL_Y + 6, 0xFF666666, false);
            gfx.drawString(font, "technique", DETAIL_X + 3, DETAIL_Y + 16, 0xFF666666, false);
            super.render(gfx, mouseX, mouseY, partialTick);
            return;
        }

        ITechnique selected = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(selectedTechniqueId);
        if (selected == null) {
            gfx.drawString(font, "Missing", DETAIL_X + 3, DETAIL_Y + 6, 0xFFFF6666, false);
            gfx.drawString(font, "technique", DETAIL_X + 3, DETAIL_Y + 16, 0xFFFF6666, false);
            super.render(gfx, mouseX, mouseY, partialTick);
            return;
        }

        TechniqueState selectedState = getTechniqueState(entityData, selectedTechniqueId, selected);

        int y = DETAIL_Y + 4;

        String title = selected.getDisplayTitle() != null
                ? selected.getDisplayTitle().getString()
                : selectedTechniqueId.getPath();

        for (String line : wrapText(title, font, DETAIL_W - 6)) {
            gfx.drawString(font, line, DETAIL_X + 3, y, 0xFF4FC3F7, false);
            y += 10;
            if (y > DETAIL_Y + 24) break;
        }

        y += 1;
        gfx.fill(DETAIL_X + 2, y, DETAIL_X + DETAIL_W - 2, y + 1, 0x55006396);
        y += 6;

        gfx.drawString(font, "Path", DETAIL_X + 3, y, 0xFFAAAAAA, false);
        y += 10;
        gfx.drawString(font, capitalizePath(selected.getPath()), DETAIL_X + 3, y, 0xFFFFFFFF, false);
        y += 14;

        gfx.drawString(font, "Status", DETAIL_X + 3, y, 0xFFAAAAAA, false);
        y += 10;
        String statusText = selectedState == TechniqueState.CURRENT ? "Current" : "Practiced";
        int statusColor = selectedState == TechniqueState.CURRENT ? 0xFF99EEFF : 0xFF888888;
        gfx.drawString(font, statusText, DETAIL_X + 3, y, statusColor, false);
        y += 14;

        gfx.drawString(font, "Technique Cap", DETAIL_X + 3, y, 0xFFAAAAAA, false);
        y += 10;

        String capRealm = selected.getMajorRealmName(selected.getMaxMajorRealm()).getString()
                + " (" + selected.getMaxMajorRealm() + " / "
                + selected.getMaxMinorRealm(selected.getMaxMajorRealm()) + ")";

        for (String line : wrapText(capRealm, font, DETAIL_W - 6)) {
            gfx.drawString(font, line, DETAIL_X + 3, y, 0xFFFFFFFF, false);
            y += 10;
            if (y > DETAIL_Y + 95) break;
        }

        y += 4;
        gfx.drawString(font, "Description", DETAIL_X + 3, y, 0xFFAAAAAA, false);
        y += 10;

        String desc = selected.getShortDescription() != null
                ? selected.getShortDescription().getString()
                : "";

        if (desc.isEmpty()) {
            gfx.drawString(font, "No description", DETAIL_X + 3, y, 0xFF666666, false);
        } else {
            for (String line : wrapText(desc, font, DETAIL_W - 6)) {
                gfx.drawString(font, line, DETAIL_X + 3, y, 0xFFCCCCCC, false);
                y += 10;
                if (y > DETAIL_Y + DETAIL_H - 8) break;
            }
        }

        super.render(gfx, mouseX, mouseY, partialTick);
    }

    public void tryClick(double lx, double ly) {
        if (lx >= LIST_X && lx <= LIST_X + LIST_W && ly >= LIST_Y && ly <= LIST_Y + LIST_H) {
            int index = (int) ((ly - LIST_Y) / ROW_H);
            if (index >= 0 && index < visibleTechniques.size()) {
                selectedTechniqueId = visibleTechniques.get(index);
            }
        }
    }
}

//package net.thejadeproject.ascension.gui.elements.cultivation;
//
//import net.lucent.easygui.gui.RenderableElement;
//import net.lucent.easygui.gui.UIFrame;
//import net.lucent.easygui.gui.events.EasyEvents;
//import net.lucent.easygui.gui.events.type.EasyEvent;
//import net.lucent.easygui.gui.events.type.EasyMouseEvent;
//import net.lucent.easygui.gui.textures.ITextureData;
//import net.lucent.easygui.gui.textures.TextureDataSubsection;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.phys.Vec2;
//import net.thejadeproject.ascension.AscensionCraft;
//
//public class TechniquesPanel extends RenderableElement {
//
//    private enum TechniqueState {
//        CURRENT,
//        LEARNED,
//        AVAILABLE
//    }
//
//    private static final ITextureData BG = new TextureDataSubsection(
//            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/gui/screen/cultivation_techniques_panel.png"),
//            160, 180, 0, 0, 160, 180);
//
//    private Runnable onClose;
//
//    public TechniquesPanel(UIFrame frame) {
//        super(frame);
//        setWidth(160);
//        setHeight(180);
//        addEventListener(EasyEvents.MOUSE_DOWN_EVENT, this::onMouseDown);
//    }
//
//    public void setOnClose(Runnable onClose) {
//        this.onClose = onClose;
//    }
//
//    private void onMouseDown(EasyEvent event) {
//        if (!(event instanceof EasyMouseEvent mouseEvent)) return;
//        Vec2 local = globalToLocalPositionPoint((float) mouseEvent.getMouseX(), (float) mouseEvent.getMouseY());
//        int bx = getWidth() - 12;
//        if (local.x >= bx && local.x < bx + 10 && local.y >= 2 && local.y < 12) {
//            if (onClose != null) onClose.run();
//            event.setCanceled(true);
//        }
//    }
//
//    @Override
//    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
//
//        var font = Minecraft.getInstance().font;
//        int w = getWidth();
//        BG.render(gfx);
//        gfx.drawString(font, "TECHNIQUES", 5, 3, 0xFF4FC3F7, false);
//        gfx.drawString(font, "\u00d7", w - 12 + (10 - font.width("\u00d7") + 1) / 2, 3, 0xFFFF5555, false);
//        gfx.drawString(font, "Coming soon", 6, 30, 0xFF666666, false);
//        super.render(gfx, mouseX, mouseY, partialTick);
//    }
//}