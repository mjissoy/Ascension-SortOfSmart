package net.thejadeproject.ascension.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.thejadeproject.ascension.effects.ModEffects;

public class ModFoodProperties {
    public static final FoodProperties REGENERATION_PILL = new FoodProperties.Builder().alwaysEdible().fast()
            .effect(() -> new MobEffectInstance(ModEffects.QI_ENHANCED_REGENERATION, 200), 1f).build();

    public static final FoodProperties GOLDEN_SUN_LEAF = new FoodProperties.Builder().fast().alwaysEdible()
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 50), 1f).build();
    public static final FoodProperties IRONWOOD_SPROUT = new FoodProperties.Builder().fast().alwaysEdible()
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 50), 1f).build();
    public static final FoodProperties WHITE_JADE_ORCHID = new FoodProperties.Builder().fast().alwaysEdible()
            .effect(() -> new MobEffectInstance(ModEffects.CLEANSING, 50), 1f).build();

}
