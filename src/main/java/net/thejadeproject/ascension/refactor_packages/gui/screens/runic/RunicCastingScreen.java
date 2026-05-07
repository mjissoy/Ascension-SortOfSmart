package net.thejadeproject.ascension.refactor_packages.gui.screens.runic;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.elements.built_in.EasyButton;
import net.lucent.easygui.gui.elements.built_in.EasyLabel;
import net.lucent.easygui.gui.layout.positioning.rules.PositioningRules;
import net.lucent.easygui.screen.EasyScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.refactor_packages.network.server_bound.runic.CastRunicSequencePayload;
import net.thejadeproject.ascension.refactor_packages.runic.runes.IRunicRune;
import net.thejadeproject.ascension.refactor_packages.runic.runes.ModRunicRunes;

import java.util.ArrayList;
import java.util.List;

public class RunicCastingScreen extends EasyScreen {

    private final List<ResourceLocation> usableRunes;
    private final List<ResourceLocation> selectedRunes = new ArrayList<>();
    private final List<RuneButton> runeButtons = new ArrayList<>();
    private EasyLabel hoverLabel;
    private RenderableElement hoverBox;
    private final int maxRuneSlots;
    private final int durationSeconds;
    private EasyLabel selectedLabel;

    public RunicCastingScreen(int maxRuneSlots, int durationSeconds, List<ResourceLocation> usableRunes) {
        super(Component.translatable("ascension.runic.casting.title"));

        this.maxRuneSlots = maxRuneSlots;
        this.durationSeconds = durationSeconds;
        this.usableRunes = List.copyOf(usableRunes);

        build(getUIFrame());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
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

        selectedLabel = label(frame, Component.empty(), 15, 42, 330, 14, 0xFFFFFFFF);
        selectedLabel.setTextScale(0.85F);
        panel.addChild(selectedLabel);
        refreshSelectedLabel();

        hoverBox = new RenderableElement(frame, 190, 40) {
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

        int startX = 15;
        int startY = 68;
        int buttonW = 78;
        int buttonH = 16;
        int gap = 6;
        int columns = 4;

        for (int i = 0; i < usableRunes.size(); i++) {
            ResourceLocation runeId = usableRunes.get(i);
            int col = i % columns;
            int row = i / columns;

            RuneButton runeButton = new RuneButton(
                    frame,
                    runeId,
                    startX + col * (buttonW + gap),
                    startY + row * (buttonH + gap),
                    buttonW,
                    buttonH
            );

            runeButtons.add(runeButton);
            panel.addChild(runeButton);
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

        TextButton cast = new TextButton(frame, 245, 195, 100, 18, Component.translatable("ascension.runic.casting.cast")) {
            @Override
            public void onClick() {
                if (selectedRunes.isEmpty()) {
                    return;
                }

                PacketDistributor.sendToServer(new CastRunicSequencePayload(List.copyOf(selectedRunes)));
                Minecraft.getInstance().setScreen(null);
            }
        };
        panel.addChild(cast);
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
            selectedLabel.setText(Component.translatable("ascension.runic.casting.selected.empty"));
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

}
