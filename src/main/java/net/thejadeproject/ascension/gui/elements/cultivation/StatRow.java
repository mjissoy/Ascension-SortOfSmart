package net.thejadeproject.ascension.gui.elements.cultivation;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.elements.built_in.EasyLabel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.attributes.AttributeValueContainer;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;

public class StatRow extends RenderableElement {
    private final Holder<Attribute> attribute;
    private final EasyLabel valueLabel;

    public StatRow(UIFrame frame, String label, Holder<Attribute> attribute) {
        super(frame);
        this.attribute = attribute;
        setWidth(148);
        setHeight(10);

        EasyLabel nameLabel = new EasyLabel(frame);
        nameLabel.setText(Component.literal(label));
        nameLabel.setTextColor(0xFFAAAAAA);
        nameLabel.getPositioning().setX(0);
        nameLabel.getPositioning().setY(2);
        addChild(nameLabel);

        valueLabel = new EasyLabel(frame);
        valueLabel.setText(Component.literal("—"));
        valueLabel.setTextColor(0xFFFFFFFF);
        valueLabel.getPositioning().setX(80);
        valueLabel.getPositioning().setY(2);
        addChild(valueLabel);
    }

    private String getDisplayValue() {
        var mc = Minecraft.getInstance();
        if (mc.player == null) return "—";

        IEntityData entityData = mc.player.getData(ModAttachments.ENTITY_DATA);
        if (entityData == null) return "—";

        AttributeValueContainer container = entityData.getAscensionAttributeHolder().getAttribute(attribute);
        if (container != null) {
            return String.format("%.2f", container.getValue());
        }

        var inst = mc.player.getAttribute(attribute);
        return inst == null ? "—" : String.format("%.2f", inst.getValue());
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        valueLabel.setText(Component.literal(getDisplayValue()));
        super.render(gfx, mouseX, mouseY, partialTick);
    }
}