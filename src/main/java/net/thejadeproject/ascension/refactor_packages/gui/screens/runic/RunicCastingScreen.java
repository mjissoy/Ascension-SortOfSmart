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
        panel.setWidth(270);
        panel.setHeight(190);
        panel.getPositioning().setPositioningRule(PositioningRules.CENTER);
        panel.getPositioning().setX(-135);
        panel.getPositioning().setY(-95);
        frame.setRoot(panel);

        EasyLabel title = label(frame, Component.translatable("ascension.runic.casting.title"), 0, 8, 270, 12, 0xFFE8D8FF);
        title.setTextPositioningX(EasyLabel.TextPositionRule.CENTER);
        panel.addChild(title);

        EasyLabel info = label(
                frame,
                Component.translatable("ascension.runic.casting.info", maxRuneSlots, durationSeconds),
                0,
                23,
                270,
                10,
                0xFFBEB4D7
        );
        info.setTextPositioningX(EasyLabel.TextPositionRule.CENTER);
        info.setTextScale(0.8F);
        panel.addChild(info);

        selectedLabel = label(frame, Component.empty(), 15, 42, 240, 14, 0xFFFFFFFF);
        selectedLabel.setTextScale(0.85F);
        panel.addChild(selectedLabel);
        refreshSelectedLabel();

        int startX = 15;
        int startY = 64;
        int buttonW = 76;
        int buttonH = 16;
        int gap = 6;

        for (int i = 0; i < usableRunes.size(); i++) {
            ResourceLocation runeId = usableRunes.get(i);
            int col = i % 3;
            int row = i / 3;

            RuneButton runeButton = new RuneButton(frame, runeId, startX + col * (buttonW + gap), startY + row * (buttonH + gap), buttonW, buttonH);
            panel.addChild(runeButton);
        }

        TextButton backspace = new TextButton(frame, 15, 154, 76, 18, Component.translatable("ascension.runic.casting.backspace")) {
            @Override
            public void onClick() {
                if (!selectedRunes.isEmpty()) {
                    selectedRunes.remove(selectedRunes.size() - 1);
                    refreshSelectedLabel();
                }
            }
        };
        panel.addChild(backspace);

        TextButton clear = new TextButton(frame, 97, 154, 76, 18, Component.translatable("ascension.runic.casting.clear")) {
            @Override
            public void onClick() {
                selectedRunes.clear();
                refreshSelectedLabel();
            }
        };
        panel.addChild(clear);

        TextButton cast = new TextButton(frame, 179, 154, 76, 18, Component.translatable("ascension.runic.casting.cast")) {
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
        private final EasyLabel label;

        private TextButton(UIFrame frame, int x, int y, int width, int height, Component text) {
            super(frame, x, y);
            setWidth(width);
            setHeight(height);

            label = label(frame, text, 0, 0, width, height, 0xFFFFFFFF);
            label.setTextScale(0.75F);
            label.setTextPositioningX(EasyLabel.TextPositionRule.CENTER);
            addChild(label);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            int color = isPressed() ? 0xCC6F4DBA : isHovered() ? 0xAA4F3A7A : 0xAA211733;
            guiGraphics.fill(0, 0, getWidth(), getHeight(), color);
            guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), 0xFFB79CFF);
        }
    }

    private static class RunicPanel extends RenderableElement {
        private RunicPanel(UIFrame frame) {
            super(frame);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            guiGraphics.fill(0, 0, getWidth(), getHeight(), 0xDD0D0718);
            guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), 0xFFE8D8FF);
        }
    }
}
