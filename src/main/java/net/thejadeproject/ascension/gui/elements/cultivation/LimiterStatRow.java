package net.thejadeproject.ascension.gui.elements.cultivation;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.attributes.AttributeValueContainer;
import net.thejadeproject.ascension.refactor_packages.network.server_bound.cultivation.UpdateSuppressionValue;

public class LimiterStatRow extends RenderableElement {
    private final Holder<Attribute> attribute;
    private final ResourceLocation attributeId;
    private final String label;

    public LimiterStatRow(UIFrame frame, String label, Holder<Attribute> attribute, ResourceLocation attributeId) {
        super(frame);
        this.label = label;
        this.attribute = attribute;
        this.attributeId = attributeId;
        setWidth(148);
        setHeight(12);
    }

    public void tryClick(double lx, double ly) {
        int plusX = getWidth() - 11;
        int minusX = plusX - 13;

        if (lx >= minusX && lx <= minusX + 8 && ly >= 1 && ly <= 11) {
            changeSuppression(getStep());
        } else if (lx >= plusX && lx <= plusX + 8 && ly >= 1 && ly <= 11) {
            changeSuppression(-getStep());
        }
    }

    private AttributeValueContainer getContainer() {
        var mc = Minecraft.getInstance();
        if (mc.player == null) return null;

        var entityData = mc.player.getData(ModAttachments.ENTITY_DATA);
        if (entityData == null) return null;

        return entityData.getAscensionAttributeHolder().getAttribute(attribute);
    }

    private double getCurrentSuppression() {
        AttributeValueContainer container = getContainer();
        return container == null ? 0.0 : container.getSuppressionPercent();
    }

    private double getStep() {
        if (Screen.hasShiftDown() && Screen.hasControlDown()) return 1.0;
        if (Screen.hasControlDown()) return 0.001;
        if (Screen.hasShiftDown()) return 0.1;
        return 0.01;
    }

    private void changeSuppression(double delta) {
        double current = getCurrentSuppression();
        double next = Math.max(0.0, Math.min(1.0, current + delta));
        PacketDistributor.sendToServer(new UpdateSuppressionValue(attributeId, next));
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        super.render(gfx, mouseX, mouseY, partialTick);

        String realStr = getDisplayValue();
        var container = getContainer();

        var font = Minecraft.getInstance().font;
        int plusX = getWidth() - 11;
        int minusX = plusX - 13;

        gfx.drawString(font, label, 0, 2, 0xFFAAAAAA, false);

        int textWidth = font.width(realStr);
        int rightLimit = getWidth() - 26;
        int textX = rightLimit - textWidth;

        int valueColor = (container != null && container.isSuppressed())
                ? 0xFFFF7A7A
                : 0xFFFFFFFF;

        gfx.drawString(font, realStr, textX, 2, valueColor, false);

        gfx.fill(minusX, 1, minusX + 8, 11, 0xFF222222);
        gfx.drawString(font, "-", minusX + 1, 2, 0xFFFFFFFF, false);

        gfx.fill(plusX, 1, plusX + 8, 11, 0xFF222222);
        gfx.drawString(font, "+", plusX + 1, 2, 0xFFFFFFFF, false);
    }

    private String getDisplayValue() {
        AttributeValueContainer container = getContainer();
        if (container != null) {
            double shown = container.getValue();
            double full = container.getUnsuppressedValue();

            if (container.isSuppressed()) {
                return String.format("%.2f (%.2f)", shown, full);
            }
            return String.format("%.2f", shown);
        }

        var mc = Minecraft.getInstance();
        if (mc.player == null) return "—";
        var inst = mc.player.getAttribute(attribute);
        return inst == null ? "—" : String.format("%.2f", inst.getValue());
    }


}