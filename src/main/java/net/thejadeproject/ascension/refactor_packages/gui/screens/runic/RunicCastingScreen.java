package net.thejadeproject.ascension.refactor_packages.gui.screens.runic;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.elements.built_in.EasyButton;
import net.lucent.easygui.gui.elements.built_in.EasyLabel;
import net.lucent.easygui.gui.layout.positioning.rules.PositioningRules;
import net.lucent.easygui.screen.EasyScreen;
import net.thejadeproject.ascension.refactor_packages.gui.elements.general.ScrollBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.refactor_packages.network.server_bound.runic.CastRunicSequencePayload;
import net.thejadeproject.ascension.refactor_packages.runic.runes.IRunicRune;
import net.thejadeproject.ascension.refactor_packages.runic.runes.ModRunicRunes;
import net.thejadeproject.ascension.refactor_packages.runic.runes.RunicRuneType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class RunicCastingScreen extends EasyScreen {

    private final List<ResourceLocation> usableRunes;
    private final List<ResourceLocation> selectedRunes = new ArrayList<>();
    private final List<RuneButton> runeButtons = new ArrayList<>();
    private final Map<RunicRuneType, RunicRuneScrollBox> runeScrollBoxes = new EnumMap<>(RunicRuneType.class);
    private final Map<RunicRuneType, TextButton> tabButtons = new EnumMap<>(RunicRuneType.class);
    private RunicRuneType activeRuneType = RunicRuneType.SOURCE;
    private EasyLabel hoverLabel;
    private RenderableElement hoverBox;
    private final int maxRuneSlots;
    private final int durationSeconds;
    private EasyLabel selectedLabel;
    private int remainingTicks;
    private boolean closingSafely;


    public RunicCastingScreen(int maxRuneSlots, int durationSeconds, List<ResourceLocation> usableRunes) {
        super(Component.translatable("ascension.runic.casting.title"));

        this.maxRuneSlots = maxRuneSlots;
        this.durationSeconds = durationSeconds;
        this.usableRunes = List.copyOf(usableRunes);
        this.remainingTicks = Math.max(0, durationSeconds * 20);

        build(getUIFrame());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (usableRunes.isEmpty() || closingSafely || durationSeconds <= 0) {
            return;
        }

        remainingTicks--;

        if (remainingTicks <= 0) {
            closingSafely = true;
            PacketDistributor.sendToServer(new CastRunicSequencePayload(List.of()));
            Minecraft.getInstance().setScreen(null);
        }
    }

    @Override
    public void onClose() {
        if (!usableRunes.isEmpty() && !closingSafely) {
            PacketDistributor.sendToServer(new CastRunicSequencePayload(List.of()));
        }

        super.onClose();
    }

    private float getTimerProgress() {
        if (durationSeconds <= 0) {
            return 0.0F;
        }

        return Math.max(0.0F, Math.min(1.0F, remainingTicks / (durationSeconds * 20.0F)));
    }

    private Component getTimerText() {
        float secondsLeft = Math.max(0.0F, remainingTicks / 20.0F);
        return Component.translatable("ascension.runic.casting.timer", String.format("%.1f", secondsLeft));
    }


    private void build(UIFrame frame) {
        frame.setPauseGame(false);

        RunicPanel panel = new RunicPanel(frame);
        panel.setWidth(360);
        panel.setHeight(230);
        panel.getPositioning().setPositioningRule(PositioningRules.CENTER);
        panel.getPositioning().setX(-180);
        panel.getPositioning().setY(-115);
        frame.setRoot(panel);

        EasyLabel title = label(frame, Component.translatable("ascension.runic.casting.title"), 0, 8, 360, 12, 0xFFE8D8FF);
        title.setTextPositioningX(EasyLabel.TextPositionRule.CENTER);
        panel.addChild(title);

        EasyLabel info = label(
                frame,
                Component.translatable("ascension.runic.casting.info", maxRuneSlots, durationSeconds),
                0,
                23,
                360,
                10,
                0xFFBEB4D7
        );
        info.setTextPositioningX(EasyLabel.TextPositionRule.CENTER);
        info.setTextScale(0.8F);
        panel.addChild(info);

        TimerBar timerBar = new TimerBar(frame, 15, 36, 330, 6);
        panel.addChild(timerBar);

        TimerLabel timerLabel = new TimerLabel(frame, 15, 42, 330, 10);
        panel.addChild(timerLabel);

        selectedLabel = label(frame, Component.empty(), 15, 53, 330, 14, 0xFFFFFFFF);
        selectedLabel.setTextScale(0.85F);
        panel.addChild(selectedLabel);
        refreshSelectedLabel();

        hoverBox = new RenderableElement(frame, 190, 51) {
            @Override
            public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                guiGraphics.fill(0, 0, getWidth(), getHeight(), 0xCC12091F);
                guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), 0xFFB79CFF);
            }
        };

        hoverBox.setWidth(155);
        hoverBox.setHeight(22);
        hoverBox.setVisible(false);
        panel.addChild(hoverBox);

        hoverLabel = label(frame, Component.empty(), 6, 5, 143, 10, 0xFFE8D8FF);
        hoverLabel.setTextScale(0.75F);
        hoverBox.addChild(hoverLabel);

        if (usableRunes.isEmpty()) {
            addEmptyState(panel, frame);
        } else {
            addRuneTabs(panel, frame);
        }

        TextButton backspace = new TextButton(frame, 15, 195, 100, 18, Component.translatable("ascension.runic.casting.backspace")) {
            @Override
            public void onClick() {
                if (!selectedRunes.isEmpty()) {
                    selectedRunes.remove(selectedRunes.size() - 1);
                    refreshSelectedLabel();
                }
            }
        };
        panel.addChild(backspace);

        TextButton clear = new TextButton(frame, 130, 195, 100, 18, Component.translatable("ascension.runic.casting.clear")) {
            @Override
            public void onClick() {
                selectedRunes.clear();
                refreshSelectedLabel();
            }
        };
        panel.addChild(clear);

        TextButton cast = new TextButton(
                frame,
                245,
                195,
                100,
                18,
                Component.translatable(usableRunes.isEmpty()
                        ? "ascension.runic.casting.close"
                        : "ascension.runic.casting.cast")
        ) {
            @Override
            public void onClick() {
                if (usableRunes.isEmpty()) {
                    closingSafely = true;
                    Minecraft.getInstance().setScreen(null);
                    return;
                }

                if (selectedRunes.isEmpty()) {
                    return;
                }

                closingSafely = true;
                PacketDistributor.sendToServer(new CastRunicSequencePayload(List.copyOf(selectedRunes)));
                Minecraft.getInstance().setScreen(null);
            }
        };
        panel.addChild(cast);
    }

    private void addEmptyState(RenderableElement panel, UIFrame frame) {
        RenderableElement emptyBox = new RenderableElement(frame, 42, 78) {
            @Override
            public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                guiGraphics.fill(0, 0, getWidth(), getHeight(), 0x6612091F);
                guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), 0xAA7A5ACF);
            }
        };

        emptyBox.setWidth(276);
        emptyBox.setHeight(78);
        panel.addChild(emptyBox);

        EasyLabel title = label(
                frame,
                Component.translatable("ascension.runic.casting.empty.title"),
                0,
                10,
                276,
                12,
                0xFFE8D8FF
        );
        title.setTextPositioningX(EasyLabel.TextPositionRule.CENTER);
        title.setTextScale(0.9F);
        emptyBox.addChild(title);

        EasyLabel lineOne = label(
                frame,
                Component.translatable("ascension.runic.casting.empty.line_1"),
                12,
                31,
                252,
                10,
                0xFFBEB4D7
        );
        lineOne.setTextPositioningX(EasyLabel.TextPositionRule.CENTER);
        lineOne.setTextScale(0.75F);
        emptyBox.addChild(lineOne);

        EasyLabel lineTwo = label(
                frame,
                Component.translatable("ascension.runic.casting.empty.line_2"),
                12,
                46,
                252,
                10,
                0xFFBEB4D7
        );
        lineTwo.setTextPositioningX(EasyLabel.TextPositionRule.CENTER);
        lineTwo.setTextScale(0.75F);
        emptyBox.addChild(lineTwo);
    }

    private void addRune(ResourceLocation runeId) {
        if (selectedRunes.size() >= maxRuneSlots) {
            return;
        }

        selectedRunes.add(runeId);
        refreshSelectedLabel();
    }

    private void refreshSelectedLabel() {
        if (selectedLabel == null) {
            return;
        }

        if (selectedRunes.isEmpty()) {
            selectedLabel.setText(Component.translatable(
                    usableRunes.isEmpty()
                            ? "ascension.runic.casting.selected.no_usable"
                            : "ascension.runic.casting.selected.empty"
            ));
            return;
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < selectedRunes.size(); i++) {
            if (i > 0) {
                builder.append("  >  ");
            }

            builder.append(selectedRunes.get(i).getPath());
        }

        selectedLabel.setText(Component.literal(builder.toString()));
    }

    private static EasyLabel label(UIFrame frame, Component text, int x, int y, int width, int height, int color) {
        EasyLabel label = new EasyLabel(frame);
        label.setText(text);
        label.setTextColor(color);
        label.setWidth(width);
        label.setHeight(height);
        label.getPositioning().setX(x);
        label.getPositioning().setY(y);
        label.setScaleToFit(true);
        label.setTextPositioningY(EasyLabel.TextPositionRule.CENTER);
        return label;
    }


    private static class RunicRuneScrollBox extends ScrollBox {
        private static final int BUTTON_WIDTH = 78;
        private static final int BUTTON_HEIGHT = 16;
        private static final int GAP = 6;
        private static final int COLUMNS = 4;
        private static final int ROW_HEIGHT = BUTTON_HEIGHT + GAP;

        private RunicRuneScrollBox(UIFrame frame, int x, int y, int width, int height) {
            super(frame, ROW_HEIGHT);
            setWidth(width);
            setHeight(height);
            getPositioning().setX(x);
            getPositioning().setY(y);
            useCustomChildAdditionLogic = true;
        }

        @Override
        public void addChild(RenderableElement element) {
            super.addChild(element);
            updateVisibility(element);
        }

        @Override
        public void updatePos(RenderableElement element) {
            int index = getChildren().size();
            int col = index % COLUMNS;
            int row = index / COLUMNS;

            element.getPositioning().setFromRawX(col * (BUTTON_WIDTH + GAP));
            element.getPositioning().setFromRawY(row * ROW_HEIGHT);
        }

        @Override
        public int getMaxYScroll() {
            int rows = Math.ceilDiv(getChildren().size(), COLUMNS);
            return Math.max(0, rows * ROW_HEIGHT - getHeight());
        }

        @Override
        public void updateVisibility(RenderableElement element) {
            boolean visible = element.getPositioning().getY() + element.getHeight() > 0
                    && element.getPositioning().getY() < getHeight();
            element.setActive(visible);
            element.setVisible(visible);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            guiGraphics.fill(0, 0, getWidth(), getHeight(), 0x6612091F);
            guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), 0xAA7A5ACF);
        }
    }

    private class RuneButton extends TextButton {
        private final ResourceLocation runeId;

        private RuneButton(UIFrame frame, ResourceLocation runeId, int x, int y, int width, int height) {
            super(frame, x, y, width, height, getRuneName(runeId));
            this.runeId = runeId;
        }

        @Override
        public void onClick() {
            addRune(runeId);
        }
    }

    private static Component getRuneName(ResourceLocation runeId) {
        IRunicRune rune = ModRunicRunes.get(runeId);
        return rune == null ? Component.literal(runeId.getPath()) : rune.getName();
    }

    private static class TextButton extends EasyButton {
        private final Component text;

        private TextButton(UIFrame frame, int x, int y, int width, int height, Component text) {
            super(frame, x, y);
            this.text = text;

            setWidth(width);
            setHeight(height);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            boolean manuallyHovered = isPointBounded(mouseX, mouseY);

            int color = isPressed()
                    ? 0xCC6F4DBA
                    : manuallyHovered ? 0xAA4F3A7A : 0xAA211733;

            guiGraphics.fill(0, 0, getWidth(), getHeight(), color);
            guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), 0xFFB79CFF);

            int textY = (getHeight() - Minecraft.getInstance().font.lineHeight) / 2 + 1;

            guiGraphics.drawCenteredString(
                    Minecraft.getInstance().font,
                    text,
                    getWidth() / 2,
                    textY,
                    0xFFFFFFFF
            );
        }
    }

    private class TimerLabel extends EasyLabel {
        private TimerLabel(UIFrame frame, int x, int y, int width, int height) {
            super(frame);
            setText(Component.empty());
            setTextColor(0xFFBEB4D7);
            setWidth(width);
            setHeight(height);
            getPositioning().setX(x);
            getPositioning().setY(y);
            setScaleToFit(true);
            setTextScale(0.7F);
            setTextPositioningX(EasyLabel.TextPositionRule.CENTER);
            setTextPositioningY(EasyLabel.TextPositionRule.CENTER);
        }

        @Override
        public void renderTick(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            setText(getTimerText());
            super.renderTick(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    private class TimerBar extends RenderableElement {
        private TimerBar(UIFrame frame, int x, int y, int width, int height) {
            super(frame, x, y);
            setWidth(width);
            setHeight(height);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            guiGraphics.fill(0, 0, getWidth(), getHeight(), 0xAA12091F);

            int fillWidth = (int) (getWidth() * getTimerProgress());
            guiGraphics.fill(0, 0, fillWidth, getHeight(), 0xCC8A63FF);
            guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), 0xFFB79CFF);
        }
    }

    private class RunicPanel extends RenderableElement {
        private RunicPanel(UIFrame frame) {
            super(frame);
        }

        @Override
        public void renderTick(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            ResourceLocation hoveredRune = null;

            for (RuneButton button : runeButtons) {
                if (button.isPointBounded(mouseX, mouseY)) {
                    hoveredRune = button.runeId;
                    break;
                }
            }

            refreshHoverLabel(hoveredRune);

            super.renderTick(guiGraphics, mouseX, mouseY, partialTick);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            guiGraphics.fill(0, 0, getWidth(), getHeight(), 0xDD0D0718);
            guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), 0xFFE8D8FF);
        }
    }

    private void refreshHoverLabel(ResourceLocation hoveredRune) {
        if (hoverBox == null || hoverLabel == null) {
            return;
        }

        if (hoveredRune == null) {
            hoverBox.setVisible(false);
            hoverLabel.setText(Component.empty());
            return;
        }

        IRunicRune rune = ModRunicRunes.get(hoveredRune);

        if (rune == null) {
            hoverBox.setVisible(false);
            hoverLabel.setText(Component.empty());
            return;
        }

        hoverBox.setVisible(true);
        hoverLabel.setText(Component.translatable(
                "ascension.runic.casting.hover",
                rune.getName(),
                formatEnumName(rune.getType().name()),
                formatEnumName(rune.getDepth().name())
        ));
    }

    private static String formatEnumName(String name) {
        String lower = name.toLowerCase();

        if (lower.isEmpty()) {
            return lower;
        }

        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

private void addRuneTabs(RenderableElement panel, UIFrame frame) {
    int tabX = 15;

    for (RunicRuneType type : RunicRuneType.values()) {
        TextButton tab = new TextButton(
                frame,
                tabX,
                76,
                78,
                16,
                Component.literal(formatEnumName(type.name()))
        ) {
            @Override
            public void onClick() {
                setActiveRuneType(type);
            }
        };

        tabButtons.put(type, tab);
        panel.addChild(tab);
        tabX += 84;

        RunicRuneScrollBox scrollBox = new RunicRuneScrollBox(frame, 15, 98, 330, 86);
        scrollBox.setVisible(type == activeRuneType);
        scrollBox.setActive(type == activeRuneType);

        runeScrollBoxes.put(type, scrollBox);
        panel.addChild(scrollBox);
    }

    for (ResourceLocation runeId : usableRunes) {
        IRunicRune rune = ModRunicRunes.get(runeId);

        if (rune == null) {
            continue;
        }

        RunicRuneScrollBox scrollBox = runeScrollBoxes.get(rune.getType());

        if (scrollBox == null) {
            continue;
        }

        RuneButton runeButton = new RuneButton(
                frame,
                runeId,
                0,
                0,
                RunicRuneScrollBox.BUTTON_WIDTH,
                RunicRuneScrollBox.BUTTON_HEIGHT
        );

        runeButtons.add(runeButton);
        scrollBox.addChild(runeButton);
    }

    setActiveRuneType(activeRuneType);
}

private void setActiveRuneType(RunicRuneType type) {
    activeRuneType = type;

    for (Map.Entry<RunicRuneType, RunicRuneScrollBox> entry : runeScrollBoxes.entrySet()) {
        boolean active = entry.getKey() == type;
        entry.getValue().setVisible(active);
        entry.getValue().setActive(active);
    }
}

}
