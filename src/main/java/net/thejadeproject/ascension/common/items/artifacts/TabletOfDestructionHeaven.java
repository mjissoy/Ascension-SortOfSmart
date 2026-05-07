package net.thejadeproject.ascension.common.items.artifacts;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.thejadeproject.ascension.common.items.artifacts.bases.BaseTabletOfDestruction;

import java.util.List;

public class TabletOfDestructionHeaven extends BaseTabletOfDestruction {
    private static final int COOLDOWN = 100; // 5 seconds
    private static final int WIDTH = 4, HEIGHT = 7, DEPTH = 22;

    public TabletOfDestructionHeaven(Properties properties) {
        super(properties);
    }

    @Override
    protected int getCooldownTicks() { return COOLDOWN; }
    @Override
    protected int getWidth() { return WIDTH; }
    @Override
    protected int getHeight() { return HEIGHT; }
    @Override
    protected int getDepth() { return DEPTH; }
    @Override
    protected boolean supportsDropBlocks() { return true; }
    @Override
    protected boolean supportsContainerLinking() { return true; }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        boolean dropBlocks = isDropBlocksEnabled(stack);
        Component status = Component.literal(String.valueOf(dropBlocks))
                .withStyle(dropBlocks ? ChatFormatting.GREEN : ChatFormatting.RED);
        tooltipComponents.add(Component.translatable("item.ascension.tablet_of_destruction_heaven.drop_blocks").append(status));
        tooltipComponents.add(Component.translatable("ascension.tablet.toggle_mode_info"));

        tooltipComponents.add(Component.translatable("item.ascension.tablet_of_destruction_heaven.link_info"));

        // Show linked container
        var linkData = getLinkedContainer(stack);
        if (linkData.pos() != null && context.level() != null &&
                linkData.dimension().equals(context.level().dimension().location().toString())) {
            var state = context.level().getBlockState(linkData.pos());
            String blockName = state.getBlock().getName().getString();
            tooltipComponents.add(Component.translatable("item.ascension.tablet_of_destruction_heaven.linked_to",
                    blockName, linkData.pos().getX(), linkData.pos().getY(), linkData.pos().getZ()));
        }
    }

    @Override
    protected Component getCooldownMessage() {
        return Component.translatable("item.ascension.tablet_of_destruction_heaven.cooldown");
    }
}