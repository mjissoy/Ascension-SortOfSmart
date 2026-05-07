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
import net.thejadeproject.ascension.refactor_packages.runic.runes.IRunicRune;
import net.thejadeproject.ascension.refactor_packages.runic.runes.ModRunicRunes;
import net.thejadeproject.ascension.refactor_packages.runic.sequences.IRunicSequence;
import net.thejadeproject.ascension.refactor_packages.runic.sequences.ModRunicSequences;

import java.util.List;

public class RunicCodexScreen extends EasyScreen {

    private static final int PANEL_WIDTH = 420;
    private static final int PANEL_HEIGHT = 260;
    private static final int RUNE_PAGE_SIZE = 9;
    private static final int SEQUENCE_PAGE_SIZE = 9;
    private static final int PAGE_CONTROL_Y = 178;

    private final List<ResourceLocation> knownRunes;
    private final List<ResourceLocation> discoveredSequences;

    private RenderableElement runeListContainer;
    private RenderableElement sequenceListContainer;
    private RenderableElement detailContainer;
    private EasyLabel runePageLabel;
    private EasyLabel sequencePageLabel;

    private int runePage;
    private int sequencePage;
    private ResourceLocation selectedRune;
    private ResourceLocation selectedSequence;

    public RunicCodexScreen(List<ResourceLocation> knownRunes, List<ResourceLocation> discoveredSequences) {
        super(Component.translatable("ascension.runic.codex.title"));

        this.knownRunes = List.copyOf(knownRunes);
        this.discoveredSequences = List.copyOf(discoveredSequences);

        if (!this.knownRunes.isEmpty()) {
            this.selectedRune = this.knownRunes.get(0);
        } else if (!this.discoveredSequences.isEmpty()) {
            this.selectedSequence = this.discoveredSequences.get(0);
        }

        build(getUIFrame());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void build(UIFrame frame) {
        frame.setPauseGame(false);

        CodexPanel panel = new CodexPanel(frame);
        panel.setWidth(PANEL_WIDTH);
        panel.setHeight(PANEL_HEIGHT);
        panel.getPositioning().setPositioningRule(PositioningRules.CENTER);
        panel.getPositioning().setX(-PANEL_WIDTH / 2);
        panel.getPositioning().setY(-PANEL_HEIGHT / 2);
        frame.setRoot(panel);

        EasyLabel title = label(frame, Component.translatable("ascension.runic.codex.title"), 0, 8, PANEL_WIDTH, 14, 0xFFF6E7FF);
        title.setTextPositioningX(EasyLabel.TextPositionRule.CENTER);
        title.setTextScale(1.1F);
        panel.addChild(title);

        EasyLabel runeHeader = label(frame, Component.translatable("ascension.runic.codex.runes"), 22, 31, 160, 12, 0xFF3A203F);
        runeHeader.setTextPositioningX(EasyLabel.TextPositionRule.CENTER);
        panel.addChild(runeHeader);

        EasyLabel sequenceHeader = label(frame, Component.translatable("ascension.runic.codex.sequences"), 238, 31, 160, 12, 0xFF3A203F);
        sequenceHeader.setTextPositioningX(EasyLabel.TextPositionRule.CENTER);
        panel.addChild(sequenceHeader);

        runeListContainer = new RenderableElement(frame, 22, 48);
        runeListContainer.setWidth(160);
        runeListContainer.setHeight(137);
        panel.addChild(runeListContainer);

        sequenceListContainer = new RenderableElement(frame, 238, 48);
        sequenceListContainer.setWidth(160);
        sequenceListContainer.setHeight(137);
        panel.addChild(sequenceListContainer);

        detailContainer = new RenderableElement(frame, 22, 194) {
            @Override
            public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                guiGraphics.fill(0, 0, getWidth(), getHeight(), 0x44FFFFFF);
                guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), 0xAA3A203F);
            }
        };
        detailContainer.setWidth(376);
        detailContainer.setHeight(52);
        panel.addChild(detailContainer);

        TextButton runePrev = new TextButton(frame, 22, PAGE_CONTROL_Y, 36, 10, Component.literal("<")) {
            @Override
            public void onClick() {
                if (runePage > 0) {
                    runePage--;
                    refreshRuneList();
                }
            }
        };
        panel.addChild(runePrev);

        runePageLabel = label(frame, Component.empty(), 61, PAGE_CONTROL_Y, 82, 10, 0xFF3A203F);
        runePageLabel.setTextScale(0.7F);
        runePageLabel.setTextPositioningX(EasyLabel.TextPositionRule.CENTER);
        panel.addChild(runePageLabel);

        TextButton runeNext = new TextButton(frame, 146, PAGE_CONTROL_Y, 36, 10, Component.literal(">")) {
            @Override
            public void onClick() {
                if (runePage + 1 < getPageCount(knownRunes.size(), RUNE_PAGE_SIZE)) {
                    runePage++;
                    refreshRuneList();
                }
            }
        };
        panel.addChild(runeNext);

        TextButton sequencePrev = new TextButton(frame, 238, PAGE_CONTROL_Y, 36, 10, Component.literal("<")) {
            @Override
            public void onClick() {
                if (sequencePage > 0) {
                    sequencePage--;
                    refreshSequenceList();
                }
            }
        };
        panel.addChild(sequencePrev);

        sequencePageLabel = label(frame, Component.empty(), 277, PAGE_CONTROL_Y, 82, 10, 0xFF3A203F);
        sequencePageLabel.setTextScale(0.7F);
        sequencePageLabel.setTextPositioningX(EasyLabel.TextPositionRule.CENTER);
        panel.addChild(sequencePageLabel);

        TextButton sequenceNext = new TextButton(frame, 362, PAGE_CONTROL_Y, 36, 10, Component.literal(">")) {
            @Override
            public void onClick() {
                if (sequencePage + 1 < getPageCount(discoveredSequences.size(), SEQUENCE_PAGE_SIZE)) {
                    sequencePage++;
                    refreshSequenceList();
                }
            }
        };
        panel.addChild(sequenceNext);

        refreshRuneList();
        refreshSequenceList();
        refreshDetails();
    }

    private void refreshRuneList() {
        runeListContainer.removeChildren();

        if (knownRunes.isEmpty()) {
            EasyLabel empty = label(getUIFrame(), Component.translatable("ascension.runic.codex.no_runes"), 0, 4, 160, 12, 0xFF6B4F3A);
            empty.setTextPositioningX(EasyLabel.TextPositionRule.CENTER);
            empty.setTextScale(0.75F);
            runeListContainer.addChild(empty);
            refreshRunePageLabel();
            return;
        }

        int start = runePage * RUNE_PAGE_SIZE;
        int end = Math.min(start + RUNE_PAGE_SIZE, knownRunes.size());

        for (int i = start; i < end; i++) {
            ResourceLocation runeId = knownRunes.get(i);
            int localIndex = i - start;

            TextButton button = new TextButton(getUIFrame(), 0, localIndex * 15, 160, 13, getRuneName(runeId)) {
                @Override
                protected boolean isSelected() {
                    return runeId.equals(selectedRune);
                }

                @Override
                public void onClick() {
                    selectedRune = runeId;
                    selectedSequence = null;
                    refreshRuneList();
                    refreshSequenceList();
                    refreshDetails();
                }
            };

            runeListContainer.addChild(button);
        }

        refreshRunePageLabel();
    }

    private void refreshSequenceList() {
        sequenceListContainer.removeChildren();

        if (discoveredSequences.isEmpty()) {
            EasyLabel empty = label(getUIFrame(), Component.translatable("ascension.runic.codex.no_sequences"), 0, 4, 160, 12, 0xFF6B4F3A);
            empty.setTextPositioningX(EasyLabel.TextPositionRule.CENTER);
            empty.setTextScale(0.75F);
            sequenceListContainer.addChild(empty);
            refreshSequencePageLabel();
            return;
        }

        int start = sequencePage * SEQUENCE_PAGE_SIZE;
        int end = Math.min(start + SEQUENCE_PAGE_SIZE, discoveredSequences.size());

        for (int i = start; i < end; i++) {
            ResourceLocation sequenceId = discoveredSequences.get(i);
            int localIndex = i - start;

            TextButton button = new TextButton(getUIFrame(), 0, localIndex * 15, 160, 13, getSequenceName(sequenceId)) {
                @Override
                protected boolean isSelected() {
                    return sequenceId.equals(selectedSequence);
                }

                @Override
                public void onClick() {
                    selectedSequence = sequenceId;
                    selectedRune = null;
                    refreshRuneList();
                    refreshSequenceList();
                    refreshDetails();
                }
            };

            sequenceListContainer.addChild(button);
        }

        refreshSequencePageLabel();
    }

    private void refreshDetails() {
        detailContainer.removeChildren();

        if (selectedRune != null) {
            addRuneDetails(selectedRune);
            return;
        }

        if (selectedSequence != null) {
            addSequenceDetails(selectedSequence);
            return;
        }

        EasyLabel empty = label(getUIFrame(), Component.translatable("ascension.runic.codex.select_entry"), 8, 8, 360, 12, 0xFF6B4F3A);
        empty.setTextScale(0.8F);
        detailContainer.addChild(empty);
    }

    private void addRuneDetails(ResourceLocation runeId) {
        IRunicRune rune = ModRunicRunes.get(runeId);

        EasyLabel name = label(getUIFrame(), getRuneName(runeId), 8, 5, 160, 12, 0xFF2A162F);
        name.setTextScale(0.85F);
        detailContainer.addChild(name);

        if (rune == null) {
            addDetailLine(Component.literal(runeId.toString()), 18);
            return;
        }

        addDetailLine(Component.translatable(
                "ascension.runic.codex.rune_details",
                formatEnumName(rune.getType().name()),
                formatEnumName(rune.getDepth().name()),
                rune.getMinimumRunicRealmToUse()
        ), 18);

        addDetailLine(Component.translatable("ascension.runic.codex.rune_id", runeId.toString()), 31);
    }

    private void addSequenceDetails(ResourceLocation sequenceId) {
        IRunicSequence sequence = ModRunicSequences.get(sequenceId);

        EasyLabel name = label(getUIFrame(), getSequenceName(sequenceId), 8, 3, 200, 12, 0xFF2A162F);
        name.setTextScale(0.85F);
        detailContainer.addChild(name);

        if (sequence == null) {
            addDetailLine(Component.literal(sequenceId.toString()), 18);
            return;
        }

        addDetailLine(Component.translatable("ascension.runic.sequence." + sequenceId.getPath() + ".desc"), 16);

        addDetailLine(Component.translatable(
                "ascension.runic.codex.sequence_details",
                formatEnumName(sequence.getTier().name()),
                sequence.getMinimumRunicRealm(),
                sequence.getQiCost()
        ), 28);

        addDetailLine(Component.translatable("ascension.runic.codex.sequence_formula", formatRuneList(sequence.getRequiredRunes())), 40);
    }

    private void addDetailLine(Component text, int y) {
        EasyLabel line = label(getUIFrame(), text, 8, y, 360, 9, 0xFF3A203F);
        line.setTextScale(0.7F);
        detailContainer.addChild(line);
    }

    private void refreshRunePageLabel() {
        if (runePageLabel != null) {
            runePageLabel.setText(Component.translatable(
                    "ascension.runic.codex.page",
                    getDisplayedPage(knownRunes.size(), RUNE_PAGE_SIZE, runePage),
                    getPageCount(knownRunes.size(), RUNE_PAGE_SIZE)
            ));
        }
    }

    private void refreshSequencePageLabel() {
        if (sequencePageLabel != null) {
            sequencePageLabel.setText(Component.translatable(
                    "ascension.runic.codex.page",
                    getDisplayedPage(discoveredSequences.size(), SEQUENCE_PAGE_SIZE, sequencePage),
                    getPageCount(discoveredSequences.size(), SEQUENCE_PAGE_SIZE)
            ));
        }
    }

    private static int getDisplayedPage(int entryCount, int pageSize, int page) {
        if (entryCount <= 0) {
            return 0;
        }

        return page + 1;
    }

    private static int getPageCount(int entryCount, int pageSize) {
        if (entryCount <= 0) {
            return 0;
        }

        return (entryCount + pageSize - 1) / pageSize;
    }

    private static Component getRuneName(ResourceLocation runeId) {
        IRunicRune rune = ModRunicRunes.get(runeId);
        return rune == null ? Component.literal(runeId.getPath()) : rune.getName();
    }

    private static Component getSequenceName(ResourceLocation sequenceId) {
        return Component.translatable("ascension.runic.sequence." + sequenceId.getPath());
    }

    private static String formatRuneList(List<ResourceLocation> runeIds) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < runeIds.size(); i++) {
            if (i > 0) {
                builder.append(" > ");
            }

            IRunicRune rune = ModRunicRunes.get(runeIds.get(i));
            builder.append(rune == null ? runeIds.get(i).getPath() : rune.getName().getString());
        }

        return builder.toString();
    }

    private static String formatEnumName(String name) {
        String lower = name.toLowerCase();

        if (lower.isEmpty()) {
            return lower;
        }

        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
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

    private static class TextButton extends EasyButton {
        private final Component text;

        private TextButton(UIFrame frame, int x, int y, int width, int height, Component text) {
            super(frame, x, y);
            this.text = text;

            setWidth(width);
            setHeight(height);
        }

        protected boolean isSelected() {
            return false;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            boolean manuallyHovered = isPointBounded(mouseX, mouseY);

            int color = isSelected()
                    ? 0xCC6B38B6
                    : isPressed() ? 0xAA4A276F : manuallyHovered ? 0x884A276F : 0x33FFFFFF;

            int outline = isSelected() || manuallyHovered ? 0xFF2A162F : 0x663A203F;
            int textColor = isSelected() ? 0xFFFFF4FF : 0xFF2A162F;

            guiGraphics.fill(0, 0, getWidth(), getHeight(), color);
            guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), outline);

            Minecraft minecraft = Minecraft.getInstance();
            int textX = (getWidth() - minecraft.font.width(text)) / 2;
            int textY = (getHeight() - minecraft.font.lineHeight) / 2 + 1;

            guiGraphics.drawString(
                    minecraft.font,
                    text,
                    textX,
                    textY,
                    textColor,
                    false
            );
        }
    }

    private static class CodexPanel extends RenderableElement {
        private CodexPanel(UIFrame frame) {
            super(frame);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            guiGraphics.fill(0, 0, getWidth(), getHeight(), 0xFFE5D0A3);

            guiGraphics.fill(0, 0, getWidth(), 24, 0xFF2A162F);

            guiGraphics.fill(getWidth() / 2 - 1, 24, getWidth() / 2 + 1, getHeight(), 0x883A203F);

            guiGraphics.fill(12, 28, getWidth() / 2 - 8, 188, 0x66FFF4D1);
            guiGraphics.fill(getWidth() / 2 + 8, 28, getWidth() - 12, 188, 0x66FFF4D1);

            guiGraphics.renderOutline(0, 0, getWidth(), getHeight(), 0xFF2A162F);
            guiGraphics.renderOutline(10, 26, getWidth() / 2 - 16, 164, 0xAA3A203F);
            guiGraphics.renderOutline(getWidth() / 2 + 6, 26, getWidth() / 2 - 16, 164, 0xAA3A203F);

            guiGraphics.fill(0, 0, 3, getHeight(), 0xFF1A0E21);
            guiGraphics.fill(getWidth() - 3, 0, getWidth(), getHeight(), 0xFF1A0E21);

            guiGraphics.fill(0, 23, getWidth(), 24, 0xFF7A3FD1);
        }
    }
}