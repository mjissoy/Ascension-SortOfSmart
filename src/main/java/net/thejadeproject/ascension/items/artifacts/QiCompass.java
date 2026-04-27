package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.blocks.custom.SpiritVeinBlock;

public class QiCompass extends CompassItem {
    
    public static final String VEIN_X = "vein_x";
    public static final String VEIN_Y = "vein_y";
    public static final String VEIN_Z = "vein_z";
    public static final String HAS_VEIN = "has_vein";
    
    public static final int SEARCH_RADIUS = 512;
    
    public QiCompass(Properties properties) {
        super(properties.stacksTo(1));
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clicked = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        
        if (player == null) return InteractionResult.PASS;
        
        if (level.getBlockState(clicked).getBlock() instanceof SpiritVeinBlock && isTracking(stack)) {
            BlockPos tracked = getTrackedPos(stack);
            if (tracked != null && tracked.equals(clicked)) {
                clearTrackedVein(stack);
                
                if (!level.isClientSide) {
                    level.playSound(null, player.blockPosition(), SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0f, 0.8f);
                    player.displayClientMessage(Component.translatable("item.ascension.qi_compass.cleared").withStyle(ChatFormatting.GRAY), true);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        if (level.isClientSide) {
            return InteractionResultHolder.pass(stack);
        }
        
        if (isTracking(stack)) {
            player.displayClientMessage(Component.translatable("item.ascension.qi_compass.tracking").withStyle(ChatFormatting.LIGHT_PURPLE), true);
            return InteractionResultHolder.success(stack);
        }

        ServerLevel serverLevel = (ServerLevel) level;
        BlockPos found = findNearestVein(serverLevel, player.blockPosition());
        
        if (found != null) {
            lockOnVein(stack, found, serverLevel);
            level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, Sound);
        }
    }
}
