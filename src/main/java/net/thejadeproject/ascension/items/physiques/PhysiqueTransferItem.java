package net.thejadeproject.ascension.items.physiques;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
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
import net.thejadeproject.ascension.util.ModAttachments;

import javax.annotation.Nullable;
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
            if (targetPhysiqueId == null || targetPhysiqueId.isEmpty()) {
                return InteractionResultHolder.fail(stack);
            }

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            String oldPhysique = player.getData(ModAttachments.PHYSIQUE);

            PlayerDataChangeHandler.resetData(player, targetPhysiqueId);

            NeoForge.EVENT_BUS.post(new PhysiqueChangeEvent(player, oldPhysique, targetPhysiqueId));

            ResourceLocation physiqueResource = ResourceLocation.bySeparator(targetPhysiqueId, ':');
            IPhysique newPhysique = AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(physiqueResource);

            if (newPhysique != null) {
                newPhysique.onPhysiqueAcquisition(player);
            }

            if (player instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, new SyncPlayerPhysique(targetPhysiqueId));
            }

            player.displayClientMessage(
                    Component.literal("Your physique has been transferred to ")
                            .append(getPhysiqueDisplayName(targetPhysiqueId)),
            true);

            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public Component getName(ItemStack stack) {
        String targetPhysiqueId = stack.get(ModDataComponents.PHYSIQUE_ID.get());
        if (targetPhysiqueId != null && !targetPhysiqueId.isEmpty()) {
            ResourceLocation physiqueResource = ResourceLocation.bySeparator(targetPhysiqueId, ':');
            IPhysique physique = AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(physiqueResource);
            if (physique != null) {
                return Component.literal(physique.getDisplayTitle() + " Transfer Stone");
            }
        }
        return Component.literal("Physique Transfer Stone");
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        String targetPhysiqueId = stack.get(ModDataComponents.PHYSIQUE_ID.get());
        if (targetPhysiqueId != null && !targetPhysiqueId.isEmpty()) {
            tooltip.add(Component.literal("§7Target: " + getPhysiqueDisplayName(targetPhysiqueId)));
        } else {
            tooltip.add(Component.literal("§7No physique set"));
        }

        tooltip.add(Component.literal("§eShift-right-click to transfer physique"));
        tooltip.add(Component.literal("§cWarning: Resets all cultivation progress!"));
    }

    private Component getPhysiqueDisplayName(String targetPhysiqueId) {
        ResourceLocation physiqueResource = ResourceLocation.bySeparator(targetPhysiqueId, ':');
        IPhysique physique = AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(physiqueResource);
        if (physique != null) {
            return Component.literal("§6" + physique.getDisplayTitle());
        }
        return Component.literal("§7Unknown Physique");
    }

    public static ItemStack createWithPhysique(String physiqueId) {
        ItemStack stack = new ItemStack(ModItems.PHYSIQUE_SLIP.get());
        stack.set(ModDataComponents.PHYSIQUE_ID.get(), physiqueId);
        return stack;
    }
}