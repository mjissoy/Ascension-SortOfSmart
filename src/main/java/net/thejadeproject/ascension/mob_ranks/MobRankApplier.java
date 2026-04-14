package net.thejadeproject.ascension.mob_ranks;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.thejadeproject.ascension.AscensionCraft;

public final class MobRankApplier {
    private static final ResourceLocation HEALTH_ID =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "mob_rank_health");
    private static final ResourceLocation DAMAGE_ID =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "mob_rank_damage");
    private static final ResourceLocation SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "mob_rank_speed");

    private MobRankApplier() {
    }

    public static void applyFromData(LivingEntity entity, MobRankData data) {
        MobRankDefinition definition = MobRankResolver.resolveDefinition(data);
        applyRank(entity, definition);
    }

    public static void applyRank(LivingEntity entity, MobRankDefinition definition) {
        MobRankStatProfile finalStats = MobRankResolver.resolveFinalStats(entity, definition);

        double healthBonus = AscensionStatConversions.maxHealthBonus(finalStats.vitality());
        double damageBonus = AscensionStatConversions.attackDamageBonus(finalStats.strength());
        double speedBonus = AscensionStatConversions.movementSpeedBonus(finalStats.strength(), finalStats.agility());

        applyAddValue(entity, Attributes.MAX_HEALTH, HEALTH_ID, healthBonus);
        applyAddValue(entity, Attributes.ATTACK_DAMAGE, DAMAGE_ID, damageBonus);
        applyAddValue(entity, Attributes.MOVEMENT_SPEED, SPEED_ID, speedBonus);

        entity.setHealth(entity.getMaxHealth());
    }

    private static void applyAddValue(LivingEntity entity,
                                      Holder<Attribute> attribute,
                                      ResourceLocation id,
                                      double amount) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance == null) return;

        instance.removeModifier(id);
        if (amount != 0) {
            instance.addPermanentModifier(new AttributeModifier(id, amount, AttributeModifier.Operation.ADD_VALUE));
        }
    }
}