package net.thejadeproject.ascension.items.pills;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
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

        // If player is sneaking, eat the pill instead of throwing it
        if (player.isShiftKeyDown()) { {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(itemstack);
            }
        }
        // If not sneaking, throw the pill
        else {
            if (!level.isClientSide) {
                PoisonPillProjectile pillProjectile = new PoisonPillProjectile(
                        ModEntities.POISON_PILL.get(),
                        level,
                        player,
                        this.effect,
                        this
                );

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

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 16; // Same as most food items
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        // Apply the effect to the eater when finished eating
        if (livingEntity instanceof Player player && !level.isClientSide && effect != null) {
            player.addEffect(new MobEffectInstance(effect));
        }

        // Handle normal food consumption (nutrition, saturation, etc.)
        return super.finishUsingItem(stack, level, livingEntity);
    }
}