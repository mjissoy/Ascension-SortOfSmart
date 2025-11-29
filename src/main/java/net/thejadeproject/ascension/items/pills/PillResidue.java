package net.thejadeproject.ascension.items.pills;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.util.ModDamageTypes;

public class PillResidue extends Item {
    public PillCooldownItem.onItemUse consumer;
    public PillResidue(Properties properties) {
        super(properties);
    }


    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide() && livingEntity instanceof Player player) {

            DamageSource damageSource = new DamageSource(
                    level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageTypes.PILL_RESIDUE)
            );
            //player.kill();
            player.hurt(damageSource, Float.MAX_VALUE);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            if(consumer != null) consumer.accept(stack,level,livingEntity);
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
