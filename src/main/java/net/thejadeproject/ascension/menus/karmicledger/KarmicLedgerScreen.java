package net.thejadeproject.ascension.menus.karmicledger;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.events.karma.KarmaRank;
import net.thejadeproject.ascension.network.clientBound.OpenKarmicLedgerScreen;

import java.util.Map;


@OnlyIn(Dist.CLIENT)
public class KarmicLedgerScreen extends Screen {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/gui/karmic_ledger.png");

    private final OpenKarmicLedgerScreen data;
    private int leftPos;
    private int topPos;
    private int imageWidth = 176;
    private int imageHeight = 166;

    public KarmicLedgerScreen(OpenKarmicLedgerScreen data) {
        super(Component.literal("Karmic Debt Ledger"));
        this.data = data;
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        // Add close button
        this.addRenderableWidget(Button.builder(
                Component.literal("Close"),
                button -> this.onClose()
        ).bounds(this.leftPos + this.imageWidth - 60, this.topPos + this.imageHeight - 25, 50, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fillGradient(0, 0, this.width, this.height, 0x00000000, 0x00000000);

        // Draw background texture
        RenderSystem.enableBlend();
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        RenderSystem.disableBlend();

        // Draw title
        guiGraphics.drawCenteredString(this.font, Component.literal("Karmic Debt Ledger"),
                this.width / 2, this.topPos + 6, 0x404040);

        // Draw karma information
        KarmaRank rank = KarmaRank.fromId(data.karmaRank());
        String color = rank.getChatColor();

        guiGraphics.drawString(this.font,
                Component.literal("Current Karma: " + color + data.karmaValue() + " (" + rank.getId().toUpperCase() + ")"),
                this.leftPos + 8, this.topPos + 30, 0xFFFFFF, false);

        // Draw player kills
        guiGraphics.drawString(this.font,
                Component.literal("Player Kills: §c" + data.playerKills()),
                this.leftPos + 8, this.topPos + 50, 0xFFFFFF, false);

        // Draw mob kills header
        guiGraphics.drawString(this.font,
                Component.literal("Mob Kills:"),
                this.leftPos + 8, this.topPos + 70, 0xFFFFFF, false);

        // Draw mob kills list
        int yOffset = 85;
        int count = 0;
        for (Map.Entry<String, Integer> entry : data.mobKills().entrySet()) {
            if (yOffset < this.topPos + this.imageHeight - 30 && count < 10) {
                String mobName = formatMobName(entry.getKey());
                guiGraphics.drawString(this.font,
                        Component.literal("  " + mobName + ": §a" + entry.getValue()),
                        this.leftPos + 8, this.topPos + yOffset, 0xFFFFFF, false);
                yOffset += 12;
                count++;
            }
        }

        // Draw karma effects info
        String effectText = "";
        switch (rank) {
            case SAINT -> effectText = "§2Effects: Good Luck, -25% Villager Prices";
            case DEMONIC -> effectText = "§4Effects: Bad Luck, +25% Villager Prices";
            case NEUTRAL -> effectText = "§7Effects: No special effects";
        }

        guiGraphics.drawString(this.font,
                Component.literal(effectText),
                this.leftPos + 8, this.topPos + this.imageHeight - 45, 0xFFFFFF, false);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private String formatMobName(String entityId) {
        // Convert entity ID to readable name
        if (entityId.contains(":")) {
            entityId = entityId.split(":")[1];
        }

        // Replace underscores with spaces and capitalize words
        String[] words = entityId.split("_");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return result.toString().trim();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Override to do nothing or render a transparent background
        guiGraphics.fillGradient(0, 0, this.width, this.height, 0x00000000, 0x00000000);
    }
}