package net.thejadeproject.ascension.gui.elements.cultivation;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.elements.built_in.EasyLabel;
import net.lucent.easygui.gui.events.EasyEvents;
import net.lucent.easygui.gui.events.type.EasyEvent;
import net.lucent.easygui.gui.events.type.EasyMouseEvent;
import net.lucent.easygui.gui.layout.positioning.rules.PositioningRules;
import net.lucent.easygui.gui.textures.ITextureData;
import net.lucent.easygui.gui.textures.TextureDataSubsection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.gui.elements.skill_view.SkillMenuContainer;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;

import java.util.ArrayList;
import java.util.List;

public class CultivationMenuContainer extends RenderableElement {

    private static final ITextureData BG = new TextureDataSubsection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/gui/screen/cultivation_menu.png"),
            228, 180, 0, 0, 228, 180);


    // Path Tabs
    private static final ITextureData PATH_TAB_ACTIVE = new TextureDataSubsection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/gui/screen/tabs/path_tab_active.png"),
            62, 14, 0, 0, 62, 14);

    private static final ITextureData PATH_TAB_IDLE = new TextureDataSubsection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/gui/screen/tabs/path_tab_idle.png"),
            62, 14, 0, 0, 62, 14);


    // Skill, Technique, Physique, Stats Tabs
    private static final ITextureData SKIL_TAB_ACTIVE = new TextureDataSubsection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/gui/screen/tabs/skill_tab_active.png"),
            62, 12, 0, 0, 62, 12);

    private static final ITextureData SKIL_TAB_IDLE = new TextureDataSubsection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/gui/screen/tabs/skill_tab_idle.png"),
            62, 12, 0, 0, 62, 12);

    private static final ITextureData TECH_TAB_ACTIVE = new TextureDataSubsection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/gui/screen/tabs/tech_tab_active.png"),
            62, 12, 0, 0, 62, 12);

    private static final ITextureData TECH_TAB_IDLE = new TextureDataSubsection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/gui/screen/tabs/tech_tab_idle.png"),
            62, 12, 0, 0, 62, 12);

    private static final ITextureData PHYS_TAB_ACTIVE = new TextureDataSubsection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/gui/screen/tabs/physique_tab_active.png"),
            62, 12, 0, 0, 62, 12);

    private static final ITextureData PHYS_TAB_IDLE = new TextureDataSubsection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/gui/screen/tabs/physique_tab_idle.png"),
            62, 12, 0, 0, 62, 12);

    private static final ITextureData STAT_TAB_ACTIVE = new TextureDataSubsection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/gui/screen/tabs/stats_tab_active.png"),
            62, 12, 0, 0, 62, 12);

    private static final ITextureData STAT_TAB_IDLE = new TextureDataSubsection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/gui/screen/tabs/stats_tab_idle.png"),
            62, 12, 0, 0, 62, 12);

