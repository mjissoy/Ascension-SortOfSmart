package net.thejadeproject.ascension.mob_ranks;

import net.minecraft.world.entity.LivingEntity;

public final class MobRankResolver {
    private MobRankResolver() {
    }

    public static MobRankDefinition resolveDefinition(MobRankData data) {
        return MobRankList.get(data.getRealmId(), data.getStage());
    }

    public static MobBodyStatBias resolveBodyBias(LivingEntity entity) {
        return MobBiasList.getFor(entity);
    }

    public static MobRankStatProfile resolveFinalStats(LivingEntity entity, MobRankDefinition definition) {
        return definition.baseStats().add(resolveBodyBias(entity).asProfile());
    }

    public static MobRankStatProfile resolveFinalStats(LivingEntity entity, MobRankData data) {
        return resolveFinalStats(entity, resolveDefinition(data));
    }
}