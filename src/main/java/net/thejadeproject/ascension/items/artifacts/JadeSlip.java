package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class JadeSlip extends Item {

    public JadeSlip(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            if (!level.isClientSide()) {
                // Check if already linked
                if (isLinked(stack)) {
                    String currentOwner = getPlayerName(stack);
                    player.sendSystemMessage(Component.translatable("item.ascension.jade_slip.already_linked", currentOwner).withStyle(ChatFormatting.RED));
                    return InteractionResultHolder.fail(stack);
                }

                // Store player information using custom data component
                setPlayerData(stack, player);

                player.sendSystemMessage(Component.translatable("item.ascension.jade_slip.linked", player.getGameProfile().getName()).withStyle(ChatFormatting.YELLOW));
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        return InteractionResultHolder.pass(stack);
    }

    public static void setPlayerData(ItemStack jadeSlip, Player player) {
        // Get or create custom data component
        var customData = jadeSlip.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        var tag = customData.copyTag();

        // Store both UUID and player name
        tag.putUUID("linked_player", player.getUUID());
        tag.putString("linked_player_name", player.getGameProfile().getName());

        jadeSlip.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static String getPlayerName(ItemStack jadeSlip) {
        var customData = jadeSlip.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return null;

        var tag = customData.copyTag();
        return tag.contains("linked_player_name") ? tag.getString("linked_player_name") : null;
    }

    @Nullable
    public static UUID getPlayerUUID(ItemStack jadeSlip) {
        var customData = jadeSlip.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return null;

        var tag = customData.copyTag();
        return tag.contains("linked_player") ? tag.getUUID("linked_player") : null;
    }

    public static boolean isLinked(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof JadeSlip)) return false;
        return getPlayerUUID(stack) != null;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        // Only show tooltip if the item is linked
        if (isLinked(stack)) {
            String playerName = getPlayerName(stack);
            tooltip.add(Component.translatable("item.ascension.jade_slip.tooltip.linked", playerName).withStyle(ChatFormatting.YELLOW));
        }
    }

    // Optional: Make the item glow when linked
    @Override
    public boolean isFoil(ItemStack stack) {
        return isLinked(stack);
    }

    // Prevent stacking of linked Jade Slips
    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }
}