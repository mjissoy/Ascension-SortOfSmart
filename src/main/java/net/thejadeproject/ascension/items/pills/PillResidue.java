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
            // Create the custom damage source
            DamageSource damageSource = new DamageSource(
                    level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageTypes.PILL_RESIDUE)
            );

            // Apply damage that bypasses totem protection
            // We'll use a combination of approaches to ensure the custom damage source is used

            // First, try to apply lethal damage with our custom source
            boolean wasKilled = player.hurt(damageSource, Float.MAX_VALUE);

            // If the player is still alive (due to totem), we need to bypass it
            if (player.isAlive()) {
                // Force death with our custom damage source
                // We'll use reflection or a different approach to set the last damage source
                try {
                    // Try to set the last damage source via reflection
                    var lastDamageSourceField = LivingEntity.class.getDeclaredField("lastDamageSource");
                    lastDamageSourceField.setAccessible(true);
                    lastDamageSourceField.set(player, damageSource);
                } catch (Exception e) {
                    // If reflection fails, fall back to another method
                }

                // Now kill the player - this should use our custom damage source
                player.setHealth(0);
                player.die(damageSource);
            }

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            if(consumer != null) consumer.accept(stack,level,livingEntity);
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }
}