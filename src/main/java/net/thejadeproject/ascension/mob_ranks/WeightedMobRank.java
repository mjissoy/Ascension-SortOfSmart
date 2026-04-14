package net.thejadeproject.ascension.mob_ranks;

public record WeightedMobRank(
        MobRankDefinition definition,
        int weight
) {
    public WeightedMobRank {
        if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }
    }
}