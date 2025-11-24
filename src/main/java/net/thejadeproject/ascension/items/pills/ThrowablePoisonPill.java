package net.thejadeproject.ascension.items.pills;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.entity.PoisonPillProjectile;
import net.thejadeproject.ascension.entity.ModEntities;

public class ThrowablePoisonPill extends Item {
    private final MobEffectInstance effect;

    public ThrowablePoisonPill(Properties properties, MobEffectInstance effect) {
        super(properties);
        this.effect = effect;
    }

    public MobEffectInstance getEffect() {
        return effect;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            PoisonPillProjectile pillProjectile = new PoisonPillProjectile(
                    ModEntities.POISON_PILL.get(),
                    level,
                    player,
                    this.effect,
                    this // Pass the item itself
            );

            // The item is already set in the constructor, but we can set it again for safety
            pillProjectile.setItem(itemstack);
            pillProjectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(pillProjectile);
        }

        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}