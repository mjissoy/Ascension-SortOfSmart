package net.thejadeproject.ascension.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.MilkBucketItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.thejadeproject.ascension.AscensionCraft;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class MilkBucketCancel {
    private static boolean isMilkBucket(ItemStack stack) {
        return stack.getItem() instanceof MilkBucketItem || stack.is(Items.MILK_BUCKET);
    }

    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent.Start event) {
        // Check if the item being used is a milk bucket
        ItemStack item = event.getItem();
        if (item.getItem() == Items.MILK_BUCKET) {
            // Cancel the event so the milk bucket cannot be used
            event.setCanceled(true);

            // Send message to player if the entity is a player
            if (event.getEntity() instanceof Player player) {
                player.displayClientMessage(
                        Component.translatable("message.ascension.milk_denied"),
                        true
                );
            }
        }
    }
}
