package net.thejadeproject.ascension.refactor_packages.gui.elements.hud;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.elements.built_in.EasyLabel;
import net.lucent.easygui.gui.layout.positioning.rules.PositioningRules;
import net.lucent.easygui.gui.textures.ITextureData;
import net.lucent.easygui.gui.textures.TextureData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.qi.EntityQiContainer;

import java.text.DecimalFormat;

public class QiBar extends RenderableElement {

    private final ResourceLocation textureIdentifier = ResourceLocation.fromNamespaceAndPath(
            AscensionCraft.MOD_ID,
            "textures/gui/main/overlays/qi_bar.png"
    );

    private final ITextureData textureData = new TextureData(textureIdentifier, 85, 9);
    private final DecimalFormat format = new DecimalFormat("#.0");

    /**
     * Which qi pool this bar displays.
     *
     * By default this shows pure/essence qi.
     * You can change this to another path id later if you want a Fire Qi bar,
     * Water Qi bar, etc.
     */
    private final ResourceLocation displayedQiPath = EntityQiContainer.PURE_QI;

    public QiBar(UIFrame frame) {
        super(frame);
        setWidth(textureData.getWidth());
        setHeight(textureData.getHeight());
    }

    private EasyLabel getOrCreateLabel() {
        if (getChildren().isEmpty()) {
            EasyLabel label = new EasyLabel(getUiFrame());
            addChild(label);

            label.setWidth(50);
            label.setHeight(getHeight());

            label.getPositioning().setXPositioningRule(PositioningRules.CENTER);
            label.getPositioning().setX(-label.getWidth() / 2);

            label.setTextPositioningX(EasyLabel.TextPositionRule.CENTER);
            label.setTextPositioningY(EasyLabel.TextPositionRule.CENTER);
            label.setTextColor(-1);
            label.setScaleToFit(true);
        }

        return (EasyLabel) getChildren().get(0);
    }

    public double getProgress() {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            getOrCreateLabel().setText(Component.empty());
            return 0.0D;
        }

        if (!minecraft.player.hasData(ModAttachments.ENTITY_DATA)) {
            getOrCreateLabel().setText(Component.empty());
            return 0.0D;
        }

        IEntityData entityData = minecraft.player.getData(ModAttachments.ENTITY_DATA);
        EntityQiContainer entityQiContainer = entityData.getQiContainer();

        double currentQi = entityQiContainer.getCurrentQi(displayedQiPath);
        double maxQi = entityQiContainer.getMaxQi(displayedQiPath);

        EasyLabel label = getOrCreateLabel();

        if (maxQi <= 0.0D) {
            label.setText(Component.literal("0.0/0.0"));
            return 0.0D;
        }

        label.setText(Component.literal(
                format.format(currentQi) + "/" + format.format(maxQi)
        ));

        return Math.max(0.0D, Math.min(1.0D, currentQi / maxQi));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        double progress = getProgress();

        if (progress > 0.0D) {
            textureData.render(
                    guiGraphics,
                    (int) (getWidth() * progress),
                    getHeight()
            );
        }
    }
}