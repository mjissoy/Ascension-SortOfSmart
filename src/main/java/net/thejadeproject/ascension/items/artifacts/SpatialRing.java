package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.items.data_components.ModDataComponents;
import net.thejadeproject.ascension.items.data_components.spatial_ring.SpatialRingItemStackHandler;
import net.thejadeproject.ascension.items.data_components.spatial_ring.SpatialRingMenuProvider;

public class SpatialRing extends Item {
    public SpatialRing(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(usedHand != InteractionHand.MAIN_HAND) return InteractionResultHolder.fail(player.getItemInHand(usedHand));

        ItemStack stack = player.getMainHandItem();
        if(stack.has(ModDataComponents.SPIRIT_RING_DATA)){
            if(!player.isShiftKeyDown()){
                player.openMenu(new SpatialRingMenuProvider(SpatialRingItemStackHandler.Type.INVENTORY));
            }else{
                //TODO open modifier menu
            }

            return InteractionResultHolder.success(stack);
        }
        return  super.use(level, player, usedHand);
    }
}
