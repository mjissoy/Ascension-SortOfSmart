package net.thejadeproject.ascension.mob_ranks;

public record MobRankDefinition(
        String realmId,
        int stage,
        MobRankStatProfile baseStats
) {
}