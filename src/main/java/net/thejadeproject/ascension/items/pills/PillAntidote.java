package net.thejadeproject.ascension.items.pills;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

public class PillAntidote extends Item {
    private int cooldownTime = 50; // Default cooldown time
    private List<Holder<MobEffect>> effectsToRemove;

    public PillAntidote(Properties properties) {
        super(properties);
        // No default effects - must be specified via constructor or setter
        this.effectsToRemove = Arrays.asList();
    }

    // Constructor with custom cooldown
    public PillAntidote(Properties properties, int cooldownTime) {
        super(properties);
        this.cooldownTime = cooldownTime;
        this.effectsToRemove = Arrays.asList();
    }

    // Constructor with custom cooldown and effects
    public PillAntidote(Properties properties, int cooldownTime, Holder<MobEffect>... effectsToRemove) {
        super(properties);
        this.cooldownTime = cooldownTime;
        this.effectsToRemove = Arrays.asList(effectsToRemove);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide() && livingEntity instanceof Player player) {
            // Apply cooldown on server side
            player.getCooldowns().addCooldown(this, cooldownTime);

            // Remove all specified effects
            for (Holder<MobEffect> effect : effectsToRemove) {
                livingEntity.removeEffect(effect);
            }

            // Consume one item from the stack
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        // Call super to handle any food effects from properties
        return super.finishUsingItem(stack, level, livingEntity);
    }

    // Optional method to change cooldown time if needed
    public PillAntidote setCooldownTime(int cooldownTime) {
        this.cooldownTime = cooldownTime;
        return this;
    }

    // Method to set effects to remove
    public PillAntidote setEffectsToRemove(Holder<MobEffect>... effects) {
        this.effectsToRemove = Arrays.asList(effects);
        return this;
    }

    // Method to add additional effects to remove (without replacing existing ones)
    public PillAntidote addEffectsToRemove(Holder<MobEffect>... effects) {
        this.effectsToRemove.addAll(Arrays.asList(effects));
        return this;
    }
}