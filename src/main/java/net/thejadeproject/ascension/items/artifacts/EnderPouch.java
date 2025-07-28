package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EnderPouch extends Item {

    public EnderPouch(Properties properties) {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            player.openMenu(new SimpleMenuProvider(
                    (containerId, playerInventory, playerEntity) -> ChestMenu.threeRows(containerId, playerInventory, player.getEnderChestInventory()),
                    Component.translatable("item.ascension.ender_pouch_gui")
            ));

            // Play sound on server, will be broadcast to all nearby players
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENDER_CHEST_OPEN, SoundSource.PLAYERS,
                    0.5F, level.random.nextFloat() * 0.1F + 0.9F);

            player.getCooldowns().addCooldown(this, 50);
        } else {
            // Play sound only for the activating player on client
            level.playSound(player, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENDER_CHEST_OPEN, SoundSource.PLAYERS,
                    0.5F, level.random.nextFloat() * 0.1F + 0.9F);
        }

        return InteractionResultHolder.success(stack);
    }
}
