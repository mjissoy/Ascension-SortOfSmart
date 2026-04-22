package net.thejadeproject.ascension.mob_ranks;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Random;

public final class MobRankRoller {
    private static final Random RANDOM = new Random();

    private static final List<WeightedMobRank> DEFAULT_WEIGHTS = List.of(
            new WeightedMobRank(MobRankList.get("mortal", 1), 45),
            new WeightedMobRank(MobRankList.get("mortal", 2), 30),
            new WeightedMobRank(MobRankList.get("mortal", 3), 17),
            new WeightedMobRank(MobRankList.get("qi_gathering", 1), 6),
            new WeightedMobRank(MobRankList.get("qi_gathering", 2), 2),
            new WeightedMobRank(MobRankList.get("qi_gathering", 3), 0),
            new WeightedMobRank(MobRankList.get("formation_establishment", 1), 0),
            new WeightedMobRank(MobRankList.get("formation_establishment", 2), 0),
            new WeightedMobRank(MobRankList.get("formation_establishment", 3), 0),
            new WeightedMobRank(MobRankList.get("golden_core", 1), 0),
            new WeightedMobRank(MobRankList.get("golden_core", 2), 0),
            new WeightedMobRank(MobRankList.get("golden_core", 3), 0)
    );

    private MobRankRoller() {
    }

    public static boolean canHaveRank(LivingEntity entity) {
        if (entity.level().isClientSide()) return false;
        if (entity instanceof Player) return false;
        return true;
    }

    public static MobRankDefinition rollRank(LivingEntity entity) {
        return rollFromWeights(DEFAULT_WEIGHTS);
    }

    private static MobRankDefinition rollFromWeights(List<WeightedMobRank> weights) {
        int totalWeight = 0;
        for (WeightedMobRank weighted : weights) {
            totalWeight += weighted.weight();
        }

        if (totalWeight <= 0) {
            return MobRankList.getFirst();
        }

        int roll = RANDOM.nextInt(totalWeight);
        int running = 0;

        for (WeightedMobRank weighted : weights) {
            running += weighted.weight();
            if (roll < running) {
                return weighted.definition();
            }
        }

        return MobRankList.getFirst();
    }
}