//    private static final ITextureData DAO_TAB_ACTIVE = new TextureDataSubsection(
//            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/gui/screen/tabs/dao_tab_active.png"),
//            62, 12, 0, 0, 62, 12);
//
//    private static final ITextureData DAO_TAB_IDLE = new TextureDataSubsection(
//            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/gui/screen/tabs/dao_tab_idle.png"),
//            62, 12, 0, 0, 62, 12);

    private static final ITextureData BOTTOM_TAB_REJECTED = new TextureDataSubsection(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/gui/screen/tabs/bottom_tab_rejected.png"),
            62, 12, 0, 0, 62, 12);

    private static final int WIDTH = 228;
    private static final int HEIGHT = 180;
    private static final int SIDEBAR_W = 68;
    private static final int PANEL_W = 160;
    private static final int TAB_H = 14;
    private static final int BOTTOM_TAB_H = 12;
    private static final int TAB_GAP = 2;
    private static final int TAB_X = 3;
    private static final int TAB_W = SIDEBAR_W - 6;
    private static final int SIDE_GAP = 6;

    private static final int SKILLS_SHIFT = 163;
    private static final int SKILLS_X     = 117;

    private final List<ResourceLocation> pathTabs = new ArrayList<>();
    private final PathDetailPanel pathPanel;
    private final StatsPanel statsPanel;
    private final TechniquesPanel techniquesPanel;
    private final PhysiquePanel physiquePanel;
    private final TechniquePopup techniquePopup;

    private ResourceLocation selectedPath = null;
    private RenderableElement centerPanel = null;
    private RenderableElement rightPanel = null;
    private RenderableElement leftPanel = null;
    private RenderableElement rejectedPanel = null;
    private long rejectedTime = 0;
    private SkillMenuContainer skillMenu = null;
    private boolean skillsOpen = false;



    public CultivationMenuContainer(UIFrame frame) {
        super(frame);
        getPositioning().setPositioningRule(PositioningRules.CENTER);
        getPositioning().setX(-WIDTH / 2);
        getPositioning().setY(-HEIGHT / 2);
        setWidth(WIDTH);
        setHeight(HEIGHT);

        IEntityData entityData = Minecraft.getInstance().player.getData(ModAttachments.ENTITY_DATA);
        for (ResourceLocation pathId : AscensionRegistries.Paths.PATHS_REGISTRY.keySet()) {
            if (entityData != null && entityData.hasPath(pathId)) {
                pathTabs.add(pathId);
            }
        }

        techniquePopup = new TechniquePopup(frame);
        techniquePopup.getPositioning().setX(SIDEBAR_W - 1);
        techniquePopup.getPositioning().setY(0);
        techniquePopup.setActive(false);

        pathPanel = new PathDetailPanel(frame, techniquePopup);
        pathPanel.getPositioning().setX(SIDEBAR_W - 1);
        pathPanel.getPositioning().setY(0);
        pathPanel.setActive(false);

        statsPanel = new StatsPanel(frame);
        statsPanel.getPositioning().setX(SIDEBAR_W - 1);
        statsPanel.getPositioning().setY(0);
        statsPanel.setActive(false);

        techniquesPanel = new TechniquesPanel(frame);
        techniquesPanel.getPositioning().setX(SIDEBAR_W - 1);
        techniquesPanel.getPositioning().setY(0);
        techniquesPanel.setActive(false);

        physiquePanel = new PhysiquePanel(frame);
        physiquePanel.getPositioning().setX(SIDEBAR_W - 1);
        physiquePanel.getPositioning().setY(0);
        physiquePanel.setActive(false);

        addChild(pathPanel);
        addChild(statsPanel);
        addChild(techniquesPanel);
        addChild(physiquePanel);
        addChild(techniquePopup);

        EasyLabel pathsLabel = new EasyLabel(frame);
        pathsLabel.setText(Component.literal("PATHS"));
        pathsLabel.setTextColor(0xFF4FC3F7);
        pathsLabel.getPositioning().setX(6);
        pathsLabel.getPositioning().setY(6);
        addChild(pathsLabel);

        if (!pathTabs.isEmpty()) {
            selectPath(pathTabs.get(0));
        }

        addEventListener(EasyEvents.MOUSE_DOWN_EVENT, this::onMouseDown);
    }

    private void selectPath(ResourceLocation pathId) {
        if (centerPanel != null) {
            centerPanel.setActive(false);
            centerPanel = null;
        }
        selectedPath = pathId;
        pathPanel.setPath(pathId);
        pathPanel.setActive(true);
    }

    private void selectStats() {
        if (skillsOpen) { rejectedPanel = statsPanel; rejectedTime = System.currentTimeMillis(); return; }
        if (!toggleSidePanel(statsPanel)) { rejectedPanel = statsPanel; rejectedTime = System.currentTimeMillis(); }
    }

    private void selectTechniques() {
        if (skillsOpen) { rejectedPanel = techniquesPanel; rejectedTime = System.currentTimeMillis(); return; }
        if (!toggleSidePanel(techniquesPanel)) { rejectedPanel = techniquesPanel; rejectedTime = System.currentTimeMillis(); }
    }

    private void selectPhysique() {
        if (skillsOpen) { rejectedPanel = physiquePanel; rejectedTime = System.currentTimeMillis(); return; }
        if (!toggleSidePanel(physiquePanel)) { rejectedPanel = physiquePanel; rejectedTime = System.currentTimeMillis(); }
    }

    private void selectNone() {
        selectedPath = null;
        pathPanel.setActive(false);
        techniquePopup.setActive(false);
    }

    public void setSkillMenu(SkillMenuContainer skillMenu) {
        this.skillMenu = skillMenu;
    }

    private void selectSkills() {
        skillsOpen = !skillsOpen;
        if (skillsOpen) {
            if (leftPanel != null) { leftPanel.setActive(false); leftPanel = null; }
            if (rightPanel != null) { rightPanel.setActive(false); rightPanel = null; }
            if (centerPanel != null) {
                centerPanel.setActive(false);
                centerPanel = null;
                if (selectedPath != null) pathPanel.setActive(true);
            }
            techniquePopup.setActive(false);
            getPositioning().setX(-WIDTH / 2 - SKILLS_SHIFT);
            if (skillMenu != null) {
                skillMenu.getPositioning().setX(SKILLS_X);
                skillMenu.getPositioning().setY(0);
                skillMenu.markDirty();
                skillMenu.setActive(true);
                skillMenu.setOnClose(() -> { if (skillsOpen) selectSkills(); });
            }
        } else {
            getPositioning().setX(-WIDTH / 2);
            if (skillMenu != null) skillMenu.setActive(false);
        }
    }

    private boolean toggleSidePanel(RenderableElement panel) {
        if (panel == centerPanel) {
            centerPanel = null;
            panel.setActive(false);
            if (selectedPath != null) pathPanel.setActive(true);
            return true;
        } else if (panel == rightPanel) {
            rightPanel = null;
            panel.setActive(false);
            return true;
        } else if (panel == leftPanel) {
            leftPanel = null;
            panel.setActive(false);
            return true;
        } else if (centerPanel == null) {
            centerPanel = panel;
            panel.getPositioning().setX(SIDEBAR_W - 1);
            panel.getPositioning().setY(0);
            pathPanel.setActive(false);
            panel.setActive(true);
            registerCloseCallback(panel);
            return true;
        } else if (rightPanel == null) {
            rightPanel = panel;
            panel.getPositioning().setX(WIDTH + SIDE_GAP);
            panel.getPositioning().setY(0);
            panel.setActive(true);
            registerCloseCallback(panel);
            return true;
        } else if (leftPanel == null) {
            leftPanel = panel;
            panel.getPositioning().setX(-panel.getWidth() - SIDE_GAP);
            panel.getPositioning().setY(0);
            panel.setActive(true);
            registerCloseCallback(panel);
            return true;
        }
        return false;
    }

    private void registerCloseCallback(RenderableElement panel) {
        if (panel instanceof TechniquesPanel tp) tp.setOnClose(() -> toggleSidePanel(panel));
        else if (panel instanceof PhysiquePanel pp) pp.setOnClose(() -> toggleSidePanel(panel));
        else if (panel instanceof StatsPanel sp) sp.setOnClose(() -> toggleSidePanel(panel));
    }

    private RenderableElement activeContentPanel() {
        if (centerPanel != null) return centerPanel;
        if (selectedPath != null) return pathPanel;
        return null;
    }

    private void onMouseDown(EasyEvent event) {
        if (!(event instanceof EasyMouseEvent mouseEvent)) return;
        Vec2 local = globalToLocalPositionPoint((float) mouseEvent.getMouseX(), (float) mouseEvent.getMouseY());
        double mx = local.x;
        double my = local.y;

        if (techniquePopup.isActive()) {
            int popupX = techniquePopup.getPositioning().getX();
            int popupY = techniquePopup.getPositioning().getY();

            double ppx = mx - popupX;
            double ppy = my - popupY;

            if (ppx >= 0 && ppx < techniquePopup.getWidth() && ppy >= 0 && ppy < techniquePopup.getHeight()) {
                int closeBx = techniquePopup.getWidth() - 12;
                if (ppx >= closeBx && ppx < closeBx + 10 && ppy >= 2 && ppy < 12) {
                    techniquePopup.setActive(false);
                    event.setCanceled(true);
                    return;
                }

                event.setCanceled(true);
                return;
            }
        }

        RenderableElement activePanel = activeContentPanel();
        if (activePanel != null) {
            int panelX = activePanel.getPositioning().getX();
            int panelY = activePanel.getPositioning().getY();

            int closeBx = panelX + activePanel.getWidth() - 12;
            if (mx >= closeBx && mx < closeBx + 10 && my >= panelY + 2 && my < panelY + 12) {
                selectNone();
                event.setCanceled(true);
                return;
            }

        }

        int tabsStartY = 19;
        for (int i = 0; i < pathTabs.size(); i++) {
            int tabY = tabsStartY + i * (TAB_H + TAB_GAP);
            if (mx >= TAB_X && mx <= TAB_X + TAB_W && my >= tabY && my <= tabY + TAB_H) {
                selectPath(pathTabs.get(i));
                event.setCanceled(true);
                return;
            }
        }

        int statsTabY = HEIGHT - BOTTOM_TAB_H - 4;
        int physiqueTabY = statsTabY - BOTTOM_TAB_H - TAB_GAP;
        int techniquesTabY = physiqueTabY - BOTTOM_TAB_H - TAB_GAP;
        int skillsTabY = techniquesTabY - BOTTOM_TAB_H - TAB_GAP;

        if (mx >= TAB_X && mx <= TAB_X + TAB_W && my >= skillsTabY && my <= skillsTabY + BOTTOM_TAB_H) {
            selectSkills();
            event.setCanceled(true);
            return;
        }
        if (mx >= TAB_X && mx <= TAB_X + TAB_W && my >= techniquesTabY && my <= techniquesTabY + BOTTOM_TAB_H) {
            selectTechniques();
            event.setCanceled(true);
            return;
        }
        if (mx >= TAB_X && mx <= TAB_X + TAB_W && my >= physiqueTabY && my <= physiqueTabY + BOTTOM_TAB_H) {
            selectPhysique();
            event.setCanceled(true);
            return;
        }
        if (mx >= TAB_X && mx <= TAB_X + TAB_W && my >= statsTabY && my <= statsTabY + BOTTOM_TAB_H) {
            selectStats();
            event.setCanceled(true);
            return;
        }

        if (activePanel != null) {
            int panelX = activePanel.getPositioning().getX();
            int panelY = activePanel.getPositioning().getY();
            double px = mx - panelX;
            double py = my - panelY;

            if (px >= 0 && px < activePanel.getWidth() && py >= 0 && py < activePanel.getHeight()) {
                if (centerPanel == statsPanel) {
                    statsPanel.tryClick(px, py);
                } else if (centerPanel == techniquesPanel) {
                    techniquesPanel.tryClick(px, py);
                } else if (centerPanel == physiquePanel) {
                } else if (centerPanel == null && selectedPath != null) {
                    pathPanel.tryClick(px, py);
                }
            }
        }
        event.setCanceled(true);
    }

    private static void fillBorderedRect(GuiGraphics gfx, int x, int y, int w, int h, int bg, int border) {
        gfx.fill(x, y, x + w, y + h, bg);
        gfx.fill(x, y, x + w, y + 1, border);
        gfx.fill(x, y + h - 1, x + w, y + h, border);
        gfx.fill(x, y, x + 1, y + h, border);
        gfx.fill(x + w - 1, y, x + w, y + h, border);
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        Font font = Minecraft.getInstance().font;

        BG.render(gfx);

        int tabsStartY = 19;
        for (int i = 0; i < pathTabs.size(); i++) {
            ResourceLocation pathId = pathTabs.get(i);
            int tabY = tabsStartY + i * (TAB_H + TAB_GAP);
            boolean active = pathId.equals(selectedPath);

            renderTextureAt(gfx, active ? PATH_TAB_ACTIVE : PATH_TAB_IDLE, TAB_X, tabY);

            String rawName = pathId.getPath();
            String name = rawName.isEmpty()
                    ? rawName
                    : Character.toUpperCase(rawName.charAt(0)) + rawName.substring(1);

            int nameX = TAB_X + (TAB_W - font.width(name)) / 2;
            int nameY = tabY + (TAB_H - 8) / 2;
            gfx.drawString(font, name, nameX, nameY, active ? 0xFF4FC3F7 : 0xFF888888, false);
        }

        int statsTabY = HEIGHT - BOTTOM_TAB_H - 4;
        int physiqueTabY = statsTabY - BOTTOM_TAB_H - TAB_GAP;
        int techniquesTabY = physiqueTabY - BOTTOM_TAB_H - TAB_GAP;
        int skillsTabY = techniquesTabY - BOTTOM_TAB_H - TAB_GAP;

        boolean showRejected = rejectedPanel != null && (System.currentTimeMillis() - rejectedTime) < 600;
        boolean statsActive = statsPanel == centerPanel || statsPanel == rightPanel || statsPanel == leftPanel;
        boolean techniquesActive = techniquesPanel == centerPanel || techniquesPanel == rightPanel || techniquesPanel == leftPanel;
        boolean physiqueActive = physiquePanel == centerPanel || physiquePanel == rightPanel || physiquePanel == leftPanel;

        renderBottomTab(gfx, font, skillsTabY, "Skills", skillsOpen, false,
                SKIL_TAB_IDLE, SKIL_TAB_ACTIVE);

        renderBottomTab(gfx, font, techniquesTabY, "Techniques", techniquesActive,
                showRejected && rejectedPanel == techniquesPanel,
                TECH_TAB_IDLE, TECH_TAB_ACTIVE);

        renderBottomTab(gfx, font, physiqueTabY, "Physique", physiqueActive,
                showRejected && rejectedPanel == physiquePanel,
                PHYS_TAB_IDLE, PHYS_TAB_ACTIVE);

        renderBottomTab(gfx, font, statsTabY, "Stats", statsActive,
                showRejected && rejectedPanel == statsPanel,
                STAT_TAB_IDLE, STAT_TAB_ACTIVE);
        super.render(gfx, mouseX, mouseY, partialTick);
    }

    private void renderBottomTab(
            GuiGraphics gfx,
            Font font,
            int tabY,
            String label,
            boolean active,
            boolean rejected,
            ITextureData idleTexture,
            ITextureData activeTexture
    ) {
        int textY = tabY + (BOTTOM_TAB_H - 8) / 2;
        int textX = TAB_X + (TAB_W - font.width(label)) / 2;

        if (rejected) {
            renderTextureAt(gfx, BOTTOM_TAB_REJECTED, TAB_X, tabY);
            gfx.drawString(font, label, textX, textY, 0xFFFF5555, false);
        } else if (active) {
            renderTextureAt(gfx, activeTexture, TAB_X, tabY);
            gfx.drawString(font, label, textX, textY, 0xFFFFFFFF, false);
        } else {
            renderTextureAt(gfx, idleTexture, TAB_X, tabY);
            gfx.drawString(font, label, textX, textY, 0xFF888888, false);
        }
    }

    private static void renderTextureAt(GuiGraphics gfx, ITextureData texture, int x, int y) {
        gfx.pose().pushPose();
        gfx.pose().translate(x, y, 0);
        texture.render(gfx);
        gfx.pose().popPose();
    }

}