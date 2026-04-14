package net.thejadeproject.ascension.mob_ranks;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Random;

public final class MobRankRoller {
    private static final Random RANDOM = new Random();

    private static final List<WeightedMobRank> DEFAULT_WEIGHTS = List.of(
            new WeightedMobRank(MobRankList.MORTAL_1, 45),
            new WeightedMobRank(MobRankList.MORTAL_2, 30),
            new WeightedMobRank(MobRankList.MORTAL_3, 17),
            new WeightedMobRank(MobRankList.QI_GATHERING_1, 6),
            new WeightedMobRank(MobRankList.QI_GATHERING_2, 2),
            new WeightedMobRank(MobRankList.QI_GATHERING_3, 0)
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
            return MobRankList.MORTAL_1;
        }

        int roll = RANDOM.nextInt(totalWeight);
        int running = 0;

        for (WeightedMobRank weighted : weights) {
            running += weighted.weight();
            if (roll < running) {
                return weighted.definition();
            }
        }

        return MobRankList.MORTAL_1;
    }
}