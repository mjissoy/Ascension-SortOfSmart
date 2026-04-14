package net.thejadeproject.ascension.mob_ranks;

public record MobRankStatProfile(
        double vitality,
        double strength,
        double agility
) {
    public MobRankStatProfile add(MobRankStatProfile other) {
        return new MobRankStatProfile(
                Math.max(0, this.vitality + other.vitality),
                Math.max(0, this.strength + other.strength),
                Math.max(0, this.agility + other.agility)
        );
    }
}