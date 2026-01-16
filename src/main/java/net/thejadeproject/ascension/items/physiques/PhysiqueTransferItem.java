package net.thejadeproject.ascension.items.physiques;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.cultivation.player.PlayerDataChangeHandler;
import net.thejadeproject.ascension.events.ModDataComponents;
import net.thejadeproject.ascension.events.custom.PhysiqueChangeEvent;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.network.clientBound.SyncPlayerPhysique;
import net.thejadeproject.ascension.progression.physiques.IPhysique;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

import java.util.List;

public class PhysiqueTransferItem extends Item {

    public PhysiqueTransferItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide() && player.isShiftKeyDown()) {
            String targetPhysiqueId = stack.get(ModDataComponents.PHYSIQUE_ID.get());
            Integer purity = stack.get(ModDataComponents.PURITY.get());

            if (targetPhysiqueId == null || targetPhysiqueId.isEmpty() || purity == null) {
                return InteractionResultHolder.fail(stack);
            }

            // Check if purity is 100%
            if (purity < 100) {
                player.sendSystemMessage(
                        Component.literal("Cannot transfer physique: ")
                                .append(Component.literal(purity + "%").withStyle(ChatFormatting.GOLD))
                                .append(Component.literal(" purity. Need "))
                                .append(Component.literal("100%").withStyle(ChatFormatting.GREEN))
                                .append(Component.literal(" purity."))
                );
                return InteractionResultHolder.fail(stack);
            }

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            PlayerDataChangeHandler.changePhysique(player, ResourceLocation.bySeparator(targetPhysiqueId,':'));



            // Send feedback message
            player.sendSystemMessage(
                    Component.literal("Successfully transferred to ")
                            .append(getPhysiqueDisplayName(targetPhysiqueId))
                            .append(Component.literal("!"))
            );

            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public Component getName(ItemStack stack) {
        String targetPhysiqueId = stack.get(ModDataComponents.PHYSIQUE_ID.get());
        Integer purity = stack.get(ModDataComponents.PURITY.get());

        if (targetPhysiqueId != null && !targetPhysiqueId.isEmpty()) {
            ResourceLocation physiqueResource = ResourceLocation.parse(targetPhysiqueId);
            IPhysique physique = AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(physiqueResource);
            if (physique != null) {
                Component baseName = Component.empty().append(physique.getDisplayTitle()).append(" Blood Essence");

                // Add purity to name if not 100%
                if (purity != null && purity < 100) {
                    return baseName.copy()
                            .append(Component.literal(" [").withStyle(ChatFormatting.GRAY))
                            .append(Component.literal(purity + "%").withStyle(ChatFormatting.GOLD))
                            .append(Component.literal("]").withStyle(ChatFormatting.GRAY));
                }
                return baseName;
            }
        }
        return Component.literal("Blood Essence");
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        String targetPhysiqueId = stack.get(ModDataComponents.PHYSIQUE_ID.get());
        Integer purity = stack.get(ModDataComponents.PURITY.get());

        if (targetPhysiqueId != null && !targetPhysiqueId.isEmpty()) {
            tooltip.add(Component.literal("Target: ").withStyle(ChatFormatting.GRAY)
                    .append(getPhysiqueDisplayName(targetPhysiqueId)));

            if (purity != null) {
                ChatFormatting purityColor = ChatFormatting.GRAY;
                if (purity >= 100) purityColor = ChatFormatting.GREEN;
                else if (purity >= 75) purityColor = ChatFormatting.YELLOW;
                else if (purity >= 50) purityColor = ChatFormatting.GOLD;
                else if (purity >= 25) purityColor = ChatFormatting.RED;

                tooltip.add(Component.literal("Purity: ").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(purity + "%").withStyle(purityColor)));
            }
        } else {
            tooltip.add(Component.literal("No physique set").withStyle(ChatFormatting.GRAY));
        }

        tooltip.add(Component.literal("Shift-right-click to transfer physique")
                .withStyle(ChatFormatting.YELLOW));

        if (purity != null && purity >= 100) {
            tooltip.add(Component.literal("Ready to use!").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD));
        } else if (purity != null) {
            tooltip.add(Component.literal("Requires 100% purity to use")
                    .withStyle(ChatFormatting.RED));
        }

        tooltip.add(Component.literal("Warning: Resets all cultivation progress!")
                .withStyle(ChatFormatting.DARK_RED));
    }

    private Component getPhysiqueDisplayName(String targetPhysiqueId) {
        ResourceLocation physiqueResource = ResourceLocation.parse(targetPhysiqueId);
        IPhysique physique = AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(physiqueResource);
        if (physique != null) {
            return Component.empty().append(physique.getDisplayTitle()).withStyle(ChatFormatting.GOLD);
        }
        return Component.literal("Unknown Physique").withStyle(ChatFormatting.GRAY);
    }

    public static ItemStack createWithPhysique(String physiqueId, int purity) {
        ItemStack stack = new ItemStack(ModItems.BLOOD_ESSENCE.get());
        stack.set(ModDataComponents.PHYSIQUE_ID.get(), physiqueId);
        stack.set(ModDataComponents.PURITY.get(), Math.min(purity, 100));
        return stack;
    }

    // Check if two slips can be combined (same physique ID)
    public static boolean canCombine(ItemStack stack1, ItemStack stack2) {
        String id1 = stack1.get(ModDataComponents.PHYSIQUE_ID.get());
        String id2 = stack2.get(ModDataComponents.PHYSIQUE_ID.get());

        if (id1 == null || id2 == null) return false;
        return id1.equals(id2);
    }

    // Get the resulting purity when combining two slips
    public static int getCombinedPurity(ItemStack stack1, ItemStack stack2) {
        Integer purity1 = stack1.get(ModDataComponents.PURITY.get());
        Integer purity2 = stack2.get(ModDataComponents.PURITY.get());

        if (purity1 == null) purity1 = 0;
        if (purity2 == null) purity2 = 0;

        return Math.min(purity1 + purity2, 100);
    }
}